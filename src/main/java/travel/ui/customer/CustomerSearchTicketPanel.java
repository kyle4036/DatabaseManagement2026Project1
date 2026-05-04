package travel.ui.customer;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;

import java.time.format.DateTimeFormatter; 
import java.util.List;

import travel.services.BookingService;
import travel.ui.MainFrame;
import travel.model.*;



public class CustomerSearchTicketPanel extends JPanel{

    private final MainFrame mainFrame;
    private final BookingService bService = new BookingService();
    private JTextField dateField;
    private JTextField departureField;
    private JTextField arrivalField;

    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JButton bookButton;

    private JPanel resultsPanel;

    private List<Flight> flightList;

    //private JPanel avaialbleFlights = null;

    public CustomerSearchTicketPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        flightList = bService.getAllFlights();
        tableModel = new DefaultTableModel();
        buildUI();
    }

    private void buildUI(){
        setLayout(new BorderLayout(20,20));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        add(createSearchPanel(), BorderLayout.NORTH);
        add(createResultsPanel(), BorderLayout.CENTER);

        JButton bookButton = new JButton("Book Selected Flight");
        bookButton.addActionListener(e -> withGuard(this::bookSelectedFlight));

        add(bookButton, BorderLayout.SOUTH);
    }

    private JPanel createSearchPanel(){
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Search Flights"));

        departureField = new JTextField();
        arrivalField = new JTextField();
        dateField = new JTextField();

        JButton searchButton = new JButton("Search Flights");
        searchButton.addActionListener(e -> withGuard(this::searchPressed));

        panel.add(new JLabel("Departure:"));
        panel.add(departureField);

        panel.add(new JLabel("Arrival:"));
        panel.add(arrivalField);

        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);

        panel.add(new JLabel()); // spacer
        panel.add(searchButton);

        updateTicketsPanel();

        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {
            "Flight #", "From", "To", "Departure", 
            "Arrival", "Type", "Seats Taken"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // prevents user editing
            }
        };

        resultsTable = new JTable(tableModel);
        resultsTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void searchPressed(){
        List<Flight> flights = bService.findByRoute(
            departureField.getText().trim(),
            arrivalField.getText().trim(),
            dateField.getText().trim()
        );

        flightList = flights;

        updateTicketsPanel();
    }

    private void updateTicketsPanel(){
        tableModel.setRowCount(0); // clear table

        if (flightList == null || flightList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No flights found.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Flight f : flightList) {
            tableModel.addRow(new Object[]{
                f.getFlightNumber(),
                f.getDeparturePortID(),
                f.getDestinationPortID(),
                f.getDepartureTime() != null ? f.getDepartureTime().format(formatter) : "",
                f.getArrivalTime() != null ? f.getArrivalTime().format(formatter) : "",
                f.getFlightType(),
                f.getSeatsTaken()
            });
        }
    }

    private void bookSelectedFlight() {
        int selectedRow = resultsTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flight.");
            return;
        }

        //String flightNumber = (String) tableModel.getValueAt(selectedRow, 0);

        bService.bookByFlight(flightList.get(selectedRow));

        JOptionPane.showMessageDialog(this, "Flight booked successfully.");
    }

    /*
    private void bookFlight(Flight flight) {
        bService.addTicket(flight);
        JOptionPane.showMessageDialog(this, "Flight booked successfully.");
    }*/

    /*
    private void buildUI(){
        setLayout(new BorderLayout(20,20));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JButton searchButton = new JButton("Search Flights");
        searchButton.addActionListener(e -> withGuard(this::searchPressed));

        this.add(searchButton);
 
    }
    
    private void searchPressed(){
        List<Flight> flights = promptSearch();
        if(flights == null){
            return;
        }

        this.updateTicketsPanel(flights);
    }

    private List<Flight> promptSearch(){
        JTextField dateField = new JTextField();
        JTextField departureField = new JTextField();
        JTextField arrivalField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0,2,8,8));

        panel.add(new JLabel("Date:"));
        panel.add(dateField);
        panel.add(new JLabel("Departure Airport:"));
        panel.add(departureField);
        panel.add(new JLabel("Arrival Airport:"));
        panel.add(arrivalField);
        
        int choice = JOptionPane.showConfirmDialog(this, panel, "Search Flights" , JOptionPane.OK_CANCEL_OPTION);
        if (choice != JOptionPane.OK_OPTION) {
            return null;
        }

        return bService.findByRoute(
                departureField.getText().trim(),
                arrivalField.getText().trim(),
                dateField.getText().trim());

    }

    private void updateTicketsPanel(List<Flight> flights){

    }

    */

    private void withGuard(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Operation Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

}
