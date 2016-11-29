package com.voyageone.task2.cms.service.uscategory;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.components.sneakerhead.bean.SneakerheadCategoryModel;
import com.voyageone.components.sneakerhead.service.SneakerHeadFeedService;
import com.voyageone.service.impl.cms.feed.FeedCategoryTreeService;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by vantis on 2016/11/28.
 * 闲舟江流夕照晚 =。=
 */
@Service
public class CmsSneakerheadUsCategorySyncService extends BaseCronTaskService {

    private final SneakerHeadFeedService sneakerHeadFeedService;
    private final FeedCategoryTreeService feedCategoryTreeService;

    @Autowired
    public CmsSneakerheadUsCategorySyncService(SneakerHeadFeedService sneakerHeadFeedService,
                                               FeedCategoryTreeService feedCategoryTreeService) {
        this.sneakerHeadFeedService = sneakerHeadFeedService;
        this.feedCategoryTreeService = feedCategoryTreeService;
    }

    @Override
    protected String getTaskName() {
        return this.getClass().getSimpleName();
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public void onStartup(List<TaskControlBean> taskControlBeanList) {
        $info("开始导入美国分类和产品信息...");

        try {
            // 获得 category
            SneakerheadCategoryModel category = sneakerHeadFeedService.getCategory(true);

            // TODO: 2016/11/29 拍平category 并且过滤出来code的列表 vantis

        } catch (IOException e) {
            $error("导入美国分类和产品信息发生错误: " + e.getMessage());
            throw new BusinessException("导入美国分类和产品信息发生错误: " + e.getMessage(), e);
        }
    }

    private List<String> flattenCategories(List<SneakerheadCategoryModel> categoryModels) {
        return null;
    }
}
