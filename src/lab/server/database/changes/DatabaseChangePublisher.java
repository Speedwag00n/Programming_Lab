package lab.server.database.changes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static lab.util.packet.PacketSettings.*;

public class DatabaseChangePublisher implements Runnable {

    private DatagramSocket socket;
    private int port;
    private boolean working = true;
    private Object obj = new Object();

    private ConcurrentHashMap<Subscriber, Object> subscribers = new ConcurrentHashMap<>();

    private ConcurrentLinkedQueue<DatabaseChange> changes = new ConcurrentLinkedQueue<>();

    public DatabaseChangePublisher() {

    }

    @Override
    public void run() {
        while (working) {
            DatabaseChange currentChange = changes.poll();
            if (currentChange != null) {
                byte[] responseBuffer = null;
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(currentChange);
                    responseBuffer = baos.toByteArray();
                } catch (IOException e) {

                }
                for (Subscriber subscriber : subscribers.keySet()) {
                    int countOfPackets = (responseBuffer.length + SINGLE_SERVER_METADATA) / (PACKET_LENGTH - REQUIRED_SERVER_METADATA_LENGTH) + (((responseBuffer.length + SINGLE_SERVER_METADATA) % (PACKET_LENGTH - REQUIRED_SERVER_METADATA_LENGTH) == 0 ? 0 : 1));
                    byte[] buffer = new byte[PACKET_LENGTH];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, subscriber.getInetAddress(), subscriber.getPort());
                    byte space = " ".getBytes()[0];
                    for (int i = 0; i < countOfPackets; i++) {
                        buffer[0] = ((byte) ((countOfPackets & 0xff00) >> 8));
                        buffer[1] = ((byte) (countOfPackets & 0x00ff));
                        buffer[2] = ((byte) ((i & 0xff00) >> 8));
                        buffer[3] = ((byte) (i & 0x00ff));
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
                        packet = new DatagramPacket(buffer, buffer.length, subscriber.getInetAddress(), subscriber.getPort());
                        try {
                            socket.send(packet);
                            Thread.sleep(10);
                        }
                        catch (IOException e) {

                        }
                        catch (InterruptedException e) {

                        }
                    }
                }
            }
        }
    }

    public void connect() throws SocketException {
        socket = new DatagramSocket();
        port = socket.getPort();
    }

    public void subscribe(InetAddress inetAddress, int port){
        subscribers.put(new Subscriber(inetAddress, port), obj);
    }

    public int getPort(){
        return port;
    }

    public void addChange(DatabaseChange change){
        changes.add(change);
    }

}
