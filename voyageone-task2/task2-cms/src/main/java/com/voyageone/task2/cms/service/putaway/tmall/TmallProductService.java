package com.voyageone.task2.cms.service.putaway.tmall;

import com.taobao.api.ApiException;
import com.taobao.api.response.TmallItemSchemaAddResponse;
import com.taobao.api.response.TmallItemSchemaUpdateResponse;
import com.taobao.api.response.TmallItemUpdateSchemaGetResponse;
import com.taobao.top.schema.enums.FieldTypeEnum;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaReader;
import com.taobao.top.schema.factory.SchemaWriter;
import com.taobao.top.schema.field.*;
import com.taobao.top.schema.value.ComplexValue;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.*;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaDao;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.cms.bean.*;
import com.voyageone.task2.cms.bean.tcb.*;
import com.voyageone.task2.cms.dao.BrandMapDao;
import com.voyageone.task2.cms.dao.DictWordDao;
import com.voyageone.task2.cms.dao.PlatformPropCustomMappingDao;
import com.voyageone.task2.cms.enums.PlatformWorkloadStatus;
import com.voyageone.task2.cms.enums.TmallWorkloadStatus;
import com.voyageone.task2.cms.model.ConditionPropValueModel;
import com.voyageone.task2.cms.model.CustomPlatformPropMappingModel;
import com.voyageone.task2.cms.service.putaway.AbstractSkuFieldBuilder;
import com.voyageone.task2.cms.service.putaway.ConditionPropValueRepo;
import com.voyageone.task2.cms.service.putaway.SkuFieldBuilderFactory;
import com.voyageone.task2.cms.service.putaway.UploadProductHandler;
import com.voyageone.task2.cms.service.putaway.rule_parser.ExpressionParser;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.ims.modelbean.DictWordBean;
import com.voyageone.ims.rule_expression.DictWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by Leo on 2015/5/28.
 */
