package lab.server.start;

import com.sun.security.ntlm.Client;
import lab.util.CollectionElementsManager;
import lab.util.CommandsExecutor;
import lab.util.commands.Commands;

import static lab.util.packet.PacketSettings.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class ServerProcessThread implements Runnable {

    private ArrayList<DatagramPacket> packetsParts;
    private CollectionElementsManager collectionManager;
    private DatagramSocket socket;

    public ServerProcessThread(ArrayList<DatagramPacket> aPacketsParts, CollectionElementsManager aCollectionManager){
        packetsParts = aPacketsParts;
        collectionManager = aCollectionManager;
    }

    @Override
    public void run() {
        int commandCode = packetsParts.get(0).getData()[REQUIRED_CLIENT_METADATA_LENGTH] & 0xff;
        Commands.CommandsList command = Commands.CommandsList.getCommand(commandCode);
        byte[] argument = unpackArgument(packetsParts);
        ResponseBuilder response = new ResponseBuilder();
        ClientID clientID = ClientID.getClientID(packetsParts.get(0).getAddress(), packetsParts.get(0).getPort());
        InetSocketAddress inetAddress = new InetSocketAddress(clientID.getAddress(), clientID.getPort());
        Commands.CommandExecutionStatus status = CommandsExecutor.executeCommand(command, argument, collectionManager, clientID, response);
        if (status == Commands.CommandExecutionStatus.MADE_LOGGED)
            clientID.successfulLogged();
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {

        }
        byte[] responseBuffer = response.toString().getBytes();
        int countOfPackets = (responseBuffer.length + COMMAND_EXECUTION_CODE_LENGTH) / (PACKET_LENGTH - REQUIRED_SERVER_METADATA_LENGTH) + (((responseBuffer.length + COMMAND_EXECUTION_CODE_LENGTH) % (PACKET_LENGTH - REQUIRED_SERVER_METADATA_LENGTH) == 0 ? 0 : 1));
        byte[] buffer = new byte[PACKET_LENGTH];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetAddress);
        byte space = " ".getBytes()[0];
        if (countOfPackets > 256){
            buffer[0] = (byte)1;
            buffer[1] = (byte)0;
            for (int i = REQUIRED_SERVER_METADATA_LENGTH; i < PACKET_LENGTH; i++){
                buffer[i] = space;
            }
            try {
                socket.send(packet);
            }
            catch (IOException e) {

            }
            clientID.finishProcessing();
            return;
        }
        for (int i = 0; i < countOfPackets; i++){
            buffer[0] = (byte)countOfPackets;
            buffer[1] = (byte)i;
            if (i == 0)
                buffer[2] = (byte)status.getCode();
            int bytesLeft = (responseBuffer.length + ((i == 0) ? 0 : COMMAND_EXECUTION_CODE_LENGTH)) - i * (PACKET_LENGTH - REQUIRED_SERVER_METADATA_LENGTH);
            int offset = i * (PACKET_LENGTH - REQUIRED_SERVER_METADATA_LENGTH) - ((i == 0) ? 0 : COMMAND_EXECUTION_CODE_LENGTH);
            if (bytesLeft >= (PACKET_LENGTH - REQUIRED_SERVER_METADATA_LENGTH)){
                for (int j = 0; j < (PACKET_LENGTH - REQUIRED_SERVER_METADATA_LENGTH - ((i == 0) ? COMMAND_EXECUTION_CODE_LENGTH : 0)); j++){
                    buffer[j + REQUIRED_SERVER_METADATA_LENGTH + ((i == 0) ? COMMAND_EXECUTION_CODE_LENGTH : 0)] = responseBuffer[offset + j];
                }
            }
            else {
                for (int j = 0; j < bytesLeft; j++) {
                    buffer[j + REQUIRED_SERVER_METADATA_LENGTH + ((i == 0) ? COMMAND_EXECUTION_CODE_LENGTH : 0)] = responseBuffer[offset + j];
                }
                for (int j = bytesLeft; j < (PACKET_LENGTH - REQUIRED_SERVER_METADATA_LENGTH - ((i == 0) ? COMMAND_EXECUTION_CODE_LENGTH : 0)); j++){
                    buffer[j + REQUIRED_SERVER_METADATA_LENGTH + ((i == 0) ? COMMAND_EXECUTION_CODE_LENGTH : 0)] = space;
                }
            }
            packet = new DatagramPacket(buffer, buffer.length, inetAddress);
            try {
                socket.send(packet);
            } catch (IOException e) {

            }
        }

        clientID.finishProcessing();
    }

    private static byte[] unpackArgument(ArrayList<DatagramPacket> packetsParts){
        byte[] argument = new byte[(packetsParts.get(0).getData()[0] & 0xff) * (PACKET_LENGTH - REQUIRED_CLIENT_METADATA_LENGTH) - COMMAND_CODE_LENGTH];
        for (int i = 0; i < packetsParts.size(); i++){
            byte[] currentPacketData = packetsParts.get(i).getData();
            for (int j = 0; j < PACKET_LENGTH - REQUIRED_CLIENT_METADATA_LENGTH - ((i == 0) ? COMMAND_CODE_LENGTH : 0); j++) {
                argument[i * (PACKET_LENGTH - REQUIRED_CLIENT_METADATA_LENGTH) - ((i == 0) ? 0 : COMMAND_CODE_LENGTH) + j] = currentPacketData[j + REQUIRED_CLIENT_METADATA_LENGTH + ((i == 0) ? COMMAND_CODE_LENGTH : 0)];
            }
        }
        return argument;
    }

}
