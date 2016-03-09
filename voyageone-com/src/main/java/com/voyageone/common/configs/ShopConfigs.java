package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Enums.ShopConfigEnums;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.beans.ShopConfigBean;
import com.voyageone.common.configs.dao.ShopConfigDao;
import com.voyageone.common.configs.dao.ShopDao;
import com.voyageone.common.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 访问 tm_channel_shop 和 tm_channel_shop_configs 表配置
 * Created by Jack on 4/17/2015.
 */
public class ShopConfigs {

    private static Configs configs;
    private static Shops shops;
    private static List<ShopBean> allShops;
    private static List<CartBean> allCarts;
    private static final Log logger = LogFactory.getLog(ShopConfigs.class);

    /**
     * 初始化店铺相关的基本信息和配置信息
     *
     * @param shopDao       用于获取基本信息
     * @param shopConfigDao 用于获取配置信息
     */
    public static void init(ShopDao shopDao, ShopConfigDao shopConfigDao) {
        if (configs == null) {
            List<ShopConfigBean> all = shopConfigDao.getAll();

            logger.info("ShopConfig 读取数量: " + all.size());

            configs = new Configs(all);
        }

        if (shops == null) {
            allShops = shopDao.getAll();

            logger.info("Shop 读取数量: " + allShops.size());

            shops = new Shops(allShops);
        }

        if (allCarts == null) {
            allCarts = shopDao.getAllCart();

            logger.info("Carts 读取数量: " + allCarts.size());

        }
    }

    /**
     * 获取指定店铺的基本信息（类型重载）
     *
     * @return ShopBean
     */
    public static ShopBean getShop(String order_channel_id, int cart_id) {
        return getShop(order_channel_id, String.valueOf(cart_id));
    }

    public static ShopBean getShop(OrderChannelBean channel, CartBean cart) {
        return getShop(channel.getOrder_channel_id(), cart.getCart_id());
    }


    /**
     * 获取指定店铺的基本信息
     *
     * @return ShopBean
     */
    public static ShopBean getShop(String order_channel_id, String cart_id) {
        if (shops == null) {
            logger.error("====== ShopConfig 没有初始化，必须先调用 init");
            return null;
        }

        return shops.get(order_channel_id + "_" + cart_id);
    }

    /**
     * 获取指定渠道的店铺信息
     *
     * @return List<ShopBean>
     */
    public static List<ShopBean> getChannelShopList(String order_channel_id) {
        if (allShops == null) {
            logger.error("====== ShopConfig 没有初始化，必须先调用 init");
            return null;
        }
        List<ShopBean> channelShopList = new ArrayList<>();
        for (ShopBean bean : allShops) {
            if (order_channel_id.equals(bean.getOrder_channel_id())) {
                channelShopList.add(bean);
            }
        }
        return channelShopList;
    }


    /**
     * 获取指定店铺的渠道信息
     *
     * @return List<ShopBean>
     */
    public static List<ShopBean> getCartShopList(String cart_id) {
        if (allShops == null) {
            logger.error("====== ShopConfig 没有初始化，必须先调用 init");
            return null;
        }
        List<ShopBean> cartShopList = new ArrayList<>();
        for (ShopBean bean : allShops) {
            if (cart_id.equals(bean.getCart_id())) {
                cartShopList.add(bean);
            }
        }
        return cartShopList;
    }

    public static List<ShopBean> getShopListByPlatform(PlatFormEnums.PlatForm platform) {
        if (allShops == null) {
            logger.error("====== ShopConfig 没有初始化，必须先调用 init");
            return null;
        }
        List<ShopBean> shopList = new ArrayList<>();
        for (ShopBean bean : allShops) {
            if (platform.getId().equals(bean.getPlatform_id())) {
                shopList.add(bean);
            }
        }
        return shopList;
    }

    /**
     * 获取所有渠道的店铺信息
     *
     * @return List<ShopBean>
     */
    public static List<ShopBean> getShopList() {
        if (allShops == null) {
            logger.error("====== ShopConfig 没有初始化，必须先调用 init");
            return null;
        }
        return allShops;
    }

    /**
     * 获取指定渠道信息
     *
     * @return CartBean
     */
    public static CartBean getCart(String cart_id) {
        if (allCarts == null) {
            logger.error("====== ShopConfig 没有初始化，必须先调用 init");
            return null;
        }

        return allCarts.stream()
                .filter(bean -> cart_id.equals(bean.getCart_id()))
                .findFirst()
                .orElse(null);
    }

    public static CartBean getCart(int cart_id) {
        return getCart(String.valueOf(cart_id));
    }

    /**
     * 获取指定平台的渠道信息
     *
     * @return List<CartBean>
     */
    public static List<CartBean> getCartList(PlatFormEnums.PlatForm platform) {
        if (allCarts == null) {
            logger.error("====== ShopConfig 没有初始化，必须先调用 init");
            return null;
        }
        return allCarts.stream()
                .filter(bean -> platform.getId().equals(bean.getPlatform_id()))
                .collect(Collectors.toList());
    }

    /**
     * 获取所有的渠道信息
     *
     * @return List<CartBean>
     */
    public static List<CartBean> getAllCartList() {

        return allCarts;
    }

