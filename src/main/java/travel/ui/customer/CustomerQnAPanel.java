package travel.ui.customer;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import travel.dao.QnADAO;
import travel.model.QnA;
import travel.ui.MainFrame;
import travel.ui.Screen;

public class CustomerQnAPanel extends JPanel {

    private final MainFrame mainFrame;
    private final QnADAO qnaDAO = new QnADAO();

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JTextArea answerDisplay;

    public CustomerQnAPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top: search bar + buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        JButton showAllBtn = new JButton("Show All");
        JButton myQuestionsBtn = new JButton("My Questions");
        JButton askBtn = new JButton("Ask a Question");
        JButton backBtn = new JButton("Back");

        searchBtn.addActionListener(e -> onSearch());
        showAllBtn.addActionListener(e -> loadAll());
        myQuestionsBtn.addActionListener(e -> loadMyQuestions());
        askBtn.addActionListener(e -> onAskQuestion());
        backBtn.addActionListener(e -> mainFrame.showScreen(Screen.CUSTOMER_HOME));

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        topPanel.add(showAllBtn);
        topPanel.add(myQuestionsBtn);
        topPanel.add(askBtn);
        topPanel.add(backBtn);

        add(topPanel, BorderLayout.NORTH);

        // Center: questions table
        String[] columns = {"ID", "Question", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> onRowSelected());

        // Hide the ID column from display but keep it in the model for lookup
        table.getColumnModel().getColumn(0).setMaxWidth(50);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom: answer display area
        answerDisplay = new JTextArea(5, 40);
        answerDisplay.setEditable(false);
        answerDisplay.setLineWrap(true);
        answerDisplay.setWrapStyleWord(true);
        answerDisplay.setBorder(BorderFactory.createTitledBorder("Answer"));
        add(new JScrollPane(answerDisplay), BorderLayout.SOUTH);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        loadAll();
    }

    private void loadAll() {
        populateTable(qnaDAO.findAll());
    }

    private void loadMyQuestions() {
        if (mainFrame.getCurrentCustomer() == null) return;
        int id = mainFrame.getCurrentCustomer().getCustomerID();
        populateTable(qnaDAO.findByCustomer(id));
    }

    private void onSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadAll();
        } else {
            populateTable(qnaDAO.searchByKeyword(keyword));
        }
    }

    private void populateTable(List<QnA> questions) {
        tableModel.setRowCount(0);
        answerDisplay.setText("");
        for (QnA q : questions) {
            String status = q.getAnswer() != null ? "Answered" : "Pending";
            tableModel.addRow(new Object[]{q.getQuestionID(), q.getQuestion(), status});
        }
    }

    private void onRowSelected() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        int questionID = (int) tableModel.getValueAt(row, 0);
        List<QnA> all = qnaDAO.findAll();
        for (QnA q : all) {
            if (q.getQuestionID() == questionID) {
                if (q.getAnswer() != null) {
                    answerDisplay.setText(q.getAnswer());
                } else {
                    answerDisplay.setText("(No answer yet)");
                }
                break;
            }
        }
    }

    private void onAskQuestion() {
        if (mainFrame.getCurrentCustomer() == null) return;

        String question = JOptionPane.showInputDialog(this,
            "Type your question:", "Ask a Question", JOptionPane.PLAIN_MESSAGE);

        if (question != null && !question.trim().isEmpty()) {
            QnA q = new QnA();
            q.setCustomerID(mainFrame.getCurrentCustomer().getCustomerID());
            q.setQuestion(question.trim());
            qnaDAO.insert(q);
            loadAll(); // refresh the table
            JOptionPane.showMessageDialog(this, "Question posted.");
        }
    }
}
