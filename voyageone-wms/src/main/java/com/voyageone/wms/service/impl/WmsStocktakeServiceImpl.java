package com.voyageone.wms.service.impl;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.*;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.beans.MasterInfoBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.core.CoreConstants;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.PermissionBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.core.util.PageUtil;
import com.voyageone.wms.WmsConstants;
import com.voyageone.wms.WmsMsgConstants;
import com.voyageone.wms.dao.StocktakeDao;
import com.voyageone.wms.formbean.FormStocktake;
import com.voyageone.wms.service.WmsStocktakeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**   
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.service.impl]  
 * @ClassName    [WmsReturnServiceImpl]   
 * @Description  [return服务类接口实现类]   
 * @Author       [sky]   
 * @CreateDate   [20150427]   
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]  
 * @Version      [v1.0] 
 */
@Service
public class WmsStocktakeServiceImpl implements WmsStocktakeService {

	private static Log logger = LogFactory.getLog(WmsStocktakeServiceImpl.class);

	private static String SUCCESSFULFLG  = "SUCCESSFULFLG", RESMSG = "RESMSG" ,STOCKTAKE_DETAIL_ID = "STOCKTAK_DETAIL_ID";

	@Autowired
	private StocktakeDao stocktakeDao;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
	DefaultTransactionDefinition def =new DefaultTransactionDefinition();

