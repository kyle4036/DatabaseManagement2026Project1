package travel.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Ticket {

    private int ticketNumber;
    private int customerID;
    private Timestamp purchaseTime; // Question: Would we have to modify this for Java Swing?
    private BigDecimal bookingFee;
    private BigDecimal fareCost;
    private String tripType;
    private String status;

    public Ticket() {

    }

    public Ticket(int ticketNumber, int customerID, Timestamp purchaseTime, BigDecimal bookingFee, BigDecimal fareCost, String tripType, String status){
        this.ticketNumber = ticketNumber;
        this.customerID = customerID;
        this.purchaseTime = purchaseTime;
        this.bookingFee = bookingFee;
        this.fareCost = fareCost;
        this.tripType = tripType;
        this.status = status;
    }

    public int getTicketNumber(){
        return ticketNumber;
    }

    public void setTicketNumber(int v){
        this.ticketNumber = v;
    }

    public int getCustomerID(){
        return customerID;
    }

    public void setCustomerID(int v){
        this.customerID = v;
    }

    public Timestamp getPurchaseTime(){
        return purchaseTime;
    }

    public void setPurchaseTime(Timestamp v){
        this.purchaseTime = v;
    }

    public BigDecimal getBookingFee(){
        return bookingFee;
    }

    public void setBookingFee(BigDecimal v){
        this.bookingFee = v;
    }

    public BigDecimal getFareCost(){
        return fareCost;
    }

    public void setFareCost(BigDecimal v){
        this.fareCost = v;
    }

    public String getTripType(){
        return tripType;
    }

    public void setTripType(String v){
        this.tripType = v;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String v){
        this.status = v;
    }

    public static Timestamp getCurrentTimestamp(){
        java.util.Date utilDate = new java.util.Date();
        Timestamp sq = new Timestamp(utilDate.getTime());
        return sq;
    }

}