package ofa.mobile.pojo;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class ShipmentLineDetail {
    String deliveryNumber, itemNumber, orderNumber, requestedQuantity;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public ShipmentLineDetail(JSONObject temp) throws JSONException {
        StringBuffer local;
        local = new StringBuffer(temp.getString("DELIVERY_NUM"));
        if (local.indexOf("@nil") == -1) {
            this.setDeliveryNumber(temp.getString("DELIVERY_NUM"));
        }

        local = new StringBuffer(temp.getString("ITEM_NUMBER"));
        if (local.indexOf("@nil") == -1) {
            this.setItemNumber(temp.getString("ITEM_NUMBER"));
        }

        local = new StringBuffer(temp.getString("ORDER_NUMBER"));
        if (local.indexOf("@nil") == -1) {
            this.setOrderNumber(temp.getString("ORDER_NUMBER"));
        }

        local = new StringBuffer(temp.getString("REQUESTED_QUANTITY"));
        if (local.indexOf("@nil") == -1) {
            this.setRequestedQuantity(temp.getString("REQUESTED_QUANTITY"));
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

    public void setItemNumber(String itemNumber) {
        String oldItemNumber = this.itemNumber;
        this.itemNumber = itemNumber;
        _propertyChangeSupport.firePropertyChange("itemNumber", oldItemNumber, itemNumber);
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setOrderNumber(String orderNumber) {
        String oldOrderNumber = this.orderNumber;
        this.orderNumber = orderNumber;
        _propertyChangeSupport.firePropertyChange("orderNumber", oldOrderNumber, orderNumber);
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setRequestedQuantity(String requestedQuantity) {
        String oldRequestedQuantity = this.requestedQuantity;
        this.requestedQuantity = requestedQuantity;
        _propertyChangeSupport.firePropertyChange("requestedQuantity", oldRequestedQuantity, requestedQuantity);
    }

    public String getRequestedQuantity() {
        return requestedQuantity;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.removePropertyChangeListener(l);
    }
}
