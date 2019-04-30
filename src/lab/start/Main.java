package lab.start;

import lab.util.CommandsExecutor;
import lab.util.commands.Commands;
import lab.util.packet.PacketOverflowException;

import static lab.util.packet.PacketSettings.*;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    private static boolean working = true;
    private static final String ADDRESS = "localhost";
    private static int PORT;
    private static InetSocketAddress inetAddress;
    private static DatagramChannel channel = null;
    private static int countOfAttempts = 5;
    private static int receivingTime = 500;

    public static void main(String[] args) {

        try {
            int port;
            if ((args.length > 0) && ((port = Integer.parseInt(args[0])) >= 0) && (port < 65536)) {
                PORT = port;
            }
            else {
                System.out.println("Введите корректный порт.");
                System.exit(0);
            }
        }
        catch (NumberFormatException e){
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

        main: while(working) {

            String commandLine = null;

            System.out.println("Введите команду");

            try {
                commandLine = commandsScanner.nextLine();

            }
            catch(NoSuchElementException ex) {
                System.out.println("Завершение работы приложения");
                try {
                    channel.close();
                }
                catch (IOException e) {

                }
                working = false;
                continue;
            }
            String[] commandParts = CommandsExecutor.parseCommand(commandLine);
            if (commandParts[0].equals("exit")) {
                System.out.println("Завершение работы приложения");
                working = false;
                continue;
            }
            Commands.CommandsList command = Commands.getCommand(commandParts[0]);
            if (command == null){
                System.out.println("Команда не найдена. Воспользуйтесь командой \"help help\" для получения подробного описания доступных команд и их аргументах.");
                continue;
            }
            if (!Commands.isCommandValid(command, commandParts.length > 1)){
                System.out.println("У команды отсутствует аргумент. Воспользуйтесь командой \"help help\" для получения подробного описания доступных команд и их аргументах.");
                continue;
            }
            byte[] packedCommandArgs = Commands.packArgument(command, ((commandParts.length > 1) ? commandParts[1] : null));
            for (int i = 0; i < countOfAttempts; i++) {

                try {
                    if(!sendRequest(command, packedCommandArgs))
                        continue main;
                }
                catch (PacketOverflowException e) {
                    System.out.println("Запрос превышает допустимый размер и не может быть отправлен.");
                    continue main;
                }

                try {
                    Thread.sleep(900);
                } catch (InterruptedException e) {

                }

                if (!receiveResponse()) {
                    continue;
                }
                else
                    continue main;
            }

            System.out.println("Похоже, что сервер временно недоступен, попробуйте отправить запрос чуть позже");

        }

    }

    private static boolean sendRequest(Commands.CommandsList command, byte[] packedCommandArgs) throws PacketOverflowException {

        if (packedCommandArgs == null)
            return false;
        ByteBuffer commandArgs = ByteBuffer.wrap(packedCommandArgs);
        int countOfPackets = (commandArgs.limit() + COMMAND_CODE_LENGTH) / (PACKET_LENGTH - METADATA_LENGTH) + ((((commandArgs.limit() + COMMAND_CODE_LENGTH) % (PACKET_LENGTH - METADATA_LENGTH)) == 0) ? 0 : 1);
        if (countOfPackets > 256){
            throw new PacketOverflowException("Can't send request because there're too many packets for this request");
        }
        ByteBuffer buffer = ByteBuffer.allocate(PACKET_LENGTH);
        for (int i = 0; i < countOfPackets; i++){
            buffer.clear();
            buffer.put((byte)countOfPackets);
            buffer.put((byte)i);
            if (i == 0)
                buffer.put((byte)command.getNumber());
            if (((commandArgs.limit() - (i*(PACKET_LENGTH - METADATA_LENGTH) - ((i == 0) ? 0 : COMMAND_CODE_LENGTH))) >= (PACKET_LENGTH - METADATA_LENGTH - ((i == 0) ? COMMAND_CODE_LENGTH : 0))))
                buffer.put(commandArgs.array(), i*(PACKET_LENGTH - METADATA_LENGTH) - ((i == 0) ? 0 : COMMAND_CODE_LENGTH), PACKET_LENGTH - METADATA_LENGTH - ((i == 0) ? COMMAND_CODE_LENGTH : 0));
            else {
                buffer.put(commandArgs.array(), i*(PACKET_LENGTH - METADATA_LENGTH) - ((i == 0) ? 0 : COMMAND_CODE_LENGTH), (commandArgs.limit() - (i*(PACKET_LENGTH - METADATA_LENGTH) - ((i == 0) ? 0 : COMMAND_CODE_LENGTH))));
                byte[] spaces = new byte[(PACKET_LENGTH - METADATA_LENGTH - ((i == 0) ? COMMAND_CODE_LENGTH : 0)) - (commandArgs.limit() - (i*(PACKET_LENGTH - METADATA_LENGTH) - ((i == 0) ? 0 : COMMAND_CODE_LENGTH)))];
                Arrays.fill(spaces, " ".getBytes()[0]);
                buffer.put(spaces);
            }
            buffer.flip();
            try {
                channel.send(buffer, inetAddress);
            } catch (IOException e) {

            }
        }
        return true;
    }

    private static boolean receiveResponse(){
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
                if (receivedPackets == 1){
                    countOfPackets = buffer.get(0) & 0xff;
                }
                while (packetsParts.size() < currentPacketNumber) {
                    packetsParts.add(null);
                }
                if ((packetsParts.size() == currentPacketNumber)){
                    packetsParts.add(buffer);
                }
                if (packetsParts.get(currentPacketNumber) == null) {
                    packetsParts.set(currentPacketNumber, buffer);
                }
            }
            catch (IOException e) {

            }
        }
        if (countOfPackets == receivedPackets){
            response = ByteBuffer.allocate(countOfPackets * (PACKET_LENGTH - METADATA_LENGTH));
            for (ByteBuffer buf : packetsParts){
                buf.flip();
                response.put(buf.array(), METADATA_LENGTH, (PACKET_LENGTH - METADATA_LENGTH));
            }
            System.out.println(new String(response.array()).trim());
            return true;
        }
        else
            return false;
    }

}
