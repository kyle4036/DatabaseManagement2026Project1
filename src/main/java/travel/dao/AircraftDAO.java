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
        Aircraft aircraft = new Aircraft();
        
        aircraft.setCraftID(rs.getInt("craftID"));
        aircraft.setLineID(rs.getString("lineID"));
        aircraft.setPortID(rs.getString("portID"));
        aircraft.setCapacity(rs.getInt("capacity"));
        aircraft.setModel(rs.getString("model"));
        

        return aircraft;
    }

    public void insert(Aircraft aircraft){
        String sql = """
            INSERT INTO Aircrafts
            (craftID, lineID, portID, capacity, model)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, aircraft.getCraftID());
            ps.setString(2, aircraft.getLineID());
            ps.setString(3, aircraft.getPortID());
            ps.setInt(4, aircraft.getCapacity());
            ps.setString(5, aircraft.getModel());

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

    public void update(Aircraft aircraft){
        String sql = """
            UPDATE Aircrafts
            SET lineID = ?, portID = ?, capacity = ?, model = ?
            WHERE craftID = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, aircraft.getLineID());
            ps.setString(2, aircraft.getPortID());
            ps.setInt(3, aircraft.getCapacity());
            ps.setString(4, aircraft.getModel());
            ps.setInt(5, aircraft.getCraftID());

            ps.executeUpdate();

        } catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

}
