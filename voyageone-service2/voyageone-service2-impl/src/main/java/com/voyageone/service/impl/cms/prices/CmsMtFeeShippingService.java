package com.voyageone.service.impl.cms.prices;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.asserts.Assert;
import com.voyageone.common.util.MapUtil;
import com.voyageone.service.dao.cms.CmsMtFeeShippingDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtFeeShippingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 运费相关操作
 * Created by jonas on 8/2/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
@Service
public class CmsMtFeeShippingService extends BaseService {

    private static final Byte BY_WEIGHT = 0;
    private static final Byte BY_PC = 1;

    private final CmsMtFeeShippingDao cmsMtFeeShippingDao;

    @Autowired
    public CmsMtFeeShippingService(CmsMtFeeShippingDao cmsMtFeeShippingDao) {
        this.cmsMtFeeShippingDao = cmsMtFeeShippingDao;
    }

    /**
     * 根据发货方式计算商品运费
     *
     * @param shippingType 发货方式, 按发货方式判断计重还是计件
     * @param weight       商品重量, 当计重时, 需要参与计算, 单位 lb
     */
    public Double getShippingFee(String shippingType, double weight) {

        Assert.hasText(shippingType).elseThrowDefaultWithTitle("shippingType");

        Map<String, Object> queryMap = MapUtil.toMap("shippingType", shippingType);

        // 查询 shippingType 对应的发货配置
        // 是计件, 还是计重
        CmsMtFeeShippingModel shippingModel = cmsMtFeeShippingDao.selectOne(queryMap);

        Byte feeType = shippingModel.getFeeType();

        if (BY_WEIGHT.equals(feeType)) {

            // 如果是计重
            // 那么获取配置中的首重, 首重费用. 续重和续重费用

            Integer firstWeight = shippingModel.getFirstWeight();
            Double firstWeightFee = shippingModel.getFirstWeightFee();
            Integer additionalWeight = shippingModel.getAdditionalWeight();
            Double additionalWeightFee = shippingModel.getAdditionalWeightFee();

            // 如果商品重量不足以继续计算
            // 就直接用首重费用了
            // 否则按照公式, 计算运费
            if (weight <= firstWeight) {
                return firstWeightFee;
            } else {
                return firstWeightFee + Math.ceil((weight - firstWeight) / additionalWeight) * additionalWeightFee;
            }
        } else if (BY_PC.equals(feeType)) {

            // 如果是计件, 则直接返回费用即可
            // 无需计算
            return shippingModel.getPcFee();
        } else {

            // 或者配置不正确
            // 无法计算运费
            throw new BusinessException("无法计算运费, 因为无法匹配 feeType: " + feeType);
        }
    }
}
