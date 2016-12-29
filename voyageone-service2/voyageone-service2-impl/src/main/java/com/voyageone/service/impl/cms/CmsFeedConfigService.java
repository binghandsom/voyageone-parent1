package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsMtFeedConfigBean;
import com.voyageone.service.dao.cms.CmsMtFeedConfigDao;
import com.voyageone.service.dao.cms.CmsMtFeedConfigInfoDao;
import com.voyageone.service.daoext.cms.CmsMtFeedConfigDaoExt;
import com.voyageone.service.daoext.cms.CmsMtFeedConfigInfoDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtFeedConfigInfoModel;
import com.voyageone.service.model.cms.CmsMtFeedConfigModel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@Service
public class CmsFeedConfigService extends BaseService {
    @Autowired
    private CmsMtFeedConfigDaoExt cmsMtFeedConfigDaoExt;
    @Autowired
    private CmsMtFeedConfigInfoDaoExt cmsMtFeedConfigInfoDaoExt;
    @Autowired
    private CmsMtFeedConfigDao cmsMtFeedConfigDao;
    @Autowired
    private CmsMtFeedConfigInfoDao cmsMtFeedConfigInfoDao;

    /**
     * Feed配置项目管理*数据初始化
     */
    public Map<String, Object> search(String channelId) {
        Map<String, Object> resultMap = new HashMap();
        //cms_mt_feed_config_key取得主数据
        List<CmsMtFeedConfigBean> cmsMtFeedConfigKeyList = cmsMtFeedConfigDaoExt.selectFeedConFigKey();
        //cfgName,channelID
        List<CmsMtFeedConfigBean> cmsMtFeedConfigList = cmsMtFeedConfigDaoExt.selectFeedConFigByChannelId(channelId);
        for (CmsMtFeedConfigBean cmsMtFeedConfigBean : cmsMtFeedConfigKeyList) {
            cmsMtFeedConfigList.stream().filter(bean -> cmsMtFeedConfigBean.getCfgName().equals(bean.getCfgName())).forEach(bean -> {
                cmsMtFeedConfigBean.setOrderChannelId(bean.getOrderChannelId());
                cmsMtFeedConfigBean.setCfgVal1(bean.getCfgVal1());
                cmsMtFeedConfigBean.setCfgVal2(bean.getCfgVal2());
                cmsMtFeedConfigBean.setCfgVal3(bean.getCfgVal3());
                cmsMtFeedConfigBean.setComment(bean.getComment());
                cmsMtFeedConfigBean.setCmsIsCfgVal1Display(bean.getCmsIsCfgVal1Display());
                cmsMtFeedConfigBean.setCmsIsCfgVal2Display(bean.getCmsIsCfgVal3Display());
                cmsMtFeedConfigBean.setCmsIsCfgVal3Display(bean.getCmsIsCfgVal3Display());
            });
        }
        //Feed-Master属性一览
        resultMap.put("feedConfigList", cmsMtFeedConfigKeyList);
        //Feed属性一览
        List<CmsMtFeedConfigInfoModel> cmsMtFeedConfigInfoList = cmsMtFeedConfigInfoDaoExt.selectFeedConFigInfo(channelId);
        resultMap.put("feedList", cmsMtFeedConfigInfoList);

        return resultMap;
    }

    /**
     * Feed-Master属性一览*保存按钮
     */
    public void save(List<CmsMtFeedConfigBean> info, String channelId, String userName) {
        //循环数据到数据库里面检索根据channelId删除
        cmsMtFeedConfigDaoExt.deleteFeedConFigByChannelId(channelId);
        //循环取得页面数据插入到数据库里
        for (CmsMtFeedConfigBean cmsMtFeedConfigBean : info) {
            CmsMtFeedConfigModel cmsMtFeedConfigModel = new CmsMtFeedConfigModel();
            cmsMtFeedConfigModel.setOrderChannelId(channelId);
            cmsMtFeedConfigModel.setCfgName(cmsMtFeedConfigBean.getCfgName());
            cmsMtFeedConfigModel.setCfgVal2(cmsMtFeedConfigBean.getCfgVal2());
            cmsMtFeedConfigModel.setCfgVal3(cmsMtFeedConfigBean.getCfgVal3());
            cmsMtFeedConfigModel.setIsAttribute(0);
            cmsMtFeedConfigModel.setAttributeType(0);
            cmsMtFeedConfigModel.setComment(cmsMtFeedConfigBean.getComment());
            cmsMtFeedConfigModel.setDisplaySort(-1);
            cmsMtFeedConfigModel.setModifier(userName);
            cmsMtFeedConfigModel.setModified(new Date());
            cmsMtFeedConfigModel.setStatus(1);
            cmsMtFeedConfigModel.setCfgVal1(cmsMtFeedConfigBean.getCfgVal1());
            cmsMtFeedConfigDao.insert(cmsMtFeedConfigModel);
        }
    }

