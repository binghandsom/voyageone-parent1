package com.voyageone.service.impl.com.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.com.ComMtTaskBean;
import com.voyageone.service.dao.com.ComMtTaskDao;
import com.voyageone.service.dao.com.TmTaskControlDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.ComMtTaskModel;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.service.model.com.TmTaskControlModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/18
 */
@Service("AdminTaskService")
public class TaskService extends BaseService {
	
	private static final String RUN_FLG_NAME = "run_flg";
	
	private static final String TASK_ATTR_EMPTY_VALUE = "";
	
	@Autowired
	private ComMtTaskDao taskDao;
	
	@Autowired
	private TmTaskControlDao taskCtrlDao;

	public PageModel<ComMtTaskBean> searchTypeByPage(String taskType, String taskName, String taskComment,
			Integer pageNum, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@VOTransactional
	public void addOrUpdateType(ComMtTaskModel taskModel, String runFlg, String username, boolean append) {
		TmTaskControlModel taskCtrlModel = null;
		boolean success = false;
		// 保存任务信息
		if (append) {
			// 添加任务信息
			taskModel.setCreater(username);
			taskModel.setModifier(username);
			Integer taskId = taskDao.insert(taskModel);
			if (taskId > 0) {
				// 添加任务运行属性值
				taskCtrlModel = new TmTaskControlModel();
				taskCtrlModel.setTaskId(String.valueOf(taskId));
			}
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
				params.put("taskId", String.valueOf(taskModel.getTaskId()));
				params.put("cfgName", RUN_FLG_NAME);
				TmTaskControlModel taskCtrlKey = taskCtrlDao.selectOne(params);
				// 删除任务运行属性值
				boolean isDeleted = false;
				if (taskCtrlKey == null) {
					isDeleted = true;
				} else {
					isDeleted = taskCtrlDao.delete(taskCtrlKey) > 0;
				}
				// 添加任务运行属性值
				if (isDeleted) {
					taskCtrlModel = new TmTaskControlModel();
					taskCtrlModel.setTaskId(String.valueOf(taskModel.getTaskId()));
				}
			}
		}
		
		// 添加任务运行属性值
		if (taskCtrlModel != null) {
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
			if (taskDao.delete(taskId) <= 0) {
				throw new BusinessException("删除任务信息失败");
			} else {
				// 检索任务的属性信息
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("taskId", String.valueOf(taskIds));
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

}
