package travel.model;

class Customer {

    private int customerID;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;

    public Customer() {

    }

    public Customer(int customerID, String firstName, String lastName, String username, String password, String email, String phoneNumber) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public int getCustomerID(){
        return customerID;
    }

    public void setCustomerID(int v){
        this.customerID = v;
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

    public String getEmail(){
        return email;
    }

    public void setEmail(String v){
        this.email = v;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(String v){
        this.phoneNumber = v;
    }

}