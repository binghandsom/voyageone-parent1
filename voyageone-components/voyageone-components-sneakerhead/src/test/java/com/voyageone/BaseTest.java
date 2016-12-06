package com.voyageone;

import com.voyageone.common.logger.VOAbsLoggable;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by vantis on 2016/11/28.
 * 闲舟江流夕照晚 =。=
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class BaseTest extends VOAbsLoggable {
}
