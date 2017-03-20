package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * feed->master导入服务
 *
 * @author tom on 2016/2/18.
 * @version 2.4.0
 * @since 2.0.0
 */
@Service
public class CmsSetMainPropMongo2Service  extends BaseCronTaskService {

    @Autowired
    SetMainPropService setMainPropService;
    @Override
    protected String getTaskName() {
        return "CmsSetMainPropMongoJob";
    }

    /**
     * feed数据 -> 主数据
     * 关联代码1 (从天猫获取Fields):
     * 当需要从天猫上,拉下数据填充到product表里的场合, skip_mapping_check会是1
     * 并且生成product之后, 会有一个程序来填满fields, 测试程序是:CmsPlatformProductImportServiceTest
     * 关联代码2 (切换主类目的时候):
     *
     * @param taskControlList job 配置
     * @throws Exception
     */
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 默认线程池最大线程数
        int threadPoolCnt = 5;
        // 保存每个channel最终导入结果(成功失败件数信息)
        Map<String, String> resultMap = new ConcurrentHashMap<>();
        // 保存是否需要清空缓存(添加过品牌等信息时，需要清空缓存)
        Map<String, String> needReloadMap = new ConcurrentHashMap<>();
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolCnt);
        // 根据渠道运行
        for (final String orderChannelID : orderChannelIdList) {
            // 获取是否跳过mapping check
            String skip_mapping_check = TaskControlUtils.getVal2(taskControlList, TaskControlEnums.Name.order_channel_id, orderChannelID);
            boolean bln_skip_mapping_check = true;
            if (StringUtils.isEmpty(skip_mapping_check) || "0".equals(skip_mapping_check)) {
                bln_skip_mapping_check = false;
            }
//            // 获取前一次的价格强制击穿时间
//            String priceBreakTime = TaskControlUtils.getEndTime(taskControlList, TaskControlEnums.Name.order_channel_id, orderChannelID);
            // 主逻辑
            SetMainPropService.setMainProp mainProp = setMainPropService.new setMainProp(orderChannelID, false, bln_skip_mapping_check);
            mainProp.setTaskName(getTaskName());
            // 启动多线程
            executor.execute(() -> mainProp.doRun(resultMap, needReloadMap));
        }
        // ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成(已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
        // 当所有已经提交的任务执行完毕后将会关闭ExecutorService。
        executor.shutdown(); // 并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        try {
            // 阻塞，直到线程池里所有任务结束
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        // 判断是否需要清空缓存
        if ("1".equals(needReloadMap.get(CacheKeyEnums.KeyEnum.ConfigData_TypeChannel.toString()))) {
            // 清除缓存（这样就能马上在画面上展示出最新追加的brand，productType，sizeType等初始化mapping信息）
            TypeChannels.reload();
        }

        $info("=================feed->master导入  最终结果=====================");
        resultMap.entrySet().stream()
                .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
                .forEach(p -> $info(p.getValue()));
        $info("=================feed->master导入  主线程结束====================");
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }
}
