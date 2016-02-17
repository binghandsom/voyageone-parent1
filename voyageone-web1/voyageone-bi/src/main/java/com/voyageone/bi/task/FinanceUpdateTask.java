package com.voyageone.bi.task;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.batch.bi.etl.job.BaseBiTaskJob;
import com.voyageone.batch.bi.etl.service.BaseBiKettleService;
import com.voyageone.bi.task.sup.FinancialBillUpdateTask;
import com.voyageone.common.util.DateTimeUtil;

@Service
public class FinanceUpdateTask extends BaseBiTaskJob {

    @Autowired
    FinancialBillUpdateTask financialBillUpdateTask;

    @Override
    protected BaseBiKettleService[] getTaskServices() {
        BaseBiKettleService.setJobXmlPath("/kettle_xml/finance/");
        Map<String, String> params = new HashMap<String, String>();
        Date date = DateTimeUtil.addDays(-30);
        String strDate = DateTimeUtil.format(date, "yyyy-MM-dd HH:mm:ss");
        params.put("start_date", strDate);
        financialBillUpdateTask.setParams(params);
        return new BaseBiKettleService[]{financialBillUpdateTask};
    }
}
