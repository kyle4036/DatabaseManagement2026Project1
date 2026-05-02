package travel.dao;

import java.sql.*;

import travel.DBConnection;
import travel.model.Airport;

public class AirportDAO {
  
    private Connection connection = null;


    private Airport mapRow(ResultSet rs) throws SQLException {
        Airport airport = new Airport();
        
        airport.setPortID(rs.getString("portID"));
        airport.setName(rs.getString("name"));
        airport.setCity(rs.getString("city"));
        airport.setCountry(rs.getString("country"));

        return airport;
    }

    public void insert(Airport airport){
        String sql = """
            INSERT INTO Airports
            (portID, name, city, country)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, airport.getPortID());
            ps.setString(2, airport.getName());
            ps.setString(3, airport.getCity());
            ps.setString(4, airport.getCountry());

            ps.executeUpdate();

        } catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public void delete(String portID){
        String sql = "DELETE FROM Airports WHERE portID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, portID);
            
            ps.executeUpdate();

        } catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public void update(Airport airport){
        String sql = """
            UPDATE Airports
            SET name = ?, city = ?, country = ?
            WHERE portID = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, airport.getName());
            ps.setString(2, airport.getCity());
            ps.setString(3, airport.getCountry());            
            ps.setString(4, airport.getPortID());

            ps.executeUpdate();

        } catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

}
