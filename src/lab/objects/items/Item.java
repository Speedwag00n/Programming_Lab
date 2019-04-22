package lab.objects.items;

import lab.interfaces.Named;
import lab.locations.Location;

import java.io.Serializable;

public abstract class Item implements Named, Serializable {

    private String name = "Безымянный";
    private Location location;

    public Item(){

    }

    public Item(String aName){
        setName(aName);
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

    public Location getLocation(){
        return location;
    }

    public void setLocation(Location aLocation){
        location = aLocation;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Item object = (Item) obj;
        if (!name.equals(object.name))
            return false;
        return true;
    }

    @Override
    public int hashCode(){
        final int prime = 17;
        int result = 1;
        result = result*prime + ((name == null) ? 0 : name.hashCode());
        return result;
    }

}
