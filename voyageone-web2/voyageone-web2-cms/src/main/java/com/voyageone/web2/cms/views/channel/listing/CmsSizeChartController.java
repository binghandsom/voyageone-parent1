package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = CmsUrlConstants.CHANNEL.LISTING.SIZE_CHART.ROOT, method = RequestMethod.POST)
public class CmsSizeChartController extends CmsController {
    @Autowired
    private CmsSizeChartService cmsSizeChartService;
    /**
     * 尺码关系一览初始化画面
     */
    @RequestMapping(CmsUrlConstants.CHANNEL.LISTING.SIZE_CHART.INIT_SIZE_CHART)
    public AjaxResponse sizeChartInit() {
        //店铺渠道取得
        String channelId=this.getUser().getSelChannelId();
        //取得尺码关系一览初始化
        Map<String, Object> resultBean=cmsSizeChartService.sizeChartInit(getLang(), channelId);
        //返回数据的类型
        return success(resultBean);
    }
    /**
     * 尺码关系一览检索画面
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.LISTING.SIZE_CHART.SEARCH_SIZE_CHART)
    public AjaxResponse sizeChartSearch(@RequestBody Map param) {
        //店铺渠道取得
        String channelId=this.getUser().getSelChannelId();
        //取得尺码关系一览初始化
        Map<String, Object> resultBean=cmsSizeChartService.sizeChartSearch(channelId,param);
        //返回数据的类型
        return success(resultBean);
    }

    /**
     * 尺码关系一览初删除
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.LISTING.SIZE_CHART.DELETE_SIZE_CHART)
    public AjaxResponse sizeChartDelete(@RequestBody Map param) {
        //店铺渠道取得
        String channelId=this.getUser().getSelChannelId();
        cmsSizeChartService.sizeChartDelete(channelId, param);
        //返回数据的类型
        return success(param);
    }


    /***
     * 尺码关系一览编辑画面
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.LISTING.SIZE_CHART.EDIT_SIZE_CHART)
    public AjaxResponse sizeChartEdit(@RequestBody Map param) {
        //店铺渠道取得
        String channelId=this.getUser().getSelChannelId();
        //取得尺码关系一览初始化
        Map<String, Object> resultBean=cmsSizeChartService.sizeChartEdit(channelId, param);
        //返回数据的类型
        return success(resultBean);
    }

    /**
     * 尺码关系一览编辑详情编辑画面
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.LISTING.SIZE_CHART.DETAIL_SIZE_CHART)
    public AjaxResponse sizeChartDetail(@RequestBody Map param) {
        //店铺渠道取得
        String channelId=this.getUser().getSelChannelId();
        //取得尺码关系一览初始化
        Map<String, Object> resultBean=cmsSizeChartService.sizeChartDetail(channelId,param);
        //返回数据的类型
        return success(resultBean);
    }
}
