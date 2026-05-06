package travel.ui.customer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import travel.model.Customer;
import travel.model.ReservationReportRow;
import travel.services.ReservationService;
import travel.ui.MainFrame;
import travel.ui.Screen;

public class CustomerReservationsPanel extends JPanel {

    private static final String FILTER_ALL = "ALL";
    private static final String FILTER_FUTURE = "FUTURE";
    private static final String FILTER_PAST = "PAST";

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private final MainFrame mainFrame;
    private final ReservationService reservationService = new ReservationService();
    private final DefaultTableModel tableModel = new DefaultTableModel(
        new Object[] {
            "Ticket #", "Leg", "Flight #", "Airline", "From", "To",
            "Departure Date", "Departure Time", "Arrival Time",
            "Seat", "Class", "Trip Type", "Fare", "Booking Fee", "Status"
        },
        0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(tableModel);
    private final JLabel statusLabel = new JLabel(" ");
    private String currentFilter = FILTER_ALL;

    public CustomerReservationsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("My Reservations");
        title.setFont(new Font("Lucida Sans", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel controls = new JPanel(new BorderLayout(10, 10));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton allBtn = new JButton("Show All");
        JButton futureBtn = new JButton("Future");
        JButton pastBtn = new JButton("Past");

        allBtn.addActionListener(e -> filterReservations(FILTER_ALL));
        futureBtn.addActionListener(e -> filterReservations(FILTER_FUTURE));
        pastBtn.addActionListener(e -> filterReservations(FILTER_PAST));

        filterPanel.add(allBtn);
        filterPanel.add(futureBtn);
        filterPanel.add(pastBtn);
        filterPanel.add(statusLabel);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton cancelBtn = new JButton("Cancel Selected");
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");

        cancelBtn.addActionListener(e -> withGuard(this::cancelSelectedReservation));
        refreshBtn.addActionListener(e -> withGuard(this::refreshReservations));
        backBtn.addActionListener(e -> mainFrame.showScreen(Screen.CUSTOMER_HOME));

        actionPanel.add(cancelBtn);
        actionPanel.add(refreshBtn);
        actionPanel.add(backBtn);

        controls.add(filterPanel, BorderLayout.WEST);
        controls.add(actionPanel, BorderLayout.EAST);
        add(controls, BorderLayout.SOUTH);
    }

    public void refreshReservations() {
        filterReservations(currentFilter);
    }

    public void filterReservations(String filter) {
        Customer customer = mainFrame.getCurrentCustomer();
        tableModel.setRowCount(0);

        if (customer == null) {
            statusLabel.setText("No customer is currently logged in.");
            return;
        }

        currentFilter = normalizeFilter(filter);
        List<ReservationReportRow> reservations = switch (currentFilter) {
            case FILTER_FUTURE -> reservationService.viewFutureReservations(customer.getCustomerID());
            case FILTER_PAST -> reservationService.viewPastReservations(customer.getCustomerID());
            default -> reservationService.viewAllReservations(customer.getCustomerID());
        };

        for (ReservationReportRow reservation : reservations) {
            tableModel.addRow(new Object[] {
                reservation.getTicketNumber(),
                reservation.getLegOrder(),
                blankIfNull(reservation.getFlightNumber()),
                blankIfNull(reservation.getLineID()),
                blankIfNull(reservation.getOriginPortID()),
                blankIfNull(reservation.getDestinationPortID()),
                formatDate(reservation.getDepartureDate()),
                formatTime(reservation.getDepartureTime()),
                formatTime(reservation.getArrivalTime()),
                blankIfNull(reservation.getSeatNumber()),
                blankIfNull(reservation.getTicketClass()),
                blankIfNull(reservation.getTripType()),
                formatMoney(reservation.getFareCost()),
                formatMoney(reservation.getBookingFee()),
                blankIfNull(reservation.getStatus())
            });
        }

        statusLabel.setText(buildStatusMessage(currentFilter, reservations.size()));
    }

    @Override
    public void addNotify() {
        super.addNotify();
        refreshReservations();
    }

    private void cancelSelectedReservation() {
        int row = table.getSelectedRow();
        if (row < 0) {
            throw new IllegalArgumentException("Select a reservation row first.");
        }

        int ticketNumber = (int) tableModel.getValueAt(row, 0);
        String status = String.valueOf(tableModel.getValueAt(row, 14));
        String departureDateText = String.valueOf(tableModel.getValueAt(row, 6));
        String ticketClass = String.valueOf(tableModel.getValueAt(row, 10));

        if ("Cancelled".equalsIgnoreCase(status)) {
            throw new IllegalArgumentException("That reservation is already cancelled.");
        }

        if (departureDateText.isBlank() || LocalDate.parse(departureDateText).isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Only future reservations can be cancelled.");
        }

        String normalizedTicketClass = ReservationService.normalizeTicketClass(ticketClass);
        if (!ReservationService.TICKET_CLASS_BUSINESS.equals(normalizedTicketClass)
                && !ReservationService.TICKET_CLASS_FIRST.equals(normalizedTicketClass)) {
            throw new IllegalArgumentException("Only business or first class reservations can be cancelled.");
        }

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Cancel reservation for ticket #" + ticketNumber + "?",
            "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION
        );
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        reservationService.cancelReservation(ticketNumber);
        refreshReservations();
        JOptionPane.showMessageDialog(this, "Reservation cancelled.");
    }

    private String normalizeFilter(String filter) {
        if (FILTER_FUTURE.equalsIgnoreCase(filter)) {
            return FILTER_FUTURE;
        }
        if (FILTER_PAST.equalsIgnoreCase(filter)) {
            return FILTER_PAST;
        }
        return FILTER_ALL;
    }

    private String buildStatusMessage(String filter, int count) {
        return switch (filter) {
            case FILTER_FUTURE -> "Showing " + count + " future reservation(s)";
            case FILTER_PAST -> "Showing " + count + " past reservation(s)";
            default -> "Showing " + count + " reservation(s)";
        };
    }

    private String formatDate(LocalDate date) {
        return date == null ? "" : DATE_FORMAT.format(date);
    }

    private String formatTime(LocalTime time) {
        return time == null ? "" : TIME_FORMAT.format(time);
    }

    private String formatMoney(BigDecimal amount) {
        return amount == null ? "" : "$" + amount.toPlainString();
    }

    private String blankIfNull(String value) {
        return value == null ? "" : value;
    }

    private void withGuard(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Reservation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
