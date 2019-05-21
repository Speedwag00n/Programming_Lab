package lab.server.database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * PasswordGenerator class allows to receive new passwords and encode them using MD2 algorithm of hashing.
 * * @author Nemankov Ilia
 * * @version 1.0.0
 * * @since 1.7.0
 */
public class PasswordsGenerator {

    private MessageDigest messageDigest;

    /**
     * Constructor of PasswordGenerator class.
     */
    public PasswordsGenerator() {
        try {
            messageDigest = MessageDigest.getInstance("MD2");
        } catch (NoSuchAlgorithmException e) {

        }
    }

    /**
     * Gives new password.
     *
     * @return new password that is formed by 16 latin letters or arabic numbers
     */
    public String getPassword() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
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

    /**
     * Encode received password using MD2 algorithm of hashing.
     *
     * @param password password that need to be encoded.
     * @return encoded password (32 hex symbols).
     */
    public String encode(String password) {
        byte[] hash = messageDigest.digest(password.getBytes());
        StringBuilder result = new StringBuilder();
        for (byte b : hash) {
            result.append(String.format("%02x", b));
        }
        return result.toString().toLowerCase();
    }

}
