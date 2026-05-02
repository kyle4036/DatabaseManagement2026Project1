package travel.dao;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

import travel.DBConnection;
import travel.model.Airline;

public class AirlineDAO {

    private Airline mapRow(ResultSet rs) throws SQLException {
        Airline a = new Airline();
        a.setLineID(rs.getString("lineID"));
        a.setName(rs.getString("name"));
        return a;
    }

    public List<Airline> findAll() {
        String sql = "SELECT * FROM Airlines";
        List<Airline> results = new ArrayList<>();
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) results.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    public Airline findByKey(String lineID) {
        String sql = "SELECT * FROM Airlines WHERE lineID = ?";
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lineID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(Airline a) {
        String sql = "INSERT INTO Airlines (lineID, name) VALUES (?, ?)";
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getLineID());
            ps.setString(2, a.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Airline a) {
        String sql = "UPDATE Airlines SET name = ? WHERE lineID = ?";
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getName());
            ps.setString(2, a.getLineID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String lineID) {
        String sql = "DELETE FROM Airlines WHERE lineID = ?";
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lineID);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
