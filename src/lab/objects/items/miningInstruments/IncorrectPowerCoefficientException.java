package lab.objects.items.miningInstruments;

/**
 * Exception that throws when power coefficient of mining instrument of its subclasses is incorrect.
 * * @author Nemankov Ilia
 * * @version 1.0.0
 * * @since 1.4.0
 */
public class IncorrectPowerCoefficientException extends Exception {

    private float coefficient;

    /**
     * Constructor of IncorrectPowerCoefficientException class.
     *
     * @param message      message that describe cause of exception.
     * @param aCoefficient coefficient that is incorrect
     */
    public IncorrectPowerCoefficientException(String message, float aCoefficient) {
        super(message);
        coefficient = aCoefficient;
    }

    /**
     * Method that returns incorrect power coefficient.
     *
     * @return incorrect power coefficient.
     */
    public float getPowerCoefficient() {
        return coefficient;
    }

}
