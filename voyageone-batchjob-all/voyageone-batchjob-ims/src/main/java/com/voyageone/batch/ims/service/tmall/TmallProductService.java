package com.voyageone.batch.ims.service.tmall;

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
import com.voyageone.batch.ims.ImsConstants;
import com.voyageone.batch.ims.bean.*;
import com.voyageone.batch.ims.bean.tcb.*;
import com.voyageone.batch.ims.dao.*;
import com.voyageone.batch.ims.enums.PlatformWorkloadStatus;
import com.voyageone.batch.ims.enums.TmallWorkloadStatus;
import com.voyageone.batch.ims.modelbean.*;
import com.voyageone.batch.ims.service.*;
import com.voyageone.batch.ims.service.rule_parser.ExpressionParser;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.tmall.TbProductService;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.ims.enums.CmsFieldEnum;
import com.voyageone.ims.modelbean.DictWordBean;
import com.voyageone.ims.rule_expression.DictWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Leo on 2015/5/28.
 */
@Repository
public class TmallProductService implements PlatformServiceInterface {
    private static Log logger = LogFactory.getLog(TmallProductService.class);
    @Autowired
    TbProductService tbProductService;
    @Autowired
    private BrandMapDao brandMapDao;
    @Autowired
    private PlatformPropDao platformPropDao;
    @Autowired
    private MasterDataMappingService masterDataMappingService;
    @Autowired
    private PropValueDao propValueDao;
    @Autowired
    private PropDao propDao;
    @Autowired
    private SkuFieldBuilderFactory skuFieldBuilderFactory;
    @Autowired
    private PriceSectionBuilder priceSectionBuilder;
    @Autowired
    private SkuInfoDao skuInfoDao;
    @Autowired
    private DictWordDao dictWordDao;
    @Autowired
    private IssueLog issueLog;
    @Autowired
    private DarwinStyleMappingDao darwinStyleMappingDao;
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
     * @param tcb
     * @throws TaskSignal
     */
    private void addProductForStatusInit(UploadProductTcb tcb) throws TaskSignal {
        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        ShopBean shopBean = ShopConfigs.getShop(workLoadBean.getOrder_channel_id(),
                String.valueOf(workLoadBean.getCart_id()));

        TmallUploadRunState tmallUploadRunState = new TmallUploadRunState(tcb);
        //款号也有可能从workload带过来，因为如果是达尔文，可能任务被拆分，拆分的子任务会带上款号
        tmallUploadRunState.setStyle_code(workLoadBean.getStyle_code());
        CmsModelPropBean cmsModelProp = workLoadBean.getCmsModelProp();
        TmallWorkloadStatus tmallWorkloadStatus = new TmallWorkloadStatus(workLoadBean.getWorkload_status());
        workLoadBean.setWorkload_status(tmallWorkloadStatus);

        //开始上传产品，预先创建product run state, 为保存状态做状态
        if (tcb.getPlatformUploadRunState() == null)
        {
            tcb.setPlatformUploadRunState(tmallUploadRunState);
        }

        Long categoryCode = Long.valueOf(tcb.getPlatformCId());
        String brandCode = brandMapDao.cmsBrandToPlatformBrand(workLoadBean.getOrder_channel_id(),
                workLoadBean.getCart_id(), Integer.parseInt(cmsModelProp.getProp(CmsFieldEnum.CmsModelEnum.brand)));

        if (brandCode == null || "".equals(brandCode))
        {
            String abortCause = "Job abort: can not find brand_code by brandId "
                    + cmsModelProp.getProp(CmsFieldEnum.CmsModelEnum.brand)
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
            issueLog.log(e, ErrorType.BatchJob, SubSystem.IMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }
        tmallUploadRunState.setIs_darwin(isDarwin);

        //上传商品时，使用第一个code作为主商品
        workLoadBean.setMainProductProp(cmsModelProp.getCmsCodePropBeanList().get(0));
        tmallWorkloadStatus.setValue(TmallWorkloadStatus.ADD_SEARCH_PRODUCT);
    }

    /**
     * 在天猫平台上传商品时搜索产品是否已经存在
     * 1. 调用天猫API获取产品匹配的schema
     * 2. 根据天猫API的产品匹配的Schema中的字段填充值
     * 3. 调用天猫API获取该产品是否存在匹配的product
     * 4. 如果不存在product则进入上传产品状态，如果存在，则进入检查产品状态
     * @param tcb
     * @throws TaskSignal
     */
    private void addProductForStatusSearchProduct(UploadProductTcb tcb) throws TaskSignal {
        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        TmallWorkloadStatus tmallWorkloadStatus = (TmallWorkloadStatus) workLoadBean.getWorkload_status();
        long categoryCode = tmallUploadRunState.getCategory_code();
        String orderChannelId = workLoadBean.getOrder_channel_id();
        int cartId = workLoadBean.getCart_id();
        ShopBean shopBean = ShopConfigs.getShop(orderChannelId, cartId);

        List<String> productCodeList = new ArrayList<>();

        List<PlatformPropBean> platformPropBeans = new ArrayList<>();
        try {
            String schema;
            schema = tbProductService.getProductMatchSchema(categoryCode, shopBean);
            logger.debug("product_match_schema:" + schema);

            List<Field> fields = SchemaReader.readXmlForList(schema);
            for (Field field : fields)
            {
                String propId = field.getId();
                PlatformPropBean platformProp = platformPropDao.selectPlatformPropByPropId(cartId, String.valueOf(categoryCode), propId, "");
                if (platformProp != null) {
                    platformPropBeans.add(platformProp);
                }
            }
        } catch (Exception e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.IMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }

        masterDataMappingService.constructPlatformProps(this, tcb, platformPropBeans, null);
        List<Field> searchFields = (List)masterDataMappingService.resolvePlatformProps(this, tmallUploadRunState, null);


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
                //##############临时测试
                tmallWorkloadStatus.setValue(TmallWorkloadStatus.ADD_UPLOAD_PRODUCT);
//                tmallWorkloadStatus.setValue(TmallWorkloadStatus.ADD_UPLOAD_ITEM);
                tmallUploadRunState.getContextBuildFields().clearContext();
            }
        } catch (Exception e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.IMS);
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
     * @param tcb
     * @return
     * @throws TaskSignal
     */
    private String generateStyleCode(UploadProductTcb tcb) throws TaskSignal {
        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        boolean isDarwin = tmallUploadRunState.is_darwin();
        CmsModelPropBean cmsModelProp = workLoadBean.getCmsModelProp();
        List<CmsCodePropBean> cmsCodeProps = cmsModelProp.getCmsCodePropBeanList();

        if (!isDarwin) {
            String styleCode = cmsModelProp.getProp(CmsFieldEnum.CmsModelEnum.model);
            workLoadBean.setStyle_code(styleCode);
            return styleCode;
        } //如果是达尔文体系
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
            return workLoadBean.getStyle_code();
        }
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
     * @param tcb
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
                issueLog.log(e, ErrorType.BatchJob, SubSystem.IMS);
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
     * @param tcb
     * @throws TaskSignal
     */
    private void addProductForStatusUploadProduct(UploadProductTcb tcb) throws TaskSignal {
        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        TmallWorkloadStatus tmallWorkloadStatus = (TmallWorkloadStatus) workLoadBean.getWorkload_status();
        long categoryCode = tmallUploadRunState.getCategory_code();
        String brandCode = tmallUploadRunState.getBrand_code();
        String productCode = tmallUploadRunState.getProduct_code();
        ShopBean shopBean = ShopConfigs.getShop(workLoadBean.getOrder_channel_id(),
                String.valueOf(workLoadBean.getCart_id()));

        //没有找到产品id，需要重新上传Tmall产品
        if (productCode == null)
        {
            Set<String> imageSet = new HashSet<>();
            //输出参数，当构造image参数时，会填充url与它所在的field的映射关系，便于当图片上传结束时，能恢复url到字段中的值

            List<PlatformPropBean> productPlatformProps = platformPropDao.selectProductPropsByCid
                    (workLoadBean.getCart_id(), String.valueOf(categoryCode));
            masterDataMappingService.constructPlatformProps(this, tcb, productPlatformProps, imageSet);

            //如果urlSet不为空，就去上传图片
            if (imageSet.size() != 0)
            {
                //construct uploadImageParam
                UploadImageParam uploadImageParam = new UploadImageParam();
                uploadImageParam.setSrcUrlSet(imageSet);
                uploadImageParam.setShopBean(shopBean);

                //save UploadProductHandler status
                tmallWorkloadStatus.setValue(TmallWorkloadStatus.ADD_WAIT_PRODUCT_PIC);
                tmallUploadRunState.setPlatformProps(productPlatformProps);

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

            List<Field> productPlatformFields = (List)masterDataMappingService.resolvePlatformProps(this, tcb.getPlatformUploadRunState(), null);

            try {
                productCode = addTmallProduct(categoryCode, brandCode, productPlatformFields, workLoadBean);
            } catch (Exception e) {
                issueLog.log(e, ErrorType.BatchJob, SubSystem.IMS);
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
     * @param tcb
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

        List<Field> productPlatformFields = (List)masterDataMappingService.resolvePlatformProps(this, tcb.getPlatformUploadRunState(), urlMap);

        //如果上传图片失败，直接在上传图片失败时进入abort状态，就不会进入该函数，因此，此处无须判断图片是否上传成功
        String productCode;
        try {
            productCode = addTmallProduct(categoryCode, brandCode, productPlatformFields, workLoadBean);
        } catch  (Exception e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.IMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }

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
            contextBeforeUploadImage.clearContext();
            logger.info("Success to upload product, product_code: " + productCode);
        }
    }

    private void addProductForStatusUploadItem(UploadProductTcb tcb) throws TaskSignal {
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        Long categoryCode = tmallUploadRunState.getCategory_code();
        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        TmallWorkloadStatus tmallWorkloadStatus = (TmallWorkloadStatus) workLoadBean.getWorkload_status();
        ShopBean shopBean = ShopConfigs.getShop(workLoadBean.getOrder_channel_id(), String.valueOf(workLoadBean.getCart_id()));
        Set<String> imageSet = new HashSet<>();

        String  productCode = tmallUploadRunState.getProduct_code();
        workLoadBean.setProductId(productCode);

        List<PlatformPropBean> itemPlatformProps = platformPropDao.selectItemPropsByCid(workLoadBean.getCart_id(),
                String.valueOf(categoryCode));
        masterDataMappingService.constructPlatformProps(this, tcb, itemPlatformProps, imageSet);

        //如果imageSet不为空，就去上传图片
        if (imageSet.size() != 0)
        {
            //construct uploadImageParam
            UploadImageParam uploadImageParam = new UploadImageParam();
            uploadImageParam.setSrcUrlSet(imageSet);
            uploadImageParam.setShopBean(shopBean);

            //save UploadProductHandler status
            tmallWorkloadStatus.setValue(TmallWorkloadStatus.ADD_WAIT_ITEM_PIC);
            tmallUploadRunState.setPlatformProps(itemPlatformProps);

            //create UploadImageHandler Tcb
            UploadImageTcb uploadImageTcb = new UploadImageTcb(uploadImageParam);
            uploadImageTcb.setUploadProductTcb(tcb);
            tcb.setUploadImageTcb(uploadImageTcb);

            //suspend UploadProductHandler's current tcb and resume uploadImage
            throw new TaskSignal(TaskSignalType.MainTaskToUploadImage, new MainToUploadImageTaskSignalInfo(tcb, uploadImageTcb));
        }

        TmallUploadRunState.TmallContextBuildFields contextBeforeUploadImage = tmallUploadRunState.getContextBuildFields();

        List<Field> itemPlatformFields = (List)masterDataMappingService.resolvePlatformProps(this, tcb.getPlatformUploadRunState(), null);

        //在临上新的前一刻，再次做库存更新
        TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields = contextBeforeUploadImage.getContextBuildCustomFields();
        List<Field> customFields = contextBuildCustomFields.getCustomFields();
        AbstractSkuFieldBuilder skuFieldBuilder = contextBuildCustomFields.getSkuFieldBuilder();
        int totalInventory = skuFieldBuilder.updateInventoryField(workLoadBean.getOrder_channel_id(), contextBuildCustomFields, customFields);
        InputField quantityField = contextBuildCustomFields.getQuantityField();
        if (quantityField != null) {
            quantityField.setValue(String.valueOf(totalInventory));
            logger.info("商品数量设为：" + quantityField.getValue());
        }

        try {
            logger.debug("addTmallItem: [productCode:" + productCode + ", categoryCode:" + categoryCode + "]");
            String numId = addTmallItem(categoryCode, productCode, itemPlatformFields, shopBean);
            tcb.setNumId(numId);
        } catch (ApiException e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.IMS);
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
        ShopBean shopBean = ShopConfigs.getShop(workLoadBean.getOrder_channel_id(), String.valueOf(workLoadBean.getCart_id()));

        TmallUploadRunState.TmallContextBuildFields contextBeforeUploadImage = tmallUploadRunState.getContextBuildFields();

        Map<String, String> urlMap;
        if (uploadImageResult.isUploadSuccess()) {
            urlMap = uploadImageResult.getUrlMap();
        } else {
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(uploadImageResult.getFailCause(), uploadImageResult.isNextProcess()));
        }

        List<Field> itemPlatformFields = (List)masterDataMappingService.resolvePlatformProps(this, tcb.getPlatformUploadRunState(), urlMap);

        //在临上新的前一刻，再次做库存更新
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

        try {
            logger.debug("addTmallItem: [productCode:" + productCode + ", categoryCode:" + categoryCode + "]");
            String numId = addTmallItem(categoryCode, productCode, itemPlatformFields, shopBean);
            logger.debug("after addTmallItem");
            tcb.setNumId(numId);
        } catch (ApiException e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.IMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }

        logger.info("Success to upload item!");
        throw new TaskSignal(TaskSignalType.DONE, null);
    }

    private int calcTotalInventory(String orderChannelId, CmsModelPropBean cmsModelPropBean) {
        List<CmsCodePropBean> cmsCodePropBeans = cmsModelPropBean.getCmsCodePropBeanList();
        int totalInventory = 0;
        for (CmsCodePropBean cmsCodePropBean : cmsCodePropBeans) {
            String code = cmsCodePropBean.getProp(CmsFieldEnum.CmsCodeEnum.code);
            for (CmsSkuPropBean cmsSkuPropBean : cmsCodePropBean.getCmsSkuPropBeanList()) {
                String sku = cmsSkuPropBean.getProp(CmsFieldEnum.CmsSkuEnum.sku);
                String skuQuantityStr = skuInfoDao.getSkuInventory(orderChannelId, code, sku);
                int skuQuantity = 0;
                if (skuQuantityStr != null) {
                    skuQuantity = Integer.valueOf(skuQuantityStr);
                }
                totalInventory += skuQuantity;
            }
        }
        return totalInventory;
    }


    private String addTmallItem(Long categoryCode, String productCode, List<Field> itemFields, ShopBean shopBean) throws ApiException, TaskSignal {
        String xmlData;

        try {
            xmlData = SchemaWriter.writeParamXmlString(itemFields);
        } catch (TopSchemaException e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.IMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }
        StringBuffer failCause = new StringBuffer();
        logger.debug("tmall category code:" + categoryCode);
        logger.debug("xmlData:" + xmlData);
        TmallItemSchemaAddResponse addItemResponse = tbProductService.addItem(categoryCode, productCode, xmlData, shopBean);
        String numId;
        if (addItemResponse == null) {
            failCause.append(String.format("Tmall return null response when add item"));
            logger.error(failCause + ", request:" + xmlData);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(failCause.toString(), true));
        } else if (addItemResponse.getErrorCode() != null) {
            logger.debug("errorCode:" + addItemResponse.getErrorCode());
            if (addItemResponse.getSubCode().indexOf("IC_CHECKSTEP_ALREADY_EXISTS_SAME_SPU") != -1) {
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
        ShopBean shopBean = ShopConfigs.getShop(workLoadBean.getOrder_channel_id(), String.valueOf(workLoadBean.getCart_id()));
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
            //workLoadDao.updateWorkLoad(workLoadBean);
            return product_code;
        }
        else
        {
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(failCause.toString()));
        }
    }

