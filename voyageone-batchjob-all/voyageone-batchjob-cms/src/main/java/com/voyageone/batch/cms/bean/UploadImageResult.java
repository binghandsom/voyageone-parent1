package com.voyageone.batch.cms.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 15-6-8.
 */
public class UploadImageResult {
    private boolean uploadSuccess;
    private String failCause;
    private boolean nextProcess;
    private Map<String, String> urlMap;

    @Override
    public String toString() {
        String beanString = "urlMap:";
        for (String srcUrl : urlMap.keySet())
        {
            beanString += "[" + srcUrl + " ----> " + urlMap.get(srcUrl) + "]\n";
        }

        return beanString;
    }

    public boolean isUploadSuccess() {
        return uploadSuccess;
    }

    public void setUploadSuccess(boolean uploadSuccess) {
        this.uploadSuccess = uploadSuccess;
    }

    public String getFailCause() {
        return failCause;
    }

    public void setFailCause(String failCause) {
        this.failCause = failCause;
    }

    public boolean isNextProcess() {
        return nextProcess;
    }

    public void setNextProcess(boolean nextProcess) {
        this.nextProcess = nextProcess;
    }

    public UploadImageResult() {
        this.urlMap = new HashMap<>();
    }

    public Map<String, String> getUrlMap() {
        return urlMap;
    }

    public void setUrlMap(Map<String, String> urlMap) {
        this.urlMap = urlMap;
    }

    public void add(String srcUrl, String destUrl)
    {
        urlMap.put(srcUrl, destUrl);
    }

    public String getDestUrl(String srcUrl)
    {
        return urlMap.get(srcUrl);
    }
}
