package com.voyageone.service.dao.com;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.model.cms.CmsBtBusinessLogModel;
import com.voyageone.service.model.com.ComMtValueChannelModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class ComMtValueChannelDaoTest {

    @Autowired
    private ComMtValueChannelDao comMtValueChannelDao;


    /**
     * SelectList
     */
    @Test
    public void testSelectList() throws Exception {

        // 检索条件
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", "001");
        map.put("cartId", 24);

        List<ComMtValueChannelModel> list = comMtValueChannelDao.selectList(map);
        for (ComMtValueChannelModel model : list) {
            System.out.println(JacksonUtil.bean2Json(model));
        }
    }

}
