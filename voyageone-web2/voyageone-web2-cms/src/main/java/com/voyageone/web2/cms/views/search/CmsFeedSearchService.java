package com.voyageone.web2.cms.views.search;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryTreeModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.views.channel.CmsFeedCustPropService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JiangJusheng
 * @version 2.0.0, 2016/04/06
 */
@Service
public class CmsFeedSearchService extends BaseAppService {

    @Autowired
    private FeedInfoService feedInfoService;
    @Autowired
    private CmsFeedCustPropService cmsFeedCustPropService;

    // 查询产品信息时的缺省输出列
    private final String searchItems = "{'category':1,'code':1,'name':1,'model':1,'color':1,'origin':1,'brand':1,'image':1,'productType':1,'sizeType':1,'shortDescription':1,'longDescription':1,'skus':1,'attribute':1}";

    // 查询产品信息时的缺省输出列
    private final String sortItems = "{'category':1,'code':1}";

    /**
     * 获取检索页面初始化的master data数据
     * @param userInfo
     * @return
     */
    public Map<String, Object> getMasterData(UserSessionBean userInfo, CmsSessionBean cmsSession, String language) throws IOException{
        Map<String, Object> masterData = new HashMap<>();

        // 获取brand list
        masterData.put("brandList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, userInfo.getSelChannelId(), language));
        // 获取category list
        List<CmsMtFeedCategoryTreeModel> feedCatList = cmsFeedCustPropService.getCategoryList(userInfo);
        if (!feedCatList.isEmpty()) {
            feedCatList.remove(0);
        }
        List<Integer> delFlgList = new ArrayList<Integer>();
        for (int i = 0, leng = feedCatList.size(); i < leng; i ++) {
            if (feedCatList.get(i).getIsParent() == 1) {
                // 非子节点
                delFlgList.add(i);
            }
        }
        for (int leng = delFlgList.size(), i = leng - 1; i >= 0; i --) {
            feedCatList.remove(delFlgList.get(i).intValue());
        }

        masterData.put("categoryList", feedCatList);
        return masterData;
    }

    /**
     * 获取当前页的FEED信息
     * @param searchValue
     * @param userInfo
     * @return
     */
    public List<CmsBtFeedInfoModel> getFeedList(Map<String, Object> searchValue, UserSessionBean userInfo) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(getSearchQuery(searchValue));
        queryObject.setProjection(searchItems);
        queryObject.setSort(sortItems);
        int pageNum = (Integer) searchValue.get("pageNum");
        int pageSize = (Integer) searchValue.get("pageSize");
        queryObject.setSkip((pageNum - 1) * pageSize);
        queryObject.setLimit(pageSize);
        return feedInfoService.getList(userInfo.getSelChannelId(), queryObject);
    }

    /**
     * 获取当前页的product列表Cnt
     * @param searchValue
     * @param userInfo
     * @return
     */
    public long getFeedCnt(Map<String, Object> searchValue, UserSessionBean userInfo) {
        return feedInfoService.getCnt(userInfo.getSelChannelId(), getSearchQuery(searchValue));
    }

