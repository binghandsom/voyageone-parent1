package com.voyageone.web2.cms.views.channel;

import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants.CHANNEL.BLACK_BRAND;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jonas on 9/7/16.
 *
 * @author jonas
 * @version 2.6.0
 * @since 2.6.0
 */
@RestController
@RequestMapping(value = BLACK_BRAND.ROOT, method = RequestMethod.POST)
public class CmsBlackBrandController extends CmsController {

    private final CmsBlackBrandViewService blackBrandViewService;

    @Autowired
    public CmsBlackBrandController(CmsBlackBrandViewService blackBrandViewService) {
        this.blackBrandViewService = blackBrandViewService;
    }
}
