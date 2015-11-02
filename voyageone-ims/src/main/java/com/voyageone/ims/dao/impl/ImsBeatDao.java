package com.voyageone.ims.dao.impl;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.ims.model.ImsBeatInfo;
import com.voyageone.ims.model.ImsBeatItem;
import com.voyageone.ims.model.ImsBeatItemTemp;
import com.voyageone.ims.model.ImsBeat;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 价格披露任务相关表
 * Created by Jonas on 8/6/15.
 */
@Repository
public class ImsBeatDao extends BaseDao {

    @Override
    protected String namespace() {
        return "com.voyageone.ims.sql";
    }

    public ImsBeat selectByFinger(ImsBeat beat) {
        return selectOne("ims_bt_beat_selectByFinger", beat);
    }

    public int insert(ImsBeat beat) {
        return insert("ims_bt_beat_insert", beat);
    }

    public int selectCountByDescription(ImsBeat beat) {
        return selectOne("ims_bt_beat_selectCountByDescription", beat);
    }

    public ImsBeat selectById(long beat_id) {
        return selectOne("ims_bt_beat_selectById", parameters("beat_id", beat_id));
    }

    public int insertTempItems(List<ImsBeatItem> items) {
        return insert("ims_bt_beat_item_temp_insertTempItems", parameters("items", items));
    }

    public List<ImsBeatItemTemp> selectProductsByCode(long beat_id) {
        return selectList("ims_bt_beat_item_temp_selectProductsByCode", parameters("beat_id", beat_id, "now", DateTimeUtil.getNow()));
    }

    public int deleteTempItems(long beat_id) {
        return delete("ims_bt_beat_item_temp_deleteTempItems", parameters("beat_id", beat_id));
    }

    public List<ImsBeatItemTemp> selectProductsByNumiid(long beat_id) {
        return selectList("ims_bt_beat_item_temp_selectProductsByNumiid", parameters("beat_id", beat_id, "now", DateTimeUtil.getNow()));
    }

    public List<ImsBeatItem> selectItems(long beat_id) {
        return selectList("ims_bt_beat_item_selectItems_byBeatId", parameters("beat_id", beat_id));
    }

    public List<ImsBeatItem> selectItems(long beat_id, Integer beat_flg, String num_iid, int offset, int limit) {
        return selectList("ims_bt_beat_item_selectItems_byBeatId", parameters(
                "beat_id", beat_id,
                "num_iid", num_iid,
                "beat_flg", beat_flg,
                "offset", offset,
                "limit", limit));
    }

    public int selectItemsCount(long beat_id, Integer beat_flg, String num_iid) {
        return selectOne("ims_bt_beat_item_selectItemsCount_byBeatId", parameters(
                "beat_id", beat_id,
                "num_iid", num_iid,
                "beat_flg", beat_flg));
    }

    public int selectCountInRunning(long beat_id) {
        return selectOne("ims_bt_beat_item_selectCountInRunning",
                parameters("beat_id", beat_id, "now", new Date()));
    }

    public <T extends ImsBeatItem> int insertItems(List<T> subList) {
        return insert("ims_bt_beat_item_insertItems", parameters("items", subList));
    }

    public int deleteItems(ImsBeat beat) {
        return delete("ims_bt_beat_item_deleteItems", beat);
    }

    public List<ImsBeat> selectByChannel(String channel_id, String cart_id, int offset, int limit) {
        return selectList("ims_bt_beat_selectByChannel", parameters(
                "channel_id", channel_id,
                "cart_id", cart_id,
                "offset", offset,
                "limit", limit));
    }

    public int selectCountByChannel(String channel_id, String cart_id) {
        return selectOne("ims_bt_beat_selectCountByChannel", parameters(
                "channel_id", channel_id,
                "cart_id", cart_id));
    }

    public ImsBeatItem selectItem(String item_id, long beat_id) {
        return selectOne("ims_bt_beat_item_selectItem", parameters("beat_id", beat_id, "item_id", item_id));
    }

    public int updateBeatFlg(ImsBeatItem item) {
        return update("ims_bt_beat_item_updateBeatFlg", item);
    }

    public List<ImsBeatInfo> selectInfo(ImsBeat beat) {
        return selectList("ims_bt_beat_item_selectInfo", beat);
    }
}
