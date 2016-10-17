package ofa.mobile.pojo;

import java.math.BigDecimal;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class CarrierRTSDetail {
    String orderNumber, shipTo;
    BigDecimal value;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public CarrierRTSDetail() {
        super();
    }

    public CarrierRTSDetail(JSONObject temp) throws JSONException {
        StringBuffer local;
        local = new StringBuffer(temp.getString("DETAILS"));
        if (local.indexOf("@nil") == -1) {
            this.setOrderNumber(temp.getString("DETAILS"));
        }

        local = new StringBuffer(temp.getString("SHIP_TO"));
        if (local.indexOf("@nil") == -1) {
            this.setShipTo(temp.getString("SHIP_TO"));
        }

        local = new StringBuffer(temp.getString("VALUE"));
        if (local.indexOf("@nil") == -1) {
            this.setValue(new BigDecimal(temp.getString("VALUE")));
        }
    }

    public void setOrderNumber(String orderNumber) {
        String oldOrderNumber = this.orderNumber;
        this.orderNumber = orderNumber;
        _propertyChangeSupport.firePropertyChange("orderNumber", oldOrderNumber, orderNumber);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.removePropertyChangeListener(l);
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setShipTo(String shipTo) {
        String oldShipTo = this.shipTo;
        this.shipTo = shipTo;
        _propertyChangeSupport.firePropertyChange("shipTo", oldShipTo, shipTo);
    }

    public String getShipTo() {
        return shipTo;
    }

    public void setValue(BigDecimal value) {
        BigDecimal oldValue = this.value;
        this.value = value;
        _propertyChangeSupport.firePropertyChange("value", oldValue, value);
    }

    public BigDecimal getValue() {
        return value;
    }
}
