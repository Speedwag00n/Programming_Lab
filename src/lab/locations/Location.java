package lab.locations;

import lab.locations.position.RectanglePosition;
import lab.objects.items.Item;
import lab.objects.ContainsItems;
import lab.interfaces.Named;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class Location implements Named, ContainsItems, Comparable<Location>, Serializable {

    private String name = "Безымянный";
    private ArrayList<Item> itemsList = new ArrayList<>();
    private int area;
    private RectanglePosition position;
    private ZonedDateTime dateOfCreation;

    public Location(){
        System.out.println("Создана новая безымянная локация.");
    }

    public Location(String name, int area, RectanglePosition position){
        setName(name);
        this.area = area;
        this.position = position;
        this.dateOfCreation = ZonedDateTime.now();
    }

    public Location(String name, int area, RectanglePosition position, ZonedDateTime dateOfCreation){
        setName(name);
        this.area = area;
        this.position = position;
        this.dateOfCreation = dateOfCreation;
    }

    public Location(String aName){
        setName(aName);
    }

    public int getArea(){
        return area;
    }

    public RectanglePosition getPosition(){
        return position;
    }

    public ZonedDateTime getDateOfCreation(){
        return dateOfCreation;
    }

    public ArrayList<Item> getItems(){
        return itemsList;
    }

    public void addItems(ArrayList<Item> items){
        for (Item item : items)
            addItem(item);
    }

    public void addItem(Item aItem){
        if(aItem != null) {
            if(aItem.getLocation() != null){
                aItem.getLocation().delItemByReference(aItem);
            }
            itemsList.add(aItem);
            aItem.setLocation(this);
        }
    }

    public void delItemByReference(Item item){
        if(item != null) {
            itemsList.remove(item);
            item.setLocation(null);
        }
        else
            System.out.println("Некорректный предмет.");
    }

    public void delItemByIndex(int index){
        if((index >= 0)&&(index < itemsList.size())) {
            itemsList.get(index).setLocation(null);
            itemsList.remove(index);
        }
        else
            System.out.println("Некорректный индекс предмета.");
    }

    public void clearItems(){
        itemsList = new ArrayList<Item>();
        System.out.println("Локация " + getName() + " теперь пуста.");
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
        return (!name.equals("Безымянный"));
    }

    public void generateDecorations(int count, String ... names){
        class Decoration extends Item {
            public Decoration(String aName) {
                setName(aName);
            }

            @Override
            public int hashCode(){
                final int prime = 7;
                int result = super.hashCode();
                result = result*prime;
                return result;
            }
        }
        for(int i = 0; i < count; i++){
            this.addItem(new Decoration(names[(int) Math.round(Math.random() * (names.length - 1))]));
        }
    }

    public int compareTo (Location location){
        return (this.getItems().size() - location
                .getItems().size());
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Location object = (Location) obj;
        if (!name.equals(object.name))
            return false;
        if (!itemsList.equals(object.itemsList))
            return false;
        return true;
    }

    @Override
    public String toString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm:ss");
        return "Локация " + getName() + ". Количество предметов в локации: " + itemsList.size() + ". Площадь "
                + getArea() + ". Позиция: " + getPosition().toString() + ". Дата создания: " + formatter.format(getDateOfCreation()) + ".";
    }

    @Override
    public int hashCode(){
        final int prime = 17;
        int result = 1;
        result = result*prime + ((name == null) ? 0 : name.hashCode());
        result = result*prime + ((itemsList == null) ? 0 : itemsList.hashCode());
        return result;
    }

}
