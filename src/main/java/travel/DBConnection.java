package main.java.travel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection get() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/testproject", "testuser", "abc123");
    }

    public static void main(String[] args) {
        try (Connection conn = get()) {
            System.out.println("Connected: " + conn.getMetaData().getDatabaseProductVersion());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
