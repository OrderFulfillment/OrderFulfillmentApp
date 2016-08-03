package ofa.mobile.pojo;

import java.math.BigDecimal;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class PoLines {
    BigDecimal docHeaderId, docLineId, quantity;
    String docLineNum, itemNumber, carrier, warehouse, ssd;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public PoLines() {
        super();
    }

    public PoLines(JSONObject temp) throws JSONException {
        this.setCarrier(temp.getString("CARRIER"));
        this.setDocHeaderId(new BigDecimal(temp.getString("DOC_HEADER_ID")));
        this.setDocLineId(new BigDecimal(temp.getString("DOC_LINE_ID")));
        this.setDocLineNum(temp.getString("DOC_LINE_NUM"));
        this.setItemNumber(temp.getString("ITEM_NUMBER"));
        this.setQuantity(new BigDecimal(temp.getString("QUANTITY")));
        this.setSsd(temp.getString("SSD"));
        this.setWarehouse(temp.getString("WAREHOUSE"));

    }

    public void setDocHeaderId(BigDecimal docHeaderId) {
        BigDecimal oldDocHeaderId = this.docHeaderId;
        this.docHeaderId = docHeaderId;
        _propertyChangeSupport.firePropertyChange("docHeaderId", oldDocHeaderId, docHeaderId);
    }

    public BigDecimal getDocHeaderId() {
        return docHeaderId;
    }

    public void setDocLineId(BigDecimal docLineId) {
        BigDecimal oldDocLineId = this.docLineId;
        this.docLineId = docLineId;
        _propertyChangeSupport.firePropertyChange("docLineId", oldDocLineId, docLineId);
    }

    public BigDecimal getDocLineId() {
        return docLineId;
    }

    public void setQuantity(BigDecimal quantity) {
        BigDecimal oldQuantity = this.quantity;
        this.quantity = quantity;
        _propertyChangeSupport.firePropertyChange("quantity", oldQuantity, quantity);
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setDocLineNum(String docLineNum) {
        String oldDocLineNum = this.docLineNum;
        this.docLineNum = docLineNum;
        _propertyChangeSupport.firePropertyChange("docLineNum", oldDocLineNum, docLineNum);
    }

    public String getDocLineNum() {
        return docLineNum;
    }

    public void setItemNumber(String itemNumber) {
        String oldItemNumber = this.itemNumber;
        this.itemNumber = itemNumber;
        _propertyChangeSupport.firePropertyChange("itemNumber", oldItemNumber, itemNumber);
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setCarrier(String carrier) {
        String oldCarrier = this.carrier;
        this.carrier = carrier;
        _propertyChangeSupport.firePropertyChange("carrier", oldCarrier, carrier);
    }

    public String getCarrier() {
        return carrier;
    }

    public void setWarehouse(String warehouse) {
        String oldWarehouse = this.warehouse;
        this.warehouse = warehouse;
        _propertyChangeSupport.firePropertyChange("warehouse", oldWarehouse, warehouse);
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setSsd(String ssd) {
        String oldSsd = this.ssd;
        this.ssd = ssd;
        _propertyChangeSupport.firePropertyChange("ssd", oldSsd, ssd);
    }

    public String getSsd() {
        return ssd;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.removePropertyChangeListener(l);
    }
}
