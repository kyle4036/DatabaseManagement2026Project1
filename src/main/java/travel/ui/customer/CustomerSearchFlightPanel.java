package travel.ui.customer;


import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JButton;

import java.awt.GridLayout;

import java.util.List;

import travel.services.BookingService;
import travel.ui.MainFrame;
import travel.model.*;

public class CustomerSearchFlightPanel extends JPanel {

    private final MainFrame mainFrame;
    private final JLabel welcomeLabel = new JLabel();
    private final BookingService bService = new BookingService();

    public CustomerSearchFlightPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        //setLayout(new BorderLayout(20, 20));
        //setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Search Flights ");
        title.setFont(new Font("Lucida Sans", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JButton searchButton = new JButton("Search Flights");

        searchButton.addActionListener(e -> withGuard(this::searchPressed));

        this.add(searchButton);
    }

    private void searchPressed(){
        List<Flight> flights = promptSearch();
        if(flights == null){
            return;
        }

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
