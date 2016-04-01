package com.voyageone.components.ftp;

import com.voyageone.common.configs.Codes;
import com.voyageone.components.ftp.bean.BaseFtpBean;
import com.voyageone.components.ftp.bean.FtpDirectoryBean;
import com.voyageone.components.ftp.bean.FtpFilesBean;

/**
 * FtpBeanFactory
 *
 * @author chuanyu.liang 2016/5/12.
 * @version 2.0.0
 * @since 2.0.0
 */
public class FtpBeanFactory {

    /**
     * getUploadFtpBean
     *
     * @return ftpBean
     */
    public static FtpFilesBean getFtpFilesBean(FtpBeanEnum ftpBeanEnum) {
        switch (ftpBeanEnum) {
            case VO_IMAGE_CMS:
                return new FtpFilesBean(getVOImageCmsFtpBean());
            case NEXCESS_FTP:
                return new FtpFilesBean(getNexcessFtpBean());
            default:
                throw new RuntimeException("FtpBeanEnum not found.");
        }
    }

    /**
     * getUploadFtpBean
     *
     * @return ftpBean
     */
    public static FtpDirectoryBean getFtpDirectoryBean(FtpBeanEnum ftpBeanEnum) {
        switch (ftpBeanEnum) {
            case VO_IMAGE_CMS:
                return new FtpDirectoryBean(getVOImageCmsFtpBean());
            case NEXCESS_FTP:
                return new FtpDirectoryBean(getNexcessFtpBean());
            default:
                throw new RuntimeException("FtpBeanEnum not found.");
        }
    }


    /**
     * getUploadFtpBeanVOImageCms
     */
    private static BaseFtpBean getVOImageCmsFtpBean() {
        BaseFtpBean bean = new BaseFtpBean();
        bean.setHostname(Codes.getCodeName("VO_IMAGE_SERVER", "DOMAIN"));
        bean.setPort(Integer.parseInt(Codes.getCodeName("VO_IMAGE_SERVER", "prot")));
        bean.setUsername(Codes.getCodeName("VO_IMAGE_SERVER", "cmsUserName"));
        bean.setPassword(Codes.getCodeName("VO_IMAGE_SERVER", "cmsPassword"));
        return bean;
    }

    /**
     * getUploadFtpBeanVOImageCms
     */
    private static BaseFtpBean getNexcessFtpBean() {
        BaseFtpBean bean = new BaseFtpBean();
        bean.setHostname(Codes.getCodeName("NexcessFTP_CONFIG", "Url"));
        bean.setPort(Integer.parseInt(Codes.getCodeName("NexcessFTP_CONFIG", "Port")));
        bean.setUsername(Codes.getCodeName("NexcessFTP_CONFIG", "UserName"));
        bean.setPassword(Codes.getCodeName("NexcessFTP_CONFIG", "Password"));
        return bean;
    }
}
