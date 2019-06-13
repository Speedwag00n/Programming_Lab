package lab.client;

import lab.client.settings.Settings;
import lab.server.database.changes.DatabaseChange;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;

import static lab.util.packet.PacketSettings.*;

public class ServerChangeListener implements Runnable {

    private DatagramChannel channel;
    private boolean working = true;
    private static int receivingTime = 3000;
    private static int port;

    private ServerChangeListener() {
    }

    private static ServerChangeListener listener;
    private static Thread thread;

    public static Thread getThreadInstance() {
        return thread;
    }

    public static ServerChangeListener getInstance(){
        return listener;
    }

    public int getPort() {
        return port;
    }

    @Override
    public void run() {
        main: while (working) {
            if (thread.isInterrupted()){
                working = false;
                try {
                    channel.close();
                }
                catch (IOException e) {

                }
            }
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
                ByteArrayInputStream bais = new ByteArrayInputStream(response.array());
                try {
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    DatabaseChange change = (DatabaseChange) ois.readObject();
                    if (!change.getInitiator().equals(Settings.getUsername())) {
                        LocationsContainer.getInstance().addChange(change);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                continue main;
            }
        }
    }

    public static void initialize() {
        listener = new ServerChangeListener();
        thread = new Thread(listener);
        thread.setDaemon(true);
    }

    public void connect(){
        for (int i = 1000; i < 65536; i++) {
            try {
                channel = DatagramChannel.open();
                channel.bind(new InetSocketAddress(i));
                channel.configureBlocking(false);
                port = i;
                break;
            } catch (IOException e) {

            }
        }
    }

    public static void stop() {
        thread.interrupt();
    }

}
