package com.voyageone.wms.service.impl;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.StoreConfigs;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.modelbean.ChannelStoreBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.wms.WmsConstants;
import com.voyageone.wms.dao.ItemDao;
import com.voyageone.wms.dao.LocationDao;
import com.voyageone.wms.dao.StoreDao;
import com.voyageone.wms.formbean.ItemLocationFormBean;
import com.voyageone.wms.formbean.ItemLocationLogFormBean;
import com.voyageone.wms.formbean.LocationFormBean;
import com.voyageone.wms.modelbean.ItemLocationBean;
import com.voyageone.wms.modelbean.ItemLocationLogBean;
import com.voyageone.wms.modelbean.LocationBean;
import com.voyageone.wms.service.WmsLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voyageone.wms.WmsMsgConstants.ItemLocationMsg;
import com.voyageone.core.MessageConstants.ComMsg;

/**
 * Created by Tester on 5/14/2015.
 *
 * @author Jonas
 */
@Service
public class WmsLocationServiceImpl implements WmsLocationService {
    @Autowired
    private LocationDao locationDao;

    @Autowired
    private StoreDao storeDao;

    @Autowired
    private ItemDao itemDao;

    /**
     * 【货架一览画面】初始化
     * @param user 用户登录信息
     * @return Map 画面初始化项目
     */
    @Override
    public Map<String, Object> doListInit(UserSessionBean user) {
        Map<String, Object> resultMap = new HashMap<>();

        // 获取用户可用的仓库
        List<ChannelStoreBean> usersStores = new ArrayList<>();

        List<ChannelStoreBean> channelStoreList = new ArrayList<>();

        // 创建默认的选项
        ChannelStoreBean defaultAll = new ChannelStoreBean();
        defaultAll.setStore_id(0);
        defaultAll.setStore_name("ALL");

        channelStoreList.add(defaultAll);

        // 追加用户可用的仓库
//        channelStoreList.addAll(usersStores);
        // 排除品牌方管理库存的仓库
        for (ChannelStoreBean storeBean : user.getCompanyRealStoreList() ) {
            if (StoreConfigs.getStore(new Long(storeBean.getStore_id())).getInventory_manager().equals(StoreConfigEnums.Manager.YES.getId())) {
                usersStores.add(storeBean);
                channelStoreList.add(storeBean);
            }
        }


        resultMap.put("storeList", channelStoreList);
        resultMap.put("addStoreList", usersStores);

        return resultMap;
    }

    /**
     * 根据名称搜索 Location
     *
     * @param name     模糊名称
     * @param store_id 仓库
     * @param page     当前页
     * @param size     单页行数
     * @return List
     */
    @Override
    public List<LocationFormBean> searchByName(String name, int store_id, int page, int size, UserSessionBean user) {

        int offset = (page - 1) * size;

        int timeZone = user.getTimeZone();

        List<LocationFormBean>  resultMap =  locationDao.searchByName(name, store_id, offset, size, user);

        for (LocationFormBean locationFormBean : resultMap) {
            // 格式化仓库名称
            locationFormBean.setStore_name(locationFormBean.getStore_id() == 0?"":StoreConfigs.getStore(locationFormBean.getStore_id()).getStore_name());

            // 格式化时间字段（修正时区）
            locationFormBean.setCreated_local(timeZone);
            locationFormBean.setModified_local(timeZone);
        }

        return resultMap;
    }

    /**
     * 根据名称搜索 Location 的总行数
     */
    @Override
    public int searchCountByName(String name, int store_id, UserSessionBean user) {
        return locationDao.searchCountByName(name, store_id, user);
    }

    /**
     * 删除 Location
     *
     * @param location_id id
     * @param store_id    仓库
     * @param modified    最后的修改时间
     */
    @Override
    public void deleteLocation(int location_id, int store_id, String modified) {
        int itemCount = locationDao.itemCountInLocation(location_id, store_id);

        // 这个货架下，仍然还有货品。强制要求无货品时，才能删除
        if (itemCount > 0) {
            LocationFormBean location = locationDao.getLocation(location_id);

            throw new BusinessException(ItemLocationMsg.LOCATION_NOT_EMPTY, location.getLocation_name());
        }

        int count = locationDao.deleteLocation(location_id, store_id, modified);

        if (count < 1)
            throw new BusinessException(ComMsg.UPDATE_BY_OTHER);
    }

