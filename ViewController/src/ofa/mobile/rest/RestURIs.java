package ofa.mobile.rest;

import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;


public class RestURIs {
    public RestURIs() {
        super();
    }

    public static String getSoRctCountURI() {
        return "/sorctdetl1/v3/getl1count" + "/" + GetWarehouse();
    }

    public static String getSoRctDetailsL2URI(String status, String soOrRct, int minAging, int maxAging) {
        return "/sorctl2/v3/getsorctl2/" + status + "/" + soOrRct + "/" + minAging + "/" + maxAging + "/" +
               GetWarehouse();
    }

    public static String getSoHeadersURI(String status, int partyId, String soOrRct, String agingOrCount,
                                         int minAgingValue, int maxAgingValue) {
        return "/sorctl3/v3/getsorctl3/" + status + "/" + partyId + "/" + soOrRct + "/" + agingOrCount + "/" +
               minAgingValue + "/" + maxAgingValue + "/" + GetWarehouse();
    }

    public static String getSoLinesURI(String headerId, String completeStatus, String soOrRct, String agingCount,
                                       int minAging, int maxAging) {
        return "/sorctl4/v3/getsorctl4/" + headerId + "/" + completeStatus + "/" + soOrRct + "/" + agingCount + "/" +
               minAging + "/" + maxAging + "/" + GetWarehouse();
    }

    /*
     * Search Page
     */
    public static String getSearch(String itemNumber, String orderNumber, String receiptNumber) {
        return "/search/screen/v1/getdet/" + itemNumber + "/" + orderNumber + "/" + receiptNumber + "/" +
               GetWarehouse();
    }

    /*
     * Customer Section
     */
    public static String getCustomerRTS(String date) {

        return "/customer/rts/v2/getcount/" + date + "/" + GetWarehouse();
    }

    public static String getCustomerRTSDetails(String customerId) {
        return "/customer/rts/det/v2/getdet/" + customerId + "/" + GetWarehouse();
    }

    public static String getCustomerNS(String date) {
        return "/cust/count/ns/v2/getcount/" + date + "/" + GetWarehouse();
    }

    public static String getCustomerNSDetails(String customerId) {
        return "/customer/ns/det/v2/getdet/" + customerId + "/" + GetWarehouse();
    }

    /*
     * Carrier Section
     */
    public static String getCarrierRTS(String date) {
        return "/carrier/rts/v2/getcount/" + date + "/" + GetWarehouse();
    }

    public static String getCarrierRTSDetails(String carrier) {
        return "/carrier/det/rts/v2/getdet/" + carrier + "/" + GetWarehouse();
    }

    public static String getCarrierNS(String date) {
        return "/carrier/count/ns/v2/getcount/" + date + "/" + GetWarehouse();
    }

    public static String getCarrierNSDetails(String carrier) {
        return "/carrier/ns/v2/getDet/" + carrier + "/" + GetWarehouse();
    }

    /*
     * Outbound Shipments
     */
    public static String getOutboundShipment(String ssd) {
        return "/getoutshipmenthdr/v2/getdet/" + ssd + "/" + GetWarehouse();
    }

    public static String getOutboundShipmentLines(String headerId) {
        return "/getoutshipmentlines/v2/getdet/" + headerId + "/" + GetWarehouse();
    }

    /*
     * Inbound Shipments
     */
    public static String getInboundShipment(String ssd) {
        return "/getinshipmenthdr/v2/getdet/" + ssd + "/" + GetWarehouse();
    }

    public static String getInboundShipmentLines(String headerId, String identifier) {
        return "/getinshipmentlines/v2/getdet/" + headerId + "/" + identifier + "/" + GetWarehouse();
    }

    /*
     * Daily Progress Tracker
     */
    public static String getDailyProgress() {
        return "/getdailyprogresstracker/v2/getcount" + "/" + GetWarehouse();
    }

    public static String getDailyProgressHdr(String shipmentStatus) {
        return "/getshipments/v2/getdet/" + shipmentStatus + "/" + GetWarehouse();
    }

    public static String getDailyProgressLines(String shipmentLineStatus) {
        return "/getshipmentlines/v2/getdet" + shipmentLineStatus + "/" + GetWarehouse();
    }

    /*
     * Locations
     */
    public static String getLocation() {
        return "/getlocations/v1/getdetails";
    }

    /*
     * Warehouses
     */
    public static String getWarehouse(String location) {
        String urlSafe;
        try {
            urlSafe = "/getwarehouses/v1/getdetails/" + URLEncoder.encode(location, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return urlSafe;
    }

    private static String GetWarehouse() {
        //        Object whse = AdfmfJavaUtilities.getELValue("#{applicationScope.ResetDataBean.warehouse}");
        //        if (whse == null)
        //            return null;
        //        else
        //            return String.valueOf(whse);
        return "M1";
    }
}
