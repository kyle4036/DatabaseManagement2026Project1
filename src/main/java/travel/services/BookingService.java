package travel.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import travel.model.*;
import travel.dao.*;

//TODO:
//need to implement a way to find route if no direct flight exists

public class BookingService {
    private FlightDAO fDao = null;
    private TicketDAO tDao = null;

    public BookingService(){
        fDao = new FlightDAO();
        tDao = new TicketDAO();
    }

    public List<Flight> getAllFlights(){
        return fDao.findAll();
    }

    public void addTicket(Ticket t){
        tDao.insert(t);
    }

    public void bookByFlight(Flight f){

    }

    public List<Flight> findByRoute(String fromPortID, String toPortID){
        return fDao.findByRoute(fromPortID, toPortID);
    }

    public List<Flight> findByAirport(String portID){
        return fDao.findByAirport(portID);
    }

    public void removeFlightArrivals(List<Flight> flights, String portID){
        for(Flight f: flights){
            if(f.getDestinationPortID().equals(portID)){
                flights.remove(f);
            }
        }
    }

    public void removeFlightDepartures(List<Flight> flights, String portID){
        for(Flight f: flights){
            if(f.getDeparturePortID().equals(portID)){
                flights.remove(f);
            }
        }
    }

    public void removeFlightDates(List<Flight> flights, String date){
        //String dateMask = this.dateToBitMask(date);
        LocalDate lDate = LocalDate.parse(date);
        DayOfWeek day = lDate.getDayOfWeek();

        for(Flight f: flights){
            char[] dateMask = f.getDaysRunning().toCharArray();
            if(dateMask[day.getValue()] == '0'){
                flights.remove(f);
            }
        }
    }


    public static void main(String[] args) {
        //FlightDAO fd = new FlightDAO();
        new BookingService().getAllFlights();
    }

    private String dateToBitMask(String date){
        LocalDate lDate = LocalDate.parse(date);
        DayOfWeek day = lDate.getDayOfWeek();
        String mask = "0000000";
        char[] aMask = mask.toCharArray();
        aMask[day.getValue()] = '1';
        return String.valueOf(aMask);
    }


}
