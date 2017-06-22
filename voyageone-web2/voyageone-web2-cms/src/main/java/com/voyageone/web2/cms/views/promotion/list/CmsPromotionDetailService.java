package com.voyageone.web2.cms.views.promotion.list;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Enums.PromotionTypeEnums;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.ExcelUtils;
import com.voyageone.common.util.FileUtils;
import com.voyageone.service.bean.cms.*;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.dao.cms.CmsBtPromotionCodesTagDao;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.cms.TaskService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.promotion.*;
import com.voyageone.service.model.cms.CmsBtPromotionCodesTagModel;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.cms.CmsBtTaskTejiabaoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.cms.bean.CmsPromotionExportBean;
import com.voyageone.web2.cms.bean.CmsPromotionProductPriceBean;
import com.voyageone.web2.cms.views.pop.bulkUpdate.CmsAddToPromotionService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author james.li on 2015/12/15.
 * @version 2.0.0
 */
@Service
public class CmsPromotionDetailService extends BaseViewService {

    @Autowired
    CmsBtPromotionCodesTagDao cmsBtPromotionCodesTagDao;
    @Autowired
    private TaskService taskService;
    @Autowired
    private CmsPromotionIndexService cmsPromotionService;
    @Autowired
    private CmsAddToPromotionService cmsPromotionSelectService;
    @Autowired
    private PromotionDetailService promotionDetailService;
    @Autowired
    private PromotionModelService promotionModelService;
    @Autowired
    private PromotionCodeService promotionCodeService;
    @Autowired
    private PromotionSkuService promotionSkuService;
    @Autowired
    private PromotionService promotionService;


//    private static final int codeCellNum = 1;
//    private static final int priceCellNum = 8;
//    private static final int tagCellNum = 9;

//    /**
//     * promotion商品插入
//     *
//     * @param productPrices 需要插入的Product列表
//     * @param promotionId   活动ID
//     * @param operator      操作者
//     * @return Map  成功和失败的列表
//     */
//    public Map<String, List<String>> insertPromotionProduct(List<CmsPromotionProductPriceBean> productPrices, int promotionId, String operator) {
//
//        Map<String, List<String>> response = new HashMap<>();
//        response.put("succeed", new ArrayList<>());
//        response.put("fail", new ArrayList<>());
//
//        // 获取promotion信息
//        CmsBtPromotionModel promotion = cmsPromotionService.queryById(promotionId);
//        if (promotion == null) {
//            $info("promotionId不存在：" + promotionId);
//            productPrices.forEach(m -> response.get("fail").add(m.getCode()));
//            return response;
//        }
//        // 获取Tag列表
//        List<CmsBtTagModel> tags = cmsPromotionSelectService.selectListByParentTagId(promotion.getRefTagId());
//        String channelId = promotion.getChannelId();
//        Integer cartId = promotion.getCartId();
//        productPrices.forEach(item -> {
//            boolean errflg = false;
//            try {
//                CmsBtTagModel tagId = searchTag(tags, item.getTag());
//                if (tagId == null) {
//                    throw (new Exception("Tag不存在"));
//                }
//
//                PromotionDetailAddBean request = new PromotionDetailAddBean();
//                request.setModifier(operator);
//                request.setChannelId(channelId);
//                request.setCartId(cartId);
//                request.setProductCode(item.getCode());
//                request.setPromotionId(promotionId);
//                request.setPromotionPrice(item.getPrice());
//                request.setTagId(tagId.getId());
//                request.setTagPath(tagId.getTagPath());
//
//                promotionDetailService.addPromotionDetail(request);
//
//            } catch (Exception e) {
//                response.get("fail").add(item.getCode());
//                errflg = true;
//            }
//            if (!errflg) {
//                response.get("succeed").add(item.getCode());
//            }
//        });
//        return response;
//    }
    @Autowired
    private ProductService productService;

