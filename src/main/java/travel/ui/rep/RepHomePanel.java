package travel.ui.rep;


import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import travel.model.Employee;
import travel.ui.MainFrame;
import travel.ui.Screen;

public class RepHomePanel extends JPanel {

    private final MainFrame mainFrame;
    private final JLabel welcomeLabel = new JLabel();

    public RepHomePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        welcomeLabel.setFont(new Font("Lucida Sans", Font.BOLD, 22));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JButton bookForCustomer = new JButton("Book Flight for Customer");
        JButton editReservation = new JButton("Edit Reservation");
        JButton manageFlights = new JButton("Manage Flights / Aircrafts / Airports");
        JButton viewWaitlist = new JButton("View Flight Waitlist");
        JButton qnaBtn = new JButton("Questions & Answers");
        JButton logoutBtn = new JButton("Logout");

        // TODO: Complete the below actions 
        bookForCustomer.addActionListener(e -> System.out.println("TODO: book for customer"));
        editReservation.addActionListener(e -> System.out.println("TODO: edit reservation"));
        manageFlights.addActionListener(e -> System.out.println("TODO: manage flights"));
        viewWaitlist.addActionListener(e -> System.out.println("TODO: view waitlist"));
        qnaBtn.addActionListener(e -> onQnAPress());
        logoutBtn.addActionListener(e -> onLogout());

        buttonPanel.add(bookForCustomer);
        buttonPanel.add(editReservation);
        buttonPanel.add(manageFlights);
        buttonPanel.add(viewWaitlist);
        buttonPanel.add(qnaBtn);
        buttonPanel.add(logoutBtn);

        JPanel center = new JPanel();
        center.add(buttonPanel);
        add(center, BorderLayout.CENTER);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        Employee e = mainFrame.getCurrentEmployee();
        if (e != null) {
            welcomeLabel.setText("Customer Rep: " + e.getFirstName() + " " + e.getLastName());
        }
    }

    private void onQnAPress() {
        mainFrame.showScreen(Screen.REP_QNA);
    }

    private void onLogout() {
        mainFrame.clearSession();
        mainFrame.showScreen(Screen.LOGIN);
    }
}
