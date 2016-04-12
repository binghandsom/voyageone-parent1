package com.voyageone.components.sears.bean;

/**
 *
 * Created by sky on 20150729.
 */
public class GetInventoryParamBean {
    String postAction;
    String nameSpace;
    String postUrl;
    String accountId;
    String developerKey;
    String password;
    int nPageSize;
    int nPageIndex;
    String dateTimeSince;
    int updateCount;
    String storeArea;

    public String getPostAction() {
        return postAction;
    }

    public void setPostAction(String postAction) {
        this.postAction = postAction;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getDeveloperKey() {
        return developerKey;
    }

    public void setDeveloperKey(String developerKey) {
        this.developerKey = developerKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getnPageSize() {
        return nPageSize;
    }

    public void setnPageSize(int nPageSize) {
        this.nPageSize = nPageSize;
    }

    public int getnPageIndex() {
        return nPageIndex;
    }

    public void setnPageIndex(int nPageIndex) {
        this.nPageIndex = nPageIndex;
    }

    public String getDateTimeSince() {
        return dateTimeSince;
    }

    public void setDateTimeSince(String dateTimeSince) {
        this.dateTimeSince = dateTimeSince;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    public String getStoreArea() {
        return storeArea;
    }

    public void setStoreArea(String storeArea) {
        this.storeArea = storeArea;
    }
}
