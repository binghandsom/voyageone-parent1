package com.voyageone.service.dao.cms;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
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
        Map<String, Object> newMap = MySqlPageHelper.build(map).sort(sortString).toMap();

        // 执行结果
        List<CmsBtBusinessLogModel> list = cmsBtBusinessLogDao.selectList(newMap);
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
        Map<String, Object> newMap = MySqlPageHelper.build(map).limit(limit).toMap();

        List<CmsBtBusinessLogModel> list = cmsBtBusinessLogDao.selectList(newMap);
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
        int page = 3;
        // limit 条件
        int limit = 10;
        Map<String, Object> newMap = MySqlPageHelper.build(map).page(page).limit(limit).toMap();

        List<CmsBtBusinessLogModel> list = cmsBtBusinessLogDao.selectList(newMap);
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
        //String sortString = "channel_id asc, modified DESC";
        Map<String, Object> newMap = MySqlPageHelper
                .build(map)
                .limit(limit)
                .addSort("channel_id", Order.Direction.ASC)
                .addSort("modified", Order.Direction.ASC)
                .toMap();

        List<CmsBtBusinessLogModel> list = cmsBtBusinessLogDao.selectList(newMap);
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
        int page = 3;
        // limit 条件
        int limit = 10;
        // Order 条件
        String sortString = "channel_id asc, modified DESC";
        Map<String, Object> newMap = MySqlPageHelper.build(map)
                .page(page)
                .limit(limit)
                .addSort("channel_id", Order.Direction.ASC)
                .addSort("modified", Order.Direction.ASC)
                .toMap();

        List<CmsBtBusinessLogModel> list = cmsBtBusinessLogDao.selectList(newMap);
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
