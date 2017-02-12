package com.voyageone.web2.cms.views.biReport.consult;

import org.apache.commons.collections.map.HashedMap;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/2/9.
 */
public class NameCreator {

    public static Map<String,String> channelName=new HashMap();
    static
    {
        channelName.put("007","Champion");
        channelName.put("010","Jewelry");
        channelName.put("012","BCBG");
        channelName.put("014","WMF");
        channelName.put("017","LV");
        channelName.put("018","Target");
        channelName.put("024","OverStock");
        channelName.put("030","Wella");
    }


    public static String createPrefix(Map<String, Object> map)
    {
        Integer creatorId=(Integer)map.get("creatorId");
        Date createTime=(Date)map.get("createTime");
        Long lCreateTime=createTime.getTime();
        Long lCode=lCreateTime+creatorId;
        String codeStr=String.valueOf(lCode);
        String tempt=codeStr.substring(1,codeStr.length());
        System.out.println(tempt);
        char[] Encode={'R','E','W','T','S','B','X','P','Z','M','A','C','F','G'};
        char[] code=new char[tempt.length()];
        for(int i=0;i<tempt.length();i++)
        {
            code[i]=Encode[Integer.valueOf(String.valueOf(tempt.charAt(i)))];
        }
        return String.valueOf(code);
    }
    /**
     * 创建命名规则
     */
    public static String createName(Map<String,Object> nameParameters)
    {
        StringBuffer fileName=new StringBuffer();
        String reg="_";
        String channelFix = "等渠道";
        String fileTypeFix="等报告";
        String fileSuffix=".xlsx";
        String prefix=createPrefix(nameParameters);
        fileName.append(prefix+" ");
        Integer MIN= 12;
        List<Integer> fileTypeList = (List<Integer>)nameParameters.get("fileTypes");
        List<String> channelCodeList= (List<String>) nameParameters.get("channelCodeList");
        fileName.append(channelCodeList.size()>1 ? channelName.get(channelCodeList.get(0))+channelFix: channelName.get(channelCodeList.get(0)));
        for(Integer i:fileTypeList)
        {
            if(MIN > i) MIN=i;
        }
        fileName.append(fileTypeList.size()>1 ?ISheetInfo.SHEET.SHEETNAME.SheetTitles[MIN-1]+fileTypeFix: ISheetInfo.SHEET.SHEETNAME.SheetTitles[fileTypeList.get(0)-1]);
        fileName.append(fileSuffix);
        return fileName.toString().trim();
    }
    public static String getTheChannelTypeName(List<String> list) {
        String reg = " ";
        String suffix = "等";
        StringBuffer channels = new StringBuffer();
        if (null == list) {
            return channels.toString();
        }
        if (list.size() == 0) return channels.append(list.get(0)).toString();
        else {
            int Max = list.size() > 3 && list.size() > 1 ? 3 : list.size();
            for (int i = 0; i < Max; i++) {
                channels.append(channelName.get(list.get(i)));
            }
            channels.append(suffix);
            channels.append(list.size());
            channels.append("个渠道");
            return channels.toString();
        }
    }
    /**
     * 获取filetypes 名称
     * @param list
     * @return
     */
    public static String getTheFileTypeName(List<Integer> list)
    {
        String reg=" ";
        String suffix="等";
        StringBuffer fileTypes=new StringBuffer();
        if( null ==  list)
        {
            return fileTypes.toString();
        }
        if(list ==null)
            return null;
        if(list.size() == 0) return fileTypes.append(list.get(0)).toString();
        else
        {
            int Max=list.size()>3&&list.size()>1? 3:list.size();
            for(int i=0;i<Max;i++)
            {
                fileTypes.append(ISheetInfo.SHEET.SHEETNAME.SheetTitles[i]+reg);
            }
            fileTypes.append(suffix);
            fileTypes.append(list.size());
            fileTypes.append("个文件类型");
            return fileTypes.toString();
        }
    }
}
