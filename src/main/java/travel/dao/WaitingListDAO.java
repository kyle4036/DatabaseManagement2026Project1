package travel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

import travel.DBConnection;
import travel.model.Ticket;
import travel.model.WaitingList;

public class WaitingListDAO {
    
    private WaitingList mapRow(ResultSet rs) throws SQLException {
        WaitingList wl = new WaitingList();
        
        wl.setCustomerID(rs.getInt("customerID"));
        wl.setFlightNumber(rs.getString("flightNumber"));
        wl.setLineID(rs.getString("lineID"));
        wl.setRequestTime(rs.getTimestamp("requestTime").toLocalDateTime());
        
        return wl;
    }

    public WaitingList findByFlightNumber(String flightNumber, String lineID) {
        String sql = "SELECT * FROM WaitingList WHERE flightNumber = ? AND lineID = ? ORDER BY requestTime";
        
        try (Connection conn = DBConnection.get();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, flightNumber);
            ps.setString(2, lineID);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public WaitingList findByCustomer(int customerID) {
        String sql = "SELECT * FROM WaitingList WHERE customerID = ? ORDER BY requestTime";
        
        try (Connection conn = DBConnection.get();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, customerID);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public WaitingList checkWaitingList(int customerID, String flightNumber, String lineID){ //Urgent: Should I keep this method or change it to return boolean?
        String sql = "SELECT * FROM WaitingList WHERE customerID = ?, flightNumber = ?, lineID = ?";


        try (Connection conn = DBConnection.get();
            
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, customerID);
            ps.setString(2, flightNumber);
            ps.setString(3, lineID);
            
            ps.executeUpdate();
        
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(WaitingList wl) {
        String sql = """
            INSERT INTO WaitingList
            (requestTIme)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.get();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, wl.getCustomerID());
            ps.setString(2, wl.getFlightNumber());
            ps.setString(3, wl.getLineID());
            ps.setTimestamp(4, Timestamp.valueOf(wl.getRequestTime()));
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int customerID, String flightNumber, String lineID) {
        String sql = "DELETE FROM WaitingList WHERE customerID = ?, flightNumber = ?, lineID = ?";
        
        try (Connection conn = DBConnection.get();
            
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, customerID);
            ps.setString(2, flightNumber);
            ps.setString(3, lineID);
            
            ps.executeUpdate();
        
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
