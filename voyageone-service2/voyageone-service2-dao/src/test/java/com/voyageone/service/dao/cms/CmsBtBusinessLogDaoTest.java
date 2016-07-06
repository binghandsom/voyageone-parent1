package com.voyageone.service.dao.cms;

import com.voyageone.base.dao.mysql.paginator.domain.MySqlPage;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.model.cms.CmsBtBusinessLogModel;
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
public class CmsBtBusinessLogDaoTest {

    @Autowired
    private CmsBtBusinessLogDao cmsBtBusinessLogDao;

    /**
     * SelectList With Orderby like (order by channel_id asc, modified DESC)
     */
    @Test
    public void testSelectListWithOrder() throws Exception {

        // 检索条件
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", "001");
        map.put("cartId", 24);

        // Order 条件
        String sortString = "channel_id asc, modified DESC";
        MySqlPage.addMySqlPage(map, sortString);

        // 执行结果
        List<CmsBtBusinessLogModel> list = cmsBtBusinessLogDao.selectList(map);
        for (CmsBtBusinessLogModel model : list) {
            System.out.println(JacksonUtil.bean2Json(model));
        }
    }

    /**
     * SelectList With Limit like (limit 10 )
     */
    @Test
    public void testSelectListWithLimit() throws Exception {

        // 检索条件
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", "001");
        map.put("cartId", 24);

        // limit 条件
        int limit = 10;
        MySqlPage.addMySqlPage(map, limit);

        List<CmsBtBusinessLogModel> list = cmsBtBusinessLogDao.selectList(map);
        for (CmsBtBusinessLogModel model : list) {
            System.out.println(JacksonUtil.bean2Json(model));
        }
    }

    /**
     * SelectList With Page & Limit like (limit 10, 10 )
     */
    @Test
    public void testSelectListWithPageLimit() throws Exception {

        // 检索条件
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", "001");
        map.put("cartId", 24);

        // 第2页
        int page = 2;
        // limit 条件
        int limit = 10;
        MySqlPage.addMySqlPage(map, page, limit);

        List<CmsBtBusinessLogModel> list = cmsBtBusinessLogDao.selectList(map);
        for (CmsBtBusinessLogModel model : list) {
            System.out.println(JacksonUtil.bean2Json(model));
        }
    }

    /**
     * SelectList With Order Limit like( order by xxx limit 99)
     */
    @Test
    public void testSelectListWithOrderLimit() throws Exception {

        // 检索条件
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", "001");
        map.put("cartId", 24);

        // limit 条件
        int limit = 10;
        // Order 条件
        String sortString = "channel_id asc, modified DESC";
        MySqlPage.addMySqlPage(map, limit, sortString);

        List<CmsBtBusinessLogModel> list = cmsBtBusinessLogDao.selectList(map);
        for (CmsBtBusinessLogModel model : list) {
            System.out.println(JacksonUtil.bean2Json(model));
        }
    }

    /**
     * SelectList With Order Page Limit like( order by xxx asc, yyy desc limit 99, 99)
     */
    @Test
    public void testSelectListWithOrderPageLimit() throws Exception {

        // 检索条件
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", "001");
        map.put("cartId", 24);

        // 第2页
        int page = 2;
        // limit 条件
        int limit = 10;
        // Order 条件
        String sortString = "channel_id asc, modified DESC";
        MySqlPage.addMySqlPage(map, page, limit, sortString);

        List<CmsBtBusinessLogModel> list = cmsBtBusinessLogDao.selectList(map);
        for (CmsBtBusinessLogModel model : list) {
            System.out.println(JacksonUtil.bean2Json(model));
        }
    }

    /**
     * SelectList With Order Page Limit like( order by xxx asc, yyy desc limit 99, 99)
     */
    @Test
    public void testSelectCount() throws Exception {

        // 检索条件
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", "001");
        map.put("cartId", 24);

        System.out.println("count:" + cmsBtBusinessLogDao.selectCount(map));
    }

    /**
     * Select one
     */
    @Test
    public void testSelectOne() throws Exception {

        // 检索条件
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", "001");
        map.put("cartId", 24);

        System.out.println("one:" + JacksonUtil.bean2Json(cmsBtBusinessLogDao.selectOne(map)));
    }

    /**
     * SelectList
     */
    @Test
    public void testSelectList() throws Exception {

        // 检索条件
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", "001");
        map.put("cartId", 24);

        List<CmsBtBusinessLogModel> list = cmsBtBusinessLogDao.selectList(map);
        for (CmsBtBusinessLogModel model : list) {
            System.out.println(JacksonUtil.bean2Json(model));
        }
    }

}
