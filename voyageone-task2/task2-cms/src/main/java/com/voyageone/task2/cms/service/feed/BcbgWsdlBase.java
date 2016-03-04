package com.voyageone.task2.cms.service.feed;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.bean.ImageBean;
import com.voyageone.task2.cms.bean.ItemBean;
import com.voyageone.task2.cms.bean.ModelBean;
import com.voyageone.task2.cms.bean.ProductBean;
import com.voyageone.task2.cms.dao.SuperFeedDao;
import com.voyageone.task2.cms.dao.feed.BcbgSuperFeedDao;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feed;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.voyageone.task2.cms.service.feed.BcbgWsdlConstants.*;

/**
 * Bcbg 远程调用共通基础
 * Created by Jonas on 10/20/15.
 */
abstract class BcbgWsdlBase extends BaseTaskService {

    private final static String APPAREL = "Apparel";

    private final static String ACCESSORIES = "Accessories";

    private final static BigDecimal TEN = new BigDecimal(10);

    @Autowired
    protected SuperFeedDao superFeedDao;

    @Autowired
    protected BcbgSuperFeedDao bcbgSuperFeedDao;

    private static ProductBean productColumns;

    private static ItemBean itemColumns;

    /**
     * 获取子系统
     */
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    /**
     * 获取是否禁用向 CMS 处理数据的 Web Service.
     * 从配置获取, 但未进行值类型检查
     */
    protected boolean isServiceDisabled() {
        String disabledValue = Feed.getVal1(channel, FeedEnums.Name.disable_service);
        if (StringUtils.isEmpty(disabledValue))
            return false;
        return Boolean.valueOf(disabledValue);
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        $info("该 Service 为子 Service. 无可用启动逻辑.");
    }

    protected abstract String getWhereUpdateFlg();

    protected List<ItemBean> getItems(ProductBean product) {

        ItemBean itemColumns = getItemColumns();

        String where = String.format("WHERE %s AND %s = '%s'", getWhereUpdateFlg(), itemColumns.getCode(), product.getP_code());

        List<ItemBean> itemBeans = superFeedDao.selectSuperfeedItem(where, itemColumns, table_feed_full);

        $info("取得 Item [ %s\t ] 个 [ Product: %s ]", itemBeans.size(), product.getUrl_key());

        return itemBeans;
    }

    private ItemBean getItemColumns() {

        if (itemColumns != null) return itemColumns;

        itemColumns = new ItemBean();

        itemColumns.setCode(Feed.getVal1(channel, FeedEnums.Name.item_code));
        itemColumns.setI_sku(Feed.getVal1(channel, FeedEnums.Name.item_i_sku));
        itemColumns.setI_itemcode(Feed.getVal1(channel, FeedEnums.Name.item_i_itemcode));
        itemColumns.setI_size(Feed.getVal1(channel, FeedEnums.Name.item_i_size));
        itemColumns.setI_client_sku(Feed.getVal1(channel, FeedEnums.Name.item_i_client_sku));
        itemColumns.setI_barcode(Feed.getVal1(channel, FeedEnums.Name.item_i_barcode));
        return itemColumns;
    }

    protected List<ImageBean> getImages(ProductBean product) {

        String where = String.format("WHERE %s AND %s = '%s'", getWhereUpdateFlg(), Feed.getVal1(channel, FeedEnums.Name.product_p_code), product.getP_code());

        List<String> imageArrs = superFeedDao.selectSuperfeedImage(
                where,
                Feed.getVal1(channel, FeedEnums.Name.images),
                // 组合 Image 的表部分和Join部分
                String.format("%s JOIN %s ON %s", table_feed_full, table_style_full, on_product));

        $info("取得 Image 路径组合 [ %s ] 个 [ Product: %s ]", imageArrs.size(), product.getUrl_key());

        List<ImageBean> imageBeans = new ArrayList<>();

        String separator = Feed.getVal1(channel, FeedEnums.Name.image_split);

        for (String imageArr: imageArrs) {

            String[] images = imageArr.split(separator);

            for (String image: images) {

                ImageBean imageBean = new ImageBean();

                imageBean.setImage_type("1");
                imageBean.setImage(String.valueOf(imageBeans.size() + 1));
                imageBean.setImage_url(image);
                imageBean.setImage_name(image.substring(image.lastIndexOf("/") + 1, image.lastIndexOf(".")));
                imageBean.setDisplay_order("0");

                imageBeans.add(imageBean);
            }
        }

        return imageBeans;
    }

    protected List<ProductBean> getProducts() {

        $info("准备批量获取 Product");

        // 条件则根据类目筛选
        String where = String.format("WHERE %s", getWhereUpdateFlg());

        List<ProductBean> productBeans = getProductBeans(where);

        $info("取得 Product [ %s ] 个", productBeans.size());
        $info("已为 Product 补全 Item 和 Image");

        return productBeans;
    }

    /**
     * 获取某 Model 下的所有 Product
     */
    protected List<ProductBean> getProducts(ModelBean model) {

        $info("准备批量获取 Model [ %s ] 的 Product", model.getUrl_key());

        // 条件则根据类目筛选
        String where = String.format("WHERE %s AND %s = '%s'", getWhereUpdateFlg(),
                getProductColumns().getModel_url_key(), fix(model.getUrl_key()));

        List<ProductBean> productBeans = getProductBeans(where);

        $info("取得 Model [ %s ] 的 Product [ %s ] 个", model.getUrl_key(), productBeans.size());

        return productBeans;
    }

