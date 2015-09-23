package com.voyageone.batch.bi.util;

import com.voyageone.batch.bi.spider.constants.Constants;
import com.voyageone.batch.bi.spider.constants.data.DataSearchConstants;
import com.voyageone.batch.bi.spider.constants.data.DataUrlConstants;
import com.voyageone.common.util.DateTimeUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kylin on 2015/6/4.
 */
public class UtilResolveData {

    /**
     * 检查数据获取
     *
     * @param strJsonCheck
     * @return
     * @throws Exception
     */
    public static Boolean checkJson(String strJsonCheck) throws Exception {

        Boolean boolState;

        if ((strJsonCheck.contains("data") || strJsonCheck.contains("Data")) && strJsonCheck.substring(0, 1).equals("{")) {
            boolState = true;
        } else {
            boolState = false;
        }

        return boolState;
    }

    /**
     * 获取数据搜索URL
     *
     * @param strProductIID
     * @param type
     * @param dateTo
     * @return
     */
    public static String getDataURL(String strProductIID, int type, Date dateTo) {
        String strDataUrl = "";
        switch (type) {
            case DataSearchConstants.TYPE_STORE_SHOW_TM:
                //天猫店铺数据的场合(初始化)
                strDataUrl = DataUrlConstants.TB_DATA_TITLE + DataUrlConstants.TB_DATE_TYPE_STATIC_FROM
                        + DateTimeUtil.format(DateTimeUtil.addDays(dateTo, -7), Constants.FORMAT_JSON_DATE)
                        + DataUrlConstants.TB_DATE_TYPE_STATIC_MIDDLE
                        + DateTimeUtil.format(dateTo, Constants.FORMAT_JSON_DATE)
                        + DataUrlConstants.TB_STORE_DATE_TYPE_STATIC_TO
                        + DataUrlConstants.TB_DESCRIPTION_STORE + DataUrlConstants.TB_SHOP_SHOW;
                break;

            case DataSearchConstants.TYPE_PRODUCT_SHOW_TM:
                //商品详细情况数据的场合(初始化)，调用product_iid进行数据抽取。
                strDataUrl = DataUrlConstants.TB_DATA_TITLE + DataUrlConstants.TB_DATE_TYPE_STATIC_FROM
                        + DateTimeUtil.format(DateTimeUtil.addDays(dateTo, -7), Constants.FORMAT_JSON_DATE)
                        + DataUrlConstants.TB_DATE_TYPE_STATIC_MIDDLE
                        + DateTimeUtil.format(dateTo, Constants.FORMAT_JSON_DATE)
                        + DataUrlConstants.TB_PRODUCT_DATE_TYPE_STATIC_TO
                        + DataUrlConstants.TB_DESCRIPTION_PRODUCT + strProductIID + DataUrlConstants.TB_PRODUCT_SHOW;
                break;

            case DataSearchConstants.TYPE_STORE_SHOW_JD:
                //京东店铺数据的场合(1月)
                strDataUrl = DataUrlConstants.JD_DATA_TITLE_STORE + DataUrlConstants.JD_DATA_FILTER_DATE
                        + DateTimeUtil.format(dateTo, Constants.FORMAT_JSON_DATE).substring(0, 7);
                break;

            case DataSearchConstants.TYPE_PRODUCT_SHOW_JD:
                //京东商品数据的场合(1天)，调用product_iid进行数据抽取，总体。
                strDataUrl = DataUrlConstants.JD_DATA_TITLE_PRODUCT + DataUrlConstants.JD_DATA_FILTER_DATE
                        + DateTimeUtil.format(dateTo, Constants.FORMAT_JSON_DATE)
                        + DataUrlConstants.JD_DATA_FILTER_PRONUM
                        + strProductIID
                        + DataUrlConstants.JD_DATA_FILTER_END;
                break;

            case DataSearchConstants.TYPE_PRODUCT_SHOW_JD_PC:
                //京东商品数据的场合(1天)，调用product_iid进行数据抽取，PC端。
                strDataUrl = DataUrlConstants.JD_DATA_TITLE_PRODUCT_PC + DataUrlConstants.JD_DATA_FILTER_DATE
                        + DateTimeUtil.format(dateTo, Constants.FORMAT_JSON_DATE)
                        + DataUrlConstants.JD_DATA_FILTER_PRONUM
                        + strProductIID
                        + DataUrlConstants.JD_DATA_FILTER_END;
                break;

            case DataSearchConstants.TYPE_PRODUCT_SHOW_JD_MOBILE:
                //京东商品数据的场合(1天)，调用product_iid进行数据抽取，移动端。
                strDataUrl = DataUrlConstants.JD_DATA_TITLE_PRODUCT_MOBILE + DataUrlConstants.JD_DATA_FILTER_DATE
                        + DateTimeUtil.format(dateTo, Constants.FORMAT_JSON_DATE)
                        + DataUrlConstants.JD_DATA_FILTER_PRONUM
                        + strProductIID
                        + DataUrlConstants.JD_DATA_FILTER_END_MOBILE;
                break;
        }
        return strDataUrl;
    }

