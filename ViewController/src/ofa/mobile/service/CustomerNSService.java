package ofa.mobile.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.util.Locale;

import ofa.mobile.pojo.CustomerNS;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class CustomerNSService {
    private static List<CustomerNS> customerList;

    public CustomerNSService() {
        super();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -10);
        AdfmfJavaUtilities.setELValue("#{pageFlowScope.customerDate}", cal.getTime());
    }

    public CustomerNS[] getCustomerNS() {
        CustomerNS[] customers = null;
        invokeService();
        customers = customerList.toArray(new CustomerNS[0]);
        return customers;
    }

    private void invokeService() {
        customerList = new ArrayList<CustomerNS>();
        ServiceManager serviceManager = new ServiceManager();
        String fromCustomerPage = null;
        try {
            fromCustomerPage = AdfmfJavaUtilities.getELValue("#{pageFlowScope.customerPage}").toString();
        } catch (Exception e) {
            return;
        }
        if (!fromCustomerPage.equals("NS"))
            return;
        String datefromUser = null;
        String formattedDate = null;
        try {
            datefromUser = AdfmfJavaUtilities.getELValue("#{pageFlowScope.customerDate}").toString();
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Date date = originalFormat.parse(datefromUser);
            formattedDate = targetFormat.format(date);
        } catch (NullPointerException e) {
            return;
        } catch (ParseException e) {
            return;
        }

        String jsonArrayAsString = serviceManager.invokeREAD(RestURIs.getCustomerNS(formattedDate));
        try {
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PC_COUNT_DETAILS");
            JSONArray nodeArray = parentnode.getJSONArray("PC_COUNT_DETAILS_ITEM");
            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                CustomerNS customer = new CustomerNS(temp);
                customerList.add(customer);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }
}
