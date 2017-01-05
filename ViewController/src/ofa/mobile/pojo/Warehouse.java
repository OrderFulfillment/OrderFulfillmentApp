package ofa.mobile.pojo;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class Warehouse {
    String warehouse;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public Warehouse() {
        super();
    }

    public Warehouse(JSONObject temp) throws JSONException {
        StringBuffer local;
        local = new StringBuffer(temp.getString("WAREHOUSE"));
        if (local.indexOf("@nil") == -1) {
            this.setWarehouse(temp.getString("WAREHOUSE"));
        }
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
