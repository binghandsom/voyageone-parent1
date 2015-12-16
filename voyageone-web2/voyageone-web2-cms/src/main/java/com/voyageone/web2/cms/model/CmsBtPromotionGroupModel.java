package com.voyageone.web2.cms.model;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.common.util.StringUtils;

/**
 * @author james.li on 2015/12/15.
 * @version 2.0.0
 */
public class CmsBtPromotionGroupModel  extends BaseMongoModel {
    private int seq;

    private int promotionId;

    private int modelId;

    private String productModel;

    private String catPath;

    private String numIid;

    private Boolean synFlg;

    public CmsBtPromotionGroupModel(){

    }
    public CmsBtPromotionGroupModel(CmsBtProductModel productInfo, int cartId, int promotionId, String operator){
        // catPath
        this.setCatPath(StringUtils.decodeBase64(productInfo.getCatId()));

        CmsBtProductModel_Group_Platform platform = productInfo.getGroups().getPlatformByCartId(cartId);
        if(platform !=  null){
            // numIid
            this.setNumIid(platform.getNumIId());
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

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
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
