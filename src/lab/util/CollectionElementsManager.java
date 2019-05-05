package lab.util;

import lab.locations.Location;
import lab.server.start.ResponseBuilder;
import lab.util.commands.Commands;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

/**
 * This class allows to manage collection.
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.0.0
 */
public class CollectionElementsManager {

    private ConcurrentLinkedDeque<Location> collection = new ConcurrentLinkedDeque<>();
    private String mainFile;
    private Date initDate;
    private Connection databaseConnection;
    
    private static ArrayList<String> itemsDescription = new ArrayList<>();
    static
    {
        itemsDescription.add("Скала. Class: \"Rock\". Аттрибуты: \"weightOfOre\" : Float, \"weightOfStone\" : Float, \"itemName\" : String");
        itemsDescription.add("Кусок. Class: \"Piece\". Аттрибуты: \"weight\" : Float,  \"itemName\" : String");
        itemsDescription.add("Горнодобывающий инструмент. Class: \"MiningInstrument\". Аттрибуты: \"powerCoefficient\" : Float,  \"itemName\" : String");
        itemsDescription.add("Молоток. Class: \"Hammer\". Аттрибуты: \"head\" : Material, \"handle\" : Material, \"itemName\" : String \n" +
                "Возможные значения Material: Steel, Iron, Stone, Wood, Plastic");
    }

