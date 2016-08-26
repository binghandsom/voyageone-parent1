package com.voyageone.base.dao.mysql;

import com.voyageone.service.dao.cms.CmsBtBusinessLogDao;
import com.voyageone.service.model.cms.CmsBtBusinessLogModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class MybatisSqlHelperTest {

    @Autowired
    private CmsBtBusinessLogDao cmsBtBusinessLogDao;

    @Test
    public void testSelect() throws Exception {
        // get SQL
        String sql = MybatisSqlHelper.getMapperSql(CmsBtBusinessLogDao.class, "select", 6807);
        System.out.println("SQL:" + sql);
    }

    @Test
    public void testSelectList() throws Exception {
        // 检索条件
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", "010");
        map.put("cartId", 26);
        map.put("groupId", "217697");
        map.put("modified", Timestamp.valueOf("2016-08-23 11:21:27"));

        // get SQL
        String sql = MybatisSqlHelper.getMapperSql(CmsBtBusinessLogDao.class, "selectList", map);
        System.out.println("SQL:" + sql);
    }

    @Test
    public void testUpdate() throws Exception {
        // 执行结果
        CmsBtBusinessLogModel model = cmsBtBusinessLogDao.select(6807);
        String sql = MybatisSqlHelper.getMapperSql(CmsBtBusinessLogDao.class, "update", model);
        System.out.println("SQL:" + sql);
    }

    @Test
    public void testDelete() throws Exception {
        // 执行结果
        CmsBtBusinessLogModel model = cmsBtBusinessLogDao.select(6807);
        String sql = MybatisSqlHelper.getMapperSql(CmsBtBusinessLogDao.class, "delete", model);
        System.out.println("SQL:" + sql);
    }

    @Test
    public void testSelectOne() throws Exception {
        // 检索条件
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", "010");
        map.put("cartId", 26);

        // get SQL
        String sql = MybatisSqlHelper.getMapperSql(CmsBtBusinessLogDao.class, "selectOne", map);
        System.out.println("SQL:" + sql);
    }

    @Test
    public void testSelectCount() throws Exception {
        // 检索条件
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", "010");
        map.put("cartId", 26);

        // get SQL
        String sql = MybatisSqlHelper.getMapperSql(CmsBtBusinessLogDao.class, "selectCount", map);
        System.out.println("SQL:" + sql);
    }

    @Test
    public void testInsert() throws Exception {
        // 执行结果
        CmsBtBusinessLogModel model = cmsBtBusinessLogDao.select(6807);

        // get SQL
        String sql = MybatisSqlHelper.getMapperSql(CmsBtBusinessLogDao.class, "insert", model);
        System.out.println("SQL:" + sql);
    }


}
