package com.voyageone.batch.wms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.dao.TransferDao;
import com.voyageone.batch.wms.modelbean.ReceiptConfirmationBean;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.FileWriteUtils;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by jacky on 2015/10/22.
 */
@Service
public class WmsReceiptConfirmationService extends BaseTaskService {

    @Autowired
    TransferDao transferDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "WmsReceiptConfirmationJob";
    }

    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);
        // 线程
        List<Runnable> threads = new ArrayList<>();

        // 根据渠道,生成 Receipt Confirmation 文件并上传该渠道第三方 Ftp
        for (final String orderChannelID : orderChannelIdList) {
            threads.add(new Runnable() {
                @Override
                public void run()  {
                    new ReceiptConfirmation(orderChannelID).doRun();
                }
            });
        }
        runWithThreadPool(threads, taskControlList);
    }

    /**
     * 根据订单渠道,生成Receipt Confirmation文件
     */
    class ReceiptConfirmation {
        private OrderChannelBean channel;

        public ReceiptConfirmation(String orderChannelId) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
        }

        public void doRun() {
            $info(channel.getFull_name() + " ReceiptConfirmation 开始");

            //***** 获得生成Receipt Confirmation文件所需要的数据
            Map<String, List<ReceiptConfirmationBean>> receiptConfirmationByBrandMap = getReceiptConfirmation(channel);
            if (receiptConfirmationByBrandMap == null || receiptConfirmationByBrandMap.size() <= 0) {
                $info("没有要生成Receipt Confirmation文件所需要的数据");
            } else {
                //***** 生成文件
                List<Map<String, String>> successConfirmList = new ArrayList<Map<String, String>>();
                createReceiptConfirmFile(receiptConfirmationByBrandMap, channel.getOrder_channel_id(), successConfirmList);

                //***** 置位
                if (successConfirmList != null && successConfirmList.size() > 0) {
                    for (int i = 0; i < successConfirmList.size(); i++) {
                        resetConfrimFlag(successConfirmList.get(i), channel.getOrder_channel_id());
                    }
                }
            }

            $info(channel.getFull_name() + " ReceiptConfirmation 结束");
        }

        /**
         *
         * @param channel
         * @return
         */
        private Map<String, List<ReceiptConfirmationBean>> getReceiptConfirmation(OrderChannelBean channel) {
            String channelId = channel.getOrder_channel_id();
            List<ReceiptConfirmationBean> receiptConfirmList = transferDao.getReceiptConfirmation(channelId);

            Map<String, List<ReceiptConfirmationBean>> brandMap = null;
            if (receiptConfirmList != null && receiptConfirmList.size() > 0) {
                brandMap = new HashMap<String, List<ReceiptConfirmationBean>>();
                Map<String, Integer> brandLineNumMap = new HashMap<String, Integer>();
                List<String> brandList = new ArrayList<String>();
                for (ReceiptConfirmationBean bean : receiptConfirmList) {
                    String brand = bean.getBrand();
                    if (StringUtils.isNullOrBlank2(brand)) {
                        continue;
                    }

                    String timeZone = ChannelConfigs.getVal1(channelId, ChannelConfigEnums.Name.channel_time_zone);
                    String receiptDate = DateTimeUtil.getLocalTime(bean.getReceiptDate(), Integer.valueOf(timeZone));
                    receiptDate = receiptDate.substring(0, 10);
                    bean.setReceiptDate(receiptDate);

                    if (brandList.contains(brand)) {
                        int lineNumber = brandLineNumMap.get(brand) + 1;
                        bean.setLineNumber(lineNumber);
                        brandMap.get(brand).add(bean);
                        brandLineNumMap.put(brand, lineNumber);
                    } else {
                        List<ReceiptConfirmationBean> brandReceiptBeanList = new ArrayList<ReceiptConfirmationBean>();
                        bean.setLineNumber(1);
                        brandReceiptBeanList.add(bean);

                        brandMap.put(brand, brandReceiptBeanList);
                        brandLineNumMap.put(brand, 1);
                        brandList.add(brand);
                    }
                }
            }

            return brandMap;
        }

        /**
         *
         * @param receiptConfirmationByBrandMap
         * @param channelId
         * @return
         */
        private boolean createReceiptConfirmFile(Map<String, List<ReceiptConfirmationBean>> receiptConfirmationByBrandMap, String channelId, List<Map<String, String>> successConfirmList) {
            boolean isSuccess = true;

            ThirdPartyConfigBean thirdPartyConfigBean = ThirdPartyConfigs.getThirdPartyConfig(channelId, "wms_upload_receipts_file_path");
            // 本地文件生成备份路径
            String backupLocalPath = thirdPartyConfigBean.getProp_val3();
            // 第三方文件需求路径
            String uploadLocalPath = thirdPartyConfigBean.getProp_val1();

            if (StringUtils.isNullOrBlank2(backupLocalPath) || StringUtils.isNullOrBlank2(uploadLocalPath)) {
                logIssue("Receipt Confirmation's backupLocalPath&uploadLocalPath is empty!");

                $info("Receipt Confirmation's backupLocalPath&uploadLocalPath is empty!");

                return false;
            }

            FileWriteUtils fileWriter = null;
            try {
                Iterator iterator = receiptConfirmationByBrandMap.keySet().iterator();
                if (iterator != null) {
                    while (iterator.hasNext()) {
                        String brand = String.valueOf(iterator.next());

                        String now = DateTimeUtil.getNow();
                        String timeZone = ChannelConfigs.getVal1(channelId, ChannelConfigEnums.Name.channel_time_zone);
                        now = DateTimeUtil.getLocalTime(now, Integer.valueOf(timeZone));

                        String fileName = "Receipt_VO_" + brand + "_" + now.substring(0, 4) + now.substring(5, 7) + now.substring(8, 10)
                                + "_" + now.substring(11, 13) + now.substring(14, 16) + now.substring(17, 19) + ".dat";
                        String backupFileName = backupLocalPath + File.separator + fileName;

                        File backupFile = new File(backupFileName);
                        FileOutputStream fop = new FileOutputStream(backupFile);

                        fileWriter = new FileWriteUtils(fop, Charset.forName("UTF-8"), "A", "S");

                        List<ReceiptConfirmationBean> receiptConfirmationBeanList = receiptConfirmationByBrandMap.get(brand);

                        Map<String, String> successConfirm = new HashMap<String, String>();
                        successConfirm.put("brand", brand);
                        successConfirm.put("shipmentId", receiptConfirmationBeanList.get(0).getShipmentId());
                        successConfirm.put("transferId", receiptConfirmationBeanList.get(0).getTransferId());
                        successConfirm.put("taskName", "WmsReceiptConfirmationJob");

                        // Body输出
                        createReceiptConfirmFileBody(fileWriter, receiptConfirmationBeanList);

                        fileWriter.flush();
                        fileWriter.close();

                        if (backupFile.exists()) {
                            $info("backupFile:" + backupFileName + " is created success.");

                            String uploadLocalFileName = uploadLocalPath + File.separator + fileName;

                            try {
                                FileUtils.copyFile(backupFileName, uploadLocalFileName);
                            } catch (Exception ex) {
                                issueLog.log(ex, ErrorType.BatchJob, SubSystem.WMS);

                                $info(ex.getMessage(), ex);

                                try {
                                    FileUtils.delFile(backupFileName);
                                    FileUtils.delFile(uploadLocalFileName);
                                } catch (Exception e) {
                                    $info(ex.getMessage(), ex);
                                }

                                return false;

                            }

                            $info("uploadLocalFile:" + uploadLocalFileName + " is created success.");
                        }

                        isSuccess = true;

                        successConfirmList.add(successConfirm);
                    }
                }
            } catch (Exception e) {
                isSuccess = false;

                logger.error("createReceiptConfirmFile", e);

                issueLog.log(e, ErrorType.BatchJob, SubSystem.WMS);
            } finally {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            }

            return isSuccess;
        }

        /**
         * receiptConfirmation文件生成
         *
         * @param fileWriter 待上传文件名
         * @param receiptConfirmationBeanList 订单数据
         */
        private void createReceiptConfirmFileBody(FileWriteUtils fileWriter, List<ReceiptConfirmationBean> receiptConfirmationBeanList) throws IOException {

            for (int i = 0 ; i < receiptConfirmationBeanList.size(); i++) {
                ReceiptConfirmationBean receiptConfirmationBean = receiptConfirmationBeanList.get(i);

                fileWriter.write(receiptConfirmationBean.getPoNumber(), ReceiptConfirmationFormat.PONumber);
                String receiptDate = receiptConfirmationBean.getReceiptDate();
                fileWriter.write(receiptDate.substring(0, 4) + receiptDate.substring(5, 7) + receiptDate.substring(8, 10), ReceiptConfirmationFormat.ReceiptDate);
                fileWriter.write(String.valueOf(receiptConfirmationBean.getLineNumber()), ReceiptConfirmationFormat.LineNumber);
                fileWriter.write(receiptConfirmationBean.getUpc(), ReceiptConfirmationFormat.UPC);
                fileWriter.write(receiptConfirmationBean.getQuantityReceived(), ReceiptConfirmationFormat.QuantityReceived);
                fileWriter.write("868", ReceiptConfirmationFormat.CostCenter);
                fileWriter.write(receiptConfirmationBean.getCartonNumber(), ReceiptConfirmationFormat.CartonNumber);
                fileWriter.write(receiptConfirmationBean.getCustomerItem(), ReceiptConfirmationFormat.CustomerItem);
                fileWriter.write(receiptConfirmationBean.getCustomerPoNumber(), ReceiptConfirmationFormat.CustomerPONumber);
                fileWriter.write(receiptConfirmationBean.getCustPoLineNo(), ReceiptConfirmationFormat.CustPOLineNo);

                fileWriter.write(System.lineSeparator());
            }
        }

        /**
         * RCPT_BBB_YYYYMMDD_HHMMSS.dat 文件格式（upload： /opt/ftp-shared/clients-ftp/voyageone-bcbg-sftp/inventory/receipts）
         */
        private class ReceiptConfirmationFormat {
            private static final String PONumber = "S,8";
            private static final String ReceiptDate = "S,8";
            private static final String LineNumber = "S,6";
            private static final String UPC = "A,25";
            private static final String QuantityReceived = "S,15";
            private static final String CostCenter = "A,3";
            private static final String CartonNumber = "A,25";
            private static final String CustomerItem = "A,25";
            private static final String CustomerPONumber = "A,25";
            private static final String CustPOLineNo = "S,7";
        }
    }

    /**
     *
     * @param successConfirm
     * @param orderChannelId
     */
    private void resetConfrimFlag(Map<String, String> successConfirm, String orderChannelId) {
        successConfirm.put("orderChannelId", orderChannelId);

        transferDao.updateReceiptConfirmInfo(successConfirm);
    }
}
