package travel.ui.rep;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import travel.model.Aircraft;
import travel.model.Airport;
import travel.model.Flight;
import travel.services.RepresentativeService;
import travel.ui.MainFrame;
import travel.ui.Screen;

public class RepManagementPanel extends JPanel {

    private final MainFrame mainFrame;
    private final RepresentativeService repService = new RepresentativeService();

    public RepManagementPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Manage Aircrafts, Airports, and Flights");
        title.setFont(new Font("Lucida Sans", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));

        JButton addAircraft = new JButton("Add Aircraft");
        JButton updateAircraft = new JButton("Update Aircraft");
        JButton deleteAircraft = new JButton("Delete Aircraft");

        JButton addAirport = new JButton("Add Airport");
        JButton updateAirport = new JButton("Update Airport");
        JButton deleteAirport = new JButton("Delete Airport");

        JButton addFlight = new JButton("Add Flight");
        JButton updateFlight = new JButton("Update Flight");
        JButton deleteFlight = new JButton("Delete Flight");

        JButton backBtn = new JButton("Back");

        addAircraft.addActionListener(e -> withGuard(this::onAddAircraft));
        updateAircraft.addActionListener(e -> withGuard(this::onUpdateAircraft));
        deleteAircraft.addActionListener(e -> withGuard(this::onDeleteAircraft));

        addAirport.addActionListener(e -> withGuard(this::onAddAirport));
        updateAirport.addActionListener(e -> withGuard(this::onUpdateAirport));
        deleteAirport.addActionListener(e -> withGuard(this::onDeleteAirport));

        addFlight.addActionListener(e -> withGuard(this::onAddFlight));
        updateFlight.addActionListener(e -> withGuard(this::onUpdateFlight));
        deleteFlight.addActionListener(e -> withGuard(this::onDeleteFlight));

        backBtn.addActionListener(e -> mainFrame.showScreen(Screen.REP_HOME));

        buttonPanel.add(addAircraft);
        buttonPanel.add(updateAircraft);
        buttonPanel.add(deleteAircraft);

        buttonPanel.add(addAirport);
        buttonPanel.add(updateAirport);
        buttonPanel.add(deleteAirport);

        buttonPanel.add(addFlight);
        buttonPanel.add(updateFlight);
        buttonPanel.add(deleteFlight);

        buttonPanel.add(backBtn);

