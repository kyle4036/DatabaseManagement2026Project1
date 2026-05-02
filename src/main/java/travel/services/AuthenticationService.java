package travel.services;

import travel.dao.CustomerDAO;
import travel.dao.EmployeeDAO;
import travel.model.Customer;
import travel.model.Employee;
import travel.ui.LoginResult;

public class AuthenticationService {

   public LoginResult login(String username, String password) {
        CustomerDAO customerDAO = new CustomerDAO();
        Customer c = customerDAO.findByLogin(username, password);
        if (c != null) return LoginResult.asCustomer(c);
        
        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee e = employeeDAO.findByLogin(username, password); 
        if (e != null) {
            if ("admin".equals(e.getRole())) {
                return LoginResult.asAdmin(e);
            } else {
                return LoginResult.asRep(e);
            }
        }
        return LoginResult.failed();
    }
}

