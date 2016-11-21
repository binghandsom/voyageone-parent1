package com.voyageone.service.impl.cms;

import com.voyageone.common.util.BeanUtils;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.bean.cms.CmsBtShelvesInfoBean;
import com.voyageone.service.bean.cms.CmsBtShelvesProductBean;
import com.voyageone.service.dao.cms.CmsBtShelvesProductDao;
import com.voyageone.service.daoext.cms.CmsBtShelvesProductDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.promotion.PromotionCodeService;
import com.voyageone.service.model.cms.CmsBtShelvesModel;
import com.voyageone.service.model.cms.CmsBtShelvesProductExample;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 2016/11/11.
 *
 * @version 2.10.0
 * @since 2.10.0
 */
@Service
public class CmsBtShelvesProductService extends BaseService {
    private final CmsBtShelvesProductDao cmsBtShelvesProductDao;
    private final CmsBtShelvesProductDaoExt cmsBtShelvesProductDaoExt;
    private final PromotionCodeService promotionCodeService;

    public static final String SHELVES_IMAGE_PATH = "/Users/jonas/Desktop/1";

    @Autowired
    public CmsBtShelvesProductService(CmsBtShelvesProductDao cmsBtShelvesProductDao,
                                      CmsBtShelvesProductDaoExt cmsBtShelvesProductDaoExt,
                                      PromotionCodeService promotionCodeService) {
        this.cmsBtShelvesProductDao = cmsBtShelvesProductDao;
        this.cmsBtShelvesProductDaoExt = cmsBtShelvesProductDaoExt;
        this.promotionCodeService = promotionCodeService;
    }

    public List<CmsBtShelvesProductModel> getByShelvesId(Integer shelvesId) {
        return cmsBtShelvesProductDaoExt.selectByShelvesId(shelvesId);
    }

    public CmsBtShelvesProductModel getByShelvesIdProductCode(Integer shelvesId, String code) {
        CmsBtShelvesProductModel byShelvesIdAndCode = new CmsBtShelvesProductModel();
        byShelvesIdAndCode.setShelvesId(shelvesId);
        byShelvesIdAndCode.setProductCode(code);

        return cmsBtShelvesProductDao.selectOne(byShelvesIdAndCode);
    }

    public int update(CmsBtShelvesProductModel cmsBtShelvesProductModel) {
        return cmsBtShelvesProductDao.update(cmsBtShelvesProductModel);
    }

    public Integer insert(CmsBtShelvesProductModel cmsBtShelvesProductModel) {
        cmsBtShelvesProductDao.insert(cmsBtShelvesProductModel);
        return cmsBtShelvesProductModel.getId();
    }

    /**
     * 更新货架顺序
     */
    public void updateSort(List<CmsBtShelvesProductModel> cmsBtShelvesProductModels) {
        cmsBtShelvesProductModels.forEach(cmsBtShelvesProductDaoExt::updateSort);
    }

    /**
     * 更新平台状态和库存
     */
    public void updatePlatformStatus(List<CmsBtShelvesProductModel> cmsBtShelvesProductModels) {
        cmsBtShelvesProductModels.forEach(cmsBtShelvesProductDaoExt::updatePlatformStatus);
    }

    /**
     * 更新平台图片
     */
    public void updatePlatformImage(CmsBtShelvesProductModel cmsBtShelvesProductModels) {
        cmsBtShelvesProductDaoExt.updatePlatformImage(cmsBtShelvesProductModels);
    }

    /**
     * 根据货架Id获取货架里的产品信息
     */
    public CmsBtShelvesInfoBean getShelvesInfo(CmsBtShelvesModel cmsBtShelvesModel, Boolean isLoadPromotionPrice) {


        CmsBtShelvesInfoBean cmsBtShelvesInfoBean = new CmsBtShelvesInfoBean();
        cmsBtShelvesInfoBean.setShelvesModel(cmsBtShelvesModel);

        if (isLoadPromotionPrice) {
            cmsBtShelvesInfoBean.setShelvesProductModels(getShelvesProductInfo(cmsBtShelvesModel));
        } else {
            cmsBtShelvesInfoBean.setShelvesProductModels(getByShelvesId(cmsBtShelvesModel.getId()));
        }
        return cmsBtShelvesInfoBean;
    }

    public void delete(CmsBtShelvesProductModel cmsBtShelvesProductModel) {
        cmsBtShelvesProductDao.delete(cmsBtShelvesProductModel.getId());

        String fileName = String.format("%s/shelves%d/%s.jpg", CmsBtShelvesProductService.SHELVES_IMAGE_PATH, cmsBtShelvesProductModel.getShelvesId(), cmsBtShelvesProductModel.getProductCode());
        try {
            FileUtils.delFile(fileName);
        } catch (Exception ignored) {
        }
    }

    public void deleteByShelvesId(Integer shelvesId) {
        CmsBtShelvesProductExample example = new CmsBtShelvesProductExample();
        example.createCriteria().andShelvesIdEqualTo(shelvesId);
        cmsBtShelvesProductDao.deleteByExample(example);
        String fileName = String.format("%s/shelves%d", CmsBtShelvesProductService.SHELVES_IMAGE_PATH, shelvesId);
        try {
            FileUtils.deleteAllFilesOfDir(new File(fileName));
        } catch (Exception ignored) {
        }
    }

    /**
     * 根据货架产品信息包含活动价格
     */
    private List<CmsBtShelvesProductModel> getShelvesProductInfo(CmsBtShelvesModel cmsBtShelvesModel) {
        List<CmsBtShelvesProductModel> cmsBtShelvesProductModels = getByShelvesId(cmsBtShelvesModel.getId());
        if (!ListUtils.isNull(cmsBtShelvesProductModels)) {
            List<CmsBtShelvesProductModel> cmsBtShelvesProductBeens = new ArrayList<>(cmsBtShelvesProductModels.size());
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
}
