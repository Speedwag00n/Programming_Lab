package lab.objects.entities;

import lab.interfaces.Named;

public abstract class Creature implements Named {

    private String name = "Безымянный";
    private int age = 0;

    public Creature(){
    }

    public Creature(String aName){
        setName(aName);
    }

    public Creature(String aName, int aAge){
        setName(aName);
        try {
            setAge(aAge);
        }
        catch (NegativeAgeException e){
            System.out.println("Возникло исключение " + e);
        }
    }

    public String getName(){
        return name;
    }

    public void setName(String aName){
        if(aName != null)
            name = aName;
        else
            System.out.println("Некорректное значение имени.");
    }

    public boolean isNamed(){
        return (name != "Безымянный");
    }

    public int getAge(){
        return age;
    }

    public void setAge(int aAge) throws NegativeAgeException {
        if (aAge < 0)
            throw new NegativeAgeException("Введите неотрицаительное значение возраста!", aAge);
        else
            age = aAge;
    }

    public void incAge(){
        age++;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Creature object = (Creature) obj;
        if (!name.equals(object.name))
            return false;
        if (age != object.age)
            return false;
        return true;
    }

    @Override
    public int hashCode(){
        final int prime = 17;
        int result = 1;
        result = result*prime + ((name == null) ? 0 : name.hashCode());
        result = result*prime + age;
        return result;
    }

}