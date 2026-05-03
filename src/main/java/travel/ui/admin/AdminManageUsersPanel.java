package travel.ui.admin;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import travel.model.Customer;
import travel.model.Employee;
import travel.services.AdminService;
import travel.ui.MainFrame;
import travel.ui.Screen;

public class AdminManageUsersPanel extends JPanel {

    private final MainFrame mainFrame;
    private final AdminService adminService = new AdminService();

    public AdminManageUsersPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Manage Customers / Employees");
        title.setFont(new Font("Lucida Sans", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JButton addCustomer = new JButton("Add Customer");
        JButton updateCustomer = new JButton("Update Customer");
        JButton deleteCustomer = new JButton("Delete Customer");
        JButton addEmployee = new JButton("Add Employee");
        JButton updateEmployee = new JButton("Update Employee");
        JButton deleteEmployee = new JButton("Delete Employee");
        JButton backBtn = new JButton("Back");

        addCustomer.addActionListener(e -> withGuard(this::onAddCustomer));
        updateCustomer.addActionListener(e -> withGuard(this::onUpdateCustomer));
        deleteCustomer.addActionListener(e -> withGuard(this::onDeleteCustomer));
        addEmployee.addActionListener(e -> withGuard(this::onAddEmployee));
        updateEmployee.addActionListener(e -> withGuard(this::onUpdateEmployee));
        deleteEmployee.addActionListener(e -> withGuard(this::onDeleteEmployee));
        backBtn.addActionListener(e -> mainFrame.showScreen(Screen.ADMIN_HOME));

        buttonPanel.add(addCustomer);
        buttonPanel.add(updateCustomer);
        buttonPanel.add(deleteCustomer);
        buttonPanel.add(addEmployee);
        buttonPanel.add(updateEmployee);
        buttonPanel.add(deleteEmployee);
        buttonPanel.add(backBtn);

        JPanel center = new JPanel();
        center.add(buttonPanel);
        add(center, BorderLayout.CENTER);
    }

    private void onAddCustomer() {
        Customer customer = promptCustomer(false);
        if (customer == null) {
            return;
        }
        adminService.addCustomer(customer);
        showInfo("Customer added successfully.");
    }

    private void onUpdateCustomer() {
        Customer customer = promptCustomer(true);
        if (customer == null) {
            return;
        }
        adminService.updateCustomer(customer);
        showInfo("Customer updated successfully.");
    }

    private void onDeleteCustomer() {
        Integer id = promptId("Customer");
        if (id == null) {
            return;
        }
        adminService.deleteCustomer(id);
        showInfo("Customer deleted successfully.");
    }

    private void onAddEmployee() {
        Employee employee = promptEmployee(false);
        if (employee == null) {
            return;
        }
        adminService.addEmployee(employee);
        showInfo("Employee added successfully.");
    }

    private void onUpdateEmployee() {
        Employee employee = promptEmployee(true);
        if (employee == null) {
            return;
        }
        adminService.updateEmployee(employee);
        showInfo("Employee updated successfully.");
    }

    private void onDeleteEmployee() {
        Integer id = promptId("Employee");
        if (id == null) {
            return;
        }
        adminService.deleteEmployee(id);
        showInfo("Employee deleted successfully.");
    }

    private Customer promptCustomer(boolean includeId) {
        JTextField idField = new JTextField();
        JTextField firstName = new JTextField();
        JTextField lastName = new JTextField();
        JTextField username = new JTextField();
        JTextField password = new JTextField();
        JTextField email = new JTextField();
        JTextField phone = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        if (includeId) {
            panel.add(new JLabel("Customer ID"));
            panel.add(idField);
        }
        panel.add(new JLabel("First Name"));
        panel.add(firstName);
        panel.add(new JLabel("Last Name"));
        panel.add(lastName);
        panel.add(new JLabel("Username"));
        panel.add(username);
        panel.add(new JLabel("Password"));
        panel.add(password);
        panel.add(new JLabel("Email"));
        panel.add(email);
        panel.add(new JLabel("Phone"));
        panel.add(phone);

        int choice = JOptionPane.showConfirmDialog(this, panel, includeId ? "Update Customer" : "Add Customer", JOptionPane.OK_CANCEL_OPTION);
        if (choice != JOptionPane.OK_OPTION) {
            return null;
        }

        Customer c = new Customer();
        if (includeId) {
            c.setCustomerID(parseRequiredInt(idField.getText(), "Customer ID"));
        }
        c.setFirstName(firstName.getText().trim());
        c.setLastName(lastName.getText().trim());
        c.setUsername(username.getText().trim());
        c.setPassword(password.getText().trim());
        c.setEmail(email.getText().trim());
        c.setPhoneNumber(phone.getText().trim());
        return c;
    }

    private Employee promptEmployee(boolean includeId) {
        JTextField idField = new JTextField();
        JTextField firstName = new JTextField();
        JTextField lastName = new JTextField();
        JTextField username = new JTextField();
        JTextField password = new JTextField();
        JTextField role = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        if (includeId) {
            panel.add(new JLabel("Employee ID"));
            panel.add(idField);
        }
        panel.add(new JLabel("First Name"));
        panel.add(firstName);
        panel.add(new JLabel("Last Name"));
        panel.add(lastName);
        panel.add(new JLabel("Username"));
        panel.add(username);
        panel.add(new JLabel("Password"));
        panel.add(password);
        panel.add(new JLabel("Role (Admin/Rep)"));
        panel.add(role);

        int choice = JOptionPane.showConfirmDialog(this, panel, includeId ? "Update Employee" : "Add Employee", JOptionPane.OK_CANCEL_OPTION);
        if (choice != JOptionPane.OK_OPTION) {
            return null;
        }

        Employee e = new Employee();
        if (includeId) {
            e.setEmployeeID(parseRequiredInt(idField.getText(), "Employee ID"));
        }
        e.setFirstName(firstName.getText().trim());
        e.setLastName(lastName.getText().trim());
        e.setUsername(username.getText().trim());
        e.setPassword(password.getText().trim());
        e.setRole(role.getText().trim());
        return e;
    }

    private Integer promptId(String name) {
        String raw = JOptionPane.showInputDialog(this, name + " ID");
        if (raw == null) {
            return null;
        }
        return parseRequiredInt(raw, name + " ID");
    }

    private int parseRequiredInt(String value, String label) {
        String trimmed = value == null ? "" : value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(label + " is required");
        }
        try {
            return Integer.parseInt(trimmed);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + " must be a number");
        }
    }

    private void withGuard(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Operation Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Admin", JOptionPane.INFORMATION_MESSAGE);
    }
}
