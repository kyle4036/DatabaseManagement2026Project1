package travel.services;

import travel.dao.CustomerDAO;
import travel.dao.EmployeeDAO;
import travel.model.Customer;
import travel.model.Employee;

public class AuthenticationService {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    public int login(String username, String password){

        if (customerDAO.verifyCustomer(username, password) || employeeDAO.verifyEmployee(username, password)){
            return 1; // Should I modify this?
        }
        else{
            return 0; // Should I modify this?
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