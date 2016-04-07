package com.voyageone.task2.cms.service.feed;

import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.configs.beans.FeedBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.feed.FeedToCmsService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.dao.SuperFeed2Dao;
import com.voyageone.task2.cms.dao.feed.GiltFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoGiltModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james.li
 * @version 0.0.1, 16/3/8
 */
@Service
public class GiltInsert extends BaseTaskService {
    private static final String INSERT_FLG = " UpdateFlag = 1 ";

    @Autowired
    private FeedToCmsService feedToCmsService;

    @Autowired
    protected SuperFeed2Dao superFeedDao;

    @Autowired
    private GiltFeedDao giltFeedDao;

    protected String getWhereUpdateFlg() {
        return INSERT_FLG;
    }

    /**
     * 获取子系统
     */
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "Cms2GiltAnalysisJob";
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

    class Context {

        protected final ChannelConfigEnums.Channel channel;
        protected final String table;

        protected Context(ChannelConfigEnums.Channel channel) {
            this.channel = channel;
            // 主表
            this.table = Feeds.getVal1(channel, FeedEnums.Name.table_id);
        }

        private List<CmsBtFeedInfoModel> getModels(String category) throws Exception {

            Map colums = getColumns();

            // 条件则根据类目筛选
            String where = String.format("WHERE %s AND %s = '%s' ", INSERT_FLG, colums.get("category").toString(),
                    category.replace("'", "\\\'"));

            List<CmsBtFeedInfoGiltModel> vtmModelBeans = giltFeedDao.selectSuperfeedModel(where, colums, table);
            List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
            for (CmsBtFeedInfoGiltModel vtmModelBean : vtmModelBeans) {
                Map temp = JacksonUtil.json2Bean(JacksonUtil.bean2Json(vtmModelBean), HashMap.class);
                Map<String, List<String>> attribute = new HashMap<>();
                List<FeedBean> feeds = Feeds.getConfigs(channel.getId(), FeedEnums.Name.attribute);
                if (feeds != null) {
                    feeds.forEach(feedBean -> {
                        if (!StringUtil.isEmpty(feedBean.getCfg_val1())) {
                            List<String> values = new ArrayList<>();
                            values.add((String) temp.get(feedBean.getCfg_val1()));
                            attribute.put(feedBean.getCfg_val1(), values);
                        }
                    });
                }
                CmsBtFeedInfoModel cmsBtFeedInfoModel = vtmModelBean.getCmsBtFeedInfoModel();
                cmsBtFeedInfoModel.setAttribute(attribute);
                modelBeans.add(cmsBtFeedInfoModel);

            }
            return modelBeans;
        }

