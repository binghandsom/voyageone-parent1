package com.voyageone.service.impl.cms.jumei;

import com.voyageone.service.dao.cms.CmsBtJmSkuDao;
import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import com.voyageone.service.model.cms.CmsBtJmSkuModel;
import com.voyageone.service.bean.cms.businessmodel.JMUpdateSkuWithPromotionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmSkuService {
@Autowired
    CmsBtJmSkuDao dao;

    public CmsBtJmSkuModel select(int id)
    {
       return dao.select(id);
    }

    public int update(CmsBtJmSkuModel entity)
    {
   return dao.update(entity);
    }
    public int create(CmsBtJmSkuModel entity)
    {
                   return dao.insert(entity);
    }

    public List<CmsBtJmSkuModel> selectList(Map<String, Object> param) {
        return dao.selectList(param);
    }

    /**
     * 返回
     * @param skuList
     * @param promotionSkuList
     * @return
     */
    public List<JMUpdateSkuWithPromotionInfo> selectSkuList(List<CmsBtJmSkuModel> skuList, List<CmsBtJmPromotionSkuModel> promotionSkuList) {
        List<JMUpdateSkuWithPromotionInfo> result = new ArrayList<>();
        for (CmsBtJmSkuModel skuInfo : skuList) {
            JMUpdateSkuWithPromotionInfo data = new JMUpdateSkuWithPromotionInfo();
            data.setCmsBtJmSkuModel(skuInfo);
            for (CmsBtJmPromotionSkuModel promotionSku : promotionSkuList) {
                if (skuInfo.getId() == promotionSku.getCmsBtJmSkuId())
                    data.setCmsBtJmPromotionSkuModel(promotionSku);
            }
            result.add(data);
        }
        return result;
    }
}

