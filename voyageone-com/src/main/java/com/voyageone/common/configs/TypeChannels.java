package com.voyageone.common.configs;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.TypeChannelDao;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * 访问 com_mt_value_channel 表配置
 *
 * @author neil, 12/8/2015.
 * @version 2.0.0
 * @since 2.0.1
 */
public class TypeChannels {

    private static final Class selfClass = TypeChannels.class;

    private static final Logger logger = LoggerFactory.getLogger(selfClass);


    /* redis key */
    private static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_TypeChannel.toString();

    public static void reload() {
        TypeChannelDao channelValueDao= ConfigDaoFactory.getChannelValueDao();
        Map<String, TypeChannelBean> typeChannelBeanMap = new HashMap<>();
        channelValueDao.getAll().forEach(bean ->typeChannelBeanMap.put(buildKey(bean.getType_code(),bean.getChannel_id(),bean.getId()), bean));
        CacheHelper.reFreshSSB(KEY,typeChannelBeanMap);
        logger.info("TypeChannel 读取数量: " + CacheHelper.getSize(KEY));
    }
    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(String type_code, String channle_id, int real_id) {
        return type_code + CacheKeyEnums.SKIP + channle_id + CacheKeyEnums.SKIP + real_id;
    }

    private static List<TypeChannelBean> getTypeChannelBeans(String type, String channel_id) {
        Set<String> keySet = CacheHelper.getKeySet(KEY, selfClass);
        List<String> keyList=new ArrayList<>();
        keySet.forEach(k->{
            if(k.startsWith(type + CacheKeyEnums.SKIP + channel_id + CacheKeyEnums.SKIP)) keyList.add(k);
        });
        List<TypeChannelBean> beans=CacheHelper.getBeans(KEY,keyList, selfClass);
        return CollectionUtils.isEmpty(beans)
                ? new ArrayList<>()
                : beans
                    .stream()
                    .sorted((a,b)->{
                        if(a.getType_id()>b.getType_id()) return 1;
                        if(a.getType_id()==b.getType_id()&&a.getDisplay_order()>b.getDisplay_order()) return 1;
                        if(a.getType_id()==b.getType_id()&&a.getDisplay_order()==b.getDisplay_order()) return a.getValue().compareTo(b.getValue());
                        return -1;
                    })
                    .collect(Collectors.toList());
    }

    /**
     * @param type       类型名
     * @param channel_id 渠道id
     * @return 类型List
     */
    public static List<TypeChannelBean> getTypeList(String type, String channel_id) {
        return getTypeChannelBeans(type, channel_id);
     }

     /**
     * @param type       类型名
     * @param channel_id 渠道id
     * @param name       类型名称
     * @return 对应的类型值
     */
    public static String getValue(String type, String channel_id, String name) {

        List<TypeChannelBean> typeList = getTypeChannelBeans(type, channel_id);

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

        List<Map<String, String>> ret = new ArrayList<>();

        List<TypeChannelBean> typeList = getTypeChannelBeans(type, channel_id);

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

        List<Option> ret = new ArrayList<>();

        List<TypeChannelBean> typeList = getTypeChannelBeans(type, channel_id);

        if (typeList != null) {
            for (TypeChannelBean bean : typeList) {
                Option opt = new Option();
                opt.setDisplayName(bean.getName());
                opt.setValue(bean.getValue());
                ret.add(opt);
            }
        } else {
            logger.warn("channel id: " + channel_id + "property id: " + type + " 对应的options没有配置！！！");
        }

        return ret;
    }

    /**
     * @param type       type_code
     * @param channel_id 渠道id
     * @return List<Option>
     */
    public static List<TypeChannelBean> getTypeWithLang(String type, String channel_id, String lang_id) {

        List<TypeChannelBean> typeList = getTypeChannelBeans(type, channel_id);

        List<TypeChannelBean> res = new ArrayList<>();

        if (typeList != null) {
            for (TypeChannelBean bean : typeList) {
                if (!StringUtils.isEmpty(bean.getLang_id())
                        && bean.getLang_id().equals(lang_id)) {
                    res.add(bean);
                }
            }
        } else {
            logger.warn("channel id: " + channel_id + "property id: " + type + " 对应的options没有配置！！！");
        }

        return res;
    }
    /**
     * @param type       type_code
     * @param channel_id 渠道id
     * @return List<Option>
     */
    public static Map<String, TypeChannelBean> getTypeMapWithLang(String type, String channel_id, String lang_id) {

        List<TypeChannelBean> typeList = getTypeChannelBeans(type, channel_id);

        Map<String, TypeChannelBean> res = new HashMap<>();

        if (typeList != null) {
            for (TypeChannelBean bean : typeList) {
                if (!StringUtils.isEmpty(bean.getLang_id())
                        && bean.getLang_id().equals(lang_id)) {
                    res.put(bean.getValue(),bean);
                }
            }
        } else {
            logger.warn("channel id: " + channel_id + "property id: " + type + " 对应的options没有配置！！！");
        }

        return res;
    }
    /**
     * getTypeChannelByCode
     */
    public static TypeChannelBean getTypeChannelByCode(String type, String channel_id, String value, String... lang_id) {

        List<TypeChannelBean> typeList = getTypeChannelBeans(type, channel_id);

        for (TypeChannelBean typeChannelBean : typeList) {

            if (typeChannelBean.getValue().equals(value))
                return typeChannelBean;
        }
        return null;
    }

