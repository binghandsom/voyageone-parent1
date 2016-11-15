package com.voyageone.web2.cms.views.shelves;

import com.voyageone.service.fields.cms.CmsBtShelvesModelActive;
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
 *
 * @version 2.10.0
 * @since 2.10.0
 */
@RestController
@RequestMapping(method = RequestMethod.POST, value = CmsUrlConstants.SHELVES.DETAIL.ROOT)
public class CmsShelvesDetailController extends CmsController {
    private final CmsBtShelvesService cmsBtShelvesService;
    private final CmsShelvesDetailService cmsShelvesDetailService;

    @Autowired
    public CmsShelvesDetailController(CmsShelvesDetailService cmsShelvesDetailService, CmsBtShelvesService cmsBtShelvesService) {
        this.cmsShelvesDetailService = cmsShelvesDetailService;
        this.cmsBtShelvesService = cmsBtShelvesService;
    }

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.SEARCH)
    public AjaxResponse search(@RequestBody Map<String, Object> params) {
        params.put("channelId",getUser().getSelChannel());
        params.put("active", CmsBtShelvesModelActive.ACTIVATE);
        return success(cmsBtShelvesService.selectList(params));
    }

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.ADD_PRODUCT)
    public AjaxResponse addProduct(@RequestBody Map<String, Object> params){
        Integer shelvesId = (Integer) params.get("shelvesId");
        List<String> productCodes = (List<String>) params.get("productCodes");
        cmsShelvesDetailService.addProducts(shelvesId, productCodes, getUser().getUserName());
        return success(true);
    }

    @RequestMapping(CmsUrlConstants.SHELVES.DETAIL.GET_SHELVES_INFO)
    public AjaxResponse getShelvesInfo(@RequestBody List<Integer> shelvesIds){
        return success(cmsShelvesDetailService.getShelvesInfo(getUser().getSelChannelId(), shelvesIds));
    }
}