        JPanel center = new JPanel();
        center.add(buttonPanel);
        add(center, BorderLayout.CENTER);
    }

    private void onAddAircraft() {
        Aircraft aircraft = promptAircraft(false);
        if (aircraft == null) return;

        repService.addAircraft(aircraft);
        showInfo("Aircraft added successfully.");
    }

    private void onUpdateAircraft() {
        Aircraft aircraft = promptAircraft(true);
        if (aircraft == null) return;

        repService.updateAircraft(aircraft);
        showInfo("Aircraft updated successfully.");
    }

    private void onDeleteAircraft() {
        Integer aircraftID = promptIntId("Aircraft ID");
        if (aircraftID == null) return;

        repService.deleteAircraft(aircraftID);
        showInfo("Aircraft deleted successfully.");
    }

    private void onAddAirport() {
        Airport airport = promptAirport(false);
        if (airport == null) return;

        repService.addAirport(airport);
        showInfo("Airport added successfully.");
    }

    private void onUpdateAirport() {
        Airport airport = promptAirport(true);
        if (airport == null) return;

        repService.updateAirport(airport);
        showInfo("Airport updated successfully.");
    }

    private void onDeleteAirport() {
        String airportID = promptStringId("Airport ID");
        if (airportID == null) return;

        repService.deleteAirport(airportID);
        showInfo("Airport deleted successfully.");
    }

    private void onAddFlight() {
        Flight flight = promptFlight(false);
        if (flight == null) return;

        repService.addFlight(flight);
        showInfo("Flight added successfully.");
    }

    private void onUpdateFlight() {
        Flight flight = promptFlight(true);
        if (flight == null) return;

        repService.updateFlight(flight);
        showInfo("Flight updated successfully.");
    }

    private void onDeleteFlight() {
        JTextField flightNumberField = new JTextField();
        JTextField lineIdField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Flight Number"));
        panel.add(flightNumberField);
        panel.add(new JLabel("Line ID"));
        panel.add(lineIdField);

        int choice = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Delete Flight",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (choice != JOptionPane.OK_OPTION) return;

        String flightNumber = parseRequiredString(flightNumberField.getText(), "Flight Number");
        String lineID = parseRequiredString(lineIdField.getText(), "Line ID");

        repService.deleteFlight(flightNumber, lineID);
        showInfo("Flight deleted successfully.");
    }

    private Aircraft promptAircraft(boolean includeId) {
        JTextField aircraftIdField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField seatsField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));

        if (includeId) {
            panel.add(new JLabel("Aircraft ID"));
            panel.add(aircraftIdField);
        }

        panel.add(new JLabel("Aircraft Name"));
        panel.add(nameField);

        panel.add(new JLabel("Number of Seats"));
        panel.add(seatsField);

        int choice = JOptionPane.showConfirmDialog(
                this,
                panel,
                includeId ? "Update Aircraft" : "Add Aircraft",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (choice != JOptionPane.OK_OPTION) return null;

        Aircraft aircraft = new Aircraft();

        if (includeId) {
            aircraft.setCraftID(parseRequiredInt(aircraftIdField.getText(), "Aircraft ID"));
        }

        aircraft.setCapacity(parseRequiredInt(seatsField.getText(), "Number of Seats"));

        return aircraft;
    }

    private Airport promptAirport(boolean includeId) {
        JTextField airportIdField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField countryField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));

        panel.add(new JLabel("Airport ID"));
        panel.add(airportIdField);

        panel.add(new JLabel("Airport Name"));
        panel.add(nameField);

        panel.add(new JLabel("City"));
        panel.add(cityField);

        panel.add(new JLabel("Country"));
        panel.add(countryField);

        int choice = JOptionPane.showConfirmDialog(
                this,
                panel,
                includeId ? "Update Airport" : "Add Airport",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (choice != JOptionPane.OK_OPTION) return null;

        Airport airport = new Airport();

        airport.setPortID(parseRequiredString(airportIdField.getText(), "Airport ID"));
        airport.setName(parseRequiredString(nameField.getText(), "Airport Name"));
        airport.setCity(parseRequiredString(cityField.getText(), "City"));
        airport.setCountry(parseRequiredString(countryField.getText(), "Country"));

        return airport;
    }

    private Flight promptFlight(boolean includeId) {
    JTextField flightNumberField = new JTextField();
    JTextField lineIdField = new JTextField();
    JTextField aircraftIdField = new JTextField();
    JTextField departureAirportField = new JTextField();
    JTextField arrivalAirportField = new JTextField();

    // Time pickers — hour and minute dropdowns
    String[] hours = new String[24];
    for (int i = 0; i < 24; i++) hours[i] = String.format("%02d", i);
    String[] minutes = {"00", "15", "30", "45"};

    JComboBox<String> depHour = new JComboBox<>(hours);
    JComboBox<String> depMin  = new JComboBox<>(minutes);
    JComboBox<String> arrHour = new JComboBox<>(hours);
    JComboBox<String> arrMin  = new JComboBox<>(minutes);

    JPanel depTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
    depTimePanel.add(depHour); depTimePanel.add(new JLabel(":")); depTimePanel.add(depMin);

    JPanel arrTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
    arrTimePanel.add(arrHour); arrTimePanel.add(new JLabel(":")); arrTimePanel.add(arrMin);

    // Flight type dropdown
    JComboBox<String> flightTypeDropdown = new JComboBox<>(new String[]{"domestic", "international"});

    // Days checkboxes
    JCheckBox sun = new JCheckBox("S");
    JCheckBox mon = new JCheckBox("M", true);
    JCheckBox tue = new JCheckBox("T", true);
    JCheckBox wed = new JCheckBox("W", true);
    JCheckBox thu = new JCheckBox("T", true);
    JCheckBox fri = new JCheckBox("F", true);
    JCheckBox sat = new JCheckBox("S");

    JPanel daysPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
    daysPanel.add(sun); daysPanel.add(mon); daysPanel.add(tue);
    daysPanel.add(wed); daysPanel.add(thu); daysPanel.add(fri); daysPanel.add(sat);

    // Build the form
    JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));

    panel.add(new JLabel("Flight Number"));
    panel.add(flightNumberField);
    panel.add(new JLabel("Airline ID"));
    panel.add(lineIdField);
    panel.add(new JLabel("Aircraft ID"));
    panel.add(aircraftIdField);
    panel.add(new JLabel("Departure Airport"));
    panel.add(departureAirportField);
    panel.add(new JLabel("Arrival Airport"));
    panel.add(arrivalAirportField);
    panel.add(new JLabel("Departure Time"));
    panel.add(depTimePanel);
    panel.add(new JLabel("Arrival Time"));
    panel.add(arrTimePanel);
    panel.add(new JLabel("Flight Type"));
    panel.add(flightTypeDropdown);
    panel.add(new JLabel("Days (SMTWTFS)"));
    panel.add(daysPanel);

    int choice = JOptionPane.showConfirmDialog(
            this, panel,
            includeId ? "Update Flight" : "Add Flight",
            JOptionPane.OK_CANCEL_OPTION);

    if (choice != JOptionPane.OK_OPTION) return null;

    // Build bitmask from checkboxes
    String daysRunning = ""
        + (sun.isSelected() ? "1" : "0")
        + (mon.isSelected() ? "1" : "0")
        + (tue.isSelected() ? "1" : "0")
        + (wed.isSelected() ? "1" : "0")
        + (thu.isSelected() ? "1" : "0")
        + (fri.isSelected() ? "1" : "0")
        + (sat.isSelected() ? "1" : "0");

    // Build LocalTime from dropdowns
    LocalTime depTime = LocalTime.of(
        Integer.parseInt((String) depHour.getSelectedItem()),
        Integer.parseInt((String) depMin.getSelectedItem()));
    LocalTime arrTime = LocalTime.of(
        Integer.parseInt((String) arrHour.getSelectedItem()),
        Integer.parseInt((String) arrMin.getSelectedItem()));

    Flight flight = new Flight();
    flight.setFlightNumber(parseRequiredString(flightNumberField.getText(), "Flight Number"));
    flight.setLineID(parseRequiredString(lineIdField.getText(), "Airline ID"));
    flight.setCraftID(parseRequiredInt(aircraftIdField.getText(), "Aircraft ID"));
    flight.setDeparturePortID(parseRequiredString(departureAirportField.getText(), "Departure Airport"));
    flight.setDestinationPortID(parseRequiredString(arrivalAirportField.getText(), "Arrival Airport"));
    flight.setDepartureTime(depTime);
    flight.setArrivalTime(arrTime);
    flight.setFlightType((String) flightTypeDropdown.getSelectedItem());
    flight.setDaysRunning(daysRunning);
    flight.setSeatsTaken(0);

    return flight;
}    private Integer promptIntId(String label) {
        String raw = JOptionPane.showInputDialog(this, label);
        if (raw == null) return null;

        return parseRequiredInt(raw, label);
    }

    private String promptStringId(String label) {
        String raw = JOptionPane.showInputDialog(this, label);
        if (raw == null) return null;

        return parseRequiredString(raw, label);
    }

    private String parseRequiredString(String value, String label) {
        String trimmed = value == null ? "" : value.trim();

        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(label + " is required");
        }

        return trimmed;
    }
    private LocalTime parseRequiredTime(String value, String label) {
        String trimmed = parseRequiredString(value, label);

        try {
            return LocalTime.parse(trimmed); // expects HH:mm or HH:mm:ss
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(label + " must be in HH:mm format, like 14:30");
        }
    }

    private int parseRequiredInt(String value, String label) {
        String trimmed = parseRequiredString(value, label);

        try {
            return Integer.parseInt(trimmed);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + " must be a number");
        }
    }

    private double parseRequiredDouble(String value, String label) {
        String trimmed = parseRequiredString(value, label);

        try {
            return Double.parseDouble(trimmed);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + " must be a decimal number");
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
        JOptionPane.showMessageDialog(this, message, "Representative", JOptionPane.INFORMATION_MESSAGE);
    }
}
