package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static Connection connection;
    private static final String url = "jdbc:mysql://localhost:3306/katadb";
    private static final String user = "kata";
    private static final String password = "kata";

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            System.out.println(e);;
        }
        return connection;
    }
}
