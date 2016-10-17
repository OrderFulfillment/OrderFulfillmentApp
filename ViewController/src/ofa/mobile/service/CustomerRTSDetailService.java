package ofa.mobile.service;

import java.util.ArrayList;
import java.util.List;

import ofa.mobile.pojo.CustomerRTSDetail;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class CustomerRTSDetailService {
    private static List<CustomerRTSDetail> customerDetailList;

    public CustomerRTSDetailService() {
        super();
    }

    public CustomerRTSDetail[] getCustomerRTSDetail() {
        CustomerRTSDetail[] customerDetails = null;
        invokeService();
        customerDetails = customerDetailList.toArray(new CustomerRTSDetail[0]);
        return customerDetails;
    }

    private void invokeService() {
        customerDetailList = new ArrayList<CustomerRTSDetail>();
        String fromCustomerPage = null;
        try {
            fromCustomerPage = AdfmfJavaUtilities.getELValue("#{pageFlowScope.fromCustomerPage}").toString();

        } catch (Exception ex) {
            return;
        }

        if (!fromCustomerPage.equals("RTS"))
            return;
        ServiceManager serviceManager = new ServiceManager();
        String customerId = AdfmfJavaUtilities.getELValue("#{pageFlowScope.customerId}").toString();

        String jsonArrayAsString = serviceManager.invokeREAD(RestURIs.getCustomerRTSDetails(customerId));
        try {
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PC_CUST_DET_DETAILS");
            JSONArray nodeArray = parentnode.getJSONArray("PC_CUST_DET_DETAILS_ITEM");
            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                CustomerRTSDetail customerDetail = new CustomerRTSDetail(temp);
                customerDetailList.add(customerDetail);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }
}
