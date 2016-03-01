package com.voyageone.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.wms.formbean.FormReturn;
import com.voyageone.wms.formbean.FormReturnDownloadBean;
import com.voyageone.wms.modelbean.ReservationBean;
import com.voyageone.wms.modelbean.ReturnBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**   
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.dao]  
 * @ClassName    [ReturnDao]   
 * @Description  [RetrunDao类]   
 * @Author       [sky]   
 * @CreateDate   [20150427]   
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]  
 * @Version      [v1.0] 
 */
@Repository
public class ReturnDao extends BaseDao {

    /**
     * @Description 获得returnList
     * @param formReturn bean对象
     * @return List<FormReturn>
     */
	public List<FormReturn> getReturnList(FormReturn formReturn) {
		return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_return_getReturnList", formReturn);
	}

	/**
	 * @Description 获得returnInfo
	 * @param return_id
	 * @return FormReturn
	 */
	public FormReturn getReturnInfoByReturnId(int return_id) {
		return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_return_getReturnInfoByReturnId", return_id);
	}

    /**
     * @Description 获得returnListSize
     * @param formReturn bean对象
     * @return Integer
     */
	public int getReturnListSize(FormReturn formReturn) {
		return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_return_getReturnListSize", formReturn);
	}

    /**
     * @Description 获得returnListSize
     * @param formReturn bean对象
     * @return boolean
     */
	public boolean changeStatus(FormReturn formReturn) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_return_changeStatus", formReturn);
		if (retCount > 0) {
			ret = true;
		}
		return ret;
	}

    /**
     * @Description 获得ProcessinSessionName
     * @return List<String>
     */
	public List<FormReturn> getProcessinSessionName(FormReturn formReturn) {
		return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_return_getProcessingSessionName", formReturn);
	}
	
    /**
     * @Description 根据returnSessionId获得sessionInfo
     * @param formReturn bean对象
     * @return List<FormReturn>
     */
	public List<FormReturn> getSessionInfo(FormReturn formReturn) {
		return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_return_getSessionInfo", formReturn);
	}

    /**
     * @Description 根据orderNo获得orderInfo(sku)
     * @param formReturn bean对象
     * @return List<FormReturn>
     */
	public List<FormReturn> getOrderInfoByOrdNo(FormReturn formReturn) {
		return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_return_getOrderInfo", formReturn);
	}

	/**
	 * @Description 根据传入的值获得真正的OrderNumber
	 * @param formReturn 扫描值
	 * @return String
	 */
	public String getOrderNumber(FormReturn formReturn) {
		return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_return_getOrderNumber", formReturn);
	}

    /**
     * @Description 插入return记录到wms_bt_return表
     * @param list formReturn bean对象集合
     * @return boolean
     */
	public boolean insertReturnInfo(List<FormReturn> list) {
		boolean ret = false;  
		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_return_insertReturnInfo", list);
	    if(retCount == list.size()){
	    	ret = true;
	    }
	    return ret;
	}

    /**
     * @Description 创建returnSession
     * @param formReturn bean对象
     * @return boolean
     */
	public String createReturnSession(FormReturn formReturn) {
		String returnSessionId = "";
		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_return_createReturnSession", formReturn);
		
		if (retCount > 0) {
			returnSessionId = formReturn.getReturn_session_id();
		} 
		return returnSessionId;
	}

    /**
     * @Description 将tt_retrun_sessin表的状态从porcecing变成done
     * @param formReturn bean对象
     * @return boolean
     */
	public boolean changeSessionStatus(FormReturn formReturn) {
		boolean res = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_return_changeSessionStatus", formReturn);
		if (retCount > 0) {
			res = true;
		} 
		return res;
	}
	
    /**
     * @Description 将tt_reservation的对应记录的状态置成“return"
     * @param formReturn bean对象
     * @return boolean
     */
	public boolean changeReservationStatus(FormReturn formReturn) {
		boolean res = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_return_changeReservationStatus", formReturn);
		if (retCount > 0) {
			res = true;
		}
		return res;
	}

    /**
     * @Description 检查List里面return记录对应的在reservation表是否都已经ColoseDay
     * @param returnSessionId returnSessionId
     * @return boolean true:存在未close的session;
     */
	public boolean checkCloseDayFlg(String returnSessionId) {
		int retCount = selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_return_checkCloseDayFlg", returnSessionId);
		return retCount > 0;
	}

    /**
     * @Description 保存itemEdit编辑的信息
     * @param formReturn bean对象
     * @return boolean true:存在未close的session; 
     */
	public boolean saveItemEdit(FormReturn formReturn) {
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_return_saveItemEdit", formReturn);
		return retCount > 0;
	}

    /**
     * @Description 移除returnInfo
     * @param resId reservavionId
     * @return boolean
     */
	public boolean removeReturnInfo(String resId) {
		int retCount = updateTemplate.delete(Constants.DAO_NAME_SPACE_WMS + "wms_return_removeReturnInfo", resId);
		return retCount > 0;
	}

    /**
     * @Description returnSessionList Search
     * @param formReturn 对象
     * @return list
     */
	public List<FormReturn> doSessionListSearch(FormReturn formReturn) {
		return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_return_sessionListSearch", formReturn);
	}

    /**
     * @Description returnSessionList Search
     * @param formReturn 对象
     * @return int
     */
	public int getSessionListSize(FormReturn formReturn) {
		return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_return_getSessionListSizeSize", formReturn);
	}

	/**
	 * @Description downloadList Search
	 * @param formReturn 对象
	 * @return list
	 */
	public List<FormReturnDownloadBean> getDownloadList(FormReturn formReturn) {
		return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_return_getDownloadList", formReturn);
	}

	/**
	 * 更新状态
	 * @param returnInfo 更新记录
	 * @return int
	 */
	public int changeReturn(ReturnBean returnInfo) {

		return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_bt_return_changeReturn", returnInfo);
	}
}
