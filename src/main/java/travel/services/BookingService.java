package travel.services;

import java.util.List;

import travel.model.*;
import travel.dao.*;




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

    public  void addTicket(Ticket t){
        tDao.insert(t);
    }

    public static void main(String[] args) {
        //FlightDAO fd = new FlightDAO();
        new BookingService().getAllFlights();
    }
}
