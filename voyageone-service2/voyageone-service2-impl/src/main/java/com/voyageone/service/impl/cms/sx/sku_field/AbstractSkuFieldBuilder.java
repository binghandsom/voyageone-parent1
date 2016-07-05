package com.voyageone.service.impl.cms.sx.sku_field;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsMtChannelSkuConfigDao;
import com.voyageone.service.dao.cms.CmsMtPlatformPropSkuDao;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by morse.lu 2016/05/06 (copy and modified from task2 / AbstractSkuFieldBuilder)
 */
public abstract class AbstractSkuFieldBuilder extends VOAbsLoggable {

    protected CmsMtPlatformPropSkuDao cmsMtPlatformPropSkuDao;
    protected CmsMtChannelSkuConfigDao cmsMtChannelSkuConfigDao;
    private List<Field> unkownFields;

    private int cartId = -1;
    private String codeImageTemplate;

    public final boolean isYourFood(List<Field> platformProps, int cartId) {
        setCartId(cartId);
        return init(platformProps, cartId);
    }

    protected abstract boolean init(List<Field> platformProps, int cartId);

    public List<Field> buildSkuInfoField(List platformProps, ExpressionParser expressionParser, CmsMtPlatformMappingModel cmsMtPlatformMappingModel, Map<String, Integer> skuInventoryMap, ShopBean shopBean, String user) throws Exception {
        int cartId = expressionParser.getSxData().getCartId();
        if (getCartId() == -1) {
            // 未初期化
            if(!isYourFood(platformProps, cartId)) {
                // 不是这个模板
                $warn("不是%s这个模板", this.getClass().getSimpleName());
                return null;
            }
        } else if(getCartId() != cartId) {
            // 初期化时cartId 与 要设置的数据的平台不一致(正常不会有这种情况，先写着)
            $warn("初期化时cartId 与 要设置的数据的平台不一致");
            return null;
        }

        return buildSkuInfoFieldChild(platformProps, expressionParser, cmsMtPlatformMappingModel, skuInventoryMap, shopBean, user);
    }

    public abstract List buildSkuInfoFieldChild(List platformProps, ExpressionParser expressionParser, CmsMtPlatformMappingModel cmsMtPlatformMappingModel, Map<String, Integer> skuInventoryMap, ShopBean shopBean, String user) throws Exception;

    public void setDao(CmsMtPlatformPropSkuDao cmsMtPlatformPropSkuDao, CmsMtChannelSkuConfigDao cmsMtChannelSkuConfigDao) {
        this.cmsMtPlatformPropSkuDao = cmsMtPlatformPropSkuDao;
        this.cmsMtChannelSkuConfigDao = cmsMtChannelSkuConfigDao;
    }

    public int getCartId() {
        return cartId;
    }

    private void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getCodeImageTemplate() {
        return codeImageTemplate;
    }

    public void setCodeImageTemplate(String codeImageTemplate) {
        this.codeImageTemplate = codeImageTemplate;
    }

    protected final void addUnkownField(Field field) {
        if (unkownFields == null) {
            unkownFields = new ArrayList<>();
        }
        unkownFields.add(field);
    }

    protected final List<Field> getUnkownFields() {
        return unkownFields;
    }

    /**
     * 从各平台下面的fields下面去取sku属性（这里的sku）
     * 目前不支持复杂类型
     */
    protected final String getSkuValue(CmsBtProductModel cmsBtProductModel, String fieldId, String skuCode) {
        if (cmsBtProductModel.getPlatform(getCartId()).getFields() != null) {
            Object objSku = cmsBtProductModel.getPlatform(getCartId()).getFields().getAttribute("sku"); // 暂时先放这边，以后应该会改
            if (objSku != null) {
                try {
                    List<Map<String, Object>> listVal = (List<Map<String, Object>>) objSku;
                    Object objVal = null;
                    boolean hasSkuCode = false;
                    for (Map<String, Object> mapSku : listVal) {
                        if (skuCode.equals(mapSku.get("sku_outerId"))) {
                            // 商家编码，保存的是skuCode
                            objVal = mapSku.get(fieldId);
                            hasSkuCode = true;
                            break;
                        }
                    }
                    if (!hasSkuCode) {
                        // 没找到,画面的商家编码填错了
                        throw new BusinessException(String.format("画面的商家编码填错了!没有Sku=%s", skuCode));
                    }
                    if (objVal == null) {
                        return null;
                    } else if (objVal instanceof String || objVal instanceof Number || objVal instanceof Boolean) {
                        return String.valueOf(objVal);
                    } else {
                        $warn(String.format("sku属性取得暂不支持复杂类型数据[fieldId:%s,skuCode:%s]", fieldId, skuCode));
                        return null;
                    }
                } catch (ClassCastException ex) {
                    $warn(String.format("sku属性取得失败[fieldId:%s,skuCode:%s]", fieldId, skuCode));
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * sku价格取得,根据配置表来决定取哪个价格
     */
    protected final double getSkuPrice(String channelId, CmsBtProductModel cmsBtProductModel, String skuCode) {
        double skuPrice = 0d;

        // 价格有可能是用priceSale, 也有可能用priceMsrp, 通过配置表判断一下
        CmsChannelConfigBean sxPriceConfig = CmsChannelConfigs.getConfigBean(channelId
                                                        , CmsConstants.ChannelConfig.PRICE
                                                        , String.valueOf(cartId) + CmsConstants.ChannelConfig.PRICE_SX_PRICE);

        // 检查一下
        String sxPricePropName;
        if (sxPriceConfig == null) {
            return skuPrice;
        } else {
            sxPricePropName = sxPriceConfig.getConfigValue1();
            if (StringUtils.isEmpty(sxPricePropName)) {
                return skuPrice;
            }
        }

        BaseMongoMap<String, Object> cmsBtProductModelSku = null;

        for (BaseMongoMap<String, Object> loopSku : cmsBtProductModel.getPlatform(getCartId()).getSkus()) {
           if (skuCode.equals(loopSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()))) {
               cmsBtProductModelSku = loopSku;
               break;
            }
        }

        if (cmsBtProductModelSku != null) {
            try {
                skuPrice = Double.valueOf(cmsBtProductModelSku.getStringAttribute(sxPricePropName));
            } catch (Exception e) {
                $warn("No price for sku " + cmsBtProductModelSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()));
            }
        }

        return skuPrice;
    }
}
