package com.voyageone.service.impl.cms.jumei2;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.common.util.ExceptionUtil;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.*;
import com.voyageone.common.util.excel.EnumExcelColumnType;
import com.voyageone.common.util.excel.ExcelColumn;
import com.voyageone.common.util.excel.ExcelException;
import com.voyageone.common.util.excel.ExportExcelInfo;
import com.voyageone.common.util.excel.ExportFileExcelUtil;
import com.voyageone.service.dao.cms.CmsBtJmPromotionExportTaskDao;
import com.voyageone.service.dao.cms.mongo.CmsMtProdSalesHisDao;
import com.voyageone.service.daoext.cms.CmsBtJmProductImagesDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionExportTaskDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionSkuDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.JmPromotionExportMQMessageBody;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionProductService;
import com.voyageone.service.model.cms.CmsBtJmPromotionExportTaskModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsMtProdSalesHisModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.util.MapModel;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.aspectj.bridge.Version.text;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotionExportTask3Service extends BaseService {
    @Autowired
    CmsBtJmPromotionExportTaskDao dao;
    @Autowired
    CmsBtJmPromotionExportTaskDaoExt daoExt;
    @Autowired
    CmsBtJmPromotionSkuDaoExt daoExtCmsBtJmPromotionSku;
    @Autowired
    CmsBtJmPromotionProductDaoExt daoExtCmsBtJmPromotionProduct;
    @Autowired
    CmsBtJmProductImagesDaoExt daoExtCmsBtJmProductImages;
    @Autowired
    CmsBtJmPromotionProductService cmsBtJmPromotionProductService;
    @Autowired
    CmsMqSenderService cmsMqSenderService;
    @Autowired
    CmsMtProdSalesHisDao cmsMtProdSalesHisDao;
    @Autowired
    CmsBtJmPromotion3Service cmsBtJmPromotion3Service;
    @Autowired
    private ProductService productService;

    public CmsBtJmPromotionExportTaskModel get(int id) {
        return dao.select(id);
    }

    public List<CmsBtJmPromotionExportTaskModel> getByPromotionId(int promotionId) {
        return daoExt.selectByPromotionId(promotionId);
    }

    public void export(int JmBtPromotionExportTaskId, String exportPath) {
        CmsBtJmPromotionExportTaskModel model = dao.select(JmBtPromotionExportTaskId);
        Parameter parameter = null;
        if(!StringUtil.isEmpty(model.getParameter())){
            parameter = JacksonUtil.json2Bean(model.getParameter(),Parameter.class);
        }
        String fileName = "Product" + DateTimeUtil.format(new Date(), "yyyyMMddHHmmssSSS") + ".xls";
        //"/usr/JMExport/"
        String filePath = exportPath + "/" + fileName;
        model.setBeginTime(DateTimeUtilBeijing.getCurrentBeiJingDate());
        //int TemplateType = model.getTemplateType();
        try {
            dao.update(model);
            List<String> codes = null;
            if(parameter != null){
                codes = getSelCodes(parameter);
            }
            if(ListUtils.isNull(codes)){
                throw new BusinessException("没有商品要导出");
            }
            List<Map<String, Object>> listProduct = daoExtCmsBtJmPromotionProduct.selectExportListByPromotionId(model.getCmsBtJmPromotionId(), codes);
            List<Map<String, Object>> listSku = daoExtCmsBtJmPromotionSku.selectExportListByPromotionId(model.getCmsBtJmPromotionId(), codes);

            Map<String, Integer>saleMap = getPromotionSale(model.getCmsBtJmPromotionId());
            if(saleMap.size() > 0){
                listSku.forEach(sku->{
                    Integer saleQty = saleMap.get(sku.get("skuCode"));
                    if(saleQty != null){
                        sku.put("saleQty", saleQty);
                    }
                });
            }
            export(filePath, listProduct, listSku, false);
            model.setSuccessRows(listProduct.size());
            if (listProduct.isEmpty()) {
                model.setErrorMsg("未查到商品");
            }
            model.setIsExport(true);
            model.setFileName(fileName);
        }catch (BusinessException ex){
            model.setErrorMsg(ex.getMessage());
            model.setErrorCode(1);
            model.setIsExport(false);
            ex.printStackTrace();
        }
        catch (Exception ex) {
            model.setErrorMsg(ExceptionUtil.getErrorMsg(ex));
            model.setErrorCode(1);
            ex.printStackTrace();
            model.setIsExport(false);
        }
        model.setEndTime(DateTimeUtilBeijing.getCurrentBeiJingDate());
        dao.update(model);
    }

    public void export(String fileName, List<Map<String, Object>> dataSourceProduct, List<Map<String, Object>> dataSourceSku, boolean isErrorColumn) {
        ExportExcelInfo<Map<String, Object>> productInfo = getProductExportExcelInfo(dataSourceProduct, isErrorColumn);
        ExportExcelInfo<Map<String, Object>> skuInfo = getSkuExportExcelInfo(dataSourceSku, isErrorColumn);

        for (Map<String, Object> productMap : productInfo.getDataSource()) {
            String channelId = (String) productMap.get("channelId");
            String productCode = (String) productMap.get("productCode");
            String jmCatPath = "";
            if (org.apache.commons.lang3.StringUtils.isNotBlank(channelId) && StringUtils.isNotBlank(productCode)) {
                CmsBtProductModel mongoProductModel = productService.getProductByCode(channelId, productCode);
                CmsBtProductModel_Platform_Cart jmPlatformCart = null;
                if (mongoProductModel != null && (jmPlatformCart = mongoProductModel.getPlatform(27)) != null) {
                    jmCatPath = jmPlatformCart.getpCatPath();
                    if (StringUtils.isBlank((String) productMap.get("brandName"))) {
                        productMap.put("brandName", mongoProductModel.getCommon().getFields().getBrand());
                    }
                }
            }
            productMap.put("jmCartPath", jmCatPath);

        }

        try {
            ExportFileExcelUtil.exportExcel(fileName, productInfo, skuInfo);
        } catch (ExcelException | IOException e) {
            e.printStackTrace();
        }
    }

    public ExportExcelInfo<Map<String, Object>> getProductExportExcelInfo(List<Map<String, Object>> dataSource, boolean isErrorColumn) {

        ExportExcelInfo<Map<String, Object>> info = new ExportExcelInfo<>(null);
        info.setFileName("Product");
        info.setSheet("Product");
        info.setDisplayColumnName(true);
        info.setDataSource(dataSource);
        //short colorIndex = IndexedColors.GREY_25_PERCENT.getIndex();//HSSFColor.GREY_25_PERCENT.index;
        info.addExcelColumn("产品编号", "productCode", "cms_bt_jm_promotion_product");
        info.addExcelColumn("Deal每人限购", "limit", "cms_bt_jm_promotion_product");
        info.addExcelColumn("APP端模块ID", "appId", "cms_bt_jm_promotion_product");
        info.addExcelColumn("PC端模块ID", "pcId", "cms_bt_jm_promotion_product");
        info.addExcelColumn("专场标签（以|分隔）", "promotionTag", "cms_bt_jm_promotion_product");
        info.addExcelColumn("商品英文名称", "foreignLanguageName", "cms_bt_jm_product");
        info.addExcelColumn("商品中文名称", "productNameCn", "cms_bt_jm_product");
        info.addExcelColumn("长标题", "productLongName", "cms_bt_jm_product");
        info.addExcelColumn("中标题", "productMediumName", "cms_bt_jm_product");
        info.addExcelColumn("短标题", "productShortName", "cms_bt_jm_product");
        info.addExcelColumn("自定义搜索词", "searchMetaTextCustom", "cms_bt_jm_product");
        info.addExcelColumn("英文产地", "origin", "cms_bt_jm_product");
        info.addExcelColumn("保质期", "availablePeriod", "cms_bt_jm_product");//add
        info.addExcelColumn("中文产地", "addressOfProduce", "cms_bt_jm_product");
        info.addExcelColumn("适合人群", "applicableCrowd", "cms_bt_jm_product");
        info.addExcelColumn("特别说明 用于聚美上新", "specialNote", "cms_bt_jm_product");
        info.addExcelColumn("英文颜色", "colorEn", "cms_bt_jm_product");
        info.addExcelColumn("中文颜色", "attribute", "cms_bt_jm_product");
        info.addExcelColumn("主数据类目", "voCategoryName", "cms_bt_jm_product");//add
        info.addExcelColumn("主数据品牌名称", "brandName", "cms_bt_jm_product");
        info.addExcelColumn("聚美品牌名称", "brandName", "cms_bt_jm_product");
        info.addExcelColumn("商品类别", "productType", "cms_bt_jm_product");
        info.addExcelColumn("尺码类别", "sizeType", "cms_bt_jm_product");
        info.addExcelColumn("使用方法_产品介绍", "productDesEn", "cms_bt_jm_product");
        info.addExcelColumn("使用方法_产品介绍", "productDesCn", "cms_bt_jm_product");
        info.addExcelColumn("聚美MallId", "jumeiMallId", "cms_bt_jm_product");
        info.addExcelColumn("聚美HID", "jmHashId", "cms_bt_jm_product");
        info.addExcelColumn("品牌", "brandName", "cms_bt_jm_product");
        info.addExcelColumn("库存", "quantity", "cms_bt_jm_promotion_product");
        info.addExcelColumn("JM类目", "jmCartPath");

        info.addExcelColumn("错误信息", "errorMsg", "cms_bt_jm_promotion_product");

        if (isErrorColumn) {
            info.addExcelColumn(info.getErrorColumn());
        }
        return info;
    }

    public ExportExcelInfo<Map<String, Object>> getSkuExportExcelInfo(List<Map<String, Object>> dataSource, boolean isErrorColumn) {
        ExportExcelInfo<Map<String, Object>> info = new ExportExcelInfo<>(null);
        info.setFileName("Product");
        info.setSheet("Sku");
        info.setDisplayColumnName(true);
        info.setDataSource(dataSource);
        short colorIndex = IndexedColors.GREY_25_PERCENT.getIndex();//HSSFColor.GREY_25_PERCENT.index;
        info.addExcelColumn("商品Code", "productCode", "cms_bt_jm_promotion_sku");
        info.addExcelColumn("品牌方SKU(聚美商家商品编码)", "skuCode", "cms_bt_jm_promotion_sku");
        ExcelColumn column = info.addExcelColumn("规格", "format", (Object value, Map<String, Object> map, Integer index) -> {
            //规格，  property， ComboBox：FORMAL，正装； MS，中小样； OTHER，其他。默认值：其他
            if (value == null) {
                return "其他";
            }
            if ("FORMAL".equals(value)) {
                return "正装";
            } else if ("MS".equals(value)) {
                return "中小样";
            } else if ("OTHER".equals(value)) {
                return "其他";
            }
            return "其他";
        });//format转化
        column.setColorIndex(colorIndex);
        info.addExcelColumn("商品条形码", "upc", "cms_bt_jm_promotion_sku", colorIndex);
        info.addExcelColumn("尺码(VO系统)", "cmsSize", "cms_bt_jm_promotion_sku", colorIndex);
        info.addExcelColumn("容量/尺码（聚美系统)", "jmSize", "cms_bt_jm_promotion_sku", colorIndex);
        info.addExcelColumn("海外官网价格", "msrpUsd", "cms_bt_jm_promotion_sku", colorIndex);
        info.addExcelColumn("中国官网价格", "msrpRmb", "cms_bt_jm_promotion_sku", colorIndex);
        info.addExcelColumn("中国指导售价", "retailPrice", "cms_bt_jm_promotion_sku", colorIndex);
        info.addExcelColumn("中国最终售价", "salePrice", "cms_bt_jm_promotion_sku", colorIndex);
        info.addExcelColumn("团购价格", "dealPrice", "cms_bt_jm_promotion_sku");
        info.addExcelColumn("市场价格", "marketPrice", "cms_bt_jm_promotion_sku");
        info.addExcelColumn("聚美HID", "jmHashId", "cms_bt_jm_product");
        info.addExcelColumn("聚美MallId", "jumeiMallId", "cms_bt_jm_product");
        info.addExcelColumn("聚美SKU", "jmSkuNo", "cms_bt_jm_sku");
        info.addExcelColumn("活动期间销量", "saleQty", "cms_bt_jm_sku");
        if (isErrorColumn) {
            info.addExcelColumn(info.getErrorColumn());
        }
        return info;
    }

    public void insert(CmsBtJmPromotionExportTaskModel model) {
        model.setErrorMsg("");
        model.setBeginTime(DateTimeUtil.getCreatedDefaultDate());
        model.setEndTime(DateTimeUtil.getCreatedDefaultDate());
        model.setErrorCode(0);
        model.setIsExport(false);
        model.setSuccessRows(0);
        model.setFileName("");
        model.setFilePath("");
        dao.insert(model);
    }

    /**
     *
     * @param jmPromotionExportMQMessageBody
     */
    public void sendMessage(JmPromotionExportMQMessageBody jmPromotionExportMQMessageBody) {
        cmsMqSenderService.sendMessage(jmPromotionExportMQMessageBody);
    }

    private List<String> getSelCodes(Parameter parameter){
        if(!ListUtils.isNull(parameter.getSelCodeList())){
            return parameter.getSelCodeList();
        }
        if(parameter.getSearchInfo() != null){
            List<MapModel> mapModels = cmsBtJmPromotionProductService.getByWhere(parameter.getSearchInfo());
            return mapModels.stream().map(mapModel -> (String)mapModel.get("productCode")).collect(Collectors.toList());
        }
        return null;
    }

    public static class Parameter{
        List<String> selCodeList;
        Map<String, Object> searchInfo;

        public List<String> getSelCodeList() {
            return selCodeList;
        }

        public void setSelCodeList(List<String> selCodeList) {
            this.selCodeList = selCodeList;
        }

        public Map<String, Object> getSearchInfo() {
            return searchInfo;
        }

        public void setSearchInfo(Map<String, Object> searchInfo) {
            this.searchInfo = searchInfo;
        }
    }

    // 计算活动期间的sku的销量
    public Map getPromotionSale(Integer jmPromotionId){
        CmsBtJmPromotionModel cmsBtJmPromotionModel = cmsBtJmPromotion3Service.get(jmPromotionId);
        Map<String, Integer>saleMap = new HashMap<>();
        try {
            String startDate = DateTimeUtil.format(cmsBtJmPromotionModel.getActivityStart(), "yyyy-MM-dd");
            String endDate = DateTimeUtil.format(cmsBtJmPromotionModel.getActivityEnd(), "yyyy-MM-dd");
            JongoQuery queryObject = new JongoQuery();
            Criteria criteria = new Criteria("cart_id").is(27).and("channel_id").is(cmsBtJmPromotionModel.getChannelId()).and("date").gte(startDate).lte(endDate);
            queryObject.setQuery(criteria);
            if (cmsBtJmPromotionModel != null) {
                List<CmsMtProdSalesHisModel> cmsMtProdSalesHisModels = cmsMtProdSalesHisDao.select(queryObject);
                if (ListUtils.notNull(cmsMtProdSalesHisModels)) {
                    for (CmsMtProdSalesHisModel item : cmsMtProdSalesHisModels) {
                        if (saleMap.containsKey(item.getSku())) {
                            saleMap.put(item.getSku(), saleMap.get(item.getSku()) + item.getQty());
                        } else {
                            saleMap.put(item.getSku(), item.getQty());
                        }
                    }
                }
            }
        }catch (Exception e){
            $error(e.getMessage());
        }
        return saleMap;
    }
}

