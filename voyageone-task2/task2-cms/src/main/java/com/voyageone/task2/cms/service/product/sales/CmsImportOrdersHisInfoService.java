package com.voyageone.task2.cms.service.product.sales;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.mail.Mail;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 从oms系统导入产品前90天订单信息,统计销量数据
 *
 * @author jason.jiang on 2016/05/24
 * @version 2.0.0
 */
@Service
public class CmsImportOrdersHisInfoService extends BaseCronTaskService {

    @Autowired
    private CmsCopyOrdersInfoService cmsCopyOrdersInfoService;
    @Autowired
    private CmsFindProdOrdersInfoService cmsFindProdOrdersInfoService;

    private static final String _Mail_Title = "昨日销量统计结果";

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsImportOrdersHisInfoJob";
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // 保存操作过程，用于邮件通知
        Map<String, Object> statusMap = new ConcurrentHashMap<>();
        String staTime = DateTimeUtil.getNow();

        // 从oms系统导入产品前90天订单信息
        Map<String, Set<String>> prodCodeChannelMap;
        try {
            prodCodeChannelMap = cmsCopyOrdersInfoService.copyOrdersInfo(getTaskName(), statusMap);
        } catch (Exception exp) {
            $error("CmsImportOrdersHisInfoService 从oms系统导入订单信息时发生错误", exp);
            Mail.sendAlert("CmsImportOrdersHisInfoService", _Mail_Title + " 从oms系统导入订单信息时发生错误", exp);
            return;
        }
        if (prodCodeChannelMap == null || prodCodeChannelMap.isEmpty()) {
            $error("CmsImportOrdersHisInfoService 从oms系统导入产品前90天订单信息为空");
            Mail.sendAlert("CmsImportOrdersHisInfoService", _Mail_Title + " 从oms系统导入订单信息时发生错误", "<p>错误信息： 从oms系统导入产品前90天订单信息为空(包括所有店铺)</p>");
            return;
        }
        // 统计销售数据
        try {
            cmsFindProdOrdersInfoService.onStartup(taskControlList, prodCodeChannelMap, statusMap);
        } catch (Exception exp) {
            $error("CmsImportOrdersHisInfoService 统计商品销量数据时发生错误", exp);
            Mail.sendAlert("CmsImportOrdersHisInfoService", _Mail_Title + " 统计商品销量数据时发生错误", exp);
            return;
        }

        // 准备邮件内容
        StringBuilder mailTxt = new StringBuilder();
        mailTxt.append("<p>[").append(DateTimeUtil.getNow(DateTimeUtil.DEFAULT_DATE_FORMAT)).append("]销量统计状况报告</p>");
        mailTxt.append("<p>第一阶段，从oms系统导入订单信息，共耗时").append(statusMap.get("fstPhase")).append("秒</p>");
        List<OrderChannelBean> list = (List<OrderChannelBean>) statusMap.get("fstPhaseRs");
        if (list != null && list.size() > 0) {
            mailTxt.append("<style>td { background-color: white; min-width: 100px; padding: 0px 10px; }</style>");
            mailTxt.append("<table border=\"0\" cellspacing=\"1\" style=\"background-color:black\"><tbody><tr><td align=\"center\">店铺</td><td align=\"center\">销量*</td></tr>");
            for (OrderChannelBean chnObj : list) {
                mailTxt.append("<tr><td>").append(chnObj.getFull_name()).append("</td><td align=\"right\">").append(chnObj.getModifier()).append("</td></tr>");
            }
            mailTxt.append("</tbody></table>注*：这里是最近90天全平台总销量<br><br>");
        }

        mailTxt.append("<p>第二阶段，根据订单信息统计销量数据：</p>");
        list = (List<OrderChannelBean>) statusMap.get("secPhase");
        if (list == null || list.isEmpty()) {
            mailTxt.append("所有店铺都没有销量数据");
        } else {
            mailTxt.append("<style>td { background-color: white; min-width: 100px; padding: 0px 10px; }</style>");
            mailTxt.append("<table border=\"0\" cellspacing=\"1\" style=\"background-color:black\"><tbody><tr><td align=\"center\">店铺</td><td align=\"center\">耗时（秒）</td></tr>");
            for (OrderChannelBean chnObj : list) {
                mailTxt.append("<tr><td>").append(chnObj.getFull_name()).append("</td><td align=\"right\">").append(chnObj.getModifier()).append("</td></tr>");
            }
            mailTxt.append("</tbody></table>");
        }
        mailTxt.append("<br><br><p>处理开始时间：").append(staTime).append("<br>处理结束时间：").append(DateTimeUtil.getNow()).append("</p>");

        Mail.send2("CmsImportOrdersHisInfoService", _Mail_Title, mailTxt.toString());
    }

}
