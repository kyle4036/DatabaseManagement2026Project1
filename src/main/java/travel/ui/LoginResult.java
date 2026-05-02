/* This is a wrapper over the result to make AuthenticationService have a simpler time passing INFO to the UI component */
package travel.ui;

import travel.model.Customer;
import travel.model.Employee;

public class LoginResult {
    public enum Type {
        CUSTOMER, ADMIN, REP, FAILED
    }

    private final Type type;
    private final Customer customer; // null unless type is CUSTOMER
    private final Employee employee; // null unless type is ADMIN or REP

    private LoginResult(Type type, Customer c, Employee e) {
        this.type = type;
        this.customer = c;
        this.employee = e;
    }

    // Static methods that create a `LoginResult` getting info about:
    // 1) Was the login successful?
    // 2) Who logged in if so?
    public static LoginResult asCustomer(Customer c) {
        return new LoginResult(Type.CUSTOMER, c, null);
    }

    public static LoginResult asAdmin(Employee e) {
        return new LoginResult(Type.ADMIN, null, e);
    }

    public static LoginResult asRep(Employee e) {
        return new LoginResult(Type.REP, null, e);
    }

    public static LoginResult failed() {
        return new LoginResult(Type.FAILED, null, null);
    }

    public boolean isCustomer() {
        return (type == Type.CUSTOMER);
    }

    public boolean isAdmin() {
        return (type == Type.ADMIN);
    }

    public boolean isRep() {
        return (type == Type.REP);
    }

    public boolean isFailed() {
        return (type == Type.FAILED);
    }

    public Customer getCustomer() {
        return customer;
    }

    public Employee getEmployee() {
        return employee;
    }
}