package travel.model;

public class FlightSummaryRow {
    private String flightNumber;
    private String lineID;
    private String originPortID;
    private String destinationPortID;
    private String flightType;

    public FlightSummaryRow() {

    }

    public FlightSummaryRow(String flightNumber, String lineID, String originPortID, String destinationPortID, String flightType) {
        this.flightNumber = flightNumber;
        this.lineID = lineID;
        this.originPortID = originPortID;
        this.destinationPortID = destinationPortID;
        this.flightType = flightType;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getLineID() {
        return lineID;
    }

    public void setLineID(String lineID) {
        this.lineID = lineID;
    }

    public String getOriginPortID() {
        return originPortID;
    }

    public void setOriginPortID(String originPortID) {
        this.originPortID = originPortID;
    }

    public String getDestinationPortID() {
        return destinationPortID;
    }

    public void setDestinationPortID(String destinationPortID) {
        this.destinationPortID = destinationPortID;
    }

    public String getFlightType() {
        return flightType;
    }

    public void setFlightType(String flightType) {
        this.flightType = flightType;
    }
}
