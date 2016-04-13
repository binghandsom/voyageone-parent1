package com.voyageone.web2.cms.views.promotion.list;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.configs.Enums.PromotionTypeEnums;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.ExcelUtils;
import com.voyageone.service.bean.cms.PromotionDetailAddBean;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductTagService;
import com.voyageone.service.impl.cms.promotion.*;
import com.voyageone.service.model.cms.*;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.web2.cms.bean.CmsPromotionProductPriceBean;
import com.voyageone.service.dao.cms.CmsBtTasksDao;
import com.voyageone.service.model.cms.CmsBtTasksModel;
import com.voyageone.web2.cms.views.pop.bulkUpdate.CmsAddToPromotionService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2015/12/15.
 * @version 2.0.0
 */
@Service
public class CmsPromotionDetailService extends BaseAppService {

    @Autowired
    private CmsBtTasksDao cmsBtTaskDao;

    @Autowired
    private ProductTagService productTagService;

    @Autowired
    private CmsPromotionIndexService cmsPromotionService;

    @Autowired
    private SimpleTransaction simpleTransaction;

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
    private PromotionTaskService promotionTaskService;

    @Autowired
    private ProductService productService;


//    private static final int codeCellNum = 1;
//    private static final int priceCellNum = 8;
//    private static final int tagCellNum = 9;

    /**
     * promotion商品插入
     *
     * @param productPrices 需要插入的Product列表
     * @param promotionId   活动ID
     * @param operator      操作者
     * @return Map  成功和失败的列表
     */
    public Map<String, List<String>> insertPromotionProduct(List<CmsPromotionProductPriceBean> productPrices, int promotionId, String operator) {

        Map<String, List<String>> response = new HashMap<>();
        response.put("succeed", new ArrayList<>());
        response.put("fail", new ArrayList<>());

        // 获取promotion信息
        CmsBtPromotionModel promotion = cmsPromotionService.queryById(promotionId);
        if (promotion == null) {
            $info("promotionId不存在：" + promotionId);
            productPrices.forEach(m -> {
                response.get("fail").add(m.getCode());
            });
            return response;
        }
        // 获取Tag列表
        List<CmsBtTagModel> tags = cmsPromotionSelectService.selectListByParentTagId(promotion.getRefTagId());
        String channelId = promotion.getChannelId();
        Integer cartId = promotion.getCartId();
        productPrices.forEach(item -> {
            boolean errflg = false;
            simpleTransaction.openTransaction();
            try {
                CmsBtTagModel tagId = searchTag(tags, item.getTag());
                if (tagId == null) {
                    throw (new Exception("Tag不存在"));
                }

                PromotionDetailAddBean request = new PromotionDetailAddBean();
                request.setModifier(operator);
                request.setChannelId(channelId);
                request.setCartId(cartId);
                request.setProductCode(item.getCode());
                request.setPromotionId(promotionId);
                request.setPromotionPrice(item.getPrice());
                request.setTagId(tagId.getTagId());
                request.setTagPath(tagId.getTagPath());

                promotionDetailService.insertPromotionDetail(request);

            } catch (Exception e) {
                simpleTransaction.rollback();
                response.get("fail").add(item.getCode());
                errflg = true;
            }
            if (!errflg) {
                simpleTransaction.commit();
                response.get("succeed").add(item.getCode());
            }
        });
        return response;
    }

