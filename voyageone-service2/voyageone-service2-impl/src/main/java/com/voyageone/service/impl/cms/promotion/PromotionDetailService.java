package com.voyageone.service.impl.cms.promotion;

import com.voyageone.common.configs.Enums.PromotionTypeEnums;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.PromotionDetailAddBean;
import com.voyageone.service.dao.cms.*;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductTagService;
import com.voyageone.service.model.cms.CmsBtPromotionCodeModel;
import com.voyageone.service.model.cms.CmsBtPromotionGroupModel;
import com.voyageone.service.model.cms.CmsBtPromotionSkuModel;
import com.voyageone.service.model.cms.CmsBtPromotionTaskModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     */
    public void insertPromotionDetail(PromotionDetailAddBean bean) {
        String channelId = bean.getChannelId();
        Integer cartId = bean.getCartId();
        Integer promotionId = bean.getPromotionId();
        Integer tagId = bean.getTagId();
        String tagPath = bean.getTagPath();
        String productCode = bean.getProductCode();
        Long productId = bean.getProductId();
        Double promotionPrice = bean.getPromotionPrice();
        String modifier = bean.getModifier();

        // 获取Product信息
        CmsBtProductModel productInfo;
        if (!StringUtils.isEmpty(productCode))
            productInfo = productService.getProductByCode(channelId, productCode);
        else
            productInfo = productService.getProductById(channelId, productId);

        // 插入cms_bt_promotion_model表
        CmsBtPromotionGroupModel cmsBtPromotionGroupModel = new CmsBtPromotionGroupModel(productInfo, cartId, promotionId, modifier);
        cmsPromotionModelDao.insertPromotionModel(cmsBtPromotionGroupModel);

        // 插入cms_bt_promotion_code表
        CmsBtPromotionCodeModel cmsBtPromotionCodeModel = new CmsBtPromotionCodeModel(productInfo, cartId, promotionId, modifier);
        cmsBtPromotionCodeModel.setPromotionPrice(promotionPrice);
        cmsBtPromotionCodeModel.setTagId(tagId == null ? 0 : tagId);
        if(productInfo.getFields().getImages1().size() > 0){
            cmsBtPromotionCodeModel.setImage_url_1(productInfo.getFields().getImages1().get(0).getName());
        }
        if (cmsPromotionCodeDao.updatePromotionCode(cmsBtPromotionCodeModel) == 0) {
            cmsPromotionCodeDao.insertPromotionCode(cmsBtPromotionCodeModel);
        }

        productInfo.getSkus().forEach(sku -> {
            CmsBtPromotionSkuModel cmsBtPromotionSkuModel = new CmsBtPromotionSkuModel(productInfo, cartId, promotionId, modifier, sku.getSkuCode(), 0);
            if (cmsPromotionSkuDao.updatePromotionSku(cmsBtPromotionSkuModel) == 0) {
                cmsPromotionSkuDao.insertPromotionSku(cmsBtPromotionSkuModel);
            }
        });

        if (tagId != null) {

            Map<Long, List<String>> productIdTagPathsMap = new HashMap<>();
            List<String> tagPathList = new ArrayList<>();
            productIdTagPathsMap.put(productInfo.getProdId(), tagPathList);
            tagPathList.add(tagPath);
            productTagService.saveTagProducts(channelId, productIdTagPathsMap, modifier);
        }
    }


    public void insertPromotionGroup(CmsBtPromotionGroupModel cmsBtPromotionGroupModel) {

        cmsPromotionModelDao.insertPromotionModel(cmsBtPromotionGroupModel);

        for (CmsBtPromotionCodeModel code : cmsBtPromotionGroupModel.getCodes()) {
            code.setPromotionId(cmsBtPromotionGroupModel.getPromotionId());
            code.setNumIid(cmsBtPromotionGroupModel.getNumIid());
            code.setModifier(cmsBtPromotionGroupModel.getModifier());
            code.setCreater(cmsBtPromotionGroupModel.getModified());
            code.setModelId(cmsBtPromotionGroupModel.getModelId());
            if (cmsPromotionCodeDao.updatePromotionCode(code) == 0) {
                cmsPromotionCodeDao.insertPromotionCode(code);
            }
            cmsPromotionSkuDao.deletePromotionSkuByProductCode(cmsBtPromotionGroupModel.getPromotionId(), code.getProductCode());
            code.getSkus().forEach(cmsBtPromotionSkuModel -> {
                cmsBtPromotionSkuModel.setNumIid(cmsBtPromotionGroupModel.getNumIid());
                cmsBtPromotionSkuModel.setProductModel(cmsBtPromotionGroupModel.getProductModel());
                cmsBtPromotionSkuModel.setProductCode(code.getProductCode());
                cmsBtPromotionSkuModel.setPromotionId(cmsBtPromotionGroupModel.getPromotionId());
                cmsBtPromotionSkuModel.setCatPath(cmsBtPromotionGroupModel.getCatPath());
                cmsBtPromotionSkuModel.setModifier(cmsBtPromotionGroupModel.getModifier());
                cmsBtPromotionSkuModel.setCreater(cmsBtPromotionGroupModel.getModified());
                cmsPromotionSkuDao.insertPromotionSku(cmsBtPromotionSkuModel);
            });
        }
    }

    /**
     * 修改
     */
    public void update(CmsBtPromotionCodeModel promotionCodeModel, String modifier) {
        if (cmsPromotionCodeDao.updatePromotionCode(promotionCodeModel) != 0) {
            CmsBtPromotionTaskModel cmsBtPromotionTask = new CmsBtPromotionTaskModel(promotionCodeModel.getPromotionId(),
                    PromotionTypeEnums.Type.TEJIABAO.getTypeId(),
                    promotionCodeModel.getProductCode(),
                    promotionCodeModel.getNumIid(),
                    modifier);
            cmsPromotionTaskDao.updatePromotionTask(cmsBtPromotionTask);
        }
    }

    /**
     * 删除
     */
    public void remove(String channelId, List<CmsBtPromotionGroupModel> promotionModes, String modifier) {
        for (CmsBtPromotionGroupModel item : promotionModes) {
            cmsPromotionModelDao.deleteCmsPromotionModel(item);
            HashMap<String, Object> param = new HashMap<>();
            param.put("promotionId", item.getPromotionId());
            param.put("modelId", item.getModelId());

            Map<Long, List<String>> productIdTagsMap = new HashMap<>();
            List<CmsBtPromotionCodeModel> codes = cmsPromotionCodeDao.getPromotionCodeList(param);
            codes.forEach(code -> {
                Long productId = code.getProductId();
                if (!productIdTagsMap.containsKey(productId)) {
                    productIdTagsMap.put(productId, new ArrayList<>());
                }
                List<String> tagPathList = productIdTagsMap.get(productId);
                tagPathList.add(code.getTagPath());
            });

            productTagService.delete(channelId, productIdTagsMap, modifier);
            cmsPromotionCodeDao.deletePromotionCodeByModelId(item.getPromotionId(), item.getModelId());
            cmsPromotionSkuDao.deletePromotionSkuByModelId(item.getPromotionId(), item.getModelId());
        }
    }

