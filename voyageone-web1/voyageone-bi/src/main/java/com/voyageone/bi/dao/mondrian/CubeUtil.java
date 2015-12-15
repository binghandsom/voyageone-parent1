package com.voyageone.bi.dao.mondrian;

import java.util.ArrayList;
import java.util.List;


public class CubeUtil {
	
	public static String getDateDay(String date) {
		return getDateDayAddYear(date, 0);
	}
	
	public static String getDateDayPreYear(String date) {
		return getDateDayAddYear(date, -1);
	}
	
	public static String getDateDayAddYear(String date, int addYear) {
		String result = "";
		String year, month, day;
		if (date.length()==4) {
			year = date.substring(0, 4);
			result = "["+(Integer.valueOf(year)+addYear)+"]";
		} else if(date.length()==6) {
			year = date.substring(0, 4);
			month = date.substring(4, 6);
			result = "["+(Integer.valueOf(year)+addYear)+"].["+Integer.valueOf(month)+"]";
		} else if(date.length()==8) {
			year = date.substring(0, 4);
			month = date.substring(4, 6);
			day = date.substring(6, 8);
			result = "["+(Integer.valueOf(year)+addYear)+"].["+Integer.valueOf(month)+"].["+Integer.valueOf(day)+"]";
		}
		return result;
	}
	
	public static List<String> getConditonValueId(List<String> ids) {
		List<String> result = new ArrayList<String>();
		if (ids != null && ids.size()>0) {
			for (String id:ids) {
				result.add(".&[" + id + "]");
			}
		}
		return result;
	}

}
