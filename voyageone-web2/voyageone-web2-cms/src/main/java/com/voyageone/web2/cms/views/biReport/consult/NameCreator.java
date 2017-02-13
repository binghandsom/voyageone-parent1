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
        String fileTypeFix="等报表";
        String fileSuffix=".xlsx";
        String prefix=createPrefix(nameParameters);
        fileName.append(prefix+"_");
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
    /**
     * channelsName 名称
     * @param channelList
     * @return
     */
    public static String getTheChannelTypeName(List<String> channelList)
    {
        String reg=",";
        String suffix="等";
        StringBuffer channels=new StringBuffer();
        if(channelList ==null || channelList.size() == 0)
            return null;
        else
        if(channelList.size() <= 3 )
        {
            for (int i = 0; i < channelList.size()-1; i++) {
                channels.append(channelName.get(channelList.get(i).trim())+reg);
            }
            channels.append(channelName.get(channelList.get(channelList.size()-1).trim()));
            return channels.toString();
        }
        else {
            channels.append(channelName.get(channelList.get(0).trim()));
            channels.append(suffix);
            channels.append(channelList.size());
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
        String reg=",";
        String suffix="等";
        StringBuffer fileTypes=new StringBuffer();
        if(list ==null || list.size() == 0)
            return null;
        else
            if(list.size() <= 3 )
            {
                for (int i = 0; i < list.size()-1; i++) {
                    fileTypes.append(ISheetInfo.SHEET.SHEETNAME.SheetTitles[list.get(0)]+reg);
                }
                fileTypes.append(ISheetInfo.SHEET.SHEETNAME.SheetTitles[list.get(list.size()-1)]);
                return fileTypes.toString();
            }
            else {
                    fileTypes.append(ISheetInfo.SHEET.SHEETNAME.SheetTitles[getTheMinOfFileTypeList(list)-1]);
                    fileTypes.append(suffix);
                    fileTypes.append(list.size());
                    fileTypes.append("个文件报表");
                    return fileTypes.toString();
             }
    }

    /**
     * get the min of the file type value
     * @param list
     * @return
     */

    public static int getTheMinOfFileTypeList(List<Integer> list)
    {
        int min = 12;
        for (int i = 0; i < list.size(); i++) {
            if (min > list.get(i)) min = list.get(i);
        }
        return min;
    }
}
