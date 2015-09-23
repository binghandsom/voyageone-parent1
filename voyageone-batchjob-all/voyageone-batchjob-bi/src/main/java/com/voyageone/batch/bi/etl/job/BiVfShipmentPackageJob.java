package com.voyageone.batch.bi.etl.job;

import com.voyageone.batch.bi.etl.service.BaseBiKettleService;
import com.voyageone.batch.bi.etl.service.VfShipmentPackageService;
import com.voyageone.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("biVfShipmentPackageJob")
public class BiVfShipmentPackageJob extends BaseBiTaskJob {

    @Autowired
    VfShipmentPackageService vfShipmentPackageService;

    @Override
    protected BaseBiKettleService[] getTaskServices() {
        BaseBiKettleService.setJobXmlPath("/kettle_xml/finance/");
        Map<String, String> params = new HashMap<String, String>();
        Date date = DateTimeUtil.addDays(-20);
        String strDate = DateTimeUtil.format(date, "yyyy-MM-dd");
        params.put("start_date", strDate);
        vfShipmentPackageService.setParams(params);
        return new BaseBiKettleService[]{vfShipmentPackageService};
    }
}
