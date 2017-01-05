package ofa.mobile.service;

import java.util.ArrayList;
import java.util.List;

import ofa.mobile.pojo.Warehouse;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class WarehouseService {
    private static List<Warehouse> warehouseList;

    public WarehouseService() {
        super();
    }

    public Warehouse[] getWarehouse() {
        Warehouse[] warehouses = null;
        invokeService();
        warehouses = warehouseList.toArray(new Warehouse[0]);
        return warehouses;
    }

    private void invokeService() {
        warehouseList = new ArrayList<Warehouse>();
        ServiceManager serviceManager = new ServiceManager();
        String location;
        try {
            location = AdfmfJavaUtilities.getELValue("#{applicationScope.locationId}").toString();
        } catch (Exception ex) {
            return;
        }
        String jsonArrayAsString = serviceManager.invokeREAD(RestURIs.getWarehouse(location));
        try {
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PC_OUT_SETTINGS_WAREHOUSE");
            JSONArray nodeArray = parentnode.getJSONArray("PC_OUT_SETTINGS_WAREHOUSE_ITEM");
            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                Warehouse warehouse = new Warehouse(temp);
                warehouseList.add(warehouse);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }
}