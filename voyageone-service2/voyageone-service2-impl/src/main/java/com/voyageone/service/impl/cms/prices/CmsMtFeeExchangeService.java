package com.voyageone.service.impl.cms.prices;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.common.asserts.Assert;
import com.voyageone.service.dao.cms.CmsMtFeeExchangeDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtFeeExchangeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 汇率服务, 用于查询或操作汇率配置
 * <p>
 * Created by jonas on 8/4/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
@Service
public class CmsMtFeeExchangeService extends BaseService {

    private final static String USD = "USD";

    private final CmsMtFeeExchangeDao cmsMtFeeExchangeDao;

    @Autowired
    public CmsMtFeeExchangeService(CmsMtFeeExchangeDao cmsMtFeeExchangeDao) {
        this.cmsMtFeeExchangeDao = cmsMtFeeExchangeDao;
    }

    /**
     * 获取配置中的美元汇率
     *
     * @return 汇率, 如 6.7
     */
    public Double getExchangeRateForUsd() {
        return getExchangeRate(USD);
    }

    /**
     * 获取货币相应的最新汇率
     *
     * @return 汇率, 如 6.7
     */
    public Double getExchangeRate(String currencyType) {

        Assert.notNull(currencyType).elseThrowDefaultWithTitle("currencyType");

        Map<String, Object> map = MySqlPageHelper
                .build()
                .addQuery("currencyType", currencyType)
                .addSort("modified", Order.Direction.DESC)
                .toMap();

        CmsMtFeeExchangeModel feeExchangeModel = cmsMtFeeExchangeDao.selectOne(map);

        Assert.notNull(feeExchangeModel).elseThrowDefaultWithTitle("feeExchangeModel (CmsMtFeeExchangeService.getExchangeRate)");

        return feeExchangeModel.getExchangeRate();
    }
}
