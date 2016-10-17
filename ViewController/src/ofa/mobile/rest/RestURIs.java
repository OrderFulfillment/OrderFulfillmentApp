package ofa.mobile.rest;


public class RestURIs {
    public RestURIs() {
        super();
    }

    public static String getSoRctCountURI() {
        return "/L1/v2/getl1count";
    }

    public static String getSoRctDetailsL2URI(String status, String soOrRct, int minAging, int maxAging) {
        return "/L2/v2/getsorctl2/" + status + "/" + soOrRct + "/" + minAging + "/" + maxAging;
    }

    public static String getSoHeadersURI(String status, int partyId, String soOrRct, String agingOrCount,
                                         int minAgingValue, int maxAgingValue) {
        return "/L3/v2/getsorctl3/" + status + "/" + partyId + "/" + soOrRct + "/" + agingOrCount + "/" +
               minAgingValue + "/" + maxAgingValue;
    }

    public static String getSoLinesURI(String headerId, String completeStatus, String soOrRct, String agingCount,
                                       int minAging, int maxAging) {
        return "/L4/v1/getsorctl4/" + headerId + "/" + completeStatus + "/" + soOrRct + "/" + agingCount + "/" +
               minAging + "/" + maxAging;
    }

    /*
     * Search Page
     */
    public static String getSearch(String itemNumber, String orderNumber, String receiptNumber) {
        return "/search/screen/v1/getdet/" + itemNumber + "/" + orderNumber + "/" + receiptNumber;
    }

    /*
     * Customer Section
     */
    public static String getCustomerRTS(String date) {
        return "/customer/rts/v1/getcount/" + date;
    }

    public static String getCustomerRTSDetails(String customerId) {
        return "/customer/rts/det/v1/getdet/" + customerId;
    }

    public static String getCustomerNS(String date) {
        return "/cust/count/ns/v1/getcount/" + date;
    }

    public static String getCustomerNSDetails(String customerId) {
        return "/customer/ns/det/v1/getdet/" + customerId;
    }

    /*
     * Carrier Section
     */
    public static String getCarrierRTS(String date) {
        return "/carrier/rts/v1/getcount/" + date;
    }

    public static String getCarrierRTSDetails(String carrier) {
        return "/carrier/det/rts/v1/getdet/" + carrier;
    }

    public static String getCarrierNS(String date) {
        return "/carrier/count/ns/v1/getcount/" + date;
    }

    public static String getCarrierNSDetails(String customerId) {
        return "/carrier/ns/v1/getDet/" + customerId;
    }

    /*
     * Outbound Shipments
     */
    public static String getOutboundShipment(String ssd) {
        return "/getoutshipmenthdr/v1/getdet/" + ssd;
    }

    public static String getOutboundShipmentLines(String headerId) {
        return "/getoutshipmentlines/v1/getdet/" + headerId;
    }

    /*
     * Inbound Shipments
     */
    public static String getInboundShipment(String ssd) {
        return "/getinshipmenthdr/v1/getdet/" + ssd;
    }

    public static String getInboundShipmentLines(String headerId, String identifier) {
        return "/getinshipmentlines/v1/getdet/" + headerId + "/" + identifier;
    }

    /*
     * Daily Progress Tracker
     */
    public static String getDailyProgress() {
        return "/getdailyprogresstracker/v1/getcount";
    }

    public static String getDailyProgressHdr(String shipmentStatus) {
        return "/getshipments/v1/getdet/" + shipmentStatus;
    }

    public static String getDailyProgressLines(String shipmentLineStatus) {
        return "/getshipmentlines/v1/getdet" + shipmentLineStatus;
    }

}
