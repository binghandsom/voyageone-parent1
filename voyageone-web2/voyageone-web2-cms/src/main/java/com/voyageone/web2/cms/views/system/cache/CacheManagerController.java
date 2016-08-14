package com.voyageone.web2.cms.views.system.cache;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.com.cache.CommCacheControlService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * @author aooer 2016/3/25.
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping(value = CmsUrlConstants.SYSTEM.CACHE.ROOT, method ={RequestMethod.POST, RequestMethod.GET})
public class CacheManagerController extends CmsController{

    @Autowired
    private CommCacheControlService cacheControlService;

    @RequestMapping(CmsUrlConstants.SYSTEM.CACHE.INIT)
    public AjaxResponse init(HttpServletRequest request) throws Exception {
        // delete one
        String cacheKey = request.getParameter("cacheKey");
        if(!StringUtils.isEmpty(cacheKey)) {
            cacheControlService.deleteCache(CacheKeyEnums.KeyEnum.valueOf(cacheKey));
            return redirectTo("/modules/cms/app.html#/system/cache/index");
        }

        //delete all
        String delAll = request.getParameter("delAll");
        if(!StringUtils.isEmpty(delAll)){
            cacheKeySet().forEach(subCacheKey->cacheControlService.deleteCache(CacheKeyEnums.KeyEnum.valueOf(subCacheKey)));
            return redirectTo("/modules/cms/app.html#/system/cache/index");
        }
        return success(cacheKeySet());
    }

    private Set<String> cacheKeySet(){
        try {
            return cacheControlService.getCacheKeySet();
        } catch (Exception ex) {
            return new HashSet<>();
        }
    }

}
