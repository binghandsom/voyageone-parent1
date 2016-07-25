package com.voyageone.task2.vms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.VmsChannelConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.VmsChannelConfigBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.vms.VmsBtFinancialReportDao;
import com.voyageone.service.daoext.vms.VmsBtOrderDetailDaoExt;
import com.voyageone.service.model.vms.VmsBtFinancialReportModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.vms.VmsConstants;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成财务报表
 * Created on 16/06/29.
 * @author jeff.duan
 * @version 1.0
 */
@Service
public class VmsMakeFinancialReportService extends BaseTaskService {

    @Autowired
    private VmsBtFinancialReportDao vmsBtFinancialReportDao;

    @Autowired
    private VmsBtOrderDetailDaoExt vmsBtOrderDetailDaoExt;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.VMS;
    }

    @Override
    public String getTaskName() {
        return "VmsMakeFinancialReportJob";
    }


    /**
     * 生成财务报表
     *
     * @param taskControlList job 配置
     * @throws Exception
     */
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 允许运行的渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        if (orderChannelIdList != null && orderChannelIdList.size() > 0) {

            // 按渠道进行处理
            for (String orderChannelID : orderChannelIdList) {
                makeFinancialReport(orderChannelID);
            }
        }
    }

    /**
     * 生成财务报表
     *
     * @param channelId 渠道
     */
    public void makeFinancialReport(String channelId) throws ParseException {

        // 取得系统日期
        String date = DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_3);

        // 取得这个渠道出财务报表的日
        String makeReportDay = "01";
        VmsChannelConfigBean vmsChannelConfigBean = VmsChannelConfigs.getConfigBean(channelId
                , VmsConstants.ChannelConfig.MAKE_FINANCIAL_REPORT_DAY
                ,VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);
        if (vmsChannelConfigBean != null) {
            String day = vmsChannelConfigBean.getConfigValue1();
            if (!StringUtils.isEmpty(day) && day.length() <= 2) {
                if (day.length() == 1) {
                    day = "0" + day;
                }
                makeReportDay = day;
            }
        }

        // 系统日期的日 = 出财务报表的日，那么准备出财务报表
        if (!makeReportDay.equals(date.substring(6,8))) {
            return;
        }

        // 看看vms_bt_financial_report表里有没有出过这个月的财务报表
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", channelId);
        param.put("reportYearMonth", date.substring(0,6));
        List<VmsBtFinancialReportModel> models = vmsBtFinancialReportDao.selectList(param);
        if (models.size() > 0) {
            return;
        }

        // 取得这期财务报表的数据
        Map<String, Object> param1 = new HashMap<>();
        param1.put("channelId", channelId);
        param1.put("status", VmsConstants.STATUS_VALUE.PRODUCT_STATUS.RECEIVED);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strReceivedTimeFrom = date.substring(0,4) + "-" + date.substring(4,6) + "-01 00:00:00";
        Date receivedTimeFrom = sdf.parse(strReceivedTimeFrom);
        param1.put("receivedTimeFrom", receivedTimeFrom);
        param1.put("receivedTimeTo", DateTimeUtil.addMonths(receivedTimeFrom, 1));
        List<Map<String, Object>> reportDataList = vmsBtOrderDetailDaoExt.selectListByTime(param1);

        makeFinancialReportExcel(channelId, strReceivedTimeFrom, reportDataList);

    }

    /**
     * 生成财务报表Excel
     *
     * @param channelId 渠道
     * @param strReceivedTimeFrom 报表开始时间
     * @param reportDataList 报表数据
     *
     */
    public void makeFinancialReportExcel(String channelId, String strReceivedTimeFrom, List<Map<String, Object>> reportDataList) throws ParseException {

        OrderChannelBean channel = Channels.getChannel(channelId);

        String reportDateStart = strReceivedTimeFrom.substring(0,4) + strReceivedTimeFrom.substring(5,7) + strReceivedTimeFrom.substring(8,10);

        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook();
        sxssfWorkbook.setCompressTempFiles(true);

        Font boldFont = sxssfWorkbook.createFont();
        boldFont.setBold(true);


        Sheet sheet = sxssfWorkbook.createSheet(reportDateStart.substring(0, 6));

        // 设置Header行格式
        CellStyle headerCellStyle = sxssfWorkbook.createCellStyle();
        headerCellStyle.setFont(boldFont);


        // 设置单元格默认格式
        CellStyle defaultRowCellStyle = sxssfWorkbook.createCellStyle();
        defaultRowCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        defaultRowCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        defaultRowCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        defaultRowCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        defaultRowCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        defaultRowCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);


        // 标题行格式
        CellStyle titleRowCellStyle = sxssfWorkbook.createCellStyle();
        titleRowCellStyle.setFont(boldFont);
        titleRowCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        titleRowCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        titleRowCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        titleRowCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titleRowCellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        titleRowCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        titleRowCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        titleRowCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        /* 设置Header行 */
        Row headerRow = sheet.createRow(0);

        // Company
        Cell headerRowCell = headerRow.createCell(0, Cell.CELL_TYPE_STRING);
        headerRowCell.setCellValue("Company:" + channel.getFull_name());
        headerRowCell.setCellStyle(headerCellStyle);

    }
}