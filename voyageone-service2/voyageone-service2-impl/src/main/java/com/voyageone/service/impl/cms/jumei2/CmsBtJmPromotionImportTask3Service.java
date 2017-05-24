package com.voyageone.service.impl.cms.jumei2;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.idsnowflake.FactoryIdWorker;
import com.voyageone.common.util.BeanUtils;
import com.voyageone.common.util.BigDecimalUtil;
import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.common.util.excel.ExcelColumn;
import com.voyageone.common.util.excel.ExcelImportUtil;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.bean.cms.jumei.ProductImportBean;
import com.voyageone.service.bean.cms.jumei.ProductSaveInfo;
import com.voyageone.service.bean.cms.jumei.SkuImportBean;
import com.voyageone.service.dao.cms.*;
import com.voyageone.service.daoext.cms.*;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CmsBtBrandBlockService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.JmPromotionImportMQMessageBody;
import com.voyageone.service.model.cms.*;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotionImportTask3Service extends BaseService {
    @Autowired
    CmsBtJmPromotionImportTaskDao cmsBtJmPromotionImportTaskDao;
    @Autowired
    CmsBtJmPromotionImportTaskDaoExt cmsBtJmPromotionImportTaskDaoExt;
    @Autowired
    CmsBtJmPromotionDao daoCmsBtJmPromotion;
    @Autowired
    CmsBtJmPromotionProductDaoExt daoExtCmsBtJmPromotionProduct;
    @Autowired
    CmsBtTagDaoExt daoExtCmsBtTag;
    @Autowired
    CmsBtJmPromotionProductDao daoCmsBtJmPromotionProduct;
    @Autowired
    CmsBtJmPromotionTagProductDao daoCmsBtJmPromotionTagProduct;
    @Autowired
    CmsBtJmPromotionSkuDao daoCmsBtJmPromotionSku;
    @Autowired
    CmsBtJmPromotionSkuDaoExt daoExtCmsBtJmPromotionSku;
    @Autowired
    CmsBtJmProductDaoExt daoExtCmsBtJmProduct;
    @Autowired
    CmsBtJmSkuDaoExt daoExtCmsBtJmSkuDao;
    @Autowired
    CmsBtJmPromotionExportTask3Service serviceCmsBtJmPromotionExportTask3Service;
    @Autowired
    CmsBtPromotionCodesDao daoCmsBtPromotionCodes;
    @Autowired
    CmsBtPromotionDao daoCmsBtPromotion;
    @Autowired
    CmsBtJmPromotionImportSave3Service serviceCmsBtJmPromotionImportSave3;
    @Autowired
    CmsBtJmPromotionDaoExt  cmsBtJmPromotionDaoExt;
    @Autowired
    FeedInfoService feedInfoService;
    @Autowired
    CmsBtBrandBlockService cmsBtBrandBlockService;
    @Autowired
    TransactionRunner transactionRunner;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private  CmsBtPromotionGroupsDao daoCmsBtPromotionGroups;
    @Autowired
    private  CmsBtPromotionSkusDao daoCmsBtPromotionSkus;
    @Autowired
    CmsMqSenderService cmsMqSenderService;
    public void importFile(int JmBtPromotionImportTaskId, String importPath) {

        CmsBtJmPromotionImportTaskModel modelCmsBtJmPromotionImportTask = cmsBtJmPromotionImportTaskDao.select(JmBtPromotionImportTaskId);
        modelCmsBtJmPromotionImportTask.setBeginTime(DateTimeUtilBeijing.getCurrentBeiJingDate());
        try {
            cmsBtJmPromotionImportTaskDao.update(modelCmsBtJmPromotionImportTask);
            CallResult result= importExcel(modelCmsBtJmPromotionImportTask, importPath);
            if(!result.isResult())
            {
                modelCmsBtJmPromotionImportTask.setErrorMsg(result.getMsg());
                modelCmsBtJmPromotionImportTask.setErrorCode(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            long requestId=FactoryIdWorker.nextId();
            $error("CmsBtJmPromotionImportTask3Service.importFile"+requestId,"导入失败",ex);
            modelCmsBtJmPromotionImportTask.setErrorCode(1);
            modelCmsBtJmPromotionImportTask.setErrorMsg("导出失败请联系管理员！requestId:"+requestId);
            if (ex.getStackTrace().length > 0) {
                modelCmsBtJmPromotionImportTask.setErrorMsg(modelCmsBtJmPromotionImportTask.getErrorMsg() + ex.getStackTrace()[0].toString());
            }
        }
        modelCmsBtJmPromotionImportTask.setIsImport(true);
        modelCmsBtJmPromotionImportTask.setEndTime(DateTimeUtilBeijing.getCurrentBeiJingDate());
        cmsBtJmPromotionImportTaskDao.update(modelCmsBtJmPromotionImportTask);
    }

    private CallResult importExcel(CmsBtJmPromotionImportTaskModel modelCmsBtJmPromotionImportTask, String importPath) throws Exception {
        CallResult result=new CallResult();
        CmsBtJmPromotionModel modelCmsBtJmPromotion = daoCmsBtJmPromotion.select(modelCmsBtJmPromotionImportTask.getCmsBtJmPromotionId());
        modelCmsBtJmPromotionImportTask.setBeginTime(DateTimeUtilBeijing.getCurrentBeiJingDate());
        //"/usr/JMImport/"
        String filePath = importPath + "/" + modelCmsBtJmPromotionImportTask.getFileName().trim();//"/Product20160324164706.xls";
        File excelFile = new File(filePath);
        InputStream fileInputStream = null;
        Workbook book = null;
        fileInputStream = new FileInputStream(excelFile);
        if (modelCmsBtJmPromotionImportTask.getFileName().indexOf(".xlsx") > 0) {
            book = new XSSFWorkbook(fileInputStream);
        } else if (modelCmsBtJmPromotionImportTask.getFileName().indexOf(".xls") > 0) {
            book = new HSSFWorkbook(fileInputStream);
        } else {
            result.setResult(false);
            result.setMsg("导入文件格式不对");
            return  result;
            // throw new Exception("导入文件格式不对");
        }
        //读取product
        Sheet productSheet = book.getSheet("Product");
        if (productSheet == null) {
            result.setResult(false);
            result.setMsg("导入模板格式不对,请检查");
            return  result;
            // throw new Exception("导入模板不对,请检查");
        }
        List<ProductImportBean> listProductImport = new ArrayList<>();//导入的集合
        List<Map<String, Object>> listProducctErrorMap = new ArrayList<>();//错误行集合  导出错误文件
        List<ExcelColumn> listProductColumn = getProductImportColumn();//配置列信息
        ExcelImportUtil.importSheet(productSheet, listProductColumn, listProductImport, listProducctErrorMap, ProductImportBean.class);

        //读取sku
        Sheet skuSheet = book.getSheet("Sku");
        if (skuSheet == null) {
            result.setResult(false);
            result.setMsg("导入模板格式不对,请检查");
            return  result;
        }
        List<SkuImportBean> listSkuImport = new ArrayList<>();
        List<Map<String, Object>> listSkuErrorMap = new ArrayList<>();
        List<ExcelColumn> listSkuColumn = getSkuImportColumn();
        ExcelImportUtil.importSheet(skuSheet, listSkuColumn, listSkuImport, listSkuErrorMap, SkuImportBean.class);
        //save
        saveImport(modelCmsBtJmPromotion, listProductImport, listSkuImport,listProducctErrorMap, listSkuErrorMap,modelCmsBtJmPromotionImportTask.getCreater(),true);
        //导出未通过check的记录
        if (listProducctErrorMap.size() > 0 | listSkuErrorMap.size() > 0) {
            String failuresFileName = "error" + modelCmsBtJmPromotionImportTask.getFileName().trim();
            String errorfilePath = importPath + "/error" + modelCmsBtJmPromotionImportTask.getFileName().trim();
            serviceCmsBtJmPromotionExportTask3Service.export(errorfilePath, listProducctErrorMap, listSkuErrorMap, true);
            modelCmsBtJmPromotionImportTask.setFailuresFileName(failuresFileName);
            modelCmsBtJmPromotionImportTask.setErrorCode(2);
            modelCmsBtJmPromotionImportTask.setFailuresRows(listSkuErrorMap.size());
        }
        if (listProductImport.size() == 0) {
            modelCmsBtJmPromotionImportTask.setErrorMsg("没有导入的商品");
        }
        modelCmsBtJmPromotionImportTask.setSuccessRows(listSkuImport.size());
        return  result;
    }

    //check
    public void check(CmsBtJmPromotionModel model, List<ProductImportBean> listProductModel, List<SkuImportBean> listSkuModel, List<Map<String, Object>> listProducctErrorMap, List<Map<String, Object>> listSkuErrorMap,boolean isImport) throws IllegalAccessException {
        //product
        List<ProductImportBean> listErroProduct = new ArrayList<>();

        for (ProductImportBean product : listProductModel) {
            if (model.getIsPromotionFullMinus())//满减专场
            {
                CmsBtJmPromotionProductModel modelPromotionProduct = daoExtCmsBtJmPromotionProduct.selectDateRepeatByCode(model.getId(), model.getChannelId(), product.getProductCode(), model.getActivityStart(), model.getActivityEnd());
                if (modelPromotionProduct != null) { //活动日期重叠
                    product.setErrorMsg("该商品已于相关时间段内，在其它专场中完成上传，为避免财务结算问题，请放弃导入,JmPromotionId:" + modelPromotionProduct.getCmsBtJmPromotionId() + "存在该商品");//取一个活动id
                    listErroProduct.add(product);
                    continue;
                }
            } else {
                CmsBtJmPromotionProductModel modelPromotionProduct = daoExtCmsBtJmPromotionProduct.selectFullMinusDateRepeat(model.getId(), model.getChannelId(), product.getProductCode(), model.getActivityStart(), model.getActivityEnd());
                if (modelPromotionProduct != null) { //活动日期重叠
                    product.setErrorMsg("该商品已在该大促时间范围内的其它未过期聚美专场中，完成上传，且开始时间与大促开始时间不一致。无法加入当前大促专场。聚美会监控大促专场的营销数据，禁止商品在活动启动前偷跑，大促商品必须有预热。请放弃导入,JmPromotionId:" + modelPromotionProduct.getCmsBtJmPromotionId() + "存在该商品");//取一个活动id
                    listErroProduct.add(product);
                    continue;
                }
            }
//            if (com.voyageone.common.util.StringUtils.isEmpty("") && model.getPromotionType() == 2)//大促专场
//            {
//                CmsBtJmPromotionProductModel modelPromotionProduct = daoExtCmsBtJmPromotionProduct.selectDateRepeatByCode(model.getId(), model.getChannelId(), product.getProductCode(), model.getActivityStart(), model.getActivityEnd());
//                if (modelPromotionProduct != null && modelPromotionProduct.getActivityStart() != model.getActivityStart()) { //活动日期重叠 开始时间不相等
//                    product.setErrorMsg("该商品已于相关时间段内，在其它专场中完成上传，为避免财务结算问题，请放弃导入,JmPromotionId:" + modelPromotionProduct.getCmsBtJmPromotionId() + "存在该商品");//取一个活动id
//                    listErroProduct.add(product);
//                    continue;
//                }
//            }
            /*DOC-159-1:[聚美活动添加逻辑]如果未上新的产品，无条件加入到聚美活动
            if (daoExtCmsBtJmProduct.existsCode(product.getProductCode(), model.getChannelId()) != Boolean.TRUE) {
                product.setErrorMsg("code:" + product.getProductCode() + "从未上新或不存在");
                listErroProduct.add(product);
                continue;
            }
           */
        }
        if (isImport) {
            listProductModel.removeAll(listErroProduct);//移除不能导入的 product
        }
        listProducctErrorMap.addAll(BeanUtils.toMapList(listErroProduct));//返回  导出
        //sku
        String errorSkuMsg = "";
        List<SkuImportBean> listErroSku = new ArrayList<>();
        for (SkuImportBean sku : listSkuModel) {


            /*DOC-159-1:[聚美活动添加逻辑]如果未上新的产品，无条件加入到聚美活动
            if (daoExtCmsBtJmSkuDao.existsCode(sku.getSkuCode(), sku.getProductCode(), model.getChannelId()) != Boolean.TRUE) {
                sku.setErrorMsg("skuCode:" + sku.getSkuCode() + "从未上新或不存在");
                if (isImport) {
                    listErroSku.add(sku);
                }
            } else
             */
             if (sku.getDealPrice() > sku.getMarketPrice()) {
                sku.setErrorMsg("skuCode:" + sku.getSkuCode() + "请重新确认价格，市场价必须大于团购价！");
                if (isImport) {
                    listErroSku.add(sku);
                }
            }
            if (!com.voyageone.common.util.StringUtils.isEmpty(sku.getErrorMsg())) {
                errorSkuMsg += sku.getErrorMsg();
            }
        }
        if (isImport) {
            listSkuModel.removeAll(listErroSku);
        }
        listSkuErrorMap.addAll(BeanUtils.toMapList(listErroSku));//返回  导出
    }

    //save
    public void saveImport(CmsBtJmPromotionModel model, List<ProductImportBean> listProductImport, List<SkuImportBean> listSkuImport, List<Map<String, Object>> listProducctErrorMap, List<Map<String, Object>> listSkuErrorMap,String userName,boolean isImportExcel) throws IllegalAccessException {
        //check
        check(model, listProductImport, listSkuImport, listProducctErrorMap, listSkuErrorMap,isImportExcel);//check  if isImportExcel==true  移除不能导入的product
        List<ProductSaveInfo> listSaveInfo = new ArrayList<>();
        HashMap<String,Boolean> mapMasterBrand=new HashMap<>();
        CmsBtPromotionModel modelPromotion=getCmsBtPromotionModel(model.getId());
        //初始化
        ProductSaveInfo saveInfo = null;
        $info("初始化开始");
        for (ProductImportBean product : listProductImport) {
            $info("into" + product.getProductCode());
            saveInfo = new ProductSaveInfo();
            List<SkuImportBean> listProductSkuImport = getListSkuImportBeanByProductCode(listSkuImport, product.getProductCode());//获取商品的sku

            saveInfo.productInfo = productService.getProductByCode(modelPromotion.getChannelId(), product.getProductCode());
            if (saveInfo.productInfo == null) {
                product.setErrorMsg("不存在" + product.getProductCode());
                listProducctErrorMap.add(BeanUtils.toMap(product));
                listSkuErrorMap.addAll(BeanUtils.toMapList(listProductSkuImport));
                listSkuImport.remove(listProductSkuImport);
                continue;
            }
            if(isBlocked(saveInfo.productInfo,mapMasterBrand)) {
                product.setErrorMsg("该商品品牌已加入黑名单,不能导入" + product.getProductCode());
                listProducctErrorMap.add(BeanUtils.toMap(product));
                listSkuErrorMap.addAll(BeanUtils.toMapList(listProductSkuImport));
                listSkuImport.remove(listProductSkuImport);
                continue;
            }
            saveInfo.p_Platform_Cart = saveInfo.productInfo.getPlatform(CartEnums.Cart.JM);

            loadSaveInfo(saveInfo, model, listProductSkuImport, product, listProducctErrorMap, listSkuErrorMap, userName);
            loadCmsBtPromotionCodes(saveInfo, listProductSkuImport, product, modelPromotion, userName);
            if (saveInfo != null) {

                if (saveInfo._listSkuImportError.size() > 0) {
                    listSkuErrorMap.addAll(BeanUtils.toMapList(saveInfo._listSkuImportError));//初始化失败的sku
                    listSkuImport.remove(saveInfo._listSkuImportError);
                }

                if (saveInfo.jmSkuList.size() > 0) {
                    listSaveInfo.add(saveInfo);
                }

            }
        }
        $info("初始化结束");
        //保存
        $info("保存开始");
        for (ProductSaveInfo info : listSaveInfo) {
            try {
                $info("into"+ info.jmProductModel.getProductCode());
                serviceCmsBtJmPromotionImportSave3.saveProductSaveInfo(info);
            } catch (Exception ex) {
                long requestId = FactoryIdWorker.nextId();
                $error("serviceCmsBtJmPromotionImportSave3.saveProductSaveInfo" + requestId, ex);
                info._importProduct.setErrorMsg("导入失败,请联系管理员" + requestId);
                listProducctErrorMap.add(BeanUtils.toMap(info._importProduct));
            }
        }
        cmsBtJmPromotionDaoExt.updateSumbrandById(model.getId());//汇总品牌
        $info("保存结束");
    }
    public boolean isBlocked(CmsBtProductModel p_ProductInfo, HashMap<String,Boolean> mapMasterBrand) {
        String errorMsg = "";
        String platformBrandId = p_ProductInfo.getPlatform(27).getpBrandId();
        String masterBrand = p_ProductInfo.getCommon().getFields().getBrand();
        if (!mapMasterBrand.containsKey(masterBrand)) {
            CmsBtFeedInfoModel cmsBtFeedInfoModel = feedInfoService.getProductByCode(p_ProductInfo.getChannelId(), p_ProductInfo.getCommon().getFields().getCode());
            String feedBrand="";
            if(cmsBtFeedInfoModel!=null) {
                feedBrand= cmsBtFeedInfoModel.getBrand();
             }
            if (cmsBtBrandBlockService.isBlocked(p_ProductInfo.getChannelId(), 27, feedBrand, masterBrand, platformBrandId)) {
                mapMasterBrand.put(masterBrand, true);
            } else {
                mapMasterBrand.put(masterBrand, false);
            }
        }
        return mapMasterBrand.get(masterBrand);
    }
    public CmsBtPromotionModel  getCmsBtPromotionModel(int jmPromotionId)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("promotionId",jmPromotionId);
        map.put("cartId", CartEnums.Cart.JM.getValue());
        CmsBtPromotionModel promotion = daoCmsBtPromotion.selectOne(map);
        return  promotion;
    }
    private void loadSaveInfo(ProductSaveInfo saveInfo,CmsBtJmPromotionModel model, List<SkuImportBean> listProductSkuImport, ProductImportBean product, List<Map<String, Object>> listProducctErrorMap, List<Map<String, Object>> listSkuErrorMap,String userName) throws IllegalAccessException {
        //ProductSaveInfo saveInfo = new ProductSaveInfo();
        //  List<SkuImportBean> listProductSkuImport = getListSkuImportBeanByProductCode(listSkuImport, product.getProductCode());//获取商品的sku
        saveInfo.jmProductModel = daoExtCmsBtJmPromotionProduct.selectByProductCode(product.getProductCode(), model.getChannelId(), model.getId());
        if (saveInfo.jmProductModel == null) {
            saveInfo.jmProductModel = new CmsBtJmPromotionProductModel();
            saveInfo.jmProductModel.setId(0);
            saveInfo.jmProductModel.setCreater(userName);
            saveInfo.jmProductModel.setCreated(new Date());
            saveInfo.jmProductModel.setJmHashId("");
            if (!com.voyageone.common.util.StringUtils.isEmpty(product.getErrorMsg())) {
                saveInfo.jmProductModel.setErrorMsg(product.getErrorMsg());
            } else {
                saveInfo.jmProductModel.setErrorMsg("");
            }
            saveInfo.jmProductModel.setPriceStatus(0);
            saveInfo.jmProductModel.setDiscount(new BigDecimal(0));
            saveInfo.jmProductModel.setDiscount2(new BigDecimal(0));
            saveInfo.jmProductModel.setSkuCount(0);
            saveInfo.jmProductModel.setQuantity(0);
            saveInfo.jmProductModel.setDealEndTimeStatus(0);
            saveInfo.jmProductModel.setActivityStart(model.getActivityStart());
            saveInfo.jmProductModel.setActivityEnd(model.getActivityEnd());
            saveInfo.jmProductModel.setProductCode(product.getProductCode());
            saveInfo.jmProductModel.setCmsBtJmPromotionId(model.getId());
            saveInfo.jmProductModel.setChannelId(model.getChannelId());
            saveInfo.jmProductModel.setSynchStatus(0);
            saveInfo.jmProductModel.setLimit(product.getLimit());
            saveInfo.jmProductModel.setProductNameEn(saveInfo.productInfo.getCommon().getFields().getProductNameEn());
            if (saveInfo.productInfo.getCommon().getFields().getImages1() != null && saveInfo.productInfo.getCommon().getFields().getImages1().size() > 0) {
                if (saveInfo.productInfo.getCommon().getFields().getImages1().get(0).get("image1") != null) {
                    saveInfo.jmProductModel.setImage1(saveInfo.productInfo.getCommon().getFields().getImages1().get(0).get("image1").toString());
                }
            }
        } else {
            //2016/11/2
//            if (model.getPrePeriodStart().getTime() < DateTimeUtilBeijing.getCurrentBeiJingDate().getTime() && saveInfo.jmProductModel.getSynchStatus() == 2) {
//                product.setErrorMsg("该商品预热已开始,不能导入");
//                listProducctErrorMap.add(BeanUtils.toMap(product));
//                for (SkuImportBean skuImport : listProductSkuImport) {
//                    skuImport.setErrorMsg("预热已开始,不能导入");
//                    listSkuErrorMap.add(BeanUtils.toMap(skuImport));
//                }
//                return;
//            }
        }
        saveInfo.jmProductModel.setAppId(product.getAppId());
        saveInfo.jmProductModel.setPcId(product.getPcId());
        if (saveInfo.jmProductModel.getSynchStatus() == 2) {
            if (product.getLimit() != saveInfo.jmProductModel.getLimit()) {
                saveInfo.jmProductModel.setUpdateStatus(1);//已经变更
            }
        }
        saveInfo.jmProductModel.setLimit(product.getLimit());

        saveInfo.jmProductModel.setPromotionTag(getPromotionTag(product.getPromotionTag(), saveInfo.jmProductModel.getPromotionTag()));

        saveInfo.jmProductModel.setModifier(userName);
        saveInfo.jmProductModel.setModified(new Date());
        if (saveInfo.jmProductModel.getPromotionTag() == null) {
            saveInfo.jmProductModel.setPromotionTag("");
        }
        //初始化CmsBtJmPromotionTagProductModel
        loadSaveTag(product.getPromotionTag(), saveInfo, model);

        //初始化CmsBtJmPromotionSkuModel
        loadSaveSku(saveInfo, listProductSkuImport, userName);
        saveInfo.jmProductModel.setMaxMsrpUsd(new BigDecimal(saveInfo.productInfo.getCommon().getFields().getPriceMsrpEd()));
        saveInfo.jmProductModel.setMinMsrpUsd(new BigDecimal(saveInfo.productInfo.getCommon().getFields().getPriceMsrpSt()));
        saveInfo.jmProductModel.setMaxMsrpRmb(new BigDecimal(saveInfo.p_Platform_Cart.getpPriceMsrpEd()));
        saveInfo.jmProductModel.setMinMsrpRmb(new BigDecimal(saveInfo.p_Platform_Cart.getpPriceMsrpSt()));
        saveInfo.jmProductModel.setMaxRetailPrice(new BigDecimal(saveInfo.p_Platform_Cart.getpPriceRetailEd()));
        saveInfo.jmProductModel.setMinRetailPrice(new BigDecimal(saveInfo.p_Platform_Cart.getpPriceRetailSt()));
        saveInfo.jmProductModel.setMaxSalePrice(new BigDecimal(saveInfo.p_Platform_Cart.getpPriceSaleEd()));
        saveInfo.jmProductModel.setMinSalePrice(new BigDecimal(saveInfo.p_Platform_Cart.getpPriceSaleSt()));
        if (saveInfo.jmSkuList.size() > 0) {
            saveInfo.jmProductModel.setMaxMarketPrice(getMaxMarketPrice(saveInfo.jmSkuList));
            saveInfo.jmProductModel.setMinMarketPrice(getMinMarketPrice(saveInfo.jmSkuList));
            saveInfo.jmProductModel.setMaxDealPrice(getMaxDealPrice(saveInfo.jmSkuList));
            saveInfo.jmProductModel.setMinDealPrice(getMinDealPrice(saveInfo.jmSkuList));
            saveInfo.jmProductModel.setDiscount(saveInfo.jmSkuList.get(0).getDiscount());//折扣
            saveInfo.jmProductModel.setDiscount2(saveInfo.jmSkuList.get(0).getDiscount2());//折扣
            saveInfo.jmProductModel.setSkuCount(saveInfo.jmSkuList.size());
        }
        saveInfo._importProduct = product;
    }
    public  String getPromotionTag(String newPromotionTag,String oldPromotionTag) {
        HashSet<String> hs = new HashSet<>();
        if (!StringUtils.isEmpty(newPromotionTag)) {
            String[] newTagList = newPromotionTag.split("\\|");
            for (String s : newTagList) {
                if (!StringUtils.isEmpty(s)) {
                    hs.add(s);
                }
            }
        }

        if (!StringUtils.isEmpty(oldPromotionTag)) {

            String[] oldTagList = oldPromotionTag.split("\\|");
            for (String o : oldTagList) {
                if (!StringUtils.isEmpty(o)) {
                    hs.add(o);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        hs.stream().forEach(f -> {
            sb.append("|").append(f);
        });
        if (sb.length() > 0) {
            return sb.substring(1);
        }
        return "";
    }
    public BigDecimal getMaxMarketPrice(List<CmsBtJmPromotionSkuModel> skuList)
    {
        return   skuList.stream().max((m1,m2)->{return m1.getMarketPrice().doubleValue()>m2.getMarketPrice().doubleValue()?1:-1;}).get().getMarketPrice();
    }
    public BigDecimal getMinMarketPrice(List<CmsBtJmPromotionSkuModel> skuList)
    {
        return   skuList.stream().min((m1,m2)->{return m1.getMarketPrice().doubleValue()>m2.getMarketPrice().doubleValue()?1:-1;}).get().getMarketPrice();
    }
    public BigDecimal getMaxDealPrice(List<CmsBtJmPromotionSkuModel> skuList)
    {
        return   skuList.stream().max((m1,m2)->{return m1.getDealPrice().doubleValue()>m2.getDealPrice().doubleValue()?1:-1;}).get().getDealPrice();
    }
    public BigDecimal getMinDealPrice(List<CmsBtJmPromotionSkuModel> skuList)
    {
        return   skuList.stream().min((m1,m2)->{return m1.getDealPrice().doubleValue()>m2.getDealPrice().doubleValue()?1:-1;}).get().getDealPrice();
    }
    private void loadCmsBtPromotionCodes(ProductSaveInfo saveInfo, List<SkuImportBean> listSkuImport, ProductImportBean product, CmsBtPromotionModel modelPromotion,String userName) {

        // 获取Product信息 mongo
        JongoQuery query = new JongoQuery();
        CmsBtProductModel productInfo = saveInfo.productInfo;// productService.getProductByCode(modelPromotion.getChannelId(), product.getProductCode());
        query.setQuery("{\"productCodes\":\"" + product.getProductCode() + "\",\"cartId\":" + CartEnums.Cart.JM.getValue() + "}");
        CmsBtProductGroupModel groupModel = productGroupService.getProductGroupByQuery(modelPromotion.getChannelId(), query);
        if (productInfo == null) return;
        //1.CmsBtPromotionCodesModel
        CmsBtPromotionCodesModel modelCodes = getByCmsBtPromotionCodesModel(product.getProductCode(), modelPromotion.getId());
        if (modelCodes == null) {
            modelCodes = new CmsBtPromotionCodesModel();
            modelCodes.setId(0);
            modelCodes.setCreater(userName);
            modelCodes.setCreated(new Date());
            modelCodes.setCatPath(productInfo.getCommon().getCatPath());
            modelCodes.setProductModel(productInfo.getCommon().getFields().getModel());
            modelCodes.setPromotionId(modelPromotion.getId());
            modelCodes.setOrgChannelId(productInfo.getOrgChannelId());
            if (groupModel != null) {
                modelCodes.setNumIid(groupModel.getNumIId());
                modelCodes.setModelId(groupModel.getGroupId().intValue());
            }
            modelCodes.setProductId(Integer.valueOf(productInfo.getProdId().toString()));
            modelCodes.setProductCode(productInfo.getCommon().getFields().getCode());
            //modelCodes.setProductName(com.taobao.api.internal.util.StringUtils.isEmpty(productInfo.getCommon().getFields().get.getFields().getLongTitle()) ? productInfo.getFields().getProductNameEn() : productInfo.getFields().getLongTitle());
            CmsBtProductModel_Platform_Cart ptfObj = productInfo.getPlatform(CartEnums.Cart.JM.getValue());
            if (ptfObj != null && ptfObj.getSkus() != null && ptfObj.getSkus().size() > 0) {
                modelCodes.setSalePrice(ptfObj.getSkus().get(0).getDoubleAttribute("priceSale"));
                modelCodes.setRetailPrice(ptfObj.getSkus().get(0).getDoubleAttribute("priceRetail"));
                modelCodes.setMsrp(ptfObj.getSkus().get(0).getDoubleAttribute("priceMsrp"));
                if (listSkuImport != null && listSkuImport.size() > 0) {
                    modelCodes.setPromotionPrice(listSkuImport.get(0).getDealPrice());
                }
                modelCodes.setModifier(userName);
            }
            saveInfo.codesModel = modelCodes;
        }

        //2.CmsBtPromotionGroupsModel
        CmsBtPromotionGroupsModel modelGroups = getCmsBtPromotionGroupsModel(modelPromotion.getId(), modelPromotion.getChannelId(), productInfo.getCommon().getFields().getModel());
        if (modelGroups == null) {
            modelGroups = new CmsBtPromotionGroupsModel();
            modelGroups.setCatPath(productInfo.getCommon().getCatPath());
            modelGroups.setProductModel(productInfo.getCommon().getFields().getModel());
            modelGroups.setSynFlg("0");
            modelGroups.setPromotionId(modelPromotion.getId());
            modelGroups.setOrgChannelId(productInfo.getOrgChannelId());
            modelGroups.setCreater(userName);
            modelGroups.setModifier(userName);
            if (groupModel != null) {
                modelGroups.setNumIid(groupModel.getNumIId());
                modelGroups.setModelId(groupModel.getGroupId().intValue());
            } else {
                modelGroups.setNumIid("");
                modelGroups.setModelId(0);
            }
            saveInfo.groupsModel = modelGroups;
        }
        List<BaseMongoMap<String, Object>> listSkuMongo = saveInfo.p_Platform_Cart.getSkus();


        //3.CmsBtPromotionSkusModel
        for (SkuImportBean skuImport : listSkuImport) {
            CmsBtPromotionSkusModel skusModel = getCmsBtPromotionSkusModel(modelPromotion.getId(), skuImport.getProductCode(), skuImport.getSkuCode());


            if (skusModel == null) {
                CmsBtProductModel_Sku cmsBtProductModel_sku = saveInfo.productInfo.getCommon().getSku(skuImport.getSkuCode());
                BaseMongoMap<String, Object> mapSkuPlatform = getJMPlatformSkuMongo(listSkuMongo, skuImport.getSkuCode());

                skusModel = new CmsBtPromotionSkusModel();
                skusModel.setProductId(Integer.valueOf(productInfo.getProdId().toString()));
                skusModel.setProductCode(skuImport.getProductCode());
                skusModel.setProductSku(skuImport.getSkuCode());
                skusModel.setQty(0);
                skusModel.setCatPath(productInfo.getCommon().getCatPath());
                skusModel.setProductModel(productInfo.getCommon().getFields().getModel());
                skusModel.setSynFlg("0");
                skusModel.setPromotionId(modelPromotion.getId());
                skusModel.setOrgChannelId(productInfo.getOrgChannelId());
                skusModel.setCreater(userName);
                skusModel.setModifier(userName);

                if (groupModel != null) {
                    skusModel.setNumIid(groupModel.getNumIId());
                    skusModel.setModelId(groupModel.getGroupId().intValue());
                }
                if (mapSkuPlatform != null) {
                    Double priceMsrp = mapSkuPlatform.getDoubleAttribute("priceMsrp");
                    Double priceRetail = mapSkuPlatform.getDoubleAttribute("priceRetail");
                    Double priceSale = mapSkuPlatform.getDoubleAttribute("priceSale");
                    skusModel.setMsrpRmb(new BigDecimal(priceMsrp));
                    skusModel.setRetailPrice(new BigDecimal(priceRetail));
                    skusModel.setSalePrice(new BigDecimal(priceSale));
                }
                if (cmsBtProductModel_sku != null) {
                    skusModel.setMsrpUsd(new BigDecimal(cmsBtProductModel_sku.getClientMsrpPrice()));
                }
                skusModel.setPromotionPrice(new BigDecimal(skuImport.getDealPrice()));
                saveInfo.skusModels.add(skusModel);
            }
        }
    }
    CmsBtPromotionSkusModel getCmsBtPromotionSkusModel(int promotionId,String productCode,String skuCode)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("promotionId", promotionId);
//        map.put("orgChannelId", channelId);
        map.put("productCode", productCode);
        map.put("productSku", skuCode);
        return daoCmsBtPromotionSkus.selectOne(map);
    }
    CmsBtPromotionGroupsModel getCmsBtPromotionGroupsModel(int promotionId,String channelId,String productModel)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("promotionId", promotionId);
        map.put("productModel", productModel);
//        map.put("orgChannelId", channelId);
        return daoCmsBtPromotionGroups.selectOne(map);
    }
    CmsBtPromotionCodesModel getByCmsBtPromotionCodesModel(String productCode,int promotionId) {
        Map<String, Object> map = new HashMap<>();
        map.put("promotionId", promotionId);
        map.put("productCode", productCode);
        return daoCmsBtPromotionCodes.selectOne(map);
    }
    private void loadSaveSku(ProductSaveInfo saveInfo, List<SkuImportBean> listImport,String userName) {
        CmsBtJmPromotionSkuModel skuModel = null;

        List<BaseMongoMap<String, Object>> listSkuMongo = saveInfo.p_Platform_Cart.getSkus();
        for (SkuImportBean skuImportBean : listImport) {
            BaseMongoMap<String, Object> mapSkuPlatform = getJMPlatformSkuMongo(listSkuMongo, skuImportBean.getSkuCode());
            if (mapSkuPlatform == null) {
                skuImportBean.setErrorMsg("skuCode:" + skuImportBean.getSkuCode() + "jmPlatform未上新");
                saveInfo._listSkuImportError.add(skuImportBean);
                continue;
            }
            CmsBtProductModel_Sku cmsBtProductModel_sku = saveInfo.productInfo.getCommon().getSku(skuImportBean.getSkuCode());
            if (cmsBtProductModel_sku == null) {
                skuImportBean.setErrorMsg("skuCode:" + skuImportBean.getSkuCode() + " Common().getSku不存在");
                saveInfo._listSkuImportError.add(skuImportBean);
                continue;
            }
            if (saveInfo.jmProductModel.getId() != null && saveInfo.jmProductModel.getId() > 0) {
                skuModel = daoExtCmsBtJmPromotionSku.selectBySkuCode(skuImportBean.getSkuCode(), saveInfo.jmProductModel.getId(),saveInfo.jmProductModel.getCmsBtJmPromotionId());
            }
            if (skuModel == null) {
                skuModel = new CmsBtJmPromotionSkuModel();
                skuModel.setSynchStatus(0);
                skuModel.setUpdateState(0);
                skuModel.setDealPrice(new BigDecimal(0));
                skuModel.setMarketPrice(new BigDecimal(0));
                skuModel.setCmsBtJmPromotionId(saveInfo.jmProductModel.getCmsBtJmPromotionId());
                skuModel.setChannelId(saveInfo.jmProductModel.getChannelId());
                skuModel.setSkuCode(skuImportBean.getSkuCode());
                skuModel.setCreated(new Date());
                skuModel.setCreater(userName);
                skuModel.setProductCode(skuImportBean.getProductCode());
                skuModel.setErrorMsg("");
                if (saveInfo.jmProductModel.getSynchStatus() == 2) {
                    skuModel.setUpdateState(1);//已变更
                    saveInfo.jmProductModel.setUpdateStatus(1);//已变更     新增了一个sku
                }
            }
            if (saveInfo.jmProductModel.getSynchStatus() == 2) {
                if (skuModel.getDealPrice().doubleValue() != skuImportBean.getDealPrice()) {
                    skuModel.setUpdateState(1);//已变更
                    saveInfo.jmProductModel.setUpdateStatus(1);//已变更
                }
                if (skuModel.getMarketPrice().doubleValue() != skuImportBean.getMarketPrice()) {
                    skuModel.setUpdateState(1);//已变更
                    saveInfo.jmProductModel.setUpdateStatus(1);//已变更
                }
            }
            Double priceMsrp = mapSkuPlatform.getDoubleAttribute("priceMsrp");
            Double priceRetail = mapSkuPlatform.getDoubleAttribute("priceRetail");
            Double priceSale = mapSkuPlatform.getDoubleAttribute("priceSale");
            skuModel.setMsrpRmb(new BigDecimal(priceMsrp));
            skuModel.setRetailPrice(new BigDecimal(priceRetail));
            skuModel.setSalePrice(new BigDecimal(priceSale));
            skuModel.setMsrpUsd(new BigDecimal(cmsBtProductModel_sku.getClientMsrpPrice()));

            skuModel.setDealPrice(new BigDecimal(skuImportBean.getDealPrice()));
            skuModel.setMarketPrice(new BigDecimal(skuImportBean.getMarketPrice()));
            skuModel.setDiscount(BigDecimalUtil.divide(skuModel.getDealPrice(), skuModel.getMarketPrice(), 2));//折扣
            skuModel.setDiscount2(BigDecimalUtil.divide(skuModel.getDealPrice(), skuModel.getSalePrice(), 2));//折扣
            skuModel.setModified(new Date());
            skuModel.setModifier(userName);
            saveInfo.jmSkuList.add(skuModel);
            skuModel = null;
        }
    }
    private BaseMongoMap<String, Object>  getJMPlatformSkuMongo(List<BaseMongoMap<String, Object>> list,String skuCode)
    {
        for(BaseMongoMap<String, Object> map:list)
        {
            if(skuCode.equalsIgnoreCase(map.getStringAttribute("skuCode")))
            {
                return  map;
            }
        }
        return null;
    }
    private List<SkuImportBean> getListSkuImportBeanByProductCode(List<SkuImportBean> listSkuImport, String productCode) {
        return listSkuImport.stream().filter(skuImportBean -> skuImportBean.getProductCode().equals(productCode)).collect(Collectors.toList());
//        List<SkuImportBean> listResult = new ArrayList<>();
//        for (SkuImportBean sku : listSkuImport) {
//            if (sku.getProductCode().equals(productCode)) {
//                listResult.add(sku);
//            }
//        }
//        return listResult;
    }

    private void loadSaveTag(String promotionTag, ProductSaveInfo saveInfo, CmsBtJmPromotionModel model) {

        if (StringUtils.isEmpty(promotionTag)) {
            return;
        }
        CmsBtJmPromotionTagProductModel tagProductModel = null;
        String[] tagList = promotionTag.split("\\|");
        //获取该活动的所有tag
        List<CmsBtTagModel> listCmsBtTag = daoExtCmsBtTag.selectListByParentTagId(model.getRefTagId());

        for (String tagName : tagList) {
            for (CmsBtTagModel tagModel : listCmsBtTag) {
                if (tagModel.getTagName().equals(tagName)) {
                    if (saveInfo.jmProductModel.getId() > 0) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("cmsBtJmPromotionProductId", saveInfo.jmProductModel.getId());//cms_bt_jm_promotion_product_id cms_bt_tag_id
                        map.put("cmsBtTagId", tagModel.getId());
                        tagProductModel = daoCmsBtJmPromotionTagProduct.selectOne(map);
                    }
                    if (tagProductModel == null) {
                        //不存在 添加tag
                        tagProductModel = new CmsBtJmPromotionTagProductModel();
                        tagProductModel.setChannelId(model.getChannelId());
                        tagProductModel.setCmsBtTagId(tagModel.getId());
                        tagProductModel.setTagName(tagModel.getTagName());
                        tagProductModel.setCreater("");
                        tagProductModel.setModifier("");
                        saveInfo.tagList.add(tagProductModel);
                        if (saveInfo.productInfo != null) {
                            if (!saveInfo.productInfo.getTags().contains(tagModel.getTagPath())) {
                                saveInfo.productInfo.getTags().add(tagModel.getTagPath());
                            }
                        }
                    }
                } else {
                    // TODO: 2016/5/27  不做处理
                }
                tagProductModel = null;
            }
        }
        if (saveInfo.tagList.size() > 0) {
            String fullTagId = String.format("-%s-", model.getRefTagId());
            if (!saveInfo.productInfo.getTags().contains(fullTagId)) {
                saveInfo.productInfo.getTags().add(fullTagId);
            }
        }
    }

    public List<ExcelColumn> getProductImportColumn() {
        List<ExcelColumn> list = new ArrayList<>();
        list.add(new ExcelColumn("productCode", "cms_bt_jm_promotion_product", "商品代码",false));
        list.add(new ExcelColumn("appId", "cms_bt_jm_promotion_product", "APP端模块ID",true));
        list.add(new ExcelColumn("pcId", "cms_bt_jm_promotion_product", "PC端模块ID",true));
        list.add(new ExcelColumn("limit", "cms_bt_jm_promotion_product", "Deal每人限购",true));
        list.add(new ExcelColumn("promotionTag", "cms_bt_jm_promotion_product", "专场标签（以|分隔）",false));
        return list;
    }

    public List<ExcelColumn> getSkuImportColumn() {
        List<ExcelColumn> list = new ArrayList<>();
        list.add(new ExcelColumn("productCode", "cms_bt_jm_promotion_sku", "商品代码",false));
        list.add(new ExcelColumn("skuCode", "cms_bt_jm_promotion_sku", "规格代码",false));
        list.add(new ExcelColumn("dealPrice", "cms_bt_jm_promotion_sku", "团购价",false));
        list.add(new ExcelColumn("marketPrice", "cms_bt_jm_promotion_sku", "市场价",false));
        return list;
    }

    public CmsBtJmPromotionImportTaskModel get(int id) {
        return cmsBtJmPromotionImportTaskDao.select(id);
    }

    public int update(CmsBtJmPromotionImportTaskModel entity) {
        return cmsBtJmPromotionImportTaskDao.update(entity);
    }

    public int create(CmsBtJmPromotionImportTaskModel entity) {
        return cmsBtJmPromotionImportTaskDao.insert(entity);
    }
    @VOTransactional
    public void saveList(List<CmsBtJmPromotionImportTaskModel> list) {
        for (CmsBtJmPromotionImportTaskModel model : list) {
            cmsBtJmPromotionImportTaskDao.insert(model);
        }
    }
    public List<CmsBtJmPromotionImportTaskModel> getByPromotionId(int promotionId) {
        return     cmsBtJmPromotionImportTaskDaoExt.selectByPromotionId(promotionId);
    }
    /**
     * 发送 聚美导入文件消息
     * @param mqMessageBody 聚美导入文件 消息
     */
    public void sendMessage(JmPromotionImportMQMessageBody mqMessageBody) {
        cmsMqSenderService.sendMessage(mqMessageBody);
    }
}
