package com.voyageone.service.impl.com.task;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.com.ComMtTaskBean;
import com.voyageone.service.bean.com.TmTaskControlBean;
import com.voyageone.service.dao.com.ComMtTaskDao;
import com.voyageone.service.dao.com.TmTaskControlDao;
import com.voyageone.service.daoext.com.ComMtTaskDaoExt;
import com.voyageone.service.daoext.com.ComMtValueDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.ComMtTaskModel;
import com.voyageone.service.model.com.ComMtValueModel;
import com.voyageone.service.bean.com.PaginationResultBean;
import com.voyageone.service.model.com.TmTaskControlKey;
import com.voyageone.service.model.com.TmTaskControlModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/18
 */
@Service("AdminTaskService")
public class TaskManagementService extends BaseService {
	
	private static final String RUN_FLG_NAME = "run_flg";
	
	private static final String CHANNEL_ID_NAME = "order_channel_id";
	
	private static final String TASK_ATTR_EMPTY_VALUE = "";
	
	private static final String DEFAULT_TASK_COMMENT = "Run flag of task";
	
	private static final String DEFAULT_TASK_TYPE_NAME = "SchedulingTask";
	
	@Autowired
	private ComMtTaskDao taskDao;
	
	@Autowired
	private TmTaskControlDao taskCtrlDao;
	
	@Autowired
	private ComMtTaskDaoExt taskDaoExt;
	
	@Autowired
	private ComMtValueDaoExt typeAttrDaoExt;

	public List<ComMtTaskModel> getAllTask() {
		return taskDao.selectList(Collections.emptyMap());
	}
	
	public List<ComMtTaskBean> searchTaskByChannelId(String channelId) {
		List<ComMtTaskBean> tasks = taskDaoExt.searchTaskByChannelId(CHANNEL_ID_NAME, RUN_FLG_NAME, channelId);
		if (CollectionUtils.isNotEmpty(tasks)) {
			tasks.forEach(task -> {
				task.setTaskConfig(searchTaskConfigByPage(String.valueOf(task.getTaskId()),
						null, null, null, null).getResult());
			});
		}
		
		return tasks;
	}

