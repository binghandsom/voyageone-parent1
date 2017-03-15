package com.voyageone.task2.vms.service;

import com.csvreader.CsvReader;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.*;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.VmsChannelConfigBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.MD5;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.com.MessageBean;
import com.voyageone.service.dao.vms.VmsBtFeedInfoTempDao;
import com.voyageone.service.daoext.vms.VmsBtFeedFileDaoExt;
import com.voyageone.service.daoext.vms.VmsBtFeedInfoTempDaoExt;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.feed.FeedToCmsService;
import com.voyageone.service.impl.wms.ClientInventoryService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.service.model.vms.VmsBtFeedFileModel;
import com.voyageone.service.model.vms.VmsBtFeedInfoTempModel;
import com.voyageone.task2.base.BaseMQCmsService;
import com.voyageone.task2.vms.VmsConstants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 将Feed信息导入FeedInfo表
 * Created on 16/06/29.
 *
 * @author jeff.duan
 * @version 1.0
 */
@Service
@RabbitListener(queues = "voyageone_mq_vms_feed_file_import")
public class VmsFeedFileImportService extends BaseMQCmsService {

    public static final int SKU_INDEX = 0;
    public static final int PARENT_ID = 1;
    public static final int RELATIONSHIP_TYPE = 2;
    public static final int VARIATION_THEME = 3;
    public static final int TITLE = 4;
    public static final int PRODUCT_ID = 5;
    public static final int PRICE = 6;
    public static final int MSRP = 7;
    public static final int QUANTITY = 8;
    public static final int WEIGHT = 9;
    public static final int IMAGES = 10;
    public static final int DESCRIPTION = 11;
    public static final int SHORT_DESCRIPTION = 12;
    public static final int PRODUCT_ORIGIN = 13;
    public static final int CATEGORY = 14;
    public static final int BRAND = 15;
    public static final int MATERIALS = 16;
    public static final int VENDOR_PRODUCT_URL = 17;

    public static final int LIMIT_COUNT = 50000;
    public static final String BLANK_PARENT_ID = "999999";


    public final static Map<Object, String> columnMap = new HashMap() {{
        put(SKU_INDEX, "sku");
        put(PARENT_ID, "parent-id");
        put(RELATIONSHIP_TYPE, "relationship-type");
        put(VARIATION_THEME, "variation-theme");
        put(TITLE, "title");
        put(PRODUCT_ID, "product-id");
        put(PRICE, "price");
        put(MSRP, "msrp");
        put(QUANTITY, "quantity");
        put(WEIGHT, "weight");
        put(IMAGES, "images");
        put(DESCRIPTION, "description");
        put(SHORT_DESCRIPTION, "short-description");
        put(PRODUCT_ORIGIN, "product-origin");
        put(CATEGORY, "category");
        put(BRAND, "brand");
        put(MATERIALS, "materials");
        put(VENDOR_PRODUCT_URL, "vendor-product-url");
    }};

    public final static Map<Object, String> productTypeByCategoryMap = new HashMap() {{
        put("Clothing", "Apparel and Accessory");
        put("Sports & Outdoors", "Sports and Outdoors");
        put("Baby Products", "Baby");
        put("Beauty", "Beauty");
        put("Electronics", "Electronics");
        put("Health & Personal Care", "Health and Personal Care");
        put("Home, Garden & Pets", "Home and Garden");
        put("Tools & Home Improvement", "Home Improvement");
        put("Jewelry", "Jewelry");
        put("Office Products", "Office Products");
        put("Shoes", "Shoes");
        put("Sports & Outdoors", "Sports and Outdoors");
        put("Toys & Games", "product-origin");
        put("Toys and Games", "Toys and Games");
        put("Watches", "Watches");
    }};

    public final static List<String> weightUnits = new ArrayList<String>() {
        {
            add("oz");
            add("lbs");
            add("kg");
            add("g");
        }
    };

    @Autowired
    private VmsBtFeedFileDaoExt vmsBtFeedFileDaoExt;

    @Autowired
    private MessageService messageService;

    @Autowired
    private VmsBtFeedInfoTempDao vmsBtFeedInfoTempDao;

    @Autowired
    private VmsBtFeedInfoTempDaoExt vmsBtFeedInfoTempDaoExt;

    @Autowired
    private FeedToCmsService feedToCmsService;

    @Autowired
    private FeedInfoService feedInfoService;

    @Autowired
    private ClientInventoryService clientInventoryService;

