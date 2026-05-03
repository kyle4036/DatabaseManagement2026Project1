package travel.ui.customer;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import travel.ui.MainFrame;

public class CustomerReservationsPanel extends JPanel{

    private final MainFrame mainFrame;
    
    public CustomerReservationsPanel(MainFrame mainFrame){
        this.mainFrame = mainFrame;
        buildUI();
    }
    private void buildUI(){
        setLayout(new BorderLayout(20,20));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));


    }
}