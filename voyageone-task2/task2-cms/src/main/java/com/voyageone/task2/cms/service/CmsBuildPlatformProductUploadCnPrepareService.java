package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.cn.service.CnSchemaService;
import com.voyageone.ims.rule_expression.MasterWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.mongo.CmsBtSxCnInfoDao;
import com.voyageone.service.dao.wms.WmsBtInventoryCenterLogicDao;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.sx.CnImageService;
import com.voyageone.service.impl.cms.sx.ConditionPropValueService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.CmsMtChannelConditionConfigModel;
import com.voyageone.service.model.cms.mongo.CmsBtSxCnInfoModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.service.model.wms.WmsBtInventoryCenterLogicModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 独立域名产品上新xml准备
 *
 * @author morse on 2016/8/23
 * @version 2.5.0
 */
@Service
public class CmsBuildPlatformProductUploadCnPrepareService extends BaseTaskService {

    @Autowired
    private PlatformProductUploadService platformProductUploadService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private PlatformCategoryService platformCategoryService;
    @Autowired
    private ConditionPropValueService conditionPropValueService;
    @Autowired
    private CnSchemaService cnSchemaService;
    @Autowired
    private CnImageService cnImageService;

    @Autowired
    private WmsBtInventoryCenterLogicDao wmsBtInventoryCenterLogicDao;
    @Autowired
    private CmsBtSxCnInfoDao cmsBtSxCnInfoDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformProductUploadCnJob";
    }

    public String getTaskNameForUpdate() {
        return "CmsBuildPlatformProductUploadCnPrepareJob";
    }

    /**
     * 独立域名上新处理
     *
     * @param taskControlList taskcontrol信息
     */
    @Override
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 获取该任务可以运行的销售渠道
        List<String> channelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 初始化cms_mt_channel_condition_config表的条件表达式(避免多线程时2次初始化)
        conditionPropValueService.init();

        // 循环所有销售渠道
        if (channelIdList != null && channelIdList.size() > 0) {
            for (String channelId : channelIdList) {
                // 独立域名商品信息新增或更新
                doUpload(channelId, Integer.parseInt(CartEnums.Cart.CN.getId()));
            }
        }

        // 正常结束
        $info("正常结束");
    }

    /**
     * 平台产品上新主处理
     *
     * @param channelId String 渠道ID
     * @param cartId    String 平台ID
     */
    private void doUpload(String channelId, int cartId) throws Exception {
        // 默认线程池最大线程数
        int threadPoolCnt = 5;

        // 获取店铺信息
        ShopBean shopProp = Shops.getShop(channelId, cartId);
        if (shopProp == null) {
            $error("获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }
        $info("获取店铺信息成功![ChannelId:%s] [CartId:%s]", channelId, cartId);

        // 从上新的任务表中获取该平台及渠道需要上新的任务列表(group by channel_id, cart_id, group_id)
        List<CmsBtSxWorkloadModel> sxWorkloadModels = platformProductUploadService.getSxWorkloadWithChannelIdCartId(
                CmsConstants.PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE, channelId, cartId);
        if (ListUtils.isNull(sxWorkloadModels)) {
            $error("上新任务表中没有该渠道和平台对应的任务列表信息！[ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolCnt);
        // 根据上新任务列表中的groupid循环上新处理
        for (CmsBtSxWorkloadModel cmsBtSxWorkloadModel : sxWorkloadModels) {
            // 启动多线程
            executor.execute(() -> uploadExecute(cmsBtSxWorkloadModel, shopProp));
        }
        // ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成(已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
        // 当所有已经提交的任务执行完毕后将会关闭ExecutorService。
        executor.shutdown(); // 并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        try {
            // 阻塞，直到线程池里所有任务结束
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * 平台产品上新处理
     *
     * @param cmsBtSxWorkloadModel CmsBtSxWorkloadModel WorkLoad信息
     * @param shopBean             ShopBean 店铺信息
     */
    public void uploadExecute(CmsBtSxWorkloadModel cmsBtSxWorkloadModel, ShopBean shopBean) {
        // 当前groupid
        long groupId = cmsBtSxWorkloadModel.getGroupId();
        // 渠道id
        String channelId = cmsBtSxWorkloadModel.getChannelId();
        // 平台id
        int cartId = cmsBtSxWorkloadModel.getCartId();
        // 上新数据
        SxData sxData = null;

        try {
            // 上新用的商品数据信息取得
            sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);
            if (sxData == null) {
                throw new BusinessException("SxData取得失败!");
            }
            if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
                // 取得上新数据出错时，cartId有可能没有设置
                sxData.setCartId(cartId);
                // 有错误的时候，直接报错
                throw new BusinessException(sxData.getErrorMessage());
            }

            CmsConstants.PlatformActive platformActive = sxData.getPlatform().getPlatformActive();
            if (platformActive == CmsConstants.PlatformActive.ToInStock && StringUtils.isEmpty(sxData.getPlatform().getNumIId())) {
                // ToInStock 删除，但numIId = null(没上过)，就没必要推送了
                return;
            }

            // 平台类目schema信息
            CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = platformCategoryService.getPlatformCatSchema("1", cartId);
            if (cmsMtPlatformCategorySchemaModel == null) {
                String errMsg = String.format("获取平台类目schema信息失败！[PlatformCategoryId:%s] [CartId:%s]", "0", cartId);
                $error(errMsg);
                throw new BusinessException(errMsg);
            }

            sxData.setHasSku(true); // 独立域名都有sku

            // 上传code信息
            String productXml = uploadProduct(sxData, cmsMtPlatformCategorySchemaModel, shopBean, getTaskNameForUpdate());

            // 上传sku信息
            String skuXml = null;
            if (platformActive == CmsConstants.PlatformActive.ToOnSale) {
                skuXml = uploadItem(sxData, cmsMtPlatformCategorySchemaModel);
            }

            // 更新cms_bt_sx_cn_info_cXXX
            updateCmsBtSxCnInfo(groupId, channelId, cartId, sxData, productXml, skuXml, cmsBtSxWorkloadModel.getId());

            // 回写workload表   (5等待xml上传)
            sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, CmsConstants.SxWorkloadPublishStatusNum.waitCnUpload, getTaskNameForUpdate());
            $info("groupId[%s] success!", groupId);
        } catch (Exception ex) {
            // 取得sxData为空
            if (sxData == null) {
                sxData = new SxData();
                sxData.setChannelId(channelId);
                sxData.setCartId(cartId);
                sxData.setGroupId(groupId);
                sxData.setErrorMessage(String.format("取得上新用的商品数据信息失败！[ChannelId:%s] [GroupId:%s]", channelId, groupId));
            }

            // 上传产品失败，后面商品也不用上传，直接回写workload表   (失败2)
            if (ex instanceof BusinessException) {
                $error(ex.getMessage());
                sxData.setErrorMessage(ex.getMessage());
            } else {
                String errMsg = String.format("产品匹配或上传产品时异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [%s]",
                        channelId, cartId, groupId, ex.getMessage());
                $error(errMsg);
                ex.printStackTrace();
                sxData.setErrorMessage(errMsg);
            }
            // 回写workload表   (失败2)
            sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, CmsConstants.SxWorkloadPublishStatusNum.errorNum, getTaskNameForUpdate());
            // 回写详细错误信息表(cms_bt_business_log)
            sxProductService.insertBusinessLog(sxData, getTaskNameForUpdate());
            return;
        }

    }

    private void updateCmsBtSxCnInfo(long groupId, String channelId, int cartId, SxData sxData, String productXml, String skuXml, int sxWorkloadId) {
        CmsBtSxCnInfoModel findCnInfoModel;
        findCnInfoModel = cmsBtSxCnInfoDao.selectInfoByGroupId(channelId, groupId, 0);
        if (findCnInfoModel != null) {
            // 有未post的数据，状态更新掉(4:上传终止)，不要重复post
            cmsBtSxCnInfoDao.updatePublishFlg(channelId, new ArrayList<Long>(){{this.add(groupId);}}, 4, getTaskNameForUpdate(), 0);
        } else {
            findCnInfoModel = cmsBtSxCnInfoDao.selectInfoByGroupId(channelId, groupId, 1);
            if (findCnInfoModel != null) {
                // 有正在post的数据
                throw new BusinessException("前一回上传处理未结束,请稍后再试!");
            }
        }

        CmsBtSxCnInfoModel insertCnInfoModel = new CmsBtSxCnInfoModel();
        insertCnInfoModel.setChannelId(channelId);
        insertCnInfoModel.setOrgChannelId(sxData.getMainProduct().getOrgChannelId());
        insertCnInfoModel.setCartId(cartId);
        insertCnInfoModel.setGroupId(groupId);
        List<String> catIds = new ArrayList<>();
        for (CmsBtProductModel_SellerCat sellerCat : sxData.getMainProduct().getPlatform(cartId).getSellerCats()) {
            catIds.addAll(sellerCat.getcIds());
        }
        insertCnInfoModel.setCatIds(catIds);
        insertCnInfoModel.setCode(sxData.getMainProduct().getCommon().getFields().getCode());
        insertCnInfoModel.setProdId(sxData.getMainProduct().getProdId());
        insertCnInfoModel.setProductXml(productXml);
        insertCnInfoModel.setSkuXml(skuXml);
        insertCnInfoModel.setPublishFlg(0);
        insertCnInfoModel.setPlatformActive(sxData.getPlatform().getPlatformActive());
        insertCnInfoModel.setSxWorkloadId(sxWorkloadId);
        insertCnInfoModel.setCreater(getTaskNameForUpdate());
        insertCnInfoModel.setModifier(getTaskNameForUpdate());
        cmsBtSxCnInfoDao.insert(insertCnInfoModel);
    }

    /**
     * 上传产品
     *
     * @param sxData SxData
     * @param cmsMtPlatformCategorySchemaModel MongoDB  propsProduct取得用
     * @param shopBean ShopBean 店铺信息
     * @param modifier 更新者
     */
    private String uploadProduct(SxData sxData, CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel, ShopBean shopBean, String modifier) {
        // 产品schema
        String productSchema = cmsMtPlatformCategorySchemaModel.getPropsProduct();
        // 所有产品
        List<List<Field>> listProduct;

        // 根据field列表取得属性值
        try {
            // 取得所有field对应的属性值
            listProduct = constructProductPlatformProps(sxData, productSchema, shopBean, modifier);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            String errMsg = String.format("产品数据设值失败！[ChannelId:%s] [CartId:%s] [GroupId:%s]",
                    sxData.getChannelId(), sxData.getCartId(), sxData.getGroupId());
            ex.printStackTrace();
            throw new BusinessException(errMsg);
        }

        // 匹配之后的XML格式数据
        String xml = cnSchemaService.writeProductXmlString(listProduct);
        $debug("product xml:" + xml);

        return xml;
    }

    /**
     * 上传sku
     *
     * @param sxData SxData
     * @param cmsMtPlatformCategorySchemaModel MongoDB  propsProduct取得用
     */
    private String uploadItem(SxData sxData, CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel) {
        // sku schema
        String itemSchema = cmsMtPlatformCategorySchemaModel.getPropsItem();
        // 所有sku
        List<List<Field>> listSku;

        // 根据field列表取得属性值
        try {
            // 取得所有field对应的属性值
            listSku = constructSkuPlatformProps(sxData, itemSchema);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            String errMsg = String.format("Sku数据设值失败！[ChannelId:%s] [CartId:%s] [GroupId:%s]",
                    sxData.getChannelId(), sxData.getCartId(), sxData.getGroupId());
            ex.printStackTrace();
            throw new BusinessException(errMsg);
        }

        // 匹配之后的XML格式数据
        String xml = cnSchemaService.writeSkuXmlString(listSku);
        $debug("sku xml:" + xml);

        return xml;
    }

    /**
     * 产品上新schema设值
     *
     * @param sxData SxData
     * @param productSchema 产品schema
     * @param shopBean ShopBean
     * @param modifier user
     * @throws Exception
     */
    private List<List<Field>> constructProductPlatformProps(SxData sxData, String productSchema, ShopBean shopBean, String modifier) throws Exception {
        List<List<Field>> listProduct = new ArrayList<>();

        for (CmsBtProductModel product : sxData.getProductList()) {
            // 循环group下所有code
            // 表达式解析子
            sxData.setMainProduct(product);
            ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);

            List<Field> fieldList;
            try {
                // 将取得的产品Schema xml转换为List<Field>
                fieldList = SchemaReader.readXmlForList(productSchema);
            } catch (TopSchemaException ex) {
                // 解析XML异常
                String errMsg = String.format("将取得的产品Schema xml转换为field列表失败(解析XML异常)！[ChannelId:%s] [CartId:%s] [GroupId:%s]",
                        sxData.getChannelId(), sxData.getCartId(), sxData.getGroupId());
                ex.printStackTrace();
                throw new BusinessException(errMsg);
            }

            // 按各个产品设值
            constructEachProductPlatformProps(fieldList, product, shopBean, expressionParser, modifier);
            listProduct.add(fieldList);
        }

        return listProduct;
    }

    /**
     * 上新设值(code级)
     *
     * @param fields List<Field> 直接把值set进这个fields对象
     * @param product 产品CmsBtProductModel
     * @param shopBean ShopBean
     * @param expressionParser ExpressionParser
     * @param modifier user
     * @throws Exception
     */
    private void constructEachProductPlatformProps(List<Field> fields, CmsBtProductModel product, ShopBean shopBean, ExpressionParser expressionParser, String modifier) throws Exception {
        SxData sxData = expressionParser.getSxData();
        List<String> listSp = new ArrayList<>(); // 特殊处理的field_id
        listSp.add("Id");

        // 一些特殊处理的属性
        constructProductCustomPlatformProps(fields, listSp, product, expressionParser, shopBean, modifier);

        for (Field field : fields) {
            if (!listSp.contains(field.getId())) {
                // 特殊字段以外的,直接取product表的fields的值
                sxProductService.resolveMappingFromProductField(field, shopBean, expressionParser, modifier);
            }
        }
    }

    private List<List<Field>> constructSkuPlatformProps(SxData sxData, String itemSchema) throws Exception {
        List<List<Field>> listSku = new ArrayList<>();

        // 所有sku取得
        Map<String, List<String>> mapChannelSkus = new HashMap<>(); // Map<Channel, skus>
        for (CmsBtProductModel productModel : sxData.getProductList()) {
            String channelId = productModel.getOrgChannelId();
            List<String> skus = mapChannelSkus.get(channelId);
            if (skus == null) {
                skus = new ArrayList<>();
                mapChannelSkus.put(channelId, skus);
            }
            skus.addAll(productModel.getCommon().getSkus().stream().map(CmsBtProductModel_Sku::getSkuCode).collect(Collectors.toList()));
        }
        // wms逻辑库存取得
        Map<String, Integer> skuInventoryMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : mapChannelSkus.entrySet()) {
            List<WmsBtInventoryCenterLogicModel> skuInventoryList = wmsBtInventoryCenterLogicDao.selectItemDetailBySkuList(entry.getKey(), entry.getValue());
            for (WmsBtInventoryCenterLogicModel model : skuInventoryList) {
                skuInventoryMap.put(model.getSku(), model.getQtyChina());
            }
        }

        // Map<skuCode, sku属性值>
        Map<String, BaseMongoMap<String, Object>> mapSku = sxData.getSkuList().stream().collect(
                Collectors.toMap(sku -> sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()), sku -> sku)); // common下sku属性 + 各个平台下面的sku属性

        for (CmsBtProductModel product : sxData.getProductList()) {
            // 循环group下所有code
            for (BaseMongoMap<String, Object> sku : product.getPlatform(sxData.getCartId()).getSkus()) {
                List<Field> fieldList;
                try {
                    // 将取得的产品Schema xml转换为List<Field>
                    fieldList = SchemaReader.readXmlForList(itemSchema);
                } catch (TopSchemaException ex) {
                    // 解析XML异常
                    String errMsg = String.format("将取得的Sku的Schema xml转换为field列表失败(解析XML异常)！[ChannelId:%s] [CartId:%s] [GroupId:%s]",
                            sxData.getChannelId(), sxData.getCartId(), sxData.getGroupId());
                    ex.printStackTrace();
                    throw new BusinessException(errMsg);
                }

                String skuCode = sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
                constructEachSkuPlatformProps(fieldList, mapSku.get(skuCode), product, skuInventoryMap.get(skuCode));
                listSku.add(fieldList);
            }
        }

        return listSku;
    }

    private void constructEachSkuPlatformProps(List<Field> fields, BaseMongoMap<String, Object> sku, CmsBtProductModel product, Integer qty) throws Exception {
        Map<String, Field> fieldsMap = new HashMap<>();
        for (Field field : fields) {
            fieldsMap.put(field.getId(), field);
        }

        {
            // OrgChannelId 原始channel id
            String field_id = "OrgChannelId";
            Field field = fieldsMap.get(field_id);

            ((InputField) field).setValue(product.getOrgChannelId());
        }
        {
            // ProductCode code
            String field_id = "ProductCode";
            Field field = fieldsMap.get(field_id);

            ((InputField) field).setValue(product.getCommon().getFields().getCode());
        }
        {
            // Sku Sku
            String field_id = "Sku";
            Field field = fieldsMap.get(field_id);

            ((InputField) field).setValue(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()));
        }
        {
            // Quantity 库存
            String field_id = "Quantity";
            Field field = fieldsMap.get(field_id);

            if (qty == null) {
                ((InputField) field).setValue("0");
            } else {
                ((InputField) field).setValue(qty.toString());
            }
        }
        {
            // Size 原始尺码
            String field_id = "Size";
            Field field = fieldsMap.get(field_id);

            ((InputField) field).setValue(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.size.name()));
        }
        {
            // ShowSize 显示尺码
            String field_id = "ShowSize";
            Field field = fieldsMap.get(field_id);

            ((InputField) field).setValue(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name()));
        }
        {
            // Msrp 建议零售价
            String field_id = "Msrp";
            Field field = fieldsMap.get(field_id);

            ((InputField) field).setValue(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.priceMsrp.name()));
        }
        {
            // Price 价格
            String field_id = "Price";
            Field field = fieldsMap.get(field_id);

            ((InputField) field).setValue(sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.priceSale.name()));
        }

    }

    /**
     * 一些特殊处理的属性
     * 暂时代码写死吧
     *
     * @param fields List<Field> 直接把值set进这个fields对象
     */
    private void constructProductCustomPlatformProps(List<Field> fields, List<String> listSp, CmsBtProductModel product, ExpressionParser expressionParser, ShopBean shopBean, String modifier) throws Exception {
        SxData sxData = expressionParser.getSxData();
        String strUrlKey = product.getOrgChannelId() + "-" + Long.toString(product.getProdId());

        Map<String, Field> fieldsMap = new HashMap<>();
        for (Field field : fields) {
            fieldsMap.put(field.getId(), field);
        }

        {
            // Websites 网站名字
            String field_id = "Websites";
            listSp.add(field_id);
            Field field = fieldsMap.get(field_id);

            List<CmsMtChannelConditionConfigModel> conditionPropValueModels = conditionPropValueService.get(sxData.getChannelId(), field_id);

            RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
            // 优先使用设定好的
            if (ListUtils.notNull(conditionPropValueModels)) {
                // 应该只有一条,总之取第一条就好
                boolean isFind = false;
                for (CmsMtChannelConditionConfigModel conditionPropValueModel : conditionPropValueModels) {
                    // 找到第一条就好
                    String conditionExpressionStr = conditionPropValueModel.getConditionExpression();
                    RuleExpression conditionExpression = ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
                    String propValue = expressionParser.parse(conditionExpression, shopBean, modifier, null);
                    if (!StringUtils.isEmpty(propValue)) {
                        ((InputField) field).setValue(propValue);
                        isFind = true;
                        break;
                    }
                }
                if (!isFind) {
                    throw new BusinessException(String.format("网站名字[Websites]未找到符合条件的设定! [ChannelId:%s]", sxData.getChannelId()));
                }
            } else {
                throw new BusinessException(String.format("网站名字[Websites]未设定! [ChannelId:%s]", sxData.getChannelId()));
            }
        }
        {
            // Store
            String field_id = "Store";
            listSp.add(field_id);
            Field field = fieldsMap.get(field_id);

            // 暂时写死admin
            ((InputField) field).setValue("admin");
        }
        {
            // Sku code
            String field_id = "Sku";
            listSp.add(field_id);
            Field field = fieldsMap.get(field_id);

            ((InputField) field).setValue(product.getCommon().getFields().getCode());
        }
        {
            // Status 商品处理动作：1创建或更新， 2删除
            String field_id = "Status";
            listSp.add(field_id);
            Field field = fieldsMap.get(field_id);

            CmsConstants.PlatformActive platformActive = sxData.getPlatform().getPlatformActive();
            if (platformActive == CmsConstants.PlatformActive.ToOnSale) {
                ((SingleCheckField) field).setValue("1");
            } else if (platformActive == CmsConstants.PlatformActive.ToInStock) {
                ((SingleCheckField) field).setValue("2");
            } else {
                throw new BusinessException("PlatformActive must be ToOnSale or ToInStock, but now it is " + platformActive);
            }
        }
        {
            // OrgChannelId 原始channel id
            String field_id = "OrgChannelId";
            listSp.add(field_id);
            Field field = fieldsMap.get(field_id);

            ((InputField) field).setValue(product.getOrgChannelId());
        }
        {
            // UrlKey orgChannelId + "-" + cms product id
            String field_id = "UrlKey";
            listSp.add(field_id);
            Field field = fieldsMap.get(field_id);

            ((InputField) field).setValue(strUrlKey);
        }
        {
            // CategoryIds
            String field_id = "CategoryIds";
            listSp.add(field_id);
            Field field = fieldsMap.get(field_id);

            // 用"店铺内分类"，逗号分隔
            List<CmsBtProductModel_SellerCat> defaultValues = product.getPlatform(sxData.getCartId()).getSellerCats();
            if (ListUtils.notNull(defaultValues)) {
                String propValue = defaultValues.stream().map(CmsBtProductModel_SellerCat::getcId).collect(Collectors.joining(","));
                ((InputField) field).setValue(propValue);
            } else {
                throw new BusinessException(String.format("商品[code:]未选择店铺内分类!", product.getCommon().getFields().getCode()));
            }
        }
        {
            // Description 商品详细说明
            String field_id = "Description";
            listSp.add(field_id);
            Field field = fieldsMap.get(field_id);

            setDescriptionValue(expressionParser, shopBean, modifier, field, true);
        }
        {
            // ShortDescription 商品简短说明
            String field_id = "ShortDescription";
            listSp.add(field_id);
            Field field = fieldsMap.get(field_id);

            setDescriptionValue(expressionParser, shopBean, modifier, field, false);
        }
        {
            // MainImageList 商品主图  逗号分隔
            String field_id = "MainImageList";
            listSp.add(field_id);
            Field field = fieldsMap.get(field_id);

            List<CmsBtProductModel_Field_Image> imageList = sxProductService.getProductImages(product, CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE);
            int imageCnt = imageList.size() > 5 ? 5 : imageList.size(); // 最多5张图

            List<String> imageKey = new ArrayList<>();
            List<String> imageNames = new ArrayList<>();
            for (int index = 0; index < imageCnt; index++) {
                // modified by morse.lu 2016/09/26 start
                // UrlKey-1,UrlKey-2
//                String imageName = imageList.get(index).getName();
//                if (index == 0) {
//                    strImageNames = imageName;
//                } else {
//                    strImageNames = strImageNames + "," + imageName;
//                }
                imageKey.add(strUrlKey + "-" + Integer.toString(index));
                imageNames.add(imageList.get(index).getName());
                // modified by morse.lu 2016/09/26 end
            }

            // 更新cms_bt_sx_cn_images表，之后上传图片的job会抽出status=0的数据进行上传图片
            cnImageService.updateImageInfo(sxData.getChannelId(), sxData.getCartId(), product.getCommon().getFields().getCode(), strUrlKey, imageNames, getTaskNameForUpdate());

//            ((InputField) field).setValue(strImageNames);
            ((InputField) field).setValue(imageKey.stream().collect(Collectors.joining(",")));
        }
        {
            // CreatedAt 上市日期
            String field_id = "CreatedAt";
            listSp.add(field_id);
            Field field = fieldsMap.get(field_id);

            String propValue = sxProductService.getProductValueByMasterMapping(field, shopBean, expressionParser, modifier);
            if (!StringUtils.isEmpty(propValue)) {
                Date date = DateTimeUtil.parse(propValue, DateTimeUtil.DEFAULT_DATE_FORMAT);
                if (date == null) {
                    throw new BusinessException(field.getName() + "格式转换失败!");
                }
                ((InputField) field).setValue(DateTimeUtil.format(date, DateTimeUtil.DATE_TIME_FORMAT_11));
            }
        }
        {
            // Model 款号
            String field_id = "Model";
            listSp.add(field_id);
            Field field = fieldsMap.get(field_id);

            ((InputField) field).setValue(product.getCommon().getFields().getModel());
        }
        {
            // Brand 品牌
            String field_id = "Brand";
            listSp.add(field_id);
            Field field = fieldsMap.get(field_id);

            ((InputField) field).setValue(product.getCommon().getFields().getBrand());
        }
//        {
//            // IsOnSale 上下架（0下架， 1上架）
//            String field_id = "IsOnSale";
//            listSp.add(field_id);
//            Field field = fieldsMap.get(field_id);
//
//            CmsConstants.PlatformActive platformActive = sxData.getPlatform().getPlatformActive();
//            if (platformActive == CmsConstants.PlatformActive.ToOnSale) {
//                ((SingleCheckField) field).setValue("1");
//            } else if (platformActive == CmsConstants.PlatformActive.ToInStock) {
//                ((SingleCheckField) field).setValue("0");
//            } else {
//                throw new BusinessException("PlatformActive must be Onsale or Instock, but now it is " + platformActive);
//            }
//        }
        {
            // Msrp 建议零售价
            String field_id = "Msrp";
            listSp.add(field_id);
            Field field = fieldsMap.get(field_id);

            calcPrice(product, sxData, field, CmsBtProductConstants.Platform_SKU_COM.priceMsrp.name());
        }
        {
            // SalePrice 价格
            String field_id = "SalePrice";
            listSp.add(field_id);
            Field field = fieldsMap.get(field_id);

            calcPrice(product, sxData, field, CmsBtProductConstants.Platform_SKU_COM.priceSale.name());
        }

    }

    /**
     * 产品的所有sku的最高价格
     *
     * @param product 产品
     * @param sxData SxData
     * @param field Field
     * @param sxPricePropName 价格的属性id
     */
    private void calcPrice(CmsBtProductModel product, SxData sxData, Field field, String sxPricePropName) {
        Double resultPrice = 0d;
        for (BaseMongoMap<String, Object> cmsBtProductModelSku : product.getPlatform(sxData.getCartId()).getSkus()) {
            double skuPrice = 0;
            try {
                skuPrice = cmsBtProductModelSku.getDoubleAttribute(sxPricePropName);
            } catch (Exception e) {
                $warn(String.format("No price[%s] for sku [%s] ", sxPricePropName, cmsBtProductModelSku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name())));
            }
            resultPrice = Double.max(resultPrice, skuPrice);
        }

        ((InputField) field).setValue(String.valueOf(resultPrice));
    }

    /**
     * 详情描述设值
     */
    private void setDescriptionValue(ExpressionParser expressionParser, ShopBean shopBean, String modifier, Field field, boolean isRequired) throws Exception {
        SxData sxData = expressionParser.getSxData();
        String field_id = field.getId();
        String propValue = "";
        String desVal = sxProductService.getProductValueByMasterMapping(field, shopBean, expressionParser, modifier);
        if (!StringUtils.isEmpty(desVal)) {
            if ("-1".equals(desVal)) {
                // 其它
                RuleExpression ruleOther = new RuleExpression();
                MasterWord mwOther = new MasterWord("in_" + field_id);
                ruleOther.addRuleWord(mwOther);
                propValue = expressionParser.parse(ruleOther, shopBean, modifier, null);
            } else {
                propValue = sxProductService.resolveDict(desVal, expressionParser, shopBean, modifier, null);
                if (StringUtils.isEmpty(propValue)) {
                    String errorMsg = String.format("详情描述[%s]在dict表里未设定!", desVal);
                    sxData.setErrorMessage(errorMsg);
                    throw new BusinessException(errorMsg);
                }
            }
        }

        if (StringUtils.isEmpty(propValue) && isRequired) {
            throw new BusinessException(String.format("[%s]属性必须输入!", field.getName()));
        }

        ((InputField) field).setValue(propValue);
    }

}
