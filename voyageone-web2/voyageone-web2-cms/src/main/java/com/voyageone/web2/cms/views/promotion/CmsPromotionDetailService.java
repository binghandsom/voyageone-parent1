package com.voyageone.web2.cms.views.promotion;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.configs.Enums.PromotionTypeEnums;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsPromotionProductPriceBean;
import com.voyageone.web2.cms.views.pop.tag.promotion.CmsPromotionSelectService;
import com.voyageone.web2.cms.wsdl.dao.CmsPromotionCodeDao;
import com.voyageone.web2.cms.wsdl.dao.CmsPromotionModelDao;
import com.voyageone.web2.cms.wsdl.dao.CmsPromotionSkuDao;
import com.voyageone.web2.cms.wsdl.dao.CmsPromotionTaskDao;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.*;
import com.voyageone.web2.sdk.api.request.PromotionDetailAddRequest;
import com.voyageone.web2.sdk.api.request.PromotionDetailDeleteRequest;
import com.voyageone.web2.sdk.api.request.PromotionDetailUpdateRequest;
import com.voyageone.web2.sdk.api.service.ProductSdkClient;
import com.voyageone.web2.sdk.api.service.ProductTagClient;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    protected ProductSdkClient ProductGetClient;

    @Autowired
    private ProductTagClient productTagClient;

    @Autowired
    private CmsPromotionModelDao cmsPromotionModelDao;

    @Autowired
    private CmsPromotionCodeDao cmsPromotionCodeDao;

    @Autowired
    private CmsPromotionSkuDao cmsPromotionSkuDao;

    @Autowired
    private CmsPromotionService cmsPromotionService;

    @Autowired
    private CmsPromotionTaskDao cmsPromotionTaskDao;

    @Autowired
    private SimpleTransaction simpleTransaction;

    @Autowired
    private CmsPromotionSelectService cmsPromotionSelectService;

    @Autowired
    protected VoApiDefaultClient voApiClient;


    private static final int codeCellNum = 1;
    private static final int priceCellNum = 2;
    private static final int tagCellNum = 3;

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
            logger.info("promotionId不存在：" + promotionId);
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

                PromotionDetailAddRequest request=new PromotionDetailAddRequest();
                request.setModifier(operator);
                request.setChannelId(channelId);
                request.setCartId(cartId);
                request.setProductCode(item.getCode());
                request.setPromotionId(promotionId);
                request.setPromotionPrice(item.getPrice());
                request.setTagId(tagId.getTagId());
                request.setTagPath(tagId.getTagPath());

                voApiClient.execute(request);

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

    /**
     * 获取Promotion详情中的以model为单位的数据
     *
     * @param param 参数hashmap  属性有PromotionId channelId
     * @return 以model为单位的数据
     */
    public List<Map<String, Object>> getPromotionGroup(Map<String, Object> param) {
        List<Map<String, Object>> promotionGroups = cmsPromotionModelDao.getPromotionModelDetailList(param);
        promotionGroups.forEach(map -> {
            CmsBtProductModel cmsBtProductModel = ProductGetClient.getProductById(param.get("channelId").toString(), (Long) map.get("productId"));

            if (cmsBtProductModel != null) {
                map.put("image", cmsBtProductModel.getFields().getImages1().get(0).getAttribute("image1"));
                map.put("platformStatus",cmsBtProductModel.getGroups().getPlatforms().get(0).getPlatformStatus());
            }
        });

        return promotionGroups;
    }

    /**
     * 获取Promotion详情中的以code为单位的数据
     *
     * @param param 参数hashmap  属性有PromotionId channelId
     * @return 以code为单位的数据
     */
    public List<CmsBtPromotionCodeModel> getPromotionCode(Map<String, Object> param) {

        List<CmsBtPromotionCodeModel> promotionCodes = cmsPromotionCodeDao.getPromotionCodeList(param);
        promotionCodes.forEach(map -> {
            //SDK取得Product 数据
            CmsBtProductModel cmsBtProductModel = ProductGetClient.getProductById(param.get("channelId").toString(), map.getProductId());
            if (cmsBtProductModel != null) {
                map.setImage((String) cmsBtProductModel.getFields().getImages1().get(0).getAttribute("image1"));
                map.setSkuCount(cmsBtProductModel.getSkus().size());
                map.setPlatformStatus(cmsBtProductModel.getGroups().getPlatforms().get(0).getPlatformStatus());
            }
        });
        return promotionCodes;
    }

    /**
     * 获取Promotion详情中的以sku为单位的数据
     *
     * @param param 参数hashmap  属性有PromotionId channelId
     * @return 以sku为单位的数据
     */
    public List<Map<String, Object>> getPromotionSku(Map<String, Object> param) {
        List<Map<String, Object>> promotionSkus = cmsPromotionSkuDao.getPromotionSkuList(param);
        HashMap<String, CmsBtProductModel> temp = new HashMap<>(); // 优化把之前已经取到过的Product的信息保存起来
        promotionSkus.forEach(map -> {
            CmsBtProductModel cmsBtProductModel;
            if (!temp.containsKey(map.get("productId").toString())) {
                cmsBtProductModel = ProductGetClient.getProductById(param.get("channelId").toString(), (Long) map.get("productId"));
                temp.put(map.get("productId").toString(), cmsBtProductModel);
            } else {
                cmsBtProductModel = temp.get(map.get("productId").toString());
            }
            CmsBtProductModel_Sku sku = cmsBtProductModel.getSku(map.get("productSku").toString());
            if (sku != null) {
                map.put("size", sku.getSize());
            }
        });

        return promotionSkus;
    }

    public int getPromotionSkuListCnt(Map<String, Object> params) {
        return cmsPromotionSkuDao.getPromotionSkuListCnt(params);
    }

    public int getPromotionCodeListCnt(Map<String, Object> params) {
        return cmsPromotionCodeDao.getPromotionCodeListCnt(params);
    }

    public int getPromotionModelListCnt(Map<String, Object> params) {
        return cmsPromotionModelDao.getPromotionModelDetailListCnt(params);
    }

    private List<CmsPromotionProductPriceBean> resolvePromotionXls(InputStream xls) throws Exception {
        List<CmsPromotionProductPriceBean> respones = new ArrayList<>();
        Workbook wb = null;
        wb = new XSSFWorkbook(xls);
        Sheet sheet1 = wb.getSheetAt(0);
        int rowNum = 0;
        for (Row row : sheet1) {
            rowNum++;
            // 跳过第一行
            if (rowNum == 1) {
                continue;
            }
            CmsPromotionProductPriceBean item = new CmsPromotionProductPriceBean();
            if (row.getCell(codeCellNum).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                int code = (int) row.getCell(codeCellNum).getNumericCellValue();
                item.setCode(code + "");
            } else {
                item.setCode(row.getCell(codeCellNum).getStringCellValue());
            }

            if (row.getCell(priceCellNum).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                item.setPrice(row.getCell(priceCellNum).getNumericCellValue());
            } else {
                item.setPrice(Double.parseDouble(row.getCell(priceCellNum).getStringCellValue()));
            }

            item.setTag(row.getCell(tagCellNum).getStringCellValue());
            respones.add(item);
        }
        return respones;
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
    public Map<String, List<String>> uploadPromotion(InputStream xls, int promotionId, String operator) throws Exception {

        List<CmsPromotionProductPriceBean> uploadPromotionList = resolvePromotionXls(xls);
        return insertPromotionProduct(uploadPromotionList, promotionId, operator);
    }

    private CmsBtTagModel searchTag(List<CmsBtTagModel> tags, String tagName) {

        for (CmsBtTagModel tag : tags) {
            if (tag.getTagPathName().equalsIgnoreCase(tagName)) {
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
    public void teJiaBaoInit(Integer promotionId, String operator) {
        Map<String, Object> param = new HashMap<>();
        param.put("promotionId", promotionId);
        List<CmsBtPromotionCodeModel> codeList = cmsPromotionCodeDao.getPromotionCodeList(param);
        simpleTransaction.openTransaction();
        try {
            codeList.forEach(code -> {
                CmsBtPromotionTaskModel cmsBtPromotionTask = new CmsBtPromotionTaskModel(promotionId, PromotionTypeEnums.Type.TEJIABAO.getTypeId(), code.getProductCode(),code.getNumIid(), operator);
                cmsPromotionTaskDao.insertPromotionTask(cmsBtPromotionTask);
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
        PromotionDetailUpdateRequest request=new PromotionDetailUpdateRequest();
        request.setPromotionCodeModel(promotionCodeModel);
        request.setModifier(operator);
        voApiClient.execute(request);
    }

    /**
     * 删除
     * @param promotionModes promotionModes
     * @param channelId channelId
     * @param operator operator
     */
    public void delPromotionModel(List<CmsBtPromotionGroupModel> promotionModes, String channelId, String operator) {
        PromotionDetailDeleteRequest request=new PromotionDetailDeleteRequest();
        request.setPromotionModes(promotionModes);
        request.setChannelId(channelId);
        request.setModifier(operator);
        voApiClient.execute(request);
    }

    public void delPromotionCode(List<CmsBtPromotionCodeModel> promotionModes, String channelId, String operator) {

        simpleTransaction.openTransaction();
        try {
            for (CmsBtPromotionCodeModel item : promotionModes) {
                cmsPromotionCodeDao.deletePromotionCode(item);

                HashMap<String, Object> param = new HashMap<>();
                param.put("promotionId", item.getPromotionId());
                param.put("modelId", item.getModelId());
                // 获取与删除的code在同一个group的code数  如果为0 就要删除group表的数据
                if (cmsPromotionCodeDao.getPromotionCodeListCnt(param) == 0) {
                    CmsBtPromotionGroupModel model = new CmsBtPromotionGroupModel();
                    model.setModelId(item.getModelId());
                    model.setPromotionId(item.getPromotionId());
                    cmsPromotionModelDao.deleteCmsPromotionModel(model);
                }

                cmsPromotionSkuDao.deletePromotionSkuByProductId(item.getPromotionId(), item.getProductId());

                List<Long> poIds = new ArrayList<>();
                poIds.add(item.getProductId());
                //liang change
                //cmsPromotionSelectService.remove(poIds, channelId, item.getTagPath(), operator);
                productTagClient.removeTagProducts(channelId, item.getTagPath(), poIds, operator);
            }
        } catch (Exception e) {
            simpleTransaction.rollback();
            throw e;
        }
        simpleTransaction.commit();
    }
}