@Repository
public class TmallProductService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    TbProductService tbProductService;
    @Autowired
    private BrandMapDao brandMapDao;
    @Autowired
    private CmsMtPlatformCategorySchemaDao cmsMtPlatformCategorySchemaDao;
    @Autowired
    private PlatformPropCustomMappingDao platformPropCustomMappingDao;

    @Autowired
    private SkuFieldBuilderFactory skuFieldBuilderFactory;
    @Autowired
    private PriceSectionBuilder priceSectionBuilder;

    @Autowired
    private DictWordDao dictWordDao;
    @Autowired
    private IssueLog issueLog;

    @Autowired
    private ConditionPropValueRepo conditionPropValueRepo;

    public void doJob(UploadProductTcb tcb, UploadProductHandler uploadProductHandler) throws TaskSignal {
        UpJobParamBean upJobParamBean = tcb.getWorkLoadBean().getUpJobParam();

        if (UpJobParamBean.METHOD_ADD.equalsIgnoreCase(upJobParamBean.getMethod()))
        {
            addProduct(tcb, uploadProductHandler);
        }
        else if (UpJobParamBean.METHOD_UPDATE.equalsIgnoreCase(upJobParamBean.getMethod()))
        {
            updateProduct(tcb, uploadProductHandler);
        }
        else
        {
            String abortCause = "Method:" + upJobParamBean.getMethod() + " is not supported";
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(abortCause));
        }
    }

    public void addProduct(UploadProductTcb tcb, UploadProductHandler uploadProductHandler) throws TaskSignal {
        logger.debug("Add Product, workload: " + tcb.getWorkLoadBean());
        PlatformWorkloadStatus workloadStatus = tcb.getWorkLoadBean().getWorkload_status();
        switch (workloadStatus.getValue())
        {
            case TmallWorkloadStatus.ADD_INIT: {
                addProductForStatusInit(tcb);
                break;
            }
            case TmallWorkloadStatus.ADD_SEARCH_PRODUCT: {
                addProductForStatusSearchProduct(tcb);
                break;
            }
            case TmallWorkloadStatus.ADD_CHECK_PRODUCT_STATUS: {
                addProductForStatusCheckProductStatus(tcb);
                break;
            }
            case TmallWorkloadStatus.ADD_UPLOAD_PRODUCT: {
                addProductForStatusUploadProduct(tcb);
                break;
            }
            case TmallWorkloadStatus.ADD_PRODUCT_PIC_UPLOADED: {
                addProductForStatusProductImageUploaded(tcb);
                break;
            }
            case TmallWorkloadStatus.ADD_UPLOAD_ITEM:
            {
                logger.debug("It's time to upload item!");
                addProductForStatusUploadItem(tcb);
                break;
            }
            case TmallWorkloadStatus.ADD_ITEM_PIC_UPLOADED: {
                logger.debug("Item pic has been uploaded!");
                addProductForStatusItemImageUploaded(tcb);
                break;
            }
            case PlatformWorkloadStatus.JOB_DONE: {
                uploadProductHandler.stopTcb(tcb);
                break;
            }
            case TmallWorkloadStatus.ADD_WAIT_PRODUCT_PIC:
            {
                logger.error("This is not correct, debug it!");
                break;
            }
            case PlatformWorkloadStatus.JOB_ABORT: {
                uploadProductHandler.stopTcb(tcb);
                break;
            }
        }
    }

    /**
     * 在天猫平台上传商品时做的初始化操作
     * 1. 创建该任务运行时状态对象-TmallUploadRunState
     * 2. 从数据库中找到主数据品牌对应的平台品牌，并设置到TmallUploadRunState
     * 3. 将平台类目设置到TmallUploadRunState
     * 4. 判断是否时达尔文体系，并设置到TmallUploadRunState
     * 5. 选择主商品（选择第一个code作为主商品）
     * 6. 进入搜索产品状态
     * @param tcb UploadProductTcb
     * @throws TaskSignal
     */
    private void addProductForStatusInit(UploadProductTcb tcb) throws TaskSignal {
        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        String channelId = workLoadBean.getOrder_channel_id();
        int cartId = workLoadBean.getCart_id();
        ShopBean shopBean = Shops.getShop(channelId, cartId);

        TmallUploadRunState tmallUploadRunState = new TmallUploadRunState(tcb);
        SxProductBean mainSxProduct = workLoadBean.getMainProduct();
        CmsBtProductModel cmsMainProduct = mainSxProduct.getCmsBtProductModel();

        //款号也有可能从workload带过来，因为如果是达尔文，可能任务被拆分，拆分的子任务会带上款号
        tmallUploadRunState.setStyle_code(workLoadBean.getStyle_code());
        TmallWorkloadStatus tmallWorkloadStatus = new TmallWorkloadStatus(workLoadBean.getWorkload_status());
        workLoadBean.setWorkload_status(tmallWorkloadStatus);

        //开始上传产品，预先创建product run state, 为保存状态做状态
        if (tcb.getPlatformUploadRunState() == null)
        {
            tcb.setPlatformUploadRunState(tmallUploadRunState);
        }

        Long categoryCode = Long.valueOf(tcb.getPlatformCId());
        String brandCode = brandMapDao.cmsBrandToPlatformBrand(channelId, cartId, cmsMainProduct.getFields().getBrand());

        if (brandCode == null || "".equals(brandCode))
        {
            String abortCause = "Job abort: can not find brand_code by brandId "
                    + cmsMainProduct.getFields().getBrand()
                    + ", workload:" + workLoadBean;
            logger.error(abortCause);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(abortCause));
        }
        else {
            logger.debug("找到天猫品牌:" + brandCode);
        }
        if (categoryCode < 0)
        {
            String abortCause = "Job abort: can not find category_code by model, workload:" + workLoadBean;
            logger.error(abortCause);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(abortCause));
        }
        logger.debug("找到天猫分类ID:" + categoryCode);

        tmallUploadRunState.setBrand_code(brandCode);
        tmallUploadRunState.setCategory_code(categoryCode);

        //判断商品是否是达尔文体系
        StringBuffer failCause = new StringBuffer();
        Boolean isDarwin;
        try {
            isDarwin = tbProductService.isDarwin(categoryCode, Long.parseLong(brandCode), shopBean, failCause);
            if (isDarwin == null && failCause.length() != 0) {
                if (failCause.indexOf("访问淘宝超时") == -1) {
                    throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(failCause.toString(), true));
                }
                else {
                    throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(failCause.toString()));
                }
            }
        } catch (ApiException e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }
        tmallUploadRunState.setIs_darwin(isDarwin);

        CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.getPlatformCatSchemaModel(tcb.getPlatformCId(), workLoadBean.getCart_id());
        workLoadBean.setCmsMtPlatformCategorySchemaModel(cmsMtPlatformCategorySchemaModel);

        ExpressionParser expressionParser = new ExpressionParser(channelId, cartId, mainSxProduct, workLoadBean.getProcessProducts());
        tcb.setExpressionParser(expressionParser);

        tmallWorkloadStatus.setValue(TmallWorkloadStatus.ADD_SEARCH_PRODUCT);
    }

    /**
     * 在天猫平台上传商品时搜索产品是否已经存在
     * 1. 调用天猫API获取产品匹配的schema
     * 2. 根据天猫API的产品匹配的Schema中的字段填充值
     * 3. 调用天猫API获取该产品是否存在匹配的product
     * 4. 如果不存在product则进入上传产品状态，如果存在，则进入检查产品状态
     * @param tcb UploadProductTcb
     * @throws TaskSignal
     */
    private void addProductForStatusSearchProduct(UploadProductTcb tcb) throws TaskSignal {
        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        TmallWorkloadStatus tmallWorkloadStatus = (TmallWorkloadStatus) workLoadBean.getWorkload_status();

        long categoryCode = tmallUploadRunState.getCategory_code();
        String orderChannelId = workLoadBean.getOrder_channel_id();
        int cartId = workLoadBean.getCart_id();
        String channelId = workLoadBean.getOrder_channel_id();
        SxProductBean mainSxProduct = workLoadBean.getMainProduct();
        CmsBtProductModel cmsBtProductModel = mainSxProduct.getCmsBtProductModel();
        String mainCategoryId = cmsBtProductModel.getCatId();

        ExpressionParser expressionParser = tcb.getExpressionParser();

        ShopBean shopBean = Shops.getShop(orderChannelId, cartId);

        List<String> productCodeList = new ArrayList<>();

        String searchSchema;
        try {
            searchSchema = tbProductService.getProductMatchSchema(categoryCode, shopBean);
            logger.debug("product_match_schema:" + searchSchema);

            if (searchSchema == null) {
                logger.info("No match schema found");
                tmallWorkloadStatus.setValue(TmallWorkloadStatus.ADD_UPLOAD_PRODUCT);
                tmallUploadRunState.getContextBuildFields().clearContext();
                return;
            }
        } catch (Exception e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }

        CmsMtPlatformMappingModel cmsMtPlatformMappingModel = workLoadBean.getCmsMtPlatformMappingModel();
        if (cmsMtPlatformMappingModel == null) {
            logger.error("Can't find mapping relation for [channel_id:" + channelId + ", cartId:" + cartId + ", mainCategoryId:" + mainCategoryId + "]");
            return;
        }

        Map<String, Field> fieldMap;
        try {
            fieldMap = schemaToIdPropMap(searchSchema);
        } catch (TopSchemaException e) {
            logger.error(e.getMessage(), e);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("Can't convert schema to fields: " + e.getMessage()));
        }
        constructCustomPlatformProps(tcb, fieldMap, expressionParser, null);
        constructMappingPlatformProps(tcb, fieldMap, cmsMtPlatformMappingModel, expressionParser, null);
        List<Field> searchFields = resolveMappingProps(tmallUploadRunState, null);

        try {
            String[] product_codes = getProductCodesFromTmall(shopBean, categoryCode, searchFields);
            if (product_codes != null) {
                productCodeList.addAll(Arrays.asList(product_codes));
                logger.info("find product_code:" + productCodeList);
                tmallUploadRunState.setProduct_code_list(productCodeList);
                tmallWorkloadStatus.setValue(TmallWorkloadStatus.ADD_CHECK_PRODUCT_STATUS);
                tmallUploadRunState.getContextBuildFields().clearContext();
            } else {
                logger.info("No product_code matched");
                tmallWorkloadStatus.setValue(TmallWorkloadStatus.ADD_UPLOAD_PRODUCT);
                tmallUploadRunState.getContextBuildFields().clearContext();
            }
        } catch (Exception e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }
    }

    /**
     * 生成款号
     * 1. 如果不是达尔文体系，那么使用model作为款号直接返回
     * 2. 如果是达尔文体系，那么首先从workload获取款号（当该任务是达尔文已经拆分的子任务时，该款号会存在）
     * 3. 如果是达尔文体系，并且workload中没有获取到款号，那么先从darwin list表中读取到该model下所有code
     *    的款号，根据不同的款号拆分成多个子任务，抛出切分任务的信号给任务分发者，有WorkloadDispacher重新
     *    分发子任务。
     * @param tcb UploadProductTcb
     * @throws TaskSignal
     */
    private String generateStyleCode(UploadProductTcb tcb) throws TaskSignal {
        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        SxProductBean mainSxProduct = workLoadBean.getMainProduct();
        CmsBtProductModel cmsMainProduct = mainSxProduct.getCmsBtProductModel();
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        boolean isDarwin = tmallUploadRunState.is_darwin();

        if (!isDarwin) {
            String styleCode = cmsMainProduct.getFields().getCode();
            workLoadBean.setStyle_code(styleCode);
            return styleCode;
        } //如果是达尔文体系
        /*
        else {
            if (workLoadBean.getStyle_code() == null || "".equals(workLoadBean.getStyle_code())) {

                Set<WorkLoadBean> subWorkloads = new HashSet<>();

                Map<String, WorkLoadBean> styleCodeWorkloadMap = new HashMap<>();

                for (CmsCodePropBean cmsCodePropBean : cmsCodeProps) {
                    String styleCode = getStyleCodeFromDawinList(workLoadBean.getCart_id(), cmsCodePropBean.getProp(CmsFieldEnum.CmsCodeEnum.code));
                    if (styleCode == null || "".equals(styleCode)) {
                        styleCode = cmsModelProp.getProp(CmsFieldEnum.CmsModelEnum.model);
                    }

                    WorkLoadBean subWorkload = styleCodeWorkloadMap.get(styleCode);
                    if (subWorkload == null) {
                        CmsModelPropBean subCmsModelProp = new CmsModelPropBean();
                        subWorkload = workLoadBean.clone();
                        subWorkload.setCmsModelProp(subCmsModelProp);
                        subWorkload.setStyle_code(styleCode);
                        subWorkload.getWorkload_status().setValue(PlatformWorkloadStatus.JOB_INIT);

                        List<CmsCodePropBean> subCmsCodePropBeans = new ArrayList<>();
                        cmsCodePropBean.setCmsModelPropBean(subCmsModelProp);
                        subCmsCodePropBeans.add(cmsCodePropBean);
                        subCmsModelProp.setPropMap(cmsModelProp.getPropMap());
                        subCmsModelProp.setCmsCodePropBeanList(subCmsCodePropBeans);

                        subWorkloads.add(subWorkload);
                        styleCodeWorkloadMap.put(styleCode, subWorkload);
                    } else {
                        CmsModelPropBean subCmsModelProp = subWorkload.getCmsModelProp();
                        subCmsModelProp.getCmsCodePropBeanList().add(cmsCodePropBean);
                    }
                }
                throw new TaskSignal(TaskSignalType.DIVISION, new DivideTaskSignalInfo(subWorkloads));
            }
            */
            return workLoadBean.getStyle_code();
    }

    /**
     * 在天猫平台上传商品时检查产品状态(不存在，等待审核，审核完毕)
     * 对所有search product到的产品进行遍历
     *  1. 如果发现某一个产品的产品状态是审核完毕，那么跳出循环，并进入上传商品状态。
     *  2. 如果发现某一个产品的产品状态是未审核，那么继续循环
     *  3. 如果发现产品不存在，那么也继续循环
     *  4. 循环结束，如果有未审核的产品，那么抛出Abort_Job信号，任务结束，结束原因为：需要
     *     等待产品审核
     *  5. 循环结束，如果没有未审核的产品，那么进入上传产品状态
     * @param tcb UploadProductTcb
     * @throws TaskSignal
     */
    private void addProductForStatusCheckProductStatus(UploadProductTcb tcb) throws TaskSignal {
        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        TmallWorkloadStatus tmallWorkloadStatus = (TmallWorkloadStatus) workLoadBean.getWorkload_status();
        List<String> productCodeList = tmallUploadRunState.getProduct_code_list();
        String productCode = null;
        boolean wait_for_check = false; //产品是否在等待天猫审核

        for (String pid : productCodeList)
        {
            //查询产品状态
            String product_status;
            try {
                product_status = getProductStatus(workLoadBean.getOrder_channel_id(), workLoadBean.getCart_id()
                        + "", Long.parseLong(pid));
            } catch (Exception e) {
                issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
                throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
            }
            //可以发布商品
            if ("true".equalsIgnoreCase(product_status)) {
                productCode = pid;
                wait_for_check = false;
                break;
            }//没有款号
            else if (product_status == null || "".equals(product_status)){
                // 如果在循环中已经发现有产品存在但未审核，那么不会覆盖productId为null，
                // 否则，循环结束会认为没有找到产品，从而重新上传产品
                if (wait_for_check)
                    continue;
                else
                    productCode = null;
            }//等待审核
            else {
                productCode = pid;
                wait_for_check = true;
            }
        }

        if (productCode == null)
        {
            //没有找到产品ID，下一步要上传产品
            tmallWorkloadStatus.setValue(TmallWorkloadStatus.ADD_UPLOAD_PRODUCT);
            tmallUploadRunState.setProduct_code(productCode);
        }
        else if (wait_for_check) {
            tmallWorkloadStatus.setValue(TmallWorkloadStatus.ADD_WAIT_CHECK);
            String errorDesc = "发现已有产品符合我们要上传的商品，但需要等待天猫审核该产品";
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(errorDesc));
        }
        else
        {
            tmallUploadRunState.setProduct_code(productCode);
            workLoadBean.setProductId(tmallUploadRunState.getProduct_code());
            //找到产品ID，下一步要上传商品，因此任务状态变为上传商品状态
            tmallWorkloadStatus.setValue(TmallWorkloadStatus.ADD_UPLOAD_ITEM);
        }
    }

    /**
     * 天猫平台上传商品时，上传产品
     * 1. 从数据库中查询所有产品的字段
     * 2. 对所有产品字段进行mapping填值，如果遇到图片，那么抛出上传图片的信号
     * 3. 如果没有遇到图片，那么调用Tmall API上传产品，如果上传成功，进入上传商品状态，
     *    否则抛出Abort_Job信号, 错误原因为Tmall Api返回的错误
     * @param tcb UploadProductTcb
     * @throws TaskSignal
     */
    private void addProductForStatusUploadProduct(UploadProductTcb tcb) throws TaskSignal {
        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        TmallWorkloadStatus tmallWorkloadStatus = (TmallWorkloadStatus) workLoadBean.getWorkload_status();
        long categoryCode = tmallUploadRunState.getCategory_code();
        String brandCode = tmallUploadRunState.getBrand_code();
        String productCode = tmallUploadRunState.getProduct_code();
        ShopBean shopBean = Shops.getShop(workLoadBean.getOrder_channel_id(),
                String.valueOf(workLoadBean.getCart_id()));
        ExpressionParser expressionParser = tcb.getExpressionParser();
        CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = workLoadBean.getCmsMtPlatformCategorySchemaModel();
        CmsMtPlatformMappingModel cmsMtPlatformMappingModel = workLoadBean.getCmsMtPlatformMappingModel();

        //没有找到产品id，需要重新上传Tmall产品
        if (productCode == null)
        {
            Set<String> imageSet = new HashSet<>();
            //输出参数，当构造image参数时，会填充url与它所在的field的映射关系，便于当图片上传结束时，能恢复url到字段中的值

            String productSchema = cmsMtPlatformCategorySchemaModel.getPropsProduct();
            logger.debug("productSchema:" + productSchema);

            Map<String, Field> fieldMap;
            try {
                fieldMap = schemaToIdPropMap(productSchema);
            } catch (TopSchemaException e) {
                logger.error(e.getMessage(), e);
                throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("Can't convert schema to fields: " + e.getMessage()));
            }
            constructCustomPlatformProps(tcb, fieldMap, expressionParser, imageSet);
            constructMappingPlatformProps(tcb, fieldMap, cmsMtPlatformMappingModel, expressionParser, imageSet);

            //如果urlSet不为空，就去上传图片
            if (imageSet.size() != 0)
            {
                //construct uploadImageParam
                UploadImageParam uploadImageParam = new UploadImageParam();
                uploadImageParam.setSrcUrlSet(imageSet);
                uploadImageParam.setShopBean(shopBean);

                //save UploadProductHandler status
                tmallWorkloadStatus.setValue(TmallWorkloadStatus.ADD_WAIT_PRODUCT_PIC);

                //create UploadImageHandler Tcb
                UploadImageTcb uploadImageTcb = new UploadImageTcb(uploadImageParam);
                uploadImageTcb.setUploadProductTcb(tcb);
                tcb.setUploadImageTcb(uploadImageTcb);

                //suspend UploadProductHandler's current tcb and resume uploadImage
                throw new TaskSignal(TaskSignalType.MainTaskToUploadImage, new MainToUploadImageTaskSignalInfo
                        (tcb, uploadImageTcb));
            }
            TmallUploadRunState.TmallContextBuildFields contextBeforeUploadImage =
                    tmallUploadRunState.getContextBuildFields();

            List<Field> productFields = resolveMappingProps(tmallUploadRunState, null);

            try {
                productCode = addTmallProduct(categoryCode, brandCode, productFields, workLoadBean);
            } catch (Exception e) {
                issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
                throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
            }

            if (productCode == null)
            {
                throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("no productCode found"));
            }
            else {
                tmallUploadRunState.setProduct_code(productCode);
                workLoadBean.setProductId(productCode);
                tmallWorkloadStatus.setValue(TmallWorkloadStatus.ADD_CHECK_PRODUCT_STATUS);
                contextBeforeUploadImage.clearContext();
                logger.info("Success to upload product, product_code: " + productCode);
            }
        }
    }
    /**
     * 天猫平台上传商品时，当产品图片上传成功后继续上传产品
     * @param tcb UploadProductTcb
     * @throws TaskSignal
     */
    private void addProductForStatusProductImageUploaded(UploadProductTcb tcb) throws TaskSignal {
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        UploadImageResult uploadImageResult = tcb.getUploadImageResult();
        Long categoryCode = tmallUploadRunState.getCategory_code();
        String brandCode = tmallUploadRunState.getBrand_code();

        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        TmallWorkloadStatus tmallWorkloadStatus = (TmallWorkloadStatus) workLoadBean.getWorkload_status();

        TmallUploadRunState.TmallContextBuildFields contextBeforeUploadImage =
                tmallUploadRunState.getContextBuildFields();

        Map<String, String> urlMap;
        if (uploadImageResult.isUploadSuccess()) {
            urlMap = uploadImageResult.getUrlMap();
        } else {
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(uploadImageResult.getFailCause(), uploadImageResult.isNextProcess()));
        }

        List<Field> productFields = resolveMappingProps(tmallUploadRunState, urlMap);

        //如果上传图片失败，直接在上传图片失败时进入abort状态，就不会进入该函数，因此，此处无须判断图片是否上传成功
        String productCode;
        try {
            productCode = addTmallProduct(categoryCode, brandCode, productFields, workLoadBean);
        } catch  (Exception e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }
        //TODO 临时随便设置一个
        //String productCode = "465251179";

        if (productCode == null)
        {
            logger.info("Fail to upload a product, job abort!");
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("no productCode found"));
        }
        else {
            tmallUploadRunState.setProduct_code(productCode);
            List<String> productCodeList = new ArrayList<>();
            productCodeList.add(productCode);
            tmallUploadRunState.setProduct_code_list(productCodeList);
            tmallWorkloadStatus.setValue(TmallWorkloadStatus.ADD_CHECK_PRODUCT_STATUS);
            //TODO 临时直接调到上传商品
            tmallWorkloadStatus.setValue(TmallWorkloadStatus.ADD_UPLOAD_ITEM);
            contextBeforeUploadImage.clearContext();
            logger.info("Success to upload product, product_code: " + productCode);
        }
    }

    private void addProductForStatusUploadItem(UploadProductTcb tcb) throws TaskSignal {
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        Long categoryCode = tmallUploadRunState.getCategory_code();
        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        TmallWorkloadStatus tmallWorkloadStatus = (TmallWorkloadStatus) workLoadBean.getWorkload_status();
        String channelId = workLoadBean.getOrder_channel_id();
        int cartId = workLoadBean.getCart_id();
        ShopBean shopBean = Shops.getShop(channelId, cartId);
        ExpressionParser expressionParser = tcb.getExpressionParser();
        CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = workLoadBean.getCmsMtPlatformCategorySchemaModel();
        CmsMtPlatformMappingModel cmsMtPlatformMappingModel = workLoadBean.getCmsMtPlatformMappingModel();
        Set<String> imageSet = new HashSet<>();

        String  productCode = tmallUploadRunState.getProduct_code();
        workLoadBean.setProductId(productCode);

        String itemSchema = cmsMtPlatformCategorySchemaModel.getPropsItem();

        logger.debug("itemSchema:" + itemSchema);

        Map<String, Field> fieldMap;
        try {
            fieldMap = schemaToIdPropMap(itemSchema);
        } catch (TopSchemaException e) {
            logger.error(e.getMessage(), e);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("Can't convert schema to fields: " + e.getMessage()));
        }
        constructCustomPlatformProps(tcb, fieldMap, expressionParser, imageSet);
        constructMappingPlatformProps(tcb, fieldMap, cmsMtPlatformMappingModel, expressionParser, imageSet);

        //如果imageSet不为空，就去上传图片
        if (imageSet.size() != 0)
        {
            //construct uploadImageParam
            UploadImageParam uploadImageParam = new UploadImageParam();
            uploadImageParam.setSrcUrlSet(imageSet);
            uploadImageParam.setShopBean(shopBean);

            //save UploadProductHandler status
            tmallWorkloadStatus.setValue(TmallWorkloadStatus.ADD_WAIT_ITEM_PIC);

            //create UploadImageHandler Tcb
            UploadImageTcb uploadImageTcb = new UploadImageTcb(uploadImageParam);
            uploadImageTcb.setUploadProductTcb(tcb);
            tcb.setUploadImageTcb(uploadImageTcb);

            //suspend UploadProductHandler's current tcb and resume uploadImage
            throw new TaskSignal(TaskSignalType.MainTaskToUploadImage, new MainToUploadImageTaskSignalInfo(tcb, uploadImageTcb));
        }

        TmallUploadRunState.TmallContextBuildFields contextBeforeUploadImage = tmallUploadRunState.getContextBuildFields();

        List<Field> itemFields = resolveMappingProps(tmallUploadRunState, null);

        //在临上新的前一刻，再次做库存更新
        /*
        TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields = contextBeforeUploadImage.getContextBuildCustomFields();
        List<Field> customFields = contextBuildCustomFields.getCustomFields();
        AbstractSkuFieldBuilder skuFieldBuilder = contextBuildCustomFields.getSkuFieldBuilder();
        int totalInventory = skuFieldBuilder.updateInventoryField(workLoadBean.getOrder_channel_id(), contextBuildCustomFields, customFields);
        InputField quantityField = contextBuildCustomFields.getQuantityField();
        if (quantityField != null) {
            quantityField.setValue(String.valueOf(totalInventory));
            logger.info("商品数量设为：" + quantityField.getValue());
        }
        */

        try {
            logger.debug("addTmallItem: [productCode:" + productCode + ", categoryCode:" + categoryCode + "]");
            String numId = addTmallItem(categoryCode, productCode, itemFields, shopBean);
            tcb.setNumId(numId);
        } catch (ApiException e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }
        throw new TaskSignal(TaskSignalType.DONE, null);
    }

    private void addProductForStatusItemImageUploaded(UploadProductTcb tcb) throws TaskSignal {
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        Long categoryCode = tmallUploadRunState.getCategory_code();
        String productCode = tmallUploadRunState.getProduct_code();

        UploadImageResult uploadImageResult = tcb.getUploadImageResult();

        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        ShopBean shopBean = Shops.getShop(workLoadBean.getOrder_channel_id(), String.valueOf(workLoadBean.getCart_id()));

        TmallUploadRunState.TmallContextBuildFields contextBeforeUploadImage = tmallUploadRunState.getContextBuildFields();

        Map<String, String> urlMap;
        if (uploadImageResult.isUploadSuccess()) {
            urlMap = uploadImageResult.getUrlMap();
        } else {
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(uploadImageResult.getFailCause(), uploadImageResult.isNextProcess()));
        }

        List<Field> itemFields = resolveMappingProps(tmallUploadRunState, urlMap);

        //在临上新的前一刻，再次做库存更新
        /*
        TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields = contextBeforeUploadImage.getContextBuildCustomFields();
        AbstractSkuFieldBuilder skuFieldBuilder = contextBuildCustomFields.getSkuFieldBuilder();
        List<Field> customFields = contextBeforeUploadImage.getCustomFields();
        int totalInventory;
        if (skuFieldBuilder != null) {
            totalInventory = skuFieldBuilder.updateInventoryField(workLoadBean.getOrder_channel_id(), contextBuildCustomFields, customFields);
        }
        else {
            totalInventory = calcTotalInventory(workLoadBean.getOrder_channel_id(), workLoadBean.getCmsModelProp());
        }
        InputField quantityField = contextBuildCustomFields.getQuantityField();
        if (quantityField != null) {
            quantityField.setValue(String.valueOf(totalInventory));
            logger.info("商品数量设为：" + quantityField.getValue());
        }
        */

        try {
            logger.debug("addTmallItem: [productCode:" + productCode + ", categoryCode:" + categoryCode + "]");
            String numId = addTmallItem(categoryCode, productCode, itemFields, shopBean);
            logger.debug("after addTmallItem");
            tcb.setNumId(numId);
        } catch (ApiException e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }

        logger.info("Success to upload item!");
        throw new TaskSignal(TaskSignalType.DONE, null);
    }

    /*
    private int calcTotalInventory(String orderChannelId, CmsModelPropBean cmsModelPropBean) {
        List<CmsCodePropBean> cmsCodePropBeans = cmsModelPropBean.getCmsCodePropBeanList();
        int totalInventory = 0;
        for (CmsCodePropBean cmsCodePropBean : cmsCodePropBeans) {
            String code = cmsCodePropBean.getProp(CmsFieldEnum.CmsCodeEnum.code);
            for (CmsSkuPropBean cmsSkuPropBean : cmsCodePropBean.getCmsSkuPropBeanList()) {
                String sku = cmsSkuPropBean.getProp(CmsFieldEnum.CmsSkuEnum.sku);
                String skuQuantityStr = skuInfoDao.getSkuInventoryMap(orderChannelId, code, sku);
                int skuQuantity = 0;
                if (skuQuantityStr != null) {
                    skuQuantity = Integer.valueOf(skuQuantityStr);
                }
                totalInventory += skuQuantity;
            }
        }
        return totalInventory;
    }
*/

    private String addTmallItem(Long categoryCode, String productCode, List<Field> itemFields, ShopBean shopBean) throws ApiException, TaskSignal {
        String xmlData;

        try {
            xmlData = SchemaWriter.writeParamXmlString(itemFields);
        } catch (TopSchemaException e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }
        StringBuffer failCause = new StringBuffer();
        logger.debug("tmall category code:" + categoryCode);
        logger.debug("xmlData:" + xmlData);
        TmallItemSchemaAddResponse addItemResponse = tbProductService.addItem(categoryCode, productCode, xmlData, shopBean);
        String numId;
        if (addItemResponse == null) {
            failCause.append("Tmall return null response when add item");
            logger.error(failCause + ", request:" + xmlData);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(failCause.toString(), true));
        } else if (addItemResponse.getErrorCode() != null) {
            logger.debug("errorCode:" + addItemResponse.getErrorCode());
            if (addItemResponse.getSubCode().contains("IC_CHECKSTEP_ALREADY_EXISTS_SAME_SPU")) {
                String subMsg = addItemResponse.getSubMsg();
                numId = getNumIdFromSubMsg(subMsg, failCause);
                if (failCause.length() == 0 && numId != null && !"".equals(numId)) {
                    logger.debug(String.format("find numId(%s) has been uploaded before", numId));
                    return numId;
                }
            } else {
                failCause.append(addItemResponse.getSubMsg());
            }

            //天猫系统服务异常
            if (failCause.indexOf("天猫商品服务异常") != -1
                    || failCause.indexOf("访问淘宝超时") != -1) {
                logger.debug("此处应该是下次启动任务仍需处理的错误--->" + failCause.toString());
                throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(failCause.toString(), true));
            }
            else {
                throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(failCause.toString(), false));
            }
        } else {
            numId = addItemResponse.getAddItemResult();
            logger.debug("numId: " + numId);
            return numId;
        }
    }

    private String getNumIdFromSubMsg(String subMsg, StringBuffer failCause) {
        //pattern: "您已发布过同类宝贝，不允许重复发布；已发布的商品ID列表为：521369504454"
        //pattern: 您已发布过相同类目(腰带/皮带/腰链)，品牌(BCBG)，货号(FLTBC155)的宝贝，不允许重复发布；已发布的商品ID列表为：524127233288,上架的数量必须大于0
        String numId = null;
        Pattern pattern = Pattern.compile("您已发布过.*已发布的商品ID列表为：\\d+");
        Matcher matcher = pattern.matcher(subMsg);
        if (matcher.find()) {
            String matchString = subMsg.substring(matcher.start(), matcher.end());
            Pattern numIdPattern = Pattern.compile("\\d+");
            Matcher numIdMatcher = numIdPattern.matcher(matchString);
            while (numIdMatcher.find()) {
                String matchInter = matchString.substring(numIdMatcher.start(), numIdMatcher.end());
                if (matchInter.length() == 12) {
                    numId = matchInter;
                    break;
                }
            }
            if (numId == null){
                failCause.append(subMsg);
                return null;
            }

            if (matcher.start() != 0) {
                failCause.append(subMsg.substring(0, matcher.start()));
            }

            if (matcher.end() != subMsg.length()) {
                failCause.append(subMsg.substring(matcher.end() + 1));
            }
        } else {
            failCause.append(subMsg);
        }
        return numId;
    }

    private String addTmallProduct(Long category_code, String brand_code, List<Field> productFields,
                                   WorkLoadBean workLoadBean) throws ApiException, TopSchemaException, TaskSignal {
        ShopBean shopBean = Shops.getShop(workLoadBean.getOrder_channel_id(), String.valueOf(workLoadBean.getCart_id()));
        TmallWorkloadStatus tmallWorkloadStatus = (TmallWorkloadStatus) workLoadBean.getWorkload_status();
        String product_code = null;

        String valueXml;

        valueXml = SchemaWriter.writeParamXmlString(productFields);
        logger.debug("valueXml:\n" + valueXml);

        StringBuffer failCause = new StringBuffer();
        String addProductResult = tbProductService.addProduct(category_code, Long.valueOf(brand_code), valueXml, shopBean, failCause);
        //成功，则更新状态
        if (addProductResult != null)
        {
            List<Field> result_fields = SchemaReader.readXmlForList(addProductResult);
            for (Field field : result_fields)
            {
                if ("product_id".equals(field.getId()) && field.getType().equals(FieldTypeEnum.INPUT))
                {
                    product_code = ((InputField)field).getValue();
                    break;
                }
            }

            tmallWorkloadStatus.setValue(TmallWorkloadStatus.ADD_UPLOAD_ITEM);
            return product_code;
        }
        else
        {
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(failCause.toString()));
        }
    }

    /*
    private String getStyleCodeFromDawinList(int cartId, String code) {
        return darwinStyleMappingDao.selectStyleCode(cartId, code);
    }
    */

    private String[] getProductCodesFromTmall(ShopBean shopBean,
                                              long category_code,
                                              List<Field> searchFields) throws TaskSignal {

        try {
            String schema = SchemaWriter.writeParamXmlString(searchFields);
            logger.debug("product match request:" + schema);
            if (schema != null)
            {
                return tbProductService.matchProduct(category_code, schema, shopBean);
            }
        } catch (Exception e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }
        return null;
    }

    private String getProductStatus(String channel_id, String cart_id, Long product_id) throws ApiException, TopSchemaException {
        ShopBean shopBean = Shops.getShop(channel_id, cart_id);
        String schema = tbProductService.getProductSchema(product_id, shopBean);
        logger.debug("product status schema:" + schema);
        List<Field> fields = SchemaReader.readXmlForList(schema);

        for (Field field : fields)
        {
            if (field instanceof InputField && "can_publish_item".equals(field.getId())) {
                String productStatus = ((InputField) field).getDefaultValue();
                logger.info("productStatus: " + productStatus);
                return productStatus;
            }
        }

        return null;
    }

    private void updateProduct(UploadProductTcb tcb, UploadProductHandler uploadProductHandler) throws TaskSignal {
        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();

        PlatformWorkloadStatus platformWorkloadStatus = workLoadBean.getWorkload_status();
        logger.debug("Update Product, workload: " + workLoadBean);

        switch (platformWorkloadStatus.getValue()) {
            case TmallWorkloadStatus.UPDATE_INIT:
                updateProductForStatusInit(tcb);
                break;
            case TmallWorkloadStatus.UPDATE_SEARCH_ITEM:
                updateProductForStatusSearch(tcb);
                break;
            case TmallWorkloadStatus.UPDATE_ITEM:
                updateProductForStatusUpdateItem(tcb);
                break;
            case TmallWorkloadStatus.UPDATE_ITEM_PIC_UPLOADED:
                updateProductForStatusItemImageUploaded(tcb);
                break;
            case PlatformWorkloadStatus.JOB_DONE: {
                uploadProductHandler.stopTcb(tcb);
                break;
            }
            case TmallWorkloadStatus.ADD_WAIT_PRODUCT_PIC:
            {
                logger.error("This is not correct, debug it!");
                break;
            }
            case PlatformWorkloadStatus.JOB_ABORT: {
                uploadProductHandler.stopTcb(tcb);
                break;
            }
        }
    }

    private void updateProductForStatusInit(UploadProductTcb tcb) throws TaskSignal {
        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        SxProductBean mainSxProduct = workLoadBean.getMainProduct();
        CmsBtProductModel mainCmsProduct = mainSxProduct.getCmsBtProductModel();
        String channelId = workLoadBean.getOrder_channel_id();
        int cartId = workLoadBean.getCart_id();

        ShopBean shopBean = Shops.getShop(channelId, cartId);

        TmallUploadRunState tmallUploadRunState = new TmallUploadRunState(tcb);

        TmallWorkloadStatus tmallWorkloadStatus = new TmallWorkloadStatus(workLoadBean.getWorkload_status());
        workLoadBean.setWorkload_status(tmallWorkloadStatus);

        String platformCid = tcb.getPlatformCId();

        if (platformCid == null || "".equalsIgnoreCase(platformCid)) {
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("更新时，categoryCode不能为空"));
        }
        long categoryCode = Long.parseLong(tcb.getPlatformCId());

        if (workLoadBean.getNumId() == null || "".equals(workLoadBean.getNumId())) {
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("更新时，num_id不能为空"));
        }

        //开始上传产品，预先创建product run state, 为保存状态做状态
        if (tcb.getPlatformUploadRunState() == null)
        {
            tcb.setPlatformUploadRunState(tmallUploadRunState);
        }

        String brandCode = brandMapDao.cmsBrandToPlatformBrand(channelId, cartId, mainCmsProduct.getFields().getBrand());

        if (brandCode == null || "".equals(brandCode))
        {
            logger.info("Job abort: can not find brand_code by brandId " +
                    mainCmsProduct.getFields().getBrand() + ", workload:" + workLoadBean);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("No brand found"));
        }
        else {
            logger.debug("找到天猫品牌:" + brandCode);
        }
        if (categoryCode < 0)
        {
            logger.info("Job abort: can not find category_code by model, workload:" + workLoadBean);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("No category found"));
        }
        logger.debug("找到天猫分类ID:" + categoryCode);

        tmallUploadRunState.setBrand_code(brandCode);
        tmallUploadRunState.setCategory_code(categoryCode);

        //判断商品是否是达尔文体系
        StringBuffer failCause = new StringBuffer();
        Boolean isDarwin;
        try {
            isDarwin = tbProductService.isDarwin(categoryCode, Long.parseLong(brandCode), shopBean, failCause);

            if (isDarwin == null || failCause.length() != 0) {
                if (failCause.indexOf("访问淘宝超时") != -1) {
                    throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(failCause.toString(), true));
                }
                else {
                    throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(failCause.toString()));
                }
            }
        } catch (ApiException e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }
        tmallUploadRunState.setIs_darwin(isDarwin);

        CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.getPlatformCatSchemaModel(tcb.getPlatformCId(), workLoadBean.getCart_id());
        workLoadBean.setCmsMtPlatformCategorySchemaModel(cmsMtPlatformCategorySchemaModel);

        ExpressionParser expressionParser = new ExpressionParser(channelId, cartId, mainSxProduct, workLoadBean.getProcessProducts());
        tcb.setExpressionParser(expressionParser);

        //更新时，主商品在读取任务时，已经读到workload中，此处不能设置
        //workLoadBean.setMainProductProp(cmsModelProp.getCmsCodePropBeanList().get(0));

        tmallWorkloadStatus.setValue(TmallWorkloadStatus.UPDATE_SEARCH_ITEM);
    }

    private void updateProductForStatusSearch(UploadProductTcb tcb) throws TaskSignal {
        logger.warn("还没有做商品是否存在的检查！");
        tcb.getWorkLoadBean().getWorkload_status().setValue(TmallWorkloadStatus.UPDATE_ITEM);
    }

    private void updateProductForStatusUpdateItem(UploadProductTcb tcb) throws TaskSignal {
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        Long categoryCode = tmallUploadRunState.getCategory_code();
        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        TmallWorkloadStatus tmallWorkloadStatus = (TmallWorkloadStatus) workLoadBean.getWorkload_status();
        ShopBean shopBean = Shops.getShop(workLoadBean.getOrder_channel_id(), String.valueOf(workLoadBean.getCart_id()));
        String numId = workLoadBean.getNumId();
        String productId = workLoadBean.getProductId();
        Set<String> imageSet = new HashSet<>();
        CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = workLoadBean.getCmsMtPlatformCategorySchemaModel();
        ExpressionParser expressionParser = tcb.getExpressionParser();
        CmsMtPlatformMappingModel cmsMtPlatformMappingModel = workLoadBean.getCmsMtPlatformMappingModel();

        Map<String, Field> fieldMap;
        String itemSchema = cmsMtPlatformCategorySchemaModel.getPropsItem();
        try {
            fieldMap = schemaToIdPropMap(itemSchema);
        } catch (TopSchemaException e) {
            logger.error(e.getMessage(), e);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("Can't convert schema to fields: " + e.getMessage()));
        }
        tmallUploadRunState.getContextBuildFields().setFieldMap(fieldMap);
        constructCustomPlatformProps(tcb, fieldMap, expressionParser, imageSet);
        constructMappingPlatformProps(tcb, fieldMap, cmsMtPlatformMappingModel, expressionParser, imageSet);

        //如果imageSet不为空，就去上传图片
        if (imageSet.size() != 0)
        {
            //construct uploadImageParam
            UploadImageParam uploadImageParam = new UploadImageParam();
            uploadImageParam.setSrcUrlSet(imageSet);
            uploadImageParam.setShopBean(shopBean);

            //save UploadProductHandler status
            tmallWorkloadStatus.setValue(TmallWorkloadStatus.UPDATE_WAIT_ITEM_PIC);

            //create UploadImageHandler Tcb
            UploadImageTcb uploadImageTcb = new UploadImageTcb(uploadImageParam);
            uploadImageTcb.setUploadProductTcb(tcb);
            tcb.setUploadImageTcb(uploadImageTcb);

            //suspend UploadProductHandler's current tcb and resume uploadImage
            throw new TaskSignal(TaskSignalType.MainTaskToUploadImage, new MainToUploadImageTaskSignalInfo(tcb, uploadImageTcb));
        }

        List<Field> itemFields = resolveMappingProps(tmallUploadRunState, null);

        //在临上新的前一刻，再次做库存更新
        /*
        TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields = contextBeforeUploadImage.getContextBuildCustomFields();
        List<Field> customFields = contextBuildCustomFields.getCustomFields();
        AbstractSkuFieldBuilder skuFieldBuilder = contextBuildCustomFields.getSkuFieldBuilder();
        int totalInventory = skuFieldBuilder.updateInventoryField(workLoadBean.getOrder_channel_id(), contextBuildCustomFields, customFields);
        InputField quantityField = contextBuildCustomFields.getQuantityField();
        if (quantityField != null) {
            quantityField.setValue(String.valueOf(totalInventory));
            logger.info("商品数量设为：" + quantityField.getValue());
        }
        */

        try {
            logger.debug("updateTmallItem: [productCode:" + productId + ", categoryCode:" + categoryCode + ", numIId:" + numId + "]");
            numId = updateTmallItem(productId, numId, categoryCode, itemFields, shopBean);
            tcb.setNumId(numId);
        } catch (ApiException e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }
        throw new TaskSignal(TaskSignalType.DONE, null);
    }

    private void updateProductForStatusItemImageUploaded(UploadProductTcb tcb) throws TaskSignal {
        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        Long categoryCode = tmallUploadRunState.getCategory_code();

        String productId = workLoadBean.getProductId();
        String numId = workLoadBean.getNumId();

        UploadImageResult uploadImageResult = tcb.getUploadImageResult();

        ShopBean shopBean = Shops.getShop(workLoadBean.getOrder_channel_id(), String.valueOf(workLoadBean.getCart_id()));


        Map<String, String> urlMap;
        if (uploadImageResult.isUploadSuccess()) {
            urlMap = uploadImageResult.getUrlMap();
        } else {
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(uploadImageResult.getFailCause(), uploadImageResult.isNextProcess()));
        }

        List<Field> itemFields = resolveMappingProps(tmallUploadRunState, urlMap);

        //在临上新的前一刻，再次做库存更新
        /*
        TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields = contextBeforeUploadImage.getContextBuildCustomFields();
        AbstractSkuFieldBuilder skuFieldBuilder = contextBuildCustomFields.getSkuFieldBuilder();
        List<Field> customFields = contextBeforeUploadImage.getCustomFields();
        int totalInventory;
        if (skuFieldBuilder != null) {
            totalInventory = skuFieldBuilder.updateInventoryField(workLoadBean.getOrder_channel_id(), contextBuildCustomFields, customFields);
        }
        else {
            totalInventory = calcTotalInventory(workLoadBean.getOrder_channel_id(), workLoadBean.getCmsModelProp());
        }
        InputField quantityField = contextBuildCustomFields.getQuantityField();
        if (quantityField != null) {
            quantityField.setValue(String.valueOf(totalInventory));
            logger.info("商品数量设为：" + quantityField.getValue());
        }
        */

        try {
            logger.debug("updateTmallItem: [productCode:" + productId + ", categoryCode:" + categoryCode + "]");
            numId = updateTmallItem(productId, numId, categoryCode, itemFields, shopBean);
            tcb.setNumId(numId);
        } catch (ApiException e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }

        logger.info("Success to update item!");
        throw new TaskSignal(TaskSignalType.DONE, null);
    }

    private String updateTmallItem(String productId, String numId, Long categoryCode, List<Field> itemFields, ShopBean shopBean) throws TaskSignal, ApiException {
        String xmlData;

        try {
            xmlData = SchemaWriter.writeParamXmlString(itemFields);
        } catch (TopSchemaException e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }
        StringBuffer failCause = new StringBuffer();
        logger.debug("tmall category code:" + categoryCode);
        logger.debug("numId:" + numId);
        logger.debug("xmlData:" + xmlData);
        TmallItemSchemaUpdateResponse updateItemResponse = tbProductService.updateItem(productId, numId, categoryCode, xmlData, shopBean);
        if (updateItemResponse == null) {
            failCause.append("Tmall return null response when update item");
            logger.error(failCause + ", request:" + xmlData);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(failCause.toString()));
        } else if (updateItemResponse.getErrorCode() != null) {
            logger.debug("errorCode:" + updateItemResponse.getErrorCode());
            String subMsg = updateItemResponse.getSubMsg();
            numId = getNumIdFromSubMsg(subMsg, failCause);
            if (numId != null && !"".equals(numId)) {
                logger.debug(String.format("find numId(%s) has been uploaded before", numId));
                return numId;
            }
            //天猫系统服务异常
            if (failCause.indexOf("天猫商品服务异常") != -1
                    || failCause.indexOf("访问淘宝超时") != -1) {
                logger.debug("此处应该是下次启动任务仍需处理的错误--->" + failCause.toString());
                throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(failCause.toString(), true));
            }
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(failCause.toString()));
        } else {
            numId = updateItemResponse.getUpdateItemResult();
            logger.debug("numId: " + numId);
            return numId;
        }
    }

    /**
     * 价格的计算方法为：
     *  计算最高价格，库存为0的sku不参与计算
     *  如果所有sku库存都为0， 第一个的价格作为商品价格
     */
    private double calcItemPrice(List<SxProductBean> sxProducts, Map<String, Integer> skuInventoryMap,
                                 String channelId, int cartId) {
        // 价格有可能是用priceSale, 也有可能用priceMsrp, 所以需要判断一下 tom START
        CmsChannelConfigBean sxPriceConfig = CmsChannelConfigs.getConfigBean(channelId, "PRICE", String.valueOf(cartId) + ".sx_price");

        // 检查一下
        String sxPricePropName;
        if (sxPriceConfig == null) {
            return 0d;
        } else {
            sxPricePropName = sxPriceConfig.getConfigValue1();
            if (StringUtils.isEmpty(sxPricePropName)) {
                return 0d;
            }
        }
        // 价格有可能是用priceSale, 也有可能用priceMsrp, 所以需要判断一下 tom END

        Double resultPrice = 0d, onePrice = 0d;
        List<Double> skuPriceList = new ArrayList<>();
        for (SxProductBean sxProduct : sxProducts) {
            CmsBtProductModel cmsProduct = sxProduct.getCmsBtProductModel();
            for (CmsBtProductModel_Sku cmsBtProductModelSku : cmsProduct.getSkus()) {
                int skuQuantity = 0;
                Integer skuQuantityInteger = skuInventoryMap.get(cmsBtProductModelSku.getSkuCode());
                if (skuQuantityInteger != null) {
                    skuQuantity = skuQuantityInteger;
                }
                double skuPrice = 0;
                try {
//                    skuPrice = Double.valueOf(cmsBtProductModelSku.getPriceSale());
                    skuPrice = Double.valueOf(cmsBtProductModelSku.getAttribute(sxPricePropName).toString());
                } catch (Exception e) {
                    logger.warn("No price for sku " + cmsBtProductModelSku.getSkuCode());
                }
                if (onePrice - 0d == 0) {
                    onePrice = skuPrice;
                }
                if (skuQuantity > 0)  {
                    skuPriceList.add(skuPrice);
                }
            }
        }

        for (double skuPrice : skuPriceList) {
            resultPrice = Double.max(resultPrice, skuPrice);
        }
        if (resultPrice - 0d == 0) {
            resultPrice = onePrice;
        }

        return resultPrice;
    }

    public void constructCustomPlatformProps(UploadProductTcb tcb, Map<String, Field> fieldMap, ExpressionParser expressionParser, Set<String> imageSet) throws TaskSignal {
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        TmallUploadRunState.TmallContextBuildFields contextBuildFields =
                tmallUploadRunState.getContextBuildFields();
        TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields = contextBuildFields.getContextBuildCustomFields();
        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        SxProductBean mainSxProduct = workLoadBean.getMainProduct();

        //第一步，先从cms_mt_platform_prop_mapping从查找，该属性是否在范围，如果在，那么采用特殊处理
        List<CustomPlatformPropMappingModel> customPlatformPropMappingModels = platformPropCustomMappingDao.getCustomMappingPlatformProps(workLoadBean.getCart_id());

        Map<CustomMappingType, List<Field>> mappingTypePropsMap = new HashMap<>();

        for (CustomPlatformPropMappingModel customPlatformPropMappingModel : customPlatformPropMappingModels) {
            Field field = fieldMap.get(customPlatformPropMappingModel.getPlatformPropId());
            if (field != null) {
                List<Field> mappingPlatformPropBeans = mappingTypePropsMap.get(customPlatformPropMappingModel.getCustomMappingType());
                if (mappingPlatformPropBeans == null) {
                    mappingPlatformPropBeans = new ArrayList<>();
                    mappingTypePropsMap.put(customPlatformPropMappingModel.getCustomMappingType(), mappingPlatformPropBeans);
                }
                fieldMap.remove(customPlatformPropMappingModel.getPlatformPropId());
                mappingPlatformPropBeans.add(field);
            }
        }

        //品牌
        for (Map.Entry<CustomMappingType, List<Field>> entry : mappingTypePropsMap.entrySet()) {
            CustomMappingType customMappingType = entry.getKey();
            List<Field> processFields = entry.getValue();
            switch (customMappingType) {
                case BRAND_INFO: {
                    String brandCode = tmallUploadRunState.getBrand_code();
                    Field field = processFields.get(0);

                    if (field.getType() != FieldTypeEnum.SINGLECHECK) {
                        logger.error("tmall's brand field(" + field.getId() + ") must be singleCheck");
                    } else {
                        SingleCheckField singleCheckField = (SingleCheckField) field;
                        singleCheckField.setValue(brandCode);
                        contextBuildFields.addCustomField(singleCheckField);
                    }
                    break;
                }
                case SKU_INFO:
                {
                    int cartId = workLoadBean.getCart_id();
                    String categoryCode = String.valueOf(tmallUploadRunState.getCategory_code());

                    workLoadBean.setHasSku(true);

                    List<Field> allSkuFields = new ArrayList<>();
                    recursiveGetFields(processFields, allSkuFields);
                    AbstractSkuFieldBuilder skuFieldBuilder = skuFieldBuilderFactory.getSkuFieldBuilder(cartId, allSkuFields);
                    if (skuFieldBuilder == null)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("No sku builder find"));
                    }

                    skuFieldBuilder.setExpressionParser(expressionParser);

                    DictWordBean dictWordBean = dictWordDao.selectDictWordByName(workLoadBean.getOrder_channel_id(), "属性图片模板");
                    RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
                    DictWord dictWord = (DictWord) ruleJsonMapper.deserializeRuleWord(dictWordBean.getValue());
                    String codePropImageTemplate = expressionParser.parse(dictWord.getExpression(), null);
                    skuFieldBuilder.setCodeImageTemplete(codePropImageTemplate);

                    List<Field> skuInfoFields = skuFieldBuilder.buildSkuInfoField(cartId, categoryCode, processFields,
                            workLoadBean.getProcessProducts(), workLoadBean.getSkuProductMap(),
                            workLoadBean.getCmsMtPlatformMappingModel(), workLoadBean.getSkuInventoryMap(),
                            contextBuildCustomFields, imageSet);

                    if (skuInfoFields == null)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("Can't build SkuInfoField"));
                    }
                    contextBuildFields.getCustomFields().addAll(skuInfoFields);
                    contextBuildCustomFields.setSkuFieldBuilder(skuFieldBuilder);
                    break;
                }
                case PRICE_SECTION:
                {
                    if (processFields == null || processFields.size() != 1)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("price_section's platformProps must have only one prop!"));
                    }
                    SingleCheckField priceField = (SingleCheckField) processFields.get(0);
                    List<PriceSectionBuilder.PriceOption> priceOptions = PriceSectionBuilder.transferFromTmall(priceField.getOptions());
                    double usePrice = (Double) mainSxProduct.getCmsBtProductModel().getGroups().get("priceSaleSt");

                    String priceSectionValue = priceSectionBuilder.autoDetectOptionValue(priceOptions, usePrice);
                    priceField.setValue(priceSectionValue);

                    contextBuildFields.addCustomField(priceField);
                    break;
                }
                case TMALL_SERVICE_VERSION:
                {
                    if (processFields == null || processFields.size() != 1)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("tmall service version's platformProps must have only one prop!"));
                    }
                    InputField  field = (InputField) processFields.get(0);
                    field.setValue("11100");
                    contextBuildFields.addCustomField(field);
                    break;
                }
                case TMALL_STYLE_CODE:
                {
                    if (processFields == null || processFields.size() != 1)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("tmall style code's platformProps must have only one prop!"));
                    }
                    if (processFields.get(0).getType() != FieldTypeEnum.INPUT) {
                        break;
                    }
                    InputField  field = (InputField) processFields.get(0);

                    String styleCode = tmallUploadRunState.getStyle_code();
                    if (styleCode == null || "".equals(styleCode)) {
                        styleCode = generateStyleCode(tcb);
                        tmallUploadRunState.setStyle_code(styleCode);
                    }
                    // 测试代码不要提交 tom start
