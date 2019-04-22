package lab.objects;

import lab.objects.items.Item;
import java.util.ArrayList;

public interface ContainsItems {

    ArrayList<Item> getItems();
    void addItems(ArrayList<Item> skills);
    void addItem(Item aItem);
    void delItemByReference(Item skill);
    void delItemByIndex(int index);
    void clearItems();

}
