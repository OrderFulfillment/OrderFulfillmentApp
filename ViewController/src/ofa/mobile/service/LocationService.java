package ofa.mobile.service;

import java.util.ArrayList;
import java.util.List;

import ofa.mobile.pojo.Location;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class LocationService {
    private static List<Location> locationList;

    public LocationService() {
        super();
    }

    public Location[] getLocation() {
        Location[] locations = null;
        Object backFrom = null;
        try {
            backFrom = AdfmfJavaUtilities.getELValue("#{pageFlowScope.backFrom}");
            if (backFrom == null || !backFrom.equals("backFromWarehouse")) {
                invokeService();
            }
        } catch (Exception ex) {
            System.out.println("Exception caught #{pageFlowScope.backFrom}");
        }
        locations = locationList.toArray(new Location[0]);
        return locations;
    }

    private void invokeService() {
        locationList = new ArrayList<Location>();
        ServiceManager serviceManager = new ServiceManager();
        String jsonArrayAsString = serviceManager.invokeREAD(RestURIs.getLocation());
        try {
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PC_OUT_SETTINGS_LOCS");
            JSONArray nodeArray = parentnode.getJSONArray("PC_OUT_SETTINGS_LOCS_ITEM");
            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                Location location = new Location(temp);
                locationList.add(location);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }
}
