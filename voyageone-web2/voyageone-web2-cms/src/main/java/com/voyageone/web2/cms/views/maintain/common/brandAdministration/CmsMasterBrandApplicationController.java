package com.voyageone.web2.cms.views.maintain.common.brandAdministration;

import com.voyageone.service.impl.cms.tools.common.CmsMasterBrandMappingService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        return null;
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
