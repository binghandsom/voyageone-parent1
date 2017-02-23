package com.voyageone.service.impl.cms.product;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 产品code拆分合并
 *
 * @author morse on 2016/11/08
 * @version 2.6.0
 */
@Service
public class CmsProductCodeChangeGroupService {

    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private ProductPlatformService productPlatformService;

    /**
     * 移动Code-从一个Group到另外一个Group
     */
    public CmsBtProductGroupModel moveToAnotherGroup(String channelId, int cartId, String code, CmsBtProductGroupModel destGroupModel, String modifier) {
        CmsBtProductGroupModel sourceGroupModel = productGroupService.selectProductGroupByCode(channelId, code, cartId);
        moveToAnotherGroup(channelId, cartId, code, sourceGroupModel, destGroupModel, modifier);
        return sourceGroupModel;
    }

    /**
     * 移动Code-从一个Group到另外一个Group
     */
    public void moveToAnotherGroup(String channelId, int cartId, String code, CmsBtProductGroupModel sourceGroupModel, CmsBtProductGroupModel destGroupModel, String modifier) {
        if (destGroupModel == null) {
            throw new BusinessException("入参错误,移动先Group未指定!");
        }
        if (sourceGroupModel == null) {
            throw new BusinessException("入参错误,移动元Group未指定!");
        }
        if (!sourceGroupModel.getProductCodes().contains(code)) {
            throw new BusinessException("入参错误,移动元Group的code与入参code不一致!");
        }

        // 目的Group中加入这个Code
        destGroupModel.getProductCodes().add(code);
        // 重算目的group价格区间
        productGroupService.calculatePriceRange(destGroupModel);
        // 更新目标Group
        productGroupService.update(destGroupModel);

        // 设定移动Code的Product信息的相关平台下pIsMain=0
        productPlatformService.updateProductPlatformIsMain(destGroupModel.getMainProductCode(), channelId, code, cartId, modifier);

        // 处理源Group
        saveSourceGroup(channelId, cartId, code, sourceGroupModel, modifier);
    }

    /**
     * 移动Code-从一个Group到一个新的Group
     */
    public CmsBtProductGroupModel moveToNewGroup(String channelId, int cartId, String code, String modifier) {
        CmsBtProductGroupModel sourceGroupModel = productGroupService.selectProductGroupByCode(channelId, code, cartId);
        return moveToNewGroup(channelId, cartId, code, sourceGroupModel, modifier);
    }

    /**
     * 移动Code-从一个Group到一个新的Group
     */
    public CmsBtProductGroupModel moveToNewGroup(String channelId, int cartId, String code, CmsBtProductGroupModel sourceGroupModel, String modifier) {
        CmsBtProductGroupModel destGroupModel = productGroupService.createNewGroup(channelId, cartId, code, true);
        productGroupService.insert(destGroupModel);

        // 设定移动Code的Product信息的相关平台下pIsMain=1
        productPlatformService.updateProductPlatformIsMain(code, channelId, code, cartId, modifier);

        // 处理源Group
        saveSourceGroup(channelId, cartId, code, sourceGroupModel, modifier);

        return destGroupModel;
    }

    /**
     * 保存原Group
     */
    private void saveSourceGroup(String channelId, int cartId, String code, CmsBtProductGroupModel sourceGroupModel, String modifier) {
        // 移动Code如果是源Group的最后一个Code，那么删除源Group信息
        if (sourceGroupModel.getProductCodes().size() == 1) {
            productGroupService.deleteGroup(sourceGroupModel);
        } else {
            // 移动Code如果不是源Group的最后一个Code，那么把这个Code从源Group信息中移除，然后重算源group价格区间。
            sourceGroupModel.setProductCodes(sourceGroupModel.getProductCodes().stream().filter(productCode -> !productCode.equals(code)).collect(Collectors.toList()));
            sourceGroupModel.setModifier(modifier);
            if (sourceGroupModel.getMainProductCode().equals(code)) {
                // 如果是主商品被移走了
                // 把第一个code当作主商品
                String mainProductCode = sourceGroupModel.getProductCodes().get(0);
                sourceGroupModel.setMainProductCode(mainProductCode);

                sourceGroupModel.getProductCodes().forEach(productCode -> {
                    // 设定Code的Product信息的相关平台下pIsMain
                    productPlatformService.updateProductPlatformIsMain(productCode, channelId, productCode, cartId, modifier);
                });
            }
            productGroupService.calculatePriceRange(sourceGroupModel);
            productGroupService.update(sourceGroupModel);
        }
    }
}
