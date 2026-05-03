package travel.ui.customer;


import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import travel.ui.MainFrame;

public class CustomerSearchFlightPanel extends JPanel {

    private final MainFrame mainFrame;
    private final JLabel welcomeLabel = new JLabel();

    public CustomerSearchFlightPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        welcomeLabel.setFont(new Font("Lucida Sans", Font.BOLD, 22));
        add(welcomeLabel, BorderLayout.NORTH);

        JLabel title = new JLabel("Search Flights ");
    }

}
