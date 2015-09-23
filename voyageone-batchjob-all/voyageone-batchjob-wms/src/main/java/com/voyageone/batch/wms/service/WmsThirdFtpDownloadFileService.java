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
 * Created by fred on 2015/6/19.
 */
@Service
public class WmsThirdFtpDownloadFileService extends BaseTaskService {

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "WmsThirdFtpDownloadFileJob";
    }
    private final Log logger = LogFactory.getLog(getClass());
    private final String fileDownTimeWarning = "警告！超过规定天数没有下载规定的库存文件";
    private final String filePathWarning = "警告！没有相关文件路径配置，本任务无法进行！";
    //ftp连接URL
    private final String ftp_url = WmsConstants.ftpValues.FTP_URL;
    // ftp连接port
    private final String ftp_port = WmsConstants.ftpValues.FTP_PORT;
    // ftp连接usernmae
    private final String ftp_usernmae = WmsConstants.ftpValues.FTP_USERNAME;
    // ftp连接password
    private final String ftp_password = WmsConstants.ftpValues.FTP_PASSWORD;
    // ftp连接password
    private final String ftp_max_next_time = WmsConstants.ftpValues.FTP_MAX_NEXT_TIME;
    // ftp下载文件路径设置
    private final String download_file_path = WmsConstants.ftpValues.FTP_DOWNLOAD_FILE_PATH;
    //ftp连接类型
    private final String ftp_type = WmsConstants.ftpValues.FTP_TYPE;
    private final String all_file_check = "ALL";

    public void onStartup(List<TaskControlBean> taskControlList) throws IOException, MessagingException, InterruptedException {
        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList,TaskControlEnums.Name.order_channel_id);
        // 线程
        List<Runnable> threads = new ArrayList<>();

        // 根据订单渠道、店铺运行Ftp文件下载
        for (final String orderChannelID : orderChannelIdList) {
            threads.add(new Runnable() {
                @Override
                public void run()  {
                    new downloadThirdFile(orderChannelID).doRun();
                }
            });
        }
        runWithThreadPool(threads, taskControlList);
    }

    /**
     * 按渠道下载第三方文件
     */
    public class downloadThirdFile {
        private OrderChannelBean channel;

        public downloadThirdFile(String orderChannelId) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
        }

        public void doRun() {
            //JCFtp下载 ， 判断下载文件时间间隔的文件路径设定
            //String filePath = Properties.readValue(WmsConstants.JCFTPCheckTimePathSetting.WMS_FTP_JC_CHECK_STOCK_PATH);
            logger.info(channel.getFull_name() + " WmsThirdFtpDownloadFileJob 开始");
            try {
                //取得相关文件路径配置
                List<ThirdPartyConfigBean> ftpFilePaths = ThirdPartyConfigs.getThirdPartyConfigList(channel.getOrder_channel_id(), download_file_path);
                //没有相关文件路径配置，本任务无法进行
                if (ftpFilePaths == null || ftpFilePaths.size() == 0){
                    logger.info(filePathWarning);
                    logIssue(filePathWarning);
                }else{
                    //判断下载ftp文件的时间间隔
                    checkFileDownTime(ftpFilePaths);
                    String type = ThirdPartyConfigs.getVal1(channel.getOrder_channel_id(), ftp_type);
                    if ("ftp".equals(type)) {
                        //下载Ftp文件
                        downloadFileForFtp(ftpFilePaths);
                    }
                    else if ("sftp".equals(type)) {
                        //下载Ftp文件
                        downloadFileForSFtp(ftpFilePaths);
                    }

                }
            }catch(Exception e){
                logger.error(channel.getFull_name() + "下载Ftp文件发生错误：", e);
                logIssue(channel.getFull_name() + "下载Ftp文件发生错误："+ e);
            }
            logger.info(channel.getFull_name() +  " WmsThirdFtpDownloadFileJob 结束");
        }

         /**
         * 判断下载ftp文件的时间间隔
         * @param ftpFilePaths    List<ThirdPartyConfigBean>
         */
        private void checkFileDownTime(List<ThirdPartyConfigBean> ftpFilePaths){
            for (ThirdPartyConfigBean bean : ftpFilePaths) {
                //有判断用文件路径
                if (!StringUtils.isNullOrBlank2(bean.getProp_val4())) {
                    //取得上次下载文件时间
                    String lastTime = readFile(bean.getProp_val4());
                    //取得警告时间
                    if (!StringUtils.isNullOrBlank2(lastTime)) {
                        String maxNextTime = ThirdPartyConfigs.getVal1(channel.getOrder_channel_id(), ftp_max_next_time);
                        //上次下载文件时间距本次下载任务超过规定时间
                        if (DateTimeUtil.diffDays(DateTimeUtil.getDate(), DateTimeUtil.parse(lastTime, DateTimeUtil.DEFAULT_DATETIME_FORMAT)) > Integer.parseInt(maxNextTime)) {
                            logger.info(fileDownTimeWarning);
                            logIssue(fileDownTimeWarning);
                        }
                    }
                }
            }
        }

        /**
         * 下载ftp文件
         * @param ftpFilePaths    List<ThirdPartyConfigBean>
         */
        private void downloadFileForFtp(List<ThirdPartyConfigBean> ftpFilePaths) throws Exception{

            // FtpBean初期化
            FtpBean ftpBean;
            ftpBean = formatFtpBean(channel.getOrder_channel_id());
            FtpUtil ftpUtil = new FtpUtil();
            FTPClient ftpClient = new FTPClient();
            List<String> fileNames;
            try {
                //建立连接
                ftpClient = ftpUtil.linkFtp(ftpBean);
                if (ftpClient != null) {
                    for (ThirdPartyConfigBean bean : ftpFilePaths) {
                        //下载Ftp文件
                        //本地文件路径设定
                        ftpBean.setDown_localpath(bean.getProp_val2());
                        //Ftp源文件路径设定
                        ftpBean.setDown_remotepath(bean.getProp_val1());
                        //远程服务器备份路径设定
                        ftpBean.setRemote_bak_path(bean.getProp_val6());
                        //下载全部文件
                        if (bean.getProp_val3().equals(all_file_check)){
                            //Ftp源文件名设定（下载目录夹下所有文件）
                            ftpBean.setDown_filename("");
                            fileNames = ftpUtil.getFolderFileNames(ftpBean, ftpClient, bean.getProp_val1());
                            //下载文件
                            if (downloadFtpFiles(ftpBean, ftpClient, ftpUtil, fileNames)) {
                                //将FTP源文件删除
                                for (String fileName : fileNames) {
                                    if(!StringUtils.isEmpty(ftpBean.getRemote_bak_path())) {
                                        ftpUtil.removeOneFile(ftpBean, ftpClient, fileName);
                                    }else{
                                        doFileProcessing(ftpBean, ftpClient, ftpUtil, fileName, bean.getProp_val4());
                                    }
                                }
                            }
                        }else {
                            //Ftp源文件名设定
                            ftpBean.setDown_filename(StringUtils.null2Space(bean.getProp_val5()));
                            //下载文件
                            if (downloadFtpFile(ftpBean, ftpClient, ftpUtil)) {
                                if(!StringUtils.isEmpty(ftpBean.getRemote_bak_path())) {
                                    ftpUtil.removeOneFile(ftpBean, ftpClient, ftpBean.getDown_filename());
                                }else{
                                    doFileProcessing(ftpBean, ftpClient, ftpUtil, ftpBean.getDown_filename(), bean.getProp_val4());
                                }

                            }
                        }
                    }
                }
            }finally{
                //断开连接
                ftpUtil.disconnectFtp(ftpClient);
            }
        }

        /**
         * 下载第三方文件
         * @param orderChannelID    String
         */
        private FtpBean formatFtpBean(String orderChannelID){

            String url = "";
            // ftp连接port
            String port = "";
            // ftp连接usernmae
            String username = "";
            // ftp连接password
            String password = "";

            url = ThirdPartyConfigs.getVal1(orderChannelID, ftp_url);
            // ftp连接port
            port = ThirdPartyConfigs.getVal1(orderChannelID, ftp_port);
            // ftp连接usernmae
            username = ThirdPartyConfigs.getVal1(orderChannelID, ftp_usernmae);
            // ftp连接password
            password = ThirdPartyConfigs.getVal1(orderChannelID, ftp_password);

            FtpBean ftpBean = new FtpBean();
            ftpBean.setPort(port);
            ftpBean.setUrl(url);
            ftpBean.setUsername(username);
            ftpBean.setPassword(password);
            ftpBean.setFile_coding("iso-8859-1");
            return ftpBean;
        }

        /**
         * 下载FTP文件
         * @param ftpBean           FtpBean
         * @param ftpClient         FTPClient
         * @param ftpUtil           FtpUtil
         */
        private boolean downloadFtpFile(FtpBean ftpBean,FTPClient ftpClient,FtpUtil ftpUtil)  {
            String errInfo = "";
            String filePathName = "";
            try {

                filePathName = ftpBean.getDown_localpath() + "/" + ftpBean.getDown_filename();
                int result = ftpUtil.downFile(ftpBean,ftpClient);
                if(result != 2){
                    if (result == 0){
                        delLocalFile(filePathName);
                        errInfo =  "FTP server refused connection.";
                        logger.info(errInfo);
                    }else {
                        errInfo = ftpBean.getDown_remotepath() + "/" + ftpBean.getDown_filename() +  "文件不存在.";
                    }
                    logIssue(errInfo);
                    return false;
                }
            }catch (Exception e){
                logger.info(ftpBean.getDown_filename()  + " Ftp 下载文件失败");
                delLocalFile(filePathName);
                errInfo = e.getMessage();
                logIssue(errInfo,"FtpUtil.downFile 出错");
                return false;
            }
            return true;
        }


        /**
         * 下载FTP同一目录夹下多个文件
         * @param ftpBean           FtpBean
         * @param ftpClient         FTPClient
         * @param ftpUtil           FtpUtil
         */
        private boolean downloadFtpFiles(FtpBean ftpBean, FTPClient ftpClient, FtpUtil ftpUtil, List<String> fileNames)  {
            String errInfo;
            String filePathName = "";
            try {
                if (fileNames.size() == 0) {
                    logger.info(ftpBean.getDown_remotepath()  + " 目标目录夹下文件不存在");
                    return true;
                }
                int result = ftpUtil.downFiles(ftpBean, ftpClient, fileNames);

                if(result != 2){
                    if (result == 0){
                        delLocalFile(filePathName);
                        errInfo =  "FTP server refused connection.";
                        logger.info(errInfo);
                    }else {
                        errInfo = fileNames +  "文件不存在.";
                    }
                    logIssue(errInfo);
                    return false;
                }
            }catch (Exception e){
                logger.info(ftpBean.getDown_remotepath()  + " Ftp 下载文件失败");
                delLocalFile(filePathName);
                errInfo = e.getMessage();
                logIssue(errInfo,"FtpUtil.downFiles 出错");
                return false;
            }
            return true;
        }

        /**
         * 删除b本地文件
         * @param filePathName           本地JC文件
         */
        private void delLocalFile(String filePathName)  {
            try {
                //本地文件删除
                delFile(filePathName);
            }catch (Exception e){
                logIssue(e.getMessage(), filePathName + "   delLocalFile 出错");
            }
        }

        /**
         * 删除第三方文件
         * @param filePath           文件名
         */
        private void delFile(String filePath){
            File file = new File(filePath);
            logger.info( filePath + "删除文件开始" );
            try {
                //file = null;
                if (file.exists()) {
                    boolean isSuccess = file.delete();
                    if (isSuccess) {
                        logger.info(filePath + "删除成功！");
                    } else {
                        logger.info(filePath + "删除失败！");
                    }
                }
            }catch (Exception e){
                logger.info("删除文件出错");
                logIssue(filePath + "删除文件出错","delFile");
            }
            logger.info( filePath + "删除文件结束" );
        }

        /**
         * 下载FTP文件后的处理(删除Ftp源文件，重新写入判断下载文件时间间隔的文件)
         * @param ftpBean           FtpBean
         * @param ftpClient         FTPClient
         * @param ftpUtil           FtpUtil
         * @param fileName          fileName
         * @param checkFileName    checkFileName
         */
        private void doFileProcessing(FtpBean ftpBean,FTPClient ftpClient,FtpUtil ftpUtil,String fileName,String checkFileName) {
            String errInfo = "";
            try {
                ftpUtil.delOneFile(ftpBean, ftpClient, fileName);
            }catch (Exception e){
                errInfo = e.getMessage();
                logIssue(errInfo,"FtpUtil.delOneFile 出错");
            }
            //有判断下载文件时间间隔用文件路径
            if (!StringUtils.isNullOrBlank2(checkFileName)) {
                //Ftp下载完毕 ， 判断下载文件时间间隔的文件删除
                delFile(checkFileName);
                //Ftp下载 ， 判断下载文件时间间隔的文件新建
                newFile(checkFileName, DateTimeUtil.getNow());
            }
        }


        /**
         * 读取判断下载文件时间间隔的文件
         * @param filePath           文件名
         */
        private String readFile(String filePath) {
            String returnLineTxt = "";
            logger.info(filePath + " 读取文件开始" );
            try {
                File file=new File(filePath);
                if(file.isFile() && file.exists()){ //判断文件是否存在
                    String encode= CommonUtil.getCharset(filePath);
    //                InputStreamReader read = new InputStreamReader(
    //                        new FileInputStream(file),"UTF-8");//考虑到编码格式
                    InputStreamReader read = new InputStreamReader(
                            new FileInputStream(file),encode);//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    while((lineTxt = bufferedReader.readLine()) != null){
                        logger.info(lineTxt);
                        returnLineTxt = lineTxt;
                        break;
                    }
                    read.close();
                }else{
                    logger.info("找不到指定的文件");
                }
            } catch (Exception e) {
                logger.info("读取文件内容出错");
                logIssue(filePath + "读取文件内容出错","readFile");
            }

            logger.info(filePath + " 读取文件结束" );
            return returnLineTxt;
        }

        /**
         * 新建文件
         * @param filePathAndName String 文件路径及名称 如c:/fqf.txt
         * @param fileContent String 文件内容
         * @return boolean
         */
        private  void newFile(String filePathAndName, String fileContent) {

            logger.info(filePathAndName + " 写入文件开始" );
            String filePath = filePathAndName;
            try {
                //filePath = filePath.toString();
                File myFilePath = new File(filePath);
                if (!myFilePath.exists()) {
                    myFilePath.createNewFile();
                }
                FileWriter resultFile = new FileWriter(myFilePath);
                PrintWriter myFile = new PrintWriter(resultFile);
                String strContent = fileContent;
                myFile.println(strContent);
                resultFile.close();
            } catch (Exception e) {
                logger.info("写入文件内容出错");
                logIssue(filePath + "写入文件内容出错","newFile");
            }
            logger.info(filePathAndName + " 写入文件结束" );
        }

        /**
         * 下载sftp文件
         * @param ftpFilePaths    List<ThirdPartyConfigBean>
         */
        private void downloadFileForSFtp(List<ThirdPartyConfigBean> ftpFilePaths) throws Exception{

            // FtpBean初期化
            FtpBean ftpBean = new FtpBean();
            ftpBean = formatFtpBean(channel.getOrder_channel_id());
            SFtpUtil ftpUtil = new SFtpUtil();
            ChannelSftp ftpClient = new ChannelSftp();
            List<String> fileNames = new ArrayList<String>();
            try {
                //建立连接
                ftpClient = ftpUtil.linkFtp(ftpBean);
                if (ftpClient != null) {
                    for (ThirdPartyConfigBean bean : ftpFilePaths) {
                        //下载Ftp文件
                        //本地文件路径设定
                        ftpBean.setDown_localpath(bean.getProp_val2());
                        //Ftp源文件路径设定
                        ftpBean.setDown_remotepath(bean.getProp_val1());
                        //远程服务器备份路径设定
                        ftpBean.setRemote_bak_path(bean.getProp_val6());
                        //下载全部文件
                        if (bean.getProp_val3().equals(all_file_check)){
                            //Ftp源文件名设定（下载目录夹下所有文件）
                            ftpBean.setDown_filename("");
                            fileNames = ftpUtil.getFolderFileNames(ftpBean, ftpClient, bean.getProp_val1());
                            //下载文件
                            if (downloadFtpFiles(ftpBean, ftpClient, ftpUtil, fileNames) == true) {
                                //将FTP源文件删除
                                for (String fileName : fileNames) {
                                    if(!StringUtils.isEmpty(ftpBean.getRemote_bak_path())) {
                                        ftpUtil.removeOneFile(ftpBean, ftpClient, fileName);
                                    }else{
                                        doFileProcessing(ftpBean, ftpClient, ftpUtil, fileName, bean.getProp_val4());
                                    }
                                }
                            }
                        }else {
                            //Ftp源文件名设定
                            ftpBean.setDown_filename(StringUtils.null2Space(bean.getProp_val5()));
                            //下载文件
                            if (downloadFtpFile(ftpBean, ftpClient, ftpUtil) == true) {
                                if(!StringUtils.isEmpty(ftpBean.getRemote_bak_path())) {
                                    ftpUtil.removeOneFile(ftpBean, ftpClient, ftpBean.getDown_filename());
                                }else{
                                    doFileProcessing(ftpBean, ftpClient, ftpUtil, ftpBean.getDown_filename(), bean.getProp_val4());
                                }

                            }
                        }
                    }
                }
            }finally{
                //断开连接
                ftpUtil.disconnectFtp(ftpClient);
            }
        }

        /**
         * 下载FTP同一目录夹下多个文件
         * @param ftpBean           FtpBean
         * @param ftpClient         FTPClient
         * @param ftpUtil           FtpUtil
         */
        private boolean downloadFtpFiles(FtpBean ftpBean,ChannelSftp ftpClient,SFtpUtil ftpUtil,List<String> fileNames)  {
            String errInfo = "";
            String filePathName = "";
            try {
                if (fileNames.size() == 0) {
                    logger.info(ftpBean.getDown_remotepath()  + " 目标目录夹下文件不存在");
                    return true;
                }
                int result = ftpUtil.downFiles(ftpBean, ftpClient, fileNames);

                if(result != 2){
                    if (result == 0){
                        delLocalFile(filePathName);
                        errInfo = fileNames +  "文件不存在.";
                        logger.info(errInfo);
                    }else {
                        errInfo = fileNames +  "文件不存在.";
                    }
                    logIssue(errInfo);
                    return false;
                }
            }catch (Exception e){
                logger.info(e);
                delLocalFile(filePathName);
                logIssue("FtpUtil.downFiles 出错: " + e);
                return false;
            }
            return true;
        }

        /**
         * 下载FTP文件
         * @param ftpBean           FtpBean
         * @param ftpClient         FTPClient
         * @param ftpUtil           FtpUtil
         */
        private boolean downloadFtpFile(FtpBean ftpBean,ChannelSftp ftpClient,SFtpUtil ftpUtil)  {
            String errInfo = "";
            String filePathName = "";
            try {

                filePathName = ftpBean.getDown_localpath() + "/" + ftpBean.getDown_filename();
                //ftpUtil = null;
                int result = ftpUtil.downFile(ftpBean,ftpClient);

                if(result != 2){
                    if (result == 0){
                        delLocalFile(filePathName);
                        errInfo = ftpBean.getDown_remotepath() + "/" + ftpBean.getDown_filename() +  "文件不存在.";
                        logger.info(errInfo);
                    }else {
                        errInfo = ftpBean.getDown_remotepath() + "/" + ftpBean.getDown_filename() +  "文件不存在.";
                    }
                    logIssue(errInfo);
                    return false;
                }
            }catch (Exception e){
                logger.info(ftpBean.getDown_filename()  + " Ftp 下载文件失败");
                delLocalFile(filePathName);
                logIssue(ftpBean.getDown_filename() + "下载失败！: " + e);
                return false;
            }
            return true;
        }

        /**
         * 下载FTP文件后的处理(删除Ftp源文件，重新写入判断下载文件时间间隔的文件)
         * @param ftpBean           FtpBean
         * @param ftpClient         FTPClient
         * @param ftpUtil           FtpUtil
         * @param fileName          fileName
         * @param checkFileName    checkFileName
         */
        private void doFileProcessing(FtpBean ftpBean,ChannelSftp ftpClient,SFtpUtil ftpUtil,String fileName,String checkFileName) {
            String errInfo = "";
            try {
                ftpUtil.delOneFile(ftpBean, ftpClient, fileName);
            }catch (Exception e){
                errInfo = fileName + e.getMessage();
                logIssue(errInfo,"FtpUtil.delOneFile 出错");
            }
            //有判断下载文件时间间隔用文件路径
            if (!StringUtils.isNullOrBlank2(checkFileName)) {
                //Ftp下载完毕 ， 判断下载文件时间间隔的文件删除
                delFile(checkFileName);
                //Ftp下载 ， 判断下载文件时间间隔的文件新建
                newFile(checkFileName, DateTimeUtil.getNow());
            }
        }
    }

}
