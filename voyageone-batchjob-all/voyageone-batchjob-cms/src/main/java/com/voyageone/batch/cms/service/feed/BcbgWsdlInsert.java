package com.voyageone.batch.cms.service.feed;

import com.voyageone.batch.cms.bean.*;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feed;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.voyageone.batch.cms.service.feed.BcbgWsdlConstants.*;

/**
 * (临时) 为插入接口提供专门服务
 * Created by Jonas on 10/19/15.
 */
@Service
class BcbgWsdlInsert extends BcbgWsdlBase {

    private static final String INSERT_FLG = "status=10";

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

    private List<ModelBean> getModels(CategoryBean category) {

        $info("准备获取类目 [ %s ] 的 Model", category.getUrl_key());

        ModelBean modelColumns = getModelColumns();

        // 条件则根据类目筛选
        String where = String.format("WHERE %s AND %s = '%s' GROUP BY %s", INSERT_FLG, modelColumns.getCategory_url_key(),
                fix(category.getUrl_key()), grouping_model);

        List<ModelBean> modelBeans = superFeedDao.selectSuperfeedModel(where, modelColumns,
                // 组合 Model 的表部分和Join部分
                String.format("%s JOIN %s ON %s", table_feed_full, table_style_full, on_product));

        $info("取得 [ %s ] 的 Model 数 %s", category.getUrl_key(), modelBeans.size());

        for (ModelBean bean : modelBeans) {
            bean.setProductbeans(getProducts(bean));
            // 转换 Url Key 格式,这里顺序同 getCategories 一样的原理
            bean.setUrl_key(clearSpecialSymbol(bean.getUrl_key()));
            bean.setCategory_url_key(clearSpecialSymbol(bean.getCategory_url_key()));
        }

        return modelBeans;
    }

    private ModelBean getModelColumns() {
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
        return modelColumns;
    }

    private List<List<CategoryBean>> getCategories() {

        // 先从数据表中获取所有商品的类目路径,经过去重复的
        // update flg 标记, 只获取哪些即将进行新增的商品的类目
        List<String> categoryPaths = superFeedDao.selectSuperfeedCategory(
                Feed.getVal1(channel, FeedEnums.Name.category_column),
                String.format("%s JOIN %s ON %s", table_feed_full, table_style_full, on_product),
                " AND " + INSERT_FLG);

        $info("获取类目路径数 %s , 准备拆分继续处理", categoryPaths.size());

        // 建好集合, 后续会将所有类目的路径进行分解拆分存入
        // 接口提交的时候, 要求每次提交的类目必须且只能一次提交一颗完整的树
        List<List<CategoryBean>> categoriesList = new ArrayList<>();
        // 获取分隔符
        String separator = Feed.getVal1(channel, FeedEnums.Name.category_split);

        for (String categoryPath : categoryPaths) {
            // 分批提交,所以为每棵树创建独立的类目集合
            List<CategoryBean> categories = new ArrayList<>();

            // 对类目路径进行拆分
            String[] categoryNames = categoryPath.split(separator);
            // 声明一个新的路径
            String currPath = Constants.EmptyString;

            for (int i = 0; i < categoryNames.length; i++) {

                String name = categoryNames[i];

                if (StringUtils.isEmpty(name)) continue;

                CategoryBean category = new CategoryBean();
                // 设置当前类目为上一次的计算结果,如果是第一个,则刚好为 EmptyString
                category.setParent_url_key(currPath);
                // 如果上次时 EmptyString, 那么就覆盖并赋值
                // 否则就拼接后赋值
                if (currPath.equals(Constants.EmptyString))
                    category.setUrl_key(currPath = name);
                else
                    category.setUrl_key(currPath = (currPath + '-' + name));

                // 当遇到最后一级,最底层时,为叶子补全 Models
                if (i == categoryNames.length - 1) {
                    category.setModelbeans(getModels(category));
                }

                category.setC_name(name);
                category.setC_header_title(name);
                category.setC_is_enable_filter("1");
                category.setC_is_visible_on_menu("0");
                category.setC_is_published("0");
                category.setC_is_effective("1");

                // 因为数据库中的数据是没有处理特殊字符串,和转换小写的.所以 getModels 需要使用未处理的 url key 去匹配
                // 所以转换处理这一步放到每个类目的最后进行处理
                category.setUrl_key(clearSpecialSymbol(category.getUrl_key()));
                category.setParent_url_key(clearSpecialSymbol(category.getParent_url_key()));

                categories.add(category);
            }

            $info("构造类目树 %s", categories.size());

            categoriesList.add(categories);
        }

        $info("获取完整类目树 %s", categoriesList.size());

        return categoriesList;
    }

    /**
     * 调用 WsdlProductService 提交新商品
     *
     * @throws Exception
     */
    protected boolean postNewProduct() throws Exception {

        $info("准备 <构造> 类目树");
        List<List<CategoryBean>> categoriesList = getCategories();

        // 准备接收失败内容
        List<String> modelFailList = new ArrayList<>();
        List<String> productFailList = new ArrayList<>();

        if (!isServiceDisabled()) {
            foreachInsert(categoriesList, modelFailList, productFailList);
            $info("总共~ 失败的 Model: %s ; 失败的 Product: %s", modelFailList.size(), productFailList.size());
        } else {
            $info("已断开 CMS");
        }

        int[] count = bcbgSuperFeedDao.updateSuccessStatus(modelFailList, productFailList);

        $info("新商品 INSERT 处理全部完成 { Feed(M): %s, Feed(C): %s }", count[0], count[1]);

        return (count[0] + count[1]) > 0;
    }

    private void foreachInsert(List<List<CategoryBean>> categoriesList, List<String> modelFailList, List<String> productFailList) throws Exception {

        // 接口的主服务
        WsdlProductService service = new WsdlProductService(channel);

        // 分每棵树进行提交
        for (List<CategoryBean> categories : categoriesList) {

            // 主参数
            ProductsFeedInsert feedInsert = new ProductsFeedInsert();

            feedInsert.setCategorybeans(categories);
            feedInsert.setChannel_id(channel.getId());

            // 调用返回
            WsdlProductInsertResponse response = service.insert(feedInsert);

            $info("接口结果: %s ; 返回: %s", response.getResult(), response.getMessage());

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
                    $info("INSERT 接口返回: [%s] %s", productFeedDetailBean.getResultType(), productFeedDetailBean.getResultMessage());
                }
            }

            if (response.getResult().equals("NG"))
                logIssue("cms 数据导入处理", "新产品推送失败：Message=" + response.getMessage());
        }
    }
}
