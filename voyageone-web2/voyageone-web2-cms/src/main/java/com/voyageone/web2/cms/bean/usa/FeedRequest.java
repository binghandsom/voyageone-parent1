package com.voyageone.web2.cms.bean.usa;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

/**
 * USA CMS Feed相关参数
 *
 * @Author rex.wu
 * @Create 2017-07-05 17:26
 */
public class FeedRequest {

    private String code;

    // MongoDB中的FeedModel
    private CmsBtFeedInfoModel feed;
    // Save or Submit or Approve 操作标识
    private Integer flag;



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
}
