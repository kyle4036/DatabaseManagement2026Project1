package travel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import travel.DBConnection;
import travel.model.Employee;

public class EmployeeDAO {

    private Connection connection = null;

    public EmployeeDAO(DBConnection dbc){
        connection = dbc.getConnection();
    }

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

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

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

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

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

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

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

    public boolean verifyEmployee(String username, String password) {
        String sql = "SELECT 1 FROM Employees WHERE username = ? AND password = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}