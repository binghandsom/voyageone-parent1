package com.voyageone.web2.cms.view.dict_edit;

import com.voyageone.ims.bean.DictMasterPropBean;
import com.voyageone.ims.enums.CmsFieldEnum.CmsModelEnum;
import com.voyageone.ims.modelbean.DictWordBean;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.base.ajax.dt.DtRequest;
import com.voyageone.web2.base.ajax.dt.DtResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.model.CustomWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 主数据属性的字典管理
 * <p>
 * Created by Jonas on 9/15/15.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.DictManage.ROOT, method = RequestMethod.POST)
public class DictManageController extends CmsController {

    @Autowired
    private DictManageService dictManageService;

    @RequestMapping(CmsUrlConstants.DictManage.DT_GET_DICT)
    public AjaxResponse dtGetDict(@RequestBody DtRequest request) {

        DtResponse<List<DictWordBean>> response = dictManageService.dtGetAllDict(request, getUser().getSelChannelId());

        return success(response);
    }

    @RequestMapping(CmsUrlConstants.DictManage.ADD_DICT)
    public AjaxResponse addDict(@RequestBody DictWordBean dictWordBean) {
        return success(dictManageService.addDict(dictWordBean, getUser()));
    }

    @RequestMapping(CmsUrlConstants.DictManage.SET_DICT)
    public AjaxResponse setDict(@RequestBody DictWordBean dictWordBean) {
        return success(dictManageService.setDict(dictWordBean, getUser()));
    }

    @RequestMapping(CmsUrlConstants.DictManage.DEL_DICT)
    public AjaxResponse delDict(@RequestBody DictWordBean dictWordBean) {
        return success(dictManageService.delDict(dictWordBean));
    }

    @RequestMapping(CmsUrlConstants.DictManage.GET_CONST)
    public AjaxResponse getConst() {

        List<CmsModelEnum> cmsValues = dictManageService.getCmsValues();

        List<DictMasterPropBean> masterProps = dictManageService.getMasterProps();

        Map jsonObj = new HashMap<>();
        jsonObj.put("cmsValues", cmsValues);
        jsonObj.put("masterProps", masterProps);

        return success(jsonObj);
    }

    @RequestMapping(CmsUrlConstants.DictManage.GET_CUSTOMS)
    public AjaxResponse getCustomWords() {
        List<CustomWord> customWords = dictManageService.getCustomWords();

        return success(customWords);
    }

    @RequestMapping(CmsUrlConstants.DictManage.GET_DICT_LIST)
    public AjaxResponse getDictList() {
        List<DictWordBean> wordBeans = dictManageService.getDictList(getUser().getSelChannelId());

        return success(wordBeans);
    }

}
