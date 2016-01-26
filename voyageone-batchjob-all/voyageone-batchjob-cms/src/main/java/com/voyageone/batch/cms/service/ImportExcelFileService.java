package com.voyageone.batch.cms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.dao.*;
import com.voyageone.batch.cms.model.JmBtDealImportModel;
import com.voyageone.batch.cms.model.JmBtImagesModel;
import com.voyageone.batch.cms.model.JmBtProductImportModel;
import com.voyageone.batch.cms.model.JmBtSkuImportModel;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.common.components.baidu.translate.BaiduTranslateUtil;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ExcelUtils;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by jerry on 2016/01/23.
 */
@Service
public class ImportExcelFileService extends BaseTaskService {

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Autowired
    private ProductImportDao productImportDao;
    @Autowired
    private SkuImportDao skuImportDao;
    @Autowired
    private DealImportDao dealImportDao;
    @Autowired
    private ImagesDao imagesDao;

    private DefaultTransactionDefinition def = new DefaultTransactionDefinition();

    private final String import_excel_file_path = "import_excel_file_path";

    // 上传Excel最大Sheet数
    private final int maxSheetCount = 12;
    private final String productSheetName = "Product";
    private final String skuSheetName = "SKU";
    private final String dealSheetName = "Product_Deal";
    private final String imageSheetName = "IMAGE";

    private final String productSheetHead = "店铺_Channel";
    private final String skuSheetHead = "Channel";
    private final String imageSheetHead = "seq";
    private final String dealSheetHead = "店铺_Channel";

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "ImportExcelFileJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 线程
        List<Runnable> threads = new ArrayList<>();

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {

            threads.add(new Runnable() {
                @Override
                public void run() {
                    importExcelFileMain(orderChannelID);
                }
            });

        }

