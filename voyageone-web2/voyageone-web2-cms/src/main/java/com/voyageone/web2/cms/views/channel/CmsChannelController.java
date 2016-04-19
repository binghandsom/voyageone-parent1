package com.voyageone.web2.cms.views.channel;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.CompanyBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.dao.CompanyDao;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.ChannelService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.bean.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: holysky
 * @date: 2016/4/11 18:04
 */
@RestController
@RequestMapping(value = "/cms/channel/usjoi",method = { RequestMethod.POST})
public class CmsChannelController extends CmsController {

    @Resource
    ChannelService channelService;

    @Resource
    CompanyDao companyDao;

    @RequestMapping("getList")
    public AjaxResponse doGetList(@RequestBody Map<String, String> params) {

        String isUsjoiStr = params.get("allowMinimallOption");
        Integer isUsjoi=-1;
        if (!StringUtils.isNullOrBlank2(isUsjoiStr)) {
            isUsjoi = Integer.valueOf(isUsjoiStr);
        }
        List data = channelService.getChannelListBy(params.get("channelId"), params.get("channelName"), isUsjoi,params.get("active"));
        return success(Page.fromMap(params).withData(data));// page
    }



    @RequestMapping("update")
    public AjaxResponse doUpdate(@RequestBody OrderChannelBean bean) {


        bean.setModifier(getUser().getUserName());
        channelService.updateById(bean);
        return success(true);
    }

    @RequestMapping("getCarts")
    public AjaxResponse doGetCarts() {
        List<CartBean> datas = channelService.getCarts();
        return success(datas);
    }

    @RequestMapping("getCompanys")
    public AjaxResponse getCompanys() {
        List<CompanyBean> companys = companyDao.getAllActives();
        return success(companys);
    }

    @RequestMapping("updateCartIds")
    public AjaxResponse doUpdateCartIds(@RequestBody OrderChannelBean bean) {

        Preconditions.checkNotNull(bean);
        Preconditions.checkNotNull(bean.getOrder_channel_id());
        Preconditions.checkNotNull(bean.getCart_ids());
        bean.setModifier(getUser().getUserName());
        channelService.updateById(bean);
        return success(true);
    }



     @RequestMapping("save")
    public AjaxResponse save(@RequestBody OrderChannelBean bean) {
         bean.setCreater(getUser().getUserName());
         channelService.save(bean);
         return success(true);
    }

    @RequestMapping("genKey")
    public AjaxResponse genKey(@RequestBody Map bean) {
        Gson gson = new Gson();
        String result = gson.toJson(bean);
        result= Hashing.md5().hashString(result, Charsets.UTF_8).toString().toUpperCase();

        System.out.println(result);
        return success(result);
    }
}


