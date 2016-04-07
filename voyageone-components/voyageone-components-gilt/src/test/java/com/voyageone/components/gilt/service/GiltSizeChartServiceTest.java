package com.voyageone.components.gilt.service;

import com.voyageone.components.gilt.bean.GiltSizeChart;
import com.voyageone.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class GiltSizeChartServiceTest {

    @Autowired
    private GiltSizeChartService giltSizeChartService;

    @Test
    public void testGetSizeChartById() throws Exception {
        GiltSizeChart giltSizeChart=giltSizeChartService.getSizeChartById("1");
        System.out.println(JsonUtil.getJsonString(giltSizeChart));
    }
}