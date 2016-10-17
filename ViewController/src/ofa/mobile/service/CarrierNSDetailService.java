package ofa.mobile.service;

import java.util.ArrayList;
import java.util.List;

import ofa.mobile.pojo.CarrierNSDetail;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class CarrierNSDetailService {
    private static List<CarrierNSDetail> carrierDetailList;

    public CarrierNSDetailService() {
        super();
    }

    public CarrierNSDetail[] getCarrierNSDetail() {
        CarrierNSDetail[] carrierDetails = null;
        invokeService();
        carrierDetails = carrierDetailList.toArray(new CarrierNSDetail[0]);
        return carrierDetails;
    }

    private void invokeService() {
        carrierDetailList = new ArrayList<CarrierNSDetail>();
        ServiceManager serviceManager = new ServiceManager();
        String fromCarrierPage = null;
        try {
            fromCarrierPage = AdfmfJavaUtilities.getELValue("#{pageFlowScope.fromCarrierPage}").toString();
        } catch (Exception ex) {
            return;
        }
        if (!fromCarrierPage.equals("NS"))
            return;
        String carrier = AdfmfJavaUtilities.getELValue("#{pageFlowScope.carrier}").toString();

        String jsonArrayAsString = serviceManager.invokeREAD(RestURIs.getCarrierNSDetails(carrier));
        try {
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PC_CARR_DET_DETAILS");
            JSONArray nodeArray = parentnode.getJSONArray("PC_CARR_DET_DETAILS_ITEM");
            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                CarrierNSDetail carrierDetail = new CarrierNSDetail(temp);
                carrierDetailList.add(carrierDetail);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }
}
