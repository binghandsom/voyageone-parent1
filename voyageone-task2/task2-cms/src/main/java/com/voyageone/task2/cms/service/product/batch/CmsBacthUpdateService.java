package com.voyageone.task2.cms.service.product.batch;

import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.impl.cms.prices.IllegalPriceConfigException;
import com.voyageone.service.impl.cms.prices.PriceCalculateException;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 高级检索业务的批量更新
 *
 * @author jiangjusheng on 2016/08/24
 * @version 2.0.0
 */
@Service
public class CmsBacthUpdateService extends VOAbsLoggable {

    @Autowired
    private ProductService productService;
    @Autowired
    private PriceService priceService;

    public void onStartup(Map<String, Object> messageMap) {
        $debug("高级检索 批量更新 开始执行... param=" + messageMap.toString());
        String channleId = StringUtils.trimToNull((String) messageMap.get("_channleId"));
        String userName = StringUtils.trimToNull((String) messageMap.get("_userName"));
        List<String> codeList = (List<String>) messageMap.get("productIds");
        if (channleId == null || userName == null || codeList == null || codeList.isEmpty()) {
            $error("高级检索 批量更新 缺少参数");
            return;
        }

        Map<String, Object> prop = (Map<String, Object>) messageMap.get("property");
        if (prop == null || prop.isEmpty()) {
            $error("高级检索 批量更新 缺少property参数");
            return;
        }
        String prop_id = StringUtils.trimToEmpty((String) prop.get("id"));
        if ("hsCodePrivate".equals(prop_id) || "hsCodeCrop".equals(prop_id)) {
            // 税号更新
            String hsCode = null;
            Map<String, Object> valObj = (Map<String, Object>) prop.get("value");
            if (valObj != null) {
                hsCode = StringUtils.trimToEmpty((String) valObj.get("value"));
            }
            // 是否自动同步指导价到最终售价
            Boolean synPriceFlg = (Boolean) messageMap.get("synPrice");
            if (synPriceFlg == null) {
                synPriceFlg = false;
            }
            updateHsCode(prop_id, hsCode, codeList, channleId, userName, synPriceFlg);
        }
    }

    /*
     * 税号变更
     */
    private void updateHsCode(String propId, String propValue, List<String> codeList, String channleId, String userName, Boolean synPriceFlg) {
        String msg = "税号变更 " + propId + "=> " + propValue;
        for (String prodCode : codeList) {
            try {
                CmsBtProductModel newProduct = productService.getProductByCode(channleId, prodCode);
                priceService.setPrice(newProduct, synPriceFlg);
                newProduct.getPlatforms().forEach((s, platform) -> {
                    if (platform.getCartId() != 0) {
                        productService.updateProductPlatform(channleId, newProduct.getProdId(), platform, userName, false, EnumProductOperationType.BatchUpdate, msg);
                    }
                });
            } catch (PriceCalculateException e) {
                $error(String.format("高级检索 批量更新 价格计算错误 channleid=%s, prodcode=%s", channleId, prodCode), e);
                continue;
            } catch (IllegalPriceConfigException e) {
                $error(String.format("高级检索 批量更新 配置错误 channleid=%s, prodcode=%s", channleId, prodCode), e);
                continue;
            } catch (Throwable e) {
                $error(String.format("高级检索 批量更新 未知错误 channleid=%s, prodcode=%s", channleId, prodCode), e);
                continue;
            }
        }
    }
}
