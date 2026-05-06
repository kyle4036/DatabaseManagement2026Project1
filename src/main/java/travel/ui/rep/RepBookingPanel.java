package travel.ui.rep;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import travel.dao.AircraftDAO;
import travel.dao.CustomerDAO;
import travel.model.Aircraft;
import travel.model.Customer;
import travel.model.Flight;
import travel.services.BookingService;
import travel.ui.MainFrame;
import travel.ui.Screen;

public class RepBookingPanel extends JPanel {

    private final MainFrame mainFrame;
    private final BookingService bookingService = new BookingService();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final AircraftDAO aircraftDAO = new AircraftDAO();

    private JTextField usernameField;
    private JLabel customerLabel;
    private Customer selectedCustomer;

    private JTextField departureField;
    private JTextField arrivalField;
    private JTextField dateField;
    private JCheckBox roundTripBox;

    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private List<Flight> flightList;

    public RepBookingPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // TOP: customer lookup + search fields
        JPanel topPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        topPanel.setBorder(BorderFactory.createTitledBorder("Book Flight for Customer"));

        // Customer lookup
        usernameField = new JTextField(15);
        JButton lookupBtn = new JButton("Look Up");
        lookupBtn.addActionListener(e -> onLookupCustomer());

        customerLabel = new JLabel("No customer selected");
        customerLabel.setForeground(Color.RED);

        JPanel lookupRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        lookupRow.add(usernameField);
        lookupRow.add(lookupBtn);

        topPanel.add(new JLabel("Customer Username:"));
        topPanel.add(lookupRow);
        topPanel.add(new JLabel("Customer:"));
        topPanel.add(customerLabel);

        // Search fields
        departureField = new JTextField(5);
        arrivalField = new JTextField(5);
        dateField = new JTextField(10);
        roundTripBox = new JCheckBox("Round Trip");

        topPanel.add(new JLabel("Departure Airport:"));
        topPanel.add(departureField);
        topPanel.add(new JLabel("Arrival Airport:"));
        topPanel.add(arrivalField);
        topPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        topPanel.add(dateField);
        topPanel.add(new JLabel(""));
        topPanel.add(roundTripBox);

        JButton searchBtn = new JButton("Search Flights");
        searchBtn.addActionListener(e -> onSearch());
        JButton seeAllBtn = new JButton("See All Flights");
        seeAllBtn.addActionListener(e -> onSeeAll());

        topPanel.add(searchBtn);
        topPanel.add(seeAllBtn);

        add(topPanel, BorderLayout.NORTH);

        // CENTER: results table
        String[] columns = {"Flight #", "From", "To", "Depart", "Arrive", "Type", "Seats Taken"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        resultsTable = new JTable(tableModel);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(resultsTable), BorderLayout.CENTER);

        // BOTTOM: book + back buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        JButton bookBtn = new JButton("Book Selected Flight");
        bookBtn.addActionListener(e -> onBook());
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> mainFrame.showScreen(Screen.REP_HOME));

        bottomPanel.add(bookBtn);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void onLookupCustomer() {
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            customerLabel.setText("Enter a username first");
            customerLabel.setForeground(Color.RED);
            selectedCustomer = null;
            return;
        }

        Customer c = customerDAO.findByUsername(username);
        if (c == null) {
            customerLabel.setText("Customer not found: " + username);
            customerLabel.setForeground(Color.RED);
            selectedCustomer = null;
        } else {
            selectedCustomer = c;
            customerLabel.setText(c.getFirstName() + " " + c.getLastName()
                + " (ID: " + c.getCustomerID() + ")");
            customerLabel.setForeground(new Color(0, 128, 0));
        }
    }

    private void onSearch() {
        String dep = departureField.getText().trim().toUpperCase();
        String arr = arrivalField.getText().trim().toUpperCase();
        String date = dateField.getText().trim();

        if (dep.isEmpty() && arr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter at least one airport.");
            return;
        }

        List<Flight> flights;
        if (!dep.isEmpty() && !arr.isEmpty()) {
            flights = bookingService.findByRoute(dep, arr);
        } else if (!dep.isEmpty()) {
            flights = bookingService.findByAirport(dep);
            flights.removeIf(f -> f.getDestinationPortID().equals(dep));
        } else {
            flights = bookingService.findByAirport(arr);
            flights.removeIf(f -> f.getDeparturePortID().equals(arr));
        }

        if (!date.isEmpty()) {
            bookingService.removeFlightDates(flights, date);
        }

        if (roundTripBox.isSelected() && !dep.isEmpty() && !arr.isEmpty()) {
            bookingService.removeFlightNotRoundTrip(flights, dep, arr);
        }

        flightList = flights;
        updateTable();
    }

    private void onSeeAll() {
        flightList = bookingService.getAllFlights();
        updateTable();
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        if (flightList == null || flightList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No flights found.");
            return;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        for (Flight f : flightList) {
            tableModel.addRow(new Object[]{
                f.getFlightNumber(),
                f.getDeparturePortID(),
                f.getDestinationPortID(),
                f.getDepartureTime() != null ? f.getDepartureTime().format(fmt) : "",
                f.getArrivalTime() != null ? f.getArrivalTime().format(fmt) : "",
                f.getFlightType(),
                f.getSeatsTaken()
            });
        }
    }

    private void onBook() {
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this, "Look up a customer first.");
            return;
        }

        int row = resultsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a flight from the table.");
            return;
        }

        String date = dateField.getText().trim();
        if (date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a date first.");
            return;
        }

        Flight selectedFlight = flightList.get(row);

        // Check capacity
        Aircraft aircraft = aircraftDAO.findByKey(selectedFlight.getCraftID());
        if (aircraft != null && selectedFlight.getSeatsTaken() >= aircraft.getCapacity()) {
            JOptionPane.showMessageDialog(this, "Flight is full. Cannot book.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Book " + selectedFlight.getLineID() + " " + selectedFlight.getFlightNumber()
            + " on " + date + " for " + selectedCustomer.getFirstName()
            + " " + selectedCustomer.getLastName() + "?",
            "Confirm Booking", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            String[] classes = {"Economy", "Business", "First"};
            String ticketClass = (String) JOptionPane.showInputDialog(this,
                "Select ticket class:", "Ticket Class",
                JOptionPane.PLAIN_MESSAGE, null, classes, "Economy");
            if (ticketClass == null) return;

            bookingService.bookByFlight(selectedFlight, selectedCustomer, date, roundTripBox.isSelected(), ticketClass);
            JOptionPane.showMessageDialog(this,
                "Flight booked for " + selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Booking failed: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
