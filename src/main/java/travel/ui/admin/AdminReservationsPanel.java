package travel.ui.admin;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import travel.model.ReservationReportRow;
import travel.services.AdminService;
import travel.ui.MainFrame;
import travel.ui.Screen;

public class AdminReservationsPanel extends JPanel {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final MainFrame mainFrame;
    private final AdminService adminService = new AdminService();
    private final DefaultTableModel tableModel = new DefaultTableModel(
        new Object[] {
            "Ticket #", "Customer ID", "Customer", "Flight #", "Airline", "Leg",
            "From", "To", "Departure Date", "Departure Time", "Arrival Time",
            "Seat", "Class", "Trip Type", "Status", "Fare", "Booking Fee", "Purchase Time"
        },
        0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JLabel statusLabel = new JLabel("Run a reservation search.");

    public AdminReservationsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Reservations");
        title.setFont(new Font("Lucida Sans", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton byFlightBtn = new JButton("By Flight + Airline");
        JButton byCustomerBtn = new JButton("By Customer Name");
        JButton backBtn = new JButton("Back");

        byFlightBtn.addActionListener(e -> withGuard(this::runByFlight));
        byCustomerBtn.addActionListener(e -> withGuard(this::runByCustomer));
        backBtn.addActionListener(e -> mainFrame.showScreen(Screen.ADMIN_HOME));

        JPanel actions = new JPanel(new GridLayout(0, 1, 10, 10));
        actions.add(byFlightBtn);
        actions.add(byCustomerBtn);
        actions.add(backBtn);

        JPanel south = new JPanel(new BorderLayout(10, 10));
        south.add(statusLabel, BorderLayout.CENTER);
        south.add(actions, BorderLayout.EAST);
        add(south, BorderLayout.SOUTH);
    }

    private void runByFlight() {
        JTextField flightNumber = new JTextField();
        JTextField lineId = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Flight Number"));
        panel.add(flightNumber);
        panel.add(new JLabel("Airline ID"));
        panel.add(lineId);

        int choice = JOptionPane.showConfirmDialog(this, panel, "Reservations by Flight + Airline", JOptionPane.OK_CANCEL_OPTION);
        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        List<ReservationReportRow> results = adminService.reservationsByFlightNumber(
            flightNumber.getText().trim(),
            lineId.getText().trim()
        );
        populateTable(results, "flight " + flightNumber.getText().trim() + " / airline " + lineId.getText().trim());
    }

    private void runByCustomer() {
        String name = JOptionPane.showInputDialog(this, "Customer Name");
        if (name == null) {
            return;
        }

        List<ReservationReportRow> results = adminService.reservationsByCustomerName(name.trim());
        populateTable(results, "customer name \"" + name.trim() + "\"");
    }

    private void populateTable(List<ReservationReportRow> rows, String criteria) {
        tableModel.setRowCount(0);

        for (ReservationReportRow row : rows) {
            tableModel.addRow(new Object[] {
                row.getTicketNumber(),
                row.getCustomerID(),
                valueOrBlank(row.getCustomerName()),
                valueOrBlank(row.getFlightNumber()),
                valueOrBlank(row.getLineID()),
                row.getLegOrder(),
                valueOrBlank(row.getOriginPortID()),
                valueOrBlank(row.getDestinationPortID()),
                formatDate(row.getDepartureDate()),
                formatTime(row.getDepartureTime()),
                formatTime(row.getArrivalTime()),
                valueOrBlank(row.getSeatNumber()),
                valueOrBlank(row.getTicketClass()),
                valueOrBlank(row.getTripType()),
                valueOrBlank(row.getStatus()),
                row.getFareCost() == null ? "" : row.getFareCost().toString(),
                row.getBookingFee() == null ? "" : row.getBookingFee().toString(),
                formatDateTime(row.getPurchaseTime())
            });
        }

        statusLabel.setText("Showing " + rows.size() + " reservation(s) for " + criteria + ".");
    }

    private String formatDate(LocalDate value) {
        return value == null ? "" : DATE_FORMAT.format(value);
    }

    private String formatTime(LocalTime value) {
        return value == null ? "" : TIME_FORMAT.format(value);
    }

    private String formatDateTime(LocalDateTime value) {
        return value == null ? "" : DATE_TIME_FORMAT.format(value);
    }

    private String valueOrBlank(String value) {
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
