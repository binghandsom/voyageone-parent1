package com.voyageone.common.util.inch2cm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dell on 2016/3/4.
 */
public class InchStrConvert {

    public String inchToCM(String str) {
        //值为表达式   需要计算表达式
        String regEx = "[1-9][\\d,\\s]*/[\\d,\\s]*";
        String result = replaceInches_calculator(str, regEx, "inches");//1.Piece measures 2 5/8 inches   匹配结果   2 5/8 inches      正则表达式： [1-9][\d,\s]*/[\d,\s]*inches
        result = replaceInches_calculator(result, regEx, "inch");//2.Piece measures 2 5/8 inch   匹配结果   2 5/8 inch          正则表达式： [1-9][\d,\s]*/[\d,\s]*inch

        //值为小数  \d[\d,\s]*\.?[\d,\s]*\s(inches|inch)
        regEx = "\\d[\\d,\\s]*\\.?[\\d,\\s]*";
        result = replaceInches_withoutCalculator(result, regEx, "inches");//1.Piece measures 8 .9 inches in diameter 匹配结果 8 .9 inches   正则表达式： \d[\d,\s]*\.?[\d,\s]*inches
        result = replaceInches_withoutCalculator(result, regEx, "inch");//2.Piece measures 8 .9 inch in diameter 匹配结果 8 .9 inch   正则表达式：     \d[\d,\s]*\.?[\d,\s]*inch

        return result;
    }

    private String replaceInches_calculator(String str, String regEx, String suffix) {
        String regExTmp = regEx + suffix;
        String result = str;
        // 编译正则表达式 忽略大小写的写法
        Pattern pattern = Pattern.compile(regExTmp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        ArrayList<String> strList = new ArrayList<>();
        while (matcher.find()) {
            strList.add(matcher.group(0));
        }
        Calculator calculator = new Calculator();
        UnitConvert unitConvert = new UnitConvert();
        for (String s : strList) {//2 5/ 8 inches
            //  8 1/2 inches -> 是指 -> 8又二分之一英寸（也就是8+二分之一  英寸）
//            String price = s.replace(suffix, "").replace(" ", "");
            String price = s.toUpperCase().replace(suffix.toUpperCase(), "");
            String[] strSplit = price.split("\\s+");

            double valueInches = 0;//英寸
            for (String strSplitSub : strSplit) {
                valueInches += calculator.calculate(strSplitSub);
            }

            double valueCM = unitConvert.inchToCM(valueInches);//厘米
            result = result.replace(s, " " + valueCM + " cm");
        }
        return result;
    }

    private String replaceInches_withoutCalculator(String str, String regEx, String suffix) {
        String regExTmp = regEx + suffix;
        String result = str;
        // 编译正则表达式 忽略大小写的写法
        Pattern pattern = Pattern.compile(regExTmp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        ArrayList<String> strList = new ArrayList<>();
        while (matcher.find()) {
            strList.add(matcher.group(0));
        }
        UnitConvert unitConvert = new UnitConvert();
        for (String s : strList) {//2 5/ 8 inches
            String price = s.toUpperCase().replace(suffix.toUpperCase(), "").replace(" ", "");

            double valueInches = 0;//英寸
            valueInches += new BigDecimal(price).doubleValue();

            double valueCM = unitConvert.inchToCM(valueInches);//厘米
            result = result.replace(s, " " + valueCM + " cm");
        }
        return result;
    }

}
