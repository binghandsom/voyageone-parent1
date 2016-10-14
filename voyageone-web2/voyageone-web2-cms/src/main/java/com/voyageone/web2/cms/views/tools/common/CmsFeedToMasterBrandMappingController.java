package com.voyageone.web2.cms.views.tools.common;

import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by gjl on 2016/10/9.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.TOOLS.COMMON.ROOT, method = RequestMethod.POST)
public class CmsFeedToMasterBrandMappingController extends CmsController {

//    @Autowired
//    private CmsMasterBrandMappingService cmsMasterBrandMappingService;
//
//    /**
//     * Master品牌匹配初始化
//     *
//     * @param param
//     * @return resultBean
//     */
//    @RequestMapping(value = CmsUrlConstants.TOOLS.COMMON.INIT_MASTER_BRAND_INFO)
//    public AjaxResponse doSearch(@RequestBody Map param) {
//        Map<String, Object> result = new HashMap<>();
//        return success(result);
//    }
}
