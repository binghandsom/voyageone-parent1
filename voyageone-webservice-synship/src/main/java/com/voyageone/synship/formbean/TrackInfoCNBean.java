package com.voyageone.synship.formbean;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created by dell on 2015/7/23.
 */
public class TrackInfoCNBean {

    private String resultcount;

    // 订单号
    private String cwb;
    // 操作时间
    private String trackdatetime;
    // 站点
    private String branchname;
    // 跟踪信息
    private String trackevent;
    // 订单状态
    private String podresultname;

    public String getCwb() {
        return cwb;
    }

    public void setCwb(String cwb) {
        this.cwb = cwb;
    }

    public String getTrackdatetime() {
        return trackdatetime;
    }

    public void setTrackdatetime(String trackdatetime) {
        this.trackdatetime = trackdatetime;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getTrackevent() {
        return trackevent;
    }

    public void setTrackevent(String trackevent) {
        this.trackevent = trackevent;
    }

    public String getPodresultname() {
        return podresultname;
    }

    public void setPodresultname(String podresultname) {
        this.podresultname = podresultname;
    }

    @XmlAttribute
    public String getResultcount() {
        return resultcount;
    }

    public void setResultcount(String resultcount) {
        this.resultcount = resultcount;
    }
}
