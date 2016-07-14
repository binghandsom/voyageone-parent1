package com.voyageone.common;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**{"PageIndex":1,"PageRowCount":1000}
 * Created by admin on 2014/8/13.
 */
public class PageQueryParameters implements Serializable {
    private static final long serialVersionUID = -6121501427655885029L;
    private int pageIndex;
    private int pageRowCount;
    private String minValue;
    private String maxValue;
    private EnumPaginationType painationType;
    private String[] queryColumn;

    public String getParameterValue(String key) {
        if (!containsKey(key)) {
            return null;
        }
        Object o = Parameters.get(key);
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    public long getLong(String key) {
        if (isEmpty(key)) {
            return 0;
        }
        String value = Parameters.get(key).toString();
        if (value == null || value.equals("")) return 0L;
        return Long.parseLong(value);
    }

    public int getInt(String key) {
        if (isEmpty(key)) {
            return 0;
        }
        String value = Parameters.get(key).toString();
        if (value == null || value.equals("")) return 0;
        return Integer.parseInt(value);
    }

    public Boolean containsKey(String key) {
        return Parameters.containsKey(key);
    }

    public Boolean isEmpty(String key) {
        if (Parameters.containsKey(key)) {
            String value = getParameterValue(key);
            return value == null || value == "";
        } else {
            return true;
        }
    }

    /// <summary>
    ///参数值  键值对    值为对象的可以序列化为字符串
    /// </summary>
    private HashMap<String, Object> Parameters = new HashMap<String, Object>();

    public HashMap<String, Object> getParameters() {
        return Parameters;
    }

    public void setParameters(HashMap<String, Object> parameters) {
        Parameters = parameters;
    }

    public PageQueryParameters() {

    }


    public int getPageIndex() {
        return this.pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }


    public int getPageRowCount() {
        return this.pageRowCount;
    }

    public void setPageRowCount(int pageRowCount) {
        this.pageRowCount = pageRowCount;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        minValue = minValue;
    }

    public String getmaxValue() {
        return maxValue;
    }

    public void setmaxValue(String maxValue) {
        maxValue = maxValue;
    }


    public EnumPaginationType getPainationType() {
        return painationType;
    }

    public void setPainationType(EnumPaginationType painationType) {
        this.painationType = painationType;
    }

    public String[] getQueryColumn() {
        return queryColumn;
    }

    public void setQueryColumn(String[] queryColumn) {
        queryColumn = queryColumn;
    }

    public String GetValue(String key) {
        if (containsKey(key)) {
            return Parameters.get(key).toString();
        }
        return "";
    }

    public Object GetObject(String key) {
        if (containsKey(key)) {
            return Parameters.get(key).toString();
        }
        return null;
    }

    public String[] GetListValue(String key) {
        if (containsKey(key)) {
            //string str= Parameters[key];
            //  return str.EDeserializeObject<String[]>();
        }
        return null;
    }
    public void put(String key,Object value) {
        Parameters.put(key, value);
    }

    //mybatis
    public Map<String, Object> getSqlMapParameter() {
        MyPagination myPagination = new MyPagination();
        myPagination.OrderBy = "id desc";//默认
        myPagination.setPageRowCount(this.getPageRowCount());
        myPagination.setPageIndex(this.getPageIndex());
        Map<String, Object> mapResult = FilerParameter(this.getParameters());
        mapResult.put("orderByColumn", myPagination.OrderBy);
        mapResult.put("offset", myPagination.getBeginRowIndex() - 1);
        mapResult.put("pageRowCount", myPagination.getEndRowIndex() - myPagination.getBeginRowIndex() + 1);
        return mapResult;
    }

    public Map<String, Object> FilerParameter(Map<String, Object> pMap) {
        Map<String, Object> reseltMap = new HashMap<String, Object>();
        for (String key : pMap.keySet()) {
            Object value = pMap.get(key);
            if (value != null && !value.equals("")) {
                reseltMap.put(key, value);
            }
        }
        return reseltMap;
    }
}
