package com.voyageone.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.wms.formbean.FormBackorder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**   
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.dao]  
 * @ClassName    [BackorderDao]
 * @Description  [BackorderDao类]
 * @Author       [sky]   
 * @CreateDate   [20150528]
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]  
 * @Version      [v1.0] 
 */
@Repository
public class BackorderDao extends BaseDao {

    /**
     * @Description 获得backorderList的大小
     * @param formBackorder 对象
     * @return list<FormStocktake>
     */
    public int getBackorderInfoListSize(FormBackorder formBackorder) {
        return (int) selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_backorder_getBackorderListSize", formBackorder);
    }

    /**
     * @Description 获得backorderList
     * @param formBackorder 对象
     * @return list<formBackorder>
     */
    public List<FormBackorder> getBackorderInfoList(FormBackorder formBackorder) {
        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_backorder_getBackorderList", formBackorder);
    }

    /**
     * @Description 插入backorder对象
     * @param formBackorder 对象
     * @return list<formBackorder>
     */
    public boolean insertBackOrderInfo(FormBackorder formBackorder) {
        int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_backorder_insertBackorderInfo", formBackorder);
        return retCount > 0;
    }

    public boolean deleteBackOrderInfo(FormBackorder formBackorder) {
        int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_backorder_updateBackorderInfo", formBackorder);
        return retCount > 0;
    }

    /**
     * @Description 根据物品ID判断表中是否已有重复记录
     * @param reservationID 物品ID
     * @return int
     */
    public int isExistsSkuByReservationID(long reservationID) {
        return  selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_backorder_isExistsSkuByReservationID", reservationID);
    }

    /**
     * @Description 根据物品ID对应仓库的库存管理类型
     * @param reservationID 物品ID
     * @return String
     */
    public String getStoreInventoryManager(long reservationID) {
        return  selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_store_getStoreInventoryManager", reservationID);
    }

    /**
     * @Description 根据物品ID插入表
     * @param reservationID 物品ID
     * @param userName 更新者
     * @return int
     */
    public int insertSkuByReservationID(long reservationID, String userName) {

        Map<String, Object> params = new HashMap<>();

        params.put("reservationID", reservationID);
        params.put("userName", userName);

        return  updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_backorder_insertSkuByReservationID", params);
    }

    /**
     * @Description 根据物品ID删除表
     * @param reservationID 物品ID
     * @param userName 更新者
     * @return int
     */
    public int deleteSkuByReservationID(long reservationID, String userName) {

        Map<String, Object> params = new HashMap<>();

        params.put("reservationID", reservationID);
        params.put("userName", userName);

        return  updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_backorder_deleteSkuByReservationID", params);
    }

    /**
     * @Description 查看sku是否有效
     * @param formBackorder 物品ID
     * @return int
     */
    public int checkSku(FormBackorder formBackorder) {
        return  selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_backorder_skuIsExist", formBackorder);
    }
}
