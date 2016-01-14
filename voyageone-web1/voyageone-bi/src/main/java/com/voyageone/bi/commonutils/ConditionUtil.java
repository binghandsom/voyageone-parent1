package com.voyageone.bi.commonutils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.voyageone.bi.ajax.bean.AjaxSalesDetailBean;
import com.voyageone.bi.ajax.bean.AjaxSalesHomeBean;
import com.voyageone.bi.base.BiApplication;
import com.voyageone.bi.dao.ProductInfoDao;
import com.voyageone.bi.dao.cache.CacheContext;
import com.voyageone.bi.disbean.ConditionBean;
import com.voyageone.bi.disbean.ConditionBean.DateType;
import com.voyageone.bi.disbean.PageCmbBoxDisBean;
import com.voyageone.bi.tranbean.UserInfoBean;

public class ConditionUtil {
    /**
     * 简单的日期显示格式： yyyyMMdd
     */
    public static final String SIMPLE_DATE_FORMAT = "yyyyMMdd";

	/**
	 * Dimension
	 * @author DELL
	 *
	 */
	public enum Dimension {
		None("none"), TimeLine("TimeLine"), Shop("Shop"), BuyChannel("BuyChannel"), Brand("Brand"),  Category("Category"), Size("Size"), Color("Color"),
		Model("Model"), Product("Product"),  SKU("SKU");
        // 成员变量
        private String name;

        // 构造方法
        private Dimension(String name) {
            this.name = name;
        }

		public String getName() {
			return name;
		}
	}
	
	public static ConditionBean getSalesHomeYPKCondition(AjaxSalesHomeBean bean) {
		return getSalesHomeCondition(bean.getTime(), bean.getTime(), bean.getShop_ids(), bean.getSort_col(), bean.getSord());
	}
	
	public static ConditionBean getSalesHomeTimeSumL30Condition(AjaxSalesHomeBean bean) {
		Date endDate = DateTimeUtil.parse(bean.getTime(), "yyyyMMdd");
		Date startDate = DateTimeUtil.addDays(endDate, -29);
		String time_start = DateTimeUtil.formatDate(startDate, "yyyyMMdd");
		return getSalesHomeCondition(time_start, bean.getTime(), bean.getShop_ids(), bean.getSort_col(), bean.getSord());
	}
	
	public static ConditionBean getSalesHomeTimeSumYtdCondition(AjaxSalesHomeBean bean) {
		Date endDate = DateTimeUtil.parse(bean.getTime(), "yyyyMMdd");
		String time_start = DateTimeUtil.getDateYear(endDate) + "0101";
		return getSalesHomeCondition(time_start, bean.getTime(), bean.getShop_ids(), bean.getSort_col(), bean.getSord());
	}
	
	public static ConditionBean getSalesHomeCondition(String startDate, String endDate, String shop_ids, String sort, String sord) {
		ConditionBean condition = new ConditionBean();
		condition.setShop_ids(shop_ids);
		condition.setTime_type(DateType.Day.getName());
		condition.setTime_start(startDate);
		condition.setTime_end(endDate);
		//sort
		condition.setSort_col(sort);
		condition.setSord(sord);
		//BuyChannel=0
		condition.setBuyChannel_ids("0");
		return condition;
	}
	
	public static ConditionBean getSalesDetailCondition(AjaxSalesDetailBean bean, Dimension dimension, UserInfoBean userInfoBean) {
		ConditionBean condition = new ConditionBean();
		//shops
		condition.setShop_ids(bean.getShop_ids());
		
		// date
		condition.setTime_type(bean.getTime_type());
		condition.setTime_start(bean.getTime_start());
		condition.setTime_end(bean.getTime_end());
		
		//category
		condition.setCategory_ids(bean.getCategory_ids());
		condition.setCategory_child(bean.getCategory_child());
		//brand
		condition.setBrand_ids(bean.getBrand_ids());
		//color
		condition.setColor_ids(bean.getColor_ids());
		//size
		condition.setSize_ids(bean.getSize_ids());
		//product
		if (bean.getProduct_ids() != null && bean.getProduct_ids().length()>0) {
			condition.setProduct_ids(bean.getProduct_ids());
		} else if (bean.getProduct_codes() != null && bean.getProduct_codes().length()>0) {
			String productCode = bean.getProduct_codes();
			String productId = getProductIdByCode(productCode, userInfoBean);
			condition.setProduct_ids(productId);
		}
		//sort
		condition.setSort_col(bean.getSort_col());
		condition.setSord(bean.getSord());
		
		return condition;
	}
	
