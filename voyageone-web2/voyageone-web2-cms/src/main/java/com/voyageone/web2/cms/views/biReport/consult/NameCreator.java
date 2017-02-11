package com.voyageone.web2.cms.views.biReport.consult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/2/9.
 */
public class NameCreator {
    public static String createPrefix(Map<String, Object> map)
    {
        Integer creatorId=(Integer)map.get("creatorId");
        Date createTime=(Date)map.get("createTime");
        Long lCreateTime=createTime.getTime();
        Long lCode=lCreateTime+creatorId;
        String codeStr=String.valueOf(lCode);
        String tempt=codeStr.substring(5,codeStr.length());
        System.out.println(tempt);
        char[] Encode={'R','E','W','T','S','B','X','P','Z','M'};
        char[] code=new char[tempt.length()];
        for(int i=0;i<tempt.length();i++)
        {
            code[i]=Encode[Integer.valueOf(String.valueOf(tempt.charAt(i)))];
        }
        return String.valueOf(code);
    }

    /**
     * 创建命名规则
     *important！ 这里你需要增加一个fileType 参数，为了方便测试注释掉了
     */
    public static String createName(Map<String,Object> nameParameters)
    {
        StringBuffer fileName=new StringBuffer();
        String reg="_";
        String channelFix = "等渠道";
        String fileTypeFix="等";
//        fileName.append(DENG);
        String fileSuffix=".xlsx";
        String[] fileTypesStr={"店铺月报","店铺周报","店铺日报",
                "商品月报","商品周报","商品日报",
                "产品月报","产品周报","产品日报",
                "sku月报","sku周报","sku日报"};

        String prefix=createPrefix(nameParameters);
        Integer MIN= 12;
        List<Integer> fileTypeList = (List<Integer>)nameParameters.get("fileTypes");
        List<String> channelCodeList= (List<String>) nameParameters.get("channelCodeList");
        fileName.append(channelCodeList.size()>1 ? channelCodeList.get(0)+channelFix: channelCodeList.get(0));
        for(Integer i:fileTypeList)
        {
            if(MIN > i) MIN=i;
        }
        String file_fix=fileTypesStr[MIN];
        fileName.append(fileTypeList.size()>1 ? channelCodeList.get(MIN-1)+channelFix: channelCodeList.get(0));
//        String channelCode=(String)nameParameters.get("channelCode");
        String staDate=(String)nameParameters.get("staDate");
        String endDate=(String)nameParameters.get("endDate");
        fileName.append(prefix);
//        channelCodeList.forEach(Object -> fileName.append(Object + reg));
        fileName.append(staDate);
        fileName.append(reg);
        fileName.append(endDate+reg);
        fileName.append(fileSuffix);
        return fileName.toString().trim();
    }
}
