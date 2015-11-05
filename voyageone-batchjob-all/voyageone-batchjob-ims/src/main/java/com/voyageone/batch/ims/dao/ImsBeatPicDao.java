package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.ims.service.beat.ImsBeatImageInfo;
import com.voyageone.batch.ims.bean.BeatPicBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 价格披露数据操作
 *
 * Created by sky on 20150814.
 */
@Repository
public class ImsBeatPicDao extends BaseDao {

    private final static String targets = "1,2,3,4,5";

    @Override
    protected String namespace() {
        return "com.voyageone.ims.sql";
    }

    /**
     * 获取状态为 beatFlg 的记录。
     *
     * @param limit 限定的行数
     * @return 等待打图和等待恢复图片的数据集合
     */
    public List<BeatPicBean> getLimitedBeatInfo(int limit) {
        return selectList("ims_beatPic_getBeatPicInfo", parameters("limit", limit));
    }

    /**
     * 更新信息到 Beat Item
     *
     * !!! 只更新 Comment 和 Flg
     */
    public int updateItem(BeatPicBean beatPicBean) {
        return update("ims_bt_beat_item_updateItem", beatPicBean);
    }

    /**
     * 从 cms 获取简单的图片信息
     */
    public List<ImsBeatImageInfo> getImageInfo(BeatPicBean beatPicBean) {
        // targets 的应用场景参见 mybatis sql 的定义
        return selectList("cms_bt_product_selectImageInfo", parameters(
                "beat", beatPicBean,
                "targets", targets,
                "image_type_id", 1));
    }
}
