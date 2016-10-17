package ofa.mobile.pojo;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class ShipmentLine {
    String lineStatus;
    int lineCount;
    double percentage;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public ShipmentLine() {
        super();
    }

    public ShipmentLine(JSONObject temp) throws JSONException {
        StringBuffer local;
        local = new StringBuffer(temp.getString("LINE_STATUS"));
        if (local.indexOf("@nil") == -1) {
            this.setLineStatus(temp.getString("LINE_STATUS"));
        }

        local = new StringBuffer(temp.getString("LINE_CNT"));
        if (local.indexOf("@nil") == -1) {
            this.setLineCount(new Integer(temp.getString("LINE_CNT")));
        }

        local = new StringBuffer(temp.getString("PERCENTAGE"));
        if (local.indexOf("@nil") == -1) {
            this.setPercentage(new Double(temp.getString("PERCENTAGE")));
        }
    }

    public void setLineStatus(String lineStatus) {
        String oldLineStatus = this.lineStatus;
        this.lineStatus = lineStatus;
        _propertyChangeSupport.firePropertyChange("lineStatus", oldLineStatus, lineStatus);
    }

    public String getLineStatus() {
        return lineStatus;
    }

    public void setLineCount(int lineCount) {
        int oldLineCount = this.lineCount;
        this.lineCount = lineCount;
        _propertyChangeSupport.firePropertyChange("lineCount", oldLineCount, lineCount);
    }

    public int getLineCount() {
        return lineCount;
    }

    public void setPercentage(double percentage) {
        double oldPercentage = this.percentage;
        this.percentage = percentage;
        _propertyChangeSupport.firePropertyChange("percentage", oldPercentage, percentage);
    }

    public double getPercentage() {
        return percentage;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.removePropertyChangeListener(l);
    }
}
