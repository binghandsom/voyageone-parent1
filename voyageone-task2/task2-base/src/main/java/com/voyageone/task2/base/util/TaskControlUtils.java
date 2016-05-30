package com.voyageone.task2.base.util;

import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class TaskControlUtils {

    private static final Logger logger = LoggerFactory.getLogger(TaskControlUtils.class);

    private static Set<String> localIPs = CommonUtil.getLocalIPs();

    /**
     * 取得任务是否可以运行的标志位
     *
     * @param taskControlList List
     * @return 运行标志位
     */
    public static String getRunFlag(List<TaskControlBean> taskControlList) {

        String strRunFlag = TaskControlEnums.Flag.NO.getIs();
        for (TaskControlBean taskControlBean : taskControlList) {

            if (TaskControlEnums.Name.run_flg.toString().equals(taskControlBean.getCfg_name())) {
                strRunFlag = taskControlBean.getCfg_val1();
                break;
            }

        }

        return strRunFlag;
    }

    /**
     * 取得任务是否可以运行的IP
     *
     * @param taskControlList List
     * @return 运行标志位
     */
    public static String[] getRunIP(List<TaskControlBean> taskControlList) {

        String[] strRunIPArr = new String[0];
        for (TaskControlBean taskControlBean : taskControlList) {

            if (TaskControlEnums.Name.run_ip.toString().equals(taskControlBean.getCfg_name())) {
                String strRunIP = taskControlBean.getCfg_val1();
                if (!StringUtils.isEmpty(strRunIP)) {
                    strRunIPArr = strRunIP.split(",");
                }
                break;
            }

        }

        return strRunIPArr;
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
     * @param name            配置名
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
     * 取得配置属性对应的结束时间(单个)
     *
     * @param taskControlList List
     * @return 对应值2
     */
    public static String getEndTime(List<TaskControlBean> taskControlList, TaskControlEnums.Name name, String val1) {

        return taskControlList.stream()
                .filter(taskControlBean -> taskControlBean.getCfg_name().equals(name.toString()) && taskControlBean.getCfg_val1().equals(val1))
                .map(bean -> {
                    if (bean.getEnd_time() == null) {
                        return "";
                    } else {
                        return bean.getEnd_time();
                    }
                }).findFirst().orElse(null);
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
     * @param checkIP         是否checkIP
     * @return 是否可运行
     */
    public static boolean isRunnable(List<TaskControlBean> taskControlList, boolean checkIP) {
        return isRunnable(taskControlList, getTaskName(taskControlList), checkIP);
    }

    /**
     * 取得任务是否可以运行的标志位
     *
     * @param taskControlList job 配置
     * @param taskCheck       job 的 task name
     * @return 是否可运行
     */
    public static boolean isRunnable(List<TaskControlBean> taskControlList, String taskCheck) {
        return isRunnable(taskControlList, taskCheck, false);
    }

    /**
     * 取得任务是否可以运行的标志位
     *
     * @param taskControlList job 配置
     * @param taskCheck       job 的 task name
     * @param checkIP         是否checkIP
     * @return 是否可运行
     */
    public static boolean isRunnable(List<TaskControlBean> taskControlList, String taskCheck, boolean checkIP) {
        if (taskControlList.size() == 0) {
            logger.info("TASK配置表没有设定，无法执行此任务: " + taskCheck);
            return false;
        }
        // check runflag
        String runFlag = getRunFlag(taskControlList);
        logger.info(taskCheck + " RUN FLAG 为" + runFlag);
        if (runFlag.equals(TaskControlEnums.Flag.NO.getIs())) {
            logger.info("退出，不需要执行此任务: " + taskCheck);
            return false;
        }
        // check ip
        if (checkIP) {
            boolean isExistIP = false;
            String[] runIPs = getRunIP(taskControlList);
            for (String runIP : runIPs) {
                if (localIPs.contains(runIP)) {
                    isExistIP = true;
                    break;
                }
            }

            if (runIPs.length > 0) {
                if (!isExistIP) {
                    logger.info(String.format("%s 退出，ip check fail:%s", taskCheck, Arrays.toString(runIPs)));
                }
                return isExistIP;
            }
        }
        return true;
    }
}