    public Map<String, List<String>> insertPromotionProduct2(List<CmsBtPromotionGroupModel> productModels, int promotionId, String operator) {

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
        String channelId = promotion.getChannelId();
        Integer cartId = promotion.getCartId();

        for (CmsBtPromotionGroupModel productModel : productModels) {
            productModel.getCodes().forEach(cmsBtPromotionCodeModel1 -> {
                CmsBtTagModel tag = searchTag(tags, cmsBtPromotionCodeModel1.getTag());
                if (tag != null) {
                    cmsBtPromotionCodeModel1.setTagId(tag.getTagId());
                }
            });
            simpleTransaction.openTransaction();
            boolean errflg = false;
            try {
                productModel.setPromotionId(promotionId);
                productModel.setModifier(operator);
                promotionDetailService.insertPromotionGroup(productModel);
            } catch (Exception e) {
                $error(e);
                simpleTransaction.rollback();
                productModel.getCodes().forEach(cmsBtPromotionCodeModel -> response.get("fail").add(cmsBtPromotionCodeModel.getProductCode()));
                errflg = true;
            }
            if (!errflg) {
                simpleTransaction.commit();
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
    public List<Map<String, Object>> getPromotionGroup(Map<String, Object> param) {
        List<Map<String, Object>> promotionGroups = promotionModelService.getPromotionModelDetailList(param);

        if (!CollectionUtils.isEmpty(promotionGroups)) {
            promotionGroups.forEach(map -> {
                if (map.get("productId") != null && !map.get("productId").toString().equalsIgnoreCase("0")) {

                    CmsBtProductModel cmsBtProductModel = productService.getProductById(param.get("channelId").toString(),Long.parseLong(map.get("productId").toString()));

                    if (cmsBtProductModel != null) {
//                    map.put("image", cmsBtProductModel.getFields().getImages1().get(0).getAttribute("image1"));
                        map.put("platformStatus", cmsBtProductModel.getGroups().getPlatforms().get(0).getPlatformStatus());
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
    public List<CmsBtPromotionCodeModel> getPromotionCode(Map<String, Object> param) {

        List<CmsBtPromotionCodeModel> promotionCodes = promotionCodeService.getPromotionCodeList(param);
//        if (!CollectionUtils.isEmpty(promotionCodes)) {
//            promotionCodes.forEach(map -> {
//                //SDK取得Product 数据
//                CmsBtProductModel cmsBtProductModel = ProductGetClient.getProductById(param.get("channelId").toString(), map.getProductId());
//                if (cmsBtProductModel != null) {
////                    map.setImage((String) cmsBtProductModel.getFields().getImages1().get(0).getAttribute("image1"));
////                    map.setSkuCount(cmsBtProductModel.getSkus().size());
//                    map.setPlatformStatus(cmsBtProductModel.getGroups().getPlatforms().get(0).getPlatformStatus());
//                    map.setInventory(cmsBtProductModel.getBatchField().getCodeQty() == null ? 0 : cmsBtProductModel.getBatchField().getCodeQty());
//                }
//            });
//        }
        return promotionCodes;
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
                CmsBtProductModel_Sku sku = cmsBtProductModel.getSku(map.get("productSku").toString());
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
     * @param xls
     * @return
     * @throws Exception
     */
    private List<CmsBtPromotionGroupModel> resolvePromotionXls2(InputStream xls) throws Exception {
        List<CmsBtPromotionGroupModel> models = new ArrayList<>();
        Map<String, CmsBtPromotionGroupModel> hsModel = new HashMap<>();
        Workbook wb = null;
        wb = new XSSFWorkbook(xls);
        Sheet sheet1 = wb.getSheetAt(0);
        int rowNum = 0;
        for (Row row : sheet1) {
            try {
                rowNum++;
                // 跳过第一行
                if (rowNum == 1) {
                    continue;
                }

                if (row.getCell(CmsConstants.CellNum.catPathCellNum) == null || StringUtil.isEmpty(row.getCell(CmsConstants.CellNum.catPathCellNum).getStringCellValue())) {
                    break;
                }
                String groupName = row.getCell(CmsConstants.CellNum.groupNameCellNum).getStringCellValue();
                if (!StringUtil.isEmpty(groupName)) {
                    CmsBtPromotionGroupModel model = hsModel.get(groupName);
                    if (model == null) {
                        model = getMode(row);
                        models.add(model);
                        hsModel.put(groupName, model);
                    } else {
                        String code = row.getCell(CmsConstants.CellNum.productCodeCellNum).getStringCellValue();
                        CmsBtPromotionCodeModel product = model.getProductByCode(code);
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

    private CmsBtPromotionGroupModel getMode(Row row) {

        CmsBtPromotionGroupModel model = new CmsBtPromotionGroupModel();
        model.setOrgChannelId(row.getCell(CmsConstants.CellNum.channelIdCellNum).getStringCellValue());
        model.setCatPath(row.getCell(CmsConstants.CellNum.catPathCellNum).getStringCellValue());
        model.setProductModel(row.getCell(CmsConstants.CellNum.groupNameCellNum).getStringCellValue());
        if (row.getCell(CmsConstants.CellNum.numberIdCellNum).getCellType() == Cell.CELL_TYPE_NUMERIC) {
            model.setNumIid(row.getCell(CmsConstants.CellNum.numberIdCellNum).getNumericCellValue() + "");
        } else {
            model.setNumIid(row.getCell(CmsConstants.CellNum.numberIdCellNum).getStringCellValue());
        }

        String modelId;
        if (row.getCell(CmsConstants.CellNum.groupIdCellNum) != null) {
            if (row.getCell(CmsConstants.CellNum.groupIdCellNum).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                modelId = ExcelUtils.getString(row,CmsConstants.CellNum.groupIdCellNum,"#");
            } else {
                modelId = row.getCell(CmsConstants.CellNum.groupIdCellNum).getStringCellValue();
            }
            if (!StringUtil.isEmpty(modelId)) {
                model.setModelId(Long.parseLong(modelId));
            }
        }

        model.getCodes().add(getCode(row));
        return model;
    }

    private CmsBtPromotionCodeModel getCode(Row row) {

        CmsBtPromotionCodeModel code = new CmsBtPromotionCodeModel();

        code.setOrgChannelId(row.getCell(CmsConstants.CellNum.channelIdCellNum).getStringCellValue());

        if (row.getCell(CmsConstants.CellNum.productIdCellNum) != null) {
            code.setProductId(Long.parseLong(ExcelUtils.getString(row,CmsConstants.CellNum.productIdCellNum,"#")));
        }
        code.setProductModel(ExcelUtils.getString(row,CmsConstants.CellNum.groupNameCellNum));

        code.setCatPath(row.getCell(CmsConstants.CellNum.catPathCellNum).getStringCellValue());

        code.setProductCode(row.getCell(CmsConstants.CellNum.productCodeCellNum).getStringCellValue());

        code.setImage_url_1(row.getCell(CmsConstants.CellNum.image1CellNum).getStringCellValue());

        code.setImage_url_2(row.getCell(CmsConstants.CellNum.image2CellNum).getStringCellValue());

        code.setImage_url_3(row.getCell(CmsConstants.CellNum.image3CellNum).getStringCellValue());

        code.setMsrp(getNumericCellValue(row.getCell(CmsConstants.CellNum.msrpRMBCellNum)));

        code.setMsrpUS(getNumericCellValue(row.getCell(CmsConstants.CellNum.msrpUSCellNum)));

        code.setPromotionPrice(getNumericCellValue(row.getCell(CmsConstants.CellNum.promotionPriceCellNum)));

        code.setRetailPrice(getNumericCellValue(row.getCell(CmsConstants.CellNum.retailPriceCellNum)));

        code.setSalePrice(getNumericCellValue(row.getCell(CmsConstants.CellNum.salePriceCellNum)));

        code.setProductName(row.getCell(CmsConstants.CellNum.productNameCellNum).getStringCellValue());

        code.setTag(ExcelUtils.getString(row,CmsConstants.CellNum.tagCellNum));

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

        CmsBtPromotionSkuModel sku = getSku(row);
        sku.setProductCode(code.getProductCode());
        sku.setProductId(code.getProductId());
        code.getSkus().add(sku);
        return code;
    }

    private CmsBtPromotionSkuModel getSku(Row row) {

        CmsBtPromotionSkuModel sku = new CmsBtPromotionSkuModel();
        sku.setOrgChannelId(row.getCell(CmsConstants.CellNum.channelIdCellNum).getStringCellValue());
        if (row.getCell(CmsConstants.CellNum.inventoryCellNum) != null) {
            sku.setQty(getNumericCellValue(row.getCell(CmsConstants.CellNum.inventoryCellNum)).intValue());
        }
        sku.setProductSku(row.getCell(CmsConstants.CellNum.skuCellNum).getStringCellValue());
        return sku;
    }

    private Double getNumericCellValue(Cell cell) {
        if (cell == null) {
            return null;
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

        List<CmsBtPromotionGroupModel> uploadPromotionList = resolvePromotionXls2(xls);
        return insertPromotionProduct2(uploadPromotionList, promotionId, operator);
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
            if (tag.getTagId() == tagId) {
                return tag;
            }
        }
        return null;
    }

    /**
     * 特价宝商品初期化
     *
     * @param promotionId 活动ID
     * @param operator    操作者
     */
    public void teJiaBaoInit(Integer promotionId, String channelId, String operator) {

        simpleTransaction.openTransaction();
        try {
            List<CmsBtTasksModel> tasks = cmsBtTaskDao.selectByName(promotionId, null, channelId, PromotionTypeEnums.Type.TEJIABAO.getTypeId());
            if (tasks.size() == 0) {
                CmsBtPromotionModel cmsBtPromotionModel = cmsPromotionService.queryById(promotionId);
                CmsBtTasksModel cmsBtTaskModel = new CmsBtTasksModel();
                cmsBtTaskModel.setModifier(operator);
                cmsBtTaskModel.setCreater(operator);
                cmsBtTaskModel.setPromotion_id(promotionId);
                cmsBtTaskModel.setTask_type(PromotionTypeEnums.Type.TEJIABAO.getTypeId());
                cmsBtTaskModel.setTask_name(cmsBtPromotionModel.getPromotionName());
                cmsBtTaskModel.setActivity_start(cmsBtPromotionModel.getActivityStart());
                cmsBtTaskModel.setActivity_end(cmsBtPromotionModel.getActivityEnd());
                cmsBtTaskModel.setChannelId(channelId);
                cmsBtTaskDao.insert(cmsBtTaskModel);
            }

            Map<String, Object> param = new HashMap<>();
            param.put("promotionId", promotionId);
            List<CmsBtPromotionCodeModel> codeList = promotionCodeService.getPromotionCodeList(param);

            codeList.forEach(code -> {
                CmsBtPromotionTaskModel cmsBtPromotionTask = new CmsBtPromotionTaskModel(promotionId, PromotionTypeEnums.Type.TEJIABAO.getTypeId(), code.getProductCode(), code.getNumIid(), operator);
                promotionTaskService.insertPromotionTask(cmsBtPromotionTask);
            });
        } catch (Exception e) {
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();
    }

    /**
     * 更新promotionCode的信息
     *
     * @param promotionCodeModel romotionCode
     * @param operator           操作者
     */
    public void updatePromotionProduct(CmsBtPromotionCodeModel promotionCodeModel, String operator) {
        promotionDetailService.update(promotionCodeModel, operator);
    }

    /**
     * 删除
     *
     * @param promotionModes promotionModes
     * @param channelId      channelId
     * @param operator       operator
     */
    public void delPromotionModel(List<CmsBtPromotionGroupModel> promotionModes, String channelId, String operator) {
        promotionDetailService.remove(channelId, promotionModes, operator);
    }

    public void delPromotionCode(List<CmsBtPromotionCodeModel> promotionModes, String channelId, String operator) {

        simpleTransaction.openTransaction();
        try {
            for (CmsBtPromotionCodeModel item : promotionModes) {
                promotionCodeService.deletePromotionCode(item);

                CmsBtPromotionTaskModel promotionTask = new CmsBtPromotionTaskModel();
                promotionTask.setPromotionId(item.getPromotionId());
                promotionTask.setKey(item.getProductCode());
                promotionTask.setTaskType(0);
                promotionTask.setSynFlg(1);
                promotionTaskService.updatePromotionTask(promotionTask);

                HashMap<String, Object> param = new HashMap<>();
                param.put("promotionId", item.getPromotionId());
                param.put("modelId", item.getModelId());
                // 获取与删除的code在同一个group的code数  如果为0 就要删除group表的数据
                int count = promotionCodeService.getPromotionCodeListCnt(param);
                if (count == 1) {
                    CmsBtPromotionGroupModel model = new CmsBtPromotionGroupModel();
                    model.setModelId(item.getModelId());
                    model.setPromotionId(item.getPromotionId());

                    promotionModelService.deleteCmsPromotionModel(model);
                }

                promotionSkuService.remove(item.getPromotionId(), item.getProductId());

                List<Long> poIds = new ArrayList<>();
                poIds.add(item.getProductId());

                if (!StringUtil.isEmpty(item.getTagPath())) {
                    productTagService.delete(channelId, item.getTagPath(), poIds, operator);
                }
            }
        } catch (Exception e) {
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();
    }
}
