package nl.han.project.skilltree.datasource.util;

import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.util.Properties;

public class DatabaseProperties {

    private final Properties properties;

    public DatabaseProperties() {
        properties = new Properties();

        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("database.properties"));
            Class.forName(this.driver());
        }
        catch(IOException | ClassNotFoundException e){
            throw new InternalServerErrorException("Could not load database.properties");
        }
    }

    public String driver(){
        return properties.getProperty("driver");
    }
    public String connString(){
        return properties.getProperty("conn");
    }
}