    /**
     * getTypeChannelByCode
     */
    public static TypeChannelBean getTypeChannelByCode(String type, String channel_id, String value, String lang_id) {

        List<TypeChannelBean> typeList = getTypeChannelBeans(type, channel_id);
        for (TypeChannelBean typeChannelBean : typeList) {

            if (typeChannelBean.getValue().equals(value) && typeChannelBean.getLang_id().equals(lang_id))
                return typeChannelBean;
        }
        return null;
    }

    /**
     * ------------------------------------------------------------------------------
     * 只获取skuCarts的数据
     * ------------------------------------------------------------------------------
     * 在表中, add_name1里存放的是三位数字
     * 第一位是display: 用来判断页面是否展示出来
     * 第二位是approve: 用来判断这个渠道是否允许approve这个sku到平台上去售卖
     * 第三位是order: 代表的是这个渠道是否会存在有订单
     * 一般来说:
     * 主数据的cart id是0, add_name1是100 (只展示)
     * 平台数据cart id不固定, add_name1是111 (展示, 允许approve到平台售卖, 存在订单)
     * 线下数据cart id是22, add_name1是001 (只存在订单)
     * ------------------------------------------------------------------------------
     *
     * @param channel_id channel id
     * @param strDAO     大写的字母D的场合代表display, 大写的字母A的场合代表approve, 大写的字母O的场合代表order, 之外的场合返回null
     * @return skuCarts
     */
    public static List<TypeChannelBean> getTypeListSkuCarts(String channel_id, String strDAO, String language) {
        String type = Constants.comMtTypeChannel.SKU_CARTS_53;
        int charIndex;
        switch (strDAO) {
            case Constants.comMtTypeChannel.SKU_CARTS_53_D:
                charIndex = 0;
                break;
            case Constants.comMtTypeChannel.SKU_CARTS_53_A:
                charIndex = 1;
                break;
            case Constants.comMtTypeChannel.SKU_CARTS_53_O:
                charIndex = 2;
                break;
            default:
                // 不合法的输入参数
                return null;
        }
        List<TypeChannelBean> typeChannelBeanList = getTypeList(type, channel_id);
        List<TypeChannelBean> resultList = new ArrayList<>();

        if (typeChannelBeanList != null) {
            for (TypeChannelBean typeChannelBean : typeChannelBeanList) {
                // 先检查数据有效性
                String cartId = org.apache.commons.lang3.StringUtils.trimToNull(typeChannelBean.getValue());
                if (cartId  == null) {
                    logger.error("getTypeListSkuCarts 未配置cartid " + typeChannelBean.toString());
                    continue;
                }
                if ((!"0".equals(cartId) && !"1".equals(cartId)) && Carts.getCart(cartId) == null) {
                    logger.error("getTypeListSkuCarts 该cart无效 " + typeChannelBean.toString());
                    continue;
                }

                // 如果是LIKING的cartID,不再作为返回平台渠道
//                if (Carts.getCart(cartId) != null && "3".equals(Carts.getCart(cartId).getCart_type())) {
//                    continue;
//                }

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
        resultList = resultList.stream().sorted(Comparator.comparing(TypeChannelBean::getDisplay_order)).collect(Collectors.toList());
        return resultList;
    }


    /**
     * 根据指定值查询类型列表
     *
     * @param type    类型名(注意不能用type_id)
     * @param value   指定值
     * @param langId  语言类型
     * @return List<TypeChannelBean>
     */
    public static List<TypeChannelBean> getTypeChannelBeansByTypeValueLang(String type, String value, String langId) {
        Set<String> keySet = CacheHelper.getKeySet(KEY, selfClass);
        List<String> keyList = new ArrayList<>();
        keySet.forEach(k -> {
            if (k.startsWith(type + CacheKeyEnums.SKIP)) keyList.add(k);
        });
        List<TypeChannelBean> beans = CacheHelper.getBeans(KEY,keyList, selfClass);
        return CollectionUtils.isEmpty(beans)
                ? new ArrayList<>()
                : beans
                .stream()
                .filter(bean -> (bean.getValue() != null && bean.getValue().equals(value)
                        && bean.getLang_id() != null && bean.getLang_id().equals(langId)))
                .sorted((a, b) -> {
                    if (a.getType_id() > b.getType_id()) return 1;
                    if (a.getType_id() == b.getType_id() && a.getDisplay_order() > b.getDisplay_order()) return 1;
                    if (a.getType_id() == b.getType_id() && a.getDisplay_order() == b.getDisplay_order())
                        return a.getValue().compareTo(b.getValue());
                    return -1;
                })
                .collect(Collectors.toList());
    }

    public static List<TypeChannelBean> getTypeChannelBeansByTypeLang(String type,String langId) {
        Set<String> keySet = CacheHelper.getKeySet(KEY, selfClass);
        List<String> keyList = new ArrayList<>();
        keySet.forEach(k -> {
            if (k.startsWith(type + CacheKeyEnums.SKIP)) keyList.add(k);
        });
        List<TypeChannelBean> beans = CacheHelper.getBeans(KEY,keyList, selfClass);
        return CollectionUtils.isEmpty(beans)
                ? new ArrayList<>()
                : beans
                .stream()
                .filter(bean -> (bean.getLang_id() != null && bean.getLang_id().equals(langId)))
                .sorted((a, b) -> {
                    if (a.getType_id() > b.getType_id()) return 1;
                    if (a.getType_id() == b.getType_id() && a.getDisplay_order() > b.getDisplay_order()) return 1;
                    if (a.getType_id() == b.getType_id() && a.getDisplay_order() == b.getDisplay_order())
                        return a.getValue().compareTo(b.getValue());
                    return -1;
                })
                .collect(Collectors.toList());
    }
}
