package main.java.travel.model;

public class Airport {

    private String portID;
    private String name;

    public Airport() {

    }

    public Airport(String portID, String name){
        this.portID = portID;
        this.name = name;
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

}