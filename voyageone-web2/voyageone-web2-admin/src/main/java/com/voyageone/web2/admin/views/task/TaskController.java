package com.voyageone.web2.admin.views.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.voyageone.service.bean.com.TmTaskControlBean;
import com.voyageone.service.impl.com.task.TaskService;
import com.voyageone.service.model.com.ComMtTaskModel;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.service.model.com.TmTaskControlKey;
import com.voyageone.service.model.com.TmTaskControlModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.system.CommonConfigFormBean;
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

	//---------------------------------------------------------------------
	// 任务信息
	//---------------------------------------------------------------------
	
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

	//---------------------------------------------------------------------
	// 任务配置信息
	//---------------------------------------------------------------------
	
	@RequestMapping(AdminUrlConstants.Task.Self.SEARCH_TASK_CONFIG_BY_PAGE)
	public AjaxResponse searchTaskConfigByPage(@RequestBody CommonConfigFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索任务配置信息
		PageModel<TmTaskControlBean> taskConfigPage = taskService.searchTaskConfigByPage(form.getTaskId(),
				form.getCfgName(), form.getCfgVal(), form.getPageNum(), form.getPageSize());
		
		return success(taskConfigPage);
	}
	
	public AjaxResponse addTaskConfig(@RequestBody CommonConfigFormBean form) {
		// 验证参数
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getTaskId()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgName()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgVal1()));

		// 添加任务配置信息
		TmTaskControlModel model = new TmTaskControlModel();
		BeanUtils.copyProperties(form, model);
		taskService.addTaskConfig(model);
		
		return success(true);
	}
	
	public AjaxResponse deleteTaskConfig(@RequestBody CommonConfigFormBean[] forms) {
		// 验证参数
		Preconditions.checkArgument(ArrayUtils.isNotEmpty(forms), "没有可删除的任务配置信息");
		List<TmTaskControlKey> taskCtrlKeys = new ArrayList<TmTaskControlKey>();
		for (CommonConfigFormBean form : forms) {
			Preconditions.checkArgument(StringUtils.isNotBlank(form.getTaskId()));
			Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgName()));
			Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgVal1()));
			Preconditions.checkNotNull(form.getCfgVal2());
			// 设置删除主键
			TmTaskControlKey taskCtrlKey = new TmTaskControlKey();
			BeanUtils.copyProperties(form, taskCtrlKey);
			taskCtrlKeys.add(taskCtrlKey);
		}
		// 删除任务配置信息
		taskService.deleteTaskConfig(taskCtrlKeys);
		
		return success(true);
	}

}
