package com.voyageone.service.impl.cms.promotion;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.PromotionTypeEnums;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.*;
import com.voyageone.service.dao.cms.CmsBtPromotionCodesDao;
import com.voyageone.service.dao.cms.CmsBtTagDao;
import com.voyageone.service.daoext.cms.CmsBtPromotionCodesDaoExt;
import com.voyageone.service.daoext.cms.CmsBtPromotionGroupsDaoExt;
import com.voyageone.service.daoext.cms.CmsBtPromotionSkusDaoExt;
import com.voyageone.service.daoext.cms.CmsBtTaskTejiabaoDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.TaskService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductTagService;
import com.voyageone.service.model.cms.CmsBtPromotionCodesModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.cms.CmsBtTaskTejiabaoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
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
    private CmsBtPromotionGroupsDaoExt cmsPromotionModelDao;
    @Autowired
    private CmsBtPromotionCodesDaoExt cmsPromotionCodeDao;
    @Autowired
    private CmsBtPromotionSkusDaoExt cmsPromotionSkuDao;
    @Autowired
    private CmsBtTaskTejiabaoDaoExt cmsPromotionTaskDao;

    @Autowired
    private TaskService taskService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private ProductTagService productTagService;

    /**
     * 添加
     */
    @VOTransactional
    public void addPromotionDetail(PromotionDetailAddBean bean) {
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
        CmsBtProductGroupModel groupModel;
        JomgoQuery query = new JomgoQuery();
        if (!StringUtils.isEmpty(productCode)) {
            productInfo = productService.getProductByCode(channelId, productCode);
            query.setQuery("{\"productCodes\":\"" + productCode + "\",\"cartId\":" + cartId + "}");
            groupModel = productGroupService.getProductGroupByQuery(channelId, query);
        }
        else {
            productInfo = productService.getProductById(channelId, productId);
            query.setQuery("{\"productCodes\":\"" + productInfo.getFields().getCode() + "\",\"cartId\":" + cartId + "}");
            groupModel = productGroupService.getProductGroupByQuery(channelId, query);
        }

        if(productInfo == null) {
            throw new BusinessException("productCode:"+productCode+"不存在");
        }


        String numIId = groupModel == null?null:groupModel.getNumIId();
        // 插入cms_bt_promotion_model表
        CmsBtPromotionGroupsBean cmsBtPromotionGroupsBean = new CmsBtPromotionGroupsBean(productInfo, groupModel, promotionId, modifier);
        cmsBtPromotionGroupsBean.setNumIid(numIId);
        cmsPromotionModelDao.insertPromotionModel(cmsBtPromotionGroupsBean);

        // 插入cms_bt_promotion_code表
        CmsBtPromotionCodesBean cmsBtPromotionCodesBean = new CmsBtPromotionCodesBean(productInfo, groupModel, promotionId, modifier);
        cmsBtPromotionCodesBean.setNumIid(numIId);
        cmsBtPromotionCodesBean.setPromotionPrice(promotionPrice);
        cmsBtPromotionCodesBean.setTagId(tagId == null ? 0 : tagId);
        if(productInfo.getFields().getImages1().size() > 0){
            cmsBtPromotionCodesBean.setImage_url_1(productInfo.getFields().getImages1().get(0).getName());
        }
        if (cmsPromotionCodeDao.updatePromotionCode(cmsBtPromotionCodesBean) == 0) {
            cmsPromotionCodeDao.insertPromotionCode(cmsBtPromotionCodesBean);
        }

        productInfo.getSkus().forEach(sku -> {
            CmsBtPromotionSkuBean cmsBtPromotionSkuModel = new CmsBtPromotionSkuBean(productInfo, groupModel, promotionId, modifier, sku.getSkuCode(), 0);
            cmsBtPromotionSkuModel.setNumIid(numIId);
            cmsBtPromotionSkuModel.setSize(sku.getSize());
            if (cmsPromotionSkuDao.updatePromotionSku(cmsBtPromotionSkuModel) == 0) {
                cmsPromotionSkuDao.insertPromotionSku(cmsBtPromotionSkuModel);
            }
        });
    }


    @VOTransactional
    public void insertPromotionGroup(CmsBtPromotionGroupsBean cmsBtPromotionGroupsBean) {

        cmsPromotionModelDao.insertPromotionModel(cmsBtPromotionGroupsBean);

        for (CmsBtPromotionCodesBean code : cmsBtPromotionGroupsBean.getCodes()) {
            code.setPromotionId(cmsBtPromotionGroupsBean.getPromotionId());
            code.setNumIid(cmsBtPromotionGroupsBean.getNumIid());
            code.setModifier(cmsBtPromotionGroupsBean.getModifier());
            code.setModified(cmsBtPromotionGroupsBean.getModified());
            code.setModelId(cmsBtPromotionGroupsBean.getModelId());
            if (cmsPromotionCodeDao.updatePromotionCode(code) == 0) {
                cmsPromotionCodeDao.insertPromotionCode(code);
            }
            cmsPromotionSkuDao.deletePromotionSkuByProductCode(cmsBtPromotionGroupsBean.getPromotionId(), code.getProductCode());
            code.getSkus().forEach(cmsBtPromotionSkuModel -> {
                cmsBtPromotionSkuModel.setNumIid(cmsBtPromotionGroupsBean.getNumIid());
                cmsBtPromotionSkuModel.setProductModel(cmsBtPromotionGroupsBean.getProductModel());
                cmsBtPromotionSkuModel.setProductCode(code.getProductCode());
                cmsBtPromotionSkuModel.setPromotionId(cmsBtPromotionGroupsBean.getPromotionId());
                cmsBtPromotionSkuModel.setCatPath(cmsBtPromotionGroupsBean.getCatPath());
                cmsBtPromotionSkuModel.setModifier(cmsBtPromotionGroupsBean.getModifier());
                cmsBtPromotionSkuModel.setModified(cmsBtPromotionGroupsBean.getModified());
                cmsPromotionSkuDao.insertPromotionSku(cmsBtPromotionSkuModel);
            });
        }
    }

    @Autowired
    CmsBtPromotionCodesDao daoCmsBtPromotionCodes;
    @Autowired
    CmsBtTagDao daoTag;
    /**
     * 修改
     */
    @VOTransactional
    public void update(CmsBtPromotionCodesBean promotionCodeModel, String modifier) {
        CmsBtPromotionCodesModel oldPromotionCodesModel = daoCmsBtPromotionCodes.select(promotionCodeModel.getId());
        if (cmsPromotionCodeDao.updatePromotionCode(promotionCodeModel) != 0) {
//            CmsBtPromotionTaskModel cmsBtPromotionTask = new CmsBtPromotionTaskModel(promotionCodeModel.getPromotionId(),
//                    PromotionTypeEnums.Type.TEJIABAO.getTypeId(),
//                    promotionCodeModel.getProductCode(),
//                    promotionCodeModel.getNumIid(),
//                    modifier);
            CmsBtTaskTejiabaoModel cmsBtPromotionTask = new CmsBtTaskTejiabaoModel();
            cmsBtPromotionTask.setPromotionId(promotionCodeModel.getPromotionId());
            cmsBtPromotionTask.setTaskType(PromotionTypeEnums.Type.TEJIABAO.getTypeId());
            cmsBtPromotionTask.setKey(promotionCodeModel.getProductCode());
            cmsBtPromotionTask.setNumIid(promotionCodeModel.getNumIid());
            cmsBtPromotionTask.setCreater(modifier);
            cmsBtPromotionTask.setModifier(modifier);
            cmsPromotionTaskDao.updatePromotionTask(cmsBtPromotionTask);

            CmsBtTagModel modelTag = daoTag.select(oldPromotionCodesModel.getTagId());//获取修改前的tag
            updateCmsBtProductTags(promotionCodeModel, modelTag.getTagPath(),modifier);//更新商品Tags
        }
    }
    /**
     * 更新商品Tags
     */
    private void updateCmsBtProductTags(CmsBtPromotionCodesBean promotionCodeModel,String oldTagPath,String modifier) {
        //更新商品Tags  sunpt
        CmsBtProductModel productModel = productService.getProductByCode(promotionCodeModel.getOrgChannelId(), promotionCodeModel.getProductCode());
        if(productModel != null) {
            List<String> tags = productModel.getTags();
            int size = tags.size();
            boolean isUpdate = false;
            for (int i = 0; i < size; i++) {
                if (oldTagPath.equals(tags.get(i))) {
                    //存在替换
                    tags.set(i, promotionCodeModel.getTagPath());
                    isUpdate = true;
                    break;
                }
            }
            if (!isUpdate)//没有更新 就添加
            {
                tags.add(promotionCodeModel.getTagPath());
            }
            productModel.setTags(tags);
            productService.updateTags(promotionCodeModel.getOrgChannelId(), promotionCodeModel.getProductId(), tags, modifier);
        }
       //productService.update(productModel);
    }

    /**
     * 删除
     */
    @VOTransactional
    public void remove(String channelId, List<CmsBtPromotionGroupsBean> promotionModes, String modifier) {
        for (CmsBtPromotionGroupsBean item : promotionModes) {
            cmsPromotionModelDao.deleteCmsPromotionModel(item);
            HashMap<String, Object> param = new HashMap<>();
            param.put("promotionId", item.getPromotionId());
            param.put("modelId", item.getModelId());

            List<CmsBtPromotionCodesBean> codes = cmsPromotionCodeDao.selectPromotionCodeList(param);
            codes.forEach(code -> {
                List<Long> prodIdList = new ArrayList<Long>();
                prodIdList.add(code.getProductId());
                productTagService.delete(channelId, code.getTagPath(), prodIdList, "tags", modifier);

                CmsBtTaskTejiabaoModel promotionTask = new CmsBtTaskTejiabaoModel();
                promotionTask.setPromotionId(item.getPromotionId());
                promotionTask.setKey(code.getProductCode());
                promotionTask.setTaskType(0);
                promotionTask.setSynFlg(1);
                cmsPromotionTaskDao.updatePromotionTask(promotionTask);
            });

            cmsPromotionCodeDao.deletePromotionCodeByModelId(item.getPromotionId(), item.getProductModel());
            cmsPromotionSkuDao.deletePromotionSkuByModelId(item.getPromotionId(), item.getProductModel());
        }
    }

    /**
     * 判断是否
     */
    private Boolean isUpdateAllPromotionTask(CmsBtPromotionCodesBean cmsBtPromotionCodesBean) {

        Map<String, Object> parm = new HashMap<>();
        parm.put("channelId", cmsBtPromotionCodesBean.getChannelId());
        parm.put("cartId", cmsBtPromotionCodesBean.getCartId());
        parm.put("code", cmsBtPromotionCodesBean.getProductCode());
        // 找出该code有没有参加其它的活动
        List<CmsBtTaskTejiabaoModel> tasks = cmsPromotionTaskDao.selectPromotionByCodeNotInAllPromotion(parm);
        return !(tasks != null && tasks.size() > 0);
    }

    /**
     * 特价宝商品初期化
     */
    @VOTransactional
    public void addTeJiaBaoInit(List<CmsBtTasksBean> addTaskList, List<CmsBtTaskTejiabaoModel> addPromotionTaskList) {
        addTaskList.forEach(taskService::addTask);
        addPromotionTaskList.forEach(cmsPromotionTaskDao::insertPromotionTask);
    }

    @VOTransactional
    public void teJiaBaoPromotionInsert(CmsBtPromotionCodesBean cmsBtPromotionCodesBean) {
        if (cmsBtPromotionCodesBean.getPromotionId() == 0) {
            CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBean(cmsBtPromotionCodesBean.getChannelId()
                    , CmsConstants.ChannelConfig.TEJIABAO_ID
                    , cmsBtPromotionCodesBean.getCartId().toString());
            if(cmsChannelConfigBean == null || StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())){
                return;
            }else{
                cmsBtPromotionCodesBean.setPromotionId(Integer.parseInt(cmsChannelConfigBean.getConfigValue1()));
            }
        }

