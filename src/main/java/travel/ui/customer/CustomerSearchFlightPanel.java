package travel.ui.customer;


import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

import java.awt.GridLayout;

import travel.ui.MainFrame;

public class CustomerSearchFlightPanel extends JPanel {

    private final MainFrame mainFrame;
    private final JLabel welcomeLabel = new JLabel();

    public CustomerSearchFlightPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        welcomeLabel.setFont(new Font("Lucida Sans", Font.BOLD, 22));
        add(welcomeLabel, BorderLayout.NORTH);

        JLabel title = new JLabel("Search Flights ");
    }

    private void promptSearch(){
        JTextField dateField = new JTextField();
        JTextField timeField = new JTextField();
        JTextField departureField = new JTextField();
        JTextField arrivalField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0,2,8,8));

        panel.add(new JLabel("Date:"));
        panel.add(dateField);
        panel.add(new JLabel("Time:"));
        panel.add(timeField);
        panel.add(new JLabel("Departure Airport:"));
        panel.add(departureField);
        panel.add(new JLabel("Arrival Airport:"));
        panel.add(arrivalField);
        
        int choice = JOptionPane.showConfirmDialog(this, panel, "Search Flights" , JOptionPane.OK_CANCEL_OPTION);
    }

}
