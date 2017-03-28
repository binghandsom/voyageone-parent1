package com.voyageone.web2.cms.views.shelves;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.CmsBtShelvesInfoBean;
import com.voyageone.service.impl.cms.CmsBtShelvesProductService;
import com.voyageone.service.impl.cms.CmsBtShelvesService;
import com.voyageone.service.impl.cms.CmsBtShelvesTemplateService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductTagService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsShelvesMonitorMQMessageBody;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import com.voyageone.service.model.cms.CmsBtShelvesTemplateModel;
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
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private final CmsBtShelvesTemplateService cmsBtShelvesTemplateService;
    private final ProductService productService;
    private final ProductTagService productTagService;
    private final CmsMqSenderService cmsMqSenderService;
    private final RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    public CmsShelvesDetailService(CmsBtShelvesProductService cmsBtShelvesProductService,
                                   CmsBtShelvesService cmsBtShelvesService,
                                   ProductService productService, RedisTemplate<Object, Object> redisTemplate,
                                   CmsMqSenderService cmsMqSenderService, CmsBtShelvesTemplateService cmsBtShelvesTemplateService, ProductTagService productTagService) {
        this.cmsBtShelvesProductService = cmsBtShelvesProductService;
        this.cmsBtShelvesService = cmsBtShelvesService;
        this.productService = productService;
        this.redisTemplate = redisTemplate;
        this.cmsMqSenderService = cmsMqSenderService;
        this.cmsBtShelvesTemplateService = cmsBtShelvesTemplateService;
        this.productTagService = productTagService;
    }

    /**
     * 根据货架Id获取货架里的产品信息
     */
    List<CmsBtShelvesInfoBean> getShelvesInfo(List<Integer> shelvesIds, Boolean isLoadPromotionPrice, String userName) {

        List<CmsBtShelvesInfoBean> cmsBtShelvesInfoBanList = new ArrayList<>();
        shelvesIds.forEach(shelvesId -> {

            //更新redis监控标志位的超时时间
            if (CacheHelper.getValueOperation().get("ShelvesMonitor_" + shelvesId) == null) {
                CmsShelvesMonitorMQMessageBody messageMap = new CmsShelvesMonitorMQMessageBody();
                messageMap.setShelvesId(shelvesId);
                messageMap.setSender(userName);
                CacheHelper.getValueOperation().set("ShelvesMonitor_" + shelvesId, shelvesId);
                cmsMqSenderService.sendMessage(messageMap);
            }
            redisTemplate.expire("ShelvesMonitor_" + shelvesId, 1, TimeUnit.MINUTES);

            CmsBtShelvesModel cmsBtShelvesModel = cmsBtShelvesService.getId(shelvesId);

            CmsBtShelvesInfoBean cmsBtShelvesInfoBean = cmsBtShelvesProductService.getShelvesInfo(cmsBtShelvesModel, isLoadPromotionPrice);
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

        List<Long> prodIdList = new ArrayList<>(productCodes.size());
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

                if (shopBean.getPlatform_id().equalsIgnoreCase(PlatFormEnums.PlatForm.TM.getId())) {
                    if (platform.getFields() != null)
                        title = platform.getFields().getStringAttribute("title");
                } else {
                    if (platform.getFields() != null)
                        title = platform.getFields().getStringAttribute("productTitle");
                }
            }

            cmsBtShelvesProductModel.setProductCode(code);
            cmsBtShelvesProductModel.setProductName(title == null ? "" : title);
            cmsBtShelvesProductModel.setCmsInventory(productInfo.getCommon().getFields().getQuantity());
            List<CmsBtProductModel_Field_Image> imgList = productInfo.getCommonNotNull().getFieldsNotNull().getImages6();
            if (!imgList.isEmpty() && imgList.get(0).size() > 0) {
                cmsBtShelvesProductModel.setImage(imgList.get(0).getName());
            }
            if(StringUtil.isEmpty(cmsBtShelvesProductModel.getImage())){
                imgList = productInfo.getCommonNotNull().getFieldsNotNull().getImages1();
                if (!imgList.isEmpty()) {
                    cmsBtShelvesProductModel.setImage(imgList.get(0).getName());
                }
            }
            cmsBtShelvesProductModel.setShelvesId(shelvesId);
            cmsBtShelvesProductModel.setCreater(modifier);
            cmsBtShelvesProductModel.setModifier(modifier);
            cmsBtShelvesProductModels.add(cmsBtShelvesProductModel);

            prodIdList.add(productInfo.getProdId());
        });
        //更新数据库
        updateShelvesProduct(cmsBtShelvesProductModels);

        if(!ListUtils.isNull(prodIdList)){
            productTagService.addProdTag(cmsBtShelvesModel.getChannelId(), "-" + cmsBtShelvesModel.getRefTagId() + "-", prodIdList);
        }
    }

    byte[] exportAppImage(Integer shelvesId) {

        CmsBtShelvesModel cmsBtShelvesModel = cmsBtShelvesService.getId(shelvesId);

        CmsBtShelvesInfoBean cmsBtShelvesInfoBean = cmsBtShelvesProductService.getShelvesInfo(cmsBtShelvesModel, false);

        if (cmsBtShelvesModel != null && cmsBtShelvesInfoBean.getShelvesProductModels() != null) {
            CmsBtShelvesTemplateModel cmsBtShelvesTemplateModel = cmsBtShelvesTemplateService.selectById(cmsBtShelvesModel.getLayoutTemplateId());
            if (cmsBtShelvesTemplateModel.getNumPerLine() != null && cmsBtShelvesTemplateModel.getNumPerLine() > 0) {
                List<String> imageNames = cmsBtShelvesInfoBean.getShelvesProductModels()
                        .stream()
                        .filter(item -> !StringUtil.isEmpty(item.getPlatformImageUrl()))
                        .map(item -> String.format("%s/shelves%d/%s.jpg", CmsBtShelvesProductService.getShelvesImagePath(), shelvesId, item.getProductCode()))
                        .collect(Collectors.toList());
                if (imageNames.size() == 0) {
                    throw new BusinessException("货架中没有商品");
                }

                return createAppImage(imageNames, cmsBtShelvesTemplateModel.getNumPerLine());
            } else {
                throw new BusinessException("货架模板不正确");
            }

        } else {
            throw new BusinessException("没有找到对应的货架");
        }


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
                if (oldShelvesProduct.getImage() == null || !oldShelvesProduct.getImage().equalsIgnoreCase(cmsBtShelvesProductModel.getImage())) {
                    oldShelvesProduct.setImage(cmsBtShelvesProductModel.getImage());
                    oldShelvesProduct.setPlatformImageUrl("");
                }
                cmsBtShelvesProductService.update(oldShelvesProduct);
            }
        });
    }


    public byte[] createAppImage(List<String> urls, int numPerLine) {
        int spacingX = 0;
        int spacingY = 0;
        try {
            $info(urls.get(0));
            InputStream imagein = new FileInputStream(urls.get(0));
            BufferedImage image = ImageIO.read(imagein);
            imagein.close();

            Integer width = image.getWidth();
            Integer height = image.getHeight();
            List<List<String>> urlSplit = CommonUtil.splitList(urls, numPerLine);
            BufferedImage combined = new BufferedImage(width * numPerLine + (numPerLine - 1) * spacingX, height * urlSplit.size() + (urlSplit.size() - 1) * spacingY, BufferedImage.TYPE_INT_ARGB);
            Graphics g = combined.getGraphics();
            for (int i = 0; i < urlSplit.size(); i++) {
                int y;
                if (i > 0) {
                    y = i * (height + spacingY);
                } else {
                    y = i * height;
                }
                for (int j = 0; j < urlSplit.get(i).size(); j++) {
                    InputStream temp = new FileInputStream(urlSplit.get(i).get(j));
                    $info(urlSplit.get(i).get(j));
                    BufferedImage imageTemp = ImageIO.read(temp);

                    int x;
                    if (j > 0) {
                        x = j * (width + spacingX);
                    } else {
                        x = j * width;
                    }
                    g.drawImage(imageTemp, x, y, null);
                    temp.close();
                }
            }
            ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream();
            // Save as new image
            ImageIO.write(combined, "PNG", bufferedOutputStream);
            return bufferedOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
