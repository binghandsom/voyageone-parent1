package com.voyageone.bi.dao;

import com.voyageone.bi.ajax.bean.AjaxFinancialBillBean;
import com.voyageone.bi.ajax.bean.AjaxFinancialBillErrorBean;
import com.voyageone.bi.base.BiApplication;
import com.voyageone.bi.bean.BillBean;
import com.voyageone.bi.bean.BillErrorBean;
import com.voyageone.bi.bean.BillErrorExplanationBean;
import com.voyageone.bi.mapper.BillErrorExplanationMapper;
import com.voyageone.bi.mapper.BillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kylin on 2015/7/24.
 */

@Service
public class BillInfoDao {

    @Autowired
    BillMapper billMapper;
    @Autowired
    BillErrorExplanationMapper billErrorExplanationMapper;

    @Transactional("transactionManager")
    public List<BillBean> getTranspotationBillList(AjaxFinancialBillBean bean) {

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("file_upload_date_from", bean.getTime_start());
        mapParameter.put("file_upload_date_to", bean.getTime_end());
        mapParameter.put("invoice_num", bean.getInvoice_num());
        mapParameter.put("file_type", 2);
        mapParameter.put("file_name", bean.getFile_name());
        //mapParameter.put("init_index", bean.getTranspotation_page() - 1);
        mapParameter.put("get_size", Integer.parseInt(BiApplication.readValue("page_size")));
        return billMapper.select_list_vf_bill(mapParameter);
    }

    @Transactional("transactionManager")
    public int getTranspotationBillCount(AjaxFinancialBillBean bean) {

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("file_upload_date_from", bean.getTime_start());
        mapParameter.put("file_upload_date_to", bean.getTime_end());
        mapParameter.put("invoice_num", bean.getInvoice_num());
        mapParameter.put("file_type", 2);
        mapParameter.put("file_name", bean.getFile_name());
        return billMapper.select_count_vf_bill(mapParameter);
    }


    @Transactional("transactionManager")
    public List<BillBean> getTaxBillList(AjaxFinancialBillBean bean) {

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("file_upload_date_from", bean.getTime_start());
        mapParameter.put("file_upload_date_to", bean.getTime_end());
        mapParameter.put("invoice_num", bean.getInvoice_num());
        mapParameter.put("file_type", 1);
        mapParameter.put("file_name", bean.getFile_name());
        //mapParameter.put("init_index", bean.getTax_page() - 1);
        mapParameter.put("get_size", Integer.parseInt(BiApplication.readValue("page_size")));
        return billMapper.select_list_vf_bill(mapParameter);
    }

    @Transactional("transactionManager")
    public int getTaxBillCount(AjaxFinancialBillBean bean) {

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("file_upload_date_from", bean.getTime_start());
        mapParameter.put("file_upload_date_to", bean.getTime_end());
        mapParameter.put("invoice_num", bean.getInvoice_num());
        mapParameter.put("file_type", 1);
        mapParameter.put("file_name", bean.getFile_name());
        return billMapper.select_count_vf_bill(mapParameter);
    }

    @Transactional("transactionManager")
    public List<BillErrorBean> getBillErrorList(AjaxFinancialBillErrorBean bean) {

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("invoice_num", bean.getInvoice_num());
        mapParameter.put("tracking_no", bean.getTracking_no());
        mapParameter.put("main_waybill_num", bean.getMain_waybill_num());
        mapParameter.put("pay_in_warrant_num", bean.getPay_in_warrant_num());
        mapParameter.put("error_type_id", bean.getError_type_id());
        //mapParameter.put("init_index", bean.getError_page() - 1);
        mapParameter.put("get_size", Integer.parseInt(BiApplication.readValue("page_size")));
        return billMapper.select_list_viw_fms_error(mapParameter);
    }

    @Transactional("transactionManager")
    public int getBillErrorCount(AjaxFinancialBillErrorBean bean) {

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("invoice_num", bean.getInvoice_num());
        mapParameter.put("tracking_no", bean.getTracking_no());
        mapParameter.put("main_waybill_num", bean.getMain_waybill_num());
        mapParameter.put("pay_in_warrant_num", bean.getPay_in_warrant_num());
        mapParameter.put("error_type_id", bean.getError_type_id());
        return billMapper.select_count_viw_fms_error(mapParameter);
    }

    @Transactional("transactionManager")
    public List<BillBean> getBillList(AjaxFinancialBillBean bean) {

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("file_upload_date_from", bean.getTime_start());
        mapParameter.put("file_upload_date_to", bean.getTime_end());
        mapParameter.put("invoice_num", bean.getInvoice_num());
        mapParameter.put("file_name", bean.getFile_name());
        //mapParameter.put("init_index", bean.getTax_page() - 1);
        mapParameter.put("get_size", Integer.parseInt(BiApplication.readValue("page_size")));
        return billMapper.select_list_vf_bill(mapParameter);
    }

    @Transactional("transactionManager")
    public int getBillCount(AjaxFinancialBillBean bean) {

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("file_upload_date_from", bean.getTime_start());
        mapParameter.put("file_upload_date_to", bean.getTime_end());
        mapParameter.put("invoice_num", bean.getInvoice_num());
        //mapParameter.put("file_type", 1);
        mapParameter.put("file_name", bean.getFile_name());
        return billMapper.select_count_vf_bill(mapParameter);
    }

    @Transactional("transactionManager")
    public int getBillErrorExplanationCount(AjaxFinancialBillErrorBean bean) {

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("invoice_num", bean.getInvoice_num());
        mapParameter.put("tracking_no", bean.getTracking_no());
        mapParameter.put("main_waybill_num", bean.getMain_waybill_num());
        mapParameter.put("pay_in_warrant_num", bean.getPay_in_warrant_num());
        mapParameter.put("error_type_id", bean.getError_type_id());
        mapParameter.put("error_status", bean.getExplanation_error_status());
        return billErrorExplanationMapper.select_count_vf_bill_error_explanation(mapParameter);
    }

    @Transactional("transactionManager")
    public List<BillErrorExplanationBean> getBillErrorExplanationList(AjaxFinancialBillErrorBean bean) {

        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("invoice_num", bean.getInvoice_num());
        mapParameter.put("tracking_no", bean.getTracking_no());
        mapParameter.put("main_waybill_num", bean.getMain_waybill_num());
        mapParameter.put("pay_in_warrant_num", bean.getPay_in_warrant_num());
        mapParameter.put("error_type_id", bean.getError_type_id());
        mapParameter.put("error_status", bean.getExplanation_error_status().trim());
        //mapParameter.put("init_index", bean.getExplanation_error_page() - 1);
        mapParameter.put("get_size", Integer.parseInt(BiApplication.readValue("page_size")));
        return billErrorExplanationMapper.select_list_vf_bill_error_explanation(mapParameter);
    }

}
