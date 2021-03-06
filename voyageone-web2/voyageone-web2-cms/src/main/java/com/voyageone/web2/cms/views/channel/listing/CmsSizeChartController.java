package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.common.util.ConvertUtil;
import com.voyageone.service.bean.cms.CmsBtSizeChart.GetProductSizeChartListParameter;
import com.voyageone.service.impl.cms.SizeChartService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtSizeChartModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = CmsUrlConstants.CHANNEL.LISTING.SIZE_CHART.ROOT, method = RequestMethod.POST)
public class CmsSizeChartController extends CmsController {
    @Autowired
    private CmsSizeChartService cmsSizeChartService;
    @Autowired
    private SizeChartService sizeChartService;

    /**
     * 尺码关系一览初始化画面
     */
    @RequestMapping(CmsUrlConstants.CHANNEL.LISTING.SIZE_CHART.INIT_SIZE_CHART)
    public AjaxResponse sizeChartInit() {
        //店铺渠道取得
        String channelId = this.getUser().getSelChannelId();
        //取得尺码关系一览初始化
        Map<String, Object> resultBean = cmsSizeChartService.sizeChartInit(getLang(), channelId);
        //返回数据的类型
        return success(resultBean);
    }

    /**
     * 尺码关系一览检索画面
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.LISTING.SIZE_CHART.SEARCH_SIZE_CHART)
    public AjaxResponse sizeChartSearch(@RequestBody Map param) {
        //店铺渠道取得
        String channelId = this.getUser().getSelChannelId();
        //取得尺码关系一览初始化
        cmsSizeChartService.sizeChartSearch(channelId, param, getLang());
        //返回数据的类型
        return success(param);
    }

    /**
     * 尺码关系一览初删除
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.LISTING.SIZE_CHART.DELETE_SIZE_CHART)
    public AjaxResponse sizeChartDelete(@RequestBody Map param) {
        //店铺渠道取得
        String channelId = this.getUser().getSelChannelId();
        //创建者/更新者用
        param.put("userName", this.getUser().getUserName());
        //逻辑删除选中的记录
        cmsSizeChartService.sizeChartDelete(channelId, param);
        //返回数据的类型
        return success(param);
    }

    /***
     * 尺码关系一览初保存画面
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.LISTING.SIZE_CHART.SAVE_EDIT_SIZE_CHART)
    public AjaxResponse sizeChartEditSave(@RequestBody Map param) {
        //店铺渠道取得
        String channelId = this.getUser().getSelChannelId();
        //创建者/更新者用
        param.put("userName", this.getUser().getUserName());
        //取得尺码关系一览初始化
        cmsSizeChartService.sizeChartEditInsert(channelId, param);
        //返回数据的类型
        return success(param);
    }

    @RequestMapping(value = CmsUrlConstants.CHANNEL.LISTING.SIZE_CHART.GetNoMatchList)
    public AjaxResponse getNoMatchList(@RequestBody Map<String, Object> map) {
        String channelId = this.getUser().getSelChannelId();
        return success(sizeChartService.getNoMatchList(channelId, map.get("cartId").toString(), this.getLang()));
    }

    @RequestMapping(value = CmsUrlConstants.CHANNEL.LISTING.SIZE_CHART.GetListImageGroupBySizeChartId)
    public AjaxResponse getListImageGroupBySizeChartId(@RequestBody Map<String, Object> map) {
        // String channelId, int sizeChartId
        String channelId = this.getUser().getSelChannelId();
        return success(cmsSizeChartService.getListImageGroupBySizeChartId(channelId, ConvertUtil.toInt(map.get("sizeChartId"))));
    }

    @RequestMapping(value = CmsUrlConstants.CHANNEL.LISTING.SIZE_CHART.GetProductSizeChartList)
    public AjaxResponse getProductSizeChartList(@RequestBody GetProductSizeChartListParameter parameter) {
        parameter.setChannelId(getUser().getSelChannelId());
        return success(sizeChartService.getProductSizeChartList(parameter));
    }
}