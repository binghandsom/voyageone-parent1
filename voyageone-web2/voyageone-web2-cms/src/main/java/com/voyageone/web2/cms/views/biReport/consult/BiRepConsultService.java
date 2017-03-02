package com.voyageone.web2.cms.views.biReport.consult;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.PageQueryParameters;
import com.voyageone.common.util.HttpExcuteUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.report.BiReportDownloadTaskDao;
import com.voyageone.service.daoext.report.BiReportDownloadTaskDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.report.BiReportDownloadTaskModel;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by dell on 2017/1/9.
 */
@Service
public class BiRepConsultService extends BaseService {
    private static final String API_HOST = "http://openapi.voyageone.com.cn";
//    private static final String API_HOST = "http://127.0.0.1:8081";
    private static final String CREATE_XLS_FILE_TASK_URL = "/bi/rest/report/createXlsFileTask";

    @Autowired
    private BiReportDownloadTaskDaoExt  biReportDownloadTaskDaoExt;
    @Autowired
    private BiReportDownloadTaskDao biReportDownloadTaskDao;
    public List<BiReportDownloadTaskModel> getDownloadTaskList(Map map)
    {
        List<BiReportDownloadTaskModel> taskModelList=biReportDownloadTaskDaoExt.selectNoTasksByCreatorId(map);
        return taskModelList;
    }
    /**
     * get data list count for page function
     * @param parameters
     * @return
     */
    public long getCount(PageQueryParameters parameters)
    {
        return biReportDownloadTaskDaoExt.selectCount(parameters.getSqlMapParameter());
    }
    /**
     * get data list  for page function
     * @param parameters
     * @return
     */
    public List<BiReportDownloadTaskModel> getPage(PageQueryParameters parameters)
    {
        return biReportDownloadTaskDaoExt.selectPage(parameters.getSqlMapParameter());
    }
    /**
     * 处理前段传过来的数据
     * @param params
     * @return
     */
    public Map<String,Object> dealTheFileCreateRequest(Map<String, Object> params)
    {
        boolean b=true;
        List<String> channelCodeList = (List<String>) params.get("channelCodeList");
        if ( channelCodeList.size() == 1)
        {
            b=false;
            params.put("channelCode",channelCodeList.get(0));
        }
        List<Object> oFileTypes =(List <Object>)params.get("fileTypes");
        List<Integer> fileTypes =new ArrayList<>();

        if(b) {
            for (Object ob : oFileTypes) {
                if ((ob instanceof Integer) && (Integer) ob <= 3) {
                    fileTypes.add((Integer) ob);
                }
            }
        }
        else
        {
            for (Object ob:oFileTypes)
            {
                if (ob instanceof  Integer)
                    fileTypes.add((Integer) ob);
            }
        }
        params.put("fileTypes",fileTypes);
        String fileName=NameCreator.createName(params);
        params.put("fileName",fileName);
        Map<String,Object> resultMap=new HashedMap();
        BiReportDownloadTaskModel model=new BiReportDownloadTaskModel();
        model.setCheckPeriod(params.get("staDate")+"~"+params.get("endDate"));
        model.setCheckChannels(NameCreator.getTheChannelTypeName(channelCodeList));
        model.setCheckFileTypes(NameCreator.getTheFileTypeName(fileTypes));
        model.setCreated(new Date());
        model.setFileName(fileName);
        model.setCreater((String)params.get("creatorName"));
        model.setCreater((String) params.get("creatorName"));
        model.setTaskStatus(ISheetInfo.SHEET.BASICINFO.SUBMITNOTSTART);
        Map<String,Object> tempMap=new HashMap();
        tempMap.put("creater",model.getCreater());
        tempMap.put("created",model.getCreated());
        tempMap.put("taskStatus",model.getTaskStatus());
        biReportDownloadTaskDao.insert(model);
        int id=model.getId();
        params.put("taskId",id);
        String url = API_HOST + CREATE_XLS_FILE_TASK_URL;
        logger.info("api url:" + url);
        String result = null;
        try {
            String request = JacksonUtil.bean2Json(params);
            result = HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, url , request);
        } catch (Exception e) {
            $error(e.getMessage());
        }
        if(!StringUtils.isNullOrBlank2(result)) {
            $info("api result" + result);
            Map mapResult = null;
            try {
                mapResult = JacksonUtil.jsonToMap(result);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
            Map<String,Object> data = (Map<String, Object>) mapResult.get("data");
            String ecd=(String)data.get("ecd");
            resultMap.put("ecd",ecd);
            if(ecd != null && "0".equals(ecd))
            {
              model.setTaskStatus(ISheetInfo.SHEET.BASICINFO.CREATING);
                BiReportDownloadTaskModel temp = biReportDownloadTaskDao.select(model.getId());
                if(temp.getTaskStatus() < model.getTaskStatus())
                {
                    biReportDownloadTaskDao.update(model);
                }
            }
        }
        else
        {
            resultMap.put("ecd","4100"); //远程连接api服务失败
        }
        return resultMap;
    }

    public Integer outTimeTask(Integer taskId){
        BiReportDownloadTaskModel model=biReportDownloadTaskDao.select(taskId);
        model.setTaskStatus(ISheetInfo.SHEET.BASICINFO.OUTTIME);
        return biReportDownloadTaskDao.update(model);
    }
    /**
     *
     * @param channelId chanelid
     * @param language 语言
     * @return mast数据
     */
    public Map<String, Object> init(String channelId, String language) {
        Map<String, Object> result = new HashMap<>();
        return result;
    }
    public Map<String,Object> delTask(Integer id)
    {
        Map<String,Object> delResult=new HashMap();
        boolean  result = true;
        String message ="";
        if (biReportDownloadTaskDaoExt.softDel(id) == 1)
        {
            message="删除成功，请刷新列表！";
        }
        else{
          result=false;
          message="删除失败！";
         }
         delResult.put("result",result);
        delResult.put("message",message);
        return delResult;
    }
}
