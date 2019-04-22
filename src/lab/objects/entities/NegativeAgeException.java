package lab.objects.entities;

public class NegativeAgeException extends Exception{

    private float age;

    public NegativeAgeException(String message, float aAge){
        super(message);
        age = aAge;
    }

    public float getAge(){
        return age;
    }

}
