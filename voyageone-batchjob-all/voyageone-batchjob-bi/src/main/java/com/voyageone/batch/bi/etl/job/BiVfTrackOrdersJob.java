package com.voyageone.batch.bi.etl.job;

import com.voyageone.batch.bi.etl.service.BaseBiKettleService;
import com.voyageone.batch.bi.etl.service.VfTrackOrdersService;
import com.voyageone.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("biVfTrackOrdersJob")
public class BiVfTrackOrdersJob extends BaseBiTaskJob {

    @Autowired
    VfTrackOrdersService vfTrackOrdersService;

    @Override
    protected BaseBiKettleService[] getTaskServices() {
		BaseBiKettleService.setJobXmlPath("/kettle_xml/finance/");
        Map<String, String> params = new HashMap<String, String>();
        Date date = DateTimeUtil.addDays(-70);
        String strDate = DateTimeUtil.format(date, "yyyy-MM-dd");
        params.put("start_date", strDate);
        vfTrackOrdersService.setParams(params);
        return new BaseBiKettleService[]{vfTrackOrdersService};
    }
}
