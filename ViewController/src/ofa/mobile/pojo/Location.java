package ofa.mobile.pojo;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class Location {
    String locationName;
    Integer locationId;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public Location() {
        super();
    }

    public Location(JSONObject temp) throws JSONException {
        StringBuffer local;
        local = new StringBuffer(temp.getString("LOCATIONS"));
        if (local.indexOf("@nil") == -1) {
            this.setLocationName(temp.getString("LOCATIONS"));
        }

        local = new StringBuffer(temp.getString("LOCATION_ID"));
        if (local.indexOf("@nil") == -1) {
            this.setLocationId(temp.getInt("LOCATION_ID"));
        }
    }

    public void setLocationId(Integer locationId) {
        Integer oldLocationId = this.locationId;
        this.locationId = locationId;
        _propertyChangeSupport.firePropertyChange("locationId", oldLocationId, locationId);
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setPropertyChangeSupport(PropertyChangeSupport _propertyChangeSupport) {
        PropertyChangeSupport oldPropertyChangeSupport = this._propertyChangeSupport;
        this._propertyChangeSupport = _propertyChangeSupport;
        _propertyChangeSupport.firePropertyChange("propertyChangeSupport", oldPropertyChangeSupport,
                                                  _propertyChangeSupport);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return _propertyChangeSupport;
    }

    public void setLocationName(String locationName) {
        String oldLocationName = this.locationName;
        this.locationName = locationName;
        _propertyChangeSupport.firePropertyChange("locationName", oldLocationName, locationName);
    }

    public String getLocationName() {
        return locationName;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.removePropertyChangeListener(l);
    }
}
