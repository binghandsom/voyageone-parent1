package com.voyageone.batch.ims.service.beat;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums.Name;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.ims.bean.BeatPicBean;
import com.voyageone.batch.ims.dao.ImsBeatPicDao;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * 价格披露同一处理任务
 * <p>
 * Created by sky on 20150807.
 */
@Service
public class ImsBeatPicService extends BaseTaskService {

    @Autowired
    private ImsBeatPicDao imsBeatPicDao;

    @Autowired
    private ImsBeatRevertService imsBeatRevertService;

    @Autowired
    private ImsBeatUpdateService imsBeatUpdateService;

    // 每个线程处理的数量
    private static final int PRODUCT_COUNT_ON_THREAD = 1000;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.IMS;
    }

    @Override
    public String getTaskName() {
        return "ImsBeatPicJob";
    }

    /**
     * 获取打印的日志是否需要包含线程
     */
    @Override
    public boolean getLogWithThread() {
        return true;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        ImsBeatImageNameFormat.setTaskControls(taskControlList);

        doBeats(taskControlList);
    }

    /**
     * 批量处理价格披露
     *
     * @param taskControlList 任务配置
     * @throws InterruptedException
     */
    private void doBeats(List<TaskControlBean> taskControlList) throws InterruptedException {

        String thread_count = TaskControlUtils.getVal1(taskControlList, Name.thread_count);
        final int THREAD_COUNT = Integer.valueOf(thread_count);

        int limit = PRODUCT_COUNT_ON_THREAD * THREAD_COUNT;

        // 获取固定数量的，固定状态的。价格披露任务
        List<BeatPicBean> beatPics = imsBeatPicDao.getLimitedBeatInfo(limit);

        $info("价格披露：预定抽取数量：%s，实际抽取数量：%s", limit, beatPics.size());

        // 计算需要的拆分次数
        int total = beatPics.size();

        // 拆分集合并创建任务
        List<Runnable> runnableList = new ArrayList<>();

        for (int i = 0; i < total; i += PRODUCT_COUNT_ON_THREAD) {
            int end = i + PRODUCT_COUNT_ON_THREAD;

            if (end > total) end = total;

            List<BeatPicBean> subList = beatPics.subList(i, end);

            runnableList.add(() -> {
                List<BeatPicBean> err = new ArrayList<>();

                for (BeatPicBean bean : subList) {

                    // 进入任务处理之前，重置上一次的信息
                    bean.setComment(Constants.EmptyString);
                    bean.setModifier(getTaskName());

                    switch (bean.getBeat_flg()) {
                        case Waiting:
                            //打标（beat_flg为1）
                            imsBeatUpdateService.beat(bean);
                            break;
                        case Passed:
                        case Cancel:
                            //恢复打标(beat_flg为2或者10)
                            imsBeatRevertService.revert(taskControlList, bean);
                            break;
                    }

                    imsBeatPicDao.updateItem(bean);

                    if (!StringUtils.isEmpty(bean.getComment()))
                        err.add(bean);
                }

                // 最终处理完后检查错误
                if (err.size() > 0) {
                    String allIds = err.stream()
                            .map(BeatPicBean::getBeat_item_id)
                            .map(String::valueOf)
                            .collect(joining(" ], [ "));
                    logIssue("价格披露任务出现异常和错误！", " [ " + allIds + " ]");
                }

                $info("价格披露：子线程处理结束。预计处理数量：%s", subList.size());
            });
        }

        $info("价格披露：线程即将处理。拆分线程数：%s，单线程拆分数：%s", runnableList.size(), PRODUCT_COUNT_ON_THREAD);

        // 运行线程
        runWithThreadPool(runnableList, taskControlList);
    }
}
