package com.voyageone.task2.vms.service;

import com.csvreader.CsvReader;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.*;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.VmsChannelConfigBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.MD5;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.com.MessageBean;
import com.voyageone.service.dao.vms.VmsBtFeedFileDao;
import com.voyageone.service.dao.vms.VmsBtFeedInfoTempDao;
import com.voyageone.service.daoext.vms.VmsBtFeedFileDaoExt;
import com.voyageone.service.daoext.vms.VmsBtFeedInfoTempDaoExt;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.service.model.vms.VmsBtFeedFileModel;
import com.voyageone.service.model.vms.VmsBtFeedInfoTempModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.vms.VmsConstants;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * 将Feed信息导入FeedInfo表
 *
 * @author jeff.duan
 */
@Service
public class VmsFeedFileImportService extends BaseTaskService {

    public static final int SKU_INDEX = 0;
    public static final int PARENT_ID = 1;
    public static final int RELATIONSHIP_TYPE = 2;
    public static final int VARIATION_THEME = 3;
    public static final int TITLE = 4;
    public static final int PRODUCT_ID = 5;
    public static final int PRICE = 6;
    public static final int MSRP = 7;
    public static final int QUANTITY = 8;
    public static final int IMAGES = 9;
    public static final int DESCRIPTION = 10;
    public static final int SHORT_DESCRIPTION = 11;
    public static final int PRODUCT_ORIGIN = 12;
    public static final int CATEGORY = 13;
    public static final int WEIGHT = 14;
    public static final int BRAND = 15;
    public static final int MATERIALS = 16;
    public static final int VENDOR_PRODUCT_URL = 17;
    /* attributeColumnStartIndex */
    public static final int ATTRIBUTE_COLUMN_START_INDEX = 18;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final NumberFormat numberFormatter = new DecimalFormat("#");

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
        put(IMAGES, "images");
        put(DESCRIPTION, "description");
        put(SHORT_DESCRIPTION, "short-description");
        put(PRODUCT_ORIGIN, "product-origin");
        put(CATEGORY, "category");
        put(WEIGHT, "weight");
        put(BRAND, "brand");
        put(MATERIALS, "materials");
        put(VENDOR_PRODUCT_URL, "vendor-product-url");
    }};



    @Autowired
    private VmsBtFeedFileDao vmsBtFeedFileDao;

    @Autowired
    private VmsBtFeedFileDaoExt vmsBtFeedFileDaoExt;

    @Autowired
    private MessageService messageService;

    @Autowired
    private VmsBtFeedInfoTempDao vmsBtFeedInfoTempDao;

    @Autowired
    private VmsBtFeedInfoTempDaoExt vmsBtFeedInfoTempDaoExt;

    @Autowired
    protected TransactionRunner transactionRunner;



    @Override
    public SubSystem getSubSystem() {
        return SubSystem.VMS;
    }

    @Override
    public String getTaskName() {
        return "VmsFeedFileImportJob";
    }

    /**
     * f将Feed信息导入FeedInfo表
     *
     * @param taskControlList job 配置
     * @throws Exception
     */
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 允许运行的渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        if (orderChannelIdList != null && orderChannelIdList.size() > 0) {

            // 检测Feed文件是否在vms_bt_feed_file表中存在，如果不存在那么新建一条文件管理信息
            checkFeedFileDBInfo(orderChannelIdList);

            // 线程
            List<Runnable> threads = new ArrayList<>();

            // 根据渠道运行
            for (final String orderChannelID : orderChannelIdList) {
                threads.add(new Runnable() {
                    @Override
                    public void run() {
                        // 主逻辑
                        new ImportFeedFile(orderChannelID).doRun();
                    }
                });
            }

            runWithThreadPool(threads, taskControlList, orderChannelIdList.size());
        }
    }


    /**
     * 按渠道进行导入
     */
    public class ImportFeedFile {
        private OrderChannelBean channel;

        public ImportFeedFile(String orderChannelId) {
            this.channel = Channels.getChannel(orderChannelId);
        }

        public void doRun() {
            $info(channel.getFull_name() + "产品 Feed文件导入开始");

            // 查找当前渠道,取得建立时间最早的Feed导入文件
            Map<String, Object> param = new HashMap<>();
            param.put("channelId", channel.getOrder_channel_id());
            // 状态：1（等待导入）
            param.put("status", VmsConstants.FeedFileStatus.WAITING_IMPORT);
            List<VmsBtFeedFileModel> feedFileList = vmsBtFeedFileDaoExt.selectListOrderByCreateTime(param);
            VmsBtFeedFileModel model = null;
            if (feedFileList.size() > 0) {
                model = feedFileList.get(feedFileList.size() - 1);
            }
            // 存在需要导入的Feed文件
            if (model != null) {
                File feedFile = new File(model.getFileName());
                // error内容
                StringBuilder stringBuilder = new StringBuilder();
                // 文件存在的话那么处理
                if (feedFile.exists()) {
                    $info("Feed文件check的处理开始 文件路径：" + model.getFileName() + ",channel：" + channel.getFull_name());
                    // Map<String, CmsBtFeedInfoModel> feedInfoMap = readExcel(feedFile, stringBuilder);
                    // 把Feed数据插入vms_bt_feed_info_temp表
                    readCsvToDB(feedFile);
                    // check数据并且插入MongoDb表
                    doHandle();
                    $info("Feed文件check的处理结束,channel：" + channel.getFull_name());
                }
//                    // check有错的情况下，先生成ErrorFile
//                    if (stringBuilder.toString().length() >  0) {
//                        try {
//                            createErrorFile(stringBuilder);
//                        } catch (Exception e) {
//                            $error(e.getMessage());
//                        }
//                    } else {
//                        // 没有错误的情况下，导入Feed文件到数据库
//                        if (feedInfoMap.size() > 0) {
//                            $info("导入数据库开始");
//                            //                saveFeedData(feedInfoMap);
//                            $info(String.format("导入数据库结束,更新了%d件", feedInfoMap.size()));
//                        } else {
//                            $info("没有更新对象");
//                        }
//                    }
//                } else {
//                    // 一般情况下不可能发生，除非手动删除文件
//                    $error("Feed文件不存在 文件路径：" + model.getFileName() + ",channel：" + channel.getFull_name());
//                }
            }
            $info(channel.getFull_name() + "产品 Feed文件导入结束");
        }

        /**
         * check数据并且插入MongoDb表
         *
         */
        private void doHandle() {

            // Error错误
            List<Map<String, Object>> errorList = new ArrayList<>();
            int i=1;
            // 取得需要处理的Code级别的数据,每次取得固定件数
            while (true) {
                Map<String, Object> param = new HashMap<>();
                param.put("channelId", channel.getOrder_channel_id());
                param.put("parentId", "");
                param.put("updateFlg", "0");
                List<VmsBtFeedInfoTempModel> codeList = vmsBtFeedInfoTempDaoExt.selectList(param);
                // 直到全部拿完那么终止
                if (codeList.size() == 0) {
                    break;
                }
                $info("++***************" + String.valueOf(i*100));
                for (VmsBtFeedInfoTempModel codeModel : codeList) {
                    if (!StringUtils.isEmpty(codeModel.getSku())) {
                        Map<String, Object> param1 = new HashMap<>();
                        param1.put("channelId", channel.getOrder_channel_id());
                        param1.put("parentId", codeModel.getSku());
                        param1.put("updateFlg", "0");
                        List<VmsBtFeedInfoTempModel> skuModels = vmsBtFeedInfoTempDaoExt.selectList(param1);
                        checkByCodeGroup(codeModel, skuModels, errorList);
                    }
                }
                i++;
            }

        }

        /**
         * check数据，并保存FeedInfo对象列表
         *
         * @param codeModel Code级别数据
         * @param skuModels Sku级别数据
         * @param errorList 所有Error内容
         *
         */
        private void checkByCodeGroup(VmsBtFeedInfoTempModel codeModel, List<VmsBtFeedInfoTempModel> skuModels, List<Map<String, Object>> errorList) {

            // CodeModel是否同时也是Sku
            boolean isSkuLevel = false;
            // 这组是否有错，没有错的话先查到MongoDB里
            boolean errorFlg = false;
            // 如果没有其他指向他的数据，那么这个Code同时又是Sku
            if (skuModels == null || skuModels.size() == 0) {
                isSkuLevel = true;
            }

            // 先Check Code应该有的那些内容
            // Code（sku之前已经check过了）
            String sku = codeModel.getSku();

            // title
            String title = codeModel.getTitle();
            // 如果这行是Code,那么title是必须的
            if (StringUtils.isEmpty(title)) {
                // title is Required.
                addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(TITLE)}, codeModel.getRow(), columnMap.get(TITLE));
                errorFlg = true;
            }

            // images
            String images = codeModel.getImages();
            // 如果这行是Code,那么images是必须的
            if (StringUtils.isEmpty(images)) {
                // images is Required.
                addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(IMAGES)}, codeModel.getRow(), columnMap.get(IMAGES));
                errorFlg = true;
            }

            // description
            String description = codeModel.getDescription();
            // 如果这行是Code,那么description是必须的
            if (StringUtils.isEmpty(description)) {
                // description is Required.
                addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(DESCRIPTION)}, codeModel.getRow(), columnMap.get(DESCRIPTION));
                errorFlg = true;
            }

            // short-description
            String shortDescription = codeModel.getShortDescription();
            // 如果这行是Code,那么short-description是必须的
            if (StringUtils.isEmpty(shortDescription)) {
                // short-description is Required.
                addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(SHORT_DESCRIPTION)}, codeModel.getRow(), columnMap.get(SHORT_DESCRIPTION));
                errorFlg = true;
            }

            // product-origin
            String productOrigin = codeModel.getProductOrigin();
            // 如果这行是Code,那么product-origin是必须的
            if (StringUtils.isEmpty(productOrigin)) {
                // product-origin is Required.
                addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(PRODUCT_ORIGIN)}, codeModel.getRow(), columnMap.get(PRODUCT_ORIGIN));
                errorFlg = true;
            }

            // category
            String category = codeModel.getCategory();
            // TODO 处理Category的空格
            String[] categoryArray = category.split("-");
            category = "";
            for (String categoryItem : categoryArray) {
                // 不等于空的情况下，去掉首尾空格重新组装一下
                if (!StringUtils.isEmpty(categoryItem)) {
                    category += categoryItem.trim() + "-";
                }
            }
            // 去掉最后一个分隔符[-]
            if (!StringUtils.isEmpty(category)) {
                category.substring(category.length() - 1);
            }
            // 如果这行是Code,那么category是必须的
            if (StringUtils.isEmpty(category)) {
                // category is Required.
                addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(CATEGORY)}, codeModel.getRow(), columnMap.get(CATEGORY));
                errorFlg = true;
            }

            // weight
            String weight = codeModel.getWeight();
            // 如果这行是Code,那么weight是必须的
            if (StringUtils.isEmpty(weight)) {
                // weight is Required.
                addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(WEIGHT)}, codeModel.getRow(), columnMap.get(WEIGHT));
                errorFlg = true;
            }

            // brand
            String brand = codeModel.getBrand();
            // 如果这行是Code,那么brand是必须的
            if (StringUtils.isEmpty(brand)) {
                // brand is Required.
                addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(BRAND)}, codeModel.getRow(), columnMap.get(BRAND));
                errorFlg = true;
            }

            // Materials
            String materials = codeModel.getMaterials();
            // 如果这行是Code,那么materials是必须的
            if (StringUtils.isEmpty(materials)) {
                // materials is Required.
                addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(MATERIALS)}, codeModel.getRow(), columnMap.get(MATERIALS));
                errorFlg = true;
            }

            // Vendor-Product-Url
            String vendorProductUrl = codeModel.getVendorProductUrl();
            // AttributeKey
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

            // 如果这行Code是Sku的话还需要CheckSku
            if (isSkuLevel) {
//            checkSkuCommon(codeModel);
            }


            // check没有错误的情况下，生成CmsBtFeedInfoModel
            if (!errorFlg) {
                CmsBtFeedInfoModel feedInfo = new CmsBtFeedInfoModel();
                feedInfo.setCode(sku);
                feedInfo.setModel(sku);
                feedInfo.setCategory(category);
                feedInfo.setCatId(MD5.getMD5(category));
                feedInfo.setName(title);
                feedInfo.setImage(Arrays.asList(images.split(",")));
                feedInfo.setLongDescription(description);
                feedInfo.setShortDescription(shortDescription);
                feedInfo.setOrigin(productOrigin);
                feedInfo.setWeight(weight);
                feedInfo.setBrand(brand);
                feedInfo.setMaterial(materials);
                feedInfo.setClientProductURL(vendorProductUrl);
                // feedInfo.setAttribute(attributeMap);

                // 加入Sku
                if (skuModels != null && skuModels.size() > 0) {
                    List<CmsBtFeedInfoModel_Sku> skusModel = new ArrayList<>();
                    feedInfo.setSkus(skusModel);
                    for (VmsBtFeedInfoTempModel skuTemp : skuModels) {
                        // 创建Sku
                        CmsBtFeedInfoModel_Sku skuModel = new CmsBtFeedInfoModel_Sku();
                        skuModel.setBarcode(skuTemp.getProductId());
                        skuModel.setClientSku(skuTemp.getSku());
                        // skuModel.setSku(feedInfo.getCode() + skuKey);
                        // skuModel.setSize(skuKey);
                        skuModel.setImage(Arrays.asList(images.split(",")));
                        skuModel.setQty(new Integer(skuTemp.getQuantity()));
                        if (!StringUtils.isEmpty(skuTemp.getMsrp())) {
//                            skuModel.setPriceClientMsrp(new BigDecimal(skuTemp.getMsrp()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        } else {
//                            skuModel.setPriceClientMsrp(new BigDecimal(skuTemp.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        }

//                        skuModel.setPriceClientRetail(new BigDecimal(skuTemp.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//                        skuModel.setPriceCurrent(new BigDecimal(skuTemp.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

                        skusModel.add(skuModel);
                    }
                }
            }
        }

        /**
         * check数据，并保存FeedInfo对象列表
         *
         * @param codeModel Code级别数据
         * @param errorList 所有Error内容
         *
         */
        private void checkSkuCommon(VmsBtFeedInfoTempModel codeModel, List<Map<String, Object>> errorList) {

            // product-id
            String productId = codeModel.getProductId();
            // product-id是必须的
            if ( StringUtils.isEmpty(productId)) {
                // product-id is Required.
                addErrorMessage(errorList, "8000002", new Object[]{columnMap.get(PRODUCT_ID)}, codeModel.getRow(), columnMap.get(PRODUCT_ID));
            }

            // price
            String price = codeModel.getPrice();
            // price是必须的,并且是大于0的数字
            if (StringUtils.isEmpty(price) || !StringUtils.isNumeric(price) || Float.valueOf(price) <= 0) {
                // price must be a Number more than 0.
                addErrorMessage(errorList, "8000005", new Object[]{columnMap.get(PRICE)}, codeModel.getRow(), columnMap.get(PRICE));
            }

            // msrp
            String msrp = codeModel.getMsrp();
            if (!StringUtils.isEmpty(msrp)) {
                // msrp如果输入，必须是大于0的数字
                if (!StringUtils.isNumeric(msrp) || Float.valueOf(msrp) <= 0) {
                    // msrp must be a Number more than 0.
                    addErrorMessage(errorList, "8000005", new Object[]{columnMap.get(MSRP)}, codeModel.getRow(), columnMap.get(MSRP));
                }
            }

            // quantity
            String quantity = codeModel.getQuantity();
            // quantity是必须的,并且是大于0的数字
            if (StringUtils.isEmpty(quantity) || !StringUtils.isDigit(quantity)) {
                // quantity must be a positive Integer.
                addErrorMessage(errorList, "8000006", new Object[]{columnMap.get(QUANTITY)}, codeModel.getRow(), columnMap.get(QUANTITY));
            }
        }


        /**
         * 读入Feed文件，插入到临时表vms_bt_feed_info_temp里
         *
         * @param feedFile  导入Feed文件
         * @return 是否有错误
         */
        private boolean readCsvToDB(File feedFile) {

            try {
                $info("删除临时表数据开始,channel：" + channel.getFull_name());

                // 先删除这个channel下的临时数据
                int delCnt = vmsBtFeedInfoTempDaoExt.deleteByChannel(channel.getOrder_channel_id());

                $info("删除临时表数据结束,一共删除数据：" + delCnt + "件;channel：" + channel.getFull_name());

                // 转成vms_bt_feed_info_temp的Model列表
                List<VmsBtFeedInfoTempModel> feedInfoTempModels = new ArrayList<>();

                // Csv默认分割符号(逗号)
                char csvSplitSymbol = VmsConstants.COMMA;
                // Csv默认编码(utf-8)
                String csvEncode = VmsConstants.UTF_8;

                // 取得CSV分隔符
                VmsChannelConfigBean vmsChannelConfigBean = VmsChannelConfigs.getConfigBean(channel.getOrder_channel_id()
                        , VmsConstants.ChannelConfig.CSV_SPLIT_SYMBOL
                        ,VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);
                if (vmsChannelConfigBean != null) {
                    csvSplitSymbol = vmsChannelConfigBean.getConfigValue1().charAt(0);
                }

                // 取得CSV分隔符
                vmsChannelConfigBean = VmsChannelConfigs.getConfigBean(channel.getOrder_channel_id()
                        , VmsConstants.ChannelConfig.CSV_ENCODE
                        ,VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);
                if (vmsChannelConfigBean != null) {
                    csvEncode = vmsChannelConfigBean.getConfigValue1();
                }

                // Sku列表
                Map<String, String> skuMap = new HashMap<>();
                // Error信息
                StringBuilder error = new StringBuilder();

                CsvReader reader = new CsvReader(new FileInputStream(feedFile), csvSplitSymbol, Charset.forName(csvEncode));
                // Head读入
                reader.readHeaders();
                reader.getHeaders();
                int rowNum = 2;

                // Body读入
                while (reader.readRecord()) {
                    VmsBtFeedInfoTempModel model = new VmsBtFeedInfoTempModel();

                    int i = 0;
                    model.setChannelId(channel.getOrder_channel_id());
                    model.setRow(rowNum);
                    // sku
                    String item = reader.get(i++);
                    // sku必须
                    if (StringUtils.isEmpty(item)) {
                        addErrorMessage(error, "8000002", new Object[]{columnMap.get(SKU_INDEX)}, rowNum, columnMap.get(SKU_INDEX));
                    }
                    // sku长度验证
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{columnMap.get(SKU_INDEX)}, rowNum, columnMap.get(SKU_INDEX));
                    }
                    // sku唯一验证
                    if (!StringUtils.isEmpty(item)) {
                        if (skuMap.get(item) != null) {
                            addErrorMessage(error, "8000003", new Object[]{item}, rowNum, columnMap.get(SKU_INDEX));
                        } else {
                            skuMap.put(item, item);
                        }
                    }
                    model.setSku(item);

                    // parent-id
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{columnMap.get(PARENT_ID)}, rowNum, columnMap.get(PARENT_ID));
                    }
                    model.setParentId(item);

                    // relationship-type
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{columnMap.get(RELATIONSHIP_TYPE)}, rowNum, columnMap.get(RELATIONSHIP_TYPE));
                    }
                    model.setRelationshipType(item);

                    // variation-theme
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{columnMap.get(VARIATION_THEME)}, rowNum, columnMap.get(VARIATION_THEME));
                    }
                    model.setVariationTheme(item);
                    model.setTitle(reader.get(i++));

                    // product-id
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{columnMap.get(PRODUCT_ID)}, rowNum, columnMap.get(PRODUCT_ID));
                    }
                    model.setProductId(item);

                    // price
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{columnMap.get(PRICE)}, rowNum, columnMap.get(PRICE));
                    }
                    model.setPrice(item);

                    // msrp
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{columnMap.get(MSRP)}, rowNum, columnMap.get(MSRP));
                    }
                    model.setMsrp(item);

                    // quantity
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{columnMap.get(QUANTITY)}, rowNum, columnMap.get(QUANTITY));
                    }
                    model.setQuantity(item);
                    model.setImages(reader.get(i++));
                    model.setDescription(reader.get(i++));
                    model.setShortDescription(reader.get(i++));

                    // product-origin
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{columnMap.get(PRODUCT_ORIGIN)}, rowNum, columnMap.get(PRODUCT_ORIGIN));
                    }
                    model.setProductOrigin(item);

                    // category
                    item = reader.get(i++);
                    if (item.getBytes().length > 500) {
                        addErrorMessage(error, "8000011", new Object[]{columnMap.get(CATEGORY)}, rowNum, columnMap.get(CATEGORY));
                    }
                    model.setCategory(item);

                    // weight
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{columnMap.get(WEIGHT)}, rowNum, columnMap.get(WEIGHT));
                    }
                    model.setWeight(item);

                    // brand
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{columnMap.get(BRAND)}, rowNum, columnMap.get(BRAND));
                    }
                    model.setBrand(item);

                    // materials
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{columnMap.get(MATERIALS)}, rowNum, columnMap.get(MATERIALS));
                    }
                    model.setMaterials(item);
                    model.setVendorProductUrl(reader.get(i++));

                    // attribute-key-1
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-1"}, rowNum, "attribute-key-1");
                    }
                    model.setAttributeKey1(item);
                    model.setAttributeValue1(reader.get(i++));
                    // attribute-key-2
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-2"}, rowNum, "attribute-key-2");
                    }
                    model.setAttributeKey2(item);
                    model.setAttributeValue2(reader.get(i++));
                    // attribute-key-3s
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-3"}, rowNum, "attribute-key-3");
                    }
                    model.setAttributeKey3(item);
                    model.setAttributeValue3(reader.get(i++));
                    // attribute-key-4
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-4"}, rowNum, "attribute-key-4");
                    }
                    model.setAttributeKey4(item);
                    model.setAttributeValue4(reader.get(i++));
                    // attribute-key-5
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-5"}, rowNum, "attribute-key-5");
                    }
                    model.setAttributeKey5(item);
                    model.setAttributeValue5(reader.get(i++));
                    // attribute-key-6
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-6"}, rowNum, "attribute-key-6");
                    }
                    model.setAttributeKey6(item);
                    model.setAttributeValue6(reader.get(i++));
                    // attribute-key-7
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-7"}, rowNum, "attribute-key-7");
                    }
                    model.setAttributeKey7(item);
                    model.setAttributeValue7(reader.get(i++));
                    // attribute-key-8
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-8"}, rowNum, "attribute-key-8");
                    }
                    model.setAttributeKey8(item);
                    model.setAttributeValue8(reader.get(i++));
                    // attribute-key-9
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-9"}, rowNum, "attribute-key-9");
                    }
                    model.setAttributeKey9(item);
                    model.setAttributeValue9(reader.get(i++));
                    // attribute-key-10
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-10"}, rowNum, "attribute-key-10");
                    }
                    model.setAttributeKey10(item);
                    model.setAttributeValue10(reader.get(i++));
                    // attribute-key-11
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-11"}, rowNum, "attribute-key-11");
                    }
                    model.setAttributeKey11(item);
                    model.setAttributeValue11(reader.get(i++));
                    // attribute-key-12
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-12"}, rowNum, "attribute-key-12");
                    }
                    model.setAttributeKey12(item);
                    model.setAttributeValue12(reader.get(i++));
                    // attribute-key-13
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-13"}, rowNum, "attribute-key-13");
                    }
                    model.setAttributeKey13(item);
                    model.setAttributeValue13(reader.get(i++));
                    // attribute-key-14
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-14"}, rowNum, "attribute-key-14");
                    }
                    model.setAttributeKey14(item);
                    model.setAttributeValue14(reader.get(i++));
                    // attribute-key-15
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-15"}, rowNum, "attribute-key-15");
                    }
                    model.setAttributeKey15(item);
                    model.setAttributeValue15(reader.get(i++));
                    // attribute-key-16
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-16"}, rowNum, "attribute-key-16");
                    }
                    model.setAttributeKey16(item);
                    model.setAttributeValue16(reader.get(i++));
                    // attribute-key-17
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-17"}, rowNum, "attribute-key-17");
                    }
                    model.setAttributeKey17(item);
                    model.setAttributeValue17(reader.get(i++));
                    // attribute-key-18
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-18"}, rowNum, "attribute-key-18");
                    }
                    model.setAttributeKey18(item);
                    model.setAttributeValue18(reader.get(i++));
                    // attribute-key-19
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-19"}, rowNum, "attribute-key-19");
                    }
                    model.setAttributeKey19(item);
                    model.setAttributeValue19(reader.get(i++));
                    // attribute-key-20
                    item = reader.get(i++);
                    if (item.getBytes().length > 128) {
                        addErrorMessage(error, "8000011", new Object[]{"attribute-key-20"}, rowNum, "attribute-key-20");
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

                reader.close();
                // 如果check有错误，那么生成错误文件，并且把文件管理的状态变为3：导入错误
                if (error.toString().length() > 0) {
                    // 生成错误文件
                    String feedErrorFilePath = createErrorFile(error);
                    // 把文件管理的状态变为3：导入错误
                    VmsBtFeedFileModel feedFileModel = new VmsBtFeedFileModel();
                    // 更新条件
                    feedFileModel.setChannelId(channel.getOrder_channel_id());
                    feedFileModel.setFileName(feedFile.getPath());
                    // 更新内容
                    feedFileModel.setErrorFilePath(feedErrorFilePath);
                    feedFileModel.setStatus(VmsConstants.FeedFileStatus.IMPORT_WITH_ERROR);
                    vmsBtFeedFileDaoExt.updateErrorFileInfo(feedFileModel);
                    return true;
                }

                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return true;
            }
        }

