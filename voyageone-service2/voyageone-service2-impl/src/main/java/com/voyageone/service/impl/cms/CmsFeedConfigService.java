package com.voyageone.service.impl.cms;

import com.voyageone.service.bean.cms.CmsMtFeedConfigBean;
import com.voyageone.service.bean.cms.mt.channel.config.SaveListInfo;
import com.voyageone.service.dao.cms.CmsMtFeedConfigDao;
import com.voyageone.service.daoext.cms.CmsMtFeedConfigDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtFeedConfigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


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
        //cms_mt_feed_config_key取得主数据
        List<CmsMtFeedConfigBean> cmsMtFeedConfigKeyList =cmsMtFeedConfigDaoExt.selectFeedConFigKey();
        //cfgName,channelID
        List<CmsMtFeedConfigBean> cmsMtFeedConfigList = cmsMtFeedConfigDaoExt.selectFeedConFigByChannelId(channelId);
        for(CmsMtFeedConfigBean cmsMtFeedConfigBean:cmsMtFeedConfigKeyList){
            cmsMtFeedConfigList.stream().filter(bean -> cmsMtFeedConfigBean.getCfgName().equals(bean.getCfgName())).forEach(bean -> {
                cmsMtFeedConfigBean.setOrderChannelId(bean.getOrderChannelId());
                cmsMtFeedConfigBean.setCfgVal1(bean.getCfgVal1());
                cmsMtFeedConfigBean.setCfgVal2(bean.getCfgVal2());
                cmsMtFeedConfigBean.setCfgVal3(bean.getCfgVal3());
                cmsMtFeedConfigBean.setComment(bean.getComment());
                cmsMtFeedConfigBean.setCmsIsCfgVal1Display(bean.getCmsIsCfgVal1Display());
                cmsMtFeedConfigBean.setCmsIsCfgVal2Display(bean.getCmsIsCfgVal3Display());
                cmsMtFeedConfigBean.setCmsIsCfgVal3Display(bean.getCmsIsCfgVal3Display());
            });
        }
        resultMap.put("configs", cmsMtFeedConfigKeyList);
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
            cmsMtFeedConfigModel.setCfgVal2(cmsMtFeedConfigBean.getCfgVal2());
            cmsMtFeedConfigModel.setCfgVal3(cmsMtFeedConfigBean.getCfgVal3());
            cmsMtFeedConfigModel.setIsAttribute(0);
            cmsMtFeedConfigModel.setAttributeType(0);
            cmsMtFeedConfigModel.setComment(cmsMtFeedConfigBean.getComment());
            cmsMtFeedConfigModel.setDisplaySort(-1);
            cmsMtFeedConfigModel.setModifier(userName);
            cmsMtFeedConfigModel.setModified(new Date());
            cmsMtFeedConfigModel.setStatus(1);
            cmsMtFeedConfigModel.setCfgVal1(cmsMtFeedConfigBean.getCfgVal1());
            cmsMtFeedConfigDao.insert(cmsMtFeedConfigModel);
        }
    }
}