    /**
     * 解析Json，获取Web取得项目 List——天猫
     *
     * @param strJson
     * @return
     */
    public static List<String> getKeysTM(String strJson) throws Exception {

        String strKeyTM = "";
        List<String> listKeyTM = new ArrayList<>();

        //截取Web取得项目在Json中的分类说明
        String key[] = {"content", "data", "title"};
        //json取得，遍历。
        JSONObject jsonObj = JSONObject.fromObject(strJson);
        for (int i = 0; i < key.length - 1; i++) {
            if (jsonObj.containsKey(key[i])) {
                jsonObj = jsonObj.getJSONObject(key[i]);
            } else {
                return null;
            }
        }

        if (jsonObj.containsKey("title")) {
            JSONArray jsonArr = jsonObj.getJSONArray("title");
            for (int i = 0; i < jsonArr.size(); i++) {
                strKeyTM = jsonArr.getString(i);
                listKeyTM.add(i, strKeyTM);
            }

            return listKeyTM;
        } else {
            return null;
        }
    }

    /**
     * 解析Json，获取Web取得项目所对应的Value List——天猫
     *
     * @param strJson
     * @return
     */
    public static List<String> getInfoTM(String strJson) throws Exception {

        String strValueTM;
        List<String> listValueTM = new ArrayList<>();
        //截取Web取得项目在Json中的分类说明
        String key[] = {"content", "data", "data"};
        //json取得，遍历。
        JSONObject jsonObj = JSONObject.fromObject(strJson);
        for (int i = 0; i < key.length - 1; i++) {
            if (jsonObj.containsKey(key[i])) {
                jsonObj = jsonObj.getJSONObject(key[i]);
            } else {
                return null;
            }
        }
        if (jsonObj.containsKey("data")) {
            if (!"null".equals(jsonObj.get("data").toString()) || !"".equals(jsonObj.get("data").toString()) || !"[]".equals(jsonObj.get("data").toString())) {
                JSONArray jsonArr = jsonObj.getJSONArray("data");
                for (int i = 0; i < jsonArr.size(); i++) {
                    //转换Value字符串，强调：“，”的转换是为了解决数字三位分割；“%”的转换是为了解决比率问题
                    strValueTM = jsonArr.getString(i).replace("[", "(").replace("]", ")").replace("%", "").replace("\"", "\'").replace(",", "");
                    //转换Value字符串，强调：“，”转换的次生效应
                    strValueTM = strValueTM.replace("\' \'", "\', \'");
                    //转换Value字符串，强调：“，”转换的次生效应
                    strValueTM = strValueTM.replace("\'\'", "\', \'");
                    //转换Value字符串，强调：无值“-”换0
                    strValueTM = strValueTM.replace("-\'", "0\'");
                    //转换Value字符串，强调：为后面日期“-”转换做准备，负号换x
                    strValueTM = strValueTM.replace("\'-", "\'x");
                    //转换Value字符串，强调：日期“-”消除
                    strValueTM = strValueTM.replace("-", "");
                    //转换Value字符串，强调：负号转换的次生效应，x换回负号
                    strValueTM = strValueTM.replace("\'x", "\'-");
                    listValueTM.add(i, strValueTM);
                }
            } else {
                return null;
            }
        } else {
            return null;
        }

        return listValueTM;
    }

    /**
     * 解析Json，获取Web取得项目所对应的Value List——京东
     *
     * @param strKey
     * @param strJson
     * @param listValues
     * @return
     */
    public static List<String> getInfoJD(String strKey, String strJson, List<String> listValues) throws Exception {

        JSONArray jsonArrCheck = null;

        strKey = strKey.replace("_pc", "");
        strKey = strKey.replace("_mobile", "");
        //截取Web取得项目在Json中的分类说明
        String key[] = strKey.split("_");
        String strJsonArr;
        String strValueJD = "";
        List<String> listValueJD = new ArrayList<>();

        //json取得，遍历。
        JSONObject jsonObj = JSONObject.fromObject(strJson);
        for (int i = 0; i < key.length - 1; i++) {
            if (jsonObj.containsKey(key[i])) {
                jsonObj = jsonObj.getJSONObject(key[i]);
            } else {
                return null;
            }
        }

        if (strKey.contains("pcResultData")) {
            JSONObject jsonObjCheck = jsonObj.getJSONObject("Time");
            jsonArrCheck = jsonObjCheck.getJSONArray("value");

        } else if (strKey.contains("mobileResultData")) {
            JSONObject jsonObjCheck = jsonObj.getJSONObject("TimeSeg");
            jsonArrCheck = jsonObjCheck.getJSONArray("value");
        } else {
            JSONObject jsonObjCheck = jsonObj.getJSONObject("Time");
            jsonArrCheck = jsonObjCheck.getJSONArray("value");
        }

        jsonObj = jsonObj.getJSONObject(key[key.length - 1]);

        if (jsonArrCheck == null || "".equals(jsonArrCheck.toString()) || "null".equals(jsonObj.get("value").toString()) || "Time".equals(key[key.length - 1]) || "TimeSeg".equals(key[key.length - 1])) {
            return null;
        } else {
            JSONArray jsonArr = jsonObj.getJSONArray("value");

            if (listValues.size() == jsonArrCheck.size()) {
                for (int i = 0; i < listValues.size(); i++) {
                    //已获得的值字符串
                    strValueJD = listValues.get(i);
                    //转换Value字符串，强调：无值“-”换0；“%”的转换是为了解决比率问题
                    strJsonArr = jsonArr.getString(i).toString().replace("-\'", "0\'").replace("%", "");
                    BigDecimal db = new BigDecimal(strJsonArr);
                    //字符串拼接
                    strValueJD = strValueJD + " " + db.toPlainString() + ",";
                    listValueJD.add(i, strValueJD);
                }
            } else {
                for (int i = 0; i < listValues.size(); i++) {
                    //已获得的值字符串
                    strValueJD = listValues.get(i);
                    for (int j = 0; j < jsonArrCheck.size(); j++) {
                        if (listValues.get(i).contains(jsonArrCheck.getString(j))) {
                            //转换Value字符串，强调：无值“-”换0；“%”的转换是为了解决比率问题
                            strJsonArr = jsonArr.getString(j).toString().replace("-\'", "0\'").replace("%", "");
                            BigDecimal db = new BigDecimal(strJsonArr);
                            //字符串拼接
                            strValueJD = strValueJD + " " + db.toPlainString() + ",";
                            break;
                        }
                    }

                    if (listValues.get(i).equals(strValueJD)) {
                        //字符串拼接
                        strValueJD = strValueJD + " " + 0 + ",";
                    }
                    listValueJD.add(i, strValueJD);
                }
            }
            return listValueJD;
        }
    }

