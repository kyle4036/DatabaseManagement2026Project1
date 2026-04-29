package travel.dao;

import travel.DBConnection;
import travel.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.*;

public class ReportDAO {
    
//wip
    private Connection conn = null;

    public ReportDao(DBConnection dbc){
        conn = dbc.getConnection();
    }
    private ReservationReportRow mapRowRes(ResultSet rs) throws SQLException {
        ReservationReportRow r = new ReservationReportRow();
        r.setTicketNumber(rs.getInt("ticketNumber"));
        r.setCustomerID(rs.getInt("customerID"));
        r.setCustomerName(rs.getString("customerName"));
        r.setFlightNumber(rs.getString("flightNumber"));
        r.setLineID(rs.getString("lineID"));
        r.setDepartureDate(rs.getDate("departureDate").toLocalDate());
        r.setSeatNumber(rs.getString("seatNumber"));
        r.setTicketClass(rs.getString("ticketClass"));
        r.setStatus(rs.getString("status"));
        r.setFareCost(rs.getBigDecimal("fareCost"));
        r.setBookingFee(rs.getBigDecimal("bookingFee"));
        r.setPurchaseTime(rs.getTimestamp("purchaseTime").toLocalDateTime());
        return r;
    }

    private RevenueSummaryRow mapRowRevSum(ResultSet rs) throws SQLException {
        RevenueSummaryRow r = new RevenueSummaryRow();
        r.setEntityID(rs.getString("entityID"));
        r.setEntityName(rs.getString("entityName")); 
        r.setEntityType(rs.getString("entityType"));
        r.setTotalRevenue(rs.getBigDecimal("totalRevenue"));
        return r;
    }

    private FlightSummaryRow mapRowFlightSum(ResultSet rs) throws SQLException {
        FlightSummaryRow r = new FlightSummaryRow();
        r.setFlightNumber(rs.getString("flightNumber"));
        r.setLineID(rs.getString("lineID"));
        r.setDestinationPortID(rs.getString("destinationPortID"));
        r.setOriginPortID(rs.getString("originPortID"));
        r.setFlightType(rs.getString("flightType"));
        return r;
    }

    private SalesReportRow mapRowSalesSum(ResultSet rs) throws SQLException {
        SalesReportRow r = new SalesReportRow();
        r.setMonth(rs.getInt("month"));
        r.setYear(rs.getInt("year"));
        r.setTicketsSold(rs.getInt("ticketsSold"));
        r.setReservationsCount(rs.getInt("reservationsCount"));
        r.setTotalFareRevenue(rs.getBigDecimal("totalFareRevenue"));
        r.setTotalBookingFees(rs.getBigDecimal("totalBookingFees"));
        r.setTotalRevenue(rs.getBigDecimal("totalRevenue"));
        return r;
    }
    
