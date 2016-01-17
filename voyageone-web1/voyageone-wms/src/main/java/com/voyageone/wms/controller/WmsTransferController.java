package com.voyageone.wms.controller;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.base.BaseController;
import com.voyageone.wms.WmsConstants;
import com.voyageone.wms.WmsCodeConstants.TransferType;
import com.voyageone.wms.WmsConstants.ReportItems;
import com.voyageone.wms.WmsUrlConstants.TransferUrls;
import com.voyageone.wms.formbean.TransferFormBean;
import com.voyageone.wms.formbean.TransferMapBean;
import com.voyageone.wms.modelbean.TransferBean;
import com.voyageone.wms.modelbean.TransferDetailBean;
import com.voyageone.wms.modelbean.TransferItemBean;
import com.voyageone.wms.service.WmsTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tester on 5/2/2015.
 *
 * @author Jonas
 */
@Controller
@RequestMapping(method = RequestMethod.POST)
public class WmsTransferController extends BaseController {
    @Autowired
    private WmsTransferService transferService;

    /**
     * 捡货画面初始化
     */
    @RequestMapping(TransferUrls.INIT)
    @ResponseBody
    public AjaxResponseBean doListInit() {
        return AjaxResponseBean.newResult(true, this).setResultInfo(transferService.doListInit(getUser()));
    }

    /**
     * 根据条件搜索 Transfer
     */
    @RequestMapping(TransferUrls.SEARCH)
    public void search(@RequestBody Map<String, Object> params, HttpServletResponse response) {

        String po = (String) params.get("po");
        int store = (int) params.get("store");
        String status = (String) params.get("status");
        String from = (String) params.get("from");
        String to = (String) params.get("to");

        int page = (int) params.get("page");
        int size = (int) params.get("size");

        from = StringUtils.isNullOrBlank2(from) ? "1990-01-01 00:00:00" : DateTimeUtil.getGMTTimeFrom(from, getUser().getTimeZone());
        to = StringUtils.isNullOrBlank2(to) ? "2099-12-31 23:59:59" : DateTimeUtil.getGMTTimeTo(to, getUser().getTimeZone());

        if ("All".equals(status)) {
            status = "";
        }

        List<TransferFormBean> transfers = transferService.search(po, store, status, from, to, getUser().getCompanyRealStoreList(), page, size, getUser());

        int rowsCount = transferService.getSearchCount(po, store, status, from, to, getUser().getCompanyRealStoreList());

        JsonObj json = new JsonObj()
                .add("data", transfers)
                .add("rows", rowsCount);

        AjaxResponseBean
                .newResult(true)
                .setResultInfo(json)
                .writeTo(getRequest(), response);
    }

    /**
     * 删除一个 Transfer
     */
    @RequestMapping(TransferUrls.DELETE)
    public void delete(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        long transferId = Long.valueOf((String) params.get("transferId"));
        String modified = (String) params.get("modified");

        boolean isSuccess = transferService.delete(transferId, modified);

        AjaxResponseBean
                .newResult(true)
                .setResultInfo(isSuccess)
                .writeTo(getRequest(), response);
    }

    /**
     * 获取所有启用的仓库
     */
    @RequestMapping(TransferUrls.Configs.ALL)
    public void getAllConfigs(@RequestBody Map<String, Object> params, HttpServletResponse response) {

        String transferId = (String) params.get("transferId");

        Map<String, Object> result = transferService.allConfigs(transferId, getUser());

        AjaxResponseBean
                .newResult(true)
                .setResultInfo(result)
                .writeTo(getRequest(), response);
    }

    /**
     * 获取一个 Transfer
     */
    @RequestMapping(TransferUrls.GET)
    public void getTransfer(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        long transferId = Long.valueOf((String) params.get("transferId"));

        TransferBean transfer = transferService.get(transferId);

        List<TransferDetailBean> packages = transferService.getPackages(transfer.getTransfer_id());

        String map_target_name = transferService.getMapTarget(transferId);

        JsonObj json = new JsonObj()
                .add("transfer", transfer)
                .add("packages", packages)
                .add("map_target", map_target_name);

        AjaxResponseBean
                .newResult(true)
                .setResultInfo(json)
                .writeTo(getRequest(), response);
    }

