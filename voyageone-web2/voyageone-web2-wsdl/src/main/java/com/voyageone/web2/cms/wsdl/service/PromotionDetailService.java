package com.voyageone.web2.cms.wsdl.service;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.common.configs.Enums.PromotionTypeEnums;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.cms.wsdl.dao.*;
import com.voyageone.web2.sdk.api.domain.*;
import com.voyageone.web2.sdk.api.request.*;
import com.voyageone.web2.sdk.api.response.PromotionCodeAddTejiaBaoResponse;
import com.voyageone.web2.sdk.api.response.PromotionDetailPutResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/1/19.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class PromotionDetailService extends BaseService {

    @Autowired
    private CmsPromotionModelDao cmsPromotionModelDao;

    @Autowired
    private CmsPromotionCodeDao cmsPromotionCodeDao;

    @Autowired
    private CmsPromotionSkuDao cmsPromotionSkuDao;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductTagService productTagService;

    @Autowired
    private CmsBtPromotionDao cmsBtPromotionDao;

    @Autowired
    private CmsPromotionTaskDao cmsPromotionTaskDao;
    /**
     * 添加
     *
     * @param promotionDetailAddRequest request
     * @return PromotionDetailPutResponse
     */
    @Transactional
    public PromotionDetailPutResponse insert(PromotionDetailAddRequest promotionDetailAddRequest) {
        promotionDetailAddRequest.check();
        return insertPromotion(promotionDetailAddRequest);
    }

    /**
     * 修改
     *
     * @param promotionDetailUpdateRequest request
     * @return PromotionDetailPutResponse
     */
    @Transactional
    public PromotionDetailPutResponse update(PromotionDetailUpdateRequest promotionDetailUpdateRequest) {
        promotionDetailUpdateRequest.check();
        PromotionDetailPutResponse response = new PromotionDetailPutResponse();
        CmsBtPromotionCodeModel promotionCodeModel = promotionDetailUpdateRequest.getPromotionCodeModel();
        String operator = promotionDetailUpdateRequest.getModifier();
        if (cmsPromotionCodeDao.updatePromotionCode(promotionCodeModel) != 0) {
            CmsBtPromotionTaskModel cmsBtPromotionTask = new CmsBtPromotionTaskModel(promotionCodeModel.getPromotionId(), PromotionTypeEnums.Type.TEJIABAO.getTypeId(), promotionCodeModel.getProductCode(), promotionCodeModel.getNumIid(), operator);
            cmsPromotionTaskDao.updatePromotionTask(cmsBtPromotionTask);
        }
        response.setModifiedCount(1);
        return response;
    }

    /**
     * 删除
     *
     * @param promotionDetailDeleteRequest request
     * @return PromotionDetailPutResponse
     */
    @Transactional
    public PromotionDetailPutResponse remove(PromotionDetailDeleteRequest promotionDetailDeleteRequest) {

        promotionDetailDeleteRequest.check();

        PromotionDetailPutResponse response = new PromotionDetailPutResponse();
        List<CmsBtPromotionGroupModel> promotionModes = promotionDetailDeleteRequest.getPromotionModes();
        String channelId = promotionDetailDeleteRequest.getChannelId();
        String operator = promotionDetailDeleteRequest.getModifier();

        for (CmsBtPromotionGroupModel item : promotionModes) {
            cmsPromotionModelDao.deleteCmsPromotionModel(item);
            HashMap<String, Object> param = new HashMap<>();
            param.put("promotionId", item.getPromotionId());
            param.put("modelId", item.getModelId());

            List<CmsBtPromotionCodeModel> codes = cmsPromotionCodeDao.getPromotionCodeList(param);
            codes.forEach(code -> {
                List<Long> poIds = new ArrayList<>();
                poIds.add(code.getProductId());

                ProductsTagDeleteRequest productsTagDeleteRequest = new ProductsTagDeleteRequest(channelId);
                productsTagDeleteRequest.setModifier(operator);
                for (Long productId : poIds) {
                    productsTagDeleteRequest.addProductIdTagPathsMap(productId, code.getTagPath());
                }
                productTagService.delete(productsTagDeleteRequest);
            });
            cmsPromotionCodeDao.deletePromotionCodeByModelId(item.getPromotionId(), item.getModelId());
            cmsPromotionSkuDao.deletePromotionSkuByModelId(item.getPromotionId(), item.getModelId());
        }
        response.setRemovedCount(promotionModes.size());
        return response;
    }


    private PromotionDetailPutResponse insertPromotion(PromotionDetailAddRequest promotionDetailAddRequest){
        PromotionDetailPutResponse response = new PromotionDetailPutResponse();
        String channelId = promotionDetailAddRequest.getChannelId();
        Integer cartId = promotionDetailAddRequest.getCartId();
        Integer promotionId = promotionDetailAddRequest.getPromotionId();
        Integer tagId = promotionDetailAddRequest.getTagId();
        String tagPath = promotionDetailAddRequest.getTagPath();
        String productCode = promotionDetailAddRequest.getProductCode();
        Long productId = promotionDetailAddRequest.getProductId();
        Double promotionPrice = promotionDetailAddRequest.getPromotionPrice();
        String operator = promotionDetailAddRequest.getModifier();

        // 获取Product信息
        ProductGetRequest productGetRequest = new ProductGetRequest(channelId);
        if (!StringUtils.isEmpty(productCode))
            productGetRequest.setProductCode(productCode);
        else
            productGetRequest.setProductId(productId);
        CmsBtProductModel productInfo = productService.selectOne(productGetRequest).getProduct();

        // 插入cms_bt_promotion_model表
        CmsBtPromotionGroupModel cmsBtPromotionGroupModel = new CmsBtPromotionGroupModel(productInfo, cartId, promotionId, operator);
        cmsPromotionModelDao.insertPromotionModel(cmsBtPromotionGroupModel);

        // 插入cms_bt_promotion_code表
        CmsBtPromotionCodeModel cmsBtPromotionCodeModel = new CmsBtPromotionCodeModel(productInfo, cartId, promotionId, operator);
        cmsBtPromotionCodeModel.setPromotionPrice(promotionPrice);
        cmsBtPromotionCodeModel.setTagId(tagId==null?0:tagId);
        if (cmsPromotionCodeDao.updatePromotionCode(cmsBtPromotionCodeModel) == 0) {
            cmsPromotionCodeDao.insertPromotionCode(cmsBtPromotionCodeModel);
        }

        productInfo.getSkus().forEach(sku -> {
            CmsBtPromotionSkuModel cmsBtPromotionSkuModel = new CmsBtPromotionSkuModel(productInfo, cartId, promotionId, operator, sku.getSkuCode(), 0);
            if (cmsPromotionSkuDao.updatePromotionSku(cmsBtPromotionSkuModel) == 0) {
                cmsPromotionSkuDao.insertPromotionSku(cmsBtPromotionSkuModel);
            }
        });

        if(tagId != null) {
            // tag写入数据库
            List<Long> prodIds = new ArrayList<>();
            prodIds.add(productInfo.getProdId());
            ProductsTagPutRequest productsTagPutRequest = new ProductsTagPutRequest(channelId);
            productsTagPutRequest.setModifier(operator);
            for (Long productId2 : prodIds) {
                productsTagPutRequest.addProductIdTagPathsMap(productId2, tagPath);
            }
            productTagService.saveTagProducts(productsTagPutRequest);
        }
        response.setInsertedCount(1);
        return response;
    }


    /**
     * 判断是否
     * @param promotionCodeAddTejiaBaoRequest
     * @return
     */
    private Boolean isUpdateAllPromotionTask(PromotionCodeAddTejiaBaoRequest promotionCodeAddTejiaBaoRequest){

        Map<String,Object> parm = new HashMap<>();
        parm.put("channelId", promotionCodeAddTejiaBaoRequest.getChannelId());
        parm.put("cartId", promotionCodeAddTejiaBaoRequest.getCartId());
        parm.put("code", promotionCodeAddTejiaBaoRequest.getProductCode());
        // 找出该code有没有参加其它的活动
        List<CmsBtPromotionTaskModel> tasks = cmsPromotionTaskDao.getPromotionByCodeNotInAllPromotion(parm);
        return !(tasks != null && tasks.size()>0);
    }

    public PromotionCodeAddTejiaBaoResponse teJiaBaoPromotionInsert(PromotionCodeAddTejiaBaoRequest promotionCodeAddTejiaBaoRequest){
        PromotionCodeAddTejiaBaoResponse response = new PromotionCodeAddTejiaBaoResponse();
        CmsBtPromotionTaskModel newTask = new CmsBtPromotionTaskModel(promotionCodeAddTejiaBaoRequest.getPromotionId(),PromotionTypeEnums.Type.TEJIABAO.getTypeId(),promotionCodeAddTejiaBaoRequest.getProductCode(),promotionCodeAddTejiaBaoRequest.getNumIid(),promotionCodeAddTejiaBaoRequest.getModifier());
        //如果没有参加其他活动的场合 插入全店特价宝的活动的TASK中
        if(isUpdateAllPromotionTask(promotionCodeAddTejiaBaoRequest)){
            newTask.setSynFlg(1);
        }
        if(cmsPromotionTaskDao.updatePromotionTask(newTask) == 0){
            cmsPromotionTaskDao.insertPromotionTask(newTask);
        }
        PromotionDetailAddRequest promotionDetailAddRequest = new PromotionDetailAddRequest();

        BeanUtils.copyProperties(promotionCodeAddTejiaBaoRequest,promotionDetailAddRequest);
        insertPromotion(promotionDetailAddRequest);
        response.setModifiedCount(1);
        return response;
    }

    public PromotionCodeAddTejiaBaoResponse teJiaBaoPromotionUpdate(PromotionCodeAddTejiaBaoRequest promotionCodeUpdateTejiaBaoRequest){

        CmsBtPromotionCodeModel promotionCodeModel = new CmsBtPromotionCodeModel();
        BeanUtils.copyProperties(promotionCodeUpdateTejiaBaoRequest,promotionCodeModel);
        String operator = promotionCodeUpdateTejiaBaoRequest.getModifier();
        if (cmsPromotionCodeDao.updatePromotionCode(promotionCodeModel) != 0) {
            CmsBtPromotionTaskModel cmsBtPromotionTask = new CmsBtPromotionTaskModel(promotionCodeModel.getPromotionId(), PromotionTypeEnums.Type.TEJIABAO.getTypeId(), promotionCodeModel.getProductCode(), promotionCodeModel.getNumIid(), operator);
            if(isUpdateAllPromotionTask(promotionCodeUpdateTejiaBaoRequest)){
                cmsBtPromotionTask.setSynFlg(1);
            }
            if(cmsPromotionTaskDao.updatePromotionTask(cmsBtPromotionTask) == 0){
                cmsPromotionTaskDao.insertPromotionTask(cmsBtPromotionTask);
            }
        }

        PromotionCodeAddTejiaBaoResponse response = new PromotionCodeAddTejiaBaoResponse();
        response.setModifiedCount(1);
        return response;
    }
}
