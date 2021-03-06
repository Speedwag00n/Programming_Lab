package lab.util.packet;

/**
 * Class that contains public static constants for UDP packets format.
 *
 * @author Nemankov Ilia
 * @version 1.1.0
 * @since 1.6.0
 */
public class PacketSettings {

    public static final int PACKET_LENGTH = 2048;
    public static final int REQUIRED_CLIENT_METADATA_LENGTH = 4;
    public static final int REQUIRED_SERVER_METADATA_LENGTH = 4;

    public static final int COMMAND_CODE_LENGTH = 1;
    public static final int TOKEN_LENGTH = 16;
    public static final int SINGLE_CLIENT_METADATA = COMMAND_CODE_LENGTH + TOKEN_LENGTH;

    public static final int COMMAND_EXECUTION_CODE_LENGTH = 1;
    public static final int SINGLE_SERVER_METADATA = COMMAND_EXECUTION_CODE_LENGTH + TOKEN_LENGTH;

}