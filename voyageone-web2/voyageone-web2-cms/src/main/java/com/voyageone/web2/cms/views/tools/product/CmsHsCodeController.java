package com.voyageone.web2.cms.views.tools.product;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by gjl on 2016/6/7.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.TOOLS.PRODUCT.ROOT, method = RequestMethod.POST)
public class CmsHsCodeController extends CmsController {

    @Autowired
    private CmsHsCodeService cmsHsCodeService;

    /**
     * HsCode信息初始化
     * @param param
     * @return
     */
    @RequestMapping(value = CmsUrlConstants.TOOLS.PRODUCT.INIT_HS_CODE_INFO)
    public AjaxResponse initHsCodeInfo(@RequestBody Map param) {
        //店铺渠道取得
        String channelId=this.getUser().getSelChannelId();
        //当前用户名称
        String userName=this.getUser().getUserName();
        //取得尺码关系一览初始化
        Map<String, Object> resultBean = cmsHsCodeService.searchHsCodeInfo(getLang(),channelId,userName,param);
        //返回数据的类型
        return success(resultBean);
    }

    /**
     * 获取任务
     * @param param
     * @return
     */
    @RequestMapping(value = CmsUrlConstants.TOOLS.PRODUCT.GET_HS_CODE_INFO)
    public AjaxResponse getHsCodeInfo(@RequestBody Map param) {
        //店铺渠道取得
        String channelId=this.getUser().getSelChannelId();
        //当前用户名称
        String userName=this.getUser().getUserName();
        //取得尺码关系一览初始化
        Map<String, Object> resultBean=cmsHsCodeService.getHsCodeInfo(getLang(), channelId, userName, param);
        //返回数据的类型
        return success(resultBean);
    }

    /**
     * HsCode信息检索
     * @param param
     * @return
     */
    @RequestMapping(value = CmsUrlConstants.TOOLS.PRODUCT.SEARCH_HS_CODE_INFO)
    public AjaxResponse searchHsCodeInfo(@RequestBody Map param) {
        //店铺渠道取得
        String channelId=this.getUser().getSelChannelId();
        //当前用户名称
        String userName=this.getUser().getUserName();
        //取得尺码关系一览初始化
        Map<String, Object> resultBean=cmsHsCodeService.searchHsCodeInfo(getLang(), channelId, userName, param);
        //返回数据的类型
        return success(resultBean);
    }

    /**
     * HsCode信息保存
     * @param param
     * @return
     */
    @RequestMapping(value = CmsUrlConstants.TOOLS.PRODUCT.SAVE_HS_CODE_INFO)
    public AjaxResponse saveHsCodeInfo(@RequestBody Map param) {
        //店铺渠道取得
        String channelId=this.getUser().getSelChannelId();
        //当前用户名称
        String userName=this.getUser().getUserName();
        //取得尺码关系一览初始化
        Map<String, Object> resultBean=cmsHsCodeService.saveHsCodeInfo(channelId,userName,param);
        //返回数据的类型
        return success(resultBean);
    }
}

