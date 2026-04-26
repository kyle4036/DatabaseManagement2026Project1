package travel.model;

public class Employee {

    private int employeeID;
    private String firstName;
    private String lastName;
    private String username;
    private String password;    
    private String role;

    public Employee() {

    }

    public Employee(int employeeID, String firstName, String lastName, String username, String password, String role) {
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
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

    public String getUsername(){
        return username;
    }

    public void setUsername(String v){
        this.username = v;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String v){
        this.password = v;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String v){
        this.role = v;
    }

}