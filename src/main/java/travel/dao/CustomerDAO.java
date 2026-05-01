package travel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import travel.DBConnection;
import travel.model.Customer;

public class CustomerDAO {

    private Customer mapRow(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        
        c.setCustomerID(rs.getInt("customerID"));
        c.setFirstName(rs.getString("firstName"));
        c.setLastName(rs.getString("lastName"));
        c.setUsername(rs.getString("username"));
        c.setPassword(rs.getString("password"));
        c.setEmail(rs.getString("email"));
        c.setPhoneNumber(rs.getString("phoneNumber"));
        return c;
    }

    public List<Customer> findAll() {
        String sql = "SELECT * FROM Customers";
        List<Customer> results = new ArrayList<>();
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) results.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    public Customer findByKey(int customerID) {
        String sql = "SELECT * FROM Customers WHERE customerID = ?";
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Customer findByUsername(String username) {
        String sql = "SELECT * FROM Customers WHERE username = ?";
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifyCustomer(String username, String password) {
        String sql = "SELECT * FROM Customers WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.get(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {  // If a row exists with username and password, return true, otherwise false
                if (rs.next()) {
                    return true;
                }
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(Customer c) {
        String sql = """
            INSERT INTO Customers
            (firstName, lastName, username, password, email, phoneNumber)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getFirstName());
            ps.setString(2, c.getLastName());
            ps.setString(3, c.getUsername());
            ps.setString(4, c.getPassword());
            ps.setString(5, c.getEmail());
            ps.setString(6, c.getPhoneNumber());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Customer c) {
        String sql = """
            UPDATE Customers
            SET firstName = ?, lastName = ?, username = ?,
                password = ?, email = ?, phoneNumber = ?
            WHERE customerID = ?
        """;
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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

    public void delete(int customerID) {
        String sql = "DELETE FROM Customers WHERE customerID = ?";
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
