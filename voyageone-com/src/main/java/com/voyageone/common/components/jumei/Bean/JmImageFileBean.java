package com.voyageone.common.components.jumei.Bean;

import com.voyageone.common.util.ImgUtils;
import com.voyageone.common.util.StringUtils;

import java.io.File;
import java.io.IOException;

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
        if (file == null) {
            throw new Exception("file not found!");
        }
        if (!file.exists()) {
            throw new Exception("file not exist!");
        }
    }


    public String getBase64Content() throws IOException {
        String result = null;
        if (this.file != null && this.file.exists()) {
            result = ImgUtils.encodeToString(file);
        }
        return result;
    }
}
