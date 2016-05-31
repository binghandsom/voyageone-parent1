package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.system.dictionary.CmsDictionaryIndexBean;
import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsMtPlatformDictModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jonas, 1/19/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class CmsMtPlatformDictDaoExt extends ServiceBaseDao {

    public List<CmsMtPlatformDictModel> selectByChannel(CmsDictionaryIndexBean params) {
        return selectList("cms_mt_dict_selectByCondition", params);
    }

    public int selectAllCount(CmsDictionaryIndexBean params) {
        return selectOne("cms_mt_dict_selectAllCount", params);
    }

    public int insertDict(CmsMtPlatformDictModel cmsMtPlatformDictModel) {
        return insert("cms_mt_dict_insertDict", cmsMtPlatformDictModel);
    }

//    public int insertDictLog(CmsMtPlatFormDictModel cmsMtPlatFormDictModel) {
//        return insert("cms_mt_dict_insertDict_log", cmsMtPlatFormDictModel);
//    }

    public int updateDict(CmsMtPlatformDictModel cmsMtPlatformDictModel) {
        return update("cms_mt_dict_updateDict", cmsMtPlatformDictModel);
    }

    public int deleteDict(CmsMtPlatformDictModel cmsMtPlatformDictModel) {
        return delete("cms_mt_dict_deleteDict", cmsMtPlatformDictModel);
    }

    public List<CmsMtPlatformDictModel> selectByName(CmsMtPlatformDictModel cmsMtPlatformDictModel) {
        return selectList("cms_mt_dict_selectByName", cmsMtPlatformDictModel);
    }

    public CmsMtPlatformDictModel selectById(CmsMtPlatformDictModel cmsMtPlatformDictModel) {
        return selectOne("cms_mt_dict_selectById", cmsMtPlatformDictModel);
    }

    public List<CmsMtPlatformDictModel> selectSimpleDict(String channel_id) {
        return selectList("cms_mt_dict_selectSimpleDict", parameters("channel_id", channel_id));
    }

    /**
     * 根据channel_id,cart_id获取对应的字典对象列表
     *
     * @param orderChannelId String 渠道id
     * @param cartId         int            平台id
     * @return List<CmsMtDictModel> 字典对象列表
     */
    public List<CmsMtPlatformDictModel> selectByChannelCartId(String orderChannelId, int cartId) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("orderChannelId", orderChannelId);
        dataMap.put("cartId", cartId);
        return selectList("cms_mt_dict_selectByChannelCartId", dataMap);
    }
}
