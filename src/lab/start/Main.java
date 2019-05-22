package lab.start;

import lab.server.response.Logger;
import lab.server.response.ResponseBuilder;
import lab.util.commands.*;
import lab.util.packet.PacketOverflowException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static lab.util.commands.Commands.CommandExecutionStatus;
import static lab.util.packet.PacketSettings.*;

/**
 * Main class of client side of application that reads commands from command line, parses them and sends server command to server side.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.6.0
 */
public class Main {

    private static boolean working = true;
    private static final String ADDRESS = "localhost";
    private static int PORT;
    private static InetSocketAddress inetAddress;
    private static DatagramChannel channel = null;
    private static int countOfAttempts = 5;
    private static int receivingTime = 3000;

    public static void main(String[] args) {

        try {
            int port;
            if ((args.length > 0) && ((port = Integer.parseInt(args[0])) >= 0) && (port < 65536)) {
                PORT = port;
            } else {
                System.out.println("Введите корректный порт.");
                System.exit(0);
            }
        } catch (NumberFormatException e) {
            System.out.println("Введите корректный порт.");
            System.exit(0);
        }

        Scanner commandsScanner = new Scanner(System.in);

        try {
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
        } catch (IOException e) {

        }

        inetAddress = new InetSocketAddress(ADDRESS, PORT);

        StringBuilder commandBuffer = new StringBuilder();

        main:
        while (working) {

            String commandLine;

            if (!commandBuffer.toString().contains(";"))
                System.out.println("Введите команду.");

            while(!commandBuffer.toString().contains(";")) {
                try {
                    commandLine = commandsScanner.nextLine();
                } catch (NoSuchElementException ex) {
                    System.out.println("Завершение работы приложения.");
                    try {
                        channel.close();
                    } catch (IOException e) {

                    }
                    working = false;
                    continue;
                }
                if (commandLine.trim().length() != 0)
                    commandBuffer.append(commandLine.replace("\n", ""));
            }

            String[] commands = commandBuffer.toString().split(";", 2);
            String commandString = commands[0];
            commandBuffer.setLength(0);
            commandBuffer.append(commands[1]);
            Command command;
            try {
                command = Commands.getCommand(commandString);
            } catch (InvalidArgumentsException e) {
                System.out.println(e.getMessage());
                continue;
            }
            if (command == null) {
                System.out.println("Команда не найдена. Воспользуйтесь командой \"help\" для получения подробного описания доступных команд и их аргументах.");
                continue;
            }
            Logger clientLogger = new ResponseBuilder();
            command.setLogger(clientLogger);
            if (command instanceof ClientCommand) {
                command.execute();
                System.out.println(clientLogger.toString());
            } else if (command instanceof ServerCommand) {
                CommandExecutionStatus status = trySendRequest((ServerCommand) command);
                if (status == CommandExecutionStatus.CLIENT_CANT_SEND)
                    System.out.println("Запрос превышает допустимый размер и не может быть отправлен.");
                else if (status == CommandExecutionStatus.NO_RESPONSE)
                    System.out.println("Похоже, что сервер временно недоступен, попробуйте отправить запрос чуть позже");
            }
        }

    }

    private static CommandExecutionStatus trySendRequest(ServerCommand command) {
        for (int i = 0; i < countOfAttempts; i++) {
            CommandExecutionStatus status;
            try {
                sendRequest((command));
            } catch (PacketOverflowException e) {
                return CommandExecutionStatus.CLIENT_CANT_SEND;
            }
            if ((status = receiveResponse()) == null)
                continue;
            else
                return status;
        }
        return CommandExecutionStatus.NO_RESPONSE;
    }

