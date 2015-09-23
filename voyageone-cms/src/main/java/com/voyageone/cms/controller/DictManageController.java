package com.voyageone.cms.controller;

import com.voyageone.base.BaseController;
import com.voyageone.cms.modelbean.CustomWord;
import com.voyageone.cms.service.DictManageService;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.ajax.dt.DtRequest;
import com.voyageone.core.ajax.dt.DtResponse;
import com.voyageone.ims.bean.DictMasterPropBean;
import com.voyageone.ims.enums.CmsFieldEnum.CmsModelEnum;
import com.voyageone.ims.modelbean.DictWordBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.voyageone.cms.UrlConstants.DictManage.*;

/**
 * 主数据属性的字典管理
 * <p>
 * Created by Jonas on 9/15/15.
 */
@RestController
@RequestMapping(value = ROOT, method = RequestMethod.POST)
public class DictManageController extends BaseController {

    @Autowired
    private DictManageService dictManageService;

    @RequestMapping(DT_GET_DICT)
    public AjaxResponseBean dtGetDict(@RequestBody DtRequest request) {

        DtResponse<List<DictWordBean>> response = dictManageService.dtGetAllDict(request, getUser().getSelChannel());

        return success(response);
    }

    @RequestMapping(ADD_DICT)
    public AjaxResponseBean addDict(@RequestBody DictWordBean dictWordBean) {
        return success(dictManageService.addDict(dictWordBean, getUser()));
    }

    @RequestMapping(SET_DICT)
    public AjaxResponseBean setDict(@RequestBody DictWordBean dictWordBean) {
        return success(dictManageService.setDict(dictWordBean, getUser()));
    }

    @RequestMapping(DEL_DICT)
    public AjaxResponseBean delDict(@RequestBody DictWordBean dictWordBean) {
        return success(dictManageService.delDict(dictWordBean));
    }

    @RequestMapping(GET_CONST)
    public AjaxResponseBean getConst() {

        List<CmsModelEnum> cmsValues = dictManageService.getCmsValues();

        List<DictMasterPropBean> masterProps = dictManageService.getMasterProps();

        JsonObj jsonObj = new JsonObj();
        jsonObj.add("cmsValues", cmsValues);
        jsonObj.add("masterProps", masterProps);

        return success(jsonObj);
    }

    @RequestMapping(GET_CUSTOMS)
    public AjaxResponseBean getCustomWords() {
        List<CustomWord> customWords = dictManageService.getCustomWords();

        return success(customWords);
    }

    @RequestMapping(GET_DICT_LIST)
    public AjaxResponseBean getDictList() {
        List<DictWordBean> wordBeans = dictManageService.getDictList(getUser().getSelChannel());

        return success(wordBeans);
    }
}
