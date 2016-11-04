package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoCartDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.IntStream;

@Repository
public class CmsMtPlatformCategoryDao extends BaseMongoCartDao<CmsMtPlatformCategoryTreeModel> {

    public WriteResult deletePlatformCategories(Integer cartId, String channelId) {
        String queryStr = "{'cartId':" + cartId + ",'channelId':'" + channelId + "'}";
        return deleteWithQuery(queryStr, cartId);
    }

    public List<CmsMtPlatformCategoryTreeModel> selectPlatformCategoriesByCartId(Integer cartId) {
        String queryStr = "{cartId:" + cartId + "}";
        return select(queryStr, cartId);
    }

    public CmsMtPlatformCategoryTreeModel selectByChannel_CartId_CatId(String channelId, int cartId, String categoryId) {
        String queryStrTemp = "{" +
                "channelId:'%s'" +
                ",cartId:%s" +
                ",catId:'%s'" +
                "}";
        String queryStr = String.format(queryStrTemp, channelId, cartId, categoryId);
        return selectOneWithQuery(queryStr, cartId);
    }

    public List<CmsMtPlatformCategoryTreeModel> selectByChannel_CartId(String channelId, int cartId) {
        String queryStrTemp = "{" +
                "channelId:'%s'" +
                ",cartId:%s" +
                "}";
        String queryStr = String.format(queryStrTemp, channelId, cartId);
        return select(queryStr, cartId);
    }

    /**
     * 根据 categoryPath 绝对定位查找类目或子类目
     *
     * @return 返回绝对定位到的类目或子类目
     * @since 2.9.0
     */
    public CmsMtPlatformCategoryTreeModel selectOne(String categoryPath, String channelId, int cartId) {

        // 根据 path 计算类目深度
        int deep = StringUtils.countMatches(categoryPath, ">");

        // 根据深度计算条件和列筛选
        // 对常见的 1，2，3 进行部分优化
        String children = "children";
        String query = "catPath";
        String projection = null;
        if (deep == 1) {
            query = String.format("%s.%s", children, query);
            projection = children + ".$";
        } else if (deep == 2) {
            query = String.format("%s.%s.%s", children, children, query);
            projection = String.format("%s.%s.$", children, children);
        } else if (deep == 3) {
            query = String.format("%s.%s.%s.%s", children, children, children, query);
            projection = String.format("%s.%s.%s.$", children, children, children);
        } else if (deep > 3) {
            Object[] prefixArray = IntStream.range(0, deep).mapToObj(i -> children).toArray();
            String prefix = StringUtils.join(prefixArray, ".");
            query = String.format("%s.%s", prefix, query);
            projection = prefix + ".$";
        }

        // 执行查询
        Criteria criteria = new Criteria(query).is(categoryPath).and("channelId").is(channelId);
        JongoQuery jongoQuery = new JongoQuery(criteria);
        if (projection != null)
            jongoQuery.setProjection(String.format("{%s:1}", projection));
        CmsMtPlatformCategoryTreeModel cmsMtPlatformCategoryTreeModel = selectOneWithQuery(jongoQuery, cartId);

        // 循环查找实际目标
        while (StringUtils.isEmpty(cmsMtPlatformCategoryTreeModel.getCatPath()) && !cmsMtPlatformCategoryTreeModel.getChildren().isEmpty()) {
            cmsMtPlatformCategoryTreeModel = cmsMtPlatformCategoryTreeModel.getChildren().get(0);
        }

        return cmsMtPlatformCategoryTreeModel;
    }
}
