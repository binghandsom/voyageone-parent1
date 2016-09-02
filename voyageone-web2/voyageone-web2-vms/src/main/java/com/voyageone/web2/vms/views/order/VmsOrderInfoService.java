package com.voyageone.web2.vms.views.order;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.util.BeanUtils;
import com.voyageone.common.util.MapUtil;
import com.voyageone.service.bean.vms.order.*;
import com.voyageone.service.bean.vms.shipment.ShipmentBean;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.vms.order.OrderDetailService;
import com.voyageone.service.impl.vms.shipment.ShipmentService;
import com.voyageone.service.model.vms.VmsBtOrderDetailModel;
import com.voyageone.service.model.vms.VmsBtShipmentModel;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.vms.bean.SortParamBean;
import com.voyageone.web2.vms.bean.VmsChannelSettingBean;
import com.voyageone.web2.vms.bean.order.OrderSearchInfoBean;
import com.voyageone.web2.vms.bean.order.ScanInfoBean;
import com.voyageone.web2.vms.views.common.VmsChannelConfigService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private VmsChannelConfigService vmsChannelConfigService;

    @Autowired
    public VmsOrderInfoService(OrderDetailService orderDetailService,
                               ShipmentService shipmentService,
                               VmsChannelConfigService vmsChannelConfigService) {
        this.orderDetailService = orderDetailService;
        this.shipmentService = shipmentService;
        this.vmsChannelConfigService = vmsChannelConfigService;
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
    public OrderInfoBean getOrderInfo(UserSessionBean user, OrderSearchInfoBean orderSearchInfoBean) {
        SortParamBean sortParamBean = new SortParamBean();
        if (null == orderSearchInfoBean.getSortParamBean()) {
            sortParamBean.setColumnName(CONSOLIDATION_ORDER_TIME);
            sortParamBean.setDirection((null != orderSearchInfoBean.getStatus()
                    && orderSearchInfoBean.getStatus().equals(STATUS_VALUE.PRODUCT_STATUS.OPEN) ?
                    Order.Direction.ASC : Order.Direction.DESC));
        } else {
            sortParamBean.setColumnName(orderSearchInfoBean.getSortParamBean().getColumnName());
            sortParamBean.setDirection(orderSearchInfoBean.getSortParamBean().getDirection());
        }
        OrderInfoBean orderInfoBean = new OrderInfoBean();
        orderInfoBean.setTotal(this.getTotalOrderNum(user, orderSearchInfoBean, sortParamBean));
        orderInfoBean.setOrderList(this.getOrders(user, orderSearchInfoBean, sortParamBean));
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
     * @param user                当前用户
     * @param orderSearchInfoBean 搜索信息
     * @return 拣货单Excel
     * @throws IOException
     */
    public byte[] getExcelBytes(UserSessionBean user, OrderSearchInfoBean orderSearchInfoBean) throws IOException {

        List<VmsBtOrderDetailModel> orderDetailList;
        // 搜索条件
        Map<String, Object> selectParams = new HashMap<>();
        selectParams.put("channelId", user.getSelChannelId());
        selectParams.put("status", orderSearchInfoBean.getStatus());
        selectParams.put("orderDateFrom", orderSearchInfoBean.getOrderDateFrom());
        selectParams.put("orderDateTo", orderSearchInfoBean.getOrderDateTo());

        if (null != orderSearchInfoBean.getSortParamBean()) {
            selectParams = MySqlPageHelper.build(selectParams)
                    .addSort(orderSearchInfoBean.getSortParamBean().getColumnName(),
                            orderSearchInfoBean.getSortParamBean().getDirection())
                    .toMap();
        } else {
            selectParams = MySqlPageHelper.build(selectParams)
                    .addSort("client_sku", Order.Direction.ASC)
                    .toMap();
        }

        orderDetailList = orderDetailService.selectOrderList(selectParams);

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

        Cell titleRowCell0 = titleRow.createCell(skuCellNumber);
        titleRowCell0.setCellValue("SKU");
        titleRowCell0.setCellStyle(titleRowCellStyle);

        Cell titleRowCell1 = titleRow.createCell(descriptionCellNumber);
        titleRowCell1.setCellValue("Name");
        titleRowCell1.setCellStyle(titleRowCellStyle);

        Cell titleRowCell2 = titleRow.createCell(orderIdCellNumber);
        titleRowCell2.setCellValue("OrderID");
        titleRowCell2.setCellStyle(titleRowCellStyle);
        // 动态属性
        List<String> attributes = vmsChannelConfigService.getChannelConfig(user).getAdditionalAttributes();
        if (attributes != null && attributes.size() > 0) {
            for(int i = 1;i <= attributes.size();i++) {
                Cell titleRowCellAttribute = titleRow.createCell(orderIdCellNumber + i);
                titleRowCellAttribute.setCellValue(attributes.get(i - 1));
                titleRowCellAttribute.setCellStyle(titleRowCellStyle);
            }
        }

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

            if (attributes != null && attributes.size() > 0) {
                for(int j = 1;j <= attributes.size();j++) {
                    Cell attributeCell = dataRow.createCell(orderIdCellNumber + j);
                    if (j == 1) {
                        attributeCell.setCellValue(vmsBtOrderDetailModel.getAttribute1() == null ? "" : vmsBtOrderDetailModel.getAttribute1());
                    } else if (j == 2) {
                        attributeCell.setCellValue(vmsBtOrderDetailModel.getAttribute2() == null ? "" : vmsBtOrderDetailModel.getAttribute2());
                    } else if (j == 3) {
                        attributeCell.setCellValue(vmsBtOrderDetailModel.getAttribute3() == null ? "" : vmsBtOrderDetailModel.getAttribute3());
                    }
                    attributeCell.setCellStyle(defaultRowCellStyle);
                }
            }
        }

        // 整理宽度
        sheet.autoSizeColumn(skuCellNumber);
        sheet.autoSizeColumn(descriptionCellNumber);
        sheet.autoSizeColumn(orderIdCellNumber);
        if (attributes != null && attributes.size() > 0) {
            for(int i = 1;i <= attributes.size();i++) {
                sheet.autoSizeColumn(orderIdCellNumber + i);
            }
        }

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
     * @param user                当前用户
     * @param orderSearchInfoBean 搜索条件
     * @return 订单列表
     */
    public List<AbstractSubOrderInfoBean> getOrders(UserSessionBean user, OrderSearchInfoBean orderSearchInfoBean, SortParamBean
            sortParamBean) {

        List<AbstractSubOrderInfoBean> orderList = new ArrayList<>();
        Map<String, Object> orderSearchParamsWithLimitAndSort = this.organizeOrderSearchParams(user, orderSearchInfoBean,
                sortParamBean);
        /*
         * 根据渠道配置设置
         */
        switch (vmsChannelConfigService.getChannelConfig(user).getVendorOperateType()) {

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
        VmsChannelSettingBean channelConfigs = vmsChannelConfigService.getChannelConfig(user);

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
                                BeanUtils.copy(vmsBtOrderDetailModel, orderInfoBean);
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
        VmsChannelSettingBean channelConfigs = vmsChannelConfigService.getChannelConfig(user);

        List<VmsBtOrderDetailModel> vmsBtOrderDetailModelList = orderDetailService.selectOrderList
                (orderSearchParamsWithLimitAndSort);
        return vmsBtOrderDetailModelList.stream()
                .map(vmsBtOrderDetailModel -> {
                    SubOrderInfoBean orderInfoBean = new SubOrderInfoBean();
                    BeanUtils.copy(vmsBtOrderDetailModel, orderInfoBean);
                    if (!STATUS_VALUE.SALE_PRICE_SHOW.SHOW.equals(channelConfigs.getSalePriceShow()))
                        orderInfoBean.setRetailPrice(BigDecimal.ZERO);
                    return orderInfoBean;
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据输入条件组出对应的搜索Map
     *
     * @param user                当前用户
     * @param orderSearchInfoBean 搜索条件
     * @return 搜索条件Map
     */
    private Map<String, Object> organizeOrderSearchParams(UserSessionBean user, OrderSearchInfoBean orderSearchInfoBean,
                                                          SortParamBean sortParamBean) {
        Map<String, Object> orderSearchParams;
        try {
            orderSearchParams = MapUtil.toMap(orderSearchInfoBean);
        } catch (IllegalAccessException e) {
            throw new BusinessException("8000037", e);
        }

        orderSearchParams.put("channelId", user.getSelChannel().getId());

        // limit sort条件
        Map<String, Object> orderSearchParamsWithLimitAndSort = MySqlPageHelper.build(orderSearchParams)
                .addSort(sortParamBean.getColumnName(), sortParamBean.getDirection())
                .limit(orderSearchInfoBean.getSize())
                .page(orderSearchInfoBean.getCurr())
                .toMap();

        $debug(orderSearchParamsWithLimitAndSort.toString());
        return orderSearchParamsWithLimitAndSort;
    }

    /**
     * 获取条件下的订单总数
     *
     * @param user                当前用户
     * @param orderSearchInfoBean 搜索条件
     * @return 订单总数
     */
    private long getTotalOrderNum(UserSessionBean user, OrderSearchInfoBean orderSearchInfoBean, SortParamBean sortParamBean) {
        if (STATUS_VALUE.VENDOR_OPERATE_TYPE.ORDER.equals(vmsChannelConfigService.getChannelConfig(user).getVendorOperateType())) {
            // 平台订单
            Map<String, Object> orderSearchParamsWithLimitAndSort =
                    organizeOrderSearchParams(user, orderSearchInfoBean, sortParamBean);
            return orderDetailService.getTotalOrderNum(orderSearchParamsWithLimitAndSort);
        } else if (STATUS_VALUE.VENDOR_OPERATE_TYPE.SKU.equals(vmsChannelConfigService.getChannelConfig(user).getVendorOperateType())) {
            // 大订单sku
            Map<String, Object> skuSearchParamsWithLimitAndSort =
                    organizeOrderSearchParams(user, orderSearchInfoBean, sortParamBean);
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

        // 查找对应OrderId中 是否有不可正常扫描的SKU
        Map<String, Object> checkParams = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannelId());
            put("consolidationOrderId", orderId);
        }};

        List<VmsBtOrderDetailModel> orderDetailList = orderDetailService.select(checkParams);

        // 已被其他人扫描
        long scannedToAnotherShipmentCount = orderDetailList.stream()
                .filter(vmsBtOrderDetailModel ->
                        null != vmsBtOrderDetailModel.getShipmentId()
                                && !shipment.getId().equals(vmsBtOrderDetailModel.getShipmentId()))
                .count();
        if (scannedToAnotherShipmentCount > 0) throw new BusinessException("8000023");

        // 已被取消
        long cancelledCount = orderDetailList.stream()
                .filter(vmsBtOrderDetailModel -> !vmsBtOrderDetailModel.getStatus().equals(STATUS_VALUE.PRODUCT_STATUS
                        .OPEN))
                .count();
        if (cancelledCount > 0) throw new BusinessException("8000024");

        return orderDetailService.getScannedSku(user.getSelChannelId(), shipment.getId(), orderId).stream()
                .map(vmsBtOrderDetailModel -> {
                    SubOrderInfoBean subOrderInfoBean = new SubOrderInfoBean();
                    BeanUtils.copy(vmsBtOrderDetailModel, subOrderInfoBean);
                    return subOrderInfoBean;
                })
                .collect(Collectors.toList());
    }

    /**
     * 扫barcode 将对应的sku加入shipment
     *
     * @param user         当前用户
     * @param scanInfoBean 扫描参数
     * @return 扫描影响的条数
     */
    public int scanBarcodeInOrder(UserSessionBean user, ScanInfoBean scanInfoBean) {

        ShipmentBean shipment = scanInfoBean.getShipment();
        String barcode = scanInfoBean.getBarcode();
        String orderId = scanInfoBean.getConsolidationOrderId();

        // 检查订单状态
        Map<String, Object> checkParams = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannelId());
            put("consolidationOrderId", scanInfoBean.getConsolidationOrderId());
        }};

        // 不属于该shipment(即已扫入其他shipment)
        List<VmsBtOrderDetailModel> skuList = orderDetailService.select(checkParams);
        long invalidCount = skuList.stream()
                .filter(vmsBtOrderDetailModel -> (null != vmsBtOrderDetailModel.getShipmentId()
                        && !vmsBtOrderDetailModel.getShipmentId().equals(shipment.getId())))
                .count();
        if (invalidCount > 0) throw new BusinessException("8000023");

        // 检查状态不正常的(非OPEN)
        List<VmsBtOrderDetailModel> invalidStatusList = skuList.stream()
                .filter(vmsBtOrderDetailModel -> !vmsBtOrderDetailModel.getStatus().equals(STATUS_VALUE.PRODUCT_STATUS
                        .OPEN))
                .collect(Collectors.toList());
        if (invalidStatusList.size() > 0) {

            // 已被取消
            long cancelledCount = invalidStatusList.stream()
                    .filter(vmsBtOrderDetailModel -> vmsBtOrderDetailModel.getStatus().equals(STATUS_VALUE.PRODUCT_STATUS
                            .CANCEL))
                    .count();

            // 已经完整扫描
            long packagedCount = invalidStatusList.stream()
                    .filter(vmsBtOrderDetailModel -> vmsBtOrderDetailModel.getStatus().equals(STATUS_VALUE.PRODUCT_STATUS
                            .PACKAGE))
                    .count();

            if (cancelledCount > 0) throw new BusinessException("8000024");
            if (packagedCount > 0) throw new BusinessException("8000034");
            // 理论上应该只有以上两种情况 最后加一个抛出确保今后状态加了之类的不会出错=。=
            throw new BusinessException("8000035");
        }

        // 状态不为OPEN
        VmsBtShipmentModel dbShipment = shipmentService.select(shipment.getId());
        if (!dbShipment.getStatus().equals(STATUS_VALUE.SHIPMENT_STATUS.OPEN)) throw new BusinessException("8000025");

        return orderDetailService.scanInOrder(user.getSelChannelId(), user.getUserName(),
                barcode, orderId, shipment.getId());
    }

    /**
     * 确认当期订单是否完整扫描 同时相应更新sku状态
     *
     * @param user         当前用户
     * @param scanInfoBean 扫描参数
     * @return 是否完整扫描
     */
    public boolean orderScanFinished(UserSessionBean user, ScanInfoBean scanInfoBean) {

        ShipmentBean shipment = scanInfoBean.getShipment();
        String orderId = scanInfoBean.getConsolidationOrderId();

        // 检查订单状态
        Map<String, Object> checkParams = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannelId());
            put("consolidationOrderId", scanInfoBean.getConsolidationOrderId());
        }};

        // 检查当前订单是否全部扫描完毕
        List<VmsBtOrderDetailModel> currentOrderInfo = orderDetailService.select(checkParams);
        long scannedCount = currentOrderInfo.stream()
                .filter(vmsBtOrderDetailModel -> null != vmsBtOrderDetailModel.getShipmentId()
                        && vmsBtOrderDetailModel.getShipmentId().equals(shipment.getId())
                        && vmsBtOrderDetailModel.getStatus().equals(STATUS_VALUE.PRODUCT_STATUS.OPEN))
                .count();
        return scannedCount == currentOrderInfo.size();
    }

    /**
     * 确认打包订单
     *
     * @param user         当前用户
     * @param scanInfoBean 扫描条件
     * @return 更新条数
     */
    public int finishOrderScanning(UserSessionBean user, ScanInfoBean scanInfoBean) {
        ShipmentBean shipment = scanInfoBean.getShipment();
        String orderId = scanInfoBean.getConsolidationOrderId();

        // 检查订单状态
        Map<String, Object> checkParams = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannelId());
            put("consolidationOrderId", scanInfoBean.getConsolidationOrderId());
        }};

        // 检查当前订单是否全部扫描完毕
        List<VmsBtOrderDetailModel> currentOrderInfo = orderDetailService.select(checkParams);
        long scannedCount = currentOrderInfo.stream()
                .filter(vmsBtOrderDetailModel -> null != vmsBtOrderDetailModel.getShipmentId()
                        && vmsBtOrderDetailModel.getShipmentId().equals(shipment.getId())
                        && vmsBtOrderDetailModel.getStatus().equals(STATUS_VALUE.PRODUCT_STATUS.OPEN))
                .count();
        if (scannedCount == currentOrderInfo.size())
            return orderDetailService.updateOrderStatus(user.getSelChannelId(), scanInfoBean.getConsolidationOrderId()
                    , STATUS_VALUE.PRODUCT_STATUS.PACKAGE, user.getUserName());
        throw new BusinessException("8000038");
    }

    /**
     * 取消扫描
     *
     * @param user         当前用户
     * @param scanInfoBean 扫描信息
     * @return 更新条数
     */
    public int revertScanning(UserSessionBean user, ScanInfoBean scanInfoBean) {
        // TODO: 16-8-29 检查当前订单状态 vantis
        ShipmentBean shipment = scanInfoBean.getShipment();
        String orderId = scanInfoBean.getConsolidationOrderId();
        // 检查订单状态
        Map<String, Object> checkParams = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannelId());
            put("consolidationOrderId", scanInfoBean.getConsolidationOrderId());
        }};

        // 检查当前订单是否全部扫描完毕
        List<VmsBtOrderDetailModel> currentOrderInfo = orderDetailService.select(checkParams);
        long scannedCount = currentOrderInfo.stream()
                .filter(vmsBtOrderDetailModel -> null != vmsBtOrderDetailModel.getShipmentId()
                        && vmsBtOrderDetailModel.getShipmentId().equals(shipment.getId())
                        && !vmsBtOrderDetailModel.getStatus().equals(STATUS_VALUE.PRODUCT_STATUS.OPEN))
                .count();
        if (scannedCount == 0)
            return orderDetailService.removeSkuOrderId(user.getSelChannelId(), orderId);
        // TODO: 16-8-29 取消订单内sku的扫描状态 vantis
        throw new BusinessException("8000039");
    }

    /**
     * 确认shipment中的订单信息
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
        List<VmsBtOrderDetailModel> skusInshipment = orderDetailService.select(params);
        return skusInshipment.stream()
                .filter(vmsBtOrderDetailModel -> !STATUS_VALUE.PRODUCT_STATUS.PACKAGE.equals(vmsBtOrderDetailModel
                        .getStatus()))
                .map(VmsBtOrderDetailModel::getConsolidationOrderId)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 获取已扫描过的SKU信息
     *
     * @param user     当前用户
     * @param shipment 当前shipment
     * @return 已扫描过的SKU列表
     */
    public List<SubOrderInfoBean> getScannedSkuList(UserSessionBean user, ShipmentBean shipment) {
        if (null == shipment) return new ArrayList<>();
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannelId());
            put("shipmentId", shipment.getId());
        }};

        if (STATUS_VALUE.VENDOR_OPERATE_TYPE.SKU.equals(vmsChannelConfigService.getChannelConfig(user)
                .getVendorOperateType()))
            params = MySqlPageHelper.build(params)
                    .addSort("containerizing_time", Order.Direction.DESC)
                    .toMap();
        else
            params = MySqlPageHelper.build(params)
                    .addSort("consolidation_order_id", Order.Direction.ASC)
                    .addSort("containerizing_time", Order.Direction.ASC)
                    .toMap();

        return orderDetailService.selectOrderList(params).stream()
                .map(vmsBtOrderDetailModel -> {
                    SubOrderInfoBean subOrderInfoBean = new SubOrderInfoBean();
                    BeanUtils.copy(vmsBtOrderDetailModel, subOrderInfoBean);
                    return subOrderInfoBean;
                })
                .collect(Collectors.toList());
    }

    /**
     * sku级别扫描barcode
     *
     * @param user         当前用户
     * @param scanInfoBean 扫描信息
     * @return 扫码影响结果
     */
    public int scanBarcodeInSku(UserSessionBean user, ScanInfoBean scanInfoBean) {
        // 确认shipment状态
        VmsBtShipmentModel shipment = shipmentService.select(scanInfoBean.getShipment().getId());
        if (!user.getSelChannelId().equals(shipment.getChannelId())) throw new BusinessException("8000030");
        // SKU级别扫描的情况下，加上即使状态是shipped的shipment也能够继续扫描
        // if (!STATUS_VALUE.SHIPMENT_STATUS.OPEN.equals(shipment.getStatus())) throw new BusinessException(("8000025"));

        return orderDetailService.scanInSku(user.getSelChannelId(), user.getUserName(),
                scanInfoBean.getBarcode(), scanInfoBean.getShipment().getId(), shipment.getStatus());
    }

    /**
     * 获取尚未扫描的SKU列表
     *
     * @param user     当前用户
     * @param shipment 当前shipment
     * @param orderId  当前orderId
     * @return 尚未扫描的SKU列表
     */
    public List<SubOrderInfoBean> getWaitingSkuList(UserSessionBean user, ShipmentBean shipment,
                                                    String orderId) {

        // 查找订单内容
        Map<String, Object> checkParams = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannelId());
            put("consolidationOrderId", orderId);
        }};

        List<VmsBtOrderDetailModel> orderDetailList = orderDetailService.select(checkParams);

        return orderDetailList.stream()
                .filter(vmsBtOrderDetailModel -> null == vmsBtOrderDetailModel.getShipmentId())
                .map(vmsBtOrderDetailModel -> {
                    SubOrderInfoBean subOrderInfoBean = new SubOrderInfoBean();
                    BeanUtils.copy(vmsBtOrderDetailModel, subOrderInfoBean);
                    return subOrderInfoBean;
                })
                .collect(Collectors.toList());
    }

    /**
     * 统计已经扫描好的订单数
     *
     * @param user       当前用户
     * @param shipmentId 当前shipment的ID
     * @return 统计订单数
     */
    public Long countScannedOrder(UserSessionBean user, Integer shipmentId) {
        if (null == shipmentId || shipmentId == 0) return 0L;
        Map<String, Object> searchParams = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannelId());
            put("shipmentId", shipmentId);
        }};
        return orderDetailService.select(searchParams).stream()
                .filter(vmsBtOrderDetailModel -> STATUS_VALUE.PRODUCT_STATUS.PACKAGE.equals(vmsBtOrderDetailModel
                        .getStatus()))
                .map(VmsBtOrderDetailModel::getConsolidationOrderId)
                .distinct()
                .count();
    }
}
