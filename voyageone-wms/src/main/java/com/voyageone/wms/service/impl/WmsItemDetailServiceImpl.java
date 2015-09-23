package com.voyageone.wms.service.impl;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.MessageConstants.ComMsg;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.wms.WmsMsgConstants;
import com.voyageone.wms.WmsMsgConstants.NewItemMsg;
import com.voyageone.wms.dao.ItemDao;
import com.voyageone.wms.dao.external.WmsProductDao;
import com.voyageone.wms.dao.external.WmsSizeDao;
import com.voyageone.wms.modelbean.ItemDetailBean;
import com.voyageone.wms.modelbean.external.WmsProductBean;
import com.voyageone.wms.modelbean.external.WmsProductTypeBean;
import com.voyageone.wms.service.WmsItemDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * wms_bt_item_details 对应的 service
 * Created by Tester on 5/21/2015.
 *
 * @author Jonas
 */
@Service
public class WmsItemDetailServiceImpl implements WmsItemDetailService {
    @Autowired
    private ItemDao itemDao;

    @Autowired
    private WmsProductDao productDao;

    @Autowired
    private WmsSizeDao sizeDao;

    /**
     * 使用 Code 获取渠道内的 Product 和 Item Detail
     *
     * @param code             商品 Code
     * @param order_channel_id 渠道 ID
     * @return Map: itemDetails & product
     */
    @Override
    public Map<String, Object> getProduct(String code, String order_channel_id) {
        // 先查找商品
        WmsProductBean productBean = productDao.getProductByCode(code, order_channel_id);

        // 如果商品没有，返回空，等待前台请求创建
        if (productBean == null) return null;

        List<ItemDetailBean> itemDetailBeans = itemDao.selectItemDetails(code, order_channel_id);

        Map<String, Object> result = new HashMap<>();

        result.put("product", productBean);
        result.put("itemDetails", itemDetailBeans);

        return result;
    }

    /**
     * 保存或创建 Product
     *
     * @param productBean 上传的 WmsProductBean
     * @param user        当前操作人
     * @return 数据库中的 WmsProductBean
     */
    @Override
    public WmsProductBean saveProduct(WmsProductBean productBean, UserSessionBean user) {
        // 先验证输入内容
        validProductBean(productBean);

        // 再验证一些数据库的内容
        validProductBeanByDb(productBean);

        // 验证是否已存在
        WmsProductBean productBeanInDb = getProductByCode(productBean);

        return productBeanInDb == null
                ? insertProduct(productBean, user)
                : updateProduct(productBean, user);
    }

    @Override
    public List<String> getAllSize(String order_channel_id, int product_type_id) {
        // 验证渠道值
        Channel channel = Channel.valueOfId(order_channel_id);

        if (channel == null)
            throw new BusinessException(ComMsg.NOT_FOUND_CHANNEL);

        return sizeDao.getAllSize(order_channel_id, product_type_id);
    }

    @Override
    public ItemDetailBean saveItemDetail(ItemDetailBean itemDetailBean, UserSessionBean user) {
        // 先验证输入内容
        validItemDetailBean(itemDetailBean);

        // 输入内容和数据库中的内容进行，合法验证
        validItemDetailBeanByDb(itemDetailBean);

        // 验证是否已存在
        ItemDetailBean itemDetailBeanInDb = getItemDetail(itemDetailBean);

        return itemDetailBeanInDb == null
                ? insertItemDetail(itemDetailBean, user)
                : updateItemDetailBarcode(itemDetailBean, user);
    }

    @Override
    public List<WmsProductTypeBean> getAllProductTypes(String order_channel_id) {
        // 验证渠道
        Channel channel = Channel.valueOfId(order_channel_id);

        if (channel == null)
            throw new BusinessException(ComMsg.NOT_FOUND_CHANNEL);

        return productDao.getAllProductTypes(order_channel_id);
    }

    private ItemDetailBean updateItemDetailBarcode(ItemDetailBean itemDetailBean, UserSessionBean user) {

        itemDetailBean.setModifier(user.getUserName());

        int count = itemDao.updateItemDetailBarcode(itemDetailBean);

        if (count < 1)
            throw new BusinessException(ComMsg.UPDATE_BY_OTHER);

        return getItemDetail(itemDetailBean);
    }

