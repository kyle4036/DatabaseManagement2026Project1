package travel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import travel.DBConnection;
import travel.model.Employee;

public class EmployeeDAO {

    private Employee mapRow(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        
        e.setEmployeeID(rs.getInt("employeeID"));
        e.setFirstName(rs.getString("firstName"));
        e.setLastName(rs.getString("lastName"));
        e.setUsername(rs.getString("username"));
        e.setPassword(rs.getString("password"));
        e.setRole(rs.getString("role"));
        
        return e;
    }

    public List<Employee> findAll() {
        String sql = "SELECT * FROM Employees";
        List<Employee> results = new ArrayList<>();
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) results.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    public Employee findByKey(int employeeID) {
        String sql = "SELECT * FROM Employees WHERE employeeID = ?";
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Employee findByUsername(String username) {
        String sql = "SELECT * FROM Employees WHERE username = ?";
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

    public Employee findByLogin(String username, String password) {
        String sql = "SELECT * FROM Employees WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Employee> findByRole(String role) {
        String sql = "SELECT * FROM Employees WHERE role = ?";
        List<Employee> results = new ArrayList<>();
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) results.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    public void insert(Employee e) {
        String sql = """
            INSERT INTO Employees
            (firstName, lastName, username, password, role)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getFirstName());
            ps.setString(2, e.getLastName());
            ps.setString(3, e.getUsername());
            ps.setString(4, e.getPassword());
            ps.setString(5, e.getRole());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void update(Employee e) {
        String sql = """
            UPDATE Employees
            SET firstName = ?, lastName = ?, username = ?,
                password = ?, role = ?
            WHERE employeeID = ?
        """;
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getFirstName());
            ps.setString(2, e.getLastName());
            ps.setString(3, e.getUsername());
            ps.setString(4, e.getPassword());
            ps.setString(5, e.getRole());
            ps.setInt(6, e.getEmployeeID());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void delete(int employeeID) {
        String sql = "DELETE FROM Employees WHERE employeeID = ?";
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeID);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
