package com.voyageone.common.configs;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.beans.MasterInfoBean;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.TypeDao;
import com.voyageone.common.redis.CacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Jack
 * @version 0.0.1
 * 访问 com_mt_value 表配置
 * Created by Jack on 4/21/2015.
 */
public class Types {

    private static final Class selfClass = Types.class;

    private static final Logger logger = LoggerFactory.getLogger(selfClass);

    /* redis key */
    private static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_Type.toString();

    public static void reload() {
        Map<String, TypeBean> typeBeansMap = new HashMap<>();
        TypeDao typeDao = ConfigDaoFactory.getTypeDao();
        typeDao.getAll().forEach(bean -> {
            typeBeansMap.put(buildKey(bean.getType_id(), bean.getLang_id(), bean.getValue()), bean);
        });
        CacheHelper.reFreshSSB(KEY, typeBeansMap);
        logger.info("Type 读取数量: " + CacheHelper.getSize(KEY));
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(int type_id, String lang_id, String value) {
        return type_id + CacheKeyEnums.SKIP + lang_id + CacheKeyEnums.SKIP + value;
    }

    /**
     * 取得TypeName
     *
     * @param typeId type的type_id
     * @param value  type的value
     * @param langId TypeBean 的 lang_Id 字段
     * @return name
     */
    public static String getTypeName(int typeId, String langId, String value) {
        TypeBean typeBean = CacheHelper.getBean(KEY, buildKey(typeId, langId, value), selfClass);
        return typeBean == null ? null : typeBean.getName();
    }

    /**
     * 获取 type_id 的第一个 TypeBean
     *
     * @param typeId Type 的 type_id 字段
     * @param langId TypeBean 的 lang_Id 字段
     * @return TypeBean
     */
    public static TypeBean getTypeBean(int typeId, String langId) {
        Set<String> keySet = CacheHelper.getKeySet(KEY, selfClass);
        if (CollectionUtils.isEmpty(keySet)) return null;
        List<String> keyList = new ArrayList<>();
        keySet.forEach(k -> {
            if (k.startsWith(buildKey(typeId, langId, ""))) keyList.add(k);
        });
        Collections.sort(keyList);
        List<TypeBean> beans = CacheHelper.getBeans(KEY, keyList, selfClass);
        return CollectionUtils.isEmpty(beans) ? null : beans.get(0);
    }

    /**
     * 获取 type_code 的第一个 TypeBean
     *
     * @param typeName Type 的 type_code 字段
     * @param langId   TypeBean 的 lang_Id 字段
     * @return TypeBean
     */
    public static TypeBean getTypeBean(String typeName, String langId) {
        List<TypeBean> beans = CacheHelper.getAllBeans(KEY, selfClass);
        for (TypeBean typeBean : beans) {
            if (typeBean.getType_code().equals(typeName) && typeBean.getLang_id().equals(langId)) return typeBean;
        }
        return null;
    }

    public static Map<String, String> getTypeMap(int typeId, String langId) {
        Set<String> keySet = CacheHelper.getKeySet(KEY, selfClass);
        if (CollectionUtils.isEmpty(keySet)) return null;
        List<String> keyList = new ArrayList<>();
        keySet.forEach(k -> {
            if (k.startsWith(buildKey(typeId, langId, ""))) keyList.add(k);
        });
        Collections.sort(keyList);
        List<TypeBean> beans = CacheHelper.getBeans(KEY, keyList, selfClass);
        if (CollectionUtils.isEmpty(beans)) return null;
        Map<String, String> result = new LinkedHashMap<>();
        beans.forEach(bean -> result.put(String.valueOf(bean.getValue()), bean.getName()));
        return result;
    }

    /**
     * 获取 type_id 的MapList
     *
     * @param typeId     Type 的 type_id 字段
     * @param defaultAll 是否有All选项
     * @param langId     TypeBean 的 lang_Id 字段
     * @return List
     */
    public static List<Map<String, String>> getTypeMapList(int typeId, boolean defaultAll, String langId) {
        List<Map<String, String>> ret = new ArrayList<>();
        //加入All选项
        if (defaultAll) {
            Map<String, String> mapAll = new HashMap<>();
            mapAll.put("id", null);
            mapAll.put("name", "ALL");
            ret.add(mapAll);
        }
        Set<String> keySet = CacheHelper.getKeySet(KEY, selfClass);
        if (CollectionUtils.isEmpty(keySet)) return ret;
        List<String> keyList = new ArrayList<>();
        keySet.forEach(k -> {
            if (k.startsWith(buildKey(typeId, langId, ""))) keyList.add(k);
        });
        Collections.sort(keyList);
        List<TypeBean> typeList = CacheHelper.getBeans(KEY, keyList, selfClass);
        typeList.forEach(bean -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("id", String.valueOf(bean.getValue()));
                    map.put("name", bean.getName());
                    ret.add(map);
                }
        );
        return ret;
    }

