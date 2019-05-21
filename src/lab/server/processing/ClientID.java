package lab.server.processing;

import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * ClientID encapsulate IP address and port of client to use it for identification.
 * Object are received by invocation of fabrication method that returns already existing object or creates new one.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.6.0
 */
public class ClientID {

    private InetAddress address;
    private int port;
    private int receivedPackets;
    private boolean isProcessing;
    private static Map<ClientID, WeakReference<ClientID>> ids = Collections.synchronizedMap(new WeakHashMap<>());

    private ClientID(InetAddress aAddress, int aPort) {

        address = aAddress;
        port = aPort;

    }

    /**
     * Factory method which returns object of ClientID.
     *
     * @param address address of client.
     * @param port    port of client.
     * @return ClientID object.
     */
    public static ClientID getClientID(InetAddress address, int port) {

        ClientID clientID = new ClientID(address, port);
        if (!ids.containsKey(clientID)) {
            ids.put(clientID, new WeakReference<>(clientID));
            return clientID;
        }

        return ids.get(clientID).get();

    }

    /**
     * This method gets address of client.
     *
     * @return address of client.
     */
    public InetAddress getAddress() {
        return address;
    }

    /**
     * This method gets port of client.
     *
     * @return port of client.
     */
    public int getPort() {
        return port;
    }

    /**
     * This method gets count of received packets from this client during session.
     *
     * @return counts of received packets from this client during session.
     */
    public int getReceivedPackets() {
        return receivedPackets;
    }

    /**
     * Increment count of received packets from this client during session.
     */
    public void incReceivedPackets() {
        receivedPackets++;
    }

    /**
     * This method allows to find out that has this client processing request or not.
     *
     * @return true if this client has processing request else false.
     */
    public boolean isProcessing() {
        return isProcessing;
    }

    /**
     * Marks current client that it has processing request.
     */
    public void startProcessing() {
        isProcessing = true;
    }

    /**
     * Marks current client that it hasn't had processing request yet.
     */
    public void finishProcessing() {
        isProcessing = false;
    }

    /**
     * Removes existing clients that haven't processing requests.
     */
    public static void clearAll() {
        for (ClientID clientID : ids.keySet()) {
            if (!clientID.isProcessing)
                ids.remove(clientID);
        }
    }

    /**
     * Clears count of received packets from this client.
     *
     * @return current ClientID.
     */
    public ClientID clearOutsideStatement() {
        receivedPackets = 0;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ClientID object = (ClientID) obj;
        if (!address.equals(object.address))
            return false;
        if (port != object.port)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Клиент с IP " + address + " и портом " + port;
    }

    @Override
    public int hashCode() {
        final int prime = 17;
        int result = 1;
        result = result * prime + ((address == null) ? 0 : address.hashCode());
        result = result * prime + port;
        return result;
    }

}
