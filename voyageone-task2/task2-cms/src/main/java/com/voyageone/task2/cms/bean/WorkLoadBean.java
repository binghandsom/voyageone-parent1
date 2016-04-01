package com.voyageone.task2.cms.bean;

import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.cms.enums.PlatformWorkloadStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * Created by Leo on 2015/5/26.
 */
public class WorkLoadBean implements Cloneable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    //任务相关
    private String order_channel_id;
    private int cart_id;
    private String catId;
    private Long groupId;
    private UpJobParamBean upJobParam;
    private List<SxProductBean> processProducts;
    private SxProductBean mainProduct;
    private Map<CmsBtProductModel_Sku, SxProductBean> skuProductMap;
    private CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel;
    private CmsMtPlatformMappingModel cmsMtPlatformMappingModel;
    private Map<String, Integer> skuInventoryMap;

    private CmsBtSxWorkloadModel sxWorkloadModel;

    //任务状态
    private PlatformWorkloadStatus workload_status;

    //平台相关
    private String productId;
    private String numId;

    //库存更新时，要选择商品上传时是否有SKU属性
    private boolean hasSku;
    private String failCause;
    private String style_code;
    private int isPublished;
    //标志错误出现时，是否在任务下次运行时，该任务是否仍需要执行,
    //如果需要执行，那么publish_status会保持0，否则设为2
    private boolean nextProcess;

    public WorkLoadBean() {
        nextProcess = false;
    }

    /*
    public WorkLoadBean(WorkLoadBean copyWorkload)
    {
        order_channel_id = copyWorkload.getOrder_channel_id();
        cart_id = copyWorkload.getCart_id();
        workload_status = copyWorkload.getWorkload_status().clone();
        catId = copyWorkload.getCatId();
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

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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
                ", cat_id:" + catId +
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
            logger.error(e.getMessage(), e);
        }
        return cloneObj;
    }

    public CmsMtPlatformCategorySchemaModel getCmsMtPlatformCategorySchemaModel() {
        return cmsMtPlatformCategorySchemaModel;
    }

    public void setCmsMtPlatformCategorySchemaModel(CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel) {
        this.cmsMtPlatformCategorySchemaModel = cmsMtPlatformCategorySchemaModel;
    }

    public CmsMtPlatformMappingModel getCmsMtPlatformMappingModel() {
        return cmsMtPlatformMappingModel;
    }

    public void setCmsMtPlatformMappingModel(CmsMtPlatformMappingModel cmsMtPlatformMappingModel) {
        this.cmsMtPlatformMappingModel = cmsMtPlatformMappingModel;
    }

    public Map<String, Integer> getSkuInventoryMap() {
        return skuInventoryMap;
    }

    public void setSkuInventoryMap(Map<String, Integer> skuInventoryMap) {
        this.skuInventoryMap = skuInventoryMap;
    }

    public Map<CmsBtProductModel_Sku, SxProductBean> getSkuProductMap() {
        return skuProductMap;
    }

    public void setSkuProductMap(Map<CmsBtProductModel_Sku, SxProductBean> skuProductMap) {
        this.skuProductMap = skuProductMap;
    }

    public List<SxProductBean> getProcessProducts() {
        return processProducts;
    }

    public void setProcessProducts(List<SxProductBean> processProducts) {
        this.processProducts = processProducts;
    }

    public CmsBtSxWorkloadModel getSxWorkloadModel() {
        return sxWorkloadModel;
    }

    public void setSxWorkloadModel(CmsBtSxWorkloadModel sxWorkloadModel) {
        this.sxWorkloadModel = sxWorkloadModel;
    }

    public boolean isNextProcess() {
        return nextProcess;
    }

    public void setNextProcess(boolean nextProcess) {
        this.nextProcess = nextProcess;
    }

    public boolean isHasSku() {
        return hasSku;
    }

    public void setHasSku(boolean hasSku) {
        this.hasSku = hasSku;
    }

    public SxProductBean getMainProduct() {
        return mainProduct;
    }

    public void setMainProduct(SxProductBean mainProduct) {
        this.mainProduct = mainProduct;
    }
}
