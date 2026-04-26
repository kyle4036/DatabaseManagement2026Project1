package travel.model;

import java.time.LocalTime;

public class Flight {

    private int flightNumber;
    private String lineID;
    private String departurePortID;
    private String destinationPortID;
    private LocalTime departureTime; // Question: Should this be LocalTime or ZonedDateTime? Also, would we have to modify this for Java Swing?
    private LocalTime arrivalTime; // Question: Should this be LocalTime or ZonedDateTime? Also, would we have to modify this for Java Swing?
    private String flightType; // this will be domestic or international NOT round trip
    private String daysRunning; // this will be a "bit" mask for a string in the format of 0s and 1s (SMTWTFS)
    private int seatsTaken;
    private int craftID;

    public Flight() {

    }

    public Flight(int flightNumber, String lineID, String departurePortID,
            String destinationPortID, LocalTime departureTime,
            LocalTime arrivalTime, String flightType, String daysRunning,
            int seatsTaken, int craftID) {

        this.flightNumber = flightNumber;
        this.lineID = lineID;
        this.departurePortID = departurePortID;
        this.destinationPortID = destinationPortID;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.flightType = flightType;
        this.daysRunning = daysRunning;
        this.seatsTaken = seatsTaken;
        this.craftID = craftID;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int v) {
        this.flightNumber = v;
    }

    public String getLineID() {
        return lineID;
    }

    public void setLineID(String v) {
        this.lineID = v;
    }

    public String getDeparturePortID() {
        return departurePortID;
    }

    public void setDeparturePortID(String v) {
        this.departurePortID = v;
    }

    public String getDestinationPortID() {
        return destinationPortID;
    }

    public void setDestinationPortID(String v) {
        this.destinationPortID = v;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime v) {
        this.departureTime = v;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime v) {
        this.arrivalTime = v;
    }

    public String getFlightType() {
        return flightType;
    }

    public void setFlightType(String v) {
        this.flightType = v;
    }

    public String getDaysRunning() {
        return daysRunning;
    }

    public void setDaysRunning(String v) {
        this.daysRunning = v;
    }

    public int getSeatsTaken() {
        return seatsTaken;
    }

    public void setSeatsTaken(int v) {
        this.seatsTaken = v;
    }

    public int getCraftID() {
        return craftID;
    }

    public void setCraftID(int v) {
        this.craftID = v;
    }

}
