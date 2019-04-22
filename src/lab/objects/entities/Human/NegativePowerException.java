package lab.objects.entities.Human;

public class NegativePowerException extends Exception{

    private float power;

    public NegativePowerException(String message, float aPower){
        super(message);
        power = aPower;
    }

    public float getPower(){
        return power;
    }

}
