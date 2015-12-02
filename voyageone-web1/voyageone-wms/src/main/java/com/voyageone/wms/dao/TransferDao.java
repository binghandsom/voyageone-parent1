package com.voyageone.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.core.modelbean.ChannelStoreBean;
import com.voyageone.wms.formbean.TransferFormBean;
import com.voyageone.wms.formbean.TransferMapBean;
import com.voyageone.wms.modelbean.TransferBean;
import com.voyageone.wms.modelbean.TransferDetailBean;
import com.voyageone.wms.modelbean.TransferItemBean;
import com.voyageone.wms.modelbean.TransferMappingBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TransferDao extends BaseDao {
    private final static String PREFIX = Constants.DAO_NAME_SPACE_WMS;

    /**
     * 根据 id 查询 Transfer
     *
     * @param transfer_id {int}
     * @return TransferBean
     */
    public TransferBean getTransfer(int transfer_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_id", transfer_id);

        return selectOne(PREFIX + "wms_bt_transfer_getTransferInfo", params);
    }

    /**
     * 入出库信息追加
     */
    public int insertTransferInfo(TransferBean bean) {
        return updateTemplate.insert(PREFIX + "wms_bt_transfer_insertTransferInfo", bean);
    }

    /**
     * 入出库信息更新，只更新部分信息。标识位和部分属性不会更新
     * @param bean TransferBean
     * @return int
     */
    public int updateTransferInfo(TransferBean bean, String lastModified) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer", bean);
        params.put("lastModified", lastModified);

        return updateTemplate.update(PREFIX + "wms_bt_transfer_updateTransferInfo", params);
    }

    /**
     * 获取所有 Transfer 数量
     */
    public int getTransferCounts() {
        return selectOne(PREFIX + "wms_bt_transfer_getCounts");
    }

    public List<TransferFormBean> search(String po, int Store, String status, String from, String to, List<ChannelStoreBean> channelStoreList,int offset, int size) {
        Map<String, Object> params = paramsForSearch(po, Store, status, from, to, channelStoreList, offset, size);

        return selectList(PREFIX + "wms_bt_transfer_search", params);
    }

    public int searchCount(String po, int Store, String status, String from, String to, List<ChannelStoreBean> channelStoreList) {
        Map<String, Object> params = paramsForSearch(po, Store, status, from, to, channelStoreList, null, null);

        return selectOne(PREFIX + "wms_bt_transfer_search_count", params);
    }

    private Map<String, Object> paramsForSearch(String po, int store, String status, String from, String to, List<ChannelStoreBean> channelStoreList, Object offset, Object size) {
        Map<String, Object> params = new HashMap<>();

        params.put("po_number", po);
        params.put("transfer_status", status);
        params.put("fromDate", from);
        params.put("toDate", to);
        params.put("store_id", store);
        params.put("storeList", channelStoreList);

        params.put("status_type_id", TypeConfigEnums.MastType.transferStatus.getId());
        params.put("type_type_id", TypeConfigEnums.MastType.transferType.getId());

        if (offset != null && size != null) {
            params.put("offset", offset);
            params.put("size", size);
        }

        return params;
    }

    /**
     * 删除状态为 Open 的 Transfer
     *
     * @param transferId {int}
     * @param modified   {Date}
     * @return {int}
     */
    public int deleteTransfer(int transferId, String modified) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_id", transferId);
        params.put("modified", modified);

        return updateTemplate.delete(PREFIX + "wms_bt_transfer_delete", params);
    }

    /**
     * 删除 Transfer 下的所有 Package，无视状态和 modified 验证
     *
     * @param transferId {int}
     * @return {int}
     */
    public int deleteDetails(int transferId) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_id", transferId);

        return updateTemplate.delete(PREFIX + "wms_bt_transfer_deleteDetails", params);
    }

    /**
     * 删除 Transfer 下所有的 Item，无视 Package 状态
     *
     * @param transferId {int}
     * @return {int}
     */
    public int deleteTransferItems(int transferId) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_id", transferId);

        return updateTemplate.delete(PREFIX + "wms_bt_transfer_deleteItemsByTransfer", params);
    }

    /**
     * 根据 transfer name 和 transfer 的 modified 查询一个 transfer
     *
     * @param transfer_name String
     * @param modified      String
     * @return TransferBean
     */
    public TransferBean getByName(String transfer_name, String modified) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_name", transfer_name);

        params.put("modified", modified);

        return selectOne(PREFIX + "wms_bt_transfer_get_by_name_modified", params);
    }

    public List<TransferDetailBean> getPackages(int transfer_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_id", transfer_id);

        return selectList(PREFIX + "wms_bt_transfer_detail_get_packages", params);
    }

    public TransferDetailBean getPackage(int transferId, String packageName) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_id", transferId);
        params.put("package_name", packageName);

        return selectOne(PREFIX + "wms_bt_transfer_detail_get_package", params);
    }

    public int getDetailMaxNum(int transferId) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_id", transferId);

        return selectOne(PREFIX + "wms_bt_transfer_detail_get_max", params);
    }

    public int insertTransferDetail(TransferDetailBean detailBean) {
        return updateTemplate.insert(PREFIX + "wms_bt_transfer_detail_insert", detailBean);
    }

    public int deleteDetail(int package_id, String modified) {
        Map<String, Object> params = new HashMap<>();

        params.put("package_id", package_id);
        params.put("modified", modified);

        return updateTemplate.delete(PREFIX + "wms_bt_transfer_deletePackage", params);
    }

    public int reOpenPackage(int package_id, String modified) {
        Map<String, Object> params = new HashMap<>();

        params.put("package_id", package_id);
        params.put("modified", modified);

        return updateTemplate.delete(PREFIX + "wms_bt_transfer_reOpenPackage", params);
    }

    public int deletePackageItems(int package_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("package_id", package_id);

        return updateTemplate.delete(PREFIX + "wms_bt_transfer_deleteItemsByPackage", params);
    }

    /**
     * 根据 transfer_package_id 查询
     * @param package_id int
     * @return TransferDetailBean
     */
    public TransferDetailBean getPackage(int package_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("package_id", package_id);

        return selectOne(PREFIX + "wms_bt_transfer_detail_get_package_by_id", params);
    }

    public TransferItemBean getItem(int package_id, String barcode) {
        Map<String, Object> params = new HashMap<>();

        params.put("package_id", package_id);
        params.put("barcode", barcode);

        return selectOne(PREFIX + "wms_bt_transfer_item_get_by_barcode", params);
    }

    public int insertItem(TransferItemBean item) {
        return updateTemplate.insert(PREFIX + "wms_bt_transfer_item_insert", item);
    }

    public int deleteItem(int transfer_item_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("item_id", transfer_item_id);

        return updateTemplate.delete(PREFIX + "wms_bt_transfer_item_delete", params);
    }

    public int updateItemQty(TransferItemBean item, String lastModified) {
        Map<String, Object> params = new HashMap<>();

        params.put("item", item);
        params.put("lastModified", lastModified);

        return updateTemplate.update(PREFIX + "wms_bt_transfer_item_update_qty", params);
    }

    public boolean transferHasName(String name) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_name", name);

        int count = selectOne(PREFIX + "wms_bt_transfer_has_name", params);

        return count > 0;
    }

    public boolean detailHasName(int transfer_id, String name) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_id", transfer_id);
        params.put("package_name", name);

        int count = selectOne(PREFIX + "wms_bt_transfer_detail_has_name", params);

        return count > 0;
    }

    public TransferBean getByName(String transfer_name) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_name", transfer_name);

        return selectOne(PREFIX + "wms_bt_transfer_get_by_name", params);
    }

    public int insertMap(TransferMappingBean transferMappingBean) {
        return updateTemplate.insert(PREFIX + "wms_bt_transfer_mapping_insert", transferMappingBean);
    }

    public int deleteMapByIn(int transfer_in_id, String modified) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_in_id", transfer_in_id);
        params.put("modified", modified);

        return updateTemplate.delete(PREFIX + "wms_bt_transfer_mapping_delete_in", params);
    }

    public int deleteMapByOut(int transfer_out_id,  String modified) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_out_id", transfer_out_id);
        params.put("modified", modified);

        return updateTemplate.delete(PREFIX + "wms_bt_transfer_mapping_delete_out", params);
    }

    public int updatePackageStatus(TransferDetailBean detail, String modified) {
        Map<String, Object> params = new HashMap<>();

        params.put("detail", detail);
        params.put("lastModified", modified);

        return updateTemplate.update(PREFIX + "wms_bt_transfer_detail_update_status", params);
    }

    /**
     * 检查指定的 transfer 是否有成功的 Mapping
     * @param transfer_id int
     * @return boolean
     */
    public boolean alreadyMap(int transfer_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_in_id", transfer_id);
        params.put("mapping_status", true);

        int count = selectOne(PREFIX + "wms_bt_transfer_mapping_check_status", params);

        return count > 0;
    }

    public List<TransferItemBean> allItemInTransfer(int transfer_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_id", transfer_id);

        return selectList(PREFIX + "wms_bt_transfer_item_all_in_transfer", params);
    }

    public int getItemCountInTransfer(int transfer_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_id", transfer_id);

        return selectOne(PREFIX + "wms_bt_transfer_item_count_in_transfer", params);
    }

    public List<TransferItemBean> getItemsInPackage(int package_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("package_id", package_id);

        return selectList(PREFIX + "wms_bt_transfer_item_all_in_package", params);
    }

    public int getItemCountInPackage(int package_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("package_id", package_id);

        return selectOne(PREFIX + "wms_bt_transfer_item_count_in_package", params);
    }

    public Integer getMapTarget(int transfer_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_id", transfer_id);

        return selectOne(PREFIX + "wms_bt_transfer_mapping_get_target", params);
    }

    /**
     * 无视 Mapping 的状态。检查是否已经和别的 in 进行了关联
     */
    public boolean isMappingOther(TransferMapBean map) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_in_id", map.getTransfer().getTransfer_id());
        params.put("transfer_out_id", map.getContext().getTransfer_id());

        int count = selectOne(PREFIX + "wms_bt_transfer_mapping_count_mapping_other", params);

        return count > 0;
    }

    public boolean isMappingRight(TransferMapBean map) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_in_id", map.getTransfer().getTransfer_id());
        params.put("transfer_out_id", map.getContext().getTransfer_id());

        int count = selectOne(PREFIX + "wms_bt_transfer_mapping_count_mapping_right", params);

        return count > 0;
    }

    /**
     * 通过对比通过的数量，来进行 Transfer 的比较。每行比对是按照 SKU 和 数量 进行比对。
     */
    public boolean compare(TransferMapBean map) {
        int outCount = getItemCountInTransfer(map.getContext().getTransfer_id());

        int comparedCount = getComparedCount(map);

        return outCount == comparedCount;
    }

    /**
     * 获取两个 Transfer 比对成功的数量，比对是按照 SKU 和 数量 进行比对。
     */
    private int getComparedCount(TransferMapBean map) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_in_id", map.getTransfer().getTransfer_id());
        params.put("transfer_out_id", map.getContext().getTransfer_id());

        return selectOne(PREFIX + "wms_bt_transfer_mapping_count_compare_right", params);
    }

    public boolean isAllPackageClosed(int transfer_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_id", transfer_id);

        int count = selectOne(PREFIX + "wms_bt_transfer_detail_count_unclosed", params);

        return count < 1;
    }

    public int closeMapping(int transfer_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("transfer_id", transfer_id);

        return updateTemplate.update(PREFIX + "wms_bt_transfer_mapping_close", params);
    }

    /**
     * 获得可捡货信息一览

     * @param transferId transferId
     * @param storeList 用户所属的仓库
     * @param orderChannelList 用户所属的ChannelID
     * @return List<TransferFormBean>
     */
    public List<TransferFormBean> downloadTransferItems(String transferId, List<ChannelStoreBean>  storeList, List<String> orderChannelList) {

        Map<String, Object> params = new HashMap<>();

        params.put("transferId", transferId);
        params.put("storeList", storeList);
        params.put("orderChannelList", orderChannelList);

        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_WMS + "wms_bt_transfer_downloadTransferItems", params);

    }
}
