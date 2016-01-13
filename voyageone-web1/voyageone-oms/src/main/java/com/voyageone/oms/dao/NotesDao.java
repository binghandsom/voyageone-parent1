package com.voyageone.oms.dao;

import java.util.HashMap;
import java.util.List;

import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.oms.formbean.OutFormOrderdetailNotes;
import com.voyageone.oms.modelbean.NotesBean;

@Repository
public class NotesDao extends BaseDao {
	
	/**
	 * 获得订单Notes信息
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailNotes> getOrderNotesInfo(String orderNumber) {
		 List<OutFormOrderdetailNotes> orderNotesList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_notes_getOrderNotesInfo", orderNumber);
		
		return orderNotesList;
	}
	
	/**
	 * 获得订单Notes信息，根据一组（order_number）
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailNotes> getOrderNotesInfo(List<String> orderNumberList) {
		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("orderNumberList", orderNumberList);
		
		List<OutFormOrderdetailNotes> orderNotesList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_notes_getOrderNotesInfoByOrderNumList", paraIn);
		
		return orderNotesList;
	}
	
	/**
	 * 获得订单Notes信息，根据一组（sourceOrderId）
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailNotes> getOrderNotesInfoBySourceOrderId(String sourceOrderId) {
		
		List<OutFormOrderdetailNotes> orderNotesList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_notes_getOrderNotesInfoBySourceOrderId", sourceOrderId);
		
		return orderNotesList;
	}
	
	/**
	 * 订单Note追加
	 * 
	 * @return
	 */
	public boolean insertNotesInfo(NotesBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_notes_insertNotesInfo", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}
}
