package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.CmsBtBeatInfoBean;
import com.voyageone.service.model.cms.enums.jiagepilu.BeatFlag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by jonasvlag on 16/2/29.
 * 转换原 class 形式到 interface 形式, by Jonas on 2016-05-24 11:12:58
 *
 * @version 2.1.0
 * @since 2.0.0
 */
@Repository
public interface CmsBtBeatInfoDaoExt {

    int insertList(@Param("list") List<CmsBtBeatInfoBean> modelList);

    int updateNoCodeMessage(@Param("taskId") int taskId, @Param("synFlag") int synFlag, @Param("message") String message);

    int updateNoNumiidMessage(@Param("taskId") int taskId, @Param("synFlag") int synFlag, @Param("message") String message);

    int updateCodeNotMatchNumiidMessage(@Param("taskId") int taskId, @Param("synFlag") int synFlag, @Param("message") String message);

    List<CmsBtBeatInfoBean> selectListByTask(@Param("taskId") int taskId);

    List<CmsBtBeatInfoBean> selectListByTaskWithPrice(
            @Param("taskId") int taskId, @Param("flag") BeatFlag flag, @Param("searchKey") String searchKey,
            @Param("offset") int offset, @Param("size") int size);

    int selectListByTaskCount(@Param("taskId") int taskId, @Param("flag") BeatFlag flag, @Param("searchKey") String searchKey);

    int deleteByTask(@Param("taskId") int taskId);

    int selectCountInFlags(@Param("taskId") int taskId, @Param("flags") BeatFlag... flags);

    int updateFlag(CmsBtBeatInfoBean beatInfoModel);

    int updateFlags(@Param("taskId") int taskId, @Param("synFlag") int flag, @Param("imageStatus") int imageStatus,
                    @Param("force") boolean force, @Param("modifier") String userName);

    List<Map<String, Object>> selectSummary(@Param("taskId") int taskId);

    List<CmsBtBeatInfoBean> selectListByNumiidInOtherTask(@Param("promotionId") int promotionId, @Param("taskId") int taskId, @Param("numIid") String numIid);

    CmsBtBeatInfoBean selectOneByNumiid(@Param("taskId") int taskId, @Param("numIid") String numIid);

    int updateCode(CmsBtBeatInfoBean model);

    /**
     * 查询在特定条件下的, 需要处理的价格披露信息
     *
     * @param limit TOP 行数
     * @return 带有 Promotion_Code 和 Task 信息 CmsBtBeatInfoModel
     */
    List<CmsBtBeatInfoBean> selectListNeedBeatFullData(
            @Param("limit") int limit, @Param("upFlag") int upFlag, @Param("revertFlag") int revertFlag,
            @Param("downFlag") int downFlag, @Param("now") Date now, @Param("cartIds")List<Integer> cartIds);

    int updateFlagAndMessage(CmsBtBeatInfoBean beatInfoModel);
}
