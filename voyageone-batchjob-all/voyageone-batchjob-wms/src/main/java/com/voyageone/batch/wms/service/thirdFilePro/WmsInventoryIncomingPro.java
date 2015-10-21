package com.voyageone.batch.wms.service.thirdFilePro;

import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.batch.wms.modelbean.*;
import com.voyageone.common.components.FixedLengthReader;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service public class WmsInventoryIncomingPro extends ThirdFileProBaseService {

    private List<OrderUpdateBean> bakOrderUpdtBean = new ArrayList<>();
    private String bakTempTable = "";


    public void doRun(String channelId, List < TaskControlBean > taskControlList, List < ThirdPartyConfigBean > thirdPartyConfigBean) {
        OrderChannelBean channel = ChannelConfigs.getChannel(channelId);
        assert channel != null;
        log(channel.getFull_name() + ": " + getTaskName() + "开始");
        for (ThirdPartyConfigBean tcb : thirdPartyConfigBean) {
            try {
                processFile(tcb, channel.getOrder_channel_id());
            } catch(Exception e) {
                logger.error(channel.getFull_name() + "解析第三方Shipment文件发生错误：", e);
                logIssue(channel.getFull_name() + "解析第三方Shipment文件发生错误：" + e);
            }
        }
        log(channel.getFull_name() + ": " + getTaskName() + "结束");
    }

    /**
     * @description 文件数据做相关处理
     * @param tcb 第三方表的配置
     * @param orderChannelId 渠道
     */
    private void processFile(ThirdPartyConfigBean tcb, String orderChannelId) {
        //需要处理的文件名
        String fileName = tcb.getProp_val1();
        //根据文件名获取文件路径
        final String filePath = tcb.getProp_val2();;
        //实际操作的文件名列表,按时间升序排序
        final Object[] fileNameList = processFileNames(fileName, filePath);
        //文件头部的定长
        final String[] headFixedLength = tcb.getProp_val3().split(",");
        //文件明细的定长
        final String[] detailFixedLength = tcb.getProp_val4().split(",");

        //完成处理后的FTP备份路径
        final String ftpBakFilePath = tcb.getProp_val5();
        //完成处理后的服务器备份路径
        final String webBakFilePath = tcb.getProp_val6();

        // 解析文件
        for(Object fileNameInfo : fileNameList) {
            String filenameInfoStr = (String) fileNameInfo;
            List<bulkShipmentBean> fileDataList = readFile(filePath, filenameInfoStr, headFixedLength, detailFixedLength, orderChannelId);
            if(fileDataList != null && fileDataList.size() > 0) {
                updateTabels(filenameInfoStr, orderChannelId, fileDataList, filePath, ftpBakFilePath,webBakFilePath);
            }

        }

    }

    /**
     * @description 读取文件，将数据设置到OrderUpdateBean的集合里面
     * @param filePath 文件所在路径
     * @param filename 文件名称
     * @param headFixedLength 文件头部的定长
     * @param detailFixedLength 文件明细的定长
     * @param orderChannelId 渠道
     * @return OrderUpdateBean集合
     */
    protected List<bulkShipmentBean> readFile(String filePath, String filename,String[] headFixedLength, String[] detailFixedLength, String orderChannelId) {
        OrderChannelBean channel = ChannelConfigs.getChannel(orderChannelId);
        log(channel.getFull_name() + "读取 " + filename + " 文件信息开始");
        List<bulkShipmentBean> bulkShipmentBeans = new ArrayList<>();

        try {
            filePath = filePath + "/" + filename;
            File file = new File(filePath);
            //判断文件是否下载完毕
            while (FileUtils.fileIsInUse(file)){
                Thread.sleep(1000);
            }
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), CommonUtil.getCharset(filePath));
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt;
                int countRow = 0;
                bulkShipmentBean bulkShipmentBean = new bulkShipmentBean();
                FixedLengthReader fixedLengthReader = new FixedLengthReader(true, false);
                while ((lineTxt = bufferedReader.readLine()) != null) {

                    // 判断是头文件还是明细文件
                    if (lineTxt.substring(0,1).equals(WmsConstants.INVENTORY_INCOMING.HEAD)){
                        if (countRow > 0) {
                            bulkShipmentBeans.add(bulkShipmentBean);
                        }
                        bulkShipmentBean = new bulkShipmentBean();

                        List<String> head = fixedLengthReader.readLine(lineTxt, headFixedLength);

                        bulkShipmentHeadBean bulkShipmentHeadBean = new bulkShipmentHeadBean();

                        bulkShipmentHeadBean.setHeader_identifier(head.get(0));
                        bulkShipmentHeadBean.setHdl_unit_exid(head.get(1));
                        bulkShipmentHeadBean.setKuwev_kunnr(head.get(2));
                        bulkShipmentHeadBean.setLikp_bldat(head.get(3));
                        bulkShipmentHeadBean.setTotal_quantity(head.get(4));
                        bulkShipmentHeadBean.setShip_via_code(head.get(5));
                        bulkShipmentHeadBean.setTracking_num(head.get(6));
                        bulkShipmentHeadBean.setShipping_material(head.get(7));
                        //bulkShipmentHeadBean.setCancel_flag(head.get(8));

                        bulkShipmentBean.setBulkShipmentHead(bulkShipmentHeadBean);
                    }
                    else if (lineTxt.substring(0,1).equals(WmsConstants.INVENTORY_INCOMING.DETAIL)){

                        List<String> detail = fixedLengthReader.readLine(lineTxt, detailFixedLength);

                        bulkShipmentDetailBean bulkShipmentDetailBean = new bulkShipmentDetailBean();

                        bulkShipmentDetailBean.setItem_identifier(detail.get(0));
                        bulkShipmentDetailBean.setCarton_line(detail.get(1));
                        bulkShipmentDetailBean.setLips_vgbel(detail.get(2));
                        bulkShipmentDetailBean.setLips_posnr(detail.get(3));
                        bulkShipmentDetailBean.setLips_matnr(detail.get(4));
                        bulkShipmentDetailBean.setDlv_qty(detail.get(5));
                        bulkShipmentDetailBean.setFact_unit_nom(detail.get(6));
                        bulkShipmentDetailBean.setBase_uom(detail.get(7));
                        bulkShipmentDetailBean.setStock_type(detail.get(8));
                        bulkShipmentDetailBean.setPlant(detail.get(9));
                        bulkShipmentDetailBean.setStge_loc(detail.get(10));
                        bulkShipmentDetailBean.setUpc(detail.get(11));

                        List<bulkShipmentDetailBean> BulkShipmentDetails = bulkShipmentBean.getBulkShipmentDetails();
                        if (BulkShipmentDetails == null){
                            BulkShipmentDetails = new ArrayList<>();
                        }
                        BulkShipmentDetails.add(bulkShipmentDetailBean);

                        bulkShipmentBean.setBulkShipmentDetails(BulkShipmentDetails);

                    }

                    countRow++;
                }
                bufferedReader.close();
                read.close();

                //文件中可处理数据总记录数为0
                if (countRow == 0) {
                    logger.info(channel.getFull_name() + "读取 " + filename + "文件的可处理数据数为0");
                    throw new RuntimeException(channel.getFull_name() + "读取 "+ filename+ "文件的可处理数据数为0");
                }else {
                    logger.info(channel.getFull_name() + "读取 " + filename + "文件的可处理数据数为"+countRow);
                }
            } else {
                logger.info(channel.getFull_name() + "找不到指定的文件 filePath:" + filePath);
            }
        } catch (Exception e) {
            throw new RuntimeException(channel.getFull_name() + "读取 "+ filePath + " 文件出错!" + e);
        }
        log(channel.getFull_name() +  "读取 " + filename + " 文件信息结束");
        return bulkShipmentBeans;
    }

    /**
     * @description 更新数据库
     * @param fileNameInfo 文件名称
     * @param orderChannelId 渠道Id
     * @param bulkShipmentBeans 参数Map
     */
    private void updateTabels(final String fileNameInfo, final String orderChannelId, final List<bulkShipmentBean> bulkShipmentBeans, final String filePath, final String ftpBakFilePath, final String webBakFilePath){
        OrderChannelBean channel = ChannelConfigs.getChannel(orderChannelId);
        log(channel.getFull_name()+"处理文件 " + fileNameInfo + " 开始");
        transactionRunner.runWithTran(() -> {
            try {
                //wms_bt_client_shipment
                ClientShipmentBean clientShipmentBean = setClientShipment(orderChannelId, fileNameInfo);
                long shipmentId = clientShipmentDao.insertClientShipment(clientShipmentBean);

                for (com.voyageone.batch.wms.modelbean.bulkShipmentBean bulkShipmentBean : bulkShipmentBeans) {
                    //wms_bt_client_package
                    ClientPackageBean clientPackageBean = setClientPackage(shipmentId,bulkShipmentBean.getBulkShipmentHead());
                    long packageId = clientShipmentDao.insertClientPackage(clientPackageBean);

                    for (com.voyageone.batch.wms.modelbean.bulkShipmentDetailBean bulkShipmentDetailBean : bulkShipmentBean.getBulkShipmentDetails()) {
                        //wms_bt_client_package_item
                        ClientPackageItemBean clientPackageItemBean = setClientPackageItem(shipmentId,packageId,bulkShipmentDetailBean);
                        clientShipmentDao.insertClientPackageItem(clientPackageItemBean);
                    }
                }

                // 处理完毕，复制文件
                copyFile(filePath, fileNameInfo, webBakFilePath);
                // 处理完毕，移除文件
                moveFile(filePath, fileNameInfo, ftpBakFilePath);
            } catch (Exception e) {
                logger.error(channel.getFull_name() + "处理文件 " + fileNameInfo + " 失败：" + e);
                throw new RuntimeException(channel.getFull_name() + "处理文件 " + fileNameInfo + " 失败：" + e.getMessage());
            }

            log(channel.getFull_name() + "处理文件 " + fileNameInfo + " 结束");
        });
    }

    /**
     * @description 编辑ClientShipment
     * @param orderChannelId 订单渠道
     * @param fileName 文件名
     * @return ClientShipmentBean
     */
    private ClientShipmentBean setClientShipment(String orderChannelId, String fileName){
        ClientShipmentBean clientShipmentBean = new ClientShipmentBean();

        clientShipmentBean.setOrder_channel_id(orderChannelId);
        clientShipmentBean.setFile_name(fileName);
        clientShipmentBean.setBrand(fileName.substring(4,7));
        clientShipmentBean.setTransfer_id(0);
        clientShipmentBean.setSyn_flg("0");
        clientShipmentBean.setActive("1");
        clientShipmentBean.setCreated(DateTimeUtil.getNow());
        clientShipmentBean.setCreater(getTaskName());
        clientShipmentBean.setModified(DateTimeUtil.getNow());
        clientShipmentBean.setModifier(getTaskName());

        return clientShipmentBean;
    }

    /**
     * @description 编辑ClientPackage
     * @param bulkShipmentHead 头文件
     * @return ClientPackageBean
     */
    private ClientPackageBean setClientPackage(long shipmentId, bulkShipmentHeadBean bulkShipmentHead){
        ClientPackageBean clientPackageBean = new ClientPackageBean();

        clientPackageBean.setShipment_id(shipmentId);
        clientPackageBean.setUcc128_carton_no(bulkShipmentHead.getHdl_unit_exid());
        clientPackageBean.setShip_to_store(bulkShipmentHead.getKuwev_kunnr());
        clientPackageBean.setShip_date(bulkShipmentHead.getLikp_bldat());
        clientPackageBean.setTotal_carton_quantity(bulkShipmentHead.getTotal_quantity());
        clientPackageBean.setShip_via(bulkShipmentHead.getShip_via_code());
        clientPackageBean.setTracking_no(bulkShipmentHead.getTracking_num());
        clientPackageBean.setShipping_material(bulkShipmentHead.getShipping_material());
        clientPackageBean.setCancellation_flag(bulkShipmentHead.getCancel_flag());
        clientPackageBean.setActive("1");
        clientPackageBean.setCreated(DateTimeUtil.getNow());
        clientPackageBean.setCreater(getTaskName());
        clientPackageBean.setModified(DateTimeUtil.getNow());
        clientPackageBean.setModifier(getTaskName());

        return clientPackageBean;
    }

    /**
     * @description 编辑ClientPackageItem
     * @param bulkShipmentDetailBean 明细文件
     * @return ClientPackageItemBean
     */
    private ClientPackageItemBean setClientPackageItem(long shipmentId, long packageId, bulkShipmentDetailBean bulkShipmentDetailBean){
        ClientPackageItemBean clientPackageItemBean = new ClientPackageItemBean();

        clientPackageItemBean.setShipment_id(shipmentId);
        clientPackageItemBean.setPackage_id(packageId);
        clientPackageItemBean.setCarton_line_number(bulkShipmentDetailBean.getCarton_line());
        clientPackageItemBean.setSto_no(bulkShipmentDetailBean.getLips_vgbel());
        clientPackageItemBean.setSto_line_no(bulkShipmentDetailBean.getLips_posnr());
        clientPackageItemBean.setArticle_number(bulkShipmentDetailBean.getLips_matnr());
        clientPackageItemBean.setShipped_qty(bulkShipmentDetailBean.getDlv_qty());
        clientPackageItemBean.setFact_unit_nom(bulkShipmentDetailBean.getFact_unit_nom());
        clientPackageItemBean.setBase_uom(bulkShipmentDetailBean.getBase_uom());
        clientPackageItemBean.setStock_type(bulkShipmentDetailBean.getStock_type());
        clientPackageItemBean.setSite(bulkShipmentDetailBean.getPlant());
        clientPackageItemBean.setShipping_point(bulkShipmentDetailBean.getStge_loc());
        clientPackageItemBean.setUpc(bulkShipmentDetailBean.getUpc());
        clientPackageItemBean.setActive("1");
        clientPackageItemBean.setCreated(DateTimeUtil.getNow());
        clientPackageItemBean.setCreater(getTaskName());
        clientPackageItemBean.setModified(DateTimeUtil.getNow());
        clientPackageItemBean.setModifier(getTaskName());

        return clientPackageItemBean;
    }

    @Override public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override public String getTaskName() {
        return "ThirdOrderFilePro";
    }

    @Override protected void onStartup(List < TaskControlBean > taskControlList) throws Exception {

    }

}