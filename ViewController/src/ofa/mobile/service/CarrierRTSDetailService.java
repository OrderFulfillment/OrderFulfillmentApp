package ofa.mobile.service;

import java.util.ArrayList;
import java.util.List;

import ofa.mobile.pojo.CarrierRTSDetail;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class CarrierRTSDetailService {
    private static List<CarrierRTSDetail> carrierDetailList;

    public CarrierRTSDetailService() {
        super();
    }

    public CarrierRTSDetail[] getCarrierRTSDetail() {
        CarrierRTSDetail[] carrierDetails = null;
        invokeService();
        carrierDetails = carrierDetailList.toArray(new CarrierRTSDetail[0]);
        return carrierDetails;
    }

    private void invokeService() {
        carrierDetailList = new ArrayList<CarrierRTSDetail>();
        String fromCarrierPage = null;
        try {
            fromCarrierPage = AdfmfJavaUtilities.getELValue("#{pageFlowScope.fromCarrierPage}").toString();
        } catch (Exception ex) {
            return;
        }

        if (!fromCarrierPage.equals("RTS"))
            return;

        ServiceManager serviceManager = new ServiceManager();
        String carrier = AdfmfJavaUtilities.getELValue("#{pageFlowScope.carrier}").toString();

        String jsonArrayAsString = serviceManager.invokeREAD(RestURIs.getCarrierRTSDetails(carrier));
        try {
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PC_CARR_DET_DETAILS");
            JSONArray nodeArray = parentnode.getJSONArray("PC_CARR_DET_DETAILS_ITEM");
            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                CarrierRTSDetail carrierDetail = new CarrierRTSDetail(temp);
                carrierDetailList.add(carrierDetail);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }
}
