package travel.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import travel.services.AuthenticationService;

/**
//   Login screen where the user types in user+pass
//   On success, MainFrame's session is set and the appropriate home screen is shown.
**/
public class LoginPanel extends JPanel {

    private static final Font FIELD_FONT = new Font("Lucida Sans", Font.PLAIN, 16);

    private final MainFrame mainFrame;
    private final AuthenticationService auth = new AuthenticationService();

    private final JTextField     usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JLabel         msgLabel      = new JLabel(" ");

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        usernameField.setFont(FIELD_FONT);
        passwordField.setFont(FIELD_FONT);

        // Title
        JLabel title = new JLabel("Travel Reservation — Login");
        title.setFont(new Font("Lucida Sans", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        // Username
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;

        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> onLoginClicked());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);

        // Message label (errors / status)
        msgLabel.setForeground(Color.RED);
        gbc.gridy = 4;
        add(msgLabel, gbc);

        passwordField.addActionListener(e -> onLoginClicked());
    }

    private void onLoginClicked() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            msgLabel.setText("Username and password are required");
            return;
        }

        LoginResult result = auth.login(username, password);

        if (result.isCustomer()) {
            mainFrame.setCurrentCustomer(result.getCustomer());
            clearFields();
            mainFrame.showScreen(Screen.CUSTOMER_HOME); 
        } else if (result.isAdmin()) {
            mainFrame.setCurrentEmployee(result.getEmployee());
            clearFields();
            mainFrame.showScreen(Screen.ADMIN_HOME);
        } else if (result.isRep()) {
            mainFrame.setCurrentEmployee(result.getEmployee());
            clearFields();
            mainFrame.showScreen(Screen.REP_HOME);
        } else {
            msgLabel.setText("Invalid username or password");
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        msgLabel.setText(" ");
    }
}