    /**
     * 获取 type_id 的BeanList
     *
     * @param typeId     Type 的 type_id 字段
     * @param defaultAll 是否有All选项
     * @param langId     TypeBean 的 lang_Id 字段
     * @return List<MasterInfoBean>
     */
    public static List<MasterInfoBean> getMasterInfoFromId(int typeId, boolean defaultAll, String langId) {
        List<MasterInfoBean> ret = new ArrayList<>();
        //加入All选项
        if (defaultAll) {
            MasterInfoBean masterInfoAll = new MasterInfoBean();
            masterInfoAll.setType(typeId);
            masterInfoAll.setName("All");
            ret.add(masterInfoAll);
        }
        Set<String> keySet = CacheHelper.getKeySet(KEY, selfClass);
        if (CollectionUtils.isEmpty(keySet)) return ret;
        List<String> keyList = new ArrayList<>();
        keySet.forEach(k -> {
            if (k.startsWith(buildKey(typeId, langId, ""))) keyList.add(k);
        });
        Collections.sort(keyList);
        List<TypeBean> typeList = CacheHelper.getBeans(KEY, keyList, selfClass);
        typeList.forEach(bean -> {
                    MasterInfoBean masterInfoBean = new MasterInfoBean();
                    masterInfoBean.setType(typeId);
                    masterInfoBean.setId(String.valueOf(bean.getValue()));
                    masterInfoBean.setName(String.valueOf(bean.getName()));
                    ret.add(masterInfoBean);
                }
        );

        return ret;
    }

    public static List<TypeBean> getTypeList(int typeId, String langId) {
        Set<String> keySet = CacheHelper.getKeySet(KEY, selfClass);
        if (CollectionUtils.isEmpty(keySet)) return null;
        List<String> keyList = new ArrayList<>();
        keySet.forEach(k -> {
            if (k.startsWith(buildKey(typeId, langId, ""))) keyList.add(k);
        });
        Collections.sort(keyList);
        return CollectionUtils.isEmpty(keyList) ? null : CacheHelper.getBeans(KEY, keyList, selfClass);
    }

    public static List<TypeBean> getTypeList(String typeName, String langId) {
        List<TypeBean> allBeans = CacheHelper.getAllBeans(KEY, selfClass);
        List<TypeBean> beans = allBeans
                .stream()
                .filter(b -> b.getType_code().equals(typeName) && b.getLang_id().equals(langId))
                .collect(Collectors.toList());
        return CollectionUtils.isEmpty(beans) ? null : beans;
    }

    /**
     * 取得 Type 对象的 value 字段
     *
     * @param typeId type的typeId
     * @param name   type的Name
     * @param langId TypeBean 的 lang_Id 字段
     * @return value
     */
    public static String getValue(int typeId, String name, String langId) {
        List<TypeBean> typeList = getTypeList(typeId, langId);
        if (CollectionUtils.isEmpty(typeList)) return null;
        TypeBean bean = typeList.stream().filter(b -> b.getName().equals(name)).findFirst().get();
        return bean == null ? "" : bean.getValue();
    }

    /**
     * 获取 type_id 的第一个 TypeBean 的 Name 字段
     *
     * @param typeId TypeBean 的 type_id 字段
     * @return TypeBean
     */
    public static String getTypeName(int typeId) {
        TypeBean bean = getTypeBean(typeId);
        return bean != null ? bean.getName() : null;
    }

    /**
     * 取得TypeName
     *
     * @param typeId type的type_id
     * @param value  type的value
     * @return name
     */
    public static String getTypeName(int typeId, String value) {
        List<TypeBean> typeList = getTypeList(typeId);
        if (CollectionUtils.isEmpty(typeList)) return null;
        for (TypeBean bean : typeList)
            if (value.equals(bean.getValue()))
                return bean.getName();
        return null;
    }