    public Map<String, List<String>> insertPromotionProduct2(List<CmsBtPromotionGroupsBean> productModels, int promotionId, String operator) {

        Map<String, List<String>> response = new HashMap<>();
        response.put("succeed", new ArrayList<>());
        response.put("fail", new ArrayList<>());

        // 获取promotion信息
        CmsBtPromotionModel promotion = cmsPromotionService.queryById(promotionId);
        if (promotion == null) {
            $info("promotionId不存在：" + promotionId);
            return response;
        }
        // 获取Tag列表
        List<CmsBtTagModel> tags = cmsPromotionSelectService.selectListByParentTagId(promotion.getRefTagId());

        for (CmsBtPromotionGroupsBean productModel : productModels) {
            // votodo: 2016/11/10  tag
//            productModel.getCodes().forEach(cmsBtPromotionCodeModel1 -> {
//                CmsBtTagModel tag = searchTag(tags, cmsBtPromotionCodeModel1.getTag());
//                if (tag != null) {
//                    cmsBtPromotionCodeModel1.setTagId(tag.getId());
//                }
//            });

            boolean errflg = false;
            try {
                productModel.setPromotionId(promotionId);
                productModel.setModifier(operator);
                productModel.setChannelId(promotion.getChannelId());
                promotionDetailService.insertPromotionGroup(productModel,tags);
            } catch (Exception e) {
                $error(e);
                productModel.getCodes().forEach(cmsBtPromotionCodeModel -> response.get("fail").add(cmsBtPromotionCodeModel.getProductCode()));
                errflg = true;
            }
            if (!errflg) {
                productModel.getCodes().forEach(cmsBtPromotionCodeModel -> response.get("succeed").add(cmsBtPromotionCodeModel.getProductCode()));
            }

        }

        return response;
    }

    /**
     * 获取Promotion详情中的以model为单位的数据
     *
     * @param param 参数hashmap  属性有PromotionId channelId
     * @return 以model为单位的数据
     */
    public List<Map<String, Object>> getPromotionGroup(Map<String, Object> param, int cartId) {
        List<Map<String, Object>> promotionGroups = promotionModelService.getPromotionModelDetailList(param);
        JongoQuery queryObject = new JongoQuery();

        if (!CollectionUtils.isEmpty(promotionGroups)) {
            promotionGroups.forEach(map -> {
                if (map.get("productId") != null && !map.get("productId").toString().equalsIgnoreCase("0")) {
                    String channelId = (String) param.get("channelId");
                    long productId = Long.parseLong(map.get("productId").toString());

                    queryObject.setQuery("{'prodId':" + productId + "}");
                    queryObject.setProjection("{'common.fields.code':1,'platforms.P" + cartId + ".pStatus':1}");

                    List<CmsBtProductModel> modelList = productService.getList(channelId, queryObject);
                    if (modelList != null && modelList.size() > 0) {
                        CmsBtProductModel_Platform_Cart cartObj = modelList.get(0).getPlatform(cartId);
                        if (cartObj != null) {
                            map.put("platformStatus", cartObj.getpStatus());
                        }
                    }
                }
            });
        }

        return promotionGroups;
    }

    /**
     * 获取Promotion详情中的以code为单位的数据
     *
     * @param param 参数hashmap  属性有PromotionId channelId
     * @return 以code为单位的数据
     */
    public List<CmsBtPromotionCodesBean> getPromotionCode(Map<String, Object> param, int cartId) {
        List<CmsBtPromotionCodesBean> promList = promotionCodeService.getPromotionCodeList(param);

        JongoQuery queryObject = new JongoQuery();
        queryObject.setProjection("{'batchField':1,'common.fields.code':1,'common.fields.quantity':1,'_id':0}");

        if (!CollectionUtils.isEmpty(promList)) {
            promList.forEach(map -> {
                // 取得Product 数据
                queryObject.setQuery("{\"prodId\":" + map.getProductId() + "}");

                List<CmsBtProductBean> prodList = productService.getListWithGroup((String) param.get("channelId"), cartId, queryObject);
                if (prodList != null && prodList.size() > 0) {
//                    map.setImage((String) cmsBtProductModel.getFields().getImages1().get(0).getAttribute("image1"));
//                    map.setSkuCount(cmsBtProductModel.getSkus().size());
                    CmsBtProductBean cmsBtProductModel = prodList.get(0);
                    map.setPlatformStatus(cmsBtProductModel.getGroupBean().getPlatformStatus());
                    map.setInventory(cmsBtProductModel.getCommon().getFields().getQuantity());
                }
                setTagNames(map);
            });
        }
        return promList;
    }

