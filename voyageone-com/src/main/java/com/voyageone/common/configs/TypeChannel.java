package com.voyageone.common.configs;

import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.configs.dao.TypeChannelDao;
import com.voyageone.common.masterdate.schema.option.Option;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 访问 com_mt_value_channel 表配置
 *
 * @author neil, 12/8/2015.
 * @version 2.0.0
 * @since 2.0.1
 */
public class TypeChannel {

    private static HashMap<String, List<TypeChannelBean>> typeMap;

    private static Log logger = LogFactory.getLog(TypeChannel.class);

    public static void init(TypeChannelDao channelValueDao) {
        if (typeMap != null) return;

        typeMap = new HashMap<>();

        logger.debug("Type 初始化");

        List<TypeChannelBean> beans = channelValueDao.getAll();

        logger.info("Type 读取数量：" + beans.size());

        List<TypeChannelBean> childList;

        for (TypeChannelBean bean : beans) {
            String key = bean.getType_code() + "-" + bean.getChannel_id();
            childList = typeMap.get(key);

            if (childList == null) {
                typeMap.put(key, childList = new ArrayList<>());
            }
            childList.add(bean);
        }

        logger.debug("Type 按 ID 已存入：" + typeMap.size());
    }

    /**
     * @param type       类型名
     * @param channel_id 渠道id
     * @return 类型List
     */
    public static List<TypeChannelBean> getTypeList(String type, String channel_id) {

        String key = type + "-" + channel_id;
        List<TypeChannelBean> typeList = typeMap.get(key);
        List<TypeChannelBean> retTypeList = new ArrayList<>();

        for (TypeChannelBean bean : typeList) {
            retTypeList.add(bean);
        }
        return retTypeList;
    }

    /**
     * @param type       类型名
     * @param channel_id 渠道id
     * @param name       类型名称
     * @return 对应的类型值
     */
    public static String getValue(String type, String channel_id, String name) {
        String key = type + "-" + channel_id;
        List<TypeChannelBean> typeList = typeMap.get(key);

        for (TypeChannelBean bean : typeList) {
            if (name.equals(bean.getName())) {
                return bean.getValue();
            }
        }

        return null;
    }

    /**
     * @param type       type_code
     * @param channel_id 渠道id
     */
    public static List<Map<String, String>> getTypeMapList(String type, String channel_id) {
        String key = type + "-" + channel_id;
        List<Map<String, String>> ret = new ArrayList<>();

        List<TypeChannelBean> typeList = typeMap.get(key);

        for (TypeChannelBean bean : typeList) {
            Map<String, String> tempMap = new HashMap<>();
            tempMap.put("id", String.valueOf(bean.getValue()));
            tempMap.put("name", bean.getName());
            ret.add(tempMap);
        }
        return ret;
    }

    /**
     * @param type       type_code
     * @param channel_id 渠道id
     * @return List<Option>
     */
    public static List<Option> getOptions(String type, String channel_id, String...lang_id) {
        String key = type + "-" + channel_id;
        List<Option> ret = new ArrayList<>();

        List<TypeChannelBean> typeList = typeMap.get(key);

        for (TypeChannelBean bean : typeList) {
            Option opt = new Option();
            opt.setDisplayName(bean.getName());
            opt.setValue(bean.getValue());
            ret.add(opt);
        }
        return ret;
    }
}
