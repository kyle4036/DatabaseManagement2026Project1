package travel.model;

class Airline {

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

    public String airlineOpreatesIn(){
        Airport airport = new Airport();
        
        Airline airline = new Airline(); //Question: Do we need this as well?
        
        return airport.getPortID();
    }

}