package com.voyageone.service.impl.cms;

import com.google.common.base.Strings;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.dao.CartDao;
import com.voyageone.common.configs.dao.OrderChannelDao;
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
    CartDao cartDao;

    /**
     *
     * @param channelId
     * @param channelName
     * @param allowMinimall  -1 表示查询所有
     * @return
     */
    public List getChannelListBy(String channelId,String channelName,Integer allowMinimall) {

        List<OrderChannelBean> channelList = Channels.getChannelList();
        Predicate<OrderChannelBean> filterByCon=(bean)->{

            boolean flg = true;
            if(!Strings.isNullOrEmpty(channelId)){
                flg=flg && channelId.equalsIgnoreCase(bean.getOrder_channel_id());
            }
            if(!Strings.isNullOrEmpty(channelName) ){
                flg=flg && channelName.equalsIgnoreCase(bean.getName());
            }
            if(allowMinimall!=null && allowMinimall!=-1){
                flg = flg &&(allowMinimall== bean.getIs_usjoi());
            }
            return flg;

        };
        return channelList.stream().filter(filterByCon)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void updateById(OrderChannelBean bean) {
        channelDao.updateById(bean);
    }

    /**
     * 根据id查询Cart 注意如果为empty list 返回所有
     * @return
     */
    public List<CartBean> getCarts() {
        return cartDao.getAll();
    }
}
