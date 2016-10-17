package ofa.mobile.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Locale;

import ofa.mobile.pojo.CarrierRTS;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class CarrierRTSService {
    private static List<CarrierRTS> carrierList;

    public CarrierRTSService() {
        super();
    }

    public CarrierRTS[] getCarrierRTS() {
        CarrierRTS[] carriers = null;
        invokeService();
        carriers = carrierList.toArray(new CarrierRTS[0]);
        return carriers;
    }

    private void invokeService() {
        carrierList = new ArrayList<CarrierRTS>();
        ServiceManager serviceManager = new ServiceManager();
        String fromCarrierPage = null;
        try {
            fromCarrierPage = AdfmfJavaUtilities.getELValue("#{pageFlowScope.carrierPage}").toString();
        } catch (Exception e) {
            fromCarrierPage = "RTS";
        }
        if (!fromCarrierPage.equals("RTS"))
            return;

        String datefromUser = null;
        String formattedDate = null;
        try {
            datefromUser = AdfmfJavaUtilities.getELValue("#{pageFlowScope.carrierDate}").toString();
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Date date = originalFormat.parse(datefromUser);
            formattedDate = targetFormat.format(date);
        } catch (NullPointerException e) {
            return;
        } catch (ParseException e) {
            return;
        }

        try {
            String jsonArrayAsString = serviceManager.invokeREAD(RestURIs.getCarrierRTS(formattedDate));
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PC_COUNT_DETAILS");
            JSONArray nodeArray = parentnode.getJSONArray("PC_COUNT_DETAILS_ITEM");
            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                CarrierRTS carrier = new CarrierRTS(temp);
                carrierList.add(carrier);
            }
        } catch (NullPointerException e) {
            return;
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }
}
