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
import com.voyageone.common.util.ConvertUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.*;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.AddProductSaveParameter;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.InitParameter;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.TagTreeNode;
import com.voyageone.service.bean.cms.businessmodel.CmsBtTag.TagCodeCountInfo;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.ProductTagInfo;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.UpdatePromotionProductTagParameter;
import com.voyageone.service.dao.cms.CmsBtPromotionCodesDao;
import com.voyageone.service.dao.cms.CmsBtTagDao;
import com.voyageone.service.daoext.cms.*;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CmsBtBrandBlockService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.TaskService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductTagService;
import com.voyageone.service.model.cms.*;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.service.model.util.MapModel;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author aooer 2016/1/19.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class PromotionDetailService extends BaseService {

    @Autowired
    PromotionSkuService promotionSkuService;
    @Autowired
    PromotionCodesTagService promotionCodesTagService;
    @Autowired
    PromotionService  promotionService;//promotionService promotionCodesTagService
    @Autowired
    CmsBtPromotionDaoExtCamel cmsBtPromotionDaoExtCamel;
    @Autowired
    CmsBtPromotionCodesDao daoCmsBtPromotionCodes;
    @Autowired
    CmsBtTagDao daoTag;
    @Autowired
    CmsBtPromotionCodesDaoExtCamel daoExtCamelCmsBtPromotionCodes;
    @Autowired
    TagService tagService;
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
    private FeedInfoService feedInfoService;
    @Autowired
    private CmsBtBrandBlockService brandBlockService;
    @Autowired
    private CmsBtPromotionGroupsDaoExtCamel daoExtCamelCmsBtPromotionGroups;
    @Autowired
    private CmsBtPromotionSkusDaoExtCamel daoExtCamelCmsBtPromotionSkus;

    @VOTransactional
    public void addPromotionDetail(PromotionDetailAddBean bean) {
        if (!check_addPromotionDetail(bean))//验证不通过 不能添加活动
        {
            return;
        }
        addPromotionDetail(bean, true);
    }

    public  boolean check_addPromotionDetail(PromotionDetailAddBean bean) {
        String channelId = bean.getChannelId();
        Integer cartId = bean.getCartId();
        String productCode = bean.getProductCode();
        Long productId = bean.getProductId();
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
        productCode = productInfo.getCommon().getFields().getCode();
        // 取得feed 品牌
        CmsBtFeedInfoModel cmsBtFeedInfoModel = feedInfoService.getProductByCode(channelId, productInfo.getCommon().getFields().getCode());
        String feedBrand = null;
        if (cmsBtFeedInfoModel == null) {
            $warn("addToPromotion CmsBtFeedInfoModel channelId=%s, code=%s", channelId, productCode);
        } else {
            feedBrand = cmsBtFeedInfoModel.getBrand();
        }
        String masterBrand = productInfo.getCommonNotNull().getFieldsNotNull().getBrand();
        String platBrand = productInfo.getPlatformNotNull(cartId).getpBrandId();
        if (brandBlockService.isBlocked(channelId, cartId, feedBrand, masterBrand, platBrand)) {
            $warn("addToPromotion 该品牌为黑名单 channelId=%s, cartId=%d, code=%s, feed brand=%s, master brand=%s, platform brand=%s", channelId, cartId, productCode, feedBrand, masterBrand, platBrand);

            return false;
        }
        List<CmsBtProductModel_Sku> skusList = productInfo.getCommonNotNull().getSkus();
        if (skusList == null || skusList.isEmpty()) {
            $warn("addPromotionDetail product sku不存在 参数:" + bean.toString() + " 商品:" + productInfo.toString());
            throw new BusinessException("商品Sku数据不存在");
        }
        bean.setProductInfo(productInfo);
        bean.setGroupModel(groupModel);
        return true;
    }
    /**
     * 添加商品到promotion
     */
    @VOTransactional
    public void addPromotionDetail(PromotionDetailAddBean bean, boolean isUpdatePromotionPrice) {
        String channelId = bean.getChannelId();
        Integer cartId = bean.getCartId();
        Integer promotionId = bean.getPromotionId();
        String modifier = bean.getModifier();

        // 获取Product信息
        CmsBtProductModel productInfo=bean.getProductInfo();   //check方法  已经初始化
        CmsBtProductGroupModel groupModel=bean.getGroupModel();//check方法  已经初始化

        String numIId = groupModel == null ? null : groupModel.getNumIId();
        // 插入cms_bt_promotion_model表
        CmsBtPromotionGroupsBean cmsBtPromotionGroupsBean = new CmsBtPromotionGroupsBean(productInfo, groupModel, promotionId, modifier);
        cmsBtPromotionGroupsBean.setNumIid(numIId);
        cmsPromotionModelDao.insertPromotionModel(cmsBtPromotionGroupsBean);

        //初始化PromotionCode
        CmsBtPromotionCodesModel codesModel = loadCmsBtPromotionCodesModel(productInfo, groupModel, promotionId, modifier, cartId);

        //初始化PromotionSku
        List<CmsBtPromotionSkuBean> listPromotionSku = loadPromotionSkus(bean, productInfo, groupModel, promotionId, modifier, isUpdatePromotionPrice);

        //计算PromotionSku活动价
        promotionSkuService.loadSkuPrice(listPromotionSku, bean.getAddProductSaveParameter());

        //保存sku
        listPromotionSku.forEach(cmsBtPromotionSkuModelBean -> {
            if (cmsPromotionSkuDao.updatePromotionSku(cmsBtPromotionSkuModelBean) == 0) {
                cmsPromotionSkuDao.insertPromotionSku(cmsBtPromotionSkuModelBean);
            }
        });


        //PromotionSku活动最大价格 为商品活动价格
        double maxPromotionPrice = listPromotionSku.stream().mapToDouble(m -> m.getPromotionPrice().doubleValue()).max().getAsDouble();
        codesModel.setPromotionPrice(maxPromotionPrice);

        //保存codes
        if (codesModel.getId() == null || codesModel.getId() == 0) {
            daoCmsBtPromotionCodes.insert(codesModel);
        } else {
            daoCmsBtPromotionCodes.update(codesModel);
        }
        if (cartId != 2) {
            // 更新 promotionCodesTag
            promotionCodesTagService.updatePromotionCodesTag(bean.getTagList(), channelId, codesModel.getId(), modifier);

            //更新mongo product tag
            productService.updateCmsBtProductTags(channelId, productInfo, bean.getRefTagId(), bean.getTagList(), modifier);
        }
    }

    List<CmsBtPromotionSkuBean>   loadPromotionSkus(PromotionDetailAddBean bean,CmsBtProductModel productInfo,CmsBtProductGroupModel groupModel, int promotionId, String modifier,boolean isUpdatePromotionPrice) {
        List<CmsBtProductModel_Sku> skusList = productInfo.getCommonNotNull().getSkus();
        String numIId = groupModel == null ? null : groupModel.getNumIId();
        List<BaseMongoMap<String, Object>> listSkuMongo = productInfo.getPlatform(bean.getCartId()).getSkus();
        List<CmsBtPromotionSkuBean> listPromotionSku = new ArrayList<>();
        skusList.forEach(sku -> {
            BaseMongoMap<String, Object> mapSkuPlatform = getPlatformSkuMongo(listSkuMongo, sku.getSkuCode());
            CmsBtPromotionSkuBean cmsBtPromotionSkuModelBean = new CmsBtPromotionSkuBean(productInfo, groupModel, promotionId, modifier, sku.getSkuCode(), 0);
            cmsBtPromotionSkuModelBean.setNumIid(numIId);
            cmsBtPromotionSkuModelBean.setSize(sku.getSize());
            if (mapSkuPlatform != null && Boolean.valueOf(mapSkuPlatform.getStringAttribute("isSale"))) {
                Double priceMsrp = mapSkuPlatform.getDoubleAttribute("priceMsrp");
                Double priceRetail = mapSkuPlatform.getDoubleAttribute("priceRetail");
                Double priceSale = mapSkuPlatform.getDoubleAttribute("priceSale");
                cmsBtPromotionSkuModelBean.setMsrpRmb(new BigDecimal(priceMsrp));
                cmsBtPromotionSkuModelBean.setRetailPrice(new BigDecimal(priceRetail));
                cmsBtPromotionSkuModelBean.setSalePrice(new BigDecimal(priceSale));
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
            }
        });
        return  listPromotionSku;
    }
    CmsBtPromotionCodesModel loadCmsBtPromotionCodesModel(CmsBtProductModel productInfo, CmsBtProductGroupModel groupModel, int promotionId, String modifier, int cartId) {
        CmsBtPromotionCodesModel codesModel = get(promotionId, productInfo.getCommon().getFields().getCode());
        if(codesModel==null)codesModel=new CmsBtPromotionCodesModel();
        String numIId = groupModel == null ? null : groupModel.getNumIId();
        // 插入cms_bt_promotion_code表
        CmsBtPromotionCodesBean cmsBtPromotionCodesBean = new CmsBtPromotionCodesBean(productInfo, groupModel, promotionId, modifier, cartId);
        codesModel.setNumIid(numIId);
        //cmsBtPromotionCodesBean.setPromotionPrice(promotionPrice);
        //cmsBtPromotionCodesBean.setTagId(tagId == null ? 0 : tagId);

        codesModel.setProductId(ConvertUtil.toInt(productInfo.getProdId()));
        codesModel.setProductCode(productInfo.getCommon().getFields().getCode());
        codesModel.setProductName(com.taobao.api.internal.util.StringUtils.isEmpty(productInfo.getCommon().getFields().getOriginalTitleCn()) ? productInfo.getCommon().getFields().getProductNameEn() : productInfo.getCommon().getFields().getOriginalTitleCn());
//        this.setProductName(productInfo.getFields().getProductNameEn());
        CmsBtProductModel_Platform_Cart ptfObj = productInfo.getPlatform(cartId);

        if (ptfObj != null &&ptfObj.getSkus() != null &&!ptfObj.getSkus().isEmpty()) {
            codesModel.setSalePrice(ptfObj.getSkus().get(0).getDoubleAttribute("priceSale"));
            codesModel.setRetailPrice(ptfObj.getSkus().get(0).getDoubleAttribute("priceRetail"));
            codesModel.setMsrp(ptfObj.getSkus().get(0).getDoubleAttribute("priceMsrp"));
        }
        codesModel.setCatPath(productInfo.getCommon().getCatPath());

        // ProductModel
        codesModel.setProductModel(productInfo.getCommon().getFields().getModel());


        //codesModel.set.setSynFlg("0");

        codesModel.setPromotionId(promotionId);
        codesModel.setOrgChannelId(productInfo.getOrgChannelId());

        codesModel.setCreater(modifier);

        codesModel.setModifier(modifier);
        if (groupModel != null) {
            // numIid
            codesModel.setNumIid(groupModel.getNumIId());
            // modelId
            codesModel.setModelId(groupModel.getGroupId().intValue());
        }

        List<CmsBtProductModel_Field_Image> imgList = productInfo.getCommonNotNull().getFieldsNotNull().getImages6();
        if (!imgList.isEmpty()) {
            codesModel.setImageUrl1(imgList.get(0).getName());
        }
        if(StringUtil.isEmpty(codesModel.getImageUrl1())){
            imgList = productInfo.getCommonNotNull().getFieldsNotNull().getImages1();
            if (!imgList.isEmpty()) {
                codesModel.setImageUrl1(imgList.get(0).getName());
            }
        }

        return codesModel;
    }
    CmsBtPromotionCodesModel get(int promotionId,String code) {
        Map<String, Object> map = new HashedMap();
        map.put("promotionId", promotionId);
        map.put("productCode", code);
        return daoCmsBtPromotionCodes.selectOne(map);
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
    public void insertPromotionGroup(CmsBtPromotionGroupsBean cmsBtPromotionGroupsBean,List<CmsBtTagModel> tagModelList) {

        cmsPromotionModelDao.insertPromotionModel(cmsBtPromotionGroupsBean);

        for (CmsBtPromotionCodesBean code : cmsBtPromotionGroupsBean.getCodes()) {
            code.setPromotionId(cmsBtPromotionGroupsBean.getPromotionId());
            code.setNumIid(cmsBtPromotionGroupsBean.getNumIid());
            code.setModifier(cmsBtPromotionGroupsBean.getModifier());
            code.setModified(cmsBtPromotionGroupsBean.getModified());
            code.setModelId(cmsBtPromotionGroupsBean.getModelId());
            code.setChannelId(cmsBtPromotionGroupsBean.getChannelId());
            code.setOrgChannelId(cmsBtPromotionGroupsBean.getOrgChannelId());
            int codesId=0;
            CmsBtPromotionCodesModel codesModel = get(cmsBtPromotionGroupsBean.getPromotionId(),code.getProductCode());
            if(codesModel==null)
            {
                cmsPromotionCodeDao.insertPromotionCode(code);
                codesId=code.getId();
            }
            else {
                codesId = codesModel.getId();
                code.setId(codesId);
                cmsPromotionCodeDao.updatePromotionCode(code);
            }

            //CmsBtTagModel tag = searchTag(tags, code.getTag());
            UpdatePromotionProductTagParameter tagParameter = getUpdatePromotionProductTagParameter(tagModelList, code);
            if (tagParameter.getTagList().size()>0) {
                promotionCodesTagService.updatePromotionProductTag(tagParameter, code.getChannelId(), code.getModifier());
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
    public UpdatePromotionProductTagParameter  getUpdatePromotionProductTagParameter(List<CmsBtTagModel> tagModelList,CmsBtPromotionCodesBean code) {

        UpdatePromotionProductTagParameter parameter = new UpdatePromotionProductTagParameter();

        parameter.setId(code.getId());

        parameter.setTagList(new ArrayList<>());

        if(org.springframework.util.StringUtils.isEmpty(code.getTag()))
        {
            return parameter;
        }
        String[] tagList = code.getTag().split("\\|");

        for (String s : tagList) {
            CmsBtTagModel tag = searchTag(tagModelList, s);
            if (tag != null) {
                ProductTagInfo tagInfo = new ProductTagInfo();
                tagInfo.setTagId(tag.getId());
                tagInfo.setTagName(tag.getTagName());
                tagInfo.setChecked(2);//增加
                parameter.getTagList().add(tagInfo);
            }
        }

        return parameter;

    }
    private CmsBtTagModel searchTag(List<CmsBtTagModel> tags, String tagName) {

        for (CmsBtTagModel tag : tags) {
            if (tag.getTagName().equalsIgnoreCase(tagName)) {
                return tag;
            }
        }
        return null;
    }

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

            // votodo: 2016/11/10   删除tag处理    新增tag表 cms_bt_promotion_codes_tag
            // 删除旧的的TAG 插入新的TAG
//            CmsBtTagModel modelTag = daoTag.select(oldPromotionCodesModel.getTagId());//获取修改前的tag
//            if(modelTag == null){
//                updateCmsBtProductTags(promotionCodeModel, null,modifier);//更新商品Tags
//            }else{
//                updateCmsBtProductTags(promotionCodeModel, modelTag.getTagPath(),modifier);//更新商品Tags
//            }
        }
    }
    /**
     * 更新商品Tags
     */
//    private void updateCmsBtProductTags(CmsBtPromotionCodesBean promotionCodeModel,String oldTagPath,String modifier) {
//        //更新商品Tags  sunpt
//        CmsBtProductModel productModel = productService.getProductByCode(promotionCodeModel.getOrgChannelId(), promotionCodeModel.getProductCode());
//        if(productModel != null) {
//            List<String> tags = productModel.getTags();
//            int size = tags.size();
//            boolean isUpdate = false;
//            if(!StringUtil.isEmpty(oldTagPath)) {
//                for (int i = 0; i < size; i++) {
//                    if (oldTagPath.equals(tags.get(i))) {
//                        //存在替换
//                        tags.set(i, promotionCodeModel.getTagPath());
//                        isUpdate = true;
//                        break;
//                    }
//                }
//            }
//            if (!isUpdate)//没有更新 就添加
//            {
//                tags.add(promotionCodeModel.getTagPath());
//            }
//            productModel.setTags(tags);
//            productService.updateTags(promotionCodeModel.getOrgChannelId(), promotionCodeModel.getProductId(), tags, modifier);
//        }
//       //productService.update(productModel);
//    }

    /**
     * 删除
     */
    @VOTransactional
    public void remove(String channelId, List<CmsBtPromotionGroupsBean> promotionModes, String modifier) {
        if (promotionModes.size() == 0) return;
        CmsBtPromotionModel promotionModel = promotionService.getByPromotionId(promotionModes.get(0).getPromotionId());

        Date  activityStart= DateTimeUtil.parse(promotionModel.getActivityStart(),"yyyy-MM-dd");

        if(DateTimeUtilBeijing.toLocalTime(activityStart)<new Date().getTime())
        {
           throw  new  BusinessException("活动已经开始不能删除");
        }

        List<String> codeList = new ArrayList<>();
        for (CmsBtPromotionGroupsBean item : promotionModes) {
            cmsPromotionModelDao.deleteCmsPromotionModel(item);
            HashMap<String, Object> param = new HashMap<>();
            param.put("promotionId", item.getPromotionId());
            param.put("modelId", item.getModelId());

            List<CmsBtPromotionCodesBean> codes = cmsPromotionCodeDao.selectPromotionCodeList(param);
            codes.forEach(code -> {
                codeList.add(code.getProductCode());
//                List<Long> prodIdList = new ArrayList<>();
//                prodIdList.add(code.getProductId());
//                productTagService.delete(channelId, code.getTagPath(), prodIdList, "tags", modifier);

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
        //批量删除tag
        promotionCodesTagService.deleteListByPromotionId_Codes(channelId, promotionModel.getPromotionId(), codeList, promotionModel.getRefTagId());
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
        // votodo tag改造 增加表  cms_bt_promotion_codes_tag
        //request.setTagId(cmsBtPromotionCodesBean.getTagId());
        //request.setTagPath(cmsBtPromotionCodesBean.getTagPath());

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

            // votodo: 2016/11/10     tag 改造   增加表 cms_bt_promotion_codes_tag

           // request.setTagId(cmsBtPromotionCodesBean.getTagId());
            //request.setTagPath(cmsBtPromotionCodesBean.getTagPath());

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
        if(promotionModes.size()==0) return;

        CmsBtPromotionModel promotionModel = promotionService.getByPromotionId(promotionModes.get(0).getPromotionId());

        Date  activityStart= DateTimeUtil.parse(promotionModel.getActivityStart(),"yyyy-MM-dd");

        if(DateTimeUtilBeijing.toLocalTime(activityStart)<new Date().getTime())
        {
            throw  new  BusinessException("活动已经开始不能删除");
        }
        List<String> codeList=new ArrayList<>();

        for (CmsBtPromotionCodesBean item : promotionModes) {

            codeList.add(item.getProductCode());

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

//            List<Long> poIds = new ArrayList<>();
//            poIds.add(item.getProductId());
//            if (!StringUtil.isEmpty(item.getTagPath())) {
//                productTagService.delete(channelId, item.getTagPath(), poIds, "tags", operator);
//            }
        }

        //批量删除tag
        promotionCodesTagService.deleteListByPromotionId_Codes(channelId,promotionModel.getPromotionId(),codeList,promotionModel.getRefTagId());
    }

    @VOTransactional
   public  void  deleteFromPromotion(CmsBtPromotionModel promotion, AddProductSaveParameter parameter) {

        Map<String, Object> map = new HashMap<>();
        map.put("listProductCode", parameter.getCodeList());
        map.put("promotionId", promotion.getId());

        //批量删除 promotionCodesTag
        promotionCodesTagService.batchDeleteByCodes(parameter.getCodeList(), promotion.getPromotionId());
        //批量删除 code
        daoExtCamelCmsBtPromotionCodes.deleteByPromotionCodeList(map);
        //批量删除 sku
        daoExtCamelCmsBtPromotionSkus.deleteByPromotionCodeList(map);
        //批量删除  product tag
        productService.removeTagByCodes(promotion.getChannelId(), parameter.getCodeList(), promotion.getRefTagId());

        // `cms_bt_promotion_codes_tag`
        // `cms_bt_promotion_skus`
        // `cms_bt_promotion_codes`
        //group不删除

    }
    public Map init(InitParameter params, String channelId,List<String> codeList) {
        Map<String, Object> data = new HashedMap();
        int cartId = params.getCartId();

        List<TagTreeNode> listTagTreeNode = new ArrayList<>();
        List<MapModel> list = cmsBtPromotionDaoExtCamel.selectAddPromotionList(channelId, cartId,codeList, params.getActivityStart(), params.getActivityEnd());
        list.forEach(m -> listTagTreeNode.add(getPromotionTagTreeNode(m, codeList)));

        data.put("listTreeNode", listTagTreeNode);
        return data;
    }
    //获取活动的节点数据
    TagTreeNode getPromotionTagTreeNode(MapModel model, List<String> codeList) {
        int codeCount = codeList.size();
        int id = ConvertUtil.toInt(model.get("id"));
        int productCount = ConvertUtil.toInt(model.get("productCount"));
        TagTreeNode tagTreeNode = new TagTreeNode();
        tagTreeNode.setId(id);
        if (productCount > 0) {
            tagTreeNode.setChecked(productCount == codeCount ? 2 : 1);
        }
        tagTreeNode.setName(ConvertUtil.toString(model.get("promotionName")));
        tagTreeNode.setChildren(new ArrayList<>());
        List<TagCodeCountInfo> list = tagService.getListTagCodeCount(id, ConvertUtil.toInt(model.get("refTagId")), codeList);
        if (list.size() == 0) return tagTreeNode;
        list.forEach(f -> {
            TagTreeNode node = new TagTreeNode();
            node.setId(f.getId());
            node.setName(f.getTagName());
            if (f.getProductCount() > 0) {
                node.setChecked(f.getProductCount() == codeCount ? 2 : 1);//0:未选 1：半选 2全选
            }
            node.setOldChecked(node.getChecked());
            tagTreeNode.getChildren().add(node);
        });
        tagTreeNode.setOldChecked(tagTreeNode.getChecked());
        return tagTreeNode;
    }
}
