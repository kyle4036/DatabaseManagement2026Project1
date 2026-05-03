package travel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import travel.DBConnection;
import travel.model.Ticket;

public class TicketDAO {
    
    private Ticket mapRow(ResultSet rs) throws SQLException {
        Ticket t = new Ticket();
        
        t.setTicketNumber(rs.getInt("ticketNumber"));
        t.setCustomerID(rs.getInt("customerID"));
        t.setPurchaseTime(rs.getTimestamp("purchaseTime").toLocalDateTime());
        t.setBookingFee(rs.getBigDecimal("bookingFee"));
        t.setFareCost(rs.getBigDecimal("fareCost"));
        t.setTripType(rs.getString("tripType"));
        t.setStatus(rs.getString("status"));
        
        return t;
    }

    public List<Ticket> findAll(int customerID) {
        String sql = "SELECT * FROM Tickets WHERE customerID = ?";
        
        List<Ticket> results = new ArrayList<>();
        
        try (Connection conn = DBConnection.get();
            PreparedStatement ps = conn.prepareStatement(sql)){
             
            ps.setInt(1, customerID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) results.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    public Ticket findByKey(int ticketNumber) {
        String sql = "SELECT * FROM Tickets WHERE ticketNumber = ?";
        
        try (Connection conn = DBConnection.get();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, ticketNumber);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }    

    public void insert(Ticket t) {
        String sql = """
            INSERT INTO Tickets
            (customerID, purchaseTime, bookingFee, fareCost, tripType, status)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.get();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getCustomerID());
            ps.setTimestamp(2, Timestamp.valueOf(t.getPurchaseTime()));
            ps.setBigDecimal(3, t.getBookingFee());
            ps.setBigDecimal(4, t.getFareCost());
            ps.setString(5, t.getTripType());
            ps.setString(6, t.getStatus());
            
            ps.executeUpdate();
        
            try (ResultSet rs = ps.getGeneratedKeys()){
                if (rs.next()){
                    t.setTicketNumber(rs.getInt(1));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Ticket t) {
        String sql = """
            UPDATE Tickets
            SET customerID = ?, purchaseTime = ?,
                bookingFee = ?, fareCost = ?, tripType = ?, status = ?
            WHERE ticketNumber = ?
        """;

        try (Connection conn = DBConnection.get();
            
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, t.getCustomerID());
            ps.setTimestamp(2, Timestamp.valueOf(t.getPurchaseTime()));
            ps.setBigDecimal(3, t.getBookingFee());
            ps.setBigDecimal(4, t.getFareCost());
            ps.setString(5, t.getTripType());
            ps.setString(6, t.getStatus());
            ps.setInt(7, t.getTicketNumber());

            ps.executeUpdate();
        
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cancel(int ticketNumber) {
        String sql = """
            UPDATE Tickets
            SET status = 'CANCELLED'
            WHERE ticketNumber = ?
        """;

        try (Connection conn = DBConnection.get();
            
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, ticketNumber);

            ps.executeUpdate();
        
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int ticketNumber) {
        String sql = "DELETE FROM Tickets WHERE ticketNumber = ?";
        
        try (Connection conn = DBConnection.get();
            
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, ticketNumber);
            
            ps.executeUpdate();
        
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        TicketDAO td = new TicketDAO();
        
        td.insert(new Ticket(
            5, 
            1, 
            LocalDateTime.now(), 
            new BigDecimal("19.99"),
            new BigDecimal("19.99"),
            "One-way",
            "1"));
        System.out.println("success maybe");
    }
}
