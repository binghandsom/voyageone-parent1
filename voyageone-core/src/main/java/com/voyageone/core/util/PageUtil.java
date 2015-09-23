/**
 * 
 */
package com.voyageone.core.util;

import com.voyageone.core.ajax.AjaxRequestBean;


/**
 * @author jacky
 *
 */
public class PageUtil {

	public static boolean pageInit(AjaxRequestBean bean, int count) {
		// 当前页数
		int page = 1;
		// 每页记录数
		int rows = 10;
		if (bean.getPage() != 0) {
			page = bean.getPage();
		} else {
			bean.setPage(page);
		}
		if (bean.getRows() != 0) {
			rows = bean.getRows();
		} else {
			bean.setRows(rows);
		}
		
		// 总记录数大于0
		if (count > 0) {
			// 当前页起始记录下标
			int offset = (page - 1) * rows;
			// 当前页起始记录下标正常
			if ((offset <= (count - 1)) && (offset >= 0)) {
				bean.setOffset(offset);
				
				return true;
			}
		}
		return false;
	}
	
}
