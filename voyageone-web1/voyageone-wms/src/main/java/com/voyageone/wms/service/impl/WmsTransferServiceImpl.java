package com.voyageone.wms.service.impl;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.base.exception.SystemException;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.StoreConfigs;
import com.voyageone.common.configs.Type;
import com.voyageone.common.configs.beans.MasterInfoBean;
import com.voyageone.common.configs.beans.StoreBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.CoreConstants;
import com.voyageone.core.MessageConstants.ComMsg;
import com.voyageone.core.modelbean.ChannelStoreBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.wms.WmsCodeConstants.TransferStatus;
import com.voyageone.wms.WmsCodeConstants.TransferType;
import com.voyageone.wms.WmsConstants;
import com.voyageone.wms.WmsMsgConstants;
import com.voyageone.wms.WmsMsgConstants.TransferMsg;
import com.voyageone.wms.dao.ClientShipmentDao;
import com.voyageone.wms.dao.ClientSkuDao;
import com.voyageone.wms.dao.ItemDao;
import com.voyageone.wms.dao.StoreDao;
import com.voyageone.wms.dao.TransferDao;
import com.voyageone.wms.formbean.ClientShipmentCompareBean;
import com.voyageone.wms.formbean.FormStocktake;
import com.voyageone.wms.formbean.TransferFormBean;
import com.voyageone.wms.formbean.TransferMapBean;
import com.voyageone.wms.modelbean.*;
import com.voyageone.wms.service.WmsTransferService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Tester on 4/30/2015.
 *
 * @author Jonas
 */
@Service
public class WmsTransferServiceImpl implements WmsTransferService {

    private static Log logger = LogFactory.getLog(WmsTransferServiceImpl.class);

    @Autowired
    private TransferDao transferDao;

    @Autowired
    private StoreDao storeDao;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private ClientShipmentDao clientShipmentDao;

    @Autowired
    private ClientSkuDao clientSkuDao;

    @Autowired
    private HttpServletRequest request;

    // 第三方barcode输入框，输入提示
    private String popupInput = "popupInput";
    // 第三方barcode输入框，ItemCode,Size 重复输入
    private String repeatInput = "repeatInput";

