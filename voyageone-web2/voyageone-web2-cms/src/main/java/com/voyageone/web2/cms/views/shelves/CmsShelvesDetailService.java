package com.voyageone.web2.cms.views.shelves;

import com.voyageone.common.util.BeanUtils;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.bean.cms.CmsBtShelvesInfoBean;
import com.voyageone.service.bean.cms.CmsBtShelvesProductBean;
import com.voyageone.service.dao.cms.CmsBtShelvesProductDao;
import com.voyageone.service.impl.cms.CmsBtShelvesProductService;
import com.voyageone.service.impl.cms.CmsBtShelvesService;
import com.voyageone.service.impl.cms.promotion.PromotionCodeService;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 2016/11/15.
 */
@Service
public class CmsShelvesDetailService {

    private final CmsBtShelvesService cmsBtShelvesService;

    private final CmsBtShelvesProductService cmsBtShelvesProductService;

    private final PromotionCodeService promotionCodeService;

    @Autowired
    public CmsShelvesDetailService(CmsBtShelvesProductService cmsBtShelvesProductService, CmsBtShelvesService cmsBtShelvesService, PromotionCodeService promotionCodeService) {
        this.cmsBtShelvesProductService = cmsBtShelvesProductService;
        this.cmsBtShelvesService = cmsBtShelvesService;
        this.promotionCodeService = promotionCodeService;
    }

    /**
     * 根据货架Id获取货架里的产品信息
     */
    public List<CmsBtShelvesInfoBean> getShelvesInfo(String channelId, List<Integer> shelvesIds) {
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
     * 根据货架产品信息包含活动价格
     */
    private List<CmsBtShelvesProductBean> getShelvesProductInfo(CmsBtShelvesModel cmsBtShelvesModel) {
        List<CmsBtShelvesProductModel> cmsBtShelvesProductModels = cmsBtShelvesProductService.getByShelvesId(cmsBtShelvesModel.getId());
        if(!ListUtils.isNull(cmsBtShelvesProductModels)) {
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
                if(finalCmsBtPromotionCodes != null){
                    cmsBtShelvesProductBean.setPromotionPrice(getPromotionPrice(item.getProductCode(), finalCmsBtPromotionCodes));
                }
            });

            return cmsBtShelvesProductBeens;
        }
        return new ArrayList<>();
    }

    private Double getPromotionPrice(String code, List<CmsBtPromotionCodesBean> cmsBtPromotionCodes){
        CmsBtPromotionCodesBean promotionCodesBean = cmsBtPromotionCodes.stream().filter(cmsBtPromotionCodesBean -> cmsBtPromotionCodesBean.getProductCode().equalsIgnoreCase(code)).findFirst().orElse(null);
        if(promotionCodesBean != null){
            return promotionCodesBean.getPromotionPrice();
        }else{
            return 0.0;
        }
    }
}
