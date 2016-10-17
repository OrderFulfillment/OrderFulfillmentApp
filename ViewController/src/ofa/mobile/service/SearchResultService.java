package ofa.mobile.service;

import java.util.ArrayList;
import java.util.List;

import ofa.mobile.pojo.SearchResult;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class SearchResultService {
    private static List<SearchResult> searchResultList;

    public SearchResultService() {
        super();
    }

    public SearchResult[] getSearchResult() {
        SearchResult[] searchResults = null;
        invokeService();
        searchResults = searchResultList.toArray(new SearchResult[0]);
        return searchResults;
    }

    private void invokeService() {
        searchResultList = new ArrayList<SearchResult>();
        ServiceManager serviceManager = new ServiceManager();

        String itemNumber = AdfmfJavaUtilities.getELValue("#{pageFlowScope.itemNumber}").toString();
        String orderNumber = AdfmfJavaUtilities.getELValue("#{pageFlowScope.orderNumber}").toString();
        String receiptNumber = AdfmfJavaUtilities.getELValue("#{pageFlowScope.receiptNumber}").toString();

        String jsonArrayAsString =
            serviceManager.invokeREAD(RestURIs.getSearch(itemNumber, orderNumber, receiptNumber));
        try {
            JSONObject jsonObject = new JSONObject(jsonArrayAsString);
            JSONObject parentnode = jsonObject.getJSONObject("PC_SEARCH_DETAILS");
            JSONArray nodeArray = parentnode.getJSONArray("PC_SEARCH_DETAILS_ITEM");

            int size = nodeArray.length();
            for (int i = 0; i < size; i++) {
                JSONObject temp = nodeArray.getJSONObject(i);
                SearchResult searchResult = new SearchResult(temp);
                searchResultList.add(searchResult);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }
}
