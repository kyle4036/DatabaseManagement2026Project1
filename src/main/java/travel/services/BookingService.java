package travel.services;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import travel.model.*;
import travel.dao.*;

//TODO:
//need to implement a way to find route if no direct flight exists

public class BookingService {
    private FlightDAO fDao = null;
    private TicketDAO tDao = null;
    private FlightTicketDAO ftDao = null;

    public BookingService(){
        fDao = new FlightDAO();
        tDao = new TicketDAO();
        ftDao = new FlightTicketDAO();
    }

    public List<Flight> getAllFlights(){
        return fDao.findAll();
    }

    public void addTicket(Ticket t){
        tDao.insert(t);
    }

    public void bookByFlight(Flight f, Customer customer, String date, boolean roundTripCheck, String ticketClass){
        String normalizedTicketClass = ReservationService.normalizeTicketClass(ticketClass);
        if (normalizedTicketClass == null) {
            throw new IllegalArgumentException("Select a valid ticket class: Economy, Business, or First.");
        }

        String tripType = roundTripCheck ? "Round trip" : "One way";

        Ticket t = new Ticket(  -1, 
                                customer.getCustomerID(), 
                                LocalDateTime.now(),
                                new BigDecimal(20),
                                new BigDecimal(100), 
                                tripType,
                                "active");
        this.addTicket(t);

        int lastTicket = tDao.getLastTicket(customer.getCustomerID());
        FlightTicket ft = new FlightTicket(
                lastTicket, 
                f.getFlightNumber(), 
                f.getLineID(),
                1,
                LocalDate.parse(date),
                "",
                normalizedTicketClass,
                "");

        ftDao.insert(ft);
        
        if(roundTripCheck){
            Flight iFlight = getInverseFlights(f).get(0);
            FlightTicket ft2 = new FlightTicket(
                lastTicket,
                iFlight.getFlightNumber(),
                iFlight.getLineID(),
                2,
                LocalDate.parse(date).plusWeeks(1),
                "",
                "",
                ""
            );
            ftDao.insert(ft2);
        }

        fDao.updateSeatsTaken(f.getFlightNumber(), f.getLineID(), f.getSeatsTaken()-1);
    }

    public List<Flight> findByRoute(String fromPortID, String toPortID){
        return fDao.findByRoute(fromPortID, toPortID);
    }

    public List<Flight> findByAirport(String portID){
        return fDao.findByAirport(portID);
    }

    public void sortFlights(List<Flight> flights, String sortCriteria) {
        if (flights == null || flights.size() < 2 || sortCriteria == null) {
            return;
        }

        Comparator<Flight> comparator = switch (sortCriteria.trim().toLowerCase()) {
            case "price" -> Comparator.comparing(this::estimateBaseFare);
            case "take-off time" -> Comparator.comparing(
                Flight::getDepartureTime,
                Comparator.nullsLast(Comparator.naturalOrder())
            );
            case "landing time" -> Comparator.comparing(
                Flight::getArrivalTime,
                Comparator.nullsLast(Comparator.naturalOrder())
            );
            default -> null;
        };

        if (comparator != null) {
            flights.sort(comparator
                .thenComparing(Flight::getFlightNumber, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Flight::getLineID, Comparator.nullsLast(Comparator.naturalOrder())));
        }
    }

    public BigDecimal estimateBaseFare(Flight flight) {
        // Current booking flow uses a flat fare for new tickets, so search sorting
        // keeps "price" aligned with that behavior until flights store real fares.
        return new BigDecimal("100.00");
    }

    public void removeFlightArrivals(List<Flight> flights, String portID){
        flights.removeIf((Flight f) -> f.getDestinationPortID().equals(portID));
        /*
        for(Flight f: flights){
            if(f.getDestinationPortID().equals(portID)){
                flights.remove(f);
            }
        }*/
    }

    public void removeFlightDepartures(List<Flight> flights, String portID){
        flights.removeIf((Flight f) -> f.getDeparturePortID().equals(portID));
    }

    public void removeFlightDates(List<Flight> flights, String date){
        //String dateMask = this.dateToBitMask(date);
        LocalDate lDate = LocalDate.parse(date);
        DayOfWeek day = lDate.getDayOfWeek();
        int dval = day.getValue() % 7;

        flights.removeIf(
            (Flight f) ->
                f.getDaysRunning().toCharArray()
                [
                    //day.getValue()//-1
                    dval
                ]
                == '0'
        );
        /* 
        for(Flight f: flights){
            char[] dateMask = f.getDaysRunning().toCharArray();
            if(dateMask[day.getValue()] == '0'){
                flights.remove(f);
            }
        }*/
    }

    public void removeFlightNotRoundTrip(List<Flight> flights, String fromPortID, String toPortID){
        /*List<Flight> tempFlights = findByRoute(toPortID, fromPortID);
        flights.removeAll(tempFlights);*/
        //List<Flight> tempFlights = getAllFlights();
        flights.removeIf((Flight f) ->
                (getInverseFlights(f).isEmpty())
        );
    }

    public List<Flight> getInverseFlights(Flight f){
        //List<Flight> tempFlights = findByRoute(f.getDestinationPortID(), f.getDeparturePortID());
        //System.out.println(tempFlights);
        return findByRoute(f.getDestinationPortID(), f.getDeparturePortID());

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