    @Autowired
    protected TransactionRunner transactionRunner;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.VMS;
    }

    @Override
    public String getTaskName() {
        return "VmsFeedFileImportService";
    }

    /**
     * 将Feed信息导入FeedInfo表
     *
     * @param messageMap Message内容
     * @throws Exception
     */
    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        // 渠道
        String channelId = (String) messageMap.get("channelId");
        // 处理的csv文件名
        String fileName = (String) messageMap.get("fileName");
        // 文件上传类型
        String uploadType = (String) messageMap.get("uploadType");
        if (!StringUtils.isEmpty(channelId) && fileName != null) {
            new ImportFeedFile(channelId, fileName, uploadType).doRun();
        } else {
            $error("参数channelId或者fileName为空");
        }
    }

    /**
     * 按渠道进行导入
     */
    public class ImportFeedFile {
        private OrderChannelBean channel;
        private String fileName;
        private String uploadType;
        private String errorFileName = "";
        private String priceMsrpFormula = "";
        private String priceRetailFormula = "";

        public ImportFeedFile(String orderChannelId, String fileName, String uploadType) {
            this.channel = Channels.getChannel(orderChannelId);
            this.fileName = fileName;
            this.uploadType = uploadType;
        }

        public void doRun() {

            try {
                $info(channel.getFull_name() + "产品 Feed文件导入开始");

                // 取得Feed计算公式
                priceMsrpFormula = Feeds.getVal1(channel.getOrder_channel_id(), FeedEnums.Name.price_msrp);
                priceRetailFormula = Feeds.getVal1(channel.getOrder_channel_id(), FeedEnums.Name.price_current);

                // 取得Feed文件上传路径
                String feedFilePath = "";
                // online上传的场合
                if (VmsConstants.FeedFileUploadType.ONLINE.equals(uploadType)) {
                    feedFilePath = Codes.getCodeName(VmsConstants.VMS_PROPERTY, "vms.feed.online.upload");
                    feedFilePath += "/" + channel.getOrder_channel_id() + "/";
                } else {
                    // ftp上传的场合
                    feedFilePath = Codes.getCodeName(VmsConstants.VMS_PROPERTY, "vms.feed.ftp.upload");
                    feedFilePath += "/" + channel.getOrder_channel_id() + "/feed/";
                }

                // 存在需要导入的Feed文件
                File feedFile = new File(feedFilePath + fileName);
                // 文件存在的话那么处理
                if (feedFile.exists()) {
                    $info("Feed文件处理开始 文件路径：" + feedFilePath + fileName + ",channel：" + channel.getFull_name());
                    // 把Feed数据插入vms_bt_feed_info_temp表
                    boolean result = readCsvToDB(feedFile);
                    if (!result) {
                        // check数据并且插入MongoDb表
                        doHandle(feedFile.getName());
                    }
                    $info("Feed文件处理结束,channel：" + channel.getFull_name());
                } else {
                    // 一般情况下不可能发生，除非手动删除文件
                    $error("Feed文件不存在 文件路径：" + feedFilePath + fileName + ",channel：" + channel.getFull_name());
                    throw new BusinessException("File Not Exist,File Path:" + feedFilePath + fileName);
                }
                $info(channel.getFull_name() + "产品 Feed文件导入结束");
            } catch (Exception ex) {
                // 把文件管理的状态变为5：系统异常
                VmsBtFeedFileModel feedFileModel = new VmsBtFeedFileModel();
                // 更新条件
                feedFileModel.setChannelId(channel.getOrder_channel_id());
                feedFileModel.setFileName(fileName);
                // 更新内容
                String errorMsg = ex.getMessage();
                if (ex.getMessage() != null && ex.getMessage().length() > 2000) {
                    errorMsg = ex.getMessage().substring(0, 2000);
                }
                feedFileModel.setErrorMsg(errorMsg);
                feedFileModel.setStatus(VmsConstants.FeedFileStatus.IMPORT_SYSTEM_ERROR);
                feedFileModel.setModifier(getTaskName());
                vmsBtFeedFileDaoExt.updateErrorInfo(feedFileModel);
            }
        }

        /**
         * check数据并且插入MongoDb表
         *
         * @param feedFileName FeedFile文件名
         */
        private void doHandle(String feedFileName) throws IOException {

            try {
                // Error错误
                List<Map<String, Object>> errorList = new ArrayList<>();
                // checkSku重复
                List<Map<String, Object>> skuList = vmsBtFeedInfoTempDaoExt.selectSameSku(channel.getOrder_channel_id());
                for (Map<String, Object> skuMap : skuList) {
                    // sku(%s) is duplicated.
                    addErrorMessage(errorList, "8000003", new Object[]{(String) skuMap.get("sku")}, (String) skuMap.get("sku"), columnMap.get(SKU_INDEX));
                }
                // checkProductId重复
                List<Map<String, Object>> productIdList = vmsBtFeedInfoTempDaoExt.selectSameProductId(channel.getOrder_channel_id());
                for (Map<String, Object> productIdMap : productIdList) {
                    if (!StringUtils.isEmpty((String) productIdMap.get("product_id"))) {
                        Map<String, Object> paramProductId = new HashMap<>();
                        paramProductId.put("channelId", channel.getOrder_channel_id());
                        paramProductId.put("productId", (String) productIdMap.get("product_id"));
                        List<VmsBtFeedInfoTempModel> productIdModels = vmsBtFeedInfoTempDao.selectList(paramProductId);
                        for (VmsBtFeedInfoTempModel productIdModel : productIdModels) {
                            // product-id(%s) is duplicated.
                            addErrorMessage(errorList, "8000012", new Object[]{productIdModel.getProductId()}, productIdModel.getSku(), columnMap.get(PRODUCT_ID));
                        }
                    }
                }
                // 如果有Sku或者ProductId重复错误，那么直接返回
                // 如果有错误的情况下(因为error达到固定件数就会先出到文件，所以ERROR的条件 除了 errorList>0以外，还有 errorFileName不为空)
                if (errorList.size() > 0 || !StringUtils.isEmpty(errorFileName)) {
                    $info("导入时出现错误001,channel：" + channel.getFull_name());
                    // 生成错误文件
                    createErrorFile(errorList, 0);

                    // 移动文件到bak目录下
                    moveFeedFileToBak(feedFileName);

                    // 把文件管理的状态变为4：导入错误
                    VmsBtFeedFileModel feedFileModel = new VmsBtFeedFileModel();
                    // 更新条件
                    feedFileModel.setChannelId(channel.getOrder_channel_id());
                    feedFileModel.setFileName(feedFileName);
                    // 更新内容
                    feedFileModel.setErrorFileName(errorFileName);
                    feedFileModel.setStatus(VmsConstants.FeedFileStatus.IMPORT_ERROR);
                    feedFileModel.setModifier(getTaskName());
                    vmsBtFeedFileDaoExt.updateErrorInfo(feedFileModel);
                    return;
                }

                int codeCnt = 0;
                int skuCnt = 0;
                int i = 1;
                // 取得需要处理的Code级别的数据,每次取得固定件数(100件)
                while (true) {
                    List<CmsBtFeedInfoModel> feedInfoModelList = new ArrayList<>();
                    Map<String, Object> param = new HashMap<>();
                    param.put("channelId", channel.getOrder_channel_id());
                    param.put("parentId", BLANK_PARENT_ID);
                    param.put("updateFlg", "0");
                    List<VmsBtFeedInfoTempModel> codeList = vmsBtFeedInfoTempDaoExt.selectList(param);
                    // 直到全部拿完那么终止
                    if (codeList.size() == 0) {
                        break;
                    }
                    $info("***************处理件数:" + String.valueOf(i * 100) + ",channel：" + channel.getFull_name());
                    for (VmsBtFeedInfoTempModel codeModel : codeList) {
                        Map<String, Object> param1 = new HashMap<>();
                        param1.put("channelId", channel.getOrder_channel_id());
                        param1.put("parentId", codeModel.getSku());
                        param1.put("updateFlg", "0");
                        List<VmsBtFeedInfoTempModel> skuModels = vmsBtFeedInfoTempDaoExt.selectList(param1);
                        CmsBtFeedInfoModel model = checkByCodeGroup(codeModel, skuModels, errorList);
                        if (model != null) {
                            feedInfoModelList.add(model);
                        }
                        // 更新VmsBtFeedInfoTemp表状态为1：update完了
                        // 更新Code级别数据
                        VmsBtFeedInfoTempModel updateCodeModel = new VmsBtFeedInfoTempModel();
                        // 更新条件
                        updateCodeModel.setId(codeModel.getId());
                        // 更新对象
                        updateCodeModel.setUpdateFlg("1");
                        vmsBtFeedInfoTempDao.update(updateCodeModel);
                        if (skuModels.size() > 0) {
                            // 更新Sku级别数据
                            VmsBtFeedInfoTempModel updateSkuModel = new VmsBtFeedInfoTempModel();
                            // 更新条件
                            updateSkuModel.setChannelId(channel.getOrder_channel_id());
                            updateSkuModel.setParentId(codeModel.getSku());
                            // 更新对象
                            updateSkuModel.setUpdateFlg("1");
                            vmsBtFeedInfoTempDaoExt.updateStatus(updateSkuModel);
                        }
                    }

                    // 插入MongoDb表
                    if (feedInfoModelList.size() > 0) {
                        $info("point-mongo-start" + ",channel：" + channel.getFull_name());
                        Map<String, List<CmsBtFeedInfoModel>> response = feedToCmsService.updateProduct(channel
                                        .getOrder_channel_id(), feedInfoModelList, getTaskName());
                        List<CmsBtFeedInfoModel> succeed = response.get("succeed");
                        codeCnt += succeed.size();
                        skuCnt += succeed.stream().mapToInt((model) -> model.getSkus().size()).summaryStatistics().getSum();
                        $info("point-mongo-end" + ",channel：" + channel.getFull_name());
                    }
                    i++;
                }
                $info("插入MongoDb表,成功Code数: " + codeCnt + ", Sku数:" + skuCnt + ",channel：" + channel.getFull_name());

                // 处理剩余的Sku件数（没有匹配上parent-id）
                i = 1;
                while (true) {
                    Map<String, Object> param = new HashMap<>();
                    param.put("channelId", channel.getOrder_channel_id());
                    param.put("updateFlg", "0");
                    List<VmsBtFeedInfoTempModel> otherList = vmsBtFeedInfoTempDaoExt.selectList(param);
                    // 直到全部拿完那么终止
                    if (otherList.size() == 0) {
                        break;
                    }
                    $info("***************处理剩余件数:" + String.valueOf(i * 100) + ",channel：" + channel.getFull_name());
                    for (VmsBtFeedInfoTempModel otherModel : otherList) {
                        // parent-id(%s) is invalidate.
                        addErrorMessage(errorList, "8000004", new Object[]{otherModel.getParentId()}, otherModel.getSku(), columnMap.get(PARENT_ID));
                        // sku的共通属性check
                        checkSkuCommon(otherModel, errorList);

                        // 更新VmsBtFeedInfoTemp表状态为1：update完了
                        // 更新Code级别数据
                        VmsBtFeedInfoTempModel updateCodeModel = new VmsBtFeedInfoTempModel();
                        // 更新条件
                        updateCodeModel.setId(otherModel.getId());
                        // 更新对象
                        updateCodeModel.setUpdateFlg("1");
                        vmsBtFeedInfoTempDao.update(updateCodeModel);
                    }
                }
                // 如果有错误的情况下(因为error达到固定件数就会先出到文件，所以ERROR的条件 除了 errorList>0以外，还有 errorFileName不为空)
                if (errorList.size() > 0 || !StringUtils.isEmpty(errorFileName)) {
                    $info("导入时出现错误002,channel：" + channel.getFull_name());
                    // 生成错误文件
                    createErrorFile(errorList, codeCnt);

                    // 移动文件到bak目录下
                    moveFeedFileToBak(feedFileName);

                    // 把文件管理的状态变为4：导入错误
                    VmsBtFeedFileModel feedFileModel = new VmsBtFeedFileModel();
                    // 更新条件
                    feedFileModel.setChannelId(channel.getOrder_channel_id());
                    feedFileModel.setFileName(feedFileName);
                    // 更新内容
                    feedFileModel.setErrorFileName(errorFileName);
                    feedFileModel.setUpdatedCodeNum(codeCnt);
                    feedFileModel.setUpdatedSkuNum(skuCnt);
                    feedFileModel.setStatus(VmsConstants.FeedFileStatus.IMPORT_ERROR);
                    feedFileModel.setModifier(getTaskName());
                    vmsBtFeedFileDaoExt.updateErrorInfo(feedFileModel);
                } else {
                    // 移动文件到bak目录下
                    moveFeedFileToBak(feedFileName);

                    // 把文件管理的状态变为3：导入成功
                    VmsBtFeedFileModel feedFileModel = new VmsBtFeedFileModel();
                    // 更新条件
                    feedFileModel.setChannelId(channel.getOrder_channel_id());
                    feedFileModel.setFileName(feedFileName);
                    // 更新内容
                    feedFileModel.setErrorFileName("");
                    feedFileModel.setUpdatedCodeNum(codeCnt);
                    feedFileModel.setUpdatedSkuNum(skuCnt);
                    feedFileModel.setStatus(VmsConstants.FeedFileStatus.IMPORT_COMPLETED);
                    feedFileModel.setModifier(getTaskName());
                    vmsBtFeedFileDaoExt.updateErrorInfo(feedFileModel);
                    VmsChannelConfigBean vmsUpdateInventory = VmsChannelConfigs.getConfigBean(channel.getOrder_channel_id(), VmsConstants.ChannelConfig.UPDATE_INVENTORY,
                            VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);
                    if (vmsUpdateInventory == null || "1".equals(vmsUpdateInventory.getConfigValue1())) {
                        // 库存同步
                        clientInventoryService.updateClientInventorySynFlag(channel.getOrder_channel_id());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                $error(ex.getMessage());
                throw ex;
            }
        }

        /**
         * check数据，没有问题的话，保存为FeedInfo对象
         *
         * @param codeModel Code级别数据
         * @param skuModels Sku级别数据
         * @param errorList 所有Error内容
         * @return FeedInfo对象
         */
        private CmsBtFeedInfoModel checkByCodeGroup(VmsBtFeedInfoTempModel codeModel, List<VmsBtFeedInfoTempModel> skuModels, List<Map<String, Object>> errorList) throws IOException {

            // CodeModel是否同时也是Sku
            boolean isSkuLevel = false;
            // 这一组Code数据是否有error
            boolean errorFlg = false;
            // 如果没有其他指向他的数据，那么这个Code同时又是Sku
            if (skuModels == null || skuModels.size() == 0) {
                isSkuLevel = true;
            }

            // 先Check Code应该有的那些内容
            // Code
            String sku = codeModel.getSku();

            // title
            String title = codeModel.getTitle();
            // 如果这行是Code,那么title是必须的
            if (StringUtils.isEmpty(title)) {
                // title is Required.
                addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(TITLE)}, codeModel.getSku(), columnMap.get(TITLE));
                errorFlg = true;
            }

            // images
            String images = codeModel.getImages();
            // 如果这行是Code,那么images是必须的
            if (StringUtils.isEmpty(images)) {
                // images is Required.
                addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(IMAGES)}, codeModel.getSku(), columnMap.get(IMAGES));
                errorFlg = true;
            }

            // description
            String description = codeModel.getDescription();
            // 如果这行是Code,那么description是必须的
            if (StringUtils.isEmpty(description)) {
                // description is Required.
                addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(DESCRIPTION)}, codeModel.getSku(), columnMap.get(DESCRIPTION));
                errorFlg = true;
            }

            // short-description
            String shortDescription = codeModel.getShortDescription();
            // 如果这行是Code,那么short-description是必须的
            if (StringUtils.isEmpty(shortDescription)) {
                // short-description is Required.
                addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(SHORT_DESCRIPTION)}, codeModel.getSku(), columnMap.get(SHORT_DESCRIPTION));
                errorFlg = true;
            }

            // product-origin
            String productOrigin = codeModel.getProductOrigin();
            // 如果这行是Code,那么product-origin是必须的
            if (StringUtils.isEmpty(productOrigin)) {
                // product-origin is Required.
                addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(PRODUCT_ORIGIN)}, codeModel.getSku(), columnMap.get(PRODUCT_ORIGIN));
                errorFlg = true;
            }

            // category
            String category = codeModel.getCategory();
            String[] categoryArray = category.split("/");
            category = "";
            // productType取Category的第一级
            String productType = "";
            for (String categoryItem : categoryArray) {
                // 不等于空的情况下，去掉首尾空格，并替换半角横杠为全角横杠，重新组装一下
                if (!StringUtils.isEmpty(categoryItem)) {
                    category += categoryItem.trim().replaceAll("-", "－") + "-";
                    if (StringUtils.isEmpty(productType)) {
                        productType = categoryItem.trim().replaceAll("-", "－");
                    }
                }
            }
            // 去掉最后一个分隔符[-]
            if (!StringUtils.isEmpty(category)) {
                category = category.substring(0, category.length() - 1);
            }
            // 如果这行是Code,那么category是必须的
            if (StringUtils.isEmpty(category)) {
                // category is Required.
                addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(CATEGORY)}, codeModel.getSku(), columnMap.get(CATEGORY));
                errorFlg = true;
            }

            // brand
            String brand = codeModel.getBrand();
            // 如果这行是Code,那么brand是必须的
            if (StringUtils.isEmpty(brand)) {
                // brand is Required.
                addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(BRAND)}, codeModel.getSku(), columnMap.get(BRAND));
                errorFlg = true;
            }

            // Materials
            String materials = codeModel.getMaterials();
            // 如果这行是Code,那么materials是必须的
            if (StringUtils.isEmpty(materials)) {
                // materials is Required.
                addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(MATERIALS)}, codeModel.getSku(), columnMap.get(MATERIALS));
                errorFlg = true;
            }

            // Vendor-Product-Url
            String vendorProductUrl = codeModel.getVendorProductUrl();

            // AttributeKey
            Map<String, List<String>> attributeMap = new HashMap<>();
            makeAttributeMap(attributeMap, codeModel, "code");


            // 如果这行Code是Sku的话还需要CheckSku
            if (isSkuLevel) {
                // sku的共通属性check
                if (checkSkuCommon(codeModel, errorList)) {
                    errorFlg = true;
                }
            }

            // 再Check Sku应该有的那些内容
            if (skuModels != null && skuModels.size() > 0) {
                // 同一parent-id下,Sku的唯一标识的集合
                List<String> skuKeys = new ArrayList<>();
                for (VmsBtFeedInfoTempModel skuTemp : skuModels) {
                    // sku的共通属性check
                    if (checkSkuCommon(skuTemp, errorList)) {
                        errorFlg = true;
                    }

                    // variation-theme
                    String variationTheme = skuTemp.getVariationTheme();
                    // 如果存在多个sku，那么variation-theme必须指定
                    if (skuModels.size() >= 2 && StringUtils.isEmpty(variationTheme)) {
                        // variation-theme is Required.
                        addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(VARIATION_THEME)}, skuTemp.getSku(), columnMap.get(VARIATION_THEME));
                        errorFlg = true;
                    }

                    // check同一parent-id下面每个Sku的唯一标识
                    if (!StringUtils.isEmpty(variationTheme)) {

                        // 从Attribute属性里 找到唯一标识
                        Map<String, String> skuKeyMap = getSkuKey(skuTemp);
                        String keyValue = skuKeyMap.get("Value");
                        // 去掉key中的空格
                        if (!StringUtils.isEmpty(keyValue)) {
                            keyValue = keyValue.replaceAll(" ", "");
                        }

                        // 没有设定Sku唯一标识的情况下
                        if (StringUtils.isEmpty(keyValue)) {
                            // %s(variation-theme) must be set in attribute.
                            addErrorMessage(errorList, "8000008", new Object[]{variationTheme}, skuTemp.getSku(), "attribute");
                            errorFlg = true;
                        }

                        // Sku唯一标识重复的情况下
                        if (!StringUtils.isEmpty(keyValue)) {
                            if (skuKeys.contains(keyValue)) {
                                // %s must be a unique value in same parent-id.
                                addErrorMessage(errorList, "8000010", new Object[]{variationTheme}, skuTemp.getSku(), "attribute-key-" + skuKeyMap.get("AttributeNum"));
                                errorFlg = true;
                            } else {
                                skuKeys.add(keyValue);
                            }
                        }
                    }
                }
            }


            // check没有错误的情况下，生成CmsBtFeedInfoModel
            CmsBtFeedInfoModel feedInfo = null;
            if (!errorFlg) {
                feedInfo = new CmsBtFeedInfoModel();
                feedInfo.setChannelId(channel.getOrder_channel_id());
                feedInfo.setCode(sku);
                feedInfo.setModel(sku);
                feedInfo.setCategory(category);
                feedInfo.setCatId(MD5.getMD5(category));
                feedInfo.setName(title);
                feedInfo.setImage(Arrays.asList(images.split(",")));
                feedInfo.setLongDescription(description);
                feedInfo.setShortDescription(shortDescription);
                feedInfo.setOrigin(productOrigin);
                // feedInfo.setWeight(weight);
                feedInfo.setBrand(brand);
                feedInfo.setMaterial(materials);
                feedInfo.setClientProductURL(vendorProductUrl);
                if (productTypeByCategoryMap.get(productType) != null) {
                    productType = productTypeByCategoryMap.get(productType);
                }
                feedInfo.setProductType(productType);
                if (attributeMap.get("color") != null) {
                    feedInfo.setColor(attributeMap.get("color").get(0));
                } else {
                    feedInfo.setColor("");
                }
                if (attributeMap.get("gender") != null) {
                    feedInfo.setSizeType(attributeMap.get("gender").get(0));
                } else {
                    feedInfo.setSizeType("No Size Type");
                }
                // 加入Sku
                List<CmsBtFeedInfoModel_Sku> skusModel = new ArrayList<>();
                feedInfo.setSkus(skusModel);
                if (isSkuLevel) {
                    // 创建Sku
                    CmsBtFeedInfoModel_Sku skuModel = new CmsBtFeedInfoModel_Sku();
                    skuModel.setBarcode(codeModel.getProductId());
                    skuModel.setClientSku(codeModel.getSku());
                    if (!"030".equals(channel.getOrder_channel_id())) {
                        skuModel.setSku(channel.getOrder_channel_id() + "-" + codeModel.getSku());
                    } else {
                        skuModel.setSku(codeModel.getSku());
                    }
                    skuModel.setSize("One Size");
                    skuModel.setImage(Arrays.asList(images.split(",")));
                    skuModel.setQty(new Integer(codeModel.getQuantity()));
                    skuModel.setRelationshipType("");
                    skuModel.setVariationTheme("");
                    setFeedWeight(skuModel, feedInfo, codeModel.getWeight());
                    if (!StringUtils.isEmpty(codeModel.getMsrp())) {
                        skuModel.setPriceClientMsrp(new BigDecimal(codeModel.getMsrp()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    } else {
                        skuModel.setPriceClientMsrp(new BigDecimal(codeModel.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    }
                    skuModel.setPriceClientRetail(new BigDecimal(codeModel.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    skuModel.setPriceNet(new BigDecimal(codeModel.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    skuModel.setPriceMsrp(calculatePriceByFormula(skuModel, priceMsrpFormula));
                    skuModel.setPriceCurrent(calculatePriceByFormula(skuModel, priceRetailFormula));
                    Map<String, String> skuAttributeMap = new HashMap<>();
                    makeAttributeMap(skuAttributeMap, codeModel, "sku");
                    skuModel.setAttribute(skuAttributeMap);
                    skusModel.add(skuModel);
                } else {
                    for (VmsBtFeedInfoTempModel skuTemp : skuModels) {
                        // 创建Sku
                        CmsBtFeedInfoModel_Sku skuModel = new CmsBtFeedInfoModel_Sku();
                        skuModel.setBarcode(skuTemp.getProductId());
                        skuModel.setClientSku(skuTemp.getSku());
                        Map<String, String> skuKeyMap = getSkuKey(skuTemp);
                        // skuModel.setSku(channel.getOrder_channel_id() + "-" + codeModel.getSku() + "-" + skuKeyMap.get("Value").replaceAll(" ", ""));
                        if (!"030".equals(channel.getOrder_channel_id())) {
                            skuModel.setSku(channel.getOrder_channel_id() + "-" + skuTemp.getSku());
                        }  else {
                            skuModel.setSku(skuTemp.getSku());
                        }
                        skuModel.setSize(skuKeyMap.get("Value"));
                        skuModel.setImage(Arrays.asList(images.split(",")));
                        skuModel.setQty(new Integer(skuTemp.getQuantity()));
                        skuModel.setRelationshipType(skuTemp.getRelationshipType());
                        skuModel.setVariationTheme(skuTemp.getVariationTheme());
                        setFeedWeight(skuModel, feedInfo, skuTemp.getWeight());
                        if (!StringUtils.isEmpty(skuTemp.getMsrp())) {
                            skuModel.setPriceClientMsrp(new BigDecimal(skuTemp.getMsrp()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        } else {
                            skuModel.setPriceClientMsrp(new BigDecimal(skuTemp.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        }

                        skuModel.setPriceClientRetail(new BigDecimal(skuTemp.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        skuModel.setPriceNet(new BigDecimal(skuTemp.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//
                        skuModel.setPriceMsrp(calculatePriceByFormula(skuModel, priceMsrpFormula));
                        skuModel.setPriceCurrent(calculatePriceByFormula(skuModel, priceRetailFormula));
                        Map<String, String> skuAttributeMap = new HashMap<>();
                        makeAttributeMap(skuAttributeMap, skuTemp, "sku");
                        skuModel.setAttribute(skuAttributeMap);
                        skusModel.add(skuModel);
                    }
                }

                // AttributeVms存放用户导入Feed的Attribute
                feedInfo.setAttributeVms(attributeMap);
                // AttributeVms存放用户导入Feed的Attribute加上所有共通属性
                feedInfo.setAttribute(createCmsAttribute(attributeMap, feedInfo));
            }

            return feedInfo;
        }

        /**
         * 做出Cms用的AttributeMap
         *
         * @param attributeMap 做出的AttributeMap
         * @param feedInfo    CmsBtFeedInfoModel
         * @return Cms用的AttributeMap
         */
        private Map<String, List<String>> createCmsAttribute(Map<String, List<String>> attributeMap, CmsBtFeedInfoModel feedInfo) {
            Map<String, List<String>> cmsAttributeMap = new HashMap<>();
            // 先加入attributeMap里的内容
            for (Map.Entry<String, List<String>> entry : attributeMap.entrySet()) {
                cmsAttributeMap.put(entry.getKey(), entry.getValue());
            }

            // 加入共通属性
            // title
            cmsAttributeMap.put("title", new ArrayList<String>() {{
                this.add(feedInfo.getName());
            }});

            // description
            cmsAttributeMap.put("description", new ArrayList<String>() {{
                this.add(feedInfo.getLongDescription());
            }});

            // short-description
            cmsAttributeMap.put("short-description", new ArrayList<String>() {{
                this.add(feedInfo.getShortDescription());
            }});

            // product-origin
            cmsAttributeMap.put("product-origin", new ArrayList<String>() {{
                this.add(feedInfo.getOrigin());
            }});

            // product-type
            cmsAttributeMap.put("product-type", new ArrayList<String>() {{
                this.add(feedInfo.getProductType());
            }});

            // category
            cmsAttributeMap.put("category", new ArrayList<String>() {{
                this.add(feedInfo.getCategory());
            }});

            // brand
            cmsAttributeMap.put("brand", new ArrayList<String>() {{
                this.add(feedInfo.getBrand());
            }});

            // materials
            cmsAttributeMap.put("materials", new ArrayList<String>() {{
                this.add(feedInfo.getMaterial());
            }});

            // weight
            cmsAttributeMap.put("weight", new ArrayList<String>() {{
                if (feedInfo.getSkus().size() > 0) {
                    this.add(feedInfo.getSkus().get(0).getWeightOrg() + " " + feedInfo.getSkus().get(0).getWeightOrgUnit());
                }
            }});

            return cmsAttributeMap;
        }

        /**
         * 做出AttributeMap
         *
         * @param attributeMap 做出的AttributeMap
         * @param codeModel    VmsBtFeedInfoTempModel
         * @param flag         'code' or 'sku'
         * @return 可变主题对应Attribute的序号和内容
         */
        private void makeAttributeMap(Map attributeMap, VmsBtFeedInfoTempModel codeModel, String flag) {
            String attributeKey1 = codeModel.getAttributeKey1();
            String attributeKey2 = codeModel.getAttributeKey2();
            String attributeKey3 = codeModel.getAttributeKey3();
            String attributeKey4 = codeModel.getAttributeKey4();
            String attributeKey5 = codeModel.getAttributeKey5();
            String attributeKey6 = codeModel.getAttributeKey6();
            String attributeKey7 = codeModel.getAttributeKey7();
            String attributeKey8 = codeModel.getAttributeKey8();
            String attributeKey9 = codeModel.getAttributeKey9();
            String attributeKey10 = codeModel.getAttributeKey10();
            String attributeKey11 = codeModel.getAttributeKey11();
            String attributeKey12 = codeModel.getAttributeKey12();
            String attributeKey13 = codeModel.getAttributeKey13();
            String attributeKey14 = codeModel.getAttributeKey14();
            String attributeKey15 = codeModel.getAttributeKey15();
            String attributeKey16 = codeModel.getAttributeKey16();
            String attributeKey17 = codeModel.getAttributeKey17();
            String attributeKey18 = codeModel.getAttributeKey18();
            String attributeKey19 = codeModel.getAttributeKey19();
            String attributeKey20 = codeModel.getAttributeKey20();
            // AttributeValue
            String attributeValue1 = codeModel.getAttributeValue1();
            String attributeValue2 = codeModel.getAttributeValue2();
            String attributeValue3 = codeModel.getAttributeValue3();
            String attributeValue4 = codeModel.getAttributeValue4();
            String attributeValue5 = codeModel.getAttributeValue5();
            String attributeValue6 = codeModel.getAttributeValue6();
            String attributeValue7 = codeModel.getAttributeValue7();
            String attributeValue8 = codeModel.getAttributeValue8();
            String attributeValue9 = codeModel.getAttributeValue9();
            String attributeValue10 = codeModel.getAttributeValue10();
            String attributeValue11 = codeModel.getAttributeValue11();
            String attributeValue12 = codeModel.getAttributeValue12();
            String attributeValue13 = codeModel.getAttributeValue13();
            String attributeValue14 = codeModel.getAttributeValue14();
            String attributeValue15 = codeModel.getAttributeValue15();
            String attributeValue16 = codeModel.getAttributeValue16();
            String attributeValue17 = codeModel.getAttributeValue17();
            String attributeValue18 = codeModel.getAttributeValue18();
            String attributeValue19 = codeModel.getAttributeValue19();
            String attributeValue20 = codeModel.getAttributeValue20();
            if (!StringUtils.isEmpty(attributeKey1)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey1, new ArrayList<String>() {{
                        this.add(attributeValue1);
                    }});
                } else {
                    attributeMap.put(attributeKey1, attributeValue1);
                }
            }
            if (!StringUtils.isEmpty(attributeKey2)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey2, new ArrayList<String>() {{
                        this.add(attributeValue2);
                    }});
                } else {
                    attributeMap.put(attributeKey2, attributeValue2);
                }
            }
            if (!StringUtils.isEmpty(attributeKey3)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey3, new ArrayList<String>() {{
                        this.add(attributeValue3);
                    }});
                } else {
                    attributeMap.put(attributeKey3, attributeValue3);
                }
            }
            if (!StringUtils.isEmpty(attributeKey4)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey4, new ArrayList<String>() {{
                        this.add(attributeValue4);
                    }});
                } else {
                    attributeMap.put(attributeKey4, attributeValue4);
                }
            }
            if (!StringUtils.isEmpty(attributeKey5)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey5, new ArrayList<String>() {{
                        this.add(attributeValue5);
                    }});
                } else {
                    attributeMap.put(attributeKey5, attributeValue5);
                }
            }
            if (!StringUtils.isEmpty(attributeKey6)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey6, new ArrayList<String>() {{
                        this.add(attributeValue6);
                    }});
                } else {
                    attributeMap.put(attributeKey6, attributeValue6);
                }
            }
            if (!StringUtils.isEmpty(attributeKey7)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey7, new ArrayList<String>() {{
                        this.add(attributeValue7);
                    }});
                } else {
                    attributeMap.put(attributeKey7, attributeValue7);
                }
            }
            if (!StringUtils.isEmpty(attributeKey8)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey8, new ArrayList<String>() {{
                        this.add(attributeValue8);
                    }});
                } else {
                    attributeMap.put(attributeKey8, attributeValue8);
                }
            }
            if (!StringUtils.isEmpty(attributeKey9)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey9, new ArrayList<String>() {{
                        this.add(attributeValue9);
                    }});
                } else {
                    attributeMap.put(attributeKey9, attributeValue9);
                }
            }
            if (!StringUtils.isEmpty(attributeKey10)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey10, new ArrayList<String>() {{
                        this.add(attributeValue10);
                    }});
                } else {
                    attributeMap.put(attributeKey10, attributeValue10);
                }
            }
            if (!StringUtils.isEmpty(attributeKey11)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey11, new ArrayList<String>() {{
                        this.add(attributeValue11);
                    }});
                } else {
                    attributeMap.put(attributeKey11, attributeValue11);
                }
            }
            if (!StringUtils.isEmpty(attributeKey12)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey12, new ArrayList<String>() {{
                        this.add(attributeValue12);
                    }});
                } else {
                    attributeMap.put(attributeKey12, attributeValue12);
                }
            }
            if (!StringUtils.isEmpty(attributeKey13)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey13, new ArrayList<String>() {{
                        this.add(attributeValue13);
                    }});
                } else {
                    attributeMap.put(attributeKey13, attributeValue13);
                }
            }
            if (!StringUtils.isEmpty(attributeKey14)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey14, new ArrayList<String>() {{
                        this.add(attributeValue14);
                    }});
                } else {
                    attributeMap.put(attributeKey14, attributeValue14);
                }
            }
            if (!StringUtils.isEmpty(attributeKey15)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey15, new ArrayList<String>() {{
                        this.add(attributeValue15);
                    }});
                } else {
                    attributeMap.put(attributeKey15, attributeValue15);
                }
            }
            if (!StringUtils.isEmpty(attributeKey16)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey16, new ArrayList<String>() {{
                        this.add(attributeValue16);
                    }});
                } else {
                    attributeMap.put(attributeKey16, attributeValue16);
                }
            }
            if (!StringUtils.isEmpty(attributeKey17)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey17, new ArrayList<String>() {{
                        this.add(attributeValue17);
                    }});
                } else {
                    attributeMap.put(attributeKey17, attributeValue17);
                }
            }
            if (!StringUtils.isEmpty(attributeKey18)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey18, new ArrayList<String>() {{
                        this.add(attributeValue18);
                    }});
                } else {
                    attributeMap.put(attributeKey18, attributeValue18);
                }
            }
            if (!StringUtils.isEmpty(attributeKey19)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey19, new ArrayList<String>() {{
                        this.add(attributeValue19);
                    }});
                } else {
                    attributeMap.put(attributeKey19, attributeValue19);
                }
            }
            if (!StringUtils.isEmpty(attributeKey20)) {
                if ("code".equals(flag)) {
                    attributeMap.put(attributeKey20, new ArrayList<String>() {{
                        this.add(attributeValue20);
                    }});
                } else {
                    attributeMap.put(attributeKey20, attributeValue20);
                }
            }
        }

        /**
         * 从Attribute属性中找出可变主题
         *
         * @param skuModel sku级别数据
         * @return 可变主题对应Attribute的序号和内容
         */
        private Map<String, String> getSkuKey(VmsBtFeedInfoTempModel skuModel) {
            Map<String, String> returnMap = new HashMap<>();
            // 可变主题
            String variationTheme = skuModel.getVariationTheme();
            variationTheme = variationTheme.toLowerCase();

            // AttributeKey
            String attributeKey1 = skuModel.getAttributeKey1();
            if (variationTheme.equals(attributeKey1.toLowerCase())) {
                returnMap.put("AttributeNum", "1");
                returnMap.put("Value", skuModel.getAttributeValue1());
                return returnMap;
            }
            String attributeKey2 = skuModel.getAttributeKey2();
            if (variationTheme.equals(attributeKey2.toLowerCase())) {
                returnMap.put("AttributeNum", "2");
                returnMap.put("Value", skuModel.getAttributeValue2());
                return returnMap;
            }
            String attributeKey3 = skuModel.getAttributeKey3();
            if (variationTheme.equals(attributeKey3.toLowerCase())) {
                returnMap.put("AttributeNum", "3");
                returnMap.put("Value", skuModel.getAttributeValue3());
                return returnMap;
            }
            String attributeKey4 = skuModel.getAttributeKey4();
            if (variationTheme.equals(attributeKey4.toLowerCase())) {
                returnMap.put("AttributeNum", "4");
                returnMap.put("Value", skuModel.getAttributeValue4());
                return returnMap;
            }
            String attributeKey5 = skuModel.getAttributeKey5();
            if (variationTheme.equals(attributeKey5.toLowerCase())) {
                returnMap.put("AttributeNum", "5");
                returnMap.put("Value", skuModel.getAttributeValue5());
                return returnMap;
            }
            String attributeKey6 = skuModel.getAttributeKey6();
            if (variationTheme.equals(attributeKey6.toLowerCase())) {
                returnMap.put("AttributeNum", "6");
                returnMap.put("Value", skuModel.getAttributeValue6());
                return returnMap;
            }
            String attributeKey7 = skuModel.getAttributeKey7();
            if (variationTheme.equals(attributeKey7.toLowerCase())) {
                returnMap.put("AttributeNum", "7");
                returnMap.put("Value", skuModel.getAttributeValue7());
                return returnMap;
            }
            String attributeKey8 = skuModel.getAttributeKey8();
            if (variationTheme.equals(attributeKey8.toLowerCase())) {
                returnMap.put("AttributeNum", "8");
                returnMap.put("Value", skuModel.getAttributeValue8());
                return returnMap;
            }
            String attributeKey9 = skuModel.getAttributeKey9();
            if (variationTheme.equals(attributeKey9.toLowerCase())) {
                returnMap.put("AttributeNum", "9");
                returnMap.put("Value", skuModel.getAttributeValue9());
                return returnMap;
            }
            String attributeKey10 = skuModel.getAttributeKey10();
            if (variationTheme.equals(attributeKey10.toLowerCase())) {
                returnMap.put("AttributeNum", "10");
                returnMap.put("Value", skuModel.getAttributeValue10());
                return returnMap;
            }
            String attributeKey11 = skuModel.getAttributeKey11();
            if (variationTheme.equals(attributeKey11.toLowerCase())) {
                returnMap.put("AttributeNum", "11");
                returnMap.put("Value", skuModel.getAttributeValue11());
                return returnMap;
            }
            String attributeKey12 = skuModel.getAttributeKey12();
            if (variationTheme.equals(attributeKey12.toLowerCase())) {
                returnMap.put("AttributeNum", "12");
                returnMap.put("Value", skuModel.getAttributeValue12());
                return returnMap;
            }
            String attributeKey13 = skuModel.getAttributeKey13();
            if (variationTheme.equals(attributeKey13.toLowerCase())) {
                returnMap.put("AttributeNum", "13");
                returnMap.put("Value", skuModel.getAttributeValue13());
                return returnMap;
            }
            String attributeKey14 = skuModel.getAttributeKey14();
            if (variationTheme.equals(attributeKey14.toLowerCase())) {
                returnMap.put("AttributeNum", "14");
                returnMap.put("Value", skuModel.getAttributeValue14());
                return returnMap;
            }
            String attributeKey15 = skuModel.getAttributeKey15();
            if (variationTheme.equals(attributeKey15.toLowerCase())) {
                returnMap.put("AttributeNum", "15");
                returnMap.put("Value", skuModel.getAttributeValue15());
                return returnMap;
            }
            String attributeKey16 = skuModel.getAttributeKey16();
            if (variationTheme.equals(attributeKey16.toLowerCase())) {
                returnMap.put("AttributeNum", "16");
                returnMap.put("Value", skuModel.getAttributeValue16());
                return returnMap;
            }
            String attributeKey17 = skuModel.getAttributeKey17();
            if (variationTheme.equals(attributeKey17.toLowerCase())) {
                returnMap.put("AttributeNum", "17");
                returnMap.put("Value", skuModel.getAttributeValue17());
                return returnMap;
            }
            String attributeKey18 = skuModel.getAttributeKey18();
            if (variationTheme.equals(attributeKey18.toLowerCase())) {
                returnMap.put("AttributeNum", "18");
                returnMap.put("Value", skuModel.getAttributeValue18());
                return returnMap;
            }
            String attributeKey19 = skuModel.getAttributeKey19();
            if (variationTheme.equals(attributeKey19.toLowerCase())) {
                returnMap.put("AttributeNum", "19");
                returnMap.put("Value", skuModel.getAttributeValue19());
                return returnMap;
            }
            String attributeKey20 = skuModel.getAttributeKey20();
            if (variationTheme.equals(attributeKey20.toLowerCase())) {
                returnMap.put("AttributeNum", "20");
                returnMap.put("Value", skuModel.getAttributeValue20());
                return returnMap;
            }
            return returnMap;
        }

        /**
         * checkSku数据
         *
         * @param codeModel Code级别数据
         * @param errorList 所有Error内容
         * @return 是否有错误
         */
        private boolean checkSkuCommon(VmsBtFeedInfoTempModel codeModel, List<Map<String, Object>> errorList) throws IOException {

            // 是否有错误
            boolean errorFlg = false;

            // 如果sku在feedInfo表中存在，那么parent id是不能变更的
            if (!BLANK_PARENT_ID.equals(codeModel.getParentId())) {
                JongoQuery queryObject = new JongoQuery();
                queryObject.setQuery("{\"skus.clientSku\":\"" + codeModel.getSku() + "\"}");
                List<CmsBtFeedInfoModel> feeds = feedInfoService.getList(channel.getOrder_channel_id(), queryObject);
                if (feeds.size() > 0 && !feeds.get(0).getCode().equals(codeModel.getParentId())) {
                    // The parent-id can not be changed from %s to %s.
                    addErrorMessage(errorList, "8000007", new Object[]{feeds.get(0).getCode(), codeModel.getParentId()}, codeModel.getSku(), columnMap.get(PARENT_ID));
                    errorFlg = true;
                }
            }

            // product-id
            String productId = codeModel.getProductId();
            // product-id是必须的
            if (StringUtils.isEmpty(productId)) {
                // product-id is Required.
                addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(PRODUCT_ID)}, codeModel.getSku(), columnMap.get(PRODUCT_ID));
                errorFlg = true;
            }

            // price
            String price = codeModel.getPrice();
            // price是必须的,并且是大于0的数字
            if (StringUtils.isEmpty(price) || !StringUtils.isNumeric(price) || Float.valueOf(price) <= 0) {
                // price must be a Number more than 0.
                addErrorMessage(errorList, "8000005", new Object[]{columnMap.get(PRICE)}, codeModel.getSku(), columnMap.get(PRICE));
                errorFlg = true;
            }

            // msrp
            String msrp = codeModel.getMsrp();
            if (!StringUtils.isEmpty(msrp)) {
                // msrp如果输入，必须是大于0的数字
                if (!StringUtils.isNumeric(msrp) || Float.valueOf(msrp) <= 0) {
                    // msrp must be a Number more than 0.
                    addErrorMessage(errorList, "8000005", new Object[]{columnMap.get(MSRP)}, codeModel.getSku(), columnMap.get(MSRP));
                    errorFlg = true;
                }
            }

            // quantity
            String quantity = codeModel.getQuantity();
            // quantity是必须的,并且是大于0的数字
            if (StringUtils.isEmpty(quantity) || !StringUtils.isDigit(quantity)) {
                // quantity must be a positive Integer.
                addErrorMessage(errorList, "8000006", new Object[]{columnMap.get(QUANTITY)}, codeModel.getSku(), columnMap.get(QUANTITY));
                errorFlg = true;
            }

            // weight
            String weight = codeModel.getWeight();
            // 如果以有重量单位，那么去掉最后的重量单位
            if (!StringUtils.isEmpty(weight)) {
                for (String weightUnit : weightUnits) {
                    if (weight.toLowerCase().lastIndexOf(weightUnit) > 0) {
                        weight = weight.substring(0, weight.toLowerCase().lastIndexOf(weightUnit));
                        weight = weight.trim();
                        break;
                    }
                }
            }
            // 如果这行是Code,那么weight是必须是大于0的数字
            if (StringUtils.isEmpty(weight) || !StringUtils.isNumeric(weight) || Float.valueOf(weight) <= 0) {
                // weight must be a Number more than 0.
                addErrorMessage(errorList, "8000033", new Object[]{columnMap.get(WEIGHT)}, codeModel.getSku(), columnMap.get(WEIGHT));
                errorFlg = true;
            }

            return errorFlg;
        }


        /**
         * 读入Feed文件，插入到临时表vms_bt_feed_info_temp里
         *
         * @param feedFile 导入Feed文件
         * @return 是否有错误 true:有错；false：没错
         */
        private boolean readCsvToDB(File feedFile) throws IOException {

            try {
                // 删除临时表vms_bt_feed_info_temp里的数据
                deleteFeedInfoTemp(channel.getOrder_channel_id());

                // 转成vms_bt_feed_info_temp的Model列表
                List<VmsBtFeedInfoTempModel> feedInfoTempModels = new ArrayList<>();

                // Csv默认分割符号(逗号)
                char csvSplitSymbol = VmsConstants.COMMA;
                // Csv默认编码(utf-8)
                String csvEncode = VmsConstants.UTF_8;

                // 取得CSV分隔符
                VmsChannelConfigBean vmsChannelConfigBean = VmsChannelConfigs.getConfigBean(channel.getOrder_channel_id()
                        , VmsConstants.ChannelConfig.FEED_CSV_SPLIT_SYMBOL
                        , VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);
                if (vmsChannelConfigBean != null) {
                    csvSplitSymbol = vmsChannelConfigBean.getConfigValue1().charAt(0);
                }

                // 取得CSV编码
                vmsChannelConfigBean = VmsChannelConfigs.getConfigBean(channel.getOrder_channel_id()
                        , VmsConstants.ChannelConfig.FEED_CSV_ENCODE
                        , VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);
                if (vmsChannelConfigBean != null) {
                    csvEncode = vmsChannelConfigBean.getConfigValue1();
                }

                // Error信息
                StringBuilder error = new StringBuilder();

                CsvReader reader = new CsvReader(new FileInputStream(feedFile), csvSplitSymbol, Charset.forName(csvEncode));
                // Head读入
                boolean checkHeader = true;
                if (reader.readHeaders()) {
                    String headers[] = reader.getHeaders();
                    if (headers == null || headers.length < columnMap.size()) {
                        // 列数小于VoyageOneFeedTemplate定义的列数
                        checkHeader = false;
                    } else {
                        for (Map.Entry<Object, String> entry : columnMap.entrySet()) {
                            // 列名与VoyageOneFeedTemplate定义的列名不匹配
                            if (!entry.getValue().equals(headers[(int) entry.getKey()].trim())) {
                                checkHeader = false;
                                break;
                            }
                        }
                    }
                } else {
                    // 空文件的场合
                    checkHeader = false;
                }
                if (!checkHeader) {
                    addErrorMessage(error, "8000026", null, "", "");
                }

                int rowNum = 2;
                // header验证没有问题的情况下，mBody读入
                if (error.toString().length() == 0) {
                    while (reader.readRecord()) {
                        VmsBtFeedInfoTempModel model = new VmsBtFeedInfoTempModel();

                        int i = 0;
                        model.setChannelId(channel.getOrder_channel_id());
                        model.setRow(rowNum);
                        // sku
                        String sku = reader.get(i++);
                        // sku必须
                        if (StringUtils.isEmpty(sku)) {
                            addErrorMessage(error, "8000002", new Object[]{columnMap.get(SKU_INDEX)}, sku, columnMap.get(SKU_INDEX));
                        }
                        // sku长度验证
                        if (sku.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{columnMap.get(SKU_INDEX)}, sku, columnMap.get(SKU_INDEX));
                        }

                        model.setSku(sku);

                        // parent-id
                        String item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{columnMap.get(PARENT_ID)}, sku, columnMap.get(PARENT_ID));
                        }
                        if (StringUtils.isEmpty(item)) {
                            model.setParentId(BLANK_PARENT_ID);
                        } else {
                            model.setParentId(item);
                        }

                        // relationship-type
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{columnMap.get(RELATIONSHIP_TYPE)}, sku, columnMap.get(RELATIONSHIP_TYPE));
                        }
                        model.setRelationshipType(item);

                        // variation-theme
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{columnMap.get(VARIATION_THEME)}, sku, columnMap.get(VARIATION_THEME));
                        }
                        model.setVariationTheme(item);
                        model.setTitle(reader.get(i++));

                        // product-id
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{columnMap.get(PRODUCT_ID)}, sku, columnMap.get(PRODUCT_ID));
                        }
                        model.setProductId(item);

                        // price
                        item = reader.get(i++);
                        item = item.replaceAll("\\$", "");
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{columnMap.get(PRICE)}, sku, columnMap.get(PRICE));
                        }
                        model.setPrice(item);

                        // msrp
                        item = reader.get(i++);
                        item = item.replaceAll("\\$", "");
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{columnMap.get(MSRP)}, sku, columnMap.get(MSRP));
                        }
                        model.setMsrp(item);

                        // quantity
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{columnMap.get(QUANTITY)}, sku, columnMap.get(QUANTITY));
                        }
                        model.setQuantity(item);

                        // weight
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{columnMap.get(WEIGHT)}, sku, columnMap.get(WEIGHT));
                        }
                        model.setWeight(item);
                        model.setImages(reader.get(i++));
                        model.setDescription(reader.get(i++));
                        model.setShortDescription(reader.get(i++));

                        // product-origin
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{columnMap.get(PRODUCT_ORIGIN)}, sku, columnMap.get(PRODUCT_ORIGIN));
                        }
                        model.setProductOrigin(item);

                        // category
                        item = reader.get(i++);
                        if (item.getBytes().length > 500) {
                            addErrorMessage(error, "8000011", new Object[]{columnMap.get(CATEGORY)}, sku, columnMap.get(CATEGORY));
                        }
                        model.setCategory(item);

                        // brand
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{columnMap.get(BRAND)}, sku, columnMap.get(BRAND));
                        }
                        model.setBrand(item);

                        // materials
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{columnMap.get(MATERIALS)}, sku, columnMap.get(MATERIALS));
                        }
                        model.setMaterials(item);
                        model.setVendorProductUrl(reader.get(i++));

                        // attribute-key-1
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-1"}, sku, "attribute-key-1");
                        }
                        model.setAttributeKey1(item);
                        model.setAttributeValue1(reader.get(i++));
                        // attribute-key-2
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-2"}, sku, "attribute-key-2");
                        }
                        model.setAttributeKey2(item);
                        model.setAttributeValue2(reader.get(i++));
                        // attribute-key-3s
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-3"}, sku, "attribute-key-3");
                        }
                        model.setAttributeKey3(item);
                        model.setAttributeValue3(reader.get(i++));
                        // attribute-key-4
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-4"}, sku, "attribute-key-4");
                        }
                        model.setAttributeKey4(item);
                        model.setAttributeValue4(reader.get(i++));
                        // attribute-key-5
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-5"}, sku, "attribute-key-5");
                        }
                        model.setAttributeKey5(item);
                        model.setAttributeValue5(reader.get(i++));
                        // attribute-key-6
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-6"}, sku, "attribute-key-6");
                        }
                        model.setAttributeKey6(item);
                        model.setAttributeValue6(reader.get(i++));
                        // attribute-key-7
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-7"}, sku, "attribute-key-7");
                        }
                        model.setAttributeKey7(item);
                        model.setAttributeValue7(reader.get(i++));
                        // attribute-key-8
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-8"}, sku, "attribute-key-8");
                        }
                        model.setAttributeKey8(item);
                        model.setAttributeValue8(reader.get(i++));
                        // attribute-key-9
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-9"}, sku, "attribute-key-9");
                        }
                        model.setAttributeKey9(item);
                        model.setAttributeValue9(reader.get(i++));
                        // attribute-key-10
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-10"}, sku, "attribute-key-10");
                        }
                        model.setAttributeKey10(item);
                        model.setAttributeValue10(reader.get(i++));
                        // attribute-key-11
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-11"}, sku, "attribute-key-11");
                        }
                        model.setAttributeKey11(item);
                        model.setAttributeValue11(reader.get(i++));
                        // attribute-key-12
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-12"}, sku, "attribute-key-12");
                        }
                        model.setAttributeKey12(item);
                        model.setAttributeValue12(reader.get(i++));
                        // attribute-key-13
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-13"}, sku, "attribute-key-13");
                        }
                        model.setAttributeKey13(item);
                        model.setAttributeValue13(reader.get(i++));
                        // attribute-key-14
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-14"}, sku, "attribute-key-14");
                        }
                        model.setAttributeKey14(item);
                        model.setAttributeValue14(reader.get(i++));
                        // attribute-key-15
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-15"}, sku, "attribute-key-15");
                        }
                        model.setAttributeKey15(item);
                        model.setAttributeValue15(reader.get(i++));
                        // attribute-key-16
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-16"}, sku, "attribute-key-16");
                        }
                        model.setAttributeKey16(item);
                        model.setAttributeValue16(reader.get(i++));
                        // attribute-key-17
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-17"}, sku, "attribute-key-17");
                        }
                        model.setAttributeKey17(item);
                        model.setAttributeValue17(reader.get(i++));
                        // attribute-key-18
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-18"}, sku, "attribute-key-18");
                        }
                        model.setAttributeKey18(item);
                        model.setAttributeValue18(reader.get(i++));
                        // attribute-key-19
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-19"}, sku, "attribute-key-19");
                        }
                        model.setAttributeKey19(item);
                        model.setAttributeValue19(reader.get(i++));
                        // attribute-key-20
                        item = reader.get(i++);
                        if (item.getBytes().length > 128) {
                            addErrorMessage(error, "8000011", new Object[]{"attribute-key-20"}, sku, "attribute-key-20");
                        }
                        model.setAttributeKey20(item);
                        model.setAttributeValue20(reader.get(i++));
