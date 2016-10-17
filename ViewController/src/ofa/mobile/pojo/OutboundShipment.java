package ofa.mobile.pojo;

import java.math.BigDecimal;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class OutboundShipment {
    String shipNumber, headerId, value, source, identifier;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public OutboundShipment() {
        super();
    }

    public OutboundShipment(JSONObject temp) throws JSONException {
        StringBuffer local;
        local = new StringBuffer(temp.getString("SHIP_NUM"));
        if (local.indexOf("@nil") == -1) {
            this.setShipNumber(temp.getString("SHIP_NUM"));
        }

        local = new StringBuffer(temp.getString("HEADER_ID"));
        if (local.indexOf("@nil") == -1) {
            this.setHeaderId(temp.getString("HEADER_ID"));
        }

        local = new StringBuffer(temp.getString("VALUE"));
        if (local.indexOf("@nil") == -1) {
            this.setValue(temp.getString("VALUE"));
        }

        local = new StringBuffer(temp.getString("SOURCE"));
        if (local.indexOf("@nil") == -1) {
            this.setSource(temp.getString("SOURCE"));
        }

        local = new StringBuffer(temp.getString("IDENTIFIER"));
        if (local.indexOf("@nil") == -1) {
            this.setIdentifier(temp.getString("IDENTIFIER"));
        }
    }

    public void setShipNumber(String shipNumber) {
        String oldShipNumber = this.shipNumber;
        this.shipNumber = shipNumber;
        _propertyChangeSupport.firePropertyChange("shipNumber", oldShipNumber, shipNumber);
    }

    public String getShipNumber() {
        return shipNumber;
    }

    public void setHeaderId(String headerId) {
        String oldHeaderId = this.headerId;
        this.headerId = headerId;
        _propertyChangeSupport.firePropertyChange("headerId", oldHeaderId, headerId);
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setValue(String value) {
        String oldValue = this.value;
        this.value = value;
        _propertyChangeSupport.firePropertyChange("value", oldValue, value);
    }

    public String getValue() {
        return value;
    }

    public void setSource(String source) {
        String oldSource = this.source;
        this.source = source;
        _propertyChangeSupport.firePropertyChange("source", oldSource, source);
    }

    public String getSource() {
        return source;
    }

    public void setIdentifier(String identifier) {
        String oldIdentifier = this.identifier;
        this.identifier = identifier;
        _propertyChangeSupport.firePropertyChange("identifier", oldIdentifier, identifier);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.removePropertyChangeListener(l);
    }
}
