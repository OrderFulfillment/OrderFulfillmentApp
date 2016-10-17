package ofa.mobile.pojo;

import java.math.BigDecimal;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class SoRctCount {
    String countType, countName;
    String countValue;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public SoRctCount() {
        super();
    }

    public SoRctCount(JSONObject temp) throws JSONException {
        StringBuffer local;
        local = new StringBuffer(temp.getString("COUNT_TYPE"));
        if (local.indexOf("@nil") == -1) {
            this.countType = temp.getString("COUNT_TYPE");
        }

        local = new StringBuffer(temp.getString("COUNT_NAME"));
        if (local.indexOf("@nil") == -1) {
            this.countName = temp.getString("COUNT_NAME");
        }

        local = new StringBuffer(temp.getString("COUNT_VALUE"));
        if (local.indexOf("@nil") == -1) {
            this.countValue = String.valueOf(temp.getInt("COUNT_VALUE"));
        }
    }

    public void setCountValue(String countValue) {
        String oldCountValue = this.countValue;
        this.countValue = countValue;
        _propertyChangeSupport.firePropertyChange("countValue", oldCountValue, countValue);
    }

    public String getCountValue() {
        return countValue;
    }

    public void setCountType(String countType) {
        String oldCountType = this.countType;
        this.countType = countType;
        _propertyChangeSupport.firePropertyChange("countType", oldCountType, countType);
    }

    public String getCountType() {
        return countType;
    }

    public void setCountName(String countName) {
        String oldCountName = this.countName;
        this.countName = countName;
        _propertyChangeSupport.firePropertyChange("countName", oldCountName, countName);
    }

    public String getCountName() {
        return countName;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.removePropertyChangeListener(l);
    }
}
