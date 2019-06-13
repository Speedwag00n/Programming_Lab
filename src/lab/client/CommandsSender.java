package lab.client;

import lab.client.settings.Settings;
import lab.util.commands.Commands;
import lab.util.commands.ServerCommand;
import lab.util.packet.PacketOverflowException;
import lab.util.packet.PacketSettings;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Arrays;

import static lab.util.packet.PacketSettings.*;

/**
 * This class allows to send commands to server and receive answer from it.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.6.0
 */
public class CommandsSender {

    private static final String ADDRESS = "localhost";
    private static int port;
    private static InetSocketAddress inetAddress;
    private static DatagramChannel channel = null;
    private static int countOfAttempts = 3;
    private static int receivingTime = 3000;

    private CommandsSender(){
        port = Settings.getServerPort();
        try {
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
        } catch (IOException e) {

        }
        inetAddress = new InetSocketAddress(ADDRESS, port);
    }

    private static CommandsSender singleCommandSender;

    /**
     * Method that allows to receive instance of CommandsSender class.
     *
     * @return instance of CommandsSender class.
     */
    public static CommandsSender getInstance(){
        if (singleCommandSender == null || Settings.getToken() == null)
            singleCommandSender = new CommandsSender();
        return singleCommandSender;
    }

    /**
     * Sends commands to server and receives status with data.
     *
     * @param command command that needs to be execute on server.
     * @return status of command execution on server.
     */
    public ServerAnswer trySendRequest(ServerCommand command) {
        for (int i = 0; i < countOfAttempts; i++) {
            ServerAnswer serverAnswer;
            try {
                sendRequest((command));
            } catch (PacketOverflowException e) {
                serverAnswer = new ServerAnswer();
                serverAnswer.setStatus(Commands.CommandExecutionStatus.CLIENT_CANT_SEND);
                return serverAnswer;
            }
            if ((serverAnswer = receiveResponse()) == null)
                continue;
            else
                return serverAnswer;
        }
        ServerAnswer serverAnswer = new ServerAnswer();
        serverAnswer.setAnswer("Похоже, что сервер временно недоступен, попробуйте отправить запрос чуть позже.");
        serverAnswer.setStatus(Commands.CommandExecutionStatus.NO_RESPONSE);
        return serverAnswer;
    }

    private void sendRequest(ServerCommand command) throws PacketOverflowException {
        ByteBuffer commandArgs = ByteBuffer.wrap(command.getPackedData());
        int countOfPackets = (commandArgs.limit() + SINGLE_CLIENT_METADATA) / (PACKET_LENGTH - REQUIRED_CLIENT_METADATA_LENGTH) + ((((commandArgs.limit() + SINGLE_CLIENT_METADATA) % (PACKET_LENGTH - REQUIRED_CLIENT_METADATA_LENGTH)) == 0) ? 0 : 1);
        if (countOfPackets > 65536) {
            throw new PacketOverflowException("Can't send request because there're too many packets for this request");
        }
        ByteBuffer buffer = ByteBuffer.allocate(PACKET_LENGTH);
        for (int i = 0; i < countOfPackets; i++) {
            buffer.clear();
            buffer.put((byte) ((countOfPackets & 0xff00) >> 8));
            buffer.put((byte) (countOfPackets & 0x00ff));
            buffer.put((byte) ((i & 0xff00) >> 8));
            buffer.put((byte) (i & 0x00ff));
            int offset = i * (PACKET_LENGTH - REQUIRED_CLIENT_METADATA_LENGTH) - ((i == 0) ? 0 : SINGLE_CLIENT_METADATA);
            if (i == 0) {
                buffer.put((byte) command.getCode());
                if (Settings.getToken() != null) {
                    byte[] token = Settings.getToken().getBytes();
                    for (int j = 0; j < PacketSettings.TOKEN_LENGTH; j++){
                        buffer.put(token[j]);
                    }
                }
                else {
                    byte zero = "0".getBytes()[0];
                    for (int j = 0; j < PacketSettings.TOKEN_LENGTH; j++){
                        buffer.put(zero);
                    }
                }
            }
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

    private ServerAnswer receiveResponse() {
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
                startTime = System.currentTimeMillis();
                receivedPackets++;
                int currentPacketNumber = (buffer.get(2) & 0xff) * 256 + (buffer.get(3) & 0xff);
                if (receivedPackets == 1) {
                    countOfPackets = (buffer.get(0) & 0xff) * 256 + (buffer.get(1) & 0xff);
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
            ServerAnswer serverAnswer = new ServerAnswer();
            serverAnswer.setStatus(Commands.CommandExecutionStatus.getStatus(packetsParts.get(0).get(4) & 0xff));
            if (serverAnswer.getStatus() == Commands.CommandExecutionStatus.SERVER_SEND_DATA)
                serverAnswer.setPackedData(response.array());
            else
                serverAnswer.setAnswer(new String(response.array()).trim());
            return serverAnswer;
        } else
            return null;
    }

}