    /**
     * 获取指定店铺的第一个配置参数
     *
     * @param order_channel_id 渠道
     * @param cart_id          店铺
     * @param name             配置名
     * @return String
     */
    public static String getVal1(String order_channel_id, String cart_id, ShopConfigEnums.Name name) {
        List<ShopConfigBean> beans = getConfigs(order_channel_id, cart_id, name);
        return (beans == null || beans.size() < 1) ? "" : beans.get(0).getCfg_val1();
    }

    /**
     * 获取指定店铺的第一个配置参数
     *
     * @param shopBean 店铺
     * @param name     配置名
     * @return String
     */
    public static String getVal1(ShopBean shopBean, ShopConfigEnums.Name name) {
        return getVal1(shopBean.getOrder_channel_id(), shopBean.getCart_id(), name);
    }

    /**
     * 获取指定店铺、配置名称、第一个参数的第二个参数
     *
     * @param order_channel_id 渠道
     * @param cart_id          店铺
     * @param name             配置名
     * @param val1             第一参数
     * @return String
     */
    public static String getVal2(String order_channel_id, String cart_id, ShopConfigEnums.Name name, String val1) {
        List<ShopConfigBean> beans = getConfigs(order_channel_id, cart_id, name);
        if (beans == null || beans.size() < 1) {
            return "";
        }
        String val2 = "";
        for (ShopConfigBean shopConfigBean : beans) {
            if (val1.equals(shopConfigBean.getCfg_val1())) {
                val2 = shopConfigBean.getCfg_val2();
                break;
            }
        }
        return val2;
    }

    /**
     * 获取指定店铺、配置名称、第一个参数的第二个参数
     *
     * @param shopBean 店铺
     * @param name     配置名
     * @param val1     第一参数
     * @return String
     */
    public static String getVal2(ShopBean shopBean, ShopConfigEnums.Name name, String val1) {
        return getVal2(shopBean.getOrder_channel_id(), shopBean.getCart_id(), name, val1);
    }

    /**
     * 获取指定店铺的一个配置的所有参数
     *
     * @return List<ShopConfigBean>
     */
    public static List<ShopConfigBean> getConfigs(String order_channel_id, String cart_id, ShopConfigEnums.Name name) {
        if (configs == null) {
            logger.error("====== ShopConfig 没有初始化，必须先调用 init");
            return null;
        }

        if (!configs.containsKey(order_channel_id + "_" + cart_id)) return null;

        ConfigMap map = configs.get(order_channel_id + "_" + cart_id);

        if (!map.containsKey(name)) return null;

        return map.get(name);
    }
    
    /* ====== 辅助 内部类 ====== */

    /**
     * 专用存储店铺基础信息
     *
     * @author Jack
     */
    private static class Shops extends HashMap<String, ShopBean> {
        private static final long serialVersionUID = 1L;

        public Shops(List<ShopBean> beans) {
            for (ShopBean bean : beans) {
                put(bean.getOrder_channel_id() + '_' + bean.getCart_id(), bean);
            }
        }
    }

    /**
     * 专用存储所有店铺的配置信息
     *
     * @author Jack
     */
    private static class Configs extends HashMap<String, ConfigMap> {
        private static final long serialVersionUID = 1L;

        public Configs(List<ShopConfigBean> beans) {
            for (ShopConfigBean bean : beans) {
                put(bean);
            }
        }

        public void put(ShopConfigBean config) {
            String id = config.getOrder_channel_id() + "_" + config.getCart_id();
            ConfigMap map = containsKey(id) ? get(id) : new ConfigMap();

            try {
                ShopConfigEnums.Name name = ShopConfigEnums.Name.valueOf(config.getCfg_name());
                map.put(name, config);
                put(id, map);
            } catch (IllegalArgumentException e) {
                logger.warn(String.format("Feed 枚举匹配警告: [%s] NO \"%s\"", config.getOrder_channel_id(), config.getCfg_name()));
            }
        }
    }

    /**
     * 专用存储单个店铺的所有配置信息
     *
     * @author Jack
     */
    private static class ConfigMap extends HashMap<ShopConfigEnums.Name, List<ShopConfigBean>> {
        private static final long serialVersionUID = 1L;

        public void put(ShopConfigEnums.Name name, ShopConfigBean config) {
            if (!containsKey(name)) {
                super.put(name, new ArrayList<ShopConfigBean>());
            }
            get(name).add(config);
        }
    }

    /**
     * 获取指定店铺名（带渠道）
     *
     * @return ShopNameDis
     */
    public static String getShopNameDis(String order_channel_id, String cart_id) {
        if (shops == null) {
            logger.error("====== ShopConfig 没有初始化，必须先调用 init");
            return null;
        }

        ShopBean shopBean = shops.get(order_channel_id + "_" + cart_id);

        String shopNameDis = shopBean.getShop_name();
        if (!StringUtils.isNullOrBlank2(shopBean.getComment())) {
            shopNameDis = shopNameDis + "(" + shopBean.getComment() + ")";
        }
        return shopNameDis;
    }

}
