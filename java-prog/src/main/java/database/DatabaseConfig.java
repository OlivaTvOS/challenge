package database;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConfig {
    private static Properties properties = new Properties();

    static {
        try (FileInputStream input = new FileInputStream("local.properties")){
            properties.load(input);
        } catch (IOException e){
            System.out.println("O arquivo não existe!");
        }
    }

    public static String get(String key){
        String value = properties.getProperty(key);
        if (value == null) {
            value = System.getenv(key);
        }
        return value;
    }
}
