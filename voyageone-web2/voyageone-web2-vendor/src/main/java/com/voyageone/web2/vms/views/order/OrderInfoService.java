package com.voyageone.web2.vms.views.order;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.VmsChannelConfigs;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.beans.VmsChannelConfigBean;
import com.voyageone.common.util.MapUtil;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.vms.order.VmsOrderDetailService;
import com.voyageone.service.impl.vms.shipment.VmsShipmentService;
import com.voyageone.service.model.vms.VmsBtOrderDetailModel;
import com.voyageone.service.model.vms.VmsBtShipmentModel;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.vms.VmsConstants;
import com.voyageone.web2.vms.VmsConstants.ChannelConfig;
import com.voyageone.web2.vms.VmsConstants.STATUS_VALUE;
import com.voyageone.web2.vms.VmsConstants.TYPE_ID;
import com.voyageone.web2.vms.bean.SortParam;
import com.voyageone.web2.vms.bean.VmsChannelSettings;
import com.voyageone.web2.vms.bean.order.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * order info service of controller
 * Created by vantis on 16-7-6.
 */
@Service
public class OrderInfoService extends BaseService {

    private VmsOrderDetailService vmsOrderDetailService;
    private VmsShipmentService vmsShipmentService;

    @Autowired
    public OrderInfoService(VmsOrderDetailService vmsOrderDetailService, VmsShipmentService vmsShipmentService) {
        this.vmsOrderDetailService = vmsOrderDetailService;
        this.vmsShipmentService = vmsShipmentService;
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
     * 获取当前用户/Channel已建立的shipment
     *
     * @param user 当前用户
     * @return shipmentBean
     */
    public VmsBtShipmentModel getCurrentShipment(UserSessionBean user) {

        Map<String, Object> shipmentSearchParams = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannel().getId());
            put("status", STATUS_VALUE.SHIPMENT_STATUS.OPEN);
        }};
        return vmsShipmentService.select(shipmentSearchParams);
    }

    /**
     * 读取channel相应配置
     *
     * @param user 当前用户
     * @return 当前用户所选择channel的配置
     */
    public VmsChannelSettings getChannelConfigs(UserSessionBean user) {

        VmsChannelConfigBean vmsChannelConfigBean = VmsChannelConfigs.getConfigBean(user.getSelChannelId(),
                ChannelConfig.VENDOR_OPERATE_TYPE, ChannelConfig.COMMON_CONFIG_CODE);

        // Missing required configures for this channel, please contact with the system administrator for help.
        if (null == vmsChannelConfigBean) throw new BusinessException("8000019");

        VmsChannelSettings vmsChannelSettings = new VmsChannelSettings();
        vmsChannelSettings.setVendorOperateType(vmsChannelConfigBean.getConfigValue1());
        return vmsChannelSettings;
    }

    /**
     * 默认条件获取订单信息(Open)
     *
     * @param user 当前用户
     * @return Order信息内容
     */
    public OrderInfoBean getOrderInfo(UserSessionBean user, OrderSearchInfo orderSearchInfo) {
        SortParam sortParam = new SortParam();
        sortParam.setColumnName(VmsConstants.CONSOLIDATION_ORDER_TIME);
        sortParam.setDirection((null != orderSearchInfo.getStatus()
                && orderSearchInfo.getStatus() == STATUS_VALUE.PRODUCT_STATUS.OPEN) ?
                Order.Direction.ASC : Order.Direction.DESC);
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

        Map<String, Object> cancelOrderParam = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannel().getId());
            put("status", STATUS_VALUE.PRODUCT_STATUS.CANCEL);
            put("orderId", item.getOrderId());
            put("modifier", user.getUserName());
        }};

        Map<String, Object> checkParam = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannel().getId());
            put("orderId", item.getOrderId());
        }};

        // TODO: 16-7-12 订单当前状态测试 vantis
        List<VmsBtOrderDetailModel> invalidOrderModelList = vmsOrderDetailService.selectOrderList(checkParam)
                .stream()
                .filter(vmsBtOrderDetailModel -> !vmsBtOrderDetailModel.getStatus().equals(String.valueOf(STATUS_VALUE
                        .PRODUCT_STATUS.OPEN)))
                .collect(Collectors.toList());

        if (null != invalidOrderModelList && invalidOrderModelList.size() > 0)
            throw new BusinessException("8000020");

        // 检测通过 进行状态变更
        cancelOrderParam.put("status", STATUS_VALUE.PRODUCT_STATUS.CANCEL);
        return vmsOrderDetailService.updateOrderStatus(cancelOrderParam);
    }

    /**
     * sku级别的取消
     *
     * @param user 当前用户
     * @param item 需要取消的对象
     * @return 取消条数
     */
    public int cancelSku(UserSessionBean user, SubOrderInfoBean item) {

        Map<String, Object> cancelSkuParam = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannel().getId());
            put("reservationId", item.getReservationId());
            put("status", STATUS_VALUE.PRODUCT_STATUS.CANCEL);
            put("modifier", user.getUserName());
        }};

        Map<String, Object> checkParam = new HashMap<String, Object>() {{
            put("channelId", user.getSelChannel().getId());
            put("orderId", item.getOrderId());
        }};

        List<VmsBtOrderDetailModel> invalidOrderModelList = vmsOrderDetailService.selectOrderList(checkParam)
                .stream()
                .filter(vmsBtOrderDetailModel -> !vmsBtOrderDetailModel.getStatus().equals(String.valueOf(STATUS_VALUE
                        .PRODUCT_STATUS.OPEN)))
                .collect(Collectors.toList());

        if (null != invalidOrderModelList && invalidOrderModelList.size() > 0)
            throw new BusinessException("8000020");

        return vmsOrderDetailService.updateOrderStatus(cancelSkuParam);
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
                vmsOrderDetailService.select(sortedSelectParams);
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
        if (VmsConstants.PICKING_LIST_ORDER_TYPE.ORDER.equals(downloadInfo.getOrderType())) {
            skuCellNumber = 2;
            orderIdCellNumber = 0;
        }

        Cell titleRowCell0 = titleRow.createCell(skuCellNumber);
        titleRowCell0.setCellValue("SKU");
        titleRowCell0.setCellStyle(titleRowCellStyle);

        Cell titleRowCell1 = titleRow.createCell(descriptionCellNumber);
        titleRowCell1.setCellValue("Description");
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
            descriptionCell.setCellValue(vmsBtOrderDetailModel.getDescription());
            descriptionCell.setCellStyle(defaultRowCellStyle);

            Cell orderIdCell = dataRow.createCell(orderIdCellNumber);
            orderIdCell.setCellValue(vmsBtOrderDetailModel.getOrderId());
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
        Map<String, Object> orderSearchParamsWithLimitAndSort = organizeOrderSearchParams(user, orderSearchInfo, sortParam);
        /**
         * 根据渠道配置设置
         */
        switch (this.getChannelConfigs(user).getVendorOperateType()) {

            // sku级的订单获取
            case STATUS_VALUE.VENDOR_OPERATE_TYPE.SKU: {
                return this.getSkuOrderInfoBean(orderSearchParamsWithLimitAndSort);
            }

            // 平台订单级获取
            case STATUS_VALUE.VENDOR_OPERATE_TYPE.ORDER: {

                return this.getPlatformOrderInfoBeen(orderSearchParamsWithLimitAndSort);
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
    private List<AbstractSubOrderInfoBean> getPlatformOrderInfoBeen(Map<String, Object> orderSearchParamsWithLimitAndSort) {
        return vmsOrderDetailService.selectPlatformOrderIdList(orderSearchParamsWithLimitAndSort).stream()
                .map(orderId -> {

                    // 获取平台订单id下的所有的sku
                    List<VmsBtOrderDetailModel> vmsBtOrderDetailModelList = vmsOrderDetailService
                            .selectOrderList(new HashMap<String, Object>() {{
                                put("orderId", orderId);
                            }});

                    // 按照第一个sku初始化平台订单id内容
                    PlatformSubOrderInfoBean platformOrderInfoBean = new PlatformSubOrderInfoBean();
                    platformOrderInfoBean.setOrderId(vmsBtOrderDetailModelList.get(0).getOrderId());
                    platformOrderInfoBean.setOrderDateTime(vmsBtOrderDetailModelList.get(0).getOrderTime());
                    platformOrderInfoBean.setStatus(vmsBtOrderDetailModelList.get(0).getStatus());

                    // 将订单下的sku信息录入
                    vmsBtOrderDetailModelList.stream()
                            .map(vmsBtOrderDetailModel -> new SubOrderInfoBean() {{

                                // 整理格式
                                setReservationId(vmsBtOrderDetailModel.getReservationId());
                                setOrderId(vmsBtOrderDetailModel.getOrderId());
                                setOrderDateTime(vmsBtOrderDetailModel.getOrderTime());
                                setDesc(vmsBtOrderDetailModel.getDescription());
                                setPrice(vmsBtOrderDetailModel.getClientRetailPrice());
                                setSku(vmsBtOrderDetailModel.getClientSku());
                                setStatus(vmsBtOrderDetailModel.getStatus());
                            }})
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
    private List<AbstractSubOrderInfoBean> getSkuOrderInfoBean(Map<String, Object> orderSearchParamsWithLimitAndSort) {
        List<VmsBtOrderDetailModel> vmsBtOrderDetailModelList = vmsOrderDetailService.selectOrderList
                (orderSearchParamsWithLimitAndSort);
        return vmsBtOrderDetailModelList.stream()
                .map(vmsBtOrderDetailModel -> {
                    SubOrderInfoBean orderInfoBean = new SubOrderInfoBean();
                    orderInfoBean.setReservationId(vmsBtOrderDetailModel.getReservationId());
                    orderInfoBean.setOrderId(vmsBtOrderDetailModel.getOrderId());
                    orderInfoBean.setSku(vmsBtOrderDetailModel.getClientSku());
                    orderInfoBean.setDesc(vmsBtOrderDetailModel.getDescription());
                    orderInfoBean.setOrderDateTime(vmsBtOrderDetailModel.getOrderTime());
                    orderInfoBean.setPrice(vmsBtOrderDetailModel.getClientRetailPrice());
                    orderInfoBean.setStatus(vmsBtOrderDetailModel.getStatus());
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
        if (STATUS_VALUE.VENDOR_OPERATE_TYPE.ORDER.equals(this.getChannelConfigs(user).getVendorOperateType())) {
            // 平台订单
            Map<String, Object> orderSearchParamsWithLimitAndSort =
                    organizeOrderSearchParams(user, orderSearchInfo, sortParam);
            return vmsOrderDetailService.getTotalOrderNum(orderSearchParamsWithLimitAndSort);
        } else if (STATUS_VALUE.VENDOR_OPERATE_TYPE.SKU.equals(this.getChannelConfigs(user).getVendorOperateType())) {
            // 大订单sku
            Map<String, Object> skuSearchParamsWithLimitAndSort =
                    organizeOrderSearchParams(user, orderSearchInfo, sortParam);
            return vmsOrderDetailService.getTotalSkuNum(skuSearchParamsWithLimitAndSort);
        } else return 0;
    }
}
