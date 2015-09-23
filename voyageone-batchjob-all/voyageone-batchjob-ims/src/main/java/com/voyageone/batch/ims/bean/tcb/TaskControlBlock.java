package com.voyageone.batch.ims.bean.tcb;

import java.util.List;

/**
 * Created by Leo on 15-6-8.
 */
public class TaskControlBlock {
    //protected TaskStatus taskStatus;

   //当前任务所在的队列
    protected List<TaskControlBlock> tcb_queue;

    protected Object runningParam;

    /*
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }
    */

    public List<TaskControlBlock> getTcb_queue() {
        return tcb_queue;
    }

    public void setTcb_queue(List<TaskControlBlock> tcb_queue) {
        this.tcb_queue = tcb_queue;
    }

    public Object getRunningParam() {
        return runningParam;
    }

    public void setRunningParam(Object runningParam) {
        this.runningParam = runningParam;
    }

    /*
    @Override
    public String toString() {
        return "[TaskStatus: " + taskStatus + ", tcb_queue:" + tcb_queue + "]";
    }
    */
}
