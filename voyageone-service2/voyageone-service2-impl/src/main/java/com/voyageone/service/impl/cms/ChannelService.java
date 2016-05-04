package com.voyageone.service.impl.cms;

import com.google.common.base.Strings;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Carts;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.dao.OrderChannelDao;
import com.voyageone.common.configs.dao.ShopDao;
import com.voyageone.service.dao.cms.mongo.CmsBtConfigHistoryDao;
import com.voyageone.service.model.cms.mongo.CmsBtConfigHistory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ChannelService {

    @Resource
    OrderChannelDao channelDao;

    @Resource
    ShopDao cartDao;

    @Resource
    CmsBtConfigHistoryDao historyDao;

    /**
     * @param channelId
     * @param channelName
     * @param allowMinimall -1 表示查询所有
     * @param active        0 表示查询删除过的.1表示查询未删除的.null的话查询所有
     * @return
     */
    public List getChannelListBy(String channelId, String channelName, Integer allowMinimall, String active) {

        List<OrderChannelBean> channelList = channelDao.getAll();
        Predicate<OrderChannelBean> filterByCon = (bean) -> {

            boolean flg = true;
            if (!Strings.isNullOrEmpty(channelId)) {
                flg = flg && channelId.equalsIgnoreCase(bean.getOrder_channel_id());
            }
            if (!Strings.isNullOrEmpty(channelName)) {
                flg = flg && channelName.equalsIgnoreCase(bean.getName());
            }
            if (allowMinimall != null && allowMinimall != -1) {
                flg = flg && (allowMinimall == bean.getIs_usjoi());
            }
            if (!Strings.isNullOrEmpty(active)) {
                flg = flg && (active.equals(bean.getActive()));
            }
            return flg;

        };
        return channelList.stream().filter(filterByCon)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void updateById(OrderChannelBean bean) {
        OrderChannelBean originalBean = Channels.getChannel(bean.getOrder_channel_id());
        channelDao.updateById(bean);
        Channels.invalidate();

        CmsBtConfigHistory<OrderChannelBean> history = CmsBtConfigHistory.build(originalBean, bean, "CHANNEL MODIFY", bean.getModifier());
        historyDao.insert(history);
    }

    /**
     * 根据id查询Cart 注意如果为empty list 返回所有
     *
     * @return
     */
    public List<CartBean> getCarts() {
        return Carts.getAllCartList();
    }

    public void save(OrderChannelBean bean) {
        try {
            channelDao.insertChannel(bean);
            Channels.invalidate();
            CmsBtConfigHistory<OrderChannelBean> history = CmsBtConfigHistory.build(null, bean, "CHANNEL ADD", bean.getModifier());
            historyDao.insert(history);
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                throw new BusinessException("channel id is not unique,please rewrite a new id",e);
            }
            throw new BusinessException("insert channel error with message:"+e.getMessage(), e);
        }
    }
}
