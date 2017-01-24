package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoCartDao;
import com.voyageone.base.dao.mongodb.JongoAggregate;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

        String children = "children";
        if (deep == 0) {
            JongoQuery query = new JongoQuery();
            query.setQuery("{catPath: #}");
            query.setParameters(categoryPath);
            List<CmsMtPlatformCategoryTreeModel> models = select(query, cartId);
            return models.isEmpty() ? null : models.get(0);
        } else {
            Object[] prefixArray = IntStream.range(0, deep - 1).mapToObj(i -> children).toArray();
            String prefix = prefixArray.length > 0 ? "." +StringUtils.join(prefixArray, ".") : "";

            List<JongoAggregate> aggregateList = new ArrayList<>();
            aggregateList.add(new JongoAggregate("{ $unwind : \"$children\" }"));

            String unwindInfo = String.format("{ $unwind : \"$children%s\"}", prefix);
            aggregateList.add(new JongoAggregate(unwindInfo));

            String matchInfo = String.format("{$match: {\"children%s.catPath\": \"%s\"}}", prefix, categoryPath);
            aggregateList.add(new JongoAggregate(matchInfo));

            String projectInfo = String.format("{$project: {\"children%s\": 1}}", prefix);
            aggregateList.add(new JongoAggregate(projectInfo));

            List<Map<String, Object>> models = aggregateToMap(cartId, aggregateList);

            if (models.isEmpty())
                return null;

            Map<String, Object> newCategoryTree = (Map<String, Object>)models.get(0).get("children");
            while (newCategoryTree.get("catPath") == null || !categoryPath.equals(newCategoryTree.get("catPath").toString())) {
                newCategoryTree = (Map<String, Object>)newCategoryTree.get("children");
            }
//            if (categoryPath.equals(newCategoryTree.get("catPath").toString())) {
//            } else {
//                return null;
//            }

            CmsMtPlatformCategoryTreeModel cmsMtPlatformCategoryTreeModel = new CmsMtPlatformCategoryTreeModel();
            cmsMtPlatformCategoryTreeModel.setCatId(newCategoryTree.get("catId").toString());
//            cmsMtPlatformCategoryTreeModel.setCartId(Integer.valueOf(newCategoryTree.get("cartId").toString()));
            cmsMtPlatformCategoryTreeModel.setCartId(cartId);
            cmsMtPlatformCategoryTreeModel.setChannelId(newCategoryTree.get("channelId").toString());
            cmsMtPlatformCategoryTreeModel.setCatName(newCategoryTree.get("catName").toString());
            cmsMtPlatformCategoryTreeModel.setCatPath(newCategoryTree.get("catPath").toString());
            cmsMtPlatformCategoryTreeModel.setParentCatId(newCategoryTree.get("parentCatId").toString());
            cmsMtPlatformCategoryTreeModel.setIsParent(Integer.valueOf(newCategoryTree.get("isParent").toString()));
            return cmsMtPlatformCategoryTreeModel;
        }
    }
}
