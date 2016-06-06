package com.voyageone.service.impl.cms.jumei2;

import com.voyageone.service.bean.cms.jumei.UpdateSkuDealPriceParameter;
import com.voyageone.service.dao.cms.CmsBtJmPromotionSkuDao;
import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotionSku3Service {
    @Autowired
    CmsBtJmPromotionSkuDao dao;

    public CmsBtJmPromotionSkuModel select(int id) {
        return dao.select(id);
    }

    public int update(CmsBtJmPromotionSkuModel entity) {
        return dao.update(entity);
    }

    public int insert(CmsBtJmPromotionSkuModel entity) {
        return dao.insert(entity);
    }

    public int delete(int id) {
        return dao.delete(id);
    }


   public int updateDealPrice(UpdateSkuDealPriceParameter parameter,String modifier) {

       CmsBtJmPromotionSkuModel model = dao.select(parameter.getPromotionSkuId());
       model.setDealPrice(new BigDecimal(parameter.getDealPrice()));
       model.setModifier(modifier);
       return update(model);
   }

}