    private void setTagNames(CmsBtPromotionCodesBean bean) {
        // CmsBtJmPromotionTagProductModel parameter = new CmsBtJmPromotionTagProductModel();
        // parameter.setCmsBtJmPromotionProductId(Integer.valueOf(map.get("id").toString()));
        Map<String, Object> map = new HashedMap();
        map.put("cmsBtPromotionCodesId", bean.getId());
        List<CmsBtPromotionCodesTagModel> cmsBtJmPromotionTagProductModelList = cmsBtPromotionCodesTagDao.selectList(map);
        List<String> tagNameList = cmsBtJmPromotionTagProductModelList
                .stream()
                .map(CmsBtPromotionCodesTagModel::getTagName)
                .collect(Collectors.toList());
        bean.setTagNameList(tagNameList);
    }

    /**
     * 获取Promotion详情中的以sku为单位的数据
     *
     * @param param 参数hashmap  属性有PromotionId channelId
     * @return 以sku为单位的数据
     */
    public List<Map<String, Object>> getPromotionSku(Map<String, Object> param) {
        List<Map<String, Object>> promotionSkus = promotionSkuService.getPromotionSkuList(param);
        if (!CollectionUtils.isEmpty(promotionSkus)) {
            HashMap<String, CmsBtProductModel> temp = new HashMap<>(); // 优化把之前已经取到过的Product的信息保存起来
            promotionSkus.forEach(map -> {
                CmsBtProductModel cmsBtProductModel;
                if (!temp.containsKey(map.get("productId").toString())) {
                    cmsBtProductModel = productService.getProductById(param.get("channelId").toString(), Long.parseLong(map.get("productId").toString()));
                    temp.put(map.get("productId").toString(), cmsBtProductModel);
                } else {
                    cmsBtProductModel = temp.get(map.get("productId").toString());
                }
                CmsBtProductModel_Sku sku = cmsBtProductModel.getCommon().getSku(map.get("productSku").toString());
                if (sku != null) {
                    map.put("size", sku.getSize());
                }
            });
        }
        return promotionSkus;
    }

    public int getPromotionSkuListCnt(Map<String, Object> params) {
        return promotionSkuService.getPromotionSkuListCnt(params);
    }

    public int getPromotionCodeListCnt(Map<String, Object> params) {
        return promotionCodeService.getPromotionCodeListCnt(params);
    }

    public int getPromotionModelListCnt(Map<String, Object> params) {
        return promotionModelService.getPromotionModelDetailListCnt(params);
    }

//    private List<CmsPromotionProductPriceBean> resolvePromotionXls(InputStream xls) throws Exception {
//        List<CmsPromotionProductPriceBean> respones = new ArrayList<>();
//        Workbook wb = null;
//        wb = new XSSFWorkbook(xls);
//        Sheet sheet1 = wb.getSheetAt(0);
//        int rowNum = 0;
//        for (Row row : sheet1) {
//            try {
//                rowNum++;
//                // 跳过第一行
//                if (rowNum == 1) {
//                    continue;
//                }
//                CmsPromotionProductPriceBean item = new CmsPromotionProductPriceBean();
//                if (row.getCell(codeCellNum).getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                    int code = (int) row.getCell(codeCellNum).getNumericCellValue();
//                    item.setCode(code + "");
//                } else {
//                    item.setCode(row.getCell(codeCellNum).getStringCellValue());
//                }
//                if (StringUtil.isEmpty(item.getCode())) {
//                    continue;
//                }
//
//                if (row.getCell(priceCellNum).getCellType() == Cell.CELL_TYPE_NUMERIC || row.getCell(priceCellNum).getCellType() == Cell.CELL_TYPE_FORMULA) {
//                    item.setPrice(Double.parseDouble(StringUtils.getNumPrecision2(row.getCell(priceCellNum).getNumericCellValue())));
//                } else {
//                    item.setPrice(Double.parseDouble(row.getCell(priceCellNum).getStringCellValue()));
//                }
//
//                item.setTag(row.getCell(tagCellNum).getStringCellValue());
//                respones.add(item);
//            }catch (Exception e){
//                throw new BusinessException(String.format("第%d行数据格式不对",rowNum));
//            }
//        }
//        return respones;
//    }

