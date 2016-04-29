package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.system.dictionary.CmsDictionaryIndexBean;
import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsMtPlatFormDictModel;
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

    public List<CmsMtPlatFormDictModel> selectByChannel(CmsDictionaryIndexBean params) {
        return selectList("cms_mt_dict_selectByCondition", params);
    }

    public int selectAllCount(CmsDictionaryIndexBean params) {
        return selectOne("cms_mt_dict_selectAllCount", params);
    }

    public int insertDict(CmsMtPlatFormDictModel cmsMtPlatFormDictModel) {
        return insert("cms_mt_dict_insertDict", cmsMtPlatFormDictModel);
    }

//    public int insertDictLog(CmsMtPlatFormDictModel cmsMtPlatFormDictModel) {
//        return insert("cms_mt_dict_insertDict_log", cmsMtPlatFormDictModel);
//    }

    public int updateDict(CmsMtPlatFormDictModel cmsMtPlatFormDictModel) {
        return update("cms_mt_dict_updateDict", cmsMtPlatFormDictModel);
    }

    public int deleteDict(CmsMtPlatFormDictModel cmsMtPlatFormDictModel) {
        return delete("cms_mt_dict_deleteDict", cmsMtPlatFormDictModel);
    }

    public List<CmsMtPlatFormDictModel> selectByName(CmsMtPlatFormDictModel cmsMtPlatFormDictModel) {
        return selectList("cms_mt_dict_selectByName", cmsMtPlatFormDictModel);
    }

    public CmsMtPlatFormDictModel selectById(CmsMtPlatFormDictModel cmsMtPlatFormDictModel) {
        return selectOne("cms_mt_dict_selectById", cmsMtPlatFormDictModel);
    }

    public List<CmsMtPlatFormDictModel> selectSimpleDict(String channel_id) {
        return selectList("cms_mt_dict_selectSimpleDict", parameters("channel_id", channel_id));
    }

    /**
     * 根据channel_id,cart_id获取对应的字典对象列表
     *
     * @param orderChannelId String 渠道id
     * @param cartId         int            平台id
     * @return List<CmsMtDictModel> 字典对象列表
     */
    public List<CmsMtPlatFormDictModel> selectByChannelCartId(String orderChannelId, int cartId) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("order_channel_id", orderChannelId);
        dataMap.put("cartId", cartId);
        return selectOne("cms_mt_dict_selectByChannelCartId", dataMap);
    }
}
