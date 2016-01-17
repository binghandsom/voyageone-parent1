package com.voyageone.wms.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.core.modelbean.ChannelStoreBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.wms.formbean.TransferFormBean;
import com.voyageone.wms.formbean.TransferMapBean;
import com.voyageone.wms.modelbean.TransferBean;
import com.voyageone.wms.modelbean.TransferDetailBean;
import com.voyageone.wms.modelbean.TransferItemBean;

import java.util.List;
import java.util.Map;

/**
 * Created by Tester on 4/30/2015.
 * @author Jonas
 */
public interface WmsTransferService {

    List<TransferFormBean> search(String po, int Store, String status, String from, String to, List<ChannelStoreBean> channelStoreList, int page, int i, UserSessionBean user);

    boolean delete(long transferId, String modified);

    TransferBean get(long transferId);

    Map<String, Object> allConfigs(String transferId, UserSessionBean user);

    int getSearchCount(String po, int Store, String status, String from, String to, List<ChannelStoreBean> channelStoreList);

    TransferBean update(TransferMapBean bean, UserSessionBean user);

    TransferBean insert(TransferMapBean bean, UserSessionBean user);

    List<TransferDetailBean> getPackages(long transfer_id);

    TransferDetailBean getPackage(long transferId, String packageName);

    TransferDetailBean createPackage(long transferId, String packageName, UserSessionBean user) throws BusinessException;

    boolean deletePackage(long package_id, String modified);

    boolean reOpenPackage(long package_id, String modified);

    String addItem(long package_id, String barcode, int num, String itemCode, String color, String size, UserSessionBean user);

    boolean closePackage(long package_id, String modified, UserSessionBean user);

    TransferBean save(TransferMapBean mapBean, UserSessionBean user);

    Map<String, Object> doListInit(UserSessionBean user);

    TransferBean submitTransfer(TransferMapBean transfer, UserSessionBean user);

    Map<String, Object> compareTransfer(TransferBean transfer);

    List<TransferItemBean> allItemInTransfer(long transfer_in_id);

    List<TransferItemBean> getItemsInPackage(long package_id);

    String getMapTarget(long transferId);

    Map<String,List<TransferItemBean>> allItemInMap(long transfer_in_id);

    byte[] downloadTransferList(String transfer_id, UserSessionBean user);

    byte[] downloadClientShipment(String param, UserSessionBean user);

    byte[] downloadTransferCompare(String param, UserSessionBean user);
}
