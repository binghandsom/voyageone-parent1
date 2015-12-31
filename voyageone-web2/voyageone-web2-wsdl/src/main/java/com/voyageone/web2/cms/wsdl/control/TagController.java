package com.voyageone.web2.cms.wsdl.control;

import com.voyageone.web2.cms.wsdl.BaseController;
import com.voyageone.web2.cms.wsdl.service.TagService;
import com.voyageone.web2.sdk.api.request.*;
import com.voyageone.web2.sdk.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * product Controller
 *
 * @author jerry 15/12/29
 * @version 2.0.1
 * @since. 2.0.0
 */

@RestController
@RequestMapping(
        value  = "/rest/tag",
        method = RequestMethod.POST
)
public class TagController extends BaseController {

    @Autowired
    private TagService tagService;

    /**
     * selectOne
     *
     * @return CmsBtProductModel
     */
    @RequestMapping("add")
    public TagAddResponse add(@RequestBody TagAddRequest request) {
        return tagService.addTag(request);
    }

    /**
     * selectOne
     *
     * @return CmsBtProductModel
     */
    @RequestMapping("remove")
    public TagRemoveResponse remove(@RequestBody TagRemoveRequest request) {
        return tagService.removeTag(request);
    }

    /**
     * selectOne
     *
     * @return CmsBtProductModel
     */
    @RequestMapping("selectListByParentTagId")
    public TagsGetByParentTagIdResponse selectListByParentTagId(@RequestBody TagsGetByParentTagIdRequest request) {
        return tagService.selectListByParentTagId(request);
    }

    /**
     * selectOne
     *
     * @return CmsBtProductModel
     */
    @RequestMapping("selectParentTagByChannel")
    public TagsGetByChannelIdResponse selectListByChannelId(@RequestBody TagsGetByChannelIdRequest request) {
        return tagService.selectListByChannelId(request);
    }
}