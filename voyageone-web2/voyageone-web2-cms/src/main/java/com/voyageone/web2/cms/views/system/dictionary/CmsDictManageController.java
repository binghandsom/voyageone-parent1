package com.voyageone.web2.cms.views.system.dictionary;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.model.CmsMtDictModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * 主数据属性的字典管理
 * <p>
 * Created by Jonas on 9/15/15.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.SYSTEM.DICTIONARY.ROOT, method = RequestMethod.POST)
public class CmsDictManageController extends CmsController {

    @Autowired
    private CmsDictManageService dictManageService;

    @RequestMapping(CmsUrlConstants.SYSTEM.DICTIONARY.INIT)
    public AjaxResponse init() throws Exception {
        return success(dictManageService.getMasterData(getUser()));
    }

    @RequestMapping(CmsUrlConstants.SYSTEM.DICTIONARY.DT_GET_DICT)
    public AjaxResponse dtGetDict(@RequestBody Map request) {
        return success(dictManageService.dtGetAllDict(request, getUser().getSelChannelId()));
    }
/*
    @RequestMapping(CmsUrlConstants.SYSTEM.DICTIONARY.ADD_DICT)
    public AjaxResponse addDict(@RequestBody DictWordBean dictWordBean) {
        return success(dictManageService.addDict(dictWordBean, getUser()));
    }

    @RequestMapping(CmsUrlConstants.SYSTEM.DICTIONARY.SET_DICT)
    public AjaxResponse setDict(@RequestBody DictWordBean dictWordBean) {
        return success(dictManageService.setDict(dictWordBean, getUser()));
    }
*/
    @RequestMapping(CmsUrlConstants.SYSTEM.DICTIONARY.DEL_DICT)
    public AjaxResponse delDict(@RequestBody CmsMtDictModel cmsMtDictModel) {
        return success(dictManageService.delDict(cmsMtDictModel, getUser()));
    }
/*
    @RequestMapping(CmsUrlConstants.SYSTEM.DICTIONARY.GET_CONST)
    public AjaxResponse getConst() {

        List<CmsModelEnum> cmsValues = dictManageService.getCmsValues();

        List<DictMasterPropBean> masterProps = dictManageService.getMasterProps();

        Map jsonObj = new HashMap<>();
        jsonObj.put("cmsValues", cmsValues);
        jsonObj.put("masterProps", masterProps);

        return success(jsonObj);
    }

    @RequestMapping(CmsUrlConstants.SYSTEM.DICTIONARY.GET_CUSTOMS)
    public AjaxResponse getCustomWords() {
        List<CustomWord> customWords = dictManageService.getCustomWords();

        return success(customWords);
    }

    @RequestMapping(CmsUrlConstants.SYSTEM.DICTIONARY.GET_DICT_LIST)
    public AjaxResponse getDictList() {
        List<DictWordBean> wordBeans = dictManageService.getDictList(getUser().getSelChannelId());

        return success(wordBeans);
    }
*/
}
