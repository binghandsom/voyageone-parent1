package com.voyageone.common;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

/**
 * {"PageIndex":1,"PageRowCount":1000}
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
    private Map<String, Object> parameters = new HashMap<>();
    private Map<String, String> orderBy = new HashMap<>();

    public String getParameterValue(String key) {
        if (!containsKey(key)) {
            return null;
        }
        Object o = parameters.get(key);
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    public long getLong(String key) {
        if (isEmpty(key)) {
            return 0;
        }
        String value = parameters.get(key).toString();
        if (value == null || value.equals("")) return 0L;
        return Long.parseLong(value);
    }

    public int getInt(String key) {
        if (isEmpty(key)) {
            return 0;
        }
        String value = parameters.get(key).toString();
        if (value == null || value.equals("")) return 0;
        return Integer.parseInt(value);
    }

    public Boolean containsKey(String key) {
        return parameters.containsKey(key);
    }

    public Boolean isEmpty(String key) {
        if (parameters.containsKey(key)) {
            String value = getParameterValue(key);
            return StringUtils.isEmpty(value);
        } else {
            return true;
        }
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, Object> parameters) {
        this.parameters = parameters;
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
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
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
            return parameters.get(key).toString();
        }
        return "";
    }

    public Object GetObject(String key) {
        if (containsKey(key)) {
            return parameters.get(key).toString();
        }
        return null;
    }

    public String[] GetListValue(String key) {
        if (containsKey(key)) {
            //string str= parameters[key];
            //  return str.EDeserializeObject<String[]>();
        }
        return null;
    }

    public void put(String key, Object value) {
        parameters.put(key, value);
    }

    public Map<String, Object> getSqlMapParameter() {
        MyPagination myPagination = new MyPagination();
        myPagination.OrderBy = "id desc";//默认
        if (orderBy.size() > 0)
            myPagination.OrderBy = orderBy.entrySet().stream().map(entiry -> entiry.getKey() + " " + entiry.getValue()).collect(joining(","));
        myPagination.setPageRowCount(this.getPageRowCount());
        myPagination.setPageIndex(this.getPageIndex());
        Map<String, Object> mapResult = FilerParameter(this.getParameters());
        mapResult.put("orderByColumn", myPagination.OrderBy);
        mapResult.put("offset", myPagination.getBeginRowIndex() - 1);
        mapResult.put("pageRowCount", myPagination.getEndRowIndex() - myPagination.getBeginRowIndex() + 1);
        return mapResult;
    }

    private Map<String, Object> FilerParameter(Map<String, Object> pMap) {
        Map<String, Object> reseltMap = new HashMap<String, Object>();
        for (String key : pMap.keySet()) {
            Object value = pMap.get(key);
            if (value != null && !value.equals("")) {
                reseltMap.put(key, value);
            }
        }
        return reseltMap;
    }

    public Map<String, String> getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Map<String, String> orderBy) {
        this.orderBy = orderBy;
    }
}