    /**
     * Feed属性一览*删除按钮
     */
    public void delete(int id) {
        cmsMtFeedConfigInfoDao.deleteByPrimaryKey(id);
    }

    /**
     * Feed属性一览*feed属性保存按钮
     */
    public void saveFeed(List<CmsMtFeedConfigInfoModel> cmsMtFeedConfigInfoModelList, String channelId, String userName) {
        //循环数据到数据库里面检索根据channelId删除
        cmsMtFeedConfigDaoExt.deleteFeedConFigInfoByChannelId(channelId);
        //循环取得页面数据插入到数据库里
        for (CmsMtFeedConfigInfoModel model : cmsMtFeedConfigInfoModelList) {
            CmsMtFeedConfigInfoModel cmsMtFeedConfigInfoModel = new CmsMtFeedConfigInfoModel();
            cmsMtFeedConfigInfoModel.setOrderChannelId(channelId);
            cmsMtFeedConfigInfoModel.setCfgTableName(model.getCfgTableName());
            cmsMtFeedConfigInfoModel.setCfgName(model.getCfgName());
            cmsMtFeedConfigInfoModel.setCfgIsAttribute(model.getCfgIsAttribute());
            cmsMtFeedConfigInfoModel.setModifier(userName);
            cmsMtFeedConfigInfoModel.setModified(new Date());
            cmsMtFeedConfigInfoDao.insert(cmsMtFeedConfigInfoModel);
        }
    }

    /**
     * Feed属性一览*feed表保存按钮
     */
    public void createFeed(HashMap<String, Object> map) {
        //取得表名称
        String tableName = (String) map.get("tableName");

        if (StringUtils.isEmpty(tableName)) {
            throw new BusinessException("表名必须填写");
        }
        //取得表结
        List<HashMap> cmsMtFeedConfigInfoModelList = (List<HashMap>) map.get("feedList");

        List<String> cfgTableNameColumn = new ArrayList<>();
        cmsMtFeedConfigInfoModelList.stream()
                .filter(modelHashMap -> !StringUtils.isEmpty((String) modelHashMap.get("cfgTableName")))
                .forEach(modelHashMap -> cfgTableNameColumn.add((String) modelHashMap.get("cfgTableName")));
        //取得表结构的列称
        String[] columns = new String[cfgTableNameColumn.size()];
        int i = 0;
        for (String column : cfgTableNameColumn) {
            columns[i++] = column;
        }
        //生成表
        if (columns.length > 0) {
            Map<Object, Object> params = new HashMap<>();
            params.put("tableName", "voyageone_cms2." + tableName);
            params.put("keys", columns);
            cmsMtFeedConfigInfoDaoExt.createdTable(params);
        }
    }

