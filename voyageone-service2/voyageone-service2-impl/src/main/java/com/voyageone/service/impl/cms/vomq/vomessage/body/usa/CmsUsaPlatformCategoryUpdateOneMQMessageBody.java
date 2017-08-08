package com.voyageone.service.impl.cms.vomq.vomessage.body.usa;

import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.poi.util.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/8/7.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_USA_PLATFORM_CATEGORY_UPDATE_ONE)
public class CmsUsaPlatformCategoryUpdateOneMQMessageBody extends BaseMQMessageBody {

    List<String> productCodes;
    String pCatPath;
    String pCatId;
    Integer cartId;
    //判断是否替换,true替换,false不替换
    Boolean flag;
    Map<String,Object> mapping;

    public Map<String, Object> getMapping() {
        return mapping;
    }
    public void setMapping(Map<String, Object> mapping) {
        this.mapping = mapping;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getpCatPath() {
        return pCatPath;
    }

    public void setpCatPath(String pCatPath) {
        this.pCatPath = pCatPath;
    }

    public String getpCatId() {
        return pCatId;
    }

    public void setpCatId(String pCatId) {
        this.pCatId = pCatId;
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
        if (StringUtils.isEmpty(pCatId)) {
            throw new MQMessageRuleException("美国CMS-高级检索-批量设置平台类目MQ发送异常, 参数pCatId为空.");
        }
        if (StringUtils.isEmpty(pCatPath)) {
            throw new MQMessageRuleException("美国CMS-高级检索-批量设置平台类目MQ发送异常, 参数pCatPath为空.");
        }
        if (flag == null) {
            throw new MQMessageRuleException("美国CMS-高级检索-批量设置平台类目MQ发送异常, 参数flag为空.");
        }

    }
}
