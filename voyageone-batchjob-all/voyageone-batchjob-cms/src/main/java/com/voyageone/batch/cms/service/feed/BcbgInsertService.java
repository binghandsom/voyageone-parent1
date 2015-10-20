package com.voyageone.batch.cms.service.feed;

import com.voyageone.batch.cms.bean.*;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feed;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * (临时) 为插入接口提供专门服务
 * Created by Jonas on 10/19/15.
 */
@Service
class BcbgInsertService extends BcbgWsdlBase {

    private static final String INSERT_FLG = "update_flg = 1";

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "BcbgAnalysis.Insert";
    }

    @Override
    protected String getWhereUpdateFlg() {
        return INSERT_FLG;
    }

    class Context extends ContextBase {

        private final Pattern special_symbol;

        private final String modelTable;

        protected Context(ChannelConfigEnums.Channel channel) {

            super(channel);

            this.special_symbol = Pattern.compile(Feed.getVal1(channel, FeedEnums.Name.url_special_symbol));

            this.modelTable = Feed.getVal1(channel, FeedEnums.Name.model_table_id);
        }

        private List<ModelBean> getModels(CategoryBean category) {

            $info("准备获取类目 [ %s ] 的 Model", category.getUrl_key());

            ModelBean modelColumns = new ModelBean();

            // 为每个字段指定其映射到的数据表的列.
            // 在后面的查询,自动从数据表填充值.
            modelColumns.setUrl_key(Feed.getVal1(channel, FeedEnums.Name.model_url_key));
            modelColumns.setCategory_url_key(Feed.getVal1(channel, FeedEnums.Name.model_category_url_key));
            modelColumns.setM_product_type(Feed.getVal1(channel, FeedEnums.Name.model_m_product_type));
            modelColumns.setM_brand(Feed.getVal1(channel, FeedEnums.Name.model_m_brand));
            modelColumns.setM_model(Feed.getVal1(channel, FeedEnums.Name.model_m_model));
            modelColumns.setM_name(Feed.getVal1(channel, FeedEnums.Name.model_m_name));
            modelColumns.setM_short_description(Feed.getVal1(channel, FeedEnums.Name.model_m_short_description));
            modelColumns.setM_long_description(Feed.getVal1(channel, FeedEnums.Name.model_m_long_description));
            modelColumns.setM_size_type(Feed.getVal1(channel, FeedEnums.Name.model_m_size_type));
            modelColumns.setM_is_unisex(Feed.getVal1(channel, FeedEnums.Name.model_m_is_unisex));
            modelColumns.setM_weight(Feed.getVal1(channel, FeedEnums.Name.model_m_weight));
            modelColumns.setM_is_taxable(Feed.getVal1(channel, FeedEnums.Name.model_m_is_taxable));
            modelColumns.setM_is_effective(Feed.getVal1(channel, FeedEnums.Name.model_m_is_effective));

            // 条件则根据类目筛选
            String where = String.format("WHERE %s AND %s = '%s' %s", INSERT_FLG, modelColumns.getCategory_url_key(), category.getUrl_key(), Feed.getVal1(channel, FeedEnums.Name.model_sql_ending));

            List<ModelBean> modelBeans = superFeedDao.selectSuperfeedModel(where, modelColumns, modelTable);

            $info("取得 [ %s ] 的 Model 数 %s", category.getUrl_key(), modelBeans.size());

            for (ModelBean bean: modelBeans) {
                bean.setProductbeans(getProducts(bean));
            }

            return modelBeans;
        }

        private List<CategoryBean> getCategories() {

            // 先从数据表中获取所有商品的类目路径,经过去重复的
            // update flg 标记, 只获取哪些即将进行新增的商品的类目
            List<String> categoryPaths = superFeedDao.selectSuperfeedCategory(
                    Feed.getVal1(channel, FeedEnums.Name.category_column),
                    table, " AND " + INSERT_FLG);

            $info("获取类目路径数 %s , 准备拆分继续处理", categoryPaths.size());

            // 建好集合, 后续会将所有类目的路径进行分解拆分存入
            List<CategoryBean> categories = new ArrayList<>();
            // 获取分隔符
            String separator = Feed.getVal1(channel, FeedEnums.Name.category_split);

            for (String categoryPath : categoryPaths) {
                // 对类目路径进行拆分
                String[] categoryNames = categoryPath.split(separator);
                // 声明一个新的路径
                String currPath = Constants.EmptyString;

                for (int i = 0; i < categoryNames.length; i++) {

                    String name = categoryNames[i];

                    if (StringUtils.isEmpty(name)) continue;
                    // 清除特殊符号,并转化为小写
                    name = clearSpecialSymbol(name);

                    CategoryBean category = new CategoryBean();
                    // 设置当前类目为上一次的计算结果,如果是第一个,则刚好为 EmptyString
                    category.setParent_url_key(currPath);
                    // 如果上次时 EmptyString, 那么就覆盖并赋值
                    // 否则就拼接后赋值
                    if (currPath.equals(Constants.EmptyString))
                        category.setUrl_key(currPath = name);
                    else
                        category.setUrl_key(currPath = (currPath + '-' + name));
                    // 如果已存在则跳过 (这里 categoryBean 的 equals 已重写)
                    if (categories.contains(category)) continue;

                    category.setC_name(name);
                    category.setC_header_title(name);
                    category.setC_is_enable_filter("1");
                    category.setC_is_visible_on_menu("0");
                    category.setC_is_published("0");
                    category.setC_is_effective("1");

                    // 最后一级,最底层
                    if (i == categoryNames.length - 1) {
                        category.setModelbeans(getModels(category));
                    }

                    categories.add(category);
                }
            }

            $info("获取类目数 %s", categories.size());

            return categories;
        }

        private String clearSpecialSymbol(String name) {
            return special_symbol.matcher(name.toLowerCase()).replaceAll(Constants.EmptyString);
        }

        /**
         * 调用 ProductService 提交新商品
         *
         * @throws Exception
         */
        protected void postNewProduct() throws Exception {
            // 接口的主服务
            ProductService service = new ProductService(channel);
            // 主参数
            ProductsFeedInsert feedInsert = new ProductsFeedInsert();

            $info("准备<构造> Insert 接口参数");

            feedInsert.setCategorybeans(getCategories());
            feedInsert.setChannel_id(channel.getId());

            $info("准备<提交> Insert 接口");

            // 调用返回
            WsdlProductInsertResponse response = service.insert(feedInsert);

            $info("接口结果: %s ; 返回: %s", response.getResult(), response.getMessage());

            // 准备接收失败内容
            List<String> modelFailList = new ArrayList<>();
            List<String> productFailList = new ArrayList<>();

            ProductFeedResponseBean productFeedResponseBean = response.getResultInfo();

            if (response.getResult().equals("OK") && productFeedResponseBean.getSuccess().size() > 0) {
                // 出错统计
                List<ProductFeedDetailBean> productFeedDetailBeans = productFeedResponseBean.getFailure();
                for (ProductFeedDetailBean productFeedDetailBean : productFeedDetailBeans) {
                    //  处理类型 1:category 无; 2:model
                    if (productFeedDetailBean.getBeanType() == 2)
                        modelFailList.add(productFeedDetailBean.getDealObject().getModel());

                    //  处理类型 3:product; 4:item
                    if (productFeedDetailBean.getBeanType() == 3 || productFeedDetailBean.getBeanType() == 4)
                        productFailList.add(productFeedDetailBean.getDealObject().getCode());
                }
            }

            $info("失败的 Model: %s ; 失败的 Product: %s", modelFailList.size(), productFailList.size());

            if (response.getResult().equals("NG"))
                logIssue("cms 数据导入处理", "新产品推送失败：Message=" + response.getMessage());

            bcbgSuperFeedDao.insertFullWithoutFail(modelFailList, productFailList);

            $info("新商品 Insert 处理全部完成");
        }
    }
}
