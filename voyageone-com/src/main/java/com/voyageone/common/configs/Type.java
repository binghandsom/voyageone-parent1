package com.voyageone.common.configs;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.MasterInfoBean;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.dao.TypeDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * 访问 com_mt_value 表配置
 * Created by Jack on 4/21/2015.
 *
 * @author Jack
 * @version 0.0.1
 */
public class Type {
    private static HashMap<Integer, List<TypeBean>> typeMap;

    private static Log logger = LogFactory.getLog(Type.class);

    public static void init(TypeDao typeDao) {
        if (typeMap != null) return;

        typeMap = new HashMap<>();

        logger.debug("Type 初始化");

        List<TypeBean> beans = typeDao.getAll();

        logger.info("Type 读取数量：" + beans.size());

        List<TypeBean> childList;

        for (TypeBean bean : beans) {
            childList = typeMap.get(bean.getType_id());

            if (childList == null) typeMap.put(bean.getType_id(), childList = new ArrayList<>());

            childList.add(bean);
        }

        logger.debug("Type 按 ID 已存入：" + typeMap.size());
    }

//    /**
//     * 获取 type_id 的第一个 TypeBean 的 Name 字段
//     *
//     * @param typeId TypeBean 的 type_id 字段
//     * @param langId TypeBean 的 lang_Id 字段
//     * @return TypeBean
//     */
//    public static String getTypeName(int typeId, String langId) {
//        TypeBean bean = getTypeBean(typeId, langId);
//
//        return bean != null
//                ? bean.getName()
//                : null;
//    }

    /**
     * 取得TypeName
     *
     * @param typeId   type的type_id
     * @param value type的value
     * @param langId TypeBean 的 lang_Id 字段
     * @return name
     */
    public static String getTypeName(int typeId, String value, String langId) {
        List<TypeBean> typeList = typeMap.get(typeId);

        for (TypeBean bean : typeList) {
            if (value.equals(bean.getValue()) && langId.equals(bean.getLang_id())) {
                return bean.getName();
            }
        }

        return null;
    }

    /**
     * 获取 type_id 的第一个 TypeBean
     *
     * @param typeId Type 的 type_id 字段
     * @param langId TypeBean 的 lang_Id 字段
     * @return TypeBean
     */
    public static TypeBean getTypeBean(int typeId, String langId) {
        List<TypeBean> typeList = typeMap.get(typeId);

        if (typeList != null) {
            for (TypeBean bean : typeList) {
                if (bean.getLang_id().equals(langId)) {
                    return bean;
                }
            }
        }
        return null;
    }

    public static Map<String, String> getTypeMap(int typeId, String langId) {
        Map<String, String> ret = new LinkedHashMap<>();

        List<TypeBean> typeList = typeMap.get(typeId);

        for (TypeBean bean : typeList) {
            if (langId.equals(bean.getLang_id())) {
                ret.put(String.valueOf(bean.getValue()), bean.getName());
            }
        }

        return ret;
    }

    /**
     * 获取 type_id 的MapList
     *
     * @param typeId Type 的 type_id 字段
     * @param defaultAll 是否有All选项
     * @param langId TypeBean 的 lang_Id 字段
     * @return List<Map<String,String>>
     */
    public static  List<Map<String,String>> getTypeMapList(int typeId, boolean defaultAll, String langId) {

        List<Map<String,String>> ret = new ArrayList<>();

        List<TypeBean> typeList = typeMap.get(typeId);

        //加入All选项
        if (defaultAll) {
            Map<String, String> mapAll = new HashMap<>();
            mapAll.put("id", null);
            mapAll.put("name", "ALL");
            ret.add(mapAll);
        }
        for (TypeBean bean : typeList) {
            Map<String,String> tempMap = new HashMap<>();
            if (langId.equals(bean.getLang_id())) {
                tempMap.put("id", String.valueOf(bean.getValue()));
                tempMap.put("name", bean.getName());
            }
            ret.add(tempMap);
        }
        return ret;
    }