    /**
     * Single constructor of CollectionElementsManager
     * @param aMainFile Way to file with XML code from which program then it start takes data for initialize collection. Also this file is used to save collection then user put command save or exit
     */
    public CollectionElementsManager(String aMainFile){
        mainFile = aMainFile;
        initDate = new Date();
        CollectionElementsBuilder.fromFile(mainFile, collection);
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            databaseConnection = DriverManager.getConnection("jdbc:postgresql://localhost/postgres?user=test&password=123");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private boolean synchronizeCollection (){
        collection.clear();
        return CollectionElementsBuilder.fromFile(mainFile, collection);
    }

    /**
     * Adds element to the collection
     * @param location Element that needs to add to the collection
     * @return Returns false if element equals null else it is specified by {@link LinkedList}
     */
    public boolean addElementInCollection(Location location, ResponseBuilder response){
        synchronized (collection){
            synchronizeCollection();
            boolean result = false;
            if (location != null) {
                result = collection.add(location);
                response.append("Элемент был успешно добавлен.");
            }
            else
                response.append("Элемент пуст.");
            saveCollection(response);
            return result;
        }
    }

    /**
     * Removes element from the collection
     * @param location Element that needs to remove from the collection
     * @return Return false if element equals null else it's specified by {@link LinkedList}
     */
    public boolean removeElementInCollection(Location location, ResponseBuilder response) {
        synchronized (collection) {
            synchronizeCollection();
            boolean result = false;
            if (location != null) {
                if (result = collection.remove(location))
                    response.append("Элемент удален.");
                else
                    response.append("Данного элемента нет в коллекции");
            } else
                response.append("Элемент пуст.");
            saveCollection(response);
            return result;
        }
    }

    /**
     * Gets maximum element from collection
     * @return It's specified by {@link Collections}
     */
    public Location getMaxElement(){
        synchronized (collection) {
            synchronizeCollection();
            return collection.stream().max(Comparator.naturalOrder()).get();
        }
    }

    /**
     * Gets minimum element from collection
     * @return It's specified by {@link Collections}
     */
    public Location getMinElement(){
        synchronized (collection) {
            synchronizeCollection();
            return collection.stream().min(Comparator.naturalOrder()).get();
        }
    }

    /**
     * Removes elements that are greater than element that is param
     * @param location Element with witch compare elements from the collection
     * @return Return count of removed elements
     */
    public int removeGreater(Location location, ResponseBuilder response) {
        synchronized (collection) {
            synchronizeCollection();
            int count = 0;
            if (location != null) {
                count = (int) collection.stream().filter((element) -> location.compareTo(element) < 0).count();
                collection = collection.stream().filter((element) -> location.compareTo(element) >= 0).collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
            } else
                response.append("Элемент пуст.");
            response.append("Было удалено " + count + " элементов.");
            saveCollection(response);
            return count;
        }
    }

    /**
     * Removes elements that are lower than element that is param
     * @param location Element with witch compare elements from the collection
     * @return Return count of removed elements
     */
    public int removeLower(Location location, ResponseBuilder response){
        synchronized (collection) {
            synchronizeCollection();
            int count = 0;
            if (location != null) {
                count = (int) collection.stream().filter((element) -> location.compareTo(element) > 0).count();
                collection = collection.stream().filter((element) -> location.compareTo(element) <= 0).collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
            } else
                response.append("Элемент пуст.");
            response.append("Было удалено " + count + " элементов.");
            saveCollection(response);
            return count;
        }
    }

    public boolean addIfMax(Location location, ResponseBuilder response){
        synchronized (collection) {
            synchronizeCollection();
            boolean result = false;
            if (location != null) {
                if (location.compareTo(getMaxElement()) > 0) {
                    collection.add(location);
                    response.append("Элемент оказался наибольшим и был добавлен в коллекцию.");
                    return true;
                } else {
                    response.append("В коллекции присутствуют элементы больше введенного.");
                    return false;
                }
            } else
                response.append("Элемент пуст.");
            return false;
        }
    }

    public int importObjects(Collection<Location> locations, ResponseBuilder response){
        synchronized (collection) {
            synchronizeCollection();
            int count = (int) locations.stream().filter((element) -> element != null).count();
            locations.stream().filter((element) -> element != null).forEach((element) -> collection.add(element));
            response.append("Было добавлено " + count + " обьектов.");
            saveCollection(response);
            return count;
        }
    }

    public boolean register(String argument, ResponseBuilder response){
        String[] loginAndEmail = argument.split(" ", 2);
        if (loginAndEmail.length == 2 && Commands.isLoginValid(loginAndEmail[0].trim()) && Commands.isEmailValid(loginAndEmail[1].trim())){
            Statement statement;
            try {
                statement = databaseConnection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT UserID FROM Users WHERE Login LIKE '" + loginAndEmail[0].trim() + "'");
                if (rs.next()){
                    response.append("Не удалось зарегистрировать нового пользователя. Логин уже занят.");
                    return false;
                }
                rs = statement.executeQuery("SELECT UserID FROM Users WHERE Email LIKE '" + loginAndEmail[1].trim() + "'");
                if (rs.next()){
                    response.append("Не удалось зарегистрировать нового пользователя. Данный почтовый адрес уже используется.");
                    return false;
                }
                statement.executeUpdate("INSERT INTO Users (Login, Email, Password) VALUES ('" + loginAndEmail[0].trim() + "', '" + loginAndEmail[1].trim() + "', '110022')");
                response.append("Новый пользователь зарегистрирован, сгенерированный пароль выслан на указанную почту.");
                return true;
            } catch (SQLException e) {

            }
        }
        response.append("Не удалось зарегистрировать нового пользователя.");
        return false;
    }

    public boolean login(String argument, ResponseBuilder response){
        String[] loginAndPassword = argument.split(" ", 2);
        if (loginAndPassword.length == 2 && Commands.isLoginValid(loginAndPassword[0].trim()) && Commands.isPasswordValid(loginAndPassword[1].trim())){
            Statement statement;
            try {
                statement = databaseConnection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT UserID FROM Users WHERE Login LIKE '" + loginAndPassword[0].trim() + "' AND Password LIKE '" + loginAndPassword[1].trim() + "'");
                if (rs.next()){
                    response.append("Авторизация прошла успешно.");
                    return true;
                }
            } catch (SQLException e) {

            }
        }
        response.append("Авторизация не удалась. Проверьте правильность введенного логина и пароля.");
        return false;
    }

    /**
     * Takes keyword and show information. In this version there're accessible keyword: <br>
     * - add - shows information about command "add". <br>
     * - element - shows format of element that needs to use certain command. <br>
     * - items - shows information about supported elements in this version of program. <br>
     * - remove_greater - shows information about command "remove_greater". <br>
     * - show - shows information about command "show". <br>
     * - info - shows information about command "info". <br>
     * - remove_lower - shows information about command "remove_lower". <br>
     * - remove - shows information about command "remove". <br>
     * - add_if_max - shows information about command "add_if_max". <br>
     * - exit - shows information about command "exit". <br>
     * @param word Keyword that represents section of command "help"
     */
    public boolean help(String word, ResponseBuilder response){
        switch (word.trim()){
            case "add":
                response.append("Команда \"add {element}\" добавляет новый элемент в коллекцию. Подробнее о формате {element} \n смотрите по команде \"help element\"");
                break;
            case "element":
                response.append("{element} - это описание локации в формате json. \n" +
                        "Её структура: {\"locationName\" : \"Имя локации\", \"area\" : \"Площадь\", \"position\" : [{ \"X\" : \"X левой нижней точки\", \"Y\" : \"Y левой нижней точки\" }, { \"X\" : \"X правой верхней точки\", \"Y\" : \"Y правой верхней точки\" }], " +
                        "\"items\" : [ {\"class\" : \"Класс n-го предмета в локации\", \"attributes\" : [{\"Название i-го аттрибута n-го элемента\" : \"Значение i-го аттрибута n-го элемента\" , ... }]}, ... ] } \n" +
                        "Подробнее о существующих элементах смотрите по команде \"help items\"");
                break;
            case "items":
                response.append("В данной версии программы доступно " + itemsDescription.size() + " предметов: \n ");
                for (String s : itemsDescription)
                    response.append(s);
                break;
            case "remove_greater":
                response.append("Команда \"remove_greater {element}\" удаляет из коллекции все элементы, превышающие заданный. Подробнее о формате {element} \n смотрите по команде \"help element\"");
                break;
            case "show":
                response.append("Команда \"show\" выводит в стандартный поток вывода все элементы коллекции в строковом представлении");
                break;
            case "info":
                response.append("Команда \"info\" выводит в стандартный поток вывода информацию о коллекции");
                break;
            case "remove_lower":
                response.append("Команда \"remove_lower {element}\" удаляет из коллекции все элементы, превышающие заданный. Подробнее о формате {element} \n смотрите по команде \"help element\"");
                break;
            case "remove":
                response.append("Команда \"remove {element}\" удаляет элемент из коллекции по его значению. Подробнее о формате {element} \n смотрите по команде \"help element\"");
                break;
            case "add_if_max":
                response.append("Команда \"add_if_max {element}\" добавляет новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции. Подробнее о формате {element} \n смотрите по команде \"help element\"");
                break;
            case "exit":
                response.append("Команда \"exit\" сохраняет текущее состояние коллекции в стандартный файл и завершает работу программы.");
                break;
            case "help":
                response.append("Команда \"help команда\" выводит информацию о доступых командах (remove, show, add_if_max, remove_greater, remove_lower, info, add, exit, register, login)");
                break;
            case "register":
                response.append("Команда \"help register\" используется для создания новых аккаунтов. Формат команды: \"register логин почта\".");
                response.append("Если логин и почта ещё не были использованы для создани других аккаунтов, программа создаст новый аккаунт и пришлёт сгенерированный пароль на указанную почту.");
                response.append("Обратите внимание, что логин и почта (часть до домена) не должны содержать никаких других символов, кроме латинских букв a-z, A-Z и цифр 0-9.");
                break;
            case "login":
                response.append("Команда \"help login\" используется для входа в систему, который необходим для последующей работы с объектами в базе данных.");
                response.append("Формат команды: \"login логин пароль\"");
                response.append("Обратите внимание, что логин и пароль не должны содержать никаких других символов, кроме латинских букв a-z, A-Z и цифр 0-9.");
                break;
                default:
                    response.append("Неизвестная команда. Подробнее о доступных командах смотрите по команде \"help help\"");
                    return false;
        }
        return true;
    }

    /**
     * Shows in standard output stream information about the collection, its date of initialization and its size.
     */
    public boolean info(ResponseBuilder response){
        synchronized (collection) {
            synchronizeCollection();
            SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy, в hh:mm:ss");
            response.append("Программа хранит объекты класса Location, содержащие классы Item, в коллекции типа ConcurrentLinkedDeque");
            response.append("Коллекция была загружена " + formater.format(initDate) + ". Сейчас она содержит " + collection.size() + " элементов.");
            return true;
        }
    }

    /**
     * Shows in standard output stream all elements from the collection like a Strings
     */
    public boolean show(ResponseBuilder response) {
        synchronized (collection) {
            synchronizeCollection();
            if (!collection.isEmpty()) {
                collection.stream().forEach((Location location) -> {
                    response.append(location.toString());
                    response.append("Предметы в локации:");
                    location.getItems().stream().forEach((item) -> response.append(item.toString()));
                });
                return true;
            }
            else {
                response.append("Коллекция пуста.");
                return false;
            }
        }
    }

    /**
     * Serializes collection to XML code and tries to save it to standard file. If program can't do that it tries to create new file and make it new main file. If program can't do that again collection won't saving
     * @return Return true if saving passed successfully or return false if collection didn't save to any file
     */
    public boolean saveCollection(ResponseBuilder response){
        try(BufferedOutputStream bufferedStream = new BufferedOutputStream(new FileOutputStream(mainFile))){
            bufferedStream.write(CollectionElementsBuilder.collectionToXML(collection).getBytes());
            bufferedStream.flush();
            response.append("Коллекция успешно сохранена");
        }
        catch(IOException | NullPointerException ex){
            Date date = new Date();
            SimpleDateFormat formater = new SimpleDateFormat("dd MM yyyy hh mm ss");
            mainFile = "Lab5 " + formater.format(date) + ".txt";
            File fileFotSaving = new File(mainFile);
            try(BufferedOutputStream bufferedStream = new BufferedOutputStream(new FileOutputStream(fileFotSaving))) {
                bufferedStream.write(CollectionElementsBuilder.collectionToXML(collection).getBytes());
                bufferedStream.flush();
                response.append("Коллекция успешно сохранена в резервный файл, так как не удалось произвести сохранение в основной файл");
            }
            catch (IOException exp){
                response.append("Не удалось сохранить коллекцию");
                return false;
            }
        }
        return true;
    }

    /**
     * Close program
     */
    public void exit(){
        System.out.println("Распознана команда завершения работы");
    }

}