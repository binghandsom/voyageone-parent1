package com.voyageone.service.impl.cms.promotion;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.PromotionTypeEnums;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
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
    private CmsBtPromotionModelDao cmsPromotionModelDao;

    @Autowired
    private CmsBtPromotionCodeDao cmsPromotionCodeDao;

    @Autowired
    private CmsBtPromotionSkuDao cmsPromotionSkuDao;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductTagService productTagService;

    @Autowired
    private CmsBtPromotionDao cmsBtPromotionDao;

    @Autowired
    private CmsBtPromotionTaskDao cmsPromotionTaskDao;

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

        if(productInfo == null) {
            throw new BusinessException("productCode:"+productCode+"不存在");
        }

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
            List<CmsBtPromotionCodeModel> codes = cmsPromotionCodeDao.selectPromotionCodeList(param);
            codes.forEach(code -> {
                Long productId = code.getProductId();
                if (!productIdTagsMap.containsKey(productId)) {
                    productIdTagsMap.put(productId, new ArrayList<>());
                }
                List<String> tagPathList = productIdTagsMap.get(productId);
                tagPathList.add(code.getTagPath());

                CmsBtPromotionTaskModel promotionTask = new CmsBtPromotionTaskModel();
                promotionTask.setPromotionId(item.getPromotionId());
                promotionTask.setKey(code.getProductCode());
                promotionTask.setTaskType(0);
                promotionTask.setSynFlg(1);
                cmsPromotionTaskDao.updatePromotionTask(promotionTask);

            });

            if(codes.size() > 0){
                productTagService.delete(channelId, productIdTagsMap, modifier);
            }
            cmsPromotionCodeDao.deletePromotionCodeByModelId(item.getPromotionId(), item.getProductModel());
            cmsPromotionSkuDao.deletePromotionSkuByModelId(item.getPromotionId(), item.getProductModel());


        }
    }

    /**
     * 判断是否
     *
     * @param cmsBtPromotionCodeModel
     * @return
     */
    private Boolean isUpdateAllPromotionTask(CmsBtPromotionCodeModel cmsBtPromotionCodeModel) {

        Map<String, Object> parm = new HashMap<>();
        parm.put("channelId", cmsBtPromotionCodeModel.getChannelId());
        parm.put("cartId", cmsBtPromotionCodeModel.getCartId());
        parm.put("code", cmsBtPromotionCodeModel.getProductCode());
        // 找出该code有没有参加其它的活动
        List<CmsBtPromotionTaskModel> tasks = cmsPromotionTaskDao.getPromotionByCodeNotInAllPromotion(parm);
        return !(tasks != null && tasks.size() > 0);
    }

    public void teJiaBaoPromotionInsert(CmsBtPromotionCodeModel cmsBtPromotionCodeModel) {

        if(cmsBtPromotionCodeModel.getPromotionId() == 0){
            CmsChannelConfigBean cmsChannelConfigBean =CmsChannelConfigs.getConfigBean(cmsBtPromotionCodeModel.getChannelId(), "TEJIABAO_ID", cmsBtPromotionCodeModel.getCartId().toString());
            if(cmsChannelConfigBean == null || StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())){
                return;
            }else{
                cmsBtPromotionCodeModel.setPromotionId(Integer.parseInt(cmsChannelConfigBean.getConfigValue1()));
            }
        }

        CmsBtPromotionTaskModel newTask = new CmsBtPromotionTaskModel(cmsBtPromotionCodeModel.getPromotionId(), PromotionTypeEnums.Type.TEJIABAO.getTypeId(), cmsBtPromotionCodeModel.getProductCode(), cmsBtPromotionCodeModel.getNumIid(), cmsBtPromotionCodeModel.getModifier());
        //如果没有参加其他活动的场合 插入全店特价宝的活动的TASK中
        if (isUpdateAllPromotionTask(cmsBtPromotionCodeModel)) {
            newTask.setSynFlg(1);
        }
        if (cmsPromotionTaskDao.updatePromotionTask(newTask) == 0) {
            cmsPromotionTaskDao.insertPromotionTask(newTask);
        }

        PromotionDetailAddBean request=new PromotionDetailAddBean();
        request.setModifier(cmsBtPromotionCodeModel.getModifier());
        request.setChannelId(cmsBtPromotionCodeModel.getChannelId());
        request.setCartId(cmsBtPromotionCodeModel.getCartId());
        request.setProductId(cmsBtPromotionCodeModel.getProductId());
        request.setProductCode(cmsBtPromotionCodeModel.getProductCode());
        request.setPromotionId(cmsBtPromotionCodeModel.getPromotionId());
        request.setPromotionPrice(cmsBtPromotionCodeModel.getPromotionPrice());
        request.setTagId(cmsBtPromotionCodeModel.getTagId());
        request.setTagPath(cmsBtPromotionCodeModel.getTagPath());

        insertPromotionDetail(request);
    }

    public void teJiaBaoPromotionUpdate(CmsBtPromotionCodeModel cmsBtPromotionCodeModel) {

        if(cmsBtPromotionCodeModel.getPromotionId() == 0){
            CmsChannelConfigBean cmsChannelConfigBean =CmsChannelConfigs.getConfigBean(cmsBtPromotionCodeModel.getChannelId(), "TEJIABAO_ID", cmsBtPromotionCodeModel.getCartId().toString());
            if(cmsChannelConfigBean == null || StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())){
                return;
            }else{
                cmsBtPromotionCodeModel.setPromotionId(Integer.parseInt(cmsChannelConfigBean.getConfigValue1()));
            }
        }

        String operator = cmsBtPromotionCodeModel.getModifier();
        if (cmsPromotionCodeDao.updatePromotionCode(cmsBtPromotionCodeModel) != 0) {
            CmsBtPromotionTaskModel cmsBtPromotionTask = new CmsBtPromotionTaskModel(cmsBtPromotionCodeModel.getPromotionId(), PromotionTypeEnums.Type.TEJIABAO.getTypeId(), cmsBtPromotionCodeModel.getProductCode(), cmsBtPromotionCodeModel.getNumIid(), operator);
            if (isUpdateAllPromotionTask(cmsBtPromotionCodeModel)) {
                cmsBtPromotionTask.setSynFlg(1);
            }
            if (cmsPromotionTaskDao.updatePromotionTask(cmsBtPromotionTask) == 0) {
                cmsPromotionTaskDao.insertPromotionTask(cmsBtPromotionTask);
            }
        }else{
            teJiaBaoPromotionInsert(cmsBtPromotionCodeModel);
        }
    }
}
