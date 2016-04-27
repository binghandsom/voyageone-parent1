package com.voyageone.web2.cms.views.mapping.dictionary;

import com.voyageone.service.bean.cms.system.dictionary.CmsDictionaryIndexBean;
import com.voyageone.service.model.cms.CmsMtPlatFormDictModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 主数据属性的字典管理
 * <p>
 * Created by Jonas on 9/15/15.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.MAPPING.DICTIONARY.ROOT, method = RequestMethod.POST)
public class CmsDictManageController extends CmsController {

    @Autowired
    private CmsDictManageService dictManageService;

    /**
     * 获取字典页面list页面的初始化数据
     * @return
     * @throws Exception
     */
    @RequestMapping(CmsUrlConstants.MAPPING.DICTIONARY.INIT)
    public AjaxResponse init() throws Exception {
        return success(dictManageService.getMasterData(getUser(), getLang()));
    }

    /**
     * 获取字典页面list页面的字典列表数据
     * @param request
     * @return
     */
    @RequestMapping(CmsUrlConstants.MAPPING.DICTIONARY.GET_DICT_LIST)
    public AjaxResponse getDictList(@RequestBody CmsDictionaryIndexBean request) {
        return success(dictManageService.dtGetAllDict(request, getUser().getSelChannelId(), getLang()));
    }

    /**
     * 获取单个字典数据
     * @param request
     * @return
     */
    @RequestMapping(CmsUrlConstants.MAPPING.DICTIONARY.GET_DICT)
    public AjaxResponse getDict(@RequestBody CmsMtPlatFormDictModel request) {
        return success(dictManageService.getDict(request, getUser()));
    }


    /**
     * 删除字典页面list页面的单个字典数据
     * @param request
     * @return
     */
    @RequestMapping(CmsUrlConstants.MAPPING.DICTIONARY.DEL_DICT)
    public AjaxResponse delDict(@RequestBody CmsMtPlatFormDictModel request) {
        return success(dictManageService.delDict(request, getUser()));
    }

    /**
     * 添加一个新的字典数据
     * @param request
     * @return
     */
    @RequestMapping(CmsUrlConstants.MAPPING.DICTIONARY.ADD_DICT)
    public AjaxResponse addDict(@RequestBody CmsMtPlatFormDictModel request) {
        dictManageService.addDict(request, getUser());
        return new AjaxResponse();
    }

    /**
     * 获取字典值得master数据
     * @return
     */
    @RequestMapping(CmsUrlConstants.MAPPING.DICTIONARY.GET_CONST)
    public AjaxResponse getConst() {
        return success(dictManageService.getMasterProps(getUser(), getLang(), getCmsSession()));
    }

    /**
     * 获取自定义的字典列表
     * @return
     */
    @RequestMapping(CmsUrlConstants.MAPPING.DICTIONARY.GET_CUSTOMS)
    public AjaxResponse getCustoms() {
        return success(dictManageService.getCustoms());
    }

    /**
     * 修改字典页面list页面的某个字典数据
     * @param cmsMtPlatFormDictModel
     * @return
     */
    @RequestMapping(CmsUrlConstants.MAPPING.DICTIONARY.SET_DICT)
    public AjaxResponse setDict(@RequestBody CmsMtPlatFormDictModel cmsMtPlatFormDictModel) {
        return success(dictManageService.setDict(cmsMtPlatFormDictModel, getUser()));
    }
}
