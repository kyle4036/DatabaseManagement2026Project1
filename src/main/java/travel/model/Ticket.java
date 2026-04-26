package main.java.travel.model;

import java.time.LocalTime;

public class Ticket {

    private int ticketNumber;
    private int customerID;
    private LocalTime purchaseTime; // Question: Would we have to modify this for Java Swing?
    private float bookingFee;
    private float fareCost;
    private String tripType;
    private String status;

    public Ticket() {

    }

    public Ticket(int ticketNumber, int customerID, LocalTime purchaseTime, float bookingFee, float fareCost, String tripType, String status){
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

    public LocalTime getPurchaseTime(){
        return purchaseTime;
    }

    public void setPurchaseTime(LocalTime v){
        this.purchaseTime = v;
    }

    public float getBookingFee(){
        return bookingFee;
    }

    public void setBookingFee(float v){
        this.bookingFee = v;
    }

    public float getFareCost(){
        return fareCost;
    }

    public void setFareCost(float v){
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

}