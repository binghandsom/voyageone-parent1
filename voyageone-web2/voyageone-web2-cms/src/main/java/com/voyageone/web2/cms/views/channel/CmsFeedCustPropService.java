package com.voyageone.web2.cms.views.channel;

import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.service.dao.cms.mongo.CmsMtFeedCategoryTreeDao;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryTreeModelx;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.dao.CmsBtFeedCustomPropDao;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiang, 2016/2/26
 * @version 2.0.0从
 */
@Service
public class CmsFeedCustPropService extends BaseAppService {

    @Autowired
    private CmsMtFeedCategoryTreeDao cmsMtFeedCategoryTreeDao;
    @Autowired
    private CmsBtFeedCustomPropDao cmsBtFeedCustomPropDao;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private SimpleTransaction simpleTransaction;

    // 取得类目路径数据
    public List<CmsMtFeedCategoryModel> getTopCategories(UserSessionBean user) {
        CmsMtFeedCategoryTreeModelx treeModelx = cmsMtFeedCategoryTreeDao.findFeedCategoryx(user.getSelChannelId());
        return treeModelx.getCategoryTree();
    }

    // 根据类目路径查询自定义已翻译属性信息
    public List<Map<String, Object>> selectAllAttr(String channelId, String catPath) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("channelId", channelId);
        params.put("feedCatPath", catPath);
        return cmsBtFeedCustomPropDao.selectAllAttr(params);
    }

    // 根据类目路径查询自定义已翻译属性信息
    public List<Map<String, Object>> selectTransProp(String channelId, String catPath) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("channelId", channelId);
        params.put("feedCatPath", catPath);
        return cmsBtFeedCustomPropDao.selectTransProp(params);
    }

    // 根据类目路径查询自定义未翻译属性信息
    public List<Map<String, Object>> selectOrigProp(String channelId, String catPath) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("channelId", channelId);
        params.put("feedCatPath", catPath);
        return cmsBtFeedCustomPropDao.selectOrigProp(params);
    }

    // 取得全店铺共通配置属性
    public String getSameAttr(String channelId) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("channelId", channelId);
        String rslt = cmsBtFeedCustomPropDao.getSameAttr(params);
        return rslt;
    }

    // 根据类目路径查询自定义未翻译属性信息(不包含共通属性)
    public List<Object> selectCatAttr(String channelId) {
        Query query = new Query();
        Criteria criteria = Criteria.where("channelId").is(channelId);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, Object.class, "cms_mt_feed_category_tree");
        //TODO-- 这里只能使用Object对象来影射，不能使用Map.class，可能是spring mongoTemplate的问题
    }

    // 查询指定类目属性是否存在
    public boolean isAttrExist(Map<String, Object> params, String catPath, String chnId) {
        Map<String, Object> sqlPara = new HashMap<String, Object>();
        sqlPara.putAll(params);
        sqlPara.put("cat_path", catPath);
        sqlPara.put("channelId", chnId);
        return cmsBtFeedCustomPropDao.isAttrExist(sqlPara);
    }

    // 保存属性
    public void saveAttr( List<Map<String, Object>> addList,  List<Map<String, Object>> updList, String catPath, UserSessionBean userInfo) {
        simpleTransaction.openTransaction();
        try {
            if (addList.size() > 0) {
                Map<String, Object> params = new HashMap<String, Object>(4);
                params.put("channelId", userInfo.getSelChannelId());
                params.put("cat_path", catPath);
                params.put("userName", userInfo.getUserName());
                params.put("list", addList);
                int tslt = cmsBtFeedCustomPropDao.addAttr(params);
                if (tslt != addList.size()) {
                    logger.error("添加属性结果与期望不符：添加条数=" + addList.size() + " 实际更新件数=" + tslt);
                } else {
                    logger.debug("添加属性成功 实际更新件数=" + tslt);
                }
            }
            if (updList.size() > 0) {
                for (Map<String, Object> item : updList) {
                    item.put("userName", userInfo.getUserName());
                    int tslt = cmsBtFeedCustomPropDao.updateAttr(item);
                    if (tslt != 1) {
                        logger.error("修改属性结果失败，params=" + item.toString());
                    }
                }
            }
            simpleTransaction.commit();
        } catch(Exception exp) {
            logger.error("保存属性时失败", exp);
            simpleTransaction.rollback();
        }
    }

    // 查询指定属性值是否存在
    public boolean isPropValueExist(int propId, String chnId, String origValue) {
        Map<String, Object> sqlPara = new HashMap<String, Object>();
        sqlPara.put("propId", propId);
        sqlPara.put("channelId", chnId);
        sqlPara.put("origValue", origValue);
        return cmsBtFeedCustomPropDao.isPropValueExist(sqlPara);
    }

    // 查询指定属性值是否存在
    public boolean isPropValueExist(int valueId) {
        Map<String, Object> sqlPara = new HashMap<String, Object>();
        sqlPara.put("valueId", valueId);
        return cmsBtFeedCustomPropDao.isPropValueExistById(sqlPara);
    }

    // 添加属性值
    public int addPropValue(int propId, String chnId, String origValue, String transValue, String userName) {
        Map<String, Object> sqlPara = new HashMap<String, Object>();
        sqlPara.put("propId", propId);
        sqlPara.put("channelId", chnId);
        sqlPara.put("origValue", origValue);
        sqlPara.put("transValue", transValue);
        sqlPara.put("userName", userName);
        return cmsBtFeedCustomPropDao.addPropValue(sqlPara);
    }

    // 修改属性值
    public int updatePropValue(int valueId, String transValue, String userName) {
        Map<String, Object> sqlPara = new HashMap<String, Object>();
        sqlPara.put("valueId", valueId);
        sqlPara.put("transValue", transValue);
        sqlPara.put("userName", userName);
        return cmsBtFeedCustomPropDao.updatePropValue(sqlPara);
    }

    // 查询属性值
    public List<Map<String, Object>> selectPropValue(String catPath, int tSts, String propName, String propValue, String chaId) {
        Map<String, Object> sqlPara = new HashMap<String, Object>();
        sqlPara.put("feedCatPath", catPath);
        sqlPara.put("propName", propName);
        sqlPara.put("propValue", propValue);
        sqlPara.put("tSts", tSts);
        sqlPara.put("channelId", chaId);
        return cmsBtFeedCustomPropDao.selectPropValue(sqlPara);
    }
}
