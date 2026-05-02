package travel.services;

import travel.dao.AircraftDAO;
import travel.dao.FlightDAO;
import travel.dao.AirportDAO;
import travel.dao.FlightTicketDAO;
import travel.dao.TicketDAO;

import travel.model.Aircraft;
import travel.model.Flight;
import travel.model.Airport;
import travel.model.FlightTicket;
import travel.model.Customer;
import travel.model.Ticket;
import java.util.List;


public class RepresentativeService {
    private final AircraftDAO aircraftDAO;
    private final FlightDAO flightDAO;
    private final AirportDAO airportDAO;
    private final TicketDAO ticketDAO;
    private final FlightTicketDAO flightTicketDAO;
    

    public RepresentativeService(){
        this.aircraftDAO = new AircraftDAO();
        this.flightDAO = new FlightDAO();
        this.airportDAO = new AirportDAO();
        this.ticketDAO = new TicketDAO();
        this.flightTicketDAO = new FlightTicketDAO();
    }

    public void addAircraft(Aircraft aircraft) {
        verifyAircraft(aircraft);
        aircraftDAO.insert(aircraft);
    }

    public void verifyAircraft(Aircraft aircraft) {

        if (aircraft.getCraftID() <= 0) {
            throw new IllegalArgumentException("Craft ID must be a positive integer.");
        }   
        if (aircraft.getLineID() == null || aircraft.getLineID().isEmpty()) {
            throw new IllegalArgumentException("Line ID cannot be null or empty.");
        }
        if (aircraft.getPortID() == null || aircraft.getPortID().isEmpty()) {
            throw new IllegalArgumentException("Port ID cannot be null or empty.");
        }
        if (aircraft.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be a positive integer.");
        }
        if (aircraft.getModel() == null || aircraft.getModel().isEmpty()) {
            throw new IllegalArgumentException("Model cannot be null or empty.");
        }
    }

    public void deleteAircraft(int craftID) {
        aircraftDAO.delete(craftID);
    }

    public void updateAircraft(Aircraft aircraft) {
        verifyAircraft(aircraft);
        aircraftDAO.update(aircraft);
    }

    public void addFlight(Flight flight) {
        verifyFlight(flight);
        flightDAO.insert(flight);
    }

    public void verifyFlight(Flight flight) {
        if (flight.getFlightNumber() == null || flight.getFlightNumber().isEmpty()) {
            throw new IllegalArgumentException("Flight number cannot be null or empty.");
        }
        if (flight.getLineID() == null || flight.getLineID().isEmpty()) {
            throw new IllegalArgumentException("Line ID cannot be null or empty.");
        }
        if (flight.getDepartureTime() == null) {
            throw new IllegalArgumentException("Departure time cannot be null.");
        }
        if (flight.getArrivalTime() == null) {
            throw new IllegalArgumentException("Arrival time cannot be null.");
        }
        if (flight.getFlightType() == null || flight.getFlightType().isEmpty()) {
            throw new IllegalArgumentException("Flight type cannot be null or empty.");
        }
        if (flight.getDaysRunning() == null || flight.getDaysRunning().isEmpty()) {
            throw new IllegalArgumentException("Days running cannot be null or empty.");
        }
        if (flight.getSeatsTaken() < 0) {
            throw new IllegalArgumentException("Seats taken cannot be negative.");
        }
        if (flight.getCraftID() <= 0) {
            throw new IllegalArgumentException("Craft ID must be a positive integer.");
        }
        if (flight.getDeparturePortID() == null || flight.getDeparturePortID().isEmpty()) {
            throw new IllegalArgumentException("Departure port ID cannot be null or empty.");
        }
        if (flight.getDestinationPortID() == null || flight.getDestinationPortID().isEmpty()) {
            throw new IllegalArgumentException("Destination port ID cannot be null or empty.");
        }
    }

    public void deleteFlight(String flightNumber, String lineID) {
        flightDAO.delete(flightNumber, lineID);
    }

    public void updateFlight(Flight flight) {
        verifyFlight(flight);
        flightDAO.update(flight);
    }


    public void addAirport(Airport airport) {
        verifyAirport(airport);
        airportDAO.insert(airport);
    }

    public void verifyAirport(Airport airport) {
        if (airport.getPortID() == null || airport.getPortID().isEmpty()) {
            throw new IllegalArgumentException("Port ID cannot be null or empty.");
        }
        if (airport.getName() == null || airport.getName().isEmpty()) {
            throw new IllegalArgumentException("Airport name cannot be null or empty.");
        }
        if (airport.getCity() == null || airport.getCity().isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty.");
        }
        if (airport.getCountry() == null || airport.getCountry().isEmpty()) {
            throw new IllegalArgumentException("Country cannot be null or empty.");
        }
    }

    public void deleteAirport(String portID) {
        airportDAO.delete(portID);
    }

    public void updateAirport(Airport airport) {
        verifyAirport(airport);
        airportDAO.update(airport);
    }

    public List<Flight> getFlightsByPortID(String portID) {
        if (portID == null || portID.isEmpty()) {
            throw new IllegalArgumentException("Port ID cannot be null or empty.");
        }
        return flightDAO.findByAirport(portID);
    }
}