	public PaginationResultBean<ComMtTaskBean> searchTaskByPage(String taskType, String taskName, String taskComment,
																Integer pageNum, Integer pageSize) {
		PaginationResultBean<ComMtTaskBean> paginationResultBean = new PaginationResultBean<ComMtTaskBean>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("taskType", taskType);
		params.put("taskName", taskName);
		params.put("taskComment", taskComment);
		params.put("taskAttrName", RUN_FLG_NAME);
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			paginationResultBean.setCount(taskDaoExt.selectTypeCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询任务信息
		paginationResultBean.setResult(taskDaoExt.selectTypeByPage(params));
		
		return paginationResultBean;
	}

	@VOTransactional
	public void addOrUpdateTask(ComMtTaskModel taskModel, String runFlg, String username, boolean append) {
		// 设置任务运行属性
		TmTaskControlModel newTaskCtrl = new TmTaskControlModel();
		newTaskCtrl.setTaskId(taskModel.getTaskName());
		newTaskCtrl.setCfgName(RUN_FLG_NAME);
		newTaskCtrl.setCfgVal1(runFlg);
		
		boolean success = false;
		// 保存任务信息
		if (append) {
			// 添加任务信息
			newTaskCtrl.setComment(DEFAULT_TASK_COMMENT);
			newTaskCtrl.setCfgVal2(TASK_ATTR_EMPTY_VALUE);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("taskName", taskModel.getTaskName());
			if (taskDao.selectOne(params) != null) {
				throw new BusinessException("存在同名的任务名[" + taskModel.getTaskName() + "]");
			}
			taskModel.setCreater(username);
			taskModel.setModifier(username);
			success = taskDao.insert(taskModel) > 0;
		} else {
			// 更新任务信息
			ComMtTaskModel taskInfo = taskDao.select(taskModel.getTaskId());
			if (taskInfo == null) {
				throw new BusinessException("更新的任务信息不存在");
			}
			taskModel.setModifier(username);
			if (taskDao.update(taskModel) > 0) {
				// 检索任务运行属性值
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("taskId", taskModel.getTaskName());
				params.put("cfgName", RUN_FLG_NAME);
				TmTaskControlModel taskCtrl = taskDaoExt.selectTaskConfig(params);
				if (taskCtrl != null) {
					newTaskCtrl.setCfgVal2(taskCtrl.getCfgVal2());
					newTaskCtrl.setComment(taskCtrl.getComment());
				}
				// 删除任务运行属性值
				success = taskCtrlDao.delete(taskCtrl) > 0;
			}
		}
		
		// 添加任务运行属性值
		if (success) {
			success = taskCtrlDao.insert(newTaskCtrl) > 0;
		}
		
		if (!success) {
			throw new BusinessException("保存任务信息失败");
		}
	}

	@VOTransactional
	public void deleteTask(List<Integer> taskIds) {
		for (Integer taskId : taskIds) {
			// 删除任务信息
			ComMtTaskModel taskModel = taskDao.select(taskId);
			if (taskModel == null) {
				throw new BusinessException("删除的任务信息不存在");
			}
			if (taskDao.delete(taskId) <= 0) {
				throw new BusinessException("删除任务信息失败");
			} else {
				// 检索任务的属性信息
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("taskId", taskModel.getTaskName());
				List<TmTaskControlModel> taskCtrlList = taskCtrlDao.selectList(params);
				// 删除任务的属性信息
				for (TmTaskControlModel taskCtrl : taskCtrlList) {
					if (taskCtrlDao.delete(taskCtrl) <= 0) {
						throw new BusinessException("删除任务属性信息失败");
					}
				}
			}
		}
	}

	public PaginationResultBean<TmTaskControlBean> searchTaskConfigByPage(String taskId, String cfgName, String cfgVal,
																		  Integer pageNum, Integer pageSize) {
		PaginationResultBean<TmTaskControlBean> paginationResultBean = new PaginationResultBean<TmTaskControlBean>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("taskId", taskId);
		params.put("cfgName", cfgName);
		params.put("cfgVal", cfgVal);
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			paginationResultBean.setCount(taskDaoExt.selectTaskConfigCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询任务配置信息
		paginationResultBean.setResult(taskDaoExt.selectTaskConfigByPage(params));
		
		return paginationResultBean;
	}

	public void addTaskConfig(TmTaskControlModel model) {
		if (model.getCfgVal2() == null) {
			model.setCfgVal2(TASK_ATTR_EMPTY_VALUE);
		}
		if (taskCtrlDao.select(model) != null) {
			throw new BusinessException("添加的任务配置信息已存在");
		}
		if (taskCtrlDao.insert(model) <= 0) {
			throw new BusinessException("添加任务配置信息失败");
		}
	}

	@VOTransactional
	public void deleteTaskConfig(List<TmTaskControlKey> taskCtrlKeys) {
		for (TmTaskControlKey taskCtrlKey : taskCtrlKeys) {
			if (taskCtrlDao.delete(taskCtrlKey) <= 0) {
				throw new BusinessException("删除任务配置信息失败");
			}
		}
	}

	public List<ComMtValueModel> getAllTaskType() {
		return typeAttrDaoExt.selectTypeAttributeByTypeName(DEFAULT_TASK_TYPE_NAME);
	}

	@VOTransactional
	public void startOrStopTask(String taskName, boolean runFlg) {
		// 检索任务运行属性值
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("taskId", taskName);
		params.put("cfgName", RUN_FLG_NAME);
		TmTaskControlModel taskCtrl = taskDaoExt.selectTaskConfig(params);
		// 删除任务运行属性值
		if (taskCtrlDao.delete(taskCtrl) <= 0) {
			throw new BusinessException("删除任务的运行属性失败");
		}
		// 添加任务运行属性值
		TmTaskControlModel newTaskCtrl = new TmTaskControlModel();
		newTaskCtrl.setTaskId(taskName);
		newTaskCtrl.setCfgName(RUN_FLG_NAME);
		newTaskCtrl.setCfgVal1(runFlg ? "1" : "0");
		newTaskCtrl.setCfgVal2(taskCtrl.getCfgVal2());
		newTaskCtrl.setComment(taskCtrl.getComment());
		if (taskCtrlDao.insert(newTaskCtrl) <= 0) {
			throw new BusinessException("添加任务的运行属性失败");
		}
	}

}
