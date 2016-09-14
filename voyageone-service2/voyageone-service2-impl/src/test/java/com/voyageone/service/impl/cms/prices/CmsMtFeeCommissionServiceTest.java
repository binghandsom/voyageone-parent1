package com.voyageone.service.impl.cms.prices;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jonas on 9/14/16.
 *
 * @version 2.6.0
 * @since 2.6.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsMtFeeCommissionServiceTest {

    @Autowired
    private CmsMtFeeCommissionService feeCommissionService;

    @Test
    public void test() {
        CmsMtFeeCommissionService.CommissionQueryBuilder queryBuilder = feeCommissionService.new CommissionQueryBuilder();
        queryBuilder.withCategory("???");
        queryBuilder.withCart(23);
        queryBuilder.withChannel("010");
        queryBuilder.withPlatform(1);
        Double commission = queryBuilder.getCommission(CmsMtFeeCommissionService.COMMISSION_TYPE_PLATFORM);
        System.out.println("\n\n"+commission+"\n\n");
    }
}