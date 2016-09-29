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
     * @param hsCode 税号
     * @return 关税税率
     */
    public Double getTaxRate(String hsCode, String shippingType) {

        Map<String, Object> queryMap = new HashMap<String, Object>() {{
            put("hsCode", hsCode);
            put("shippingType", shippingType);
        }};

        CmsMtFeeTaxModel feeTaxModel = cmsMtFeeTaxDao.selectOne(queryMap);

        if (feeTaxModel == null)
            return null;

        // 此处进行拆箱, 作为错误检查
        double vaTaxRate = feeTaxModel.getVaTaxRate();
        double consumptionTaxRate = feeTaxModel.getConsumptionTaxRate();

        return vaTaxRate + consumptionTaxRate;
    }

    /**
     *获取默认税率
     *
     * @return 默认税率
     */
    public Double getDefaultTaxRate() {

        Double defaultTaxRate;

        defaultTaxRate = getTaxRate("","");

        if(defaultTaxRate == null){
            $warn("***   配置表中没有配置默认税率，系统设置税率为11.9   ***");
            //默认税号设置为11.9   更新时间2016-09-27
            defaultTaxRate = 11.9;
        }

        return defaultTaxRate;
    }
}
