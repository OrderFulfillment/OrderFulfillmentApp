package ofa.mobile.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import java.util.Locale;

import ofa.mobile.pojo.InboundShipment;
import ofa.mobile.pojo.PoLines;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class InboundShipmentService {
    private static List<InboundShipment> inShipments;

    public InboundShipmentService() {
        super();
    }

    private void invokeService() {
        inShipments = new ArrayList<InboundShipment>();
        ServiceManager serviceManager = new ServiceManager();
        String datefromUser = null;
        String formattedDate = null;

        try {
            datefromUser = AdfmfJavaUtilities.getELValue("#{pageFlowScope.inboundDate}").toString();
            DateFormat originalFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
            DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Date date = originalFormat.parse(datefromUser);
            formattedDate = targetFormat.format(date);
        } catch (NullPointerException e) {
            return;
        } catch (ParseException e) {
            return;
        }

        String jsonArrayAsString = serviceManager.invokeREAD(RestURIs.getInboundShipment(formattedDate));
        try {
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PC_IN_SHIP_HDR");
            JSONArray nodeArray = parentnode.getJSONArray("PC_IN_SHIP_HDR_ITEM");
            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                InboundShipment inboundShipment = new InboundShipment(temp);
                inShipments.add(inboundShipment);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public InboundShipment[] getInboundShipment() {
        InboundShipment[] inboundArray = null;
        invokeService();
        Iterator<InboundShipment> itr = inShipments.iterator();
        List<InboundShipment> tempList = new ArrayList<InboundShipment>();
        while (itr.hasNext()) {
            InboundShipment lines = itr.next();
            tempList.add(lines);
        }
        inboundArray = tempList.toArray(new InboundShipment[0]);
        return inboundArray;
    }
}
