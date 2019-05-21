package lab.objects.items;

import lab.interfaces.Named;
import lab.locations.Location;

import java.io.Serializable;

/**
 * Item class.
 * * @author Nemankov Ilia
 * * @version 1.0.0
 * * @since 1.3.0
 */
public abstract class Item implements Named, Serializable {

    private String name = "Безымянный";
    private Location location;

    /**
     * Empty constructor.
     */
    public Item() {

    }

    /**
     * Constructor of item with its name.
     *
     * @param aName name of item.
     */
    public Item(String aName) {
        setName(aName);
    }

    /**
     * Getter to receive name of item.
     *
     * @return name of item.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter to set name of item.
     *
     * @param aName name of item.
     */
    public void setName(String aName) {
        if (aName != null)
            name = aName;
        else
            System.out.println("Некорректное значение имени.");
    }

    /**
     * Method that allows to know is item named or not.
     *
     * @return false if item is named by default name else return true.
     */
    public boolean isNamed() {
        return (name != "Безымянный");
    }

    /**
     * Method that allows to know in which location item is.
     *
     * @return
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Method that set item in new location.
     *
     * @param aLocation new location where item'll be.
     */
    public void setLocation(Location aLocation) {
        location = aLocation;
    }

    @Override
    public boolean equals(Object obj) {
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
    public int hashCode() {
        final int prime = 17;
        int result = 1;
        result = result * prime + ((name == null) ? 0 : name.hashCode());
        return result;
    }

}
