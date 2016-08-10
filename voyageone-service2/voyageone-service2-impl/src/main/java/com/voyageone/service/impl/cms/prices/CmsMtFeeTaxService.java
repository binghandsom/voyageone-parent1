package com.voyageone.service.impl.cms.prices;

import com.voyageone.common.asserts.Assert;
import com.voyageone.common.util.MapUtil;
import com.voyageone.service.dao.cms.CmsMtFeeTaxDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtFeeTaxModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Double getTaxRate(String hsCode) {

        Assert.notNull(hsCode).elseThrowDefaultWithTitle("hsCode (CmsMtFeeTaxService.getTaxRate)");

        Map<String, Object> queryMap = MapUtil.toMap("shippingType", "", "hsCode", hsCode);

        CmsMtFeeTaxModel feeTaxModel = cmsMtFeeTaxDao.selectOne(queryMap);

        if (feeTaxModel == null)
            return null;

        // 此处进行拆箱, 作为错误检查
        double vaTaxRate = feeTaxModel.getVaTaxRate();
        double consumptionTaxRate = feeTaxModel.getConsumptionTaxRate();

        return vaTaxRate + consumptionTaxRate;
    }
}
