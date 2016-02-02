package com.voyageone.batch.wms.service.inventory.sync;

import com.google.gson.Gson;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.wms.dao.InventoryDao;
import com.voyageone.batch.wms.modelbean.InventorySynLogBean;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ShopConfigEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 为同步库存的 主服务 和 子服务，提供统一的辅助功能
 * <br />
 * Created by jonas on 15/6/10.
 */
public abstract class WmsSyncInventoryBaseService extends BaseTaskService {
    private final static String SYN_FLAG_IGNORE = "4";

    private final static String SYN_FLAG_PASS = "1";

    @Autowired
    protected InventoryDao inventoryDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "wmsSyncInventory";
    }

    @Override
    protected abstract void onStartup(List<TaskControlBean> taskControlList) throws Exception;

    /**
     * 如果 sync_inventory_flg 配置正是字符串 0。则说明需要同步，否则都不同步
     *
     * @param shopBean 店铺
     * @return boolean
     */
    protected boolean needSync(ShopBean shopBean) {
        String value = ShopConfigs.getVal1(shopBean, ShopConfigEnums.Name.sync_inventory_flg);

        return value.equals("1");
    }

    /**
     * 移动数据到备份数据表
     */
    private void moveData(InventorySynLogBean inventorySynLogBean) {

        // 删除同步记录，以及同步记录之前的记录（同一店铺、SKU）
        inventoryDao.deleteInventorySynLog(inventorySynLogBean);

        // 无论有没有删除成功，都插入历史记录。
        // 如果删除不成功，可以根据历史找出删除

        inventorySynLogBean.setModifier(getTaskName());

        inventoryDao.insertInventorySynLogHistory(inventorySynLogBean);
    }

    /**
     * 移动因某些原因忽略的数据
     */
    protected void moveIgnore(InventorySynLogBean inventorySynLogBean, String remarks) {
        inventorySynLogBean.setSyn_flg(SYN_FLAG_IGNORE);
        inventorySynLogBean.setRemarks(remarks == null ? Constants.EmptyString : remarks);

        moveData(inventorySynLogBean);
    }

    /**
     * 移动已完成的数据到备份数据表
     */
    protected void movePass(InventorySynLogBean inventorySynLogBean) {
        inventorySynLogBean.setSyn_flg(SYN_FLAG_PASS);

        moveData(inventorySynLogBean);
    }

    /**
     * 更新已刷新的数据的标志位
     */
    protected void updateJMFlg(InventorySynLogBean inventorySynLogBean) {
        inventorySynLogBean.setSyn_flg(SYN_FLAG_PASS);
        inventorySynLogBean.setModifier(getTaskName());

        inventoryDao.updateJMFlg(inventorySynLogBean);
    }

    /**
     * 记录失败的记录
     *
     * @param e       失败的异常
     * @param attJson 附加的信息
     */
    protected void logFailRecord(Exception e, Object attJson) {
        String json = attJson == null ? "" : new Gson().toJson(attJson);
        issueLog.log(e, ErrorType.BatchJob, getSubSystem(), json);
    }

    /**
     * 记录失败的行记录
     */
    protected void logFailRecord(String msg, Object attJson) {
        String json = attJson == null ? "" : new Gson().toJson(attJson);
        issueLog.log(getTaskName(), msg, ErrorType.BatchJob, getSubSystem(), json);
    }
}
