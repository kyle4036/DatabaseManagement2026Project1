package travel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import travel.DBConnection;
import travel.model.Aircraft;
import travel.model.Flight;

public class FlightDAO {

    private Connection connection = null;

    public FlightDAO(DBConnection dbc) {
        connection = dbc.getConnection();
    }

    // Invisible Helper method that converts JDBC output from type ResultSet to
    // types our `Flight`
    private Flight mapRow(ResultSet rs) throws SQLException {
        Flight f = new Flight();
        
        f.setFlightNumber(rs.getString("flightNumber"));
        f.setLineID(rs.getString("lineID"));
        f.setDeparturePortID(rs.getString("departure_portID"));
        f.setDestinationPortID(rs.getString("destination_portID"));
        f.setDepartureTime(rs.getTime("departureTime").toLocalTime());
        f.setArrivalTime(rs.getTime("arrivalTime").toLocalTime());
        f.setFlightType(rs.getString("flightType"));
        f.setDaysRunning(rs.getString("daysRunning"));
        f.setSeatsTaken(rs.getInt("seatsTaken"));
        f.setCraftID(rs.getInt("craftID"));
        
        return f;
    }

    public void insert(Flight f){
        String sql = """
            INSERT INTO Flights
            (flightNumber, lineID, departure_portID, destination_portID, departureTime, arrivalTime, flightType, daysRunning, seatsTaken, craftID)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, f.getFlightNumber());
            ps.setString(2, f.getLineID());
            ps.setString(3, f.getDeparturePortID());
            ps.setString(4, f.getDestinationPortID());
            ps.setTime(5, Time.valueOf(f.getDepartureTime()));
            ps.setTime(6, Time.valueOf(f.getArrivalTime()));
            ps.setString(7, f.getFlightType());
            ps.setString(8, f.getDaysRunning());
            ps.setInt(9, f.getSeatsTaken());
            ps.setInt(10, f.getCraftID());

            ps.executeUpdate();

        } catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public void delete(String flightNumber, String lineID){
        String sql = "DELETE FROM Flights WHERE flightNumber = ? AND lineID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, flightNumber);
            ps.setString(2, lineID);
            
            ps.executeUpdate();

        } catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public void update(Flight f){
        String sql = """
            UPDATE Flights
            SET departure_portID = ?, destination_portID = ?, departureTime = ?, arrivalTime = ?, flightType = ?, daysRunning = ?, seatsTaken = ?, craftID = ?
            WHERE flightNumber = ? AND lineID = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, f.getDeparturePortID());
            ps.setString(2, f.getDestinationPortID());
            ps.setTime(3, Time.valueOf(f.getDepartureTime()));
            ps.setTime(4, Time.valueOf(f.getArrivalTime()));
            ps.setString(5, f.getFlightType());
            ps.setString(6, f.getDaysRunning());
            ps.setInt(7, f.getSeatsTaken());
            ps.setInt(8, f.getCraftID());
            ps.setString(9, f.getFlightNumber());
            ps.setString(10, f.getLineID());

            ps.executeUpdate();

        } catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    
    public List<Flight> findAll() {
        String sql = "SELECT * FROM Flights";
        
        ArrayList<Flight> results = new ArrayList<>();
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
                
            ResultSet rs = ps.executeQuery()) {

            while (rs.next())
                results.add(mapRow(rs));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    public Flight findByKey(String flightNumber, String lineID) throws SQLException {
        String sql = "SELECT * FROM Flights WHERE flightNumber = ? AND lineID = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, flightNumber);
            ps.setString(2,lineID);

            try (ResultSet rs = ps.executeQuery()){
                return mapRow(rs);
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<Flight> findByRoute(String from, String to) {
        String sql = "SELECT * FROM Flights WHERE departure_portID = ? AND destination_portID = ?";
        
        ArrayList<Flight> results = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, from);
            ps.setString(2, to);

            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    results.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return results;
    }

}
