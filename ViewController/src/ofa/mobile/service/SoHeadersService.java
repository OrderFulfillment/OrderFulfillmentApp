package ofa.mobile.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ofa.mobile.pojo.SoHeaders;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class SoHeadersService {
    private static List<SoHeaders> soHeadersList;

    public SoHeadersService() {
        super();
    }

    private void invokeService() {
        soHeadersList = new ArrayList<SoHeaders>();
        ServiceManager serviceManager = new ServiceManager();
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

        String soOrRct = AdfmfJavaUtilities.getELValue("#{pageFlowScope.soOrRct}").toString();
        Integer partyId = Integer.parseInt(AdfmfJavaUtilities.getELValue("#{pageFlowScope.customerId}").toString());
        String ageorCount = AdfmfJavaUtilities.getELValue("#{pageFlowScope.ageOrCount}").toString();
        String jsonArrayAsString =
            serviceManager.invokeREAD(RestURIs.getSoHeadersURI(status, partyId, soOrRct, ageorCount, 0, 200));
        try {
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PX_HEADER_TYPE");
            JSONArray nodeArray = parentnode.getJSONArray("PX_HEADER_TYPE_ITEM");
            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                SoHeaders soheaders = new SoHeaders(temp);
                soHeadersList.add(soheaders);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public SoHeaders[] getSoHeaders() {
        SoHeaders[] soHeadersArray = null;
        invokeService();
        Iterator<SoHeaders> itr = soHeadersList.iterator();
        List<SoHeaders> tempList = new ArrayList<SoHeaders>();
        while (itr.hasNext()) {
            SoHeaders headers = itr.next();
            tempList.add(headers);
        }
        soHeadersArray = tempList.toArray(new SoHeaders[0]);
        return soHeadersArray;
    }
}