    /**
     * 获取 type_id 的第一个 TypeBean
     *
     * @param typeId Type 的 type_id 字段
     * @return TypeBean
     */
    public static TypeBean getTypeBean(int typeId) {
        List<TypeBean> typeList = getTypeList(typeId);
        if (CollectionUtils.isEmpty(typeList)) return null;
        return typeList.get(0);
    }

    public static Map<String, String> getTypeMap(int typeId) {
        Map<String, String> ret = new LinkedHashMap<>();
        List<TypeBean> typeList = getTypeList(typeId);
        if (CollectionUtils.isEmpty(typeList)) return ret;
        typeList.forEach(bean -> ret.put(String.valueOf(bean.getValue()), bean.getName()));
        return ret;
    }

    /**
     * 获取 type_id 的MapList
     *
     * @param typeId     Type 的 type_id 字段
     * @param defaultAll 是否有All选项
     * @return List
     */
    public static List<Map<String, String>> getTypeMapList(int typeId, boolean defaultAll) {
        List<Map<String, String>> ret = new ArrayList<>();
        //加入All选项
        if (defaultAll) {
            Map<String, String> mapAll = new HashMap<>();
            mapAll.put("id", null);
            mapAll.put("name", "ALL");
            ret.add(mapAll);
        }
        List<TypeBean> typeList = getTypeList(typeId);
        if (CollectionUtils.isEmpty(typeList)) return ret;
        typeList.forEach(bean -> {
            Map<String, String> tempMap = new HashMap<>();
            tempMap.put("id", String.valueOf(bean.getValue()));
            tempMap.put("name", bean.getName());
            ret.add(tempMap);
        });
        return ret;
    }

    /**
     * 获取 type_id 的BeanList
     *
     * @param typeId     Type 的 type_id 字段
     * @param defaultAll 是否有All选项
     * @return List<MasterInfoBean>
     */
    public static List<MasterInfoBean> getMasterInfoFromId(int typeId, boolean defaultAll) {
        List<MasterInfoBean> ret = new ArrayList<>();
        //加入All选项
        if (defaultAll) {
            MasterInfoBean masterInfoAll = new MasterInfoBean();
            masterInfoAll.setType(typeId);
            masterInfoAll.setName("All");
            ret.add(masterInfoAll);
        }
        List<TypeBean> typeList = getTypeList(typeId);
        if (CollectionUtils.isEmpty(typeList)) return ret;
        typeList.forEach(bean -> {
            MasterInfoBean masterInfoBean = new MasterInfoBean();
            masterInfoBean.setType(typeId);
            masterInfoBean.setId(String.valueOf(bean.getValue()));
            masterInfoBean.setName(String.valueOf(bean.getName()));
            ret.add(masterInfoBean);
        });
        return ret;
    }

    public static List<TypeBean> getTypeList(int typeId) {
        List<TypeBean> typeList = getTypeList(typeId, Constants.LANGUAGE.EN);
        return CollectionUtils.isEmpty(typeList) ? null : typeList;
    }

    public static List<TypeBean> getTypeList(String typeName) {
        List<TypeBean> beans = getTypeList(typeName, Constants.LANGUAGE.EN);
        return CollectionUtils.isEmpty(beans) ? null : beans;
    }

    /**
     * 取得 Type 对象的 value 字段
     *
     * @param typeId type的typeId
     * @param name   type的Name
     * @return value
     */
    public static String getValue(int typeId, String name) {
        List<TypeBean> typeList = getTypeListById(typeId);
        TypeBean beant = typeList
                .stream()
                .findFirst()
                .filter(bean -> name.equals(bean.getName()) && Constants.LANGUAGE.EN.equals(bean.getLang_id()))
                .get();
        return beant == null ? "" : beant.getValue();
    }

    public static List<TypeBean> getTypeListById(int typeId) {
        List<TypeBean> beans = new ArrayList<>();
        Set<String> keySet = CacheHelper.getKeySet(KEY, selfClass);
        if (CollectionUtils.isEmpty(keySet)) return beans;
        List<String> keyList = new ArrayList<>();
        keySet.forEach(k -> {
            if (k.startsWith(typeId + CacheKeyEnums.SKIP)) keyList.add(k);
        });
        Collections.sort(keyList);
        beans = CacheHelper.getBeans(KEY, keyList, selfClass);
        return beans == null ? new ArrayList<>() : beans;
    }
}
