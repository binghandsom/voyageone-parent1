package com.voyageone.batch.synship.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.batch.SynshipConstants;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums.Name;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.synship.dao.*;
import com.voyageone.batch.synship.modelbean.*;
import com.voyageone.batch.synship.service.ems.B2COrderServiceStub;
import com.voyageone.batch.synship.service.ems.EmsService;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import org.apache.axis2.AxisFault;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.voyageone.batch.SynshipConstants.AuditResult.FAIL;
import static com.voyageone.batch.SynshipConstants.AuditResult.PASS;
import static com.voyageone.batch.SynshipConstants.IdCardStatus.*;
import static com.voyageone.batch.SynshipConstants.Reason.*;
import static com.voyageone.batch.SynshipConstants.*;

/**
 * 从 Synship CloudClient 迁移的身份证验证任务
 * <p>
 * Created by Jonas on 9/22/15.
 */
@Service
public class SynShipGetEtkStatusService extends BaseTaskService {

    @Autowired
    TrackingDao trackingDao;

    /**
     * 获取子系统
     */
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.SYNSHIP;
    }

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "SynShipGetEtkStatusJob";
    }

    /**
     * 获取打印的日志是否需要包含线程
     */
    @Override
    public boolean getLogWithThread() {
        return true;
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        String thread_count = TaskControlUtils.getVal1(taskControlList, Name.thread_count);
        final int THREAD_COUNT = Integer.valueOf(thread_count);

        // 获取所有需要轮询数据
        List<EtkTrackingBean> etkTrackingBeans = trackingDao.getEtkTracking();

        // 如果木有数据，那自然就结束掉
        if (etkTrackingBeans == null || etkTrackingBeans.size() == 0) {
            $info("没有找到需要轮询的ETK记录");
            return;
        }

        List<Runnable> runnable = new ArrayList<>();

        List<Exception> exceptions = new ArrayList<>();

        int total = etkTrackingBeans.size();

        //按线程平分件数
        int split = total/THREAD_COUNT;
        split = split + THREAD_COUNT;

        $info("需要轮询的ETK记录件数：" + total + "，线程数：" + THREAD_COUNT + "，平分件数：" + split);

        // 将集合拆分到线程
        for (int i = 0; i < THREAD_COUNT; i++) {
            int start = i * split;
            int end = start + split;

            if (end >= total) {
                end = total;
                // 如果已经不足够了。说明就算后续还可以开线程，但是已经不需要了。
                // 标记 i 为最大，结束 for 循环
                i = THREAD_COUNT;
            }

            List<EtkTrackingBean> subList = etkTrackingBeans.subList(start, end);

            runnable.add(() -> {
                try {
                    validOnThread(subList);
                } catch (Exception e) {
                    exceptions.add(e);
                }
            });
        }

        $info("生成的线程任务数：" + runnable.size());

        runWithThreadPool(runnable, taskControlList);

        // 任务结束后统一生成 issueLog
        exceptions.forEach(this::logIssue);
    }

    protected void validOnThread(List<EtkTrackingBean> etkTrackingBeans) throws JSONException, InterruptedException {
        // 有数据的话，那么开始加载一些固定的配置


    }


}
