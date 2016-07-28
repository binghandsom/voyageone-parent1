package com.voyageone.service.impl.vms.report;

import com.voyageone.service.dao.vms.VmsBtFinancialReportDao;
import com.voyageone.service.daoext.vms.VmsBtFinancialReportDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.vms.VmsBtFinancialReportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    @Autowired
    private VmsBtFinancialReportDaoExt vmsBtFinancialReportDaoExt;

    /**
     * 条件搜索FinancialReport
     * @param param 搜索条件
     * @return FinancialReport列表
     */
    public List<VmsBtFinancialReportModel> getFinancialReportList(Map<String, Object> param) {

        return vmsBtFinancialReportDao.selectList(param);

    }

    /**
     * 更新FinancialReport状态
     * @param channelId 渠道id
     * @param status 状态
     * @param modifier 更新者
     * @return 更新件数
     */
    public int updateFinancialReportStatus(String channelId, Integer id, String status, String modifier) {
        VmsBtFinancialReportModel model = new VmsBtFinancialReportModel();
        model.setChannelId(channelId);
        model.setId(id);
        model.setStatus(status);
        model.setModifier(modifier);
        return vmsBtFinancialReportDaoExt.update(model);

    }

}