    /**
     * @param xls xls文件流
     * @return CmsBtPromotionGroupModel
     * @throws Exception
     */
    private List<CmsBtPromotionGroupsBean> resolvePromotionXls2(InputStream xls) throws Exception {
        List<CmsBtPromotionGroupsBean> models = new ArrayList<>();
        Map<String, CmsBtPromotionGroupsBean> hsModel = new HashMap<>();
        Workbook wb = new XSSFWorkbook(xls);
        Sheet sheet1 = wb.getSheetAt(0);
        int rowNum = 0;
        for (Row row : sheet1) {
            try {
                rowNum++;
                // 跳过第一行
                if (rowNum == 1) {
                    continue;
                }

                if (row.getCell(CmsConstants.CellNum.channelIdCellNum) == null || StringUtil.isEmpty(row.getCell(CmsConstants.CellNum.channelIdCellNum).getStringCellValue())) {
                    break;
                }
                String groupName = ExcelUtils.getString(row, CmsConstants.CellNum.groupIdCellNum);
                if (!StringUtil.isEmpty(groupName)) {
                    CmsBtPromotionGroupsBean model = hsModel.get(groupName);
                    if (model == null) {
                        model = getMode(row);
                        models.add(model);
                        hsModel.put(groupName, model);
                    } else {
                        String code = row.getCell(CmsConstants.CellNum.productCodeCellNum).getStringCellValue();
                        CmsBtPromotionCodesBean product = model.getProductByCode(code);
                        if (product == null) {
                            model.getCodes().add(getCode(row));
                        } else {
                            product.getSkus().add(getSku(row));
                        }
                    }
                }
            } catch (Exception e) {
                throw new BusinessException(String.format("第%d行数据格式不对 (%s)", rowNum, e.toString()));
            }
        }
        return models;
    }

    private CmsBtPromotionGroupsBean getMode(Row row) {

        CmsBtPromotionGroupsBean model = new CmsBtPromotionGroupsBean();
        model.setOrgChannelId(ExcelUtils.getString(row, CmsConstants.CellNum.channelIdCellNum));
        model.setCatPath(ExcelUtils.getString(row, CmsConstants.CellNum.catPathCellNum));
        model.setProductModel(ExcelUtils.getString(row, CmsConstants.CellNum.groupNameCellNum));
//        if (row.getCell(CmsConstants.CellNum.numberIdCellNum).getCellType() == Cell.CELL_TYPE_NUMERIC) {
//            model.setNumIid(row.getCell(CmsConstants.CellNum.numberIdCellNum).getNumericCellValue() + "");
//        } else {
//            model.setNumIid(row.getCell(CmsConstants.CellNum.numberIdCellNum).getStringCellValue());
//        }
        model.setNumIid(ExcelUtils.getString(row, CmsConstants.CellNum.numberIdCellNum));

        String modelId;
        if (row.getCell(CmsConstants.CellNum.groupIdCellNum) != null) {
            if (row.getCell(CmsConstants.CellNum.groupIdCellNum).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                modelId = ExcelUtils.getString(row, CmsConstants.CellNum.groupIdCellNum, "#");
            } else {
                modelId = row.getCell(CmsConstants.CellNum.groupIdCellNum).getStringCellValue();
            }
            if (!StringUtil.isEmpty(modelId)) {
                model.setModelId(Integer.parseInt(modelId));
            }
        }

        model.getCodes().add(getCode(row));
        return model;
    }