//                    model.setMd5(getMd5(model));
                        model.setCreater(getTaskName());
                        model.setModifier(getTaskName());
                        model.setUpdateFlg("0");
                        if (error.toString().length() == 0) {
                            feedInfoTempModels.add(model);
                        }
                        rowNum++;
                        if (error.toString().length() == 0 && feedInfoTempModels.size() > 1000) {
                            vmsBtFeedInfoTempDaoExt.insertList(feedInfoTempModels);
                            feedInfoTempModels.clear();
                        }
                    }
                    if (error.toString().length() == 0 && feedInfoTempModels.size() > 0) {
                        vmsBtFeedInfoTempDaoExt.insertList(feedInfoTempModels);
                        feedInfoTempModels.clear();
                    }
                }

                reader.close();
                // 如果check有错误，那么生成错误文件，并且把文件管理的状态变为3：导入错误
                if (error.toString().length() > 0) {
                    $info("导入Temp表时出现错误,channel：" + channel.getFull_name());
                    // 生成错误文件
                    String feedErrorFileName = createErrorFile(error);

                    // 移动文件到bak目录下
                    moveFeedFileToBak(feedFile.getName());

                    // 把文件管理的状态变为4：导入错误
                    VmsBtFeedFileModel feedFileModel = new VmsBtFeedFileModel();
                    // 更新条件
                    feedFileModel.setChannelId(channel.getOrder_channel_id());
                    feedFileModel.setFileName(feedFile.getName());
                    // 更新内容
                    feedFileModel.setErrorFileName(feedErrorFileName);
                    feedFileModel.setStatus(VmsConstants.FeedFileStatus.IMPORT_ERROR);
                    feedFileModel.setModifier(getTaskName());
                    vmsBtFeedFileDaoExt.updateErrorInfo(feedFileModel);

                    return true;
                }
                $info("导入Temp表成功,导入件数：" + rowNum + "件,channel：" + channel.getFull_name());

                return false;
            } catch (IOException ex) {
                $error(ex.getMessage());
                throw ex;
            }
        }

        /**
         * 删除临时表vms_bt_feed_info_temp里的数据
         *
         * @param channelId 渠道id
         */
        private void deleteFeedInfoTemp(String channelId) {
            int delCnt = 0;
            long all = vmsBtFeedInfoTempDaoExt.selectListCount(channelId);
            // 算出每一次删除5万条数据，那么一共要删除几次
            int loopCnt = new Long(all).intValue() / LIMIT_COUNT;
            if (new Long(all).intValue() % LIMIT_COUNT > 0) {
                loopCnt++;
            }

            $info("删除临时表数据开始,channel：" + channel.getFull_name());

            for (int i = 0; i < loopCnt; i++) {
                // 先删除这个channel下的临时数据
                delCnt += vmsBtFeedInfoTempDaoExt.deleteByChannelWithLimit(channelId);
                $info("已经删除数据：" + delCnt + "件;channel：" + channel.getFull_name());
            }

            $info("删除临时表数据结束,一共删除数据：" + delCnt + "件;channel：" + channel.getFull_name());
        }

        /**
         * 追加ErrorMessage
         *
         * @param errorList   所有Error内容
         * @param messageCode 错误Code
         * @param args        参数
         * @param sku         SKU
         * @param column      列号
         * @return FeedInfoModel数据（列表）
         */
        private void addErrorMessage(List<Map<String, Object>> errorList, String messageCode, Object[] args, String sku, String column) throws IOException {

            Map<String, Object> errorMap = new HashMap<>();

            // 取得具体的ErrorMessage内容
            MessageBean messageBean = messageService.getMessage("en", messageCode);
            if (messageBean != null) {
                String message = String.format(messageBean.getMessage(), args);
                errorMap.put("sku", sku);
                errorMap.put("column", column);
                errorMap.put("message", message);
            } else {
                // 一般情况下不可能，除非ct_message_info表中没有加入这个message
                errorMap.put("sku", sku);
                errorMap.put("column", column);
                errorMap.put("message", messageCode);
            }

            errorList.add(errorMap);

            // 如果errorList超过了一定的数量那么先输出一部分
            if (errorList.size() == 10000) {
                createErrorFile(errorList, 0);
                errorList.clear();
            }


        }

        /**
         * 追加ErrorMessage
         *
         * @param error       所有Error内容
         * @param messageCode 错误Code
         * @param args        参数
         * @param sku         SKU
         * @param column      列名
         * @return FeedInfoModel数据（列表）
         */
        private void addErrorMessage(StringBuilder error, String messageCode, Object[] args, String sku, String column) {
            // 取得具体的ErrorMessage内容
            MessageBean messageBean = messageService.getMessage("en", messageCode);
            if (messageBean != null) {
                String message = String.format(messageBean.getMessage(), args);
                error.append(sku + VmsConstants.COMMA + column + VmsConstants.COMMA + message + "\r\n");
            } else {
                // 一般情况下不可能，除非ct_message_info表中没有加入这个message
                error.append(sku + VmsConstants.COMMA + column + VmsConstants.COMMA + messageCode + "\r\n");
            }
        }

        /**
         * 生成Feed错误结果文件
         *
         * @param stringBuilder 错误内容
         *                      <p>
         *                      return Feed错误结果文件名
         */
        private String createErrorFile(StringBuilder stringBuilder) throws IOException {
            // 取得Feed文件检查结果路径
            String feedErrorFilePath = createErrorFilePath();
            // Feed文件检查结果文件名
            String feedErrorFileName = "Feed_Check_Result_" + channel.getOrder_channel_id() + DateTimeUtil.getNow("_yyyyMMdd_HHmmss") + ".csv";
            try (OutputStream outputStream = new FileOutputStream(feedErrorFilePath + feedErrorFileName)) {
                // 先加一个头
                byte[] headerInBytes = "sku,column,message\r\n".getBytes();
                outputStream.write(headerInBytes);
                // 再写入内容
                byte[] contentInBytes = stringBuilder.toString().getBytes();
                outputStream.write(contentInBytes);
                outputStream.flush();
                outputStream.close();
            }
            return feedErrorFileName;
        }

        /**
         * 生成Feed错误结果文件
         *
         * @param errorList  错误内容列表
         * @param successCnt 成功导入件数
         */
        private void createErrorFile(List<Map<String, Object>> errorList, int successCnt) throws IOException {
            // 取得Feed文件检查结果路径
            String feedErrorFilePath = createErrorFilePath();
            // 是否写入头
            boolean writeHeader = false;
            // Feed文件检查结果文件名
            if (StringUtils.isEmpty(errorFileName)) {
                String feedErrorFileName = "Feed_Check_Result_" + channel.getOrder_channel_id() + DateTimeUtil.getNow("_yyyyMMdd_HHmmss") + ".csv";
                errorFileName = feedErrorFileName;
                writeHeader = true;
            }
            StringBuilder stringBuilder = new StringBuilder();
            // 以append模式生成错误结果文件
            try (OutputStream outputStream = new FileOutputStream(feedErrorFilePath + errorFileName, true)) {
                // 按rowNum排序
                // Collections.sort(errorList, new MapComparator());
                for (Map<String, Object> error : errorList) {
                    stringBuilder.append(String.valueOf(error.get("sku")) + VmsConstants.COMMA
                            + String.valueOf(error.get("column")) + VmsConstants.COMMA
                            + String.valueOf(error.get("message")) + "\r\n");
                }
                if (writeHeader) {
                    String header = "sku,column,message\r\n";
                    byte[] headerInBytes = header.getBytes();
                    outputStream.write(headerInBytes);
                }
                // 再写入内容
                if (!StringUtils.isEmpty(stringBuilder.toString())) {
                    byte[] contentInBytes = stringBuilder.toString().getBytes();
                    outputStream.write(contentInBytes);
                }
                // 加一个尾
//                if (successCnt > 0) {
//                    String footer = "\"The above data have errors, other data have been imported successfully.\"\r\n";
//                    outputStream.write(footer.getBytes());
//                }
                outputStream.flush();
                outputStream.close();
            }
        }

        /**
         * 生成Feed错误结果文件路径
         * <p>
         * return Feed错误结果文件路径
         */
        private String createErrorFilePath() {
            $info("生成Feed检索结果Error文件,channel：" + channel.getFull_name());
            // 取得Feed文件检查结果路径
            String feedErrorFilePath = Codes.getCodeName(VmsConstants.VMS_PROPERTY, "vms.feed.check");
            feedErrorFilePath += "/" + channel.getOrder_channel_id() + "/";
            // 创建文件目录
            FileUtils.mkdirPath(feedErrorFilePath);
            return feedErrorFilePath;
        }

        /**
         * 移动Feed文件到bak目录下
         *
         * @param feedFileName FeedFile文件名
         */
        private void moveFeedFileToBak(String feedFileName) {
            // 取得Feed文件上传路径
            String feedFilePath = "";
            // online上传的场合
            if (VmsConstants.FeedFileUploadType.ONLINE.equals(uploadType)) {
                feedFilePath = Codes.getCodeName(VmsConstants.VMS_PROPERTY, "vms.feed.online.upload");
                feedFilePath += "/" + channel.getOrder_channel_id() + "/";
            } else {
                // ftp上传的场合
                feedFilePath = Codes.getCodeName(VmsConstants.VMS_PROPERTY, "vms.feed.ftp.upload");
                feedFilePath += "/" + channel.getOrder_channel_id() + "/feed/";
            }
            // 创建文件目录
            FileUtils.mkdirPath(feedFilePath + "bak/");
            FileUtils.moveFile(feedFilePath + feedFileName, feedFilePath + "bak/" + feedFileName);
        }

        /**
         * 设置Feed的Weight属性
         *
         * @param skuInfo  skuInfoModel
         * @param feedInfo FeedInfoModel
         * @param weight   重量
         */
        private void setFeedWeight(CmsBtFeedInfoModel_Sku skuInfo, CmsBtFeedInfoModel feedInfo, String weight) {

            String weightOrg = "";
            String weightOrgUnit = "lbs";
            String weightCalc = "";

            // 解析出原始的重量和重量单位
            for (String weightUnit : weightUnits) {
                if (weight.toLowerCase().lastIndexOf(weightUnit) > 0) {
                    weightOrg = weight.substring(0, weight.toLowerCase().lastIndexOf(weightUnit));
                    weightOrg = weightOrg.trim();
                    weightOrgUnit = weight.substring(weight.toLowerCase().lastIndexOf(weightUnit), weight.length());
                    break;
                }
            }

            if (StringUtils.isEmpty(weightOrg)) {
                weightOrg = weight;
            }

            // 按lbs的单位进行转换
            if ("oz".equals(weightOrgUnit.toLowerCase())) {
                weightCalc = round(Double.parseDouble(weightOrg) / 16.0).toString();
            } else if ("g".equals(weightOrgUnit.toLowerCase())) {
                weightCalc = round(Double.parseDouble(weightOrg) / 453.59237).toString();
            } else if ("kg".equals(weightOrgUnit.toLowerCase())) {
                weightCalc = round(Double.parseDouble(weightOrg) / 0.4535924).toString();
            } else {
                weightCalc = round(Double.parseDouble(weightOrg)).toString();
            }

            skuInfo.setWeightOrg(weightOrg);
            skuInfo.setWeightOrgUnit(weightOrgUnit);
            skuInfo.setWeightCalc(weightCalc);

            if (StringUtils.isEmpty(feedInfo.getWeight())) {
                feedInfo.setWeight(weightCalc);
            }
        }

        private Double round(Double value){
            BigDecimal b = new BigDecimal(value);
            return b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        /**
         * calculatePriceByFormula 根据公式计算价格
         *
         * @param feedSkuInfo CmsBtFeedInfoModel_Sku Feed的SKU信息
         * @param formula     String   计算公式
         * @return 计算后价格
         */
        private Double calculatePriceByFormula(CmsBtFeedInfoModel_Sku feedSkuInfo, String formula) {

            Double priceClientMsrp = feedSkuInfo.getPriceClientMsrp();
            Double priceClientRetail = feedSkuInfo.getPriceClientRetail();
            Double priceNet = feedSkuInfo.getPriceNet();
            String weight = feedSkuInfo.getWeightCalc();

            if (StringUtils.isEmpty(formula)) {
                return 0.0;
            }

            // 根据公式计算价格
            try {
                // 向上取整
                weight = String.valueOf(Math.ceil(new Double(weight)));
                // 价格说明：
                // price_client_msrp:美金专柜价
                // price_client_retail:美金指导价
                // price_net:美金成本价
                // weight:磅单位的重量
                ExpressionParser parser = new SpelExpressionParser();
                formula = formula.replaceAll("price_client_msrp", String.valueOf(priceClientMsrp))
                        .replaceAll("price_client_retail", String.valueOf(priceClientRetail))
                        .replaceAll("price_net", String.valueOf(priceNet))
                        .replaceAll("weight", String.valueOf(weight));
                double valueDouble = parser.parseExpression(formula).getValue(Double.class);
                return new BigDecimal(String.valueOf(valueDouble)).setScale(0, BigDecimal.ROUND_UP).doubleValue();
            } catch (Exception ex) {
                $error("计算价格发生错误:" + ex.getMessage());
                return 0.0;
            }
        }
    }

    public class MapComparator implements Comparator<Map<String, Object>> {
        @Override
        public int compare(Map<String, Object> map1, Map<String, Object> map2) {
            Integer rowNum1 = (Integer) map1.get("row");
            Integer rowNum2 = (Integer) map2.get("row");
            return rowNum1.compareTo(rowNum2);
        }
    }
}