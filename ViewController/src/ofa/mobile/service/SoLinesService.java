package ofa.mobile.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ofa.mobile.pojo.SoLines;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class SoLinesService {
    private static List<SoLines> soLinesList;

    public SoLinesService() {
        super();
    }

    private void invokeService() {
        soLinesList = new ArrayList<SoLines>();
        ServiceManager serviceManager = new ServiceManager();
        String orderId = AdfmfJavaUtilities.getELValue("#{pageFlowScope.orderId}").toString();
        String soOrRct = AdfmfJavaUtilities.getELValue("#{pageFlowScope.soOrRct}").toString();
        String ageorCount = AdfmfJavaUtilities.getELValue("#{pageFlowScope.ageOrCount}").toString();
        String completeStatus = AdfmfJavaUtilities.getELValue("#{pageFlowScope.orderOrRctStatus}").toString();
        String status = null;
        switch (completeStatus) {
        case "On Hold":
            status = "H";
            break;
        case "Back Ordered":
            status = "B";
            break;
        case "In Jeopardy":
            status = "J";
            break;
        default:
            status = "P";
            break;
        }
        String jsonArrayAsString =
            serviceManager.invokeREAD(RestURIs.getSoLinesURI(orderId, status, soOrRct, ageorCount, 0, 200));
        try {
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PX_LINE_TYPE");
            JSONArray nodeArray = parentnode.getJSONArray("PX_LINE_TYPE_ITEM");
            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                SoLines solines = new SoLines(temp);
                soLinesList.add(solines);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public SoLines[] getSoLines() {
        SoLines[] soLinesArray = null;
        invokeService();
        Iterator<SoLines> itr = soLinesList.iterator();
        List<SoLines> tempList = new ArrayList<SoLines>();
        while (itr.hasNext()) {
            SoLines lines = itr.next();
            tempList.add(lines);
        }
        soLinesArray = tempList.toArray(new SoLines[0]);
        return soLinesArray;
    }
}
