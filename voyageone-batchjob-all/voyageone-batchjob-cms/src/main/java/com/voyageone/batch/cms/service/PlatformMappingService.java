package com.voyageone.batch.cms.service;

import com.jayway.jsonpath.JsonPath;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformCategoryDao;
import com.voyageone.cms.service.model.CmsMtFeedCategoryTreeModel;
import com.voyageone.cms.service.model.CmsMtPlatformCategoryTreeModel;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/12/7.
 */
@Service
public class PlatformMappingService extends BaseTaskService {
    private final static String JOB_NAME = "platformMappingTask";

    @Autowired
    CmsMtPlatformCategoryDao cmsMtPlatformCategoryDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return JOB_NAME;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        List<CmsMtPlatformCategoryTreeModel> platformCategoryTree = cmsMtPlatformCategoryDao.selectPlatformCategoriesByCartId(23);
        for (CmsMtPlatformCategoryTreeModel platformCategory : platformCategoryTree){
            List<CmsMtPlatformCategoryTreeModel> finallyCategories = getFinallyCategories(platformCategory.getChannelId(),platformCategory.getCartId());
        }
    }

    /**
     * 获取该channel下所有的叶子类目
     * @param channelId
     * @return
     */
    private List<CmsMtPlatformCategoryTreeModel> getFinallyCategories(String channelId,int cartId){

        List<CmsMtPlatformCategoryTreeModel> cmsMtFeedCategoryTreeModel = cmsMtPlatformCategoryDao.selectByChannel_CartId(channelId,cartId);
        Object jsonObj = JsonPath.parse(cmsMtFeedCategoryTreeModel).json();
        List<CmsMtPlatformCategoryTreeModel> child = JsonPath.read(jsonObj, "$..children[?(@.isParent == 0)]");
        return child;
    }

}
