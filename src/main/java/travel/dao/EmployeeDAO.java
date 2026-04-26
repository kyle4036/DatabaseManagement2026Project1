package main.java.travel.dao;

import main.java.travel.DBConnection;
import main.java.travel.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public void insert(Employee e) {
        String sql = """
            INSERT INTO Employees
            (employeeID, firstName, lastName, username, password, role)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection = DBConnection.get();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, e.getEmployeeID());
            ps.setString(2, e.getFirstName());
            ps.setString(3, e.getLastName());
            ps.setString(4, e.getUsername());
            ps.setString(5, e.getPassword());
            ps.setString(6, e.getRole());

            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM Employees WHERE employeeID = ?";

        try (Connection connection = DBConnection.get();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
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

        try (Connection connection = DBConnection.get();
             PreparedStatement ps = connection.prepareStatement(sql)) {

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
}