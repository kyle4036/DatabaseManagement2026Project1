package travel.ui;

import travel.DBConnection;
import travel.services.AuthenticationService;
import travel.model.*;
import travel.dao.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;


//clear; javac ./src/travel/ui/LoginFrame.java; java -cp "./mysql-connector-j-8.3.0.jar" ./src/travel/ui/LoginFrame.java

public class LoginFrame extends JFrame{
    
    final private Font mainFont = new Font("Lucida Sans", Font.BOLD, 18);
    JLabel msg;
    private Connection con = null;
    private Statement stmt = null;
    private static boolean userLoggedin = false;
    private JTextField tfuser, tfpasswd;
    private static String user = "";

    private Customer custAcc = null;
    private Employee empAcc = null;
    private LoginAccountListener loginListener = null;
    private DBConnection dbc = null;
    private AuthenticationService AuService = null;
    private CustomerDAO cDao = null;
    private EmployeeDAO eDao = null;
    public UserAccountType accType;

    public LoginFrame(DBConnection dbc){
        con = dbc.getConnection();
        stmt = dbc.getStatement();
        AuService = new AuthenticationService(dbc);
        this.dbc = dbc;

        cDao = new CustomerDAO(dbc);
        eDao = new EmployeeDAO(dbc);
    }
    //public LoginFrame(){};

    public void initialize(){
        JPanel lframe = this.createLoginPanel();
        this.add(lframe);
        this.setTitle("Login Page");
        this.setSize(500, 300);
        this.setMinimumSize(new Dimension(300, 200));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public JPanel createLoginPanel() {
        // inputPanel: ------------------------------------
        // -- inputPanel components
        JLabel lbuser=new JLabel("Username");
        lbuser.setFont(mainFont);
        tfuser=new JTextField();
        tfuser.setFont(mainFont);
        JLabel lbpasswd=new JLabel("Password");
        lbpasswd.setFont(mainFont);
        tfpasswd=new JTextField();
        tfpasswd.setFont(mainFont);

        //-- create inputPanel and add its components
        JPanel inputPanel=new JPanel();
        inputPanel.setLayout(new GridLayout(2,2,5,5));
        inputPanel.setOpaque(false); // so that form color is seen as background
        inputPanel.add(lbuser);
        inputPanel.add(tfuser);
        inputPanel.add(lbpasswd);
        inputPanel.add(tfpasswd);

        // msg : ------------------------------------
        msg=new JLabel(); // text will be added later (it is global variable)
        msg.setFont(mainFont);

        // buttonPanel: ------------------------------------
        // -- buttonPanel components
        JButton btnAdd=new JButton("Add User");
        btnAdd.setFont(mainFont);

        // add a listener
        btnAdd.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                    // read the text of the text fields and store new user in the database
                    // first just put it on Welcome
                    String user=tfuser.getText();
                    String passwd=tfpasswd.getText();
                    // insert into the database
                    /*String instruction="insert into users values ";
                    instruction+="('"+user+"','"+passwd+"')";
                    try{
                        int result=stmt.executeUpdate(instruction);
                        String s="User "+user+" has been Added "+result;
                        msg.setText(s);
                    }
                    catch (SQLException e1) {
                        msg.setText("Unable to add new user");
                    }
                    */
                    Customer newCust = new Customer();
                    newCust.setUsername(user);
                    newCust.setPassword(passwd);

                    cDao.insert(newCust); 
                }
            }
        );

        JButton btnLogin=new JButton("Login");
        btnLogin.setFont(mainFont);
        // add a listener
        btnLogin.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                    // read the text of the text fields and check that
                    // they are in the database
                    user=tfuser.getText();
                    String passwd=tfpasswd.getText();

                    userLoggedin = AuService.login(user, passwd);

                    if(userLoggedin && loginListener != null){
                        custAcc = cDao.getLastVerifiedCustomer();
                        loginListener.onLogin();
                    }

                    //loginListener.onLogin();

                    /*
                    String query="select * from users where ";
                    // check to see if they are in the database 
                    query+="user='"+user+"' and password='"+passwd+"'";
                    ResultSet rset=null;
                    try{
                        rset=stmt.executeQuery(query);
                        if (rset.next()){
                            String s="Welcome "+user;
                            userLoggedin=true;
                            msg.setText(s);
                        }
                        else{
                            String s="Unknow user "+user+" not logged in";
                            msg.setText(s);
                        }
                    }
                    catch (SQLException e1) {
                        msg.setText("<html> Error in the query: "+query);
                        // e1.printStackTrace();
                    }
                    */

                }
            }
        );

        JButton btnClear=new JButton("Clear");
        btnClear.setFont(mainFont);

        // add a listener
        btnClear.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                    // clear the text fields
                    tfuser.setText("");
                    tfpasswd.setText("");
                    msg.setText("");
                }
            }
        );

        //-- create buttonPanel and add its components
        JPanel buttonPanel=new JPanel();
        buttonPanel.setLayout(new GridLayout(1,3,5,5));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnClear);

        // mainPanel: ------------------------------------
        //-------------- create main panel
        JPanel mainPanel=new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(230,140,140));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // -- add mainPanel's components
        mainPanel.add(inputPanel,BorderLayout.NORTH);
        mainPanel.add(msg,BorderLayout.CENTER);
        mainPanel.add(buttonPanel,BorderLayout.SOUTH);

        return mainPanel;
    }

    public UserAccountType getUserAccountType(){
        return accType;
    }

    public void close(){
        this.dispose();
    }

    public void setLoginAccountListener(LoginAccountListener l){
        loginListener = l;
    }

    public static void main(String[] args) throws Exception {
        
        // Initialize the connection to the database
        /* String url = "jdbc:mysql://localhost:3306/testproject";
        String user = "testuser";
        String password = "abc123"; */
        DBConnection dbc = new DBConnection();
        try {
            dbc.initialize();
        } catch (SQLException e) {
            System.out.println("Unable to create a connection to the database");
            e.printStackTrace();
            System.exit(0);
        }

        LoginFrame myFrame = new LoginFrame(dbc);
        myFrame.setLoginAccountListener(()->{
            accountListenerTest();
        });

        myFrame.initialize();
    }

    public static void accountListenerTest(){
        System.out.println("able to login");
        System.out.println("helloworld");
    }
    
}
