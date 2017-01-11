package com.voyageone.web2.cms.views.biReport.consult;

import com.voyageone.common.PageQueryParameters;
import com.voyageone.common.configs.beans.TestBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/1/9.
 */
@Service
public class BiRepConsultService {
    public Map<String, Object> biRepDownload(PageQueryParameters parameters) {
        String channel=parameters.getParameterValue("channel");
        String channelStart=parameters.getParameterValue("channelStart");
        String channelEnd=parameters.getParameterValue("channelEnd");
        Map<String, Object> result = new HashMap<>();
        List<TestBean> testBeanList=new ArrayList<TestBean>();
        for(int i=0;i<10;i++)
        {
            TestBean testBean=new TestBean("channel"+i,channel,channelStart,channelEnd);
            testBeanList.add(testBean);
        }
        result.put("testBeanList",testBeanList);
      /*  result.put("platformTypeList", TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, language));
        result.put("promotionStatus", TypeConfigEnums.MastType.promotionStatus.getList(language));*/

        return result;
    }
    //create the bi_report xls file
    //first step:create the xls recognized fileName













}
