package com.voyageone.service.impl.cms.vomq.vomessage.body.usa;

import com.voyageone.common.util.ListUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import java.util.List;

/**
 * Created by james on 2017/7/7.
 *
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_USA_PRODUCT_ADD_UPDATE)
public class CmsProductAddUpdateMQMessageBody extends BaseMQMessageBody {

    private String code;
    private String name;
    private String color;
    private Double msrp;

    //0:new/update/1:delete
    private Integer status;
    private List<SkuModel> skuList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getMsrp() {
        return msrp;
    }

    public void setMsrp(Double msrp) {
        this.msrp = msrp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<SkuModel> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<SkuModel> skuList) {
        this.skuList = skuList;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if(ListUtils.isNull(skuList)){
            throw new MQMessageRuleException("sku为空");
        }
    }

    public static class SkuModel {
        private String sku;
        //0: 无效 / 1: 有效
        private Integer isSale;

        private String size;

        private String barcode;

        private Integer qty;

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public Integer getIsSale() {
            return isSale;
        }

        public void setIsSale(Integer isSale) {
            this.isSale = isSale;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public Integer getQty() {
            return qty;
        }

        public void setQty(Integer qty) {
            this.qty = qty;
        }
    }
}
