package com.voyageone.components.ftp;

/**
 * FtpConstants
 *
 * @author chuanyu.liang 2016/5/12.
 * @version 2.0.0
 * @since 2.0.0
 */
public class FtpConstants {

    public enum FtpTypeEnum {
        FTP,
        SFTP
    }

    public enum FtpConnectEnum {
        /**
         * cms image sftp
         */
        VO_IMAGE_CMS,

        /**
         * nexcess ftp
         */
        @Deprecated NEXCESS_FTP,

        /**
         * scene7 ftp
         */
        @Deprecated SCENE7_FTP,

        /**
         * feed channel ftp
         */
        FEED_CHANNEL_FTP,

        /**
         * Image Server (Middle Ware)
         */
        IS,
    }
}
