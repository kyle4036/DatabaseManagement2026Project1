package travel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import travel.DBConnection;
import travel.model.Flight;

public class FlightDAO {

    // Invisible Helper method that converts JDBC output from type ResultSet to
    // types our `Flight`
    private Flight mapRow(ResultSet rs) throws SQLException {
        Flight f = new Flight();
        f.setFlightNumber(Integer.parseInt(rs.getString("flightNumber")));
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

    public List<Flight> findAll() {
        String sql = "SELECT * FROM Flights";
        ArrayList<Flight> results = new ArrayList<>();
        try (Connection conn = DBConnection.get();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next())
                results.add(mapRow(rs));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    public Flight findByKey(String flightNumber, String lineID) {
        return new Flight();
    }

    public List<Flight> findByRoute(String from, String to) {
        return new ArrayList<>();
    }

    public void insert(Flight f) {
    }

    public void update(Flight f) {
    }

    public void delete(String flightNumber, String lineID) {
    }
}
