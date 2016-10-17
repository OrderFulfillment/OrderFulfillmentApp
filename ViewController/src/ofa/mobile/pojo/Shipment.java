package ofa.mobile.pojo;

import java.math.BigDecimal;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class Shipment {
    String shipmentStatus;
    int count;
    Double percentage;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public Shipment(JSONObject temp) throws JSONException {
        StringBuffer local;
        local = new StringBuffer(temp.getString("SHIPMENT_STATUS"));
        if (local.indexOf("@nil") == -1) {
            this.setShipmentStatus(temp.getString("SHIPMENT_STATUS"));
        }

        local = new StringBuffer(temp.getString("SHIPMENT_CNT"));
        if (local.indexOf("@nil") == -1) {
            this.setCount(new Integer(temp.getString("SHIPMENT_CNT")));
        }

        local = new StringBuffer(temp.getString("PERCENTAGE"));
        if (local.indexOf("@nil") == -1) {
            this.setPercentage(new Double(temp.getString("PERCENTAGE")));
        }
    }

    public void setShipmentStatus(String shipmentStatus) {
        String oldShipmentStatus = this.shipmentStatus;
        this.shipmentStatus = shipmentStatus;
        _propertyChangeSupport.firePropertyChange("shipmentStatus", oldShipmentStatus, shipmentStatus);
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public void setCount(int count) {
        int oldCount = this.count;
        this.count = count;
        _propertyChangeSupport.firePropertyChange("count", oldCount, count);
    }

    public int getCount() {
        return count;
    }

    public void setPercentage(Double percentage) {
        Double oldPercentage = this.percentage;
        this.percentage = percentage;
        _propertyChangeSupport.firePropertyChange("percentage", oldPercentage, percentage);
    }

    public Double getPercentage() {
        return percentage;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.removePropertyChangeListener(l);
    }
}
