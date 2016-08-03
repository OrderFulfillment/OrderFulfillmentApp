package ofa.mobile.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ofa.mobile.pojo.PoLines;
import ofa.mobile.pojo.PoLines;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class PoLinesService {
    private static List<PoLines> poLinesList;

    public PoLinesService() {
        super();
    }

    private void invokeService() {
        poLinesList = new ArrayList<PoLines>();
        ServiceManager serviceManager = new ServiceManager();
        String orderId = AdfmfJavaUtilities.getELValue("#{pageFlowScope.orderId}").toString();

        String jsonArrayAsString = serviceManager.invokeREAD(RestURIs.getSoLinesURI(orderId, "S"));
        try {
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PX_LINE_TYPE");
            JSONArray nodeArray = parentnode.getJSONArray("PX_LINE_TYPE_ITEM");
            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                PoLines solines = new PoLines(temp);
                poLinesList.add(solines);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public PoLines[] getPoLines() {
        PoLines[] soLinesArray = null;
        invokeService();
        Iterator<PoLines> itr = poLinesList.iterator();
        List<PoLines> tempList = new ArrayList<PoLines>();
        while (itr.hasNext()) {
            PoLines lines = itr.next();
            tempList.add(lines);
        }
        soLinesArray = tempList.toArray(new PoLines[tempList.size()]);
        return soLinesArray;
    }
}
