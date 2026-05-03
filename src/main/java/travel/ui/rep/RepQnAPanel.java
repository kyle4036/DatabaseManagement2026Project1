package travel.ui.rep;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import travel.dao.QnADAO;
import travel.model.QnA;
import travel.ui.MainFrame;
import travel.ui.Screen;

public class RepQnAPanel extends JPanel {

    private final MainFrame mainFrame;
    private final QnADAO qnaDAO = new QnADAO();

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea questionDisplay;
    private JTextArea replyField;
    private JLabel statusLabel;

    // Track the currently selected questionID
    private int selectedQuestionID = -1;

    public RepQnAPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top: filter buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        JButton unansweredBtn = new JButton("Show Unanswered");
        JButton allBtn = new JButton("Show All");
        JButton backBtn = new JButton("Back");

        unansweredBtn.addActionListener(e -> loadUnanswered());
        allBtn.addActionListener(e -> loadAll());
        backBtn.addActionListener(e -> mainFrame.showScreen(Screen.REP_HOME));

        statusLabel = new JLabel(" ");

        topPanel.add(unansweredBtn);
        topPanel.add(allBtn);
        topPanel.add(backBtn);
        topPanel.add(statusLabel);

        add(topPanel, BorderLayout.NORTH);

        // Center: split between table and reply area
        // Left side: question list
        String[] columns = {"ID", "Customer ID", "Question", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setMaxWidth(90);
        table.getSelectionModel().addListSelectionListener(e -> onRowSelected());

        JScrollPane tableScroll = new JScrollPane(table);

        // Right side: question detail + reply
        JPanel replyPanel = new JPanel();
        replyPanel.setLayout(new BoxLayout(replyPanel, BoxLayout.Y_AXIS));

        questionDisplay = new JTextArea(4, 30);
        questionDisplay.setEditable(false);
        questionDisplay.setLineWrap(true);
        questionDisplay.setWrapStyleWord(true);
        questionDisplay.setBorder(BorderFactory.createTitledBorder("Question"));

        replyField = new JTextArea(4, 30);
        replyField.setLineWrap(true);
        replyField.setWrapStyleWord(true);
        replyField.setBorder(BorderFactory.createTitledBorder("Your Reply"));

        JButton submitReply = new JButton("Submit Reply");
        submitReply.addActionListener(e -> onSubmitReply());

        replyPanel.add(new JScrollPane(questionDisplay));
        replyPanel.add(Box.createVerticalStrut(8));
        replyPanel.add(new JScrollPane(replyField));
        replyPanel.add(Box.createVerticalStrut(8));
        replyPanel.add(submitReply);

        // Split pane: table on left, reply on right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableScroll, replyPanel);
        splitPane.setDividerLocation(400);

        add(splitPane, BorderLayout.CENTER);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        loadUnanswered();
    }

    private void loadAll() {
        populateTable(qnaDAO.findAll());
        statusLabel.setText("Showing all questions");
    }

    private void loadUnanswered() {
        populateTable(qnaDAO.findUnanswered());
        statusLabel.setText("Showing unanswered questions");
    }

    private void populateTable(List<QnA> questions) {
        tableModel.setRowCount(0);
        questionDisplay.setText("");
        replyField.setText("");
        selectedQuestionID = -1;

        for (QnA q : questions) {
            String status = q.getAnswer() != null ? "Answered" : "Pending";
            tableModel.addRow(new Object[]{
                q.getQuestionID(), q.getCustomerID(), q.getQuestion(), status
            });
        }
    }

    private void onRowSelected() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        selectedQuestionID = (int) tableModel.getValueAt(row, 0);
        String question = (String) tableModel.getValueAt(row, 2);
        String status = (String) tableModel.getValueAt(row, 3);

        questionDisplay.setText(question);

        if ("Answered".equals(status)) {
            // Show existing answer in the reply field
            List<QnA> all = qnaDAO.findAll();
            for (QnA q : all) {
                if (q.getQuestionID() == selectedQuestionID) {
                    replyField.setText(q.getAnswer());
                    break;
                }
            }
        } else {
            replyField.setText("");
        }
    }

    private void onSubmitReply() {
        if (selectedQuestionID < 0) {
            JOptionPane.showMessageDialog(this, "Select a question first.");
            return;
        }

        String answer = replyField.getText().trim();
        if (answer.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Reply cannot be empty.");
            return;
        }

        if (mainFrame.getCurrentEmployee() == null) return;

        int employeeID = mainFrame.getCurrentEmployee().getEmployeeID();
        qnaDAO.answer(selectedQuestionID, employeeID, answer);

        JOptionPane.showMessageDialog(this, "Reply submitted.");
        loadUnanswered(); // refresh to remove the answered question from the list
    }
}
