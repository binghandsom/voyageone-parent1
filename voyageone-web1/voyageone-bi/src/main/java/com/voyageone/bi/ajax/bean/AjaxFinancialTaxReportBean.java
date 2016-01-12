package com.voyageone.bi.ajax.bean;

import com.voyageone.bi.base.AjaxRequestBean;
import com.voyageone.bi.base.AjaxResponseBean;
import com.voyageone.bi.commonutils.ConditionUtil;
import com.voyageone.bi.commonutils.Contants;
import com.voyageone.bi.commonutils.StringUtils;
import com.voyageone.bi.disbean.TaxDetailReportDisBean;
import com.voyageone.common.util.DateTimeUtil;

import java.util.List;

// 页面初期化用
public class AjaxFinancialTaxReportBean extends AjaxRequestBean {

    public class Result extends AjaxResponseBean {
        // 月度报表请求
        private List<TaxDetailReportDisBean> taxDetailReportDisBean;

        public List<TaxDetailReportDisBean> getTaxDetailReportDisBean() {
            return taxDetailReportDisBean;
        }

        public void setTaxDetailReportDisBean(List<TaxDetailReportDisBean> taxDetailReportDisBean) {
            this.taxDetailReportDisBean = taxDetailReportDisBean;
        }

        private int report_page;
        private int report_page_size;
        private int report_record_count;

        private String report_file_path;
        private String report_file_name;

        public int getReport_page() {
            return report_page;
        }

        public void setReport_page(int report_page) {
            this.report_page = report_page;
        }

        public int getReport_page_size() {
            return report_page_size;
        }

        public void setReport_page_size(int report_page_size) {
            this.report_page_size = report_page_size;
        }

        public int getReport_record_count() {
            return report_record_count;
        }

        public void setReport_record_count(int report_record_count) {
            this.report_record_count = report_record_count;
        }

        public String getReport_file_path() {
            return report_file_path;
        }

        public void setReport_file_path(String report_file_path) {
            this.report_file_path = report_file_path;
        }

        public String getReport_file_name() {
            return report_file_name;
        }

        public void setReport_file_name(String report_file_name) {
            this.report_file_name = report_file_name;
        }
    }

    // 渠道
    private String channel_ids = null;

    // 快件单号
    private String tracking_no = null;

    // 提单号
    private String main_waybill_num = null;

    // 店铺订单号
    private String source_order_id = null;

    // 系统订单号
    private String order_number = null;

    private String pay_in_warrant_num = null;

    private String data_null_check = null;

    // 时间
    private String time_start;
    private String time_end;

    private int report_page;
    private int report_page_size;
    private int report_record_count;


    private String web_root_path;

    private String web_file_path;

    private String report_file_path;
    private String report_file_name;

    public String getChannel_ids() {
        return channel_ids;
    }

    public void setChannel_ids(String channel_ids) {
        this.channel_ids = channel_ids;
    }

    public String getTracking_no() {
        return tracking_no;
    }

    public void setTracking_no(String tracking_no) {
        this.tracking_no = tracking_no;
    }

    public String getSource_order_id() {
        return source_order_id;
    }

    public void setSource_order_id(String source_order_id) {
        this.source_order_id = source_order_id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }


    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public String getData_null_check() {
        return data_null_check;
    }

    public void setData_null_check(String data_null_check) {
        this.data_null_check = data_null_check;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public int getReport_page() {
        return report_page;
    }

    public void setReport_page(int report_page) {
        this.report_page = report_page;
    }

    public int getReport_page_size() {
        return report_page_size;
    }

    public void setReport_page_size(int report_page_size) {
        this.report_page_size = report_page_size;
    }

    public int getReport_record_count() {
        return report_record_count;
    }

    public void setReport_record_count(int report_record_count) {
        this.report_record_count = report_record_count;
    }

    public String getPay_in_warrant_num() {
        return pay_in_warrant_num;
    }

    public void setPay_in_warrant_num(String pay_in_warrant_num) {
        this.pay_in_warrant_num = pay_in_warrant_num;
    }

    public String getWeb_root_path() {
        return web_root_path;
    }

    public void setWeb_root_path(String web_root_path) {
        this.web_root_path = web_root_path;
    }

    public String getWeb_file_path() {
        return web_file_path;
    }

    public void setWeb_file_path(String web_file_path) {
        this.web_file_path = web_file_path;
    }

    public String getReport_file_path() {
        return report_file_path;
    }

    public void setReport_file_path(String report_file_path) {
        this.report_file_path = report_file_path;
    }

    public String getReport_file_name() {
        return report_file_name;
    }

    public void setReport_file_name(String report_file_name) {
        this.report_file_name = report_file_name;
    }

    public String getMain_waybill_num() {
        return main_waybill_num;
    }

    public void setMain_waybill_num(String main_waybill_num) {
        this.main_waybill_num = main_waybill_num;
    }

    // 入力参数检查
    @Override
    public boolean checkInput() {

        if (StringUtils.isEmpty(time_start)) {
            AjaxResponseBean result = getResponseBean();
            result.setReqResult(Contants.AJAX_RESULT_FALSE);
            result.setReqResultInfo("start date is needed");
            return false;
        }

        String msg = ConditionUtil.checkStartDate(time_start);
        if (msg != null) {
            AjaxResponseBean result = getResponseBean();
            result.setReqResult(Contants.AJAX_RESULT_FALSE);
            result.setReqResultInfo(msg);
            return false;
        }
        if (StringUtils.isEmpty(time_end)) {
            AjaxResponseBean result = getResponseBean();
            result.setReqResult(Contants.AJAX_RESULT_FALSE);
            result.setReqResultInfo("end date is needed");
            return false;
        }
        msg = ConditionUtil.checkEndDate(time_end);
        if (msg != null) {
            AjaxResponseBean result = getResponseBean();
            result.setReqResult(Contants.AJAX_RESULT_FALSE);
            result.setReqResultInfo(msg);
            return false;
        }

        // 门店
        if (channel_ids == null || channel_ids.length() == 0) {
            AjaxResponseBean result = getResponseBean();
            result.setReqResult(Contants.AJAX_RESULT_FALSE);
            result.setReqResultInfo("shop is needed");
            return false;
        }

        if(DateTimeUtil.getDate().before(DateTimeUtil.parse(time_start, DateTimeUtil.DEFAULT_DATE_FORMAT))){
            AjaxResponseBean result = getResponseBean();
            result.setReqResult(Contants.AJAX_RESULT_FALSE);
            result.setReqResultInfo("start date can not be earlier than current day");
            return false;
        }

        if(DateTimeUtil.getDate().before(DateTimeUtil.parse(time_end, DateTimeUtil.DEFAULT_DATE_FORMAT))){
            AjaxResponseBean result = getResponseBean();
            result.setReqResult(Contants.AJAX_RESULT_FALSE);
            result.setReqResultInfo("end date can not be earlier than current day");
            return false;
        }

        if(DateTimeUtil.parse(time_end, DateTimeUtil.DEFAULT_DATE_FORMAT).before(DateTimeUtil.parse(time_start, DateTimeUtil.DEFAULT_DATE_FORMAT))){
            AjaxResponseBean result = getResponseBean();
            result.setReqResult(Contants.AJAX_RESULT_FALSE);
            result.setReqResultInfo("end date can not be earlier than start day");
            return false;
        }

        return true;
    }

    @Override
    protected AjaxResponseBean initResponseBean() {
        return new Result();
    }

}
