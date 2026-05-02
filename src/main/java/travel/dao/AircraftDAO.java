package travel.dao;

import java.sql.*;

import travel.DBConnection;
import travel.model.Aircraft;

public class AircraftDAO {
  
    private Connection connection = null;

    public AircraftDAO(DBConnection dbc){
        connection = dbc.getConnection();
    }

    private Aircraft mapRow(ResultSet rs) throws SQLException {
        Aircraft a = new Aircraft();
        
        a.setCraftID(rs.getInt("craftID"));
        a.setLineID(rs.getString("lineID"));
        a.setPortID(rs.getString("portID"));
        a.setCapacity(rs.getInt("capacity"));
        
        return a;
    }

    public void insert(Aircraft a){
        String sql = """
            INSERT INTO Aircrafts
            (craftID, lineID, portID, capacity)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, a.getCraftID());
            ps.setString(2, a.getLineID());
            ps.setString(3, a.getPortID());
            ps.setInt(4, a.getCapacity());

            ps.executeUpdate();

        } catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public void delete(int craftID){
        String sql = "DELETE FROM Aircrafts WHERE craftID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, craftID);
            
            ps.executeUpdate();

        } catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public void update(Aircraft a){
        String sql = """
            UPDATE Aircrafts
            SET lineID = ?, portID = ?, capacity = ?
            WHERE craftID = ?a
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, a.getLineID());
            ps.setString(2, a.getPortID());
            ps.setInt(3, a.getCapacity());
            ps.setInt(4, a.getCraftID());

            ps.executeUpdate();

        } catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

}