//        CmsBtPromotionTaskModel newTask = new CmsBtPromotionTaskModel(cmsBtPromotionCodesBean.getPromotionId(), PromotionTypeEnums.Type.TEJIABAO.getTypeId(), cmsBtPromotionCodesBean.getProductCode(), cmsBtPromotionCodesBean.getNumIid(), cmsBtPromotionCodesBean.getModifier());
        CmsBtTaskTejiabaoModel newTask = new CmsBtTaskTejiabaoModel();
        newTask.setPromotionId(cmsBtPromotionCodesBean.getPromotionId());
        newTask.setTaskType(PromotionTypeEnums.Type.TEJIABAO.getTypeId());
        newTask.setKey(cmsBtPromotionCodesBean.getProductCode());
        newTask.setNumIid(cmsBtPromotionCodesBean.getNumIid());
        newTask.setCreater(cmsBtPromotionCodesBean.getModifier());
        newTask.setModifier(cmsBtPromotionCodesBean.getModifier());

        //如果没有参加其他活动的场合 插入全店特价宝的活动的TASK中
        if (isUpdateAllPromotionTask(cmsBtPromotionCodesBean)) {
            newTask.setSynFlg(1);
        }
        if (cmsPromotionTaskDao.updatePromotionTask(newTask) == 0) {
            cmsPromotionTaskDao.insertPromotionTask(newTask);
        }

        PromotionDetailAddBean request=new PromotionDetailAddBean();
        request.setModifier(cmsBtPromotionCodesBean.getModifier());
        request.setChannelId(cmsBtPromotionCodesBean.getChannelId());
        request.setCartId(cmsBtPromotionCodesBean.getCartId());
        request.setProductId(cmsBtPromotionCodesBean.getProductId());
        request.setProductCode(cmsBtPromotionCodesBean.getProductCode());
        request.setPromotionId(cmsBtPromotionCodesBean.getPromotionId());
        request.setPromotionPrice(cmsBtPromotionCodesBean.getPromotionPrice());
        request.setTagId(cmsBtPromotionCodesBean.getTagId());
        request.setTagPath(cmsBtPromotionCodesBean.getTagPath());

        addPromotionDetail(request);
    }

    @VOTransactional
    public void teJiaBaoPromotionUpdate(CmsBtPromotionCodesBean cmsBtPromotionCodesBean) {

        if (cmsBtPromotionCodesBean.getPromotionId() == 0) {
            CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBean(cmsBtPromotionCodesBean.getChannelId()
                    , CmsConstants.ChannelConfig.TEJIABAO_ID
                    , cmsBtPromotionCodesBean.getCartId().toString());
            if(cmsChannelConfigBean == null || StringUtils.isEmpty(cmsChannelConfigBean.getConfigValue1())){
                return;
            }else{
                cmsBtPromotionCodesBean.setPromotionId(Integer.parseInt(cmsChannelConfigBean.getConfigValue1()));
            }
        }

        String operator = cmsBtPromotionCodesBean.getModifier();
        if (cmsPromotionCodeDao.updatePromotionCode(cmsBtPromotionCodesBean) != 0) {
//            CmsBtPromotionTaskModel cmsBtPromotionTask = new CmsBtPromotionTaskModel(cmsBtPromotionCodesBean.getPromotionId(), PromotionTypeEnums.Type.TEJIABAO.getTypeId(), cmsBtPromotionCodesBean.getProductCode(), cmsBtPromotionCodesBean.getNumIid(), operator);
            CmsBtTaskTejiabaoModel cmsBtPromotionTask = new CmsBtTaskTejiabaoModel();
            cmsBtPromotionTask.setPromotionId(cmsBtPromotionCodesBean.getPromotionId());
            cmsBtPromotionTask.setTaskType(PromotionTypeEnums.Type.TEJIABAO.getTypeId());
            cmsBtPromotionTask.setKey(cmsBtPromotionCodesBean.getProductCode());
            cmsBtPromotionTask.setNumIid(cmsBtPromotionCodesBean.getNumIid());
            cmsBtPromotionTask.setCreater(operator);
            cmsBtPromotionTask.setModifier(operator);

            if (isUpdateAllPromotionTask(cmsBtPromotionCodesBean)) {
                cmsBtPromotionTask.setSynFlg(1);
            }
            if (cmsPromotionTaskDao.updatePromotionTask(cmsBtPromotionTask) == 0) {
                cmsPromotionTaskDao.insertPromotionTask(cmsBtPromotionTask);
            }
        }else{
            teJiaBaoPromotionInsert(cmsBtPromotionCodesBean);
        }
    }

    @VOTransactional
    public void delPromotionCode(List<CmsBtPromotionCodesBean> promotionModes, String channelId, String operator) {
        for (CmsBtPromotionCodesBean item : promotionModes) {
            cmsPromotionCodeDao.deletePromotionCode(item);

            CmsBtTaskTejiabaoModel promotionTask = new CmsBtTaskTejiabaoModel();
            promotionTask.setPromotionId(item.getPromotionId());
            promotionTask.setKey(item.getProductCode());
            promotionTask.setTaskType(0);
            promotionTask.setSynFlg(1);
            cmsPromotionTaskDao.updatePromotionTask(promotionTask);

            HashMap<String, Object> param = new HashMap<>();
            param.put("promotionId", item.getPromotionId());
            param.put("modelId", item.getModelId());
            // 获取与删除的code在同一个group的code数  如果为0 就要删除group表的数据
            int count = cmsPromotionCodeDao.selectPromotionCodeListCnt(param);
            if (count == 1) {
                CmsBtPromotionGroupsBean model = new CmsBtPromotionGroupsBean();
                model.setModelId(item.getModelId());
                model.setPromotionId(item.getPromotionId());
                cmsPromotionModelDao.deleteCmsPromotionModel(model);
            }

            cmsPromotionSkuDao.deletePromotionSkuByProductId(item.getPromotionId(), item.getProductId());

            List<Long> poIds = new ArrayList<>();
            poIds.add(item.getProductId());
            if (!StringUtil.isEmpty(item.getTagPath())) {
                productTagService.delete(channelId, item.getTagPath(), poIds, "tags", operator);
            }
        }
    }
}
