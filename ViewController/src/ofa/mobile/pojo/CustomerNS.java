package ofa.mobile.pojo;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class CustomerNS {
    String customer;
    int count, customerId;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public CustomerNS() {
        super();
    }

    public CustomerNS(JSONObject temp) throws JSONException {
        StringBuffer local;
        local = new StringBuffer(temp.getString("DETAILS"));
        if (local.indexOf("@nil") == -1) {
            this.setCustomer(temp.getString("DETAILS"));
        }

        local = new StringBuffer(temp.getInt("COUNT"));
        if (local.indexOf("@nil") == -1) {
            this.setCount(temp.getInt("COUNT"));
        }

        local = new StringBuffer(temp.getInt("CUSTOMER_ID"));
        if (local.indexOf("@nil") == -1) {
            this.setCustomerId(temp.getInt("CUSTOMER_ID"));
        }
    }

    public void setCustomer(String customer) {
        String oldCustomer = this.customer;
        this.customer = customer;
        _propertyChangeSupport.firePropertyChange("customer", oldCustomer, customer);
    }

    public String getCustomer() {
        return customer;
    }

    public void setCount(int count) {
        int oldCount = this.count;
        this.count = count;
        _propertyChangeSupport.firePropertyChange("count", oldCount, count);
    }

    public int getCount() {
        return count;
    }

    public void setCustomerId(int customerId) {
        int oldCustomerId = this.customerId;
        this.customerId = customerId;
        _propertyChangeSupport.firePropertyChange("customerId", oldCustomerId, customerId);
    }

    public int getCustomerId() {
        return customerId;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.removePropertyChangeListener(l);
    }
}
