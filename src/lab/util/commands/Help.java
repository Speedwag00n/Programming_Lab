package lab.util.commands;

import lab.server.response.Logger;

import java.util.ArrayList;

/**
 * This command allows to receive description of program client side work.
 *
 * @author Nemankov Ilia
 * @version 1.0.0
 * @since 1.7.0
 */
public class Help extends ClientCommand {

    private static ArrayList<String> itemsDescription = new ArrayList<>();

    static {
        itemsDescription.add("Скала. Class: \"Rock\". Аттрибуты: \"weightOfOre\" : Float, \"weightOfStone\" : Float, \"itemName\" : String");
        itemsDescription.add("Кусок. Class: \"Piece\". Аттрибуты: \"weight\" : Float,  \"itemName\" : String");
        itemsDescription.add("Горнодобывающий инструмент. Class: \"MiningInstrument\". Аттрибуты: \"powerCoefficient\" : Float,  \"itemName\" : String");
        itemsDescription.add("Молоток. Class: \"Hammer\". Аттрибуты: \"head\" : Material, \"handle\" : Material, \"itemName\" : String \n" +
                "Возможные значения Material: Steel, Iron, Stone, Wood, Plastic");
    }

    public Help(String argument) {
        super(argument);
    }

    @Override
    public Commands.CommandExecutionStatus execute() {
        Logger logger = getLogger();
        switch (getArgument()) {
            case "add":
                logger.append("Команда \"add логин пароль {element}\" добавляет новый элемент в коллекцию. Подробнее о формате {element} смотрите по команде \"help element\"");
                logger.append("Поробнее о формате логина и пароля можно узнать с помощью команды \"help auth\".");
                break;
            case "element":
                logger.append("{element} - это описание локации в формате json. \n" +
                        "Её структура: {\"locationName\" : \"Имя локации\", \"area\" : \"Площадь\", \"position\" : [{ \"X\" : \"X левой нижней точки\", \"Y\" : \"Y левой нижней точки\" }, { \"X\" : \"X правой верхней точки\", \"Y\" : \"Y правой верхней точки\" }], " +
                        "\"items\" : [ {\"class\" : \"Класс n-го предмета в локации\", \"attributes\" : [{\"Название i-го аттрибута n-го элемента\" : \"Значение i-го аттрибута n-го элемента\" , ... }]}, ... ] } \n" +
                        "Подробнее о существующих элементах смотрите по команде \"help items\"");
                break;
            case "items":
                logger.append("В данной версии программы доступно " + itemsDescription.size() + " предметов: \n ");
                for (String s : itemsDescription)
                    logger.append(s);
                break;
            case "remove_greater":
                logger.append("Команда \"remove_greater логин пароль {element}\" удаляет из коллекции все элементы, превышающие заданный. Подробнее о формате {element} смотрите по команде \"help element\"");
                logger.append("Поробнее о формате логина и пароля можно узнать с помощью команды \"help auth\".");
                break;
            case "show":
                logger.append("Команда \"show логин пароль\" выводит в стандартный поток вывода все элементы коллекции в строковом представлении");
                logger.append("Поробнее о формате логина и пароля можно узнать с помощью команды \"help auth\".");
                break;
            case "info":
                logger.append("Команда \"info логин пароль\" выводит в стандартный поток вывода информацию о коллекции");
                logger.append("Поробнее о формате логина и пароля можно узнать с помощью команды \"help auth\".");
                break;
            case "remove_lower":
                logger.append("Команда \"remove_lower логин пароль {element}\" удаляет из коллекции все элементы, превышающие заданный. Подробнее о формате {element} смотрите по команде \"help element\"");
                logger.append("Поробнее о формате логина и пароля можно узнать с помощью команды \"help auth\".");
                break;
            case "remove":
                logger.append("Команда \"remove логин пароль {element}\" удаляет элемент из коллекции по его значению. Подробнее о формате {element} \n смотрите по команде \"help element\"");
                logger.append("Поробнее о формате логина и пароля можно узнать с помощью команды \"help auth\".");
                break;
            case "add_if_max":
                logger.append("Команда \"add_if_max логин пароль {element}\" добавляет новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции. Подробнее о формате {element} \n смотрите по команде \"help element\"");
                logger.append("Поробнее о формате логина и пароля можно узнать с помощью команды \"help auth\".");
                break;
            case "exit":
                logger.append("Команда \"exit\" завершает работу программы.");
                break;
            case "":
                logger.append("Команда \"help команда\" выводит информацию о доступых командах (remove, show, add_if_max, remove_greater, remove_lower, info, add, exit, register).");
                logger.append("Также команда позволяет узнать о подробностях ее работы (element, items, auth).");
                break;
            case "auth":
                logger.append("Логин, используемый в программе должен быть не длиннее, чем 30 символов, и не содержать никаких других символов, кроме латинских букв a-z, A-Z и цифр 0-9.");
                logger.append("Пароль, используемый в программе должен быть 16-ти значным и не содержать никаких других символов, кроме латинских букв a-z, A-Z и цифр 0-9.");
                logger.append("Учитывайте, что пароль, генерируемый программой и отправляемый на указанную почту при регистрации, уже соответствует необходимым требованиям.");
                break;
            case "register":
                logger.append("Команда \"register\" используется для создания новых аккаунтов. Формат команды: \"register логин почта\".");
                logger.append("Если логин и почта ещё не были использованы для создани других аккаунтов, программа создаст новый аккаунт и пришлёт сгенерированный пароль на указанную почту.");
                logger.append("Обратите внимание, что почта не должна содержать никаких других символов, кроме латинских букв a-z, A-Z и цифр 0-9, а также символов \'.\', \'-\', \'_\'.");
                logger.append("Поробнее о формате логина можно узнать с помощью команды \"help auth\".");
                break;
            default:
                logger.append("Неизвестная команда. Подробнее о доступных командах смотрите по команде \"help\"");
                return Commands.CommandExecutionStatus.CLIENT_NOT_SUCCESSFUL;
        }
        return Commands.CommandExecutionStatus.CLIENT_SUCCESSFUL;
    }

    @Override
    public int getCode() {
        return Commands.getCommandCode("help");
    }

}
