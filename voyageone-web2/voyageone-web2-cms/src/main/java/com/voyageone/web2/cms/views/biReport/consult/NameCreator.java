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
        String prefix=createPrefix(nameParameters);
        List<Integer> fileTypeList = (List<Integer>)nameParameters.get("fileTypes");
        String reg="_";
        String fileSuffix=".xlsx";
        StringBuffer fileName=new StringBuffer();
        String channelCode=(String)nameParameters.get("channelCode");
        String staDate=(String)nameParameters.get("staDate");
        String endDate=(String)nameParameters.get("endDate");
        fileName.append(prefix);
        fileName.append(channelCode+reg);
        fileName.append(staDate);
        fileName.append(reg);
        fileName.append(endDate+reg);
        if(fileTypeList.contains(1))
        {
            fileName.append(ISheetInfo.SHEET.SHEETNAME.SheetTitles[0]+reg);
        }
        if(fileTypeList.contains(2))
        {
            fileName.append(ISheetInfo.SHEET.SHEETNAME.SheetTitles[1]+reg);
        }
        if(fileTypeList.contains(3))
        {
            fileName.append(ISheetInfo.SHEET.SHEETNAME.SheetTitles[2]+reg);
        }
        if(fileTypeList.contains(4))
        {
            fileName.append(ISheetInfo.SHEET.SHEETNAME.SheetTitles[3]+reg);
        }
        if(fileTypeList.contains(5))
        {
            fileName.append(ISheetInfo.SHEET.SHEETNAME.SheetTitles[4]+reg);
        }
        if(fileTypeList.contains(6))
        {
            fileName.append(ISheetInfo.SHEET.SHEETNAME.SheetTitles[5]+reg);
        }
        if(fileTypeList.contains(7))
        {
            fileName.append(ISheetInfo.SHEET.SHEETNAME.SheetTitles[6]+reg);
        }
        if(fileTypeList.contains(8))
        {
            fileName.append(ISheetInfo.SHEET.SHEETNAME.SheetTitles[7]+reg);
        }
        if(fileTypeList.contains(9))
        {
            fileName.append(ISheetInfo.SHEET.SHEETNAME.SheetTitles[8]+reg);
        }
        if(fileTypeList.contains(10))
        {
            fileName.append(ISheetInfo.SHEET.SHEETNAME.SheetTitles[9]+reg);
        }
        if(fileTypeList.contains(11))
        {
            fileName.append(ISheetInfo.SHEET.SHEETNAME.SheetTitles[10]+reg);
        }
        if(fileTypeList.contains(12))
        {
            fileName.append(ISheetInfo.SHEET.SHEETNAME.SheetTitles[11]+reg);
        }
        fileName.append(fileSuffix);
        return fileName.toString().trim();
    }
}
