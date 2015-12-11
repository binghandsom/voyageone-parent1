package com.voyageone.web2.cms.views;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/9
 * @version 2.0.0
 */

@RestController
@RequestMapping(
        value  = "/cms/pop/prop_change/",
        method = RequestMethod.POST
)
public class CmsPropChangeController extends CmsController {

    @Autowired
    private CmsPropChangeService propChangeService;

    @RequestMapping("init")
    public AjaxResponse init() {
        String channel_id = getUser().getSelChannelId();
        List<JSONObject> result = propChangeService.getPropOptions(channel_id);
        return success(result);
    }

    @RequestMapping("save")
    public AjaxResponse save(@RequestBody JSONObject params) {
        propChangeService.savePropOptions(params);
        return success(true);
    }
}
