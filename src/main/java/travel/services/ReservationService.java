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

        ticket.setStatus("Cancelled");
        ticketDAO.update(ticket);
    }

    public List<ReservationReportRow> viewAllReservations(int customerID) {
        List<ReservationReportRow> reservations = new ArrayList<>();

        for (Ticket ticket : ticketDAO.findAll(customerID)) {
            List<FlightTicket> flightTickets = flightTicketDAO.findByTicket(ticket.getTicketNumber());
            if (flightTickets.isEmpty()) {
                throw new IllegalStateException(
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
        return ticket != null && !isCancelled(ticket.getStatus()) && hasFutureTravel(ticketNumber);
    }

    private boolean hasFutureTravel(int ticketNumber) {
        LocalDate today = LocalDate.now();
        return flightTicketDAO.findByTicket(ticketNumber).stream()
            .map(FlightTicket::getDepartureDate)
            .anyMatch(date -> date != null && !date.isBefore(today));
    }

    private boolean isCancelled(String status) {
        return status != null && "cancelled".equalsIgnoreCase(status);
    }

    private ReservationReportRow buildReservationRow(Ticket ticket, FlightTicket flightTicket, Flight flight) {
        if (flight == null) {
            throw new IllegalStateException(
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
