package com.voyageone.web2.cms.views.channel;


import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jefff.duan on 2015/5/26.
 * @version 2.0.0
 */
@Service
public class CmsChannelService {

    @Autowired
    ChannelService channelService;

    public void save(OrderChannelBean bean) {
        check(bean);
        channelService.save(bean);
    }

    public void update(OrderChannelBean bean) {
        check(bean);
        channelService.updateById(bean);
    }

    public void check(OrderChannelBean bean) {

        if (StringUtils.isEmpty(bean.getOrder_channel_id())
                || StringUtils.isEmpty(bean.getCompany_id())
                || StringUtils.isEmpty(bean.getName())
                || StringUtils.isEmpty(bean.getSend_name())
                || StringUtils.isEmpty(bean.getScrect_key())
                || StringUtils.isEmpty(bean.getSession_key())) {
            // 请输入必填项目
            throw new BusinessException("7000080");
        }

        if (!StringUtils.isDigit(bean.getOrder_channel_id()) || bean.getOrder_channel_id().length() != 3) {
            throw new BusinessException("Channel ID 必须是3位数字");
        }

        if (bean.getName().getBytes().length > 20) {
            throw new BusinessException("Name不能超过20");
        }

        if (!StringUtils.isEmpty(bean.getFull_name()) && bean.getFull_name().getBytes().length > 50) {
            throw new BusinessException("Full Name不能超过50");
        }

        if (!StringUtils.isEmpty(bean.getImg_url()) && bean.getImg_url().getBytes().length > 250) {
            throw new BusinessException("Image URL不能超过250");
        }

        if (bean.getSend_name().getBytes().length > 20) {
            throw new BusinessException("Send Name不能超过20");
        }

        if (!StringUtils.isEmpty(bean.getSend_address()) && bean.getSend_address().getBytes().length > 100) {
            throw new BusinessException("Send Address不能超过100");
        }

        if (!StringUtils.isEmpty(bean.getSend_tel()) && bean.getSend_tel().getBytes().length > 20) {
            throw new BusinessException("Send Tel不能超过20");
        }

        if (!StringUtils.isEmpty(bean.getSend_zip()) && bean.getSend_zip().getBytes().length > 10) {
            throw new BusinessException("Send Zip不能超过10");
        }
    }

}
