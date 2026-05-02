package travel;

import travel.ui.LoginFrame;

import javax.swing.*;

import java.util.concurrent.CountDownLatch;

import travel.model.UserAccountType;

public class TravelMain {

    JLabel msg;
    private static DBConnection dbc = null;
    private static LoginFrame lFrame = null;

    public static void main(String[] args){
        dbc = new DBConnection();
        try{
            dbc.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }

        CountDownLatch latch = new CountDownLatch(1);

        lFrame = new LoginFrame(dbc);
        lFrame.setLoginAccountListener( () ->{
            latch.countDown();
            loginAccountAction();
        });
        lFrame.initialize();
        try{
            latch.await();
        }catch(InterruptedException e){
            e.printStackTrace();
        }


    }

    public static UserAccountType loginAccountAction(){
        //note -below is only a test function
        LoginFrame.accountListenerTest();

        return lFrame.getUserAccountType();
    }
}
