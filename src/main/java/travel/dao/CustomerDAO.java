package travel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import travel.DBConnection;
import travel.model.Customer;


public class CustomerDAO {
    
    private Connection connection = null;
    private Customer custAcc = null;

    public CustomerDAO(DBConnection dbc){
        connection = dbc.getConnection();
    }

    private Customer mapRow(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setCustomerID(rs.getInt("customerID"));
        c.setEmail(rs.getString("email"));
        c.setFirstName(rs.getString("firstName"));
        c.setLastName(rs.getString("lastName"));
        c.setPassword(rs.getString("password"));
        c.setPhoneNumber(rs.getString("phoneNumber"));
        c.setUsername(rs.getString("username"));
        return c;
    }



    public void insert(Customer c) {
          String sql = """
                INSERT INTO Customers 
                (customerID, firstName, lastName, username, password, email, phoneNumber)
                VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, c.getCustomerID());
                ps.setString(2, c.getFirstName());
                ps.setString(3, c.getLastName());
                ps.setString(4, c.getUsername());
                ps.setString(5, c.getPassword());
                ps.setString(6, c.getEmail());
                ps.setString(7, c.getPhoneNumber());

                ps.executeUpdate(); 

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    
    public void delete(int id) {
        String sql =  "DELETE FROM Customers C WHERE C.customerID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //potential sysout for "No customer found with ID" 
    }

    public void update(Customer c) {
        String sql = """
            UPDATE Customers
            SET firstName = ?, lastName = ?, username = ?, 
                password = ?, email = ?, phoneNumber = ?
            WHERE customerID = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, c.getFirstName());
            ps.setString(2, c.getLastName());
            ps.setString(3, c.getUsername());
            ps.setString(4, c.getPassword());
            ps.setString(5, c.getEmail());
            ps.setString(6, c.getPhoneNumber());
            ps.setInt(7, c.getCustomerID());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Customer getLastVerifiedCustomer(){
        return custAcc;
    }

    public boolean verifyCustomer(String username, String password) {
        String sql = "SELECT 1 FROM Customers WHERE username = ? AND password = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                custAcc = mapRow(rs);
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception{
        DBConnection dbc = new DBConnection();
        try{dbc.initialize();}catch(Exception e){throw e;}
        CustomerDAO CDao = new CustomerDAO(dbc);
        boolean v = CDao.verifyCustomer("user1", "pass1");
        if(v)
            System.out.println("user exists!");
        else
            System.out.println("user doesn't exist!");

        System.exit(0);
    }

}

