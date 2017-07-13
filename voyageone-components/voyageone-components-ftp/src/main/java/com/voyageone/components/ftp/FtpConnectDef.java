package com.voyageone.components.ftp;

import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.ftp.bean.FtpConnectBean;

/**
 * FtpConnectDef
 *
 * @author chuanyu.liang 2016/5/12.
 * @version 2.0.0
 * @since 2.0.0
 */
@SuppressWarnings({"Duplicates", "WeakerAccess"})
public class FtpConnectDef {

    /**
     * VO_IMAGE_SERVER Config
     */
    private final static String VO_IMAGE_SERVER = "VO_IMAGE_SERVER";

    public static FtpConnectBean getVOImageCmsFtpBean() {
        FtpConnectBean bean = new FtpConnectBean();
        bean.setHostname(Codes.getCodeName(VO_IMAGE_SERVER, "DOMAIN"));
        String strPort = Codes.getCodeName(VO_IMAGE_SERVER, "Port");
        if (!StringUtils.isEmpty(strPort)) {
            bean.setPort(Integer.parseInt(strPort));
        }
        bean.setUsername(Codes.getCodeName(VO_IMAGE_SERVER, "UserName"));
        bean.setPassword(Codes.getCodeName(VO_IMAGE_SERVER, "Password"));

        String strFileCoding = Codes.getCodeName(VO_IMAGE_SERVER, "FileCoding");
        if (!StringUtils.isEmpty(strFileCoding)) {
            bean.setCoding(strFileCoding);
        }
        return bean;
    }

    /**
     * Nexcess FTP Config
     */
    private final static String NexcessFTP_CONFIG = "NexcessFTP_CONFIG";

    public static FtpConnectBean getNexcessFtpBean() {
        FtpConnectBean bean = new FtpConnectBean();
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

    public static FtpConnectBean getScene7FtpBean() {
        FtpConnectBean bean = new FtpConnectBean();
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
    public static FtpConnectBean getFeedChannelFtpBean(String channel_id) {
        FtpConnectBean bean = new FtpConnectBean();
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

    private final static String IS_MID_FTP = "IS_MID_FTP";

    public static FtpConnectBean getImageServerMidFtpBean() {
        FtpConnectBean bean = new FtpConnectBean();
        bean.setHostname(Codes.getCodeName(IS_MID_FTP, "Domain"));
        String strPort = Codes.getCodeName(IS_MID_FTP, "Port");
        if (!StringUtils.isEmpty(strPort)) {
            bean.setPort(Integer.parseInt(strPort));
        }
        bean.setUsername(Codes.getCodeName(IS_MID_FTP, "UserName"));
        bean.setPassword(Codes.getCodeName(IS_MID_FTP, "Password"));

        String strFileCoding = Codes.getCodeName(IS_MID_FTP, "Encoding");
        if (!StringUtils.isEmpty(strFileCoding)) {
            bean.setCoding(strFileCoding);
        }
        return bean;
    }
}
