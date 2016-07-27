package com.voyageone.web2.vms.views.order;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.util.BeanUtil;
import com.voyageone.common.util.MapUtil;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.vms.order.OrderDetailService;
import com.voyageone.service.impl.vms.shipment.ShipmentService;
import com.voyageone.service.model.vms.VmsBtOrderDetailModel;
import com.voyageone.service.model.vms.VmsBtShipmentModel;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.vms.bean.SortParam;
import com.voyageone.web2.vms.bean.VmsChannelSettings;
import com.voyageone.web2.vms.bean.order.*;
import com.voyageone.web2.vms.bean.shipment.ShipmentBean;
import com.voyageone.web2.vms.bean.shipment.ShipmentEndCountBean;
import com.voyageone.web2.vms.views.common.ChannelConfigService;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.voyageone.web2.vms.VmsConstants.*;


/**
 * order info service of controller
 * Created by vantis on 16-7-6.
 */
@Service
public class VmsOrderInfoService extends BaseService {

    private OrderDetailService orderDetailService;
    private ShipmentService shipmentService;
    private ChannelConfigService channelConfigService;

    @Autowired
    public VmsOrderInfoService(OrderDetailService orderDetailService,
                               ShipmentService shipmentService,
                               ChannelConfigService channelConfigService) {
        this.orderDetailService = orderDetailService;
        this.shipmentService = shipmentService;
        this.channelConfigService = channelConfigService;
    }

    /**
     * 获得所有SKU相关的状态 用于页面选项显示
     *
     * @return skuStatusBeanList
     */
    public List<SkuStatusBean> getAllOrderStatusesList() {

        List<TypeBean> skuStatusList = Types.getTypeList(TYPE_ID.PRODUCT_STATUS);

        if (null == skuStatusList) return new ArrayList<>();

        return skuStatusList.stream()
                .map(typeBean -> {
                    SkuStatusBean skuStatusBean = new SkuStatusBean();
                    skuStatusBean.setName(typeBean.getName());
                    skuStatusBean.setValue(typeBean.getValue());
                    return skuStatusBean;
                })
                .collect(Collectors.toList());
    }


    /**
     * 默认条件获取订单信息(Open)
     *
     * @param user 当前用户
     * @return Order信息内容
     */
    public OrderInfoBean getOrderInfo(UserSessionBean user, OrderSearchInfo orderSearchInfo) {
        SortParam sortParam = new SortParam();
        sortParam.setColumnName(CONSOLIDATION_ORDER_TIME);
        sortParam.setDirection((null != orderSearchInfo.getStatus()
                && orderSearchInfo.getStatus().equals(STATUS_VALUE.PRODUCT_STATUS.OPEN) ?
                Order.Direction.ASC : Order.Direction.DESC));
        OrderInfoBean orderInfoBean = new OrderInfoBean();
        orderInfoBean.setTotal(this.getTotalOrderNum(user, orderSearchInfo, sortParam));
        orderInfoBean.setOrderList(this.getOrders(user, orderSearchInfo, sortParam));
        return orderInfoBean;
    }

