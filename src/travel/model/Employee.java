package travel.model;

class Employee {

    private int employeeID;
    private String firstName;
    private String lastName;
    private String role;

    public Employee() {

    }

    public Employee(int employeeID, String firstName, String lastName, String role) {
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public int getEmployeeID(){
        return employeeID;
    }

    public void setEmployeeID(int v){
        this.employeeID = v;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String v){
        this.firstName = v;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String v){
        this.lastName = v;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String v){
        this.role = v;
    }

}