package travel.model;

import java.time.ZonedDateTime;

class WaitingList {

    private int customerID;
    private int flightNumber;
    private String lineID;
    private ZonedDateTime requestTime;

    public WaitingList() {

    }

    public WaitingList(int customerID, int flightNumber, String lineID, ZonedDateTime requestTime) {
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

    public ZonedDateTime getRequestTime(){
        return requestTime;
    }

    public void setRequestTime(ZonedDateTime v){
        this.requestTime = v;
    }
}