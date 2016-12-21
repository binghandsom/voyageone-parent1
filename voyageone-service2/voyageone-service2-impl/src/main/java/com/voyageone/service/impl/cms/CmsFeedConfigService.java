package com.voyageone.service.impl.cms;

import com.voyageone.service.bean.cms.CmsMtFeedConfigBean;
import com.voyageone.service.bean.cms.mt.channel.config.SaveListInfo;
import com.voyageone.service.dao.cms.CmsMtFeedConfigDao;
import com.voyageone.service.daoext.cms.CmsMtFeedConfigDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtFeedConfigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CmsFeedConfigService extends BaseService {
    @Autowired
    private CmsMtFeedConfigDaoExt cmsMtFeedConfigDaoExt;
    @Autowired
    private CmsMtFeedConfigDao cmsMtFeedConfigDao;

    /**
     * 数据初始化
     *
     * @param channelId
     * @return
     */
    public Map<String, Object> search(String channelId) {
        Map<String, Object> resultMap = new HashMap();
        List<CmsMtFeedConfigBean> configs = cmsMtFeedConfigDaoExt.selectFeedConFigByChannelId(channelId);
        resultMap.put("configs", configs);
        return resultMap;
    }

    /**
     * 数据保存
     *
     * @param info
     * @param channelId
     * @param userName
     */
    public void save(List<CmsMtFeedConfigBean> info, String channelId, String userName) {
        //循环数据到数据库里面检索根据channelId删除
        cmsMtFeedConfigDaoExt.deleteFeedConFigByChannelId(channelId);
        //循环取得页面数据插入到数据库里
        for (CmsMtFeedConfigBean cmsMtFeedConfigBean : info) {
            CmsMtFeedConfigModel cmsMtFeedConfigModel = new CmsMtFeedConfigModel();
            cmsMtFeedConfigModel.setOrderChannelId(channelId);
            cmsMtFeedConfigModel.setCfgName(cmsMtFeedConfigBean.getCfgName());
            cmsMtFeedConfigModel.setCfgVal1(cmsMtFeedConfigBean.getCfgVal1());
            cmsMtFeedConfigModel.setCfgVal2(cmsMtFeedConfigBean.getCfgVal2());
            cmsMtFeedConfigModel.setComment(cmsMtFeedConfigBean.getCfgVal2());
            cmsMtFeedConfigDao.insert(cmsMtFeedConfigModel);
        }
    }
}
