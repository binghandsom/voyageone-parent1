package com.voyageone.components.ftp;

import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.util.StringUtils;
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
    public static FtpFilesBean getFtpFilesBean(FtpBeanEnum ftpBeanEnum, String... args) {
        return new FtpFilesBean(createBaseFtpBean(ftpBeanEnum, args));
    }

    /**
     * getUploadFtpBean
     *
     * @return ftpBean
     */
    public static FtpDirectoryBean getFtpDirectoryBean(FtpBeanEnum ftpBeanEnum, String... args) {
        return new FtpDirectoryBean(createBaseFtpBean(ftpBeanEnum, args));
    }

    /**
     * getUploadFtpBean
     *
     * @return ftpBean
     */
    private static BaseFtpBean createBaseFtpBean(FtpBeanEnum ftpBeanEnum, String... args) {
        switch (ftpBeanEnum) {
            case VO_IMAGE_CMS:
                return getVOImageCmsFtpBean();
            case NEXCESS_FTP:
                return getNexcessFtpBean();
            case SCENE7_FTP:
                return getScene7FtpBean();
            case FEED_CHANNEL_FTP:
                if (args == null || args.length==0) {
                    throw new RuntimeException(String.format("channelId not found[%s].", ftpBeanEnum));
                }
                return getFeedChannelFtpBean(args[0]);
            default:
                throw new RuntimeException(String.format("FtpBeanEnum not found[%s].", ftpBeanEnum));
        }
    }


    /**
     * VO_IMAGE_SERVER Config
     */
    private final static String VO_IMAGE_SERVER = "VO_IMAGE_SERVER";
    private static BaseFtpBean getVOImageCmsFtpBean() {
        BaseFtpBean bean = new BaseFtpBean();
        bean.setHostname(Codes.getCodeName(VO_IMAGE_SERVER, "DOMAIN"));
        String strPort = Codes.getCodeName(VO_IMAGE_SERVER, "Port");
        if (!StringUtils.isEmpty(strPort)) {
            bean.setPort(Integer.parseInt(strPort));
        }
        bean.setUsername(Codes.getCodeName(VO_IMAGE_SERVER, "UserName"));
        bean.setPassword(Codes.getCodeName(VO_IMAGE_SERVER, "Password"));
        return bean;
    }

    /**
     * Nexcess FTP Config
     */
    private final static String NexcessFTP_CONFIG = "NexcessFTP_CONFIG";
    private static BaseFtpBean getNexcessFtpBean() {
        BaseFtpBean bean = new BaseFtpBean();
        bean.setHostname(Codes.getCodeName(NexcessFTP_CONFIG, "Url"));
        String strPort = Codes.getCodeName(NexcessFTP_CONFIG, "Port");
        if (!StringUtils.isEmpty(strPort)) {
            bean.setPort(Integer.parseInt(strPort));
        }
        bean.setUsername(Codes.getCodeName(NexcessFTP_CONFIG, "UserName"));
        bean.setPassword(Codes.getCodeName(NexcessFTP_CONFIG, "Password"));

        String strFileCoding = Codes.getCodeName(NexcessFTP_CONFIG, "FileCoding");
        if (!StringUtils.isEmpty(strFileCoding)) {
            bean.setCoding(strFileCoding);
        }

        return bean;
    }

    /**
     * Scene7 FTP Config
     */
    private final static String S7FTP_CONFIG = "S7FTP_CONFIG";
    private static BaseFtpBean getScene7FtpBean() {
        BaseFtpBean bean = new BaseFtpBean();
        bean.setHostname(Codes.getCodeName(S7FTP_CONFIG, "Url"));
        String strPort = Codes.getCodeName(S7FTP_CONFIG, "Port");
        if (!StringUtils.isEmpty(strPort)) {
            bean.setPort(Integer.parseInt(strPort));
        }
        bean.setUsername(Codes.getCodeName(S7FTP_CONFIG, "UserName"));
        bean.setPassword(Codes.getCodeName(S7FTP_CONFIG, "Password"));

        String strFileCoding = Codes.getCodeName(S7FTP_CONFIG, "FileCoding");
        if (!StringUtils.isEmpty(strFileCoding)) {
            bean.setCoding(strFileCoding);
        }
        return bean;
    }

    /**
     * Feed FTP Config
     */
    private static BaseFtpBean getFeedChannelFtpBean(String channel_id) {
        BaseFtpBean bean = new BaseFtpBean();
        bean.setHostname(Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_url));
        String strPort = Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_port);
        if (!StringUtils.isEmpty(strPort)) {
            bean.setPort(Integer.parseInt(strPort));
        }
        bean.setUsername(Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_username));
        bean.setPassword(Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_password));

        String strFileCoding = Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_file_coding);
        if (!StringUtils.isEmpty(strFileCoding)) {
            bean.setCoding(strFileCoding);
        }
        return bean;
    }


}
