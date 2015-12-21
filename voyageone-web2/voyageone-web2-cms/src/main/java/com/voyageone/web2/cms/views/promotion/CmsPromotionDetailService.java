package com.voyageone.web2.cms.views.promotion;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsPromotionProductPriceBean;
import com.voyageone.web2.cms.dao.CmsPromotionCodeDao;
import com.voyageone.web2.cms.dao.CmsPromotionDao;
import com.voyageone.web2.cms.dao.CmsPromotionModelDao;
import com.voyageone.web2.cms.dao.CmsPromotionSkuDao;
import com.voyageone.web2.cms.model.CmsBtPromotionCodeModel;
import com.voyageone.web2.cms.model.CmsBtPromotionGroupModel;
import com.voyageone.web2.cms.model.CmsBtPromotionModel;
import com.voyageone.web2.cms.model.CmsBtPromotionSkuModel;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.PostProductSelectOneRequest;
import com.voyageone.web2.sdk.api.service.PostProductSelectOneClient;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author james.li on 2015/12/15.
 * @version 2.0.0
 */
@Service
public class CmsPromotionDetailService extends BaseAppService {

    @Autowired
    protected PostProductSelectOneClient productSelectOneClient;

    @Autowired
    CmsPromotionModelDao cmsPromotionModelDao;

    @Autowired
    CmsPromotionCodeDao cmsPromotionCodeDao;

    @Autowired
    CmsPromotionSkuDao cmsPromotionSkuDao;

    @Autowired
    private CmsPromotionDao cmsPromotionDao;

    @Autowired
    private SimpleTransaction simpleTransaction;

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

        CmsBtPromotionModel promotion = cmsPromotionDao.getPromotionById(promotionId);
        if (promotion == null) {
            logger.info("promotionId不存在：" + promotionId);
            productPrices.forEach(m -> {
                response.get("fail").add(m.getCode());
            });
            return response;
        }
        String channelId = promotion.getChannelId();
        Integer cartId = promotion.getCartId();
        productPrices.forEach(item -> {
            boolean errflg = false;
            simpleTransaction.openTransaction();
            try {
                // 获取Product信息
                CmsBtProductModel productInfo = productSelectOneClient.getProductByCode(channelId, item.getCode());

                // 插入cms_bt_promotion_model表
                CmsBtPromotionGroupModel cmsBtPromotionGroupModel = new CmsBtPromotionGroupModel(productInfo, cartId, promotionId, operator);
                cmsPromotionModelDao.insertPromotionModel(cmsBtPromotionGroupModel);

                // 插入cms_bt_promotion_code表
                CmsBtPromotionCodeModel cmsBtPromotionCodeModel = new CmsBtPromotionCodeModel(productInfo, cartId, promotionId, operator);
                cmsBtPromotionCodeModel.setPromotionPrice(item.getPrice());
                if (cmsPromotionCodeDao.updatePromotionModel(cmsBtPromotionCodeModel) == 0) {
                    cmsPromotionCodeDao.insertPromotionCode(cmsBtPromotionCodeModel);
                }

                productInfo.getSkus().forEach(sku -> {
                    CmsBtPromotionSkuModel cmsBtPromotionSkuModel = new CmsBtPromotionSkuModel(productInfo, cartId, promotionId, operator, sku.getSkuCode(), sku.getQty());
                    if (cmsPromotionSkuDao.updatePromotionSku(cmsBtPromotionSkuModel) == 0) {
                        cmsPromotionSkuDao.insertPromotionSku(cmsBtPromotionSkuModel);
                    }
                });
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
            CmsBtProductModel cmsBtProductModel = productSelectOneClient.getProductById("300", (Long) map.get("productId"));

            if (cmsBtProductModel != null) {
                map.put("image", cmsBtProductModel.getFields().getImages1().get(0).getName());
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

        List<CmsBtPromotionCodeModel> promotionGroups = cmsPromotionCodeDao.getPromotionCodeList(param);
        promotionGroups.forEach(map -> {
            //SDK取得Product 数据
            CmsBtProductModel cmsBtProductModel = productSelectOneClient.getProductById("300", map.getProductId());
            if (cmsBtProductModel != null) {
                map.setImage(cmsBtProductModel.getFields().getImages1().get(0).getName());
                map.setSkuCount(cmsBtProductModel.getSkus().size());
                map.setSizeType(cmsBtProductModel.getFields().getSizeType());
            }
        });
        return promotionGroups;
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
                cmsBtProductModel = productSelectOneClient.getProductById("300", (Long) map.get("productId"));
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

    public List<CmsPromotionProductPriceBean> resolvePromotionXls(InputStream xls) throws Exception {
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
            if (row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                int code = (int) row.getCell(0).getNumericCellValue();
                item.setCode(code + "");
            } else {
                item.setCode(row.getCell(0).getStringCellValue());
            }

            if (row.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                item.setPrice(row.getCell(1).getNumericCellValue());
            } else {
                item.setPrice(Double.parseDouble(row.getCell(1).getStringCellValue()));
            }

            item.setTag(row.getCell(2).getStringCellValue());
            respones.add(item);
        }
        return respones;
    }

    public Map<String, List<String>> uploadPromotion(InputStream xls, int promotionId, String operator) throws Exception {

        List<CmsPromotionProductPriceBean> uploadPromotionList = resolvePromotionXls(xls);
        return insertPromotionProduct(uploadPromotionList,promotionId,operator);
    }

}
