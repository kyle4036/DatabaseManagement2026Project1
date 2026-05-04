package travel.ui;

/* Originally I had this all in 'MainFrame.java' but it is used in almost all *Frame.java files so
*  I decided it would be best to make it its own class
* These are just constants so we know what panel to display on MainFrame
*/
public final class Screen {

    public static final String LOGIN                = "login";
    public static final String CUSTOMER_HOME        = "customerHome";
    public static final String ADMIN_HOME           = "adminHome";
    public static final String REP_HOME             = "repHome";

    public static final String ADMIN_MANAGE_USERS   = "adminManageUsers";
    public static final String ADMIN_SALES_REPORT   = "adminSalesReport";
    public static final String ADMIN_REVENUE_REPORT = "adminRevenueReport";
    public static final String ADMIN_ACTIVE_FLIGHTS = "adminActiveFlights";
    public static final String ADMIN_RESERVATIONS   = "adminReservations";

    public static final String CUSTOMER_SEARCH      = "customerSearch";
    public static final String CUSTOMER_RESERVE     = "customerReserve";
    public static final String CUSTOMER_WAITLIST    = "customerWait";
    public static final String CUSTOMER_QNA         = "customerQNA";
    
    public static final String REP_MANAGEMENT       = "repManagement";
    public static final String REP_QNA              = "repQNA";
    public static final String REP_WAITLIST         = "repWAITLIST";
    private Screen() {}
}
