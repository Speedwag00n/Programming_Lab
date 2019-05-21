package lab.objects;

import lab.objects.items.Item;

import java.util.List;

/**
 * The ContainsItem interface should be implemented by any class which is container for items.
 * * @author Nemankov Ilia
 * * @version 1.0.0
 * * @since 1.3.0
 */
public interface ContainsItems {

    /**
     * Getter for receive list of object items.
     *
     * @return list of object items.
     */
    List<Item> getItems();

    /**
     * Method that allows to add items to object container.
     *
     * @param items list of items that need to be added.
     */
    void addItems(List<Item> items);

    /**
     * Method that allows to add item to object container.
     *
     * @param item item that need to be added.
     */
    void addItem(Item item);

    /**
     * Method that allows to remove item from object container.
     *
     * @param item item that need to be removed.
     */
    void delItemByReference(Item item);

    /**
     * Method that allows to remove item from object container using index of this item in list of items.
     *
     * @param index index of item that need to be removed.
     */
    void delItemByIndex(int index);

    /**
     * Clear list of object items.
     */
    void clearItems();

}
