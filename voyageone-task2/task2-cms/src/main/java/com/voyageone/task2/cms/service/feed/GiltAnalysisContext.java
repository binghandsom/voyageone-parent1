package com.voyageone.task2.cms.service.feed;

import com.voyageone.task2.cms.CmsConstants;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feed;
import com.voyageone.task2.cms.bean.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jonas, 2/3/16.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltAnalysisContext {

    private Map<String, CategoryBean> categoryBeanMap = new HashMap<>();

    private List<ProductsFeedUpdate> feedUpdateList = new ArrayList<>();

    private Map<String, ProductsFeedUpdate> feedUpdateMap = new HashMap<>();

    private List<List<CategoryBean>> categoriesList = new ArrayList<>();

    private Map<String, ModelBean> modelBeanMap = new HashMap<>();

    private Map<String, ProductBean> productBeanMap = new HashMap<>();

    private boolean isSyncFinalRmb() {
        String syncFinalRmbValue = Feed.getVal1(ChannelConfigEnums.Channel.GILT, FeedEnums.Name.sync_final_rmb);
        if (StringUtils.isEmpty(syncFinalRmbValue))
            return true;
        return Boolean.valueOf(syncFinalRmbValue);
    }

    public List<List<CategoryBean>> getCategoriesList() {
        return categoriesList;
    }

    public List<ProductsFeedUpdate> getFeedUpdateList() {
        return feedUpdateList;
    }

    public void put(SuperFeedGiltBean feedGiltBean) {

        ProductBean productBean = getProduct(feedGiltBean);

        ItemBean itemBean = new ItemBean();

        itemBean.setCode(feedGiltBean.getProduct_look_id());
        itemBean.setI_sku(feedGiltBean.getId());
        itemBean.setI_itemcode(feedGiltBean.getProduct_look_id());
        itemBean.setI_size(feedGiltBean.getAttributes_size_value());
        itemBean.setI_barcode(feedGiltBean.getProduct_codes_first());
        itemBean.setI_client_sku(feedGiltBean.getId());

        productBean.getItembeans().add(itemBean);
    }

    public void put(SuperFeedGiltBean newItem, SuperFeedGiltBean oldItem) {

        if (feedUpdateMap.containsKey(newItem.getProduct_look_id()))
            return;

        Map<String, String> updateFields = getNewestFields(newItem, oldItem);

        if (updateFields == null || updateFields.size() < 1)
            return;

        ProductsFeedUpdate feedUpdate = new ProductsFeedUpdate();

        feedUpdate.setChannel_id(ChannelConfigEnums.Channel.GILT.getId());
        feedUpdate.setCode(newItem.getProduct_look_id());
        feedUpdate.setProduct_url_key(newItem.getCategories_key() + "-" + newItem.getProduct_look_id());
        feedUpdate.setUpdatefields(updateFields);

        feedUpdateList.add(feedUpdate);
        feedUpdateMap.put(newItem.getProduct_look_id(), feedUpdate);
    }

    private Map<String, String> getNewestFields(SuperFeedGiltBean newItem, SuperFeedGiltBean oldItem) {

        if (oldItem == null)
            return getFocusUpdateFields(newItem);

        Map<String, String> updateFields = new HashMap<>();

        /*
         * 价格部分后续修改时请注意
         * 因为 DB 是 SKU 级别数据, 所以为了 DISTINCT 时减小误差几率
         * 查询时, 尽量少的 SELECT 字段. 如果需要使用新价格字段, 需要同时修改 SQL
         */

        if (!newItem.getPrices_retail_value().equals(oldItem.getPrices_retail_value())) {
            updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_MSRP, newItem.getPrices_retail_value());
            updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_CN_PRICE, newItem.getPrices_retail_value());
        }

        if (!newItem.getPrices_sale_value().equals(oldItem.getPrices_sale_value())) {
            updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_CN_PRICE_RMB, newItem.getPrices_sale_value());
        }

        if (!newItem.getPrices_cost_value().equals(oldItem.getPrices_cost_value())) {
            updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_PRICE, newItem.getPrices_cost_value());