    /**
     * 不会修改 Transfer 的状态，仅保存其描述信息
     */
    @RequestMapping(TransferUrls.SAVE)
    public void saveTransfer(@RequestBody TransferMapBean map, HttpServletResponse response) {
        TransferBean transfer = map.getTransfer();

        // 如果是 PO，则同步 from 和 to
        if (TransferType.PO.equals(transfer.getTransfer_type())) {
            transfer.setTransfer_from_store(transfer.getTransfer_to_store());
        }
        // 如果是 RE，则同步 from 和 to
        else if (TransferType.RE.equals(transfer.getTransfer_type())) {
            transfer.setTransfer_to_store(transfer.getTransfer_from_store());
        }

        // 检查基础信息的有效性
        List<String> msgList = checkTransferForSave(map);

        if (msgList.size() > 0) {
            AjaxResponseBean res = AjaxResponseBean.newResult(false);
            res.setMessage(StringUtils.join(msgList, "<br />"));
            res.setMessageType(6);
            res.writeTo(getRequest(), response);
            return;
        }

        TransferBean beanAtDb = transferService.save(map, getUser());

        AjaxResponseBean
                .newResult(true)
                .setResultInfo(beanAtDb)
                .writeTo(getRequest(), response);
    }

    /**
     * 获取一个 Package
     */
    @RequestMapping(TransferUrls.Package.GET)
    public void getPackage(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        long transferId = Long.valueOf((String) params.get("transferId"));
        String packageName = (String) params.get("packageName");

        TransferDetailBean detail = transferService.getPackage(transferId, packageName);

        AjaxResponseBean.newResult(true)
                .setResultInfo(detail)
                .writeTo(getRequest(), response);
    }

    /**
     * 尝试创建一个 Package
     */
    @RequestMapping(TransferUrls.Package.CREATE)
    public void createPackage(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        long transferId = Long.valueOf((String) params.get("transferId"));
        String packageName = (String) params.get("packageName");

        TransferDetailBean detail = transferService.createPackage(transferId, packageName, getUser());

        AjaxResponseBean.newResult(true)
                .setResultInfo(detail)
                .writeTo(getRequest(), response);
    }

    /**
     * 关闭一个 Package，仅修改状态
     */
    @RequestMapping(TransferUrls.Package.CLOSE)
    public void closePackage(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        long package_id = Long.valueOf((String) params.get("package_id"));
        String modified = (String) params.get("modified");

        boolean isSuccess = transferService.closePackage(package_id, modified, getUser());

        AjaxResponseBean.newResult(isSuccess).setResultInfo(isSuccess).writeTo(getRequest(), response);
    }

    /**
     * 删除一个 Package，同时删除其所有的 Item
     */
    @RequestMapping(TransferUrls.Package.DELETE)
    public void deletePackage(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        long package_id = Long.valueOf((String) params.get("package_id"));
        String modified = (String) params.get("modified");

        boolean is = transferService.deletePackage(package_id, modified);

        AjaxResponseBean.newResult(true)
                .setResultInfo(is)
                .writeTo(getRequest(), response);
    }

    /**
     * 重新打开一个 Package
     */
    @RequestMapping(TransferUrls.Package.REOPEN)
    public void reOpenPackage(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        long package_id = Long.valueOf((String) params.get("package_id"));
        String modified = (String) params.get("modified");

        boolean is = transferService.reOpenPackage(package_id, modified);

        AjaxResponseBean.newResult(true)
                .setResultInfo(is)
                .writeTo(getRequest(), response);
    }

    /**
     * 向 Package 中添加一个 Item
     */
    @RequestMapping(TransferUrls.Item.ADD)
    public void addItem(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        long package_id = Long.valueOf((String) params.get("package_id"));
        String barcode = (String) params.get("barcode");
        int num = (int) params.get("num");

        String itemCode = (String) params.get("itemCode");
        String color = (String) params.get("color");
        String size = (String) params.get("size");

        String sku = transferService.addItem(package_id, barcode, num, itemCode, color, size, getUser());

        AjaxResponseBean.newResult(true)
                .setResultInfo(sku)
                .writeTo(getRequest(), response);
    }

