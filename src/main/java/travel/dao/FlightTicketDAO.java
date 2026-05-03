package travel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import travel.DBConnection;
import travel.model.FlightTicket;

public class FlightTicketDAO {
    
    private FlightTicket mapRow(ResultSet rs) throws SQLException {
        FlightTicket ft = new FlightTicket();
        
        ft.setTicketNumber(rs.getInt("ticketNumber"));
        ft.setFlightNumber(rs.getInt("flightNumber"));
        ft.setLineID(rs.getString("lineID"));
        ft.setLegOrder(rs.getInt("legOrder"));
        ft.setDepartureDate(rs.getDate("departureDate").toLocalDate());
        ft.setSeatNumber(rs.getString("seatNumber"));
        ft.setTicketClass(rs.getString("ticketClass"));
        ft.setMealOrder(rs.getString("mealOrder"));
        
        return ft;
    }

    public List<FlightTicket> findByFlight(int flightNumber) {
        String sql = "SELECT * FROM FlightTickets WHERE flightNumber = ?";
        
        List<FlightTicket> results = new ArrayList<>();
        
        try (Connection conn = DBConnection.get();
            PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setInt(1, flightNumber);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) results.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    public List<FlightTicket> findByTicket(int ticketNumber) {
        String sql = "SELECT * FROM FlightTickets WHERE ticketNumber = ?";
        
        List<FlightTicket> results = new ArrayList<>();
        
        try (Connection conn = DBConnection.get();
            PreparedStatement ps = conn.prepareStatement(sql)){
            
            ps.setInt(1, ticketNumber);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) results.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    public void insert(FlightTicket ft) {
        String sql = """
            INSERT INTO FlightTickets
            (ticketNumber, flightNumber, lineID, legOrder, departureDate, seatNumber, ticketClass, mealOrder)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.get();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, ft.getTicketNumber());
            ps.setInt(2, ft.getFlightNumber());
            ps.setString(3,ft.getLineID());
            ps.setInt(4, ft.getLegOrder());
            ps.setDate(5, Date.valueOf(ft.getDepartureDate()));
            ps.setString(6, ft.getSeatNumber());
            ps.setString(7, ft.getTicketClass());
            ps.setString(8, ft.getMealOrder());
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(FlightTicket ft) {
        String sql = """
            UPDATE FlightTickets
            SET flightNumber = ?, lineID = ?,
                departureDate = ?, seatNumber = ?, ticketClass = ?, mealOrder = ?
            WHERE ticketNumber = ? AND legOrder = ?
        """;

        try (Connection conn = DBConnection.get();
            
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, ft.getFlightNumber());
            ps.setString(2,ft.getLineID());
            ps.setDate(3, Date.valueOf(ft.getDepartureDate()));
            ps.setString(4, ft.getSeatNumber());
            ps.setString(5, ft.getTicketClass());
            ps.setString(6, ft.getMealOrder());
            ps.setInt(7, ft.getTicketNumber());
            ps.setInt(8, ft.getLegOrder());

            ps.executeUpdate();
        
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int ticketNumber, int legOrder) {
        String sql = "DELETE FROM FlightTickets WHERE ticketNumber = ? AND legOrder = ?";
        
        try (Connection conn = DBConnection.get();
            
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, ticketNumber);
            ps.setInt(2, legOrder);
            
            ps.executeUpdate();
        
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}