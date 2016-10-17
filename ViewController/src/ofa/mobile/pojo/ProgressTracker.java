package ofa.mobile.pojo;

import java.util.ArrayList;

import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class ProgressTracker {
    ArrayList<Shipment> shipments;
    ArrayList<ShipmentLine> shipmentLines;

    public ProgressTracker() {
        super();
    }

    public ProgressTracker(JSONObject temp) throws JSONException {
        JSONObject shipmentCount = temp.getJSONObject("PC_SHIP_COUNT");
        JSONArray shipmentCountArray = shipmentCount.getJSONArray("PC_SHIP_COUNT_ITEM");
        shipments = new ArrayList<Shipment>();

        for (int i = 0; i < shipmentCountArray.length(); i++) {
            shipments.add(new Shipment(shipmentCountArray.getJSONObject(i)));
        }

        JSONObject lineCount = temp.getJSONObject("PC_LINE_COUNT");
        JSONArray shipmentLineArray = lineCount.getJSONArray("PC_LINE_COUNT_ITEM");
        shipmentLines = new ArrayList<ShipmentLine>();

        for (int i = 0; i < shipmentLineArray.length(); i++) {
            shipmentLines.add(new ShipmentLine(shipmentLineArray.getJSONObject(i)));
        }
    }

    public ArrayList<Shipment> getShipments() {
        return shipments;
    }

    public ArrayList<ShipmentLine> getShipmentLines() {
        return shipmentLines;
    }
}
