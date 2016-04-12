package com.voyageone.common.configs;

import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author aooer 2016/4/5.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class UsJoisTest {


    @Test
    public void testIsExists() throws Exception {
        List<OrderChannelBean> aa = Channels.getUsJoiChannelList();
        System.out.println();
    }
}