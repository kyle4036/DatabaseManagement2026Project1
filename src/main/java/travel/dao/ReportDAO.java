package travel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import travel.DBConnection;
import travel.model.FlightSummaryRow;
import travel.model.ReservationReportRow;
import travel.model.RevenueSummaryRow;
import travel.model.SalesReportRow;

public class ReportDAO {

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
        r.setOriginPortID(rs.getString("originPortID"));
        r.setDestinationPortID(rs.getString("destinationPortID"));
        r.setFlightType(rs.getString("flightType"));
        r.setReservationCount(rs.getInt("reservationCount"));
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

    // All reservations for a given flight. Used by admin "list reservations by flight number"
    public List<ReservationReportRow> getReservationsByFlightNumber(String flightNumber, String lineID) {
        String sql = """
                SELECT T.ticketNumber, T.customerID,
                       CONCAT(C.firstName, ' ', C.lastName) AS customerName,
                       FT.flightNumber, FT.lineID, FT.departureDate, FT.seatNumber,
                       FT.ticketClass, T.status, T.fareCost, T.bookingFee, T.purchaseTime
                FROM Tickets T
                JOIN FlightTickets FT ON T.ticketNumber = FT.ticketNumber
                JOIN Customers C ON T.customerID = C.customerID
                WHERE FT.flightNumber = ? AND FT.lineID = ?
                """;
        List<ReservationReportRow> results = new ArrayList<>();
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, flightNumber);
            ps.setString(2, lineID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) results.add(mapRowRes(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    // Reservations matching a customer name (LIKE keyword allows us to fuzzy match easier)
    public List<ReservationReportRow> getReservationsByCustomerName(String name) {
        String sql = """
                SELECT T.ticketNumber, T.customerID,
                       CONCAT(C.firstName, ' ', C.lastName) AS customerName,
                       FT.flightNumber, FT.lineID, FT.departureDate, FT.seatNumber,
                       FT.ticketClass, T.status, T.fareCost, T.bookingFee, T.purchaseTime
                FROM Tickets T
                JOIN FlightTickets FT ON T.ticketNumber = FT.ticketNumber
                JOIN Customers C ON T.customerID = C.customerID
                WHERE CONCAT(C.firstName, ' ', C.lastName) LIKE ?
                """;
        List<ReservationReportRow> results = new ArrayList<>();
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) results.add(mapRowRes(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    public RevenueSummaryRow getRevenueSummaryByCustomer(int customerID) {
        String sql = """
                SELECT CAST(C.customerID AS CHAR) AS entityID,
                       CONCAT(C.firstName, ' ', C.lastName) AS entityName,
                       'Customer' AS entityType,
                       COALESCE(SUM(T.fareCost + T.bookingFee), 0) AS totalRevenue
                FROM Tickets T
                JOIN Customers C ON T.customerID = C.customerID
                WHERE C.customerID = ?
                GROUP BY C.customerID
                """;
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowRevSum(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Revenue attributed to a specific flight
     * Takes into account that multi-leg ticket's revenue is split evenly across its legs so we don't double count
     */
    public RevenueSummaryRow getRevenueSummaryByFlight(String flightNumber, String lineID) {
        String sql = """
                SELECT CONCAT(F.flightNumber, '-', F.lineID) AS entityID,
                       CONCAT(F.lineID, ' ', F.flightNumber) AS entityName,
                       'Flight' AS entityType,
                       COALESCE(SUM((T.fareCost + T.bookingFee) * 1.0 / LC.legCount), 0) AS totalRevenue
                FROM Tickets T
                JOIN FlightTickets FT ON T.ticketNumber = FT.ticketNumber
                JOIN Flights F ON FT.flightNumber = F.flightNumber AND FT.lineID = F.lineID
                JOIN (
                    SELECT ticketNumber, COUNT(*) AS legCount
                    FROM FlightTickets
                    GROUP BY ticketNumber
                ) LC ON T.ticketNumber = LC.ticketNumber
                WHERE F.flightNumber = ? AND F.lineID = ?
                GROUP BY F.flightNumber, F.lineID
                """;
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, flightNumber);
            ps.setString(2, lineID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowRevSum(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public RevenueSummaryRow getRevenueSummaryByAirline(String lineID) {
        String sql = """
                SELECT A.lineID AS entityID,
                       A.name AS entityName,
                       'Airline' AS entityType,
                       COALESCE(SUM((T.fareCost + T.bookingFee) * 1.0 / LC.legCount), 0) AS totalRevenue
                FROM Tickets T
                JOIN FlightTickets FT ON T.ticketNumber = FT.ticketNumber
                JOIN (
                    SELECT ticketNumber, COUNT(*) AS legCount
                    FROM FlightTickets
                    GROUP BY ticketNumber
                ) LC ON T.ticketNumber = LC.ticketNumber
                JOIN Airlines A ON FT.lineID = A.lineID
                WHERE A.lineID = ?
                GROUP BY A.lineID, A.name
                """;
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lineID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowRevSum(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public RevenueSummaryRow getMostRevenueByCustomer() {
        String sql = """
                SELECT CAST(C.customerID AS CHAR) AS entityID,
                       CONCAT(C.firstName, ' ', C.lastName) AS entityName,
                       'Customer' AS entityType,
                       SUM(T.fareCost + T.bookingFee) AS totalRevenue
                FROM Tickets T
                JOIN Customers C ON T.customerID = C.customerID
                GROUP BY C.customerID
                ORDER BY totalRevenue DESC
                LIMIT 1
                """;
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return mapRowRevSum(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Top 10 flights by tickets sold
    public List<FlightSummaryRow> getMostActiveFlights() {
        String sql = """
                SELECT F.flightNumber,
                       F.lineID,
                       F.departure_portID  AS originPortID,
                       F.destination_portID AS destinationPortID,
                       F.flightType,
                       COUNT(FT.ticketNumber) AS reservationCount
                FROM Flights F
                JOIN FlightTickets FT ON F.flightNumber = FT.flightNumber AND F.lineID = FT.lineID
                GROUP BY F.flightNumber, F.lineID,
                         F.departure_portID, F.destination_portID, F.flightType
                ORDER BY reservationCount DESC
                LIMIT 10
                """;
        List<FlightSummaryRow> results = new ArrayList<>();
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) results.add(mapRowFlightSum(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    public SalesReportRow getMonthlySalesSummary(int month, int year) {
        String sql = """
                WITH MonthTickets AS (
                    SELECT *
                    FROM Tickets
                    WHERE MONTH(purchaseTime) = ? AND YEAR(purchaseTime) = ?
                )
                SELECT
                    ? AS month,
                    ? AS year,
                    (SELECT COUNT(*) FROM MonthTickets) AS ticketsSold,
                    (SELECT COUNT(*)
                     FROM MonthTickets MT
                     JOIN FlightTickets FT ON MT.ticketNumber = FT.ticketNumber) AS reservationsCount,
                    COALESCE((SELECT SUM(fareCost) FROM MonthTickets), 0) AS totalFareRevenue,
                    COALESCE((SELECT SUM(bookingFee) FROM MonthTickets), 0) AS totalBookingFees,
                    COALESCE((SELECT SUM(fareCost + bookingFee) FROM MonthTickets), 0) AS totalRevenue
                """;
        try (Connection conn = DBConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            ps.setInt(3, month);
            ps.setInt(4, year);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowSalesSum(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
