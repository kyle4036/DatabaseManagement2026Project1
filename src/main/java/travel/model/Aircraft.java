package travel.model;

public class Aircraft {

    private int craftID;
    private String lineID;
    private String portID;
    private int capacity;

    public Aircraft() {
        
    }

    public Aircraft(int craftID, String lineID, String portID, int capacity){
        this.craftID = craftID;
        this.lineID = lineID;
        this.portID = portID;
        this.capacity= capacity;
    }

    public int getCraftID(){
        return craftID;
    }

    public void setCraftID(int v){
        this.craftID = v;
    }
    
    public String getLineID(){
        return lineID;
    }

    public void setLineID(String v){
        this.lineID = v;
    }

    public String getPortID(){
        return portID;
    }

    public void setPortID(String v){
        this.portID = v;
    }

    public int getCapacity(){
        return capacity;
    }

    public void setCapacity(int v){
        this.capacity = v;
    }

}