package com.voyageone.components.ftp.tools;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.ftp.bean.FtpSubFileBean;

/**
 * Created by DELL on 2016/5/18.
 */
public class BaseFtpUtils {

    public final static String seperator = "/";


    public static String getLocalFilePathName(FtpSubFileBean fileBean) {
        String localFilePathName = "";
        if (!StringUtils.isEmpty(fileBean.getLocalPath())) {
            localFilePathName = fileBean.getLocalPath();
        }

        if (!localFilePathName.endsWith(seperator)) {
            localFilePathName += seperator;
        }
        localFilePathName += fileBean.getLocalFilename();
        return localFilePathName;
    }

    public static String getRemoteFilePathName(FtpSubFileBean fileBean) {
        String remoteFilePathName = seperator;
        if (!StringUtils.isEmpty(fileBean.getRemotePath())) {
            remoteFilePathName = fileBean.getRemotePath();
        }

        if (!remoteFilePathName.endsWith(seperator)) {
            remoteFilePathName += seperator;
        }
        remoteFilePathName += fileBean.getRemoteFilename();
        return remoteFilePathName;
    }

}
