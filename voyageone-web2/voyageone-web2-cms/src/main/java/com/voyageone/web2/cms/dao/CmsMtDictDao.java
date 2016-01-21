package com.voyageone.web2.cms.dao;

import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.web2.base.dao.WebBaseDao;
import com.voyageone.web2.base.dao.WebDaoNs;
import com.voyageone.web2.cms.model.CmsMtDictModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jonas, 1/19/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class CmsMtDictDao extends WebBaseDao {
    @Override
    protected WebDaoNs webNs() {
        return WebDaoNs.CMS;
    }

    public List<CmsMtDictModel> selectByChannel(ChannelConfigEnums.Channel channel) {
        return selectList("cms_mt_dict_selectByChannel", parameters("channel_id", channel.getId()));
    }
}
