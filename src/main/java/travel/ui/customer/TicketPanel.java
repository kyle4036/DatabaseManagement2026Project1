package travel.ui.customer;

import javax.swing.*;
import java.awt.BorderLayout;

import travel.ui.*;
import travel.model.*;
import travel.services.BookingService;

public class TicketPanel extends JPanel{ 
       private JLabel ticketInfo = null; 
       private BookingService bService = null;
       private Ticket ticket = null;
       private Customer c = null;
       private final MainFrame mainFrame;
       public TicketPanel(Flight flight, MainFrame mainFrame){
              this.mainFrame = mainFrame;
              bService = new BookingService();
              
              ticketInfo = new JLabel(flight.getDeparturePortID() + " :" + 
                                      flight.getDestinationPortID() + " :");

              ticket = new Ticket(
                     -1,
                     c.getCustomerID(),
                     null,
                     25.00,

              );

              JButton buyButton = new JButton("Buy Ticket");
              buyButton.addActionListener(e -> buyBtnPressed());

              this.add(ticketInfo);
              this.add(buyButton);
       }

       private void buyBtnPressed(){
              bService.addTicket(ticket);
       }

       @Override
       public void addNotify() {
              super.addNotify();
              Customer c = mainFrame.getCurrentCustomer();
       }
}