    private String getStyleCodeFromDawinList(int cartId, String code) {
        return darwinStyleMappingDao.selectStyleCode(cartId, code);
    }

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
            issueLog.log(e, ErrorType.BatchJob, SubSystem.IMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }
        return null;
    }

    private String getProductStatus(String channel_id, String cart_id, Long product_id) throws ApiException, TopSchemaException {
        ShopBean shopBean = ShopConfigs.getShop(channel_id, cart_id);
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

    /**
     * 判断商品是否是达尔文体系
     * @param categoryId 天猫分类ID
     * @param brandId 天猫品牌ID
     * @return boolean
     */
    private boolean isDarwin(long categoryId, String brandId)
    {
        //tbCategoryService.getTbBrandCat()
        return true;
    }

    private void updateProduct(UploadProductTcb tcb, UploadProductHandler uploadProductHandler) throws TaskSignal {
        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();

        PlatformWorkloadStatus platformWorkloadStatus = workLoadBean.getWorkload_status();
        logger.debug("Update Product, workload: " + tcb.getWorkLoadBean());

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
        ShopBean shopBean = ShopConfigs.getShop(workLoadBean.getOrder_channel_id(),
                String.valueOf(workLoadBean.getCart_id()));

        TmallUploadRunState tmallUploadRunState = new TmallUploadRunState(tcb);
        CmsModelPropBean cmsModelProp = workLoadBean.getCmsModelProp();

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

        String brandCode = brandMapDao.cmsBrandToPlatformBrand(workLoadBean.getOrder_channel_id(),
                workLoadBean.getCart_id(), Integer.parseInt(cmsModelProp.getProp(CmsFieldEnum.CmsModelEnum.brand)));

        if (brandCode == null || "".equals(brandCode))
        {
            logger.info("Job abort: can not find brand_code by brandId " +
                    cmsModelProp.getProp(CmsFieldEnum.CmsModelEnum.brand) + ", workload:" + workLoadBean);
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

            if (isDarwin == null && failCause.length() != 0) {
                if (failCause.indexOf("访问淘宝超时") != -1) {
                    throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(failCause.toString(), true));
                }
                else {
                    throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(failCause.toString()));
                }
            }
        } catch (ApiException e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.IMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }
        tmallUploadRunState.setIs_darwin(isDarwin);

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
        ShopBean shopBean = ShopConfigs.getShop(workLoadBean.getOrder_channel_id(), String.valueOf(workLoadBean.getCart_id()));
        String numId = workLoadBean.getNumId();
        String productId = workLoadBean.getProductId();
        Set<String> imageSet = new HashSet<>();

        List<PlatformPropBean> itemPlatformProps = platformPropDao.selectItemPropsByCid(workLoadBean.getCart_id(),
                String.valueOf(categoryCode));
        masterDataMappingService.constructPlatformProps(this, tcb, itemPlatformProps, imageSet);

        //如果imageSet不为空，就去上传图片
        if (imageSet.size() != 0)
        {
            //construct uploadImageParam
            UploadImageParam uploadImageParam = new UploadImageParam();
            uploadImageParam.setSrcUrlSet(imageSet);
            uploadImageParam.setShopBean(shopBean);

            //save UploadProductHandler status
            tmallWorkloadStatus.setValue(TmallWorkloadStatus.UPDATE_WAIT_ITEM_PIC);
            tmallUploadRunState.setPlatformProps(itemPlatformProps);

            //create UploadImageHandler Tcb
            UploadImageTcb uploadImageTcb = new UploadImageTcb(uploadImageParam);
            uploadImageTcb.setUploadProductTcb(tcb);
            tcb.setUploadImageTcb(uploadImageTcb);

            //suspend UploadProductHandler's current tcb and resume uploadImage
            throw new TaskSignal(TaskSignalType.MainTaskToUploadImage, new MainToUploadImageTaskSignalInfo(tcb, uploadImageTcb));
        }

        TmallUploadRunState.TmallContextBuildFields contextBeforeUploadImage = tmallUploadRunState.getContextBuildFields();

        List<Field> itemPlatformFields = (List)masterDataMappingService.resolvePlatformProps(this, tcb.getPlatformUploadRunState(), null);

        //在临上新的前一刻，再次做库存更新
        TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields = contextBeforeUploadImage.getContextBuildCustomFields();
        List<Field> customFields = contextBuildCustomFields.getCustomFields();
        AbstractSkuFieldBuilder skuFieldBuilder = contextBuildCustomFields.getSkuFieldBuilder();
        int totalInventory = skuFieldBuilder.updateInventoryField(workLoadBean.getOrder_channel_id(), contextBuildCustomFields, customFields);
        InputField quantityField = contextBuildCustomFields.getQuantityField();
        if (quantityField != null) {
            quantityField.setValue(String.valueOf(totalInventory));
            logger.info("商品数量设为：" + quantityField.getValue());
        }

        try {
            logger.debug("updateTmallItem: [productCode:" + productId + ", categoryCode:" + categoryCode + "]");
            numId = updateTmallItem(productId, numId, categoryCode, itemPlatformFields, shopBean);
            tcb.setNumId(numId);
        } catch (ApiException e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.IMS);
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

        ShopBean shopBean = ShopConfigs.getShop(workLoadBean.getOrder_channel_id(), String.valueOf(workLoadBean.getCart_id()));

        TmallUploadRunState.TmallContextBuildFields contextBeforeUploadImage = tmallUploadRunState.getContextBuildFields();

        Map<String, String> urlMap;
        if (uploadImageResult.isUploadSuccess()) {
            urlMap = uploadImageResult.getUrlMap();
        } else {
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(uploadImageResult.getFailCause(), uploadImageResult.isNextProcess()));
        }

        List<Field> itemPlatformFields = (List)masterDataMappingService.resolvePlatformProps(this, tcb.getPlatformUploadRunState(), urlMap);

        //在临上新的前一刻，再次做库存更新
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

        try {
            logger.debug("updateTmallItem: [productCode:" + productId + ", categoryCode:" + categoryCode + "]");
            numId = updateTmallItem(productId, numId, categoryCode, itemPlatformFields, shopBean);
            tcb.setNumId(numId);
        } catch (ApiException e) {
            issueLog.log(e, ErrorType.BatchJob, SubSystem.IMS);
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
            issueLog.log(e, ErrorType.BatchJob, SubSystem.IMS);
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(e.getMessage()));
        }
        StringBuffer failCause = new StringBuffer();
        logger.debug("tmall category code:" + categoryCode);
        logger.debug("xmlData:" + xmlData);
        TmallItemSchemaUpdateResponse updateItemResponse = tbProductService.updateItem(productId, numId, categoryCode, xmlData, shopBean);
        if (updateItemResponse == null) {
            failCause.append(String.format("Tmall return null response when update item"));
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

    @Override
    public List<Object> resolveCustomMappingProps(PlatformUploadRunState platformUploadRunState,
                                                  Map<String, String> urlMap) {
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) platformUploadRunState;
        TmallUploadRunState.TmallContextBuildFields tmallContextBuildFields = tmallUploadRunState.getContextBuildFields();
        TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields = tmallContextBuildFields.getContextBuildCustomFields();

        // sku中的属性图片
        AbstractSkuFieldBuilder skuFieldBuilder = contextBuildCustomFields.getSkuFieldBuilder();
        if (skuFieldBuilder != null)
        {
            skuFieldBuilder.updateSkuPropImage(urlMap, contextBuildCustomFields);

        }

        return (List)tmallContextBuildFields.getCustomFields();
    }

    /**
     * 价格的计算方法为：
     *  计算最高价格，库存为0的sku不参与计算
     *  如果所有sku库存都为0， 第一个的价格作为商品价格
     * @param cmsModelProp
     * @return
     */
    private double calcItemPrice(CmsModelPropBean cmsModelProp) {
        Double resultPrice = 0d, onePrice = 0d;
        List<Double> skuPriceList = new ArrayList<>();
        for (CmsCodePropBean cmsCodeProp : cmsModelProp.getCmsCodePropBeanList()) {
            for (CmsSkuPropBean cmsSkuProp : cmsCodeProp.getCmsSkuPropBeanList()) {
                int skuQuantity = 0;
                try {
                    skuQuantity = Integer.valueOf(cmsSkuProp.getProp(CmsFieldEnum.CmsSkuEnum.sku_quantity));
                } catch (Exception e) {
                    logger.warn("No quantity for sku " + cmsSkuProp.getProp(CmsFieldEnum.CmsSkuEnum.sku));
                }
                double skuPrice = 0;
                try {
                    skuPrice = Double.valueOf(cmsSkuProp.getProp(CmsFieldEnum.CmsSkuEnum.sku_price));
                } catch (Exception e) {
                    logger.warn("No price for sku " + cmsSkuProp.getProp(CmsFieldEnum.CmsSkuEnum.sku));
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

    @Override
    public List<Object> resolveMasterMappingProps(PlatformUploadRunState platformUploadRunState,
                                                  Map<String, String> urlMap) {

        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) platformUploadRunState;
        TmallUploadRunState.TmallContextBuildFields tmallContextBuildFields = tmallUploadRunState.getContextBuildFields();
        Map<PlatformPropBean, List<PropValueBean>> propValueMapping = tmallContextBuildFields.getPlatformPropBeanValueMap();

        //替换属性值中的旧的url地址为上传后的url地址
        if (urlMap != null) {
            for (Map.Entry<PlatformPropBean, List<PropValueBean>> entry : propValueMapping.entrySet()) {
                for (PropValueBean propValueBean : entry.getValue()) {
                    for (Map.Entry<String, String> urlEntry : urlMap.entrySet()) {
                        if (propValueBean.getProp_value()!= null && propValueBean.getProp_value().indexOf(urlEntry.getKey()) != -1) {
                            propValueBean.setProp_value(propValueBean.getProp_value().replace(urlEntry.getKey(), urlEntry.getValue()));
                        }
                    }
                }
            }
        }

        Map<PropValueBean, PropValueTreeNode> valueTreeMap = masterDataMappingService.buildPropValueTree(propValueMapping);
        List<PropValueTreeNode> leafPropValueTreeNodes = masterDataMappingService.getLeafPropValueTreeNode((new ArrayList<>(valueTreeMap.values())));

        List<Object> platformFields = new ArrayList<>();

        List<PropValueTreeNode> redundantMultiCheckTreeNodes = new ArrayList<>();
        List<PropValueTreeNode> multicheckParentTreeNodes  = new ArrayList<>();
        for (PropValueTreeNode propValueTreeNode : leafPropValueTreeNodes)
        {
            switch (propValueTreeNode.getPlatformProp().getPlatformPropType())
            {
                case ImsConstants.PlatformPropType.C_INPUT:
                {
                    InputField inputField = (InputField) FieldTypeEnum.createField(FieldTypeEnum.INPUT);
                    inputField.setId(propValueTreeNode.getPlatformProp().getPlatformPropId());
                    inputField.setValue(propValueTreeNode.getPropValue().getProp_value());
                    propValueTreeNode.setPlatformField(inputField);
                    break;
                }
                case ImsConstants.PlatformPropType.C_SINGLE_CHECK:
                {
                    SingleCheckField singleCheckField = (SingleCheckField) FieldTypeEnum.createField(FieldTypeEnum.SINGLECHECK);
                    singleCheckField.setId(propValueTreeNode.getPlatformProp().getPlatformPropId());
                    singleCheckField.setValue(propValueTreeNode.getPropValue().getProp_value());
                    propValueTreeNode.setPlatformField(singleCheckField);
                    break;
                }
                case ImsConstants.PlatformPropType.C_MULTI_CHECK:
                {
                    if (!redundantMultiCheckTreeNodes.contains(propValueTreeNode)) {
                        MultiCheckField multiCheckField = (MultiCheckField) FieldTypeEnum.createField(FieldTypeEnum.MULTICHECK);
                        multiCheckField.setId(propValueTreeNode.getPlatformProp().getPlatformPropId());

                        if (propValueTreeNode.getParentNode() == null) {
                            logger.error("MultiCheck format is wrong, It must have a parent");
                        }

                        propValueTreeNode.getParentNode().setPlatformField(multiCheckField);
                        for (PropValueTreeNode childPropValueTreeNode : propValueTreeNode.getParentNode().getChildNodes()) {
                            multiCheckField.addValue(childPropValueTreeNode.getPropValue().getProp_value());
                            redundantMultiCheckTreeNodes.add(childPropValueTreeNode);
                        }
                        multicheckParentTreeNodes.add(propValueTreeNode.getParentNode());
                        break;
                    }
                }
            }
        }
        leafPropValueTreeNodes.removeAll(redundantMultiCheckTreeNodes);
        leafPropValueTreeNodes.addAll(multicheckParentTreeNodes);

        List<PropValueTreeNode> processedPropValueTreeNodes = new ArrayList<>();
        for (PropValueTreeNode propValueTreeNode : leafPropValueTreeNodes)
        {
            if (propValueTreeNode.getParentNode() == null)
            {
                processedPropValueTreeNodes.add(propValueTreeNode);
                platformFields.add(propValueTreeNode.getPlatformField());
            }
        }
        leafPropValueTreeNodes.removeAll(processedPropValueTreeNodes);

        while (!leafPropValueTreeNodes.isEmpty()) {
            PropValueTreeNode propValueTreeNode = leafPropValueTreeNodes.get(0);
            PropValueTreeNode parentPropValueTreeNode = propValueTreeNode.getParentNode();

            boolean buildResult = true;
            processedPropValueTreeNodes.clear();
            if (parentPropValueTreeNode.getPlatformProp().getPlatformPropType() == ImsConstants.PlatformPropType.C_COMPLEX) {
                ComplexField complexField = new ComplexField();
                complexField.setId(parentPropValueTreeNode.getPlatformProp().getPlatformPropId());
                parentPropValueTreeNode.setPlatformField(complexField);

                ComplexValue complexValue = new ComplexValue();
                complexField.setComplexValue(complexValue);
                for (PropValueTreeNode childPropValueTreeNode : parentPropValueTreeNode.getChildNodes()) {
                    PlatformPropBean childPlatformPropBean = childPropValueTreeNode.getPlatformProp();
                    switch (childPlatformPropBean.getPlatformPropType()) {
                        case ImsConstants.PlatformPropType.C_INPUT: {
                            InputField inputField = (InputField) childPropValueTreeNode.getPlatformField();
                            complexValue.setInputFieldValue(inputField.getId(), inputField.getValue());
                            break;
                        }
                        case ImsConstants.PlatformPropType.C_SINGLE_CHECK: {
                            SingleCheckField singleCheckField = (SingleCheckField) childPropValueTreeNode.getPlatformField();
                            complexValue.setSingleCheckFieldValue(singleCheckField.getId(), singleCheckField.getValue());
                            break;
                        }
                        case ImsConstants.PlatformPropType.C_MULTI_CHECK: {
                            MultiCheckField multiCheckField = (MultiCheckField) childPropValueTreeNode.getPlatformField();
                            complexValue.setMultiCheckFieldValues(multiCheckField.getId(), multiCheckField.getValues());
                            break;
                        }
                        case ImsConstants.PlatformPropType.C_COMPLEX: {
                            ComplexField innerComplexField = (ComplexField) childPropValueTreeNode.getPlatformField();
                            if (innerComplexField == null) {
                                buildResult = false;
                                break;
                            }
                            complexValue.setComplexFieldValue(innerComplexField.getId(), innerComplexField.getComplexValue());
                            break;
                        }
                        case ImsConstants.PlatformPropType.C_MULTI_COMPLEX: {
                            MultiComplexField multiComplexField = (MultiComplexField) childPropValueTreeNode.getPlatformField();
                            if (multiComplexField == null) {
                                buildResult = false;
                                break;
                            }
                            complexValue.setMultiComplexFieldValues(multiComplexField.getId(), multiComplexField.getComplexValues());
                            break;
                        }
                    }
                    //如果有一个发现build失败，那么，整个parentField的构造就失败，这说明有些子是复杂类型，而它们没有先被处理掉
                    if (!buildResult)
                        break;
                    processedPropValueTreeNodes.add(childPropValueTreeNode);
                }

                if (buildResult) {
                    if (parentPropValueTreeNode.getParentNode() == null) {
                        platformFields.add(complexField);
                    } else {
                        leafPropValueTreeNodes.add(parentPropValueTreeNode);
                    }
                }
                else {
                    //将该结点放在链表最后
                    leafPropValueTreeNodes.remove(propValueTreeNode);
                    leafPropValueTreeNodes.add(propValueTreeNode);
                }
            } else if (parentPropValueTreeNode.getPlatformProp().getPlatformPropType() == ImsConstants.PlatformPropType.C_MULTI_COMPLEX) {
                PropValueTreeNode ancestorPropValueTreeNode = parentPropValueTreeNode.getParentNode();
                if (ancestorPropValueTreeNode == null) {
                    logger.error("MultiComplex Prop's format is wrong");
                }

                MultiComplexField multiComplexField = new MultiComplexField();
                assert ancestorPropValueTreeNode != null;
                multiComplexField.setId(ancestorPropValueTreeNode.getPlatformProp().getPlatformPropId());

                for (PropValueTreeNode parentLevelPropValueTreeNode : ancestorPropValueTreeNode.getChildNodes()) {
                    ComplexValue complexValue = new ComplexValue();
                    for (PropValueTreeNode childPropValueTreeNode : parentLevelPropValueTreeNode.getChildNodes()) {
                        PlatformPropBean childPlatformPropBean = childPropValueTreeNode.getPlatformProp();
                        switch (childPlatformPropBean.getPlatformPropType()) {
                            case ImsConstants.PlatformPropType.C_INPUT: {
                                InputField inputField = (InputField) childPropValueTreeNode.getPlatformField();
                                complexValue.setInputFieldValue(inputField.getId(), inputField.getValue());
                                break;
                            }
                            case ImsConstants.PlatformPropType.C_SINGLE_CHECK: {
                                SingleCheckField singleCheckField = (SingleCheckField) childPropValueTreeNode.getPlatformField();
                                complexValue.setSingleCheckFieldValue(singleCheckField.getId(), singleCheckField.getValue());
                                break;
                            }
                            case ImsConstants.PlatformPropType.C_MULTI_CHECK: {
                                MultiCheckField multiCheckField = (MultiCheckField) childPropValueTreeNode.getPlatformField();
                                complexValue.setMultiCheckFieldValues(multiCheckField.getId(), multiCheckField.getValues());
                                break;
                            }
                            case ImsConstants.PlatformPropType.C_COMPLEX: {
                                ComplexField innerComplexField = (ComplexField) childPropValueTreeNode.getPlatformField();
                                if (innerComplexField == null) {
                                    buildResult = false;
                                    break;
                                }
                                complexValue.setComplexFieldValue(innerComplexField.getId(), innerComplexField.getComplexValue());
                                break;
                            }
                            case ImsConstants.PlatformPropType.C_MULTI_COMPLEX: {
                                MultiComplexField innerMultiComplexField = (MultiComplexField) childPropValueTreeNode.getPlatformField();
                                if (innerMultiComplexField == null) {
                                    buildResult = false;
                                    break;
                                }
                                complexValue.setMultiComplexFieldValues(innerMultiComplexField.getId(), innerMultiComplexField.getComplexValues());
                                break;
                            }
                        }
                        if (!buildResult)
                            break;
                        processedPropValueTreeNodes.add(childPropValueTreeNode);
                    }
                    if (!buildResult)
                        break;
                    multiComplexField.addComplexValue(complexValue);
                }

                if (buildResult) {
                    if (ancestorPropValueTreeNode.getParentNode() == null) {
                        platformFields.add(multiComplexField);
                    } else {
                        ancestorPropValueTreeNode.setPlatformField(multiComplexField);
                        leafPropValueTreeNodes.add(ancestorPropValueTreeNode);
                    }
                } else
                {
                    //将该结点放在链表最后
                    leafPropValueTreeNodes.remove(propValueTreeNode);
                    leafPropValueTreeNodes.add(propValueTreeNode);
                }

            }
            leafPropValueTreeNodes.removeAll(processedPropValueTreeNodes);
        }
        return platformFields;
    }

    @Override
    public void constructCustomPlatformPropsBeforeUploadImage(UploadProductTcb tcb, Map<CustomMappingType,
            List<PlatformPropBean>> mappingTypePropsMap, ExpressionParser expressionParser, Set<String> imageSet, List<PlatformPropBean> platformPropsWillBeFilted) throws TaskSignal {
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        TmallUploadRunState.TmallContextBuildFields contextBeforeUploadImage =
                tmallUploadRunState.getContextBuildFields();
        TmallUploadRunState.TmallContextBuildCustomFields contextBuildCustomFields = contextBeforeUploadImage.getContextBuildCustomFields();
        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();

        //品牌
        for (Map.Entry<CustomMappingType, List<PlatformPropBean>> entry : mappingTypePropsMap.entrySet()) {
            CustomMappingType customMappingType = entry.getKey();
            List<PlatformPropBean> platformProps = entry.getValue();
            switch (customMappingType) {
                case BRAND_INFO: {
                    String brandCode = tmallUploadRunState.getBrand_code();
                    PlatformPropBean platformProp = platformProps.get(0);

                    if (platformProp.getPlatformPropType() != ImsConstants.PlatformPropType.C_SINGLE_CHECK) {
                        logger.error("tmall's brand field(" + platformProp.getPlatformCartId() + ") must be singleCheck");
                    } else {
                        SingleCheckField singleCheckField = (SingleCheckField) FieldTypeEnum.createField(FieldTypeEnum.SINGLECHECK);
                        singleCheckField.setId(platformProp.getPlatformPropId());
                        singleCheckField.setValue(brandCode);
                        contextBeforeUploadImage.addCustomField(singleCheckField);
                    }
                    break;
                }
                case SKU_INFO:
                {
                    int cartId = workLoadBean.getCart_id();
                    String categoryCode = String.valueOf(tmallUploadRunState.getCategory_code());

                    workLoadBean.setHasSku(true);

                    //TODO 将要从tmall平台获取已被占用的color, 暂时赋值为null
                    List<String> excludeColors = null;
                    AbstractSkuFieldBuilder skuFieldBuilder = skuFieldBuilderFactory.getSkuFieldBuilder(cartId, platformProps);

                    if (skuFieldBuilder == null)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("No sku builder find"));
                    }
                    //
                    DictWordBean dictWordBean = dictWordDao.selectDictWordByName(workLoadBean.getOrder_channel_id(), "属性图片模板");
                    RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
                    DictWord dictWord = (DictWord) ruleJsonMapper.deserializeRuleWord(dictWordBean.getValue());
                    String codePropImageTemplate = expressionParser.parse(dictWord.getExpression(), null);
                    skuFieldBuilder.setCodeImageTemplete(codePropImageTemplate);

                    List<Field> skuInfoFields = skuFieldBuilder.buildSkuInfoField(cartId, categoryCode, platformProps,
                            workLoadBean.getCmsModelProp(), contextBuildCustomFields, imageSet);

                    if (skuInfoFields == null)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("Can't build SkuInfoField"));
                    }
                    contextBeforeUploadImage.getCustomFields().addAll(skuInfoFields);
                    contextBuildCustomFields.setSkuFieldBuilder(skuFieldBuilder);
                    break;
                }
                case PRICE_SECTION:
                {
                    if (platformProps == null || platformProps.size() != 1)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("price_section's platformProps must have only one prop!"));
                    }
                    CmsCodePropBean mainProductProp = workLoadBean.getMainProductProp();
                    PlatformPropBean platformProp = platformProps.get(0);
                    Field field = priceSectionBuilder.buildPriceSectionField(platformProp, mainProductProp);
                    contextBeforeUploadImage.addCustomField(field);
                    break;
                }
                case TMALL_SERVICE_VERSION:
                {
                    if (platformProps == null || platformProps.size() != 1)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("tmall service version's platformProps must have only one prop!"));
                    }
                    PlatformPropBean platformProp = platformProps.get(0);
                    InputField  field = (InputField) FieldTypeEnum.createField(FieldTypeEnum.INPUT);
                    field.setId(platformProp.getPlatformPropId());
                    field.setValue("11100");
                    contextBeforeUploadImage.addCustomField(field);
                    break;
                }
                case TMALL_STYLE_CODE:
                {
                    if (platformProps == null || platformProps.size() != 1)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("tmall style code's platformProps must have only one prop!"));
                    }
                    PlatformPropBean platformProp = platformProps.get(0);
                    InputField  field = (InputField) FieldTypeEnum.createField(FieldTypeEnum.INPUT);
                    field.setId(platformProp.getPlatformPropId());

                    String styleCode = tmallUploadRunState.getStyle_code();
                    if (styleCode == null || "".equals(styleCode)) {
                        styleCode = generateStyleCode(tcb);
                        tmallUploadRunState.setStyle_code(styleCode);
                    }
                    field.setValue(styleCode);
                    logger.debug("tmall style code[" + field.getId() + "]: " + field.getValue());
                    contextBeforeUploadImage.addCustomField(field);
                    break;
                }
                case TMALL_ITEM_QUANTITY:
                {
                    if (platformProps == null || platformProps.size() != 1)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("tmall item quantity's platformProps must have only one prop!"));
                    }

                    PlatformPropBean platformProp = platformProps.get(0);
                    InputField  field = (InputField) FieldTypeEnum.createField(FieldTypeEnum.INPUT);
                    field.setId(platformProp.getPlatformPropId());
                    //初始值先设为0，等到库存更新之后，重新更新他的值
                    field.setValue("0");
                    contextBuildCustomFields.setQuantityField(field);
                    contextBeforeUploadImage.addCustomField(field);
                    break;
                }
                case TMALL_ITEM_PRICE:
                {
                    if (platformProps == null || platformProps.size() != 1)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("tmall item price's platformProps must have only one prop!"));
                    }

                    PlatformPropBean platformProp = platformProps.get(0);
                    InputField  field = (InputField) FieldTypeEnum.createField(FieldTypeEnum.INPUT);
                    field.setId(platformProp.getPlatformPropId());

                    double itemPrice = calcItemPrice(workLoadBean.getCmsModelProp());
                    field.setValue(String.valueOf(itemPrice));

                    contextBuildCustomFields.setPriceField(field);
                    contextBeforeUploadImage.addCustomField(field);
                    break;
                }
                case TMALL_XINGHAO:
                {
                    if (platformProps == null || platformProps.size() != 2)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("tmall item xinghao's platformProps must have two props!"));
                    }

                    for (PlatformPropBean platformProp : platformProps) {
                        if (platformProp.getPlatformPropType() == ImsConstants.PlatformPropType.C_SINGLE_CHECK) {
                            SingleCheckField field = (SingleCheckField) FieldTypeEnum.createField(FieldTypeEnum.SINGLECHECK);
                            field.setId(platformProp.getPlatformPropId());
                            //prop_1626510（型号）值设为-1(表示其他）
                            field.setValue("-1");
                            contextBeforeUploadImage.addCustomField(field);
                        }
                        else {
                            //其他的型号值填货号
                            InputField field = (InputField) FieldTypeEnum.createField(FieldTypeEnum.INPUT);
                            field.setId(platformProp.getPlatformPropId());

                            String styleCode = tmallUploadRunState.getStyle_code();
                            if (styleCode == null || "".equals(styleCode)) {
                                styleCode = generateStyleCode(tcb);
                                tmallUploadRunState.setStyle_code(styleCode);
                            }

                            field.setValue(styleCode);
                            contextBeforeUploadImage.addCustomField(field);
                        }
                    }
                    break;
                }
                case TMALL_OUT_ID: {
                    if (platformProps == null || platformProps.size() != 1)
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
                    if (hasSku)
                    {
                        logger.info("已经有sku属性，忽略商品外部编码");
                        continue;
                    }
                    PlatformPropBean platformProp = platformProps.get(0);
                    InputField  field = (InputField) FieldTypeEnum.createField(FieldTypeEnum.INPUT);
                    field.setId(platformProp.getPlatformPropId());
                    List<CmsCodePropBean> cmsCodePropBeans = workLoadBean.getCmsModelProp().getCmsCodePropBeanList();
                    if (cmsCodePropBeans.size() != 1) {
                        String errorCause = "包含商品外部编码的类目必须只有一个code";
                        logger.error(errorCause);
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(errorCause));
                    }
                    CmsCodePropBean cmsCodeProp = cmsCodePropBeans.get(0);
                    List<CmsSkuPropBean> cmsSkuProps = cmsCodeProp.getCmsSkuPropBeanList();
                    if (cmsSkuProps.size() != 1) {
                        String errorCause = "包含商品外部编码的类目必须只有一个sku";
                        logger.error(errorCause);
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(errorCause));
                    }
                    field.setValue(cmsSkuProps.get(0).getProp(CmsFieldEnum.CmsSkuEnum.sku));
                    contextBeforeUploadImage.addCustomField(field);
                    break;
                }
                case TMALL_SHOP_CATEGORY:
                {
                    if (platformProps == null || platformProps.size() != 1)
                    {
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("tmall item shop_category's platformProps must have one prop!"));
                    }

                    if ("010".equals(workLoadBean.getOrder_channel_id())) {
                        PlatformPropBean platformProp = platformProps.get(0);
                        MultiCheckField field = (MultiCheckField) FieldTypeEnum.createField(FieldTypeEnum.MULTICHECK);
                        String platformPropId = platformProp.getPlatformPropId();
                        List<ConditionPropValue> conditionPropValues = conditionPropValueRepo.get(workLoadBean.getOrder_channel_id(), platformPropId);
                        field.setId(platformPropId);
                        if (conditionPropValues != null && !conditionPropValues.isEmpty()) {
                            RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
                            for (ConditionPropValue conditionPropValue : conditionPropValues) {
                                String conditionExpressionStr = conditionPropValue.getCondition_expression();
                                RuleExpression conditionExpression= ruleJsonMapper.deserializeRuleExpression(conditionExpressionStr);
                                String propValue = expressionParser.parse(conditionExpression, null);
                                if (propValue != null) {
                                    field.addValue(propValue);
                                }
                            }
                            contextBeforeUploadImage.addCustomField(field);
                        }
                    } else if ("012".equals(workLoadBean.getOrder_channel_id())) {
                        /*
                        for (Iterator<PlatformPropBean> iter = platformPropsWillBeFilted.iterator(); iter.hasNext(); ) {
                            PlatformPropBean platformPropBean = iter.next();
                            if ("seller_cids".equals(platformPropBean.getPlatformPropId())) {
                                iter.remove();
                            }
                        }
                        */
                        final String sellerCategoryPropId = "seller_cids";
                        if (workLoadBean.getUpJobParam().getMethod() == UpJobParamBean.METHOD_UPDATE) {
                            String numId = workLoadBean.getNumId();
                            ShopBean shopBean = ShopConfigs.getShop(workLoadBean.getOrder_channel_id(), String.valueOf(workLoadBean.getCart_id()));
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
                                    contextBeforeUploadImage.addCustomField(field);
                                }
                            } catch (TopSchemaException e) {
                                e.printStackTrace();
                            } catch (ApiException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void constructDarwinPlatformProps(UploadProductTcb tcb, PlatformPropBean platformProp, String platformPropValue) throws TaskSignal {
        int platformPropType = platformProp.getPlatformPropType();
        if (platformPropType != ImsConstants.PlatformPropType.C_INPUT
                && platformPropType != ImsConstants.PlatformPropType.C_SINGLE_CHECK) {
            throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo("达尔文字段目前只支持Input和SingleCheck, 不支持type=" + platformPropType));
        }
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        List<Field> darwinFields = tmallUploadRunState.getContextBuildFields().getDarwinFields();
        switch (platformPropType) {
            case ImsConstants.PlatformPropType.C_INPUT:
                InputField inputField = (InputField) FieldTypeEnum.createField(FieldTypeEnum.INPUT);
                inputField.setId(platformProp.getPlatformPropId());
                inputField.setValue(platformPropValue);
                darwinFields.add(inputField);
                logger.debug("add darwin field, type:input, propId:" + platformProp.getPlatformPropId() + ", value:" + platformPropValue);
                break;
            case ImsConstants.PlatformPropType.C_SINGLE_CHECK:
                SingleCheckField singleCheckField = (SingleCheckField) FieldTypeEnum.createField(FieldTypeEnum.SINGLECHECK);
                singleCheckField.setId(platformProp.getPlatformPropId());
                singleCheckField.setValue(platformPropValue);
                logger.debug("add darwin field, type:singleCheck, propId:" + platformProp.getPlatformPropId() + ", value:" + platformPropValue);
                break;
            default:
                logger.error("Unexpect field darwin type!");
        }
    }

    @Override
    public void constructFieldMappingPlatformPropsBeforeUploadImage(UploadProductTcb tcb, PlatformPropBean platformProp,
                                                                    List<PlatformPropBean> platformProps,
                                                                    PropMappingBean masterPropMapping,
                                                                    ExpressionParser expressionParser,
                                                                    Set<String> imageSet) throws TaskSignal {
        TmallUploadRunState tmallUploadRunState = (TmallUploadRunState) tcb.getPlatformUploadRunState();
        TmallUploadRunState.TmallContextBuildFields contextBeforeUploadImage = tmallUploadRunState.getContextBuildFields();

        WorkLoadBean workLoadBean = tcb.getWorkLoadBean();
        RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();

        Map<PlatformPropBean, List<PropValueBean>> platformPropBeanValueMap = contextBeforeUploadImage.getPlatformPropBeanValueMap();

        String plainPropValue;
        List<PropValueBean> propValueBeans = propValueDao.selectPropValue(workLoadBean.getOrder_channel_id(),
                workLoadBean.getLevel(),
                workLoadBean.getLevelValue(),
                String.valueOf(masterPropMapping.getPropId()));
        PropBean propBean = propDao.selectPropByPropId(masterPropMapping.getPropId());

        //将主数据的属性值进行解析后重新赋值到主数据属性值中, 并设置平台属性与主属性值之间的map关系
        if (propValueBeans != null) {
            for (Iterator<PropValueBean> propValueBeanIterator = propValueBeans.iterator(); propValueBeanIterator.hasNext();) {
                PropValueBean propValueBean = propValueBeanIterator.next();
                String propValue = propValueBean.getProp_value();
                //如果有值，那么直接使用该表中的表达式值
                if (propValue != null && !"".equals(propValue)) {
                    RuleExpression ruleExpression = ruleJsonMapper.deserializeRuleExpression(propValue);
                    if (ruleExpression == null)
                    {
                        String error = "master prop[id=" + masterPropMapping.getPropId() + "]'s value is illegal! value=" + propValue;
                        throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(error));
                    }
                    plainPropValue = expressionParser.parse(ruleExpression, imageSet);
                    if (plainPropValue == null) {
                        logger.error("fail to parse expression, " + propValue );
                        plainPropValue = expressionParser.parse(ruleExpression, imageSet);
                    }
                }
                else
                    continue;//如果没有值，那么要看主数据属性表中是否有默认值,如果有,则使用默认值
                //暂时认为默认值由保存数据时考虑
                /*
                else {
                    if (propBean.getPropValueDefault() != null && !"".equals(propBean.getPropValueDefault())) {
                        //propValue = propBean.getPropValueDefault();
                        //暂时，默认值认为不能是表达式
                        // RuleExpression ruleExpression = ruleJsonMapper.deserializeRuleExpression(propValue);
                        //plainPropValue = expressionParser.parse(ruleExpression, imageSet);
                        plainPropValue = propBean.getPropValueDefault();
                    }
                }
                */

                //如果经过解析，发现字段的值表达式不存在值，那么，应该将这个值移除，也就是说，这个字段将不应该出现在上传到平台的
                //xml中
                if (plainPropValue == null) {
                    logger.info("Can't find mapping option value, propId:" + propBean.getPropId());
                    //propValueBeans.remove(propValueBean);
                    propValueBeanIterator.remove();
                    continue;
                } else {
                    //如果是单选或多选，那么要从ims_mt_prop_option_mapping中解决option映射的问题
                    if (!"seller_cids".equals(platformProp.getPlatformPropId())) {
                        if (propBean.getPropType() == ImsConstants.PlatformPropType.C_SINGLE_CHECK || propBean.getPropType() == ImsConstants.PlatformPropType.C_MULTI_CHECK) {
                            plainPropValue = masterDataMappingService.getPlatformOptionValueByMasterOptionValue(propBean.getPropId(), plainPropValue);
                        } else {
                        }
                    }
                }

                propValueBean.setProp_value(plainPropValue);
            }
            platformPropBeanValueMap.put(platformProp, propValueBeans);
        }

        //保存通过主数据字段匹配的属性处理结果
        contextBeforeUploadImage.setPlatformPropBeanValueMap(platformPropBeanValueMap);
        contextBeforeUploadImage.setPlatformProps(platformProps);
    }

    @Override
    public void constructMappingByTypePlatformPropsBeforeUploadImage(UploadProductTcb tcb,
                                                                     PlatformPropBean platformProp) {

    }
}
