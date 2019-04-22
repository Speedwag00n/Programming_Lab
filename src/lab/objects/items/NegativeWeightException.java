package lab.objects.items;

public class NegativeWeightException extends Exception{

    private float weight;

    public NegativeWeightException(String message, float aWeight){
        super(message);
        weight = aWeight;
    }

    public float getWeight(){
        return weight;
    }

}
