package com.voyageone.batch.cms.dao.feed;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.bean.SuperFeedGiltBean;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Jonas, 2/3/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class GiltFeedDao extends BaseDao {
    /**
     * 获取 mapper 的 namespace，只在初始化时调用
     */
    @Override
    protected String namespace() {
        return Constants.getDaoNameSpace(SubSystem.CMS);
    }

    public int insertListTemp(List<SuperFeedGiltBean> feedGiltBeanList) {
        return insert("cms_zz_worktable_gilt_superfeed_insertList", parameters("items", feedGiltBeanList));
    }

    public int insertTemp(SuperFeedGiltBean giltSku) {
        return insert("cms_zz_worktable_gilt_superfeed_insert", giltSku);
    }

    public SuperFeedGiltBean selectBySku(Long sku) {
        return selectOne("cms_zz_worktable_gilt_superfeed_full_select", parameters("product_id", sku));
    }

    public int[] updateFlg() {

        int insertingCount = update("cms_zz_worktable_gilt_superfeed_full_updateFlgToInsert1", null);

        int updatingCount = update("cms_zz_worktable_gilt_superfeed_full_updateFlgToUpdate", null);

        insertingCount += update("cms_zz_worktable_gilt_superfeed_full_updateFlgToInsert2", null);

        return new int[]{insertingCount, updatingCount};
    }

    public int[] appendInserting() {

        int deleteCount = delete("cms_zz_worktable_gilt_superfeed_full_appendInserting1", null);

        int insertCount = insert("cms_zz_worktable_gilt_superfeed_full_appendInserting2", null);

        return new int[]{deleteCount, insertCount};
    }

    public List<SuperFeedGiltBean> selectByUpdateFlg(String updateFlag) {
        return selectList("cms_zz_worktable_gilt_superfeed_full_selectByUpdateFlg", parameters("updateFlag", updateFlag));
    }

    public int[] updateInsertSuccess(List<String> modelFailList, List<String> productFailList) {
        Map<String, Object> params = parameters(
                "success_status", SuperFeedGiltBean.INSERTED,
                "target_status", SuperFeedGiltBean.INSERTING,
                "modelFailList", modelFailList,
                "productFailList", productFailList);

        int count1, count2 = 0, count3 = 0;

        count1 = update("cms_zz_worktable_gilt_superfeed_full_updateInsertSuccess1", params);

        if (!modelFailList.isEmpty())
            count2 = update("cms_zz_worktable_gilt_superfeed_full_updateInsertSuccess2", params);

        if (!productFailList.isEmpty())
            count3 = update("cms_zz_worktable_gilt_superfeed_full_updateInsertSuccess3", params);

        return new int[] { count1, count2, count3 };
    }

    public int clearTemp() {
        return delete("cms_zz_worktable_gilt_superfeed_clear", null);
    }
}
