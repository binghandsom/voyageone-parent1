package com.voyageone.bi.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voyageone.bi.ajax.bean.AjaxFinancialOfficialTaxReportBean;
import com.voyageone.bi.bean.OfficialTaxBean;
import com.voyageone.bi.commonutils.DateTimeUtil;
import com.voyageone.bi.mapper.OfficialTaxMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voyageone.bi.ajax.bean.AjaxFinancialCostReportBean;
import com.voyageone.bi.ajax.bean.AjaxFinancialMonthlyReportBean;
import com.voyageone.bi.ajax.bean.AjaxFinancialTaxReportBean;
import com.voyageone.bi.base.BiApplication;
import com.voyageone.bi.disbean.DetailReportDisBean;
import com.voyageone.bi.disbean.MonthlyReportDisBean;
import com.voyageone.bi.disbean.TaxDetailReportDisBean;
import com.voyageone.bi.mapper.DetailReportMapper;
import com.voyageone.bi.mapper.MonthlyReportMapper;
import com.voyageone.bi.mapper.TaxDetailReportMapper;

/**
 * Created by Kylin on 2015/7/24.
 */

@Service
public class ReportInfoDao {


    @Autowired
    private MonthlyReportMapper monthlyReportMapper;

    @Autowired
    private DetailReportMapper detailReportMapper;

    @Autowired
    private TaxDetailReportMapper taxDetailReportMapper;

    @Autowired
    private OfficialTaxMapper officialTaxMapper;

    @Transactional("transactionManager")
    public int getMonthlyReportCount(AjaxFinancialMonthlyReportBean ajaxFinancialMonthlyReportBean) {

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        //mapParameter.put("tracking_no", BiApplication.readValue("track_no"));
        mapParameter.put("date_calc_from", ajaxFinancialMonthlyReportBean.getTime_start());
        mapParameter.put("date_calc_to", ajaxFinancialMonthlyReportBean.getTime_end());
        mapParameter.put("channel_code", ajaxFinancialMonthlyReportBean.getChannel_ids());
        return monthlyReportMapper.select_count_monthly_report(mapParameter);
    }

    @Transactional("transactionManager")
    public List<MonthlyReportDisBean> getMonthlyReportList(AjaxFinancialMonthlyReportBean ajaxFinancialMonthlyReportBean) {

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        //mapParameter.put("tracking_no", BiApplication.readValue("track_no"));
        mapParameter.put("date_calc_from", ajaxFinancialMonthlyReportBean.getTime_start());
        mapParameter.put("date_calc_to", ajaxFinancialMonthlyReportBean.getTime_end());
        mapParameter.put("channel_code", ajaxFinancialMonthlyReportBean.getChannel_ids());
        //mapParameter.put("init_index", ajaxFinancialMonthlyReportBean.getReport_page() - 1);
        mapParameter.put("get_size", Integer.parseInt(BiApplication.readValue("page_size")));
        return monthlyReportMapper.select_list_monthly_report(mapParameter);
    }

    @Transactional("transactionManager")
    public int getDetailReportCount(AjaxFinancialCostReportBean ajaxFinancialCostReportBean) {

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        //mapParameter.put("tracking_no_fuzzy", BiApplication.readValue("track_no"));
        mapParameter.put("channel_code", ajaxFinancialCostReportBean.getChannel_ids());
        mapParameter.put("tracking_no", ajaxFinancialCostReportBean.getTracking_no());
        mapParameter.put("source_order_id", ajaxFinancialCostReportBean.getSource_order_id());
        mapParameter.put("client_order_number", ajaxFinancialCostReportBean.getClient_order_number());
        mapParameter.put("order_number", ajaxFinancialCostReportBean.getOrder_number());
        mapParameter.put("ship_date_from", ajaxFinancialCostReportBean.getTime_start());
        mapParameter.put("ship_date_to", ajaxFinancialCostReportBean.getTime_end());
//        if (ajaxFinancialCostReportBean.getTime_end().equals(DateTimeUtil.getAddDateTime("YYYY-MM-DD", -1))){
//            mapParameter.put("ship_date_to", "");
//        }else {
//            mapParameter.put("ship_date_to", ajaxFinancialCostReportBean.getTime_end());
//        }
        mapParameter.put("data_null_check", ajaxFinancialCostReportBean.getData_null_check());
        return detailReportMapper.select_count_detail_report(mapParameter);
    }

    @Transactional("transactionManager")
    public List<DetailReportDisBean> getDetailReportList(AjaxFinancialCostReportBean ajaxFinancialCostReportBean) {

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        //mapParameter.put("tracking_no_fuzzy", BiApplication.readValue("track_no"));
        mapParameter.put("channel_code", ajaxFinancialCostReportBean.getChannel_ids());
        mapParameter.put("tracking_no", ajaxFinancialCostReportBean.getTracking_no());
        mapParameter.put("main_waybill_num", ajaxFinancialCostReportBean.getMain_waybill_num());
        mapParameter.put("source_order_id", ajaxFinancialCostReportBean.getSource_order_id());
        mapParameter.put("client_order_number", ajaxFinancialCostReportBean.getClient_order_number());
        mapParameter.put("order_number", ajaxFinancialCostReportBean.getOrder_number());
        mapParameter.put("ship_date_from", ajaxFinancialCostReportBean.getTime_start());
        mapParameter.put("ship_date_to", ajaxFinancialCostReportBean.getTime_end());
        mapParameter.put("data_null_check", ajaxFinancialCostReportBean.getData_null_check());
        //mapParameter.put("init_index", ajaxFinancialCostReportBean.getReport_page() - 1);
        mapParameter.put("get_size", Integer.parseInt(BiApplication.readValue("page_size")));
        return detailReportMapper.select_list_detail_report(mapParameter);
    }


