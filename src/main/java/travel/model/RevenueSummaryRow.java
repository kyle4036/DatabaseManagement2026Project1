package travel.model;

import java.math.BigDecimal;

public class RevenueSummaryRow {

    //can either be airline, customer, or flight
    private String entityType;
    private String entityID;
    private String entityName;
    private BigDecimal totalRevenue;

    public RevenueSummaryRow() {

    }

    public RevenueSummaryRow(String entityType, String entityID, String entityName, BigDecimal totalRevenue) {
        this.entityType = entityType;
        this.entityID = entityID;
        this.entityName = entityName;
        this.totalRevenue = totalRevenue;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getEntityID() {
        return entityID;
    }

    public String getEntityName() {
        return entityName;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;

    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }



}
