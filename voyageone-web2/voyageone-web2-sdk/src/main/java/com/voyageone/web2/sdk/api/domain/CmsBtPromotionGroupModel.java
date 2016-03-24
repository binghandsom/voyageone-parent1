package com.voyageone.web2.sdk.api.domain;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.cms.CmsConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * @author james.li on 2015/12/15.
 * @version 2.0.0
 */
public class CmsBtPromotionGroupModel extends BaseMongoModel {
    private int seq;

    private String channelId;

    private Integer cartId;

    private int promotionId;

    private Long modelId;

    private String productModel;

    private String catPath;

    private String numIid;

    private Boolean synFlg;

    private Integer inventory;

    private CmsConstants.PlatformStatus platformStatus;

    private String tag;

    private List<CmsBtPromotionCodeModel> codes;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public CmsBtPromotionGroupModel(){
        super();
    }
    public CmsBtPromotionGroupModel(CmsBtProductModel productInfo, int cartId, int promotionId, String operator){
        this();
        // catPath
        this.setCatPath(productInfo.getCatPath());

        CmsBtProductModel_Group_Platform platform = productInfo.getGroups().getPlatformByCartId(cartId);
        if(platform !=  null){
            // numIid
            this.setNumIid(platform.getNumIId() == null ? "": platform.getNumIId());
            // modelId
            this.setModelId(platform.getGroupId());
            // ProductModel
            this.setProductModel(productInfo.getFields().getModel());
            // SynFlg
            this.setSynFlg(false);

            this.promotionId = promotionId;

            this.setCreater(operator);

            this.setModifier(operator);
        }

    }
    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getCatPath() {
        return catPath;
    }

    public void setCatPath(String catPath) {
        this.catPath = catPath;
    }

    public String getNumIid() {
        return numIid;
    }

    public void setNumIid(String numIid) {
        this.numIid = numIid;
    }

    public Boolean getSynFlg() {
        return synFlg;
    }

    public void setSynFlg(Boolean synFlg) {
        this.synFlg = synFlg;
    }

    public CmsConstants.PlatformStatus getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(CmsConstants.PlatformStatus platformStatus) {
        this.platformStatus = platformStatus;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public List<CmsBtPromotionCodeModel> getCodes() {
        if(codes == null) {
            codes = new ArrayList<>();
        }
        return codes;
    }

    public void setCodes(List<CmsBtPromotionCodeModel> codes) {
        this.codes = codes;
    }

    public CmsBtPromotionCodeModel getProductByCode(String code) {
        for(CmsBtPromotionCodeModel item:codes){
            if(item.getProductCode().equalsIgnoreCase(code)){
                return item;
            }
        }
        return null;
    }
}
