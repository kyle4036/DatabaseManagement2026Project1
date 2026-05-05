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
