package lab.server.start;

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
        int commandCode = packetsParts.get(0).getData()[METADATA_LENGTH] & 0xff;
        Commands.CommandsList command = Commands.CommandsList.values()[commandCode - 1];
        byte[] argument = unpackArgument(packetsParts);
        ResponseBuilder response = new ResponseBuilder();
        CommandsExecutor.executeCommand(command, argument, collectionManager, response);
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {

        }
        InetSocketAddress inetAddress = new InetSocketAddress(packetsParts.get(0).getAddress(), packetsParts.get(0).getPort());
        byte[] responseBuffer = response.toString().getBytes();
        int countOfPackets = responseBuffer.length / (PACKET_LENGTH - METADATA_LENGTH) + ((responseBuffer.length % (PACKET_LENGTH - METADATA_LENGTH) == 0 ? 0 : 1));
        byte[] buffer = new byte[PACKET_LENGTH];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetAddress);
        byte space = " ".getBytes()[0];
        if (countOfPackets > 256){
            buffer[0] = (byte)1;
            buffer[1] = (byte)0;
            for (int i = METADATA_LENGTH; i < PACKET_LENGTH; i++){
                buffer[i] = space;
            }
            try {
                socket.send(packet);
            }
            catch (IOException e) {

            }
            ClientID.getClientID(inetAddress.getAddress(), inetAddress.getPort()).finishProcessing();
            return;
        }
        for (int i = 0; i < countOfPackets; i++){
            buffer[0] = (byte)countOfPackets;
            buffer[1] = (byte)i;
            if ((responseBuffer.length - i * (PACKET_LENGTH - METADATA_LENGTH) >= (PACKET_LENGTH - METADATA_LENGTH))){
                for (int j = 0; j < (PACKET_LENGTH - METADATA_LENGTH); j++){
                    buffer[j + METADATA_LENGTH] = responseBuffer[i * (PACKET_LENGTH - METADATA_LENGTH) + j];
                }
            }
            else {
                for (int j = 0; j < (responseBuffer.length - i * (PACKET_LENGTH - METADATA_LENGTH)); j++) {
                    buffer[j + METADATA_LENGTH] = responseBuffer[i * (PACKET_LENGTH - METADATA_LENGTH) + j];
                }
                for (int j = (responseBuffer.length - i * (PACKET_LENGTH - METADATA_LENGTH)); j < (PACKET_LENGTH - METADATA_LENGTH); j++){
                    buffer[j + METADATA_LENGTH] = space;
                }
            }
            packet = new DatagramPacket(buffer, buffer.length, inetAddress);
            try {
                socket.send(packet);
            } catch (IOException e) {

            }
        }

        ClientID.getClientID(inetAddress.getAddress(), inetAddress.getPort()).finishProcessing();
    }

    private static byte[] unpackArgument(ArrayList<DatagramPacket> packetsParts){
        byte[] argument = new byte[(packetsParts.get(0).getData()[0] & 0xff) * (PACKET_LENGTH - METADATA_LENGTH) - COMMAND_CODE_LENGTH];
        for (int i = 0; i < packetsParts.size(); i++){
            byte[] currentPacketData = packetsParts.get(i).getData();
            for (int j = 0; j < PACKET_LENGTH - METADATA_LENGTH - ((i == 0) ? COMMAND_CODE_LENGTH : 0); j++) {
                argument[i * (PACKET_LENGTH - METADATA_LENGTH) - ((i == 0) ? 0 : COMMAND_CODE_LENGTH) + j] = currentPacketData[j + METADATA_LENGTH + ((i == 0) ? COMMAND_CODE_LENGTH : 0)];
            }
        }
        return argument;
    }

}
