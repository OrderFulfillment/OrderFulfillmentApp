package ofa.mobile.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ofa.mobile.pojo.InboundShipmentDetail;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class InboundShipmentDetailService {
    private static List<InboundShipmentDetail> inShipmentsDetails;

    public InboundShipmentDetailService() {
        super();
    }

    private void invokeService() {
        inShipmentsDetails = new ArrayList<InboundShipmentDetail>();
        ServiceManager serviceManager = new ServiceManager();

        String headerId = AdfmfJavaUtilities.getELValue("#{pageFlowScope.headerId}").toString();
        String identifier = AdfmfJavaUtilities.getELValue("#{pageFlowScope.identifier}").toString();

        String jsonArrayAsString = serviceManager.invokeREAD(RestURIs.getInboundShipmentLines(headerId, identifier));
        try {
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PC_IN_SHIP_LINE");
            JSONArray nodeArray = parentnode.getJSONArray("PC_IN_SHIP_LINE_ITEM");
            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                InboundShipmentDetail inboundShipmentDetail = new InboundShipmentDetail(temp);
                inShipmentsDetails.add(inboundShipmentDetail);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public InboundShipmentDetail[] getInboundShipmentDetail() {
        InboundShipmentDetail[] inboundDetailArray = null;
        invokeService();
        Iterator<InboundShipmentDetail> itr = inShipmentsDetails.iterator();
        List<InboundShipmentDetail> tempList = new ArrayList<InboundShipmentDetail>();
        while (itr.hasNext()) {
            InboundShipmentDetail lines = itr.next();
            tempList.add(lines);
        }
        inboundDetailArray = tempList.toArray(new InboundShipmentDetail[0]);
        return inboundDetailArray;
    }
}
