package lab.server.database.changes;

import java.net.InetAddress;

public class Subscriber {

    private InetAddress inetAddress;
    private int port;

    public Subscriber(InetAddress inetAddress, int port) {
        this.inetAddress = inetAddress;
        this.port = port;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Subscriber object = (Subscriber) obj;
        if (!inetAddress.equals(object.inetAddress))
            return false;
        if (port != object.port)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 17;
        int result = 1;
        result = result * prime + ((inetAddress == null) ? 0 : inetAddress.hashCode());
        result = result * prime + port;
        return result;
    }

}
