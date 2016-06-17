package com.voyageone.components.overstock.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.overstock.bean.event.OverstockEventTypeUpdateRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author aooer 2016/6/8.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class OverstockEventServiceTest {

    private static final Logger log = LoggerFactory.getLogger("test");

    @Autowired
    private OverstockEventService overstockEventService;

    @Test
    public void testQueryingForNewShipment() throws Exception {
        log.info(JacksonUtil.bean2Json(overstockEventService.queryingForNewShipment()));
    }

    @Test
    public void testQueryingForNewOrderLineCanceled() throws Exception {
        log.info(JacksonUtil.bean2Json(overstockEventService.queryingForNewOrderLineCanceled()));
    }

    @Test
    public void testQueryingForNewVariationInventory() throws Exception {
        log.info(JacksonUtil.bean2Json(overstockEventService.queryingForNewVariationInventory()));
    }

    @Test
    public void testQueryingForNewVariation() throws Exception {
        log.info(JacksonUtil.bean2Json(overstockEventService.queryingForNewVariation()));
    }

    @Test
    public void testQueryingForNewVariationNew() throws Exception {
        log.info(JacksonUtil.bean2Json(overstockEventService.queryingForNewVariationNew()));
    }

    @Test
    public void testQueryingForNewVariationPrice() throws Exception {
        log.info(JacksonUtil.bean2Json(overstockEventService.queryingForNewVariationPrice()));
    }

    @Test
    public void testQueryingForNewOrderLineCredit() throws Exception {
        log.info(JacksonUtil.bean2Json(overstockEventService.queryingForNewOrderLineCredit()));
    }

    @Test
    public void testQueryingForNewReturn() throws Exception {
        log.info(JacksonUtil.bean2Json(overstockEventService.queryingForNewReturn()));
    }

    @Test
    public void testUpdatingEventStatus() throws Exception {
        log.info(JacksonUtil.bean2Json(overstockEventService.updatingEventStatus(new OverstockEventTypeUpdateRequest(){{setEventId("asds");}})));
    }
}