package com.voyageone.service.impl.cms;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.dao.cms.CmsBtBrandBlockDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.vomqjobservice.CmsBrandBlockService;
import com.voyageone.service.model.cms.CmsBtBrandBlockModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 品牌屏蔽（品牌黑名单）相关的操作
 * Created by jonas on 9/6/16.
 *
 * @author jonas
 * @version 2.6.0
 * @since 2.6.0
 */
@Service
public class CmsBtBrandBlockService extends BaseService {
    public final static int BRAND_TYPE_FEED = 0;
    public final static int BRAND_TYPE_MASTER = 1;
    public final static int BRAND_TYPE_PLATFORM = 2;

    private final CmsBtBrandBlockDao brandBlockDao;

    CmsBrandBlockService cmsBrandBlockService;

    @Autowired
    public CmsBtBrandBlockService(CmsBtBrandBlockDao brandBlockDao, CmsBrandBlockService cmsBrandBlockService) {
        this.brandBlockDao = brandBlockDao;
        this.cmsBrandBlockService = cmsBrandBlockService;
    }

    /**
     * 屏蔽指定品牌
     *
     * @param channelId 渠道
     * @param cartId    店铺
     * @param brandType 品牌类型，{@link CmsBtBrandBlockService#BRAND_TYPE_FEED} / {@link CmsBtBrandBlockService#BRAND_TYPE_MASTER} / {@link CmsBtBrandBlockService#BRAND_TYPE_PLATFORM}
     * @param brand     品牌值
     * @param username  屏蔽人
     */
    public void block(String channelId, int cartId, int brandType, String brand, String username) {
        switch (brandType) {
            case BRAND_TYPE_FEED:
            case BRAND_TYPE_MASTER:
            case BRAND_TYPE_PLATFORM:
                if (isBlocked(channelId, cartId, brandType, brand))
                    return;
                break;
            default:
                return;
        }
        CmsBtBrandBlockModel brandBlockModel = new CmsBtBrandBlockModel();
        brandBlockModel.setChannelId(channelId);
        switch (brandType) {
            case BRAND_TYPE_FEED:
                brandBlockModel.setCartId(1);
                break;
            case BRAND_TYPE_MASTER:
                brandBlockModel.setCartId(0);
                break;
            case BRAND_TYPE_PLATFORM:
                brandBlockModel.setCartId(cartId);
                break;
        }
        brandBlockModel.setType(brandType);
        brandBlockModel.setBrand(brand);
        brandBlockModel.setCreater(username);
        brandBlockModel.setModifier(username);

        brandBlockDao.insert(brandBlockModel);
        cmsBrandBlockService.sendMessage(brandBlockModel, true, username);
    }

    /**
     * 解除指定品牌的屏蔽
     *
     * @param channelId 渠道
     * @param cartId    店铺
     * @param brandType 品牌类型，{@link CmsBtBrandBlockService#BRAND_TYPE_FEED} / {@link CmsBtBrandBlockService#BRAND_TYPE_MASTER} / {@link CmsBtBrandBlockService#BRAND_TYPE_PLATFORM}
     * @param brand     品牌值
     */
    public void unblock(String channelId, int cartId, int brandType, String brand, String userName) {
        switch (brandType) {
            case BRAND_TYPE_FEED:
                cartId = 1;
                break;
            case BRAND_TYPE_MASTER:
                cartId = 0;
                break;
            case BRAND_TYPE_PLATFORM:
                break;
            default:
                return;
        }

        CmsBtBrandBlockModel brandBlockModel = new CmsBtBrandBlockModel();
        brandBlockModel.setChannelId(channelId);
        brandBlockModel.setCartId(cartId);
        brandBlockModel.setType(brandType);
        brandBlockModel.setBrand(brand);

        brandBlockModel = brandBlockDao.selectOne(brandBlockModel);

        brandBlockDao.delete(brandBlockModel.getId());

        // 同上，只是相反
        cmsBrandBlockService.sendMessage(brandBlockModel, false, userName);
    }

    /**
     * 判断品牌是否已经被屏蔽
     *
     * @param channelId       渠道
     * @param cartId          店铺，该参数只在需要判断 platform 品牌的时候，才是必传参数
     * @param feedBrand       feed 品牌，只在需要判断 feed 品牌时，传入该参数
     * @param masterBrand     master 品牌，只在需要判断 master 品牌时，传入该参数
     * @param platformBrandId platform 品牌的 id，只在需要判断 platform 品牌时，传入该参数
     * @return true 表示其中一个品牌已被屏蔽, false 则全部未屏蔽
     */
    public boolean isBlocked(String channelId, Integer cartId, String feedBrand, String masterBrand, String platformBrandId) {
        return !StringUtils.isEmpty(feedBrand) && isBlocked(channelId, cartId, BRAND_TYPE_FEED, feedBrand)
                || !StringUtils.isEmpty(masterBrand) && isBlocked(channelId, cartId, BRAND_TYPE_MASTER, masterBrand)
                || cartId != null && !StringUtils.isEmpty(platformBrandId) && isBlocked(channelId, cartId, BRAND_TYPE_PLATFORM, platformBrandId)
                // 可以根据master品牌设定平台黑名单
                || cartId != null && !StringUtils.isEmpty(platformBrandId) && isBlocked(channelId, cartId, BRAND_TYPE_PLATFORM, masterBrand);

    }

    /**
     * 判断品牌是否已经被屏蔽
     *
     * @param brandBlockModel 包含品牌信息的数据模型
     * @return 是否已屏蔽
     */
    public boolean isBlocked(CmsBtBrandBlockModel brandBlockModel) {
        return isBlocked(brandBlockModel.getChannelId(), brandBlockModel.getCartId(), brandBlockModel.getType(), brandBlockModel.getBrand());
    }

    private boolean isBlocked(String channelId, int cartId, int brandType, String brand) {
        return brandBlockDao.selectCount(new HashMap<String, Object>(4, 1f) {{
            put("channelId", channelId);
            // 黑名单允许各个平台分开设定
            put("cartId", (brandType == BRAND_TYPE_FEED) ? 1 : (brandType == BRAND_TYPE_MASTER ? 0 : cartId));
            put("type", brandType);
            put("brand", brand);
        }}) > 0;
    }

    /**
     * 匹配类型，0、1、2 分别代表：feed、master、platform
     */
    int getBrandCount(String channelId, String cartId, int type) {
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", channelId);
        map.put("cartId", cartId);
        map.put("type", type);
        return brandBlockDao.selectCount(map);
    }
}
