package com.voyageone.service.impl.cms;

import com.google.common.base.Strings;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Carts;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.dao.OrderChannelDao;
import com.voyageone.service.dao.cms.mongo.CmsBtConfigHistoryDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsBtConfigHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService extends BaseService {

    @Autowired
    OrderChannelDao channelDao;

    @Autowired
    CmsBtConfigHistoryDao historyDao;

    /**
     * @param channelId String
     * @param channelName String
     * @param allowMinimall -1 表示查询所有
     * @param active        0 表示查询删除过的.1表示查询未删除的.null的话查询所有
     * @return List<OrderChannelBean>
     */
    public List<OrderChannelBean> getChannelListBy(String channelId, String channelName, Integer allowMinimall, String active) {

        OrderChannelBean bean = new OrderChannelBean();
        if (!Strings.isNullOrEmpty(channelId)) {
            bean.setOrder_channel_id(channelId);
        }
        if (!Strings.isNullOrEmpty(channelName)) {
            bean.setName(channelName);
        }
        if (allowMinimall != null && allowMinimall != -1) {
            bean.setIs_usjoi(allowMinimall);
        }
        if (!Strings.isNullOrEmpty(active)) {
            bean.setActive(active);
        }
        return channelDao.getList(bean);
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
     * @return <CartBean>
     */
    public List<CartBean> getCarts() {
        return Carts.getAllCartList();
    }

    public void save(OrderChannelBean bean) {
        try {
            OrderChannelBean old = channelDao.getOne(bean.getOrder_channel_id());
            if (old != null) {
                if ("1".equals(old.getActive())) {
                    // 主键存在,并且非逻辑删除的情况
                    throw new BusinessException("channel id is not unique,please rewrite a new id");
                } else {
                    // 主键存在,但是逻辑删除的情况
                    bean.setActive("1");
                    channelDao.updateById(bean);
                }
            } else {
                // 主键不存在的情况（包括逻辑删除也不存在）
                channelDao.insertChannel(bean);
            }
            Channels.invalidate();
            CmsBtConfigHistory<OrderChannelBean> history = CmsBtConfigHistory.build(null, bean, "CHANNEL ADD", bean.getModifier());
            historyDao.insert(history);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("insert channel error with message:"+e.getMessage(), e);
        }
    }
}
