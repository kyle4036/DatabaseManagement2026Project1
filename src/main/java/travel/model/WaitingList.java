package travel.model;

import java.time.LocalDateTime;

public class WaitingList {

    private int customerID;
    private String flightNumber;
    private String lineID;
    private LocalDateTime requestTime; // Question: Would we have to modify this for Java Swing?

    public WaitingList() {

    }

    public WaitingList(int customerID, String flightNumber, String lineID, LocalDateTime requestTime) {
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

    public String getFlightNumber(){
        return flightNumber;
    }

    public void setFlightNumber(String v){
        this.flightNumber = v;
    }

    public String getLineID(){
        return lineID;
    }

    public void setLineID(String v){
        this.lineID = v;
    }

    public LocalDateTime getRequestTime(){
        return requestTime;
    }

    public void setRequestTime(LocalDateTime v){
        this.requestTime = v;
    }
}
