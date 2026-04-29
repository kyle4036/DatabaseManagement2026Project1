package travel.services;

import travel.DBConnection;
import travel.dao.*;
import travel.model.*;

import java.util.List;

public class AdminService {

    private final CustomerDAO customerDAO;
    private final EmployeeDAO employeeDAO;
    private final ReportDAO reportDAO;

    public AdminService(DBConnection dbc) {
        this.customerDAO = new CustomerDAO(dbc);
        this.employeeDAO = new EmployeeDAO(dbc);
        this.reportDAO = new ReportDAO(dbc);
    }


    public void addCustomer(Customer c) {
        verifyCustomer(c);
        customerDAO.insert(c);
    }

    public void deleteCustomer(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid customer ID");
        }
        customerDAO.delete(id);
    }

    public void updateCustomer(Customer c) {
        verifyCustomer(c);
        customerDAO.update(c);
    }

    private void verifyCustomer(Customer c) {
       if (c.getFirstName() == null || c.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (c.getLastName() == null || c.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        if (c.getUsername() == null || c.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (c.getPassword() == null || c.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (c.getEmail() == null || c.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (c.getPhoneNumber() == null || c.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
    }

   public void addEmployee(Employee e) {
        verifyEmployee(e);
        employeeDAO.insert(e);
    }

    public void deleteEmployee(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid employee ID");
        }
        employeeDAO.delete(id);
    }

    public void updateEmployee(Employee e) {
        verifyEmployee(e);
        employeeDAO.update(e);
    }

    private void verifyEmployee(Employee e) {
        if (e.getFirstName() == null || e.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (e.getLastName() == null || e.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        if (e.getUsername() == null || e.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (e.getPassword() == null || e.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (e.getRole() == null || e.getRole().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty");
        }
    }

    public List<ReservationReportRow> reservationsByFlightNumber(String flightNumber, String lineID) {
        if (flightNumber == null || flightNumber.isEmpty()) {
            throw new IllegalArgumentException("Flight number cannot be empty");
        }
        if (lineID == null || lineID.isEmpty()) {
            throw new IllegalArgumentException("Line ID cannot be empty");
        }
        return reportDAO.getReservationsByFlightNumber(flightNumber, lineID);
    }

    public List<ReservationReportRow> reservationsByCustomerNameAndID(String name, int customerID) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty");
        }
        if (customerID <= 0) {
            throw new IllegalArgumentException("Invalid customer ID");
        }
        return reportDAO.getReservationsByCustomerNameAndID(name, customerID);
    }

    public RevenueSummaryRow revenueSummaryByCustomer(int customerID) {
        if (customerID <= 0) {
            throw new IllegalArgumentException("Invalid customer ID");
        }
        return reportDAO.getRevenueSummaryByCustomer(customerID);
    }

    public RevenueSummaryRow revenueSummaryByFlight(String flightNumber, String lineID) {
        if (flightNumber == null || flightNumber.isEmpty()) {
            throw new IllegalArgumentException("Flight number cannot be empty");
        }
        if (lineID == null || lineID.isEmpty()) {
            throw new IllegalArgumentException("Line ID cannot be empty");
        }
        return reportDAO.getRevenueSummaryByFlight(flightNumber, lineID);
    }

    public RevenueSummaryRow revenueSummaryByAirline(String lineID) {
        if (lineID == null || lineID.isEmpty()) {
            throw new IllegalArgumentException("Line ID cannot be empty");
        }
        return reportDAO.getRevenueSummaryByAirline(lineID);
    }

    public RevenueSummaryRow mostRevenueByCustomer() {
        return reportDAO.getMostRevenueByCustomer();
    }

    public List<FlightSummaryRow> mostActiveFlights() {
        return reportDAO.getMostActiveFlights();
    }

    public SalesReportRow monthlySalesSummary(int month, int year) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month");
        }
        if (year <= 0) {
            throw new IllegalArgumentException("Invalid year");
        }
        return reportDAO.getMonthlySalesSummary(month, year);
    }
}
