package com.voyageone.batch.cms.service.feed;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.bean.ImageBean;
import com.voyageone.batch.cms.bean.ItemBean;
import com.voyageone.batch.cms.bean.ModelBean;
import com.voyageone.batch.cms.bean.ProductBean;
import com.voyageone.batch.cms.dao.SuperFeedDao;
import com.voyageone.batch.cms.dao.feed.BcbgSuperFeedDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Bcbg 远程调用共通基础
 * Created by Jonas on 10/20/15.
 */
abstract class BcbgWsdlBase extends BaseTaskService {

    @Autowired
    protected SuperFeedDao superFeedDao;

    @Autowired
    protected BcbgSuperFeedDao bcbgSuperFeedDao;

    /**
     * 获取子系统
     */
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
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

    protected abstract class ContextBase {

        private final Pattern special_symbol;

        protected final ChannelConfigEnums.Channel channel;

        protected final String table;

        protected final String imageTable;

        protected final String productTable;

        protected ContextBase(ChannelConfigEnums.Channel channel) {
            this.channel = channel;
            this.table = Feed.getVal1(channel, FeedEnums.Name.table_id);
            this.imageTable = Feed.getVal1(channel, FeedEnums.Name.image_table_id);
            this.special_symbol = Pattern.compile(Feed.getVal1(channel, FeedEnums.Name.url_special_symbol));
            this.productTable = Feed.getVal1(channel, FeedEnums.Name.product_table_id);
        }

        protected List<ItemBean> getItems(ProductBean product) {

            ItemBean itemColumns = new ItemBean();

            itemColumns.setCode(Feed.getVal1(channel, FeedEnums.Name.item_code));
            itemColumns.setI_sku(Feed.getVal1(channel, FeedEnums.Name.item_i_sku));
            itemColumns.setI_itemcode(Feed.getVal1(channel, FeedEnums.Name.item_i_itemcode));
            itemColumns.setI_size(Feed.getVal1(channel, FeedEnums.Name.item_i_size));
            itemColumns.setI_barcode(Feed.getVal1(channel, FeedEnums.Name.item_i_barcode));

            String where = String.format("WHERE %s AND %s = '%s'", getWhereUpdateFlg(), itemColumns.getCode(), product.getP_code());

            return superFeedDao.selectSuperfeedItem(where, itemColumns, table);
        }

        protected List<ImageBean> getImages(ProductBean product) {

            String where = String.format("WHERE %s AND %s = '%s'", getWhereUpdateFlg(), Feed.getVal1(channel, FeedEnums.Name.product_p_code), product.getP_code());

            List<String> imageArrs = superFeedDao.selectSuperfeedImage(
                    where,
                    Feed.getVal1(channel, FeedEnums.Name.images),
                    imageTable);

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

            ProductBean productColumns = getProductColumns();

            // 条件则根据类目筛选
            String where = String.format("WHERE %s", getWhereUpdateFlg());

            List<ProductBean> productBeans = superFeedDao.selectSuperfeedProduct(where, productColumns, productTable);

            $info("取得 Product [ %s ] 个", productBeans.size());

            for (ProductBean productBean: productBeans) {

                productBean.setItembeans(getItems(productBean));
                productBean.setImages(getImages(productBean));
            }

            $info("已为 Product 补全 Item 和 Image");

            return productBeans;
        }

        /**
         * 获取某 Model 下的所有 Product
         */
        protected List<ProductBean> getProducts(ModelBean model) {

            $info("准备批量获取 Model [ %s ] 的 Product", model.getUrl_key());

            ProductBean productColumns = getProductColumns();

            // 条件则根据类目筛选
            String where = String.format("WHERE %s AND %s = '%s'", getWhereUpdateFlg(), productColumns.getModel_url_key(), model.getUrl_key());

            List<ProductBean> productBeans = superFeedDao.selectSuperfeedProduct(where, productColumns, productTable);

            $info("取得 Model [ %s ] 的 Product [ %s ] 个", model.getUrl_key(), productBeans.size());

            for (ProductBean productBean: productBeans) {
                productBean.setItembeans(getItems(productBean));
                productBean.setImages(getImages(productBean));
                // 转换 Url Key 格式,这里顺序同之前的get类方法一样的原理
                productBean.setUrl_key(clearSpecialSymbol(productBean.getUrl_key()));
                productBean.setCategory_url_key(clearSpecialSymbol(productBean.getCategory_url_key()));
                productBean.setModel_url_key(clearSpecialSymbol(productBean.getModel_url_key()));
            }

            $info("已为 Model [ %s ] 的 Product 补全 Item 和 Image", model.getUrl_key());

            return productBeans;
        }

        protected String clearSpecialSymbol(String name) {
            return special_symbol.matcher(name.toLowerCase()).replaceAll(Constants.EmptyString).replace(" ", "-");
        }

        /**
         * 获取商品级别的列定义
         */
        private ProductBean getProductColumns() {

            ProductBean productColumns = new ProductBean();

            // 为每个字段指定其映射到的数据表的列.
            // 在后面的查询,自动从数据表填充值.
            productColumns.setUrl_key(Feed.getVal1(channel, FeedEnums.Name.product_url_key));
            productColumns.setModel_url_key(Feed.getVal1(channel, FeedEnums.Name.product_model_url_key));
            productColumns.setCategory_url_key(Feed.getVal1(channel, FeedEnums.Name.product_category_url_key));
            productColumns.setP_code(Feed.getVal1(channel, FeedEnums.Name.product_p_code));
            productColumns.setP_name(Feed.getVal1(channel, FeedEnums.Name.product_p_name));
            productColumns.setP_color(Feed.getVal1(channel, FeedEnums.Name.product_p_color));
            productColumns.setP_msrp(Feed.getVal1(channel, FeedEnums.Name.product_p_msrp));
            productColumns.setP_made_in_country(Feed.getVal1(channel, FeedEnums.Name.product_p_made_in_country));
            productColumns.setPe_short_description(Feed.getVal1(channel, FeedEnums.Name.product_pe_short_description));
            productColumns.setPe_long_description(Feed.getVal1(channel, FeedEnums.Name.product_pe_long_description));
            productColumns.setPs_price(Feed.getVal1(channel, FeedEnums.Name.product_ps_price));
            productColumns.setCps_cn_price_rmb(Feed.getVal1(channel, FeedEnums.Name.product_cps_cn_price_rmb));
            productColumns.setCps_cn_price(Feed.getVal1(channel, FeedEnums.Name.product_cps_cn_price));
            productColumns.setCps_cn_price_final_rmb(Feed.getVal1(channel, FeedEnums.Name.product_cps_cn_price_final_rmb));

            return productColumns;
        }
    }
}
