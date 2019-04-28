package lab.server.start;

import lab.util.CollectionElementsManager;
import static lab.util.packet.PacketSettings.*;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Main class of server side of application that controls processing of users data.
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.6.0
 */

public class RunServer {

    private static boolean working = true;
    private static DatagramSocket socket;
    private static int PORT;
    private static final String filePath = System.getenv("LAB_FIVE");

    private static HashMap<ClientID, ArrayList<DatagramPacket>> packetsParts = new HashMap<>();
    private static CollectionElementsManager collectionManager;

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

        SocketAddress address = new InetSocketAddress(PORT);
        try {
            socket = new DatagramSocket(null);
            socket.bind(address);
            socket.setSoTimeout(1000);
        }
        catch (SocketException e) {
            System.out.println("Порт занят, сервер не может быть запущен");
            return;
        }

        collectionManager = new CollectionElementsManager(filePath);

        while (working) {

            byte[] buffer = new byte[PACKET_LENGTH];

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            try {
                socket.receive(packet);
            }
            catch (SocketTimeoutException e){
                packetsParts.clear();
                ClientID.clearAll();
                continue;
            }
            catch (IOException e) {

            }

            ClientID clientID = ClientID.getClientID(packet.getAddress(), packet.getPort());

            if (!packetsParts.containsKey(clientID)){
                packetsParts.put(clientID, new ArrayList<>());
                clientID.clearOutsideStatement();
            }

            if (clientID.isProcessing())
                continue;

            int countOfPackets = packet.getData()[0] & 0xff;
            int currentPacketNumber = packet.getData()[1] & 0xff;

            while (packetsParts.get(clientID).size() < currentPacketNumber) {
                packetsParts.get(clientID).add(null);
            }
            if ((packetsParts.get(clientID).size() == currentPacketNumber)){
                packetsParts.get(clientID).add(packet);
                clientID.incReceivedPackets();
            }
            if (packetsParts.get(clientID).get(currentPacketNumber) == null) {
                packetsParts.get(clientID).set(currentPacketNumber, packet);
                clientID.incReceivedPackets();
            }

            if (countOfPackets == clientID.getReceivedPackets()) {
                compileRequest(clientID);

            }

        }

        socket.close();

    }

    private static void compileRequest(ClientID clientID){
        ServerProcessThread processRequest = new ServerProcessThread(packetsParts.remove(clientID), collectionManager);
        Thread processRequestThread = new Thread(processRequest);
        clientID.startProcessing();
        processRequestThread.start();
    }


}