    @Transactional("transactionManager")
    public int getTaxDetailReportCount(AjaxFinancialTaxReportBean ajaxFinancialTaxReportBean) {

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        //mapParameter.put("tracking_no_fuzzy", BiApplication.readValue("track_no"));
        mapParameter.put("channel_code", ajaxFinancialTaxReportBean.getChannel_ids());
        mapParameter.put("tracking_no", ajaxFinancialTaxReportBean.getTracking_no());
        mapParameter.put("source_order_id", ajaxFinancialTaxReportBean.getSource_order_id());
        mapParameter.put("pay_in_warrant_num", ajaxFinancialTaxReportBean.getPay_in_warrant_num());
        mapParameter.put("order_number", ajaxFinancialTaxReportBean.getOrder_number());
        mapParameter.put("ship_date_from", ajaxFinancialTaxReportBean.getTime_start());
        mapParameter.put("ship_date_to", ajaxFinancialTaxReportBean.getTime_end());
        mapParameter.put("data_null_check", ajaxFinancialTaxReportBean.getData_null_check());
        return taxDetailReportMapper.select_count_tax_detail_report(mapParameter);
    }

    @Transactional("transactionManager")
    public List<TaxDetailReportDisBean> getTaxDetailReportList(AjaxFinancialTaxReportBean ajaxFinancialTaxReportBean) {

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        //mapParameter.put("tracking_no_fuzzy", BiApplication.readValue("track_no"));
        mapParameter.put("channel_code", ajaxFinancialTaxReportBean.getChannel_ids());
        mapParameter.put("tracking_no", ajaxFinancialTaxReportBean.getTracking_no());
        mapParameter.put("main_waybill_num", ajaxFinancialTaxReportBean.getMain_waybill_num());
        mapParameter.put("source_order_id", ajaxFinancialTaxReportBean.getSource_order_id());
        mapParameter.put("pay_in_warrant_num", ajaxFinancialTaxReportBean.getPay_in_warrant_num());
        mapParameter.put("order_number", ajaxFinancialTaxReportBean.getOrder_number());
        mapParameter.put("ship_date_from", ajaxFinancialTaxReportBean.getTime_start());
        mapParameter.put("ship_date_to", ajaxFinancialTaxReportBean.getTime_end());
        mapParameter.put("data_null_check", ajaxFinancialTaxReportBean.getData_null_check());
        //mapParameter.put("init_index", ajaxFinancialTaxReportBean.getReport_page() - 1);
        mapParameter.put("get_size", Integer.parseInt(BiApplication.readValue("page_size")));
        return taxDetailReportMapper.select_list_tax_detail_report(mapParameter);
    }

    @Transactional("transactionManager")
    public int getOfficialTaxReportCount(AjaxFinancialOfficialTaxReportBean ajaxFinancialOfficialTaxReportBean) {

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        //mapParameter.put("tracking_no_fuzzy", BiApplication.readValue("track_no"));
        mapParameter.put("channel_code", ajaxFinancialOfficialTaxReportBean.getChannel_ids());
        mapParameter.put("tracking_no", ajaxFinancialOfficialTaxReportBean.getTracking_no());
        mapParameter.put("source_order_id", ajaxFinancialOfficialTaxReportBean.getSource_order_id());
        mapParameter.put("pay_in_warrant_num", ajaxFinancialOfficialTaxReportBean.getPay_in_warrant_num());
        mapParameter.put("order_number", ajaxFinancialOfficialTaxReportBean.getOrder_number());
        mapParameter.put("ship_date_from", ajaxFinancialOfficialTaxReportBean.getTime_start());
        mapParameter.put("ship_date_to", ajaxFinancialOfficialTaxReportBean.getTime_end());
        mapParameter.put("data_null_check", ajaxFinancialOfficialTaxReportBean.getData_null_check());
        mapParameter.put("port_id", ajaxFinancialOfficialTaxReportBean.getPort_ids());
        return officialTaxMapper.select_count_official_tax_detail_report(mapParameter);
    }

    @Transactional("transactionManager")
    public List<OfficialTaxBean> getOfficialTaxReportList(AjaxFinancialOfficialTaxReportBean ajaxFinancialOfficialTaxReportBean) {

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        //mapParameter.put("tracking_no_fuzzy", BiApplication.readValue("track_no"));
        mapParameter.put("channel_code", ajaxFinancialOfficialTaxReportBean.getChannel_ids());
        mapParameter.put("tracking_no", ajaxFinancialOfficialTaxReportBean.getTracking_no());
        mapParameter.put("main_waybill_num", ajaxFinancialOfficialTaxReportBean.getMain_waybill_num());
        mapParameter.put("source_order_id", ajaxFinancialOfficialTaxReportBean.getSource_order_id());
        mapParameter.put("pay_in_warrant_num", ajaxFinancialOfficialTaxReportBean.getPay_in_warrant_num());
        mapParameter.put("order_number", ajaxFinancialOfficialTaxReportBean.getOrder_number());
        mapParameter.put("ship_date_from", ajaxFinancialOfficialTaxReportBean.getTime_start());
        mapParameter.put("ship_date_to", ajaxFinancialOfficialTaxReportBean.getTime_end());
        mapParameter.put("data_null_check", ajaxFinancialOfficialTaxReportBean.getData_null_check());
        mapParameter.put("port_id", ajaxFinancialOfficialTaxReportBean.getPort_ids());
        //mapParameter.put("init_index", ajaxFinancialTaxReportBean.getReport_page() - 1);
        mapParameter.put("get_size", Integer.parseInt(BiApplication.readValue("page_size")));
        return officialTaxMapper.select_list_official_tax_detail_report(mapParameter);
    }

}
