package lab.client.mvc.model;

import lab.client.CommandsSender;
import lab.client.LocationsContainer;
import lab.client.ServerAnswer;
import lab.client.settings.Settings;
import lab.locations.Location;
import lab.locations.position.CoordsPair;
import lab.locations.position.RectanglePosition;
import lab.objects.items.Item;
import lab.util.CollectionElementsBuilder;
import lab.util.commands.Add;
import lab.util.commands.InvalidArgumentsException;
import lab.util.commands.ServerCommand;
import lab.util.commands.Update;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class LocationCreationModel {

    private ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());

    private String name;
    private RectanglePosition position;

    private List<Item> itemsList = new ArrayList<>();

    private Location oldLocation;

    private String itemIcon;

    public void setName(String name) {
        this.name = name.trim();
    }

    public String getName() {
        return name;
    }

    public void validateName(String name) throws InvalidArgumentsException {
        if (name.trim().length() == 0)
            throw new InvalidArgumentsException(resources.getString("location_creation_model_empty_name"));
    }

    public void setPosition(String bottomLeftX, String bottomLeftY, String topRightX, String topRightY) {
        this.position = new RectanglePosition(new CoordsPair(Integer.parseInt(bottomLeftX), Integer.parseInt(bottomLeftY)), new CoordsPair(Integer.parseInt(topRightX), Integer.parseInt(topRightY)));
    }

    public RectanglePosition getPosition() {
        return position;
    }

    public void validatePosition(String bottomLeftX, String bottomLeftY, String topRightX, String topRightY) throws InvalidArgumentsException {
        if (Integer.parseInt(bottomLeftX) > Integer.parseInt(topRightX))
            throw new InvalidArgumentsException(resources.getString("location_creation_model_swapped_x"));
        if (Integer.parseInt(bottomLeftX) == Integer.parseInt(topRightX))
            throw new InvalidArgumentsException(resources.getString("location_creation_model_equals_x"));
        if (Integer.parseInt(bottomLeftY) > Integer.parseInt(topRightY))
            throw new InvalidArgumentsException(resources.getString("location_creation_model_swapped_y"));
        if (Integer.parseInt(bottomLeftY) == Integer.parseInt(topRightY))
            throw new InvalidArgumentsException(resources.getString("location_creation_model_equals_y"));
    }

    public void validatePiece(String name, String weight) throws InvalidArgumentsException {
        if (name.length() == 0)
            throw new InvalidArgumentsException(resources.getString("location_creation_model_empty_name"));
        try {
            if (Float.parseFloat(weight) < 0)
                throw new InvalidArgumentsException(resources.getString("piece_negative_weight"));
        } catch (NumberFormatException e) {
            throw new InvalidArgumentsException(resources.getString("piece_invalid_weight"));
        }
    }

    public void validateRock(String name, String stoneWeight, String oreWeight) throws InvalidArgumentsException {
        if (name.length() == 0)
            throw new InvalidArgumentsException(resources.getString("location_creation_model_empty_name"));
        try {
            if (Float.parseFloat(stoneWeight) < 0)
                throw new InvalidArgumentsException(resources.getString("rock_negative_stone_weight"));
        } catch (NumberFormatException e) {
            throw new InvalidArgumentsException(resources.getString("rock_invalid_stone_weight"));
        }
        try {
            if (Float.parseFloat(oreWeight) < 0)
                throw new InvalidArgumentsException(resources.getString("rock_negative_ore_weight"));
        } catch (NumberFormatException e) {
            throw new InvalidArgumentsException(resources.getString("rock_invalid_ore_weight"));
        }
    }

    public void validateMiningInstrument(String name, String coefficient) throws InvalidArgumentsException {
        if (name.length() == 0)
            throw new InvalidArgumentsException(resources.getString("location_creation_model_empty_name"));
        try {
            Float.parseFloat(coefficient);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentsException(resources.getString("mining_instrument_invalid_coefficient"));
        }
        if (Float.parseFloat(coefficient) < 0 || Float.parseFloat(coefficient) > 1)
            throw new InvalidArgumentsException(resources.getString("mining_instrument_coefficient_out_of_diapason"));
    }

    public void validateHammer(String name, Object headMaterial, Object handleMaterial) throws InvalidArgumentsException {
        if (name.length() == 0)
            throw new InvalidArgumentsException(resources.getString("location_creation_model_empty_name"));
        if (headMaterial == null)
            throw new InvalidArgumentsException(resources.getString("hammer_invalid_head_material"));
        if (handleMaterial == null)
            throw new InvalidArgumentsException(resources.getString("hammer_invalid_handle_material"));
    }

    public List<Item> getItems() {
        return itemsList;
    }

    public Location getLocation() {
        int area = (position.getRightTopPoint().getX() - position.getLeftBottomPoint().getX()) *
                (position.getRightTopPoint().getY() - position.getLeftBottomPoint().getY());
        Location location = new Location(name, area, position);
        location.addItems(itemsList);
        location.setOwner(Settings.getUsername());

        return location;
    }

    public Location getOldLocation() {
        return oldLocation;
    }

    public void setLocation(Location location) {
        oldLocation = location;
        this.name = location.getName();
        this.position = location.getPosition();
        this.itemsList = new ArrayList<>();
        this.itemsList.addAll(location.getItems());
    }

    public Location updateLocation(boolean updateOldLocation) {
        Location newLocation;
        if (updateOldLocation)
            newLocation = oldLocation;
        else {
            newLocation = new Location(oldLocation.getName(), oldLocation.getArea(), oldLocation.getPosition(), oldLocation.getDateOfCreation());
            newLocation.addItems(oldLocation.getItems());
            newLocation.setOwner(oldLocation.getOwner());
        }
        newLocation.setName(name);
        newLocation.setArea((position.getRightTopPoint().getX() - position.getLeftBottomPoint().getX()) *
                (position.getRightTopPoint().getY() - position.getLeftBottomPoint().getY()));
        newLocation.setPosition(position);

        newLocation.clearItems();
        newLocation.addItems(itemsList);

        return newLocation;
    }

    public Item addItem(String className, HashMap<String, String> itemAttributes) {
        Item item = null;
        try {
            item = CollectionElementsBuilder.constructItem(className, itemAttributes);
        } catch (NoSuchMethodException e) {

        } catch (IllegalAccessException e) {

        } catch (InvocationTargetException e) {

        }
        itemsList.add(item);
        return item;
    }

    public void deleteItem(Item item) {
        itemsList.remove(item);
    }

    public ServerAnswer saveLocation(Location location) {
        CommandsSender commandsSender = CommandsSender.getInstance();
        ServerCommand addCommand = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(location);
            baos.toByteArray();
            addCommand = new Add(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        LocationsContainer.getInstance().getLocations().add(location);
        return commandsSender.trySendRequest(addCommand);
    }

    public ServerAnswer updateLocationOnServer() {
        CommandsSender commandsSender = CommandsSender.getInstance();
        ServerCommand updateCommand = null;
        Location oldLocation = this.oldLocation;
        Location newLocation = updateLocation(false);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(oldLocation);
            oos.writeObject(newLocation);
            baos.toByteArray();
            updateCommand = new Update(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ServerAnswer serverAnswer = commandsSender.trySendRequest(updateCommand);
        updateLocation(true);
        return serverAnswer;
    }

    public void saveItemIcon(String filePath) {
        this.itemIcon = filePath;
    }

    public void assignIcon(Item item) throws InvalidArgumentsException {
        File iconFile;
        if (itemIcon != null)
            iconFile = new File(itemIcon);
        else
            return;
        try {
            byte[] packedIcon = Files.readAllBytes(iconFile.toPath());
            item.setPackedIcon(packedIcon);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}