    /**
     * 增加 Location
     *
     * @param location_name name
     * @param store_id      仓库
     * @param user          当前用户
     */
    @Override
    public void addLocation(String location_name, int store_id, UserSessionBean user) {
        // 验证名称的唯一性
        if (locationDao.isNameExists(location_name, store_id)) {
            throw new BusinessException(ItemLocationMsg.LOCATION_NAME_EXISTS, location_name);
        }

        // 生成一个 LocationBean
        LocationBean location = createLocation(location_name, store_id, user);

        int count = locationDao.insertLocation(location);

        if (count < 1)
            throw new BusinessException(ComMsg.UPDATE_BY_OTHER);
    }

    /**
     * 【货架绑定画面】初始化
     * @param user 用户登录信息
     * @return Map 画面初始化项目
     */
    @Override
    public Map<String, Object> doBindInit(UserSessionBean user) {
        Map<String, Object> resultMap = new HashMap<>();

        //获取用户仓库
        resultMap.put("storeList", user.getCompanyRealStoreList());

        return resultMap;
    }

    /**
     * 在目标仓库，检索目标商品的所有位置，以及其变更记录
     *
     * @param code     商品的 Code 或 Barcode
     * @param store_id 目标仓库
     * @return Map/ itemLocations/ itemLocationLogs
     */
    @Override
    public Map<String, Object> searchItemLocations(String code, int store_id, UserSessionBean user) {
        // 检索渠道
        String order_channel_id = storeDao.getChannel_id(store_id);

        // 没找到仓库的渠道
        if (StringUtils.isEmpty(order_channel_id))
            throw new BusinessException(ItemLocationMsg.NOT_FOUND_CHANNEL);

        // 根据画面输入code，取得真实的code
        code = getCode(code, order_channel_id);

        // 取得仓库的名称
        String storeName = StoreConfigs.getStore(store_id).getStore_name();

        // 检索货位
        List<ItemLocationFormBean> itemLocations = locationDao.selectItemLocations(code, store_id);

        // 检索日志
        List<ItemLocationLogFormBean> itemLocationLogs = locationDao.selectItemLocationLogs(code, store_id);

        // 格式化日志对象的“修改”时间字段，对其进行时区处理
        int timeZone = user.getTimeZone();
        for (ItemLocationLogFormBean bean : itemLocationLogs) bean.setModified_local(timeZone);

        Map<String, Object> map = new HashMap<>();

        map.put("store_id", store_id);

        map.put("storeName", storeName);

        map.put("code", code);

        map.put("sku", "");

        map.put("itemLocations", itemLocations);

        map.put("itemLocationLogs", itemLocationLogs);

        return map;
    }

    /**
     * 在目标仓库，检索目标商品的所有位置，以及其变更记录
     *
     * @param sku     商品的 sku
     * @param store_id 目标仓库
     * @return Map/ itemLocations/ itemLocationLogs
     */
    @Override
    public Map<String, Object> searchItemLocationsBySku(String sku, int store_id, UserSessionBean user) {
        // 检索渠道
        String order_channel_id = storeDao.getChannel_id(store_id);

        // 没找到仓库的渠道
        if (StringUtils.isEmpty(order_channel_id))
            throw new BusinessException(ItemLocationMsg.NOT_FOUND_CHANNEL);

        // 根据画面输入sku，取得真实的sku，和code
        ArrayList<String> codeAndSku = getSkuAndCode(sku, order_channel_id);
        sku = codeAndSku.get(0);
        String code = codeAndSku.get(1);

        // 取得仓库的名称
        String storeName = StoreConfigs.getStore(store_id).getStore_name();

        // 检索货位
        List<ItemLocationFormBean> itemLocations = locationDao.selectItemLocationsBySku(sku, store_id);

        // 检索日志
        List<ItemLocationLogFormBean> itemLocationLogs = locationDao.selectItemLocationLogsBySku(sku, store_id);

        // 格式化日志对象的“修改”时间字段，对其进行时区处理
        int timeZone = user.getTimeZone();
        for (ItemLocationLogFormBean bean : itemLocationLogs) bean.setModified_local(timeZone);

        Map<String, Object> map = new HashMap<>();

        map.put("store_id", store_id);

        map.put("storeName", storeName);

        map.put("code", code);

        map.put("sku", sku);

        map.put("itemLocations", itemLocations);

        map.put("itemLocationLogs", itemLocationLogs);

        return map;
    }

