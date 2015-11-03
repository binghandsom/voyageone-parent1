package com.voyageone.oms.service.impl;

import com.csvreader.CsvReader;
import com.voyageone.common.Constants;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.oms.OmsConstants;
import com.voyageone.oms.OmsMessageConstants;
import com.voyageone.oms.dao.OrderDetailDao;
import com.voyageone.oms.dao.SettlementDao;
import com.voyageone.oms.formbean.OutFormOrderdetailOrders;
import com.voyageone.oms.formbean.OutFormUploadSettlementFile;
import com.voyageone.oms.modelbean.SettlementBean;
import com.voyageone.oms.modelbean.SettlementFileBean;
import com.voyageone.oms.service.OmsOrderAccountingsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class OmsOrderAccountingsServiceImpl implements OmsOrderAccountingsService {
	private static Log logger = LogFactory.getLog(OmsOrderAccountingsServiceImpl.class);

	@Autowired
	private SettlementDao settlementDao;

	@Autowired
	private OrderDetailDao orderDetailDao;

	@Autowired
	private DataSourceTransactionManager transactionManager;
	DefaultTransactionDefinition def =new DefaultTransactionDefinition();

	// 文件类型
	private enum AccountingType {
		//	Settlement 文件
		//	未知
		UnKnow("00"),
		//	支付宝
		AliPay("04"),
		//	支付宝国际
		AlipayInterNational("06"),
		//	京东
		Jdpay("07"),
		//	京东国际
		JdpayInterNational("08"),
		//	独立域名
		// Cnpay("09"),
		//	微信
		Weixinpay("10"),

		//	Transaction 文件
		//	支付宝国际
		AlipayInterNationalForTran("06");

		private final String value;

		AccountingType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	// 每次Insert件数
	private final static int insertRecCountFixed = 50;

	// Default currency
	private final static String defaultCurrency = "CNY";

	// Prefix of Alipay number
	private final static String prefixAlipayNum = "BO";

	// Empty datetime
	private final static String emptyDatetime = "0000-00-00 00:00:00";


	/**
	 * 保存账务文件
	 *
	 * @param file 结算文件
	 * @param user 当前用户
	 * @return
	 */
	public void saveSettlementFile(String fileType, MultipartFile file, AjaxResponseBean result, UserSessionBean user) {
		//	上传文件检查
		if(chkSettlementFile(file, result)) {
			//	保存账务
			saveSettlementFileSub(fileType, file, result, user);
		}
	}

	/**
	 * 保存账务文件子函数
	 *
	 * @param file 结算文件
	 * @param user 当前用户
	 * @return
	 */
	private void saveSettlementFileSub(String fileType, MultipartFile file, AjaxResponseBean result, UserSessionBean user) {

		logger.info("saveSettlementFileSub start");
		logger.info("saveSettlementFileSub file = " + file.getOriginalFilename());
		//	子处理返回
		List<Object> ret = null;
		//	Fields of settlement_file
		String accountNo = "";
		String beginTime = emptyDatetime;
		String endTime = emptyDatetime;
		String exportTime = emptyDatetime;
		String totalIncome = "0";
		String totalExpense = "0";
		String orderChannelId = "";
		String cartId = "";

		//	结果返回
		OutFormUploadSettlementFile uploadResult = new OutFormUploadSettlementFile();
		//		结果返回(文件名)
		uploadResult.setSettlementFileId(file.getOriginalFilename());

		//	文件类型识别
		logger.info("	文件类型识别");
		ret = getFileType(fileType, file, result);

		//	文件读入
		if ((boolean)ret.get(0)) {
			// 返回值清空
			ret.set(0,false);

			logger.info("	文件读入");
			//	缓存读入
			switch ((AccountingType)ret.get(1)) {
				//	Settlement 文件
				//		支付宝
				case AliPay:
					ret = readTMFile(file, result);
					accountNo = (String)ret.get(4);
					beginTime = (String)ret.get(5);
					endTime = (String)ret.get(6);
					exportTime = (String)ret.get(7);
					totalIncome = (String)ret.get(8);
					totalExpense = (String)ret.get(9);
					orderChannelId = (String)ret.get(10);
					cartId = (String)ret.get(11);
					break;
				//		支付宝国际
				case AlipayInterNational:
					ret = readTGFile(file, result);
					totalIncome = (String)ret.get(4);
					totalExpense = (String)ret.get(5);
					orderChannelId = (String)ret.get(6);
					cartId = (String)ret.get(7);
					break;
				//		京东
				case Jdpay:
					ret = readJDFile(file, result);
					break;
				//		京东国际
				case JdpayInterNational:
					ret = readJGFile(file, result);
					break;
				//		微信
				case Weixinpay:
					ret = readWXFile(file, result);
					totalIncome = (String)ret.get(4);
					totalExpense = (String)ret.get(5);
					orderChannelId = (String)ret.get(6);
					cartId = (String)ret.get(7);
					break;

				//	Transaction 文件
				//		支付宝国际
				case AlipayInterNationalForTran:
					ret = readTGFileForTran(file, result);
					totalIncome = (String)ret.get(4);
					totalExpense = (String)ret.get(5);
					orderChannelId = (String)ret.get(6);
					cartId = (String)ret.get(7);
					break;
			}

			if ((boolean)ret.get(0)) {
				List<SettlementBean> settlementList = (List<SettlementBean>)ret.get(1);
				//		结果返回(处理件数)
				uploadResult.setUploadRecCount(String.valueOf(settlementList.size()));
				//		结果返回（渠道名称）
				uploadResult.setOrderChannelName((String)ret.get(2));
				//		结果返回（店铺名称）
				uploadResult.setCartName((String)ret.get(3));
			}
		}

		//	DB保存
		if ((boolean)ret.get(0)) {
			// 返回值清空
			ret.set(0, false);

			logger.info("	DB保存");
			ret = saveDB(fileType, (List<SettlementBean>)ret.get(1), file.getOriginalFilename(), user, accountNo, beginTime, endTime, exportTime,
					totalIncome, totalExpense, orderChannelId, cartId);
			if (!(boolean)ret.get(0)) {
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210064, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		}

		//	处理结果返回
		if ((boolean)ret.get(0)) {

			// 设置返回结果
			Map<String, Object> resultMap = new HashMap<String, Object>();
			// 		订单
			resultMap.put("uploadResult", uploadResult);

			result.setResultInfo(resultMap);
		}

		logger.info("saveSettlementFileSub end");
	}

	/**
	 * 保存账务处理检查
	 *
	 * @param file 结算文件
	 * @return
	 */
	private boolean chkSettlementFile(MultipartFile file, AjaxResponseBean result) {
		boolean ret = true;

		logger.info("chkSettlementFile start");

		if (file.isEmpty()) {
			ret = false;

			// 上传的Settlement文件为空
			result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210066, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		}

		if (ret) {
			SettlementFileBean settlementFileInfo = settlementDao.getSettlementFileInfo(file.getOriginalFilename());
			if (settlementFileInfo != null) {
				ret = false;

				// 该文件已处理
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210063, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		}

		logger.info("chkSettlementFile result = " + ret);
		logger.info("chkSettlementFile end");
		return ret;
	}

	/**
	 * 文件类型识别
	 *
	 * @param file 结算文件
	 * @return List<0>	返回结果
	 * 			List<1>	文件类型
	 */
	private List<Object> getFileType(String fileType, MultipartFile file, AjaxResponseBean result) {
		List<Object> ret = new ArrayList<>();

		boolean isSuccess = true;
		AccountingType retAccountingKind = AccountingType.UnKnow;

		try {
			CsvReader reader = new CsvReader(file.getInputStream(), ',', Charset.forName("GBK"));

			// Head读入
			reader.readHeaders();
			String[] headers = reader.getHeaders();

			// Settlement 文件的场合
			if (OmsConstants.AccountingFileType.SettlementFile.equals(fileType)) {
				if (OmsConstants.AccountKindIdentify.TG.equals(headers[0]) &&
						OmsConstants.AccountKindIdentify.TG2.equals(headers[1])) {
					retAccountingKind = AccountingType.AlipayInterNational;
				}
				else if(OmsConstants.AccountKindIdentify.WX.equals(headers[0]))	{
					retAccountingKind = AccountingType.Weixinpay;
				}
				else if(OmsConstants.AccountKindIdentify.TM.equals(headers[0]))	{
					retAccountingKind = AccountingType.AliPay;
				}
			// Transaction 文件的场合
			} else {
				 if(OmsConstants.AccountKindIdentify.TG.equals(headers[0]) &&
					OmsConstants.AccountKindIdentify.TGForTran.equals(headers[1])) {
					 retAccountingKind = AccountingType.AlipayInterNationalForTran;
				 }
			}

			reader.close();

			//	结果判定
			if (retAccountingKind == AccountingType.UnKnow) {
				isSuccess = false;
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210067, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}
		} catch (Exception e) {
			isSuccess = false;
		}

		ret.add(isSuccess);
		ret.add(retAccountingKind);

		return ret;
	}

	/**
	 * DB 保存
	 * @param settlementList 缓存文件内容
	 * @param settlementFileId 缓存文件名
	 * @param user 当前用户
	 * @param accountNo 帐号
	 * @param beginTime 账单起始时间
	 * @param endTime 账单结束时间
	 * @param exportTime 账单导出时间
	 * @param totalIncome 收入合计
	 * @param totalExpense 支出合计
	 * @param orderChannelId 账单渠道
	 * @param cartId 账单店铺
	 * @return List<0>	返回结果
	 */
	private List<Object> saveDB(String fileType, List<SettlementBean> settlementList, String settlementFileId, UserSessionBean user,
								String accountNo, String beginTime, String endTime,
								String exportTime, String totalIncome, String totalExpense,
								String orderChannelId, String cartId) {
		List<Object> ret = new ArrayList<>();

		boolean isSuccess = true;

		TransactionStatus status=transactionManager.getTransaction(def);

		try {
			// oms_bt_settlement 的 origin_source_order_id 更新用
			ArrayList<String> originSourceOrderList = new ArrayList<String>();
			// oms_bt_settlement 追加用
			StringBuffer insertSettlementValues = new StringBuffer();
			int insertCount = 0;

			for (int i = 0; i < settlementList.size(); i++) {
				SettlementBean settlementInfo = settlementList.get(i);

				//originSourceOrderList.add(settlementInfo.getOriginSourceOrderId());

				//	oms_bt_settlement 插入
				insertCount = insertCount + 1;
				if (insertCount == 1) {
					insertSettlementValues.append(getSettlementInsertString(settlementInfo, settlementFileId, user));
				} else {
					insertSettlementValues.append(",");
					insertSettlementValues.append(getSettlementInsertString(settlementInfo, settlementFileId, user));
				}

				if (insertCount == insertRecCountFixed) {
					//	oms_bt_settlement
					//		oms_bt_settlement追加（根据文件表插入）
					logger.info("		insertSettlementInfo");
					isSuccess = settlementDao.insertSettlementInfo(insertSettlementValues.toString(), insertCount);

					if (!isSuccess) {
						break;
					}

					//	变量初期化
					insertCount = 0;
					insertSettlementValues.delete(0, insertSettlementValues.length());
					originSourceOrderList.clear();
				}
			}

			if (isSuccess) {
				if (insertCount > 0) {
					logger.info("		insertSettlementInfo");
					isSuccess = settlementDao.insertSettlementInfo(insertSettlementValues.toString(), insertCount);
				}
			}

			if (isSuccess) {
				if (OmsConstants.AccountingFileType.SettlementFile.equals(fileType)) {
					//	Settlement文件的场合
					//		oms_bt_group_orders  payment_total_settle 更新
					logger.info("		updatePaymentSettleInfo");
					isSuccess = orderDetailDao.updatePaymentSettleInfo(settlementFileId);
				} else {
					//	Transaction文件的场合
					//		oms_bt_group_orders  rate 更新
					logger.info("		updateOrderRateInfo");
					isSuccess = orderDetailDao.updateOrderRateInfo(settlementFileId);
				}
			}

			//	oms_bt_settlement_file
			if (isSuccess) {
				logger.info("		saveSettlementFile");
				isSuccess = saveSettlementFile(fileType, settlementList.get(0), settlementFileId, user,
						accountNo, beginTime, endTime,
						exportTime, totalIncome, totalExpense,
						orderChannelId, cartId);
			}

			if (isSuccess) {
				logger.info("saveDB success");
				transactionManager.commit(status);
			} else {
				logger.info("saveDB error");
				transactionManager.rollback(status);
			}
		} catch (Exception e) {
			isSuccess = false;
			logger.error("saveDB", e);
			transactionManager.rollback(status);

		}

		ret.add(isSuccess);
		return ret;
	}

	/**
	 * oms_bt_settlement_file表插入
	 * @param settlementInfo 缓存文件内容
	 * @param settlementFileId 缓存文件名
	 * @param user 当前用户
	 * @param accountNo 帐号
	 * @param beginTime 账单起始时间
	 * @param endTime 账单结束时间
	 * @param exportTime 账单导出时间
	 * @param totalIncome 收入合计
	 * @param totalExpense 支出合计
	 * @param orderChannelId 账单渠道
	 * @param cartId 账单店铺
	 * @return boolean 保存结果
	 */
	private boolean saveSettlementFile(String fileType, SettlementBean settlementInfo, String settlementFileId, UserSessionBean user,
									   String accountNo, String beginTime, String endTime,
									   String exportTime, String totalIncome, String totalExpense,
									   String orderChannelId, String cartId) {
		boolean ret = true;
		SettlementFileBean settlementFileInfo = new SettlementFileBean();

		settlementFileInfo.setOrderChannelId(orderChannelId);
		settlementFileInfo.setCartId(cartId);
		settlementFileInfo.setFileType(fileType);
		settlementFileInfo.setPayType(settlementInfo.getPayType());
		settlementFileInfo.setSettlementFileId(settlementFileId);
		settlementFileInfo.setAccount_no(accountNo);
		settlementFileInfo.setBegin_time(beginTime);
		settlementFileInfo.setEnd_time(endTime);
		settlementFileInfo.setExport_time(exportTime);
		settlementFileInfo.setTotal_income(totalIncome);
		settlementFileInfo.setTotal_expense(totalExpense);
		settlementFileInfo.setCreater(user.getUserName());
		settlementFileInfo.setModifier(user.getUserName());

		ret = settlementDao.insertSettlementFileInfo(settlementFileInfo);

		return ret;
	}

	/**
	 * oms_bt_settlement表插入用sql文取得
	 *
	 * @param settlementInfo settlement对象
	 * @param settlementFileId 缓存文件名
	 * @param user 当前用户
	 * @return
	 */
	private String getSettlementInsertString(SettlementBean settlementInfo, String settlementFileId, UserSessionBean user) {
		StringBuffer insertString = new StringBuffer();

		insertString.append("(");

		insertString.append(formatStringField(settlementInfo.getOrderChannelId())).append(",");
		insertString.append(settlementInfo.getCartId()).append(",");
		insertString.append(settlementInfo.getFileType()).append(",");
		insertString.append(formatDatetimeField(settlementInfo.getPaymentTime())).append(",");
		insertString.append(formatDatetimeField(settlementInfo.getSettlementTime())).append(",");
		insertString.append(formatStringField(settlementInfo.getSourceOrderId())).append(",");
		insertString.append(formatStringField(settlementInfo.getOriginSourceOrderId())).append(",");
		insertString.append(formatStringField(settlementInfo.getBusinessType())).append(",");
		insertString.append(formatStringField(StringUtils.transferStr(settlementInfo.getComment()))).append(",");
		insertString.append(formatStringField(settlementInfo.getPayType())).append(",");
		insertString.append(formatDecimalField(settlementInfo.getDebit())).append(",");
		insertString.append(formatDecimalField(settlementInfo.getDebitForeign())).append(",");
		insertString.append(formatDecimalField(settlementInfo.getCredit())).append(",");
		insertString.append(formatDecimalField(settlementInfo.getCreditForeign())).append(",");
		insertString.append(formatDecimalField(settlementInfo.getRate())).append(",");
		insertString.append(formatStringField(settlementInfo.getCurrency())).append(",");
		insertString.append(formatDecimalField(settlementInfo.getFee())).append(",");
		insertString.append(formatDecimalField(settlementInfo.getFeeRate())).append(",");
		insertString.append(formatStringField(settlementInfo.getPayNo())).append(",");
		insertString.append(formatStringField(settlementInfo.getPayAccount())).append(",");
		insertString.append(formatStringField(StringUtils.transferStr(settlementInfo.getDescription()))).append(",");
		insertString.append(formatStringField(StringUtils.transferStr(settlementFileId))).append(",");
		insertString.append(formatStringField(StringUtils.transferStr(settlementInfo.getPay_type_detail()))).append(",");
		insertString.append(formatDecimalField(settlementInfo.getSettlement())).append(",");
		insertString.append(formatDecimalField(settlementInfo.getSettlement_foreign())).append(",");
		insertString.append(settlementInfo.isProcess_flag()).append(",");
		insertString.append(formatStringField(settlementInfo.getTrade_type())).append(",");
		insertString.append(formatStringField(settlementInfo.getTrade_status())).append(",");
		insertString.append(formatStringField(settlementInfo.getAli_int_stem_from())).append(",");
		insertString.append(formatStringField(settlementInfo.getWx_public_account_id())).append(",");
		insertString.append(formatStringField(settlementInfo.getWx_shop_id())).append(",");
		insertString.append(formatStringField(settlementInfo.getWx_special_shop_id())).append(",");
		insertString.append(formatStringField(settlementInfo.getWx_device_id())).append(",");
		insertString.append(formatStringField(settlementInfo.getWx_weixin_order_id())).append(",");
		insertString.append(formatStringField(settlementInfo.getShop_order_id())).append(",");
		insertString.append(formatStringField(settlementInfo.getWx_user_id())).append(",");
		insertString.append(formatStringField(settlementInfo.getPayment_channel())).append(",");
		insertString.append(formatDecimalField(settlementInfo.getWx_company_bonus())).append(",");
		insertString.append(formatStringField(settlementInfo.getWx_weixin_refund_id())).append(",");
		insertString.append(formatStringField(settlementInfo.getWx_shop_refund_id())).append(",");
		insertString.append(formatDecimalField(settlementInfo.getWx_company_bonus_refund())).append(",");
		insertString.append(formatStringField(settlementInfo.getWx_refund_type())).append(",");
		insertString.append(formatStringField(settlementInfo.getWx_refund_status())).append(",");
		insertString.append(formatStringField(settlementInfo.getWx_shop_data_package())).append(",");
		insertString.append(formatStringField(StringUtils.transferStr(settlementInfo.getGoods_name()))).append(",");
		insertString.append(formatStringField(settlementInfo.getAli_accounting_no())).append(",");
		insertString.append(formatStringField(StringUtils.transferStr(settlementInfo.getAli_pay_account_full()))).append(",");
		insertString.append(formatDecimalField(settlementInfo.getAli_account_balance())).append(",");
		insertString.append(formatDatetimeField(settlementInfo.getJg_order_time())).append(",");
		insertString.append(formatDecimalField(settlementInfo.getJg_product_total())).append(",");
		insertString.append(formatDecimalField(settlementInfo.getJg_shipping_total())).append(",");
		insertString.append(formatDecimalField(settlementInfo.getJg_order_total())).append(",");
		insertString.append(formatDecimalField(settlementInfo.getJg_sales_total())).append(",");
		insertString.append(formatDecimalField(settlementInfo.getJg_more_off_sales_total())).append(",");
		insertString.append(formatDecimalField(settlementInfo.getJg_coupon_jing_total())).append(",");
		insertString.append(formatDecimalField(settlementInfo.getJg_coupon_dong_total())).append(",");
		insertString.append(formatDatetimeField(settlementInfo.getJg_refund_time())).append(",");
		insertString.append(formatStringField(settlementInfo.getJg_refund_id())).append(",");
		insertString.append(formatStringField(StringUtils.transferStr(settlementInfo.getJg_class1_category()))).append(",");
		insertString.append(formatStringField(StringUtils.transferStr(settlementInfo.getJg_class2_category()))).append(",");
		insertString.append(formatStringField(StringUtils.transferStr(settlementInfo.getJg_class3_category()))).append(",");
		insertString.append(formatStringField(settlementInfo.getJg_sku_no())).append(",");
		insertString.append(formatStringField(StringUtils.transferStr(settlementInfo.getJg_goods_no()))).append(",");
		insertString.append(formatDecimalField(settlementInfo.getJg_sku_price_unit())).append(",");
		insertString.append(formatStringField(settlementInfo.getJg_ticket_no())).append(",");
		insertString.append(formatStringField(settlementInfo.getJg_fee_type())).append(",");
		insertString.append(formatStringField(settlementInfo.getJg_adjustment_type())).append(",");

		insertString.append("now()").append(",");
		insertString.append("now()").append(",");
		insertString.append(formatStringField(user.getUserName())).append(",");
		insertString.append(formatStringField(user.getUserName()));

		insertString.append(")");

		return insertString.toString();
	}

	/**
	 * sql文用字符串格式化
	 *
	 * @param stringField 字段
	 * @return
	 */
	private String formatStringField(String stringField) {
		StringBuffer ret = new StringBuffer();

		if (StringUtils.isEmpty(stringField)) {
			// null 插入
			// ret.append(stringField);
			ret.append("''");
		} else  {
			ret.append("'");
			ret.append(stringField);
			ret.append("'");
		}

		return ret.toString();
	}

	/**
	 * sql文用字符串格式化
	 *
	 * @param stringField 字段
	 * @return
	 */
	private String formatDecimalField(String stringField) {
		StringBuffer ret = new StringBuffer();

		if (StringUtils.isEmpty(stringField)) {
			ret.append("0");
		} else  {
			ret.append(stringField);
		}

		return ret.toString();
	}

	/**
	 * Format decimal field.
	 *
	 * @param stringValue 字段
	 * @param decimalPlaces 小数位
	 * @return String
	 */
	private String formatDecimalField(String stringValue, int decimalPlaces) {
		String ret = "";

		try {
			Double amount = Double.valueOf(stringValue);

			NumberFormat formatter = NumberFormat.getInstance();
			formatter.setMaximumFractionDigits(decimalPlaces);
			formatter.setGroupingUsed(false);
			ret = formatter.format(amount);
		}
		catch (NumberFormatException e) {
			logger.error("FormatDecimalField", e);
			ret = "";
			throw e;
		}
		return ret;
	}

	/**
	 * sql文用字符串格式化
	 *
	 * @param stringField 字段
	 * @return
	 */
	private String formatDatetimeField(String stringField) {
		StringBuffer ret = new StringBuffer();

		if (StringUtils.isEmpty(stringField)) {
			ret.append("'");
			ret.append(emptyDatetime);
			ret.append("'");
		} else  {
			ret.append("'");
			ret.append(stringField);
			ret.append("'");
		}

		return ret.toString();
	}


	/**
	 * 天猫国际汇率取得（6.223200　－〉　6.2232）
	 *
	 * @param origTGRate 原始汇率
	 * @return
	 */
	private String formatTGRate(String origTGRate) {
		String ret = "";

		if(!StringUtils.isEmpty(origTGRate)) {
			ret = origTGRate;

			String[] rateArr = origTGRate.split("\\.");
			if (rateArr.length == 2) {
				if (rateArr[1].length() > 4) {
					ret = rateArr[0] + "." + rateArr[1].substring(0, 4);
				}
			}
		}
		return ret;
	}


	/**
	 * Remove special character for field of Weixin file
	 *
	 * @param stringField 字段
	 * @return
	 */
	private String formatWxStringField(String stringField) {
		String ret = "";

		if(!StringUtils.isEmpty(stringField))	{
			ret=stringField.replace("`", "");
		}

		return ret;
	}

	/**
	 * Format precent to number for fee rate of Weixin
	 *
	 * @param stringPercent 百分比字段
	 * @return String
	 */
	private String formatWxPercentToNumber(String stringPercent) {
		String ret = "";

		try {
			ret = formatWxStringField(stringPercent);
			ret = ret.replace("%", "");
			Double number = Double.valueOf(ret) / 100;

			NumberFormat formatter = NumberFormat.getInstance();
			formatter.setMaximumFractionDigits(3);
			formatter.setGroupingUsed(false);
			ret = formatter.format(number);
		}
		catch (NumberFormatException e) {
			logger.error("FormatPercentToNumber", e);
			ret = "";
			throw e;
		}
		return ret;
	}

	/**
	 * Calculate amount for Weixin pay.
	 *
	 * @param stringAmount
	 * @param stringReturnAmount
	 * @return String
	 */
	private String calculateAmountForWxPay(String stringAmount, String stringReturnAmount) {
		String ret = "";

		try {
			Double amount = Double.valueOf(formatWxStringField(stringAmount));
			Double returnAmount = Double.valueOf(formatWxStringField(stringReturnAmount));

			NumberFormat formatter = NumberFormat.getInstance();
			formatter.setMaximumFractionDigits(2);
			formatter.setGroupingUsed(false);
			ret = formatter.format(amount - returnAmount);
		}
		catch (NumberFormatException e) {
			logger.error("calculateAmountForWxPay", e);
			ret = "";
			throw e;
		}
		return ret;
	}

	/**
	 * Get Weixin OrderId
	 *
	 * @param origWxOrderId
	 * @return
	 */
	private String formatWxOrderId(String origWxOrderId) {
		String ret = "";
		ret = formatWxStringField(origWxOrderId);

		String[] orderIdArr = ret.split("_");
		if (orderIdArr.length == 2) {
			if (orderIdArr[0].length() > 0) {
				ret = orderIdArr[0];
			}
		}

		return ret;
	}

	/**
	 * Remove special character for field of Alipay file
	 *
	 * @param date
	 * @return
	 */
	private String formatAlipayDatetime(String date) {
		String ret = "";

		if(!StringUtils.isEmpty(date))	{
			ret = date.replaceAll("年", "-").replaceAll("月", "-").replaceAll("日", "");
		}

		return ret;
	}

	/**
	 * Remove special empty character for field of Alipay file
	 *
	 * @param payNum
	 * @return
	 */
	private String formatAlipayNumber(String payNum) {
		String ret = "";

		if(!StringUtils.isEmpty(payNum))	{
			ret = payNum.replaceAll("\\s+", "");
		}

		return ret;
	}

	/**
	 * Calculate amount for Alipay.
	 *
	 * @param stringCredit
	 * @return String
	 */
	private String calculateCreditForAlipay(String stringCredit) {
		String ret = "";

		try {
			Double credit = Double.valueOf(stringCredit) * -1;

			NumberFormat formatter = NumberFormat.getInstance();
			formatter.setMaximumFractionDigits(2);
			formatter.setGroupingUsed(false);
			ret = formatter.format(credit);
		}
		catch (NumberFormatException e) {
			logger.error("calculateCreditForAlipay", e);
			ret = "";
			throw e;
		}
		return ret;
	}

	/**
	 * 文件读入
	 *
	 * @param file 结算文件
	 * @return List<0>	返回结果
	 *			List<1>	缓存文件内容
	 *			List<2> 渠道名称
	 *			List<3>	店铺名称
	 *			List<4> 收入合计
	 *			List<5>	支出合计
	 *			List<6> 渠道ID
	 *			List<7>	店铺ID
	 */
	private List<Object> readTGFile(MultipartFile file, AjaxResponseBean result) {
		List<Object> ret = new ArrayList<>();

		boolean isSuccess = true;
		List<SettlementBean> settlementBeanList = new ArrayList<SettlementBean>();
		//	订单渠道名称
		String orderChannelName = "";
		//	店铺名称
		String cartName = "";
		//  Total income
		Double totalIncome = 0d;
		//  Total expense
		Double totalExpense = 0d;
		//  Channel ID
		String orderChannelId = "";
		//  Cart ID
		String cartId = "";

		try {
			CsvReader reader = new CsvReader(file.getInputStream(), ',', Charset.forName("GBK"));

			// Head读入
			reader.readHeaders();

			// Body读入
			while (reader.readRecord()) {
				SettlementBean settlement = new SettlementBean();
				// 文件类型
				settlement.setFileType(OmsConstants.AccountingFileType.SettlementFile);

				// 从文件字段
				settlement.setOriginSourceOrderId(reader.get(AliPayInt.Partner_transaction_id));
				settlement.setDebitForeign(reader.get(AliPayInt.Amount));
				settlement.setDebit(reader.get(AliPayInt.Rmb_amount));
				settlement.setCreditForeign(reader.get(AliPayInt.Fee));
				settlement.setSettlement(reader.get(AliPayInt.Settlement));
				settlement.setSettlement_foreign(reader.get(AliPayInt.Rmb_settlement));
				settlement.setCurrency(reader.get(AliPayInt.Currency));
				settlement.setRate(formatTGRate(reader.get(AliPayInt.Rate)));

				if (!StringUtils.isEmpty(reader.get(AliPayInt.Payment_time))) {
					settlement.setPaymentTime(DateTimeUtil.getGMTTime(reader.get(AliPayInt.Payment_time), 8));
				}

				if (!StringUtils.isEmpty(reader.get(AliPayInt.Settlement_time))) {
					settlement.setSettlementTime(DateTimeUtil.getGMTTime(reader.get(AliPayInt.Settlement_time), 8));
				}

				settlement.setTrade_type(reader.get(AliPayInt.Type));
				settlement.setTrade_status(reader.get(AliPayInt.Status));
				settlement.setAli_int_stem_from(reader.get(AliPayInt.Stem_from));
				settlement.setComment(reader.get(AliPayInt.Remarks));

				// 其他字段
				//	业务类型
				settlement.setBusinessType(OmsConstants.BusinessType.PAYMENT);
				//	账务方式
				settlement.setPayType(AccountingType.AlipayInterNational.getValue());

				//	DB关联字段取得
				String sourceOrderIdFromFile = reader.get(AliPayInt.Partner_transaction_id);
				OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfoByOrigSourceOrderId(sourceOrderIdFromFile);
				//	文件的订单，OMS不存在的场合，异常
				if (ordersInfo == null) {
					//String msgContext = String.format(OmsMessageConstants.MessageContent.ORDER_IS_NOT_EXIST, sourceOrderIdFromFile);
					//result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, msgContext);

					//isSuccess = false;
					//break;

					//For advance order, save data even though the order number doesn't exists in OMS
					settlement.setProcess_flag(false);
					settlement.setOrderChannelId("");
					settlement.setCartId("0");
					settlement.setSourceOrderId("");
					settlement.setPayNo("");
					settlement.setPayAccount("");
				} else {
					settlement.setProcess_flag(true);
					settlement.setOrderChannelId(ordersInfo.getOrderChannelId());
					settlement.setCartId(ordersInfo.getCartId());
					settlement.setSourceOrderId(ordersInfo.getSourceOrderId());
					settlement.setPayNo(ordersInfo.getPoNumber());
					settlement.setPayAccount(ordersInfo.getAccount());

					orderChannelName = ordersInfo.getOrderChannelName();
					cartName = ordersInfo.getCartName();
					orderChannelId = ordersInfo.getOrderChannelId();
					cartId = ordersInfo.getCartId();
				}

				//Get total amount
				Double rmbAmount = Double.valueOf(reader.get(AliPayInt.Rmb_amount));
				if (rmbAmount >= 0d) {
					totalIncome += rmbAmount;
				} else {
					totalExpense += rmbAmount;
				}

				settlementBeanList.add(settlement);
			}
			reader.close();

			//	结果判定
			if (isSuccess && settlementBeanList.size() == 0) {
				isSuccess = false;
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210065, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}

		} catch (Exception e) {
			isSuccess = false;

			logger.error("readTGFile", e);
		}

		ret.add(isSuccess);
		ret.add(settlementBeanList);
		ret.add(orderChannelName);
		ret.add(cartName);

		//  Format total amount
		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(2);
		formatter.setGroupingUsed(false);
		ret.add(formatter.format(totalIncome));
		ret.add(formatter.format(totalExpense));
		ret.add(orderChannelId);
		ret.add(cartId);

		return ret;
	}

	/**
	 * 支付宝文件读入
	 *
	 * @param file 结算文件
	 * @return List<0>	返回结果
	 *			List<1>	缓存文件内容
	 *			List<2> 渠道名称
	 *			List<3>	店铺名称
	 *			List<4>	支付宝帐号
	 *			List<5>	账单起始时间
	 *			List<6>	账单结束时间
	 *			List<7>	账单导出时间
	 *			List<8> 收入合计
	 *			List<9>	支出合计
	 *			List<10> 渠道ID
	 *			List<11>	店铺ID
	 */
	private List<Object> readTMFile(MultipartFile file, AjaxResponseBean result) {
		List<Object> ret = new ArrayList<>();

		boolean isSuccess = true;
		List<SettlementBean> settlementBeanList = new ArrayList<SettlementBean>();
		//	Channel of order
		String orderChannelName = "";
		//	Name of cart
		String cartName = "";
		//  Pay number of Alipay
		String payNum = "";
		//  Business type description
		String businessType = "";
		//  Notes
		String notes = "";
		//	Account number
		String accountNumber = "";
		//	Begin time
		String beginTime = "";
		//	End time
		String endTime = "";
		//	Exported time
		String exportTime = "";
		//  Total income
		String totalIncome = "0";
		//  Total expense
		String totalExpense = "0";
		//  Channel ID
		String orderChannelId = "";
		//  Cart ID
		String cartId = "";
		//  Source order id
		String sourceOrderId = "";

		//Prefix of description
		String prefixFlag = "#";
		//	Prefix of account number
		String prefixAccountNum = "#账号";
		//	Prefix of begin time
		String prefixBeginTime = "#起始日期";
		//	Prefix of exported time
		String prefixExportTime = "#导出时间";
		//	Prefix of string
		String prefixString = "[";
		//	Postfix of string
		String postfixString = "]";
		//  Prefix of total income
		String prefixTotalIncome = "#收入合计";
		//  Prefix of total expense
		String prefixTotalExpense = "#支出合计";
		//  商家保证金理赔
		Boolean isBaozhengjinlipei = false;
		String prefixBaozhengjinlipei = "商家保证金理赔";
		String prefixStringBaozhengjin = "{";
		String postfixStringBaozhengjin = "}";

		//	Title of list
		String titleOfList01 = "账务流水号";
		String titleOfList02 = "业务流水号";
		String titleOfList03 = "共";
		String titleOfList04 = "元";

		try {
			CsvReader reader = new CsvReader(file.getInputStream(), ',', Charset.forName("GBK"));

			// Head读入
			reader.readHeaders();
			String fieldValue = "";

			// Body读入
			while (reader.readRecord()) {
				fieldValue = reader.get(0);

				if(fieldValue.startsWith(prefixFlag) || (reader.get(0).equals(titleOfList01) && reader.get(1).equals(titleOfList02))) {
					if (fieldValue.startsWith(prefixAccountNum)) {
						accountNumber = fieldValue.substring(fieldValue.indexOf(prefixString) + 1, fieldValue.indexOf(postfixString));
					} else if (fieldValue.startsWith(prefixBeginTime)) {
						beginTime = DateTimeUtil.getGMTTime(formatAlipayDatetime(fieldValue.substring(fieldValue.indexOf(prefixString) + 1, fieldValue.indexOf(postfixString))), 8);
						endTime = DateTimeUtil.getGMTTime(formatAlipayDatetime(fieldValue.substring(fieldValue.lastIndexOf(prefixString) + 1, fieldValue.lastIndexOf(postfixString))), 8);
					}
					else if(fieldValue.startsWith(prefixExportTime)) {
						exportTime = DateTimeUtil.getGMTTime(formatAlipayDatetime(fieldValue.substring(fieldValue.indexOf(prefixString) + 1, fieldValue.indexOf(postfixString))), 8);
					}
					else if(fieldValue.startsWith(prefixTotalIncome)) {
						totalIncome = fieldValue.substring(fieldValue.indexOf(titleOfList03) + 1, fieldValue.indexOf(titleOfList04));
					}
					else if(fieldValue.startsWith(prefixTotalExpense)) {
						totalExpense = fieldValue.substring(fieldValue.indexOf(titleOfList03) + 1, fieldValue.indexOf(titleOfList04));
					}

					continue;
				}

				// Get pay number.
				payNum = formatAlipayNumber(reader.get(AliPay.PayNum));
				// Get business type description
				businessType = reader.get(AliPay.Paytype);
				// Get notes
				notes = reader.get(AliPay.Remarks);

				SettlementBean settlement = new SettlementBean();

				// 文件类型
				settlement.setFileType(OmsConstants.AccountingFileType.SettlementFile);

				// Read fields
				settlement.setAli_accounting_no(reader.get(AliPay.Accounting_no));
				settlement.setPayNo(payNum);
				settlement.setShop_order_id(reader.get(AliPay.Shop_order_id));
				settlement.setGoods_name(reader.get(AliPay.Goods_name));
				settlement.setSettlementTime(DateTimeUtil.getGMTTime(reader.get(AliPay.Settlement_time), 8));
				settlement.setAli_pay_account_full(reader.get(AliPay.Pay_account_full));
				settlement.setDebit(formatDecimalField(reader.get(AliPay.Debit), 2));
				// Used credit * -1 when save into database
				settlement.setCredit(calculateCreditForAlipay(reader.get(AliPay.Credit)));
				settlement.setAli_account_balance(reader.get(AliPay.Account_balance));
				settlement.setPayment_channel(reader.get(AliPay.Payment_channel));
				settlement.setTrade_type(reader.get(AliPay.Paytype));
				settlement.setCurrency(defaultCurrency);
				settlement.setComment(notes);

				// 其他字段
				//	业务类型-收费
				if (businessType.equals(OmsConstants.BusinessTypeDescription.SHOUFEI)) {
					settlement.setBusinessType(OmsConstants.BusinessType.CHARGE);
				}
				else if (businessType.equals(OmsConstants.BusinessTypeDescription.ZHUANZHANG) &&
						payNum.startsWith(prefixAlipayNum)) {
					if (notes.contains(prefixBaozhengjinlipei)) {
						//商家保证金理赔
						settlement.setBusinessType(OmsConstants.BusinessType.PAYMENT);
						sourceOrderId = notes.substring(notes.indexOf(prefixStringBaozhengjin) + 1, notes.indexOf(postfixStringBaozhengjin));
						isBaozhengjinlipei = true;
					} else {
						settlement.setBusinessType(OmsConstants.BusinessType.TRANSFER);
					}
				}
				else if (businessType.equals(OmsConstants.BusinessTypeDescription.TIXIAN)) {
					settlement.setBusinessType(OmsConstants.BusinessType.TIXIAN);
				}

				//	账务方式
				settlement.setPayType(AccountingType.AliPay.getValue());

				OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfoByPayNo(payNum);

				//如果是商家保证金理赔，用SourceOrderId再次查找订单信息
				if (isBaozhengjinlipei && ordersInfo == null && !StringUtils.isEmpty(sourceOrderId)) {
					ordersInfo = orderDetailDao.getOrdersInfoByOrigSourceOrderId(sourceOrderId);
				}

				//	文件的订单，OMS不存在的场合，异常
				if (ordersInfo == null) {
					//String msgContext = String.format(OmsMessageConstants.MessageContent.PAYNUMBER_IS_NOT_EXIST, payNum);
					//result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, msgContext);

					//isSuccess = false;
					//break;

					//For advance order, save data even though the order number doesn't exists in OMS
					settlement.setOriginSourceOrderId("");
					settlement.setOrderChannelId("");
					settlement.setCartId("0");
					settlement.setSourceOrderId("");
					settlement.setPayAccount("");
					settlement.setProcess_flag(false);
				} else {
					if(StringUtils.isEmpty(settlement.getBusinessType())) {
						settlement.setBusinessType(OmsConstants.BusinessType.PAYMENT);
					}

					settlement.setOriginSourceOrderId(ordersInfo.getOriginSourceOrderId());
					settlement.setOrderChannelId(ordersInfo.getOrderChannelId());
					settlement.setCartId(ordersInfo.getCartId());
					settlement.setSourceOrderId(ordersInfo.getSourceOrderId());
					settlement.setPayAccount(ordersInfo.getAccount());
					settlement.setProcess_flag(true);

					orderChannelName = ordersInfo.getOrderChannelName();
					cartName = ordersInfo.getCartName();

					orderChannelId = ordersInfo.getOrderChannelId();
					cartId = ordersInfo.getCartId();
				}

				settlementBeanList.add(settlement);
			}
			reader.close();

			//	结果判定
			if (isSuccess && settlementBeanList.size() == 0) {
				isSuccess = false;
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210065, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}

		} catch (Exception e) {
			isSuccess = false;

			logger.error("readTMFile", e);
		}

		ret.add(isSuccess);
		ret.add(settlementBeanList);
		ret.add(orderChannelName);
		ret.add(cartName);
		ret.add(accountNumber);
		ret.add(beginTime);
		ret.add(endTime);
		ret.add(exportTime);
		ret.add(totalIncome);
		ret.add(totalExpense);
		ret.add(orderChannelId);
		ret.add(cartId);

		return ret;
	}

	/**
	 * 京东文件读入
	 *
	 * @param file 结算文件
	 * @return List<0>	返回结果
	 *			List<1>	缓存文件内容
	 *			List<2> 渠道名称
	 */
	private List<Object> readJDFile(MultipartFile file, AjaxResponseBean result) {
		return null;
	}

	/**
	 * 京东国际文件读入
	 *
	 * @param file 结算文件
	 * @return List<0>	返回结果
	 *			List<1>	缓存文件内容
	 *			List<2> 渠道名称
	 */
	private List<Object> readJGFile(MultipartFile file, AjaxResponseBean result) {
		return null;
	}

	/**
	 * 微信文件读入
	 *
	 * @param file 结算文件
	 * @return List<0>	返回结果
	 *			List<1>	缓存文件内容
	 *			List<2> 渠道名称
	 *			List<3>	店铺名称
	 *			List<4> 收入合计
	 *			List<5>	支出合计
	 *			List<6> 渠道ID
	 *			List<7>	店铺ID
	 */
	private List<Object> readWXFile(MultipartFile file, AjaxResponseBean result) {
		List<Object> ret = new ArrayList<>();

		boolean isSuccess = true;
		List<SettlementBean> settlementBeanList = new ArrayList<SettlementBean>();
		//	Channel of order
		String orderChannelName = "";
		//	Name of cart
		String cartName = "";
		//  Order number of Weixin
		String wxOrderId = "";
		//  Total income
		String totalIncome = "0";
		//  Total expense
		String totalExpense = "0";
		//  Title of total amount
		String titleOfList01 = "总交易单数";
		String titleOfList02 = "总交易额";
		//  Counter
		int counter = 0;
		//  Last line count
		int lastCount = 0;
		//  Channel ID
		String orderChannelId = "";
		//  Cart ID
		String cartId = "";

		try {
			CsvReader reader = new CsvReader(file.getInputStream(), ',', Charset.forName("GBK"));

			// Head读入
			reader.readHeaders();

			// Body读入
			while (reader.readRecord()) {
				counter++;
				// Get OrderId at first.
				wxOrderId = formatWxOrderId(reader.get(WeixinPay.WebOrderId));

				if (reader.get(0).equals(titleOfList01) && reader.get(1).equals(titleOfList02)) {
					//Get last line count
					lastCount = counter;
					continue;
				} else if (lastCount > 0 && counter == (lastCount + 1)) {
					//Get total rmb amount in last line
					totalIncome = formatWxStringField(reader.get(1));
					totalExpense = formatWxStringField(reader.get(2));
					continue;
				} else if (StringUtils.isEmpty(wxOrderId)) {
					//Skip current line if OrderId is empty
					continue;
				}

				SettlementBean settlement = new SettlementBean();

				// 文件类型
				settlement.setFileType(OmsConstants.AccountingFileType.SettlementFile);

				// 从文件字段
				settlement.setSettlementTime(DateTimeUtil.getGMTTime(formatWxStringField(reader.get(WeixinPay.Settlement_time)), 8));
				settlement.setWx_public_account_id(formatWxStringField(reader.get(WeixinPay.Public_account_id)));
				settlement.setWx_shop_id(formatWxStringField(reader.get(WeixinPay.Shop_id)));
				settlement.setWx_special_shop_id(formatWxStringField(reader.get(WeixinPay.Special_shop_id)));
				settlement.setWx_device_id(formatWxStringField(reader.get(WeixinPay.Device_id)));
				settlement.setWx_weixin_order_id(formatWxStringField(reader.get(WeixinPay.Weixin_order_id)));
				settlement.setOriginSourceOrderId(wxOrderId);
				settlement.setWx_user_id(formatWxStringField(reader.get(WeixinPay.User_id)));
				settlement.setTrade_type(formatWxStringField(reader.get(WeixinPay.Trade_type)));
				settlement.setTrade_status(formatWxStringField(reader.get(WeixinPay.Trade_status)));
				settlement.setPayment_channel(formatWxStringField(reader.get(WeixinPay.Payment_channel)));
				settlement.setCurrency(formatWxStringField(reader.get(WeixinPay.Currency)));
				settlement.setDebit(formatDecimalField(formatWxStringField(reader.get(WeixinPay.Debit)), 2));
				settlement.setWx_company_bonus(formatWxStringField(reader.get(WeixinPay.Company_bonus)));
				settlement.setWx_weixin_refund_id(formatWxStringField(reader.get(WeixinPay.Weixin_refund_id)));
				settlement.setWx_shop_refund_id(formatWxStringField(reader.get(WeixinPay.Shop_refund_id)));
				settlement.setCredit(formatDecimalField(formatWxStringField(reader.get(WeixinPay.Credit)), 2));
				settlement.setWx_company_bonus_refund(formatWxStringField(reader.get(WeixinPay.Company_bonus_refund)));
				settlement.setWx_refund_type(formatWxStringField(reader.get(WeixinPay.Refund_type)));
				settlement.setWx_refund_status(formatWxStringField(reader.get(WeixinPay.Refund_status)));
				settlement.setGoods_name(formatWxStringField(reader.get(WeixinPay.Goods_name)));
				settlement.setWx_shop_data_package(formatWxStringField(reader.get(WeixinPay.Shop_data_package)));
				settlement.setFee(formatWxStringField(reader.get(WeixinPay.Fee)));
				settlement.setFeeRate(formatWxPercentToNumber(reader.get(WeixinPay.FeeRate)));
				settlement.setComment(formatWxStringField(reader.get(WeixinPay.WebOrderId)));

				// 其他字段
				//	业务类型
				settlement.setBusinessType(OmsConstants.BusinessType.PAYMENT);
				//	账务方式
				settlement.setPayType(AccountingType.Weixinpay.getValue());

				String sourceOrderIdFromFile = wxOrderId;
				OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfoByOrigSourceOrderId(sourceOrderIdFromFile);
				//	文件的订单，OMS不存在的场合，异常
				if (ordersInfo == null) {
					String msgContext = String.format(OmsMessageConstants.MessageContent.ORDER_IS_NOT_EXIST, sourceOrderIdFromFile);
					result.setResult(false, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION, msgContext);

					isSuccess = false;
					break;
				} else {
					settlement.setProcess_flag(true);
					settlement.setOrderChannelId(ordersInfo.getOrderChannelId());
					settlement.setCartId(ordersInfo.getCartId());
					settlement.setSourceOrderId(ordersInfo.getSourceOrderId());
					settlement.setPayNo(ordersInfo.getPoNumber());
					settlement.setPayAccount(ordersInfo.getAccount());

					orderChannelName = ordersInfo.getOrderChannelName();
					cartName = ordersInfo.getCartName();
					orderChannelId = ordersInfo.getOrderChannelId();
					cartId = ordersInfo.getCartId();
				}

				settlementBeanList.add(settlement);
			}
			reader.close();

			//	结果判定
			if (isSuccess && settlementBeanList.size() == 0) {
				isSuccess = false;
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210065, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}

		} catch (Exception e) {
			isSuccess = false;

			logger.error("readWXFile", e);
		}

		ret.add(isSuccess);
		ret.add(settlementBeanList);
		ret.add(orderChannelName);
		ret.add(cartName);
		ret.add(totalIncome);
		ret.add(totalExpense);
		ret.add(orderChannelId);
		ret.add(cartId);

		return ret;
	}

	/**
	 * 文件读入
	 *
	 * @param file 结算文件
	 * @return List<0>	返回结果
	 *			List<1>	缓存文件内容
	 *			List<2> 渠道名称
	 *			List<3>	店铺名称
	 *			List<4> 收入合计
	 *			List<5>	支出合计
	 *			List<6> 渠道ID
	 *			List<7>	店铺ID
	 */
	private List<Object> readTGFileForTran(MultipartFile file, AjaxResponseBean result) {
		List<Object> ret = new ArrayList<>();

		boolean isSuccess = true;
		List<SettlementBean> settlementBeanList = new ArrayList<SettlementBean>();
		//	订单渠道名称
		String orderChannelName = "";
		//	店铺名称
		String cartName = "";
		//  Total income
		Double totalIncome = 0d;
		//  Total expense
		Double totalExpense = 0d;
		//  Channel ID
		String orderChannelId = "";
		//  Cart ID
		String cartId = "";

		try {
			CsvReader reader = new CsvReader(file.getInputStream(), ',', Charset.forName("GBK"));

			// Head读入
			reader.readHeaders();

			// Body读入
			while (reader.readRecord()) {
				SettlementBean settlement = new SettlementBean();

				// 文件类型
				settlement.setFileType(OmsConstants.AccountingFileType.TransactionFile);

				// 从文件字段
				settlement.setOriginSourceOrderId(reader.get(AliPayIntForTran.Partner_transaction_id));
				settlement.setDebitForeign(reader.get(AliPayIntForTran.Amount));
				settlement.setDebit(reader.get(AliPayIntForTran.Rmb_amount));
				settlement.setCreditForeign(reader.get(AliPayIntForTran.Fee));
				settlement.setSettlement(reader.get(AliPayIntForTran.Rmb_settlement));
				settlement.setSettlement_foreign(reader.get(AliPayIntForTran.Settlement));
				settlement.setCurrency(reader.get(AliPayIntForTran.Currency));
				settlement.setRate(formatTGRate(reader.get(AliPayIntForTran.Rate)));
				if (!StringUtils.isEmpty(reader.get(AliPayIntForTran.Payment_time))) {
					settlement.setPaymentTime(DateTimeUtil.getGMTTime(reader.get(AliPayIntForTran.Payment_time), 8));
				}

				if (!StringUtils.isEmpty(reader.get(AliPayIntForTran.Settlement_time))) {
					settlement.setSettlementTime(DateTimeUtil.getGMTTime(reader.get(AliPayIntForTran.Settlement_time), 8));
				}

				settlement.setTrade_type(reader.get(AliPayIntForTran.Type));

				// 其他字段
				//	业务类型
				settlement.setBusinessType(OmsConstants.BusinessType.PAYMENT);
				//	账务方式
				settlement.setPayType(AccountingType.AlipayInterNational.getValue());

				//	DB关联字段取得
				String sourceOrderIdFromFile = reader.get(AliPayInt.Partner_transaction_id);
				OutFormOrderdetailOrders ordersInfo = orderDetailDao.getOrdersInfoByOrigSourceOrderId(sourceOrderIdFromFile);
				//	文件的订单，OMS不存在的场合，异常
				if (ordersInfo == null) {
					//For advance order, save data even though the order number doesn't exists in OMS
					settlement.setProcess_flag(false);
					settlement.setOrderChannelId("");
					settlement.setCartId("0");
					settlement.setSourceOrderId("");
					settlement.setPayNo("");
					settlement.setPayAccount("");
				} else {
					settlement.setProcess_flag(true);
					settlement.setOrderChannelId(ordersInfo.getOrderChannelId());
					settlement.setCartId(ordersInfo.getCartId());
					settlement.setSourceOrderId(ordersInfo.getSourceOrderId());
					settlement.setPayNo(ordersInfo.getPoNumber());
					settlement.setPayAccount(ordersInfo.getAccount());

					orderChannelName = ordersInfo.getOrderChannelName();
					cartName = ordersInfo.getCartName();
					orderChannelId = ordersInfo.getOrderChannelId();
					cartId = ordersInfo.getCartId();
				}

				//Get total amount
				Double rmbAmount = Double.valueOf(reader.get(AliPayInt.Rmb_amount));
				if (rmbAmount >= 0d) {
					totalIncome += rmbAmount;
				} else {
					totalExpense += rmbAmount;
				}

				settlementBeanList.add(settlement);
			}
			reader.close();

			//	结果判定
			if (isSuccess && settlementBeanList.size() == 0) {
				isSuccess = false;
				result.setResult(false, OmsMessageConstants.MESSAGE_CODE_210065, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
			}

		} catch (Exception e) {
			isSuccess = false;

			logger.error("readTGFileForTran", e);
		}

		ret.add(isSuccess);
		ret.add(settlementBeanList);
		ret.add(orderChannelName);
		ret.add(cartName);

		//  Format total amount
		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(2);
		formatter.setGroupingUsed(false);
		ret.add(formatter.format(totalIncome));
		ret.add(formatter.format(totalExpense));
		ret.add(orderChannelId);
		ret.add(cartId);

		return ret;
	}

	/**
	 * Settlement文件格式
	 */
	/**
	 * 国际支付宝文件格式
	 */
	private class AliPayInt {
		private static final int Partner_transaction_id = 0;
		private static final int Amount = 1;
		private static final int Rmb_amount = 2;
		private static final int Fee = 3;
		private static final int Settlement = 4;
		private static final int Rmb_settlement = 5;
		private static final int Currency = 6;
		private static final int Rate = 7;
		private static final int Payment_time = 8;
		private static final int Settlement_time = 9;
		private static final int Type = 10;
		private static final int Status = 11;
		private static final int Stem_from = 12;
		private static final int Remarks = 13;
	}

	/**
	 * 微信支付文件格式
	 */
	private class WeixinPay {
		private static final int Settlement_time = 0;
		private static final int Public_account_id = 1;
		private static final int Shop_id = 2;
		private static final int Special_shop_id = 3;
		private static final int Device_id = 4;
		private static final int Weixin_order_id = 5;
		private static final int WebOrderId = 6;
		private static final int User_id = 7;
		private static final int Trade_type = 8;
		private static final int Trade_status = 9;
		private static final int Payment_channel = 10;
		private static final int Currency = 11;
		private static final int Debit = 12;
		private static final int Company_bonus = 13;
		private static final int Weixin_refund_id = 14;
		private static final int Shop_refund_id = 15;
		private static final int Credit = 16;
		private static final int Company_bonus_refund = 17;
		private static final int Refund_type = 18;
		private static final int Refund_status = 19;
		private static final int Goods_name = 20;
		private static final int Shop_data_package = 21;
		private static final int Fee = 22;
		private static final int FeeRate = 23;
	}

	/**
	 * 支付宝文件格式
	 */
	private class AliPay {
		private static final int Accounting_no = 0;
		private static final int PayNum = 1;
		private static final int Shop_order_id = 2;
		private static final int Goods_name = 3;
		private static final int Settlement_time = 4;
		private static final int Pay_account_full = 5;
		private static final int Debit = 6;
		private static final int Credit = 7;
		private static final int Account_balance = 8;
		private static final int Payment_channel = 9;
		private static final int Paytype = 10;
		private static final int Remarks = 11;
	}

	/**
	 * Transaction文件格式
	 */
	/**
	 * 国际支付宝文件格式
	 */
	private class AliPayIntForTran {
		private static final int Partner_transaction_id = 0;
		private static final int Transaction_id = 1;
		private static final int Amount = 2;
		private static final int Rmb_amount = 3;
		private static final int Fee = 4;
		private static final int Refund = 5;
		private static final int Settlement = 6;
		private static final int Rmb_settlement = 7;
		private static final int Currency = 8;
		private static final int Rate = 9;
		private static final int Payment_time = 10;
		private static final int Settlement_time = 11;
		private static final int Type = 12;
	}
}
