package lab.server.processing;

import lab.util.packet.PacketSettings;

import java.net.InetAddress;
import java.util.*;

/**
 * The Session class represent connection between authorized client and server.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.8.0
 */
public class Session {

    private String sessionToken;
    private InetAddress address;
    private int port;
    private String login;

    private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    private Session(InetAddress address, int port, String login){
        this.sessionToken = generateToken();
        this.address = address;
        this.port = port;
        this.login = login;
    }

    public static Session isAuthorized(String token, InetAddress address, int port) {
        synchronized(sessions) {
            for (Session session : sessions) {
                if (session.sessionToken.equals(token) && session.address.equals(address) && session.port == port)
                    return session;
            }
            return null;
        }
    }

    public static String createSession(InetAddress address, int port, String login) {
        synchronized(sessions) {
            for (Session session : sessions) {
                if (session.login.equals(login) || (session.address.equals(address) && session.port == port)) {
                    sessions.remove(session);
                    break;
                }
            }
            Session session = new Session(address, port, login);
            sessions.add(session);
            return session.sessionToken;
        }
    }

    /**
     * Login getter.
     *
     * @return user login.
     */
    public String getLogin() {
        return login;
    }

    private String generateToken() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < PacketSettings.TOKEN_LENGTH; i++) {
            int j = random.nextInt(3);
            int symbolCode;
            if (j == 0)
                symbolCode = random.nextInt(9) + 48; //Generate number
            else if (j == 1)
                symbolCode = random.nextInt(25) + 65; //Generate uppercase letter
            else
                symbolCode = random.nextInt(25) + 97; //Generate lowercase letter
            stringBuilder.append((char) symbolCode);
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Session object = (Session) obj;
        if (!sessionToken.equals(object.sessionToken))
            return false;
        if (!address.equals(object.address))
            return false;
        if (port != object.port)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 17;
        int result = 1;
        result = result * prime + sessionToken.hashCode();
        result = result * prime + address.hashCode();
        result = result * prime + port;
        return result;
    }
}
