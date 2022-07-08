package nl.han.project.skilltree.datasource.util;

import nl.han.project.skilltree.domain.exceptions.ServerException;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private DatabaseConnection(){
        throw new IllegalStateException("Utility class");
    }

    public static Connection buildConnection() {
        try {
            DatabaseProperties prop = new DatabaseProperties();
            Class.forName(prop.driver());
            return DriverManager.getConnection(prop.connString());
        }
        catch (Exception e) {
            throw new ServerException("Could not connect to database");
        }
    }
}
