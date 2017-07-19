package com.voyageone.components.ftp;

import com.voyageone.common.util.StringUtils;
import com.voyageone.components.ftp.bean.FtpConnectBean;
import com.voyageone.components.ftp.service.BaseFtpComponent;
import com.voyageone.components.ftp.service.FtpComponent;
import com.voyageone.components.ftp.service.SftpComponent;

/**
 * FtpComponentFactory
 *
 * @author chuanyu.liang 2016/5/12.
 * @version 2.0.0
 * @since 2.0.0
 */
public class FtpComponentFactory {

    public static BaseFtpComponent get(FtpConstants.FtpConnectEnum connectEnum) {
        switch (connectEnum) {
            case IS:
            case VO_IMAGE_CMS:
                return getSFtpComponent(connectEnum);
            case SCENE7_FTP:
            case FEED_CHANNEL_FTP:
                return getFtpComponent(connectEnum);
            default:
                throw new UnSupportedFtpConnectEnumException(connectEnum);
        }
    }

    /**
     * getFtpComponent
     *
     * @param connectEnum FtpConnectEnum
     * @return BaseFtpComponent
     */
    public static BaseFtpComponent getFtpComponent(FtpConstants.FtpConnectEnum connectEnum) {
        return getFtpComponent(FtpConstants.FtpTypeEnum.FTP, connectEnum, null);
    }

    /**
     * getSFtpComponent
     *
     * @param connectEnum FtpConnectEnum
     * @return BaseFtpComponent
     */
    public static BaseFtpComponent getSFtpComponent(FtpConstants.FtpConnectEnum connectEnum) {
        return getFtpComponent(FtpConstants.FtpTypeEnum.SFTP, connectEnum, null);
    }

    /**
     * getFtpComponent
     *
     * @param typeEnum    FtpTypeEnum
     * @param connectEnum connectEnum
     * @param channelId   String
     * @return BaseFtpComponent
     */
    private static BaseFtpComponent getFtpComponent(FtpConstants.FtpTypeEnum typeEnum, FtpConstants.FtpConnectEnum connectEnum, String channelId) {
        FtpConnectBean connectBean = createConnectBean(connectEnum, channelId);
        switch (typeEnum) {
            case FTP:
                return new FtpComponent(connectBean);
            case SFTP:
                return new SftpComponent(connectBean);
            default:
                return null;
        }
    }

    /**
     * createConnectBean
     */
    private static FtpConnectBean createConnectBean(FtpConstants.FtpConnectEnum connectEnum, String channelId) {
        switch (connectEnum) {
            case VO_IMAGE_CMS:
                return FtpConnectDef.getVOImageCmsFtpBean();
            case NEXCESS_FTP:
                return FtpConnectDef.getNexcessFtpBean();
            case SCENE7_FTP:
                return FtpConnectDef.getScene7FtpBean();
            case FEED_CHANNEL_FTP:
                if (StringUtils.isEmpty(channelId)) {
                    throw new RuntimeException("channelId not found.");
                }
                return FtpConnectDef.getFeedChannelFtpBean(channelId);
            case IS:
                return FtpConnectDef.getImageServerMidFtpBean();
            default:
                throw new RuntimeException(String.format("FtpBeanEnum not found[%s].", connectEnum));
        }
    }
}
