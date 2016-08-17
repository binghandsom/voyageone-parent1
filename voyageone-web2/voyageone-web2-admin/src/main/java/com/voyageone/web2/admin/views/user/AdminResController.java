package com.voyageone.web2.admin.views.user;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.base.ajax.AjaxResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by Ethan Shi on 2016-08-17.
 */

@RestController
@RequestMapping(value = AdminUrlConstants.User.Res.ROOT, method = RequestMethod.POST)
public class AdminResController extends AdminController {


    @RequestMapping(AdminUrlConstants.User.Res.SEARCH_RES)
    public AjaxResponse searchRes(@RequestBody Map requestBean) {

        String application = requestBean.getOrDefault("application", "").toString();

        return success(true);
    }


}
