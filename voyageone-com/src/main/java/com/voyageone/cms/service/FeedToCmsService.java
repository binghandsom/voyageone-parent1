package com.voyageone.cms.service;

import com.jayway.jsonpath.JsonPath;
import com.voyageone.cms.service.bean.FeedCategoryBean;
import com.voyageone.cms.service.bean.FeedCategoryChildBean;
import com.voyageone.cms.service.dao.FeedToCmsDao;
import com.voyageone.common.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/11/26.
 */
@Service
public class FeedToCmsService {
    @Autowired
    FeedToCmsDao feedToCmsDao;

    /**
     * 获取feed类目
     *
     * @param channelId
     * @return
     */
    public List<FeedCategoryChildBean> getFeedCategory(String channelId) {
        return (List<FeedCategoryChildBean>) feedToCmsDao.getFeedCategory(channelId).getCategoryTree();
    }

    /**
     * 设定feed类目
     *
     * @param channelId
     * @param tree
     */
    public void setFeedCategory(String channelId, List<FeedCategoryChildBean> tree) {
        feedToCmsDao.setFeedCategory(channelId, tree);
    }

    /**
     * 根据category从tree中找到节点
     * @param tree
     * @param cat
     * @return
     */
    public FeedCategoryChildBean findCategory(List<FeedCategoryChildBean> tree, String cat) {
        Object jsonObj = JsonPath.parse(JsonUtil.getJsonString(tree)).json();
        System.out.println(jsonObj.toString());
        List<FeedCategoryChildBean> child = JsonPath.read(jsonObj, "$..child[?(@.category == '" + cat + "')]");
        return child == null ? null : child.get(0);
    }
}
