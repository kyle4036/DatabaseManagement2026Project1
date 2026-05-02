package travel.services;

import travel.DBConnection;
import travel.dao.CustomerDAO;
import travel.dao.EmployeeDAO;
import travel.model.Customer;
import travel.model.Employee;

public class AuthenticationService {

    private CustomerDAO customerDAO = null;
    private EmployeeDAO employeeDAO = null;

    public AuthenticationService(DBConnection dbc){
        customerDAO = new CustomerDAO(dbc);
        employeeDAO = new EmployeeDAO(dbc);
    }

    public boolean login(String username, String password){

        if (customerDAO.verifyCustomer(username, password) || employeeDAO.verifyEmployee(username, password)){
            return true; // Should I modify this?
        }
        else{
            return false; // Should I modify this?
        }
    }

    public void registerCustomer(int customerID, String firstName, String lastName, String username, String password, String email, String phoneNumber){
        Customer customer = new Customer(customerID, firstName, lastName, username, password, email, phoneNumber);
                
        customerDAO.insert(customer);
    }

    public void registerEmployee(int employeeID, String firstName, String lastName, String username, String password, String role){
        Employee employee = new Employee(employeeID, firstName, lastName, username, password, role);
                
        employeeDAO.insert(employee);
    }

}