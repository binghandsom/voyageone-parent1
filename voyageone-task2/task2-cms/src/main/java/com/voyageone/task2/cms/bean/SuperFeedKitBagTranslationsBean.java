package com.voyageone.task2.cms.bean;

/**
 * Created by gjl on 2017/2/16.
 */
public class SuperFeedKitBagTranslationsBean extends SuperFeedBean  {

    private String productid;

    private String sitename;

    private String siteid;

    private String territory;

    private String title;

    private String detaileddescription;

    private String gender;

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid == null ? null : productid.trim();
    }

    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename == null ? null : sitename.trim();
    }

    public String getSiteid() {
        return siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid == null ? null : siteid.trim();
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory == null ? null : territory.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDetaileddescription() {
        return detaileddescription;
    }

    public void setDetaileddescription(String detaileddescription) {
        this.detaileddescription = detaileddescription == null ? null : detaileddescription.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

}
