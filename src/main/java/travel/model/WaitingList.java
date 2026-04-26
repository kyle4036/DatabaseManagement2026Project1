package main.java.travel.model;

import java.time.LocalTime;

public class WaitingList {

    private int customerID;
    private int flightNumber;
    private String lineID;
    private LocalTime requestTime; // Question: Would we have to modify this for Java Swing?

    public WaitingList() {

    }

    public WaitingList(int customerID, int flightNumber, String lineID, LocalTime requestTime) {
        this.customerID = customerID;
        this.flightNumber = flightNumber;
        this.lineID = lineID;
        this.requestTime = requestTime;
    }

    public int getCustomerID(){
        return customerID;
    }

    public void setCustomerID(int v){
        this.customerID = v;
    }

    public int getFlightNumber(){
        return flightNumber;
    }

    public void setFlightNumber(int v){
        this.flightNumber = v;
    }

    public String getLineID(){
        return lineID;
    }

    public void setLineID(String v){
        this.lineID = v;
    }

    public LocalTime getRequestTime(){
        return requestTime;
    }

    public void setRequestTime(LocalTime v){
        this.requestTime = v;
    }
}