    private static void sendRequest(ServerCommand command) throws PacketOverflowException {

        ByteBuffer commandArgs = ByteBuffer.wrap(command.getPackedData());
        int countOfPackets = (commandArgs.limit() + SINGLE_CLIENT_METADATA) / (PACKET_LENGTH - REQUIRED_CLIENT_METADATA_LENGTH) + ((((commandArgs.limit() + SINGLE_CLIENT_METADATA) % (PACKET_LENGTH - REQUIRED_CLIENT_METADATA_LENGTH)) == 0) ? 0 : 1);
        if (countOfPackets > 256) {
            throw new PacketOverflowException("Can't send request because there're too many packets for this request");
        }
        ByteBuffer buffer = ByteBuffer.allocate(PACKET_LENGTH);
        for (int i = 0; i < countOfPackets; i++) {
            buffer.clear();
            buffer.put((byte) countOfPackets);
            buffer.put((byte) i);
            int offset = i * (PACKET_LENGTH - REQUIRED_CLIENT_METADATA_LENGTH) - ((i == 0) ? 0 : SINGLE_CLIENT_METADATA);
            if (i == 0)
                buffer.put((byte) command.getCode());
            if (((commandArgs.limit() - offset) >= (PACKET_LENGTH - REQUIRED_CLIENT_METADATA_LENGTH - ((i == 0) ? SINGLE_CLIENT_METADATA : 0))))
                buffer.put(commandArgs.array(), offset, PACKET_LENGTH - REQUIRED_CLIENT_METADATA_LENGTH - ((i == 0) ? SINGLE_CLIENT_METADATA : 0));
            else {
                buffer.put(commandArgs.array(), offset, (commandArgs.limit() - offset));
                byte[] spaces = new byte[(PACKET_LENGTH - REQUIRED_CLIENT_METADATA_LENGTH - ((i == 0) ? SINGLE_CLIENT_METADATA : 0)) - (commandArgs.limit() - offset)];
                Arrays.fill(spaces, " ".getBytes()[0]);
                buffer.put(spaces);
            }
            buffer.flip();
            try {
                channel.send(buffer, inetAddress);
            } catch (IOException e) {

            }
        }
    }

    private static CommandExecutionStatus receiveResponse() {
        ArrayList<ByteBuffer> packetsParts = new ArrayList<>();
        int countOfPackets = -1;
        int receivedPackets = 0;
        ByteBuffer response;
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < receivingTime) {
            ByteBuffer buffer = ByteBuffer.allocate(PACKET_LENGTH);
            try {
                channel.receive(buffer);
                if (buffer.position() == 0)
                    continue;
                receivedPackets++;
                int currentPacketNumber = buffer.get(1) & 0xff;
                if (receivedPackets == 1) {
                    countOfPackets = buffer.get(0) & 0xff;
                }
                while (packetsParts.size() < currentPacketNumber) {
                    packetsParts.add(null);
                }
                if ((packetsParts.size() == currentPacketNumber)) {
                    packetsParts.add(buffer);
                }
                if (packetsParts.get(currentPacketNumber) == null) {
                    packetsParts.set(currentPacketNumber, buffer);
                }
                if (countOfPackets == receivedPackets){
                    break;
                }
            } catch (IOException e) {

            }
        }
        if (countOfPackets == receivedPackets) {
            response = ByteBuffer.allocate(countOfPackets * (PACKET_LENGTH - REQUIRED_SERVER_METADATA_LENGTH) - SINGLE_CLIENT_METADATA);
            for (int i = 0; i < packetsParts.size(); i++) {
                ByteBuffer buf = packetsParts.get(i);
                buf.flip();
                response.put(buf.array(), REQUIRED_SERVER_METADATA_LENGTH + ((i == 0) ? SINGLE_SERVER_METADATA : 0), (PACKET_LENGTH - REQUIRED_SERVER_METADATA_LENGTH - ((i == 0) ? SINGLE_SERVER_METADATA : 0)));
            }
            System.out.println(new String(response.array()).trim());
            return CommandExecutionStatus.getStatus(packetsParts.get(0).get(2) & 0xff);
        } else
            return null;
    }

}
