package travel.ui.admin;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import travel.model.RevenueSummaryRow;
import travel.services.AdminService;
import travel.ui.MainFrame;
import travel.ui.Screen;

public class AdminRevenueReportPanel extends JPanel {

    private final MainFrame mainFrame;
    private final AdminService adminService = new AdminService();

    private final JLabel entityTypeValue = new JLabel("-");
    private final JLabel entityIdValue = new JLabel("-");
    private final JLabel entityNameValue = new JLabel("-");
    private final JLabel totalRevenueValue = new JLabel("-");

    public AdminRevenueReportPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Revenue Reports");
        title.setFont(new Font("Lucida Sans", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel resultPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        resultPanel.add(new JLabel("Entity Type"));
        resultPanel.add(entityTypeValue);
        resultPanel.add(new JLabel("Entity ID"));
        resultPanel.add(entityIdValue);
        resultPanel.add(new JLabel("Entity Name"));
        resultPanel.add(entityNameValue);
        resultPanel.add(new JLabel("Total Revenue"));
        resultPanel.add(totalRevenueValue);

        JButton byCustomerBtn = new JButton("By Customer ID");
        JButton byFlightBtn = new JButton("By Flight + Airline");
        JButton byAirlineBtn = new JButton("By Airline");
        JButton topCustomerBtn = new JButton("Most Revenue Customer");
        JButton backBtn = new JButton("Back");

        byCustomerBtn.addActionListener(e -> withGuard(this::runByCustomer));
        byFlightBtn.addActionListener(e -> withGuard(this::runByFlight));
        byAirlineBtn.addActionListener(e -> withGuard(this::runByAirline));
        topCustomerBtn.addActionListener(e -> withGuard(this::runTopCustomer));
        backBtn.addActionListener(e -> mainFrame.showScreen(Screen.ADMIN_HOME));

        JPanel actions = new JPanel(new GridLayout(0, 1, 10, 10));
        actions.add(byCustomerBtn);
        actions.add(byFlightBtn);
        actions.add(byAirlineBtn);
        actions.add(topCustomerBtn);
        actions.add(backBtn);

        JPanel center = new JPanel(new BorderLayout(20, 20));
        center.add(resultPanel, BorderLayout.CENTER);
        center.add(actions, BorderLayout.EAST);
        add(center, BorderLayout.CENTER);
    }

    private void runByCustomer() {
        Integer customerId = promptInt("Customer ID");
        if (customerId == null) {
            return;
        }
        setResult(adminService.revenueSummaryByCustomer(customerId));
    }

    private void runByFlight() {
        JTextField flightNumber = new JTextField();
        JTextField lineId = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Flight Number"));
        panel.add(flightNumber);
        panel.add(new JLabel("Airline ID"));
        panel.add(lineId);

        int choice = JOptionPane.showConfirmDialog(this, panel, "Revenue by Flight + Airline", JOptionPane.OK_CANCEL_OPTION);
        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        setResult(adminService.revenueSummaryByFlight(flightNumber.getText().trim(), lineId.getText().trim()));
    }

    private void runByAirline() {
        String lineId = JOptionPane.showInputDialog(this, "Airline ID");
        if (lineId == null) {
            return;
        }
        setResult(adminService.revenueSummaryByAirline(lineId.trim()));
    }

    private void runTopCustomer() {
        setResult(adminService.mostRevenueByCustomer());
    }

    private Integer promptInt(String label) {
        String raw = JOptionPane.showInputDialog(this, label);
        if (raw == null) {
            return null;
        }
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(label + " is required");
        }
        try {
            return Integer.parseInt(trimmed);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + " must be a number");
        }
    }

    private void setResult(RevenueSummaryRow row) {
        if (row == null) {
            entityTypeValue.setText("-");
            entityIdValue.setText("-");
            entityNameValue.setText("-");
            totalRevenueValue.setText("-");
            JOptionPane.showMessageDialog(this, "No revenue data found.", "Revenue Reports", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        entityTypeValue.setText(valueOrDash(row.getEntityType()));
        entityIdValue.setText(valueOrDash(row.getEntityID()));
        entityNameValue.setText(valueOrDash(row.getEntityName()));
        totalRevenueValue.setText(row.getTotalRevenue() == null ? "-" : row.getTotalRevenue().toString());
    }

    private String valueOrDash(String value) {
        return value == null || value.isEmpty() ? "-" : value;
    }

    private void withGuard(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Revenue Report Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