    private CmsBtPromotionCodesBean getCode(Row row) {

        CmsBtPromotionCodesBean code = new CmsBtPromotionCodesBean();

        code.setOrgChannelId(ExcelUtils.getString(row, CmsConstants.CellNum.channelIdCellNum));

        if (row.getCell(CmsConstants.CellNum.productIdCellNum) != null) {
            String id = ExcelUtils.getString(row, CmsConstants.CellNum.productIdCellNum, "#");
            if (!StringUtil.isEmpty(id)) {
                code.setProductId(Long.parseLong(id));
            }

        }
        code.setProductModel(ExcelUtils.getString(row, CmsConstants.CellNum.groupNameCellNum));

        code.setCatPath(ExcelUtils.getString(row, CmsConstants.CellNum.catPathCellNum));

        code.setProductCode(ExcelUtils.getString(row, CmsConstants.CellNum.productCodeCellNum));

        code.setImage_url_1(ExcelUtils.getString(row, CmsConstants.CellNum.image1CellNum));

        code.setImage_url_2(ExcelUtils.getString(row, CmsConstants.CellNum.image2CellNum));

        code.setImage_url_3(ExcelUtils.getString(row, CmsConstants.CellNum.image3CellNum));

        code.setMsrp(getNumericCellValue(row.getCell(CmsConstants.CellNum.msrpRMBCellNum)));

        code.setMsrpUS(getNumericCellValue(row.getCell(CmsConstants.CellNum.msrpUSCellNum)));

        code.setPromotionPrice(getNumericCellValue(row.getCell(CmsConstants.CellNum.promotionPriceCellNum)));

        code.setRetailPrice(getNumericCellValue(row.getCell(CmsConstants.CellNum.retailPriceCellNum)));

        code.setSalePrice(getNumericCellValue(row.getCell(CmsConstants.CellNum.salePriceCellNum)));

        code.setProductName(ExcelUtils.getString(row, CmsConstants.CellNum.productNameCellNum));

        code.setTag(ExcelUtils.getString(row, CmsConstants.CellNum.tagCellNum));

        if (row.getCell(CmsConstants.CellNum.timeCellNum) != null) {
            code.setTime(row.getCell(CmsConstants.CellNum.timeCellNum).getStringCellValue());
        }

        if (row.getCell(CmsConstants.CellNum.property1CellNum) != null) {
            code.setProperty1(row.getCell(CmsConstants.CellNum.property1CellNum).getStringCellValue());
        }

        if (row.getCell(CmsConstants.CellNum.property2CellNum) != null) {
            code.setProperty2(row.getCell(CmsConstants.CellNum.property2CellNum).getStringCellValue());
        }

        if (row.getCell(CmsConstants.CellNum.property3CellNum) != null) {
            code.setProperty3(row.getCell(CmsConstants.CellNum.property3CellNum).getStringCellValue());
        }

        if (row.getCell(CmsConstants.CellNum.property4CellNum) != null) {
            code.setProperty4(row.getCell(CmsConstants.CellNum.property4CellNum).getStringCellValue());
        }

        CmsBtPromotionSkuBean sku = getSku(row);
        sku.setProductCode(code.getProductCode());
        sku.setProductId(code.getProductId());
        code.getSkus().add(sku);
        return code;
    }

    private CmsBtPromotionSkuBean getSku(Row row) {

        CmsBtPromotionSkuBean sku = new CmsBtPromotionSkuBean();
        sku.setOrgChannelId(ExcelUtils.getString(row, CmsConstants.CellNum.channelIdCellNum));
        if (row.getCell(CmsConstants.CellNum.inventoryCellNum) != null) {
            sku.setQty(getNumericCellValue(row.getCell(CmsConstants.CellNum.inventoryCellNum)).intValue());
        }
        sku.setProductSku(ExcelUtils.getString(row, CmsConstants.CellNum.skuCellNum));
        sku.setMsrpRmb(new BigDecimal(getNumericCellValue(row.getCell(CmsConstants.CellNum.msrpRMBCellNum))));

        sku.setMsrpUsd(new BigDecimal(getNumericCellValue(row.getCell(CmsConstants.CellNum.msrpUSCellNum))));

        sku.setPromotionPrice(new BigDecimal(getNumericCellValue(row.getCell(CmsConstants.CellNum.promotionPriceCellNum))));

        sku.setRetailPrice(new BigDecimal(getNumericCellValue(row.getCell(CmsConstants.CellNum.retailPriceCellNum))));

        sku.setSalePrice(new BigDecimal(getNumericCellValue(row.getCell(CmsConstants.CellNum.salePriceCellNum))));
        return sku;
    }

    private Double getNumericCellValue(Cell cell) {
        if (cell == null) {
            return 0.0;
        }
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return cell.getNumericCellValue();
        } else {
            return Double.parseDouble(cell.getStringCellValue());
        }
    }

    /**
     * 通过导入excel文件的方式导入活动商品
     *
     * @param xls         excel文件流
     * @param promotionId 活动ID
     * @param operator    操作者
     * @return 成功和失败的列表
     * @throws Exception
     */