//        public boolean insertFeedInfo(List<VmsBtFeedInfoTempModel> feedInfoTempModels) {
//            boolean isSuccess = true;
//
//            for (VmsBtFeedInfoTempModel feedInfoTemp : feedInfoTempModels) {
//
//                if (superfeeddao.insertSuperfeedJEInfo(superfeed) <= 0) {
//                    $info("产品信息插入失败 InventoryNumber = " + superfeed.getInventoryNumber());
//                }
//            }
//            return isSuccess;
//        }

        private String getMd5(VmsBtFeedInfoTempModel model) {
            StringBuffer temp = new StringBuffer();
            temp.append(model.getSku());
            temp.append(model.getParentId());
            temp.append(model.getRelationshipType());
            temp.append(model.getVariationTheme());
            temp.append(model.getTitle());
            temp.append(model.getProductId());
            temp.append(model.getPrice());
            temp.append(model.getMsrp());
            temp.append(model.getQuantity());
            temp.append(model.getImages());
            temp.append(model.getDescription());
            temp.append(model.getShortDescription());
            temp.append(model.getProductOrigin());
            temp.append(model.getCategory());
            temp.append(model.getWeight());
            temp.append(model.getBrand());
            temp.append(model.getMaterials());
            temp.append(model.getVendorProductUrl());
            temp.append(model.getAttributeKey1());
            temp.append(model.getAttributeValue1());
            temp.append(model.getAttributeKey2());
            temp.append(model.getAttributeValue2());
            temp.append(model.getAttributeKey3());
            temp.append(model.getAttributeValue3());
            temp.append(model.getAttributeKey4());
            temp.append(model.getAttributeValue4());
            temp.append(model.getAttributeKey5());
            temp.append(model.getAttributeValue5());
            temp.append(model.getAttributeKey6());
            temp.append(model.getAttributeValue6());
            temp.append(model.getAttributeKey7());
            temp.append(model.getAttributeValue7());
            temp.append(model.getAttributeKey8());
            temp.append(model.getAttributeValue8());
            temp.append(model.getAttributeKey9());
            temp.append(model.getAttributeValue9());
            temp.append(model.getAttributeKey10());
            temp.append(model.getAttributeValue10());
            temp.append(model.getAttributeKey11());
            temp.append(model.getAttributeValue11());
            temp.append(model.getAttributeKey12());
            temp.append(model.getAttributeValue12());
            temp.append(model.getAttributeKey13());
            temp.append(model.getAttributeValue13());
            temp.append(model.getAttributeKey14());
            temp.append(model.getAttributeValue14());
            temp.append(model.getAttributeKey15());
            temp.append(model.getAttributeValue15());
            temp.append(model.getAttributeKey16());
            temp.append(model.getAttributeValue16());
            temp.append(model.getAttributeKey17());
            temp.append(model.getAttributeValue17());
            temp.append(model.getAttributeKey18());
            temp.append(model.getAttributeValue18());
            temp.append(model.getAttributeKey19());
            temp.append(model.getAttributeValue19());
            temp.append(model.getAttributeKey20());
            temp.append(model.getAttributeValue20());
            return  MD5.getMD5(temp.toString());
        }

