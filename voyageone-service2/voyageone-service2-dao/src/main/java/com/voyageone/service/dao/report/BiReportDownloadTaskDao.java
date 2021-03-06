/*
 * BiReportDownloadTaskDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.report;

import com.voyageone.service.model.report.BiReportDownloadTaskModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface BiReportDownloadTaskDao {
    List<BiReportDownloadTaskModel> selectList(Object map);

    BiReportDownloadTaskModel selectOne(Object map);

    int selectCount(Object map);

    BiReportDownloadTaskModel select(Integer id);

    int insert(BiReportDownloadTaskModel record);

    int update(BiReportDownloadTaskModel record);

    int delete(Integer id);
}