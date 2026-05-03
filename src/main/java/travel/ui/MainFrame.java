package travel.ui;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import travel.model.Customer;
import travel.model.Employee;
import travel.ui.admin.AdminHomePanel;
import travel.ui.customer.CustomerHomePanel;
import travel.ui.customer.CustomerReservationsPanel;
import travel.ui.rep.RepHomePanel;


/**
 * The single window for the entire app. Holds a CardLayout container with
 * one panel per "screen". Panels call mainFrame.showScreen(name) to navigate.
 */
public class MainFrame extends JFrame {

    // Layout that lets us stack panels and show one at a time
    private final CardLayout cards = new CardLayout();
    private final JPanel container = new JPanel(cards);

    // Session state — who is currently logged in
    private Customer currentCustomer;
    private Employee currentEmployee;

    /*
     * `MainFrame` sets up a Frame that will host a single panel and switches
     *  between these panels depending on what the USER does.
     *  EXAMPLE: when a user clicks the login with a valid user+pass, it will switch from the
     *  `loginPanel` to the landing page panel (depending on what type of USER you are)
     */
    public MainFrame() {
        // Build each panel, give it a reference to this main frame
        // we can then use mainFrame.showScreen("...") on the name to add .
        LoginPanel login = new LoginPanel(this);
        CustomerHomePanel customerHome = new CustomerHomePanel(this);
        AdminHomePanel adminHome = new AdminHomePanel(this);
        RepHomePanel repHome = new RepHomePanel(this);

        // add each panel under an indexed name, CardLayout will use these names to swap.
        container.add(login, Screen.LOGIN);
        container.add(customerHome, Screen.CUSTOMER_HOME);
        //this.addCustomerPanels();
        container.add(adminHome, Screen.ADMIN_HOME);
        container.add(repHome, Screen.REP_HOME);

        // This is initializing the MainFrame constructor to always start on the LOGIN page
        cards.show(container, Screen.LOGIN);

        add(container);
        setTitle("Travel Reservation System");
        setSize(800, 600);
        setMinimumSize(new Dimension(500, 400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void showScreen(String name) {
        cards.show(container, name);
    }

    // Panels will read these to know who's logged in.

    public Customer getCurrentCustomer() { return currentCustomer; }
    public Employee getCurrentEmployee() { return currentEmployee; }

    public void setCurrentCustomer(Customer c) {
        this.currentCustomer = c;
        this.currentEmployee = null;
    }

    public void setCurrentEmployee(Employee e) {
        this.currentEmployee = e;
        this.currentCustomer = null;
    }

    public void clearSession() {
        this.currentCustomer = null;
        this.currentEmployee = null;
    }

    private void addCustomerPanels(){
        CustomerHomePanel chp = new CustomerHomePanel(this);
        CustomerReservationsPanel crp = new CustomerReservationsPanel(this);
        
    }
}