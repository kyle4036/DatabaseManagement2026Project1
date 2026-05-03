package travel.ui.customer;

import javax.swing.*;
import java.awt.BorderLayout;

import travel.ui.*;

public class CustomerSearchTicketPanel extends JPanel{

    private final MainFrame mainFrame;
    public CustomerSearchTicketPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI(){
        setLayout(new BorderLayout(20,20));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        
    }
    
}