    /**
     * 获取 type_id 的BeanList
     *
     * @param typeId Type 的 type_id 字段
     * @param defaultAll 是否有All选项
     * @param langId TypeBean 的 lang_Id 字段
     * @return List<MasterInfoBean>
     */
    public static List<MasterInfoBean> getMasterInfoFromId(int typeId, boolean defaultAll, String langId) {

        List<MasterInfoBean> ret = new ArrayList<>();

        List<TypeBean> typeList = typeMap.get(typeId);

        //加入All选项
        if (defaultAll) {
            MasterInfoBean masterInfoAll = new MasterInfoBean();
            masterInfoAll.setType(typeId);
            masterInfoAll.setName("All");
            ret.add(masterInfoAll);
        }
        for (TypeBean bean : typeList) {
            if (langId.equals(bean.getLang_id())) {
                MasterInfoBean masterInfoBean = new MasterInfoBean();
                masterInfoBean.setType(typeId);
                masterInfoBean.setId(String.valueOf(bean.getValue()));
                masterInfoBean.setName(String.valueOf(bean.getName()));
                ret.add(masterInfoBean);
            }
        }
        return ret;
    }

    public static List<TypeBean> getTypeList(int typeId, String langId) {

        List<TypeBean> typeList = typeMap.get(typeId);
        List<TypeBean> retTypeList = new ArrayList<>();

        for (TypeBean bean : typeList) {
            if (langId.equals(bean.getLang_id())) {
                retTypeList.add(bean);
            }
        }
        return retTypeList;
    }

    /**
     * 取得 Type 对象的 value 字段
     *
     * @param typeId   type的typeId
     * @param name type的Name
     * @param langId TypeBean 的 lang_Id 字段
     * @return value
     */
    public static String getValue(int typeId, String name, String langId) {
        List<TypeBean> typeList = typeMap.get(typeId);

        for (TypeBean bean : typeList) {
            if (name.equals(bean.getName()) && langId.equals(bean.getLang_id())) {
                return bean.getValue();
            }
        }

        return null;
    }

    /**
     * 获取 type_id 的第一个 TypeBean 的 Name 字段
     *
     * @param typeId TypeBean 的 type_id 字段
     * @return TypeBean
     */
    public static String getTypeName(int typeId) {
        TypeBean bean = getTypeBean(typeId);

        return bean != null
                ? bean.getName()
                : null;
    }

    /**
     * 取得TypeName
     *
     * @param typeId   type的type_id
     * @param value type的value
     * @return name
     */
    public static String getTypeName(int typeId, String value) {
        List<TypeBean> typeList = typeMap.get(typeId);

        for (TypeBean bean : typeList) {
            if (value.equals(bean.getValue()) && Constants.LANGUAGE.EN.equals(bean.getLang_id())) {
                return bean.getName();
            }
        }

        return null;
    }

    /**
     * 获取 type_id 的第一个 TypeBean
     *
     * @param typeId Type 的 type_id 字段
     * @return TypeBean
     */
    public static TypeBean getTypeBean(int typeId) {
        List<TypeBean> typeList = typeMap.get(typeId);

        if (typeList != null) {
            for (TypeBean bean : typeList) {
                if (bean.getLang_id().equals(Constants.LANGUAGE.EN)) {
                    return bean;
                }
            }
        }
        return null;
    }

    public static Map<String, String> getTypeMap(int typeId) {
        Map<String, String> ret = new LinkedHashMap<>();

        List<TypeBean> typeList = typeMap.get(typeId);

        for (TypeBean bean : typeList) {
            if (Constants.LANGUAGE.EN.equals(bean.getLang_id())) {
                ret.put(String.valueOf(bean.getValue()), bean.getName());
            }
        }

        return ret;
    }