	@Override
	public void sessionListInit(HttpServletRequest request, HttpServletResponse response, Map<String, String> paramMap, UserSessionBean user, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<>();
		//获取用户仓库
		List storeList = user.getCompanyRealStoreList();
		resultMap.put("storeList", storeList);
		//获取页面查询条件下拉框内容
		doGetComBoxInfo(paramMap, resultMap);
		// 获取开始日期（当前日期的一个月前）
		String date_from = DateTimeUtil.parseStr(DateTimeUtil.getLocalTime(DateTimeUtil.addMonths(DateTimeUtil.getDate(), -1), user.getTimeZone()), DateTimeUtil.DEFAULT_DATE_FORMAT);
		resultMap.put("fromDate", date_from);
		// 获取结束日期（当前日期）
		String date_to = DateTimeUtil.parseStr(DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), user.getTimeZone()), DateTimeUtil.DEFAULT_DATE_FORMAT);
		resultMap.put("toDate", date_to);
		//结果集发送到页面
		resultDeal(request, response, resultMap);
	}

	@Override
	public void getStocktakeSessionList(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		List<FormStocktake> sessionList = new ArrayList<>();
		FormStocktake formStocktake = JsonUtil.jsonToBean(JsonUtil.getJsonString(object), FormStocktake.class);
		//页面传递进来的是本地时间，转化成格林威治时间
		changeDateTimeToGMT(formStocktake, user);
		setFormCommonValue(request, formStocktake, user);
		try{
			int total = stocktakeDao.getSessionListSize(formStocktake);
			resultMap.put("total", total);
			if(PageUtil.pageInit(formStocktake, total)) {
				sessionList = stocktakeDao.getStocktakeSessionList(formStocktake);
			}
			//将结果集中的时间字段由格林威治时间转化成本地时间
			setLocalDateTime(sessionList, user);
			resultMap.put("sessionList", sessionList);
			resultDeal(request, response, resultMap);
		}catch (Exception e){
			logger.error(e);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
	}

	@Override
	public void newSession(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		FormStocktake formStocktake = JsonUtil.jsonToBean(JsonUtil.getJsonString(object), FormStocktake.class);
		//设置值---------start
		setFormCommonValue(request, formStocktake, user);
		formStocktake.setOrder_channel_id(getChannelIdByStoreId(formStocktake.getStore_id()));
		formStocktake.setStocktake_status("0"); //设置状态为：processing
		formStocktake.setSyn_flg("0");			//设置同步标志为未同步
		formStocktake.setActive(1);				//设置为有效地
		//设置值---------end
		try {
			int newSessionId = stocktakeDao.createSession(formStocktake);
			resultMap.put("newSessionId", newSessionId);
			resultDeal(request, response, resultMap);
		}catch (Exception e){
			logger.error(e);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
	}

	@Override
	public void sectionListInit(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			FormStocktake formStocktake = JsonUtil.jsonToBean(JsonUtil.getJsonString(object), FormStocktake.class);
			setFormCommonValue(request, formStocktake, user);
			FormStocktake newFormStocktake = stocktakeDao.getSessionInfoBySessionId(formStocktake.getStocktake_id());
			int total = stocktakeDao.getSectionListSize(formStocktake);
			List<FormStocktake> sectionList = new ArrayList<>();
			if(PageUtil.pageInit(formStocktake, total)) {
				 sectionList = stocktakeDao.getSectionListBySessionId(formStocktake);
			}
			setLocalDateTime(sectionList, user);
			if(!StringUtils.isEmpty(newFormStocktake.getStocktake_name())){
				newFormStocktake.setStocktake_name(DateTimeUtil.getLocalTime(newFormStocktake.getStocktake_name(), user.getTimeZone()));
			}
			resultMap.put("total", total);
			resultMap.put("sessionName", newFormStocktake.getStocktake_name());
			resultMap.put("sessionId", formStocktake.getStocktake_id());
			resultMap.put("sectionList", sectionList);
			resultMap.put("sessionStatus", newFormStocktake.getStocktake_status());
			resultDeal(request, response, resultMap);
		}catch (Exception e){
			logger.error(e);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
	}

	@Override
	public void sessionDelete(HttpServletRequest request, HttpServletResponse response, String sessionIdStr) {
		Map<String, Object> resultMap = new HashMap<>();
		int sessionIdInt = Integer.parseInt(sessionIdStr);
		boolean flag;
		//开启事物
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			flag = stocktakeDao.deleteSession(sessionIdInt);
			if(flag){
				FormStocktake formStocktake = new FormStocktake();
				formStocktake.setStocktake_id(sessionIdInt);
				int sectionSize = stocktakeDao.getSectionListSize(formStocktake);
				if(sectionSize != 0) {
					flag = stocktakeDao.deleteSectionBySessionId(sessionIdInt);
				}
			}
			if(flag){
				int stocktakeItemSize = stocktakeDao.getStocktakeItemSizeBySessionId(sessionIdInt);
				if(stocktakeItemSize != 0 ) {
					flag = stocktakeDao.deleteStocktakeItemBySessionId(sessionIdInt);
				}
			}
			if(flag){
				transactionManager.commit(status);
			}
		}catch (Exception e){
			logger.error(e);
			transactionManager.rollback(status);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
		if(flag){
			resultMap.put(WmsConstants.Common.RESMSG, "Delete successful!");
		}else{
			transactionManager.rollback(status);
			resultMap.put(WmsConstants.Common.RESMSG, "Delete failed!");
		}
		resultMap.put(WmsConstants.Common.SUCCESEFLG, flag);
		resultDeal(request, response, resultMap);
	}

	@Override
	public void sessionStock(HttpServletRequest request, HttpServletResponse response, String sessionId, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		FormStocktake formStocktake = new FormStocktake();
		formStocktake.setStocktake_id(Integer.parseInt(sessionId));
		//设置状态为Stock; 0:processing; 1:stock; 2:Compare ; 3:Done
		formStocktake.setStocktake_status("1");
		setFormCommonValue(request, formStocktake, user);
		boolean flag = false;
		try {
			if(stockSessionCheck(sessionId)) {
				flag = stocktakeDao.changeSessionStatus(formStocktake);
			}
		}catch (Exception e){
			logger.error(e);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
		if(flag){
			resultMap.put(WmsConstants.Common.RESMSG, "Change to stock successful!");
		}else{
			//resultMap.put(WmsConstants.Common.RESMSG, "Change to stock failed! Please check the section status!");
            throw new BusinessException(WmsMsgConstants.TakeStockMsg.CHANGE_TO_STOCK_FAILED);
		}
		resultMap.put(WmsConstants.Common.SUCCESEFLG, true);
		resultDeal(request, response, resultMap);
	}

	@Override
	public void newSection(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		FormStocktake formStocktake = JsonUtil.jsonToBean(JsonUtil.getJsonString(object), FormStocktake.class);
		setFormCommonValue(request, formStocktake, user);
		int stocktake_detail_id = stocktakeDao.sessionContainSectionCheck(formStocktake);
		//【-1】session不包含section
		if(stocktake_detail_id == -1){
			Map<String, Object> map = createSection(formStocktake);
			resultMap.put("stocktake_detail_id", map.get(STOCKTAKE_DETAIL_ID));
			resultMap.put(WmsConstants.Common.RESMSG, map.get(RESMSG));
					resultMap.put(WmsConstants.Common.SUCCESEFLG, map.get(SUCCESSFULFLG));
		}else{
			//若session包含SectionName则将对应的stocktake_detail_id返回前台
			resultMap.put("stocktake_detail_id", stocktake_detail_id);
			resultMap.put(WmsConstants.Common.SUCCESEFLG, true);
		}
		resultDeal(request, response, resultMap);
	}

	@Override
	public void deleteSection(HttpServletRequest request, HttpServletResponse response, int stocktake_detail_id) {
		Map<String, Object> resultMap = new HashMap<>();
		boolean flag;
		//开启事物
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			flag = stocktakeDao.deleteSectionById(stocktake_detail_id);
			if(flag){
				flag =stocktakeDao.deleteItemByStocktakeDetailId(stocktake_detail_id);
			}
			if(flag){
				transactionManager.commit(status);
				resultMap.put(WmsConstants.Common.RESMSG, "Delete section successful!");
			}else{
				transactionManager.rollback(status);
				resultMap.put(WmsConstants.Common.RESMSG, "Delete section failed!");
			}
		}catch (Exception e){
			logger.error(e);
			transactionManager.rollback(status);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
		resultMap.put(WmsConstants.Common.SUCCESEFLG, flag);
		resultDeal(request, response, resultMap);
	}

	@Override
	public void inventoryInit(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<>();
		try{
			FormStocktake formStocktake = JsonUtil.jsonToBean(JsonUtil.getJsonString(object), FormStocktake.class);
			List<FormStocktake> itemList = new ArrayList<>();
			//int total = stocktakeDao.getSessionSectionItemSize(formStocktake);
			//上面注释原因说明：用主键做count()查询不如下面的获取total的代码效率快
			int row = formStocktake.getRows();
			formStocktake.setRows(0);
			int total = stocktakeDao.getSessionSectionItem(formStocktake).size();
			formStocktake.setRows(row);
			if(PageUtil.pageInit(formStocktake, total)) {
				itemList = stocktakeDao.getSessionSectionItem(formStocktake);
			}
			resultMap.put("total", total);
			resultMap.put("itemList", itemList);
			resultDeal(request, response, resultMap);
		}catch (Exception e){
			logger.error(e);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
	}

	@Transactional
	@Override
	public void upcScan(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<>();
		boolean flag;
		FormStocktake formStocktake = JsonUtil.jsonToBean(JsonUtil.getJsonString(object), FormStocktake.class);
		setFormCommonValue(request,formStocktake, user);
        String paramUpc = formStocktake.getUpc();
        String paramSku = formStocktake.getSku();
        if(StringUtils.isEmpty(paramSku) && StringUtils.isEmpty(paramUpc)){
            throw new BusinessException(WmsMsgConstants.TakeStockMsg.UPCSKU_CANNOT_BOTH_NULL);
        }
        FormStocktake fstNew = getProductByUpcOrSku(formStocktake);
		if(fstNew == null){
            resultMap.put(WmsConstants.Common.SUCCESEFLG, false);
            String msg = "";
            if(!StringUtils.isEmpty(paramUpc)){
                msg = "barCode【" + paramUpc + "】";
            }
            if(!StringUtils.isEmpty(paramSku)){
                msg = msg + "sku【" + paramSku + "】";
            }
			throw new BusinessException(WmsMsgConstants.TakeStockMsg.NONEXISTENT_BARCODE, msg);
		}else{
			//获取对应的Item的信息
			formStocktake.setUpc(fstNew.getUpc());
			formStocktake.setSize(fstNew.getSize());
			formStocktake.setSku(fstNew.getSku());
			formStocktake.setCode(fstNew.getCode());
			flag = stocktakeDao.updateSessionSectionItem(formStocktake);
			if(!flag){
				flag = stocktakeDao.addSessionSectionItem(formStocktake);
			}
			if(flag){
				flag = stocktakeDao.updateSection(formStocktake);
			}
			if(!flag){
				resultMap.put(WmsConstants.Common.RESMSG, "Add or update item faild");
			}
		}
		resultMap.put(WmsConstants.Common.SUCCESEFLG, flag);
		resultDeal(request, response, resultMap);
	}

	@Override
	public void sectionDetailInit(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		FormStocktake formStocktake = JsonUtil.jsonToBean(JsonUtil.getJsonString(object), FormStocktake.class);
		formStocktake.setLocation_name(formStocktake.getSectionName());
		try{
			int row = formStocktake.getRows();
			formStocktake.setRows(0);
			int total = stocktakeDao.getSessionSectionItem(formStocktake).size();
			formStocktake.setRows(row);
			List<FormStocktake> itemList = new ArrayList<>();
			if(PageUtil.pageInit(formStocktake, total)) {
				itemList = stocktakeDao.getSessionSectionItem(formStocktake);
			}
			setLocalDateTime(itemList, user);
			resultMap.put("total", total);
			resultMap.put("itemList", itemList);
			resultDeal(request, response, resultMap);
		}catch (Exception e){
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
	}

	@Override
	public void closeSection(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		FormStocktake formStocktake = JsonUtil.jsonToBean(JsonUtil.getJsonString(object), FormStocktake.class);
		setFormCommonValue(request, formStocktake, user);
		//设置状态[1]为close
		formStocktake.setSectionStatus("1");
		boolean flag;
		try {
			//flag = stocktakeDao.closeSection(formStocktake);
			flag = stocktakeDao.updateSection(formStocktake);
		}catch (Exception e){
			logger.error(e);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
		if(flag){
			resultMap.put(WmsConstants.Common.RESMSG, "Close section successful!");
		}else{
			resultMap.put(WmsConstants.Common.RESMSG, "Close section failed!");
		}
		resultMap.put(WmsConstants.Common.SUCCESEFLG, flag);
		resultDeal(request, response, resultMap);
	}

	@Override
	public void reScan(HttpServletRequest request, HttpServletResponse response, int stocktake_detail_id) {
		Map<String, Object> resultMap = new HashMap<>();
		boolean flag;
		try {
			flag = stocktakeDao.deleteItemByStocktakeDetailId(stocktake_detail_id);
			if(flag){
				resultMap.put(WmsConstants.Common.RESMSG, "Delete Items successful!");
			} else {
				resultMap.put(WmsConstants.Common.RESMSG, "Delete Items failed!");
			}
		}catch (Exception e){
			logger.error(e);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
		resultMap.put(WmsConstants.Common.SUCCESEFLG, flag);
		resultDeal(request, response, resultMap);
	}

	@Override
	public void compareInit(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		List<FormStocktake> compareList = new ArrayList<>();
		FormStocktake formStocktake = JsonUtil.jsonToBean(JsonUtil.getJsonString(object), FormStocktake.class);
		try{
			int total = stocktakeDao.getCompareListSizeBySessionId(formStocktake.getStocktake_id());
			resultMap.put("total", total);
			if(PageUtil.pageInit(formStocktake, total)) {
				compareList = stocktakeDao.getCompareInfoList(formStocktake);
			}
			setLocalDateTime(compareList, user);
			resultMap.put("compareInfoList", compareList);
			resultDeal(request, response, resultMap);
		}catch (Exception e){
			logger.error(e);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
	}

	@Override
	public void checkBoxValueChange(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		FormStocktake formStocktake = JsonUtil.jsonToBean(JsonUtil.getJsonString(object), FormStocktake.class);
		setFormCommonValue(request, formStocktake, user);
		boolean flag;
		try {
			flag = stocktakeDao.changeCompareStatus(formStocktake);
		}catch (Exception e){
			logger.error(e);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
		if(!flag){
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
		resultDeal(request, response, resultMap);
	}

	@Override
	public void checkSessionStatus(HttpServletRequest request, HttpServletResponse response, int stocktake_id) {
		Map<String, Object> resultMap = new HashMap<>();
		FormStocktake formStocktake = new FormStocktake();
		formStocktake.setStocktake_id(stocktake_id);
		try {
			formStocktake = stocktakeDao.getSessionInfo(formStocktake);
		}catch (Exception e){
			logger.error(e);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
		resultMap.put("sessionInfo", formStocktake);
		resultDeal(request, response, resultMap);
	}

	@Override
	public void doSessionDone(HttpServletRequest request, HttpServletResponse response, int stocktake_id, UserSessionBean user) {
		if(!stocktakeDao.isMaxStockTakeId(stocktake_id)){
			throw new BusinessException(WmsMsgConstants.TakeStockMsg.CANNOT_FIXED_OLD_SESSION);
		}
		Map<String, Object> resultMap = new HashMap<>();
		FormStocktake formStocktake = new FormStocktake();
		formStocktake.setStocktake_id(stocktake_id);
		//设置状态为Stock; 0:processing; 1:stock; 2:Compare ; 3:Done
		formStocktake.setStocktake_status("3");
		setFormCommonValue(request, formStocktake, user);
		boolean flag;
		try {
			flag = stocktakeDao.changeSessionStatus(formStocktake);
		}catch (Exception e){
			logger.error(e);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
		if(flag){
			resultMap.put(WmsConstants.Common.RESMSG, "Session status change to 【Done】 successful!");
		}else{
			resultMap.put(WmsConstants.Common.RESMSG, "Session status change to 【Done】 failed!");
		}
		resultMap.put(WmsConstants.Common.SUCCESEFLG, flag);
		resultDeal(request, response, resultMap);
	}

	@Override
	public void checkSectionStatus(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			FormStocktake formStocktake = JsonUtil.jsonToBean(JsonUtil.getJsonString(object), FormStocktake.class);
			setFormCommonValue(request, formStocktake, user);
			//不进行分页查询
			formStocktake.setRows(0);
			List<FormStocktake>	sectionList = stocktakeDao.getSectionListBySessionId(formStocktake);
			resultMap.put("sectionList", sectionList);
			resultDeal(request, response, resultMap);
		}catch (Exception e){
			logger.error(e);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
	}

	@Override
	public byte[] downloadCompResReport(String param, UserSessionBean user) {
		FormStocktake formStocktake = JsonUtil.jsonToBean(param, FormStocktake.class);
		List<FormStocktake> formStocktakes  = stocktakeDao.getCompareResList(formStocktake);
		return createReportByte(formStocktakes);
	}

	private byte[] createReportByte(List<FormStocktake> formStocktakes) {
		byte[] bytes;
		try{
			// 报表模板名取得
			String templateFile = com.voyageone.common.configs.Properties.readValue(WmsConstants.ReportItems.StocktakeCompResRpt.TEMPLATE_PATH) + WmsConstants.ReportItems.StocktakeCompResRpt.TEMPLATE_NAME;
			// 报表模板名读入
			InputStream templateInput = new FileInputStream(templateFile);
			Workbook workbook = WorkbookFactory.create(templateInput);
			// 设置内容
			setInvDelReptContent(workbook, formStocktakes);
			// 输出内容
			ByteArrayOutputStream outData = new ByteArrayOutputStream();
			workbook.write(outData);
			bytes = outData.toByteArray();
			// 关闭
			templateInput.close();
			workbook.close();
			outData.close();
		} catch (Exception e) {
			logger.info("盘点库存差异报表下载失败：" + e);
			return null;
		}
		return bytes;
	}

	/**
	 * 设置compRes报表内容
	 * @param workbook 报表模板
	 * @param formStocktakes 库存内容按sku
	 */
	private void setInvDelReptContent(Workbook workbook, List<FormStocktake> formStocktakes) {
		// 模板Sheet
		int sheetNo = WmsConstants.ReportItems.StocktakeCompResRpt.TEMPLATE_SHEET_NO;
		// 初始行
		int intRow = WmsConstants.ReportItems.StocktakeCompResRpt.TEMPLATE_FIRSTROW_NO;

		// 按照模板克隆一个sheet
		Sheet sheet = workbook.cloneSheet(sheetNo);

		// 设置模板sheet页后的sheet名为报告sheet名
		workbook.setSheetName(sheetNo + 1, WmsConstants.ReportItems.StocktakeCompResRpt.RPT_SHEET_NAME);

		for (FormStocktake formStocktake : formStocktakes) {
			if(intRow != WmsConstants.ReportItems.StocktakeCompResRpt.TEMPLATE_FIRSTROW_NO) {
				Row newRow = sheet.createRow(intRow);
				//根据第2行（第一行是标题）格式设置每行的格式,第一列不处理
				for (int col = 1; col <= WmsConstants.ReportItems.StocktakeCompResRpt.COLNUM; col++) {
					Cell newCell = newRow.createCell(col);
					Cell oldCell = sheet.getRow(WmsConstants.ReportItems.StocktakeCompResRpt.TEMPLATE_FIRSTROW_NO).getCell(col);
					newCell.setCellStyle(oldCell.getCellStyle());
				}
			}
			// 得到当前行
			Row currentRow = sheet.getRow(intRow);
			// sku
			currentRow.getCell(WmsConstants.ReportItems.StocktakeCompResRpt.Col.COLNUM_SKU).setCellValue(formStocktake.getSku());
			// upc
			currentRow.getCell(WmsConstants.ReportItems.StocktakeCompResRpt.Col.COLNUM_UPC).setCellValue(formStocktake.getUpc());
			// inventory
			currentRow.getCell(WmsConstants.ReportItems.StocktakeCompResRpt.Col.COLNUM_INVENTORY).setCellValue(formStocktake.getInventory());
			// stockqty
			currentRow.getCell(WmsConstants.ReportItems.StocktakeCompResRpt.Col.COLNUM_STOCKQTY).setCellValue(formStocktake.getStocktake_qty());
			// stockqty_offset
			currentRow.getCell(WmsConstants.ReportItems.StocktakeCompResRpt.Col.COLNUM_STOCKQTY_OFFSET).setCellValue(formStocktake.getStocktake_qty_offset());

			intRow = intRow + 1;
		}
		// 如果有记录的话，删除模板sheet
		if (formStocktakes.size() > 0) {
			workbook.removeSheetAt(sheetNo);
		}
	}

	//将查询条件中的时间字段转化成格林威治时间
	private void changeDateTimeToGMT(FormStocktake formStocktake, UserSessionBean user) {
		if(!StringUtils.isEmpty(formStocktake.getModifiedTime_s())) {
			formStocktake.setModifiedTime_s(DateTimeUtil.getGMTTimeFrom(formStocktake.getModifiedTime_s(), user.getTimeZone()));
		}
		if(!StringUtils.isEmpty(formStocktake.getModifiedTime_e())){
			formStocktake.setModifiedTime_e(DateTimeUtil.getGMTTimeTo(formStocktake.getModifiedTime_e(), user.getTimeZone()));
		}
	}

	//将时间类型的字段处理成本地时间（默认时间是格林威治时间）
	private void setLocalDateTime(List<FormStocktake> sessionList, UserSessionBean user) {
		for(FormStocktake fs : sessionList){
			if(!StringUtils.isEmpty(fs.getCreated())){
				fs.setCreated_local(DateTimeUtil.getLocalTime(fs.getCreated(), user.getTimeZone()));
			}
			if(!StringUtils.isEmpty(fs.getModified())){
				fs.setModified_local(DateTimeUtil.getLocalTime(fs.getModified(), user.getTimeZone()));
			}
			if(!StringUtils.isEmpty(fs.getStocktake_name())){
				if(!DateTimeUtil.getLocalTime(fs.getStocktake_name(), user.getTimeZone()).contains("error")) {
					fs.setStocktake_name(DateTimeUtil.getLocalTime(fs.getStocktake_name(), user.getTimeZone()));
				}
			}
		}
	}

	//根据upc获取产品
	private FormStocktake getProductByUpcOrSku(FormStocktake formStocktake) {
		return stocktakeDao.getProductByUpcOrSku(formStocktake);
	}

	//创建Section
	private Map<String, Object> createSection(FormStocktake formStocktake) {
		Map<String, Object> resultMap = new HashMap<>();
		int locationId = stocktakeDao.locationNameCheck(formStocktake);
		boolean successfulFlg;
		if(locationId != -1){
			formStocktake.setLocation_id(locationId);
			//设置section状态："0"：processing; "1":"done"
			formStocktake.setSectionStatus("0");
			formStocktake.setActive(1);
			//创建Section
			int stocktake_detail_id = stocktakeDao.createSection(formStocktake);
			if(stocktake_detail_id != -1){
				successfulFlg = true;
				resultMap.put(RESMSG,"Create section successful!");
				resultMap.put(STOCKTAKE_DETAIL_ID, stocktake_detail_id);
			}else{
				successfulFlg = false;
				resultMap.put(RESMSG,"Create section failed!");
			}
		}else{
            throw new BusinessException(WmsMsgConstants.ItemLocationMsg.NOT_FOUND_LOCATION, formStocktake.getSectionName());
		}
		resultMap.put(SUCCESSFULFLG, successfulFlg);
		return resultMap;
	}

	//验证是否满足Stock的条件； 1: 目前的状态为 processing; 2: session中所有的section都为done状态
	private boolean stockSessionCheck(String sessionId) {
		boolean hasProcessingSection = stocktakeDao.hasProcessingSection(Integer.parseInt(sessionId));
		return !hasProcessingSection;
	}

	/**
	 * @Description 获取下拉框内容,paramMap格式{"typeName":"typeId;hasAllOptionFlg"}
	 *               typeName：contorl里面接受数据的名称；
	 *               typeId：com_mt_value里面的typeId;
	 *               hasAllOptionFlg：是否要ALL选项,默认true
	 * @param paramMap 参数Map
	 * @param resultMap 结果集Map
	 * @created 20150507
	 */
	private void doGetComBoxInfo(Map<String, String> paramMap, Map<String, Object> resultMap) {
		for(String strkey : paramMap.keySet()){
			String[] strArray = paramMap.get(strkey).split(";");
			String typeId;
			boolean hasAllOption = true;
			if(strArray.length == 1){
				typeId = strArray[0];
			}else if(strArray.length == 2){
				typeId = strArray[0];
				hasAllOption = Boolean.parseBoolean(strArray[1]);
			}else{
				return;
			}
			List<MasterInfoBean> list = Type.getMasterInfoFromId(Integer.parseInt(typeId), hasAllOption);
			resultMap.put(strkey, list);
		}
	}

	//发送结果集合
	private void resultDeal(HttpServletRequest request, HttpServletResponse response, Map<String, Object> resultMap){
		AjaxResponseBean result = new AjaxResponseBean();
		result.setResult(true);
		result.setResultInfo(resultMap);
		result.writeTo(request, response);
		//logger.info(result.toString());
	}

	//获取用户对应的channel
	private List<String> getOrderChanleList(UserSessionBean user){
		HashMap<String, PermissionBean> propertyPermissions = user.getPropertyPermissions();
		List<String> orderChannelIdList = new ArrayList<>();
		for(String propertyId : propertyPermissions.keySet()){
			orderChannelIdList.add(propertyId);
		}
		return orderChannelIdList;
	}

	//设置FormStocktake公用信息
	private void setFormCommonValue(HttpServletRequest request, FormStocktake formStocktake, UserSessionBean user){
		//设置用户名称
		formStocktake.setUser(user.getUserName());
		//用户对应的渠道
		formStocktake.setOrderChannelId(getOrderChanleList(user));
		//设置用户语言
		String lang = (String)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_LANG);
		formStocktake.setLang(lang);
		//设置typeId
		Map<String, Integer> typeIdMap = new HashMap<>();
		typeIdMap.put(WmsConstants.Stocktake.SESSIONSTATUS, TypeConfigEnums.MastType.stocktakeSessionStatus.getId());
		typeIdMap.put(WmsConstants.Stocktake.SECTIONSTATUS, TypeConfigEnums.MastType.stocktakeSectionStatus.getId());
		typeIdMap.put(WmsConstants.Stocktake.STOCKTAKETYPE, TypeConfigEnums.MastType.stocktakeType.getId());
		formStocktake.setTypeIdMap(typeIdMap);
	}

	//通过storeId获取orderChannelId
	private String getChannelIdByStoreId(int storeId){
		return stocktakeDao.getChannelIdByStoreId(storeId);
	}
}
