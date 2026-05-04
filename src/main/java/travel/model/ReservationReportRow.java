package travel.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


//More detailed report row for reservations, to be used in the Admin Reservation Report, instead of just using FlightTicket and Ticket objects. 

public class ReservationReportRow {

    private int ticketNumber;
    private int customerID;
    private String customerName;
    private String flightNumber;
    private String lineID;
    private int legOrder;
    private String originPortID;
    private String destinationPortID;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private String seatNumber;
    private String ticketClass;
    private String tripType;
    private String status;
    private BigDecimal fareCost;
    private BigDecimal bookingFee;
    private LocalDateTime purchaseTime;



    public ReservationReportRow() {

    }

    public ReservationReportRow(
        int ticketNumber,
        int customerID,
        String customerName,
        String flightNumber,
        String lineID,
        int legOrder,
        String originPortID,
        String destinationPortID,
        LocalDate departureDate,
        LocalTime departureTime,
        LocalTime arrivalTime,
        String seatNumber,
        String ticketClass,
        String tripType,
        String status,
        BigDecimal fareCost,
        BigDecimal bookingFee,
        LocalDateTime purchaseTime
    ) {
        this.ticketNumber = ticketNumber;
        this.customerID = customerID;
        this.customerName = customerName;
        this.flightNumber = flightNumber;
        this.lineID = lineID;
        this.legOrder = legOrder;
        this.originPortID = originPortID;
        this.destinationPortID = destinationPortID;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.seatNumber = seatNumber;
        this.ticketClass = ticketClass;
        this.tripType = tripType;
        this.status = status;
        this.fareCost = fareCost;
        this.bookingFee = bookingFee;
        this.purchaseTime = purchaseTime;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getLineID() {
        return lineID;
    }

    public void setLineID(String lineID) {
        this.lineID = lineID;
    }

    public int getLegOrder() {
        return legOrder;
    }

    public void setLegOrder(int legOrder) {
        this.legOrder = legOrder;
    }

    public String getOriginPortID() {
        return originPortID;
    }

    public void setOriginPortID(String originPortID) {
        this.originPortID = originPortID;
    }

    public String getDestinationPortID() {
        return destinationPortID;
    }

    public void setDestinationPortID(String destinationPortID) {
        this.destinationPortID = destinationPortID;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getTicketClass() {
        return ticketClass;
    }

    public void setTicketClass(String ticketClass) {
        this.ticketClass = ticketClass;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getFareCost() {
        return fareCost;
    }

    public void setFareCost(BigDecimal fareCost) {
        this.fareCost = fareCost;
    }

    public BigDecimal getBookingFee() {
        return bookingFee;
    }

    public void setBookingFee(BigDecimal bookingFee) {
        this.bookingFee = bookingFee;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(LocalDateTime purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

}
