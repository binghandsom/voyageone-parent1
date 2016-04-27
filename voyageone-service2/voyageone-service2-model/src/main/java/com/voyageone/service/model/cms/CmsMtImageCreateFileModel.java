package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsMtImageCreateFileModel extends BaseModel {

    /**
     * 请求参数生成的hashcode
     */
    private long hashCode;

    /**
     * 渠道id
     */
    private String channelId;

    /**
     * 模板id
     */
    private int templateId;

    /**
     * 上传oss的的文件名
     */
    private String file;

    /**
     * 本地文件路径
     */
    private String filePath;

    /**
     * 模板参数
     */
    private String vparam;

    /**
     * 阿里云oss 文件存放路径 不包含域名shenzhen-vo.oss-cn-shenzhen.aliyuncs.com
     */
    private String ossFilePath;

    /**
     * 美国cdn 文件存放路径  仅channel:001  需要生成
     */
    private String usCdnFilePath;

    /**
     * 1:生成成功
     */
    private int state;

    /**
     * oss阿里云上传状态   1:上传成功
     */
    private int ossState;

    /**
     * 美国cdn上传状态     1:上传成功
     */
    private int uscdnState;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 错误码
     */
    private int errorCode = 0;

    /**
     * 请求参数生成的hashcode
     */
    public long getHashCode() {

        return this.hashCode;
    }

    public void setHashCode(long hashCode) {
        this.hashCode = hashCode;
    }

    /**
     * 渠道id
     */
    public String getChannelId() {
        return this.channelId;
    }

    public void setChannelId(String channelId) {
        if (channelId != null) {
            this.channelId = channelId;
        } else {
            this.channelId = "";
        }
    }

    /**
     * 模板id
     */
    public int getTemplateId() {
        return this.templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    /**
     * 上传oss的的文件名
     */
    public String getFile() {
        return this.file;
    }

    public void setFile(String file) {
        if (file != null) {
            this.file = file;
        } else {
            this.file = "";
        }
    }

    /**
     * 本地文件路径
     */
    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        if (filePath != null) {
            this.filePath = filePath;
        } else {
            this.filePath = "";
        }
    }

    /**
     * 模板参数
     */
    public String getVparam() {
        return this.vparam;
    }

    public void setVparam(String vparam) {
        if (vparam != null) {
            this.vparam = vparam;
        } else {
            this.vparam = "";
        }
    }

    /**
     * 阿里云oss 文件存放路径 不包含域名shenzhen-vo.oss-cn-shenzhen.aliyuncs.com
     */
    public String getOssFilePath() {
        return this.ossFilePath;
    }

    public void setOssFilePath(String ossFilePath) {
        if (ossFilePath != null) {
            this.ossFilePath = ossFilePath;
        } else {
            this.ossFilePath = "";
        }
    }

    /**
     * 美国cdn 文件存放路径  仅channel:001  需要生成
     */
    public String getUsCdnFilePath() {
        return this.usCdnFilePath;
    }

    public void setUsCdnFilePath(String usCdnFilePath) {
        if (usCdnFilePath != null) {
            this.usCdnFilePath = usCdnFilePath;
        } else {
            this.usCdnFilePath = "";
        }
    }

    /**
     * 1:生成成功
     */
    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    /**
     * oss阿里云上传状态   1:上传成功
     */
    public int getOssState() {
        return this.ossState;
    }

    public void setOssState(int ossState) {
        this.ossState = ossState;
    }

    /**
     * 美国cdn上传状态     1:上传成功
     */
    public int getUscdnState() {
        return this.uscdnState;
    }

    public void setUscdnState(int uscdnState) {
        this.uscdnState = uscdnState;
    }

    /**
     * 错误信息
     */
    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        if (errorMsg != null) {
            this.errorMsg = errorMsg;
        } else {
            this.errorMsg = "";
        }

    }

    /**
     * 错误码
     */
    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}