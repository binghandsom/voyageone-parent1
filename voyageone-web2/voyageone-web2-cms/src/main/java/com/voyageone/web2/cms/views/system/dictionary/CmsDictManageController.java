package com.voyageone.web2.cms.views.system.dictionary;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.system.dictionary.CmsDictionaryIndexBean;
import com.voyageone.web2.cms.model.CmsMtDictModel;
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
@RequestMapping(value = CmsUrlConstants.SYSTEM.DICTIONARY.ROOT, method = RequestMethod.POST)
public class CmsDictManageController extends CmsController {

    @Autowired
    private CmsDictManageService dictManageService;

    /**
     * 获取字典页面list页面的初始化数据
     * @return
     * @throws Exception
     */
    @RequestMapping(CmsUrlConstants.SYSTEM.DICTIONARY.INIT)
    public AjaxResponse init() throws Exception {
        return success(dictManageService.getMasterData(getUser()));
    }

    /**
     * 获取字典页面list页面的字典列表数据
     * @param request
     * @return
     */
    @RequestMapping(CmsUrlConstants.SYSTEM.DICTIONARY.DT_GET_DICT)
    public AjaxResponse dtGetDict(@RequestBody CmsDictionaryIndexBean request) {
        return success(dictManageService.dtGetAllDict(request, getUser().getSelChannelId()));
    }

    /**
     * 删除字典页面list页面的单个字典数据
     * @param cmsMtDictModel
     * @return
     */
    @RequestMapping(CmsUrlConstants.SYSTEM.DICTIONARY.DEL_DICT)
    public AjaxResponse delDict(@RequestBody CmsMtDictModel cmsMtDictModel) {
        return success(dictManageService.delDict(cmsMtDictModel, getUser()));
    }

    /**
     * 添加一个新的字典数据
     * @param cmsMtDictModel
     * @return
     */
    @RequestMapping(CmsUrlConstants.SYSTEM.DICTIONARY.ADD_DICT)
    public AjaxResponse addDict(@RequestBody CmsMtDictModel cmsMtDictModel) {
        dictManageService.addDict(cmsMtDictModel, getUser());
        return new AjaxResponse();
    }

    /**
     * 获取字典值得master数据
     * @return
     */
    @RequestMapping(CmsUrlConstants.SYSTEM.DICTIONARY.GET_CONST)
    public AjaxResponse getConst() {
        return success(dictManageService.getMasterProps(getUser()));
    }

    /**
     * 修改字典页面list页面的某个字典数据
     * @param cmsMtDictModel
     * @return
     */
//    @RequestMapping(CmsUrlConstants.SYSTEM.DICTIONARY.SET_DICT)
//    public AjaxResponse setDict(@RequestBody CmsMtDictModel cmsMtDictModel) {
//        return success(dictManageService.setDict(cmsMtDictModel, getUser()));
//    }
/*

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
