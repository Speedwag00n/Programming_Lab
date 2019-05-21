package lab.locations;

import lab.interfaces.Named;
import lab.locations.position.RectanglePosition;
import lab.objects.ContainsItems;
import lab.objects.items.Item;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Class of location that contains items
 * * @author Nemankov Ilia
 * * @version 1.0.0
 * * @since 1.3.0
 */
public class Location implements Named, ContainsItems, Comparable<Location>, Serializable {

    private String name = "Безымянный";
    private ArrayList<Item> itemsList = new ArrayList<>();
    private int area;
    private RectanglePosition position;
    private ZonedDateTime dateOfCreation;
    private String owner = "";
    private int id = 0;

    /**
     * Empty constructor of Location
     */
    public Location() {
        System.out.println("Создана новая безымянная локация.");
    }

    /**
     * Constructor of location without date of creation.
     * Date of creation initialize by date and time of object creation and by current timezone.
     *
     * @param name     Name of location.
     * @param area     Area of location.
     * @param position Position of location (rectangle where it is).
     */
    public Location(String name, int area, RectanglePosition position) {
        setName(name);
        this.area = area;
        this.position = position;
        this.dateOfCreation = ZonedDateTime.now();
    }

    /**
     * Constructor of location with date of creation.
     *
     * @param name           Name of location.
     * @param area           Area of location.
     * @param position       Position of location (rectangle where it is).
     * @param dateOfCreation Date and time of object creation.
     */
    public Location(String name, int area, RectanglePosition position, ZonedDateTime dateOfCreation) {
        setName(name);
        this.area = area;
        this.position = position;
        this.dateOfCreation = dateOfCreation;
    }

    /**
     * Constructor of location that allows to create location where defined only name.
     *
     * @param name Name of location.
     */
    public Location(String name) {
        setName(name);
    }

    /**
     * Getter for receive area of location.
     *
     * @return area of location.
     */
    public int getArea() {
        return area;
    }

    /**
     * Getter for receive position of location.
     *
     * @return position of location.
     */
    public RectanglePosition getPosition() {
        return position;
    }

    /**
     * Getter for receive date of object creation.
     *
     * @return date of object creation.
     */
    public ZonedDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    /**
     * Getter for receive list of location items.
     *
     * @return list of location items.
     */
    public List<Item> getItems() {
        return itemsList;
    }

    /**
     * Method that allows to add items to location.
     *
     * @param items list of items that need to be added.
     */
    public void addItems(List<Item> items) {
        for (Item item : items)
            addItem(item);
    }

    /**
     * Method that allows to add item to location.
     *
     * @param item item that need to be added.
     */
    public void addItem(Item item) {
        if (item != null) {
            if (item.getLocation() != null) {
                item.getLocation().delItemByReference(item);
            }
            itemsList.add(item);
            item.setLocation(this);
        }
    }

    /**
     * Method that allows to remove item from location.
     *
     * @param item item that need to be removed.
     */
    public void delItemByReference(Item item) {
        if (item != null) {
            itemsList.remove(item);
            item.setLocation(null);
        } else
            System.out.println("Некорректный предмет.");
    }

    /**
     * Method that allows to remove item from location using index of this item in list of items.
     *
     * @param index index of item that need to be removed.
     */
    public void delItemByIndex(int index) {
        if ((index >= 0) && (index < itemsList.size())) {
            itemsList.get(index).setLocation(null);
            itemsList.remove(index);
        } else
            System.out.println("Некорректный индекс предмета.");
    }

    /**
     * Clear list of location items.
     */
    public void clearItems() {
        itemsList = new ArrayList<>();
        System.out.println("Локация " + getName() + " теперь пуста.");
    }

    /**
     * Getter to receive name of location.
     *
     * @return name of location.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter to set name of location.
     *
     * @param name name of location.
     */
    public void setName(String name) {
        if (name != null)
            this.name = name;
        else
            System.out.println("Некорректное значение имени.");
    }

    /**
     * Method that allows to know is location named or not.
     *
     * @return false if location is named by default name else return true.
     */
    public boolean isNamed() {
        return (!name.equals("Безымянный"));
    }

    public int compareTo(Location location) {
        return (this.getItems().size() - location
                .getItems().size());
    }

    /**
     * Setter to set owner of location.
     *
     * @param owner owner of location.
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Getter to receive owner of location.
     *
     * @return owner of location.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Setter to set id of location.
     *
     * @param id id of location.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter to receive id of location.
     *
     * @return id of location.
     */
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Location object = (Location) obj;
        if (!name.equals(object.name))
            return false;
        if (area != object.area)
            return false;
        if (!position.equals(object.position))
            return false;
        if (!itemsList.equals(object.itemsList))
            return false;
        if (!owner.equals(object.owner))
            return false;
        return true;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm:ss");
        return "Локация " + getName() + ". Количество предметов в локации: " + itemsList.size() + ". Площадь "
                + getArea() + ". Позиция: " + getPosition().toString() + ". Дата создания: " + formatter.format(getDateOfCreation()) + ".";
    }

    @Override
    public int hashCode() {
        final int prime = 17;
        int result = 1;
        result = result * prime + ((name == null) ? 0 : name.hashCode());
        result = result * prime + area;
        result = result * prime + ((position == null) ? 0 : position.hashCode());
        result = result * prime + ((itemsList == null) ? 0 : itemsList.hashCode());
        result = result * prime + ((owner == null) ? 0 : owner.hashCode());
        return result;
    }

}
