package travel.ui.rep;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import travel.model.Customer;
import travel.model.Employee;
import travel.model.FlightTicket;
import travel.services.AdminService;
import travel.services.ReservationService;
import travel.ui.MainFrame;
import travel.ui.Screen;

public class RepReservationPanel extends JPanel {

    private final MainFrame mainFrame;
    private final ReservationService reservationService = new ReservationService();

    public RepReservationPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Manage Customers / Employees");
        title.setFont(new Font("Lucida Sans", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);
    }

    private void onEditReservation() {
        FlightTicket flightTicket = promptFlightTicket(false);
        if (flightTicket == null) {
            return;
        }
        reservationService.editReservation(flightTicket);
        showInfo("Reservation updated successfully.");
    }

    private FlightTicket promptFlightTicket(boolean includeId) {
        JTextField idField = new JTextField();
        JTextField seatNumber = new JTextField();
        JTextField ticketClass = new JTextField();        
        JTextField mealOrder = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        if (includeId) {
            panel.add(new JLabel("Ticket Number"));
            panel.add(idField);
            panel.add(new JLabel("Leg Order"));
            panel.add(idField);
        }
        panel.add(new JLabel("Seat Number"));
        panel.add(seatNumber);
        panel.add(new JLabel("Ticket Class"));
        panel.add(ticketClass);
        panel.add(new JLabel("Meal Order"));
        panel.add(mealOrder);

        int choice = JOptionPane.showConfirmDialog(this, panel, includeId ? "Edit Reservation" : "Make Reservation", JOptionPane.OK_CANCEL_OPTION);
        if (choice != JOptionPane.OK_OPTION) {
            return null;
        }

        FlightTicket flightTicket = new FlightTicket();
        if (includeId) {
            flightTicket.setTicketNumber(parseRequiredInt(idField.getText(), "Ticket Number"));
            flightTicket.setLegOrder(parseRequiredInt(idField.getText(), "Leg Order"));
        }
        flightTicket.setSeatNumber(seatNumber.getText().trim());
        flightTicket.setTicketClass(ticketClass.getText().trim());
        flightTicket.setMealOrder(mealOrder.getText().trim());
        return flightTicket;
    }

    private int parseRequiredInt(String value, String label) {
        String trimmed = value == null ? "" : value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(label + " is required");
        }
        try {
            return Integer.parseInt(trimmed);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + " must be a number");
        }
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Representative", JOptionPane.INFORMATION_MESSAGE);
    }
}