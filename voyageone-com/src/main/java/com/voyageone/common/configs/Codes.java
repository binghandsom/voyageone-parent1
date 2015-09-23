package com.voyageone.common.configs;

import com.voyageone.common.configs.beans.CodeBean;
import com.voyageone.common.configs.beans.MasterInfoBean;
import com.voyageone.common.configs.dao.CodeDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * 访问 tm_code 表配置
 * Created by Jonas on 4/16/2015.
 *
 * @author Jonas
 * @version 0.0.1
 */
public class Codes {
    private static HashMap<String, List<CodeBean>> codeMap;

    private static Log logger = LogFactory.getLog(Codes.class);

    public static void init(CodeDao codeDao) {
        if (codeMap != null) return;

        codeMap = new HashMap<>();

        logger.debug("Code 初始化");

        List<CodeBean> beans = codeDao.getAll();

        logger.info("Code 读取数量：" + beans.size());

        List<CodeBean> childList;

        for (CodeBean bean : beans) {
            childList = codeMap.get(bean.getId());

            if (childList == null) codeMap.put(bean.getId(), childList = new ArrayList<>());

            childList.add(bean);
        }

        logger.debug("Code 按 ID 已存入：" + codeMap.size());
    }

    /**
     * 获取 id 的第一个 CodeBean 的 Name 字段
     *
     * @param id CodeBean 的 id 字段
     * @return CodeBean
     */
    public static String getCodeName(String id) {
        CodeBean bean = getCodeBean(id);

        return bean != null
                ? bean.getName()
                : null;
    }

    /**
     * 取得CodeName
     *
     * @param id   code的id
     * @param code code的值
     * @return codeName
     */
    public static String getCodeName(String id, String code) {
        List<CodeBean> codeList = codeMap.get(id);

        for (CodeBean bean : codeList) {
            if (code.equals(bean.getCode())) {
                return bean.getName();
            }
        }

        return null;
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
        List<CodeBean> codeList = codeMap.get(id);

        for (CodeBean bean : codeList) {
            if (code.equals(bean.getCode())) {
                return bean.getName1();
            }
        }

        return null;
    }

    /**
     * 获取 id 的第一个 CodeBean
     *
     * @param id Code 的 id 字段
     * @return CodeBean
     */
    public static CodeBean getCodeBean(String id) {
        List<CodeBean> codeList = codeMap.get(id);

        return codeList != null && codeList.size() > 0
                ? codeList.get(0)
                : null;
    }

    public static Map<String, String> getCodeMap(String id) {
        Map<String, String> ret = new LinkedHashMap<>();

        List<CodeBean> codeList = codeMap.get(id);

        for (CodeBean bean : codeList) {
            ret.put(String.valueOf(bean.getCode()), bean.getName());
        }

        return ret;
    }

    /**
     * 获取 id 的MapList
     *
     * @param id Code 的 id 字段
     * @param defaultAll 是否有All选项
     * @return List<Map<String, String>>
     */
    public static  List<Map<String,String>> getCodeMapList(String id, boolean defaultAll) {

        List<Map<String,String>> ret = new ArrayList<Map<String,String>>();

        List<CodeBean> typeList = codeMap.get(id);

        //加入All选项
        if (defaultAll) {
            Map<String, String> mapAll = new HashMap<String, String>();
            mapAll.put("id", null);
            mapAll.put("name", "ALL");
            ret.add(mapAll);
        }

        for (CodeBean bean : typeList) {
            Map<String,String> tempMap = new HashMap<String,String>();
            tempMap.put("id", String.valueOf(bean.getCode()));
            tempMap.put("name", bean.getName());
            ret.add(tempMap);
        }
        return ret;
    }

    public static List<CodeBean> getCodeList(String id) {
        return codeMap.get(id);
    }

    /**
     * 取得 Code 对象的 Code 字段
     *
     * @param id   code的id
     * @param name code的Name
     * @return code
     */
    public static String getCode(String id, String name) {
        List<CodeBean> codeList = codeMap.get(id);

        for (CodeBean bean : codeList) {
            if (name.equals(bean.getName())) {
                return bean.getCode();
            }
        }

        return null;
    }
}
