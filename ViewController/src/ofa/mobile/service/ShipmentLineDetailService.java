package ofa.mobile.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ofa.mobile.pojo.ShipmentDetail;
import ofa.mobile.pojo.ShipmentLineDetail;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class ShipmentLineDetailService {
    private static List<ShipmentLineDetail> lineList;

    public ShipmentLineDetailService() {
        super();
    }

    public ShipmentLineDetail[] getShipmentDetail() {
        ShipmentLineDetail[] shipmentLineArray = null;
        invokeService();
        Iterator<ShipmentLineDetail> itr = lineList.iterator();
        List<ShipmentLineDetail> tempList = new ArrayList<ShipmentLineDetail>();
        while (itr.hasNext()) {
            ShipmentLineDetail lines = itr.next();
            tempList.add(lines);
        }
        shipmentLineArray = tempList.toArray(new ShipmentLineDetail[0]);
        return shipmentLineArray;
    }

    private void invokeService() {
        lineList = new ArrayList<ShipmentLineDetail>();
        ServiceManager serviceManager = new ServiceManager();
        String shipmentLineStatus = AdfmfJavaUtilities.getELValue("#{pageFlowScope.shipmentLineStatus}").toString();
        String jsonArrayAsString = serviceManager.invokeREAD(RestURIs.getDailyProgressLines(shipmentLineStatus));
        try {
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PC_SHIPMENT_LINE_DET");
            JSONArray nodeArray = parentnode.getJSONArray("PC_SHIPMENT_LINE_DET_ITEM");
            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                ShipmentLineDetail detail = new ShipmentLineDetail(temp);
                lineList.add(detail);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }
}