	public static String getProductIdByCode(String productCode, UserInfoBean userInfoBean) {
		@SuppressWarnings("unchecked")
		List<PageCmbBoxDisBean> products = (List<PageCmbBoxDisBean>) CacheContext.getInstance().get(ProductInfoDao.cacheKey);
		if (products == null || products.size() == 0) {
			ProductInfoDao productInfoDao = SpringBeanFactoryUtils.getBean(ProductInfoDao.class);
			String dbName = DaoUtils.getFirstDbName(userInfoBean);
			products = productInfoDao.selectAllProductIDAndCode(dbName);
		}
		for (PageCmbBoxDisBean bean:products) {
			if (bean.getName()!=null && bean.getName().equals(productCode)) {
				return bean.getCode();
			}
		}
		return null;
		
	}
	
	
	public static String getDateDayAddYear(DateType dateType, String date, int addYear) {
		String result = "";
		String year, month, day;
		switch(dateType) {
		case Year:
			year = date.substring(0, 4);
			result = String.valueOf(Integer.valueOf(year)+addYear);
			break;
		case Month:
			year = date.substring(0, 4);
			month = date.substring(4, 6);
			result = (Integer.valueOf(year)+addYear)+month;
			break;
		case Day:
			year = date.substring(0, 4);
			month = date.substring(4, 6);
			day = date.substring(6, 8);
			result = (Integer.valueOf(year)+addYear)+month+day;
			break;
		}
		return result;
	}
	
	public static ConditionBean copyProperties(ConditionBean source) {
		ConditionBean result = new ConditionBean();
		BeanUtils.copyProperties(source, result);
		return result;
	}
	
	public static String checkStartDate(String date) {
		int startYear = Integer.parseInt(BiApplication.readValue("start_date_year"));
		int year = Integer.parseInt(date.substring(0, 4));
		if (startYear<=year) {
			return null;
		}
		return "Start date is after "+ startYear + "-01-01";
	}
	
	public static String checkEndDate(String date) {
		int endYear = Integer.parseInt(BiApplication.readValue("end_date_year"));
		int year = Integer.parseInt(date.substring(0, 4));
		if (endYear>=year) {
			return null;
		}
		return "End date is before "+ endYear + "-12-31";
	}
	
	public static String checkCategoryLevel(String category_ids) {
		Set<String> levelSet = new HashSet<String>();
		if (category_ids != null && category_ids.length()>0) {
			String[] arr = category_ids.split(",");
			for (String category:arr) {
				String level = category.split("-")[1];
				levelSet.add(level);
			}
		}
		String result = null;
		if (levelSet.size()>1) {
			result = "Category Level is Diff";
		}
		return result;
	}

	public static ConditionBean getMomConditionBean(ConditionBean source)  {
		ConditionBean result = source.createCopy();
		DateType dateType = result.getTimeType();
		String time_start = result.getTime_start();
		String time_end = result.getTime_end();
		if (DateType.Day.toString().equals(dateType.toString()) && time_start.equals(time_end)) {
			time_start = getLastDate(dateType, Calendar.DATE, result.getTime_start());
			time_end = getLastDate(dateType, Calendar.DATE, result.getTime_end());
		} else {
			time_start = getLastDate(dateType, Calendar.MONTH, result.getTime_start());
			time_end = getLastDate(dateType, Calendar.MONTH, result.getTime_end());
		}
		result.setTime_start(time_start);
		result.setTime_end(time_end);
		return result;
	}
	
	public static ConditionBean getMomConditionBeanTimeLine(ConditionBean source)  {
		ConditionBean result = source.createCopy();
		DateType dateType = result.getTimeType();
		String time_start = result.getTime_start();
		String time_end = result.getTime_end();
		if (DateType.Day.toString().equals(dateType.toString())) {
			time_start = getLastDate(dateType, Calendar.DATE, result.getTime_start());
			time_end = getLastDate(dateType, Calendar.DATE, result.getTime_end());
		} else {
			time_start = getLastDate(dateType, Calendar.MONTH, result.getTime_start());
			time_end = getLastDate(dateType, Calendar.MONTH, result.getTime_end());
		}
		result.setTime_start(time_start);
		result.setTime_end(time_end);
		return result;
	}

	public static ConditionBean getYoyConditionBean(ConditionBean source)  {
		ConditionBean result = source.createCopy();
		DateType dateType = result.getTimeType();
		String time_start = getLastDate(dateType, Calendar.YEAR, result.getTime_start());
		String time_end = getLastDate(dateType, Calendar.YEAR, result.getTime_end());
		result.setTime_start(time_start);
		result.setTime_end(time_end);
		return result;
	}

	private static String getLastDate(DateType dateType, int field, String strDate) {
		String newStrDate = strDate;
		switch (dateType) {
		case Year:
			newStrDate = strDate.substring(0, 4)+"0101";
			break;
		case Month:
			newStrDate = strDate.substring(0, 6)+"01";
			break;
		case Day:
			break;
		}
		DateFormat df = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
		Date date = null;
		try {
			date = df.parse(newStrDate);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, -1);
        String retDate = df.format(calendar.getTime());
        switch (dateType) {
 		case Year:
 			retDate = retDate.substring(0, 4);
 			break;
 		case Month:
 			retDate = retDate.substring(0, 6);
 			break;
 		case Day:
 			break;
 		}
        return retDate;
     }
	
	 public static void main(String[] args) throws ParseException {
        System.out.println(getLastDate(DateType.Year, Calendar.MONTH, "2014"));
        System.out.println(getLastDate(DateType.Month, Calendar.MONTH, "201402"));
        System.out.println(getLastDate(DateType.Day, Calendar.MONTH, "20120229"));
    }
}