    /**
     * 提交一个 Transfer，除了保存描述信息外，强制变更其状态
     */
    @RequestMapping(TransferUrls.SUBMIT)
    public void submitTransfer(@RequestBody TransferMapBean map, HttpServletResponse response) {
        TransferBean transfer = map.getTransfer();

        // 如果是 PO，则同步 from 和 to
        if (TransferType.PO.equals(transfer.getTransfer_type())) {
            transfer.setTransfer_from_store(transfer.getTransfer_to_store());
        }
        // 如果是 RE，则同步 from 和 to
        else if (TransferType.RE.equals(transfer.getTransfer_type())) {
            transfer.setTransfer_to_store(transfer.getTransfer_from_store());
        }

        // 检查基础信息的有效性
        List<String> msgList = checkTransferForSave(map);

        if (msgList.size() > 0) {
            AjaxResponseBean res = AjaxResponseBean.newResult(false);
            res.setMessage(StringUtils.join(msgList, "<br />"));
            res.setMessageType(MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
            res.writeTo(getRequest(), response);
            return;
        }

        TransferBean beanAtDb = transferService.submitTransfer(map, getUser());

        AjaxResponseBean
                .newResult(true)
                .setResultInfo(beanAtDb)
                .writeTo(getRequest(), response);
    }

    /**
     * 获取所有启用的仓库
     */
    @RequestMapping(TransferUrls.COMPARE)
    public void compareTransfer(@RequestBody TransferMapBean map, HttpServletResponse response) {

        TransferBean transfer = map.getTransfer();

        Map<String, Object> result = transferService.compareTransfer(transfer);

        AjaxResponseBean
                .newResult(true)
                .setResultInfo(result)
                .writeTo(getRequest(), response);
    }

    /**
     * 获取两个Transfer， Out 和 In 的所有 Item。
     */
    @RequestMapping(TransferUrls.Item.COMPARE)
    public void compare(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        long transfer_in_id = Long.valueOf((String) params.get("transfer_id"));

        Map<String, List<TransferItemBean>> items = transferService.allItemInMap(transfer_in_id);

        AjaxResponseBean
                .newResult(true)
                .setResultInfo(items)
                .writeTo(getRequest(), response);
    }

    /**
     * 获取一个 Package 下的所有 Item
     */
    @RequestMapping(TransferUrls.Item.SELECT)
    public void getPackageItems(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        long package_id = Long.valueOf((String) params.get("package_id"));

        List<TransferItemBean> items = transferService.getItemsInPackage(package_id);

        AjaxResponseBean.newResult(true)
                .setResultInfo(items)
                .writeTo(getRequest(), response);
    }

    /**
     * 下载入库、出库、进货清单
     *
     */
    @RequestMapping(value = TransferUrls.DOWNLOAD, method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadTransferList(String transfer_id) throws IOException {

        byte[] bytes = transferService.downloadTransferList(transfer_id,getUser());

        String outFile =  WmsConstants.ReportTransferItems.FILE_NAME + "_" + DateTimeUtil.getNow() + transfer_id +".xls";

        return  genResponseEntityFromBytes(outFile, bytes);

    }

    /**
     * ClientShipment详情报表下载
     */
    @RequestMapping(value = TransferUrls.DOWNLOAD_CLIENT_SHIPMENT, method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadClientShipment(String param) throws IOException {
        byte[] bytes = transferService.downloadClientShipment(param, getUser());
        String outFile = ReportItems.ClientShipment.RPT_NAME + "_" + DateTimeUtil.getNow() + ReportItems.ClientShipment.RPT_SUFFIX;
        return  genResponseEntityFromBytes(outFile, bytes);
    }

    /**
     * CompareTransfer详情报表下载
     */
    @RequestMapping(value = TransferUrls.DOWNLOAD_TRANSFER_COMPARE, method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadTransferCompare(String param) throws IOException {
        byte[] bytes = transferService.downloadTransferCompare(param, getUser());
        String outFile = ReportItems.TransferCompare.RPT_NAME + "_" + DateTimeUtil.getNow() + ReportItems.TransferCompare.RPT_SUFFIX;
        return  genResponseEntityFromBytes(outFile, bytes);
    }

    /**
     * 在保存或提交 Transfer 之前，对其进行输入校验
     */
    private List<String> checkTransferForSave(TransferMapBean map) {
        TransferBean transfer = map.getTransfer();

        List<String> msgList = new ArrayList<>();

        if (StringUtils.isEmpty(transfer.getTransfer_type())) {
            msgList.add("transfer type is empty");
        }

        if (transfer.getTransfer_from_store() < 1) {
            msgList.add("target store \" from \", is empty");
        }

        if (transfer.getTransfer_to_store() < 1) {
            msgList.add("target store \" to \", is empty");
        }

        if (TransferType.PO.equals(transfer.getTransfer_type())) {

            if (StringUtils.isEmpty(transfer.getPo_number()))
                msgList.add("po number is empty");

        } else if (TransferType.IN.equals(transfer.getTransfer_type()) || TransferType.OUT.equals(transfer.getTransfer_type())) {

            if (transfer.getTransfer_from_store() == transfer.getTransfer_to_store())
                msgList.add("target store \" from \" cant equals \" to \".");

        }

        if (TransferType.IN.equals(transfer.getTransfer_type())
                && StringUtils.isEmpty(map.getContext_name())) {
            msgList.add("Mapping transfer out is empty.");
        }
        return msgList;
    }

}