    public List<ReservationReportRow> getReservationsByFlightNumber(String flightNumber, String lineID) {
        String sql = """
                SELECT T.ticketNumber, T.customerID, CONCAT(C.firstName, ' ', C.lastName) AS customerName, FT.flightNumber,
                FT.lineID, FT.departureDate, FT.seatNumber, FT.ticketClass, T.status, T.fareCost, T.bookingFee, T.purchaseTime
                FROM Tickets T
                JOIN FlightTickets FT ON T.ticketNumber = FT.ticketNumber 
                JOIN Customers C ON T.customerID = C.customerID
                WHERE FT.flightNumber = ? AND FT.lineID = ?;
                """;
        ArrayList<ReservationReportRow> results = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, flightNumber);
                ps.setString(2, lineID);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next())
                        results.add(mapRowRes(rs));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return results;

    }

    public List<ReservationReportRow> getReservationsByCustomerNameAndID(String name, int customerID) {
        String sql = """
                SELECT T.ticketNumber, T.customerID, CONCAT(C.firstName, ' ', C.lastName) AS customerName, FT.flightNumber,
                FT.lineID, FT.departureDate, FT.seatNumber, FT.ticketClass, T.status, T.fareCost, T.bookingFee, T.purchaseTime
                FROM Tickets T
                JOIN FlightTickets FT ON T.ticketNumber = FT.ticketNumber 
                JOIN Customers C ON T.customerID = C.customerID
                WHERE CONCAT(C.firstName, ' ', C.lastName) LIKE ? AND C.customerID = ?;
                """;

        ArrayList<ReservationReportRow> results = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, "%" + name + "%");
                ps.setInt(2, customerID);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next())
                        results.add(mapRowRes(rs));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        return results;
    }


    public RevenueSummaryRow getRevenueSummaryByCustomer(int customerID) {
        String sql = """
                SELECT CONCAT(C.firstName, ' ', C.lastName) AS entityName, 'Customer' AS entityType, C.customerID AS entityID,
                SUM(T.fareCost + T.bookingFee) AS totalRevenue
                FROM Tickets T
                JOIN Customers C ON T.customerID = C.customerID
                WHERE C.customerID = ?
                GROUP BY C.customerID;
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return mapRowRevSum(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
     } 

    public RevenueSummaryRow getRevenueSummaryByFlight(String flightNumber, String lineID) {
        String sql = """
                SELECT CONCAT(F.lineID, ' ', F.flightNumber) AS entityName, 'Flight' AS entityType, 
                CONCAT(F.flightNumber, '-', F.lineID) AS entityID,
                SUM((T.fareCost + T.bookingFee) * 1.0 / LC.legCount) AS totalRevenue
                FROM Tickets T
                JOIN FlightTickets FT ON T.ticketNumber = FT.ticketNumber 
                JOIN Flights F ON FT.flightNumber = F.flightNumber AND FT.lineID = F.lineID
                JOIN (
                    SELECT ticketNumber, COUNT(*) AS legCount
                    FROM FlightTickets
                    GROUP BY ticketNumber
                ) LC ON T.ticketNumber = LC.ticketNumber
                WHERE F.flightNumber = ? AND F.lineID = ?
                GROUP BY F.flightNumber, F.lineID;
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, flightNumber);
            ps.setString(2, lineID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return mapRowRevSum(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

    public RevenueSummaryRow getRevenueSummaryByAirline (String lineID) {
        String sql = """
                SELECT A.name AS entityName, 'Airline' AS entityType, A.lineID AS entityID,
                SUM((T.fareCost + T.bookingFee) * 1.0 / LC.legCount) AS totalRevenue
                FROM Tickets T
                JOIN FlightTickets FT ON T.ticketNumber = FT.ticketNumber
                JOIN (
                    SELECT ticketNumber, COUNT(*) AS legCount
                    FROM FlightTickets
                    GROUP BY ticketNumber
                ) LC ON T.ticketNumber = LC.ticketNumber
                JOIN Airlines A ON FT.lineID = A.lineID
                WHERE A.lineID = ?
                GROUP BY A.lineID, A.name;
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lineID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return mapRowRevSum(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public RevenueSummaryRow getMostRevenueByCustomer () {
        String sql = """
                SELECT CONCAT(C.firstName, ' ', C.lastName) AS entityName, 'Customer' AS entityType, C.customerID AS entityID,
                SUM(T.fareCost + T.bookingFee) AS totalRevenue
                FROM Tickets T
                JOIN Customers C ON T.customerID = C.customerID
                GROUP BY C.customerID
                ORDER BY totalRevenue DESC
                LIMIT 1;
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return mapRowRevSum(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
     }

    public List<FlightSummaryRow> getMostActiveFlights() {
        String sql = """
                SELECT F.flightNumber, F.lineID, F.destinationPortID, F.departurePortID, F.flightType,
                COUNT(FT.ticketNumber) AS reservationCount
                FROM Flights F
                JOIN FlightTickets FT ON F.flightNumber = FT.flightNumber AND F.lineID = FT.lineID
                GROUP BY F.flightNumber, F.lineID
                ORDER BY reservationCount DESC
                LIMIT 10;
                """;
        ArrayList<FlightSummaryRow> results = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next())
                        results.add(mapRowFlightSum(rs));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return results;
     }
    
    public SalesReportRow getMonthlySalesSummary(int month, int year) {
        String sql = """
                SELECT
                ? AS month,
                ? AS year,

                (
                    SELECT COUNT(*)
                    FROM Tickets T
                    WHERE MONTH(T.purchaseTime) = ?
                    AND YEAR(T.purchaseTime) = ?
                ) AS ticketsSold,

                (
                    SELECT COUNT(*)
                    FROM Tickets T
                    JOIN FlightTicket FT ON T.ticketNumber = FT.ticketNumber
                    WHERE MONTH(T.purchaseTime) = ?
                    AND YEAR(T.purchaseTime) = ?
                ) AS reservationsCount,

                (
                    SELECT COALESCE(SUM(T.fareCost), 0)
                    FROM Tickets T
                    WHERE MONTH(T.purchaseTime) = ?
                    AND YEAR(T.purchaseTime) = ?
                ) AS totalFareRevenue,

                (
                    SELECT COALESCE(SUM(T.bookingFee), 0)
                    FROM Tickets T
                    WHERE MONTH(T.purchaseTime) = ?
                    AND YEAR(T.purchaseTime) = ?
                ) AS totalBookingFees,

                (
                    SELECT COALESCE(SUM(T.fareCost + T.bookingFee), 0)
                    FROM Tickets T
                    WHERE MONTH(T.purchaseTime) = ?
                    AND YEAR(T.purchaseTime) = ?
                ) AS totalRevenue
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return mapRowSalesSum(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
     }
}
