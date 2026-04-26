package travel.model;

import java.time.LocalDate;

class FlightTicket {

    private int ticketNumber;
    private int flightNumber;
    private String lineID;
    private int legOrder;
    private LocalDate departureDate; // Question: Would we have to modify this for Java Swing? 
    private String seatNumber;
    private String ticketClass;
    private String mealOrder;

    public FlightTicket() {

    }

    public FlightTicket(int ticketNumber, int flightNumber, String lineID, int legOrder, LocalDate departureDate, String seatNumber, String ticketClass, String mealOrder){
        this.ticketNumber = ticketNumber;
        this.flightNumber = flightNumber;
        this.lineID = lineID;
        this.legOrder = legOrder;
        this.departureDate = departureDate;
        this.seatNumber = seatNumber;
        this.ticketClass = ticketClass;
        this.mealOrder = mealOrder;
    }

    public int getTicketNumber(){
        return ticketNumber;
    }

    public void setTicketNumber(int v){
        this.ticketNumber = v;
    }

    public int getFlightNumber(){
        return flightNumber;
    }

    public void setFlightNumber(int v){
        this.flightNumber = v;
    }

    public String getlineID(){
        return lineID;
    }

    public void setLineID(String v){
        this.lineID = v;
    }

    public int getLegOrder(){
        return legOrder;
    }

    public void setLegOrder(int v){
        this.legOrder = v;
    }

    public LocalDate getDepartureDate(){
        return departureDate;
    }

    public void setDepartureDate(LocalDate v){
        this.departureDate = v;
    }

    public String getSeatNumber(){
        return seatNumber;
    }

    public void setSeatNumber(String v){
        this.seatNumber = v;
    }

    public String getTicketClass(){
        return ticketClass;
    }

    public void setTicketClass(String v){
        this.ticketClass = v;
    }

    public String getMealOrder(){
        return mealOrder;
    }

    public void setMealOrder(String v){
        this.mealOrder = v;
    }    

}