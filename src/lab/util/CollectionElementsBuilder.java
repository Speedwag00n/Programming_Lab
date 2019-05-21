package lab.util;

import lab.locations.Location;
import lab.locations.position.CoordsPair;
import lab.locations.position.RectanglePosition;
import lab.objects.items.Item;
import lab.objects.items.miningInstruments.Hammer;
import lab.objects.items.miningInstruments.MiningInstrument;
import lab.objects.items.rock.Rock;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Auxiliary class that contains static methods for parse Location and Item objects from xml and json format or pack them to xml format.
 *
 * @author Nemankov Ilia
 * @version 1.1.0
 * @since 1.5.0
 */
public class CollectionElementsBuilder {

    /**
     * Adds to the collection new elements which program constructs from data from XML file. <br>
     * Method skips elements that don't have only two tags (locationName and items array) <br>
     * Method skips items that have unknown or unsupported name of class, invalid form of attributes representation or values of attributes that can't be cast to necessary type.
     * Also it skips items that don't have only two tags (class and attributes array) <br>
     * Method counts added elements, missed elements and items and shows information in the end of invoking.
     *
     * @param filePath way to file with XML code from which program takes data for update collection
     * @return true if program found file and its XML code is valid else return false
     */
    public static boolean fromFile(String filePath, Collection<Location> collection) {

        String xml;
        try (Scanner scanner = new Scanner(new File(filePath))) {

            StringBuilder buildingXML = new StringBuilder();

            while (scanner.hasNextLine()) {

                buildingXML.append(scanner.nextLine().trim());

            }
            xml = buildingXML.toString();

        } catch (FileNotFoundException | NullPointerException ex) {
            System.out.println("Не удалось загрузить элементы, файл не найден.");
            return false;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        int loadingElements = 0, outsideElements = 0, defectiveElements = 0, defectiveItems = 0, unknownClasses = 0;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            NodeList elements = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < elements.getLength(); i++) {
                if (!elements.item(i).getNodeName().equals("element")) {
                    outsideElements++;
                    continue;
                }
                NodeList elementChildren = elements.item(i).getChildNodes();
                if (elementChildren.getLength() != 5
                        || !elementChildren.item(0).getNodeName().equals("locationName")
                        || !elementChildren.item(1).getNodeName().equals("area")
                        || !elementChildren.item(2).getNodeName().equals("position")
                        || !elementChildren.item(3).getNodeName().equals("date")
                        || !elementChildren.item(4).getNodeName().equals("items")) {
                    defectiveElements++;
                    continue;
                }
                NodeList positionNodes = elementChildren.item(2).getChildNodes();
                int area = 0;
                RectanglePosition rectanglePosition = null;
                if (positionNodes.getLength() != 2
                        || !positionNodes.item(0).getNodeName().equals("leftBottomPoint")
                        || !positionNodes.item(1).getNodeName().equals("rightTopPoint")) {
                    defectiveElements++;
                    continue;
                } else {
                    NodeList leftBottomPointNode = positionNodes.item(0).getChildNodes();
                    if (leftBottomPointNode.getLength() != 2
                            || !leftBottomPointNode.item(0).getNodeName().equals("X")
                            || !leftBottomPointNode.item(1).getNodeName().equals("Y")) {
                        defectiveElements++;
                        continue;
                    }
                    NodeList rightTopPointNode = positionNodes.item(1).getChildNodes();
                    if (rightTopPointNode.getLength() != 2
                            || !rightTopPointNode.item(0).getNodeName().equals("X")
                            || !rightTopPointNode.item(1).getNodeName().equals("Y")) {
                        defectiveElements++;
                        continue;
                    }
                    try {
                        area = Integer.parseInt(elementChildren.item(1).getTextContent());
                        rectanglePosition = new RectanglePosition(new CoordsPair(Integer.parseInt(leftBottomPointNode.item(0).getTextContent()), Integer.parseInt(leftBottomPointNode.item(1).getTextContent())),
                                new CoordsPair(Integer.parseInt(rightTopPointNode.item(0).getTextContent()), Integer.parseInt(rightTopPointNode.item(0).getTextContent())));
                    } catch (NumberFormatException ex) {
                        defectiveElements++;
                        continue;
                    }
                }
                NodeList date = elementChildren.item(3).getChildNodes();
                ZonedDateTime zonedDateTime = null;
                if (date.getLength() != 2
                        || !date.item(0).getNodeName().equals("seconds")
                        || !date.item(1).getNodeName().equals("timeZone")) {
                    defectiveElements++;
                    continue;
                } else {
                    try {
                        zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(date.item(0).getTextContent())), ZoneId.of(date.item(1).getTextContent()));
                    } catch (NumberFormatException ex) {
                        defectiveElements++;
                        continue;
                    }
                }
                Location location = new Location(elementChildren.item(0).getTextContent(), area, rectanglePosition, zonedDateTime);

