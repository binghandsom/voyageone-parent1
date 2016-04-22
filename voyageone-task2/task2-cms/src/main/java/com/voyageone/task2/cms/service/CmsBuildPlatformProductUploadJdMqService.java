package com.voyageone.task2.cms.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MD5;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.service.bean.cms.product.ProductUpdateBean;
import com.voyageone.service.dao.cms.mongo.CmsMtCategorySchemaDao;
import com.voyageone.service.dao.cms.mongo.CmsMtCommonSchemaDao;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.PlatformProductUploadJdService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductSkuService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtCommonSchemaModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.task2.base.BaseMQTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.CmsConstants;
import com.voyageone.task2.cms.bean.SxProductBean;
import com.voyageone.task2.cms.bean.TmpOldCmsDataBean;
import com.voyageone.task2.cms.bean.UpJobParamBean;
import com.voyageone.task2.cms.bean.WorkLoadBean;
import com.voyageone.task2.cms.dao.TmpOldCmsDataDao;
import com.voyageone.task2.cms.enums.PlatformWorkloadStatus;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 京东平台产品上新服务
 * Product表中产品复存在就向京东平台新增商品，否则就更新商品
 *
 * @author desmond on 2016/4/12.
 * @version 2.0.0
 */
@Service
public class CmsBuildPlatformProductUploadJdMqService extends BaseMQTaskService {

    private static final String JOB_NAME = "CmsBuildPlatformProductUploadJdJob";

    @Autowired
    private PlatformProductUploadJdService jdProductUploadService;

    @Autowired
    private PlatformCategoryService platformCategoryService;

