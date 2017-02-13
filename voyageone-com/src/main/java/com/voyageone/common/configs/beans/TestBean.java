package com.voyageone.common.configs.beans;

/**
 * Created by dell on 2017/1/5.
 */
public class TestBean {
    private String channel;
    private String fileName;
    private String startTime;
    private String endTime;

    public TestBean(String channel,String fileName,String startTime,String endTime)
    {
        this.channel=channel;
        this.fileName=fileName;
        this.startTime=startTime;
        this.endTime=endTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getChannel() {
        return channel;

    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