                NodeList itemsNodes = elementChildren.item(4).getChildNodes();
                for (int j = 0; j < itemsNodes.getLength(); j++) {
                    NodeList itemsChildren = itemsNodes.item(j).getChildNodes();
                    if (itemsChildren.getLength() != 2
                            || !itemsChildren.item(0).getNodeName().equals("class")
                            || !itemsChildren.item(1).getNodeName().equals("attributes")) {
                        defectiveItems++;
                        continue;
                    }
                    try {
                        HashMap<String, String> fields = new HashMap<>();
                        for (int k = 0; k < itemsChildren.item(1).getChildNodes().getLength(); k++) {
                            fields.put(itemsChildren.item(1).getChildNodes().item(k).getNodeName(), itemsChildren.item(1).getChildNodes().item(k).getTextContent());
                        }
                        location.addItem(constructItem(itemsChildren.item(0).getTextContent(), fields));
                    } catch (NoSuchMethodException ex) {
                        unknownClasses++;
                    } catch (IllegalAccessException ex) {

                    } catch (InvocationTargetException ex) {
                        defectiveItems++;
                    }

                }

                if (collection.add(location))
                    loadingElements++;

            }
        } catch (ParserConfigurationException ex) {
        } catch (SAXException ex) {
            System.out.println("Ошибка при загрузке данных из файла. Невалидный XML код.");
            return false;
        } catch (IOException ex) {
            System.out.println("Ошибка при загрузке данных из файла, поток для чтения более недоступен.");
            return false;
        }

        System.out.println("Загрузка элементов в коллекцию из файла прошла успешно. Загружено " + loadingElements + " элементов.\nОбнаружено "
                + outsideElements + " посторонних элементов, " + defectiveElements + " элементов и " + defectiveItems + " предметов в неверном формате.\n"
                + "Найдено " + unknownClasses + " предметов, принадлежащих неизвестным классам.");
        return true;
    }

    /**
     * Creates from elements of collection XML code.
     *
     * @return XML code of serialized collection like a String
     */
    public static String collectionToXML(Collection<Location> collection) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("collection");
            document.appendChild(root);
            for (Location location : collection) {
                Element element = document.createElement("element");
                root.appendChild(element);
                Element locationName = document.createElement("locationName");
                locationName.setTextContent(location.getName());
                element.appendChild(locationName);
                Element area = document.createElement("area");
                area.setTextContent("" + location.getArea());
                element.appendChild(area);
                Element position = document.createElement("position");
                element.appendChild(position);
                Element leftBottomPoint = document.createElement("leftBottomPoint");
                position.appendChild(leftBottomPoint);
                Element leftBottomPointX = document.createElement("X");
                leftBottomPointX.setTextContent("" + location.getPosition().getLeftBottomPoint().getX());
                leftBottomPoint.appendChild(leftBottomPointX);
                Element leftBottomPointY = document.createElement("Y");
                leftBottomPointY.setTextContent("" + location.getPosition().getLeftBottomPoint().getY());
                leftBottomPoint.appendChild(leftBottomPointY);
                Element rightTopPoint = document.createElement("rightTopPoint");
                position.appendChild(rightTopPoint);
                Element rightTopPointX = document.createElement("X");
                rightTopPointX.setTextContent("" + location.getPosition().getRightTopPoint().getX());
                rightTopPoint.appendChild(rightTopPointX);
                Element rightTopPointY = document.createElement("Y");
                rightTopPointY.setTextContent("" + location.getPosition().getRightTopPoint().getY());
                rightTopPoint.appendChild(rightTopPointY);
                Element date = document.createElement("date");
                element.appendChild(date);
                Element seconds = document.createElement("seconds");
                seconds.setTextContent("" + location.getDateOfCreation().toInstant().getEpochSecond());
                date.appendChild(seconds);
                Element timeZone = document.createElement("timeZone");
                timeZone.setTextContent("" + location.getDateOfCreation().getZone().toString());
                date.appendChild(timeZone);
                Element items = document.createElement("items");
                element.appendChild(items);
                for (Item itemInLocation : location.getItems()) {
                    Element item = document.createElement("item");
                    items.appendChild(item);
                    Element className = document.createElement("class");
                    String classNameText = itemInLocation.getClass().getSimpleName();
                    className.setTextContent(classNameText);
                    item.appendChild(className);
                    Element attributes = document.createElement("attributes");
                    item.appendChild(attributes);
                    try {
                        Set<Map.Entry<String, String>> fields = ((HashMap) CollectionElementsBuilder.class.getMethod("save" +
                                classNameText, new Class[]{Item.class}).invoke(null, itemInLocation)).entrySet();
                        for (Map.Entry<String, String> entry : fields) {
                            Element attribute = document.createElement(entry.getKey());
                            attribute.setTextContent(entry.getValue());
                            attributes.appendChild(attribute);
                        }
                    } catch (NoSuchMethodException ex) {

                    } catch (IllegalAccessException ex) {

                    } catch (InvocationTargetException ex) {

                    }
                }
            }
            DOMSource domSource = new DOMSource(document);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);

            return writer.toString();
        } catch (ParserConfigurationException ex) {

        } catch (TransformerConfigurationException ex) {

        } catch (TransformerException ex) {

        }

        return null;
    }

    /**
     * Method receives data in json format for creating new element, creates it and returns. <br>
     * Method skips items that have unknown or unsupported name of class, invalid form of attributes representation or values of attributes that can't be cast to necessary type
     *
     * @param jsonElement Argument of command that is valid json code for creating new element
     * @return location if data in json format contains array of items and json code is valid and correct else return null
     */
    public static Location parseJsonElement(String jsonElement) {

        try {

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(jsonElement);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray items = (JSONArray) jsonObject.get("items");
            Iterator<JSONObject> iterator = items.iterator();
            JSONArray position = (JSONArray) jsonObject.get("position");
            Iterator<JSONObject> positionIterator = position.iterator();
            JSONObject leftBottomPosition = positionIterator.next();
            JSONObject rightTopPosition = positionIterator.next();
            RectanglePosition rectanglePosition = new RectanglePosition(new CoordsPair(Integer.parseInt((String) leftBottomPosition.get("X")), Integer.parseInt((String) leftBottomPosition.get("Y"))),
                    new CoordsPair(Integer.parseInt((String) rightTopPosition.get("X")), Integer.parseInt((String) rightTopPosition.get("Y"))));
            Location location = new Location((String) jsonObject.get("locationName"), Integer.parseInt((String) jsonObject.get("area")), rectanglePosition);


            while (iterator.hasNext()) {

                try {
                    JSONObject item = iterator.next();
                    String className = (String) item.get("class");
                    JSONArray attributes = (JSONArray) item.get("attributes");
                    HashMap<String, String> fields = new HashMap<>();
                    JSONObject attribute = (JSONObject) attributes.get(0);
                    Set<String> keys = attribute.keySet();

                    for (String key : keys) {
                        fields.put(key, (String) attribute.get(key));
                    }
                    Item itemInLocation = constructItem(className, fields);
                    System.out.println(itemInLocation.toString());
                    location.addItem(itemInLocation);
                } catch (NoSuchMethodException ex) {
                    System.out.println("Неизвестное имя класса");
                } catch (NullPointerException ex) {
                    System.out.println("Неверный формат предмета");
                } catch (InvocationTargetException ex) {
                    System.out.println("Неверное значение аттрибутов предметов");
                }
            }

            System.out.println(location.toString());

            return location;

        } catch (ParseException | ClassCastException ex) {
            System.out.println("Элемент задан в невенром формате");
        } catch (IllegalAccessException ex) {

        } catch (NullPointerException ex) {
            System.out.println("Отсутствует массив предметов локации");
        } catch (Exception ex) {
            System.out.println("Элемент задан в неверном формета");
        }
        return null;
    }

    /**
     * Method receives class name and HashMap (its keys are fields name and its values are values of these fields) and find necessary method to create new item. <br>
     * Search takes place thanks to reflection, program find method with name: "create" + className.
     *
     * @param className Name of class which needs to create
     * @param fields    HashMap (its keys are fields name and its values are values of these fields) that contains attributes for creating new item
     * @return new item
     * @throws NoSuchMethodException     It takes place if className param is unknown or unsupported class name
     * @throws IllegalAccessException    For supported class this exception never throws, but it can be throwed when trying to invoke outside method that don't relate to creating items
     * @throws InvocationTargetException It throws when values of attributes of item has type that can't be cast to necessary type (For example, expect Number, recieved String with letters)
     */
    public static Item constructItem(String className, HashMap<String, String> fields) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return (Item) CollectionElementsBuilder.class.getMethod("create" + className, new Class[]{HashMap.class}).invoke(null, fields);
    }

    /**
     * Receives attributes contained in HashMap and create new {@link lab.objects.items.rock.Rock}
     *
     * @param fields HashMap (its keys are fields name and its values are values of these fields)
     * @return new {@link lab.objects.items.rock.Rock} object
     */
    public static Rock createRock(HashMap<String, String> fields) {
        Rock rock = new Rock(Float.valueOf(fields.get("weightOfOre")), Float.valueOf(fields.get("weightOfStone")), fields.get("itemName"));
        try {
            if (fields.get("countOfPieces") != null) {
                Field count = Rock.class.getField("countOfPieces");
                count.setAccessible(true);
                count.set(rock, fields.get("countOfPieces"));
            }
        } catch (NoSuchFieldException ex) {

        } catch (IllegalAccessException ex) {

        }

        return rock;
    }

    /**
     * Serializes {@link lab.objects.items.rock.Rock} and returns its attributes contained in HashMap
     *
     * @param item Item that needs to serialize
     * @return HashMap (its keys are fields name and its values are values of these fields)
     */
    public static HashMap<String, String> saveRock(Item item) {
        Rock rock = (Rock) item;
        HashMap<String, String> fields = new HashMap<>();
        fields.put("weightOfOre", String.valueOf(rock.getWeightOfOre()));
        fields.put("weightOfStone", String.valueOf(rock.getWeightOfStone()));
        fields.put("itemName", String.valueOf(rock.getName()));
        try {
            Field count = Rock.class.getDeclaredField("countOfPieces");
            count.setAccessible(true);
            Object value = count.get(rock);
            fields.put("countOfPieces", value.toString());
        } catch (NoSuchFieldException ex) {

        } catch (IllegalAccessException ex) {

        }

        return fields;
    }

    /**
     * Receives attributes contained in HashMap and create new {@link lab.objects.items.rock.Rock.Piece}
     *
     * @param fields HashMap (its keys are fields name and its values are values of these fields)
     * @return new {@link lab.objects.items.rock.Rock.Piece} object
     */
    public static Rock.Piece createPiece(HashMap<String, String> fields) {
        return new Rock.Piece(Float.valueOf(fields.get("weight")), fields.get("itemName"));
    }

    /**
     * Serializes {@link lab.objects.items.rock.Rock.Piece} and returns its attributes contained in HashMap
     *
     * @param item Item that needs to serialize
     * @return HashMap (its keys are fields name and its values are values of these fields)
     */
    public static HashMap<String, String> savePiece(Item item) {
        Rock.Piece piece = (Rock.Piece) item;
        HashMap<String, String> fields = new HashMap<>();
        fields.put("weight", String.valueOf(piece.getWeight()));
        fields.put("itemName", String.valueOf(piece.getName()));
        return fields;
    }

    /**
     * Receives attributes contained in HashMap and create new {@link lab.objects.items.miningInstruments.MiningInstrument}
     *
     * @param fields HashMap (its keys are fields name and its values are values of these fields)
     * @return new {@link lab.objects.items.miningInstruments.MiningInstrument} object
     */
    public static MiningInstrument createMiningInstrument(HashMap<String, String> fields) {
        return new MiningInstrument(Float.valueOf(fields.get("powerCoefficient")), fields.get("itemName"));
    }

    /**
     * Serializes {@link lab.objects.items.miningInstruments.MiningInstrument} and returns its attributes contained in HashMap
     *
     * @param item Item that needs to serialize
     * @return HashMap (its keys are fields name and its values are values of these fields)
     */
    public static HashMap<String, String> saveMiningInstrument(Item item) {
        MiningInstrument instrument = (MiningInstrument) item;
        HashMap<String, String> fields = new HashMap<>();
        fields.put("powerCoefficient", String.valueOf(instrument.getPowerCoefficient()));
        fields.put("itemName", String.valueOf(instrument.getName()));
        return fields;
    }

    /**
     * Receives attributes contained in HashMap and create new {@link lab.objects.items.miningInstruments.Hammer}
     *
     * @param fields HashMap (its keys are fields name and its values are values of these fields)
     * @return new {@link lab.objects.items.miningInstruments.Hammer} object
     */
    public static Hammer createHammer(HashMap<String, String> fields) {
        return new Hammer(fields.get("itemName"), MiningInstrument.Material.fromStringToMaterial(fields.get("head")), MiningInstrument.Material.fromStringToMaterial(fields.get("handle")));
    }

    /**
     * Serializes {@link lab.objects.items.miningInstruments.Hammer} and returns its attributes contained in HashMap
     *
     * @param item Item that needs to serialize
     * @return HashMap (its keys are fields name and its values are values of these fields)
     */
    public static HashMap<String, String> saveHammer(Item item) {
        Hammer hammer = (Hammer) item;
        HashMap<String, String> fields = new HashMap<>();
        fields.put("head", String.valueOf(hammer.getHeadMaterialName()));
        fields.put("handle", String.valueOf(hammer.getHandleMaterialName()));
        fields.put("itemName", String.valueOf(hammer.getName()));
        return fields;
    }

}
