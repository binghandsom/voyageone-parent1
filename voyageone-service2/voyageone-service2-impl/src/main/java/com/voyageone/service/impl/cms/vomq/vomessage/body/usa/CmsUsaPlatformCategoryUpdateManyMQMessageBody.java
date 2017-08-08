package com.voyageone.service.impl.cms.vomq.vomessage.body.usa;

import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/8/7.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_USA_PLATFORM_CATEGORY_UPDATE_MANY)
public class CmsUsaPlatformCategoryUpdateManyMQMessageBody extends BaseMQMessageBody {

    List<String> productCodes;
    List<String> pCatPaths;
//    String pCatPath;

//    String pCatId;
    Integer cartId;
    //true:添加类目,false:移除类目
    Boolean statue;

    public List<String> getpCatPath() {
        return pCatPaths;
    }

    public void setpCatPath(List<String> pCatPath) {
        this.pCatPaths = pCatPath;
    }

    public Boolean getStatue() {
        return statue;
    }

    public void setStatue(Boolean statue) {
        this.statue = statue;
    }

    public List<String> getProductCodes() {
        return productCodes;
    }

    public void setProductCodes(List<String> productCodes) {
        this.productCodes = productCodes;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if (org.apache.commons.lang.StringUtils.isBlank(super.getChannelId())) {
            throw new MQMessageRuleException("美国CMS-高级检索-批量设置平台类目MQ发送异常, 参数channelId为空.");
        }
        if (CollectionUtils.isEmpty(productCodes)) {
            throw new MQMessageRuleException("美国CMS-高级检索-批量设置平台类目MQ发送异常, 参数productCodes为空.");
        }
        if (cartId == null) {
            throw new MQMessageRuleException("美国CMS-高级检索-批量设置平台类目MQ发送异常, 参数cartId为空.");
        }

        if (ListUtils.isNull(pCatPaths)) {
            throw new MQMessageRuleException("美国CMS-高级检索-批量设置平台类目MQ发送异常, 参数pCatPath为空.");
        }
        if (statue == null) {
            throw new MQMessageRuleException("美国CMS-高级检索-批量设置平台类目MQ发送异常, 参数statue为空.");
        }
    }
}