    /**
     * 初始化Web取得项目所对应的Value List
     *
     * @param strJson
     * @return
     */
    public static List<String> initialListValues(String strJson, int iShowType) {

        List<String> listValues = new ArrayList<String>();

        //json取得，遍历。
        JSONObject jsonObj = JSONObject.fromObject(strJson);
        jsonObj = jsonObj.getJSONObject("resultData");
        switch (iShowType) {
            case DataSearchConstants.TYPE_STORE_SHOW_JD:
                jsonObj = jsonObj.getJSONObject("detail");
                break;
            default:
                jsonObj = jsonObj.getJSONObject("summary");
        }


        jsonObj = jsonObj.getJSONObject("Time");
        JSONArray jsonArr = jsonObj.getJSONArray("value");

        for (int i = 0; i < jsonArr.size(); i++) {
            listValues.add("(" + jsonArr.getString(i) + ",");
        }

        return listValues;
    }

    /**
     * 收尾Web取得项目所对应的Value List
     *
     * @param listModifyValues
     * @return
     */
    public static List<String> endListValues(List<String> listModifyValues) {
        List<String> listModified = new ArrayList<String>();

        for (String strModifyValue : listModifyValues) {
            String strModified = strModifyValue.substring(0, strModifyValue.lastIndexOf(",")) + ")";
            listModified.add(strModified);
        }

        return listModified;
    }


    public static String replaceJson(int iEcomm, String strJson) {
        switch (iEcomm) {
            case DataSearchConstants.ECOMM_TM:
                //截取Json数据
                if (strJson.contains("{\"content\"") && strJson.contains("</pre>")) {
                    strJson = strJson.substring(strJson.indexOf("{\"content\""), strJson.indexOf("</pre>"));
                }
                break;

            case DataSearchConstants.ECOMM_TB:
                //截取Json数据
                if (strJson.contains("{\"content\"") && strJson.contains("</pre>")) {
                    strJson = strJson.substring(strJson.indexOf("{\"content\""), strJson.indexOf("</pre>"));
                }
                break;

            case DataSearchConstants.ECOMM_OF:
                //strJson = strJson;
                break;


            case DataSearchConstants.ECOMM_TG:
                //截取Json数据
                if (strJson.contains("{\"content\"") && strJson.contains("</pre>")) {
                    strJson = strJson.substring(strJson.indexOf("{\"content\""), strJson.indexOf("</pre>"));
                }
                break;


            case DataSearchConstants.ECOMM_JD:
                strJson = replaceJsonJD(strJson);
                break;


            case DataSearchConstants.ECOMM_CN:
                //strJson = strJson;
                break;


            case DataSearchConstants.ECOMM_JG:
                strJson = replaceJsonJD(strJson);
                break;


        }

        return strJson;
    }

    private static String replaceJsonJD(String strJson) {
        //截取Json数据
        if (strJson.contains(">{") && strJson.contains("</pre>")) {
            strJson = strJson.substring(strJson.indexOf(">{") + 1, strJson.indexOf("</pre>"));
            strJson = strJson.replace("\"{", "{").replace("}\"", "}");
            strJson = strJson.replace("\"{", "{").replace("}\"", "}");
            strJson = strJson.replace("\\\\\"", "");
            strJson = strJson.replace("\\\"", "\"");
        }
        return strJson;
    }

}
