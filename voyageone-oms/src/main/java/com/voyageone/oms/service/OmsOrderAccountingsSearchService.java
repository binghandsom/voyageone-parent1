package com.voyageone.oms.service;

import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.oms.formbean.OutFormSearchSettlementFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * OMS 订单明细检索service
 * 
 * @author jacky
 *
 */
public interface OmsOrderAccountingsSearchService {

	/**
	 * 账务文件检索
	 *
	 * @param storeId 渠道ID
	 * @param channelId cartID
	 * @param searchDateFrom 检索日开始
	 * @param searchDateTo 检索日终了
	 * @param page 当前页
	 * @param size 页面行数
	 * @param user 当前用户
	 * @return
	 */
	public List<OutFormSearchSettlementFile> searchSettlementFile(String fileType, List<String> storeId, List<String> channelId, String searchDateFrom, String searchDateTo, int page, int size, UserSessionBean user);

	/**
	 * 账务文件件数检索
	 *
	 * @param storeId 渠道ID
	 * @param channelId cartID
	 * @param searchDateFrom 检索日开始
	 * @param searchDateTo 检索日终了
	 * @return
	 */
	public  int getSearchSettlementFileCount(String fileType, List<String> storeId, List<String> channelId, String searchDateFrom, String searchDateTo);
}
