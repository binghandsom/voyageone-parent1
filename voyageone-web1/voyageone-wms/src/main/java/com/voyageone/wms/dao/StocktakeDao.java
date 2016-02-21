package com.voyageone.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.wms.formbean.FormStocktake;
import com.voyageone.wms.modelbean.StocktakeBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**   
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.dao]  
 * @ClassName    [StocktakeDao]
 * @Description  [StocktakeDao类]
 * @Author       [sky]   
 * @CreateDate   [20150518]
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]  
 * @Version      [v1.0] 
 */
@Repository
public class StocktakeDao extends BaseDao {

    /**
     * @Description 获得stocktakeSessionList
     * @param formStocktake bean对象
     * @return list<FormStocktake>
     */
	public List<FormStocktake> getStocktakeSessionList(FormStocktake formStocktake) {
        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_getSessionList", formStocktake);
	}

    /**
     * @Description 获得stocktakeSessionList大（分页用）
     * @param formStocktake bean对象
     * @return int
     */
    public int getSessionListSize(FormStocktake formStocktake) {
        return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_getSessionListSize", formStocktake);
    }

    /**
     * @Description 创建盘点session
     * @param formStocktake bean对象
     * @return stocktakeId
     */
    public int createSession(FormStocktake formStocktake) {
        int stocktakeId = 0;
        int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_createSession", formStocktake);
        if (retCount > 0) {
            stocktakeId = formStocktake.getStocktake_id();
        }
        return stocktakeId;
    }

    /**
     * @Description 通过storeId获取对应的orderChannelId
     * @param storeId 仓库Id
     * @return orderChannelId
     */
    public String getChannelIdByStoreId(int storeId) {
        return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_getChannelIdByStoreId", storeId);
    }

    /**
     * @Description 通过stocktake_id获取对应的sessionName
     * @param stocktake_id 盘点Id也就是SessionID
     * @return sessionName
     */
    public FormStocktake getSessionInfoBySessionId(int stocktake_id) {
        return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_getSessionInfoBySessionId", stocktake_id);
    }

    /**
     * @Description 获取sectionListSize
     * @param formStocktake bean对象
     * @return sectionListSize
     */
    public int getSectionListSize(FormStocktake formStocktake) {
        return (int) selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_getSectionListSize", formStocktake);
    }

    /**
     * @Description 获取sectionList
     * @param formStocktake bean对象
     * @return formStocktake 列表
     */
    public List<FormStocktake> getSectionListBySessionId(FormStocktake formStocktake) {
        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_getSectionList", formStocktake);
    }

