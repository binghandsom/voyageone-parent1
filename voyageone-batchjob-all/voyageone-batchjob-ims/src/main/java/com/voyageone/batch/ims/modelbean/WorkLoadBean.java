package com.voyageone.batch.ims.modelbean;

import com.voyageone.batch.ims.bean.UpJobParamBean;
import com.voyageone.batch.ims.enums.PlatformWorkloadStatus;

/**
 * Created by Leo on 2015/5/26.
 */
public class WorkLoadBean implements Cloneable{
    private String order_channel_id;
    private int cart_id;
    private PlatformWorkloadStatus workload_status;
    private int modelId;
    private int groupId;
    private UpJobParamBean upJobParam;
    private String productId;
    private String numId;
    private CmsModelPropBean cmsModelProp;
    //主商品属性, 上新时被设置，上新结束回调时，写入ims_bt_product表的main_product_flg
    private String mainCode;
    private CmsCodePropBean mainProductProp;
    //库存更新时，要选择商品上传时是否有SKU属性
    private boolean hasSku;
    private int level;
    private String levelValue;
    private String failCause;
    private String style_code;
    private int isPublished;

    public WorkLoadBean() {}

    /*
    public WorkLoadBean(WorkLoadBean copyWorkload)
    {
        order_channel_id = copyWorkload.getOrder_channel_id();
        cart_id = copyWorkload.getCart_id();
        workload_status = copyWorkload.getWorkload_status().clone();
        modelId = copyWorkload.getModelId();
        groupId = copyWorkload.getGroupId();
        upJobParam = copyWorkload.getUpJobParam();
        productId = copyWorkload.getProductId();
        numId = copyWorkload.getNumId();
        cmsModelProp = copyWorkload.getCmsModelProp();
        level = copyWorkload.getLevel();
        levelValue = copyWorkload.getLevelValue();
        failCause = copyWorkload.getFailCause();
    }
    */

    public String getStyle_code() {
        return style_code;
    }

    public void setStyle_code(String style_code) {
        this.style_code = style_code;
    }

    public String getOrder_channel_id() {
        return order_channel_id;
    }

    public void setOrder_channel_id(String order_channel_id) {
        this.order_channel_id = order_channel_id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public PlatformWorkloadStatus getWorkload_status() {
        return workload_status;
    }

    public void setWorkload_status(PlatformWorkloadStatus platformWorkloadStatus) {
        this.workload_status = platformWorkloadStatus;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public CmsModelPropBean getCmsModelProp() {
        return cmsModelProp;
    }

    public void setCmsModelProp(CmsModelPropBean cmsModelProp) {
        this.cmsModelProp = cmsModelProp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLevelValue() {
        return levelValue;
    }

    public void setLevelValue(String levelValue) {
        this.levelValue = levelValue;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getNumId() {
        return numId;
    }

    public void setNumId(String numId) {
        this.numId = numId;
    }

    public UpJobParamBean getUpJobParam() {
        return upJobParam;
    }

    public void setUpJobParam(UpJobParamBean upJobParam) {
        this.upJobParam = upJobParam;
    }

    public String getFailCause() {
        return failCause;
    }

    public void setFailCause(String failCause) {
        this.failCause = failCause;
    }

    public int getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(int isPublished) {
        this.isPublished = isPublished;
    }

    @Override
    public String toString()
    {
        return "["+
                "order_channel_id:" + order_channel_id +
                ", cart_id:" + cart_id +
                ", model_id:" + modelId +
                ", group_id:" + groupId +
                ", style_code:" + style_code +
                ", workload_status:" + workload_status +
                ", num_id:" + numId +
                ", failCause:" + failCause +
                "]";
    }

    @Override
    public WorkLoadBean clone(){
        WorkLoadBean cloneObj = null;
        try {
            cloneObj = (WorkLoadBean) super.clone();
            cloneObj.setWorkload_status(this.getWorkload_status().clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cloneObj;
    }

    public boolean isHasSku() {
        return hasSku;
    }

    public void setHasSku(boolean hasSku) {
        this.hasSku = hasSku;
    }

    public String getMainCode() {
        return mainCode;
    }

    public void setMainCode(String mainCode) {
        this.mainCode = mainCode;
    }

    public CmsCodePropBean getMainProductProp() {
        return mainProductProp;
    }

    public void setMainProductProp(CmsCodePropBean mainProductProp) {
        this.mainProductProp = mainProductProp;
    }
}