    /**
     * 返回页面端的检索条件拼装成mongo使用的条件
     * @param searchValue
     * @return
     */
    private String getSearchQuery(Map<String, Object> searchValue) {
        StringBuffer result = new StringBuffer();

        // 获取查询的价格类型
        String priceType = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("priceType"));
        if (priceType != null) {
            // 获取查询的价格区间下限
            float priceSta = NumberUtils.toFloat(org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("priceValueSta")), -1);
            // 获取查询的价格区间上限
            float priceEnd = NumberUtils.toFloat(org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("priceValueEnd")), -1);
            if (priceSta > -1 && priceEnd > -1 && priceEnd < priceSta) {
                throw new BusinessException("设置的查询价格区间不正确");
            }
            if (priceSta > -1 || priceEnd > -1) {
                result.append("\"skus." + priceType + "\":{" );
                if (priceSta > -1) {
                    result.append(MongoUtils.splicingValue("$gte", priceSta));
                }
                if (priceEnd > -1) {
                    if (priceSta > -1) {
                        result.append(",");
                    }
                    result.append(MongoUtils.splicingValue("$lte", priceEnd));
                }
                result.append("},");
            }
        }

        // 获取createdTime start
        String createTimeSta = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("createTimeSta"));
        // 获取createdTime End
        String createTimeEnd = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("createTimeEnd"));
        if (createTimeSta != null || createTimeEnd != null) {
            result.append("\"created\":{" );
            if (createTimeSta != null) {
                result.append(MongoUtils.splicingValue("$gte", createTimeSta + " 00.00.00"));
            }
            if (createTimeEnd != null) {
                if (createTimeSta != null) {
                    result.append(",");
                }
                result.append(MongoUtils.splicingValue("$lte", createTimeEnd + " 23.59.59"));
            }
            result.append("},");
        }

        // 获取updateTime start
        String updateTimeSta = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("updateTimeSta"));
        // 获取updateTime End
        String updateTimeEnd = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("updateTimeEnd"));
        if (updateTimeSta != null || updateTimeEnd != null) {
            result.append("\"modified\":{" );
            if (updateTimeSta != null) {
                result.append(MongoUtils.splicingValue("$gte", updateTimeSta + " 00.00.00.000"));
            }
            if (updateTimeEnd != null) {
                if (updateTimeSta != null) {
                    result.append(",");
                }
                result.append(MongoUtils.splicingValue("$lte", updateTimeEnd + " 23.59.59.999"));
            }
            result.append("},");
        }

        // 获取category
        String category = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("category"));
        if (category != null) {
            result.append(MongoUtils.splicingValue("category", category));
            result.append(",");
        }

        // 获取product name
        String prodName = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("name"));
        if (prodName != null) {
            result.append(MongoUtils.splicingValue("name", prodName));
            result.append(",");
        }

        // 获取输入的模糊查询字符串,用于检索code,name,model,short_description,long_description
        List<String> strList = (List<String>) searchValue.get("fuzzyList");
        if (strList != null && strList.size() > 0) {
            List<String> orSearch = new ArrayList<>();
            String[] fuzzyArr = new String[strList.size()];
            fuzzyArr = strList.toArray(fuzzyArr);
            orSearch.add(MongoUtils.splicingValue("code", fuzzyArr));
            orSearch.add(MongoUtils.splicingValue("name", fuzzyArr));
            orSearch.add(MongoUtils.splicingValue("model", fuzzyArr));

            if (strList.size() == 1) {
                orSearch.add(MongoUtils.splicingValue("short_description", strList.get(0), "$regex"));
                orSearch.add(MongoUtils.splicingValue("long_description", strList.get(0), "$regex"));
            }
            result.append(MongoUtils.splicingValue("", orSearch.toArray(), "$or"));
            result.append(",");
        }

        // 获取brand
        String brand = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("brand"));
        if (brand != null) {
            result.append(MongoUtils.splicingValue("brand", brand));
            result.append(",");
        }

        // 获取color
        String color = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("color"));
        if (color != null) {
            result.append(MongoUtils.splicingValue("color", color));
            result.append(",");
        }

        // 获取product type
        String productType = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("productType"));
        if (productType != null) {
            result.append(MongoUtils.splicingValue("productType", productType));
            result.append(",");
        }

        // 获取size type
        String sizeType = org.apache.commons.lang3.StringUtils.trimToNull((String) searchValue.get("sizeType"));
        if (sizeType != null) {
            result.append(MongoUtils.splicingValue("sizeType",sizeType));
            result.append(",");
        }

        if (!StringUtils.isEmpty(result.toString())) {
            return "{" + result.toString().substring(0, result.toString().length() - 1) + "}";
        }
        else {
            return "";
        }
    }

}
