package com.voyageone.batch.cms.service.feed;

import com.voyageone.batch.cms.bean.*;

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

    private List<List<CategoryBean>> categoriesList = new ArrayList<>();

    private Map<String, ModelBean> modelBeanMap = new HashMap<>();

    private Map<String, ProductBean> productBeanMap = new HashMap<>();

    public List<List<CategoryBean>> getCategoriesList() {
        return categoriesList;
    }

    public void put(SuperFeedGiltBean feedGiltBean, SuperFeedGiltBean oldSku) {

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
        productBean.setPs_price(feedGiltBean.getPrices_sale_value());
        productBean.setItembeans(new ArrayList<>());
        productBean.setImages(getImages(feedGiltBean));
        productBean.setP_image_item_count(String.valueOf(productBean.getImages().size()));

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

        if (curr != null)
            curr.setModelbeans(new ArrayList<>());

        return curr;
    }
}
