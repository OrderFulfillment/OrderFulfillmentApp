package ofa.mobile.service;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

import ofa.mobile.constants.OfaConstants;
import ofa.mobile.pojo.SoRctCount;
import ofa.mobile.pojo.SoRctDetailsL2;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class SoRctDetailsL2Service {
    private static List<SoRctDetailsL2> soRctDetailsL2List;

    public SoRctDetailsL2Service() {
        super();
        invokeService();
    }

    Boolean isBackButton() {
        if (AdfmfJavaUtilities.getELValue("#{pageFlowScope.backFrom}") == OfaConstants.BACK_FROM_ORDERS_EXCEPTION)
            return true;
        return false;
    }

    private void invokeService() {
        try {
            soRctDetailsL2List = new ArrayList<SoRctDetailsL2>();
            ServiceManager serviceManager = new ServiceManager();
            String completeStatus = AdfmfJavaUtilities.getELValue("#{pageFlowScope.orderOrRctStatus}").toString();
            String status = null;
            switch (completeStatus) {
            case "On Hold":
                status = "H";
                break;
            case "Back Ordered Count":
                status = "B";
                break;
            case "In Jeopardy":
                status = "J";
                break;
            }
            String soOrRct = AdfmfJavaUtilities.getELValue("#{pageFlowScope.soOrRct}").toString();
            String countOrAging = AdfmfJavaUtilities.getELValue("C").toString();
            String jsonArrayAsString =
                serviceManager.invokeREAD(RestURIs.getSoRctDetailsL2URI(status, soOrRct, countOrAging));

            try {
                JSONObject jsonObject = new JSONObject(jsonArrayAsString);
                JSONObject parentnode = jsonObject.getJSONObject("PX_CUST_COUNT");
                JSONArray nodeArray = parentnode.getJSONArray("PX_CUST_COUNT_ITEM");
                int size = nodeArray.length();
                for (int i = 0; i < size; i++) {
                    JSONObject temp = nodeArray.getJSONObject(i);
                    SoRctDetailsL2 soRctdetailsL2 = new SoRctDetailsL2(temp);
                    soRctDetailsL2List.add(soRctdetailsL2);
                }
            } catch (Exception e) {
                e.getMessage();
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SoRctDetailsL2[] getRctDetailsCountL2() {
        SoRctDetailsL2[] rctDetailsArray = null;
        try {
            if (soRctDetailsL2List.isEmpty() || isBackButton()) {
                invokeService();
            }
            Iterator<SoRctDetailsL2> itr = soRctDetailsL2List.iterator();
            List<SoRctDetailsL2> tempList = new ArrayList<SoRctDetailsL2>();
            while (itr.hasNext()) {
                SoRctDetailsL2 rctDetails = itr.next();
                if (rctDetails.getSoRcptFlag().equals("R") && rctDetails.getCountAgingFlag().equals("C"))
                    tempList.add(rctDetails);
            }
            rctDetailsArray = tempList.toArray(new SoRctDetailsL2[tempList.size()]);
            return rctDetailsArray;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rctDetailsArray;
    }

    public SoRctDetailsL2[] getRctDetailsAgingL2() {
        SoRctDetailsL2[] rctDetailsArray = null;
        try {
            if (soRctDetailsL2List.isEmpty() || isBackButton()) {
                invokeService();
            }
            Iterator<SoRctDetailsL2> itr = soRctDetailsL2List.iterator();
            List<SoRctDetailsL2> tempList = new ArrayList<SoRctDetailsL2>();
            while (itr.hasNext()) {
                SoRctDetailsL2 rctDetails = itr.next();
                if (rctDetails.getSoRcptFlag().equals("R") && rctDetails.getCountAgingFlag().equals("A"))
                    tempList.add(rctDetails);
            }
            rctDetailsArray = tempList.toArray(new SoRctDetailsL2[tempList.size()]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rctDetailsArray;
    }

    public SoRctDetailsL2[] getSoDetailsAgingL2() {
        SoRctDetailsL2[] soDetailsArray = null;
        try {
            if (soRctDetailsL2List.isEmpty() || isBackButton()) {
                invokeService();
            }
            Iterator<SoRctDetailsL2> itr = soRctDetailsL2List.iterator();
            List<SoRctDetailsL2> tempList = new ArrayList<SoRctDetailsL2>();
            while (itr.hasNext()) {
                SoRctDetailsL2 soDetails = itr.next();
                if (soDetails.getSoRcptFlag().equals("S") && soDetails.getCountAgingFlag().equals("A"))
                    tempList.add(soDetails);
            }
            soDetailsArray = tempList.toArray(new SoRctDetailsL2[tempList.size()]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soDetailsArray;
    }

    public SoRctDetailsL2[] getSoDetailsCountL2() {
        SoRctDetailsL2[] soDetailsArray = null;
        try {
            if (soRctDetailsL2List.isEmpty() || isBackButton()) {
                invokeService();
            }
            Iterator<SoRctDetailsL2> itr = soRctDetailsL2List.iterator();
            List<SoRctDetailsL2> tempList = new ArrayList<SoRctDetailsL2>();
            while (itr.hasNext()) {
                SoRctDetailsL2 soDetails = itr.next();
                if (soDetails.getSoRcptFlag().equals("S") && soDetails.getCountAgingFlag().equals("C"))
                    tempList.add(soDetails);
            }
            soDetailsArray = tempList.toArray(new SoRctDetailsL2[tempList.size()]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soDetailsArray;
    }
}
