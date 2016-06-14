package com.voyageone.task2.cms.service;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.*;
import com.voyageone.common.util.ExcelUtils;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.MD5;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.*;
import com.voyageone.service.impl.cms.*;
import com.voyageone.service.model.cms.mongo.*;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 将主类目/对应的平台类目信息导入到cms_mt_category_tree表
 *
 * @author jeff.duan on 2016/6/12.
 * @version 2.1.0
 */
@Service
public class CmsImportCategoryTreeService extends BaseTaskService {

    @Autowired
    private CmsMtCategoryTreeAllDao cmsMtCategoryTreeAllDao;
    @Autowired
    private CategoryTreeAllService categoryTreeAllService;
    @Autowired
    private PlatformCategoryService platformCategoryService;

    Map<String, Map<String, CmsMtPlatformCategoryTreeModel>> platformsCategoryTreeMap = new HashMap<>();


    private final static String CATEGORY_SHEET_NAME = "主数据类目层次";
    private final static String TMALL_MAPPING_SHEET_NAME = "天猫-Master";
    private final static String JD_MAPPING_SHEET_NAME = "京东-Master";
    private final static String JUMEI_MAPPING_SHEET_NAME = "聚美-Master";

    // 主类目开始列
    private final static int CATEGORY_START_INDEX = 1;
    // 主类目的列数
    private final static int CATEGORY_MAX_LEVEL = 10;

    // 平台类目开始列
    private final static int PLATFORM_CATEGORY_START_INDEX = 12;

