package main.java.travel.dao;
import java.util.*;

public class ReportDAO {
    
//wip
    public List<String> getReservationsByFlightNumber(String flightNumber){
        String sql = "SELECT * FROM FlightTickets T WHERE T.flightNumber = ?";
        return new ArrayList<>();

    }
}
