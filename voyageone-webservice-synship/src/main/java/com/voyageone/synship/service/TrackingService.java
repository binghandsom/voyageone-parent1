package com.voyageone.synship.service;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JaxbUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.synship.SynshipConstants;
import com.voyageone.synship.dao.TrackingDao;
import com.voyageone.synship.formbean.TrackInfoBean;
import com.voyageone.synship.formbean.TrackInfoJuMeiBean;
import com.voyageone.synship.formbean.TrackInfoJuMeiListBean;
import com.voyageone.synship.modelbean.WaybillRouteBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2015/7/27.
 */
@Service
public class TrackingService {

    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private TrackingDao trackingDao;

    /**
     * 根据Synship物流单号取得该订单的物流信息
     * @param synShipNo 检索参数
     * @return String 物流信息
     */
    public String getTrackingInfo(String synShipNo, PlatFormEnums.PlatForm platForm) {

        logger.info("需要查询的物流单号：" + synShipNo + "，商家：" + platForm.toString());

        // 根据检索参数取得记录
        List<TrackInfoBean> resultMap = trackingDao.getTrackingInfo(synShipNo);

        List<TrackInfoBean> lstTrackInfo = new ArrayList<>();

        // 对于物流信息进行编辑
        for (TrackInfoBean trackInfo : resultMap) {

            // 操作时间的本地化
            trackInfo.setProcess_time(DateTimeUtil.getLocalTime(trackInfo.getProcess_time(), SynshipConstants.TimeZone.CN));

            // 跟踪信息的设置(如果有快递公司、单号，则需要拼接输出)
            if (!StringUtils.isNullOrBlank2(trackInfo.getTracking_no())) {
                trackInfo.setTracking_event(String.format(trackInfo.getTracking_info(), StringUtils.null2Space2(trackInfo.getTracking_type()), StringUtils.null2Space2(trackInfo.getTracking_no())));
            } else {
                trackInfo.setTracking_event(trackInfo.getTracking_info());
            }

            lstTrackInfo.add(trackInfo);

            // 详细物流信息的展开
            if (SynshipConstants.TrackingInfo.SPREAD.equals(StringUtils.null2Space2(trackInfo.getTracking_spread_flg())) && !StringUtils.isNullOrBlank2(trackInfo.getTracking_no())) {
                List<WaybillRouteBean> lstWaybillRoute = trackingDao.getWaybillRoute(trackInfo.getTracking_no(), trackInfo.getTracking_type());

                for (WaybillRouteBean waybillRoute : lstWaybillRoute) {

                    TrackInfoBean trackInfoDetail = new TrackInfoBean();

                    trackInfoDetail.setSyn_ship_no(trackInfo.getSyn_ship_no());
                    trackInfoDetail.setOrder_channel_id(trackInfo.getOrder_channel_id());
                    trackInfoDetail.setCart_id(trackInfo.getCart_id());
                    trackInfoDetail.setLocation(trackInfo.getLocation());
                    trackInfoDetail.setDisplay_flg(trackInfo.getDisplay_flg());
                    trackInfoDetail.setDisplay_status(trackInfo.getDisplay_status());

                    trackInfoDetail.setProcess_time(DateTimeUtil.getLocalTime(waybillRoute.getAcceptTime(), SynshipConstants.TimeZone.CN));
                    // 拼接后的路由信息
                    if (!StringUtils.isNullOrBlank2(waybillRoute.getRemark())) {
                        trackInfoDetail.setTracking_event(waybillRoute.getAcceptAddress() + ":"+waybillRoute.getRemark());
                    } else {
                        trackInfoDetail.setTracking_event(waybillRoute.getAcceptAddress());
                    }

                    lstTrackInfo.add(trackInfoDetail);
                }
            }

        }

        String resultTrackingInfo = "";

        // 根据平台，调用相应的 输入方法
        switch (platForm) {
            case OF:
            case TM:
            case JD:
            case CN:
                break;
            case JM:
                resultTrackingInfo = createTrackingInfoJM(lstTrackInfo);
                break;

        }

        // 返回抽出结果
        return resultTrackingInfo;
    }

    private String createTrackingInfoJM(List<TrackInfoBean> lstTrackInfo) {

        List<TrackInfoJuMeiBean> lstTrackInfoJuMei = new ArrayList<>();
        int idx = 0;

        for (TrackInfoBean trackInfo : lstTrackInfo) {
            TrackInfoJuMeiBean trackInfoJuMeiBean = new TrackInfoJuMeiBean();

            // 仅仅设置需要显示的节点
            if (SynshipConstants.TrackingInfo.DISPLAY.equals(StringUtils.null2Space2(trackInfo.getDisplay_flg()))) {
                idx = idx + 1;
                trackInfoJuMeiBean.setResultcount(String.valueOf(idx));
                trackInfoJuMeiBean.setCwb(trackInfo.getSyn_ship_no());
                trackInfoJuMeiBean.setTrackdatetime(trackInfo.getProcess_time());
                trackInfoJuMeiBean.setBranchname(trackInfo.getLocation());
                trackInfoJuMeiBean.setTrackevent(trackInfo.getTracking_event());
                trackInfoJuMeiBean.setPodresultname(trackInfo.getDisplay_status());

                lstTrackInfoJuMei.add(trackInfoJuMeiBean);

            }
        }

        logger.info("返回的物流信息件数："+lstTrackInfoJuMei.size());

        String trackingInfoJMXml = "";
        if (lstTrackInfoJuMei.size() > 0) {
            TrackInfoJuMeiListBean trackInfoJuMeiListBean = new TrackInfoJuMeiListBean();
            trackInfoJuMeiListBean.setRow(lstTrackInfoJuMei);
            trackingInfoJMXml = JaxbUtil.convertToXml(trackInfoJuMeiListBean, "UTF-8");
        }
        return trackingInfoJMXml;
    }

}
