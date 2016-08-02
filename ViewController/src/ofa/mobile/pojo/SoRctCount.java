package ofa.mobile.pojo;

import java.math.BigDecimal;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class SoRctCount {
    String countType, countName;
    int countValue;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public SoRctCount() {
        super();
    }

    public SoRctCount(JSONObject temp) throws JSONException {
        this.countType = temp.getString("COUNT_TYPE");
        this.countName = temp.getString("COUNT_NAME");
        this.countValue = temp.getInt("COUNT_VALUE");
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

    public void setCountValue(int countValue) {
        int oldCountValue = this.countValue;
        this.countValue = countValue;
        _propertyChangeSupport.firePropertyChange("countValue", oldCountValue, countValue);
    }

    public int getCountValue() {
        return countValue;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.removePropertyChangeListener(l);
    }
}
