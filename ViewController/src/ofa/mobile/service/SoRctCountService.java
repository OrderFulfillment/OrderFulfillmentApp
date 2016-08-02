package ofa.mobile.service;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ofa.mobile.pojo.SoRctCount;

import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class SoRctCountService {
    private static List<SoRctCount> soRctCountList;

    public SoRctCountService() {
        super();
        invokeService();
    }

    private void invokeService() {
        soRctCountList = new ArrayList<SoRctCount>();
        ServiceManager serviceManager = new ServiceManager();
        String jsonArrayAsString = serviceManager.invokeREAD(RestURIs.getSoRctCountURI());
        try {
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PX_COUNTS");
            JSONArray nodeArray = parentnode.getJSONArray("PX_COUNTS_ITEM");
            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                SoRctCount soRctCount = new SoRctCount(temp);
                soRctCountList.add(soRctCount);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public SoRctCount[] getSoCount() {
        SoRctCount[] soCountArray = null;
        if (soRctCountList.isEmpty()) {
            invokeService();
        }
        Iterator<SoRctCount> itr = soRctCountList.iterator();
        List<SoRctCount> tempList = new ArrayList<SoRctCount>();
        while (itr.hasNext()) {
            SoRctCount soCount = itr.next();
            if (soCount.getCountType().equals("SO"))
                tempList.add(soCount);
        }
        soCountArray = tempList.toArray(new SoRctCount[tempList.size()]);
        return soCountArray;
    }

    public SoRctCount[] getRcptCount() {
        SoRctCount[] rctCountArray = null;
        if (soRctCountList.isEmpty()) {
            invokeService();
        }
        Iterator<SoRctCount> itr = soRctCountList.iterator();
        List<SoRctCount> tempList = new ArrayList<SoRctCount>();
        while (itr.hasNext()) {
            SoRctCount rctCount = itr.next();
            if (rctCount.getCountType().equals("RPT"))
                tempList.add(rctCount);
        }
        rctCountArray = tempList.toArray(new SoRctCount[tempList.size()]);
        return rctCountArray;
    }
}
