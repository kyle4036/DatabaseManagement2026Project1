package travel.ui.customer;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Font;

import travel.model.Customer;
import travel.ui.MainFrame;
import travel.ui.Screen;
import travel.dao.WaitingListDAO;

/**
 * Landing page after a customer logs in
 * For now it just shows a welcome and the navigation buttons
 * TODO: Buttons are just placeholders rn, fill in later
 */
public class CustomerHomePanel extends JPanel {

    private final MainFrame mainFrame;
    private final JLabel welcomeLabel = new JLabel();
    private WaitingListDAO wDao = new WaitingListDAO();

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
        JButton qnaBtn          = new JButton("Questions and Answers");
        JButton logoutBtn       = new JButton("Logout");

        searchBtn.addActionListener(e -> searchBtnPressed());
        reservationsBtn.addActionListener(e -> reservationBtnPressed());
        waitlistBtn.addActionListener(e -> waitBtnPressed());
        qnaBtn.addActionListener(e -> qnaBtnPressed());
        logoutBtn.addActionListener(e -> onLogout());
        

        buttonPanel.add(searchBtn);
        buttonPanel.add(reservationsBtn);
        buttonPanel.add(waitlistBtn);
        buttonPanel.add(qnaBtn);
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
    private void searchBtnPressed(){
        mainFrame.showScreen(Screen.CUSTOMER_SEARCH);
    }
    private void reservationBtnPressed(){
        mainFrame.showScreen(Screen.CUSTOMER_RESERVE);  
    }
    private void waitBtnPressed(){
        mainFrame.showScreen(Screen.CUSTOMER_WAITLIST);
    }
    private void qnaBtnPressed() {
        mainFrame.showScreen(Screen.CUSTOMER_QNA);
    }
}
