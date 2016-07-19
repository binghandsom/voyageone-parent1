package com.voyageone.task2.cms.service.monitor;

/**
 * Created by DELL on 2016/7/8.
 */
public class FileMonitorBean {

    private String filePath;

    private Object extendObj;

    public FileMonitorBean(String filePath) {
        this.filePath = filePath;
    }

    public FileMonitorBean(String filePath, Object extendObj) {
        this.filePath = filePath;
        this.extendObj = extendObj;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @SuppressWarnings("unchecked")
    public <T> T getExtendObj() {
        return (T) extendObj;
    }

    public void setExtendObj(Object extendObj) {
        this.extendObj = extendObj;
    }
}
