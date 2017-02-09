package com.voyageone.web2.cms.views.biReport.consult;

import com.voyageone.common.PageQueryParameters;
import com.voyageone.service.dao.report.BiReportDownloadTaskDao;
import com.voyageone.service.dao.report.BiReportSalesProduct010Dao;
import com.voyageone.service.dao.report.BiReportSalesShop010Dao;
import com.voyageone.service.daoext.report.BiReportDownloadTaskDaoExt;
import com.voyageone.service.daoext.report.BiReportSalesShop010DaoExt;
import com.voyageone.service.model.report.BiReportDownloadTaskModel;
import com.voyageone.service.model.report.ShopSalesOfChannel010Model;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dell on 2017/1/9.
 */
@Service
public class BiRepConsultService {
    private final String filePath="E://bi_report//xlsxFile//";
    @Autowired
    private BiReportDownloadTaskDaoExt  biReportDownloadTaskDaoExt;
    @Autowired
    private BiReportDownloadTaskDao biReportDownloadTaskDao;
    @Autowired
    private DateUtilHelper dateUtilHelper;
    @Autowired
    private BiReportSalesProduct010Dao biReportSalesProduct010Dao;
    @Autowired
    private BiReportSalesShop010Dao biReportSalesShop010Dao;
    @Autowired
    private BiRepExcelFileCreator biRepExcelFileCreator;
    @Autowired
    private BiReportSalesShop010DaoExt biReportSalesShop010DaoExt;

    public List<BiReportDownloadTaskModel> getDownloadTaskList(Map map)
    {
        List<BiReportDownloadTaskModel> taskModelList=biReportDownloadTaskDaoExt.selectNoTasksByCreatorId(map);
        return taskModelList;
    }

    /**
     * get data list count for page function
     * @param parameters
     * @return
     */
    public long getCount(PageQueryParameters parameters)
    {
        return biReportDownloadTaskDaoExt.selectCount(parameters.getSqlMapParameter());
    }
    /**
     * get data list  for page function
     * @param parameters
     * @return
     */
    public List<BiReportDownloadTaskModel> getPage(PageQueryParameters parameters)
    {
        return biReportDownloadTaskDaoExt.selectPage(parameters.getSqlMapParameter());
    }
    public Map<String,Object> dealTheFileCreateRequest(Map<String, Object> params)
    {
        String fileName=NameCreator.createName(params);
        params.put("fileName",fileName);

        BiReportDownloadTaskModel model=new BiReportDownloadTaskModel();
        model.setCreatorId((Integer) params.get("creatorId"));
        model.setCreateTime((Date) params.get("createTime"));
        model.setFilePath(filePath);
        model.setFileName(fileName);
        model.setTaskStatus(ISheetInfo.SHEET.BASICINFO.CREATING);
        model.setCreatorName((String) params.get("creatorName"));
        biReportDownloadTaskDao.insert(model);
        return params;
    }
    public Integer outTimeTask(Integer taskId){
        BiReportDownloadTaskModel model=biReportDownloadTaskDao.select(taskId);
        model.setTaskStatus(ISheetInfo.SHEET.BASICINFO.OUTTIME);
        return biReportDownloadTaskDao.update(model);
    }
    /**
     *
     * @param channelId chanelid
     * @param language 语言
     * @return mast数据
     */
    public Map<String, Object> init(String channelId, String language) {
        Map<String, Object> result = new HashMap<>();
        return result;
    }
}
