package travel.ui.admin;

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

public class AdminHomePanel extends JPanel {

    private final MainFrame mainFrame;
    private final JLabel welcomeLabel = new JLabel();

    public AdminHomePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        welcomeLabel.setFont(new Font("Lucida Sans", Font.BOLD, 22));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JButton manageUsers   = new JButton("Manage Customers / Employees");
        JButton salesReport   = new JButton("Sales Report");
        JButton revenueReport = new JButton("Revenue Reports");
        JButton activeFlights = new JButton("Most Active Flights");
        JButton logoutBtn     = new JButton("Logout");

        manageUsers.addActionListener(e -> onManageUsers());
        salesReport.addActionListener(e -> onSalesReport());
        revenueReport.addActionListener(e -> onRevenueReport());
        activeFlights.addActionListener(e -> onActiveFlights());
        logoutBtn.addActionListener(e -> onLogout());

        buttonPanel.add(manageUsers);
        buttonPanel.add(salesReport);
        buttonPanel.add(revenueReport);
        buttonPanel.add(activeFlights);
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
            welcomeLabel.setText("Admin: " + e.getFirstName() + " " + e.getLastName());
        }
    }

    private void onLogout() {
        mainFrame.clearSession();
        mainFrame.showScreen(Screen.LOGIN);
    }

    private void onManageUsers() {
        mainFrame.showScreen(Screen.ADMIN_MANAGE_USERS);
    }

    private void onSalesReport() {
        mainFrame.showScreen(Screen.ADMIN_SALES_REPORT);
    }

    private void onRevenueReport() {
        mainFrame.showScreen(Screen.ADMIN_REVENUE_REPORT);
    }

    private void onActiveFlights() {
        mainFrame.showScreen(Screen.ADMIN_ACTIVE_FLIGHTS);
    }
}
