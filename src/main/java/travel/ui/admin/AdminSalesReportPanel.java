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

import travel.model.SalesReportRow;
import travel.services.AdminService;
import travel.ui.MainFrame;
import travel.ui.Screen;

public class AdminSalesReportPanel extends JPanel {

    private final MainFrame mainFrame;
    private final AdminService adminService = new AdminService();

    private final JTextField monthField = new JTextField();
    private final JTextField yearField = new JTextField();
    private final JLabel ticketsSoldValue = new JLabel("-");
    private final JLabel reservationsValue = new JLabel("-");
    private final JLabel fareRevenueValue = new JLabel("-");
    private final JLabel bookingFeesValue = new JLabel("-");
    private final JLabel totalRevenueValue = new JLabel("-");

    public AdminSalesReportPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Sales Report");
        title.setFont(new Font("Lucida Sans", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.add(new JLabel("Month (1-12)"));
        formPanel.add(monthField);
        formPanel.add(new JLabel("Year"));
        formPanel.add(yearField);
        formPanel.add(new JLabel("Tickets Sold"));
        formPanel.add(ticketsSoldValue);
        formPanel.add(new JLabel("Reservations Count"));
        formPanel.add(reservationsValue);
        formPanel.add(new JLabel("Total Fare Revenue"));
        formPanel.add(fareRevenueValue);
        formPanel.add(new JLabel("Total Booking Fees"));
        formPanel.add(bookingFeesValue);
        formPanel.add(new JLabel("Total Revenue"));
        formPanel.add(totalRevenueValue);

        JButton runReportBtn = new JButton("Run Report");
        JButton backBtn = new JButton("Back");
        runReportBtn.addActionListener(e -> runReport());
        backBtn.addActionListener(e -> mainFrame.showScreen(Screen.ADMIN_HOME));

        JPanel actions = new JPanel(new GridLayout(1, 2, 10, 10));
        actions.add(runReportBtn);
        actions.add(backBtn);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.add(formPanel, BorderLayout.CENTER);
        center.add(actions, BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);
    }

    private void runReport() {
        try {
            int month = parseInt(monthField.getText(), "Month");
            int year = parseInt(yearField.getText(), "Year");
            SalesReportRow row = adminService.monthlySalesSummary(month, year);

            if (row == null) {
                resetValues();
                JOptionPane.showMessageDialog(this, "No sales report data found for that month/year.", "Sales Report", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            ticketsSoldValue.setText(String.valueOf(row.getTicketsSold()));
            reservationsValue.setText(String.valueOf(row.getReservationsCount()));
            fareRevenueValue.setText(String.valueOf(row.getTotalFareRevenue()));
            bookingFeesValue.setText(String.valueOf(row.getTotalBookingFees()));
            totalRevenueValue.setText(String.valueOf(row.getTotalRevenue()));
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Sales Report Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int parseInt(String value, String label) {
        String trimmed = value == null ? "" : value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(label + " is required");
        }
        try {
            return Integer.parseInt(trimmed);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + " must be a number");
        }
    }

    private void resetValues() {
        ticketsSoldValue.setText("-");
        reservationsValue.setText("-");
        fareRevenueValue.setText("-");
        bookingFeesValue.setText("-");
        totalRevenueValue.setText("-");
    }
}