//        /**
//         * 读入Feed文件，并做check，生成FeedInfoModel数据
//         *
//         * @param feedFile  导入Feed文件
//         *
//         * @return FeedInfoModel数据（列表）
//         */
//        private Map<String, CmsBtFeedInfoModel> readExcel(File feedFile, StringBuilder stringBuilder) {
//            // Code列表
//            Map<String, CmsBtFeedInfoModel> codeMap = new HashMap<>();
//
//            XSSFWorkbook wb = null;
//            int attributeItemsNum = 0;
//            try {
//                wb = new XSSFWorkbook(new FileInputStream(feedFile));
//            } catch (Exception e) {
//                // Fail to read Feed File.
//                addErrorMessage(stringBuilder, "8000001", null, "", "");
//                return codeMap;
//            }
//
//            Sheet sheet = wb.getSheet("Vo-Feed");
//            if (sheet == null) {
//                // Sheet Name[Vo-Feed] is not exist.
//                addErrorMessage(stringBuilder, "8000007", null, "", "");
//                return codeMap;
//            }
//
//            // Parent-Id列表<Parent-Id, 个数>
//            Map<String, Integer> parentIdMap = new HashMap<>();
//            // 先所有数据循环一次，集计出所有Parent-ID列表<Parent-Id, 个数>和Code列表
//            int index = 0;
//            for (Row row : sheet) {
//                String sku = getCellValue(row, SKU_INDEX);
//                String parentId = getCellValue(row, PARENT_ID);
//                // Sku不等于空白，Parent-ID等于空白，那么这行就是Code
//                if (index != 0 && !StringUtils.isEmpty(sku) && StringUtils.isEmpty(parentId)) {
//                    CmsBtFeedInfoModel model = codeMap.get(sku);
//                    if (model == null) {
//                        codeMap.put(sku, new CmsBtFeedInfoModel());
//                    }
//                }
//                // 集计出所有Parent-ID列表
//                if (index != 0 && !StringUtils.isEmpty(parentId)) {
//                    Integer num = parentIdMap.get(parentId);
//                    if (num == null) {
//                        parentIdMap.put(parentId, new Integer(1));
//                    } else {
//                        parentIdMap.put(parentId, num + 1);
//                    }
//                }
//                index++;
//            }
//
//            // Sku列表
//            Set<String> skuSet = new HashSet<>();
//            index = 0;
//            for (Row row : sheet) {
//                if (index == 0) {
//                    // attribute项目的个数取得
//                    attributeItemsNum = readHeader(row);
//                } else {
//                    // 数据行
//                    checkRecord(row, String.valueOf(index + 1), attributeItemsNum, stringBuilder, codeMap,  parentIdMap, skuSet);
//                }
//                index++;
//            }
//            return codeMap;
//        }