        runWithThreadPool(threads, taskControlList);
    }

    /**
     * Excel导入主函数
     *
     */
    private void importExcelFileMain(String orderChannelID) {
        // 路径配置信息读取
        FtpBean filePathBean = formatSalesOrdersUploadFtpBean(orderChannelID);

        // 处理文件列表
        List<String> uploadFileList = FileUtils.getFileGroup2(filePathBean.getUpload_localpath());

        for (int i = 0; i < uploadFileList.size(); i++) {
            String filePath = filePathBean.getUpload_localpath() + File.separator + uploadFileList.get(i);

            importExcelFile(filePath, uploadFileList.get(i));
        }
    }

    /**
     * 配置Bean生成
     *
     */
    private FtpBean formatSalesOrdersUploadFtpBean(String orderChannelID) {
        FtpBean ftpBean = new FtpBean();

        // 配置信息读取
        List<ThirdPartyConfigBean> ftpFilePaths = ThirdPartyConfigs.getThirdPartyConfigList(orderChannelID, import_excel_file_path);

        // 本地文件路径
        ftpBean.setUpload_localpath(ftpFilePaths.get(0).getProp_val1());
        // 本地文件备份路径
        ftpBean.setUpload_local_bak_path(ftpFilePaths.get(0).getProp_val2());

        return ftpBean;
    }

    /**
     * Excel文件系统导入
     *
     */
    private void importExcelFile(String filePath, String fileName) {
        boolean ret = true;
        try {
            File excelFile = new File(filePath);
            InputStream fileInputStream = new FileInputStream(excelFile);
            Workbook book = WorkbookFactory.create(fileInputStream);

            // Excel Sheet数检查
            ret = chkSheetCount(book);
            if (!ret ){
                $info("上传的文档 [ %s ]Sheet数异常", fileName);
            }

//            // Sku Sheet读入
//            ret = readSkuSheet(book);
//            if (!ret) {
//                $info("上传的文档 [ %s ]Sku Sheet读入异常", fileName);
//            }

//            // Image Sheet读入
//            ret = readImageSheet(book);
//            if (!ret) {
//                $info("上传的文档 [ %s ]Image Sheet读入异常", fileName);
//            }

            // Deal Sheet读入
            ret = readDealSheet(book);
            if (!ret) {
                $info("上传的文档 [ %s ]Deal Sheet读入异常", fileName);
            }

            // Product Sheet读入
            ret = readProductSheet(book);
            if (!ret) {
                $info("上传的文档 [ %s ]Product Sheet读入异常", fileName);
            }
        } catch (Exception e) {
            logger.error("importExcelFile file error = " + fileName, e);
            issueLog.log(e,
                    ErrorType.BatchJob,
                    SubSystem.CMS,
                    "importExcelFile file = " + fileName
            );
        }
    }

    /**
     * Excel文件Sheet数检查
     *
     */
    private boolean chkSheetCount(Workbook book) {
        boolean ret = false;

        Sheet temp = book.getSheetAt(maxSheetCount - 1);
        if (temp != null ){
            ret = true;
        }
        return ret;
    }

    /**
     * prduct Sheet 读入
     *
     */
    private boolean readProductSheet(Workbook book) {
        boolean ret = true;

        Sheet productSheet = book.getSheet(productSheetName);
        for (Row row : productSheet) {

            if (isProductHeadRow(row)) {
                continue;
            }

            // Product Bean 生成
            JmBtProductImportModel productImportModel = getProductModel(row);

            // 内容检查
            ret = chkProductImportBean(productImportModel);

            // 数据库追加
            if (ret) {
                ret = insertProductTable(productImportModel);
            }
        }

        return ret;
    }

    private boolean isProductHeadRow(Row row) {
        boolean ret = false;

        if (productSheetHead.equals(ExcelUtils.getString(row, PruductSheetFormat.channel_id_index))) {
            ret = true;
        }

        return ret;
    }

    private boolean isSkuHeadRow(Row row) {
        boolean ret = false;

        if (skuSheetHead.equals(ExcelUtils.getString(row, SkuSheetFormat.channel_id_index))) {
            ret = true;
        }

        return ret;
    }

    private boolean isImageHeadRow(Row row) {
        boolean ret = false;

        if (imageSheetHead.equals(ExcelUtils.getString(row, ImageSheetFormat.seq_index))) {
            ret = true;
        }

        return ret;
    }

    private boolean isDealHeadRow(Row row) {
        boolean ret = false;

        if (dealSheetHead.equals(ExcelUtils.getString(row, DealSheetFormat.channel_id_index))) {
            ret = true;
        }

        return ret;
    }

    /**
     * 根据上传Excel，Product Bean生成
     *
     */
    private JmBtProductImportModel getProductModel(Row row) {
        JmBtProductImportModel productModel = new JmBtProductImportModel();
        productModel.setChannelId(ExcelUtils.getString(row, PruductSheetFormat.channel_id_index));
        productModel.setProductCode(ExcelUtils.getString(row, PruductSheetFormat.product_code_index));
        productModel.setProductDes(ExcelUtils.getString(row, PruductSheetFormat.product_des_index));
//        productModel.setCategoryLv4Id(Integer.valueOf(ExcelUtils.getString(row, PruductSheetFormat.category_lv4_id_index)));
        productModel.setCategoryLv4Id(10);
        productModel.setBrandId(Integer.valueOf(ExcelUtils.getString(row, PruductSheetFormat.brand_id_index, "#")));
        productModel.setProductName(ExcelUtils.getString(row, PruductSheetFormat.product_name_index));
        productModel.setForeignLanguageName(ExcelUtils.getString(row, PruductSheetFormat.foreign_language_name_index));
//        productModel.setFunctionIds(ExcelUtils.getString(row, PruductSheetFormat.function_ids_index));
        productModel.setFunctionIds("");

        productModel.setSynFlg("0");
        productModel.setCreater(getTaskName());
        productModel.setModifier(getTaskName());

        return productModel;
    }

    /**
     * 根据上传Excel，Sku Bean生成
     *
     */
    private JmBtSkuImportModel getSkuModel(Row row) {
        JmBtSkuImportModel skuModel = new JmBtSkuImportModel();
        skuModel.setChannelId(ExcelUtils.getString(row, SkuSheetFormat.channel_id_index));
        skuModel.setProductCode(ExcelUtils.getString(row, SkuSheetFormat.product_code_index));
        skuModel.setSku(ExcelUtils.getString(row, SkuSheetFormat.sku_index));
        //skuModel.setUpcCode(ExcelUtils.getString(row, SkuSheetFormat.upc_code_index));
        skuModel.setUpcCode("");
        skuModel.setAbroadPrice(Double.valueOf(ExcelUtils.getString(row, SkuSheetFormat.abroad_price_index)));
        skuModel.setDealPrice(ExcelUtils.getNum(row, SkuSheetFormat.deal_price_index));
        skuModel.setMarketPrice(ExcelUtils.getNum(row, SkuSheetFormat.market_price_index));
        skuModel.setSize(ExcelUtils.getString(row, SkuSheetFormat.size_index));
        skuModel.setHscode(ExcelUtils.getString(row, SkuSheetFormat.hscode_index));

        skuModel.setCreater(getTaskName());
        skuModel.setModifier(getTaskName());

        return skuModel;
    }

    /**
     * 根据上传Excel，Image Bean生成
     *
     */
    private JmBtImagesModel getImageModel(Row row) {
        JmBtImagesModel imagesModel = new JmBtImagesModel();
        imagesModel.setChannelId(ExcelUtils.getString(row, ImageSheetFormat.channel_id_index));
        imagesModel.setImageKey(ExcelUtils.getString(row, ImageSheetFormat.image_key_index));
        imagesModel.setImageType(Integer.valueOf(ExcelUtils.getString(row, ImageSheetFormat.image_type_index, "#")));
        imagesModel.setImageTypeExtend(ExcelUtils.getString(row, ImageSheetFormat.image_type_extend_index));
        imagesModel.setImageIndex(Integer.valueOf(ExcelUtils.getString(row, ImageSheetFormat.image_index, "#")));
        imagesModel.setOriginUrl(ExcelUtils.getString(row, ImageSheetFormat.origin_url_index));
        imagesModel.setJmUrl(ExcelUtils.getString(row, ImageSheetFormat.jm_url_index));

        imagesModel.setSynFlg(0);
        imagesModel.setCreater(getTaskName());
        imagesModel.setModifier(getTaskName());

        return imagesModel;
    }

    /**
     * 根据上传Excel，Deal Bean生成
     *
     */
    private JmBtDealImportModel getDealModel(Row row) {
        JmBtDealImportModel dealModel = new JmBtDealImportModel();
        dealModel.setChannelId(ExcelUtils.getString(row, DealSheetFormat.channel_id_index));
        dealModel.setProductCode(ExcelUtils.getString(row, DealSheetFormat.product_code_index));
        dealModel.setDealId(ExcelUtils.getString(row, DealSheetFormat.deal_id_index));
//        dealModel.setStartTime(ExcelUtils.getString(row, DealSheetFormat.start_time_index));
//        dealModel.setEndTime(ExcelUtils.getString(row, DealSheetFormat.end_time_index));
        dealModel.setStartTime("2015-01-01 11:11:00");
        dealModel.setEndTime("2015-01-01 11:11:00");

//        dealModel.setUserPurchaseLimit(Integer.valueOf(ExcelUtils.getString(row, DealSheetFormat.user_purchase_limit_index, "#")));
        dealModel.setUserPurchaseLimit(2);

        dealModel.setShippingSystemId(Integer.valueOf(ExcelUtils.getString(row, DealSheetFormat.shipping_system_id_index, "#")));
        dealModel.setProductLongName(ExcelUtils.getString(row, DealSheetFormat.product_long_name_index));
        dealModel.setProductMediumName(ExcelUtils.getString(row, DealSheetFormat.product_medium_name_index));
        dealModel.setProductShortName(ExcelUtils.getString(row, DealSheetFormat.product_short_name_index));
        dealModel.setSearchMetaTextCustom(ExcelUtils.getString(row, DealSheetFormat.search_meta_text_custom_index));

        dealModel.setSynFlg(0);
        dealModel.setCreater(getTaskName());
        dealModel.setModifier(getTaskName());

        return dealModel;
    }

    /**
     * Product Bean检查
     *
     */
    private boolean chkProductImportBean(JmBtProductImportModel productImportModel) {
        boolean ret = true;

        // name
        if (!chkLength(productImportModel.getProductName(), PruductSheetFormat.product_name_length)) {
            ret = false;
        }

        // foreign_language_name
        if (!chkLength(productImportModel.getForeignLanguageName(), PruductSheetFormat.foreign_language_name_length)) {
            ret = false;
        }
        return ret;
    }

    /**
     * Product Bean数据库追加
     *
     */
    private boolean insertProductTable(JmBtProductImportModel productImportModel) {
        boolean ret = true;

        ret = productImportDao.insertProductImportInfo(productImportModel);

        return ret;
    }

    /**
     * Sku Bean数据库追加
     *
     */
    private boolean insertSkuTable(JmBtSkuImportModel skuImportModel) {
        boolean ret = true;

        ret = skuImportDao.insertSkuImportInfo(skuImportModel);

        return ret;
    }

    /**
     * Deal Bean数据库追加
     *
     */
    private boolean insertDealTable(JmBtDealImportModel dealImportModel) {
        boolean ret = true;

        ret = dealImportDao.insertDealImportInfo(dealImportModel);

        return ret;
    }

    /**
     * Images Bean数据库追加
     *
     */
    private boolean insertImagesTable(JmBtImagesModel imagesModel) {
        boolean ret = true;

        ret = imagesDao.insertImagesInfo(imagesModel);

        return ret;
    }

    /**
     * sku Sheet 读入
     *
     */
    private boolean readSkuSheet(Workbook book) {
        boolean ret = true;

        Sheet skuSheet = book.getSheet(skuSheetName);
        for (Row row : skuSheet) {

            if (isSkuHeadRow(row)) {
                continue;
            }

            // Sku Bean 生成
            JmBtSkuImportModel skuImportModel = getSkuModel(row);

//            // 内容检查
//            ret = chkProductImportBean(skuImportModel);

            // 数据库追加
            if (ret) {
                ret = insertSkuTable(skuImportModel);
            }
        }

        return ret;
    }

    /**
     * Image Sheet 读入
     *
     */
    private boolean readImageSheet(Workbook book) {
        boolean ret = true;

        Sheet imageSheet = book.getSheet(imageSheetName);
        for (Row row : imageSheet) {

            if (isImageHeadRow(row)) {
                continue;
            }

            // Images Bean 生成
            JmBtImagesModel imagesModel = getImageModel(row);

//            // 内容检查
//            ret = chkProductImportBean(skuImportModel);

            // 数据库追加
            if (ret) {
                ret = insertImagesTable(imagesModel);
            }
        }

        return ret;
    }

    /**
     * Deal Sheet 读入
     *
     */
    private boolean readDealSheet(Workbook book) {
        boolean ret = true;

        Sheet dealSheet = book.getSheet(dealSheetName);
        for (Row row : dealSheet) {

            if (isDealHeadRow(row)) {
                continue;
            }

            // Deal Bean 生成
            JmBtDealImportModel dealImportModel = getDealModel(row);

//            // 内容检查
//            ret = chkProductImportBean(skuImportModel);

            // 数据库追加
            if (ret) {
                ret = insertDealTable(dealImportModel);
            }
        }

        return ret;
    }

    /**
     * 输入项目长度检查
     *
     */
    private boolean chkLength(String content, int length){
        boolean ret = false;

        if (content.length() <= length) {
            ret = true;
        }

        return ret;
    }

    /**
     * Product 格式
     *
     */
    private class PruductSheetFormat {
        private static final int channel_id_index = 0;
        private static final int product_code_index = 2;
        private static final int product_des_index = 21;
        private static final int category_lv4_id_index = 0;
        private static final int brand_id_index = 14;

        private static final int product_name_index = 5;
        private static final int product_name_length = 100;
        private static final String product_name_only_include_char = "（";

        private static final int foreign_language_name_index = 6;
        private static final int foreign_language_name_length = 100;

        private static final int attribute_index = 17;
        private static final int address_of_produce_index = 19;

        private static final int function_ids_index = 0;

    }

    /**
     * Sku 格式
     *
     */
    private class SkuSheetFormat {
        private static final int channel_id_index = 0;
        private static final int product_code_index = 2;
        private static final int sku_index = 3;
        // TODO 新追加
        private static final int upc_code_index = 0;
        private static final int abroad_price_index = 6;
        private static final int deal_price_index = 7;
        private static final int market_price_index = 8;
        private static final int size_index = 5;
        private static final int hscode_index = 5;
    }

    /**
     * Image 格式
     *
     */
    private class ImageSheetFormat {
        private static final int seq_index = 0;
        private static final int channel_id_index = 1;
        private static final int image_key_index = 2;
        private static final int image_type_index = 3;
        private static final int image_type_extend_index = 4;
        private static final int image_index = 5;
        private static final int origin_url_index = 6;
        private static final int jm_url_index = 7;
    }

    /**
     * Deal 格式
     *
     */
    private class DealSheetFormat {
        private static final int channel_id_index = 0;
        private static final int product_code_index = 2;
        private static final int deal_id_index = 1;
        private static final int start_time_index = 4;
        private static final int end_time_index = 5;
        private static final int user_purchase_limit_index = 6;
        private static final int shipping_system_id_index = 7;
        private static final int product_long_name_index = 8;
        private static final int product_medium_name_index = 9;
        private static final int product_short_name_index = 10;
        private static final int search_meta_text_custom_index = 13;
    }
}
