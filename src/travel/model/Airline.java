package travel.model;

public class Airline {

    private String lineID;
    private String name;

    public Airline() {

    }

    public Airline(String lineID, String name){
        this.lineID = lineID;
        this.name = name;
    }

    public String getLineID(){
        return lineID;
    }

    public void setLineID(String v){
        this.lineID = v;
    }

    public String getName(){
        return name;
    }

    public void setName(String v){
        this.name = v;
    }

}