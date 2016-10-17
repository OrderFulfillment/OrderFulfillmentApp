package ofa.mobile.service;

import java.util.ArrayList;
import java.util.List;

import ofa.mobile.pojo.ProgressTracker;
import ofa.mobile.rest.RestURIs;
import ofa.mobile.rest.ServiceManager;

import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class ProgressTrackerService {
    private static List<ProgressTracker> progrssTrackerList;

    public ProgressTrackerService() {
        super();
    }

    public ProgressTracker[] getProgressTracker() {
        ProgressTracker[] progressTracker = null;
        invokeService();
        progressTracker = progrssTrackerList.toArray(new ProgressTracker[0]);
        return progressTracker;
    }

    private void invokeService() {
        progrssTrackerList = new ArrayList<ProgressTracker>();
        ServiceManager serviceManager = new ServiceManager();
        String jsonArrayAsString = serviceManager.invokeREAD(RestURIs.getDailyProgress());
        ProgressTracker progressTracker;
        try {
            progressTracker = new ProgressTracker(new JSONObject(jsonArrayAsString));
            progrssTrackerList.add(progressTracker);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
