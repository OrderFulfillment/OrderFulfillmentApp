package ofa.mobile.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ofa.mobile.pojo.ShipmentDetail;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class ShipmentDetailService {
    private static List<ShipmentDetail> detailsList;

    public ShipmentDetailService() {
        super();
    }

    public ShipmentDetail[] getShipmentDetail() {
        ShipmentDetail[] shipmentDetailArray = null;
        invokeService();
        Iterator<ShipmentDetail> itr = detailsList.iterator();
        List<ShipmentDetail> tempList = new ArrayList<ShipmentDetail>();
        while (itr.hasNext()) {
            ShipmentDetail lines = itr.next();
            tempList.add(lines);
        }
        shipmentDetailArray = tempList.toArray(new ShipmentDetail[0]);
        return shipmentDetailArray;
    }

    private void invokeService() {
        detailsList = new ArrayList<ShipmentDetail>();
        ServiceManager serviceManager = new ServiceManager();
        String shipmentStatus = AdfmfJavaUtilities.getELValue("#{pageFlowScope.shipmentStatus}").toString();
        String jsonArrayAsString = serviceManager.invokeREAD(RestURIs.getDailyProgressHdr(shipmentStatus));
        try {
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PC_SHIPMENT_HDR_DET");
            JSONArray nodeArray = parentnode.getJSONArray("PC_SHIPMENT_HDR_DET_ITEM");
            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                ShipmentDetail detail = new ShipmentDetail(temp);
                detailsList.add(detail);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }
}
