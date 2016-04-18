package com.voyageone.web2.cms.views.channel;

import com.google.common.base.Preconditions;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.CompanyBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.dao.CompanyDao;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.ChannelService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
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


    static class Page implements Serializable{
        int curr=1;//当前页
        int size=20;//页大小
        List data; //总数据
        int total=0;//总条数

        private Page(){}

        public static Page fromMap(Map<String, String> params) {
            Page page = new Page();
            int curr = params.containsKey("curr")?Integer.valueOf(params.get("curr")):1;
            int size = params.containsKey("size") ? Integer.valueOf(params.get("size")) : 20;
            page.setCurr(curr);
            page.setSize(size);
            return page;
        }

        public Page withData(List data) {
            this.total=(data!=null&&data.size()>0)?data.size():0;
            this.setData(data);
            return this;
        }

        public int getCurr() {
            return curr;
        }

        public void setCurr(int curr) {
            this.curr = curr;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotal() {
            return total;
        }

        public List getData() {
            int fromIndex=(curr-1)*size;
            int toIndex=fromIndex+size;
            return data.subList(fromIndex, toIndex>data.size()?data.size():toIndex);
        }

        public void setData(List data) {
            this.data = data;
        }
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
}


