package com.voyageone.service.impl.wms;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.voyageone.common.configs.Stores;
import com.voyageone.common.configs.beans.StoreBean;
import com.voyageone.service.bean.wms.InventoryCenterBean;
import com.voyageone.service.bean.wms.ItemDetailsBean;
import com.voyageone.service.dao.wms.WmsBtInventoryCenterDao;
import com.voyageone.service.dao.wms.WmsBtInventoryCenterLogicDao;
import com.voyageone.service.dao.wms.WmsBtItemDetailsDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.wms.WmsBtInventoryCenterLogicModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * InventoryCenterLogicService
 *
 * @author chuanyu.liang 15/12/30
 * @version 2.0.0
 */
@Service
public class InventoryCenterLogicService extends BaseService {
    @Autowired
    private WmsBtInventoryCenterLogicDao inventoryCenterLogicDao;

    @Autowired
    private WmsBtItemDetailsDao wmsBtItemDetailsDao;

    @Autowired
    private WmsBtInventoryCenterDao wmsBtInventoryCenterDao;


    public List<WmsBtInventoryCenterLogicModel> getInventoryItemDetail(Map<String, Object> param) {
        return inventoryCenterLogicDao.selectItemDetail(param);
    }

    public Integer getLogicInventoryCnt(Map<String, Object> param) {
        return inventoryCenterLogicDao.selectLogicInventoryCnt(param);
    }

    public void updateCodeForMove(String channelId, String itemCodeOld, List<String> skuList, String itemCodeNew, String modifier) {
        inventoryCenterLogicDao.updateCodeForMove(channelId, itemCodeOld, skuList, itemCodeNew, modifier);
    }


    private static final ImmutableSet<String> CNHK = ImmutableSet.of("CN", "HK");
    /**
     * code级别 返回相关库存数据
     * @param code
     * @return
     */
    public WmsCodeStoreInvBean getCodeStockDetails(String channelId,String code) {
        List<ItemDetailsBean> items = wmsBtItemDetailsDao.selectByCode(channelId, code);
        List<StoreBean> channelStores = Stores.getChannelStoreList(channelId,false,false);//非虚拟仓库

        List<Long> storeIds = channelStores.stream().map(store -> store.getStore_id()).collect(Collectors.toList());
        Map<Long, String> idNameMap = new HashMap<>();
        Set<Long> inOwnStores = new HashSet<>();
        Set<Long> inNOwnStores = new HashSet<>();
        Set<Long> gbOwnStores = new HashSet<>();
        Set<Long> gbNOwnStores = new HashSet<>();

        //仓库处理
        for (final StoreBean store : channelStores) {
            long store_id = store.getStore_id();
            idNameMap.put(store_id, store.getStore_name());
            if ("1".equals(store.getIs_sale()) && "0".equals(store.getStore_type())) {//自营可销售
                if ("1".equals(store.getInventory_manager())
                        && CNHK.contains(store.getStore_area())) {//国内自营仓(我们管理库存)
                    inOwnStores.add(store_id);
                } else if ("1".equals(store.getInventory_manager())
                        && !CNHK.contains(store.getStore_area())) {  //国外自营仓(我们管理库存)
                    gbOwnStores.add(store_id);

                } else if ("1".equals(store.getInventory_manager())
                        && !CNHK.contains(store.getStore_area())) {//国外自营仓(不管理库存)
                    gbNOwnStores.add(store_id);
                }
            } else if ("1".equals(store.getIs_sale()) && !"0".equals(store.getStore_type()) && CNHK.contains(store.getStore_area())) { //国内第三方仓库
                inNOwnStores.add(store_id);
            }
        }
        //重新过滤
        storeIds= Lists.newArrayList(Iterators.concat(inOwnStores.iterator(), inNOwnStores.iterator(), gbOwnStores.iterator(), gbNOwnStores
                .iterator()));

        WmsCodeStoreInvBean result = new WmsCodeStoreInvBean();


        //设置库存
        for (final ItemDetailsBean skuItem : items) {
            WmsCodeStoreInvBean.StocksBean stock = new WmsCodeStoreInvBean.StocksBean();
            List<InventoryCenterBean> inventoryCenters = wmsBtInventoryCenterDao.selectSkuInventoryBy(channelId, storeIds,
                                                                                                      Arrays.asList(skuItem.getSku()));

            int  skuAllCount= inventoryCenters.stream().mapToInt(inv -> inv.getQty()).sum();
            stock.setBase(new WmsCodeStoreInvBean.StocksBean.BaseBean(skuItem.getSku(),skuItem.getSize(),"",skuAllCount));
            Map<String, Long> orderCounter = wmsBtInventoryCenterDao.countOrderInStoresBySku(channelId, skuItem.getSku(), storeIds);
            stock.setOrder(new WmsCodeStoreInvBean.StocksBean.OrderBean(orderCounter.getOrDefault("openCount",0L),
                                                                        orderCounter.getOrDefault("onHold",0L),
                                                                        orderCounter.getOrDefault("newOrder",0L)));

            stock.setInOwn(toInvShowMap(idNameMap, inventoryCenters, inOwnStores));
            stock.setInNOwn(toInvShowMap(idNameMap, inventoryCenters, inNOwnStores));
            stock.setGbOwn(toInvShowMap(idNameMap, inventoryCenters, gbOwnStores));
            stock.setGbNOwn(toInvShowMap(idNameMap, inventoryCenters,gbNOwnStores));
            result.getStocks().add(stock);
        }

        //设置header
        result.setHeader(new WmsCodeStoreInvBean.HeaderBean(transform(idNameMap,inOwnStores),
                                                            transform(idNameMap,inNOwnStores),
                                                            transform(idNameMap,gbOwnStores),
                                                            transform(idNameMap,gbNOwnStores)));
        return result;
    }

    /**
     * append total to first
     * @param idNameMap
     * @param storeIds
     * @return
     */
    public List<String> transform(final Map<Long, String> idNameMap, Set<Long> storeIds) {
        List<String> result = storeIds.stream().map(st ->idNameMap.getOrDefault(st,""))
                                                                  .collect(toList());
        result.add("total");
        return result;
    }

    /**
     * 将 inventory转换成     { "total": 7000, "KSS":1000, "LAK":1000 } 的形式
     * @param idNameMap
     * @param inventorys
     * @param filterStores
     * @return
     */
    private  static Map<String, Integer> toInvShowMap(final Map<Long, String> idNameMap, final List<InventoryCenterBean> inventorys,
                                                      final Set<Long> filterStores) {
        int total = 0;
        Map<String, Integer> result = new HashMap<>();
        for (final InventoryCenterBean inv : inventorys) {
            if (filterStores.contains(Long.valueOf(inv.getStore_id()))) {
                int qty = inv.getQty();
                result.put(idNameMap.get(Long.valueOf(inv.getStore_id())), qty);
                total += qty;
            }
        }
        result.put("total", total);
        return result;
    }
}
