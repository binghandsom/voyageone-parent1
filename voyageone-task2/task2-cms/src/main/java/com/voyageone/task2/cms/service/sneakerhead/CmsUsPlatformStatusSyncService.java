package com.voyageone.task2.cms.service.sneakerhead;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.components.sneakerhead.SneakerHeadBase;
import com.voyageone.components.sneakerhead.bean.platformstatus.usPlatformModel.CodeUsPlatformModel;
import com.voyageone.components.sneakerhead.bean.platformstatus.usPlatformModel.UsPlatformStatusModel;
import com.voyageone.components.sneakerhead.service.SneakerheadApiService;
import com.voyageone.service.impl.cms.product.ProductPlatformService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_UsPlatform_Cart;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by vantis on 2016/12/2.
 * 闲舟江流夕照晚 =。=
 */
@Service
public class CmsUsPlatformStatusSyncService extends BaseCronTaskService {

    private final SneakerheadApiService sneakerheadApiService;
    private final ProductService productService;
    private final ProductPlatformService productPlatformService;

    private static final Integer DEFAULT_THREAD_COUNT = 10;
    private static final Integer DEFAULT_ATOM_COUNT = 100;

    @Autowired
    public CmsUsPlatformStatusSyncService(SneakerheadApiService sneakerheadApiService,
                                          ProductService productService,
                                          ProductPlatformService productPlatformService) {
        this.sneakerheadApiService = sneakerheadApiService;
        this.productService = productService;
        this.productPlatformService = productPlatformService;
    }

    @Override
    protected String getTaskName() {
        return "CmsUsPlatformStatusSyncJob";
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        $info("开始导入美国平台状态信息...");
        // 每页处理条数
        Integer atomCount = getAtomCount(taskControlList);

        // 获得 001 下所有的商品 code 数量
        long sum = countAllCodes();
        $info("共计 " + sum + " 个 code 等待处理 分组处理...");

        List<Runnable> runnableList = new ArrayList<>();

        for (int page = 0; page * atomCount < sum; page++)
            runnableList.add(manageRunnable(page, atomCount));

        $info("共计 " + runnableList.size() + "页 分页处理...");
        runWithThreadPool(runnableList, taskControlList, DEFAULT_THREAD_COUNT);

        $info("本次美国平台状态信息导入完毕");
    }

    private long countAllCodes() {
        JongoQuery jongoQuery = new JongoQuery();
        jongoQuery.setProjection("{\"common.fields.code\":1}");
        jongoQuery.setSort("{\"_id\":1}");
        return productService.countByQuery(jongoQuery.getQuery(), new Object[0], ChannelConfigEnums.Channel.SN.getId());
    }

    private Runnable manageRunnable(Integer page, Integer atomCount) {
        JongoQuery jongoQuery = new JongoQuery();
        // jongoQuery.setProjection("{'common.fields.code':1,'_id':0}");
        jongoQuery.setSort("{\"_id\":1}");

        jongoQuery.setLimit(atomCount);
        jongoQuery.setSkip(page * atomCount);
        jongoQuery.setSort("{\"_id\": 1}");

        return () -> {
            try {
                // 获取现有 product code
                List<CmsBtProductModel> productList =
                        productService.getList(ChannelConfigEnums.Channel.SN.getId(), jongoQuery);
//                List<String> codeList = productList.stream()
//                        .map(product -> product.getCommon().getFields().getCode())
//                        .collect(Collectors.toList());
                List<String> codeList =
                        productList.stream().map(prodObj -> prodObj.getCommonNotNull().getFieldsNotNull().getCode())
                                .filter(prodCode -> (prodCode != null && !prodCode.isEmpty())).collect(Collectors.toList());

                // 获取 code 的美国平台状态
                List<CodeUsPlatformModel> usPlatformStatusList =
                        sneakerheadApiService.getUsPlatformStatus(codeList, SneakerHeadBase.DEFAULT_DOMAIN);

                // 将信息更新到 cms_bt_product 中
                updateUsPlatformModel(usPlatformStatusList);

            } catch (Exception e) {
                throw new BusinessException("更新 Sneakerhead 美国平台状态失败", e);
            }
        };
    }

    private void updateUsPlatformModel(List<CodeUsPlatformModel> usPlatformStatusList) {
        usPlatformStatusList.forEach(usPlatformStatus -> {
            List<UsPlatformStatusModel> statuses = usPlatformStatus.getStatuses();
            Map<String, CmsBtProductModel_UsPlatform_Cart> usPlatforms = statuses.stream()
                    .map(usPlatformStatusModel -> {
                        CmsBtProductModel_UsPlatform_Cart cmsBtProductModel_usPlatform_cart
                                = new CmsBtProductModel_UsPlatform_Cart();
                        cmsBtProductModel_usPlatform_cart.setCartId(
                                Integer.parseInt(usPlatformStatusModel.getCartId()));
                        cmsBtProductModel_usPlatform_cart.setStatus(usPlatformStatusModel.getStatus().name());

                        return cmsBtProductModel_usPlatform_cart;
                    }).collect(Collectors.toMap(
                            cmsBtProductModel_usPlatform_cart -> "P" + cmsBtProductModel_usPlatform_cart.getCartId(),
                            cmsBtProductModel_usPlatform_cart -> cmsBtProductModel_usPlatform_cart));
            productPlatformService.updateUsPlatforms(
                    ChannelConfigEnums.Channel.SN.getId(),
                    usPlatformStatus.getCode(), usPlatforms, this.getClass().getSimpleName());
        });
    }

    private Integer getAtomCount(List<TaskControlBean> taskControlList) {

        String atomCountString =
                TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.atom_count);

        try {
            return Integer.parseInt(atomCountString);
        } catch (NumberFormatException | NullPointerException e) {
            $info("导入美国平台状态任务的 atom_count 设置不正确 采用默认值: " + DEFAULT_ATOM_COUNT);
            return DEFAULT_ATOM_COUNT;
        }
    }
}
