package travel;

import travel.DBSetup;
import travel.ui.MainFrame;

public class TravelMain {

    public static void main(String[] args) {
        for (String a : args) {
            if (a.equals("--reset")) DBSetup.resetAndSeed();
        }

        MainFrame mFrame = new MainFrame();
        mFrame.setVisible(true);
    }
}