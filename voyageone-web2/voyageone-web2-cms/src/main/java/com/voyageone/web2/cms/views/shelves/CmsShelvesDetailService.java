package com.voyageone.web2.cms.views.shelves;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.BeanUtils;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.bean.cms.CmsBtShelvesInfoBean;
import com.voyageone.service.bean.cms.CmsBtShelvesProductBean;
import com.voyageone.service.impl.cms.CmsBtShelvesProductService;
import com.voyageone.service.impl.cms.CmsBtShelvesService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.promotion.PromotionCodeService;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.web2.base.BaseViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private final PromotionCodeService promotionCodeService;
    private final ProductService productService;

    @Autowired
    public CmsShelvesDetailService(CmsBtShelvesProductService cmsBtShelvesProductService, CmsBtShelvesService cmsBtShelvesService, PromotionCodeService promotionCodeService, ProductService productService) {
        this.cmsBtShelvesProductService = cmsBtShelvesProductService;
        this.cmsBtShelvesService = cmsBtShelvesService;
        this.promotionCodeService = promotionCodeService;
        this.productService = productService;
    }

    /**
     * 根据货架Id获取货架里的产品信息
     */
    List<CmsBtShelvesInfoBean> getShelvesInfo(String channelId, List<Integer> shelvesIds) {
        List<CmsBtShelvesInfoBean> cmsBtShelvesInfoBeens = new ArrayList<>();
        shelvesIds.forEach(shelvesId -> {
            CmsBtShelvesInfoBean cmsBtShelvesInfoBean = new CmsBtShelvesInfoBean();
            CmsBtShelvesModel cmsBtShelvesModel = cmsBtShelvesService.getId(shelvesId);
            if (cmsBtShelvesInfoBean != null) {
                cmsBtShelvesInfoBean.setShelvesModel(cmsBtShelvesModel);
                cmsBtShelvesInfoBean.setShelvesProductModels(getShelvesProductInfo(cmsBtShelvesModel));
                cmsBtShelvesInfoBeens.add(cmsBtShelvesInfoBean);
            }
        });
        return cmsBtShelvesInfoBeens;
    }

    /**
     * 产品加入货架
     */
    void addProducts(Integer shelvesId, List<String> productCodes, String modifier) {
        CmsBtShelvesModel cmsBtShelvesModel = cmsBtShelvesService.getId(shelvesId);

        if (cmsBtShelvesModel == null) {
            throw new BusinessException("货架不存在");
        }

        List<CmsBtShelvesProductModel> cmsBtShelvesProductModels = new ArrayList<>();
        productCodes.forEach(code -> {
            CmsBtProductModel productInfo = productService.getProductByCode(cmsBtShelvesModel.getChannelId(), code);
            CmsBtShelvesProductModel cmsBtShelvesProductModel = new CmsBtShelvesProductModel();
            CmsBtProductModel_Platform_Cart platform = productInfo.getPlatform(cmsBtShelvesModel.getCartId());
            if (platform != null) {
                cmsBtShelvesProductModel.setNumIid(platform.getpNumIId());
                cmsBtShelvesProductModel.setSalePrice(platform.getpPriceSaleEd());
            }
            cmsBtShelvesProductModel.setProductCode(code);
            cmsBtShelvesProductModel.setCmsInventory(productInfo.getCommon().getFields().getQuantity());
            List<CmsBtProductModel_Field_Image> imgList = productInfo.getCommonNotNull().getFieldsNotNull().getImages6();
            if (!imgList.isEmpty()) {
                cmsBtShelvesProductModel.setImage(imgList.get(0).getName());
            } else {
                imgList = productInfo.getCommonNotNull().getFieldsNotNull().getImages1();
                if (!imgList.isEmpty()) {
                    cmsBtShelvesProductModel.setImage(imgList.get(0).getName());
                }
            }
            cmsBtShelvesProductModel.setModifier(modifier);
            cmsBtShelvesProductModels.add(cmsBtShelvesProductModel);
        });
        //更新数据库
        updateShelvesProduct(cmsBtShelvesProductModels);
    }

    /**
     * 根据货架产品信息包含活动价格
     */
    private List<CmsBtShelvesProductBean> getShelvesProductInfo(CmsBtShelvesModel cmsBtShelvesModel) {
        List<CmsBtShelvesProductModel> cmsBtShelvesProductModels = cmsBtShelvesProductService.getByShelvesId(cmsBtShelvesModel.getId());
        if (!ListUtils.isNull(cmsBtShelvesProductModels)) {
            List<CmsBtShelvesProductBean> cmsBtShelvesProductBeens = new ArrayList<>(cmsBtShelvesProductModels.size());
            List<CmsBtPromotionCodesBean> cmsBtPromotionCodes = null;
            if (cmsBtShelvesModel.getPromotionId() != null && cmsBtShelvesModel.getPromotionId() > 0) {
                cmsBtPromotionCodes = promotionCodeService.getPromotionCodeListByIdOrgChannelId(cmsBtShelvesModel.getPromotionId(), cmsBtShelvesModel.getChannelId());
            }
            List<CmsBtPromotionCodesBean> finalCmsBtPromotionCodes = cmsBtPromotionCodes;
            cmsBtShelvesProductModels.forEach(item -> {
                CmsBtShelvesProductBean cmsBtShelvesProductBean = new CmsBtShelvesProductBean();
                BeanUtils.copy(item, cmsBtShelvesProductBean);
                cmsBtShelvesProductBeens.add(cmsBtShelvesProductBean);
                if (finalCmsBtPromotionCodes != null) {
                    cmsBtShelvesProductBean.setPromotionPrice(getPromotionPrice(item.getProductCode(), finalCmsBtPromotionCodes));
                }
            });

            return cmsBtShelvesProductBeens;
        }
        return new ArrayList<>();
    }

    private Double getPromotionPrice(String code, List<CmsBtPromotionCodesBean> cmsBtPromotionCodes) {
        CmsBtPromotionCodesBean promotionCodesBean = cmsBtPromotionCodes.stream().filter(cmsBtPromotionCodesBean -> cmsBtPromotionCodesBean.getProductCode().equalsIgnoreCase(code)).findFirst().orElse(null);
        if (promotionCodesBean != null) {
            return promotionCodesBean.getPromotionPrice();
        } else {
            return 0.0;
        }
    }

    private void updateShelvesProduct(List<CmsBtShelvesProductModel> cmsBtShelvesProductModels) {

        cmsBtShelvesProductModels.forEach(cmsBtShelvesProductModel -> {
            CmsBtShelvesProductModel oldShelvesProduct = cmsBtShelvesProductService.getByShelvesIdProductCode(cmsBtShelvesProductModel.getShelvesId(), cmsBtShelvesProductModel.getProductCode());
            if (null == oldShelvesProduct) {
                cmsBtShelvesProductService.insert(cmsBtShelvesProductModel);
            } else {
                oldShelvesProduct.setSalePrice(cmsBtShelvesProductModel.getSalePrice());
                oldShelvesProduct.setNumIid(cmsBtShelvesProductModel.getNumIid());
                oldShelvesProduct.setModifier(cmsBtShelvesProductModel.getModifier());
                oldShelvesProduct.setModified(new Date());
                if (!oldShelvesProduct.getImage().equalsIgnoreCase(cmsBtShelvesProductModel.getImage())) {
                    oldShelvesProduct.setImage(cmsBtShelvesProductModel.getImage());
                    oldShelvesProduct.setPlatformImageUrl("");
                }
                cmsBtShelvesProductService.update(oldShelvesProduct);
            }
        });
    }
}
