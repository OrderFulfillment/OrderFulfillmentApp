package ofa.mobile.rest;

import java.util.logging.Level;

import oracle.adfmf.dc.ws.rest.RestServiceAdapter;
import oracle.adfmf.framework.api.Model;
import oracle.adfmf.util.logging.Trace;

public class ServiceManager {
    public ServiceManager() {
        super();
    }

    //GET
    public String invokeREAD(String requestURI) {
        System.out.println("request");
        return this.invokeRestRequest(RestServiceAdapter.REQUEST_TYPE_GET, requestURI, "");
    }

    //POST
    public String invokeUPDATE(String requestURI, String payload) {

        return this.invokeRestRequest(RestServiceAdapter.REQUEST_TYPE_POST, requestURI, payload);
    }

    //PUT
    public String invokeCREATE(String requestURI, String payload) {
        System.out.println("Inside invokeCreate");
        return this.invokeRestRequest(RestServiceAdapter.REQUEST_TYPE_PUT, requestURI, payload);
    }


    //DELETE
    public String invokeDELETE(String requestURI) {
        return this.invokeRestRequest(RestServiceAdapter.REQUEST_TYPE_POST, requestURI, "");
    }

    /**
     * Method that handles the boilerplate code for obtaining and configuring a service
     * adapter instance.
     *
     * @param httpMethod GET, POST, PUT, DELETE
     * @param requestURI the URI to appends to the base REST URL. URIs are expected to start with "/"
     * @return
     */
    private String invokeRestRequest(String httpMethod, String requestURI, String payload) {
        System.out.println("Inside rest");
        String restPayload = "";

        RestServiceAdapter restServiceAdapter = Model.createRestServiceAdapter();
        restServiceAdapter.clearRequestProperties();
        //bioConnection URL :http://ussltcsnl3432.solutions.glbsnet.com:7004/
        restServiceAdapter.setConnectionName("snet");

        //set GET, POST, DELETE, PUT
        restServiceAdapter.setRequestType(httpMethod);
        restServiceAdapter.addRequestProperty("Authorization", "None ");
        restServiceAdapter.addRequestProperty("Content-Language", "en-US");
        restServiceAdapter.addRequestProperty("Content-Type", "application/json");
        restServiceAdapter.setRequestURI(requestURI);
        restServiceAdapter.setRetryLimit(0);
        String response = "";
        if (payload != null) {
            //send with empty payload
            restPayload = payload;
        }

        try {
            response = (restServiceAdapter.send(restPayload)).toString();
        } catch (Exception e) {
            //log error
            Trace.log("REST_JSON", Level.SEVERE, this.getClass(), "invokeRestRequest",
                      "Invoke of REST Resource failed for " + httpMethod + " to " + requestURI);
            System.out.println("REST_JSON" + e.getLocalizedMessage());
            Trace.log("REST_JSON", Level.SEVERE, this.getClass(), "invokeRestRequest", e.getLocalizedMessage());
        }
        return response;
    };
}

