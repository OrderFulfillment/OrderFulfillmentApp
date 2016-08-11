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
}
