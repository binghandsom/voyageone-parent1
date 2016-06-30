package com.voyageone.task2.vms.service;

import com.google.common.base.Joiner;
import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.*;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.ComplexField;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.MultiComplexField;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MD5;
import com.voyageone.common.util.StringUtils;
import com.voyageone.common.util.inch2cm.InchStrConvert;
import com.voyageone.service.bean.cms.Condition;
import com.voyageone.service.bean.cms.feed.FeedCustomPropWithValueBean;
import com.voyageone.service.bean.cms.product.ProductPriceBean;
import com.voyageone.service.bean.cms.product.ProductSkuPriceBean;
import com.voyageone.service.bean.cms.product.ProductUpdateBean;
import com.voyageone.service.bean.cms.task.stock.StockExcelBean;
import com.voyageone.service.bean.com.MessageBean;
import com.voyageone.service.dao.cms.mongo.*;
import com.voyageone.service.dao.vms.VmsBtFeedFileDao;
import com.voyageone.service.daoext.cms.CmsBtImagesDaoExt;
import com.voyageone.service.daoext.vms.VmsBtFeedFileDaoExt;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.cms.*;
import com.voyageone.service.impl.cms.feed.FeedCustomPropService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductPriceLogService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductSkuService;
import com.voyageone.service.model.cms.CmsBtImagesModel;
import com.voyageone.service.model.cms.enums.MappingPropType;
import com.voyageone.service.model.cms.enums.Operation;
import com.voyageone.service.model.cms.enums.SrcType;
import com.voyageone.service.model.cms.mongo.CmsMtCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeAllModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeAllModel_Platform;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedMappingModel;
import com.voyageone.service.model.cms.mongo.feed.mapping.Mapping;
import com.voyageone.service.model.cms.mongo.feed.mapping.Prop;
import com.voyageone.service.model.cms.mongo.feed.mapping2.CmsBtFeedMapping2Model;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.service.model.vms.VmsBtFeedFileModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.bean.ItemDetailsBean;
import com.voyageone.task2.cms.dao.ItemDetailsDao;
import com.voyageone.task2.cms.dao.TmpOldCmsDataDao;
import com.voyageone.task2.vms.VmsConstants;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
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

    public final static Map<Object, String> columnMap = new HashMap() {{
        put(SKU_INDEX, "Sku");
        put(PARENT_ID, "Parent-id");
        put(RELATIONSHIP_TYPE, "Relationship-type");
        put(VARIATION_THEME, "Variation-theme");
        put(TITLE, "Title");
        put(PRODUCT_ID, "Product-ID");
        put(PRICE, "Price");
        put(MSRP, "MSRP");
        put(QUANTITY, "Quantity");
        put(IMAGES, "Images");
        put(DESCRIPTION, "Description");
        put(SHORT_DESCRIPTION, "Short-Description");
        put(PRODUCT_ORIGIN, "Product Origin");
        put(CATEGORY, "Category");
        put(WEIGHT, "Weight");
        put(BRAND, "Brand");
        put(MATERIALS, "Materials");
    }};

    /**
     * AttributeColumnStartIndex
     */
    public static final int ATTRIBUTE_COLUMN_START_INDEX = 17;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final NumberFormat numberFormatter = new DecimalFormat("#");

    @Autowired
    private VmsBtFeedFileDao vmsBtFeedFileDao;

    @Autowired
    private VmsBtFeedFileDaoExt vmsBtFeedFileDaoExt;

    @Autowired
    private MessageService messageService;



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
            param.put("channel_id", channel.getOrder_channel_id());
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
                    $info("Feed文件check的处理开始 文件路径：" + model.getFileName());
                    List<CmsBtFeedInfoModel> feedInfoList = readExcel(feedFile, stringBuilder);
                    $info("Feed文件check的处理结束");


                    if (feedInfoList.size() > 0) {
                        $info("导入数据库开始");
                        //                saveFeedData(feedInfoList);
                        $info(String.format("导入数据库结束,更新了%d件", feedInfoList.size()));
                    } else {
                        $info("没有更新对象");
                    }
                } else {
                    // 一般情况下不可能发生，除非手动删除文件
                    $error("Feed文件不存在 文件路径：" + model.getFileName());
                }
            }
            $info(channel.getFull_name() + "产品 Feed文件导入结束");

        }

        /**
         * 读入Feed文件，并做check，生成FeedInfoModel数据
         *
         * @param feedFile  导入Feed文件
         *
         * @return FeedInfoModel数据（列表）
         */
        private List<CmsBtFeedInfoModel> readExcel(File feedFile, StringBuilder stringBuilder) {
            List<CmsBtFeedInfoModel> feedInfoList = new ArrayList<>();

            Workbook wb = null;
            int attributeItemsNum = 0;
            try {
                wb = WorkbookFactory.create(new FileInputStream(feedFile));
            } catch (Exception e) {
                addErrorMessage(stringBuilder, "8000001", null, "", "");
            }

            Sheet sheet = wb.getSheetAt(0);
            Set<String> parentIdSet = new HashSet<>();
            // 先所有数据循环一次，集计出所有Parent-ID列表 Map
            int index = 0;
            for (Row row : sheet) {
                String parentId = getCellValue(row, PARENT_ID);
                if (index != 0 && !StringUtils.isEmpty(parentId)) {
                    parentIdSet.add(parentId);
                }
                index++;
            }

            Set<String> skuSet = new HashSet<>();
            index = 0;
            for (Row row : sheet) {
                if (index == 0) {
                    // Attribute项目的个数取得
                    attributeItemsNum = readHeader(row);
                } else {
                    // 数据行
                    checkRecord(row, String.valueOf(index + 1), attributeItemsNum, stringBuilder, feedInfoList, skuSet);
                }
                index++;
            }
            return feedInfoList;
        }

        /**
         * Attribute项目的个数取得
         *
         * @param row                   行
         * @return Attribute项目的个数
         */
        private int readHeader(Row row) {
            int attributeItemsNum = 0;
            int attributeEndIndex;
            attributeEndIndex = ATTRIBUTE_COLUMN_START_INDEX;

            for (int index = ATTRIBUTE_COLUMN_START_INDEX; ; index++) {
                String AttributeName = getCellValue(row, index);
                // 直到Tile栏Attribute的内容是空白
                if (StringUtils.isEmpty(AttributeName)) {
                    // 设定最后一个Attribute项目的索引
                    attributeEndIndex = index - 1;
                    if (attributeEndIndex < ATTRIBUTE_COLUMN_START_INDEX) {
                        attributeEndIndex = ATTRIBUTE_COLUMN_START_INDEX;
                    }
                    break;
                }
            }

            // Attribute项目的个数
            attributeItemsNum = (attributeEndIndex - ATTRIBUTE_COLUMN_START_INDEX + 1)/2;

            return attributeItemsNum;
        }

        /**
         * check数据，并保存FeedInfo对象列表
         *
         * @param row 数据行
         * @param rowNum 行号
         * @param attributeItemsNum 自定义属性个数
         * @param feedInfoList FeedInfo对象列表
         * @param skuSet 判断Sku重复用的Set
         *
         */
        private void checkRecord(Row row, String rowNum, int attributeItemsNum, StringBuilder stringBuilder, List<CmsBtFeedInfoModel> feedInfoList, Set<String> skuSet) {

            // Sku
            String Sku = getCellValue(row, SKU_INDEX);
            // ClientSku必须输入
            if (StringUtils.isEmpty(Sku)) {
                // Sku is required.
                addErrorMessage(stringBuilder, "8000002", new Object[]{columnMap.get(SKU_INDEX)}, rowNum, columnMap.get(SKU_INDEX));
            } else {
                // ClientSku不能重复
                if (skuSet.contains(Sku)) {
                    // Sku is duplicated.
                    addErrorMessage(stringBuilder, "8000003", new Object[]{columnMap.get(SKU_INDEX)}, rowNum, columnMap.get(SKU_INDEX));
                } else {
                    skuSet.add(Sku);
                }
            }



        }







        /**
         * 返回单元格值
         *
         * @param row 行
         * @param col 列
         * @return 单元格值
         */
        public String getCellValue(Row row, int col) {
            if (row == null) return null;
            Cell cell = row.getCell(col);
            if (cell == null) return null;
            return getCellValue(cell);
        }

        public String getCellValue(Cell cell) {
            String ret;
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    ret = "";
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    ret = String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    ret = null;
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    Workbook wb = cell.getSheet().getWorkbook();
                    CreationHelper crateHelper = wb.getCreationHelper();
                    FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
                    ret = getCellValue(evaluator.evaluateInCell(cell));
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        ret = simpleDateFormat.format(cell.getDateCellValue());
                    } else {
                        ret = numberFormatter.format(cell.getNumericCellValue());
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    ret = cell.getStringCellValue();
                    break;
                default:
                    ret = null;
            }

            return ret;
        }

        /**
         * 追加ErrorMessage
         *
         * @param stringBuilder  ErrorMessage所有内容
         * @param messageCode  错误Code
         * @param args 参数
         * @param rowNum  行号
         * @param column  列号
         *
         * @return FeedInfoModel数据（列表）
         */
        private void addErrorMessage(StringBuilder stringBuilder, String messageCode, Object[] args, String rowNum, String column) {
            // 取得具体的ErrorMessage内容
            MessageBean messageBean = messageService.getMessage("en", messageCode);
            if (messageBean != null) {
                String message = String.format(messageBean.getMessage(), args);
                stringBuilder.append(rowNum + "," + column + "," + message + "\r");
            } else {
                // 一般情况下不可能，除非ct_message_info表中没有加入这个message
                stringBuilder.append(rowNum + "," + column + "," + messageCode + "\r");
            }

//            if (errorFile == null) {
//                // 取得Feed文件上传路径
//                String feedFileCheck = com.voyageone.common.configs.Properties.readValue("vms.feed.check");
//                String now = DateTimeUtil.getNow("yyyyMMdd_HHmmss");
//                errorFile = new File(feedFileCheck + "/" + channel.getOrder_channel_id() + "/Feed_Check_Result_" + now + ".csv");
//                FileOutputStream outputStream = new FileOutputStream(errorFile);
//            }

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
                        if (fileName.lastIndexOf(".xlsx") > -1) {
                            if (".xlsx".equals(fileName.substring(fileName.length() - 5))) {
                                // 看看文件信息是否在vms_bt_feed_file表中存在
                                Map<String, Object> param = new HashMap<>();
                                param.put("channel_id", orderChannelID);
                                param.put("file_name", fileName);
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