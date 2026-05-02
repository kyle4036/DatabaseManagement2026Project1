package travel.services;

import java.util.List;

import travel.model.*;
import travel.dao.*;


//clear; javac ./src/travel/services/BookingService.java; java -cp "./mysql-connector-j-8.3.0.jar" ./src/travel/services/BookingService.java

public class BookingService {
    private FlightDAO fDao = null;

    public BookingService(){
        fDao = new FlightDAO();
    }

    public List<Flight> getAllFlights(){
        return fDao.findAll();
    }
    public static void main(String[] args) {
        //FlightDAO fd = new FlightDAO();
        new BookingService().getAllFlights();
    }
}