    /**
     * 在目标仓库，检索货架上的所有商品
     *
     * @param skuPara 画面输入Sku（Barcode）
     * @param order_channel_id 当前渠道
     * @return ArrayList[0] sku
     *          ArrayList[1] code
     */
    private ArrayList<String> getSkuAndCode(String skuPara, String order_channel_id) {
        ArrayList<String> ret = new ArrayList<String>();
        String sku = skuPara;
        String code = "";

        // 根据输入的条形码找到对应的UPC
        sku = itemDao.getUPC(order_channel_id, sku);

        // 通过 sku 和 barcode 进行匹配。检索所有匹配商品的 sku （去重复）
        List<HashMap<String, String>> skus = itemDao.searchSku(sku, order_channel_id);

        // 如果结果有多个，说明输入的 sku 不能唯一匹配
        if (skus.size() != 1)
            throw new BusinessException(ItemLocationMsg.CANT_MATCH_UNIQUE_ITEM, sku);

        HashMap<String, String> skuAndCode = skus.get(0);
        sku = skuAndCode.get("sku");
        code = skuAndCode.get("itemcode");

        ret.add(sku);
        ret.add(code);

        return ret;
    }

    /**
     * 在目标仓库，检索货架上的所有商品
     *
     * @param codePara 画面输入code（Barcode）
     * @param order_channel_id 当前渠道
     * @return code
     */
    private String getCode(String codePara, String order_channel_id) {
        String code = codePara;
        // 根据输入的条形码找到对应的UPC
        code = itemDao.getUPC(order_channel_id, code);

        // 通过 itemcode 和 barcode 进行匹配。检索所有匹配商品的 itemcode （去重复）
        List<String> codes = itemDao.searchItemCode(code, order_channel_id);

        // 如果结果有多个，说明输入的 code 不能唯一匹配
        if (codes.size() != 1)
            throw new BusinessException(ItemLocationMsg.CANT_MATCH_UNIQUE_ITEM, code);

        code = codes.get(0);

        return code;
    }

    /**
     * 在目标仓库，检索货架上的所有商品
     *
     * @param location_id 货架Id
     * @param store_id 目标仓库
     * @return Map/ itemLocations/ itemLocationLogs
     */
    @Override
    public Map<String, Object> searchItemLocationsByLocationId(int location_id, int store_id, String location_name, UserSessionBean user) {
        // 检索渠道
        String order_channel_id = storeDao.getChannel_id(store_id);

        // 没找到仓库的渠道
        if (StringUtils.isEmpty(order_channel_id))
            throw new BusinessException(ItemLocationMsg.NOT_FOUND_CHANNEL);

        // 取得仓库的名称
        String storeName = StoreConfigs.getStore(store_id).getStore_name();

        // 检索货位
        List<ItemLocationFormBean> itemLocations = locationDao.selectItemLocationsByLocationId(location_id, store_id);

        Map<String, Object> map = new HashMap<>();

        map.put("store_id", store_id);

        map.put("storeName", storeName);

        map.put("locationName", location_name);

        map.put("itemLocations", itemLocations);

        map.put("total", itemLocations.size());

        return map;
    }

