package travel.model;

public class Airport {

    private String portID;
    private String name;
    private String city;
    private String country;

    public Airport() {

    }

    public Airport(String portID, String name, String city, String country){
        this.portID = portID;
        this.name = name;
        this.city = city;
        this.country = country;
    }

    public String getPortID(){
        return portID;
    }

    public void setPortID(String v){
        this.portID = v;
    }

    public String getName(){
        return name;
    }

    public void setName(String v){
        this.name = v;
    }

    public String getCity(){
        return city;
    }

    public void setCity(String v){
        this.city = v;
    }

    public String getCountry(){
        return country;
    }

    public void setCountry(String v){
        this.country = v;
    }

}