    public byte[] getExcelFileFeedInfo(String channelId) throws IOException, InvalidFormatException {
        String templatePath = "/usr/web/contents/other/third_party/feed.xlsx";
        //Feed属性一览
        List<CmsMtFeedConfigInfoModel> cmsMtFeedConfigInfoModelList = cmsMtFeedConfigInfoDaoExt.selectFeedConFigInfo(channelId);
        $info("准备打开文档 [ %s ]", templatePath);
        try (InputStream inputStream = new FileInputStream(templatePath);
             //SXSSFWorkbook
             SXSSFWorkbook book = new SXSSFWorkbook(new XSSFWorkbook(inputStream))) {
            // 数据写入EXCEL
            writeExcelStockInfoRecord(book, cmsMtFeedConfigInfoModelList);
            $info("文档写入完成");
            // 返回值设定
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                book.write(outputStream);
                $info("已写入输出流");
                return outputStream.toByteArray();
            }
        }
    }

    /**
     * 库存隔离Excel的数据写入
     */
    private void writeExcelStockInfoRecord(Workbook book, List<CmsMtFeedConfigInfoModel> modelList) {
        //取得Excel第一个sheet
        Sheet sheet = book.getSheetAt(0);
        // 行号
        int lineIndex = 1;
        // 列号
        int colIndex;
        for (CmsMtFeedConfigInfoModel model : modelList) {
            Row row = FileUtils.row(sheet, lineIndex++);
            colIndex = 0;
            //id
            FileUtils.cell(row, colIndex++, null).setCellValue(model.getId());
            //Feed属性名称
            FileUtils.cell(row, colIndex++, null).setCellValue(String.valueOf(model.getCfgName()));
            //是否作为第三方属性导入
            FileUtils.cell(row, colIndex++, null).setCellValue(String.valueOf(model.getCfgIsAttribute()));
            //feed表结构名称
            FileUtils.cell(row, colIndex++, null).setCellValue(String.valueOf(model.getCfgTableName()));
        }
    }

    /**
     * 导出excel
     */
    public void importExcelFileInfo(MultipartFile file, String userName, String channelId) {
        try {
            // 列号
            int colIndex;
            Workbook wb = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = wb.getSheetAt(0);
            boolean isHeader = true;
            for (Row row : sheet) {
                colIndex = 0;
                if (isHeader) {
                    //Title行*文件Title判断
                    isHeader = false;
                    String id= row.getCell(colIndex++).getStringCellValue();
                    String cfgName= row.getCell(colIndex++).getStringCellValue();
                    String cfgIsAttribute= row.getCell(colIndex++).getStringCellValue();
                    String cfgTableName= row.getCell(colIndex++).getStringCellValue();
                    if (!"id".equals(id)) {
                        throw new BusinessException("表格Tittle错误");
                    }
                    if (!"Feed属性名称".equals(cfgName)) {
                        throw new BusinessException("表格Tittle错误");
                    }
                    if (!"是否作为第三方属性导入".equals(cfgIsAttribute)) {
                        throw new BusinessException("表格Tittle错误");
                    }
                    if (!"feed表结构名称".equals(cfgTableName)) {
                        throw new BusinessException("表格Tittle错误");
                    }
                } else {
                    CmsMtFeedConfigInfoModel model = new CmsMtFeedConfigInfoModel();
                    model.setId((int) row.getCell(colIndex).getNumericCellValue());
                    model.setOrderChannelId(channelId);
                    row.getCell(colIndex++).setCellType(Cell.CELL_TYPE_STRING);
                    if(row.getCell(colIndex)==null){
                        model.setCfgName("");
                    }else{
                        model.setCfgName(row.getCell(colIndex).getStringCellValue());
                    }
                    row.getCell(colIndex++).setCellType(Cell.CELL_TYPE_STRING);
                    if(row.getCell(colIndex)==null){
                        model.setCfgIsAttribute("");
                    }else{
                        model.setCfgIsAttribute(row.getCell(colIndex).getStringCellValue());
                    }
                    row.getCell(colIndex++).setCellType(Cell.CELL_TYPE_STRING);
                    if(row.getCell(colIndex)==null){
                        model.setCfgTableName("");
                    }else{
                        model.setCfgTableName(row.getCell(colIndex).getStringCellValue());
                    }

                    model.setModifier(userName);
                    model.setModified(new Date());
                    //判断excel的数据是插入还是更新
                    int cnt = cmsMtFeedConfigInfoDaoExt.selectFeedConFigInfoCnt(model.getId());
                    if (cnt == 0) {
                        //插入
                        model.setId(null);
                        cmsMtFeedConfigInfoDao.insert(model);
                    } else {
                        //更新
                        cmsMtFeedConfigInfoDao.updateByPrimaryKey(model);
                    }
                }
            }
        } catch (Exception e) {
            throw new BusinessException("读取excel异常");
        }
    }
}
