package com.voyageone.wms.service.impl;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.ChannelStoreBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.core.util.PageUtil;
import com.voyageone.wms.WmsConstants;
import com.voyageone.wms.WmsMsgConstants;
import com.voyageone.wms.dao.BackorderDao;
import com.voyageone.wms.dao.StocktakeDao;
import com.voyageone.wms.formbean.FormBackorder;
import com.voyageone.wms.service.WmsBackorderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class WmsBackorderServiceImpl implements WmsBackorderService {

	private static Log logger = LogFactory.getLog(WmsBackorderServiceImpl.class);

	@Autowired
	private BackorderDao backorderDao;
	@Autowired
	private StocktakeDao stocktakeDao;
	
	@Override
	public void getBackorderInfoList(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		List<FormBackorder> backorderList = new ArrayList<>();
		FormBackorder formBackorder = JsonUtil.jsonToBean(JsonUtil.getJsonString(object), FormBackorder.class);
		setFormCommonValue(formBackorder, user);
		try{
			int total = backorderDao.getBackorderInfoListSize(formBackorder);
			resultMap.put("total", total);
			if(PageUtil.pageInit(formBackorder, total)) {
				backorderList = backorderDao.getBackorderInfoList(formBackorder);
			}
			//将结果集中的时间字段由格林威治时间转化成本地时间
			setLocalDateTime(backorderList, user);
			resultMap.put("backorderList", backorderList);
			resultDeal(request, response, resultMap);
		}catch (Exception e){
			logger.error(e);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
	}

	@Override
	public void addBackorderInfo(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		FormBackorder formBackorder = JsonUtil.jsonToBean(JsonUtil.getJsonString(object), FormBackorder.class);
		setFormCommonValue(formBackorder, user);
        String sku = formBackorder.getSku();
		boolean flag;
        int existflag = skuIsExist(formBackorder);
        if(existflag == -1 ) {
            logger.error("不存在的sku:" + sku);
            throw new BusinessException(WmsMsgConstants.BackOrderListMsg.NONEXISTENT_SKU, sku);
        }else if(existflag == 1)
        {
            flag = backorderDao.insertBackOrderInfo(formBackorder);
            if(!flag){
                logger.error("添加sku到backorderList失败，SKU:" + sku);
            }
            resultMap.put(WmsConstants.Common.SUCCESEFLG, flag);
            resultDeal(request, response, resultMap);
        }else if(existflag == 2){
            logger.error("需要添加的sku已经存在于超卖列表，SKU:" + sku);
            throw new BusinessException(WmsMsgConstants.BackOrderListMsg.EXIST_SKU, sku);
        }
	}

	@Override
	public void delBackorderInfo(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		FormBackorder formBackorder = JsonUtil.jsonToBean(JsonUtil.getJsonString(object), FormBackorder.class);
		setFormCommonValue(formBackorder, user);
		formBackorder.setActive(0);
		boolean flag = backorderDao.deleteBackOrderInfo(formBackorder);
		if(flag){
			resultMap.put(WmsConstants.Common.RESMSG, "Delete backorder successful!");
		}
		resultMap.put(WmsConstants.Common.SUCCESEFLG, flag);
		resultDeal(request, response, resultMap);
	}

	@Override
	public void popInit(HttpServletRequest request, HttpServletResponse response, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("userStore", user.getCompanyRealStoreList());
		resultDeal(request, response, resultMap);
	}

	@Override
	public void listInit(HttpServletRequest request, HttpServletResponse response, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		//获取用户仓库
		ArrayList<ChannelStoreBean> channelStoreList = new ArrayList<>();
		ChannelStoreBean channelStoreBean = new ChannelStoreBean();
		channelStoreBean.setStore_id(0);
		channelStoreBean.setStore_name("ALL");
		channelStoreList.add(channelStoreBean);
		channelStoreList.addAll(user.getCompanyRealStoreList());
		resultMap.put("userStore", channelStoreList);
		resultDeal(request, response, resultMap);
	}

	//返回： -1、错误的sku； 1、sku存在，但是没在backOrder表中； 2、在backOrder中表存在;
	private int skuIsExist(FormBackorder formBackorder) {
		int resCount  = backorderDao.checkSku(formBackorder);
		if(resCount > 0){
			resCount = backorderDao.getBackorderInfoListSize(formBackorder);
			if(resCount > 0){
				return 2;
			}
			return 1;
		}else{
			return -1;
		}
	}

	//将时间类型的字段处理成本地时间（默认时间是格林威治时间）
	private void setLocalDateTime(List<FormBackorder> sessionList, UserSessionBean user) {
		for(FormBackorder fs : sessionList){
			if(!StringUtils.isEmpty(fs.getCreated())){
				fs.setCreated_local(DateTimeUtil.getLocalTime(fs.getCreated(), user.getTimeZone()));
			}
			if(!StringUtils.isEmpty(fs.getModified())){
				fs.setModified_local(DateTimeUtil.getLocalTime(fs.getModified(), user.getTimeZone()));
			}
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

	//设置FormStocktake公用信息
	private void setFormCommonValue(FormBackorder formBackorder, UserSessionBean user){
		//设置用户名称
		formBackorder.setCreater(user.getUserName());
		formBackorder.setModifier(user.getUserName());
		//用户对应的渠道
		formBackorder.setOrderChannelId(user.getChannelList());
		//根据sotreId获取ChannelId
		formBackorder.setOrder_channel_id(stocktakeDao.getChannelIdByStoreId(formBackorder.getStore_id()));
	}

}