    private ItemDetailBean insertItemDetail(ItemDetailBean itemDetailBean, UserSessionBean user) {

        itemDetailBean.setCreater(user.getUserName());
        itemDetailBean.setModifier(user.getUserName());
        itemDetailBean.setActive(true);

        int count = itemDao.insertItemDetail(itemDetailBean);

        if (count < 1)
            throw new BusinessException(ComMsg.UPDATE_BY_OTHER);

        return getItemDetail(itemDetailBean);
    }

    /**
     * 尝试根据渠道，尺码和 Code 查询相同的值
     */
    private ItemDetailBean getItemDetail(ItemDetailBean itemDetailBean) {
        return itemDao.getItemDetail(
                itemDetailBean.getOrder_channel_id(),
                itemDetailBean.getItemcode(),
                itemDetailBean.getSize()
        );
    }

    /**
     * 验证由用户输入的值
     */
    private void validItemDetailBean(ItemDetailBean itemDetailBean) {

        if (StringUtils.isEmpty(itemDetailBean.getOrder_channel_id())
                || StringUtils.isEmpty(itemDetailBean.getItemcode())
                || StringUtils.isEmpty(itemDetailBean.getSize())
                || StringUtils.isEmpty(itemDetailBean.getBarcode())
                || itemDetailBean.getBarcode().length() > 20) {
            throw new BusinessException(NewItemMsg.ITEM_DETAIL_INVALID);
        }
    }

    /**
     * 验证所关联的商品，在 cms 中是否存在，验证渠道是否存在，验证 upc 码是否已经与其他商品关联
     */
    private void validItemDetailBeanByDb(ItemDetailBean itemDetailBean) {

        Channel channel = Channel.valueOfId(itemDetailBean.getOrder_channel_id());

        if (channel == null)
            throw new BusinessException(ComMsg.NOT_FOUND_CHANNEL);

        WmsProductBean productBean = productDao.getProductByCode(itemDetailBean.getItemcode(), channel.getId());

        if (productBean == null)
            throw new BusinessException(NewItemMsg.NOT_FOUND_TARGET_PRODUCT, itemDetailBean.getItemcode());

        String skuInUse = itemDao.getSku(channel.getId(), itemDetailBean.getBarcode());

        if (!StringUtils.isEmpty(skuInUse))
            throw new BusinessException(NewItemMsg.BARCODE_IN_USE, itemDetailBean.getBarcode());
    }

    private WmsProductBean insertProduct(WmsProductBean productBean, UserSessionBean user) {
        productBean.setCreater(user.getUserName());
        productBean.setModifier(user.getUserName());

        int count = productDao.insertProduct(productBean);

        if (count < 1)
            throw new BusinessException(ComMsg.UPDATE_BY_OTHER);

        return getProductByCode(productBean);
    }

    private WmsProductBean updateProduct(WmsProductBean productBean, UserSessionBean user) {
        productBean.setModifier(user.getUserName());

        int count = productDao.updateProduct(productBean);

        if (count < 1)
            throw new BusinessException(ComMsg.UPDATE_BY_OTHER);

        return getProductByCode(productBean);
    }

    private WmsProductBean getProductByCode(WmsProductBean productBean) {
        return productDao.getProductByCode(productBean.getCode(), productBean.getChannel_id());
    }

    /**
     * 验证由用户输入的值
     */
    private void validProductBean(WmsProductBean productBean) {

        if (StringUtils.isEmpty(productBean.getChannel_id())
                || StringUtils.isEmpty(productBean.getCode())
                || productBean.getCode().length() > 25
                || StringUtils.isEmpty(productBean.getName())
                || StringUtils.isEmpty(productBean.getColor())
                || productBean.getProduct_type_id() < 1) {
            throw new BusinessException(NewItemMsg.PRODUCT_INVALID);
        }
    }

    /**
     * 验证基于数据库值
     */
    private void validProductBeanByDb(WmsProductBean productBean) {
        Channel channel = Channel.valueOfId(productBean.getChannel_id());

        if (channel == null)
            throw new BusinessException(ComMsg.NOT_FOUND_CHANNEL);

        if (!productDao.hasProductType(productBean.getProduct_type_id(), channel.getId()))
            throw new BusinessException(NewItemMsg.NOT_FOUND_PRODUCT_TYPE);
    }
}