package ofa.mobile.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ofa.mobile.pojo.PoHeaders;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class PoHeadersService {
    private static List<PoHeaders> poHeadersList;

    public PoHeadersService() {
        super();
    }

    private void invokeService() {
        poHeadersList = new ArrayList<PoHeaders>();
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
        Integer partyId = Integer.parseInt(AdfmfJavaUtilities.getELValue("#{pageFlowScope.customerId}").toString());
        String ageorCount = AdfmfJavaUtilities.getELValue("#{pageFlowScope.ageOrCount}").toString();
        String jsonArrayAsString =
            serviceManager.invokeREAD(RestURIs.getSoHeadersURI(status, partyId, soOrRct, ageorCount, 0, 100));
        try {
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PX_HEADER_TYPE");
            JSONArray nodeArray = parentnode.getJSONArray("PX_HEADER_TYPE_ITEM");
            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                PoHeaders poheaders = new PoHeaders(temp);
                poHeadersList.add(poheaders);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public PoHeaders[] getPoHeaders() {
        PoHeaders[] poHeadersArray = null;
        invokeService();
        Iterator<PoHeaders> itr = poHeadersList.iterator();
        List<PoHeaders> tempList = new ArrayList<PoHeaders>();
        while (itr.hasNext()) {
            PoHeaders headers = itr.next();
            tempList.add(headers);
        }
        poHeadersArray = tempList.toArray(new PoHeaders[tempList.size()]);
        return poHeadersArray;
    }
}
