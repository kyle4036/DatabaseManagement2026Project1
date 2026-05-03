package travel.dao;

import java.sql.*;

import travel.DBConnection;
import travel.model.Aircraft;

public class AircraftDAO {
  
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
            (lineID, portID, capacity, model)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.get();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, aircraft.getLineID());
            ps.setString(2, aircraft.getPortID());
            ps.setInt(3, aircraft.getCapacity());
            ps.setString(4, aircraft.getModel());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()){
                if (rs.next()){
                    aircraft.setCraftID(rs.getInt(1));
                }
            }

        } catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public void delete(int craftID){
        String sql = "DELETE FROM Aircrafts WHERE craftID = ?";

        try (Connection conn = DBConnection.get();
            
            PreparedStatement ps = conn.prepareStatement(sql)) {

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

        try (Connection conn = DBConnection.get();
            
            PreparedStatement ps = conn.prepareStatement(sql)) {

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
