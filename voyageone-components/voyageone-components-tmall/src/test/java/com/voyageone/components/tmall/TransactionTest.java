package com.voyageone.components.tmall;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.service.dao.com.ComBtTaobaoApiLogDao;
import com.voyageone.service.model.com.ComBtTaobaoApiLogModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author aooer 2016/3/28.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class TransactionTest {


    @Autowired
    private Ts ts;

    @Test
    public void test() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setOrder_channel_id("999");
        shopBean.setCart_id("999");
        shopBean.setAppSecret("Test-AppSecret");
        shopBean.setAppKey("Test-AppKey");
        shopBean.setSessionKey("Test-SessionKey");
        ComBtTaobaoApiLogModel model = new ComBtTaobaoApiLogModel("Test Api Method", shopBean);

        ts.insertUserOUt(model);

    }



}

@Service
class Ts{

    @Autowired
    private ComBtTaobaoApiLogDao apiLogDao;

    @Autowired
    private Ts1 ts1;

    public void insertUserOUt(ComBtTaobaoApiLogModel model) throws Exception {
        insertUser(model);
    }

    @VOTransactional
    public void insertUser(ComBtTaobaoApiLogModel model) throws Exception {
        model.setApiMethodName(model.getApiMethodName()+System.currentTimeMillis());
        Thread.sleep(10);
        int count = apiLogDao.insert(model);
        assert count > 0;
        model.setApiMethodName(model.getApiMethodName()+System.currentTimeMillis());
        ts1.insertUser1(model);
    }

    public void insertUser1(ComBtTaobaoApiLogModel model) throws Exception {
        if (true) {
            throw new Exception("exxxxx");
        }
        int count = apiLogDao.insert(model);
        assert count > 0;
    }


}

@Service
class Ts1{

    @Autowired
    private ComBtTaobaoApiLogDao apiLogDao;


    public void insertUser1(ComBtTaobaoApiLogModel model) throws Exception {
        if (true) {
            throw new Exception("exxxxx");
        }
        int count = apiLogDao.insert(model);
        assert count > 0;
    }

}
