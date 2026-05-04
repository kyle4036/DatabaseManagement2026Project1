package travel.ui.rep;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import travel.dao.WaitingListDAO;
import travel.ui.MainFrame;
import travel.ui.Screen;

public class RepWaitlistPanel extends JPanel {

    private final MainFrame mainFrame;
    private final WaitingListDAO waitinglistDAO = new WaitingListDAO();
    private DefaultTableModel tableModel;

    public RepWaitlistPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("View Flight Waitlist");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JLabel flightNumberLabel = new JLabel("Flight Number:");
        JTextField flightNumberField = new JTextField(15);

        JLabel lineIDLabel = new JLabel("Airline ID:");
        JTextField lineIDField = new JTextField(2);

        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");

        searchPanel.add(flightNumberLabel);
        searchPanel.add(flightNumberField);
        searchPanel.add(lineIDLabel);
        searchPanel.add(lineIDField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        centerPanel.add(searchPanel, BorderLayout.NORTH);
        String[] columns = {
                "Position",
                "Customer ID",
                "Customer Name",
                "Flight Number",
                "Line ID",
                "Request Time"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable waitlistTable = new JTable(tableModel);
        waitlistTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(waitlistTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton backButton = new JButton("Back");
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> {
            String flightNumber = flightNumberField.getText().trim();
            String lineID = lineIDField.getText().trim();
            if (flightNumber.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this.mainFrame,
                        "Please enter a flight number AND/OR airline ID.",
                        "Missing Flight Number AND/OR Airline ID",
                        JOptionPane.WARNING_MESSAGE);
            }

            loadWaitingListTable(flightNumber, lineID);
        });

        clearButton.addActionListener(e -> {
            flightNumberField.setText("");
            lineIDField.setText("");
            tableModel.setRowCount(0);
        });

        backButton.addActionListener(e -> {
            mainFrame.showScreen(Screen.REP_HOME);
        });
    }

    private void loadWaitingListTable(String flightNumber, String lineID) {
        tableModel.setRowCount(0);

        List<Object[]> rows = waitinglistDAO.findBy(flightNumber, lineID);

        for (Object[] row : rows) {
            tableModel.addRow(row);
        }

    }
}