//            updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_CN_PRICE, newItem.getPrices_sale_value());
//            updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_CN_PRICE_RMB, newItem.getPrices_sale_value());
            if (isSyncFinalRmb())
                updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_CN_PRICE_FINAL_RMB, newItem.getPrices_cost_value());
        }

        if (!newItem.getDescription().equals(oldItem.getDescription())) {
            updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_LONG_DESCRIPTION, newItem.getDescription());
        }

        String separator = CmsConstants.FEED_IO_UPDATEFIELDS_IMAGE_SPLIT;

        if (!newItem.getImages_url().equals(oldItem.getImages_url())) {
            updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_IMAGE_URL, newItem.getImages_url().replaceAll(",", separator));
        }

        return updateFields;
    }

    private Map<String, String> getFocusUpdateFields(SuperFeedGiltBean newItem) {

        /*
         * 价格部分后续修改时请注意
         * 因为 DB 是 SKU 级别数据, 所以为了 DISTINCT 时减小误差几率
         * 查询时, 尽量少的 SELECT 字段. 如果需要使用新价格字段, 需要同时修改 SQL
         */

        Map<String, String> updateFields = new HashMap<>();
        String separator = CmsConstants.FEED_IO_UPDATEFIELDS_IMAGE_SPLIT;

        updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_MSRP, newItem.getPrices_retail_value());
        updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_PRICE, newItem.getPrices_cost_value());
        updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_CN_PRICE, newItem.getPrices_retail_value());
        updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_CN_PRICE_RMB, newItem.getPrices_sale_value());
        updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_CN_PRICE_FINAL_RMB, newItem.getPrices_cost_value());
        updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_LONG_DESCRIPTION, newItem.getDescription());
        updateFields.put(CmsConstants.FEED_IO_UPDATEFIELDS_IMAGE_URL, newItem.getImages_url().replaceAll(",", separator));

        return updateFields;
    }

    private ProductBean getProduct(SuperFeedGiltBean feedGiltBean) {

        ModelBean modelBean = getModel(feedGiltBean);

        String url_key = modelBean.getCategory_url_key() + "-" + feedGiltBean.getProduct_look_id();

        if (productBeanMap.containsKey(url_key))
            return productBeanMap.get(url_key);

        ProductBean productBean = new ProductBean();

        productBean.setUrl_key(url_key);
        productBean.setModel_url_key(modelBean.getUrl_key());
        productBean.setCategory_url_key(modelBean.getCategory_url_key());
        productBean.setP_code(feedGiltBean.getProduct_look_id());
        productBean.setP_name(feedGiltBean.getName());
//        productBean.setP_product_type(feedGiltBean.getAttributes_size_type());
        productBean.setPe_long_description(feedGiltBean.getDescription());
        productBean.setP_color(feedGiltBean.getAttributes_color_name());
        productBean.setP_msrp(feedGiltBean.getPrices_retail_value());
        productBean.setP_made_in_country(feedGiltBean.getCountry_code());
        productBean.setP_material_fabric_1(feedGiltBean.getAttributes_material_value());
        productBean.setPs_price(feedGiltBean.getPrices_cost_value());
        productBean.setItembeans(new ArrayList<>());
        productBean.setImages(getImages(feedGiltBean));
        productBean.setP_image_item_count(String.valueOf(productBean.getImages().size()));
        productBean.setCps_cn_price(feedGiltBean.getPrices_retail_value());
        productBean.setCps_cn_price_rmb(feedGiltBean.getPrices_sale_value());
        productBean.setCps_cn_price_final_rmb(feedGiltBean.getPrices_cost_value());

        productBeanMap.put(url_key, productBean);
        modelBean.getProductbeans().add(productBean);
        return productBean;
    }

    private List<ImageBean> getImages(SuperFeedGiltBean feedGiltBean) {

        String[] urls = feedGiltBean.getImages_url().split(",");

        List<ImageBean> imageBeanList = new ArrayList<>();

        for (int i = 0; i < urls.length; i++) {

            String image = urls[i];

            ImageBean imageBean = new ImageBean();

            imageBean.setImage_type("1");
            imageBean.setImage(String.valueOf(i + 1));
            imageBean.setImage_url(image);
            imageBean.setImage_name(feedGiltBean.getId() + "-" + (i + 1));
            imageBean.setDisplay_order("0");

            imageBeanList.add(imageBean);
        }

        return imageBeanList;
    }

    private ModelBean getModel(SuperFeedGiltBean feedGiltBean) {

        CategoryBean categoryBean = getCategory(feedGiltBean);

        String url_key = categoryBean.getUrl_key() + "-" + feedGiltBean.getProduct_id();

        if (modelBeanMap.containsKey(url_key))
            return modelBeanMap.get(url_key);

        ModelBean modelBean = new ModelBean();

        modelBean.setUrl_key(url_key);
        modelBean.setCategory_url_key(categoryBean.getUrl_key());
        modelBean.setM_product_type(feedGiltBean.getAttributes_size_type());
        modelBean.setM_brand(feedGiltBean.getBrand_name());
        modelBean.setM_model(feedGiltBean.getProduct_id());
        modelBean.setM_name(feedGiltBean.getName());
        modelBean.setM_size_type(feedGiltBean.getAttributes_size_type());
        modelBean.setM_long_description(feedGiltBean.getDescription());
        modelBean.setProductbeans(new ArrayList<>());

        categoryBean.getModelbeans().add(modelBean);
        modelBeanMap.put(url_key, modelBean);
        return modelBean;
    }

    private CategoryBean getCategory(SuperFeedGiltBean feedGiltBean) {

        // 先拿完整的 Key
        // 完整的 Key 就是叶子 Key
        String categoryUrlKey = feedGiltBean.getCategories_key();

        // 检查叶子类目是否已经创建
        // 有就返回
        if (categoryBeanMap.containsKey(categoryUrlKey))
            return categoryBeanMap.get(categoryUrlKey);

        // 没有就要构建
        // 这里注意, 每个 Category List 都是完整的类目树
        // 所以每当叶子类目没有, 就要完整构建一个类目树, 也就是 Category List
        // 也就是 categoriesList 的 size 等于叶子类目的数量

        String[] keys = categoryUrlKey.split("-");

        List<CategoryBean> categoryBeanList = new ArrayList<>(keys.length);

        CategoryBean parent = null, curr = null;

        for (String key : keys) {

            String url_key;

            if (parent != null)
                url_key = parent.getUrl_key() + "-" + key;
            else
                url_key = key;

            // 如果有则直接使用, 并进入下一级
            if (categoryBeanMap.containsKey(url_key)) {
                curr = categoryBeanMap.get(url_key);
                categoryBeanList.add(curr);
                parent = curr;
                continue;
            }

            curr = new CategoryBean();
            curr.setC_name(key);
            curr.setC_header_title(key);
            curr.setUrl_key(url_key);
            curr.setModelbeans(new ArrayList<>());

            if (parent != null)
                curr.setParent_url_key(parent.getUrl_key());

            // 没有的话, 要在两个集合里都补充相应记录
            categoryBeanMap.put(curr.getUrl_key(), curr);
            categoryBeanList.add(curr);
            // 切换下一级
            parent = curr;
        }

        if (categoryBeanList.size() > 0)
            categoriesList.add(categoryBeanList);

        return curr;
    }

    public boolean isNoNeedUpdate() {
        return feedUpdateList.isEmpty();
    }
}
