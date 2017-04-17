package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsMtFeedConfigBean;
import com.voyageone.service.dao.cms.CmsMtFeedConfigDao;
import com.voyageone.service.dao.cms.CmsMtFeedConfigInfoDao;
import com.voyageone.service.daoext.cms.CmsMtFeedConfigDaoExt;
import com.voyageone.service.daoext.cms.CmsMtFeedConfigInfoDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.com.cache.CommCacheControlService;
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
    @Autowired
    private CommCacheControlService cacheControlService;

    /**
     * Feed配置项目管理*数据初始化
     */
    public Map<String, Object> search(String channelId, String userName) {
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
                cmsMtFeedConfigBean.setCmsIsCfgVal2Display(bean.getCmsIsCfgVal2Display());
                cmsMtFeedConfigBean.setCmsIsCfgVal3Display(bean.getCmsIsCfgVal3Display());
                cmsMtFeedConfigBean.setCreated(bean.getCreated());
                cmsMtFeedConfigBean.setModifier(bean.getModifier());
                cmsMtFeedConfigBean.setModified(bean.getModified());
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
        cacheControlService.deleteCache(CacheKeyEnums.KeyEnum.ConfigData_FeedConfigs);
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
        //循环取得页面数据插入到数据库里
        for (CmsMtFeedConfigInfoModel model : cmsMtFeedConfigInfoModelList) {
            CmsMtFeedConfigInfoModel cmsMtFeedConfigInfoModel = new CmsMtFeedConfigInfoModel();
            cmsMtFeedConfigInfoModel.setId(model.getId());
            cmsMtFeedConfigInfoModel.setOrderChannelId(channelId);
            cmsMtFeedConfigInfoModel.setCfgTableName(model.getCfgTableName());
            cmsMtFeedConfigInfoModel.setCfgName(model.getCfgName());
            cmsMtFeedConfigInfoModel.setCfgIsAttribute(model.getCfgIsAttribute());
            cmsMtFeedConfigInfoModel.setCreater(model.getCreater());
            cmsMtFeedConfigInfoModel.setCreated(model.getCreated());
            cmsMtFeedConfigInfoModel.setModifier(userName);
            cmsMtFeedConfigInfoModel.setModified(new Date());
            isUpdateAndInsert(cmsMtFeedConfigInfoModel, userName);
        }
    }

    /**
     * Feed属性一览*feed表保存按钮
     */
    public void createFeed(HashMap<String, Object> map, String channelId, String userName) {
        //取得表名称
        String tableName = (String) map.get("tableName");

        if (StringUtils.isEmpty(tableName)) {
            throw new BusinessException("表名必须填写");
        }
        //取得表结
        List<HashMap> cmsMtFeedConfigInfoModelList = (List<HashMap>) map.get("feedList");
        List<String> cfgTableNameColumn = new ArrayList<>();
        Boolean isSku = true;
        Boolean isCategory = true;
        Boolean isDbSku = true;
        Boolean isDbCategory = true;
        for (HashMap modelHashMap : cmsMtFeedConfigInfoModelList) {
            String name = (String) modelHashMap.get("cfgTableName");
            //判断表结构是否填写
            if (StringUtils.isEmpty(name))
                throw new BusinessException("Feed表结构名称必须填写");
            cfgTableNameColumn.add(name);
            //取得sku
            if ("sku".equals(name)) {
                isSku = false;
            }
            //取得category
            if ("category".equals(name)) {
                isCategory = false;
            }
            if (StringUtils.isDigit(name)){
                throw new BusinessException("Feed表结构名称不能为数字");
            }
            if (!StringUtils.isEmpty((String) modelHashMap.get("cfgIsAttribute"))) {
                String cfgIsAttribute = (String) modelHashMap.get("cfgIsAttribute");
                if ("Y".equals(cfgIsAttribute)) {
                    CmsMtFeedConfigModel cmsMtFeedConfigModel = new CmsMtFeedConfigModel();
                    cmsMtFeedConfigModel.setOrderChannelId(channelId);
                    cmsMtFeedConfigModel.setCfgName("attribute");
                    cmsMtFeedConfigModel.setCfgVal1((String) modelHashMap.get("cfgTableName"));
                    cmsMtFeedConfigModel.setModifier(userName);
                    cmsMtFeedConfigModel.setModified(new Date());
                    cmsMtFeedConfigModel.setDisplaySort(-1);
                    cmsMtFeedConfigModel.setStatus(1);
                    cmsMtFeedConfigModel.setIsAttribute(0);
                    cmsMtFeedConfigModel.setAttributeType(0);
                    cmsMtFeedConfigDao.insert(cmsMtFeedConfigModel);
                }
            }
        }
        List<CmsMtFeedConfigInfoModel> cmsMtFeedConfigInfoList = cmsMtFeedConfigInfoDaoExt.selectFeedConFigInfo(channelId);
        if(cmsMtFeedConfigInfoList.size()==0)throw new BusinessException("请按属性保存按钮");
        for(CmsMtFeedConfigInfoModel model:cmsMtFeedConfigInfoList){
            if(("sku".equals(model.getCfgTableName()))){
                isDbSku = false;
            }
            if(("category".equals(model.getCfgTableName()))){
                isDbCategory = false;
            }
        }
        if (isSku) {
            throw new BusinessException("Feed表结构名称无sku");
        }
        if (isCategory) {
            throw new BusinessException("Feed表结构名称无category");
        }
        if (isDbSku) {
            throw new BusinessException("请按属性保存按钮,数据库无sku");
        }
        if (isDbCategory) {
            throw new BusinessException("请按属性保存按钮,数据库无category");
        }
        //取得表结构的列称
        String[] columns = new String[cfgTableNameColumn.size()];
        int i = 0;
        for (String column : cfgTableNameColumn) {
            columns[i++] = column;
        }
        //生成表
        if (columns.length > 0) {
            Map<Object, Object> params = new HashMap<>();
            params.put("keys", columns);
            params.put("tableName", "voyageone_cms2." + "cms_zz_feed_" + tableName + "_product_temp");
            cmsMtFeedConfigInfoDaoExt.createdTable(params);
            params.put("tableName", "voyageone_cms2." + "cms_zz_feed_" + tableName + "_product_full");
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
                if (isHeader) {
                    colIndex = 0;
                    //Title行*文件Title判断
                    isHeader = false;
                    String id = row.getCell(colIndex++).getStringCellValue();
                    String cfgName = row.getCell(colIndex++).getStringCellValue();
                    String cfgIsAttribute = row.getCell(colIndex++).getStringCellValue();
                    String cfgTableName = row.getCell(colIndex++).getStringCellValue();
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
                    colIndex = 0;
                    CmsMtFeedConfigInfoModel model = new CmsMtFeedConfigInfoModel();
                    row.getCell(colIndex).setCellType(Cell.CELL_TYPE_STRING);
                    if (row.getCell(colIndex) == null) {
                        model.setCfgName("");
                    } else {
                        model.setId(Integer.parseInt(row.getCell(colIndex).getStringCellValue()));
                    }
                    model.setOrderChannelId(channelId);
                    row.getCell(colIndex++).setCellType(Cell.CELL_TYPE_STRING);
                    if (row.getCell(colIndex) == null) {
                        model.setCfgName("");
                    } else {
                        model.setCfgName(row.getCell(colIndex).getStringCellValue());
                    }
                    row.getCell(colIndex++).setCellType(Cell.CELL_TYPE_STRING);
                    if (row.getCell(colIndex) == null) {
                        model.setCfgIsAttribute("");
                    } else {
                        model.setCfgIsAttribute(row.getCell(colIndex).getStringCellValue());
                    }
                    row.getCell(colIndex++).setCellType(Cell.CELL_TYPE_STRING);
                    if (row.getCell(colIndex) == null) {
                        model.setCfgTableName("");
                    } else {
                        model.setCfgTableName(row.getCell(colIndex).getStringCellValue());
                    }

                    model.setModifier(userName);
                    model.setModified(new Date());
                    //判断excel的数据是插入还是更新
                    isUpdateAndInsert(model, userName);
                }
            }
        } catch (Exception e) {
            throw new BusinessException("读取excel异常");
        }
    }

    public void isUpdateAndInsert(CmsMtFeedConfigInfoModel model, String userName) {
        int cnt;
        if (model.getId() == null) {
            cnt = 0;
        } else {
            cnt = cmsMtFeedConfigInfoDaoExt.selectFeedConFigInfoCnt(model.getId());
        }
        if (cnt == 0) {
            //插入
            model.setId(null);
            model.setCreated(new Date());
            model.setCreater(userName);
            cmsMtFeedConfigInfoDao.insert(model);
        } else {
            //更新
            cmsMtFeedConfigInfoDao.updateByPrimaryKeySelective(model);
        }
    }
}