    /**
     * 获取 type_id 的MapList
     *
     * @param typeId Type 的 type_id 字段
     * @param defaultAll 是否有All选项
     * @return List<Map<String,String>>
     */
    public static  List<Map<String,String>> getTypeMapList(int typeId, boolean defaultAll) {

        List<Map<String,String>> ret = new ArrayList<>();

        List<TypeBean> typeList = typeMap.get(typeId);

        //加入All选项
        if (defaultAll) {
            Map<String, String> mapAll = new HashMap<>();
            mapAll.put("id", null);
            mapAll.put("name", "ALL");
            ret.add(mapAll);
        }
        for (TypeBean bean : typeList) {
            Map<String,String> tempMap = new HashMap<>();
            if (Constants.LANGUAGE.EN.equals(bean.getLang_id())) {
                tempMap.put("id", String.valueOf(bean.getValue()));
                tempMap.put("name", bean.getName());
            }
            ret.add(tempMap);
        }
        return ret;
    }

    /**
     * 获取 type_id 的BeanList
     *
     * @param typeId Type 的 type_id 字段
     * @param defaultAll 是否有All选项
     * @return List<MasterInfoBean>
     */
    public static List<MasterInfoBean> getMasterInfoFromId(int typeId, boolean defaultAll) {

        List<MasterInfoBean> ret = new ArrayList<>();

        List<TypeBean> typeList = typeMap.get(typeId);

        //加入All选项
        if (defaultAll) {
            MasterInfoBean masterInfoAll = new MasterInfoBean();
            masterInfoAll.setType(typeId);
            masterInfoAll.setName("All");
            ret.add(masterInfoAll);
        }
        for (TypeBean bean : typeList) {
            if (Constants.LANGUAGE.EN.equals(bean.getLang_id())) {
                MasterInfoBean masterInfoBean = new MasterInfoBean();
                masterInfoBean.setType(typeId);
                masterInfoBean.setId(String.valueOf(bean.getValue()));
                masterInfoBean.setName(String.valueOf(bean.getName()));
                ret.add(masterInfoBean);
            }
        }
        return ret;
    }

    public static List<TypeBean> getTypeList(int typeId) {

        List<TypeBean> typeList = typeMap.get(typeId);
        List<TypeBean> retTypeList = new ArrayList<>();

        for (TypeBean bean : typeList) {
            if (Constants.LANGUAGE.EN.equals(bean.getLang_id())) {
                retTypeList.add(bean);
            }
        }
        return retTypeList;
    }

    /**
     * 返回带有默认options的数据.
     * @param typeId
     * @return
     */
    public static List<TypeBean> getTypeListWithBlank(int typeId) {
        List<TypeBean> retTypeList = new ArrayList<>();
        TypeBean blankTypeBean = new TypeBean();
        blankTypeBean.setValue(null);
        blankTypeBean.setName("Select...");

        retTypeList.add(blankTypeBean);
        retTypeList.addAll(getTypeList(typeId));

        return retTypeList;
    }

    /**
     * 返回带有默认options的数据.
     * @param typeId
     * @param langId
     * @return
     */
    public static List<TypeBean> getTypeListWithBlank(int typeId, String langId) {
        List<TypeBean> retTypeList = new ArrayList<>();
        TypeBean blankTypeBean = new TypeBean();
        blankTypeBean.setValue(null);
        blankTypeBean.setName("Select...");

        retTypeList.add(blankTypeBean);
        retTypeList.addAll(getTypeList(typeId, langId));

        return retTypeList;
    }

    /**
     * 取得 Type 对象的 value 字段
     *
     * @param typeId   type的typeId
     * @param name type的Name
     * @return value
     */
    public static String getValue(int typeId, String name) {
        List<TypeBean> typeList = typeMap.get(typeId);

        for (TypeBean bean : typeList) {
            if (name.equals(bean.getName()) && Constants.LANGUAGE.EN.equals(bean.getLang_id())) {
                return bean.getValue();
            }
        }

        return null;
    }
}
