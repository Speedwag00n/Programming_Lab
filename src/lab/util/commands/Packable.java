package lab.util.commands;

/**
 * This interface should be implemented by any class of command which can pack its fields.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.7.0
 */
public interface Packable {

    /**
     * Packs current command object fields as a byte array.
     *
     * @return packed fields of command object.
     */
    byte[] getPackedData();

}