        private HashMap<String, Object> getColumns() {
            HashMap<String, Object> map = new HashMap<>();
            map.put("category", Feeds.getVal1(channel, FeedEnums.Name.category_column));
            map.put("channel_id", channel.getId());
            map.put("m_product_type", Feeds.getVal1(channel, FeedEnums.Name.model_m_product_type));
            map.put("m_brand", Feeds.getVal1(channel, FeedEnums.Name.model_m_brand));
            map.put("m_model", Feeds.getVal1(channel, FeedEnums.Name.model_m_model));
            map.put("m_name", Feeds.getVal1(channel, FeedEnums.Name.model_m_name));
            map.put("m_size_type", Feeds.getVal1(channel, FeedEnums.Name.model_m_size_type));
            map.put("m_weight", Feeds.getVal1(channel, FeedEnums.Name.model_m_weight));
            map.put("p_code", (Feeds.getVal1(channel, FeedEnums.Name.product_p_code)));
            map.put("p_name", (Feeds.getVal1(channel, FeedEnums.Name.product_p_name)));
            map.put("p_color", (Feeds.getVal1(channel, FeedEnums.Name.product_p_color)));
            map.put("p_made_in_country", (Feeds.getVal1(channel, FeedEnums.Name.product_p_made_in_country)));
            map.put("pe_short_description", (Feeds.getVal1(channel, FeedEnums.Name.product_pe_short_description)));
            map.put("pe_long_description", (Feeds.getVal1(channel, FeedEnums.Name.product_pe_long_description)));
//            map.put("p_msrp", (Feed.getVal1(channel, FeedEnums.Name.product_p_msrp)));
//            map.put("cps_cn_price_rmb", (Feed.getVal1(channel, FeedEnums.Name.product_cps_cn_price_rmb)));
            map.put("i_sku", (Feeds.getVal1(channel, FeedEnums.Name.item_i_sku)));
            map.put("i_itemcode", (Feeds.getVal1(channel, FeedEnums.Name.item_i_itemcode)));
            map.put("i_size", (Feeds.getVal1(channel, FeedEnums.Name.item_i_size)));
            map.put("i_barcode", (Feeds.getVal1(channel, FeedEnums.Name.item_i_barcode)));
            map.put("image", (Feeds.getVal1(channel, FeedEnums.Name.images)));
            map.put("i_client_sku", (Feeds.getVal1(channel, FeedEnums.Name.item_i_client_sku)));

            map.put("price_client_msrp", (Feeds.getVal1(channel, FeedEnums.Name.price_client_msrp)));
            map.put("price_client_retail", (Feeds.getVal1(channel, FeedEnums.Name.price_client_retail)));
            map.put("price_net", (Feeds.getVal1(channel, FeedEnums.Name.price_net)));
            map.put("price_current", (Feeds.getVal1(channel, FeedEnums.Name.price_current)));
            map.put("price_msrp", (Feeds.getVal1(channel, FeedEnums.Name.price_msrp)));
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
                    Feeds.getVal1(channel, FeedEnums.Name.category_column), table, " AND " + INSERT_FLG);
            $info("获取类目路径数 %s , 准备拆分继续处理", categoryPaths.size());

            return categoryPaths;
        }

        /**
         * 生成类目数据包含model product数据
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

            $info("准备 <构造> 类目树");
            List<String> categoriePaths = getCategories();
            $info("共"+categoriePaths.size()+"个类目");

            List<CmsBtFeedInfoModel> productSucceeList = new ArrayList<>();
            // 准备接收失败内容
            List<CmsBtFeedInfoModel> productFailAllList = new ArrayList<>();

            int i=1;
            for (String categorPath : categoriePaths) {

                // 每棵树的信息取得
                List<CmsBtFeedInfoModel> product = getCategoryInfo(categorPath);
                $info("%d/%d 取得 [ %s ] 的 Product 数 %s", i++, categoriePaths.size(), categorPath, product.size());

                executeMongoDB(product, productSucceeList, productFailAllList);
            }
            $info("总共~ 失败的 Product: %s", productFailAllList.size());

        }

        /**
         * MongoDB插入更新
         *
         * @param productAll         全部更新对象
         * @param productSucceeList  接收成功更新对象
         * @param productFailAllList 全部更新失败对象
         */
        private void executeMongoDB(List<CmsBtFeedInfoModel> productAll, List<CmsBtFeedInfoModel> productSucceeList, List<CmsBtFeedInfoModel> productFailAllList) {
            try {
                if(productAll.size() == 0) return;
                Map response = feedToCmsService.updateProduct(channel.getId(), productAll, getTaskName());
                List<String> itemIds = new ArrayList<>();
                productSucceeList = (List<CmsBtFeedInfoModel>) response.get("succeed");
                productSucceeList.forEach(feedProductModel -> feedProductModel.getSkus().forEach(feedSkuModel -> itemIds.add(feedSkuModel.getClientSku())));
                updateFull(itemIds);
                productFailAllList.addAll((List<CmsBtFeedInfoModel>) response.get("fail"));
            } catch (Exception e) {
                $error(e.getMessage());
                issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            } finally {
                productSucceeList.clear();
                productAll.clear();
            }
        }

        /**
         * 导入成功的FEED数据保存起来
         */
        @Transactional
        private void updateFull(List<String> itemIds) {
            if (itemIds.size() > 0) {
                giltFeedDao.delFull(itemIds);
                giltFeedDao.insertFull(itemIds);
                giltFeedDao.updateFeetStatus(itemIds);
            }
        }
    }
}