    /**
     * 【Transfer画面】初始化
     *
     * @param user 用户登录信息
     * @return Map 画面初始化项目
     */
    @Override
    public Map<String, Object> doListInit(UserSessionBean user) {
        Map<String, Object> resultMap = new HashMap<>();

        //获取Transfer状态
        List<MasterInfoBean> statusList = Type.getMasterInfoFromId(TypeConfigEnums.MastType.transferStatus.getId(), false);

        resultMap.put("status", statusList);

        //获取用户仓库
        ArrayList<ChannelStoreBean> channelStoreList = new ArrayList<>();

        ChannelStoreBean channelStoreBean = new ChannelStoreBean();
        channelStoreBean.setStore_id(0);
        channelStoreBean.setStore_name("ALL");

        channelStoreList.add(channelStoreBean);

        // 排除品牌方管理库存的仓库
        for (ChannelStoreBean storeBean : user.getCompanyRealStoreList() ) {
            if (StoreConfigs.getStore(new Long(storeBean.getStore_id())).getInventory_manager().equals(StoreConfigEnums.Manager.YES.getId())) {
                channelStoreList.add(storeBean);
            }
        }

        resultMap.put("storeList", channelStoreList);

        // 获取开始日期（当前日期的一个月前）
        String date_from = DateTimeUtil.parseStr(DateTimeUtil.getLocalTime(DateTimeUtil.addMonths(DateTimeUtil.getDate(), -1), user.getTimeZone()), DateTimeUtil.DEFAULT_DATE_FORMAT);
        resultMap.put("fromDate", date_from);

        // 获取结束日期（当前日期）
        String date_to = DateTimeUtil.parseStr(DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), user.getTimeZone()), DateTimeUtil.DEFAULT_DATE_FORMAT);
        resultMap.put("toDate", date_to);

        return resultMap;
    }

    /**
     * 根据条件搜索 Transfer
     *
     * @param po               PO 单号
     * @param status           Transfer 的状态
     * @param from             时间的起始范围
     * @param to               时间的结束范围
     * @param channelStoreList 可用的渠道
     * @param page             当前页数
     * @param size             每页行数
     * @return List
     */
    @Override
    public List<TransferFormBean> search(String po, int Store, String status, String from, String to, List<ChannelStoreBean> channelStoreList, int page, int size, UserSessionBean user) {
        List<TransferFormBean> beans = transferDao.search(po, Store, status, from, to, channelStoreList, page * size, size);

        for (TransferFormBean bean: beans) bean.setModified_local(user.getTimeZone());

        return beans;
    }

    /**
     * 同条件下的搜索总数
     *
     * @param po     PO 单号
     * @param status Transfer 的状态
     * @param from   时间的起始范围
     * @param to     时间的结束范围
     * @return int
     */
    @Override
    public int getSearchCount(String po, int Store, String status, String from, String to, List<ChannelStoreBean> channelStoreList) {
        return transferDao.searchCount(po, Store, status, from, to, channelStoreList);
    }

    /**
     * 删除一个 Transfer，同时删除 Package 和 Item，以及 Mapping
     *
     * @param transferId Transfer ID
     * @param modified   最后的修改时间
     * @return 是否完成删除
     */
    @Transactional
    @Override
    public boolean delete(long transferId, String modified) {
        TransferBean transfer = get(transferId);

        // 状态验证在 SQL 中
        boolean isSuccess = transferDao.deleteTransfer(transferId, modified) > 0;

        if (isSuccess) {
            // 如果成功删除了 Transfer，则强制删除 Detail 和 Items
            transferDao.deleteDetails(transferId);

            transferDao.deleteTransferItems(transferId);

            if (isIn(transfer)) {

                transferDao.deleteMapByIn(transferId, null);

            } else if (isOut(transfer)) {

                transferDao.deleteMapByOut(transferId, null);
            }
        }

        return isSuccess;
    }

    /**
     * 获取 Transfer
     *
     * @param transferId Transfer ID
     * @return TransferBean
     */
    @Override
    public TransferBean get(long transferId) {
        return transferDao.getTransfer(transferId);
    }

    /**
     * 插入一个 Transfer
     *
     * @param mapBean TransferMapBean
     * @param user    当前用户
     * @return TransferBean
     */
    @Override
    public TransferBean insert(TransferMapBean mapBean, UserSessionBean user) {
        TransferBean transfer = mapBean.getTransfer();
        TransferBean context = getContext(mapBean);

        setMainStoreInfo(transfer);
        setTransferForInsert(transfer);

        transfer.setOrigin_id(0);
        transfer.setActive(true);

        String now = DateTimeUtil.getNow();

        transfer.setCreated(now);
        transfer.setCreater(user.getUserName());
        transfer.setModified(now);
        transfer.setModifier(user.getUserName());

        if (transferDao.insertTransferInfo(transfer) < 1)
            return null;

        if (context != null) {
            if (insertMap(transfer, context, user) < 1)
                return null;
        }

        return transferDao.getByName(transfer.getTransfer_name(), transfer.getModified());
    }

    /**
     * 获取 Transfer 下的所有 Package
     *
     * @param transfer_id Transfer ID
     * @return List
     */
    @Override
    public List<TransferDetailBean> getPackages(long transfer_id) {
        List<TransferDetailBean> beans = transferDao.getPackages(transfer_id);

        for (TransferDetailBean bean: beans) bean.setModified_local(getUser().getTimeZone());

        return beans;
    }

    /**
     * 根据名字和 Transfer ID 匹配获取一个 Package
     *
     * @param transferId  Transfer ID
     * @param packageName Package 名称
     * @return TransferDetailBean
     */
    @Override
    public TransferDetailBean getPackage(long transferId, String packageName) {

        TransferDetailBean transferDetailBean = transferDao.getPackage(transferId, packageName);

        transferDetailBean.setModified_local(getUserTimeZone());

        return transferDetailBean;
    }

    /**
     * 在 Transfer 下创建一个 Package
     *
     * @param transferId  Transfer ID
     * @param packageName Package 名称
     * @param user        当前用户
     * @return TransferDetailBean
     */
    @Override
    public TransferDetailBean createPackage(long transferId, String packageName, UserSessionBean user) {
        // 获取目标 Transfer，新的 Package 创建在该对象之下
        TransferBean transferBean = get(transferId);

        // 判断是否用户输入了固定的名称，如果没有的话，自动生成一个名称
        if (StringUtils.isEmpty(packageName))
            packageName = createPackageName(transferBean);

        // 判断当前得到的名称，是否已经在 Transfer 之下存在了。如果已存在则抛出业务异常。一般自动生成的不会重复，但不排除特别情况
        if (transferDao.detailHasName(transferBean.getTransfer_id(), packageName)) {
            throw new BusinessException(TransferMsg.PACKAGE_NAME_EXISTS, packageName);
        }

        TransferDetailBean detailBean = new TransferDetailBean();

        detailBean.setTransfer_id(transferId);
        detailBean.setTransfer_package_name(packageName);
        detailBean.setPackage_status(TransferStatus.Open);

        detailBean.setActive(true);

        String now = DateTimeUtil.getNow();

        detailBean.setCreated(now);
        detailBean.setCreater(user.getUserName());
        detailBean.setModified(now);
        detailBean.setModifier(user.getUserName());

        if (transferDao.insertTransferDetail(detailBean) > 0)
            return getPackage(transferId, packageName);
        else
            throw new BusinessException(ComMsg.UPDATE_BY_OTHER);
    }

    /**
     * 删除 Package，并删除其所有 Item
     *
     * @param package_id Package ID
     * @param modified   最后修改的时间
     * @return 是否删除成功
     */
    @Override
    public boolean deletePackage(long package_id, String modified) {
        // 状态验证直接写死在 SQL 中
        if (transferDao.deleteDetail(package_id, modified) < 1)
            return false;

        transferDao.deletePackageItems(package_id);

        return true;
    }

    /**
     * 重新打开 Package
     *
     * @param package_id Package ID
     * @param modified   最后修改的时间
     * @return 是否打开成功
     */
    @Override
    public boolean reOpenPackage(long package_id, String modified) {
        // 状态验证直接写死在 SQL 中
        if (transferDao.reOpenPackage(package_id, modified) < 1)
            return false;

        return true;
    }

    /**
     * 增加（或减少，数量为负则减少）一个 Item 的数量
     *
     * @param package_id int
     * @param barcode    String
     * @param num        int
     * @param itemCode  String
     * @param color  String
     * @param size        String
     * @param user       UserSessionBean
     * @return 商品的 SKU
     */
    @Override
    public String addItem(long package_id, String barcode, int num, String itemCode, String color, String size, UserSessionBean user) {

        // 获取容器
        TransferDetailBean detailBean = transferDao.getPackage(package_id);

        // 已经关闭了
        if (TransferStatus.Close.equals(detailBean.getPackage_status())) {
            throw new BusinessException(TransferMsg.PACKAGE_ALREADY_CLOSED);
        }

        // 尝试获取“容器内，相同 Barcode 的 Item”，如果已存在，则直接操作，更改数量。如果不存在，则新建
        TransferItemBean item = transferDao.getItem(package_id, barcode);

        TransferBean transfer = get(detailBean.getTransfer_id());

        if (item == null) {
            // 如果数据库没有，并且 num 为添加操作，则创建一个
            if (num > 0) {
                item = createItem(transfer, detailBean, barcode, num, itemCode, color, size, user);

                if (popupInput.equals(item.getTransfer_sku()) || repeatInput.equals(item.getTransfer_sku())) {
                    return item.getTransfer_sku();
                } else {
                    if (insertItem(item, itemCode, color, size, transfer))
                        return item.getTransfer_sku();
                    else
                        throw new BusinessException(ComMsg.UPDATE_BY_OTHER);
                }
            }
            // 否则，为减数量操作，而正好数据库没有，所以直接返回成功
            return null;
        }

        // 进行数量计算操作
        num = item.getTransfer_qty() + num;

        // 如果数量全部抵消。则删除这个 Item 行
        if (num <= 0) {
            if (transferDao.deleteItem(item.getTransfer_item_id()) > 0)
                return item.getTransfer_sku();
            else
                throw new BusinessException(ComMsg.UPDATE_BY_OTHER);
        }

        // 更新两个数量
        item.setTransfer_qty(num);
        setItemCalcQty(transfer, item);

        // 更新其他信息
        String lastModified = item.getModified();
        item.setModified(DateTimeUtil.getNow());
        item.setModifier(user.getUserName());

        if (transferDao.updateItemQty(item, lastModified) > 0)
            return item.getTransfer_sku();
        else
            throw new BusinessException(ComMsg.UPDATE_BY_OTHER);
    }

    /**
     * 入出库项目追加（wms_bt_transfer_item，wms_bt_client_sku）
     *
     * @param item 入出库项目
     * @param itemCode
     * @param color
     * @param size
     * @param transfer  String
     * @return 追加结果
     */
    private boolean insertItem(TransferItemBean item, String itemCode, String color, String size, TransferBean transfer) {
        boolean ret = true;

        // 第三方ItemCode,color,Size 输入的场合
        if (!StringUtils.isEmpty(itemCode)) {
            // wms_bt_client_sku追加
            String to_store_channel_id;

            if (isOut(transfer)) {
                to_store_channel_id = storeDao.getChannel_id(transfer.getTransfer_to_store());
            } else {
                to_store_channel_id = transfer.getOrder_channel_id();
            }

            ClientSkuBean clientSkuBean = new ClientSkuBean();
            clientSkuBean.setOrder_channel_id(to_store_channel_id);
            clientSkuBean.setBarcode(item.getTransfer_barcode());
            clientSkuBean.setItem_code(itemCode);
            clientSkuBean.setColor(color);
            clientSkuBean.setSize(size);
            clientSkuBean.setActive(1);
            clientSkuBean.setCreater(item.getCreater());
            clientSkuBean.setModifier(item.getModifier());

            int recCount = clientSkuDao.insertItem(clientSkuBean);
            if (recCount == 0) {
                ret = false;
            }
        }

        if (ret) {
            // wms_bt_transfer_item 追加
            int recCount = transferDao.insertItem(item);
            if (recCount == 0) {
                ret = false;
            }
        }

        return ret;
    }

    /**
     * 关闭一个 Package，只更新状态
     *
     * @param package_id Package ID
     * @param modified   最后的修改时间
     * @param user       当前用户
     * @return 是否成功
     */
    @Override
    public boolean closePackage(long package_id, String modified, UserSessionBean user) {
        if (transferDao.getItemCountInPackage(package_id) < 1)
            throw new BusinessException(TransferMsg.NO_PACKAGE_ITEM_EXISTS);

        TransferDetailBean detail = transferDao.getPackage(package_id);

        detail.setTransfer_package_id(package_id);

        detail.setPackage_status(TransferStatus.Close);

        detail.setModified(DateTimeUtil.getNow());
        detail.setModifier(user.getUserName());

        return transferDao.updatePackageStatus(detail, modified) > 0;
    }

    /**
     * 保存 Transfer
     *
     * @param map  TransferMapBean
     * @param user 当前用户
     * @return 被保存的 Transfer
     */
    @Override
    public TransferBean save(TransferMapBean map, UserSessionBean user) {
        TransferBean transfer = map.getTransfer();

        if (transfer.getTransfer_id() > 0) {
            map.getTransfer().setTransfer_status(TransferStatus.Open);
            return update(map, user);
        }

        return insert(map, user);
    }

    /**
     * 获取ClientShipment和Transfer的比较结果
     *
     * @return List
     */
    @Override
    public Map<String, Object> compareTransfer(TransferBean transfer) {
        Map<String, Object> resultMap = new HashMap<>();

        List<ClientShipmentCompareBean>  clientShipmentCompareList= new ArrayList<>();

        // 设置有ClientShipment的场合，需要比较实际入库数量，如数量不一致则需要报警
        if (!StringUtils.null2Space2(transfer.getClient_shipment_id()).equals("0")) {
            clientShipmentCompareList= clientShipmentDao.getCompareResult(String.valueOf(transfer.getTransfer_id()),transfer.getClient_shipment_id());
        }

        resultMap.put("compareResult", clientShipmentCompareList.size() > 0 ? "1" :"0");

        return resultMap;
    }

    /**
     * 提交 Transfer， 保存信息外，强制变更状态，并关闭 Mapping
     *
     * @param map  TransferMapBean
     * @param user 当前用户
     * @return 被提交的 Transfer
     */
    @Override
    public TransferBean submitTransfer(TransferMapBean map, UserSessionBean user) {
        TransferBean transfer = map.getTransfer();

        // 检查是否有 Item
        if (transferDao.getItemCountInTransfer(transfer.getTransfer_id()) < 1) {
            throw new BusinessException(TransferMsg.NO_TRANSFER_ITEM_EXISTS);
        }

        // 检查所有 Package 状态
        if (!transferDao.isAllPackageClosed(transfer.getTransfer_id())) {
            throw new BusinessException(TransferMsg.PACKAGE_NOT_CLOSE);
        }

        // 如果是 IN
        if (isIn(transfer)) {
            // 检查关联的 Transfer 状态
            if (isOutClosed(map)) {
                throw new BusinessException(TransferMsg.TRANSFER_ALREADY_CLOSE, map.getContext_name());
            }

            // 检查比对结果
            if (!compare(map)) {
                throw new BusinessException(TransferMsg.IN_OUT_NOT_MATCH);
            }
        }

        transfer.setTransfer_status(TransferStatus.Close);
        transfer = update(map, user);

        // Close Mapping
        transferDao.closeMapping(transfer.getTransfer_id());

        return transfer;
    }

    /**
     * 获取 Transfer 里的所有 Item
     *
     * @param transfer_id Transfer ID
     * @return List
     */
    @Override
    public List<TransferItemBean> allItemInTransfer(long transfer_id) {
        if (transfer_id < 1)
            throw new BusinessException(TransferMsg.TRANSFER_NOT_EXISTS);

        return transferDao.allItemInTransfer(transfer_id);
    }

    /**
     * 获取 Package 下的所有 Item
     *
     * @param package_id Package ID
     * @return List
     */
    @Override
    public List<TransferItemBean> getItemsInPackage(long package_id) {
        if (package_id < 1)
            throw new BusinessException(TransferMsg.NO_PACKAGE_EXISTS);

        return transferDao.getItemsInPackage(package_id);
    }

    /**
     * 获取 Transfer In 对应的 Transfer Out 的 Name
     *
     * @param transferId Transfer ID
     * @return Transfer Name
     */
    @Override
    public String getMapTarget(long transferId) {
        Integer out_id = transferDao.getMapTarget(transferId);

        if (out_id == null) return null;

        TransferBean out = get(out_id);

        return out == null ? null : out.getTransfer_name();
    }

    /**
     * 获取 Transfer In 和其对应的 Out 的所有 Item
     *
     * @param transfer_in_id Transfer ID
     * @return Map
     */
    @Override
    public Map<String, List<TransferItemBean>> allItemInMap(long transfer_in_id) {
        Integer out_id = transferDao.getMapTarget(transfer_in_id);

        if (out_id == null)
            throw new BusinessException(TransferMsg.NO_TRANSFER_OUT_MAPPING);

        List<TransferItemBean> inItems = allItemInTransfer(transfer_in_id);
        List<TransferItemBean> outItems = allItemInTransfer(out_id);

        Map<String, List<TransferItemBean>> map = new HashMap<>();

        map.put("inItems", inItems);
        map.put("outItems", outItems);

        return map;
    }

    /**
     * 获取可用的所有 配置项
     *
     * @return List
     */
    @Override
    public Map<String, Object> allConfigs(String transferId, UserSessionBean user) {

        Map<String, Object> resultMap = new HashMap<>();

        // 获得相关渠道
        List<String> orderChannelIdList = user.getChannelList();

        List<StoreBean> storeList = new ArrayList<>();

        // 获得相关渠道下的真实仓库
        for (String orderChannelId : orderChannelIdList) {
            List<StoreBean> storeBeans = StoreConfigs.getChannelStoreList(orderChannelId, false, false);
            if (storeBeans == null) throw new SystemException("System Init Error");
//            storeList.addAll(storeBeans);
            // 排除品牌方管理库存的仓库
            for (StoreBean storeBean : storeBeans ) {
                if (storeBean.getInventory_manager().equals(StoreConfigEnums.Manager.YES.getId())) {
                    storeList.add(storeBean);
                }
            }

        }
        resultMap.put("storeList", storeList);

        // 获得该用户登录时的仓库
//        List<ChannelStoreBean> companyStoreList = user.getCompanyRealStoreList();
        ArrayList<ChannelStoreBean> companyStoreList = new ArrayList<>();
        // 排除品牌方管理库存的仓库
        for (ChannelStoreBean storeBean : user.getCompanyRealStoreList() ) {
            if (StoreConfigs.getStore(new Long(storeBean.getStore_id())).getInventory_manager().equals(StoreConfigEnums.Manager.YES.getId())) {
                companyStoreList.add(storeBean);
            }
        }
        resultMap.put("companyStoreList", companyStoreList);

        // 获得该用户可以移库的仓库
//        List<ChannelStoreBean> companyStoreToList = user.getCompanyRealStoreToList();
        ArrayList<ChannelStoreBean> companyStoreToList = new ArrayList<>();
        for (ChannelStoreBean storeBean : user.getCompanyRealStoreToList() ) {
            if (StoreConfigs.getStore(new Long(storeBean.getStore_id())).getInventory_manager().equals(StoreConfigEnums.Manager.YES.getId())) {
                companyStoreToList.add(storeBean);
            }
        }
        resultMap.put("companyStoreToList", companyStoreToList);

        // 获取品牌方发货的Shipment信息
        List<ClientShipmentBean> notMatchClientShipmentList = new ArrayList<>();

        ClientShipmentBean clientShipmentBean = new ClientShipmentBean();
        clientShipmentBean.setShipment_id(0);
        clientShipmentBean.setFile_name("Nothing");

        notMatchClientShipmentList.add(clientShipmentBean);

        List<ClientShipmentBean> clientShipmentList = clientShipmentDao.getNotMatchShipmentList(orderChannelIdList,StringUtils.isNullOrBlank2(transferId)?"0":transferId);

        notMatchClientShipmentList.addAll(clientShipmentList);

        resultMap.put("notMatchClientShipmentList", notMatchClientShipmentList);

        return resultMap;

    }

    /**
     * 更新 Transfer
     *
     * @param transferMapBean TransferMapBean
     * @param user            当前用户
     * @return 被更新的 Transfer
     */
    @Override
    public TransferBean update(TransferMapBean transferMapBean, UserSessionBean user) {
        TransferBean transfer = transferMapBean.getTransfer();

        if (transfer.getTransfer_id() < 1)
            throw new BusinessException(TransferMsg.TRANSFER_NOT_EXISTS);

        TransferBean context = getContext(transferMapBean);
        TransferBean beanAtDb = get(transfer.getTransfer_id());

        // 如果是更新操作，则检查状态
        if (beanAtDb == null)

            throw new BusinessException(TransferMsg.TRANSFER_NOT_EXISTS);

        else if (TransferStatus.Close.equals(beanAtDb.getTransfer_status()))

            throw new BusinessException(TransferMsg.TRANSFER_ALREADY_CLOSE, beanAtDb.getTransfer_name());

        String lastModified = beanAtDb.getModified();

        setMainStoreInfo(transfer);

        String now = DateTimeUtil.getNow();

        transfer.setModified(now);
        transfer.setModifier(user.getUserName());

        if (transferDao.updateTransferInfo(transfer, lastModified) < 1)
            throw new BusinessException(ComMsg.UPDATE_BY_OTHER);

        if (context != null) {
            if (insertMap(transfer, context, user) < 1)
                return null;
        }

        return transfer;
    }

    /**
     * 下载入库、出库、进货清单
     * @param user 用户登录信息
     * @return byte[] 可捡货列表
     */
    @Override
    public byte[] downloadTransferList(String transfer_id, UserSessionBean user) {

        byte[] bytes;

        // 用户所属仓库的设置
        List<ChannelStoreBean> channelStoreList = user.getCompanyStoreList();

        // 获取相关渠道
        List<String> orderChannelList = user.getChannelList();

        // 取得符合条件的记录
        List<TransferFormBean> transferList = transferDao.downloadTransferItems(transfer_id, channelStoreList, orderChannelList);

        logger.info("入库/出库/进货清单件数（TransferId=" + transfer_id + "）：" + transferList.size());
        try{

            // 报表模板名取得
            String templateFile = com.voyageone.common.configs.Properties.readValue(WmsConstants.ReportSetting.WMS_REPORT_TEMPLATE_PATH) + WmsConstants.ReportTransferItems.FILE_NAME + ".xls";

            // 报表模板名读入
            InputStream templateInput = new FileInputStream(templateFile);
            HSSFWorkbook workbook = new HSSFWorkbook(templateInput);

            // 设置内容
            if (transferList.size() > 0) {
                setTransferItems(workbook, transferList, user);
            }

            // 出力内容
            ByteArrayOutputStream outDate = new ByteArrayOutputStream();
            workbook.write(outDate);
            bytes = outDate.toByteArray();

            // 关闭
            templateInput.close();
            workbook.close();
            outDate.close();

        } catch (Exception e) {
            logger.info("入库/出库/进货清单下载失败：" + e);
            throw new BusinessException(WmsMsgConstants.TransferMsg.DOWNLOAD_FAILED, transfer_id);
        }

        return bytes;

    }

    /**
     * 设置可入库/出库/进货清单内容
     * @param workbook 报表模板
     * @param transferList 可捡货列表内容
     * @param user 用户登录信息
     */
    private  void setTransferItems(HSSFWorkbook workbook, List<TransferFormBean> transferList, UserSessionBean user) {

        HSSFSheet sheet;

        // 模板Sheet
        sheet = workbook.getSheetAt(WmsConstants.ReportTransferItems.TEMPLATE_SHEET);

        // Head项目设置用
        TransferFormBean transfer = transferList.get(0);

        HSSFRow currentRow;

        // 仓库
        long storeId = (long)transfer.getStore_id();
        currentRow = sheet.getRow(WmsConstants.ReportTransferItems.Store.Row);
        currentRow.getCell(WmsConstants.ReportTransferItems.Store.Column).setCellValue(storeId == 0l ? "" : StoreConfigs.getStore(storeId).getStore_name());

        // 入出库类型
        String transferType = transfer.getTransfer_type();
        currentRow = sheet.getRow(WmsConstants.ReportTransferItems.Type.Row);
        currentRow.getCell(WmsConstants.ReportTransferItems.Type.Column).setCellValue(StringUtils.isNullOrBlank2(transferType) ? "" :Type.getTypeName(TypeConfigEnums.MastType.transferType.getId(), transferType));

        // PO Number行数、列数
        currentRow = sheet.getRow(WmsConstants.ReportTransferItems.PO.Row);
        currentRow.getCell(WmsConstants.ReportTransferItems.PO.Column).setCellValue(transfer.getPo_number());

        // Date行数、列数
        String created = transfer.getCreated();
        currentRow = sheet.getRow(WmsConstants.ReportTransferItems.Date.Row);
        currentRow.getCell(WmsConstants.ReportTransferItems.Date.Column).setCellValue(StringUtils.isNullOrBlank2(created) ? "" : DateTimeUtil.getLocalTime(created, user.getTimeZone()));

        // From行数、列数
        long fromStoreId = (long)transfer.getTransfer_from_store();
        currentRow = sheet.getRow(WmsConstants.ReportTransferItems.From.Row);
        currentRow.getCell(WmsConstants.ReportTransferItems.From.Column).setCellValue(storeId == 0l ? "" : StoreConfigs.getStore(fromStoreId).getStore_name());

        // To行数、列数
        long toStoreId = (long)transfer.getTransfer_to_store();
        currentRow = sheet.getRow(WmsConstants.ReportTransferItems.To.Row);
        currentRow.getCell(WmsConstants.ReportTransferItems.To.Column).setCellValue(storeId == 0l ? "" : StoreConfigs.getStore(toStoreId).getStore_name());

        // TransferNumber行数、列数
        currentRow = sheet.getRow(WmsConstants.ReportTransferItems.TransferNumber.Row);
        currentRow.getCell(WmsConstants.ReportTransferItems.TransferNumber.Column).setCellValue(transfer.getTransfer_name());

        // Number of Cartons行数、列数
        currentRow = sheet.getRow(WmsConstants.ReportTransferItems.Cartons.Row);
        currentRow.getCell(WmsConstants.ReportTransferItems.Cartons.Column).setCellValue(transfer.getDetails_num());

        // Notes行数、列数
        currentRow = sheet.getRow(WmsConstants.ReportTransferItems.Notes.Row);
        currentRow.getCell(WmsConstants.ReportTransferItems.Notes.Column).setCellValue(transfer.getComment());

        //Code具体统计明细设置用
        String itemCode = "";
        int skuTotalQty = 0;
        int totalQty = 0;
        int addRows = 0;

        TreeMap<String,Integer>skuTotal = new TreeMap<>();

        for (int i = 0; i < transferList.size(); i++) {
            TransferFormBean transferFormBean =transferList.get(i);

            // 按照Code统计数量
            if (StringUtils.isNullOrBlank2((itemCode)) || itemCode.equals(transferFormBean.getItemcode()) ) {

                skuTotal.put(transferFormBean.getSize(),transferFormBean.getTransfer_qty());

                skuTotalQty = skuTotalQty + transferFormBean.getTransfer_qty();

            } else {
                skuTotal.put(WmsConstants.ReportTransferItems.Size.SortName,skuTotalQty);

                skuTotalQty = 0;

                // 将统计内容按照Code级别记入表中
                setSkuQty(sheet,skuTotal,addRows, itemCode);

                skuTotal.clear();
                skuTotal.put(transferFormBean.getSize(),transferFormBean.getTransfer_qty());

                skuTotalQty = skuTotalQty + transferFormBean.getTransfer_qty();
                addRows = addRows + WmsConstants.ReportTransferItems.ADD_ROWS;

            }

            totalQty = totalQty + transferFormBean.getTransfer_qty();
            itemCode = transferFormBean.getItemcode();

            // 最终行时，将最后统计的记录也记入其中
            if (i == transferList.size() -1) {
                skuTotal.put(WmsConstants.ReportTransferItems.Size.SortName, skuTotalQty);
                setSkuQty(sheet,skuTotal,addRows, itemCode);
            }


        }

        // Code
        currentRow = sheet.getRow(WmsConstants.ReportTransferItems.Total.Row + addRows);
        currentRow.getCell(WmsConstants.ReportTransferItems.Total.Column).setCellValue(WmsConstants.ReportTransferItems.Total.Name + totalQty);

    }

    /**
     * 将统计的SKU数量记入表中
     */
    private void  setSkuQty(HSSFSheet sheet,TreeMap<String,Integer> skuTotal, int addRows ,String itemCode ){

        // Code改变时，增加新行
        if (addRows != 0)  {
            addCodeRows(sheet, addRows);
        }

        HSSFRow currentRow;

        // Code
        currentRow = sheet.getRow(WmsConstants.ReportTransferItems.Code.Row + addRows);
        currentRow.getCell(WmsConstants.ReportTransferItems.Code.Column_Start).setCellValue(WmsConstants.ReportTransferItems.Code.Name + itemCode);

        // Size,Qty
        int skuCell = 0;
        for (Map.Entry<String, Integer> entry : skuTotal.entrySet()) {

            //  Size
            currentRow = sheet.getRow(WmsConstants.ReportTransferItems.Size.Row + addRows);
            if (skuCell != 0){
                HSSFCell newCell = currentRow.createCell(WmsConstants.ReportTransferItems.Size.Column_Detail+ skuCell);
                HSSFCell oldCell = currentRow.getCell(WmsConstants.ReportTransferItems.Size.Column_Detail);
                newCell.setCellStyle(oldCell.getCellStyle());
            }
            if (entry.getKey().equals(WmsConstants.ReportTransferItems.Size.SortName)) {
                currentRow.getCell(WmsConstants.ReportTransferItems.Size.Column_Detail+ skuCell).setCellValue(WmsConstants.ReportTransferItems.Size.Name);
            }else {
                currentRow.getCell(WmsConstants.ReportTransferItems.Size.Column_Detail+ skuCell).setCellValue(entry.getKey());
            }

            //  Qty
            currentRow = sheet.getRow(WmsConstants.ReportTransferItems.Qty.Row + addRows);
            if (skuCell != 0){
                HSSFCell newCell = currentRow.createCell(WmsConstants.ReportTransferItems.Qty.Column_Detail+ skuCell);
                HSSFCell oldCell = currentRow.getCell(WmsConstants.ReportTransferItems.Qty.Column_Detail);
                newCell.setCellStyle(oldCell.getCellStyle());
            }
            currentRow.getCell(WmsConstants.ReportTransferItems.Qty.Column_Detail+ skuCell).setCellValue(entry.getValue());

            skuCell = skuCell + 1;

        }
    }

    /**
     * 增加Code相关行
     * @param sheet Sheet
     * @param addRows 增加行数
     */
    private void  addCodeRows(HSSFSheet sheet, int addRows) {

        HSSFRow newRow;
        HSSFRow oldRow;
        HSSFCell newCell;
        HSSFCell oldCell;

        // 插入行
        insertRow(sheet,WmsConstants.ReportTransferItems.Code.Row + addRows,WmsConstants.ReportTransferItems.ADD_ROWS);

        // 增加Code新行
        newRow = sheet.createRow(WmsConstants.ReportTransferItems.Code.Row + addRows);
        oldRow = sheet.getRow(WmsConstants.ReportTransferItems.Code.Row);

        for (int col = WmsConstants.ReportTransferItems.Code.Column_Start; col <= WmsConstants.ReportTransferItems.Code.Column_End; col++) {
            newCell = newRow.createCell(col);
            oldCell = oldRow.getCell(col);

            newCell.setCellStyle(oldCell.getCellStyle());
        }

        newCell = newRow.createCell(WmsConstants.ReportTransferItems.Column_Start);
       oldCell = oldRow.getCell(WmsConstants.ReportTransferItems.Column_Start);
        newCell.setCellStyle(oldCell.getCellStyle());
        newCell = newRow.createCell(WmsConstants.ReportTransferItems.Column_End);
        oldCell = oldRow.getCell(WmsConstants.ReportTransferItems.Column_End);
        newCell.setCellStyle(oldCell.getCellStyle());

        // 增加Size新行
        newRow = sheet.createRow(WmsConstants.ReportTransferItems.Size.Row + addRows);
        oldRow = sheet.getRow(WmsConstants.ReportTransferItems.Size.Row);
        newCell = newRow.createCell(WmsConstants.ReportTransferItems.Size.Column_Title);
        oldCell = oldRow.getCell(WmsConstants.ReportTransferItems.Size.Column_Title);
        newCell.setCellStyle(oldCell.getCellStyle());
        newCell.setCellValue(oldCell.getStringCellValue());

        newCell = newRow.createCell(WmsConstants.ReportTransferItems.Size.Column_Detail);
        oldCell = oldRow.getCell(WmsConstants.ReportTransferItems.Size.Column_Detail);
        newCell.setCellStyle(oldCell.getCellStyle());

        newCell = newRow.createCell(WmsConstants.ReportTransferItems.Column_Start);
        oldCell = oldRow.getCell(WmsConstants.ReportTransferItems.Column_Start);
        newCell.setCellStyle(oldCell.getCellStyle());
        newCell = newRow.createCell(WmsConstants.ReportTransferItems.Column_End);
        oldCell = oldRow.getCell(WmsConstants.ReportTransferItems.Column_End);
        newCell.setCellStyle(oldCell.getCellStyle());

        // 增加Qty新行
        newRow = sheet.createRow(WmsConstants.ReportTransferItems.Qty.Row + addRows);
        oldRow = sheet.getRow(WmsConstants.ReportTransferItems.Qty.Row);
        newCell = newRow.createCell(WmsConstants.ReportTransferItems.Qty.Column_Title);
        oldCell = oldRow.getCell(WmsConstants.ReportTransferItems.Qty.Column_Title);
        newCell.setCellStyle(oldCell.getCellStyle());
        newCell.setCellValue(oldCell.getStringCellValue());

        newCell = newRow.createCell(WmsConstants.ReportTransferItems.Qty.Column_Detail);
        oldCell = oldRow.getCell(WmsConstants.ReportTransferItems.Qty.Column_Detail);
        newCell.setCellStyle(oldCell.getCellStyle());

        newCell = newRow.createCell(WmsConstants.ReportTransferItems.Column_Start);
        oldCell = oldRow.getCell(WmsConstants.ReportTransferItems.Column_Start);
        newCell.setCellStyle(oldCell.getCellStyle());
        newCell = newRow.createCell(WmsConstants.ReportTransferItems.Column_End);
        oldCell = oldRow.getCell(WmsConstants.ReportTransferItems.Column_End);
        newCell.setCellStyle(oldCell.getCellStyle());

    }

    /**
     * 插入行
     */
    private  void insertRow(HSSFSheet sheet, int startRow, int rows) {

        sheet.shiftRows(startRow, sheet.getLastRowNum(), rows, true, false);

        for (int i = 0; i < rows; i++) {
            HSSFRow sourceRow;//原始位置
            HSSFRow targetRow;//移动后位置
            HSSFCell sourceCell;
            HSSFCell targetCell;
            sourceRow = sheet.createRow(startRow);
            targetRow = sheet.getRow(startRow + rows);
            sourceRow.setHeight(targetRow.getHeight());

            for (int m = targetRow.getFirstCellNum(); m < targetRow.getPhysicalNumberOfCells(); m++) {
                sourceCell = sourceRow.createCell(m);
                targetCell = targetRow.getCell(m);
                sourceCell.setCellStyle(targetCell.getCellStyle());
                sourceCell.setCellType(targetCell.getCellType());
            }
            startRow++;
        }

    }

    /**
     * 根据类型，选择 from 或 to 作为主 store。并设置到 bean 的 store_id 和 channel_id 字段
     *
     * @param transferFormBean TransferBean
     */
    private void setMainStoreInfo(TransferBean transferFormBean) {
        String channel_id;
        int store_id;

        switch (transferFormBean.getTransfer_type()) {
            case TransferType.IN: // In
            case TransferType.PO: // Po
                store_id = transferFormBean.getTransfer_to_store();
                break;
            case TransferType.OUT: // Out
            case TransferType.RE: // RE
                store_id = transferFormBean.getTransfer_from_store();
                break;
            default:
                throw new BusinessException(TransferMsg.INVALID_TRANSFER_TYPE);
        }

        channel_id = storeDao.getChannel_id(store_id);

        transferFormBean.setOrder_channel_id(channel_id);
        transferFormBean.setStore_id(store_id);
    }

    /**
     * 自动为 Transfer 创建名称
     */
    private String createName(TransferBean transfer) {
        String prefix = "";

        switch (transfer.getTransfer_type()) {
            case TransferType.IN:
                prefix = "IN";
                break;
            case TransferType.OUT:
                prefix = "OUT";
                break;
            case TransferType.PO:
                prefix = "PO";
                break;
            case TransferType.RE:
                prefix = "RE";
                break;
        }

        // 此处注意，getStore_id 有可能返回的是没有仓库（就是 0），因为在创建时，必须先执行 setMainStoreInfo
        return prefix + DateTimeUtil.getNow("yyyyMMddHHmmss") + storeDao.getStoreName(transfer.getStore_id());
    }

    /**
     * 在指定的 Package 自动创建 Item
     */
    private TransferItemBean createItem(TransferBean transfer, TransferDetailBean detail, String barcode, int num, String itemCode, String color, String size, UserSessionBean user) {
        String to_store_channel_id;

        if (isOut(transfer)) {
            to_store_channel_id = storeDao.getChannel_id(transfer.getTransfer_to_store());
        } else {
            to_store_channel_id = transfer.getOrder_channel_id();
        }

        String sku = itemDao.getSku(to_store_channel_id, barcode);

        //该渠道找不到匹配SKU时
//        if (StringUtils.isEmpty(sku)) {
//            throw new BusinessException(TransferMsg.INVALID_SKU);
//        }

        TransferItemBean item = new TransferItemBean();

        item.setSyn_flg(WmsConstants.SynFlg.UNSEND);

        //该渠道找不到匹配SKU时，根据渠道进行判断：
        // 允许的场合，将Barcode设置为SKU，并且该记录的同步标志位设置为忽略
        // 不允许的场合，直接抛出错误
        if (StringUtils.isEmpty(sku)) {
            if (to_store_channel_id.equals(ChannelConfigEnums.Channel.JC.getId())) {

                // 初次输入的场合
                if (StringUtils.isEmpty(itemCode)) {
                    if (isClientSKUExist(to_store_channel_id, barcode)) {
                        sku = barcode;
                    } else {
                        sku = popupInput;
                    }
                // ItemCode，Size 输入的场合
                } else {
                    if (isClientSKUExistByItemCodeAndSize(to_store_channel_id, itemCode, color, size)) {
                        sku = repeatInput;
                    } else {
                        sku = barcode;
                    }
                }
                item.setSyn_flg(WmsConstants.SynFlg.IGNORE);
            } else {
                throw new BusinessException(TransferMsg.INVALID_SKU);
            }
        }

        item.setTransfer_id(detail.getTransfer_id());
        item.setTransfer_package_id(detail.getTransfer_package_id());
        item.setTransfer_barcode(barcode);
        item.setTransfer_sku(sku);
        item.setTransfer_qty(num);

        item.setOrder_number(0);
        item.setReservation_id(0);

        setItemCalcQty(transfer, item);

        item.setActive(true);

        String now = DateTimeUtil.getNow();

        item.setCreated(now);
        item.setCreater(user.getUserName());
        item.setModified(now);
        item.setModifier(user.getUserName());

        return item;
    }

    /**
     * wms_bt_client_sku 存在判定（根据barcode）
     */
    private boolean isClientSKUExist(String orderChannelId, String barcode) {
        boolean ret = false;

        int recCount = clientSkuDao.getRecCount(orderChannelId, barcode);
        if (recCount > 0) {
            ret = true;
        }

        return ret;
    }

    /**
     * wms_bt_client_sku 存在判定（根据itemCode，size）
     */
    private boolean isClientSKUExistByItemCodeAndSize(String orderChannelId, String itemCode, String color, String size) {
        boolean ret = false;

        int recCount = clientSkuDao.getRecCountByItemCodeAndSize(orderChannelId, itemCode, color, size);
        if (recCount > 0) {
            ret = true;
        }

        return ret;
    }

    /**
     * 为 Item 同步设置 CalcQty 字段
     */
    private void setItemCalcQty(TransferBean transfer, TransferItemBean item) {
        if (isOut(transfer) || isWithdrawal(transfer)) {
            item.setCalc_qty(-item.getTransfer_qty());
        } else {
            item.setCalc_qty(item.getTransfer_qty());
        }
    }

    private String createPackageName(TransferBean transferBean) {
        int max = transferDao.getDetailMaxNum(transferBean.getTransfer_id());

        Date date = DateTimeUtil.getDate();

        return DateTimeUtil.format(date, "HHmmss") + (max + 1);
    }

    /**
     * 辅助方法，用于 insert 之前，对一些值进行初始或调整
     *
     * @param transfer TransferBean
     */
    private void setTransferForInsert(TransferBean transfer) {
        // 如果没有给名字，则自动创建名字
        if (StringUtils.isEmpty(transfer.getTransfer_name())) {
            transfer.setTransfer_name(createName(transfer));
        }

        // 检查名字是否重复
        if (transferDao.transferHasName(transfer.getTransfer_name())) {
            throw new BusinessException(TransferMsg.EXISTS_TRANSFER_NAME, transfer.getTransfer_name());
        }

        transfer.setTransfer_status(TransferStatus.Open);
        transfer.setTransfer_origin("0");

        if (isOut(transfer)) {
            transfer.setSim_flg(TransferStatus.Open);
        } else {
            transfer.setSim_flg(TransferStatus.Close);
        }
    }

    private int insertMap(TransferBean transfer, TransferBean context, UserSessionBean user) {
        TransferMappingBean transferMappingBean = new TransferMappingBean();

        transferMappingBean.setTransfer_in_id(transfer.getTransfer_id());
        transferMappingBean.setTransfer_out_id(context.getTransfer_id());
        transferMappingBean.setMapping_status(false);
        transferMappingBean.setActive(true);

        String now = DateTimeUtil.getNow();

        transferMappingBean.setCreater(user.getUserName());
        transferMappingBean.setCreated(now);
        transferMappingBean.setModifier(user.getUserName());
        transferMappingBean.setModified(now);

        return transferDao.insertMap(transferMappingBean);
    }

    private TransferBean getContext(TransferMapBean map) {
        TransferBean context = map.getContext();
        TransferBean transfer = map.getTransfer();

        if (isIn(transfer)) {

            // 如果是空，则获取目标 Transfer
            if (context == null) {
                // ContextName 的空验证在 Controller 中进行
                context = transferDao.getByName(map.getContext_name());

                // 后续的 mapping other 校验里需要使用
                map.setContext(context);

                // 如果查不到 Transfer
                // 或者类型不是 Out
                // 获取已经被别的关联了
                if (context == null || !isOut(context) || transferDao.isMappingOther(map)) {
                    throw new BusinessException(TransferMsg.IN_OUT_NOT_MATCH);
                }
            } else if (transferDao.isMappingRight(map)) {
                // 如果已经正确关联了。那么只保留在 MappingBean 中。而不返回
                // 即，仅将 context 变为 null
                // 以此告知后续方法的操作，不要插入目标 Mapping
                context = null;
            }
        }

        return context;
    }

    private boolean compare(TransferMapBean map) {
        getContext(map);
        return transferDao.compare(map);
    }

    private boolean isOut(TransferBean transfer) {
        return TransferType.OUT.equals(transfer.getTransfer_type());
    }

    private boolean isWithdrawal(TransferBean transfer) {
        return TransferType.RE.equals(transfer.getTransfer_type());
    }

    private boolean isIn(TransferBean transfer) {
        return TransferType.IN.equals(transfer.getTransfer_type());
    }

    private boolean isOutClosed(TransferMapBean map) {
        TransferBean transferOut = map.getContext();

        // 有可能在检查之前，其他位置，已经获取了对应的 Out 对象
        // 所以此处判断处理
        if (transferOut == null) {
            transferOut = transferDao.getByName(map.getContext_name());
            map.setContext(transferOut);
        }

        return TransferStatus.Close.equals(transferOut.getTransfer_status());
    }

    private UserSessionBean getUser() {

        Object userValue = request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);

        return (UserSessionBean) userValue;
    }

    private int getUserTimeZone() {
        UserSessionBean userSessionBean = getUser();

        return userSessionBean == null ? 0 : userSessionBean.getTimeZone();
    }

    @Override
    public byte[] downloadClientShipment(String param, UserSessionBean user) {
        TransferBean transfer = JsonUtil.jsonToBean(param, TransferBean.class);
        List<ClientShipmentCompareBean>  clientShipmentCompareList = clientShipmentDao.getCompareResult(String.valueOf(transfer.getTransfer_id()),transfer.getClient_shipment_id());
        return createTransferCompare(clientShipmentCompareList);
    }


    @Override
    public byte[] downloadTransferCompare(String param, UserSessionBean user) {
        TransferBean transfer = JsonUtil.jsonToBean(param, TransferBean.class);
        List<ClientShipmentCompareBean>  clientShipmentCompareList = clientShipmentDao.getCompareResult(String.valueOf(transfer.getTransfer_id()),transfer.getClient_shipment_id());
        return createTransferCompare(clientShipmentCompareList);
    }

    private byte[] createTransferCompare(List<ClientShipmentCompareBean>  clientShipmentCompareList) {
        byte[] bytes;
        try{
            // 报表模板名取得
            String templateFile = com.voyageone.common.configs.Properties.readValue(WmsConstants.ReportItems.TransferCompare.TEMPLATE_PATH) + WmsConstants.ReportItems.TransferCompare.TEMPLATE_NAME;
            // 报表模板名读入
            InputStream templateInput = new FileInputStream(templateFile);
            Workbook workbook = WorkbookFactory.create(templateInput);
            // 设置内容
            setTransferCompare(workbook, clientShipmentCompareList);
            // 输出内容
            ByteArrayOutputStream outData = new ByteArrayOutputStream();
            workbook.write(outData);
            bytes = outData.toByteArray();
            // 关闭
            templateInput.close();
            workbook.close();
            outData.close();
        } catch (Exception e) {
            logger.info("TransferCompare报表下载失败：" + e);
            throw new BusinessException(WmsMsgConstants.TransferMsg.REPORT_FAILED, WmsConstants.ReportItems.TransferCompare.RPT_NAME);
        }
        return bytes;
    }

    /**
     * 设置compRes报表内容
     * @param workbook 报表模板
     * @param clientShipmentCompareList 库存内容按sku
     */
    private void setTransferCompare(Workbook workbook, List<ClientShipmentCompareBean>  clientShipmentCompareList) {
        // 模板Sheet
        int sheetNo = WmsConstants.ReportItems.TransferCompare.TEMPLATE_SHEET_NO;
        // 初始行
        int intRow = WmsConstants.ReportItems.TransferCompare.TEMPLATE_FIRSTROW_NO;

        // 按照模板克隆一个sheet
        Sheet sheet = workbook.cloneSheet(sheetNo);

        // 设置模板sheet页后的sheet名为报告sheet名
        workbook.setSheetName(sheetNo + 1, WmsConstants.ReportItems.TransferCompare.RPT_SHEET_NAME);

        for (ClientShipmentCompareBean clientShipmentCompareBean : clientShipmentCompareList) {
            if(intRow != WmsConstants.ReportItems.TransferCompare.TEMPLATE_FIRSTROW_NO) {
                Row newRow = sheet.createRow(intRow);
                //根据第2行（第一行是标题）格式设置每行的格式,第一列不处理
                for (int col = 0; col < WmsConstants.ReportItems.TransferCompare.COLNUM_MAX; col++) {
                    Cell newCell = newRow.createCell(col);
                    Cell oldCell = sheet.getRow(WmsConstants.ReportItems.TransferCompare.TEMPLATE_FIRSTROW_NO).getCell(col);
                    newCell.setCellStyle(oldCell.getCellStyle());
                }
            }
            // 得到当前行
            Row currentRow = sheet.getRow(intRow);
            // No
            currentRow.getCell(WmsConstants.ReportItems.TransferCompare.Col.COLNUM_NO).setCellValue(intRow -  WmsConstants.ReportItems.TransferCompare.TEMPLATE_FIRSTROW_NO + 1);
            // ASN_FILE
            currentRow.getCell(WmsConstants.ReportItems.TransferCompare.Col.COLNUM_ASN_FILE).setCellValue(StringUtils.null2Space2(clientShipmentCompareBean.getFile_name()));
            // ASN_CLIENT_SKU
            currentRow.getCell(WmsConstants.ReportItems.TransferCompare.Col.COLNUM_ASN_CLIENT_SKU).setCellValue(StringUtils.null2Space2(clientShipmentCompareBean.getArticle_number()));
            // ASN_UPC
            currentRow.getCell(WmsConstants.ReportItems.TransferCompare.Col.COLNUM_ASN_UPC).setCellValue(StringUtils.null2Space2(clientShipmentCompareBean.getUpc()));
            // ASN_SKU
            currentRow.getCell(WmsConstants.ReportItems.TransferCompare.Col.COLNUM_ASN_SKU).setCellValue(StringUtils.null2Space2(clientShipmentCompareBean.getSku()));
            // ASN_QTY
            currentRow.getCell(WmsConstants.ReportItems.TransferCompare.Col.COLNUM_ASN_QTY).setCellValue(StringUtils.null2Space2(clientShipmentCompareBean.getQty()));
            // TRANSFER_NAME
            currentRow.getCell(WmsConstants.ReportItems.TransferCompare.Col.COLNUM_TRANSFER_NAME).setCellValue(StringUtils.null2Space2(clientShipmentCompareBean.getTransfer_name()));
            // TRANSFER_PO
            currentRow.getCell(WmsConstants.ReportItems.TransferCompare.Col.COLNUM_TRANSFER_PO).setCellValue(StringUtils.null2Space2(clientShipmentCompareBean.getPo_number()));
            // TRANSFER_CLIENT_SKU
            currentRow.getCell(WmsConstants.ReportItems.TransferCompare.Col.COLNUM_TRANSFER_CLIENT_SKU).setCellValue(StringUtils.null2Space2(clientShipmentCompareBean.getClient_sku()));
            // TRANSFER_UPC
            currentRow.getCell(WmsConstants.ReportItems.TransferCompare.Col.COLNUM_TRANSFER_UPC).setCellValue(StringUtils.null2Space2(clientShipmentCompareBean.getTransfer_barcode()));
            // TRANSFER_SKU
            currentRow.getCell(WmsConstants.ReportItems.TransferCompare.Col.COLNUM_TRANSFER_SKU).setCellValue(StringUtils.null2Space2(clientShipmentCompareBean.getTransfer_sku()));
            // TRANSFER_QTY
            currentRow.getCell(WmsConstants.ReportItems.TransferCompare.Col.COLNUM_TRANSFER_QTY).setCellValue(StringUtils.null2Space2(clientShipmentCompareBean.getTransfer_qty()));

            intRow = intRow + 1;
        }
        // 如果有记录的话，删除模板sheet
        if (clientShipmentCompareList.size() > 0) {
            workbook.removeSheetAt(sheetNo);
        }
    }
}
