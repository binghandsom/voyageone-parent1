package com.voyageone.service.daoext.report;

import com.voyageone.service.model.report.BiReportDownloadTaskModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/1/16.
 */

@Repository
public interface BiReportDownloadTaskDaoExt {
    List<BiReportDownloadTaskModel> selectTasksByCreatorId(Map map);

    /**
     * 选择和status值不为参数的的下载任务
     * @param map
     * @return
     */
    List<BiReportDownloadTaskModel> selectNoTasksByCreatorId(Map map);
}
