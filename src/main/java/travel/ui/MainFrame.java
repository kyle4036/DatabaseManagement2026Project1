package travel.ui;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import travel.model.Customer;
import travel.model.Employee;
import travel.ui.admin.AdminHomePanel;
import travel.ui.admin.AdminManageUsersPanel;
import travel.ui.admin.AdminActiveFlightsPanel;
import travel.ui.admin.AdminReservationsPanel;
import travel.ui.admin.AdminRevenueReportPanel;
import travel.ui.admin.AdminSalesReportPanel;
import travel.ui.customer.CustomerHomePanel;
import travel.ui.customer.CustomerReservationsPanel;
import travel.ui.customer.CustomerSearchTicketPanel;
import travel.ui.customer.CustomerWaitListPanel;
import travel.ui.customer.CustomerQnAPanel;
import travel.ui.rep.RepHomePanel;
import travel.ui.rep.RepWaitlistPanel;
import travel.ui.rep.RepQnAPanel;
import travel.ui.rep.RepManagementPanel;
import travel.ui.rep.RepReservationPanel;
import travel.ui.rep.RepBookingPanel;

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

        addCustomerPanels();
        addAdminPanels();
        addRepPanels();

        LoginPanel login = new LoginPanel(this);
        container.add(login, Screen.LOGIN);
        cards.show(container, Screen.LOGIN);

        add(container);
        setTitle("Travel Reservation System");
        setSize(900, 600);
        setMinimumSize(new Dimension(900, 600));
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
        CustomerHomePanel         chp = new CustomerHomePanel(this);
        CustomerReservationsPanel crp = new CustomerReservationsPanel(this);
        CustomerSearchTicketPanel csp = new CustomerSearchTicketPanel(this);
        // CustomerWaitListPanel     cwp = new CustomerWaitListPanel(this);
        CustomerQnAPanel          cqp = new CustomerQnAPanel(this);

        container.add(chp, Screen.CUSTOMER_HOME);
        container.add(crp, Screen.CUSTOMER_RESERVE);
        container.add(csp, Screen.CUSTOMER_SEARCH);
        // container.add(cwp, Screen.CUSTOMER_WAITLIST);
        container.add(cqp, Screen.CUSTOMER_QNA);
    }

    private void addRepPanels() {

        RepHomePanel rhp = new RepHomePanel(this);
        RepManagementPanel rmp = new RepManagementPanel(this);
        RepBookingPanel rbp = new RepBookingPanel(this);
        RepQnAPanel rqp = new RepQnAPanel(this); 
        RepWaitlistPanel rwp = new RepWaitlistPanel(this);
        RepReservationPanel rrp = new RepReservationPanel(this);

        container.add(rhp, Screen.REP_HOME);
        container.add(rmp, Screen.REP_MANAGEMENT);
        container.add(rwp, Screen.REP_WAITLIST);
        container.add(rqp, Screen.REP_QNA);
        container.add(rrp, Screen.REP_EDIT_RESERVATION);
        container.add(rbp, Screen.REP_BOOKING);
    }

    private void addAdminPanels() {
        AdminHomePanel           ahp = new AdminHomePanel(this);
        AdminManageUsersPanel    amp = new AdminManageUsersPanel(this);
        AdminSalesReportPanel    asp = new AdminSalesReportPanel(this);
        AdminRevenueReportPanel  arp = new AdminRevenueReportPanel(this);
        AdminActiveFlightsPanel aafp = new AdminActiveFlightsPanel(this);
        AdminReservationsPanel  arsp = new AdminReservationsPanel(this);

        container.add(ahp, Screen.ADMIN_HOME);
        container.add(amp, Screen.ADMIN_MANAGE_USERS);
        container.add(asp, Screen.ADMIN_SALES_REPORT);
        container.add(arp, Screen.ADMIN_REVENUE_REPORT);
        container.add(aafp, Screen.ADMIN_ACTIVE_FLIGHTS);
        container.add(arsp, Screen.ADMIN_RESERVATIONS);
    }
}
