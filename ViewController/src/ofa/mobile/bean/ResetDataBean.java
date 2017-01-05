package ofa.mobile.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import ofa.mobile.constants.OfaConstants;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.javax.faces.model.SelectItem;

public class ResetDataBean {
    List<SelectItem> weakEnding;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);
    
    String warehouse;

    public ResetDataBean() {
    }

    public void setWarehouse(String warehouse) {
        String oldWarehouse = this.warehouse;
        this.warehouse = warehouse;
        _propertyChangeSupport.firePropertyChange("warehouse", oldWarehouse, warehouse);
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void backToHomeFromSoExcp() {
        AdfmfJavaUtilities.setELValue("#{pageFlowScope.backFrom}", OfaConstants.BACK_FROM_ORDERS_RCPT_EXCEPTION);
    }

    public void backToHomeFromRcptExcp() {
        AdfmfJavaUtilities.setELValue("#{pageFlowScope.backFrom}", OfaConstants.BACK_FROM_ORDERS_RCPT_EXCEPTION);
    }

    public void setWeakEnding(List<SelectItem> weakEnding) {
        List<SelectItem> oldWeakEnding = this.weakEnding;
        this.weakEnding = weakEnding;
        _propertyChangeSupport.firePropertyChange("weakEnding", oldWeakEnding, weakEnding);
    }

    public List<SelectItem> getWeakEnding() {
        weakEnding = new ArrayList<SelectItem>();
        Calendar c = GregorianCalendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        DateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String endDate;
        int i = 0;
        while (i < 4) {
            c.add(Calendar.DATE, i == 0 ? 0 : 7);
            endDate = df.format(c.getTime());
            weakEnding.add(new SelectItem(endDate));
            i++;
        }
        return weakEnding;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.removePropertyChangeListener(l);
    }
}
