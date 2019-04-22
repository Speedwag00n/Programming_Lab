package lab.objects.items.miningInstruments;

public class IncorrectPowerCoefficientException extends Exception{

    private float coefficient;

    public IncorrectPowerCoefficientException(String message, float aCoefficient){
        super(message);
        coefficient = aCoefficient;
    }

    public float getPowerCoefficient(){
        return coefficient;
    }

}
