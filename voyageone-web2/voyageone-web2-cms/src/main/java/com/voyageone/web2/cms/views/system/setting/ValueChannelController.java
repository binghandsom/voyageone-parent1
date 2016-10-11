package com.voyageone.web2.cms.views.system.setting;

import com.voyageone.service.model.cms.CmsBtTaskTejiabaoModel;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

/**
 * @author james.li on 2016/8/15.
 * @version 2.0.0
 */
@RestController
@RequestMapping(value = "/cms/system/valueChannel", method = {RequestMethod.POST})
public class ValueChannelController  extends BaseController {
    @Autowired
    private ValueChannelService valueChannelService;

    @RequestMapping("addHsCode")
    public AjaxResponse addHsCode(@RequestBody Map params) {
        String hsCodes = params.get("hsCodes").toString();
        Integer typeId = (Integer) params.get("typeId");
        String[] hsCodeList = hsCodes.split("\n");
        return success(valueChannelService.addHsCodes(getUser().getSelChannelId(), Arrays.asList(hsCodeList),typeId,getUser().getUserName()));
    }
}
