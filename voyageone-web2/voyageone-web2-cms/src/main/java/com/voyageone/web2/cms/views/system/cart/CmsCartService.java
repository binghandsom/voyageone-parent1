package com.voyageone.web2.cms.views.system.cart;


import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @author jefff.duan on 2015/5/26.
 * @version 2.0.0
 */
@Service
public class CmsCartService {

    @Autowired
    CartService cartService;

    public void save(CartBean bean) {
        check(bean);
        cartService.save(bean);
    }

    public void saveOrUpdate(CartBean bean) {
        check(bean);
        cartService.saveOrUpdate(bean);
    }

    public void check(CartBean bean) {

        if (StringUtils.isEmpty(bean.getCart_id())
                || StringUtils.isEmpty(bean.getName())
                || StringUtils.isEmpty(bean.getShort_name())
                || StringUtils.isEmpty(bean.getPlatform_id())
                || StringUtils.isEmpty(bean.getCart_type())) {
            // 请输入必填项目
            throw new BusinessException("7000080");
        }

        if (!StringUtils.isDigit(bean.getCart_id())) {
            throw new BusinessException("Cart ID 必须是数字类型");
        }

        if (bean.getName().getBytes().length > 25) {
            throw new BusinessException("名称不能超过25");
        }

        if (bean.getShort_name().getBytes().length > 10) {
            throw new BusinessException("短名称不能超过10");
        }
        // 数据库为非null字段
        if (bean.getDescription() == null) {
            bean.setDescription("");
        }
        if (!StringUtils.isEmpty(bean.getDescription()) && bean.getDescription().getBytes().length > 50) {
            throw new BusinessException("描述不能超过50");
        }
    }

}
