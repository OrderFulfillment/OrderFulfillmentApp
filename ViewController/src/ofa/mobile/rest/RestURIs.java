package ofa.mobile.rest;

public class RestURIs {
    public RestURIs() {
        super();
    }

    public static String getSoRctCountURI() {
        return "/L1/v1/getsorctcount";
    }

    public static String getSoRctDetailsL2URI(String status, String soOrRct, String countOrAging) {
        return "/L2/v1/getsorctl2/" + status + "/" + soOrRct + "/" + countOrAging;
    }
}