//        /**
//         * attribute项目的个数取得
//         *
//         * @param row                   行
//         * @return attribute项目的个数
//         */
//        private int readHeader(Row row) {
//            int attributeItemsNum = 0;
//            int attributeEndIndex;
//            attributeEndIndex = ATTRIBUTE_COLUMN_START_INDEX;
//
//            for (int index = ATTRIBUTE_COLUMN_START_INDEX; ; index++) {
//                String attributeName = getCellValue(row, index);
//                // 直到Tile栏attribute的内容是空白
//                if (StringUtils.isEmpty(attributeName)) {
//                    // 设定最后一个attribute项目的索引
//                    attributeEndIndex = index - 1;
//                    if (attributeEndIndex < ATTRIBUTE_COLUMN_START_INDEX) {
//                        attributeEndIndex = ATTRIBUTE_COLUMN_START_INDEX;
//                    }
//                    break;
//                }
//            }
//
//            // attribute项目的个数
//            attributeItemsNum = (attributeEndIndex - ATTRIBUTE_COLUMN_START_INDEX + 1)/2;
//
//            return attributeItemsNum;
//        }

//        /**
//         * check数据，并保存FeedInfo对象列表
//         *
//         * @param row 数据行
//         * @param rowNum 行号
//         * @param attributeItemsNum 自定义属性个数
//         * @param codeMap Code列表<Code, CmsBtFeedInfoModel>
//         * @param parentIdMap Parent-ID列表<Parent-Id, 个数>
//         * @param skuSet 判断Sku是否重复
//         *
//         */
//        private void checkRecord(Row row, String rowNum, int attributeItemsNum, StringBuilder stringBuilder,
//                                 Map<String, CmsBtFeedInfoModel> codeMap, Map<String, Integer> parentIdMap, Set<String> skuSet) {
//
//            // 是否CodeLevel的数据
//            boolean isCodeLevel = false;
//            // 是否SkuLevel的数据
//            boolean isSkuLevel = false;
//
//            // sku
//            String sku = getCellValue(row, SKU_INDEX);
//            // ClientSku必须输入
//            if (StringUtils.isEmpty(sku)) {
//                // Sku is required.
//                addErrorMessage(stringBuilder, "8000002", new Object[]{columnMap.get(SKU_INDEX)}, rowNum, columnMap.get(SKU_INDEX));
//                return;
//            } else {
//                // ClientSku不能重复
//                if (skuSet.contains(sku)) {
//                    // sku is duplicated.
//                    addErrorMessage(stringBuilder, "8000003", new Object[]{sku}, rowNum, columnMap.get(SKU_INDEX));
//                    return;
//                } else {
//                    skuSet.add(sku);
//                }
//            }
//
//            // parent-id
//            String parentId = getCellValue(row, PARENT_ID);
//            // 如果parent-id不为空白,那么parent-id的指向必须存在，而且必须是Code级别的
//            if (!StringUtils.isEmpty(parentId) && !codeMap.containsKey(parentId)) {
//                // parent-id is invalidate..
//                addErrorMessage(stringBuilder, "8000004", new Object[]{parentId}, rowNum, columnMap.get(PARENT_ID));
//            }
//
//            // 如果parent-id为空白，那么这行是Code级别的数据
//            if (StringUtils.isEmpty(parentId)) {
//                isCodeLevel = true;
//            }
//
//            // 如果parent-id不为空白，并且没有其他parent-id指向这行明细的Sku，那么这行就是Sku级别的数据
//            if (!StringUtils.isEmpty(parentId) ||
//                    (StringUtils.isEmpty(parentId) && !parentIdMap.containsKey(sku))) {
//                isSkuLevel = true;
//            }
//
//            // variation-theme
//            String variationTheme = getCellValue(row, VARIATION_THEME);
//
//            // 如果这行是Sku，并且parent-id输入的情况
//            if (isSkuLevel && !StringUtils.isEmpty(parentId)) {
//                // 还有其他行的ParentId指向这行,那么variation-theme必须指定
//                if (parentIdMap.get(parentId) >= 2 && StringUtils.isEmpty(variationTheme)) {
//                    // variation-theme is Required.
//                    addErrorMessage(stringBuilder, "8000002", new Object[]{columnMap.get(VARIATION_THEME)}, rowNum, columnMap.get(VARIATION_THEME));
//                }
//            }
//
//            // title
//            String title = getCellValue(row, TITLE);
//            // 如果这行是Code,那么title是必须的
//            if (isCodeLevel && StringUtils.isEmpty(title)) {
//                // title is Required.
//                addErrorMessage(stringBuilder, "8000002", new Object[]{columnMap.get(TITLE)}, rowNum, columnMap.get(TITLE));
//            }
//
//            // product-id
//            String productId = getCellValue(row, PRODUCT_ID);
//            // 如果这行是Sku,那么product-id是必须的
//            if (isSkuLevel && StringUtils.isEmpty(productId)) {
//                // product-id is Required.
//                addErrorMessage(stringBuilder, "8000002", new Object[]{columnMap.get(PRODUCT_ID)}, rowNum, columnMap.get(PRODUCT_ID));
//            }
//
//            // price
//            String price = getCellValue(row, PRICE);
//            // 如果这行是Sku
//            if (isSkuLevel) {
//                // price是必须的,并且是大于0的数字
//                if (StringUtils.isEmpty(price) || !StringUtils.isNumeric(price) || Float.valueOf(price) <= 0) {
//                    // price must be a Number more than 0.
//                    addErrorMessage(stringBuilder, "8000005", new Object[]{columnMap.get(PRICE)}, rowNum, columnMap.get(PRICE));
//                }
//            }
//
//            // msrp
//            String msrp = getCellValue(row, MSRP);
//            // 如果这行是Sku,并且MSRP有值
//            if (isSkuLevel && !StringUtils.isEmpty(msrp)) {
//                // msrp如果输入，必须是大于0的数字
//                if (!StringUtils.isNumeric(msrp) || Float.valueOf(msrp) <= 0) {
//                    // msrp must be a Number more than 0.
//                    addErrorMessage(stringBuilder, "8000005", new Object[]{columnMap.get(MSRP)}, rowNum, columnMap.get(MSRP));
//                }
//            }
//
//            // quantity
//            String quantity = getCellValue(row, QUANTITY);
//            // 如果这行是Sku
//            if (isSkuLevel) {
//                // quantity是必须的,并且是大于0的数字
//                if (StringUtils.isEmpty(quantity) || !StringUtils.isDigit(quantity)) {
//                    // quantity must be a positive Integer.
//                    addErrorMessage(stringBuilder, "8000006", new Object[]{columnMap.get(QUANTITY)}, rowNum, columnMap.get(QUANTITY));
//                }
//            }
//
//            // images
//            String images = getCellValue(row, IMAGES);
//            // 如果这行是Code,那么images是必须的
//            if (isCodeLevel && StringUtils.isEmpty(images)) {
//                // images is Required.
//                addErrorMessage(stringBuilder, "8000002", new Object[]{columnMap.get(IMAGES)}, rowNum, columnMap.get(IMAGES));
//            }
//
//            // description
//            String description = getCellValue(row, DESCRIPTION);
//            // 如果这行是Code,那么description是必须的
//            if (isCodeLevel && StringUtils.isEmpty(description)) {
//                // description is Required.
//                addErrorMessage(stringBuilder, "8000002", new Object[]{columnMap.get(DESCRIPTION)}, rowNum, columnMap.get(DESCRIPTION));
//            }
//
//            // short-description
//            String shortDescription = getCellValue(row, SHORT_DESCRIPTION);
//            // 如果这行是Code,那么short-description是必须的
//            if (isCodeLevel && StringUtils.isEmpty(shortDescription)) {
//                // short-description is Required.
//                addErrorMessage(stringBuilder, "8000002", new Object[]{columnMap.get(SHORT_DESCRIPTION)}, rowNum, columnMap.get(SHORT_DESCRIPTION));
//            }
//
//            // product-origin
//            String productOrigin = getCellValue(row, PRODUCT_ORIGIN);
//            // 如果这行是Code,那么product-origin是必须的
//            if (isCodeLevel && StringUtils.isEmpty(productOrigin)) {
//                // product-origin is Required.
//                addErrorMessage(stringBuilder, "8000002", new Object[]{columnMap.get(PRODUCT_ORIGIN)}, rowNum, columnMap.get(PRODUCT_ORIGIN));
//            }
//
//            // category
//            String category = getCellValue(row, CATEGORY);
//            // TODO 处理Category的空格
//            String[] categoryArray = category.split("-");
//            category = "";
//            for (String categoryItem : categoryArray) {
//                // 不等于空的情况下，去掉首尾空格重新组装一下
//                if (!StringUtils.isEmpty(categoryItem)) {
//                    category += categoryItem.trim() + "-";
//                }
//            }
//            // 去掉最后一个分隔符[-]
//            if (!StringUtils.isEmpty(category)) {
//                category.substring(category.length() - 1);
//            }
//            // 如果这行是Code,那么category是必须的
//            if (isCodeLevel && StringUtils.isEmpty(category)) {
//                // category is Required.
//                addErrorMessage(stringBuilder, "8000002", new Object[]{columnMap.get(CATEGORY)}, rowNum, columnMap.get(CATEGORY));
//            }
//
//            // weight
//            String weight = getCellValue(row, WEIGHT);
//            // 如果这行是Code,那么weight是必须的
//            if (isCodeLevel && StringUtils.isEmpty(weight)) {
//                // weight is Required.
//                addErrorMessage(stringBuilder, "8000002", new Object[]{columnMap.get(WEIGHT)}, rowNum, columnMap.get(WEIGHT));
//            }
//
//            // brand
//            String brand = getCellValue(row, BRAND);
//            // 如果这行是Code,那么brand是必须的
//            if (isCodeLevel && StringUtils.isEmpty(brand)) {
//                // brand is Required.
//                addErrorMessage(stringBuilder, "8000002", new Object[]{columnMap.get(BRAND)}, rowNum, columnMap.get(BRAND));
//            }
//
//            // Materials
//            String materials = getCellValue(row, MATERIALS);
//            // 如果这行是Code,那么materials是必须的
//            if (isCodeLevel && StringUtils.isEmpty(materials)) {
//                // materials is Required.
//                addErrorMessage(stringBuilder, "8000002", new Object[]{columnMap.get(MATERIALS)}, rowNum, columnMap.get(MATERIALS));
//            }
//
//            // Vendor-Product-Url
//            String vendorProductUrl = getCellValue(row, VENDOR_PRODUCT_URL);
//
//            // attribute中定义的Sku唯一识别标识
//            String skuKey = "";
//            int skuKeyColumnNum = 0;
//            Map<String, List<String>> attributeMap = new HashMap<>();
//            // attribute
//            if (attributeItemsNum > 0) {
//                for (int index = ATTRIBUTE_COLUMN_START_INDEX; index < ATTRIBUTE_COLUMN_START_INDEX + attributeItemsNum * 2; index = index + 2) {
//                    String attributeKey =  getCellValue(row, index);
//                    String attributeValue =  getCellValue(row, index + 1);
//                    // 这条是Sku的话,找到这条的Key（variation-theme设定的字段）
//                    if(isSkuLevel && !StringUtils.isEmpty(variationTheme) && variationTheme.equals(attributeKey)) {
//                        if (StringUtils.isEmpty(skuKey)) {
//                            skuKey = attributeValue;
//                            skuKeyColumnNum = (index - ATTRIBUTE_COLUMN_START_INDEX)/2 + 1;
//                        } else {
//                            // skuKey列重复
//                            // attribute-key is duplicated.
//                            addErrorMessage(stringBuilder, "8000009", null, rowNum, "attribute-key-" + (index - ATTRIBUTE_COLUMN_START_INDEX)/2 + 1);
//                        }
//                    }
//                    // 这条是Code的话,attribute信息设定的话加入attribute列表
//                    if (isCodeLevel && !StringUtils.isEmpty(attributeKey) && !StringUtils.isEmpty(attributeValue)) {
//                        List<String> attributeContent = new ArrayList<>();
//                        attributeContent.add(attributeValue);
//                        if (attributeMap.get(attributeKey) == null) {
//                            attributeMap.put(attributeKey, attributeContent);
//                        } else {
//                            // attribute重复
//                            // attribute-key is duplicated.
//                            addErrorMessage(stringBuilder, "8000009", null, rowNum, "attribute-key-" + (index - ATTRIBUTE_COLUMN_START_INDEX)/2 + 1);
//                        }
//                    }
//                }
//            }
//            // 这条是Sku的话,variation-theme设定了，但是attribute中没有设定skuKey的话，提示错误
//            if (isSkuLevel && StringUtils.isEmpty(skuKey) && !StringUtils.isEmpty(variationTheme)) {
//                // %s(variation-theme) must be set in attribute.
//                addErrorMessage(stringBuilder, "8000008", new Object[]{variationTheme}, rowNum, "attribute");
//            }
//
//            // 这行是Code级别的话，生成CmsBtFeedInfoModel
//            if (isCodeLevel) {
//                CmsBtFeedInfoModel feedInfo = codeMap.get(sku);
//                feedInfo.setCode(sku);
//                feedInfo.setModel(sku);
//                feedInfo.setCategory(category);
//                feedInfo.setCatId(MD5.getMD5(category));
//                feedInfo.setName(title);
//                feedInfo.setImage(Arrays.asList(images.split(",")));
//                feedInfo.setLongDescription(description);
//                feedInfo.setShortDescription(shortDescription);
//                feedInfo.setOrigin(productOrigin);
//                feedInfo.setWeight(weight);
//                feedInfo.setBrand(brand);
//                feedInfo.setMaterial(materials);
//                feedInfo.setClientProductURL(vendorProductUrl);
//                feedInfo.setAttribute(attributeMap);
//            }
//
//            // 这行是Sku级别的话，取得Code级别的CmsBtFeedInfoModel加入Sku
//            if (isSkuLevel) {
//                CmsBtFeedInfoModel feedInfo = null;
//                // 如果parent-id不为空，那么通过parent-id取到Code级别的CmsBtFeedInfoModel
//                if (!StringUtils.isEmpty(parentId)) {
//                    feedInfo = codeMap.get(parentId);
//                } else {
//                    // 否则通过sku取到Code级别的CmsBtFeedInfoModel
//                    feedInfo = codeMap.get(sku);
//                }
//
//                // 取得sku列表并且加入
//                List<CmsBtFeedInfoModel_Sku> skusModel = feedInfo.getSkus();
//                if (skusModel == null) {
//                    skusModel = new ArrayList<>();
//                    feedInfo.setSkus(skusModel);
//                }
//                // 先check一下
//                for (CmsBtFeedInfoModel_Sku skuModel : skusModel) {
//                    // 同一个parent-id下， product-id重复的话，报错
//                    if (!StringUtils.isEmpty(productId)) {
//                        if (productId.equals(skuModel.getBarcode())) {
//                            // %s must be a unique value in same parent-id.
//                            addErrorMessage(stringBuilder, "8000010", new Object[]{columnMap.get(PRODUCT_ID)}, rowNum, columnMap.get(PRODUCT_ID));
//                        }
//                    }
//                    // 同一个parent-id下， skuKey重复的话，报错
//                    if (!StringUtils.isEmpty(skuKey) && !StringUtils.isEmpty(variationTheme)) {
//                        if ((feedInfo.getCode() + skuKey).equals(skuModel.getSku())) {
//                            // %s must be a unique value in same parent-id.
//                            addErrorMessage(stringBuilder, "8000010", new Object[]{variationTheme}, rowNum, "attribute-key-" + skuKeyColumnNum);
//                        }
//                    }
//                }
//                // 创建Sku
//                CmsBtFeedInfoModel_Sku skuModel = new CmsBtFeedInfoModel_Sku();
//                skuModel.setBarcode(productId);
//                skuModel.setClientSku(sku);
//                // variation-theme是空的话，sku=client_sku
//                if (StringUtils.isEmpty(variationTheme)) {
//                    skuModel.setSku(sku);
//                    skuModel.setSize("oneSize");
//                } else {
//                    // variation-theme不是空的话，sku= code + variation-theme指定字段的唯一值
//                    skuModel.setSku(feedInfo.getCode() + skuKey);
//                    skuModel.setSize(skuKey);
//                }
//                skuModel.setImage(Arrays.asList(images.split(",")));
//                if (!StringUtils.isEmpty(quantity)) {
//                    skuModel.setQty(new Integer(quantity));
//                }
//                if (!StringUtils.isEmpty(msrp)) {
//                    skuModel.setPriceClientMsrp(new BigDecimal(msrp).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//                } else {
//                    if (!StringUtils.isEmpty(price)) {
//                        skuModel.setPriceClientMsrp(new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//                    }
//                }
//
//                if (!StringUtils.isEmpty(price)) {
//                    skuModel.setPriceClientRetail(new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//                    skuModel.setPriceCurrent(new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//                }
//
//                skusModel.add(skuModel);
//            }
//        }