//    /**
//     * 判断是否
//     *
//     * @param promotionCodeAddTejiaBaoRequest
//     * @return
//     */
//    private Boolean isUpdateAllPromotionTask(PromotionCodeAddTejiaBaoRequest promotionCodeAddTejiaBaoRequest) {
//
//        Map<String, Object> parm = new HashMap<>();
//        parm.put("channelId", promotionCodeAddTejiaBaoRequest.getChannelId());
//        parm.put("cartId", promotionCodeAddTejiaBaoRequest.getCartId());
//        parm.put("code", promotionCodeAddTejiaBaoRequest.getProductCode());
//        // 找出该code有没有参加其它的活动
//        List<CmsBtPromotionTaskModel> tasks = cmsPromotionTaskDao.getPromotionByCodeNotInAllPromotion(parm);
//        return !(tasks != null && tasks.size() > 0);
//    }
//
//    public PromotionCodeAddTejiaBaoResponse teJiaBaoPromotionInsert(PromotionCodeAddTejiaBaoRequest promotionCodeAddTejiaBaoRequest) {
//        PromotionCodeAddTejiaBaoResponse response = new PromotionCodeAddTejiaBaoResponse();
//        CmsBtPromotionTaskModel newTask = new CmsBtPromotionTaskModel(promotionCodeAddTejiaBaoRequest.getPromotionId(), PromotionTypeEnums.Type.TEJIABAO.getTypeId(), promotionCodeAddTejiaBaoRequest.getProductCode(), promotionCodeAddTejiaBaoRequest.getNumIid(), promotionCodeAddTejiaBaoRequest.getModifier());
//        //如果没有参加其他活动的场合 插入全店特价宝的活动的TASK中
//        if (isUpdateAllPromotionTask(promotionCodeAddTejiaBaoRequest)) {
//            newTask.setSynFlg(1);
//        }
//        if (cmsPromotionTaskDao.updatePromotionTask(newTask) == 0) {
//            cmsPromotionTaskDao.insertPromotionTask(newTask);
//        }
//        PromotionDetailAddRequest promotionDetailAddRequest = new PromotionDetailAddRequest();
//
//        BeanUtils.copyProperties(promotionCodeAddTejiaBaoRequest, promotionDetailAddRequest);
//        insertPromotion(promotionDetailAddRequest);
//        response.setModifiedCount(1);
//        return response;
//    }
//
//    public PromotionCodeAddTejiaBaoResponse teJiaBaoPromotionUpdate(PromotionCodeAddTejiaBaoRequest promotionCodeUpdateTejiaBaoRequest) {
//
//        CmsBtPromotionCodeModel promotionCodeModel = new CmsBtPromotionCodeModel();
//        BeanUtils.copyProperties(promotionCodeUpdateTejiaBaoRequest, promotionCodeModel);
//        String operator = promotionCodeUpdateTejiaBaoRequest.getModifier();
//        if (cmsPromotionCodeDao.updatePromotionCode(promotionCodeModel) != 0) {
//            CmsBtPromotionTaskModel cmsBtPromotionTask = new CmsBtPromotionTaskModel(promotionCodeModel.getPromotionId(), PromotionTypeEnums.Type.TEJIABAO.getTypeId(), promotionCodeModel.getProductCode(), promotionCodeModel.getNumIid(), operator);
//            if (isUpdateAllPromotionTask(promotionCodeUpdateTejiaBaoRequest)) {
//                cmsBtPromotionTask.setSynFlg(1);
//            }
//            if (cmsPromotionTaskDao.updatePromotionTask(cmsBtPromotionTask) == 0) {
//                cmsPromotionTaskDao.insertPromotionTask(cmsBtPromotionTask);
//            }
//        }
//
//        PromotionCodeAddTejiaBaoResponse response = new PromotionCodeAddTejiaBaoResponse();
//        response.setModifiedCount(1);
//        return response;
//    }
}
