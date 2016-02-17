package com.voyageone.batch.bi.etl.job;

import com.voyageone.batch.bi.etl.service.BaseBiKettleService;
import com.voyageone.batch.bi.etl.service.VfTaxTranspotationService;
import com.voyageone.batch.bi.etl.service.VfTrackOrdersService;
import com.voyageone.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("biVfTaxTranspotationJob")
public class BiVfTaxTranspotationJob extends BaseBiTaskJob {

    @Autowired
    VfTaxTranspotationService vfTaxTranspotationService;

    @Override
    protected BaseBiKettleService[] getTaskServices() {
		BaseBiKettleService.setJobXmlPath("/kettle_xml/finance/");
        Map<String, String> params = new HashMap<String, String>();
        Date date = DateTimeUtil.addDays(-30);
        String strDate = DateTimeUtil.format(date, "yyyy-MM-dd HH:mm:ss");
        params.put("start_date", strDate);
        vfTaxTranspotationService.setParams(params);
        return new BaseBiKettleService[]{vfTaxTranspotationService};
    }
}