    /**
     * 在指定的仓库，为指定的商品，添加一个货位。
     * 检查货架是否存在，code是否在存在，以及仓库、渠道等的操作权限
     *
     * @param store_id      目标仓库
     * @param code          目标商品
     * @param location_name 目标位置
     * @param user          操作人
     * @return ItemLocationBean
     */
    @Override
    public Map<String, Object> addItemLocation(int store_id, String code, String sku, String location_name, String bind_by_Location, UserSessionBean user) {
        // 检索渠道
        String order_channel_id = storeDao.getChannel_id(store_id);

        // 没找到仓库，所属的渠道
        if (StringUtils.isEmpty(order_channel_id))
            throw new BusinessException(ItemLocationMsg.NOT_FOUND_CHANNEL);

        // 根据货架，绑定对应的物品
        if (!StringUtils.isEmpty(bind_by_Location) && "1".equals(bind_by_Location)) {
            // 根据code绑定
            if (StringUtils.isEmpty(sku)) {
                sku = "";
                code = getCode(code, order_channel_id);
            } else {
            // 根据sku绑定
                ArrayList<String> codeAndSku = getSkuAndCode(sku, order_channel_id);
                sku = codeAndSku.get(0);
                code = codeAndSku.get(1);
            }
        }

        if (StringUtils.isEmpty(sku)) {
            // 检查 Code 在目标渠道里，是否存在
            if (!hasCodeInChannel(order_channel_id, code))
                throw new BusinessException(ItemLocationMsg.NOT_FOUND_CODE_IN_CHANNEL, code);
        } else {
            // 检查 SKU 在目标渠道里，是否存在
            if (!hasSkuInChannel(order_channel_id, sku))
                throw new BusinessException(ItemLocationMsg.NOT_FOUND_SKU_IN_CHANNEL, sku);
        }

        ItemLocationBean itemLocation = createItemLocation(order_channel_id, store_id, code, sku, location_name, user);

        // 检查，这个商品，是否之前就已经在相同的位置上了。
        if (isItemLocationExists(itemLocation))
            throw new BusinessException(ItemLocationMsg.ITEM_ALREADY_IN_THERE, code, location_name);

        int count = locationDao.insertItemLocation(itemLocation);

        // 插入成功之后，从新查询，获取自增 ID 和其他信息
        if (count > 0) {
            ItemLocationLogFormBean itemLocationLog = insertItemLocationLog(itemLocation, true, user);

            if (itemLocationLog != null) itemLocationLog.setModified_local(user.getTimeZone());

            ItemLocationFormBean itemLocation1 = locationDao.getItemLocationByLocation(itemLocation);

            Map<String, Object> result = new HashMap<>();
            result.put("itemLocationLog", itemLocationLog);
            result.put("itemLocation", itemLocation1);

            return result;
        } else
            throw new BusinessException(ComMsg.UPDATE_BY_OTHER);
    }

    @Override
    public ItemLocationLogFormBean deleteItemLocation(int item_location_id, String modified, UserSessionBean user) {
        ItemLocationBean itemLocation = locationDao.getItemLocation(item_location_id);

        if (itemLocation == null)
            throw new BusinessException(ComMsg.UPDATE_BY_OTHER);

        // wms_bt_item_location_ext删除
//        locationDao.deleteItemLocationExt(itemLocation);

        int count = locationDao.deleteItemLocation(item_location_id, modified);

        if (count > 0) {
            ItemLocationLogFormBean formBean = insertItemLocationLog(itemLocation, false, user);

            if (formBean != null) formBean.setModified_local(user.getTimeZone());

            return formBean;
        }

        // 或抛出 Update Other
        return null;
    }

    /**
     * 记录一次 ItemLocation 的操作，并返回带有 Location Name 的 ItemLocationLogFormBean
     *
     * @param itemLocation 变动的具体信息
     * @param add          是增加，还是删除
     * @param user         操作人
     */
    private ItemLocationLogFormBean insertItemLocationLog(ItemLocationBean itemLocation, boolean add, UserSessionBean user) {
        ItemLocationLogBean itemLocationLog = createItemLocationLogBean(itemLocation, add, user);

        // 尝试插入新建的 ItemLocationLogBean
        int count = locationDao.insertItemLocationLog(itemLocationLog);

        if (count < 1) return null;

        return locationDao.getInsertedLog(itemLocationLog);
    }

