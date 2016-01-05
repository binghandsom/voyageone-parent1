package com.voyageone.web2.cms.model;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;

/**
 * @author james.li on 2015/12/15.
 * @version 2.0.0
 */
public class CmsBtPromotionGroupModel  extends BaseMongoModel {
    private int seq;

    private int promotionId;

    private Long modelId;

    private String productModel;

    private String catPath;

    private String numIid;

    private Boolean synFlg;

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
}
