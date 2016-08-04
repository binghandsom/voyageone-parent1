package com.voyageone.service.impl.cms.prices;

import com.voyageone.service.dao.cms.CmsMtFeeTaxDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtFeeTaxModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 税率服务, 用来查询, 计算和配置税率
 * <p>
 * Created by jonas on 8/4/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
@Service
public class CmsMtFeeTaxService extends BaseService {

    private final CmsMtFeeTaxDao cmsMtFeeTaxDao;

    @Autowired
    public CmsMtFeeTaxService(CmsMtFeeTaxDao cmsMtFeeTaxDao) {
        this.cmsMtFeeTaxDao = cmsMtFeeTaxDao;
    }

    /**
     * 取关税税率
     *
     * @param shippingType 发货方式
     * @param hsCode       税号
     * @return 关税税率
     */
    private Double getTaxRate(String shippingType, String hsCode) {

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("shippingType", shippingType);
        queryMap.put("hsCode", hsCode);

        CmsMtFeeTaxModel cmsMtFeeTaxModel = cmsMtFeeTaxDao.selectOne(queryMap);

        if (cmsMtFeeTaxModel != null) {
            return cmsMtFeeTaxModel.getVaTaxRate() + cmsMtFeeTaxModel.getConsumptionTaxRate();
        }

        return null;
    }
}
