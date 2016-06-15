package com.voyageone.service.impl.cms.jumei;

import com.google.common.base.Preconditions;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.jumei.CmsBtJmPromotionSaveBean;
import com.voyageone.service.bean.cms.jumei.ProductImportBean;
import com.voyageone.service.bean.cms.jumei.SkuImportBean;
import com.voyageone.service.dao.cms.CmsBtPromotionDao;
import com.voyageone.service.dao.cms.CmsBtTagDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.CmsBtJmMasterBrandDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionDaoExt;
import com.voyageone.service.daoext.synship.SynshipComMtValueChannelDao;
import com.voyageone.service.impl.cms.CmsMtChannelValuesService;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionImportTask3Service;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import com.voyageone.service.model.cms.CmsBtJmMasterBrandModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.bean.cms.businessmodel.CmsBtJmImportSpecialImage;
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
        if (model.getRefTagId() != 0) {
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
        if (com.voyageone.common.util.StringUtils.isEmpty(parameter.getModel().getActivityAppId())) {
            parameter.getModel().setActivityAppId("");
        }
        if (com.voyageone.common.util.StringUtils.isEmpty(parameter.getModel().getActivityPcId())) {
            parameter.getModel().setActivityPcId("");
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
            insertModel(parameter);
            saveCmsBtPromotion(parameter.getModel());
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
        modelTag.setTagPath("");
        modelTag.setTagPathName("");
        modelTag.setModifier(model.getModifier());
        //Tag追加  活动名称
         daoCmsBtTag.insert(modelTag);
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
        return daoExt.getListByWhere(map);
    }

    public List<MapModel> getJMActivePromotions(String channelId) {
        Preconditions.checkArgument(StringUtils.isNotBlank(channelId), "channelId不能为空!");
        return daoExt.getActivesOfChannel(channelId);
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
    public void addProductionToPromotion(List<Long> productIds, CmsBtJmPromotionModel promotion, String channelId,
                                         Double discount,
                                         Integer priceType,
                                         String tag,
                                         String creater) {

        if (productIds == null || productIds.size() == 0) {
            log.warn("LOG00010:no product for adding to jumei promotion");
            return;
        }
        List<CmsBtProductModel> orginProducts = productDao.selectProductByIds(productIds, channelId);
        List<ProductImportBean > listProductImport = new ArrayList<>();
        List< SkuImportBean > listSkuImport = new ArrayList<>();
        orginProducts.stream().forEach(product -> { //pal
            ProductImportBean productImportBean = buildProductFrom(product, promotion);
            productImportBean.setPromotionTag(tag);
            listProductImport.add(productImportBean);
            listSkuImport.addAll(buildSkusFrom(product, discount, priceType));
        });


        cmsBtJmPromotionImportTask3Service.saveImport(promotion,listProductImport,listSkuImport);


    }


    //    @Resource
//    ComMtValueChannelDao comMtValueChannelDao;
    @Resource
    SynshipComMtValueChannelDao synshipComMtValueChannelDao;

    private ProductImportBean buildProductFrom(CmsBtProductModel model,CmsBtJmPromotionModel promotion) {
        CmsBtProductModel_Field fields = model.getFields();
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

        final Double discountCopy = discount > 1 || discount < 0 ? 1 : discount;
        final Integer priceTypeCopy = priceType == 2 ? priceType : 1;

        return model.getSkus().stream().map(oldSku -> {
            SkuImportBean bean = new SkuImportBean();
            bean.setProductCode(model.getFields().getCode());
            bean.setSkuCode(oldSku.getSkuCode());
            bean.setMarketPrice(oldSku.getPriceMsrp());
            Double finalPrice = Math.ceil(priceTypeCopy == 1 ? (oldSku.getPriceMsrp() * discountCopy) : (oldSku.getPriceSale() * discountCopy));
            bean.setDealPrice(finalPrice);
            return bean;
        }).collect(Collectors.toList());
    }

    /**
     * 对应mongo表中fields.images1数据构建需要插入到聚美活动的图片
     * @param model
     * @return
     */
    private CmsBtJmImportSpecialImage buildImagesFrom(CmsBtProductModel model) {


        CmsBtJmImportSpecialImage image = new CmsBtJmImportSpecialImage();
        image.setProductCode(model.getFields().getCode());
        List<CmsBtProductModel_Field_Image> images = null;
        if (model == null || model.getFields() == null || (images = model.getFields().getImages1()) == null) {
            return image;
        }
        image.setProductCode(model.getFields().getCode()); // productCode
        for (int i = 0; i < Math.min(images.size(),10); i++) {
            try {
                java.lang.reflect.Method method = CmsBtJmImportSpecialImage.class.getMethod("setProductImageUrlKey" + (i+1), new Class[]{String.class});
                method.invoke(image, images.get(i).getName());
            } catch (Exception e) {
                //pass
                log.error("LOG00050:CmsBtJmImportSpecialImage.setProductImageUrlKey"+i+" 方法不存在", e);
            }
        }
        return image;
    }

    //------------------聚美活动新增商品end-------------------------------------------------------------------------------
}

