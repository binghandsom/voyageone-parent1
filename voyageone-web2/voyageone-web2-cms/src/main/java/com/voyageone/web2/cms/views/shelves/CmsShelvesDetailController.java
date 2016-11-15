package com.voyageone.web2.cms.views.shelves;

import com.voyageone.service.bean.cms.shelves.CmsBtShelvesTemplateBean;
import com.voyageone.service.impl.cms.CmsBtShelvesService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by james on 2016/11/15.
 */
@RestController
@RequestMapping(method = RequestMethod.POST, value = CmsUrlConstants.SHELVES.DETAIL.ROOT)
public class CmsShelvesDetailController extends CmsController {

    @Autowired
    private CmsBtShelvesService cmsBtShelvesService;

    @Autowired
    private CmsShelvesDetailService cmsShelvesDetailService;

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.SEARCH)
    public AjaxResponse search(@RequestBody Map params) {
        params.put("channelId",getUser().getSelChannel());
        cmsBtShelvesService.selectList(params);
        return success(cmsBtShelvesService.selectList(params));
    }

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.GET_SHELVES_INFO)
    public AjaxResponse getShelvesInfo(@RequestBody List<Integer> shelvesIds){
        return success(cmsShelvesDetailService.getShelvesInfo(getUser().getSelChannelId(), shelvesIds));
    }
}
