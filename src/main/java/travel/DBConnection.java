package travel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/testproject";
    private static final String USER = "testuser";
    private static final String PASS = "abc123";

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void main(String[] args) {
        try (Connection conn = DBConnection.get()) {
            System.out.println("Connected established to " + conn.getMetaData().getDatabaseProductName()
                    + " with version " + conn.getMetaData().getDatabaseProductVersion());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
