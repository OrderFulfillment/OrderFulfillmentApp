package ofa.mobile.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import ofa.mobile.pojo.OutboundShipment;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class OutboundShipmentService {
    private static List<OutboundShipment> outShipments;

    public OutboundShipmentService() {
        super();
    }

    private void invokeService() {
        outShipments = new ArrayList<OutboundShipment>();
        ServiceManager serviceManager = new ServiceManager();
        String datefromUser = null;
        String formattedDate = null;

        try {
            datefromUser = AdfmfJavaUtilities.getELValue("#{pageFlowScope.outboundDate}").toString();
            DateFormat originalFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
            DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Date date = originalFormat.parse(datefromUser);
            formattedDate = targetFormat.format(date);
        } catch (NullPointerException e) {
            return;
        } catch (ParseException e) {
            return;
        }

        String jsonArrayAsString = serviceManager.invokeREAD(RestURIs.getOutboundShipment(formattedDate));
        try {
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PC_OUT_SHIP_HDR");
            JSONArray nodeArray = parentnode.getJSONArray("PC_OUT_SHIP_HDR_ITEM");
            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                OutboundShipment outboundShipment = new OutboundShipment(temp);
                outShipments.add(outboundShipment);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public OutboundShipment[] getOutboundShipment() {
        OutboundShipment[] outboundArray = null;
        invokeService();
        Iterator<OutboundShipment> itr = outShipments.iterator();
        List<OutboundShipment> tempList = new ArrayList<OutboundShipment>();
        while (itr.hasNext()) {
            OutboundShipment lines = itr.next();
            tempList.add(lines);
        }
        outboundArray = tempList.toArray(new OutboundShipment[0]);
        return outboundArray;
    }
}
