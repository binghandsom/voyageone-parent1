package com.voyageone.service.impl.cms;

import com.voyageone.service.impl.cms.prices.CmsMtFeeTaxService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by 123 on 2016/9/27.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsMtFeeTaxServiceTest {

    @Autowired
    private CmsMtFeeTaxService cmsMtFeeTaxService;

    @Test
    public void testGetDefaultRate(){
        Double defaultRate = cmsMtFeeTaxService.getDefaultTaxRate();
        System.out.print("税率===========>"+defaultRate);
    }


}