//                    styleCode = "test." + styleCode;
                    // 测试代码不要提交 tom end
                    field.setValue(styleCode);
                    logger.debug("tmall style code[" + field.getId() + "]: " + field.getValue());
                    contextBuildFields.addCustomField(field);
                    break;
                }
                case TMALL_ITEM_QUANTITY:
                {
                    if (processFields == null || processFields.size() != 1)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("tmall item quantity's platformProps must have only one prop!"));
                    }

                    InputField processField = (InputField) processFields.get(0);
                    //初始值先设为0，等到库存更新之后，重新更新他的值
                    Map<String, Integer> skuInventoryMap = workLoadBean.getSkuInventoryMap();
                    int totalInventory = 0;
                    for (Map.Entry<String, Integer> skuInventoryEntry : skuInventoryMap.entrySet()) {
                        totalInventory += skuInventoryEntry.getValue();
                    }
                    processField.setValue(String.valueOf(totalInventory));
                    contextBuildCustomFields.setQuantityField(processField);
                    contextBuildFields.addCustomField(processField);
                    break;
                }
                case TMALL_ITEM_PRICE:
                {
                    if (processFields == null || processFields.size() != 1)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("tmall item price's platformProps must have only one prop!"));
                    }
                    InputField  itemPriceField = (InputField) processFields.get(0);

