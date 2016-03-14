package com.voyageone.task2.cms.service.feed;

import com.voyageone.service.impl.cms.CmsFeedToCmsService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.task2.cms.dao.feed.JewelryDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoJewelryModel;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feed;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.JacksonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author james.li on 2016/1/15.
 * @version 2.0.0
 */
@Service
public class JewelryWsdlInsert extends JewelryWsdlBase {
    private static final String INSERT_FLG = "(UpdateFlag = 1 or UpdateFlag = 2)";

    @Autowired
    private CmsFeedToCmsService feedToCmsService;

    @Autowired
    private JewelryDao jewelryDao;

    @Override
    protected String getWhereUpdateFlg() {
        return INSERT_FLG;
    }

    @Override
    public String getTaskName() {
        return "JewelryAnalysis";
    }

    class Context extends ContextBase {

        protected Context(ChannelConfigEnums.Channel channel) {

            super(channel);
        }

        private List<CmsBtFeedInfoModel> getModels(String category) throws Exception {

            Map colums = getColumns();

            // 条件则根据类目筛选
            String where = String.format("WHERE %s AND %s = '%s' %s", INSERT_FLG, colums.get("category").toString(),
                    category.replace("'", "\\\'"), "and `Variation Parent SKU` != 'parent'");

            List<CmsBtFeedInfoJewelryModel> jewmodelBeans = jewelryDao.selectSuperfeedModel(where, colums, Feed.getVal1(channel, FeedEnums.Name.table_id2));
            List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
            for(CmsBtFeedInfoJewelryModel jewmodelBean : jewmodelBeans){
                Map temp= JacksonUtil.json2Bean(JacksonUtil.bean2Json(jewmodelBean), HashMap.class);
                Map<String,List<String>> attribute = new HashMap<>();
                for(int i = 0;i<99;i++){
                    String value= (String)temp.get("attribute"+i+"Value");
                    if(!StringUtil.isEmpty(value)){
                        List<String> values= new ArrayList<>();
                        values.add(value);
                        attribute.put(temp.get("attribute" + i + "Name").toString(), values);
                    }
                }

                CmsBtFeedInfoModel cmsBtFeedInfoModel = jewmodelBean.getCmsBtFeedInfoModel();
                cmsBtFeedInfoModel.setAttribute(attribute);
                modelBeans.add(cmsBtFeedInfoModel);

            }
            $info("取得 [ %s ] 的 Product 数 %s", category, modelBeans.size());

            return modelBeans;
        }

