package travel.ui.customer;

import javax.swing.*;
import java.awt.BorderLayout;

import travel.ui.*;
import travel.model.*;

public class TicketPanel extends JPanel{ 
       private JLabel ticketInfo = null; 

       public TicketPanel(Flight flight){
              ticketInfo = new JLabel(flight.getDeparturePortID() + " :" + 
                                      flight.getDestinationPortID() + " :");

              JButton buyButton = new JButton("Buy Ticket");
              buyButton.addActionListener(e -> buyBtnPressed());

              this.add(ticketInfo);
              this.add(buyButton);
       }

       private void buyBtnPressed(){

       }
}
