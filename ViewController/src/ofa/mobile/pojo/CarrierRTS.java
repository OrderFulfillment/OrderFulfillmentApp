package ofa.mobile.pojo;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class CarrierRTS {
    String carrier;
    int count;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public CarrierRTS() {
        super();
    }

    public CarrierRTS(JSONObject temp) throws JSONException {
        StringBuffer local;
        local = new StringBuffer(temp.getString("DETAILS"));
        if (local.indexOf("@nil") == -1) {
            this.setCarrier(temp.getString("DETAILS"));
        }

        local = new StringBuffer(temp.getInt("COUNT"));
        if (local.indexOf("@nil") == -1) {
            this.setCount(temp.getInt("COUNT"));
        }
    }

    public void setCarrier(String carrier) {
        String oldCarrier = this.carrier;
        this.carrier = carrier;
        _propertyChangeSupport.firePropertyChange("carrier", oldCarrier, carrier);
    }

    public String getCarrier() {
        return carrier;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.removePropertyChangeListener(l);
    }

    public void setCount(int count) {
        int oldCount = this.count;
        this.count = count;
        _propertyChangeSupport.firePropertyChange("count", oldCount, count);
    }

    public int getCount() {
        return count;
    }

}
