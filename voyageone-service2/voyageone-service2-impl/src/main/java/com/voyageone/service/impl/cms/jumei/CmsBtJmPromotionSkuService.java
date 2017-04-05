package com.voyageone.service.impl.cms.jumei;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.dao.cms.CmsBtJmPromotionSkuDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.JMRefreshPriceMQMessageBody;
import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotionSkuService {
    @Autowired
    CmsBtJmPromotionSkuDao dao;
    @Autowired
    CmsMqSenderService cmsMqSenderService;
    @Autowired
    CmsBtJmPromotionProductDaoExt cmsBtJmPromotionProductDaoExt;
    public CmsBtJmPromotionSkuModel select(int id) {
        return dao.select(id);
    }

    public int update(CmsBtJmPromotionSkuModel entity) {
        return dao.update(entity);
    }

    @VOTransactional
    public int updateWithDiscount(CmsBtJmPromotionSkuModel entity, String channelId, String modifer) {
        // 计算discount
        entity.setChannelId(channelId);
        entity.setCreater(modifer);
        entity.setModifier(modifer);
        entity.setDiscount(entity.getDealPrice().divide(entity.getMarketPrice(), 2, BigDecimal.ROUND_HALF_UP));
        if (entity.getId() > 0)
            this.update(entity);
        else
            this.insert(entity);
        cmsBtJmPromotionProductDaoExt.updateAvgPriceByPromotionProductId(entity.getCmsBtJmPromotionProductId());
        return entity.getId();
    }

    public void senderJMRefreshPriceMQMessage(int jmPromotionId, String sender, String channelId) {
        JMRefreshPriceMQMessageBody mqMessageBody = new JMRefreshPriceMQMessageBody();
        mqMessageBody.setCmsBtJmPromotionId(jmPromotionId);
        mqMessageBody.setChannelId(channelId);
        mqMessageBody.setSender(sender);
        cmsMqSenderService.sendMessage(mqMessageBody);
    }
    public int insert(CmsBtJmPromotionSkuModel entity) {
        return dao.insert(entity);
    }

    public int delete(int id) {
        return dao.delete(id);
    }

    public int create(CmsBtJmPromotionSkuModel entity) {
        return dao.insert(entity);
    }

    public List<CmsBtJmPromotionSkuModel> selectList(Map<String, Object> param) {
        return dao.selectList(param);
    }

    public CmsBtJmPromotionSkuModel selectOne(Map<String, Object> param) {
        return dao.selectOne(param);
    }
}

