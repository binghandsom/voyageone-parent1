package com.voyageone.common.configs;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.configs.dao.TypeChannelDao;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.util.StringUtils;
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
    public static List<Option> getOptions(String type, String channel_id) {
        String key = type + "-" + channel_id;
        List<Option> ret = new ArrayList<>();

        List<TypeChannelBean> typeList = typeMap.get(key);

        if (typeList != null){
            for (TypeChannelBean bean : typeList) {
                Option opt = new Option();
                opt.setDisplayName(bean.getName());
                opt.setValue(bean.getValue());
                ret.add(opt);
            }
        } else {
            logger.warn("channel id: " + channel_id + "property id: "+type +" 对应的options没有配置！！！");
        }

        return ret;
    }

    /**
     * @param type       type_code
     * @param channel_id 渠道id
     * @return List<Option>
     */
    public static List<TypeChannelBean> getTypeWithLang(String type, String channel_id, String lang_id) {
        String key = type + "-" + channel_id;
        List<TypeChannelBean> typeList = typeMap.get(key);

        List<TypeChannelBean> res = new ArrayList<>();

        if (typeList != null){
            for (TypeChannelBean bean : typeList) {
                if (!StringUtils.isEmpty(bean.getLang_id())
                        && bean.getLang_id().equals(lang_id)) {
                    res.add(bean);
                }
            }
        } else {
            logger.warn("channel id: " + channel_id + "property id: "+type +" 对应的options没有配置！！！");
        }

        return res;
    }

    /**
     *
     * @param type
     * @param channel_id
     * @param value
     * @param lang_id
     * @return
     */
    public static TypeChannelBean getTypeChannelByCode(String type, String channel_id, String value, String... lang_id) {
        String key = type + "-" + channel_id;
        List<TypeChannelBean> typeList = typeMap.get(key);

        for (TypeChannelBean typeChannelBean: typeList) {

            if (typeChannelBean.getValue().equals(value))
                return typeChannelBean;
        }
        return null;
    }

	/**
     * ------------------------------------------------------------------------------
     * 只获取skuCarts的数据
     * ------------------------------------------------------------------------------
     * 在表中, add_name1里存放的是三位数字
     *   第一位是display: 用来判断页面是否展示出来
     *   第二位是approve: 用来判断这个渠道是否允许approve这个sku到平台上去售卖
     *   第三位是order: 代表的是这个渠道是否会存在有订单
     * 一般来说:
     *   主数据的cart id是0, add_name1是100 (只展示)
     *   平台数据cart id不固定, add_name1是111 (展示, 允许approve到平台售卖, 存在订单)
     *   线下数据cart id是22, add_name1是001 (只存在订单)
     * ------------------------------------------------------------------------------
     * @param channel_id channel id
     * @param strDAO 大写的字母D的场合代表display, 大写的字母A的场合代表approve, 大写的字母O的场合代表order, 之外的场合返回null
     * @return skuCarts
     */
    public static List<TypeChannelBean> getTypeListSkuCarts(String channel_id, String strDAO, String language) {
        String type = Constants.comMtTypeChannel.SKU_CARTS_53;
        int charIndex = 0;
        switch (strDAO) {
            case Constants.comMtTypeChannel.SKU_CARTS_53_D: {
                charIndex = 0;
                break;
            }
            case Constants.comMtTypeChannel.SKU_CARTS_53_A: {
                charIndex = 1;
                break;
            }
            case Constants.comMtTypeChannel.SKU_CARTS_53_O: {
                charIndex = 2;
                break;
            }
            default: {
                // 不合法的输入参数
                return null;
            }
        }
        List<TypeChannelBean> typeChannelBeanList = getTypeList(type, channel_id);
        List<TypeChannelBean> resultList = new ArrayList<>();

        if (typeChannelBeanList != null) {

            for (TypeChannelBean typeChannelBean : typeChannelBeanList) {
                // 如果add_name1里为空, 说明这家店没有好好配置过, 所以不返回记录, 只有配置好了之后才能正常使用
                String add_name1 = typeChannelBean.getAdd_name1();
                if (!StringUtils.isEmpty(add_name1)) {
                    if (add_name1.length() == 3) {
                        add_name1 = add_name1.substring(charIndex, charIndex + 1);
                        if ("1".equals(add_name1)
                                && !StringUtils.isEmpty(typeChannelBean.getLang_id())
                                && typeChannelBean.getLang_id().equals(language)) {
                            // 这条记录是属于需要返回的数据
                            resultList.add(typeChannelBean);
                        }
                    }
                }
            }

        }

        return resultList;
    }

}
