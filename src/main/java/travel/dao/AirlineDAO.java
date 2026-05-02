package travel.dao;

import java.sql.*;

import travel.DBConnection;
import travel.model.Airline;

public class AirlineDAO {
  
    private Connection connection = null;

    public AirlineDAO(DBConnection dbc){
        connection = dbc.getConnection();
    }

    private Airline mapRow(ResultSet rs) throws SQLException {
        Airline airline = new Airline();
        
        airline.setLineID(rs.getString("lineID"));
        airline.setName(rs.getString("name"));
        
        return airline;
    }

    public void insert(Airline airline){
        String sql = """
            INSERT INTO Airlines
            (lineID, name)
            VALUES (?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, airline.getLineID());
            ps.setString(2, airline.getName());

            ps.executeUpdate();

        } catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public void delete(String lineID){
        String sql = "DELETE FROM Airlines WHERE lineID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, lineID);
            
            ps.executeUpdate();

        } catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public void update(Airline airline){
        String sql = """
            UPDATE Airlines
            SET name = ?
            WHERE lineID = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, airline.getName());
            ps.setString(2, airline.getLineID());

            ps.executeUpdate();

        } catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

}
