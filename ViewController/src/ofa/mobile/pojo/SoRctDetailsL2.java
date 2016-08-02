package ofa.mobile.pojo;

import java.math.BigDecimal;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class SoRctDetailsL2 {
    String countAgingFlag, soRcptFlag, customerName;
    int count;
    BigDecimal customerId;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public SoRctDetailsL2() {
        super();
    }

    public SoRctDetailsL2(JSONObject temp) throws JSONException {
        this.setCount(temp.getInt("COUNT"));
        this.setCountAgingFlag(temp.getString("COUNT_AGING_FLAG"));
        this.setCustomerId(new BigDecimal(temp.getString("CUSTOMER_ID")));
        this.setCustomerName(temp.getString("CUSTOMER_NAME"));
        this.setSoRcptFlag(temp.getString("SO_RCPT_FLAG"));
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

    public void setCount(int count) {
        int oldCount = this.count;
        this.count = count;
        _propertyChangeSupport.firePropertyChange("count", oldCount, count);
    }

    public int getCount() {
        return count;
    }

    public void setCustomerId(BigDecimal customerId) {
        BigDecimal oldCustomerId = this.customerId;
        this.customerId = customerId;
        _propertyChangeSupport.firePropertyChange("customerId", oldCustomerId, customerId);
    }

    public BigDecimal getCustomerId() {
        return customerId;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.removePropertyChangeListener(l);
    }
}
