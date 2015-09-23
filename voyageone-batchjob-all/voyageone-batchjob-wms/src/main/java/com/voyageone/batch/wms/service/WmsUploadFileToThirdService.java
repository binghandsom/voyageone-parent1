package com.voyageone.batch.wms.service;

import com.jcraft.jsch.ChannelSftp;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fred on 2015/8/11.
 * 上传文件至第三方FTP或SFTP服务器
 */
@Service
public class WmsUploadFileToThirdService extends BaseTaskService {
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "WmsUploadFileToThirdJob";
    }
    private final Log logger = LogFactory.getLog(getClass());

    // ftp上传文件路径设置
    private final String create_report_upload_file_path = "create_report_upload_file_path";
    private final String filePathWarning = "警告！没有相关文件路径配置，本任务无法进行！";
    //路径下全部文件上传
    private final String fileAll = "ALL";

    public void onStartup(List<TaskControlBean> taskControlList) throws IOException, MessagingException, InterruptedException {
        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);
        // 线程
        List<Runnable> threads = new ArrayList<>();

        // 根据订单渠道,文件上传Ftp
        for (final String orderChannelID : orderChannelIdList) {
            threads.add(new Runnable() {
                @Override
                public void run()  {
                    new uploadThirdFile(orderChannelID).doRun();
                }
            });
        }
        runWithThreadPool(threads, taskControlList);
    }


    /**
     * 根据订单渠道,文件上传Ftp
     */
    public class uploadThirdFile {
        private OrderChannelBean channel;

        public uploadThirdFile(String orderChannelId) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
        }

        public void doRun() {
            //JCFtp下载 ， 判断下载文件时间间隔的文件路径设定
            //String filePath = Properties.readValue(WmsConstants.JCFTPCheckTimePathSetting.WMS_FTP_JC_CHECK_STOCK_PATH);
            logger.info(channel.getFull_name() + " WmsUploadFileToThirdJob 开始");
            try {
                //取得相关文件路径配置
                List<ThirdPartyConfigBean> ftpFilePaths = ThirdPartyConfigs.getThirdPartyConfigList(channel.getOrder_channel_id(), create_report_upload_file_path);
                //没有相关文件路径配置，本任务无法进行
                if (ftpFilePaths == null || ftpFilePaths.size() == 0){
                    logger.info(filePathWarning);
                    logIssue(filePathWarning);
                }else{
                    for (ThirdPartyConfigBean bean : ftpFilePaths) {
                        //根据配置表生成上传文件名
                        List<String> ftpFileNames = setUploadFileName(bean);
                        //没有上传文件名
                        if (ftpFileNames == null || ftpFileNames.size() == 0){
                            //直接报错
                            logger.info(channel.getFull_name() + "上传Ftp文件发生错误：没有设置上传文件名");
                            logIssue(channel.getFull_name() + "上传Ftp文件发生错误：没有设置上传文件名");
                            break;
                        //路径下全部文件上传
                        }else if( ftpFileNames.size() == 1 &&  ftpFileNames.get(0).equals(fileAll)){
                            //取得上传路径下所有文件
                            ftpFileNames = new ArrayList<String>();
                            ftpFileNames =  FileUtils.getFileGroup(bean.getProp_val2());
                        }
                        // 上传文件名取得
                        if(ftpFileNames != null || ftpFileNames.size() > 0){
                            //判断文件是否存在
                            if (checkFileUpload(bean,ftpFileNames) == true &&  checkFileExists(ftpFileNames,bean.getProp_val2()) == true){
                                //上传文件
                                uploadFileProecss(ftpFileNames,bean.getProp_val3(),bean.getProp_val2(),bean.getProp_val6());
                            }else{
                                logger.info("文件已经上传或者文件还没有全部生成，请等待");
                            }
                        }else{
                            logIssue(channel.getFull_name() + "下载Ftp文件发生错误：没有获得上传文件名");
                        }
                    }
                }
            }catch(Exception e){
                logger.error(channel.getFull_name() + "上传Ftp文件发生错误：", e);
                logIssue(e.getMessage(),channel.getFull_name() + "上传Ftp文件发生错误");
            }
            logger.info(channel.getFull_name() +  " WmsUploadFileToThirdJob 结束");
        }

        /**
         * 根据配置表生成上传文件名
         * @param ftpFileBean    List<ThirdPartyConfigBean>
         */
        private List<String> setUploadFileName(ThirdPartyConfigBean ftpFileBean) throws Exception{
            logger.info("根据配置表生成上传文件名开始" );
            List<String> uploadFileNames = new ArrayList<String>();
            String strChange = "";
            //有上传文件名
            if (!StringUtils.isNullOrBlank2(ftpFileBean.getProp_val4())) {
                String [] fileNames= ftpFileBean.getProp_val4().split(",");
                //有转换关键字
                if (!StringUtils.isNullOrBlank2(ftpFileBean.getProp_val5())){
                    String [] fileChanges= ftpFileBean.getProp_val5().split(",");
                    //转换规则为yyyyMMdd
                    if (fileChanges[0].equals(DateTimeUtil.DATE_TIME_FORMAT_3)) {
                        strChange = DateTimeUtil.format(DateTimeUtil.addDays(DateTimeUtil.getDate(), Integer.valueOf(fileChanges[1])),DateTimeUtil.DATE_TIME_FORMAT_3);
                        for (String fileName :  fileNames) {
                            //设定转换之后上传文件名
                            String createFileName = fileName.replace(DateTimeUtil.DATE_TIME_FORMAT_3, strChange);
                            logger.info(createFileName);
                            uploadFileNames.add(createFileName);
                        }
                    }
                //没有转换关键字
                }else{
                    for (String fileName :  fileNames) {
                        //设定上传文件名
                        uploadFileNames.add(fileName);
                    }
                }
            }
            logger.info("根据配置表生成上传文件名结束" );
            return uploadFileNames;
        }


        /**
         * 根据配置表判断文件能否上传
         * @param ftpFileBean    List<ThirdPartyConfigBean>
         * @param ftpFileNames   上传文件名
         */
        private boolean checkFileUpload(ThirdPartyConfigBean ftpFileBean,List<String> ftpFileNames) throws Exception{
            List<String> createFileNames = new ArrayList<String>();
            //有判断用文件路径
            if (!StringUtils.isNullOrBlank2(ftpFileBean.getProp_val1())) {
                String [] checkFilePaths= ftpFileBean.getProp_val1().split(",");
                for (String filePath :  checkFilePaths) {
                    //读取判断文件内容
                    createFileNames = readFile(filePath, createFileNames);
                }
                //判断文件无内容，或者文件不存在，直接返回false，不做上传处理
                if (createFileNames == null || createFileNames.size() == 0){
                    return false;
                }else{
                    //判断文件内容（已生成文件）和需要上传的文件是否一致
                    int checkFlg = 0;
                    for (String createFileName : createFileNames){
                        for (String ftpFileName : ftpFileNames) {
                            //文件名一致
                            if (createFileName.equals(ftpFileName.substring(0,ftpFileName.lastIndexOf("."))))
                            {
                                checkFlg = checkFlg + 1;
                                break;
                            }
                        }
                    }
                    //全部文件已经准备完毕
                    if (checkFlg == createFileNames.size()){
                        return true;
                    }
                }
            //没有判断用文件，直接返回false，不做上传处理
            }else{
                return false;
            }
            return false;
        }

        /**
         * 读取判断今日生成文件的文件
         * @param filePath           文件名
         * @param createFileNames   判断文件内容（已生成文件名）
         */
        private List<String> readFile(String filePath,List<String> createFileNames) throws Exception{
            //List<String> returnLineTxtList = new ArrayList<String>();
            logger.info(filePath + " 读取文件开始" );

            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                String encode= CommonUtil.getCharset(filePath);
                try (InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encode);
                     BufferedReader bufferedReader = new BufferedReader(read);){
                    String lineTxt = null;
                    while((lineTxt = bufferedReader.readLine()) != null){
                        logger.info(lineTxt);
                        createFileNames.add(lineTxt);
                    }
                }
            }else{
                logger.info("找不到指定的文件");
            }
            logger.info(filePath + " 读取文件结束" );
            return createFileNames;
        }


        /**
         * 判断文件是否存在
         * @param ftpFileNames    List<String>文件名组
         * @param localFilePath  本地文件生成路径
         *
         */
        private boolean checkFileExists(List<String> ftpFileNames,String localFilePath) throws Exception {
            boolean isSuccess = true;
            for (String fileName : ftpFileNames) {
                File oldfile = new File(localFilePath + "/" + fileName);
                if (!oldfile.exists()) {
                    isSuccess = false;
                    break;
                }
            }
            return isSuccess;
        }


        /**
         * 上传文件
         * @param ftpFileNames    List<String>文件名组
         * @param ftpFilePath    第三方文件上传路径
         * @param localFilePath  本地文件生成路径
         * @param backFilePath   本地文件备份路径
         *
         */
        private void uploadFileProecss(List<String> ftpFileNames,String ftpFilePath,String localFilePath,String backFilePath) throws Exception {
            boolean isSuccess = false;
            String type = ThirdPartyConfigs.getVal1(channel.getOrder_channel_id(), WmsConstants.ftpValues.FTP_TYPE);

            if ("ftp".equals(type)) {
                //下载Ftp文件
                isSuccess = uploadFileForFtp(ftpFileNames,ftpFilePath,localFilePath);
            } else if ("sftp".equals(type)) {
                //下载Ftp文件
                isSuccess = uploadFileForSFtp(ftpFileNames,ftpFilePath,localFilePath);
            }
            //上传成功，备份文件
            if (isSuccess == true){
                //移动文件到指定目录
                moveFile(localFilePath, ftpFileNames,backFilePath);
            //上传不成功，所有已上传文件删除
            }else{
                if ("ftp".equals(type)) {
                    //文件上传出错(删除已上传文件)
                    delFileForFtp(ftpFilePath, ftpFileNames);
                } else if ("sftp".equals(type)) {
                    //文件上传出错(删除已上传文件)
                    delFileForSFtp(ftpFilePath,ftpFileNames);
                }
            }
            //return isSuccess;
        }

        /**
         * 下载第三方文件
         * @param orderChannelID    String
         */
        private FtpBean formatFtpBean(String orderChannelID){
            FtpBean ftpBean = new FtpBean();
            // ftp连接port
            ftpBean.setPort(ThirdPartyConfigs.getVal1(orderChannelID,WmsConstants.ftpValues.FTP_PORT));
            // ftp连接url
            ftpBean.setUrl(ThirdPartyConfigs.getVal1(orderChannelID, WmsConstants.ftpValues.FTP_URL));
            // ftp连接usernmae
            ftpBean.setUsername(ThirdPartyConfigs.getVal1(orderChannelID, WmsConstants.ftpValues.FTP_USERNAME));
            // ftp连接password
            ftpBean.setPassword(ThirdPartyConfigs.getVal1(orderChannelID, WmsConstants.ftpValues.FTP_PASSWORD));
            ftpBean.setFile_coding("utf-8");
            return ftpBean;
        }


        /**
         * 上传ftp文件
         * @param ftpFileNames     List<String>文件名组
         * @param ftpFilePath     第三方文件上传路径
         * @param localFilePath   本地文件生成路径
         *
         */
        private boolean uploadFileForFtp(List<String> ftpFileNames,String ftpFilePath,String localFilePath) throws Exception{
            logger.info("向Ftp服务器上传文件开始" );
            boolean isSuccess = false;
            // FtpBean初期化
            FtpBean ftpBean;
            ftpBean = formatFtpBean(channel.getOrder_channel_id());
            //ftpBean.setPassword("ss");
            FtpUtil ftpUtil = new FtpUtil();
            FTPClient ftpClient = new FTPClient();
            try {
                //建立连接
                ftpClient = ftpUtil.linkFtp(ftpBean);
                if (ftpClient != null) {
                    //本地文件路径设定
                    ftpBean.setUpload_localpath(localFilePath);
                    //FTP服务器保存目录设定
                    ftpBean.setUpload_path(ftpFilePath);
                    for (String fileName : ftpFileNames) {
                        //上传文件名设定
                        ftpBean.setUpload_filename(fileName);
                        //文件流取得
                        //ftpBean.setUpload_input(new FileInputStream(ftpBean.getUpload_localpath() + "/" + ftpBean.getUpload_filename()));
                        //上传文件
                        isSuccess = ftpUtil.uploadFile(ftpBean,ftpClient);
                        //上传出错不再上传，直接返回
                        if (isSuccess == false){
                            break;
                        }
                    }
                }else{
                    isSuccess = false;
                }
            } catch (Exception e) {
                isSuccess = false;
                logger.error(channel.getFull_name() + "向Ftp服务器上传文件发生错误：", e);
                logIssue(e.getMessage(),channel.getFull_name() + "向Ftp服务器上传文件发生错误");
            }finally{
                //断开连接
                if (ftpClient != null) {
                    ftpUtil.disconnectFtp(ftpClient);
                }
            }
            logger.info("向Ftp服务器上传文件结束" );
            return isSuccess;
        }

        /**
         * 上传Sftp文件
         * @param ftpFileNames     List<String>文件名组
         * @param ftpFilePath     第三方文件上传路径
         * @param localFilePath   本地文件生成路径
         *
         */
        private boolean uploadFileForSFtp(List<String> ftpFileNames,String ftpFilePath,String localFilePath) {
            logger.info("向SFtp服务器上传文件开始" );
            boolean isSuccess = true;

            // FtpBean初期化
            FtpBean ftpBean;
            ftpBean = formatFtpBean(channel.getOrder_channel_id());
            ChannelSftp ftpClient = new ChannelSftp();
            SFtpUtil sftpUtil = new SFtpUtil();
            try {
                //ftpBean.setPassword("ss");
                //建立连接
                ftpClient = sftpUtil.linkFtp(ftpBean);

                //本地文件路径设定
                ftpBean.setUpload_localpath(localFilePath);
                //FTP服务器保存目录设定
                ftpBean.setUpload_path(ftpFilePath);
                if (ftpClient != null) {
                    sftpUtil.createDirForSftp(ftpBean.getUpload_path(), ftpClient);
                    for (String fileName : ftpFileNames) {
                        //上传文件名设定
                        ftpBean.setUpload_filename(fileName);
                        isSuccess = sftpUtil.uploadFileForSFTP(ftpBean, ftpClient);
                        //上传出错不再上传，直接返回
                        if (isSuccess == false) {
                            break;
                        }
                    }
                }

            } catch (Exception e) {
                isSuccess = false;
                logger.error(channel.getFull_name() + "向SFtp服务器上传文件发生错误：", e);
                logIssue( e.getMessage(),channel.getFull_name() + "向SFtp服务器上传文件发生错误");

            } finally {
                try {
                    //断开连接
                    sftpUtil.disconnectFtp(ftpClient);
                } catch (Exception e) {
                    isSuccess = false;
                    logger.error(channel.getFull_name() + "SFtp服务器断开连接发生错误：", e);
                    logIssue( e.getMessage(),channel.getFull_name() + "SFtp服务器断开连接发生错误");
                }
            }
            logger.info("向SFtp服务器上传文件结束" );
            return isSuccess;
        }



        /**
         * 移动文件到指定目录
         * @param srcPath 源文件位置
         * @param ftpFileNames 源文件名称
         * @param bakFilePath 目标文件路径
         */
        private void moveFile(String srcPath, List<String> ftpFileNames, String bakFilePath) {
            logger.info("备份文件开始" );
            //移动文件进入备份文件夹
            //Object[] srcFileGroup = FileUtils.getFileGroup(srcFileName, srcPath).toArray();
            for (String ftpFileName: ftpFileNames) {
                String bakFileName = ftpFileName.substring(0, ftpFileName.lastIndexOf(".")) + "_" + DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_2) + ftpFileName.substring(ftpFileName.lastIndexOf("."));
                logger.info("备份文件  " + srcPath + "/" + ftpFileName + "  至  " +  bakFilePath + "/" + bakFileName);
                FileUtils.copyFile(srcPath + "/" + ftpFileName, bakFilePath + "/" + bakFileName);
                logger.info("删除文件  " + srcPath + "/" + ftpFileName);
                FileUtils.delFile(srcPath + "/" + ftpFileName);
            }
            logger.info("备份文件结束" );
        }

        /**
         * 文件上传出错(删除已上传文件)
         * @param ftpFilePath    ftpFilePath
         * @param ftpFileNames 源文件名称
         */
        private void delFileForSFtp(String ftpFilePath,List<String>ftpFileNames) throws Exception {
            logger.info("向SFtp服务器删除已上传文件开始" );
            boolean isSuccess = false;

            // FtpBean初期化
            FtpBean ftpBean;
            ftpBean = formatFtpBean(channel.getOrder_channel_id());
            ChannelSftp ftpClient = new ChannelSftp();
            SFtpUtil sftpUtil = new SFtpUtil();
            try {
                //建立连接
                ftpClient = sftpUtil.linkFtp(ftpBean);

                if (ftpClient != null) {
                    //FTP服务器保存目录设定
                    ftpBean.setDown_remotepath(ftpFilePath);
                    for (String fileName : ftpFileNames) {
                        try {
                            sftpUtil.delOneFile(ftpBean, ftpClient, fileName);
                        }catch (Exception e){
                            logger.error(channel.getFull_name() + "SFtpUtil.delOneFile 出错", e);
                        }
                    }
                }
            }catch (Exception e){
                logger.error(channel.getFull_name() + "SFtpUtil.delOneFile 出错", e);
                logIssue(e.getMessage(),"SFtpUtil.delOneFile 出错");
            }finally {
                //断开连接
                sftpUtil.disconnectFtp(ftpClient);
            }

            logger.info("向SFtp服务器删除已上传文件结束");


        }


        /**
         * 文件上传出错(删除已上传文件)
         * @param ftpFilePath    ftpFilePath
         * @param ftpFileNames 源文件名称
         *
         */
        private void delFileForFtp(String ftpFilePath,List<String>ftpFileNames) throws Exception{
            logger.info("向Ftp服务器删除已上传文件开始" );
            // FtpBean初期化
            FtpBean ftpBean;
            ftpBean = formatFtpBean(channel.getOrder_channel_id());
            FtpUtil ftpUtil = new FtpUtil();
            FTPClient ftpClient = new FTPClient();
            try {
                //建立连接
                ftpClient = ftpUtil.linkFtp(ftpBean);
                if (ftpClient != null) {
                    //FTP服务器保存目录设定
                    ftpBean.setDown_remotepath(ftpFilePath);
                    for (String fileName : ftpFileNames) {
                        ftpUtil.delOneFile(ftpBean, ftpClient, fileName);
                    }
                }
            }catch (Exception e){
                logIssue(e.getMessage(),"FtpUtil.delOneFile 出错");
            }finally{
                //断开连接
                ftpUtil.disconnectFtp(ftpClient);
            }
            logger.info("向Ftp服务器删除已上传文件结束" );
        }

    }
}
