package lab.interfaces;

/**
 * The Named interface should be implemented by any class which has name.
 * * @author Nemankov Ilia
 * * @version 1.0.0
 * * @since 1.3.0
 */
public interface Named {

    /**
     * Name getter.
     *
     * @return name.
     */
    String getName();

    /**
     * Name setter.
     *
     * @param name name.
     */
    void setName(String name);

    /**
     * Method that allows to know is object named or not.
     *
     * @return false if object is named by default name else return true.
     */
    boolean isNamed();

}
