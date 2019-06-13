package lab.util.commands;

import java.net.InetAddress;

/**
 * This class is a base of all server commands classes.
 *
 * @author Nemankov Ilia
 * @version 1.1.0
 * @since 1.7.0
 */
public abstract class ServerCommand extends Command implements Packable {

    private byte[] packedArgument;
    private String login;
    private InetAddress inetAddress;
    private int port;

    /**
     * Constructor of ServerCommand class.
     */
    public ServerCommand(){
        packedArgument = new byte[0];
    }

    /**
     * Constructor of ServerCommand class.
     *
     * @param argument argument of command.
     */
    public ServerCommand(byte[] argument) {
        this.packedArgument = argument;
    }

    /**
     * Gets packed argument of command.
     *
     * @return packed argument of command.
     */
    public byte[] getPackedArgument() {
        return packedArgument;
    }

    @Override
    public byte[] getPackedData() {
        return packedArgument;
    }

    public void setPackedArgument(byte[] argument) {
        this.packedArgument = argument;
    }

    /**
     * Login getter.
     *
     * @return login.
     */
    public String getLogin(){
        return login;
    }

    /**
     * Login setter.
     *
     * @param login username of user that send request.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * InetAddress of sending getter
     *
     * @return InetAddress of sending
     */
    public InetAddress getInetAddress() {
        return inetAddress;
    }

    /**
     * InetAddress of sending setter
     *
     * @param inetAddress InetAddress of sending
     */
    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    /**
     * Port of sending getter
     *
     * @return port of sending
     */
    public int getPort(){
        return port;
    }

    /**
     * Port of sending setter
     *
     * @param port port of sending
     */
    public void setPort(int port){
        this.port = port;
    }

    /**
     * Method that says: need client be authorized for execute this command.
     *
     * @return true if client needs to be authorized for execute this command else false.
     */
    public abstract boolean needBeAuthorized();

}
