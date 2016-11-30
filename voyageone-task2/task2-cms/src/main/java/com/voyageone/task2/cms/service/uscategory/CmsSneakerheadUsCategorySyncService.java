package com.voyageone.task2.cms.service.uscategory;

import com.google.common.collect.ArrayListMultimap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.components.sneakerhead.bean.SneakerheadCategoryModel;
import com.voyageone.components.sneakerhead.service.SneakerHeadFeedService;
import com.voyageone.service.impl.cms.feed.FeedCategoryTreeService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryTreeModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by vantis on 2016/11/28.
 * 闲舟江流夕照晚 =。=
 */
@Service
public class CmsSneakerheadUsCategorySyncService extends BaseCronTaskService {

    private final SneakerHeadFeedService sneakerHeadFeedService;
    private final FeedCategoryTreeService feedCategoryTreeService;
    private final ProductService productService;

    @Autowired
    public CmsSneakerheadUsCategorySyncService(SneakerHeadFeedService sneakerHeadFeedService,
                                               FeedCategoryTreeService feedCategoryTreeService,
                                               ProductService productService) {
        this.sneakerHeadFeedService = sneakerHeadFeedService;
        this.feedCategoryTreeService = feedCategoryTreeService;
        this.productService = productService;
    }

    @Override
    protected String getTaskName() {
        return "cmsUsCategorySyncJob";
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public void onStartup(List<TaskControlBean> taskControlBeanList) {
        $info("开始导入美国分类和产品信息...");

        final String channelId = ChannelConfigEnums.Channel.SN.getId();

        try {
            $info("调用接口 获取 category...");
            // 获得 category
            List<SneakerheadCategoryModel> categoryList = sneakerHeadFeedService.getCategory(true);

            $info("获取 category 完毕 拍平之...");

            // 拍平 category
            final List<String> categoryNames = flattenCategories(categoryList);
            $info("category 拍平了 插入到 category 表中...");
            // 插入 category 到表中
            categoryNames.parallelStream().forEach(categoryName -> {
                Date start = new Date();
                feedCategoryTreeService.addCategory(channelId, categoryName, this.getClass().getSimpleName());
                $debug("插入 category: " + categoryName + " 用时 " + (new Date().getTime() - start.getTime()) + " 毫秒");
            });
            $info("插入 category 完毕 解析更新 product 下 subCategory 的参数...");

            // 过滤出来 code列表
            final ArrayListMultimap<String, CmsMtFeedCategoryTreeModel> flattenedCodes =
                    flattenCodes(channelId, categoryList);
            // 按照 code 列表丢到 cms_bt_product 里面=。=

            $info("参数解析完毕 更新 product 表中的 subCategories...");
            // 然后丢进去=。=
            productService.updateProductFeedSubCategory(
                    channelId, flattenedCodes.asMap(), this.getClass().getSimpleName());

            $info("导入美国分类和产品信息完成");
        } catch (IOException e) {
            $error("导入美国分类和产品信息发生错误: " + e.getMessage());
            throw new BusinessException("导入美国分类和产品信息发生错误: " + e.getMessage(), e);
        }
    }

    private List<String> flattenCategories(List<SneakerheadCategoryModel> categoryModelList) {
        List<String> result = new ArrayList<>();
        categoryModelList.stream()
                .map(categoryModel -> flattenCategories("", categoryModel))
                .forEach(result::addAll);
        return result;
    }

    private List<String> flattenCategories(String parentCategoryName, SneakerheadCategoryModel categoryModel) {
        List<String> categoryNameTree = new ArrayList<>();
        String currentCategoryName = null == parentCategoryName || "".equals(parentCategoryName) ?
                categoryModel.getName().replaceAll("-", "－") :
                parentCategoryName +
                        "-"
                        + categoryModel.getName().replaceAll("-", "－");
        if (null != categoryModel.getSubCategory() && categoryModel.getSubCategory().size() > 0) {
            categoryModel.getSubCategory().forEach(subCategory ->
                    categoryNameTree.addAll(flattenCategories(currentCategoryName, subCategory)));
        } else {
            categoryNameTree.add(currentCategoryName);
        }

        return categoryNameTree;
    }

    private ArrayListMultimap<String, CmsMtFeedCategoryTreeModel> flattenCodes(String channelId, List<SneakerheadCategoryModel> categoryModelList) {
        ArrayListMultimap<String, CmsMtFeedCategoryTreeModel> result = ArrayListMultimap.create();
        categoryModelList.stream()
                .map(categoryModel -> flattenCodes(channelId, "", categoryModel))
                .forEach(result::putAll);
        return result;
    }

    private ArrayListMultimap<String, CmsMtFeedCategoryTreeModel> flattenCodes(String channelId, String parentCategoryName,
                                                                               SneakerheadCategoryModel categoryModel) {
        ArrayListMultimap<String, CmsMtFeedCategoryTreeModel> codeTree = ArrayListMultimap.create();
        String currentCategoryName = null == parentCategoryName || "".equals(parentCategoryName) ?
                categoryModel.getName().replaceAll("-", "－") :
                parentCategoryName +
                        "-"
                        + categoryModel.getName().replaceAll("-", "－");

        CmsMtFeedCategoryTreeModel currentCategoryModel =
                Optional.ofNullable(feedCategoryTreeService.getCategoryNote(channelId, currentCategoryName))
                        .orElseGet(() -> {
                            CmsMtFeedCategoryTreeModel cmsMtFeedCategoryTreeModel = new CmsMtFeedCategoryTreeModel();
                            cmsMtFeedCategoryTreeModel.setCatPath(currentCategoryName);
                            cmsMtFeedCategoryTreeModel.setCatName(categoryModel.getName().replaceAll("-", "－"));
                            return cmsMtFeedCategoryTreeModel;
                        });

        if (null != categoryModel.getCodeList()) {
            categoryModel.getCodeList().forEach(code -> codeTree.put(code, currentCategoryModel));
        }

        if (null != categoryModel.getSubCategory() && categoryModel.getSubCategory().size() > 0)
            categoryModel.getSubCategory().forEach(subCategory ->
                    codeTree.putAll(flattenCodes(channelId, currentCategoryName, subCategory)));
        return codeTree;
    }
}
