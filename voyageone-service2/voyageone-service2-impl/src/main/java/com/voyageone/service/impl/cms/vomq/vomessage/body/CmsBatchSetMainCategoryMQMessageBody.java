package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @author edward 2017/03/27.
 * @version 2.16.0
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_BATCH_SET_MAIN_CATEGORY)
public class CmsBatchSetMainCategoryMQMessageBody extends BaseMQMessageBody {

    private String catId;

    private String catPath;

    private String catPathEn;

    private List<String> productCodes;

    private String productType;

    private String sizeType;

    private String productTypeCn;

    private String sizeTypeCn;

    private String hscodeName8;

    private String hscodeName10;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatPath() {
        return catPath;
    }

    public void setCatPath(String catPath) {
        this.catPath = catPath;
    }

    public String getCatPathEn() {
        return catPathEn;
    }

    public void setCatPathEn(String catPathEn) {
        this.catPathEn = catPathEn;
    }

    public List<String> getProductCodes() {
        return productCodes;
    }

    public void setProductCodes(List<String> productCodes) {
        this.productCodes = productCodes;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType;
    }

    public String getProductTypeCn() {
        return productTypeCn;
    }

    public void setProductTypeCn(String productTypeCn) {
        this.productTypeCn = productTypeCn;
    }

    public String getSizeTypeCn() {
        return sizeTypeCn;
    }

    public void setSizeTypeCn(String sizeTypeCn) {
        this.sizeTypeCn = sizeTypeCn;
    }

    public String getHscodeName8() {
        return hscodeName8;
    }

    public void setHscodeName8(String hscodeName8) {
        this.hscodeName8 = hscodeName8;
    }

    public String getHscodeName10() {
        return hscodeName10;
    }

    public void setHscodeName10(String hscodeName10) {
        this.hscodeName10 = hscodeName10;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (StringUtils.isEmpty(super.getChannelId())) {
            throw new MQMessageRuleException("高级检索-批量设置主类目MQ发送异常, 参数channelId为空.");
        }
        if (StringUtils.isEmpty(catId)) {
            throw new MQMessageRuleException("高级检索-批量设置主类目MQ发送异常, 参数catId为空.");
        }
        if (StringUtils.isEmpty(catPath)) {
            throw new MQMessageRuleException("高级检索-批量设置主类目MQ发送异常, 参数catPath为空.");
        }
        if (StringUtils.isEmpty(catPathEn)) {
            throw new MQMessageRuleException("高级检索-批量设置主类目MQ发送异常, 参数catPathEn为空.");
        }
        if (CollectionUtils.isEmpty(productCodes)) {
            throw new MQMessageRuleException("高级检索-批量设置主类目MQ发送异常, 参数productCodes为空.");
        }
    }
}