    @Override
    public String getTaskName() {
        return "CmsImportCategoryTreeJob";
    }

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }


    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 取得导入文件
        String filePath = com.voyageone.common.configs.Properties.readValue("CmsImportCategoryTreeService_import_file_path");
        if (StringUtils.isEmpty(filePath)) {
            filePath = "/usr/category";
        }
        File allFiles = new File(filePath);
        for (File file : allFiles.listFiles()) {
            if (file.isFile() && file.getName().toLowerCase().contains(".xlsx")) {
                importCategoryTree(file);
                // 移动文件到bak
                FileUtils.moveFile(file.getAbsolutePath(), filePath + "/bak/" + file.getName());
            }
        }
    }

    /**
     * 根据xls文件导入主类目树
     *
     */
    private void  importCategoryTree(File file) throws IOException, InvalidFormatException {

        $info("处理文件：" + file.getAbsolutePath());

        Workbook wb = null;
        try {
             wb = WorkbookFactory.create(new FileInputStream(file));
        } catch (Exception e) {
            $error("导入文件出现异常：" + e.getMessage());
            throw e;
        }

         // 导入主类目数据
        Sheet categorySheet = wb.getSheet(CATEGORY_SHEET_NAME);
        if (categorySheet!= null) {
            importCategoryData(categorySheet);
        }

        // 导入天猫国际类目与主类目的匹配关系
        Sheet tmallCategorySheet = wb.getSheet(TMALL_MAPPING_SHEET_NAME);
        if (tmallCategorySheet != null) {
            importPlatformCategoryData(tmallCategorySheet, 23);
        }

//        // 导入京东类目与主类目的匹配关系
//        Sheet jdCategorySheet = wb.getSheet(JD_MAPPING_SHEET_NAME);
//        if (jdCategorySheet != null) {
//            importPlatformCategoryData(jdCategorySheet, 26);
//        }
//
//        // 导入聚美类目与主类目的匹配关系
//        Sheet jumeiCategorySheet = wb.getSheet(JUMEI_MAPPING_SHEET_NAME);
//        if (jdCategorySheet != null) {
//            importPlatformCategoryData(jumeiCategorySheet, 27);
//        }


    }

    /**
     * 导入平台类目与主类目的匹配关系
     *
     */
    private void  importPlatformCategoryData(Sheet sheet, int cartId) {
        int index = 0;

        // 根据cartId取得platformId
        String platformId = Carts.getCart(cartId).getPlatform_id();
        if (StringUtils.isEmpty(platformId)) {
            $error("找不到对应的平台ID,sheet名：" + sheet.getSheetName());
            return;
        }

        Map<String, CmsMtPlatformCategoryTreeModel> platformCategoryTreeMap = null;
        if (platformsCategoryTreeMap.containsKey(platformId)) {
            platformCategoryTreeMap = platformsCategoryTreeMap.get(platformId);
        } else {
            // 取得这个平台的所有类目
            List<CmsMtPlatformCategoryTreeModel> platformCategoryTreeList = platformCategoryService.getPlatformCategory(cartId);
            // 所有平台类目 --> 取所有叶子 --> 拍平
            Stream<CmsMtPlatformCategoryTreeModel> platformCategoryTreeStream =
                    platformCategoryTreeList.stream().flatMap(this::flattenFinal);
            // 转换成Map
            platformCategoryTreeMap = new HashMap<>();
            platformCategoryTreeList = platformCategoryTreeStream.collect(Collectors.toList());
            for (CmsMtPlatformCategoryTreeModel platformCategoryTree : platformCategoryTreeList) {
                if (!platformCategoryTreeMap.containsKey(platformCategoryTree.getCatPath())) {
                    platformCategoryTreeMap.put(platformCategoryTree.getCatPath(), platformCategoryTree);
                }
            }
            platformsCategoryTreeMap.put(platformId, platformCategoryTreeMap);
        }

        for (Row row : sheet) {
            boolean insertFlg = false;
            if(index == 0) {
                // 跳过第一行header
                index++;
                continue;
            }
            // 这一行的类目路径
            String categoryPath = "";

            // 取得这一行的主类目路径
            for (int i = CATEGORY_START_INDEX; i < CATEGORY_START_INDEX + CATEGORY_MAX_LEVEL; i++) {
                if (!StringUtils.isEmpty(ExcelUtils.getString(row, i))) {
                    categoryPath += ExcelUtils.getString(row, i) + ">";
                }
            }
            // 去掉最后一个">"
            if (categoryPath.length() > 0) {
                categoryPath = categoryPath.substring(0,categoryPath.length() -1);
            }

            // 根据主类目路径取得对应的类目的一级类目对象
            CmsMtCategoryTreeAllModel categoryTreeFistLevel = categoryTreeAllService.getFirstLevelCategoryObjectByCatPath(categoryPath);

            if (categoryTreeFistLevel == null ) {
                $error("找不到主类目,sheet名：" + sheet.getSheetName() + ",行号：" +(index + 1) + ",主类目路径：" + categoryPath);
                index++;
                continue;
            }


            // 这一行的平台类目路径
            String platformCategoryPath = "";
            // 取得这一行的平台类目路径
            for (int i = PLATFORM_CATEGORY_START_INDEX; i < PLATFORM_CATEGORY_START_INDEX + CATEGORY_MAX_LEVEL; i++) {
                if (!StringUtils.isEmpty(ExcelUtils.getString(row, i))) {
                    platformCategoryPath += ExcelUtils.getString(row, i) + ">";
                }
            }
            // 去掉最后一个">"
            if (platformCategoryPath.length() > 0) {
                platformCategoryPath = platformCategoryPath.substring(0,platformCategoryPath.length() -1);
            }

            CmsMtPlatformCategoryTreeModel platformCategoryTree = platformCategoryTreeMap.get(platformCategoryPath);

            if (platformCategoryTree == null) {
                $error("找不到平台类目,sheet名：" + sheet.getSheetName() + ",行号：" +(index + 1) + ",平台类目路径：" + platformCategoryPath);
                index++;
                continue;
            }

            // 把对应的平台类目匹配关系，设定到主类目下(包含其子类目)
            CmsMtCategoryTreeAllModel categoryTree = null;
            if (categoryPath.equals(categoryTreeFistLevel.getCatPath())) {
                categoryTree = categoryTreeFistLevel;
            } else {
                categoryTree = categoryTreeAllService.findCategory(categoryTreeFistLevel, categoryPath);
            }
            addPlatformMappingToCategoryTree(categoryTree, platformCategoryTree, platformId);

            removeCreateUpdateInfo(categoryTreeFistLevel);
            cmsMtCategoryTreeAllDao.update(categoryTreeFistLevel);

            index++;

        }
    }

    /**
     * 拍平叶子类目,不包含父级
     *
     * @param platformCategoryTreeModel 平台类目模型
     * @return 叶子类目数据流
     */
    private Stream<CmsMtPlatformCategoryTreeModel> flattenFinal(CmsMtPlatformCategoryTreeModel platformCategoryTreeModel) {

        if (platformCategoryTreeModel.getIsParent() == 0)
            return Stream.of(platformCategoryTreeModel);

        List<CmsMtPlatformCategoryTreeModel> children = platformCategoryTreeModel.getChildren();

        if (children == null) children = new ArrayList<>(0);

        return children.stream().flatMap(this::flattenFinal);
    }

    /**
     * 把对应的平台类目匹配关系，设定到主类目下(包含其子类目)
     *
     */
    private void addPlatformMappingToCategoryTree(CmsMtCategoryTreeAllModel categoryTree, CmsMtPlatformCategoryTreeModel platformCategoryTree, String platformId) {

        // 取得/生成 主类目和平台类目匹配关系列表
        List<CmsMtCategoryTreeAllModel_Platform> platformCategoryList = categoryTree.getPlatformCategory();
        if (platformCategoryList == null) {
            platformCategoryList = new ArrayList<>();
            categoryTree.setPlatformCategory(platformCategoryList);
        }
        // 取得某个平台的 平台类目匹配关系对象，并且设定为新值
        CmsMtCategoryTreeAllModel_Platform platform = getPlatformCategory(platformCategoryList, platformId);
        platform.setPlatformId(platformId);
        platform.setCatId(platformCategoryTree.getCatId());
        platform.setCatPath(platformCategoryTree.getCatPath());

        // 子类目也要做
        if (categoryTree.getChildren() != null && categoryTree.getChildren().size() > 0) {
            for (CmsMtCategoryTreeAllModel child : categoryTree.getChildren()) {
                addPlatformMappingToCategoryTree(child, platformCategoryTree, platformId);
            }
        }
    }

    /**
     * 取得某个平台的平台类目匹配关系对象
     *
     */
    private CmsMtCategoryTreeAllModel_Platform  getPlatformCategory(List<CmsMtCategoryTreeAllModel_Platform> platformCategoryList, String platformId) {
        for (CmsMtCategoryTreeAllModel_Platform platformCategory : platformCategoryList) {
            if (platformId.equals(platformCategory.getPlatformId())) {
                return platformCategory;
            }
        }
        CmsMtCategoryTreeAllModel_Platform platform = new CmsMtCategoryTreeAllModel_Platform();
        platformCategoryList.add(platform);
        return platform;
    }

    /**
     * 导入主类目数据
     *
     */
    private void  importCategoryData(Sheet sheet) {
        int index = 0;

        for (Row row : sheet) {
            boolean insertFlg = false;
            if(index == 0) {
                // 跳过第一行header
                index++;
                continue;
            }
            // 这一行的类目路径
            String categoryPath = "";

            // 取得一级主类目名称
            String categoryNamePart1 = ExcelUtils.getString(row, CATEGORY_START_INDEX);
            categoryPath += categoryNamePart1;

            if (StringUtils.isEmpty(categoryNamePart1)) {
                continue;
            }

            // 取得一级主类目对象
            CmsMtCategoryTreeAllModel categoryTree = categoryTreeAllService.getFirstLevelCategoryByCatPath(categoryNamePart1);

            if (categoryTree == null) {
                categoryTree = new CmsMtCategoryTreeAllModel();
                categoryTree.setCatId(MD5.getMD5(categoryNamePart1));
                categoryTree.setCatName(categoryNamePart1);
                categoryTree.setCatPath(categoryNamePart1);
                categoryTree.setParentCatId("0");
                if (StringUtils.isEmpty(ExcelUtils.getString(row, CATEGORY_START_INDEX + 1))) {
                    categoryTree.setIsParent(0);
                } else {
                    categoryTree.setIsParent(1);
                }
                categoryTree.setCreater(getTaskName());
                categoryTree.setModifier(getTaskName());

                insertFlg = true;
            }

            CmsMtCategoryTreeAllModel levelModel = categoryTree;

            // 取得xls的一行数据，做出这棵树
            for (int i = CATEGORY_START_INDEX + 1; i < CATEGORY_START_INDEX + CATEGORY_MAX_LEVEL; i++) {

                // 某个层级下面的类目对象
                String categoryNamePart = ExcelUtils.getString(row, i);
                // 直到这一列的值为空白，那么这条数据结束
                if (StringUtils.isEmpty(categoryNamePart)) {
                    break;
                }
                categoryPath += ">" + categoryNamePart;

                levelModel = makeCategoryObject(levelModel, categoryNamePart, categoryPath, row, i);
            }

            if (insertFlg) {
                cmsMtCategoryTreeAllDao.insert(categoryTree);
            } else {
                removeCreateUpdateInfo(categoryTree);
                cmsMtCategoryTreeAllDao.update(categoryTree);
            }
            index++;

        }
    }

    /**
     * 根据类目路径 增加类目对象
     *
     */
    private CmsMtCategoryTreeAllModel makeCategoryObject(CmsMtCategoryTreeAllModel modelParent, String categoryName, String categoryPath, Row row, int index) {

        // 找下面的child中是否存在某个类目路径
        CmsMtCategoryTreeAllModel findCategoryTree = null;
        for (CmsMtCategoryTreeAllModel levelCategoryTree : modelParent.getChildren()) {
            if (levelCategoryTree.getCatPath().equals(categoryPath)) {
                // 找到
                findCategoryTree = levelCategoryTree;
            }
        }
        // 找不到新加一个
        if (findCategoryTree == null)  {
            findCategoryTree = new CmsMtCategoryTreeAllModel();
            findCategoryTree.setCatId(MD5.getMD5(categoryPath));
            findCategoryTree.setCatName(categoryName);
            findCategoryTree.setCatPath(categoryPath);
            findCategoryTree.setParentCatId(modelParent.getCatId());
            if (StringUtils.isEmpty(ExcelUtils.getString(row, index + 1))) {
                findCategoryTree.setIsParent(0);
            } else {
                findCategoryTree.setIsParent(1);
            }
            findCategoryTree.setModifier(null);
            findCategoryTree.setModified(null);
            findCategoryTree.setCreater(null);
            findCategoryTree.setCreated(null);
            modelParent.getChildren().add(findCategoryTree);

        }

        return findCategoryTree;

    }

    /**
     * 更新对象的情况下，删除child对象的Creater/Updater/Created/Updated这几个属性
     *
     */
    private void removeCreateUpdateInfo(CmsMtCategoryTreeAllModel categoryTree) {
        for (CmsMtCategoryTreeAllModel child : categoryTree.getChildren()) {
            removeItemCreateUpdate(child);
        }

    }

    /**
     * 更新对象的情况下，删除child对象的Creater/Updater/Created/Updated这几个属性
     *
     */
    private void removeItemCreateUpdate(CmsMtCategoryTreeAllModel categoryTree) {
        categoryTree.setModifier(null);
        categoryTree.setModified(null);
        categoryTree.setCreater(null);
        categoryTree.setCreated(null);
        for (CmsMtCategoryTreeAllModel child : categoryTree.getChildren()) {
            removeItemCreateUpdate(child);
        }
    }


}
