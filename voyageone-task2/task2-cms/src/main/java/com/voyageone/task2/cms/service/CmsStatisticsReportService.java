package com.voyageone.task2.cms.service;

import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.dao.SearchDao;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.mail.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/10/20.
 */
@Service
public class CmsStatisticsReportService extends BaseCronTaskService {

    @Autowired
    private SearchDao searchDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "cmsStatisticsReportJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        Map<String,List<Integer>> statisticsInfo = new HashMap<>();
        for(TaskControlBean taskControl : taskControlList){
            if("order_channel_id".equalsIgnoreCase(taskControl.getCfg_name())){
                String channelId = taskControl.getCfg_val1();
                ChannelConfigEnums.Channel channel=  ChannelConfigEnums.Channel.valueOfId(channelId);
                List<Integer>statistics = doQuickSearch(channelId);
                statisticsInfo.put(channel.getFullName(), statistics);
            }
        }
        Mail.sendAlert("CmsStatisticsReport", "CMS统计数据", getMailContent(statisticsInfo), true);
    }

    public List<Integer> doQuickSearch(String channelId) throws IOException {

        // 1.未匹配类目：
        List<Integer> ret=new ArrayList<>();

        ret.add(searchDao.doCategoryUnMappingCnt(channelId));

        // 2.属性匹配未完成类目:
        ret.add(searchDao.doCategoryPropertyUnMappingCnt(channelId));

        // 3.属性编辑未完成产品:
        Map<String,Object> s = new HashMap<>();
        s.put("channelId", channelId);
        s.put("isApprovedDescription", "0");
        s.put("cartId", 23);
        ret.add(searchDao.doAdvanceSearchCnt(s));

        // 4.属性编辑完成未approve产品:
        s = new HashMap<>();
        s.put("channelId", channelId);
        s.put("isApprovedDescription", "1");
        s.put("isApproved", "0");
        s.put("cartId", 23);
        ret.add(searchDao.doAdvanceSearchCnt(s));

        // 5.approve但未上新产品:
        s = new HashMap<>();
        s.put("channelId", channelId);
        s.put("isApproved", "1");
        s.put("publishStatus", "0");
        s.put("cartId", 23);
        ret.add(searchDao.doAdvanceSearchCnt(s));

        // 6.approve但还没有更新:
        s = new HashMap<>();
        s.put("channelId", channelId);
        s.put("publishStatus", "3");
        s.put("cartId",23);
        ret.add(searchDao.doAdvanceSearchCnt(s));

        // 7.approve上新失败:
        s = new HashMap<>();
        s.put("channelId", channelId);
        s.put("publishStatus", "2");
        s.put("cartId",23);
        ret.add(searchDao.doAdvanceSearchCnt(s));
        return  ret;
    }

    /**
     * 生产邮件的内容
     * @param statisticsInfo
     * @return
     */
    private String getMailContent(Map<String,List<Integer>> statisticsInfo) {

        StringBuffer sb = new StringBuffer();

        sb.append("各位");
        sb.append("<br>");
        sb.append("以下信息是各个店铺的CMS中的统计数据");
        sb.append("<br>");

        sb.append("<table border=\"1\" style=\"border-collapse:collapse\">");
        sb.append("<tr bgcolor=\"#CCCCCC\">");
        sb.append("<td align=center>店铺</td>");
        sb.append("<td align=center>Feed类目未匹配</td>");
        sb.append("<td align=center>主类目属性未匹配</td>");
        sb.append("<td align=center>产品属性编辑未完成(未翻译)</td>");
        sb.append("<td align=center>产品属性编辑完成未Approve</td>");
        sb.append("<td align=center>产品Approved但未上新</td>");
        sb.append("<td align=center>已上新但未更新</td>");
        sb.append("<td align=center>产品上新失败</td>");
        sb.append("</tr>");


        statisticsInfo.forEach(
                (channel, integers) -> {
                    sb.append("<tr>");
                    sb.append("<td>");
                    sb.append(channel);
                    sb.append("</td>");
                    integers.forEach(count -> {
                        sb.append("<td>");
                        sb.append(count.toString());
                        sb.append("</td>");
                    });
                    sb.append("</tr>");
                }
        );

        sb.append("</table>");
        return sb.toString();
    }
}
