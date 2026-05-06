package travel.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import travel.dao.FlightDAO;
import travel.dao.FlightTicketDAO;
import travel.dao.TicketDAO;
import travel.dao.CustomerDAO;
import travel.model.Customer;
import travel.model.Flight;
import travel.model.FlightTicket;
import travel.model.ReservationReportRow;
import travel.model.Ticket;

public class ReservationService {
    public static final String TICKET_CLASS_ECONOMY = "Economy";
    public static final String TICKET_CLASS_BUSINESS = "Business";
    public static final String TICKET_CLASS_FIRST = "First";
    public static final String[] TICKET_CLASS_OPTIONS = {
        TICKET_CLASS_ECONOMY,
        TICKET_CLASS_BUSINESS,
        TICKET_CLASS_FIRST
    };
    /*
    Cancel reservation
    View all past or future reservations & details
    */

    private final TicketDAO ticketDAO;
    private final FlightTicketDAO flightTicketDAO;
    private final FlightDAO flightDAO;
    private final CustomerDAO customerDAO;

    public ReservationService() {
        this.ticketDAO = new TicketDAO();
        this.flightTicketDAO = new FlightTicketDAO();
        this.flightDAO = new FlightDAO();
        this.customerDAO = new CustomerDAO();
    }

    public void editReservation(FlightTicket flightTicket){
        verifyEditReservation(flightTicket);
        
        int ticketNumber = flightTicket.getTicketNumber();
        int legOrder = flightTicket.getLegOrder();
        String seatNumber = flightTicket.getSeatNumber();
        String ticketClass = flightTicket.getTicketClass();
        String mealOrder =flightTicket.getMealOrder();

        List<FlightTicket> flightTickets = flightTicketDAO.findByTicket(ticketNumber);
        if (flightTickets.isEmpty()) {
            throw new IllegalArgumentException(
                "Ticket " + ticketNumber + " does not have any associated flight legs."
            );
        }

        boolean found = false;
        for (FlightTicket flightTicketElement : flightTickets) {
            if (flightTicketElement.getLegOrder() == legOrder) {
                flightTicketElement.setSeatNumber(seatNumber);
                flightTicketElement.setTicketClass(ticketClass);
                flightTicketElement.setMealOrder(mealOrder);

                flightTicketDAO.update(flightTicket);
                
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException(
                "No flight leg, " + legOrder + " found."
            );
        }
    }

    public void verifyEditReservation(FlightTicket flightTicket) {
        int ticketNumber = flightTicket.getTicketNumber();
        int legOrder = flightTicket.getLegOrder();
        String seatNumber = flightTicket.getSeatNumber();
        String ticketClass = normalizeTicketClass(flightTicket.getTicketClass());
        String mealOrder =flightTicket.getMealOrder();
        
        if (ticketNumber <= 0) {
            throw new IllegalArgumentException("Ticket number must be a positive integer.");
        }   
        if (legOrder <= 0) {
            throw new IllegalArgumentException("Leg order must be a positive integer.");
        }
        if (seatNumber == null || seatNumber.isEmpty()) {
            throw new IllegalArgumentException("Seat number cannot be null or empty.");
        }
        if (ticketClass == null) {
            throw new IllegalArgumentException("Ticket class cannot be null or empty. It must either be 'Economy', 'Business' or 'First'.");
        }
        if (mealOrder == null || mealOrder.isEmpty()) {
            throw new IllegalArgumentException("Meal order cannot be null or empty. Anyone opting out of meal orders should have it listed as 'None'.");
        }

        flightTicket.setTicketClass(ticketClass);
    }

    public void cancelReservation(int ticketNumber) {
        Ticket ticket = ticketDAO.findByKey(ticketNumber);
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket not found with number: " + ticketNumber);
        }
        if (isCancelled(ticket.getStatus())) {
            throw new IllegalArgumentException("This reservation has already been cancelled.");
        }
        if (!hasFutureTravel(ticketNumber)) {
            throw new IllegalArgumentException("Only future reservations can be cancelled.");
        }
        if (!hasCancelableTicketClass(ticketNumber)) {
            throw new IllegalArgumentException("Only business or first class reservations can be cancelled.");
        }

        ticket.setStatus("Cancelled");
        ticketDAO.update(ticket);
    }

