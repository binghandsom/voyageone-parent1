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

    boolean delete(int transferId, String modified);

    TransferBean get(int transferId);

    Map<String, Object> allConfigs(UserSessionBean user);

    int getSearchCount(String po, int Store, String status, String from, String to, List<ChannelStoreBean> channelStoreList);

    TransferBean update(TransferMapBean bean, UserSessionBean user);

    TransferBean insert(TransferMapBean bean, UserSessionBean user);

    List<TransferDetailBean> getPackages(int transfer_id);

    TransferDetailBean getPackage(int transferId, String packageName);

    TransferDetailBean createPackage(int transferId, String packageName, UserSessionBean user) throws BusinessException;

    boolean deletePackage(int package_id, String modified);

    boolean reOpenPackage(int package_id, String modified);

    String addItem(int package_id, String barcode, int num, UserSessionBean user);

    boolean closePackage(int package_id, String modified, UserSessionBean user);

    TransferBean save(TransferMapBean mapBean, UserSessionBean user);

    Map<String, Object> doListInit(UserSessionBean user);

    TransferBean submitTransfer(TransferMapBean transfer, UserSessionBean user);

    List<TransferItemBean> allItemInTransfer(int transfer_in_id);

    List<TransferItemBean> getItemsInPackage(int package_id);

    String getMapTarget(int transferId);

    Map<String,List<TransferItemBean>> allItemInMap(int transfer_in_id);

    byte[] downloadTransferList(String transfer_id, UserSessionBean user);
}