    /**
     * 创建一个新的 ItemLocationLogBean
     *
     * @param itemLocation ItemLocationBean
     * @param add          是添加 Location 还是删除 Location
     * @param user         操作人
     * @return ItemLocationLogBean
     */
    private ItemLocationLogBean createItemLocationLogBean(final ItemLocationBean itemLocation, boolean add, UserSessionBean user) {
        ItemLocationLogBean itemLocationLog = new ItemLocationLogBean();

        itemLocationLog.setOrder_channel_id(itemLocation.getOrder_channel_id());
        itemLocationLog.setStore_id(itemLocation.getStore_id());
        itemLocationLog.setCode(itemLocation.getCode());
        itemLocationLog.setSku(itemLocation.getSku());
        itemLocationLog.setLocation_id(itemLocation.getLocation_id());
        itemLocationLog.setComment(
                add
                        ? WmsConstants.ItemLocationLogComments.INSERT
                        : WmsConstants.ItemLocationLogComments.DELETE
        );

        itemLocationLog.setActive(true);

        itemLocationLog.setCreater(user.getUserName());
        itemLocationLog.setModifier(user.getUserName());
        return itemLocationLog;
    }

    /**
     * 检查这条数据是否已经存在
     *
     * @param itemLocation ItemLocationBean
     * @return boolean
     */
    private boolean isItemLocationExists(ItemLocationBean itemLocation) {
        return locationDao.getItemLocationByLocation(itemLocation) != null;
    }

    /**
     * 创建一个新的 ItemLocationBean 实例
     *
     * @param order_channel_id 渠道
     * @param store_id         仓库
     * @param code             商品 Code
     * @param location_name    货位
     * @param user             操作人
     * @return ItemLocationBean
     */
    private ItemLocationBean createItemLocation(String order_channel_id, int store_id, String code, String sku, String location_name, UserSessionBean user) {
        LocationFormBean location = locationDao.getLocationByName(location_name, store_id);

        if (location == null)
            throw new BusinessException(ItemLocationMsg.NOT_FOUND_LOCATION, location_name);

        ItemLocationBean itemLocation = new ItemLocationBean();

        itemLocation.setOrder_channel_id(order_channel_id);
        itemLocation.setStore_id(store_id);
        itemLocation.setCode(code);
        itemLocation.setSku(sku);
        itemLocation.setLocation_id(location.getLocation_id());

        itemLocation.setActive(true);

        itemLocation.setCreater(user.getUserName());
        itemLocation.setModifier(user.getUserName());

        return itemLocation;
    }

    /**
     * 检查目标渠道下，是否有该 Code 的商品
     *
     * @param order_channel_id 渠道
     * @param code             商品 Code
     * @return boolean
     */
    private boolean hasCodeInChannel(String order_channel_id, String code) {
        return itemDao.countByCode(order_channel_id, code) > 0;
    }

    /**
     * 检查目标渠道下，是否有该 Code 的商品
     *
     * @param order_channel_id 渠道
     * @param sku             商品 Sku
     * @return boolean
     */
    private boolean hasSkuInChannel(String order_channel_id, String sku) {
        return itemDao.countBySku(order_channel_id, sku) > 0;
    }

    /**
     * 生成一个 Location Bean
     *
     * @param location_name 名称
     * @param store_id      仓库
     * @param user          当前用户
     * @return LocationBean
     */
    private LocationBean createLocation(String location_name, int store_id, UserSessionBean user) {
        String order_channel_id = storeDao.getChannel_id(store_id);

        // 没找到仓库的渠道
        if (StringUtils.isEmpty(order_channel_id))
            throw new BusinessException(ItemLocationMsg.NOT_FOUND_CHANNEL);

        LocationBean location = new LocationBean();

        location.setLocation_name(location_name);
        location.setOrder_channel_id(order_channel_id);
        location.setStore_id(store_id);

        location.setActive(true);

        String now = DateTimeUtil.getNow();

        location.setCreater(user.getUserName());
        location.setCreated(now);
        location.setModifier(user.getUserName());
        location.setModified(now);

        return location;
    }
}