    private List<ProductBean> getProductBeans(String where) {

        ProductBean productColumns = getProductColumns();

        List<ProductBean> productBeans = superFeedDao.selectSuperfeedProduct(
                String.format("%s GROUP BY %s", where, grouping_product),
                productColumns,
                // 组合 Product 的表部分和Join部分
                String.format("%s JOIN %s ON %s", table_feed_full, table_style_full, on_product));

        $info("取得 Product [ %s ] 个 [ 条件: %s ]", productBeans.size(), where);

        for (ProductBean productBean: productBeans) {
            productBean.setItembeans(getItems(productBean));
            productBean.setImages(getImages(productBean));
            // 转换 Url Key 格式,这里顺序同之前的get类方法一样的原理
            productBean.setUrl_key(clearSpecialSymbol(productBean.getUrl_key()));
            productBean.setCategory_url_key(clearSpecialSymbol(productBean.getCategory_url_key()));
            productBean.setModel_url_key(clearSpecialSymbol(productBean.getModel_url_key()));

            // 以下进行价格的转换计算
            calePrice(productBean);
        }

        return productBeans;
    }

    protected void calePrice(ProductBean productBean) {
        BigDecimal msrp = new BigDecimal(productBean.getP_msrp());
        BigDecimal price = new BigDecimal(productBean.getPs_price());

        BigDecimal duty;

        switch (productBean.getP_product_type()) {
            case ACCESSORIES:
                duty = other_duty;
                break;
            case APPAREL:
                duty = apparels_duty;
                break;
            default:
                throw new BusinessException("没有找到 MATKL_ATT1 ! 无法计算价格 !");
        }

        // 先计算 rmb 单位的 msrp (已格式化)
        BigDecimal iMsrp = toRmb(msrp, duty);
        // 计算 usd 单位下, msrp 和 price 的比例
        BigDecimal discount = price.divide(msrp, 2, BigDecimal.ROUND_HALF_DOWN);

        productBean.setCps_cn_price(iMsrp.toString());
        productBean.setCps_cn_price_rmb(iMsrp.multiply(discount).setScale(0, BigDecimal.ROUND_DOWN).toString());
        productBean.setCps_cn_price_final_rmb(iMsrp.multiply(discount).setScale(0, BigDecimal.ROUND_DOWN).toString());

        // 计算完成后去除临时使用的 type 内容
        productBean.setP_product_type(Constants.EmptyString);
    }

    private BigDecimal toRmb(BigDecimal bigDecimal, BigDecimal duty) {
        return bigDecimal
                .multiply(fixed_exchange_rate)
                .divide(duty, BigDecimal.ROUND_DOWN)
                .setScale(0, BigDecimal.ROUND_DOWN)
                .divide(TEN, BigDecimal.ROUND_DOWN)
                .multiply(TEN);
    }

    protected String clearSpecialSymbol(String name) {
        return special_symbol.matcher(name.toLowerCase()).replaceAll(Constants.EmptyString).replace(" ", "-");
    }

    /**
     * 获取商品级别的列定义
     */
    protected ProductBean getProductColumns() {

        if (productColumns != null) return productColumns;

        productColumns = new ProductBean();

        // 为每个字段指定其映射到的数据表的列.
        // 在后面的查询,自动从数据表填充值.
        productColumns.setUrl_key(Feed.getVal1(channel, FeedEnums.Name.product_url_key));
        productColumns.setModel_url_key(Feed.getVal1(channel, FeedEnums.Name.product_model_url_key));
        productColumns.setCategory_url_key(Feed.getVal1(channel, FeedEnums.Name.product_category_url_key));
        productColumns.setP_code(Feed.getVal1(channel, FeedEnums.Name.product_p_code));
        productColumns.setP_name(Feed.getVal1(channel, FeedEnums.Name.product_p_name));
        productColumns.setP_color(Feed.getVal1(channel, FeedEnums.Name.product_p_color));
        productColumns.setP_made_in_country(Feed.getVal1(channel, FeedEnums.Name.product_p_made_in_country));
        productColumns.setPe_short_description(Feed.getVal1(channel, FeedEnums.Name.product_pe_short_description));
        productColumns.setPe_long_description(Feed.getVal1(channel, FeedEnums.Name.product_pe_long_description));

        // 使用 type 字段临时存储从 master 获取的类目附加属性 MATKL_ATT1. 用来进行后续的价格计算
        productColumns.setP_product_type(Feed.getVal1(channel, FeedEnums.Name.product_type));

        // 一下全部为价格
        productColumns.setP_msrp(Feed.getVal1(channel, FeedEnums.Name.product_p_msrp));
        productColumns.setPs_price(Feed.getVal1(channel, FeedEnums.Name.product_ps_price));
        productColumns.setCps_cn_price_rmb(Feed.getVal1(channel, FeedEnums.Name.product_cps_cn_price_rmb));
        productColumns.setCps_cn_price(Feed.getVal1(channel, FeedEnums.Name.product_cps_cn_price));
        productColumns.setCps_cn_price_final_rmb(Feed.getVal1(channel, FeedEnums.Name.product_cps_cn_price_final_rmb));

        return productColumns;
    }

    protected String fix(String urlKey) {
        return urlKey.replace("'", "\\'");
    }
}
