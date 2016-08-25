package com.voyageone.web2.vms.views.feed;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
/**
 * VmsFeedSearchService
 * Created on 2016/7/11.
 * @author jeff.duan
 * @version 1.0
 */
@Service
public class VmsFeedSearchService extends BaseAppService {

    @Autowired
    private FeedInfoService feedInfoService;

    // 查询产品信息时的缺省输出列
    private final String searchItems = "{'category':1,'code':1,'name':1,'image':1,'skus':1}";

    // 查询产品信息时的缺省排序条件
    private final String sortItems = "{'code':1}";

    /**
     * 获取当前页的FEED信息
     * @param searchValue
     * @param userInfo
     * @return
     */
    public List<CmsBtFeedInfoModel> getFeedList(Map<String, Object> searchValue, UserSessionBean userInfo) {
        JongoQuery queryObject = new JongoQuery();
        queryObject.setQuery(feedInfoService.getSearchQueryForVendor(searchValue));
        // 当数据很多时，加上指定字段反而影响性能
        // queryObject.setProjection(searchItems);
        queryObject.setSort(sortItems);
        int pageNum = (Integer) searchValue.get("curr");
        int pageSize = (Integer) searchValue.get("size");
        queryObject.setSkip((pageNum - 1) * pageSize);
        queryObject.setLimit(pageSize);
        List<CmsBtFeedInfoModel> models = feedInfoService.getListForVendor(userInfo.getSelChannelId(), queryObject);
        editFeedInfo(models);
        return models;
    }

    /**
     * 获取当前页的product列表Cnt
     * @param searchValue
     * @param userInfo
     * @return
     */
    public long getFeedCnt(Map<String, Object> searchValue, UserSessionBean userInfo) {
        return feedInfoService.getCntForVendor(userInfo.getSelChannelId(), searchValue);
    }

    /**
     * 编辑FeedInfo
     * @param models 编辑的CmsBtFeedInfoModel列表
     */
    private void editFeedInfo(List<CmsBtFeedInfoModel> models) {
        for(CmsBtFeedInfoModel model : models) {
            String category = model.getCategory();
            String[] categoryArray = category.split("-");
            if (categoryArray.length > 0) {
                category = "";
                for (String categoryItem : categoryArray) {
                    // 不等于空的情况下，去掉首尾空格，并替换全角横杠为半角横杠，重新组装一下
                    if (!StringUtils.isEmpty(categoryItem)) {
                        category += categoryItem.trim().replaceAll("－", "-") + "/";
                    }
                }
                // 去掉最后一个分隔符[/]
                if (!StringUtils.isEmpty(category)) {
                    category = category.substring(0, category.length() - 1);
                }
                model.setCategory(category);
            }
        }
    }
}
