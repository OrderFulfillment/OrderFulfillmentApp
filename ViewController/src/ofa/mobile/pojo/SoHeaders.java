package ofa.mobile.pojo;

import java.math.BigDecimal;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class SoHeaders {
    BigDecimal docHeaderId, orderValue;
    String documentNumber, customer, location;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public SoHeaders() {
        super();
    }

    public SoHeaders(JSONObject temp) throws JSONException {
        StringBuffer local;
        local = new StringBuffer(temp.getString("DOC_HEADER_ID"));
        if (local.indexOf("@nil") == -1) {
            this.setDocHeaderId(new BigDecimal(temp.getString("DOC_HEADER_ID")));
        }

        local = new StringBuffer(temp.getString("CUSTOMER"));
        if (local.indexOf("@nil") == -1) {
            this.setCustomer(temp.getString("CUSTOMER"));
        }

        local = new StringBuffer(temp.getString("DOCUMENT_NUMBER"));
        if (local.indexOf("@nil") == -1) {
            this.setDocumentNumber(temp.getString("DOCUMENT_NUMBER"));
        }

        local = new StringBuffer(temp.getString("LOCATION"));
        if (local.indexOf("@nil") == -1) {
            this.setLocation(temp.getString("LOCATION"));
        }

        local = new StringBuffer(temp.getString("ORDER_VALUE"));
        if (local.indexOf("@nil") == -1) {
            this.setOrderValue(new BigDecimal(temp.getString("ORDER_VALUE")));
        }
    }

    public void setDocHeaderId(BigDecimal docHeaderId) {
        BigDecimal oldDocHeaderId = this.docHeaderId;
        this.docHeaderId = docHeaderId;
        _propertyChangeSupport.firePropertyChange("docHeaderId", oldDocHeaderId, docHeaderId);
    }

    public BigDecimal getDocHeaderId() {
        return docHeaderId;
    }

    public void setOrderValue(BigDecimal orderValue) {
        BigDecimal oldOrderValue = this.orderValue;
        this.orderValue = orderValue;
        _propertyChangeSupport.firePropertyChange("orderValue", oldOrderValue, orderValue);
    }

    public BigDecimal getOrderValue() {
        return orderValue;
    }

    public void setDocumentNumber(String documentNumber) {
        String oldDocumentNumber = this.documentNumber;
        this.documentNumber = documentNumber;
        _propertyChangeSupport.firePropertyChange("documentNumber", oldDocumentNumber, documentNumber);
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setCustomer(String customer) {
        String oldCustomer = this.customer;
        this.customer = customer;
        _propertyChangeSupport.firePropertyChange("customer", oldCustomer, customer);
    }

    public String getCustomer() {
        return customer;
    }

    public void setLocation(String location) {
        String oldLocation = this.location;
        this.location = location;
        _propertyChangeSupport.firePropertyChange("location", oldLocation, location);
    }

    public String getLocation() {
        return location;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.removePropertyChangeListener(l);
    }
}
