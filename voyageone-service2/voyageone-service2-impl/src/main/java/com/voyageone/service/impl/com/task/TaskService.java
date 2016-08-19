package com.voyageone.service.impl.com.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.voyageone.service.model.com.PageModel;
import com.voyageone.service.model.com.TmTaskControlKey;
import com.voyageone.service.model.com.TmTaskControlModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/18
 */
@Service("AdminTaskService")
public class TaskService extends BaseService {
	
	private static final String RUN_FLG_NAME = "run_flg";
	
	private static final String TASK_ATTR_EMPTY_VALUE = "";
	
	private static final String DEFAULT_TASK_TYPE_NAME = "SchedulingTask";
	
	@Autowired
	private ComMtTaskDao taskDao;
	
	@Autowired
	private TmTaskControlDao taskCtrlDao;
	
	@Autowired
	private ComMtTaskDaoExt taskDaoExt;
	
	@Autowired
	private ComMtValueDaoExt typeAttrDaoExt;

	public PageModel<ComMtTaskBean> searchTypeByPage(String taskType, String taskName, String taskComment,
			Integer pageNum, Integer pageSize) {
		PageModel<ComMtTaskBean> pageModel = new PageModel<ComMtTaskBean>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("taskType", taskType);
		params.put("taskName", taskName);
		params.put("taskComment", taskComment);
		params.put("taskAttrName", RUN_FLG_NAME);
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			pageModel.setCount(taskDaoExt.selectTypeCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询任务信息
		pageModel.setResult(taskDaoExt.selectTypeByPage(params));
		
		return pageModel;
	}

	@VOTransactional
	public void addOrUpdateType(ComMtTaskModel taskModel, String runFlg, String username, boolean append) {
		boolean success = false;
		// 保存任务信息
		if (append) {
			// 添加任务信息
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
				TmTaskControlModel taskCtrlKey = taskCtrlDao.selectOne(params);
				// 删除任务运行属性值
				success = taskCtrlDao.delete(taskCtrlKey) > 0;
			}
		}
		
		// 添加任务运行属性值
		if (success) {
			TmTaskControlModel taskCtrlModel = new TmTaskControlModel();
			taskCtrlModel.setTaskId(taskModel.getTaskName());
			taskCtrlModel.setCfgName(RUN_FLG_NAME);
			taskCtrlModel.setCfgVal1(runFlg);
			taskCtrlModel.setCfgVal2(TASK_ATTR_EMPTY_VALUE);
			success = taskCtrlDao.insert(taskCtrlModel) > 0;
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

	public PageModel<TmTaskControlBean> searchTaskConfigByPage(String taskId, String cfgName, String cfgVal,
			Integer pageNum, Integer pageSize) {
		PageModel<TmTaskControlBean> pageModel = new PageModel<TmTaskControlBean>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("taskId", taskId);
		params.put("cfgName", cfgName);
		params.put("cfgVal", cfgVal);
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			pageModel.setCount(taskDaoExt.selectTaskConfigCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询任务配置信息
		pageModel.setResult(taskDaoExt.selectTaskConfigByPage(params));
		
		return pageModel;
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

}
