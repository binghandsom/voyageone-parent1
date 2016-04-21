package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;
import com.voyageone.common.Constants;

import java.util.regex.Pattern;

/**
 * Created by jeff.duan on 2016/4/20.
 */
public class CmsBtImagesModel extends BaseModel {
    private Long id;
    private String channelId;
    private String code;
    private String originalUrl;
    private String imgName;
    private Integer updFlg;
    public static final String URL_FORMAT = "[~@.' '#$%&*_''/‘’^\\()]";
    private final Pattern special_symbol = Pattern.compile(URL_FORMAT);

    public CmsBtImagesModel(String channelId, String code, String imageUrl, int index, String modifier){
        this.channelId = channelId;
        this.originalUrl = imageUrl;
        this.imgName = channelId + "-" + special_symbol.matcher(code).replaceAll(Constants.EmptyString) + "-" + index;
        this.code = code;
        this.updFlg = 0;
        this.setCreater(modifier);
    }

    public CmsBtImagesModel(){
        super();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public Integer getUpdFlg() {
        return updFlg;
    }

    public void setUpdFlg(int updFlg) {
        this.updFlg = updFlg;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
