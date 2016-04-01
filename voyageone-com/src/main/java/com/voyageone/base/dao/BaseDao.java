package com.voyageone.base.dao;

import com.voyageone.common.Constants;
import com.voyageone.common.logger.VOAbsLoggable;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseDao extends VOAbsLoggable {
    private String namespace;

    @Autowired
    private SqlSessionTemplate selectTemplate;

    @Autowired
    protected SqlSessionTemplate updateTemplate;

    /**
     * 在每个实体类初始化的时候，完成 namespace 的组装
     */
    @PostConstruct
    private void init() {
        String n = namespace();

        this.namespace = StringUtils.isEmpty(n) ? Constants.EmptyString : (n + ".");
    }

    /**
     * 获取 mapper 的 namespace，只在初始化时调用
     */
    protected String namespace() {
        return Constants.EmptyString;
    }

    /**
     * 组装完整的 sql id
     */
    private String fullStatement(String statement) {

        return StringUtils.isEmpty(this.namespace) ? statement : (this.namespace + statement);
    }

    protected <E> List<E> selectList(String statement) {
        return selectTemplate.selectList(fullStatement(statement));
    }

    protected <E> List<E> selectList(String statement, Object parameter) {
        return selectTemplate.selectList(fullStatement(statement), parameter);
    }

    protected Map<Object, Object> selectMap(String statement, String mapKey) {
        return selectTemplate.selectMap(fullStatement(statement), mapKey);
    }

    protected Map<Object, Object> selectMap(String statement, Object parameter, String mapKey) {
        return selectTemplate.selectMap(fullStatement(statement), parameter, mapKey);
    }

    protected <T> T selectOne(String statement) {
        return selectTemplate.selectOne(fullStatement(statement));
    }

    protected <T> T selectOne(String statement, Object parameter) {
        return selectTemplate.selectOne(fullStatement(statement), parameter);
    }

    protected int insert(String statement, Object parameter) {
        return updateTemplate.insert(fullStatement(statement), parameter);
    }

    protected int update(String statement, Object parameter) {
        return updateTemplate.update(fullStatement(statement), parameter);
    }

    protected int delete(String statement) {
        return updateTemplate.delete(fullStatement(statement));
    }

    protected int delete(String statement, Object parameter) {
        return updateTemplate.delete(fullStatement(statement), parameter);
    }

    /**
     * 组装参数数组
     *
     * @param parameters k, v 顺序排列的数组，k 为字符串，即参数名，v 为值
     * @return Map
     */
    protected Map<String, Object> parameters(Object... parameters) {

        if (parameters.length % 2 != 0) throw new IllegalArgumentException("参数数组长度错误！键值匹配的参数数组应该是偶数长度！");

        Map<String, Object> map = new HashMap<>();

        for (int k = 0, v = 1; k < parameters.length; k += 2, v += 2)

            map.put(String.valueOf(parameters[k]), parameters[v]);

        return map;
    }
}
