package com.voyageone.base.dao.mysql.paginator.domain;


import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

import java.util.Map;

/**
 * 分页查询对象
 *
 * @author badqiu
 * @author hunhun
 * @author miemiedev
 */
public class MySqlPage extends PageBounds {

    public static final String key = "#MySqlPage#";

    public MySqlPage() {
        super();
    }

    public MySqlPage(int limit) {
        super(limit);
    }

    public MySqlPage(int page, int limit) {
        super(page, limit, false);
    }

    public MySqlPage(String sortString) {
        super(MySqlOrder.formString(sortString));
    }

    public MySqlPage(int page, int limit, String sortString) {
        super(page, limit, MySqlOrder.formString(sortString), false);
    }

    public static void addMySqlPage(Map<String, Object> sqlParam, int limit) {
        MySqlPage mySqlPage = new MySqlPage(limit);
        sqlParam.put(key, mySqlPage);
    }

    public static void addMySqlPage(Map<String, Object> sqlParam, int page, int limit) {
        MySqlPage mySqlPage = new MySqlPage(page, limit);
        sqlParam.put(key, mySqlPage);
    }

    public static void addMySqlPage(Map<String, Object> sqlParam, String sortString) {
        MySqlPage mySqlPage = new MySqlPage(sortString);
        sqlParam.put(key, mySqlPage);
    }

    public static void addMySqlPage(Map<String, Object> sqlParam, int limit, String sortString) {
        MySqlPage mySqlPage = new MySqlPage(1, limit, sortString);
        sqlParam.put(key, mySqlPage);
    }

    public static void addMySqlPage(Map<String, Object> sqlParam, int page, int limit, String sortString) {
        MySqlPage mySqlPage = new MySqlPage(page, limit, sortString);
        sqlParam.put(key, mySqlPage);
    }
}