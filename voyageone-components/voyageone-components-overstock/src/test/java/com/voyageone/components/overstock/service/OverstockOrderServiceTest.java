package com.voyageone.components.overstock.service;

import com.overstock.mp.mpc.externalclient.model.*;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.overstock.bean.order.OverstockOrderLineCancelRequest;
import com.voyageone.components.overstock.bean.order.OverstockOrderMultipleQueryRequest;
import com.voyageone.components.overstock.bean.order.OverstockOrderWholeCancelRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author aooer 2016/6/8.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class OverstockOrderServiceTest {

    private static final Logger log = LoggerFactory.getLogger("test");

    @Autowired
    private OverstockOrderService overstockOrderService;

    @Test
    public void testPlaceOneOrder() throws Exception {

        final OrderType order = new OrderType();
        final AddressType address = new AddressType();
        address.setName("James Tiberius Kirk");
        address.setLine1("Zero St");
        address.setLine2("362 W");
        address.setCity("Riverside");
        address.setState("IA");
        address.setPostalCode("52327");
        address.setCountry(CountryType.US);
        order.setAddress(address);
        final GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        final XMLGregorianCalendar creationDate = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(c);
        order.setCreationDate(creationDate);
        order.setCustomerFirstName("James");
        order.setCustomerLastName("Kirk");
        order.setEmail("unkmfiqufh@mpc.com");
        order.setOrderNumber("789806215788412");
        order.setShippingAmount(new CurrencyType(new BigDecimal(2.95), CurrencyTypeType.USD));
        order.setTax(new CurrencyType(new BigDecimal(0.01), CurrencyTypeType.USD));
        final OrderLineType orderLine = new OrderLineType();
        orderLine.setCommission(new CurrencyType(new BigDecimal(5.19), CurrencyTypeType.USD));
        orderLine.setPrice(new CurrencyType(new BigDecimal(58.99), CurrencyTypeType.USD));
        orderLine.setQuantity(1);
        orderLine.setShippingAmount(new CurrencyType(new BigDecimal(0.00), CurrencyTypeType.USD));
        orderLine.setShippingMethod(ShippingMethodType.TWO_DAY);
        orderLine.setTax(new CurrencyType(new BigDecimal(0.00), CurrencyTypeType.USD));
        orderLine.setOrderLineNumber("123");
        final VariationType variation = new VariationType();
        variation.setId("7ipc0x_DKABUJ1etOn0y6g");
        orderLine.setVariation(variation);
        final List<OrderLineType> orderLines = new ArrayList<>();
        orderLines.add(orderLine);
        final OrderType.Lines lines = new OrderType.Lines(orderLines);
        order.setLines(lines);

        log.info(JacksonUtil.bean2Json(overstockOrderService.placeOneOrder(order)));
    }

    @Test
    public void testCancelTheWholeOrder() throws Exception {
        OverstockOrderWholeCancelRequest request = new OverstockOrderWholeCancelRequest();
        request.setOrderId("asdferw233333333334we");
        log.info(JacksonUtil.bean2Json(overstockOrderService.cancelTheWholeOrder(request)));
    }

    @Test
    public void testCancelTheLineOrder() throws Exception {
        OverstockOrderLineCancelRequest request = new OverstockOrderLineCancelRequest();
        request.setOrderId("asdferw233333333334we");
        request.setOrderRemoveLineNumber("1sdqrewwwwwewrq");
        log.info(JacksonUtil.bean2Json(overstockOrderService.cancelTheLineOrder(request)));
    }

    @Test
    public void testQueryingForMultipleOrders() throws Exception {
        OverstockOrderMultipleQueryRequest request=new OverstockOrderMultipleQueryRequest();
        request.setOrderNumber("001232456");
        log.info(JacksonUtil.bean2Json(overstockOrderService.queryingForMultipleOrders(request)));
    }

    @Test
    public void testQueryingForOneOrder() throws Exception {
        log.info(JacksonUtil.bean2Json(overstockOrderService.queryingForOneOrder("0241s5da47f")));
    }
}