//        /**
//         * 返回单元格值
//         *
//         * @param row 行
//         * @param col 列
//         * @return 单元格值
//         */
//        public String getCellValue(Row row, int col) {
//            if (row == null) return "";
//            Cell cell = row.getCell(col);
//            if (cell == null) return "";
//            String result = getCellValue(cell);
//            if (result == null) {
//                result = "";
//            }
//            return result.trim();
//        }
//
//        public String getCellValue(Cell cell) {
//            String ret;
//            switch (cell.getCellType()) {
//                case Cell.CELL_TYPE_BLANK:
//                    ret = "";
//                    break;
//                case Cell.CELL_TYPE_BOOLEAN:
//                    ret = String.valueOf(cell.getBooleanCellValue());
//                    break;
//                case Cell.CELL_TYPE_ERROR:
//                    ret = null;
//                    break;
//                case Cell.CELL_TYPE_FORMULA:
//                    Workbook wb = cell.getSheet().getWorkbook();
//                    CreationHelper crateHelper = wb.getCreationHelper();
//                    FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
//                    ret = getCellValue(evaluator.evaluateInCell(cell));
//                    break;
//                case Cell.CELL_TYPE_NUMERIC:
//                    if (DateUtil.isCellDateFormatted(cell)) {
//                        ret = simpleDateFormat.format(cell.getDateCellValue());
//                    } else {
//                        ret = numberFormatter.format(cell.getNumericCellValue());
//                    }
//                    break;
//                case Cell.CELL_TYPE_STRING:
//                    ret = cell.getStringCellValue();
//                    break;
//                default:
//                    ret = null;
//            }
//
//            return ret;
//        }

        /**
         * 追加ErrorMessage
         *
         * @param errorList  所有Error内容
         * @param messageCode  错误Code
         * @param args 参数
         * @param rowNum  行号
         * @param column  列号
         *
         * @return FeedInfoModel数据（列表）
         */
        private void addErrorMessage(List<Map<String, Object>> errorList, String messageCode, Object[] args, Integer rowNum, String column) {

            Map<String, Object> errorMap = new HashMap<>();

            // 取得具体的ErrorMessage内容
            MessageBean messageBean = messageService.getMessage("en", messageCode);
            if (messageBean != null) {
                String message = String.format(messageBean.getMessage(), args);
                errorMap.put("row", rowNum);
                errorMap.put("column", column);
                errorMap.put("message", message);
            } else {
                // 一般情况下不可能，除非ct_message_info表中没有加入这个message
                errorMap.put("row", rowNum);
                errorMap.put("column", column);
                errorMap.put("message", messageCode);
            }

            errorList.add(errorMap);
        }

        /**
         * 追加ErrorMessage
         *
         * @param error  所有Error内容
         * @param messageCode  错误Code
         * @param args 参数
         * @param rowNum  行号
         * @param column  列号
         *
         * @return FeedInfoModel数据（列表）
         */
        private void addErrorMessage(StringBuilder error, String messageCode, Object[] args, Integer rowNum, String column) {
            // 取得具体的ErrorMessage内容
            MessageBean messageBean = messageService.getMessage("en", messageCode);
            if (messageBean != null) {
                String message = String.format(messageBean.getMessage(), args);
                error.append(String.valueOf(rowNum) + VmsConstants.COMMA + column + VmsConstants.COMMA + message + "\r");
            } else {
                // 一般情况下不可能，除非ct_message_info表中没有加入这个message
                error.append(String.valueOf(rowNum) + VmsConstants.COMMA + column + VmsConstants.COMMA + messageCode + "\r");
            }
        }

        /**
         * 生成Feed错误结果文件，并且更新vms_bt_feed_file表
         *
         * @param stringBuilder 错误内容
         *
         * return Feed错误结果文件的路径
         */
        private String createErrorFile(StringBuilder stringBuilder) throws IOException {
            $info("生成Feed检索结果Error文件,channel：" + channel.getFull_name());
            // 取得Feed文件检查结果路径
            String feedErrorFilePath = com.voyageone.common.configs.Properties.readValue("vms.feed.check");
            feedErrorFilePath += "/" + channel.getOrder_channel_id() + "/";
            // 创建文件目录
            FileUtils.mkdirPath(feedErrorFilePath);
            feedErrorFilePath += "Check_Result_" + DateTimeUtil.getNow("yyyyMMdd_HHmmss") + ".csv";
            try (OutputStream outputStream = new FileOutputStream(feedErrorFilePath)) {
                byte[] contentInBytes = stringBuilder.toString().getBytes();
                outputStream.write(contentInBytes);
                outputStream.flush();
                outputStream.close();
            }
            return feedErrorFilePath;
        }
    }







    /**
     * 检测Feed文件是否在vms_bt_feed_file表中存在，如果不存在那么新建一条文件管理信息
     *
     * @param orderChannelIdList 渠道列表
     */
    public void checkFeedFileDBInfo( List<String> orderChannelIdList) {

        // 取得Feed文件上传路径
        String feedFilePath = com.voyageone.common.configs.Properties.readValue("vms.feed.upload");

        // 按渠道进行处理
        for (final String orderChannelID : orderChannelIdList) {
            // 这个渠道的Feed文件的根目录
            File root = new File(feedFilePath + "/" + orderChannelID + "/");
            // 扫描根目录下面的所有文件（不包含子目录）
            File[] files = root.listFiles();
            // 如果存在文件，那么逐个处理
            if (files != null && files.length > 0) {
                for (File file : files) {
                    // 只处理文件，跳过目录
                    if (!file.isDirectory()) {
                        // 只处理扩展名为.xlsx的文件
                        String fileName = file.getName();
                        if (fileName.lastIndexOf(".csv") > -1) {
                            if (".csv".equals(fileName.substring(fileName.length() - 4))) {
                                // 看看文件信息是否在vms_bt_feed_file表中存在
                                Map<String, Object> param = new HashMap<>();
                                param.put("channelId", orderChannelID);
                                param.put("fileName", file.getPath());
                                List<VmsBtFeedFileModel> feedFileList = vmsBtFeedFileDao.selectList(param);
                                // 不存在说明是客户通过FTP直接传的，需要新建文件信息
                                if (feedFileList == null || feedFileList.size() == 0) {
                                    VmsBtFeedFileModel model = new VmsBtFeedFileModel();
                                    model.setChannelId(orderChannelID);
                                    model.setFileName(file.getAbsolutePath());
                                    model.setStatus(VmsConstants.FeedFileStatus.WAITING_IMPORT);
                                    model.setCreater(getTaskName());
                                    model.setModifier(getTaskName());
                                    vmsBtFeedFileDao.insert(model);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}