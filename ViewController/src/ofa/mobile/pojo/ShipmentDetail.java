package ofa.mobile.pojo;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class ShipmentDetail {
    String deliveryNumber, shipDate, customerName, shipMethod;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public ShipmentDetail(JSONObject temp) throws JSONException {
        StringBuffer local;
        local = new StringBuffer(temp.getString("DELIVERY_NUM"));
        if (local.indexOf("@nil") == -1) {
            this.setDeliveryNumber(temp.getString("DELIVERY_NUM"));
        }

        local = new StringBuffer(temp.getString("SHIP_DATE"));
        if (local.indexOf("@nil") == -1) {
            this.setShipDate(temp.getString("SHIP_DATE"));
        }

        local = new StringBuffer(temp.getString("CUSTOMER_NAME"));
        if (local.indexOf("@nil") == -1) {
            this.setCustomerName(temp.getString("CUSTOMER_NAME"));
        }

        local = new StringBuffer(temp.getString("SHIP_METHOD"));
        if (local.indexOf("@nil") == -1) {
            this.setShipMethod(temp.getString("SHIP_METHOD"));
        }
    }

    public void setDeliveryNumber(String deliveryNumber) {
        String oldDeliveryNumber = this.deliveryNumber;
        this.deliveryNumber = deliveryNumber;
        _propertyChangeSupport.firePropertyChange("deliveryNumber", oldDeliveryNumber, deliveryNumber);
    }

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setShipDate(String shipDate) {
        String oldShipDate = this.shipDate;
        this.shipDate = shipDate;
        _propertyChangeSupport.firePropertyChange("shipDate", oldShipDate, shipDate);
    }

    public String getShipDate() {
        return shipDate;
    }

    public void setCustomerName(String customerName) {
        String oldCustomerName = this.customerName;
        this.customerName = customerName;
        _propertyChangeSupport.firePropertyChange("customerName", oldCustomerName, customerName);
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setShipMethod(String shipMethod) {
        String oldShipMethod = this.shipMethod;
        this.shipMethod = shipMethod;
        _propertyChangeSupport.firePropertyChange("shipMethod", oldShipMethod, shipMethod);
    }

    public String getShipMethod() {
        return shipMethod;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.removePropertyChangeListener(l);
    }
}
