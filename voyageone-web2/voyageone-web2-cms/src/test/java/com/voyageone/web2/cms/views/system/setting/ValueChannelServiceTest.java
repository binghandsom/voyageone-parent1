package com.voyageone.web2.cms.views.system.setting;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author james.li on 2016/8/15.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class ValueChannelServiceTest {

    @Autowired
    ValueChannelService valueChannelService;
    @Test
    public void testAddHsCodes() throws Exception {
        valueChannelService.addHsCodes("777",Arrays.asList("aaa","bbbb"),43,"james");
    }
}