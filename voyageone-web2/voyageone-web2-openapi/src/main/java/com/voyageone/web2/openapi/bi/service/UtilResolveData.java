package com.voyageone.web2.openapi.bi.service;

import com.voyageone.common.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class UtilResolveData {
    private static Logger logger = LoggerFactory.getLogger(UtilResolveData.class);

    /**
     * 解析Json，获取Web取得项目所对应的Value List——天猫
     */
    @SuppressWarnings("unchecked")
    static List<List<String>> getInfoTM(String channelId, String cartId, Map<String, Object> jsonData) {
        List<List<String>> result = new ArrayList<>();
        if (jsonData.get("content") != null) {
            Map<String, Object> contentData = (Map<String, Object>) jsonData.get("content");
            if (contentData.get("data") != null) {
                Map<String, Object> rootData = (Map<String, Object>) contentData.get("data");
                if (rootData.get("data") != null) {
                    List data = (List) rootData.get("data");
                    for (Object row : data) {
                        List<String> convertRow = transformInfoTM((List<String>) row);
                        if (!convertRow.isEmpty()) {
                            convertRow.add(0, channelId);
                            convertRow.add(0, String.valueOf(cartId));
                            result.add(convertRow);
                        }
                    }
                }
            }
        }
        return result;
    }

    private static List<String> transformInfoTM(List<String> data) {
        List<String> result = new ArrayList<>();
        int i = 0;
        for (String str : data) {
            String newStr = str;
            if (i == 0) {
                // 替换[yyyy-mm-dd -> yyyymmdd]
                if (newStr != null) {
                    newStr = newStr.replace("-", "");
                }
            }
            i++;
            // 替换[%]
            if (newStr != null && newStr.contains("%")) {
                newStr = newStr.replace("%", "");
            }

            // 替换[,]
            if (newStr != null && newStr.contains(",")) {
                newStr = newStr.replace(",", "");
            }

            // 替换[-]
            if (newStr != null && "-".equals(newStr)) {
                newStr = "0";
            }

            result.add(newStr);
        }
        return result;
    }

    /**
     * 解析Json，获取Web取得项目 List——天猫
     */
    @SuppressWarnings("unchecked")
    static List<String> getKeysTM(Map<String, Object> jsonData) {
        List<String> result = new ArrayList<>();
        if (jsonData.get("content") != null) {
            Map<String, Object> contentData = (Map<String, Object>) jsonData.get("content");
            if (contentData.get("data") != null) {
                Map<String, Object> rootData = (Map<String, Object>) contentData.get("data");
                if (rootData.get("title") != null) {
                    result = (List<String>) rootData.get("title");
                }
            }
        }
        return result;
    }

    /**
     * 获取Web取得项目对应的LIST
     */
    static List<String> getColumnFromKeysTM(List<String> keys, List<Map<String, Object>> columnInfoList) {
        List<String> result = new ArrayList<>();
        result.add("cart_id");
        result.add("channel_id");
        for (Map<String, Object> columnInfo : columnInfoList) {
            String columnWebNames = (String) columnInfo.get("columnWebName");
            String[] columnWebNameArr = columnWebNames.split(",");
            boolean isFind = false;
            for (String columnWebName : columnWebNameArr) {
                if (keys.contains(columnWebName)) {
                    isFind = true;
                    result.add((String) columnInfo.get("corColumnTableName"));
                }
            }

            if (!isFind) {
                if (!"店铺ID".equals(columnWebNames)) {
                    logger.warn(columnWebNames + " not found.");
                }
            }

        }
        return result;
    }

    /**
     * 初始化JS Store Web取得项目所对应的Date Value
     */
    @SuppressWarnings("unchecked")
    static List<List<String>> getJdDateList(String channelId, String cartId, Map<String, Object> jsonData) {

        List<List<String>> listValues = new ArrayList<>();

        String resultDataStr = (String) jsonData.get("resultData");
        if (resultDataStr == null || "".equals(resultDataStr.trim())) {
            return listValues;
        }

        Map<String, Object> resultDataMap = JacksonUtil.jsonToMap(resultDataStr);
        if (resultDataMap == null || resultDataMap.isEmpty()) {
            return listValues;
        }


        Map<String, Object> detailDataMap = (Map<String, Object>) resultDataMap.get("detail");
        if (detailDataMap == null || detailDataMap.isEmpty()) {
            return listValues;
        }

        Map<String, Object> timeDataMap = (Map<String, Object>) detailDataMap.get("Time");
        if (timeDataMap == null || timeDataMap.isEmpty()) {
            return listValues;
        }

        List<String> timeValueDataList = (List<String>) timeDataMap.get("value");
        if (timeValueDataList == null || timeValueDataList.isEmpty()) {
            return listValues;
        }

        for (String timeValueData : timeValueDataList) {
            List<String> row = new ArrayList<>();
            row.add(cartId);
            row.add(channelId);
            row.add(timeValueData);
            listValues.add(row);
        }

        return listValues;
    }


    /**
     * 解析Json，获取Web取得项目所对应的Value List——京东
     */
    @SuppressWarnings("unchecked")
    static boolean getInfoJD(Map<String, Object> columnInfoMap, Map<String, Object> jsonData, List<List<String>> listValues) {
        //截取Web取得项目在Json中的分类说明
        String strKey = columnInfoMap.get("columnWebType") + "_" + columnInfoMap.get("columnWebName");
        String keyArr[] = strKey.split("_");

        boolean isRate = false;
        if (columnInfoMap.get("columnWebName").toString().contains("Rate")) {
            isRate = true;
        }

        //json取得，遍历。
        Map<String, Object> searchData = jsonData;
        for (int i = 0; i < keyArr.length - 1; i++) {
            String key = keyArr[i];
            if (searchData.containsKey(key)) {
                Object data = searchData.get(key);
                if (data instanceof Map) {
                    searchData = (Map<String, Object>) data;
                } else if (data instanceof String) {
                    searchData = JacksonUtil.jsonToMap((String) data);
                } else {
                    break;
                }
            } else {
                return false;
            }
        }

        List timeValueDataList;
        if (strKey.contains("pcResultData")) {
            Map<String, Object> timeDataMap = (Map<String, Object>) searchData.get("Time");
            timeValueDataList = (List) timeDataMap.get("value");
        } else if (strKey.contains("mobileResultData")) {
            Map<String, Object> timeDataMap = (Map<String, Object>) searchData.get("TimeSeg");
            timeValueDataList = (List) timeDataMap.get("value");
        } else {
            Map<String, Object> timeDataMap = (Map<String, Object>) searchData.get("Time");
            timeValueDataList = (List) timeDataMap.get("value");
        }

        String lastKey = keyArr[keyArr.length - 1];
        Map<String, Object> lastKeyObjectMap = (Map<String, Object>) searchData.get(lastKey);

        if (timeValueDataList == null || timeValueDataList.isEmpty() || "null".equals(lastKeyObjectMap.get("value").toString()) || "Time".equals(lastKey) || "TimeSeg".equals(lastKey)) {
            return false;
        } else {
            List dataList = (List) lastKeyObjectMap.get("value");

            if (listValues.size() == dataList.size()) {
                for (int i = 0; i < listValues.size(); i++) {
                    //已获得的值字符串
                    List<String> valuesJD = listValues.get(i);
                    //转换Value字符串，强调：无值“-”换0；“%”的转换是为了解决比率问题
                    String valueStr = String.valueOf(dataList.get(i));
                    BigDecimal bdValue = new BigDecimal(valueStr);
                    if (isRate) {
                        bdValue = bdValue.multiply(BigDecimal.valueOf(100));
                    }
                    valuesJD.add(bdValue.toPlainString());
                }
            } else {
                for (int i = 0; i < listValues.size(); i++) {
                    //已获得的值字符串
                    List<String> valuesJD = listValues.get(i);
                    boolean isFind = false;
                    for (Object aTimeValueData : timeValueDataList) {
                        String aTimeValueDataStr = String.valueOf(aTimeValueData);
                        String strValueDate = valuesJD.get(0);
                        if (strValueDate.equals(aTimeValueDataStr)) {
                            String valueStr = String.valueOf(dataList.get(i));
                            BigDecimal bdValue = new BigDecimal(valueStr);
                            valuesJD.add(bdValue.toPlainString());
                            isFind = true;
                            break;
                        }
                    }

                    if (!isFind) {
                        valuesJD.add("0");
                    }
                }
            }
        }
        return true;
    }

}
