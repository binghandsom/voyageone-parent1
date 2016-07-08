package com.voyageone.service.impl.cms.jumei;

import com.google.common.base.Preconditions;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.jumei.CmsBtJmPromotionSaveBean;
import com.voyageone.service.bean.cms.jumei.ProductImportBean;
import com.voyageone.service.bean.cms.jumei.SkuImportBean;
import com.voyageone.service.dao.cms.CmsBtJmMasterBrandDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionDao;
import com.voyageone.service.dao.cms.CmsBtPromotionDao;
import com.voyageone.service.dao.cms.CmsBtTagDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.daoext.cms.CmsBtJmProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionDaoExt;
import com.voyageone.service.daoext.synship.SynshipComMtValueChannelDao;
import com.voyageone.service.impl.cms.CmsMtChannelValuesService;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionImportTask3Service;
import com.voyageone.service.model.cms.*;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.util.MapModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotionService {
    private static final Logger log = LoggerFactory.getLogger(CmsBtJmPromotionService.class);
    @Autowired
    CmsBtJmPromotionDao dao;
    @Autowired
    CmsBtJmMasterBrandDao daoCmsBtJmMasterBrand;
    @Autowired
    CmsBtJmPromotionDaoExt daoExt;
    @Autowired
    CmsBtTagDao daoCmsBtTag;
    @Autowired
    CmsBtPromotionDao daoCmsBtPromotion;
    @Autowired
    CmsBtJmProductDaoExt cmsBtJmProductDaoExt;
    public Map<String, Object> init() {
        Map<String, Object> map = new HashMap<>();
        List<CmsBtJmMasterBrandModel> jmMasterBrandList = daoCmsBtJmMasterBrand.selectList(new HashMap<String, Object>());
        map.put("jmMasterBrandList", jmMasterBrandList);
        return map;
    }
    public CmsBtJmPromotionModel select(int id) {
        return dao.select(id);
    }
    @VOTransactional
   public void delete(int id) {
       CmsBtJmPromotionModel model = dao.select(id);
       model.setActive(0);
       dao.update(model);
       saveCmsBtPromotion(model);
   }
    public int update(CmsBtJmPromotionModel entity) {
        return dao.update(entity);
    }

    public int insert(CmsBtJmPromotionModel entity) {
        return dao.insert(entity);
    }

    public CmsBtJmPromotionSaveBean getEditModel(int id) {
        CmsBtJmPromotionSaveBean info = new CmsBtJmPromotionSaveBean();
        CmsBtJmPromotionModel model = dao.select(id);
        info.setModel(model);
        if (model.getRefTagId()!=null&&model.getRefTagId() != 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("parentTagId", model.getRefTagId());
            map.put("active", 1);
            List<CmsBtTagModel> tagList = daoCmsBtTag.selectList(map);
            info.setTagList(tagList);
        }
        return info;
    }
    @VOTransactional
    public int saveModel(CmsBtJmPromotionSaveBean parameter,String userName, String channelId) {
        parameter.getModel().setChannelId(channelId);
        if (parameter.getModel().getActivityAppId()==null) {
            parameter.getModel().setActivityAppId(0L);
        }
        if (parameter.getModel().getActivityPcId()==null) {
            parameter.getModel().setActivityPcId(0L);
        }
        if (com.voyageone.common.util.StringUtils.isEmpty(parameter.getModel().getBrand())) {
            parameter.getModel().setBrand("");
        }
        if (com.voyageone.common.util.StringUtils.isEmpty(parameter.getModel().getCategory())) {
            parameter.getModel().setCategory("");
        }
        if (parameter.getModel().getId() != null && parameter.getModel().getId() > 0) {//更新
            parameter.getModel().setModifier(userName);
            updateModel(parameter);
            saveCmsBtPromotion(parameter.getModel());
        } else {//新增
            parameter.getModel().setModifier(userName);
            parameter.getModel().setCreater(userName);
            Map<String,Object> param = new HashMap<>();
            param.put("channelId",parameter.getModel().getChannelId());
            param.put("name",parameter.getModel().getName());
            List<MapModel> model = getListByWhere(param);
            if(model == null || model.size() == 0){
                insertModel(parameter);
                saveCmsBtPromotion(parameter.getModel());
            }else{
                throw new BusinessException("4000093");
            }
        }
        return 1;
    }
    public void  saveCmsBtPromotion(CmsBtJmPromotionModel model) {
        Map<String, Object> map = new HashMap<>();
        map.put("promotionId", model.getId());
        map.put("cartId", CartEnums.Cart.JM.getValue());
        CmsBtPromotionModel promotion = daoCmsBtPromotion.selectOne(map);
        if (promotion == null) {
            promotion = new CmsBtPromotionModel();
        }
        promotion.setPromotionId(model.getId());
        promotion.setRefTagId(model.getRefTagId());
        promotion.setChannelId(model.getChannelId());
        promotion.setModifier(model.getModifier());
        promotion.setCreater(model.getCreater());
        promotion.setActive(model.getActive());
        promotion.setActivityStart(DateTimeUtil.getDateTime(model.getActivityStart(), "yyyy-MM-dd HH:mm:ss"));
        promotion.setActivityEnd(DateTimeUtil.getDateTime(model.getActivityEnd(), "yyyy-MM-dd HH:mm:ss"));
        promotion.setCartId(CartEnums.Cart.JM.getValue());
        promotion.setPromotionName(model.getName());
        promotion.setPrePeriodStart(DateTimeUtil.getDateTime(model.getPrePeriodStart(), "yyyy-MM-dd HH:mm:ss"));
        promotion.setPrePeriodEnd(DateTimeUtil.getDateTime(model.getPrePeriodEnd(), "yyyy-MM-dd HH:mm:ss"));
        promotion.setPromotionStatus(0);
        promotion.setTejiabaoId("");
        promotion.setIsAllPromotion(0);
        promotion.setActive(model.getActive());
        if (promotion.getId() == null || promotion.getId() == 0) {
            daoCmsBtPromotion.insert(promotion);
        } else {
            daoCmsBtPromotion.update(promotion);
        }
    }
    /*更新
    * */
    private int updateModel(CmsBtJmPromotionSaveBean parameter) {
        int result;
        CmsBtJmPromotionModel model = parameter.getModel();
        if (model.getRefTagId() == 0) {
            int refTagId = addTag(model);
            model.setRefTagId(refTagId);
        }
        result = dao.update(parameter.getModel());
        parameter.getTagList().forEach(cmsBtTagModel -> {
            cmsBtTagModel.setModifier(parameter.getModel().getModifier());
            if (cmsBtTagModel.getId() != null && cmsBtTagModel.getId() > 0) {
                daoCmsBtTag.update(cmsBtTagModel);
            } else {
                cmsBtTagModel.setChannelId(model.getChannelId());
                cmsBtTagModel.setParentTagId(model.getRefTagId());
                cmsBtTagModel.setTagType(2);
                cmsBtTagModel.setTagStatus(0);
                cmsBtTagModel.setTagPathName(String.format("-%s-%s-", model.getName(), cmsBtTagModel.getTagName()));
                cmsBtTagModel.setTagPath("");
                cmsBtTagModel.setCreater(model.getModifier());
                cmsBtTagModel.setModifier(model.getModifier());
                daoCmsBtTag.insert(cmsBtTagModel);
                cmsBtTagModel.setTagPath(String.format("-%s-%s-", cmsBtTagModel.getParentTagId(), cmsBtTagModel.getId()));
                daoCmsBtTag.update(cmsBtTagModel);
            }
        });
        return result;
    }
    /**
     * 新增
     */
    private int insertModel(CmsBtJmPromotionSaveBean parameter) {
        CmsBtJmPromotionModel model = parameter.getModel();
        if(StringUtil.isEmpty(model.getCategory()))
        {
            model.setCategory("");
        }
        int refTagId = addTag(model);
        model.setRefTagId(refTagId);
        // 子TAG追加
        parameter.getTagList().forEach(cmsBtTagModel -> {
            cmsBtTagModel.setChannelId(model.getChannelId());
            cmsBtTagModel.setParentTagId(refTagId);
            cmsBtTagModel.setTagType(2);
            cmsBtTagModel.setTagStatus(0);
            cmsBtTagModel.setTagPathName(String.format("-%s-%s-", model.getName(), cmsBtTagModel.getTagName()));
            cmsBtTagModel.setTagPath("");
            cmsBtTagModel.setCreater(model.getCreater());
            cmsBtTagModel.setModifier(model.getCreater());
            daoCmsBtTag.insert(cmsBtTagModel);
            cmsBtTagModel.setTagPath(String.format("-%s-%s-", refTagId, cmsBtTagModel.getId()));
            daoCmsBtTag.update(cmsBtTagModel);
        });
        return dao.insert(model);
    }
    private int addTag(CmsBtJmPromotionModel model) {
        CmsBtTagModel modelTag = new CmsBtTagModel();
        modelTag.setChannelId(model.getChannelId());
        modelTag.setTagName(model.getName());
        modelTag.setTagType(2);
        modelTag.setTagStatus(0);
        modelTag.setParentTagId(0);
        modelTag.setSortOrder(0);
        modelTag.setTagPath(String.format("-%s-", ""));
        modelTag.setTagPathName(String.format("-%s-", model.getName()));
        modelTag.setModifier(model.getModifier());
        //Tag追加  活动名称
        daoCmsBtTag.insert(modelTag);
        modelTag.setTagPath(String.format("-%s-", modelTag.getId()));
        daoCmsBtTag.update(modelTag);
        return modelTag.getId();
    }

    public List<MapModel> getListByWhere(Map<String, Object> map) {
        if (map.containsKey("state1") && !((Boolean) map.get("state1")))//待进行
        {
            map.remove("state1");  //小于开始时间
        }
        if (map.containsKey("state2") && !((Boolean) map.get("state2")))//进行中
        {
            map.remove("state2"); // 当前时间大于开始时间 小于结束时间
        }
        if (map.containsKey("state3") && !((Boolean) map.get("state3")))//完成
        {
            map.remove("state3"); //当前时间大于结束时间
        }
        return daoExt.selectListByWhere(map);
    }

    public List<MapModel> getJMActivePromotions(String channelId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(channelId), "channelId不能为空!");
        return daoExt.selectActivesOfChannel(channelId);
    }


    //------------------聚美活动新增商品begin-----------------------------------------------------------------------------
    @Resource
    CmsBtJmPromotionImportTaskService jmPromotionImportTaskService;

    @Resource
    CmsBtProductDao productDao;  //用于获取mongo中的产品信息


    @Autowired
    CmsMtChannelValuesService channelValuesService; //获取渠道价格信息

    @Autowired
    CmsBtJmPromotionImportTask3Service cmsBtJmPromotionImportTask3Service;

    /**
     * 新增产品列表到聚美的产品项目中
     *
     * @param channelId
     * @param creater   创建人
     */
    public List<String> addProductionToPromotion(List<Long> productIds, CmsBtJmPromotionModel promotion, String channelId,
                                         Double discount,
                                         Integer priceType,
                                         String tagName,
                                         String tagId,
                                         String creater) throws IllegalAccessException {

        if (productIds == null || productIds.size() == 0) {
            log.warn("LOG00010:no product for adding to jumei promotion");
            return null;
        }
        List<CmsBtProductModel> orginProducts = productDao.selectProductByIds(productIds, channelId);
        List<CmsBtProductModel> products = new ArrayList<>();


        // 检查之前有没有上新到聚美上面
        List<String> errCodes = new ArrayList();
        List<String>productCodes = new ArrayList<>();
        orginProducts.forEach(item -> productCodes.add(item.getCommon().getFields().getCode()));
        List<CmsBtJmProductModel> cmsBtJmProductModels = cmsBtJmProductDaoExt.selectByProductCodeListChannelId(productCodes, channelId);
        if(cmsBtJmProductModels == null || orginProducts.size() != cmsBtJmProductModels.size())
        {
            for(CmsBtProductModel orginProduct :orginProducts){
                boolean flg =false;
                for(CmsBtJmProductModel cmsBtJmProductModel :cmsBtJmProductModels){
                    if(orginProduct.getCommon().getFields().getCode().equalsIgnoreCase(cmsBtJmProductModel.getProductCode())){
                        flg = true;
                        products.add(orginProduct);
                        break;
                    }
                }
                if(!flg){
                    errCodes.add(orginProduct.getCommon().getFields().getCode());
                }
            }
        }else{
            products = orginProducts;
        }

        List<ProductImportBean > listProductImport = new ArrayList<>();
        List< SkuImportBean > listSkuImport = new ArrayList<>();

        // 设置批量更新product的tag
        List<BulkUpdateModel> bulkList = new ArrayList<>();

        products.stream().forEach(product -> { //pal
            ProductImportBean productImportBean = buildProductFrom(product, promotion);
            productImportBean.setPromotionTag(tagName);
            productImportBean.setDiscount(discount);
            listProductImport.add(productImportBean);
            listSkuImport.addAll(buildSkusFrom(product, discount, priceType));

            if (!product.getTags().contains(tagId))
                bulkList.add(buildBulkUpdateTag(product, tagId, creater));
        });
        List<Map<String, Object>> listSkuErrorMap = new ArrayList<>();//;错误行集合
        List<Map<String, Object>> listProducctErrorMap = new ArrayList<>();//错误行集合
        // 插入jm的promotion信息
        cmsBtJmPromotionImportTask3Service.saveImport(promotion,listProductImport,listSkuImport,listProducctErrorMap,listSkuErrorMap,promotion.getModifier(),false);

        // 批量更新product表
        if (bulkList.size() > 0) {
            productDao.bulkUpdateWithMap(channelId, bulkList, null, "$set", true);
        }
        return errCodes;
    }


    //    @Resource
