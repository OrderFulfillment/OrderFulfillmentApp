package ofa.mobile.pojo;

import java.math.BigDecimal;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class PoHeaders {
    BigDecimal docHeaderId, orderValue;
    String documentNumber, customer, location;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public PoHeaders() {
        super();
    }

    public PoHeaders(JSONObject temp) throws JSONException {
        this.setDocHeaderId(new BigDecimal(temp.getString("DOC_HEADER_ID")));
        this.setCustomer(temp.getString("CUSTOMER"));
        this.setDocumentNumber(temp.getString("DOCUMENT_NUMBER"));
        this.setLocation(temp.getString("LOCATION"));
        this.setOrderValue(new BigDecimal(temp.getString("ORDER_VALUE")));
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
