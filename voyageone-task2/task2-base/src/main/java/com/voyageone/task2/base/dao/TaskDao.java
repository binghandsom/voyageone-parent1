package com.voyageone.task2.base.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.modelbean.TaskHistoryBean;
import com.voyageone.common.Constants;
import com.voyageone.common.util.DateTimeUtil;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Repository
public class TaskDao extends BaseDao {

    public boolean insertTaskHistory(String taskId,String processType) {

        TaskHistoryBean bean = new TaskHistoryBean();
        bean.setTask_id(taskId);
        bean.setProcess_time(DateTimeUtil.getNow());
        bean.setProcess_type(processType);

        boolean ret = false;
        int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_CORE + "com_bt_task_history_insertTaskHistory", bean);
        if (retCount > 0) {
            ret = true;
        }

        return ret;
    }

    public int updateTaskControl(TaskControlBean bean) {
        return updateTemplate.update(Constants.DAO_NAME_SPACE_CORE + "tm_task_control_update", bean);
    }

    /**
     * 取得后台任务对应的相关属性
     */
    public List<TaskControlBean> getTaskControlList(String task_name) {

        return selectList(Constants.DAO_NAME_SPACE_CORE + "tm_task_control_selectByName", task_name);
    }
    

    /**
     * 取得指定后台任务最后结束的时间
     */
    public String getLastRunTime(String taskID) throws ParseException{
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	List<String> taskControlList = selectList(Constants.DAO_NAME_SPACE_CORE + "com_bt_task_history_getLastRunTime", taskID);
    	if(taskControlList.size() >0)
    	{	
    		String time = df.format(DateTimeUtil.addHours(df.parse(taskControlList.get(0)),-1));
    		return DateTimeUtil.getLocalTime(time,8);
    	}else{
    		Calendar calendar = DateTimeUtil.getCustomCalendar(8);
			calendar.add(Calendar.DATE, -1);
			return df.format(calendar.getTime());
    	}
    }

}