    /**
     * @Description 删除Session
     * @param sessionId stocktake_id
     * @return formStocktake 列表
     */
    public boolean deleteSession(int sessionId) {
        int retCount = updateTemplate.delete(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_deleteStocktakeSession", sessionId);
        return retCount > 0;
    }

    /**
     * @Description 根据SessionId删除section
     * @param sessionId stocktakeId
     * @return formStocktake 列表
     */
    public boolean deleteSectionBySessionId(int sessionId) {
        int retCount = updateTemplate.delete(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_deleteSectionBySessionId", sessionId);
        return retCount > 0;
    }

    /**
     * @Description 根据SessionId删除session里面对应的以盘点的项
     * @param sessionId stocktakeId
     * @return formStocktake 列表
     */
    public boolean deleteStocktakeItemBySessionId(int sessionId) {
        int retCount = updateTemplate.delete(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_deleteStocktakeItemBySessionId", sessionId);
        return retCount > 0;
    }

    /**
     * @Description 根据SessionId获取stockItem大小
     * @param sessionId stocktakeId
     * @return formStocktake 列表
     */
    public int getStocktakeItemSizeBySessionId(int sessionId) {
        return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_getStocktakeItemSizeBySessionId", sessionId);
    }

    /**
     * @Description 根据SessionId检查对应的session中的section是否包含未关闭的section
     * @param sessionId stocktakeId
     * @return hasProcessingSectionFlg; true:不包含； false：包含
     */
    public boolean hasProcessingSection(int sessionId) {
        int processingSectionCount = selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_getProcessingSectionCount", sessionId);
        return processingSectionCount > 0;
    }

    /**
     * @Description 根据SessionId修改对应的Stocktake的session状态
     * @param formStocktake bean对象
     * @return stocktakeStatus; 0:Processing; 1: Stock; 2:Compare; 3:Done
     */
    public boolean changeSessionStatus(FormStocktake formStocktake) {
        int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_changeSessionStatusBySessionId", formStocktake);
        return retCount > 0;
    }

    /**
     * @Description 判断某个session里面是否包含某个section
     * @param formStocktake bean对象
     * @return sessionContainSectionFlg; true:包含; false: 不包含
     */
    public int sessionContainSectionCheck(FormStocktake formStocktake) {
        int stocktakeDetail_id;
        try {
            stocktakeDetail_id = selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_sessionContainSectionCheck", formStocktake);
        }catch (Exception e){
            stocktakeDetail_id = -1;
        }
        return stocktakeDetail_id;
    }

    /**
     * @Description 根据输入（或扫描）的locationName判断次location是否存在
     * @param formStocktake bean对象
     * @return existFlg; true:存在; false: 不存在
     */
    public int locationNameCheck(FormStocktake formStocktake) {
        int location_id;
        try {
            location_id = selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_checkLocationByName", formStocktake);
        }catch (Exception e){
            location_id = -1;
        }
        return location_id;
    }

    /**
     * @Description 创建Section
     * @param formStocktake bean对象
     * @return stocktake_detail_id; -1:创建section失败; stocktake_detail_id: 成功
     */
    public int createSection(FormStocktake formStocktake) {
        int stocktake_detail_id;
        int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_createSection", formStocktake);
        if(retCount > 0){
            stocktake_detail_id = formStocktake.getStocktake_detail_id();
        }else{
            stocktake_detail_id = -1;
        }
      return stocktake_detail_id;
    }

    /**
     * @Description 删除Section
     * @param stocktake_detail_id sectionId
     * @return successfulFlg; true: 成功; false: 失败
     */
    public boolean deleteSectionById(int stocktake_detail_id) {
        int retCount = updateTemplate.delete(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_deleteSectionById", stocktake_detail_id);
        return retCount > 0;
    }

    /**
     * @Description 获取Session，Section下对应的所有的 Item
     * @param formStocktake (需要： 1、stocktakeId 2、locationName)
     * @return formStockTake对象List
     */
    public List<FormStocktake> getSessionSectionItem(FormStocktake formStocktake) {
        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_getSessionSectionItem", formStocktake);
    }

    /**
     * @Description 获取该stocktakeId的信息
     * @param formStocktake (需要： 1、stocktakeId )
     * @return StocktakeBean
     */
    public StocktakeBean getStocktake(FormStocktake formStocktake) {
        return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_get", formStocktake);
    }


    /**
     * @Description 根据Upc获取产品信息
     * @param formStocktake bean对象
     * @return formStockTake对象
     */
    public FormStocktake getProductByUpcOrSku(FormStocktake formStocktake) {
        return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_getProductByUpcOrSku", formStocktake);
    }

    /**
     * @Description 将扫描的产品插入wms_bt_take_stock_item表
     * @param formStocktake bean对象
     * @return formStockTake对象
     */
    public boolean addSessionSectionItem(FormStocktake formStocktake) {
        int retCount =  updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_insertSessionSectionItem", formStocktake);
        return retCount > 0;
    }

    /**
     * @Description 将扫描的产品插入或者
     * @param formStocktake bean对象
     * @return formStockTake对象
     */
    public boolean updateSessionSectionItem(FormStocktake formStocktake) {
        int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_updateSessionSectionItem", formStocktake);
        return retCount > 0;
    }

    /**
     * @Description 根据stocktakeDetailId删除Item
     * @param stocktake_detail_id sectionId
     * @return true 删除成功
     */
    public boolean deleteItemByStocktakeDetailId(int stocktake_detail_id) {
        try {
            updateTemplate.delete(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_deleteItemByStocktakeDetailId", stocktake_detail_id);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * @Description 根据sessionid获取compare信息
     * @param formStocktake bean对象
     * @return true 关闭Section成功
     */
    public List<FormStocktake> getCompareInfoList(FormStocktake formStocktake) {
        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_getCompareList", formStocktake);
    }

    /**
     * @Description 根据sessionid获取compare信息
     * @param stocktake_id sessionId
     * @return true 关闭Section成功
     */
    public int getCompareListSizeBySessionId(int stocktake_id) {
        return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_getCompareListSize", stocktake_id);
    }

    /**
     * @Description 修改compare Item的 isFixed状态
     * @param formStocktake bean对象
     * @return true 更新状态成功
     */
    public boolean changeCompareStatus(FormStocktake formStocktake) {
        int count = updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_changeCompareStatus", formStocktake);
        return count > 0;
    }

    /**
     * @Description 获取sessionInfo
     * @param formStocktake bean对象
     * @return true 获取成功
     */
    public FormStocktake getSessionInfo(FormStocktake formStocktake) {
        return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_getSessionInfo", formStocktake);
    }

    /**
     * @Description 更新section
     * @param formStocktake bean对象
     * @return sessionSectionItemSize
     */
    public boolean updateSection(FormStocktake formStocktake) {
        int count = updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_updateSection", formStocktake);
        return count > 0;
    }

    public boolean isMaxStockTakeId(int stocktake_id) {
        int maxStocktakeId = selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_stocktake_getMaxStocktakeId", stocktake_id);
        return stocktake_id == maxStocktakeId;
    }

    /**
     * @Description 获得wms_report_getStocktakeSessionList给下拉框使用
     * @param paramMap 对象
     * @return list FormStocktake对象集合
     */
    public List<FormStocktake> getCompareResList(FormStocktake paramMap) {
        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_report_getCompareList", paramMap);
    }

}
