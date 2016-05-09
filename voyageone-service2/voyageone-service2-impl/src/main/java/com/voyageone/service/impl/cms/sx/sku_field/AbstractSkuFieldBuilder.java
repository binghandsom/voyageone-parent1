package com.voyageone.service.impl.cms.sx.sku_field;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.service.dao.cms.CmsMtChannelSkuConfigDao;
import com.voyageone.service.dao.cms.CmsMtPlatformPropSkuDao;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;

import java.util.List;
import java.util.Map;


/**
 * Created by morse.lu 2016/05/06 (copy and modified from task2 / AbstractSkuFieldBuilder)
 */
public abstract class AbstractSkuFieldBuilder extends VOAbsLoggable {

    protected CmsMtPlatformPropSkuDao cmsMtPlatformPropSkuDao;
    protected CmsMtChannelSkuConfigDao cmsMtChannelSkuConfigDao;

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
}
