package travel;

import java.sql.Connection;

import javax.swing.JLabel;

import travel.ui.LoginFrame;

public class TravelMain {

    JLabel msg;
    private static Connection con = null;
    private static Statement stmt = null;
    
    public static void main(String[] args){
        LoginFrame lFrame = new LoginFrame();
        lFrame.initialize();
    }
}
