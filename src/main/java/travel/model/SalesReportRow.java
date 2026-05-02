package travel.model;

import java.math.BigDecimal;

public class SalesReportRow {

    private int month;
    private int year;
    private int ticketsSold;
    private int reservationsCount;
    private BigDecimal totalFareRevenue;
    private BigDecimal totalBookingFees;
    private BigDecimal totalRevenue;


    public SalesReportRow() {

    }

    public SalesReportRow( int month, int year, int ticketsSold, int reservationsCount, BigDecimal totalFareRevenue,
            BigDecimal totalBookingFees, BigDecimal totalRevenue) {
        this.month = month;
        this.year = year;
        this.ticketsSold = ticketsSold;
        this.reservationsCount = reservationsCount;
        this.totalFareRevenue = totalFareRevenue;
        this.totalBookingFees = totalBookingFees;
        this.totalRevenue = totalRevenue;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(int ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    public int getReservationsCount() {
        return reservationsCount;
    }

    public void setReservationsCount(int reservationsCount) {
        this.reservationsCount = reservationsCount;
    }

    public BigDecimal getTotalFareRevenue() {
        return totalFareRevenue;
    }

    public void setTotalFareRevenue(BigDecimal totalFareRevenue) {
        this.totalFareRevenue = totalFareRevenue;
    }

    public BigDecimal getTotalBookingFees() {
        return totalBookingFees;
    }

    public void setTotalBookingFees(BigDecimal totalBookingFees) {
        this.totalBookingFees = totalBookingFees;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

   
}