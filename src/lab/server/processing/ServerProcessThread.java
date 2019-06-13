package lab.server.processing;

import lab.server.response.Logger;
import lab.server.response.ResponseBuilder;
import lab.util.CollectionElementsManager;
import lab.util.commands.Commands;
import lab.util.commands.DBCommand;
import lab.util.commands.Login;
import lab.util.commands.ServerCommand;
import lab.util.packet.PacketSettings;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.List;

import static lab.util.packet.PacketSettings.*;

/**
 * ServerProcessThread class is used for process requests from clients.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.6.0
 */
public class ServerProcessThread implements Runnable {

    private List<DatagramPacket> packetsParts;
    private CollectionElementsManager collectionManager;
    private DatagramSocket socket;

    /**
     * Constructor of ServerProcessThread class.
     *
     * @param packetsParts      list of DatagramPacket's.
     * @param collectionManager Manager of collection with which works commands.
     */
    public ServerProcessThread(List<DatagramPacket> packetsParts, CollectionElementsManager collectionManager) {
        this.packetsParts = packetsParts;
        this.collectionManager = collectionManager;
    }

    @Override
    public void run() {
        int commandCode = packetsParts.get(0).getData()[REQUIRED_CLIENT_METADATA_LENGTH] & 0xff;
        byte[] argument = unpackArgument(packetsParts);
        ServerCommand command = Commands.getServerCommand(commandCode, argument);

        Commands.CommandExecutionStatus status = null;

        Logger serverLogger = new ResponseBuilder();
        command.setLogger(serverLogger);

        ClientID clientID = ClientID.getClientID(packetsParts.get(0).getAddress(), packetsParts.get(0).getPort());
        InetSocketAddress inetAddress = new InetSocketAddress(clientID.getAddress(), clientID.getPort());

        command.setInetAddress(clientID.getAddress());
        command.setPort(clientID.getPort());

        String receivedToken;
        byte[] packedToken = new byte[PacketSettings.TOKEN_LENGTH];
        for (int i = 0; i < PacketSettings.TOKEN_LENGTH; i++) {
            packedToken[i] = packetsParts.get(0).getData()[i + 5];
        }
        receivedToken = new String(packedToken);
        Session session = null;
        if (command.needBeAuthorized() && (session = Session.isAuthorized(receivedToken, packetsParts.get(0).getAddress(), packetsParts.get(0).getPort())) == null) {
            status = Commands.CommandExecutionStatus.NOT_LOGGED_NOT_SUCCESSFUL;
            serverLogger.append("Not authorized");
        }
        else {
            if (session != null)
                command.setLogin(session.getLogin());

            if (command instanceof DBCommand) {
                collectionManager.connect((DBCommand) command);
            }

            status = command.execute();
            if (command instanceof DBCommand) {
                ((DBCommand) command).closeConnection();
            }
        }

        if (status == Commands.CommandExecutionStatus.AUTHORIZED){
            String token = Session.createSession(packetsParts.get(0).getAddress(), packetsParts.get(0).getPort(), ((Login)command).getLogin());
            serverLogger.append(token);
        }

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {

        }

        byte[] responseBuffer;
        if (status == Commands.CommandExecutionStatus.SERVER_SEND_DATA)
            responseBuffer = command.getPackedArgument();
        else
            responseBuffer = serverLogger.toString().getBytes();
        int countOfPackets = (responseBuffer.length + SINGLE_SERVER_METADATA) / (PACKET_LENGTH - REQUIRED_SERVER_METADATA_LENGTH) + (((responseBuffer.length + SINGLE_SERVER_METADATA) % (PACKET_LENGTH - REQUIRED_SERVER_METADATA_LENGTH) == 0 ? 0 : 1));
        byte[] buffer = new byte[PACKET_LENGTH];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetAddress);
        byte space = " ".getBytes()[0];
        if (countOfPackets > 65536) {
            buffer[0] = (byte) 1;
            buffer[1] = (byte) 0;
            for (int i = REQUIRED_SERVER_METADATA_LENGTH; i < PACKET_LENGTH; i++) {
                buffer[i] = space;
            }
            try {
                socket.send(packet);
            } catch (IOException e) {

            }
            clientID.finishProcessing();
            return;
        }
        for (int i = 0; i < countOfPackets; i++) {
            buffer[0] = ((byte) ((countOfPackets & 0xff00) >> 8));
            buffer[1] = ((byte) (countOfPackets & 0x00ff));
            buffer[2] = ((byte) ((i & 0xff00) >> 8));
            buffer[3] = ((byte) (i & 0x00ff));
            if (i == 0)
                buffer[4] = (byte) status.getCode();
            int bytesLeft = (responseBuffer.length + ((i == 0) ? 0 : SINGLE_SERVER_METADATA)) - i * (PACKET_LENGTH - REQUIRED_SERVER_METADATA_LENGTH);
            int offset = i * (PACKET_LENGTH - REQUIRED_SERVER_METADATA_LENGTH) - ((i == 0) ? 0 : SINGLE_SERVER_METADATA);
            if (bytesLeft >= (PACKET_LENGTH - REQUIRED_SERVER_METADATA_LENGTH)) {
                for (int j = 0; j < (PACKET_LENGTH - REQUIRED_SERVER_METADATA_LENGTH - ((i == 0) ? SINGLE_SERVER_METADATA : 0)); j++) {
                    buffer[j + REQUIRED_SERVER_METADATA_LENGTH + ((i == 0) ? SINGLE_SERVER_METADATA : 0)] = responseBuffer[offset + j];
                }
            } else {
                for (int j = 0; j < bytesLeft; j++) {
                    buffer[j + REQUIRED_SERVER_METADATA_LENGTH + ((i == 0) ? SINGLE_SERVER_METADATA : 0)] = responseBuffer[offset + j];
                }
                for (int j = bytesLeft; j < (PACKET_LENGTH - REQUIRED_SERVER_METADATA_LENGTH - ((i == 0) ? SINGLE_SERVER_METADATA : 0)); j++) {
                    buffer[j + REQUIRED_SERVER_METADATA_LENGTH + ((i == 0) ? SINGLE_SERVER_METADATA : 0)] = space;
                }
            }
            packet = new DatagramPacket(buffer, buffer.length, inetAddress);
            try {
                socket.send(packet);
                Thread.sleep(10);
            }
            catch (IOException e) {

            }
            catch (InterruptedException e) {

            }
        }

        clientID.finishProcessing();
    }

    private static byte[] unpackArgument(List<DatagramPacket> packetsParts) {
        byte[] argument = new byte[((packetsParts.get(0).getData()[0] & 0xff)*256 + (packetsParts.get(0).getData()[1] & 0xff)) * (PACKET_LENGTH - REQUIRED_CLIENT_METADATA_LENGTH) - SINGLE_CLIENT_METADATA];
        for (int i = 0; i < packetsParts.size(); i++) {
            byte[] currentPacketData = packetsParts.get(i).getData();
            for (int j = 0; j < PACKET_LENGTH - REQUIRED_CLIENT_METADATA_LENGTH - ((i == 0) ? SINGLE_CLIENT_METADATA : 0); j++) {
                argument[i * (PACKET_LENGTH - REQUIRED_CLIENT_METADATA_LENGTH) - ((i == 0) ? 0 : SINGLE_CLIENT_METADATA) + j] = currentPacketData[j + REQUIRED_CLIENT_METADATA_LENGTH + ((i == 0) ? SINGLE_CLIENT_METADATA : 0)];
            }
        }
        return argument;
    }

}