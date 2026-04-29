package travel;

import javax.swing.JLabel;

import travel.ui.LoginFrame;


public class TravelMain {

    JLabel msg;
    private static DBConnection dbc = null;

    public static void main(String[] args){
        dbc = new DBConnection();
        try{
            dbc.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LoginFrame lFrame = new LoginFrame(dbc);
        lFrame.initialize();
    }
}
