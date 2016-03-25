package com.voyageone.task2.cms.service.feed;

import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.service.impl.cms.feed.FeedToCmsService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (临时) 为插入接口提供专门服务
 * Created by Jonas on 10/19/15.
 */
@Service
class SearsWsdlInsert extends SearsWsdlBase {

    private static final String INSERT_FLG = "update_flg = 1";

    @Autowired
    private FeedToCmsService feedToCmsService;

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "SearsAnalysis";
    }

    @Override
    protected String getWhereUpdateFlg() {
        return INSERT_FLG;
    }

    class Context extends ContextBase {

        protected Context(ChannelConfigEnums.Channel channel) {

            super(channel);
        }

        private List<CmsBtFeedInfoModel> getModels(String category) {

            Map colums = getColumns();

            // 条件则根据类目筛选
            String where = String.format("WHERE %s AND %s = '%s' %s", INSERT_FLG, colums.get("category_url_key").toString(),
                    category.replace("'", "\\\'"), Feeds.getVal1(channel, FeedEnums.Name.model_sql_ending));

            List<CmsBtFeedInfoModel> modelBeans = superFeedDao.selectSuperfeedModel(where, colums,
                    // 组合 Model 的表部分和Join部分
                    String.format("%s left join voyageone_cms.cms_zz_worktable_sears_attribute att on %s.item_id = att.item_id", modelTable, modelTable));

            $info("取得 [ %s ] 的 Product 数 %s", category, modelBeans.size());

            return modelBeans;
        }

        private HashMap<String, Object> getColumns() {
            HashMap<String, Object> map = new HashMap<>();
            map.put("category", Feeds.getVal1(channel, FeedEnums.Name.category_column));
            map.put("channel_id", channel.getId());
            map.put("url_key", Feeds.getVal1(channel, FeedEnums.Name.model_url_key));
            map.put("category_url_key", Feeds.getVal1(channel, FeedEnums.Name.model_category_url_key));
            map.put("m_product_type", Feeds.getVal1(channel, FeedEnums.Name.model_m_product_type));
            map.put("m_brand", Feeds.getVal1(channel, FeedEnums.Name.model_m_brand));
            map.put("m_model", Feeds.getVal1(channel, FeedEnums.Name.model_m_model));
            map.put("m_name", Feeds.getVal1(channel, FeedEnums.Name.model_m_name));
            map.put("m_short_description", Feeds.getVal1(channel, FeedEnums.Name.model_m_short_description));
            map.put("m_long_description", Feeds.getVal1(channel, FeedEnums.Name.model_m_long_description));
            map.put("m_size_type", Feeds.getVal1(channel, FeedEnums.Name.model_m_size_type));
            map.put("m_is_unisex", Feeds.getVal1(channel, FeedEnums.Name.model_m_is_unisex));
            map.put("m_weight", Feeds.getVal1(channel, FeedEnums.Name.model_m_weight));
            map.put("m_is_taxable", Feeds.getVal1(channel, FeedEnums.Name.model_m_is_taxable));
            map.put("m_is_effective", Feeds.getVal1(channel, FeedEnums.Name.model_m_is_effective));
            map.put("model_url_key", (Feeds.getVal1(channel, FeedEnums.Name.product_model_url_key)));
            map.put("p_url_key", (Feeds.getVal1(channel, FeedEnums.Name.product_url_key)));
            map.put("p_code", (Feeds.getVal1(channel, FeedEnums.Name.product_p_code)));
            map.put("p_name", (Feeds.getVal1(channel, FeedEnums.Name.product_p_name)));
            map.put("p_color", (Feeds.getVal1(channel, FeedEnums.Name.product_p_color)));
            map.put("p_msrp", (Feeds.getVal1(channel, FeedEnums.Name.product_p_msrp)));
            map.put("p_made_in_country", (Feeds.getVal1(channel, FeedEnums.Name.product_p_made_in_country)));
            map.put("pe_short_description", (Feeds.getVal1(channel, FeedEnums.Name.product_pe_short_description)));
            map.put("pe_long_description", (Feeds.getVal1(channel, FeedEnums.Name.product_pe_long_description)));
            map.put("ps_price", (Feeds.getVal1(channel, FeedEnums.Name.product_ps_price)));
            map.put("cps_cn_price_rmb", (Feeds.getVal1(channel, FeedEnums.Name.product_cps_cn_price_rmb)));
            map.put("cps_cn_price", (Feeds.getVal1(channel, FeedEnums.Name.product_cps_cn_price)));
            map.put("cps_cn_price_final_rmb", (Feeds.getVal1(channel, FeedEnums.Name.product_cps_cn_price_final_rmb)));
            map.put("item_code", (Feeds.getVal1(channel, FeedEnums.Name.item_code)));
            map.put("i_sku", (Feeds.getVal1(channel, FeedEnums.Name.item_i_sku)));
            map.put("i_itemcode", (Feeds.getVal1(channel, FeedEnums.Name.item_i_itemcode)));
            map.put("i_size", (Feeds.getVal1(channel, FeedEnums.Name.item_i_size)));
            map.put("i_barcode", (Feeds.getVal1(channel, FeedEnums.Name.item_i_barcode)));
            map.put("i_client_sku", (Feeds.getVal1(channel, FeedEnums.Name.item_i_client_sku)));
            map.put("image", (Feeds.getVal1(channel, FeedEnums.Name.images)));
            return map;
        }

        /**
         * 查询一共有多少类目
         *
         * @return 类目清单
         */
        private List<String> getCategories() {

            // 先从数据表中获取所有商品的类目路径,经过去重复的
            // update flg 标记, 只获取哪些即将进行新增的商品的类目
            List<String> categoryPaths = superFeedDao.selectSuperfeedCategory(
                    Feeds.getVal1(channel, FeedEnums.Name.category_column),
                    table, " AND " + INSERT_FLG + " AND model_number != '' AND brand != ''");
            $info("获取类目路径数 %s , 准备拆分继续处理", categoryPaths.size());

            return categoryPaths;
        }

        /**
         * 生成类目数据包含model product数据
         *
         * @param categoryPath
         * @return
         */
        protected List<CmsBtFeedInfoModel> getCategoryInfo(String categoryPath) {

            return getModels(categoryPath);
        }

        /**
         * 调用 WsdlProductService 提交新商品
         *
         * @throws Exception
         */
        protected void postNewProduct() throws Exception {
            // 接口的主服务
            WsdlProductService service = new WsdlProductService(channel);

            $info("准备 <构造> 类目树");
            List<String> categoriePaths = getCategories();
            // 准备接收失败内容
            List<CmsBtFeedInfoModel> productFailAllList = new ArrayList<>();


            for (String categorPath : categoriePaths) {
                // 把类目名中的【-】替换成全角的【－】
                List<String> categors = java.util.Arrays.asList(categorPath.split(" - "));
                categorPath = categors.stream().map(s -> s.replace("-", "－")).collect(Collectors.joining("-"));

                List<CmsBtFeedInfoModel> productSucceeList = new ArrayList<>();

                // 分每棵树的信息取得
                List<CmsBtFeedInfoModel> product = getCategoryInfo(categorPath);
                product.forEach(feedProductModel1 -> feedProductModel1.attributeListToMap());
                Map response = feedToCmsService.updateProduct(channel.getId(), product, getTaskName());
                List<String> itemIds = new ArrayList<>();
                productSucceeList = (List<CmsBtFeedInfoModel>) response.get("succeed");
                productSucceeList.forEach(feedProductModel -> feedProductModel.getSkus().forEach(feedSkuModel -> itemIds.add(feedSkuModel.getClientSku())));
                updateFull(itemIds);
                productFailAllList.addAll((List<CmsBtFeedInfoModel>) response.get("fail"));
            }
            $info("总共~ 失败的 Product: %s", productFailAllList.size());

        }
    }
}