//    public Map<String, List<String>> uploadPromotion(InputStream xls, int promotionId, String operator) throws Exception {
//
//        List<CmsPromotionProductPriceBean> uploadPromotionList = resolvePromotionXls2(xls);
//        return insertPromotionProduct(uploadPromotionList, promotionId, operator);
//    }
    public Map<String, List<String>> uploadPromotion(InputStream xls, int promotionId, String operator) throws Exception {

        List<CmsBtPromotionGroupsBean> uploadPromotionList = resolvePromotionXls2(xls);

        Map<String, List<String>> ret = insertPromotionProduct2(uploadPromotionList, promotionId, operator);
        promotionService.sendPromotionMq(promotionId, false, operator);
        return ret;
    }

    private CmsBtTagModel searchTag(List<CmsBtTagModel> tags, String tagName) {

        for (CmsBtTagModel tag : tags) {
            if (tag.getTagName().equalsIgnoreCase(tagName)) {
                return tag;
            }
        }
        return null;
    }

    private CmsBtTagModel searchTagById(List<CmsBtTagModel> tags, int tagId) {

        for (CmsBtTagModel tag : tags) {
            if (tag.getId() == tagId) {
                return tag;
            }
        }
        return null;
    }

    /**
     * 更新promotionCode的信息
     *
     * @param promotionCodeModel romotionCode
     * @param operator           操作者
     */
    public void updatePromotionProduct(CmsBtPromotionCodesBean promotionCodeModel, String operator) {
        promotionDetailService.update(promotionCodeModel, operator);
    }

    /**
     * 删除
     *
     * @param promotionModes promotionModes
     * @param channelId      channelId
     * @param operator       operator
     */
    public void delPromotionModel(List<CmsBtPromotionGroupsBean> promotionModes, String channelId, String operator) {
        promotionDetailService.remove(channelId, promotionModes, operator);
    }

    public void delPromotionCode(List<CmsBtPromotionCodesBean> promotionModes, String channelId, String operator) {
        promotionDetailService.delPromotionCode(promotionModes, channelId, operator);
    }

    public byte[] getTMallJuHuaSuanExport(Integer promotionId, String selChannelId) throws Exception {

        Map<String, List<CmsBtPromotionCodesBean>> groups = getPromotionInfo(promotionId, selChannelId);

        // 转成导出的格式
        List<CmsPromotionExportBean> cmsPromotionExportBeans = getExportBean(groups);
        return writeRecordToJuHuaSuan(cmsPromotionExportBeans);
    }

    public byte[] getTMallPromotionExport(Integer promotionId, String selChannelId) throws Exception {

        Map<String, List<CmsBtPromotionCodesBean>> groups = getPromotionInfo(promotionId, selChannelId);
        return writeRecordToTmall(groups);
    }

    private Map<String, List<CmsBtPromotionCodesBean>> getPromotionInfo(Integer promotionId, String selChannelId) {
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", selChannelId);
        param.put("promotionId", promotionId);

        Map<String, List<CmsBtPromotionCodesBean>> groups = new HashMap<>();
        List<CmsBtPromotionCodesBean> codes = promotionCodeService.getPromotionCodeList(param);
        // 把code按numiid进行分组
        for (CmsBtPromotionCodesBean code : codes) {
            if (StringUtil.isEmpty(code.getNumIid()) || "0".equalsIgnoreCase(code.getNumIid())) continue;
            if (groups.containsKey(code.getNumIid())) {
                groups.get(code.getNumIid()).add(code);
            } else {
                List<CmsBtPromotionCodesBean> temp = new ArrayList<>();
                temp.add(code);
                groups.put(code.getNumIid(), temp);
            }
        }
        return groups;
    }

    private List<CmsPromotionExportBean> getExportBean(Map<String, List<CmsBtPromotionCodesBean>> groups) {
        List<CmsPromotionExportBean> cmsPromotionExportBeans = new ArrayList<>();

        groups.forEach((numiid, cmsBtPromotionCodesBeans) -> {
            cmsPromotionExportBeans.add(calculatePriceQty(cmsBtPromotionCodesBeans));
        });
        return cmsPromotionExportBeans;
    }

    private CmsPromotionExportBean calculatePriceQty(List<CmsBtPromotionCodesBean> codes) {
        Integer qty = 0;
        Integer promotionPrice = 0;
        Integer price = null;
        for (CmsBtPromotionCodesBean code : codes) {
            CmsBtProductModel cmsBtProductModel = productService.getProductByCode(code.getOrgChannelId(), code.getProductCode());
            if (cmsBtProductModel != null) {
                if (cmsBtProductModel.getCommon().getFields().getQuantity() != null) {
                    qty += cmsBtProductModel.getCommon().getFields().getQuantity();
                }
                price = cmsBtProductModel.getPlatform(23).getpPriceRetailEd().intValue();
            }
            if (code.getPromotionPrice() != null && promotionPrice < code.getPromotionPrice().intValue()) {
                promotionPrice = code.getPromotionPrice().intValue();
            }
        }
        CmsPromotionExportBean cmsPromotionExportBean = new CmsPromotionExportBean();
        cmsPromotionExportBean.setQty(qty);
        cmsPromotionExportBean.setPromotionPrice(promotionPrice);
        cmsPromotionExportBean.setMsrpPrice(price);
        cmsPromotionExportBean.setNumIid(codes.get(0).getNumIid());
        return cmsPromotionExportBean;
    }

    private byte[] writeRecordToTmall(Map<String, List<CmsBtPromotionCodesBean>> codes) throws Exception {
        String templatePath = Properties.readValue(CmsProperty.Props.CMS_PROMOTION_EXPORT_TMALL);


        $info("准备生成 Item 文档 [ %s ]", codes.size());
        $info("准备打开文档 [ %s ]", templatePath);

        try (InputStream inputStream = new FileInputStream(templatePath);
             Workbook book = WorkbookFactory.create(inputStream)) {
            Sheet sheet = book.getSheetAt(1);
            Row styleRow = FileUtils.row(sheet, 1);
            CellStyle unlock = styleRow.getRowStyle();
            int rowIndex = 1;
            for (String key : codes.keySet()) {
                Row row = FileUtils.row(sheet, rowIndex);

                FileUtils.cell(row, 0, unlock).setCellValue(key);
                FileUtils.cell(row, 1, unlock).setCellValue(codes.get(key).get(0).getMsrp());
                FileUtils.cell(row, 2, unlock).setCellValue(codes.get(key).get(0).getPromotionPrice());
                rowIndex++;
            }

            $info("文档写入完成");

//            try (FileOutputStream outputFileStream = new FileOutputStream("d:/test.xlsx")) {
//
//                book.write(outputFileStream);
//
//                outputFileStream.flush();
//                outputFileStream.close();
//            }

            // 返回值设定
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                book.write(outputStream);

                $info("已写入输出流");

                return outputStream.toByteArray();
            }
        }
    }

    private byte[] writeRecordToJuHuaSuan(List<CmsPromotionExportBean> cmsPromotionExportBeans) throws Exception {
        String templatePath = Properties.readValue(CmsProperty.Props.CMS_PROMOTION_EXPORT_JUHUASUAN);


        $info("准备生成 Item 文档 [ %s ]", cmsPromotionExportBeans.size());
        $info("准备打开文档 [ %s ]", templatePath);

        try (InputStream inputStream = new FileInputStream(templatePath);
             Workbook book = WorkbookFactory.create(inputStream)) {
            Sheet sheet = book.getSheetAt(0);
            Row styleRow = FileUtils.row(sheet, 2);
            CellStyle unlock = styleRow.getRowStyle();
            int rowIndex = 2;
            for (CmsPromotionExportBean item : cmsPromotionExportBeans) {
                Row row = FileUtils.row(sheet, rowIndex);

                FileUtils.cell(row, 0, unlock).setCellValue(item.getNumIid());
                FileUtils.cell(row, 1, unlock).setCellValue(item.getPromotionPrice());
                FileUtils.cell(row, 2, unlock).setCellValue(item.getQty());
                rowIndex++;
            }

            $info("文档写入完成");

//            try (FileOutputStream outputFileStream = new FileOutputStream("d:/test.xlsx")) {
//
//                book.write(outputFileStream);
//
//                outputFileStream.flush();
//                outputFileStream.close();
//            }

            // 返回值设定
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                book.write(outputStream);

                $info("已写入输出流");

                return outputStream.toByteArray();
            }
        }

    }
}
