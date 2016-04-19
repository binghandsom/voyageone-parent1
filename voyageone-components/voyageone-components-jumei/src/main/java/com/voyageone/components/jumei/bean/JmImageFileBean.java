package com.voyageone.components.jumei.bean;

import com.voyageone.common.util.ImgUtils;
import com.voyageone.common.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 商家图片文件结构。
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since 2.0.0
 */
public class JmImageFileBean extends JmBaseBean {

    //图片名，不需要传后缀名；
    private String imgName;

    //后缀名；
    private String extName;

    //目录名（参数范围: 只允许数字、下划线、字母；大于1小于10个字节）
    private String dirName;

    //发货地区.
    private Boolean needReplace = false;

    //本地文件
    private File file;

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public Boolean getNeedReplace() {
        return needReplace;
    }

    public void setNeedReplace(Boolean needReplace) {
        this.needReplace = needReplace;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void check() throws Exception {
        if (StringUtils.isEmpty(imgName)) {
            throw new Exception("imgName not found!");
        }
        if (StringUtils.isEmpty(dirName)) {
            throw new Exception("dirName not found!");
        }
        if (needReplace == null) {
            throw new Exception("needReplace not found!");
        }

        if (file == null && inputStream == null) {
            throw new Exception("file or inputStream not exist!");
        }

        if (file != null && !file.exists()) {
            throw new Exception("file not exist!");
        }
//        if (inputStream != null && inputStream.available() == 0) {
//            throw new Exception("inputStream content not exist!");
//        }

        if (inputStream != null && extName == null) {
            throw new Exception("extName not found!");
        }
    }


    public String getBase64Content() throws IOException {
        String result = null;
        if (this.file != null && this.file.exists()) {
            result = ImgUtils.encodeToString(file);
        }else if(this.inputStream!=null ){
            result =ImgUtils.encodeToString(inputStream, extName);
        }
        return result;
    }

    /** 图片流 */
    private transient InputStream inputStream;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
