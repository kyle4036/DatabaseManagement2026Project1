package travel.ui;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Font;

import travel.model.Customer;

/**
 * Landing page after a customer logs in
 * For now it just shows a welcome and the navigation buttons
 * TODO: Buttons are just placeholders rn, fill in later
 */
public class CustomerHomePanel extends JPanel {

    private final MainFrame mainFrame;
    private final JLabel welcomeLabel = new JLabel();

    public CustomerHomePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        welcomeLabel.setFont(new Font("Lucida Sans", Font.BOLD, 22));
        add(welcomeLabel, BorderLayout.NORTH);

        // Action buttons
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JButton searchBtn       = new JButton("Search Flights");
        JButton reservationsBtn = new JButton("My Reservations");
        JButton waitlistBtn     = new JButton("My Waitlist");
        JButton logoutBtn       = new JButton("Logout");

        searchBtn.addActionListener(e -> System.out.println("TODO: search screen"));
        reservationsBtn.addActionListener(e -> System.out.println("TODO: my reservations"));
        waitlistBtn.addActionListener(e -> System.out.println("TODO: waitlist"));
        logoutBtn.addActionListener(e -> onLogout());

        buttonPanel.add(searchBtn);
        buttonPanel.add(reservationsBtn);
        buttonPanel.add(waitlistBtn);
        buttonPanel.add(logoutBtn);

        JPanel center = new JPanel();
        center.add(buttonPanel);
        add(center, BorderLayout.CENTER);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        Customer c = mainFrame.getCurrentCustomer();
        if (c != null) {
            welcomeLabel.setText("Welcome, " + c.getFirstName() + " " + c.getLastName());
        }
    }

    private void onLogout() {
        mainFrame.clearSession();
        mainFrame.showScreen(Screen.LOGIN);
    }
}