//                    double itemPrice = calcItemPrice(workLoadBean.getProcessProducts(), workLoadBean.getSkuInventoryMap());
                    double itemPrice = calcItemPrice(workLoadBean.getProcessProducts(), workLoadBean.getSkuInventoryMap(), workLoadBean.getOrder_channel_id(), workLoadBean.getCart_id());
                    itemPriceField.setValue(String.valueOf(itemPrice));

                    contextBuildCustomFields.setPriceField(itemPriceField);
                    contextBuildFields.addCustomField(itemPriceField);
                    break;
                }
                case TMALL_XINGHAO:
                {
                    if (processFields == null || processFields.size() != 2)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("tmall item xinghao's platformProps must have two props!"));
                    }

                    for (Field processField : processFields) {
                        if (processField.getType() == FieldTypeEnum.SINGLECHECK) {
                            SingleCheckField field = (SingleCheckField) FieldTypeEnum.createField(FieldTypeEnum.SINGLECHECK);
                            //prop_1626510（型号）值设为-1(表示其他）
                            field.setValue("-1");
                            contextBuildFields.addCustomField(field);
                        }
                        else {
                            //其他的型号值填货号
                            InputField field = (InputField) FieldTypeEnum.createField(FieldTypeEnum.INPUT);

                            String styleCode = tmallUploadRunState.getStyle_code();
                            if (styleCode == null || "".equals(styleCode)) {
                                styleCode = generateStyleCode(tcb);
                                tmallUploadRunState.setStyle_code(styleCode);
                            }

                            field.setValue(styleCode);
                            contextBuildFields.addCustomField(field);
                        }
                    }
                    break;
                }
                case TMALL_OUT_ID: {
                    if (processFields == null || processFields.size() != 1)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("tmall item outId's platformProps must have one prop!"));
                    }
                    boolean hasSku = false;
                    for (CustomMappingType customMappingIter : mappingTypePropsMap.keySet()) {
                        if (customMappingIter == CustomMappingType.SKU_INFO) {
                            hasSku = true;
                            break;
                        }
                    }
                    if (hasSku) {
                        logger.info("已经有sku属性，忽略商品外部编码");
                        continue;
                    }
                    // bug修正 tom START
