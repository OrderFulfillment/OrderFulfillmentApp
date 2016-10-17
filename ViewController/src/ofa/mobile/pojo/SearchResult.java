package ofa.mobile.pojo;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class SearchResult {
    String itemNumber, ssd, carrier, warehouse, headerDetails;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public SearchResult() {
        super();
    }

    public SearchResult(JSONObject temp) throws JSONException {
        StringBuffer local;
        local = new StringBuffer(temp.getString("WAREHOUSE"));
        if (local.indexOf("@nil") == -1) {
            this.setWarehouse(temp.getString("WAREHOUSE"));
        }

        local = new StringBuffer(temp.getString("HEADER_DETAILS"));
        if (local.indexOf("@nil") == -1) {
            this.setHeaderDetails(temp.getString("HEADER_DETAILS"));
        }

        local = new StringBuffer(temp.getString("CARRIER"));
        if (local.indexOf("@nil") == -1) {
            this.setCarrier(temp.getString("CARRIER"));
        }

        local = new StringBuffer(temp.getString("SSD"));
        if (local.indexOf("@nil") == -1) {
            this.setSsd(temp.getString("SSD"));
        }

        local = new StringBuffer(temp.getString("ITEM_NUMBER"));
        if (local.indexOf("@nil") == -1) {
            this.setItemNumber(temp.getString("ITEM_NUMBER"));
        }
    }

    public void setItemNumber(String itemNumber) {
        String oldItemNumber = this.itemNumber;
        this.itemNumber = itemNumber;
        _propertyChangeSupport.firePropertyChange("itemNumber", oldItemNumber, itemNumber);
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setSsd(String ssd) {
        String oldSsd = this.ssd;
        this.ssd = ssd;
        _propertyChangeSupport.firePropertyChange("ssd", oldSsd, ssd);
    }

    public String getSsd() {
        return ssd;
    }

    public void setCarrier(String carrier) {
        String oldCarrier = this.carrier;
        this.carrier = carrier;
        _propertyChangeSupport.firePropertyChange("carrier", oldCarrier, carrier);
    }

    public String getCarrier() {
        return carrier;
    }

    public void setWarehouse(String warehouse) {
        String oldWarehouse = this.warehouse;
        this.warehouse = warehouse;
        _propertyChangeSupport.firePropertyChange("warehouse", oldWarehouse, warehouse);
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setHeaderDetails(String headerDetails) {
        String oldHeaderDetails = this.headerDetails;
        this.headerDetails = headerDetails;
        _propertyChangeSupport.firePropertyChange("headerDetails", oldHeaderDetails, headerDetails);
    }

    public String getHeaderDetails() {
        return headerDetails;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.removePropertyChangeListener(l);
    }
}
