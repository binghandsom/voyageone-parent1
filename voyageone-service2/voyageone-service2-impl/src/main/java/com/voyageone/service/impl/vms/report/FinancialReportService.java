package com.voyageone.service.impl.vms.report;

import com.voyageone.service.dao.vms.VmsBtFinancialReportDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.vms.VmsBtFinancialReportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * FinancialReportService
 *
 * @author jeff.duan 16/7/7
 * @version 1.0.0
 */
@Service
public class FinancialReportService extends BaseService {

    @Autowired
    private VmsBtFinancialReportDao vmsBtFinancialReportDao;



    /**
     * 条件搜索FeedFile
     * @param param 搜索条件
     * @return FinancialReport列表
     */
    public List<VmsBtFinancialReportModel> getFinancialReportList(Map<String, Object> param) {

        return vmsBtFinancialReportDao.selectList(param);

    }
}