    @Autowired
    private TbProductService tbProductService;
    @Autowired
    private CmsMtCategorySchemaDao cmsMtCategorySchemaDao; // DAO: 主类目属性结构
    @Autowired
    private CmsMtCommonSchemaDao cmsMtCommonSchemaDao; // DAO: 共通属性结构
    @Autowired
    private TmpOldCmsDataDao tmpOldCmsDataDao; // DAO: 旧cms数据
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private ProductService productService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return JOB_NAME;
    }

    // workload和与它相关的SxWorkloadModel的对应关系
    private Map<WorkLoadBean, List<SxProductBean>> workLoadBeanListMap;

    // workload对象列表
    private Set<WorkLoadBean> workLoadBeans;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList, Map<String, Object> message) throws Exception {
        doMain(taskControlList);
    }

    /**
     * 京东平台上新处理
     *
     * @param taskControlList taskcontrol信息
     */
    public void doMain(List<TaskControlBean> taskControlList) throws Exception {

        // 获取该任务可以运行的销售渠道
        List<String> channelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 循环所有销售渠道
        if (channelIdList != null && channelIdList.size() > 0) {
            for (String channelId : channelIdList) {
                // 京东平台商品信息新增或更新
                doProductUploadJd(channelId, Integer.parseInt(CartEnums.Cart.JD.getId()));
                // 京东国际商品信息新增或更新
                doProductUploadJd(channelId, Integer.parseInt(CartEnums.Cart.JG.getId()));
            }
        }
    }

    /**
     * 京东平台产品每个平台的上新主处理
     *
     * @param channelId String 渠道ID
     */
    private void doProductUploadJd(String channelId, int cartId) {

        // 从上新的任务表中获取该平台及渠道需要上新的任务列表
        List<CmsBtSxWorkloadModel> sxWorkloadModels = jdProductUploadService.getSxWorkloadWithChannelIdCartId(CmsConstants.PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE, channelId, cartId);
        // 根据上新任务列表中的groupid循环上新处理
        for(CmsBtSxWorkloadModel cmsBtSxWorkloadModel:sxWorkloadModels) {
            long groupId = cmsBtSxWorkloadModel.getGroupId();

            // 主数据产品信息取得
//            List<CmsBtProductModel> jdProductBeanList = this.getProductsByGroupId(groupId);
            List<ProductUpdateBean> productUpdateBeanList = this.getProductsByGroupId(groupId);
//            xxxBean xxx;
            CmsBtProductModel mainProduct = new CmsBtProductLogModel();
            List<CmsBtProductModel> productList = new ArrayList<>();
            List<CmsBtProductModel_Sku> skuList = new ArrayList<>();

            // Sku排序(需要循环吗)
//          skuList = xxx(skuList);


            // 属性值准备
            // 取得mapping数据(3个参数channelId mainCategoryId platformCartId)
            // 可以取得platformCategoryId


            // 获取平台类目schema数据
            // 根据mapping表里面取得的platformCategoryId取得叶子节点的schema(包含XML)情报
//            CmsMtPlatformCategorySchemaModel comSchemaModel = platformCategoryService.getPlatformCatSchema(mainProduct.getCatPath(), cartId);
//            for (Field field : comSchemaModel.getFields()) {
//                schemaFieldList.add(field.getId());
//            }

            // 获取预设属性设定表
            // 未设计

            // 获取尺码表
            // 7. product.fields（mongodb， 共通字段略）增加三个字段
            //       * sizeGroupId        <-设置SKU信息
            //       * sizeChartIdPc      <-上传图片用
            //       * sizeChartIdMobile  <-上传图片用


            // 获取字典表(根据channel_id)
            // 上传图片的规格等信息


            // 编辑京东共通属性


            // 编辑类目属性值


            // 判断新增商品还是更新商品
            // 主product的platform的numIID是否为空，空为新增，非空为更新
            // 判断是新增商品还是更新商品
            if (!StringUtils.isEmpty(mainProduct.getGroups().getNumIId())) {
                // 更新商品
                // 调用京东商品更新API
            } else {
                // 新增商品
                // 调用京东商品增加API
            }

            // 回写product表(设置京东返回的商品ID  wareid)




            // 新增或更新SKU信息
            // 怎么判断SKU信息是新增还是更新
            // 价格要去配置表看一下用哪个价格



            // 调用京东图片空间API新增或者替换图片
            // 需要读字字典表和尺寸的sizeChartIdPc和sizeChartIdMobile的值


            // 回写workload表的状态
            // 成功的话把status：0->1 失败：0->2



        }

//        workLoadBeanListMap = new HashMap<>();
//        // 查询product表获取workload和与它相关的SxWorkloadModel的对应关系
//        workLoadBeanListMap = buildWorkloadMap(sxWorkloadModels);
//        // 获得workload对象列表
//        workLoadBeans = workLoadBeanListMap.keySet();
//
//        // 如果该平台要上新的产品存在
//        if (workLoadBeanListMap.isEmpty()) {
//            $info("没有找到需要上新的产品! CHANNEL_ID:" + channelId + ":CART_ID:" + cartId);
//        } else {
//            $debug("找到" + workLoadBeanListMap.size() + "件需要上新的产品!");
//        }

//        workloadDispatcher.dispatchAndRun(workLoadBeans, this);
//        $info("=================Main Thread is termined===========================");
//        return null;

        // 获取cms_tmp_old_cms_data的数据 ==========================================================================================
        List<TmpOldCmsDataBean> oldCmsDataBeenList = tmpOldCmsDataDao.getList();

        // 遍历, 并设置主数据
        for (TmpOldCmsDataBean oldCmsDataBean : oldCmsDataBeenList) {
//            doSetProduct(oldCmsDataBean);
        }
    }

    // temp function for compile
    private List<ProductUpdateBean> getProductsByGroupId(long productId) {
        return new ArrayList<ProductUpdateBean>();
    }
    // temp function for compile

    /**
     * 构造workload对象，并维护workload和SxWorkloadModel的对应关系
     * @param sxWorkloadModels List<CmsBtSxWorkloadModel> 需要上新的任务列表
     * @return workload和与它相关的SxWorkloadModel的对应关系
     */
    private Map<WorkLoadBean, List<SxProductBean>> buildWorkloadMap
    (List<CmsBtSxWorkloadModel> sxWorkloadModels) {

        Map<WorkLoadBean, List<SxProductBean>> workloadBeanListMap = new HashMap<>();
        for (CmsBtSxWorkloadModel sxWorkloadModel : sxWorkloadModels)
        {
            WorkLoadBean workload = new WorkLoadBean();

            workload.setSxWorkloadModel(sxWorkloadModel);

            Long groupId = sxWorkloadModel.getGroupId();
            String channelId = sxWorkloadModel.getChannelId();

            workload.setOrder_channel_id(channelId);
            workload.setGroupId(groupId);

            // 根据GroupId获取商品List
            List<CmsBtProductModel> cmsBtProductModels = productService.getProductByGroupId(channelId, groupId, false);
            List<SxProductBean> sxProductBeans = new ArrayList<>();
            CmsBtProductModel mainProductModel = null;
            CmsBtProductGroupModel mainProductPlatform = null;
            SxProductBean mainSxProduct = null;

            for (CmsBtProductModel cmsBtProductModel : cmsBtProductModels) {
                CmsBtProductGroupModel productPlatform = cmsBtProductModel.getGroups();
                SxProductBean sxProductBean = new SxProductBean(cmsBtProductModel, productPlatform, null);
                // 判断sxProductBean中是否含有要在该平台中上新的sku(有要上新的返回true，没有返回false)
                if (filtProductsByPlatform(sxProductBean)) {
                    sxProductBeans.add(sxProductBean);
                    // 设置该要上新商品的主平台
                    if (productPlatform.getIsMain()) {
                        mainProductModel = cmsBtProductModel;
                        mainProductPlatform = productPlatform;
                        mainSxProduct = sxProductBean;
                    }
                }
            }
            // 主商品
            workload.setMainProduct(mainSxProduct);

            if (mainSxProduct != null) {
                // 平台ID
                workload.setCart_id(mainProductPlatform.getCartId());
                // 类目ID
                workload.setCatId(mainProductModel.getCatId());

                UpJobParamBean upJobParam = new UpJobParamBean();
                upJobParam.setForceAdd(false);
                workload.setUpJobParam(upJobParam);

                // 判断是新增商品还是更新商品
                if (!StringUtils.isEmpty(mainProductPlatform.getNumIId())) {
                    // 更新商品
                    workload.setIsPublished(1);
                    workload.setNumId(mainProductPlatform.getNumIId());
                    upJobParam.setMethod(UpJobParamBean.METHOD_UPDATE);
                } else {
                    // 新增商品
                    workload.setIsPublished(0);
                    upJobParam.setMethod(UpJobParamBean.METHOD_ADD);
                }
                // 商品ID
                workload.setProductId(mainProductPlatform.getProductId());
            }
            // WorkLoad状态(0:JOB_INIT)
            workload.setWorkload_status(new PlatformWorkloadStatus(PlatformWorkloadStatus.JOB_INIT));

            workload.setHasSku(false);
            workloadBeanListMap.put(workload, sxProductBeans);
            workload.setProcessProducts(sxProductBeans);
        }
        return workloadBeanListMap;
    }

    /**
     * 如果sxProductBean中含有要在该平台中上新的sku, 返回true
     * 如果没有sku要上新，那么返回false
     */
    private boolean filtProductsByPlatform(SxProductBean sxProductBean) {
        CmsBtProductModel cmsBtProductModel = sxProductBean.getCmsBtProductModel();
        CmsBtProductGroupModel cmsBtProductModelGroupPlatform = sxProductBean.getCmsBtProductModelGroupPlatform();
        List<CmsBtProductModel_Sku> cmsBtProductModelSkus = cmsBtProductModel.getSkus();
        int cartId = cmsBtProductModelGroupPlatform.getCartId();

        if (cmsBtProductModelSkus == null) {
            return false;
        }

        for (Iterator<CmsBtProductModel_Sku> productSkuIterator = cmsBtProductModelSkus.iterator(); productSkuIterator.hasNext();) {
            CmsBtProductModel_Sku cmsBtProductModel_sku = productSkuIterator.next();
            if (!cmsBtProductModel_sku.isIncludeCart(cartId)) {
                productSkuIterator.remove();
            }
        }

        return !cmsBtProductModelSkus.isEmpty();
    }
























    private void doSetProduct(TmpOldCmsDataBean oldCmsDataBean) throws Exception {
        ShopBean shopBean = Shops.getShop(oldCmsDataBean.getChannel_id(), oldCmsDataBean.getCart_id());
        if (shopBean == null) {
            // 不存在的shop, 跳过
            return;
        }

        // 属性名字列表
        List<String> schemaFieldList = new ArrayList<>();
        List<String> schemaFieldSkuList = new ArrayList<>(); // sku级

        // 获取共通schema数据 ==========================================================================================
        CmsMtCommonSchemaModel comSchemaModel = getComSchemaModel();
        for (Field field : comSchemaModel.getFields()) {
            schemaFieldList.add(field.getId());
        }

        // 获取主数据当前类目的schema数据 ==========================================================================================
        CmsMtCategorySchemaModel schemaModel = cmsMtCategorySchemaDao.getMasterSchemaModelByCatId(MD5.getMD5(oldCmsDataBean.getCategory_path()));
        if (schemaModel == null) {
            // 指定的category在主数据里没有的话, 跳过
            return;
        }
        for (Field field : schemaModel.getFields()) {
            schemaFieldList.add(field.getId());
        }
        MultiComplexField multiComplexField = (MultiComplexField)schemaModel.getSku();
        for (Field field : multiComplexField.getFields()) {
            schemaFieldSkuList.add(field.getId());
        }

        // 获取product表的数据 ==========================================================================================
        CmsBtProductModel cmsProduct = getCmsProduct(oldCmsDataBean.getChannel_id(), oldCmsDataBean.getCode());
        if (cmsProduct == null) {
            // product表里没这个code的场合, 跳过当前记录
            return;
        }

        // 获取天猫上的数据 ==========================================================================================
        Map<String, Object> fieldMap = new HashMap<>();
        if (PlatFormEnums.PlatForm.TM.getId().equals(shopBean.getPlatform_id())) {
            // 只有天猫系才会更新fields字段
            fieldMap.putAll(getPlatformProduct(oldCmsDataBean.getProduct_id(), shopBean));
            fieldMap.putAll(getPlatformWareInfoItem(oldCmsDataBean.getNum_iid(), shopBean));
        }

        // 保存到product表 ==========================================================================================
        update2ProductFields(oldCmsDataBean, fieldMap, cmsProduct, schemaFieldList, schemaFieldSkuList);

    }

    /**
     * 获取common schema.
     * @return
     */
    private CmsMtCommonSchemaModel getComSchemaModel() {
        CmsMtCommonSchemaModel comSchemaModel = cmsMtCommonSchemaDao.getComSchema();

        if (comSchemaModel == null){

            //common schema 不存在时异常处理.
            String errMsg = "共通schema（cms_mt_common_schema）的信息不存在！";

            $error(errMsg);

            throw new BusinessException(errMsg);
        }

        return comSchemaModel;
    }

    private CmsBtProductModel getCmsProduct(String channelId, String code) {
        return productService.getProductByCode(channelId, code);
    }

    private Map<String, Object> getPlatformProduct(String productId, ShopBean shopBean) throws Exception {
        String schema = tbProductService.getProductSchema(Long.parseLong(productId), shopBean);

        List<Field> fields = SchemaReader.readXmlForList(schema);

        fieldHashMap fieldMap = new fieldHashMap();
        fields.forEach(field -> {
            fields2Map(field, fieldMap);
        });
        return fieldMap;

    }

    private Map<String, Object> getPlatformWareInfoItem(String numIid, ShopBean shopBean) throws Exception {
        String schema = tbProductService.doGetWareInfoItem(numIid, shopBean).getUpdateItemResult();
        List<Field> fields = SchemaReader.readXmlForList(schema);

        fieldHashMap fieldMap = new fieldHashMap();
        fields.forEach(field -> {
            fields2Map(field, fieldMap);
        });

        return fieldMap;

    }

    private void update2ProductFields(
            TmpOldCmsDataBean oldCmsDataBean,
            Map<String, Object> fields,
            CmsBtProductModel cmsProduct,
            List<String> schemaFieldList,
            List<String> schemaFieldSkuList) {

        ShopBean shopBean = Shops.getShop(oldCmsDataBean.getChannel_id(), oldCmsDataBean.getCart_id());
        if (shopBean == null) {
            return;
        }
        if (PlatFormEnums.PlatForm.TM.getId().equals(shopBean.getPlatform_id())) {
            // 只有天猫系才会更新fields字段

            CmsBtProductModel_Field cmsFields = new CmsBtProductModel_Field();
            List<CmsBtProductModel_Sku> skuList = new ArrayList<>();

            // 天猫取得的字段设定 ==========================================================================================
            for (String key : fields.keySet()) {
                // 看看schema里是否存在
                if (!schemaFieldList.contains(key) && !"sku".equals(key)) {
                    // schema里没有的字段, 无需设置
                    continue;
                }

                // 看看是否是属于不想设置的字段
                if ("brand".equals(key)
                        || "code".equals(key)
                        || "model".equals(key)
                        || "priceMsrpEd".equals(key)
                        || "priceMsrpSt".equals(key)
                        || "priceRetailEd".equals(key)
                        || "priceRetailSt".equals(key)
                        || "productNameEn".equals(key)
                        || "status".equals(key)
                        ) {
                    // 不想设置的字段, 就跳过
                    continue;
                }

                // 设定
                if (schemaFieldList.contains(key)) {
                    // 商品信息
                    cmsFields.setAttribute(key, fields.get(key));
                } else if ("sku".equals(key)) {
                    // sku级别信息
                    List<Map<String, Object>> tmallSkuList = (List<Map<String, Object>>)fields.get(key);
                    for (Map<String, Object> tmallSku : tmallSkuList) {

                        CmsBtProductModel_Sku sku = new CmsBtProductModel_Sku();

                        tmallSku.forEach((k,v)->{
                            // 去除不想设置的字段
                            if ("sku_outerId".equals(k)
                                    // 天猫上拉下来的字段
                                    || "sku_price".equals(k)
                                    || "sku_id".equals(k)
                                    || "sku_quantity".equals(k)
                                    || "sku_barcode".equals(k)

                                    // 万一有遇到与主数据的字段名称一样的, 那也不需要更新
                                    || "skuCode".equals(k)
                                    || "size".equals(k)
                                    || "qty".equals(k)
                                    || "priceMsrp".equals(k)
                                    || "priceRetail".equals(k)
                                    || "priceSale".equals(k)
                                    || "skuCarts".equals(k)
                                    || "barcode".equals(k)

                                    ) {
                                // 不想设置的字段, 就跳过

                            } else {
                                // 看看schema里是否存在
                                if (!schemaFieldSkuList.contains(k)) {
                                    // schema里没有的字段, 无需设置
                                } else {
                                    // 设定
                                    cmsProduct.getSkus().forEach((item)->{
                                        if (item.getSkuCode().equals(tmallSku.get("sku_outerId"))) {
                                            item.put(k, v);
                                        }
                                    });
                                }

                            }

                        });

                    }

                    System.out.println(oldCmsDataBean.getCode() + ":" + key + ":" + fields.get(key));
                }
            }

            // 固定字段设定 ==========================================================================================
            // product状态: 因为已经上了第三方平台, 所以默认设置为Approved
//            cmsFields.setStatus(CmsConstants.ProductStatus.Approved);
            cmsFields.setStatus("Approved");
            // 货号
            cmsFields.setAttribute("prop_13021751", oldCmsDataBean.getModel());
            // 英文标题
            cmsFields.setProductNameEn(oldCmsDataBean.getTitle_en());
            // 中文标题
            cmsFields.setOriginalTitleCn(oldCmsDataBean.getTitle_cn());
            cmsFields.setLongTitle(oldCmsDataBean.getTitle_cn());
            // 英文描述
            cmsFields.setLongDesEn(oldCmsDataBean.getDescription_en());
            // 中文描述
            cmsFields.setOriginalDesCn(oldCmsDataBean.getDescription_cn()); // 原本的长描述, 扔到原始中文描述里
            cmsFields.setLongDesCn(oldCmsDataBean.getDescription_cn_short()); // 原本的短描述, 扔到中文长描述里
            // 图片1
            List<String> imgListString = oldCmsDataBean.getImageList(oldCmsDataBean.getImg1());
            List<CmsBtProductModel_Field_Image> imgList1 = new ArrayList<>();
            imgListString.forEach(img->imgList1.add(new CmsBtProductModel_Field_Image("image1", img)));
            cmsFields.setImages1(imgList1);
            // 图片2
            imgListString = oldCmsDataBean.getImageList(oldCmsDataBean.getImg2());
            List<CmsBtProductModel_Field_Image> imgList2 = new ArrayList<>();
            imgListString.forEach(img->imgList2.add(new CmsBtProductModel_Field_Image("image2", img)));
            cmsFields.setImages2(imgList2);
            // 图片3
            imgListString = oldCmsDataBean.getImageList(oldCmsDataBean.getImg3());
            List<CmsBtProductModel_Field_Image> imgList3 = new ArrayList<>();
            imgListString.forEach(img->imgList3.add(new CmsBtProductModel_Field_Image("image3", img)));
            cmsFields.setImages3(imgList3);
            // 图片4
            imgListString = oldCmsDataBean.getImageList(oldCmsDataBean.getImg4());
            List<CmsBtProductModel_Field_Image> imgList4 = new ArrayList<>();
            imgListString.forEach(img->imgList4.add(new CmsBtProductModel_Field_Image("image4", img)));
            cmsFields.setImages4(imgList4);
            // 英文颜色
            cmsFields.setColor(oldCmsDataBean.getColor_en());
            // hs_code_pu 个人行邮税号
            cmsFields.setHsCodePrivate(oldCmsDataBean.getHs_code_pu());
            // 是否已翻译
            cmsFields.setTranslateStatus(String.valueOf(oldCmsDataBean.getTranslate_status()));
            cmsFields.setTranslator(getTaskName());
            cmsFields.setTranslateTime(DateTimeUtil.getNow());

            // product ==========================================================================================
            cmsProduct.setFields(cmsFields);
            cmsProduct.setCatPath(oldCmsDataBean.getCategory_path());
            cmsProduct.setCatId(MD5.getMD5(oldCmsDataBean.getCategory_path()));

        }

        // 设置platform信息
        Map platform = new HashMap();
        platform.put("numIid", oldCmsDataBean.getNum_iid());
        platform.put("productId", oldCmsDataBean.getProduct_id());

        String status = fields.get("item_status").toString();
        switch (status) {
            case "0": // 出售中
                platform.put("platformStatus", com.voyageone.common.CmsConstants.PlatformStatus.Onsale.name());
                platform.put("platformActive", com.voyageone.common.CmsConstants.PlatformActive.Onsale.name());
                break;
            default: // 定时上架 或者 仓库中
                platform.put("platformStatus", com.voyageone.common.CmsConstants.PlatformStatus.Instock.name());
                platform.put("platformActive", com.voyageone.common.CmsConstants.PlatformActive.Instock.name());
        }

        // 更新group
        productGroupService.saveGroups(oldCmsDataBean.getChannel_id(), cmsProduct.getFields().getCode(), Integer.parseInt(oldCmsDataBean.getCart_id()), platform);
        $info(String.format("从天猫获取product数据到cms:group:[code:%s]", oldCmsDataBean.getCode()));

        // 设置sku信息
        List<CmsBtProductModel_Sku> skus = cmsProduct.getSkus();
        for (CmsBtProductModel_Sku sku : skus) {
            sku.setPriceSale(oldCmsDataBean.getPrice_sale());
        }
        productSkuService.saveSkus(oldCmsDataBean.getChannel_id(), cmsProduct.getProdId(), skus);

        // 提交到product表 ==========================================================================================
        ProductUpdateBean productUpdateBean = new ProductUpdateBean();
        productUpdateBean.setProductModel(cmsProduct);
        productUpdateBean.setModifier(getTaskName());
        productUpdateBean.setIsCheckModifed(false); // 不做最新修改时间ｃｈｅｃｋ

        productService.updateProduct(oldCmsDataBean.getChannel_id(), productUpdateBean);

        // 更新cms_tmp_old_cms_data表
        tmpOldCmsDataDao.setFinish(oldCmsDataBean.getChannel_id(), oldCmsDataBean.getCart_id(), oldCmsDataBean.getCode());

        $info(String.format("从天猫获取product数据到cms:[code:%s]", oldCmsDataBean.getCode()));

        System.out.println("ok");
    }

    private void fields2Map(Field field, fieldHashMap fieldMap) {

        switch (field.getType()) {
            case INPUT:
                InputField inputField = (InputField) field;
                fieldMap.put(inputField.getId(), inputField.getDefaultValue());
                break;
            case MULTIINPUT:
                MultiInputField multiInputField = (MultiInputField) field;
                fieldMap.put(multiInputField.getId(), multiInputField.getDefaultValues());
                break;
            case LABEL:
                return;
            case SINGLECHECK:
                SingleCheckField singleCheckField = (SingleCheckField) field;
                fieldMap.put(singleCheckField.getId(), singleCheckField.getDefaultValue());
                break;
            case MULTICHECK:
                MultiCheckField multiCheckField = (MultiCheckField) field;
                fieldMap.put(multiCheckField.getId(), multiCheckField.getDefaultValues());
                break;
            case COMPLEX:
                ComplexField complexField = (ComplexField) field;
                Map<String,Object> values = new HashMap<>();
                if (complexField.getDefaultComplexValue() != null) {
                    for(String fieldId : complexField.getDefaultComplexValue().getFieldKeySet()){
                        values.put(fieldId, getFieldValue(complexField.getDefaultComplexValue().getValueField(fieldId)));
                    }
                }
                fieldMap.put(field.getId(), values);
                break;
            case MULTICOMPLEX:
                MultiComplexField multiComplexField = (MultiComplexField) field;
                List<Map<String, Object>> multiComplexValues = new ArrayList<>();
                if (multiComplexField.getDefaultComplexValues() != null) {
                    for(ComplexValue item : multiComplexField.getDefaultComplexValues()){
                        Map<String, Object> obj = new HashMap<>();
                        for(String fieldId : item.getFieldKeySet()){
                            obj.put(fieldId, getFieldValue(item.getValueField(fieldId)));
                        }
                        multiComplexValues.add(obj);
                    }
                }
                fieldMap.put(multiComplexField.getId(), multiComplexValues);
                break;
        }
    }

    private Object getFieldValue(Field field) {
        List<String> values;
        switch (field.getType()) {
            case INPUT:
                InputField inputField = (InputField) field;
                return inputField.getValue();

            case MULTIINPUT:
                MultiInputField multiInputField = (MultiInputField) field;
                values = new ArrayList<>();
                multiInputField.getValues().forEach(value -> values.add(value.getValue()));
                return values;

            case SINGLECHECK:
                SingleCheckField singleCheckField = (SingleCheckField) field;
                return singleCheckField.getValue().getValue();

            case MULTICHECK:
                MultiCheckField multiCheckField = (MultiCheckField) field;
                values = new ArrayList<>();
                multiCheckField.getValues().forEach(value -> values.add(value.getValue()));
                return values;

            case COMPLEX:
                ComplexField complexField = (ComplexField) field;
                Map<String, Field> fieldMap = complexField.getFieldMap();
                Map<String,Object> complexValues = new HashMap<>();
                for (String key : fieldMap.keySet()) {
                    complexValues.put(key, getFieldValue(fieldMap.get(key)));
                }
                return complexValues;

            case MULTICOMPLEX:
                MultiComplexField multiComplexField = (MultiComplexField) field;
                List<Object> multiComplexValues = new ArrayList<>();
                if (multiComplexField.getFieldMap() != null) {
                    for(ComplexValue item : multiComplexField.getComplexValues()){
                        for(String fieldId : item.getFieldKeySet()){
                            multiComplexValues.add(getFieldValue(item.getValueField(fieldId)));
                        }
                    }
                }
                return multiComplexValues;
        }

        return null;
    }

    class fieldHashMap extends HashMap<String,Object>{
        @Override
        public Object put(String key, Object value){
            if(value == null){
                return value;
            }
            return  super.put(StringUtils.replaceDot(key),value);
        }
    }
}
