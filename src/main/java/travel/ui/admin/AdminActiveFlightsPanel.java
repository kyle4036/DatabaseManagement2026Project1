package travel.ui.admin;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import travel.model.FlightSummaryRow;
import travel.services.AdminService;
import travel.ui.MainFrame;
import travel.ui.Screen;

public class AdminActiveFlightsPanel extends JPanel {

    private final MainFrame mainFrame;
    private final AdminService adminService = new AdminService();
    private final DefaultTableModel tableModel = new DefaultTableModel(
        new Object[] {"Flight #", "Airline", "Origin", "Destination", "Type", "Reservations"},
        0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public AdminActiveFlightsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Most Active Flights");
        title.setFont(new Font("Lucida Sans", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");
        refreshBtn.addActionListener(e -> loadFlights());
        backBtn.addActionListener(e -> mainFrame.showScreen(Screen.ADMIN_HOME));

        JPanel bottom = new JPanel();
        bottom.add(refreshBtn);
        bottom.add(backBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        loadFlights();
    }

    private void loadFlights() {
        tableModel.setRowCount(0);
        List<FlightSummaryRow> rows = adminService.mostActiveFlights();
        if (rows == null) {
            return;
        }
        for (FlightSummaryRow row : rows) {
            tableModel.addRow(new Object[] {
                row.getFlightNumber(),
                row.getLineID(),
                row.getOriginPortID(),
                row.getDestinationPortID(),
                row.getFlightType(),
                row.getReservationCount()
            });
        }
    }
}
