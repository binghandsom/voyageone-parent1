package com.voyageone.web2.cms.views.promotion.list;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.configs.Enums.PromotionTypeEnums;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsPromotionProductPriceBean;
import com.voyageone.web2.cms.dao.CmsBtTaskDao;
import com.voyageone.web2.cms.model.CmsBtTaskModel;
import com.voyageone.web2.cms.views.pop.bulkUpdate.CmsAddToPromotionService;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.*;
import com.voyageone.web2.sdk.api.request.*;
import com.voyageone.web2.sdk.api.service.ProductSdkClient;
import com.voyageone.web2.sdk.api.service.ProductTagClient;
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
    protected ProductSdkClient ProductGetClient;

    @Autowired
    private CmsBtTaskDao cmsBtTaskDao;

    @Autowired
    private ProductTagClient productTagClient;

    @Autowired
    private CmsPromotionIndexService cmsPromotionService;

    @Autowired
    private SimpleTransaction simpleTransaction;

    @Autowired
    private CmsAddToPromotionService cmsPromotionSelectService;

    @Autowired
    protected VoApiDefaultClient voApiClient;


    private static final int codeCellNum = 1;
    private static final int priceCellNum = 8;
    private static final int tagCellNum = 9;

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
        PromotionModelsGetRequest request=new PromotionModelsGetRequest();
        request.setParam(param);
        List<Map<String, Object>> promotionGroups =voApiClient.execute(request).getPromotionGroups();
        if(!CollectionUtils.isEmpty(promotionGroups)){
            promotionGroups.forEach(map -> {
                CmsBtProductModel cmsBtProductModel = ProductGetClient.getProductById(param.get("channelId").toString(), ((Integer) map.get("productId")).longValue());

                if (cmsBtProductModel != null) {
                    map.put("image", cmsBtProductModel.getFields().getImages1().get(0).getAttribute("image1"));
                    map.put("platformStatus", cmsBtProductModel.getGroups().getPlatforms().get(0).getPlatformStatus());
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

        PromotionCodeGetRequest request=new PromotionCodeGetRequest();
        request.setParam(param);
        List<CmsBtPromotionCodeModel> promotionCodes = voApiClient.execute(request).getCodeList();
        if(!CollectionUtils.isEmpty(promotionCodes)){
            promotionCodes.forEach(map -> {
                //SDK取得Product 数据
                CmsBtProductModel cmsBtProductModel = ProductGetClient.getProductById(param.get("channelId").toString(), map.getProductId());
                if (cmsBtProductModel != null) {
                    map.setImage((String) cmsBtProductModel.getFields().getImages1().get(0).getAttribute("image1"));
                    map.setSkuCount(cmsBtProductModel.getSkus().size());
                    map.setPlatformStatus(cmsBtProductModel.getGroups().getPlatforms().get(0).getPlatformStatus());
                    map.setInventory(cmsBtProductModel.getBatchField().getCodeQty() == null ? 0 : cmsBtProductModel.getBatchField().getCodeQty());
                }
            });
        }
        return promotionCodes;
    }

    /**
     * 获取Promotion详情中的以sku为单位的数据
     *
     * @param param 参数hashmap  属性有PromotionId channelId
     * @return 以sku为单位的数据
     */
    public List<Map<String, Object>> getPromotionSku(Map<String, Object> param) {
        PromotionSkuGetRequest request=new PromotionSkuGetRequest();
        request.setParam(param);
        List<Map<String, Object>> promotionSkus = voApiClient.execute(request).getSkus();
        if(!CollectionUtils.isEmpty(promotionSkus)){
            HashMap<String, CmsBtProductModel> temp = new HashMap<>(); // 优化把之前已经取到过的Product的信息保存起来
            promotionSkus.forEach(map -> {
                CmsBtProductModel cmsBtProductModel;
                if (!temp.containsKey(map.get("productId").toString())) {
                    cmsBtProductModel = ProductGetClient.getProductById(param.get("channelId").toString(), Long.parseLong(map.get("productId").toString()));
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
        PromotionSkuCountRequest request=new PromotionSkuCountRequest();
        request.setParam(params);
        return voApiClient.execute(request).getTotalCount();
    }

    public int getPromotionCodeListCnt(Map<String, Object> params) {
        PromotionCodeGetCountRequest request=new PromotionCodeGetCountRequest();
        request.setParam(params);
        return voApiClient.execute(request).getTotalCount();
    }

    public int getPromotionModelListCnt(Map<String, Object> params) {
        PromotionModelCountGetRequest request=new PromotionModelCountGetRequest();
        request.setParam(params);
        return voApiClient.execute(request).getCount();
    }

    private List<CmsPromotionProductPriceBean> resolvePromotionXls(InputStream xls) throws Exception {
        List<CmsPromotionProductPriceBean> respones = new ArrayList<>();
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
                CmsPromotionProductPriceBean item = new CmsPromotionProductPriceBean();
                if (row.getCell(codeCellNum).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    int code = (int) row.getCell(codeCellNum).getNumericCellValue();
                    item.setCode(code + "");
                } else {
                    item.setCode(row.getCell(codeCellNum).getStringCellValue());
                }
                if (StringUtil.isEmpty(item.getCode())) {
                    continue;
                }

                if (row.getCell(priceCellNum).getCellType() == Cell.CELL_TYPE_NUMERIC || row.getCell(priceCellNum).getCellType() == Cell.CELL_TYPE_FORMULA) {
                    item.setPrice(Double.parseDouble(StringUtils.getNumPrecision2(row.getCell(priceCellNum).getNumericCellValue())));
                } else {
                    item.setPrice(Double.parseDouble(row.getCell(priceCellNum).getStringCellValue()));
                }

                item.setTag(row.getCell(tagCellNum).getStringCellValue());
                respones.add(item);
            }catch (Exception e){
                throw new BusinessException(String.format("第%d行数据格式不对",rowNum));
            }
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

        simpleTransaction.openTransaction();
        try {
            List<CmsBtTaskModel> tasks = cmsBtTaskDao.selectByName(promotionId, null, PromotionTypeEnums.Type.TEJIABAO.getTypeId());
            if (tasks.size() == 0) {
                CmsBtPromotionModel cmsBtPromotionModel = cmsPromotionService.queryById(promotionId);
                CmsBtTaskModel cmsBtTaskModel = new CmsBtTaskModel();
                cmsBtTaskModel.setModifier(operator);
                cmsBtTaskModel.setCreater(operator);
                cmsBtTaskModel.setPromotion_id(promotionId);
                cmsBtTaskModel.setTask_type(PromotionTypeEnums.Type.TEJIABAO.getTypeId());
                cmsBtTaskModel.setTask_name(cmsBtPromotionModel.getPromotionName());
                cmsBtTaskDao.insert(cmsBtTaskModel);
            }

            Map<String, Object> param = new HashMap<>();
            param.put("promotionId", promotionId);
            PromotionCodeGetRequest requestc = new PromotionCodeGetRequest();
            requestc.setParam(param);
            List<CmsBtPromotionCodeModel> codeList = voApiClient.execute(requestc).getCodeList();

            codeList.forEach(code -> {
                CmsBtPromotionTaskModel cmsBtPromotionTask = new CmsBtPromotionTaskModel(promotionId, PromotionTypeEnums.Type.TEJIABAO.getTypeId(), code.getProductCode(), code.getNumIid(), operator);
                PromotionTaskAddRequest request = new PromotionTaskAddRequest();
                request.setCmsBtPromotionTaskModel(cmsBtPromotionTask);
                voApiClient.execute(request);
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
                PromotionCodeDeleteRequest requestc=new PromotionCodeDeleteRequest();
                requestc.setModel(item);
                voApiClient.execute(requestc);

                HashMap<String, Object> param = new HashMap<>();
                param.put("promotionId", item.getPromotionId());
                param.put("modelId", item.getModelId());
                // 获取与删除的code在同一个group的code数  如果为0 就要删除group表的数据
                PromotionCodeGetCountRequest rc=new PromotionCodeGetCountRequest();
                rc.setParam(param);
                if (voApiClient.execute(rc).getTotalCount() == 0) {
                    CmsBtPromotionGroupModel model = new CmsBtPromotionGroupModel();
                    model.setModelId(item.getModelId());
                    model.setPromotionId(item.getPromotionId());

                    PromotionModelDeleteRequest request=new PromotionModelDeleteRequest();
                    request.setModel(model);
                    voApiClient.execute(request);
                }

                PromotionSkuDeleteRequest request=new PromotionSkuDeleteRequest();
                request.setProductId(item.getProductId());
                request.setPromotionId(item.getPromotionId());
                voApiClient.execute(request);

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
