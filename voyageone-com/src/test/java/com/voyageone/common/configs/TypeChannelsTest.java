package com.voyageone.common.configs;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by jonas on 9/13/16.
 *
 * @author jonas
 * @version 2.6.0
 * @since 2.6.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class TypeChannelsTest {

    /**
     * @since 2.6.0
     */
    @Test
    public void testGetTypeListSkuCarts() {

        List<TypeChannelBean> typeChannelBeanList = TypeChannels.getTypeListSkuCarts("010", Constants.comMtTypeChannel.SKU_CARTS_53_D, Constants.LANGUAGE.EN);

        System.out.println("\n\n");

        assert typeChannelBeanList != null;
        for (TypeChannelBean typeChannelBean: typeChannelBeanList)
            System.out.println(JacksonUtil.bean2Json(typeChannelBean));

        System.out.println("\n\n");

    }
}