package travel.services;

import travel.model.Airport;



//clear; javac ./src/travel/services/BookingService.java; java -cp "./mysql-connector-j-8.3.0.jar" ./src/travel/services/BookingService.java

public class BookingService {
    private FlightDao fDao = null;
    public BookingService(){
        fDao = new FlightDAO();
    }

    public List<Flight> getAllFlights(){
        return fDao.findAll();
    }
    public static void main(String[] args) {
        //FlightDAO fd = new FlightDAO();
    }
}