//    ComMtValueChannelDao comMtValueChannelDao;
    @Resource
    SynshipComMtValueChannelDao synshipComMtValueChannelDao;

    private ProductImportBean buildProductFrom(CmsBtProductModel model,CmsBtJmPromotionModel promotion) {
        CmsBtProductModel_Field fields = model.getCommon().getFields();
        ProductImportBean bean = new ProductImportBean();
        bean.setAppId(promotion.getActivityAppId());
        bean.setPcId(promotion.getActivityPcId());
        bean.setLimit(0);
        bean.setProductCode(fields.getCode());
        return bean;
    }

    /**
     * @param model
     * @param discount  折扣,这里是正折扣,即直接计算而不是用减法,如 10元,discount为0.7那么 就是7元,而不是3元
     * @param priceType 1 表示用官方价(Msrp)打折,2表示用销售价(Sale Price)
     * @return
     */
    private List<SkuImportBean> buildSkusFrom(CmsBtProductModel model, Double discount, Integer priceType) {


        final Integer priceTypeCopy = priceType == 2 ? priceType : 1;

        return model.getCommon().getSkus().stream().map(oldSku -> {
            SkuImportBean bean = new SkuImportBean();
            bean.setProductCode(model.getCommon().getFields().getCode());
            bean.setSkuCode(oldSku.getSkuCode());
            bean.setMarketPrice(oldSku.getPriceMsrp());
            Double finalPrice;
            if (discount != null) {
                final Double discountCopy = discount > 1 || discount < 0 ? 1 : discount;
                finalPrice = Math.ceil(priceTypeCopy == 1 ? (oldSku.getPriceMsrp() * discountCopy) : (oldSku.getClientRetailPrice() * discountCopy));
            } else {
                finalPrice = oldSku.getClientRetailPrice();//.getPriceSale();
            }
            bean.setDealPrice(finalPrice);
            bean.setDiscount(discount);
            return bean;
        }).collect(Collectors.toList());
    }

    /**
     * 设置批量更新product的tags标签
     * @param model
     * @param tagId
     * @param creater
     * @return
     */
    private BulkUpdateModel buildBulkUpdateTag(CmsBtProductModel model, String tagId, String creater) {


        HashMap<String, Object> bulkQueryMap = new HashMap<>();
        if(model.getCommon() != null && model.getCommon().size() >0) {
            bulkQueryMap.put("common.fields.code", model.getCommon().getFields().getCode());
        }else{
            bulkQueryMap.put("common.fields.code", model.getCommon().getFields().getCode());
        }

        // 设置更新值
        HashMap<String, Object> bulkUpdateMap = new HashMap<>();
        List<String> newTags = model.getTags();
        newTags.add(tagId);

        bulkUpdateMap.put("tags", newTags);

        // 设定批量更新条件和值
        BulkUpdateModel bulkUpdateModel = new BulkUpdateModel();
        bulkUpdateModel.setUpdateMap(bulkUpdateMap);
        bulkUpdateModel.setQueryMap(bulkQueryMap);
        return bulkUpdateModel;
    }

    //------------------聚美活动新增商品end-------------------------------------------------------------------------------
}