        private HashMap<String, Object> getColumns() {
            HashMap<String, Object> map = new HashMap<>();
            map.put("category", Feed.getVal1(channel, FeedEnums.Name.category_column));
            map.put("channel_id", channel.getId());
            map.put("url_key", Feed.getVal1(channel, FeedEnums.Name.model_url_key));
            map.put("category_url_key", Feed.getVal1(channel, FeedEnums.Name.model_category_url_key));
            map.put("m_product_type", Feed.getVal1(channel, FeedEnums.Name.model_m_product_type));
            map.put("m_brand", Feed.getVal1(channel, FeedEnums.Name.model_m_brand));
            map.put("m_model", Feed.getVal1(channel, FeedEnums.Name.model_m_model));
            map.put("m_name", Feed.getVal1(channel, FeedEnums.Name.model_m_name));
            map.put("m_short_description", Feed.getVal1(channel, FeedEnums.Name.model_m_short_description));
            map.put("m_long_description", Feed.getVal1(channel, FeedEnums.Name.model_m_long_description));
            map.put("m_size_type", Feed.getVal1(channel, FeedEnums.Name.model_m_size_type));
            map.put("m_is_unisex", Feed.getVal1(channel, FeedEnums.Name.model_m_is_unisex));
            map.put("m_weight", Feed.getVal1(channel, FeedEnums.Name.model_m_weight));
            map.put("m_is_taxable", Feed.getVal1(channel, FeedEnums.Name.model_m_is_taxable));
            map.put("m_is_effective", Feed.getVal1(channel, FeedEnums.Name.model_m_is_effective));
            map.put("model_url_key", (Feed.getVal1(channel, FeedEnums.Name.product_model_url_key)));
            map.put("p_url_key", (Feed.getVal1(channel, FeedEnums.Name.product_url_key)));
            map.put("p_code", (Feed.getVal1(channel, FeedEnums.Name.product_p_code)));
            map.put("p_name", (Feed.getVal1(channel, FeedEnums.Name.product_p_name)));
            map.put("p_color", (Feed.getVal1(channel, FeedEnums.Name.product_p_color)));
            map.put("p_msrp", (Feed.getVal1(channel, FeedEnums.Name.product_p_msrp)));
            map.put("p_made_in_country", (Feed.getVal1(channel, FeedEnums.Name.product_p_made_in_country)));
            map.put("pe_short_description", (Feed.getVal1(channel, FeedEnums.Name.product_pe_short_description)));
            map.put("pe_long_description", (Feed.getVal1(channel, FeedEnums.Name.product_pe_long_description)));
            map.put("ps_price", (Feed.getVal1(channel, FeedEnums.Name.product_ps_price)));
            map.put("cps_cn_price_rmb", (Feed.getVal1(channel, FeedEnums.Name.product_cps_cn_price_rmb)));
            map.put("cps_cn_price", (Feed.getVal1(channel, FeedEnums.Name.product_cps_cn_price)));
            map.put("cps_cn_price_final_rmb", (Feed.getVal1(channel, FeedEnums.Name.product_cps_cn_price_final_rmb)));
            map.put("item_code", (Feed.getVal1(channel, FeedEnums.Name.item_code)));
            map.put("i_sku", (Feed.getVal1(channel, FeedEnums.Name.item_i_sku)));
            map.put("i_itemcode", (Feed.getVal1(channel, FeedEnums.Name.item_i_itemcode)));
            map.put("i_size", (Feed.getVal1(channel, FeedEnums.Name.item_i_size)));
            map.put("i_barcode", (Feed.getVal1(channel, FeedEnums.Name.item_i_barcode)));
            map.put("i_client_sku", (Feed.getVal1(channel, FeedEnums.Name.item_i_client_sku)));
            map.put("image", (Feed.getVal1(channel, FeedEnums.Name.images)));
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
                    Feed.getVal1(channel, FeedEnums.Name.category_column),
                    table, " AND " + INSERT_FLG + " AND " + Feed.getVal1(channel, FeedEnums.Name.model_m_model) + " != '' AND "+Feed.getVal1(channel, FeedEnums.Name.model_m_brand)+" != ''");
            $info("获取类目路径数 %s , 准备拆分继续处理", categoryPaths.size());

            return categoryPaths;
        }

        /**
         * 生成类目数据包含model product数据
         *
         * @param categoryPath
         * @return
         */
        protected List<CmsBtFeedInfoModel> getCategoryInfo(String categoryPath) throws Exception {

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

                List<CmsBtFeedInfoModel> productSucceeList = new ArrayList<>();

                // 分每棵树的信息取得
                List<CmsBtFeedInfoModel> product = getCategoryInfo(categorPath);

                product.forEach(cmsBtFeedInfoModel -> {
                    List<String> categors = java.util.Arrays.asList(cmsBtFeedInfoModel.getCategory().split(" - "));
                    cmsBtFeedInfoModel.setCategory(categors.stream().map(s -> s.replace("-", "－")).collect(Collectors.joining("-")));
                });

                try{
                    Map response = feedToCmsService.updateProduct(channel.getId(), product, getTaskName());
                    List<String> itemIds = new ArrayList<>();
                    productSucceeList = (List<CmsBtFeedInfoModel>) response.get("succeed");
                    productSucceeList.forEach(feedProductModel -> feedProductModel.getSkus().forEach(feedSkuModel -> itemIds.add(feedSkuModel.getClientSku())));
                    updateFull(itemIds);
                    productFailAllList.addAll((List<CmsBtFeedInfoModel>) response.get("fail"));
                }catch (Exception e){
                    logger.error(e.getMessage());
                    issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
                }
            }
            $info("总共~ 失败的 Product: %s", productFailAllList.size());

        }

        /**
         * 导入成功的FEED数据保存起来
         *
         * @param itemIds
         */
        @Transactional
        protected void updateFull(List<String> itemIds) {
            if (itemIds.size() > 0) {
                jewelryDao.delFull(itemIds);
                jewelryDao.insertFull(itemIds);
                jewelryDao.updateFeetStatus(itemIds);
            }
        }
    }
}
