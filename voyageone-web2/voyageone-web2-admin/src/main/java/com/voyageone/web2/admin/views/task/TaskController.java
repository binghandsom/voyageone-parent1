package com.voyageone.web2.admin.views.task;

import java.util.Arrays;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.service.bean.com.ComMtTaskBean;
import com.voyageone.service.impl.com.task.TaskService;
import com.voyageone.service.model.com.ComMtTaskModel;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.task.TaskFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/9
 */
@RestController
@RequestMapping(value = AdminUrlConstants.Task.Self.ROOT, method = RequestMethod.POST)
public class TaskController extends AdminController {
	
	@Resource(name = "AdminTaskService")
	private TaskService taskService;
	
	@RequestMapping(AdminUrlConstants.Task.Self.SEARCH_TASK_BY_PAGE)
	public AjaxResponse searchTaskByPage(@RequestBody TaskFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索任务信息
		PageModel<ComMtTaskBean> taskPage = taskService.searchTypeByPage(form.getTaskType(), form.getTaskName(),
				form.getTaskComment(), form.getPageNum(), form.getPageSize());
		
		return success(taskPage);
	}
	
	@RequestMapping(AdminUrlConstants.Task.Self.ADD_TASK)
	public AjaxResponse addTask(@RequestBody TaskFormBean form) {
		return addOrUpdateTask(form, true);
	}
	
	@RequestMapping(AdminUrlConstants.Task.Self.UPDATE_TASK)
	public AjaxResponse updateTask(@RequestBody TaskFormBean form) {
		return addOrUpdateTask(form, false);
	}
	
	public AjaxResponse addOrUpdateTask(TaskFormBean form, boolean append) {
		// 验证参数
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getTaskType()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getTaskName()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getTaskComment()));

		// 保存任务信息
		ComMtTaskModel taskModel = new ComMtTaskModel();
		BeanUtils.copyProperties(form, taskModel);
		taskService.addOrUpdateType(taskModel, form.getRunFlg(), getUser().getUserName(), append);
		
		return success(true);
	}
	
	@RequestMapping(AdminUrlConstants.Task.Self.DELETE_TASK)
	public AjaxResponse deleteTask(@RequestBody Integer[] taskIds) {
		// 验证参数
		Preconditions.checkArgument(ArrayUtils.isNotEmpty(taskIds));
		// 删除任务信息
		taskService.deleteTask(Arrays.asList(taskIds));
		
		return success(true);
	}

}
