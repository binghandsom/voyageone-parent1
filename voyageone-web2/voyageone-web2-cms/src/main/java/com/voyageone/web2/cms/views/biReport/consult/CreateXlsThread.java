package com.voyageone.web2.cms.views.biReport.consult;

import com.voyageone.service.dao.report.BiReportDownloadTaskDao;
import com.voyageone.service.model.report.BiReportDownloadTaskModel;

import java.util.Date;
import java.util.Map;

/**
 * Created by dell on 2017/1/19.
 */
public class CreateXlsThread implements  Runnable{
    private BiReportDownloadTaskDao biReportDownloadTaskDao;;
    private Boolean notFinish=true;
    private String fileName;
    private Map<String,Object> mapForTask;
    private Map<String,Object> mapForSelect;
    private BiRepConsultService biRepConsultService;

    CreateXlsThread( String fileName,Map<String,Object> mapForTask,Map<String,Object> mapForSelect,BiRepConsultService biRepConsultService,BiReportDownloadTaskDao biReportDownloadTaskDao)
    {
        this.fileName=fileName;
        this.mapForSelect=mapForSelect;
        this.mapForTask=mapForTask;
        this.biRepConsultService=biRepConsultService;
        this.biReportDownloadTaskDao=biReportDownloadTaskDao;
    }
    @Override
    public void run() {
        if(notFinish)
        {
            BiReportDownloadTaskModel model=new BiReportDownloadTaskModel((Integer)mapForTask.get("creatorId"),(String)mapForTask.get("fileName"),new Date(),2);
            Integer modelId=biReportDownloadTaskDao.insert(model);
            while (modelId==null||modelId==0)
            {
                modelId=biReportDownloadTaskDao.insert(model);
            }
            if(biRepConsultService.createXLSFile(mapForSelect)) {
                notFinish = false;
                model.setFinishTime(new Date());
                model.setTaskStatus(3);
                biReportDownloadTaskDao.update(model);
            }
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public BiRepConsultService getBiRepConsultService() {
        return biRepConsultService;
    }

    public void setBiRepConsultService(BiRepConsultService biRepConsultService) {
        this.biRepConsultService = biRepConsultService;
    }

    public Boolean getNotFinish() {
        return notFinish;
    }

    public void setNotFinish(Boolean notFinish) {
        this.notFinish = notFinish;
    }

    public BiReportDownloadTaskDao getBiReportDownloadTaskDao() {
        return biReportDownloadTaskDao;
    }

    public void setBiReportDownloadTaskDao(BiReportDownloadTaskDao biReportDownloadTaskDao) {
        this.biReportDownloadTaskDao = biReportDownloadTaskDao;
    }

    public Map<String, Object> getMapForTask() {
        return mapForTask;
    }

    public void setMapForTask(Map<String, Object> mapForTask) {
        this.mapForTask = mapForTask;
    }

    public Map<String, Object> getMapForSelect() {
        return mapForSelect;
    }

    public void setMapForSelect(Map<String, Object> mapForSelect) {
        this.mapForSelect = mapForSelect;
    }
}
