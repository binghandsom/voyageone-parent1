package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;

import java.math.BigDecimal;
import java.util.Set;

/**
 * 推送产品信息到wms系统
 *
 * @Author edward.lin
 * @Create 2017-04-18 16:03
 */
@VOMQQueue(value = CmsMqRoutingKey.EWMS_MQ_CREATE_OR_UPDATE_PRODUCT)
public class WmsCreateOrUpdateProductMQMessageBody extends BaseMQMessageBody {

    /*商品渠道ID，必要参数*/
    private String channelId;
    /*产品SKU，必要参数*/
    private String sku;
    /*产品名称，必要参数*/
    private String name;
    /*产品Code，必要参数*/
    private String code;
    /*品牌，非必须参数*/
    private String brand;
    /*颜色，非必须参数*/
    private String color;
    /*尺码，非必须参数*/
    private String size;
    /*产品UPC，必要参数*/
    private String barcode;
    /*非必要参数*/
    private String imageUrl;
    /*建议零售价，非必须参数*/
    private BigDecimal msrp;
    /*非必须参数*/
    private String clientSku;
    /*非必须参数*/
    private String clientMainSku;
    /*非必须参数*/
    private String cmsSku;
    /*非必须参数*/
    private String skuKind;
    /*成本价，非必须参数*/
    private BigDecimal netPrice;
    /*产品类型，非必须参数*/
    private String productType;
    /*产品详情，非必须参数*/
    private WmsCreateOrUpdateProductMQMessageBody_detail detailInfo;
    /*sku绑定的其他UPC，非必须参数*/
    private Set<String> extBarcodes;
    /*操作人，非必须参数*/
    private String userName;
    /*是否可售*/
    private Integer isSale;


    @Override
    public void check() {

    }


    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getMsrp() {
        return msrp;
    }

    public void setMsrp(BigDecimal msrp) {
        this.msrp = msrp;
    }

    public String getClientSku() {
        return clientSku;
    }

    public void setClientSku(String clientSku) {
        this.clientSku = clientSku;
    }

    public String getClientMainSku() {
        return clientMainSku;
    }

    public void setClientMainSku(String clientMainSku) {
        this.clientMainSku = clientMainSku;
    }

    public String getCmsSku() {
        return cmsSku;
    }

    public void setCmsSku(String cmsSku) {
        this.cmsSku = cmsSku;
    }

    public String getSkuKind() {
        return skuKind;
    }

    public void setSkuKind(String skuKind) {
        this.skuKind = skuKind;
    }

    public BigDecimal getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(BigDecimal netPrice) {
        this.netPrice = netPrice;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public WmsCreateOrUpdateProductMQMessageBody_detail getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(WmsCreateOrUpdateProductMQMessageBody_detail detailInfo) {
        this.detailInfo = detailInfo;
    }

    public Set<String> getExtBarcodes() {
        return extBarcodes;
    }

    public void setExtBarcodes(Set<String> extBarcodes) {
        this.extBarcodes = extBarcodes;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getIsSale() {
        return isSale;
    }

    public void setIsSale(Integer isSale) {
        this.isSale = isSale;
    }
}
