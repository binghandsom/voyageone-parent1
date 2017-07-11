package com.voyageone.web2.cms.bean.usa;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import java.util.List;
import java.util.Map;

/**
 * USA CMS Feed相关参数
 *
 * @Author rex.wu
 * @Create 2017-07-05 17:26
 */
public class FeedRequest {

    private String id;
    private String code;
    private int top;
    private String model;

    // MongoDB中的FeedModel
    private CmsBtFeedInfoModel feed;
    // Save or Submit or Approve 操作标识
    private Integer flag;

    private Boolean selAll; // 是否检索结果全量
    private List<String> codeList; // Feed Code Collection
    private Map<String, Object> searchMap; // Feed检索条件
    private Map<Integer, Integer> approveInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public CmsBtFeedInfoModel getFeed() {
        return feed;
    }

    public void setFeed(CmsBtFeedInfoModel feed) {
        this.feed = feed;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Boolean getSelAll() {
        return selAll;
    }

    public void setSelAll(Boolean selAll) {
        this.selAll = selAll;
    }

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }

    public Map<String, Object> getSearchMap() {
        return searchMap;
    }

    public void setSearchMap(Map<String, Object> searchMap) {
        this.searchMap = searchMap;
    }

    public Map<Integer, Integer> getApproveInfo() {
        return approveInfo;
    }

    public void setApproveInfo(Map<Integer, Integer> approveInfo) {
        this.approveInfo = approveInfo;
    }
}
