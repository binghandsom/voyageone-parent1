package com.voyageone.service.bean.cms;

import com.voyageone.common.CmsConstants;
import com.voyageone.service.model.cms.CmsBtPromotionGroupsModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;

import java.util.ArrayList;
import java.util.List;

//import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;


/**
 * @author james.li on 2015/12/15.
 * @version 2.0.0
 */
public class CmsBtPromotionGroupsBean extends CmsBtPromotionGroupsModel {


    private String channelId;

    private String channelName;

    private Integer cartId;

    private Integer inventory;

    private CmsConstants.PlatformStatus platformStatus;

    private String tag;

    private List<CmsBtPromotionCodesBean> codes;


    public CmsBtPromotionGroupsBean() {
        super();
    }

    public CmsBtPromotionGroupsBean(CmsBtProductModel productInfo, CmsBtProductGroupModel groupModel,int promotionId, String operator) {
        this();
        // catPath
        this.setCatPath(productInfo.getCatPath());

        // ProductModel
        this.setProductModel(productInfo.getFields().getModel());
        // SynFlg
//        this.setSynFlg(false);

        this.setSynFlg("0");

        this.setPromotionId(promotionId);
        setOrgChannelId(productInfo.getOrgChannelId());

        this.setCreater(operator);

        this.setModifier(operator);
        if(groupModel !=  null){
            // numIid
            this.setNumIid(groupModel.getNumIId());
            // modelId
            this.setModelId(groupModel.getGroupId().intValue());
        }

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

    public List<CmsBtPromotionCodesBean> getCodes() {
        if (codes == null) {
            codes = new ArrayList<>();
        }
        return codes;
    }

    public void setCodes(List<CmsBtPromotionCodesBean> codes) {
        this.codes = codes;
    }

    public CmsBtPromotionCodesBean getProductByCode(String code) {
        for (CmsBtPromotionCodesBean item : codes) {
            if (item.getProductCode().equalsIgnoreCase(code)) {
                return item;
            }
        }
        return null;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

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
}
