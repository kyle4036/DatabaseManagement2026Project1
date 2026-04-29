package travel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DBConnection {

    private Connection con = null;
    private Statement stmt = null;

    private Connection get() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/testproject", "testuser", "abc123");
    }

    public void initialize() throws SQLException{
        try{
            con = get();
            stmt = con.createStatement();
        }catch(SQLException e) {
            System.out.println("Unable to create a connection to the database");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public Connection getConnection(){
        return con;
    }
    public Statement getStatement(){
        return stmt;
    }

    public static void main(String[] args) {
        DBConnection dbc = new DBConnection();

        try{
            dbc.initialize()
            Connection conn = dbc.getConnection();
            System.out.println("Connected: " + conn.getMetaData().getDatabaseProductVersion());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
}
