package com.voyageone.web2.cms.views.maintain.common.brandAdministration;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.service.impl.cms.tools.common.CmsMasterBrandMappingService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/10/12.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.MAINTAIN_SETTING.COMMON.BRAND_ADMINISTRATION.ROOT, method = RequestMethod.POST)
public class CmsMasterBrandApplicationController extends CmsController {
    @Autowired
    private CmsMasterBrandMappingService cmsMasterBrandMappingService;

    /**
     *Master品牌待审核一览,已有Master品牌一览(初始化及检索画面)
     */
    @RequestMapping(value =CmsUrlConstants.MAINTAIN_SETTING.COMMON.BRAND_ADMINISTRATION.MASTER_BRAND_APPLICATION_SEARCH)
    public AjaxResponse getMasterBrandInfo(@RequestBody Map param) {
        Map<String, Object> result = new HashMap<>();
        //店铺渠道取得
        String channelId = this.getUser().getSelChannelId();
        //相关channel
        result.put("channelList",TypeChannels.getTypeChannelBeansByTypeValueLang(Constants.comMtTypeChannel.SKU_CARTS_53, channelId, "cn"));
        // 检索品牌映射关系的数量
        result.put("masterBrandsCount", cmsMasterBrandMappingService.searchMasterBrandApplicationCount(channelId, param));
        // 检索品牌映射关系的数据
        result.put("masterBrandList", cmsMasterBrandMappingService.searchMasterBrandApplicationInfo(channelId, param));
        //返回数据的类型
        return (AjaxResponse) result;
    }
    /**
     *Master品牌待审核一览(审核操作)
     */
    @RequestMapping(value =CmsUrlConstants.MAINTAIN_SETTING.COMMON.BRAND_ADMINISTRATION.MASTER_BRAND_APPLICATION_REVIEWED)
    public AjaxResponse reviewedMasterBrandInfo(@RequestBody Map param) {
        return null;
    }

    /**
     * 已有Master品牌一览(编辑)
     */
    @RequestMapping(value =CmsUrlConstants.MAINTAIN_SETTING.COMMON.BRAND_ADMINISTRATION.MASTER_BRAND_APPLICATION_EDIT)
    public AjaxResponse editMasterBrandInfo(@RequestBody Map param) {
        return null;
    }
    /**
     * 平台-Master品牌匹配关系一览(初始化及检索画面)
     */
    @RequestMapping(value =CmsUrlConstants.MAINTAIN_SETTING.COMMON.BRAND_ADMINISTRATION.MASTER_BRAND_APPLICATION_PLATFORM_SEARCH)
    public AjaxResponse mappingMasterBrandInfoToPlatform(@RequestBody Map param) {
        return null;
    }
}
