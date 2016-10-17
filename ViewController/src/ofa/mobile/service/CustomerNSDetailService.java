package ofa.mobile.service;

import java.util.ArrayList;
import java.util.List;

import ofa.mobile.pojo.CustomerNSDetail;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class CustomerNSDetailService {
    private static List<CustomerNSDetail> customerDetailList;

    public CustomerNSDetailService() {
        super();
    }

    public CustomerNSDetail[] getCustomerNSDetail() {
        CustomerNSDetail[] customerDetails = null;
        invokeService();
        customerDetails = customerDetailList.toArray(new CustomerNSDetail[0]);
        return customerDetails;
    }

    private void invokeService() {
        customerDetailList = new ArrayList<CustomerNSDetail>();
        String fromCustomerPage = null;
        try {
            fromCustomerPage = AdfmfJavaUtilities.getELValue("#{pageFlowScope.fromCustomerPage}").toString();
        } catch (Exception ex) {
            return;
        }

        if (!fromCustomerPage.equals("NS"))
            return;
        ServiceManager serviceManager = new ServiceManager();
        String customerId = AdfmfJavaUtilities.getELValue("#{pageFlowScope.customerId}").toString();
        String jsonArrayAsString = serviceManager.invokeREAD(RestURIs.getCustomerNSDetails(customerId));
        try {
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PC_CUST_DET_DETAILS");
            JSONArray nodeArray = parentnode.getJSONArray("PC_CUST_DET_DETAILS_ITEM");
            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                CustomerNSDetail customerDetail = new CustomerNSDetail(temp);
                customerDetailList.add(customerDetail);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }
}
