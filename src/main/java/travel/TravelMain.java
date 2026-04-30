package travel;

import travel.ui.LoginFrame;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;

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

        CountDownLatch latch = new CountDownLatch(1);

        LoginFrame lFrame = new LoginFrame(dbc);
        lFrame.setLoginAccountListener( () ->
            latch.countDown();
            loginAccountAction();
        );
        lFrame.initialize();
        latch.await();
    }

    public static void loginAccountAction(){

    }
}
