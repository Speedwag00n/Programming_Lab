package lab.objects.items;

/**
 * Exception that throws when weight of rock or piece is incorrect.
 * * @author Nemankov Ilia
 * * @version 1.0.0
 * * @since 1.4.0
 */
public class NegativeWeightException extends Exception {

    private float weight;

    /**
     * Constructor of NegativeWeightException class.
     *
     * @param message message that describe cause of exception.
     * @param aWeight weight that is incorrect
     */
    public NegativeWeightException(String message, float aWeight) {
        super(message);
        weight = aWeight;
    }

    /**
     * Method that returns incorrect weight.
     *
     * @return incorrect weight.
     */
    public float getWeight() {
        return weight;
    }

}
