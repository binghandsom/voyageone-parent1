package com.voyageone.task2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.ims.modelbean.DictWordBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 15-7-1.
 */
@Repository
public class DictWordDao extends BaseDao{
    public int addDictWord(DictWordBean dictWordBean)
    {
        return updateTemplate.insert(Constants.DAO_NAME_SPACE_CMS + "cms_insertDictWord", dictWordBean);
    }

    public List<DictWordBean> selectDictWords()
    {
        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_CMS + "cms_selectDictWords");
    }

    public DictWordBean selectDictWordByName(String orderChannelId, String name) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("order_channel_id", orderChannelId);
        dataMap.put("name", name);
        return selectOne(Constants.DAO_NAME_SPACE_CMS + "cms_selectDictWordsByName", dataMap);
    }

    /**
     * 根据channel_id获取对应的字典对象列表
     *
     * @param orderChannelId String 渠道id
     * @return List<DictWordBean> 字典对象列表
     */
    public List<DictWordBean> selectDictWordByChannel(String orderChannelId) {
        return selectOne(Constants.DAO_NAME_SPACE_CMS + "cms_selectDictWordsByChannel", orderChannelId);
    }
}
