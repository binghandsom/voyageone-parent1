package com.voyageone.service.impl.cms;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.dao.cms.CmsMtChannelCategoryConfigDao;
import com.voyageone.service.dao.cms.mongo.CmsMtCategoryTreeDao;
import com.voyageone.service.daoext.cms.CmsMtChannelCategoryConfigDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtChannelCategoryConfigModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChannelCategoryService extends BaseService {

    @Autowired
    private CmsMtChannelCategoryConfigDao cmsMtChannelCategoryConfigDao;

    @Autowired
    private CmsMtChannelCategoryConfigDaoExt cmsMtChannelCategoryConfigDaoExt;

    @Autowired
    private CmsMtCategoryTreeDao cmsMtCategoryTreeDao;

    /**
     * 取得Category Tree 根据channelId
     */
    public List<CmsMtCategoryTreeModel> getCategoriesByChannelId(String channelId) {
        List<CmsMtCategoryTreeModel> result = new ArrayList<>();

        List<CmsMtChannelCategoryConfigModel> mappings = getByChannelId(channelId);
        for (CmsMtChannelCategoryConfigModel mapping : mappings) {
            String catId = mapping.getCategoryId();
            CmsMtCategoryTreeModel model = cmsMtCategoryTreeDao.selectByCatId(catId);
            if (model != null) {
                result.add(model);
            }
        }
        return result;
    }

    /**
     * 获取父级类目及其子类目
     * @param channelId String
     * @return List<CmsMtCategoryTreeModel>
     * @throws IOException
     */
    public List<CmsMtCategoryTreeModel> getAllCategoriesByChannelId(String channelId) throws IOException{
        List<CmsMtCategoryTreeModel> result = new ArrayList<>();

        List<CmsMtChannelCategoryConfigModel> mappings = getByChannelId(channelId);
        for (CmsMtChannelCategoryConfigModel mapping : mappings) {
            String catId = mapping.getCategoryId();
            CmsMtCategoryTreeModel category = cmsMtCategoryTreeDao.selectByCatId(catId);
            if (category.getIsParent() == 1) {

                // 将父类添加到result结果集
                result.add(category);

                // 返回子类
                List<CmsMtCategoryTreeModel> childCategory
                        = JsonPath.parse(JacksonUtil.bean2Json(category)).read("$..children[?(@.catPath != null )]"
                        , new TypeRef<List<CmsMtCategoryTreeModel>>() {});
                result.addAll(childCategory);
            } else {
                result.add(category);
            }
        }

        // 清空children
        for (CmsMtCategoryTreeModel info : result) {
            info.setChildren(null);
        }

        return result;
    }

    /**
     * 返回子category列表
     * @param channelId channel Id
     * @return List<CmsMtCategoryTreeModel>
     */
    public List<CmsMtCategoryTreeModel> getFinallyCategoriesByChannelId(String channelId) throws IOException{
        List<CmsMtCategoryTreeModel> result = new ArrayList<>();

        List<CmsMtChannelCategoryConfigModel> mappings = getByChannelId(channelId);
        for (CmsMtChannelCategoryConfigModel mapping : mappings) {
            String catId = mapping.getCategoryId();
            CmsMtCategoryTreeModel category = cmsMtCategoryTreeDao.selectByCatId(catId);

            if (category.getIsParent() == 1) {

                List<CmsMtCategoryTreeModel> childCategory
                        = JsonPath.parse(JacksonUtil.bean2Json(category)).read("$..children[?(@.isParent == 0)]"
                        , new TypeRef<List<CmsMtCategoryTreeModel>>() {});
                result.addAll(childCategory);
            } else {
                result.add(category);
            }
        }

        return result;
    }

    /**
     * 取得Mapping定义 根据channelId
     */
    public List<CmsMtChannelCategoryConfigModel> getByChannelId(String channelId) {
        return cmsMtChannelCategoryConfigDaoExt.selectbyChannelId(channelId);
    }

    /**
     * 保存Mapping定义
     */
    @VOTransactional
    public void save(CmsMtChannelCategoryConfigModel model) {
        cmsMtChannelCategoryConfigDaoExt.insert(model);
    }

    /**
     * 保存Mapping List定义
     */
    @VOTransactional
    public void saveWithList(List<CmsMtChannelCategoryConfigModel> models) {
        cmsMtChannelCategoryConfigDaoExt.insertWithList(models);
    }

    @VOTransactional
    public void saveWithListOne(List<CmsMtChannelCategoryConfigModel> models) {
        System.out.println(JacksonUtil.bean2Json(cmsMtChannelCategoryConfigDao.select(15)));
        cmsMtChannelCategoryConfigDaoExt.insertWithList(models.subList(1, models.size()));
        cmsMtChannelCategoryConfigDao.insert(models.get(0));
        if (true) {
            throw new RuntimeException("bb");
        }
    }

}
