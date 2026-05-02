package travel.ui;

/* Originally I had this all in 'MainFrame.java' but it is used in almost all *Frame.java files so
*  I decided it would be best to make it its own class
* These are just constants so we know what panel to display on MainFrame
*/
public final class Screen {

    public static final String LOGIN         = "login";
    public static final String CUSTOMER_HOME = "customerHome";
    public static final String ADMIN_HOME    = "adminHome";
    public static final String REP_HOME      = "repHome";

    private Screen() {}
}