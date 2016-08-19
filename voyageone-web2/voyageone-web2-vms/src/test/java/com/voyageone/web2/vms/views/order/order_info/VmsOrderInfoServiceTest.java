package com.voyageone.web2.vms.views.order.order_info;

import com.voyageone.web2.vms.bean.order.OrderSearchInfoBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.util.JacksonUtil;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * JUnit 4 OrderInfoService
 * Created by vantis on 16-7-6.
 */
@WebAppConfiguration
@ContextConfiguration({"classpath*:META-INF/context-web2.xml","classpath*:META-INF/context-web2-mvc.xml"})
public class VmsOrderInfoServiceTest extends VOAbsLoggable {
    @Test
    public void getAllSkuStatusesList() throws Exception {
        String json = "{\"curr\":1,\"total\":0,\"size\":10,\"status\":\"1\",\"consolidationOrderId\":\"\"," +
                "\"sku\":\"\",\"orderDateFrom\":1453478400000,\"orderDateTo\":1469116800000}";
        OrderSearchInfoBean orderInfoBean = JacksonUtil.json2Bean(json, OrderSearchInfoBean.class);
        System.out.println(orderInfoBean);
    }

}