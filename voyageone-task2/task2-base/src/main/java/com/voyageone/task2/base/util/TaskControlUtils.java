package com.voyageone.task2.base.util;

import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class TaskControlUtils {

    private static final Logger logger = LoggerFactory.getLogger(TaskControlUtils.class);

    /**
     * 取得任务是否可以运行的标志位
     *
     * @param taskControlList List
     * @return 运行标志位
     */
    public static String getRunFlag(List<TaskControlBean> taskControlList) {

        String strRunFlag = TaskControlEnums.Flag.NO.getIs();
        for (TaskControlBean taskControlBean : taskControlList) {

            if (taskControlBean.getCfg_name().equals(TaskControlEnums.Name.run_flg.toString())) {
                strRunFlag = taskControlBean.getCfg_val1();
                break;
            }

        }

        return strRunFlag;
    }

    /**
     * 取得任务ID
     *
     * @param taskControlList List
     * @return 任务ID
     */
    public static String getTaskId(List<TaskControlBean> taskControlList) {
        return taskControlList.get(0).getTask_id();
    }

    /**
     * 取得任务名
     *
     * @param taskControlList List
     * @return 任务名
     */
    public static String getTaskName(List<TaskControlBean> taskControlList) {
        return taskControlList.get(0).getTask_name();
    }

    /**
     * 取得配置属性对应的值1(单个)
     *
     * @param taskControlList List
     * @return 对应值1
     */
    public static String getVal1(List<TaskControlBean> taskControlList, TaskControlEnums.Name name) {
        String stVal = "";
        for (TaskControlBean taskControlBean : taskControlList) {

            if (taskControlBean.getCfg_name().equals(name.toString())) {
                stVal = taskControlBean.getCfg_val1();
                break;
            }

        }
        return stVal;
    }

    /**
     * 取得配置属性对应的值1(复数)
     *
     * @param taskControlList List
     * @return 对应值1（列表）
     */
    public static List<String> getVal1List(List<TaskControlBean> taskControlList, TaskControlEnums.Name name) {
        return taskControlList.stream()
                .filter(taskControlBean -> taskControlBean.getCfg_name().equals(name.toString()))
                .map(TaskControlBean::getCfg_val1)
                .collect(Collectors.toList());
    }

    /**
     * 获取配置名为 name 的，完整的 val1 和 val2 的值
     *
     * @param taskControlList 总配置集合
     * @param name 配置名
     * @return val1 和 val2 的配置表
     */
    public static List<TaskControlBean> getVal1s(List<TaskControlBean> taskControlList, TaskControlEnums.Name name) {
        return taskControlList.stream()
                .filter(taskControlBean -> taskControlBean.getCfg_name().equals(name.toString()))
                .collect(toList());
    }

    /**
     * 取得配置属性对应的值2(单个)
     *
     * @param taskControlList List
     * @return 对应值2
     */
    public static String getVal2(List<TaskControlBean> taskControlList, TaskControlEnums.Name name, String val1) {
        String stVa2 = "";
        for (TaskControlBean taskControlBean : taskControlList) {

            if (taskControlBean.getCfg_name().equals(name.toString()) &&
                    taskControlBean.getCfg_val1().equals(val1)) {
                stVa2 = taskControlBean.getCfg_val2();
                break;
            }

        }
        return stVa2;
    }

    /**
     * 取得任务是否可以运行的标志位
     *
     * @param taskControlList job 配置
     * @return 是否可运行
     */
    public static boolean isRunnable(List<TaskControlBean> taskControlList) {
        return isRunnable(taskControlList, getTaskName(taskControlList));
    }

    /**
     * 取得任务是否可以运行的标志位
     *
     * @param taskControlList job 配置
     * @param taskCheck       job 的 task name
     * @return 是否可运行
     */
    public static boolean isRunnable(List<TaskControlBean> taskControlList, String taskCheck) {
        if (taskControlList.size() == 0) {
            logger.info("TASK配置表没有设定，无法执行此任务: " + taskCheck);
            return false;
        }
        String runFlag = getRunFlag(taskControlList);
        logger.info(taskCheck + " RUN FLAG 为" + runFlag);
        if (runFlag.equals(TaskControlEnums.Flag.NO.getIs())) {
            logger.info("退出，不需要执行此任务: " + taskCheck);
            return false;
        }

        return true;
    }
}
