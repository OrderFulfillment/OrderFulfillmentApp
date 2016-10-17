package ofa.mobile.pojo;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class SoRctDetailsL2 {
    String countAgingFlag, soRcptFlag, customerName;
    Integer count;
    Integer customerId;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public SoRctDetailsL2() {
        super();
    }

    public SoRctDetailsL2(JSONObject temp) throws JSONException {
        StringBuffer local;
        local = new StringBuffer(temp.getString("COUNT"));
        if (local.indexOf("@nil") == -1) {
            this.setCount(new Integer(temp.getString("COUNT")));
        }

        local = new StringBuffer(temp.getString("COUNT_AGING_FLAG"));
        if (local.indexOf("@nil") == -1) {
            this.setCountAgingFlag(temp.getString("COUNT_AGING_FLAG"));
        }

        local = new StringBuffer(temp.getString("CUSTOMER_ID"));
        if (local.indexOf("@nil") == -1) {
            this.setCustomerId(new Integer(temp.getString("CUSTOMER_ID")));
        }

        local = new StringBuffer(temp.getString("CUSTOMER_NAME"));
        if (local.indexOf("@nil") == -1) {
            this.setCustomerName(temp.getString("CUSTOMER_NAME"));
        }

        local = new StringBuffer(temp.getString("SO_RCPT_FLAG"));
        if (local.indexOf("@nil") == -1) {
            this.setSoRcptFlag(temp.getString("SO_RCPT_FLAG"));
        }
    }

    public void setCount(Integer count) {
        Integer oldCount = this.count;
        this.count = count;
        _propertyChangeSupport.firePropertyChange("count", oldCount, count);
    }

    public Integer getCount() {
        return count;
    }

    public void setCustomerId(Integer customerId) {
        Integer oldCustomerId = this.customerId;
        this.customerId = customerId;
        _propertyChangeSupport.firePropertyChange("customerId", oldCustomerId, customerId);
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCountAgingFlag(String countAgingFlag) {
        String oldCountAgingFlag = this.countAgingFlag;
        this.countAgingFlag = countAgingFlag;
        _propertyChangeSupport.firePropertyChange("countAgingFlag", oldCountAgingFlag, countAgingFlag);
    }

    public String getCountAgingFlag() {
        return countAgingFlag;
    }

    public void setSoRcptFlag(String soRcptFlag) {
        String oldSoRcptFlag = this.soRcptFlag;
        this.soRcptFlag = soRcptFlag;
        _propertyChangeSupport.firePropertyChange("soRcptFlag", oldSoRcptFlag, soRcptFlag);
    }

    public String getSoRcptFlag() {
        return soRcptFlag;
    }

    public void setCustomerName(String customerName) {
        String oldCustomerName = this.customerName;
        this.customerName = customerName;
        _propertyChangeSupport.firePropertyChange("customerName", oldCustomerName, customerName);
    }

    public String getCustomerName() {
        return customerName;
    }


    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.removePropertyChangeListener(l);
    }
}
