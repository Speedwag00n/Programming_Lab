package lab.server.start;

import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.WeakHashMap;

/**
 * ClientID encapsulate IP address and port of client to use it for identification.
 * Object are received by invocation of fabrication method that returns already existing object or creates new one.
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.6.0
 */
public class ClientID {

    private InetAddress address;
    private int port;
    private int receivedPackets;
    private boolean isProcessing;
    private boolean isLogged;
    private static WeakHashMap<ClientID, WeakReference<ClientID>> ids = new WeakHashMap<>();
    private static HashSet<ClientID> authorizedClients = new HashSet<>();

    private ClientID(InetAddress aAddress, int aPort){

        address = aAddress;
        port = aPort;

    }

    /**
     * Fabrication method to return object of ClientID.
     * @param address Address of client.
     * @param port Port of client.
     * @return Returns ClientID object.
     */
    public static ClientID getClientID(InetAddress address, int port){

        ClientID clientID = new ClientID(address, port);
        if (!ids.containsKey(clientID)) {
            ids.put(clientID, new WeakReference<>(clientID));
            return clientID;
        }

        return ids.get(clientID).get();

    }

    /**
     * This method gets address of client.
     * @return Returns address of client.
     */
    public InetAddress getAddress() {
        return address;
    }

    /**
     * This method gets port of client.
     * @return Returns port of client.
     */
    public int getPort() {
        return port;
    }

    /**
     * This method gets count of received packets from this client during session.
     * @return Returns counts of received packets from this client during session.
     */
    public int getReceivedPackets(){
        return receivedPackets;
    }

    /**
     * Increment count of received packets from this client during session.
     */
    public void incReceivedPackets(){
        receivedPackets++;
    }

    public boolean isProcessing(){
        return isProcessing;
    }

    public void successfulLogged(){
        authorizedClients.add(this);
        isLogged = true;
    }

    public boolean isLogged(){
        return isLogged;
    }

    public void startProcessing(){
        isProcessing = true;
    }

    public void finishProcessing(){
        isProcessing = false;
    }

    public static void clearAll(){
        for (ClientID clientID : ids.keySet()){
            if (!clientID.isProcessing && !clientID.isLogged)
                ids.remove(clientID);
        }
    }

    /**
     * Clears count of received packets from this client.
     * @return Returns current ClientID.
     */
    public ClientID clearOutsideStatement(){
        receivedPackets = 0;
        return this;
    }

    @Override
    public boolean equals(Object obj){
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
    public String toString(){
        return "Клиент с IP " + address + " и портом " + port;
    }

    @Override
    public int hashCode(){
        final int prime = 17;
        int result = 1;
        result = result*prime + ((address == null) ? 0 : address.hashCode());
        result = result*prime + port;
        return result;
    }

}
