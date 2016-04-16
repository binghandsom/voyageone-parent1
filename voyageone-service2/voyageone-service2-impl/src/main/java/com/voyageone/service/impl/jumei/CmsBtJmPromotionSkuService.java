package com.voyageone.service.impl.jumei;

import com.voyageone.service.dao.jumei.CmsBtJmPromotionSkuDao;
import com.voyageone.service.model.jumei.CmsBtJmPromotionSkuModel;
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

    public CmsBtJmPromotionSkuModel select(long id) {
        return dao.select(id);
    }

    public int update(CmsBtJmPromotionSkuModel entity) {
        return dao.update(entity);
    }

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
        return entity.getId();
    }

    public int insert(CmsBtJmPromotionSkuModel entity) {
        return dao.insert(entity);
    }

    public int delete(long id) {
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

