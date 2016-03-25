package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.beans.CodeBean;
import com.voyageone.common.configs.dao.CodeDao;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.redis.CacheTemplateFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 访问 tm_code 表配置
 * Created by Jonas on 4/16/2015.
 *
 * @author Jonas
 * @version 0.0.1
 */
public class Codes {

    private static final Log logger = LogFactory.getLog(Codes.class);

    /* redis key */
    private static final String KEY = CacheKeyEnums.ConfigData_Codes.toString();

    private static HashOperations<String, String, CodeBean> hashOperations = CacheTemplateFactory.getHashOperation();

    static {
        if (!CacheTemplateFactory.getCacheTemplate().hasKey(KEY)) {
            CodeDao codeDao = ConfigDaoFactory.getCodeDao();
            Map<String, CodeBean> codeBeansMap = new HashMap<>();
            codeDao.getAll().forEach(bean -> {
                        codeBeansMap.put(
                                buildKey(bean.getId(), bean.getCode()),
                                bean
                        );
                    }
            );
            CacheHelper.reFreshSSB(KEY, codeBeansMap);
            logger.info("codes 读取数量: " + hashOperations.size(KEY));
        }
    }

    /**
     * build redis hash Key
     *
     * @param id id
     * @return key
     */
    private static String buildKey(String id, String code) {
        return id + CacheHelper.SKIP + code;
    }

    /**
     * 获取 id 的第一个 CodeBean 的 Name 字段
     *
     * @param id CodeBean 的 id 字段
     * @return CodeBean
     */
    public static String getCodeName(String id) {
        CodeBean bean = getCodeBean(id);
        return bean != null ? bean.getName() : null;
    }

    /**
     * 取得CodeName
     *
     * @param id   code的id
     * @param code code的值
     * @return codeName
     */
    public static String getCodeName(String id, String code) {
        CodeBean bean = hashOperations.get(KEY, buildKey(id, code));
        return (bean == null) ? null : bean.getName();
    }

    /**
     * 取得 Code 记录的 Name 字段
     */
    public static String getCodeName(Enum<?> e) {
        String key = e.getClass().getSimpleName();
        String code = e.toString();

        return getCodeName(key, code);
    }

    /**
     * 取得CodeName1
     *
     * @param id   code的id
     * @param code code的值
     * @return codeName1
     */
    public static String getCodeName1(String id, String code) {
        CodeBean bean = hashOperations.get(KEY, buildKey(id, code));
        return (bean == null) ? null : bean.getName1();
    }

    /**
     * 获取 id 的第一个 CodeBean
     *
     * @param id Code 的 id 字段
     * @return CodeBean
     */
    public static CodeBean getCodeBean(String id) {
        List<CodeBean> codeList = getCodeList(id);
        if (CollectionUtils.isEmpty(codeList)) return null;
        return codeList.get(0);
    }

    public static Map<String, String> getCodeMap(String id) {
        Map<String, String> ret = new LinkedHashMap<>();

        List<CodeBean> codeList = getCodeList(id);

        for (CodeBean bean : codeList) {
            ret.put(String.valueOf(bean.getCode()), bean.getName());
        }

        return ret;
    }

    /**
     * 获取 id 的MapList
     *
     * @param id         Code 的 id 字段
     * @param defaultAll 是否有All选项
     * @return List<Map<String, String>>
     */
    public static List<Map<String, String>> getCodeMapList(String id, boolean defaultAll) {

        List<Map<String, String>> ret = new ArrayList<Map<String, String>>();

        List<CodeBean> typeList = getCodeList(id);

        //加入All选项
        if (defaultAll) {
            Map<String, String> mapAll = new HashMap<String, String>();
            mapAll.put("id", null);
            mapAll.put("name", "ALL");
            ret.add(mapAll);
        }

        for (CodeBean bean : typeList) {
            Map<String, String> tempMap = new HashMap<String, String>();
            tempMap.put("id", String.valueOf(bean.getCode()));
            tempMap.put("name", bean.getName());
            ret.add(tempMap);
        }
        return ret;
    }

    public static List<CodeBean> getCodeList(String id) {
        Set<String> keySet = hashOperations.keys(KEY);
        if (CollectionUtils.isEmpty(keySet)) return null;
        List<String> keyList = new ArrayList<>();
        keySet.forEach(k -> {
            if (k.startsWith(buildKey(id, ""))) keyList.add(k);
        });
        Collections.sort(keyList);
        return hashOperations.multiGet(KEY, keyList);
    }

    /**
     * 取得 Code 对象的 Code 字段
     *
     * @param id   code的id
     * @param name code的Name
     * @return code
     */
    public static String getCode(String id, String name) {
        List<CodeBean> beans = getCodeList(id);
        for (CodeBean bean : beans)
            if (bean.getName().equals(name)) return bean.getCode();
        return null;
    }
}
