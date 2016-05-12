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

/**
 * Created by gjl on 2016/5/11.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.CHANNEL.LISTING.SIZE_CHART_DETAIL.ROOT, method = RequestMethod.POST)
public class CmsSizeChartDetailController extends CmsController {
    @Autowired
    private CmsSizeChartDetailService cmsSizeChartDetailService;
    /**
     * 尺码关系一览编辑检索画面
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.LISTING.SIZE_CHART_DETAIL.SEARCH_DETAIL_SIZE_CHART)
    public AjaxResponse sizeChartDetailSearch(@RequestBody Map param) {
        //店铺渠道取得
        String channelId=this.getUser().getSelChannelId();
        //创建者/更新者用
        param.put("userName", this.getUser().getUserName());
        //取得尺码关系一览初始化
        cmsSizeChartDetailService.sizeChartDetailSearch(channelId, param, getLang());
        //返回数据的类型
        return success(param);
    }
    /**
     * 尺码关系一览编辑详情编辑画面(编辑尺码详情)
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.LISTING.SIZE_CHART_DETAIL.SAVE_DETAIL_SIZE_CHART)
    public AjaxResponse sizeChartDetailSave(@RequestBody Map param) {
        //店铺渠道取得
        String channelId=this.getUser().getSelChannelId();
        //创建者/更新者用
        param.put("userName", this.getUser().getUserName());
        //取得尺码关系一览保存
        cmsSizeChartDetailService.sizeChartDetailUpdate(channelId, param);
        //返回数据的类型
        return success(param);
    }
    /**
     * 尺码关系一览编辑详情编辑画面(编辑尺码表)
     */
    @RequestMapping(value = CmsUrlConstants.CHANNEL.LISTING.SIZE_CHART_DETAIL.SAVE_DETAIL_SIZE_MAP_CHART)
    public AjaxResponse sizeChartDetailSizeMapSave(@RequestBody Map param) {
        //店铺渠道取得
        String channelId=this.getUser().getSelChannelId();
        //创建者/更新者用
        param.put("userName", this.getUser().getUserName());
        //取得尺码关系一览保存
        cmsSizeChartDetailService.sizeChartDetailSizeMapSave(channelId, param);
        //返回数据的类型
        return success(param);
    }
}
