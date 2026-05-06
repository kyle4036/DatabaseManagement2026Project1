package travel.ui.rep;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import travel.model.FlightTicket;
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

        JLabel title = new JLabel("Edit Reservation");
        title.setFont(new Font("Lucida Sans", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);
    
        JButton editBtn = new JButton("Edit Reservation");
        JButton backBtn = new JButton("Back");

        editBtn.addActionListener(e -> withGuard(this::onEditReservation));
        backBtn.addActionListener(e -> mainFrame.showScreen(Screen.REP_HOME));

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        buttonPanel.add(editBtn);
        buttonPanel.add(backBtn);

        JPanel center = new JPanel();
        center.add(buttonPanel);
        add(center, BorderLayout.CENTER);
    }

    private void onEditReservation() {
        FlightTicket flightTicket = promptEditReservation();

        if (flightTicket == null) {
            return;
        }

        int ticketNumber = flightTicket.getTicketNumber();
        int legOrder = flightTicket.getLegOrder();
        String seatNumber = flightTicket.getSeatNumber();
        String ticketClass = flightTicket.getTicketClass();
        String mealOrder =flightTicket.getMealOrder();

        reservationService.editReservation(ticketNumber, legOrder, seatNumber, ticketClass, mealOrder);
        showInfo("Reservation updated successfully.");
    }

    private FlightTicket promptEditReservation() {
        JTextField ticketNumber = new JTextField();
        JTextField legOrder = new JTextField();
        JTextField seatNumber = new JTextField();
        JComboBox<String> ticketClass = new JComboBox<>(ReservationService.TICKET_CLASS_OPTIONS);
        JTextField mealOrder = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Ticket Number"));
        panel.add(ticketNumber);
        panel.add(new JLabel("Leg Order"));
        panel.add(legOrder);
        panel.add(new JLabel("Seat Number"));
        panel.add(seatNumber);
        panel.add(new JLabel("Ticket Class"));
        panel.add(ticketClass);
        panel.add(new JLabel("Meal Order"));
        panel.add(mealOrder);

        int choice = JOptionPane.showConfirmDialog(this, panel, "Edit Reservation", JOptionPane.OK_CANCEL_OPTION);
        if (choice != JOptionPane.OK_OPTION) {
            return null;
        }

        FlightTicket flightTicket = new FlightTicket();
        flightTicket.setTicketNumber(parseRequiredInt(ticketNumber.getText(), "Ticket Number"));
        flightTicket.setLegOrder(parseRequiredInt(legOrder.getText(), "Leg Order"));
        flightTicket.setSeatNumber(seatNumber.getText().trim());
        flightTicket.setTicketClass(String.valueOf(ticketClass.getSelectedItem()));
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

    private void withGuard(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Operation Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Edit Reservation", JOptionPane.INFORMATION_MESSAGE);
    }
}