//                    InputField  field = (InputField) processFields;

                    InputField  field = (InputField) (processFields.get(0));
                    // bug修正 tom END
                    List<SxProductBean> processProducts = workLoadBean.getProcessProducts();
                    if (processProducts.size() != 1) {
                        String errorCause = "包含商品外部编码的类目必须只有一个code";
                        logger.error(errorCause);
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(errorCause));
                    }
                    SxProductBean sxProductBean = processProducts.get(0);
                    List<CmsBtProductModel_Sku> cmsBtProductModelSkus = sxProductBean.getCmsBtProductModel().getSkus();
                    if (cmsBtProductModelSkus.size() != 1) {
                        String errorCause = "包含商品外部编码的类目必须只有一个sku";
                        logger.error(errorCause);
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(errorCause));
                    }
                    field.setValue(cmsBtProductModelSkus.get(0).getSkuCode());
                    contextBuildFields.addCustomField(field);
                    break;
                }
                case TMALL_SHOP_CATEGORY:
                {
                    if (processFields == null || processFields.size() != 1)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("tmall item shop_category's platformProps must have one prop!"));
                    }

                    Field processField = processFields.get(0);
                    String platformPropId = processField.getId();
                    List<ConditionPropValueModel> conditionPropValueModels = conditionPropValueRepo.get(workLoadBean.getOrder_channel_id(), platformPropId);

                    //优先使用条件表达式
                    if (conditionPropValueModels != null && !conditionPropValueModels.isEmpty()) {
                            RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
                            for (ConditionPropValueModel conditionPropValueModel : conditionPropValueModels) {
                                String conditionExpressionStr = conditionPropValueModel.getCondition_expression();
                                RuleExpression conditionExpression= ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
                                String propValue = expressionParser.parse(conditionExpression, null);
                                if (propValue != null) {
                                    ((MultiCheckField)processField).addValue(propValue);
                                }
                            }
                            contextBuildFields.addCustomField(processField);
                    } else {
                        final String sellerCategoryPropId = "seller_cids";
                        if (workLoadBean.getUpJobParam().getMethod() == UpJobParamBean.METHOD_UPDATE) {
                            String numId = workLoadBean.getNumId();
                            ShopBean shopBean = Shops.getShop(workLoadBean.getOrder_channel_id(), String.valueOf(workLoadBean.getCart_id()));
                            try {
                                TmallItemUpdateSchemaGetResponse response = tbProductService.doGetWareInfoItem(numId, shopBean);
                                String strXml = response.getUpdateItemResult();
                                // 读入的属性列表
                                List<Field> fieldList = null;
                                fieldList = SchemaReader.readXmlForList(strXml);
                                List<String> defaultValues = null;
                                for (Field field : fieldList) {
                                    if (sellerCategoryPropId.equals(field.getId())) {
                                        MultiCheckField multiCheckField = (MultiCheckField) field;
                                        defaultValues = multiCheckField.getDefaultValues();
                                        break;
                                    }
                                }
                                if (defaultValues != null) {
                                    MultiCheckField field = (MultiCheckField) FieldTypeEnum.createField(FieldTypeEnum.MULTICHECK);
                                    field.setId(sellerCategoryPropId);
                                    for (String defaultValue : defaultValues) {
                                        field.addValue(defaultValue);
                                    }
                                    contextBuildFields.addCustomField(field);
                                }
                            } catch (TopSchemaException | ApiException e) {
                                logger.error(e.getMessage(), e);
                            }
                        }
                    }
                    break;
                }
                case ITEM_STATUS: {
                    if (processFields == null || processFields.size() != 1)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("tmall item shop_category's platformProps must have one prop!"));
                    }

                    Field processField = processFields.get(0);
                    CmsConstants.PlatformActive platformActive = mainSxProduct.getCmsBtProductModelGroupPlatform().getPlatformActive();
                    if (platformActive == CmsConstants.PlatformActive.Onsale) {
                        ((SingleCheckField) processField).setValue("0");
                    } else if (platformActive == CmsConstants.PlatformActive.Instock) {
                        ((SingleCheckField) processField).setValue("2");
                    } else {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("PlatformActive must be Onsale or Instock, but now it is " + platformActive));
                    }
                    contextBuildFields.addCustomField(processField);
                    break;
                }
            }
        }
    }


    public Map<String, Field> schemaToIdPropMap(String schema) throws TopSchemaException {
        Map<String, Field> fieldsMap = new HashMap<>();
        List<Field> fields = SchemaReader.readXmlForList(schema);

        for (Field field : fields)
        {
            fieldsMap.put(field.getId(), field);
        }
        return fieldsMap;
    }

    public void constructMappingPlatformProps(UploadProductTcb tcb, Map<String, Field> fieldMap, CmsMtPlatformMappingModel cmsMtPlatformMappingModel, ExpressionParser expressionParser, Set<String> imageSet) throws TaskSignal {
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        SxProductBean sxProductBean = workLoadBean.getMainProduct();
        CmsBtProductModel cmsMainProduct = sxProductBean.getCmsBtProductModel();
        List<Field> mappingFields = tmallUploadRunState.getContextBuildFields().getMappingFields();
        Map<String, List<TmallUploadRunState.UrlStashEntity>> srcUrlStashEntityMap = tmallUploadRunState.getContextBuildFields().getSrcUrlStashEntityMap();

        List<MappingBean> propMapings = cmsMtPlatformMappingModel.getProps();

        for (MappingBean mappingBean : propMapings) {
            Field field = fieldMap.get(mappingBean.getPlatformPropId());
            if (field == null) {
                continue;
            }

            if ("description".equals(field.getId())) {
                System.out.println();
            }
            Field resolveField = (resolveMapping(cmsMainProduct, mappingBean, field, srcUrlStashEntityMap, expressionParser, imageSet));
            if (resolveField != null) {
                mappingFields.add(resolveField);
            }
        }
    }

    public Field resolveMapping(CmsBtProductModel cmsMainProduct, MappingBean mappingBean, Field field, Map<String, List<TmallUploadRunState.UrlStashEntity>> srcUrlStashEntityMap, ExpressionParser expressionParser, Set<String> imageSet) throws TaskSignal{
        Set<String> imageSetEachProp = new HashSet<>();

        if (MappingBean.MAPPING_SIMPLE.equals(mappingBean.getMappingType())) {
            SimpleMappingBean simpleMappingBean = (SimpleMappingBean) mappingBean;
            String expressionValue = expressionParser.parse(simpleMappingBean.getExpression(), imageSetEachProp);
            if (null == expressionValue) {
                return null;
            }
            imageSet.addAll(imageSetEachProp);

            switch (field.getType()) {
                case INPUT: {
                    ((InputField) field).setValue(expressionValue);
                    for (String imageUrl : imageSetEachProp) {
                        List<TmallUploadRunState.UrlStashEntity> destUrlStashEntities = srcUrlStashEntityMap.get(imageUrl);
                        if (destUrlStashEntities == null) {
                            destUrlStashEntities = new ArrayList<>();
                            srcUrlStashEntityMap.put(imageUrl, destUrlStashEntities);
                        }
                        destUrlStashEntities.add(new TmallUploadRunState.UrlStashEntity(field.getId(), field));
                    }
                    break;
                }
                case SINGLECHECK: {
                    ((SingleCheckField) field).setValue(expressionValue);
                    break;
                }
                case MULTIINPUT:
                    break;
                case MULTICHECK: {
                    String[] valueArrays = ExpressionParser.decodeString(expressionValue);
                    for (String value : valueArrays) {
                        ((MultiCheckField) field).addValue(value);
                    }
                    break;
                }
                case COMPLEX:
                    break;
                case MULTICOMPLEX:
                    break;
                case LABEL:
                    break;
                default:
                    logger.error("复杂类型的属性:" + field.getType() + "不能使用MAPPING_SINGLE来作为匹配类型");
                    return null;
            }
        } else if (MappingBean.MAPPING_COMPLEX.equals(mappingBean.getMappingType())) {
            ComplexMappingBean complexMappingBean = (ComplexMappingBean) mappingBean;
            if (field.getType() == FieldTypeEnum.COMPLEX) {
                Map<String, Object> masterWordEvaluationContext = (Map<String, Object>) cmsMainProduct.getFields().get(complexMappingBean.getMasterPropId());
                if (masterWordEvaluationContext != null) {
                    expressionParser.pushMasterPropContext(masterWordEvaluationContext);
                }

                ComplexField complexField = (ComplexField) field;
                ComplexValue complexValue = new ComplexValue();
                complexField.setComplexValue(complexValue);

                for (MappingBean subMappingBean : complexMappingBean.getSubMappings()) {
                    String platformPropId = subMappingBean.getPlatformPropId();
                    Map<String, Field> schemaFieldsMap = complexField.getFieldMap();

                    Field schemaField = schemaFieldsMap.get(platformPropId);
                    if (schemaField == null) {
                        logger.warn("有" + platformPropId + "的mapping关系却没有该属性");
                        logger.warn("跳过属性" + platformPropId);
                        continue;
                    }
                    Field valueField = deepCloneField(schemaField);
                    resolveMapping(cmsMainProduct, subMappingBean, valueField, srcUrlStashEntityMap, expressionParser, imageSet);
                    complexValue.put(valueField);
                }
                if (masterWordEvaluationContext != null) {
                    expressionParser.popMasterPropContext();
                }
            } else if (field.getType() == FieldTypeEnum.MULTICOMPLEX) {
                List<Map<String, Object>> masterWordEvaluationContexts = (List<Map<String, Object>>) cmsMainProduct.getFields().get(complexMappingBean.getMasterPropId());

                if (masterWordEvaluationContexts == null || masterWordEvaluationContexts.isEmpty()) {
                    logger.info("No value found for MultiComplex field: " + field.getId());
                    return field;
                }

                MultiComplexField multiComplexField = (MultiComplexField) field;
                List<ComplexValue> complexValues = new ArrayList<>();
                multiComplexField.setComplexValues(complexValues);

                for (Map<String, Object> masterWordEvaluationContext : masterWordEvaluationContexts) {
                    expressionParser.pushMasterPropContext(masterWordEvaluationContext);
                    ComplexValue complexValue = new ComplexValue();
                    complexValues.add(complexValue);

                    for (MappingBean subMappingBean : complexMappingBean.getSubMappings()) {
                        String platformPropId = subMappingBean.getPlatformPropId();
                        Map<String, Field> schemaFieldsMap = multiComplexField.getFieldMap();

                        Field schemaField = schemaFieldsMap.get(platformPropId);
                        Field valueField = deepCloneField(schemaField);
                        resolveMapping(cmsMainProduct, subMappingBean, valueField, srcUrlStashEntityMap, expressionParser, imageSet);
                        complexValue.put(valueField);
                    }

                    expressionParser.popMasterPropContext();
                }


//                if (masterWordEvaluationContexts == null || masterWordEvaluationContexts.isEmpty()) {
//                    MultiComplexField multiComplexField = (MultiComplexField) field;
//                    List<ComplexValue> complexValues = new ArrayList<>();
//                    multiComplexField.setComplexValues(complexValues);
//
//                    expressionParser.pushMasterPropContext(null);
//                    ComplexValue complexValue = new ComplexValue();
//                    complexValues.add(complexValue);
//
//                    for (MappingBean subMappingBean : complexMappingBean.getSubMappings()) {
//                        String platformPropId = subMappingBean.getPlatformPropId();
//                        Map<String, Field> schemaFieldsMap = multiComplexField.getFieldMap();
//
//                        Field schemaField = schemaFieldsMap.get(platformPropId);
//                        Field valueField = deepCloneField(schemaField);
//                        resolveMapping(cmsMainProduct, subMappingBean, valueField, srcUrlStashEntityMap, expressionParser, imageSet);
//                        complexValue.put(valueField);
//                    }
//
//                    expressionParser.popMasterPropContext();
//
//                } else {
//                    MultiComplexField multiComplexField = (MultiComplexField) field;
//                    List<ComplexValue> complexValues = new ArrayList<>();
//                    multiComplexField.setComplexValues(complexValues);
//
//                    for (Map<String, Object> masterWordEvaluationContext : masterWordEvaluationContexts) {
//                        expressionParser.pushMasterPropContext(masterWordEvaluationContext);
//                        ComplexValue complexValue = new ComplexValue();
//                        complexValues.add(complexValue);
//
//                        for (MappingBean subMappingBean : complexMappingBean.getSubMappings()) {
//                            String platformPropId = subMappingBean.getPlatformPropId();
//                            Map<String, Field> schemaFieldsMap = multiComplexField.getFieldMap();
//
//                            Field schemaField = schemaFieldsMap.get(platformPropId);
//                            Field valueField = deepCloneField(schemaField);
//                            resolveMapping(cmsMainProduct, subMappingBean, valueField, srcUrlStashEntityMap, expressionParser, imageSet);
//                            complexValue.put(valueField);
//                        }
//
//                        expressionParser.popMasterPropContext();
//                    }
//                }


            } else {
                logger.error("Unexpected field type: " + field.getType());
                return null;
            }
        } else if (MappingBean.MAPPING_MULTICOMPLEX_CUSTOM.equals(mappingBean.getMappingType())) {
            MultiComplexCustomMappingBean multiComplexCustomMappingBean = (MultiComplexCustomMappingBean) mappingBean;
            MultiComplexField multiComplexField = (MultiComplexField) field;
            List<ComplexValue> complexValues = new ArrayList<>();
            for (MultiComplexCustomMappingValue multiComplexCustomMappingValue : multiComplexCustomMappingBean.getValues()) {
                ComplexValue complexValue = new ComplexValue();
                for (MappingBean subMapping : multiComplexCustomMappingValue.getSubMappings()) {
                    String platformPropId = subMapping.getPlatformPropId();
                    Map<String, Field> schemaFieldsMap = multiComplexField.getFieldMap();

                    Field schemaField = schemaFieldsMap.get(platformPropId);
                    Field valueField = deepCloneField(schemaField);
                    resolveMapping(cmsMainProduct, subMapping, valueField, srcUrlStashEntityMap, expressionParser, imageSet);
                    complexValue.put(valueField);
                }
                complexValues.add(complexValue);
            }
            multiComplexField.setComplexValues(complexValues);
        }
        return field;
    }

    public List resolveMappingProps(PlatformUploadRunState platformUploadRunState, Map<String, String> urlMap) {
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) platformUploadRunState;
        List<Field> mappingFields = tmallUploadRunState.getContextBuildFields().getMappingFields();
        List<Field> customFields = tmallUploadRunState.getContextBuildFields().getCustomFields();

        List<Field> allFields = new ArrayList<>();
        allFields.addAll(mappingFields);
        allFields.addAll(customFields);

        Map<String, List<TmallUploadRunState.UrlStashEntity>> srcUrlStashEntityMap = tmallUploadRunState.getContextBuildFields().getSrcUrlStashEntityMap();

        if (urlMap == null || urlMap.isEmpty()) {
            return allFields;
        }

        for (Map.Entry<String, List<TmallUploadRunState.UrlStashEntity>> entityEntry : srcUrlStashEntityMap.entrySet()) {
            String srcUrl = entityEntry.getKey();
            for (TmallUploadRunState.UrlStashEntity urlStashEntity : entityEntry.getValue()) {
                Object stashObj = urlStashEntity.getStashObj();
                String propId = urlStashEntity.getFieldId();
                if (stashObj instanceof InputField) {
                    InputField stashInputField = (InputField) stashObj;
                    String oldValue = stashInputField.getValue();
                    stashInputField.setValue(oldValue.replace(srcUrl, urlMap.get(srcUrl)));
                } else if (stashObj instanceof ComplexValue) {
                    ComplexValue stashComplexValue = (ComplexValue) stashObj;
                    String oldValue = stashComplexValue.getValue(propId).getValue();
                    String newValue = oldValue.replace(srcUrl, urlMap.get(srcUrl));
                    stashComplexValue.setInputFieldValue(propId, newValue);
                } else {
                    logger.error("Unexpected stashObj type:" + stashObj.getClass().getName());
                    return null;
                }
            }
        }
        return allFields;
    }

    private void recursiveGetFields(List<Field> fields, List<Field> resultFields) {
        for (Field field : fields) {
            switch (field.getType()) {
                case COMPLEX:
                    recursiveGetFields(((ComplexField)field).getFieldList(), resultFields);
                    resultFields.add(field);
                    break;
                case MULTICOMPLEX:
                    recursiveGetFields(((MultiComplexField)field).getFieldList(), resultFields);
                    resultFields.add(field);
                    break;
                default:
                    resultFields.add(field);
            }
        }
    }


    private Field deepCloneField(Field field) throws TaskSignal {
        try {
            return SchemaReader.elementToField(field.toElement());
        } catch (Exception e) {
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }
    }
}
