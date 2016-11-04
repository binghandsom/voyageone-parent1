package com.voyageone.service.impl.cms.promotion;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.PromotionTypeEnums;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.*;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.AddProductSaveParameter;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.TagTreeNode;
import com.voyageone.service.dao.cms.CmsBtPromotionCodesDao;
import com.voyageone.service.dao.cms.CmsBtTagDao;
import com.voyageone.service.daoext.cms.*;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.TaskService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductTagService;
import com.voyageone.service.model.cms.*;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @Autowired
    PromotionSkuService promotionSkuService;

    @Autowired
    PromotionCodesTagService promotionCodesTagService;

    @VOTransactional
    public void addPromotionDetail(PromotionDetailAddBean bean){
        addPromotionDetail(bean,true);
    }
    /**
     * 添加商品到promotion
     */
    @VOTransactional
    public void addPromotionDetail(PromotionDetailAddBean bean, boolean isUpdatePromotionPrice) {
        String channelId = bean.getChannelId();
        Integer cartId = bean.getCartId();
        Integer promotionId = bean.getPromotionId();
        Integer tagId = bean.getTagId();
        //String tagPath = bean.getTagPath();
        String productCode = bean.getProductCode();
        Long productId = bean.getProductId();
        Double promotionPrice = 0.0;
        if (bean.getPromotionPrice() != null && bean.getPromotionPrice().size() > 0) {
            promotionPrice = (Double) bean.getPromotionPrice().values().toArray()[0];
        }
        String modifier = bean.getModifier();

        // 获取Product信息
        CmsBtProductModel productInfo;
        CmsBtProductGroupModel groupModel;
        JongoQuery query = new JongoQuery();
        if (!StringUtils.isEmpty(productCode)) {
            productInfo = productService.getProductByCode(channelId, productCode);
            query.setQuery("{\"productCodes\":\"" + productCode + "\",\"cartId\":" + cartId + "}");
            groupModel = productGroupService.getProductGroupByQuery(channelId, query);
        } else {
            productInfo = productService.getProductById(channelId, productId);
            query.setQuery("{\"productCodes\":\"" + productInfo.getCommon().getFields().getCode() + "\",\"cartId\":" + cartId + "}");
            groupModel = productGroupService.getProductGroupByQuery(channelId, query);
        }

        if (productInfo == null) {
            $warn("addPromotionDetail product不存在 " + bean.toString());
            throw new BusinessException("productCode:" + productCode + "不存在");
        }

        String numIId = groupModel == null ? null : groupModel.getNumIId();
        // 插入cms_bt_promotion_model表
        CmsBtPromotionGroupsBean cmsBtPromotionGroupsBean = new CmsBtPromotionGroupsBean(productInfo, groupModel, promotionId, modifier);
        cmsBtPromotionGroupsBean.setNumIid(numIId);
        cmsPromotionModelDao.insertPromotionModel(cmsBtPromotionGroupsBean);

        // 插入cms_bt_promotion_code表
        CmsBtPromotionCodesBean cmsBtPromotionCodesBean = new CmsBtPromotionCodesBean(productInfo, groupModel, promotionId, modifier, cartId);
        cmsBtPromotionCodesBean.setNumIid(numIId);
        cmsBtPromotionCodesBean.setPromotionPrice(promotionPrice);
        cmsBtPromotionCodesBean.setTagId(tagId == null ? 0 : tagId);

        List<CmsBtProductModel_Field_Image> imgList = productInfo.getCommonNotNull().getFieldsNotNull().getImages6();
        if (!imgList.isEmpty()) {
            cmsBtPromotionCodesBean.setImage_url_1(imgList.get(0).getName());
        } else {
            imgList = productInfo.getCommonNotNull().getFieldsNotNull().getImages1();
            if (!imgList.isEmpty()) {
                cmsBtPromotionCodesBean.setImage_url_1(imgList.get(0).getName());
            }
        }

        if (cmsPromotionCodeDao.updatePromotionCode(cmsBtPromotionCodesBean) == 0) {
            cmsPromotionCodeDao.insertPromotionCode(cmsBtPromotionCodesBean);
        }

        List<CmsBtProductModel_Sku> skusList = productInfo.getCommonNotNull().getSkus();
        if (skusList == null || skusList.isEmpty()) {
            $warn("addPromotionDetail product sku不存在 参数:" + bean.toString() + " 商品:" + productInfo.toString());
            throw new BusinessException("商品Sku数据不存在");
        }
        List<BaseMongoMap<String, Object>> listSkuMongo = productInfo.getPlatform(bean.getCartId()).getSkus();
        List<CmsBtPromotionSkuBean> listPromotionSku = new ArrayList<>();
        skusList.forEach(sku -> {
            BaseMongoMap<String, Object> mapSkuPlatform = getPlatformSkuMongo(listSkuMongo, sku.getSkuCode());
            CmsBtPromotionSkuBean cmsBtPromotionSkuModelBean = new CmsBtPromotionSkuBean(productInfo, groupModel, promotionId, modifier, sku.getSkuCode(), 0);
            cmsBtPromotionSkuModelBean.setNumIid(numIId);
            cmsBtPromotionSkuModelBean.setSize(sku.getSize());
            if (mapSkuPlatform != null) {
                Double priceMsrp = mapSkuPlatform.getDoubleAttribute("priceMsrp");
                Double priceRetail = mapSkuPlatform.getDoubleAttribute("priceRetail");
                Double priceSale = mapSkuPlatform.getDoubleAttribute("priceSale");
                cmsBtPromotionSkuModelBean.setMsrpRmb(new BigDecimal(priceMsrp));
                cmsBtPromotionSkuModelBean.setRetailPrice(new BigDecimal(priceRetail));
                cmsBtPromotionSkuModelBean.setSalePrice(new BigDecimal(priceSale));
            }
            if (sku != null) {
                cmsBtPromotionSkuModelBean.setMsrpUsd(new BigDecimal(sku.getClientMsrpPrice()));
            }
            if (bean.getPromotionPrice() != null && bean.getPromotionPrice().containsKey(cmsBtPromotionSkuModelBean.getProductSku())) {
                cmsBtPromotionSkuModelBean.setPromotionPrice(new BigDecimal(bean.getPromotionPrice().get(cmsBtPromotionSkuModelBean.getProductSku())));
            } else {
                if (!isUpdatePromotionPrice)//不更新活动价格 还原价格
                {
                    CmsBtPromotionSkusModel cmsBtPromotionSkusModel = promotionSkuService.get(promotionId, productInfo.getCommon().getFields().getCode(), sku.getSkuCode());
                    if (cmsBtPromotionSkusModel != null) {
                        cmsBtPromotionSkuModelBean.setPromotionPrice(cmsBtPromotionSkusModel.getPromotionPrice());
                    }
                }
            }
            if (cmsBtPromotionSkuModelBean.getPromotionPrice() == null) {
                cmsBtPromotionSkuModelBean.setPromotionPrice(new BigDecimal(0));
            }
            listPromotionSku.add(cmsBtPromotionSkuModelBean);
//            if (cmsPromotionSkuDao.updatePromotionSku(cmsBtPromotionSkuModelBean) == 0) {
//                cmsPromotionSkuDao.insertPromotionSku(cmsBtPromotionSkuModelBean);
//            }
        });
        promotionSkuService.loadSkuPrice(listPromotionSku,bean.getAddProductSaveParameter());
        listPromotionSku.forEach(cmsBtPromotionSkuModelBean -> {
            if (cmsPromotionSkuDao.updatePromotionSku(cmsBtPromotionSkuModelBean) == 0) {
                cmsPromotionSkuDao.insertPromotionSku(cmsBtPromotionSkuModelBean);
            }
        });

        // 更新 promotionCodesTag
        promotionCodesTagService.updatePromotionCodesTag(bean.getTagList(), channelId, cmsBtPromotionCodesBean.getId(), modifier);

        //更新mongo product  tag
        updateCmsBtProductTags(channelId, productInfo, bean.getRefTagId(), bean.getTagList(), modifier);
    }
    //更新mongo product  tag
    private void updateCmsBtProductTags(String channelId, CmsBtProductModel productModel,int refTagId,List<TagTreeNode> tagList, String modifier) {
        //更新商品Tags  sunpt
        if (productModel != null) {
            List<String> tags = productModel.getTags();
            int size = tags.size();
            //1.移除该活动的所有tag
            for (int i = size - 1; i >= 0; i--) {
                String tag = String.format("-%s-", refTagId);
                if (tags.get(i).indexOf(tag) == 0) {
                    tags.remove(i);
                }
            }
            //2.添加新的tag
            for (TagTreeNode tagInfo : tagList) {
                if (tagInfo.getChecked() != 0) {
                    tags.add(String.format("-%s-%s-", refTagId, tagInfo.getId()));
                }
            }
            tags.add(String.format("-%s-", refTagId));
            productModel.setTags(tags);
            //3.更新
            productService.updateTags(channelId, productModel.getProdId(), tags, modifier);
        }
    }

    private BaseMongoMap<String, Object> getPlatformSkuMongo(List<BaseMongoMap<String, Object>> list, String skuCode)
    {
        if(list==null) return  null;
        for(BaseMongoMap<String, Object> map:list)
        {
            if(skuCode.equalsIgnoreCase(map.getStringAttribute("skuCode")))
            {
                return  map;
            }
        }
        return null;
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

    @Autowired
    CmsBtPromotionCodesDaoExtCamel daoExtCamelCmsBtPromotionCodes;
    @Autowired
    private CmsBtPromotionGroupsDaoExtCamel daoExtCamelCmsBtPromotionGroups;
    @Autowired
    private CmsBtPromotionSkusDaoExtCamel daoExtCamelCmsBtPromotionSkus;
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

            // 删除旧的的TAG 插入新的TAG
            CmsBtTagModel modelTag = daoTag.select(oldPromotionCodesModel.getTagId());//获取修改前的tag
            if(modelTag == null){
                updateCmsBtProductTags(promotionCodeModel, null,modifier);//更新商品Tags
            }else{
                updateCmsBtProductTags(promotionCodeModel, modelTag.getTagPath(),modifier);//更新商品Tags
            }
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
            if(!StringUtil.isEmpty(oldTagPath)) {
                for (int i = 0; i < size; i++) {
                    if (oldTagPath.equals(tags.get(i))) {
                        //存在替换
                        tags.set(i, promotionCodeModel.getTagPath());
                        isUpdate = true;
                        break;
                    }
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
                List<Long> prodIdList = new ArrayList<>();
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
        return !(tasks != null && !tasks.isEmpty());
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
        Map<String,Double> promostionPrice = new HashedMap();
        if(cmsBtPromotionCodesBean.getSkus() != null){
            cmsBtPromotionCodesBean.getSkus().forEach(cmsBtPromotionSkuBean -> promostionPrice.put(cmsBtPromotionSkuBean.getProductSku(),cmsBtPromotionSkuBean.getPromotionPrice().doubleValue()));
        }
        request.setPromotionPrice(promostionPrice);
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

            PromotionDetailAddBean request=new PromotionDetailAddBean();
            request.setModifier(cmsBtPromotionCodesBean.getModifier());
            request.setChannelId(cmsBtPromotionCodesBean.getChannelId());
            request.setCartId(cmsBtPromotionCodesBean.getCartId());
            request.setProductId(cmsBtPromotionCodesBean.getProductId());
            request.setProductCode(cmsBtPromotionCodesBean.getProductCode());
            request.setPromotionId(cmsBtPromotionCodesBean.getPromotionId());
            Map<String,Double> promostionPrice = new HashedMap();
            if(cmsBtPromotionCodesBean.getSkus() != null){
                cmsBtPromotionCodesBean.getSkus().forEach(cmsBtPromotionSkuBean -> promostionPrice.put(cmsBtPromotionSkuBean.getProductSku(),cmsBtPromotionSkuBean.getPromotionPrice().doubleValue()));
            }
            request.setPromotionPrice(promostionPrice);
            request.setTagId(cmsBtPromotionCodesBean.getTagId());
            request.setTagPath(cmsBtPromotionCodesBean.getTagPath());

            addPromotionDetail(request);

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

    @VOTransactional
   public  void  deleteFromPromotion(CmsBtPromotionModel promotion, AddProductSaveParameter parameter) {
       Map<String, Object> map = new HashMap<>();
       map.put("listProductCode", parameter.getCodeList());
       map.put("promotionId",promotion.getId());

       //批量删除 tag
       promotionCodesTagService.batchDeleteByCodes(parameter.getCodeList(),promotion.getPromotionId());
       //批量删除 code
       daoExtCamelCmsBtPromotionCodes.deleteByPromotionCodeList(map);
       //批量删除 sku
       daoExtCamelCmsBtPromotionSkus.deleteByPromotionCodeList(map);
       //批量删除  product tag
       productService.removeTagByCodes(promotion.getChannelId(), parameter.getCodeList(), promotion.getRefTagId());


       // `cms_bt_promotion_codes_tag`
       // `cms_bt_promotion_skus`
       // `cms_bt_promotion_codes`

   }
}
