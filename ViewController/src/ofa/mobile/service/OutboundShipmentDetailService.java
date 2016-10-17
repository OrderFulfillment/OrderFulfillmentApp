package ofa.mobile.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ofa.mobile.pojo.OutboundShipmentDetail;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class OutboundShipmentDetailService {
    private static List<OutboundShipmentDetail> outShipmentsDetails;

    public OutboundShipmentDetailService() {
        super();
    }

    private void invokeService() {
        outShipmentsDetails = new ArrayList<OutboundShipmentDetail>();
        ServiceManager serviceManager = new ServiceManager();

        String headerId = AdfmfJavaUtilities.getELValue("#{pageFlowScope.headerId}").toString();

        String jsonArrayAsString = serviceManager.invokeREAD(RestURIs.getOutboundShipmentLines(headerId));
        try {
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PC_OUT_SHIP_LINE");
            JSONArray nodeArray = parentnode.getJSONArray("PC_OUT_SHIP_LINE_ITEM");
            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                OutboundShipmentDetail outboundShipmentDetail = new OutboundShipmentDetail(temp);
                outShipmentsDetails.add(outboundShipmentDetail);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public OutboundShipmentDetail[] getOutboundShipmentDetail() {
        OutboundShipmentDetail[] outboundDetailArray = null;
        invokeService();
        Iterator<OutboundShipmentDetail> itr = outShipmentsDetails.iterator();
        List<OutboundShipmentDetail> tempList = new ArrayList<OutboundShipmentDetail>();
        while (itr.hasNext()) {
            OutboundShipmentDetail lines = itr.next();
            tempList.add(lines);
        }
        outboundDetailArray = tempList.toArray(new OutboundShipmentDetail[0]);
        return outboundDetailArray;
    }
}