    public List<ReservationReportRow> viewAllReservations(int customerID) {
        List<ReservationReportRow> reservations = new ArrayList<>();

        for (Ticket ticket : ticketDAO.findAll(customerID)) {
            List<FlightTicket> flightTickets = flightTicketDAO.findByTicket(ticket.getTicketNumber());
            if (flightTickets.isEmpty()) {
                throw new IllegalArgumentException(
                    "Ticket " + ticket.getTicketNumber() + " does not have any associated flight legs."
                );
            }

            for (FlightTicket flightTicket : flightTickets) {
                Flight flight = flightDAO.findByKey(
                    flightTicket.getFlightNumber(),
                    flightTicket.getLineID()
                );
                reservations.add(buildReservationRow(ticket, flightTicket, flight));
            }
        }

        reservations.sort(
            Comparator.comparing(ReservationReportRow::getDepartureDate, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(ReservationReportRow::getDepartureTime, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(ReservationReportRow::getTicketNumber)
                .thenComparing(ReservationReportRow::getLegOrder)
        );

        return reservations;
    }

    public List<ReservationReportRow> viewPastReservations(int customerID) {
        LocalDate today = LocalDate.now();
        return viewAllReservations(customerID).stream()
            .filter(detail -> detail.getDepartureDate() != null && detail.getDepartureDate().isBefore(today))
            .toList();
    }

    public List<ReservationReportRow> viewFutureReservations(int customerID) {
        LocalDate today = LocalDate.now();
        return viewAllReservations(customerID).stream()
            .filter(detail -> detail.getDepartureDate() != null && !detail.getDepartureDate().isBefore(today))
            .toList();
    }

    public boolean canCancel(int ticketNumber) {
        Ticket ticket = ticketDAO.findByKey(ticketNumber);
        return ticket != null
            && !isCancelled(ticket.getStatus())
            && hasFutureTravel(ticketNumber)
            && hasCancelableTicketClass(ticketNumber);
    }

    private boolean hasFutureTravel(int ticketNumber) {
        LocalDate today = LocalDate.now();
        return flightTicketDAO.findByTicket(ticketNumber).stream()
            .map(FlightTicket::getDepartureDate)
            .anyMatch(date -> date != null && !date.isBefore(today));
    }

    private boolean hasCancelableTicketClass(int ticketNumber) {
        List<FlightTicket> flightTickets = flightTicketDAO.findByTicket(ticketNumber);
        return !flightTickets.isEmpty() && flightTickets.stream()
            .map(FlightTicket::getTicketClass)
            .map(ReservationService::normalizeTicketClass)
            .allMatch(ticketClass ->
                TICKET_CLASS_BUSINESS.equals(ticketClass) || TICKET_CLASS_FIRST.equals(ticketClass)
            );
    }

    private boolean isCancelled(String status) {
        return status != null && "cancelled".equalsIgnoreCase(status);
    }

    public static String normalizeTicketClass(String ticketClass) {
        if (ticketClass == null) {
            return null;
        }

        String trimmed = ticketClass.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        if (TICKET_CLASS_ECONOMY.equalsIgnoreCase(trimmed)) {
            return TICKET_CLASS_ECONOMY;
        }
        if (TICKET_CLASS_BUSINESS.equalsIgnoreCase(trimmed)) {
            return TICKET_CLASS_BUSINESS;
        }
        if (TICKET_CLASS_FIRST.equalsIgnoreCase(trimmed)) {
            return TICKET_CLASS_FIRST;
        }
        return null;
    }

    private ReservationReportRow buildReservationRow(Ticket ticket, FlightTicket flightTicket, Flight flight) {
        if (flight == null) {
            throw new IllegalArgumentException(
                "Flight " + flightTicket.getFlightNumber() + " / " + flightTicket.getLineID()
                    + " was not found for ticket " + ticket.getTicketNumber() + "."
            );
        }

        Customer customer = customerDAO.findByKey(ticket.getCustomerID());
        ReservationReportRow row = new ReservationReportRow();
        row.setTicketNumber(ticket.getTicketNumber());
        row.setCustomerID(ticket.getCustomerID());
        row.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
        row.setFlightNumber(flightTicket.getFlightNumber());
        row.setLineID(flightTicket.getLineID());
        row.setLegOrder(flightTicket.getLegOrder());
        row.setOriginPortID(flight.getDeparturePortID());
        row.setDestinationPortID(flight.getDestinationPortID());
        row.setDepartureDate(flightTicket.getDepartureDate());
        row.setDepartureTime(flight.getDepartureTime());
        row.setArrivalTime(flight.getArrivalTime());
        row.setSeatNumber(flightTicket.getSeatNumber());
        row.setTicketClass(flightTicket.getTicketClass());
        row.setTripType(ticket.getTripType());
        row.setStatus(ticket.getStatus());
        row.setFareCost(ticket.getFareCost());
        row.setBookingFee(ticket.getBookingFee());
        row.setPurchaseTime(ticket.getPurchaseTime());
        return row;
    }
}
