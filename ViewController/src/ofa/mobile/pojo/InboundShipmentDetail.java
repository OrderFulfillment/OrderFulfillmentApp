package ofa.mobile.pojo;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class InboundShipmentDetail {
    String itemNumber, quantity, dueDate, warehouse;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public InboundShipmentDetail() {
        super();
    }

    public InboundShipmentDetail(JSONObject temp) throws JSONException {
        StringBuffer local;
        local = new StringBuffer(temp.getString("ITEM_NUM"));
        if (local.indexOf("@nil") == -1) {
            this.setItemNumber(temp.getString("ITEM_NUM"));
        }

        local = new StringBuffer(temp.getString("QUANTITY"));
        if (local.indexOf("@nil") == -1) {
            this.setQuantity(temp.getString("QUANTITY"));
        }

        local = new StringBuffer(temp.getString("DUE_DATE"));
        if (local.indexOf("@nil") == -1) {
            this.setDueDate(temp.getString("DUE_DATE"));
        }

        local = new StringBuffer(temp.getString("WAREHOUSE"));
        if (local.indexOf("@nil") == -1) {
            this.setWarehouse(temp.getString("WAREHOUSE"));
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

    public void setQuantity(String quantity) {
        String oldQuantity = this.quantity;
        this.quantity = quantity;
        _propertyChangeSupport.firePropertyChange("quantity", oldQuantity, quantity);
    }

    public String getQuantity() {
        return quantity;
    }

    public void setDueDate(String dueDate) {
        String oldDueDate = this.dueDate;
        this.dueDate = dueDate;
        _propertyChangeSupport.firePropertyChange("dueDate", oldDueDate, dueDate);
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setWarehouse(String warehouse) {
        String oldWarehouse = this.warehouse;
        this.warehouse = warehouse;
        _propertyChangeSupport.firePropertyChange("warehouse", oldWarehouse, warehouse);
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.removePropertyChangeListener(l);
    }
}
