package com.voyageone.components.channeladvisor.bean.inventory;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

/**
 *
 * Created by sky on 20150729.
 */
public class GetInventoryParamBean {
    String postAction;
    String nameSpace;
    String postUrl;
    String postItemAction;
    String accountId;
    String developerKey;
    String password;
    String labelName;
    String DateRangeField;
    XMLGregorianCalendar dateTimeStart;
    XMLGregorianCalendar dateTimeEnd;
    int nPageSize;
    int nPageIndex;
    String storeArea;
    List<String> skuList;
    String centerCode;

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

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getDateRangeField() {
        return DateRangeField;
    }

    public void setDateRangeField(String dateRangeField) {
        DateRangeField = dateRangeField;
    }

    public XMLGregorianCalendar getDateTimeStart() {
        return dateTimeStart;
    }

    public void setDateTimeStart(XMLGregorianCalendar dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public XMLGregorianCalendar getDateTimeEnd() {
        return dateTimeEnd;
    }

    public void setDateTimeEnd(XMLGregorianCalendar dateTimeEnd) {
        this.dateTimeEnd = dateTimeEnd;
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

    public String getStoreArea() {
        return storeArea;
    }

    public void setStoreArea(String storeArea) {
        this.storeArea = storeArea;
    }

    public List<String> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<String> skuList) {
        this.skuList = skuList;
    }

    public String getPostItemAction() {
        return postItemAction;
    }

    public void setPostItemAction(String postItemAction) {
        this.postItemAction = postItemAction;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }
}
