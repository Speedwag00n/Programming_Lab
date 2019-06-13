package lab.client.mvc.model;

import lab.client.settings.Settings;
import lab.util.commands.InvalidArgumentsException;

import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsModel extends Model {

    ResourceBundle resources = ResourceBundle.getBundle("resources.lang.lang", Settings.getLocale());

    public int getServerPort() {
        return Settings.getServerPort();
    }

    public void applyLanguage(String value) {
        String[] langAndCountry = value.split("_");
        Locale locale = new Locale(langAndCountry[0], langAndCountry[1]);
        Settings.setLocale(locale);
    }

    public void applyPort(String port) throws InvalidArgumentsException {
        int serverPort = Integer.parseInt(port);
        if (serverPort < 0)
            throw new InvalidArgumentsException(resources.getString("settings_negative_port"));
        if (serverPort > 65535)
            throw new InvalidArgumentsException(resources.getString("settings_to_big_port"));
        Settings.setServerPort(serverPort);
    }

}
