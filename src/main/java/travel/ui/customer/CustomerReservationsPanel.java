package travel.ui.customer;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import travel.services.ReservationService;
import travel.ui.MainFrame;
import travel.ui.Screen;

public class CustomerReservationsPanel extends JPanel{

    private final MainFrame mainFrame;
    private final ReservationService reservationService = new ReservationService();


    public CustomerReservationsPanel(MainFrame mainFrame){
        this.mainFrame = mainFrame;
        buildUI();
    }
    private void buildUI(){
        setLayout(new BorderLayout(20,20));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel title = new JLabel("My Reservations");
        title.setFont(new Font("Lucida Sans", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel resultPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        JButton byCustomerBtn = new JButton("By Customer ID");
        JButton byFlightBtn = new JButton("By Flight + Airline");
        JButton byAirlineBtn = new JButton("By Airline");
        JButton topCustomerBtn = new JButton("Most Revenue Customer");
        JButton backBtn = new JButton("Back");

        backBtn.addActionListener(e -> mainFrame.showScreen(Screen.CUSTOMER_HOME));

        JPanel actions = new JPanel(new GridLayout(0, 1, 10, 10));
        actions.add(byCustomerBtn);
        actions.add(byFlightBtn);
        actions.add(byAirlineBtn);
        actions.add(topCustomerBtn);
        actions.add(backBtn);

        JPanel center = new JPanel(new BorderLayout(20, 20));
        center.add(resultPanel, BorderLayout.CENTER);
        center.add(actions, BorderLayout.East);
        add(center, BorderLayout.CENTER);
    }

    public void refreshReservations(){

    }

    public void filterReservations(String filter){

    }

    @Override
    public void addNotify(){
        super.addNotify();
        refreshReservations();
    }

    private void withGuard(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Reservation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}