package com.voyageone.service.impl.cms.jumei;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.voyageone.service.dao.cms.CmsMtChannelValuesDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.jumei.CmsBtJmMasterBrandDao;
import com.voyageone.service.dao.jumei.CmsBtJmPromotionDao;
import com.voyageone.service.daoext.jumei.CmsBtJmPromotionDaoExt;
import com.voyageone.service.daoext.synship.SynshipComMtValueChannelDao;
import com.voyageone.service.impl.cms.CmsMtChannelValuesService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import com.voyageone.service.model.jumei.CmsBtJmMasterBrandModel;
import com.voyageone.service.model.jumei.CmsBtJmPromotionModel;
import com.voyageone.service.model.jumei.businessmodel.CmsBtJmImportProduct;
import com.voyageone.service.model.jumei.businessmodel.CmsBtJmImportSku;
import com.voyageone.service.model.jumei.businessmodel.CmsBtJmImportSpecialImage;
import com.voyageone.service.model.jumei.businessmodel.JmProductImportAllInfo;
import com.voyageone.service.model.util.MapModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    public Map<String, Object> init() {
        Map<String, Object> map = new HashMap<>();
        List<CmsBtJmMasterBrandModel> jmMasterBrandList = daoCmsBtJmMasterBrand.selectList(new HashMap<String, Object>());
        map.put("jmMasterBrandList", jmMasterBrandList);
        return map;
    }

    public CmsBtJmPromotionModel select(int id) {
        return dao.select(id);
    }

    public int update(CmsBtJmPromotionModel entity) {
        return dao.update(entity);
    }

    public int insert(CmsBtJmPromotionModel entity) {
        return dao.insert(entity);
    }

    public List<MapModel> getListByWhere(Map<String, Object> map) {
        if (map.containsKey("state1") && map.get("state1").equals("false"))//待进行
        {
            map.remove("state1");  //小于开始时间
        }
        if (map.containsKey("state2") && map.get("state1").equals("false"))//进行中
        {
            map.remove("state2"); // 当前时间大于开始时间 小于结束时间
        }
        if (map.containsKey("state3") && map.get("state1").equals("false"))//完成
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

    /**
     * 新增产品列表到聚美的产品项目中
     *
     * @param channelId
     * @param creater   创建人
     */
    public void addProductionToPromotion(List<Long> productIds, CmsBtJmPromotionModel promotion, String channelId,
                                         Double discount,
                                         Integer priceType,
                                         String creater) {

        if (productIds == null || productIds.size() == 0) {
            log.warn("LOG00010:no product for adding to jumei promotion");
            return;
        }

        JmProductImportAllInfo importInfo = buildJmProductImportAllInfo(productIds, promotion, channelId, discount, priceType);

//        System.out.println(importInfo);

        jmPromotionImportTaskService.saveJmProductImportAllInfo(importInfo,creater);


    }

    protected JmProductImportAllInfo buildJmProductImportAllInfo(List<Long> productIds, CmsBtJmPromotionModel promotion, String channelId, Double discount, Integer priceType) {
        List<CmsBtProductModel> orginProducts = productDao.selectProductByIds(productIds, channelId);
        JmProductImportAllInfo importInfo = new JmProductImportAllInfo();
        importInfo.setModelCmsBtJmPromotion(promotion);
        orginProducts.parallelStream().forEach(product -> {
            importInfo.getListProductModel().add(buildProductFrom(product));
            importInfo.getListSkuModel().addAll(buildSkusFrom(product, discount, priceType));
            importInfo.getListSpecialImageModel().add(buildImagesFrom(product));
        });
        return importInfo;
    }


    //    @Resource
//    ComMtValueChannelDao comMtValueChannelDao;
    @Resource
    SynshipComMtValueChannelDao synshipComMtValueChannelDao;

    private CmsBtJmImportProduct buildProductFrom(CmsBtProductModel model) {
        CmsBtProductModel_Field fields = model.getFields();
        CmsBtJmImportProduct bean = new CmsBtJmImportProduct();
        if (fields != null) {
            bean.setChannelId(model.getChannelId());
            bean.setProductCode(fields.getCode());
            bean.setProductNameCn(fields.getProductNameCn());
            bean.setProductLongName(fields.getLongTitle());
            bean.setProductMediumName(fields.getMiddleTitle());
            bean.setProductShortName(fields.getShortTitle());
            bean.setBrandName(fields.getBrand());
            bean.setProductType(fields.getProductType());
            bean.setSizeType(fields.getSizeType());
            bean.setColorEn(fields.getColor());
            bean.setProductDesCn(fields.getLongDesCn());
            bean.setProductDesEn(fields.getLongDesEn());

            if (fields.getHsCodePrivate() != null) {  //海关编号,名称,和单位
                String value = synshipComMtValueChannelDao.selectName(fields.getHsCodePrivate(), 43, "en",model.getChannelId());
                if (StringUtils.isNotBlank(value)) {
                    List<String> props = Splitter.on(",").splitToList(value);
                    bean.setHsCode(props.size() > 0 ? props.get(0) : null);
                    bean.setHsName(props.size() > 1 ? props.get(1) : null);
                    bean.setHsUnit(props.size() > 2 ? props.get(2) : null);
                }
            }
        }
        return bean;
    }

    /**
     * @param model
     * @param discount  折扣,这里是正折扣,即直接计算而不是用减法,如 10元,discount为0.7那么 就是7元,而不是3元
     * @param priceType 1 表示用官方价(Msrp)打折,2表示用销售价(Sale Price)
     * @return
     */
    private List<CmsBtJmImportSku> buildSkusFrom(CmsBtProductModel model, Double discount, Integer priceType) {

        final Double discountCopy = discount > 1 || discount < 0 ? 1 : discount;
        final Integer priceTypeCopy = priceType == 2 ? priceType : 1;

        return model.getSkus().parallelStream().map(oldSku -> {
            CmsBtJmImportSku bean = new CmsBtJmImportSku();
            bean.setProductCode(model.getFields().getCode());
            bean.setJmSkuNo(oldSku.getSkuCode());
            bean.setUpc(oldSku.getBarcode());
            bean.setCmsSize((oldSku.getSize()));
            bean.setMarketPrice(oldSku.getPriceMsrp());
            Double finalPrice = Math.ceil(priceTypeCopy == 1 ? (oldSku.getPriceMsrp() * discountCopy) : (oldSku.getPriceSale() * discountCopy));
            bean.setDealPrice(finalPrice);
            return bean;
        }).collect(Collectors.toList());
    }

    /**
     * 对应mongo表中fields.images1数据
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

