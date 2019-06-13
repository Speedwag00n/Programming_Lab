package lab.client.settings;

import java.util.Locale;

public class Settings {

    private static Locale locale = new Locale("en", "EN");
    private static String token;
    private static String username;
    private static int serverPort = 12345;

    public static Locale getLocale(){
        return locale;
    }

    public static void setLocale(Locale newLocale) {
        locale = newLocale;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String newToken) {
        token = newToken;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String newUsername) {
        username = newUsername;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static void setServerPort(int port) {
        serverPort = port;
    }

}
