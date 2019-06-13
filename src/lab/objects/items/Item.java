package lab.objects.items;

import javafx.scene.image.Image;
import lab.interfaces.Named;
import lab.locations.Location;

import java.io.*;
import java.nio.file.Files;

/**
 * Item class.
 * * @author Nemankov Ilia
 * * @version 1.1.0
 * * @since 1.3.0
 */
public abstract class Item implements Named, Serializable {

    private String name = "Безымянный";
    private Location location;
    private byte[] icon;

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

    /**
     * This method allows to get attributes description of appropriate item.
     *
     * @return attributes description.
     */
    public String getAttributesDescription() {
        return "";
    }

    /**
     * Getter for packed item icon.
     *
     * @return packed item icon.
     */
    public byte[] getPackedIcon() {
        return icon;
    }

    /**
     * Setter for packed item icon.
     *
     * @return packed item icon.
     */
    public void setPackedIcon(byte[] icon) {
        this.icon = icon;
    }

    /**
     * Getter for item icon.
     *
     * @return item icon.
     */
    public Image getIcon() {
        if (icon != null && icon.length != 0) {
            try (ByteArrayInputStream byteStream = new ByteArrayInputStream(icon)) {
                Image image = new Image(byteStream);
                return image;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            return new Image("/resources/img/items/" + getClass().getSimpleName().toLowerCase() + ".png");
        }
        return null;
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
