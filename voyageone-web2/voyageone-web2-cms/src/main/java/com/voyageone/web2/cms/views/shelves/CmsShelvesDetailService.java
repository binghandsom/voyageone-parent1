package com.voyageone.web2.cms.views.shelves;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.service.bean.cms.CmsBtShelvesInfoBean;
import com.voyageone.service.impl.cms.CmsBtShelvesProductService;
import com.voyageone.service.impl.cms.CmsBtShelvesService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.web2.base.BaseViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by james on 2016/11/15.
 *
 * @version 2.10.0
 * @since 2.10.0
 */
@Service
class CmsShelvesDetailService extends BaseViewService {
    private final CmsBtShelvesService cmsBtShelvesService;
    private final CmsBtShelvesProductService cmsBtShelvesProductService;
    private final ProductService productService;
    private final MqSender sender;
    private final RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    public CmsShelvesDetailService(CmsBtShelvesProductService cmsBtShelvesProductService,
                                   CmsBtShelvesService cmsBtShelvesService,
                                   ProductService productService, RedisTemplate<Object, Object> redisTemplate,
                                   MqSender sender) {
        this.cmsBtShelvesProductService = cmsBtShelvesProductService;
        this.cmsBtShelvesService = cmsBtShelvesService;
        this.productService = productService;
        this.redisTemplate = redisTemplate;
        this.sender = sender;
    }

    /**
     * 根据货架Id获取货架里的产品信息
     */
    List<CmsBtShelvesInfoBean> getShelvesInfo(List<Integer> shelvesIds, Boolean isLoadPromotionPrice) {

        List<CmsBtShelvesInfoBean> cmsBtShelvesInfoBanList = new ArrayList<>();
        shelvesIds.forEach(shelvesId -> {

            //更新redis监控标志位的超时时间
            if (CacheHelper.getValueOperation().get("ShelvesMonitor_" + shelvesId) == null) {
                Map<String, Object> messageMap = new HashMap<>();
                messageMap.put("shelvesId", shelvesId);
                CacheHelper.getValueOperation().set("ShelvesMonitor_" + shelvesId, shelvesId);
                sender.sendMessage(MqRoutingKey.CMS_BATCH_ShelvesMonitorJob, messageMap);
            }
            redisTemplate.expire("ShelvesMonitor_" + shelvesId, 1, TimeUnit.MINUTES);

            CmsBtShelvesInfoBean cmsBtShelvesInfoBean = cmsBtShelvesProductService.getShelvesInfo(shelvesId, isLoadPromotionPrice);
            if (cmsBtShelvesInfoBean != null) {
                cmsBtShelvesInfoBanList.add(cmsBtShelvesInfoBean);
            }
        });
        return cmsBtShelvesInfoBanList;
    }

    /**
     * 产品加入货架
     */
    void addProducts(Integer shelvesId, List<String> productCodes, String modifier) {
        CmsBtShelvesModel cmsBtShelvesModel = cmsBtShelvesService.getId(shelvesId);

        if (cmsBtShelvesModel == null) {
            throw new BusinessException("货架不存在");
        }

        ShopBean shopBean = Shops.getShop(cmsBtShelvesModel.getChannelId(), cmsBtShelvesModel.getCartId());
        List<CmsBtShelvesProductModel> cmsBtShelvesProductModels = new ArrayList<>();
        productCodes.forEach(code -> {
            CmsBtProductModel productInfo = productService.getProductByCode(cmsBtShelvesModel.getChannelId(), code);
            CmsBtShelvesProductModel cmsBtShelvesProductModel = new CmsBtShelvesProductModel();
            CmsBtProductModel_Platform_Cart platform = productInfo.getPlatform(cmsBtShelvesModel.getCartId());
            String title = "";
            if (platform != null) {
                cmsBtShelvesProductModel.setNumIid(platform.getpNumIId());
                cmsBtShelvesProductModel.setSalePrice(platform.getpPriceSaleEd());

                if(shopBean.getPlatform_id().equalsIgnoreCase(PlatFormEnums.PlatForm.TM.getId())){
                    if(platform.getFields() != null)
                        title = platform.getFields().getStringAttribute("title");
                }else{
                    if(platform.getFields() != null)
                        title = platform.getFields().getStringAttribute("productTitle");
                }
            }

            cmsBtShelvesProductModel.setProductCode(code);
            cmsBtShelvesProductModel.setProductName(title == null?"":title);
            cmsBtShelvesProductModel.setCmsInventory(productInfo.getCommon().getFields().getQuantity());
            List<CmsBtProductModel_Field_Image> imgList = productInfo.getCommonNotNull().getFieldsNotNull().getImages6();
            if (!imgList.isEmpty() && imgList.get(0).size() > 0) {
                cmsBtShelvesProductModel.setImage(imgList.get(0).getName());
            } else {
                imgList = productInfo.getCommonNotNull().getFieldsNotNull().getImages1();
                if (!imgList.isEmpty()) {
                    cmsBtShelvesProductModel.setImage(imgList.get(0).getName());
                }
            }
            cmsBtShelvesProductModel.setShelvesId(shelvesId);
            cmsBtShelvesProductModel.setCreater(modifier);
            cmsBtShelvesProductModel.setModifier(modifier);
            cmsBtShelvesProductModels.add(cmsBtShelvesProductModel);
        });
        //更新数据库
        updateShelvesProduct(cmsBtShelvesProductModels);
    }

    private void updateShelvesProduct(List<CmsBtShelvesProductModel> cmsBtShelvesProductModels) {

        cmsBtShelvesProductModels.forEach(cmsBtShelvesProductModel -> {
            CmsBtShelvesProductModel oldShelvesProduct = cmsBtShelvesProductService.getByShelvesIdProductCode(cmsBtShelvesProductModel.getShelvesId(), cmsBtShelvesProductModel.getProductCode());
            if (null == oldShelvesProduct) {
                cmsBtShelvesProductModel.setSort(999);
                cmsBtShelvesProductService.insert(cmsBtShelvesProductModel);
            } else {
                oldShelvesProduct.setSalePrice(cmsBtShelvesProductModel.getSalePrice());
                oldShelvesProduct.setProductName(cmsBtShelvesProductModel.getProductName());
                oldShelvesProduct.setNumIid(cmsBtShelvesProductModel.getNumIid());
                oldShelvesProduct.setModifier(cmsBtShelvesProductModel.getModifier());
                oldShelvesProduct.setModified(new Date());
                if (oldShelvesProduct.getImage() ==null || !oldShelvesProduct.getImage().equalsIgnoreCase(cmsBtShelvesProductModel.getImage())) {
                    oldShelvesProduct.setImage(cmsBtShelvesProductModel.getImage());
                    oldShelvesProduct.setPlatformImageUrl("");
                }
                cmsBtShelvesProductService.update(oldShelvesProduct);
            }
        });
    }
}