    /**
     * 取消订单
     *
     * @param user 当前用户
     * @param item 被取消订单
     * @return 取消条目数
     */
    public int cancelOrder(UserSessionBean user, PlatformSubOrderInfoBean item) {

        // 检查订单状态
        Map<String, Object> checkParam = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannel().getId());
            put("consolidationOrderId", item.getConsolidationOrderId());
        }};

        List<VmsBtOrderDetailModel> invalidOrderModelList = orderDetailService.selectOrderList(checkParam)
                .stream()
                .filter(vmsBtOrderDetailModel -> !vmsBtOrderDetailModel.getStatus()
                        .equals(STATUS_VALUE.PRODUCT_STATUS.OPEN))
                .collect(Collectors.toList());

        if (null != invalidOrderModelList && invalidOrderModelList.size() > 0)
            throw new BusinessException("8000020");

        // 检测通过 进行状态变更
        return orderDetailService.updateOrderStatus(user.getSelChannelId(), item.getConsolidationOrderId(), STATUS_VALUE
                .PRODUCT_STATUS.CANCEL, user.getUserName());
    }

    /**
     * sku级别的取消
     *
     * @param user 当前用户
     * @param item 需要取消的对象
     * @return 取消条数
     */
    public int cancelSku(UserSessionBean user, SubOrderInfoBean item) {

        // 检查sku状态
        Map<String, Object> checkParam = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannel().getId());
            put("reservationId", item.getReservationId());
        }};

        List<VmsBtOrderDetailModel> invalidOrderModelList = orderDetailService.selectOrderList(checkParam).stream()
                .filter(vmsBtOrderDetailModel -> !vmsBtOrderDetailModel.getStatus()
                        .equals(STATUS_VALUE.PRODUCT_STATUS.OPEN))
                .collect(Collectors.toList());

        if (null != invalidOrderModelList && invalidOrderModelList.size() > 0)
            throw new BusinessException("8000027");

        // 检测通过 进行状态变更
        return orderDetailService.updateReservationStatus(user.getSelChannelId(), item.getReservationId(),
                STATUS_VALUE.PRODUCT_STATUS.CANCEL, user.getUserName());
    }

    /**
     * 生成下载Excel拣货单
     *
     * @param user         当前用户
     * @param downloadInfo 下载信息(排序信息)
     * @return 拣货单Excel
     * @throws IOException
     */
    public byte[] getExcelBytes(UserSessionBean user, DownloadInfo downloadInfo) throws IOException {

        // 搜索条件
        Map<String, Object> selectParams = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannel().getId());
            put("status", STATUS_VALUE.PRODUCT_STATUS.OPEN);
        }};

        $debug("Getting pickingList data...");
        Map<String, Object> sortedSelectParams = MySqlPageHelper.build(selectParams)
                .addSort(downloadInfo.getOrderType(), Order.Direction.ASC)
                .toMap();

        // 获取订单信息
        List<VmsBtOrderDetailModel> orderDetailList =
                orderDetailService.select(sortedSelectParams);
        $debug("pickingList data: " + orderDetailList.size() + " in total.");

        // 生成Excel
        $debug("Creating Excel...");
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook();
        sxssfWorkbook.setCompressTempFiles(true); // 防止缓存文件过大 采用压缩方式处理

        // 页脚
        Sheet sheet = sxssfWorkbook.createSheet("PickingList");
        Footer footer = sheet.getFooter();
        footer.setCenter("Page " + HeaderFooter.page() + " of " + HeaderFooter.numPages());

        // 设置单元格默认格式
        CellStyle defaultRowCellStyle = sxssfWorkbook.createCellStyle();
        defaultRowCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        defaultRowCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        defaultRowCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        defaultRowCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        defaultRowCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        defaultRowCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);


        // 标题行格式
        CellStyle titleRowCellStyle = sxssfWorkbook.createCellStyle();
        titleRowCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        titleRowCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        titleRowCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        titleRowCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titleRowCellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        titleRowCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        Font font = sxssfWorkbook.createFont();
        font.setBold(true);
        titleRowCellStyle.setFont(font);
        titleRowCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        titleRowCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        /* 设置标题行 */
        Row titleRow = sheet.createRow(0);

        int skuCellNumber = 0;
        int descriptionCellNumber = 1;
        int orderIdCellNumber = 2;

        // 设置内容
        if (PICKING_LIST_ORDER_TYPE.ORDER.equals(downloadInfo.getOrderType())) {
            skuCellNumber = 2;
            orderIdCellNumber = 0;
        }

        Cell titleRowCell0 = titleRow.createCell(skuCellNumber);
        titleRowCell0.setCellValue("SKU");
        titleRowCell0.setCellStyle(titleRowCellStyle);

        Cell titleRowCell1 = titleRow.createCell(descriptionCellNumber);
        titleRowCell1.setCellValue("Name");
        titleRowCell1.setCellStyle(titleRowCellStyle);

        Cell titleRowCell2 = titleRow.createCell(orderIdCellNumber);
        titleRowCell2.setCellValue("OrderID");
        titleRowCell2.setCellStyle(titleRowCellStyle);

        // 设置数据行
        for (int i = 0; i < orderDetailList.size(); i++) {
            VmsBtOrderDetailModel vmsBtOrderDetailModel = orderDetailList.get(i);
            Row dataRow = sheet.createRow(i + 1);
            Cell skuCell = dataRow.createCell(skuCellNumber);
            skuCell.setCellValue(vmsBtOrderDetailModel.getClientSku());
            skuCell.setCellStyle(defaultRowCellStyle);

            Cell descriptionCell = dataRow.createCell(descriptionCellNumber);
            descriptionCell.setCellValue(vmsBtOrderDetailModel.getName());
            descriptionCell.setCellStyle(defaultRowCellStyle);

            Cell orderIdCell = dataRow.createCell(orderIdCellNumber);
            orderIdCell.setCellValue(vmsBtOrderDetailModel.getConsolidationOrderId());
            orderIdCell.setCellStyle(defaultRowCellStyle);
        }

        // 整理宽度
        sheet.autoSizeColumn(skuCellNumber);
        sheet.autoSizeColumn(descriptionCellNumber);
        sheet.autoSizeColumn(orderIdCellNumber);

        // 设定首行冻结
        sheet.createFreezePane(0, 1, 0, 1);

        $debug("Excel file created");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        sxssfWorkbook.write(byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 根据条件搜索订单
     *
     * @param user            当前用户
     * @param orderSearchInfo 搜索条件
     * @return 订单列表
     */
    public List<AbstractSubOrderInfoBean> getOrders(UserSessionBean user, OrderSearchInfo orderSearchInfo, SortParam
            sortParam) {

        List<AbstractSubOrderInfoBean> orderList = new ArrayList<>();
        Map<String, Object> orderSearchParamsWithLimitAndSort = this.organizeOrderSearchParams(user, orderSearchInfo,
                sortParam);
        /*
         * 根据渠道配置设置
         */
        switch (channelConfigService.getChannelConfigs(user).getVendorOperateType()) {

            // sku级的订单获取
            case STATUS_VALUE.VENDOR_OPERATE_TYPE.SKU: {
                return this.getSkuOrderInfoBean(user, orderSearchParamsWithLimitAndSort);
            }

            // 平台订单级获取
            case STATUS_VALUE.VENDOR_OPERATE_TYPE.ORDER: {

                return this.getPlatformOrderInfoBeen(user, orderSearchParamsWithLimitAndSort);
            }
        }
        return orderList;
    }

    /**
     * 获取平台订单
     *
     * @param orderSearchParamsWithLimitAndSort 搜索条件
     * @return 订单列表
     */
    private List<AbstractSubOrderInfoBean> getPlatformOrderInfoBeen(UserSessionBean user, Map<String, Object>
            orderSearchParamsWithLimitAndSort) {

        // 读取用户配置
        VmsChannelSettings channelConfigs = channelConfigService.getChannelConfigs(user);

        return orderDetailService.selectPlatformOrderIdList(orderSearchParamsWithLimitAndSort).parallelStream()
                .map(consolidationOrderId -> {

                    // 获取平台订单id下的所有的sku
                    List<VmsBtOrderDetailModel> vmsBtOrderDetailModelList = orderDetailService
                            .selectOrderList(new HashMap<String, Object>() {{
                                put("channelId", orderSearchParamsWithLimitAndSort.get("channelId"));
                                put("consolidationOrderId", consolidationOrderId);
                            }});

                    // 按照第一个sku初始化平台订单id内容
                    PlatformSubOrderInfoBean platformOrderInfoBean = new PlatformSubOrderInfoBean();
                    platformOrderInfoBean.setConsolidationOrderId(vmsBtOrderDetailModelList.get(0).getConsolidationOrderId());
                    platformOrderInfoBean.setConsolidationOrderTime(vmsBtOrderDetailModelList.get(0).getConsolidationOrderTime());
                    platformOrderInfoBean.setStatus(vmsBtOrderDetailModelList.get(0).getStatus());

                    // 将订单下的sku信息录入
                    vmsBtOrderDetailModelList.stream()
                            .map(vmsBtOrderDetailModel -> {
                                SubOrderInfoBean orderInfoBean = new SubOrderInfoBean();
                                BeanUtil.copy(vmsBtOrderDetailModel, orderInfoBean);
                                if (!STATUS_VALUE.SALE_PRICE_SHOW.SHOW.equals(channelConfigs.getSalePriceShow()))
                                    orderInfoBean.setRetailPrice(BigDecimal.ZERO);
                                return orderInfoBean;
                            })
                            .forEach(platformOrderInfoBean::pushOrderInfoBean);

                    return platformOrderInfoBean;
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取大订单SKU级订单
     *
     * @param orderSearchParamsWithLimitAndSort 搜索条件
     * @return 订单列表
     */
    private List<AbstractSubOrderInfoBean> getSkuOrderInfoBean(UserSessionBean user,
                                                               Map<String, Object> orderSearchParamsWithLimitAndSort) {
        VmsChannelSettings channelConfigs = channelConfigService.getChannelConfigs(user);

        List<VmsBtOrderDetailModel> vmsBtOrderDetailModelList = orderDetailService.selectOrderList
                (orderSearchParamsWithLimitAndSort);
        return vmsBtOrderDetailModelList.stream()
                .map(vmsBtOrderDetailModel -> {
                    SubOrderInfoBean orderInfoBean = new SubOrderInfoBean();
                    BeanUtil.copy(vmsBtOrderDetailModel, orderInfoBean);
                    if (!STATUS_VALUE.SALE_PRICE_SHOW.SHOW.equals(channelConfigs.getSalePriceShow()))
                        orderInfoBean.setRetailPrice(BigDecimal.ZERO);
                    return orderInfoBean;
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据输入条件组出对应的搜索Map
     *
     * @param user            当前用户
     * @param orderSearchInfo 搜索条件
     * @return 搜索条件Map
     */
    private Map<String, Object> organizeOrderSearchParams(UserSessionBean user, OrderSearchInfo orderSearchInfo,
                                                          SortParam sortParam) {
        Map<String, Object> orderSearchParams;
        try {
            orderSearchParams = MapUtil.toMap(orderSearchInfo);
        } catch (IllegalAccessException e) {
            throw new BusinessException("WRONG SEARCH PARAMETERS.", e);
        }

        orderSearchParams.put("channelId", user.getSelChannel().getId());

        // limit sort条件
        Map<String, Object> orderSearchParamsWithLimitAndSort = MySqlPageHelper.build(orderSearchParams)
                .addSort(sortParam.getColumnName(), sortParam.getDirection())
                .limit(orderSearchInfo.getSize())
                .page(orderSearchInfo.getCurr())
                .toMap();

        $debug(orderSearchParamsWithLimitAndSort.toString());
        return orderSearchParamsWithLimitAndSort;
    }

    /**
     * 获取条件下的订单总数
     *
     * @param user            当前用户
     * @param orderSearchInfo 搜索条件
     * @return 订单总数
     */
    private long getTotalOrderNum(UserSessionBean user, OrderSearchInfo orderSearchInfo, SortParam sortParam) {
        if (STATUS_VALUE.VENDOR_OPERATE_TYPE.ORDER.equals(channelConfigService.getChannelConfigs(user).getVendorOperateType())) {
            // 平台订单
            Map<String, Object> orderSearchParamsWithLimitAndSort =
                    organizeOrderSearchParams(user, orderSearchInfo, sortParam);
            return orderDetailService.getTotalOrderNum(orderSearchParamsWithLimitAndSort);
        } else if (STATUS_VALUE.VENDOR_OPERATE_TYPE.SKU.equals(channelConfigService.getChannelConfigs(user).getVendorOperateType())) {
            // 大订单sku
            Map<String, Object> skuSearchParamsWithLimitAndSort =
                    organizeOrderSearchParams(user, orderSearchInfo, sortParam);
            return orderDetailService.getTotalSkuNum(skuSearchParamsWithLimitAndSort);
        } else return 0;
    }

    /**
     * 获取已扫描的订单信息
     *
     * @param user     当前用户
     * @param shipment 当前shipment
     * @param orderId  当前orderId
     * @return 已扫描的订单列表
     */
    public List<SubOrderInfoBean> getScannedSkuList(UserSessionBean user, ShipmentBean shipment,
                                                    String orderId) {

        // 查找对应OrderId中 是否有已经扫描的SKU不在此shipment下
        Map<String, Object> checkParams = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannelId());
            put("consolidationOrderId", orderId);
        }};

        List<VmsBtOrderDetailModel> orderDetailList = orderDetailService.select(checkParams);

        long invalidSkuCount = orderDetailList.stream()
                .filter(vmsBtOrderDetailModel ->
                        null != vmsBtOrderDetailModel.getShipmentId()
                                && !shipment.getId().equals(vmsBtOrderDetailModel.getShipmentId()))
                .count();

        //
        if (invalidSkuCount > 0) throw new BusinessException("8000023");

        return orderDetailService.getScannedSku(user.getSelChannelId(), shipment.getId(), orderId).stream()
                .map(vmsBtOrderDetailModel -> {
                    SubOrderInfoBean subOrderInfoBean = new SubOrderInfoBean();
                    BeanUtil.copy(vmsBtOrderDetailModel, subOrderInfoBean);
                    return subOrderInfoBean;
                })
                .collect(Collectors.toList());
    }

    /**
     * 扫barcode 将对应的sku加入shipment
     *
     * @param user     当前用户
     * @param scanInfo 扫描参数
     * @return 扫描影响的条数
     */
    public int scanBarcodeInOrder(UserSessionBean user, ScanInfo scanInfo) {

        ShipmentBean shipment = scanInfo.getShipment();
        String barcode = scanInfo.getBarcode();
        String orderId = scanInfo.getConsolidationOrderId();

        // 检查订单状态
        Map<String, Object> checkParams = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannelId());
            put("consolidationOrderId", scanInfo.getConsolidationOrderId());
        }};

        long invalidCount = orderDetailService.select(checkParams).stream()
                .filter(vmsBtOrderDetailModel -> (null != vmsBtOrderDetailModel.getShipmentId()
                        && !vmsBtOrderDetailModel.getShipmentId().equals(shipment.getId()))

                        || (!vmsBtOrderDetailModel.getStatus().equals(STATUS_VALUE.PRODUCT_STATUS.OPEN)))
                .count();

        if (invalidCount > 0) throw new BusinessException("8000024");

        // 检查shipment状态
        VmsBtShipmentModel dbShipment = shipmentService.select(shipment.getId());
        if (!dbShipment.getStatus().equals(STATUS_VALUE.SHIPMENT_STATUS.OPEN)) throw new BusinessException("8000025");

        return orderDetailService.scanInOrder(user.getSelChannelId(), user.getUserName(),
                barcode, orderId, shipment.getId());
    }

    /**
     * 确认当期订单是否完整扫描 同时相应更新sku状态
     *
     * @param user     当前用户
     * @param scanInfo 扫描参数
     * @return 是否完整扫描
     */
    public boolean orderScanFinished(UserSessionBean user, ScanInfo scanInfo) {

        ShipmentBean shipment = scanInfo.getShipment();
        String orderId = scanInfo.getConsolidationOrderId();

        // 检查订单状态
        Map<String, Object> checkParams = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannelId());
            put("consolidationOrderId", scanInfo.getConsolidationOrderId());
        }};

        // 检查当前订单是否全部扫描完毕
        List<VmsBtOrderDetailModel> currentOrderInfo = orderDetailService.select(checkParams);
        long scannedCount = currentOrderInfo.stream()
                .filter(vmsBtOrderDetailModel -> null != vmsBtOrderDetailModel.getShipmentId()
                        && vmsBtOrderDetailModel.getShipmentId().equals(shipment.getId())
                        && vmsBtOrderDetailModel.getStatus().equals(STATUS_VALUE.PRODUCT_STATUS.OPEN))
                .count();

        // 全部扫描完毕 则更新状态为packaged
        if (scannedCount == currentOrderInfo.size()) {
            orderDetailService.updateOrderStatus(user.getSelChannelId(), orderId, STATUS_VALUE.PRODUCT_STATUS
                    .PACKAGE, user.getUserName());
            return true;
        }
        return false;
    }

    /**
     * 确认订单信息
     *
     * @param user         当前用户
     * @param shipmentBean 当前shipment
     * @return 未完整扫描的订单列表
     */
    public List<String> confirmShipment(UserSessionBean user, ShipmentBean shipmentBean) {

        // 获取当前shipment中的订单号
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannelId());
            put("shipmentId", shipmentBean.getId());
        }};
        return orderDetailService.select(params).stream()
                .filter(vmsBtOrderDetailModel -> !STATUS_VALUE.PRODUCT_STATUS.PACKAGE.equals(vmsBtOrderDetailModel
                        .getStatus()))
                .map(VmsBtOrderDetailModel::getConsolidationOrderId)
                .distinct()
                .collect(Collectors.toList());
    }

    public ShipmentEndCountBean endShipment(UserSessionBean user, ShipmentBean shipmentBean) {

        shipmentBean.setChannelId(user.getSelChannelId());

        // 去除当前shipment中没有完整扫描的订单
        int canceledSkuCount = orderDetailService.removeSkuShipmentId(user.getSelChannelId(), shipmentBean.getId());

        // 更新shipment下的sku
        int succeedSkuCount = orderDetailService.updateOrderStatusWithShipmentId(user.getSelChannelId(), shipmentBean
                .getId(), STATUS_VALUE.PRODUCT_STATUS.SHIPPED);

        // 更新shipment
        VmsBtShipmentModel vmsBtShipmentModel = new VmsBtShipmentModel();
        BeanUtil.copy(shipmentBean, vmsBtShipmentModel);
        int succeedShipmentCount = shipmentService.save(vmsBtShipmentModel);

        return new ShipmentEndCountBean() {{
            setCanceledSkuCount(canceledSkuCount);
            setSucceedSkuCount(succeedSkuCount);
            setSucceedShipmentCount(succeedShipmentCount);
        }};
    }

    public List<SubOrderInfoBean> getScannedSkuList(UserSessionBean user, ShipmentBean shipment) {
        if (null == shipment) return new ArrayList<>();
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannelId());
            put("shipmentId", shipment.getId());
        }};

        Map<String, Object> pagedParams = MySqlPageHelper.build(params)
                .addSort("containerizing_time", Order.Direction.DESC)
                .toMap();

        return orderDetailService.selectOrderList(pagedParams).stream()
                .map(vmsBtOrderDetailModel -> {
                    SubOrderInfoBean subOrderInfoBean = new SubOrderInfoBean();
                    BeanUtil.copy(vmsBtOrderDetailModel, subOrderInfoBean);
                    return subOrderInfoBean;
                })
                .collect(Collectors.toList());
    }

    public int scanBarcodeInSku(UserSessionBean user, ScanInfo scanInfo) {
        // 确认shipment状态
        VmsBtShipmentModel shipment = shipmentService.select(scanInfo.getShipment().getId());
        if (!user.getSelChannelId().equals(shipment.getChannelId())) throw new BusinessException("8000030");
        if (!STATUS_VALUE.SHIPMENT_STATUS.OPEN.equals(shipment.getStatus())) throw new BusinessException(("8000025"));

        return orderDetailService.scanInSku(user.getSelChannelId(), user.getUserName(),
                scanInfo.getBarcode(), scanInfo.getShipment().getId());
    }
}
