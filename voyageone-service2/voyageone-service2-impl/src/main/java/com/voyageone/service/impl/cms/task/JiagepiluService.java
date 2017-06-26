package com.voyageone.service.impl.cms.task;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.service.bean.cms.CmsBtBeatInfoBean;
import com.voyageone.service.bean.cms.task.beat.CmsBtTaskJiagepiluBean;
import com.voyageone.service.bean.cms.task.beat.SearchTaskJiagepiluBean;
import com.voyageone.service.bean.cms.task.beat.SearchTaskJiagepiluResult;
import com.voyageone.service.dao.cms.CmsBtTaskJiagepiluDao;
import com.voyageone.service.dao.cms.CmsBtTaskJiagepiluImportInfoDao;
import com.voyageone.service.dao.cms.CmsBtTasksDao;
import com.voyageone.service.daoext.cms.CmsBtBeatInfoDaoExt;
import com.voyageone.service.daoext.cms.CmsBtTaskJiagepiluDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtTaskJiagepiluImportInfoModel;
import com.voyageone.service.model.cms.CmsBtTaskJiagepiluModel;
import com.voyageone.service.model.cms.CmsBtTasksModel;
import com.voyageone.service.model.cms.enums.jiagepilu.BeatFlag;
import com.voyageone.service.model.cms.enums.jiagepilu.ImageStatus;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.voyageone.common.util.ExcelUtils.getString;

/**
 * 价格披露Service
 *
 * @Author rex.wu
 * @Create 2017-06-20 15:06
 */
@Service
public class JiagepiluService extends BaseService {

    @Autowired
    private CmsBtTasksDao cmsBtTasksDao;

    @Autowired
    private CmsBtTaskJiagepiluDao cmsBtTaskJiagepiluDao;

    @Autowired
    private CmsBtTaskJiagepiluDaoExt cmsBtTaskJiagepiluDaoExt;

    @Autowired
    private ProductService productService;

    @Autowired
    private CmsBtTaskJiagepiluImportInfoDao cmsBtTaskJiagepiluImportInfoDao;

    /**
     * 获取价格披露TaskModel
     */
    public CmsBtTasksModel getJiagepiluTaskModel(Integer taskId) {
        CmsBtTasksModel taskModel = cmsBtTasksDao.select(taskId);
        if (taskModel == null) {
            throw new BusinessException(String.format("价格披露任务(taskId:%d)不存在", taskId));
        }
        return taskModel;
    }

    public List<CmsBtTaskJiagepiluModel> getProductList(Integer taskId) {
        CmsBtTaskJiagepiluModel queryModel = new CmsBtTaskJiagepiluModel();
        queryModel.setTaskId(taskId);
        return cmsBtTaskJiagepiluDao.selectList(queryModel);
    }

    public int getProductCount(Integer taskId) {
        CmsBtTaskJiagepiluModel queryModel = new CmsBtTaskJiagepiluModel();
        queryModel.setTaskId(taskId);
        return cmsBtTaskJiagepiluDao.selectCount(queryModel);
    }

    /**
     * 为价格披露Task导入商品
     *
     * @param taskId 价格披露任务ID
     * @param file   导入Excel文件
     */
    @VOTransactional
    public void importProduct(Integer taskId, MultipartFile file, String username) {

        CmsBtTasksModel tasksModel = cmsBtTasksDao.select(taskId);
        if (tasksModel == null) {
            throw new BusinessException(String.format("价格披露任务(taskId:%d)不存在", taskId));
        }

        // TODO: 2017/6/21  重复导入的条件判断

        String channelId = tasksModel.getChannelId();
        Integer cartId = tasksModel.getCartId();

        CartEnums.Cart cart = CartEnums.Cart.getValueByID(String.valueOf(cartId));
        boolean isTmSeries = CartEnums.Cart.isTmSeries(cart);
        if (!isTmSeries) {
            boolean isJdSeries = CartEnums.Cart.isJdSeries(cart);
            if (!isJdSeries) {
                throw new BusinessException(String.format("价格披露Task平台(%d)既不是天猫系也不是京东系", cartId));
            }
        }

        InputStream inputStream = null;
        try {
            String fileName = file.getOriginalFilename(); // 原始文件名

            // 读取上传的Excel文件
            inputStream = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            int rowNum = sheet.getLastRowNum();
            if (rowNum <= 1) {
                throw new BusinessException("文件内容行数不大于1");
            }

            // 如果导入有错误，则生成错误文件
            Workbook errorBook = new HSSFWorkbook();
            errorBook.createSheet();
            Sheet errorSheet = errorBook.getSheetAt(0);
            Row titleRow = FileUtils.row(errorSheet, 0);
            // 设置标题
            FileUtils.cell(titleRow, 0, null).setCellValue("num_iid");
            FileUtils.cell(titleRow, 1, null).setCellValue("code");
            FileUtils.cell(titleRow, 2, null).setCellValue("price");
            FileUtils.cell(titleRow, 3, null).setCellValue("imageName");
            FileUtils.cell(titleRow, 4, null).setCellValue("message");

            int errorRowNum = 0;

            Set<String> numIidSet = new HashSet<>();
            Map<String, Set<String>> numIidCodeMap = new HashMap<>();


            Date beginTime = new Date();
            // 从1到rowNum遍历，第一行(rowNum=0)视为标题
            for (int i = 1; i <= rowNum; i++) {
                Row row = sheet.getRow(i);

                // 取四列数据
                String numIid = getString(row, 0, "#");
                String code = getString(row, 1);
                String priceVal = getString(row, 2);
                String imageName = getString(row, 3);

                if (StringUtils.isBlank(numIid) || !StringUtils.isNumeric(numIid)) {
                    errorRowNum++;
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 0, null).setCellValue(numIid);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 1, null).setCellValue(code);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 2, null).setCellValue(priceVal);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 3, null).setCellValue(imageName);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 4, null).setCellValue("numIid为空或不是数字格式");

                    continue;
                }

                if (isTmSeries && numIidSet.contains(numIid)) {
                    errorRowNum++;
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 0, null).setCellValue(numIid);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 1, null).setCellValue(code);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 2, null).setCellValue(priceVal);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 3, null).setCellValue(imageName);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 4, null).setCellValue("numIid在上传文件中重复了");

                    continue;
                }
                if (isTmSeries) {
                    numIidSet.add(numIid);
                }

                if (StringUtils.isBlank(code)) {
                    errorRowNum++;
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 0, null).setCellValue(numIid);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 1, null).setCellValue(code);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 2, null).setCellValue(priceVal);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 3, null).setCellValue(imageName);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 4, null).setCellValue("产品Code为空");

                    continue;
                }

                if (!isTmSeries
                        && numIidCodeMap.containsKey(numIid)
                        && CollectionUtils.isNotEmpty(numIidCodeMap.get(numIid))
                        && numIidCodeMap.get(numIid).contains(code)) {

                    errorRowNum++;
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 0, null).setCellValue(numIid);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 1, null).setCellValue(code);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 2, null).setCellValue(priceVal);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 3, null).setCellValue(imageName);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 4, null).setCellValue("numIid和code在上传文件中重复了");

                    continue;
                }


                if (StringUtils.isBlank(priceVal) || !com.voyageone.common.util.StringUtils.isNumeric(priceVal) || Double.valueOf(priceVal) <= 0d) {
                    errorRowNum++;
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 0, null).setCellValue(numIid);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 1, null).setCellValue(code);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 2, null).setCellValue(priceVal);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 3, null).setCellValue(imageName);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 4, null).setCellValue("价格为空/非数字/数值小于或等于0");

                    continue;
                }

                // 如果图片名称为空，根据商品Code取图片名称
                if (StringUtils.isBlank(imageName)) {
                    imageName = this.getJiagepiluProductImage(channelId, code, cartId);
                    if (StringUtils.isBlank(imageName)) {
                        errorRowNum++;
                        FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 0, null).setCellValue(numIid);
                        FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 1, null).setCellValue(code);
                        FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 2, null).setCellValue(priceVal);
                        FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 3, null).setCellValue(imageName);
                        FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 4, null).setCellValue("根据code获取不到商品主图");

                        continue;
                    }
                }

                CmsBtTaskJiagepiluModel jiagepiluModel = new CmsBtTaskJiagepiluModel();
                jiagepiluModel.setCreated(new Date());
                jiagepiluModel.setCreater(username);
                jiagepiluModel.setTaskId(taskId);
                jiagepiluModel.setNumIid(Long.valueOf(numIid));
                jiagepiluModel.setProductCode(code);
                jiagepiluModel.setPrice(Double.valueOf(priceVal));
                jiagepiluModel.setImageName(imageName);

                // 初始"STOP"状态
                jiagepiluModel.setSynFlag(BeatFlag.STOP.getFlag());
                jiagepiluModel.setImageStatus(ImageStatus.None.getId()); // 无图状态
                jiagepiluModel.setImageTaskId(0);

                int affected = this.saveOrUpdateJiagepiluModel(jiagepiluModel, isTmSeries);
                if (affected <= 0) {
                    errorRowNum++;
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 0, null).setCellValue(numIid);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 1, null).setCellValue(code);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 2, null).setCellValue(priceVal);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 3, null).setCellValue(imageName);
                    FileUtils.cell(FileUtils.row(errorSheet, errorRowNum), 4, null).setCellValue("新增或更新记录出错");

                    continue;
                }

            }
            Date endTime = new Date();

            CmsBtTaskJiagepiluImportInfoModel jiagepiluImportInfoModel = new CmsBtTaskJiagepiluImportInfoModel();
            jiagepiluImportInfoModel.setTaskId(taskId);
            jiagepiluImportInfoModel.setFailCount(errorRowNum);
            jiagepiluImportInfoModel.setSuccessCount(rowNum - errorRowNum);
            jiagepiluImportInfoModel.setImportFileName(fileName);
            jiagepiluImportInfoModel.setImportBeginTime(beginTime);
            jiagepiluImportInfoModel.setImportEndTime(endTime);
            jiagepiluImportInfoModel.setCreater(username);
            jiagepiluImportInfoModel.setCreated(new Date());

            String errorFileName = "jiagepilu" + taskId + "-import-error-"
                    + DateTimeUtil.format(new Date(), DateTimeUtil.DATE_TIME_FORMAT_2) + ".xls";
            if (errorRowNum > 0) {
                jiagepiluImportInfoModel.setErrorFileName(errorFileName);
            }
            cmsBtTaskJiagepiluImportInfoDao.insert(jiagepiluImportInfoModel);

            // 保存导入失败文件
            if (errorRowNum > 0) {
                String importErrorDir = Properties.readValue(CmsProperty.Props.CMS_JIAGEPILU_IMPORT_ERROR_PATH);
                File pathFileObj = new File(importErrorDir);
                if (!pathFileObj.exists()) {
                    pathFileObj.mkdirs();
                }

                OutputStream outputStream = new FileOutputStream(importErrorDir + errorFileName);
                try {
                    errorBook.write(outputStream);
                    $info("已写入输出流");
                } finally {
                    outputStream.close();
                }
            }
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 如果导入文件图片名称为空，取CMS商品图片
     *
     * @param channelId 渠道ID
     * @param code      产品Code
     * @return 图片名称
     */
    private String getJiagepiluProductImage(String channelId, String code, Integer cartId) {

        if (StringUtils.isBlank(channelId) || StringUtils.isBlank(code) || cartId == null || cartId == 0) {
            return null;
        }

        String imageName = null;

        CmsBtProductModel productModel = productService.getProductByCode(channelId, code);
        CmsBtProductModel_Field fields = null;
        CmsBtProductModel_Platform_Cart platform = null;

        if (productModel != null
                && (fields = productModel.getCommon().getFields()) != null
                && (platform = productModel.getPlatform(cartId)) != null) {

            // 取平台images6图片名称，无则去images1图片名称
            if (CollectionUtils.isNotEmpty(platform.getImages6())) {
                imageName = platform.getImages6().get(0).getName();
            } else {
                if (CollectionUtils.isNotEmpty(platform.getImages1())) {
                    imageName = platform.getImages1().get(0).getName();
                }
            }
            // 如果平台下取不到图片，则取master下图片名称
            if (StringUtils.isBlank(imageName)) {
                if (fields != null) {
                    if (CollectionUtils.isNotEmpty(fields.getImages6())) {
                        imageName = fields.getImages6().get(0).getName();
                    } else {
                        if (CollectionUtils.isNotEmpty(fields.getImages1())) {
                            imageName = fields.getImages1().get(0).getName();
                        }
                    }
                }
            }
        }
        return imageName;
    }

    /**
     * 创建或更新价格披露商品Model
     *
     * @param isTmSeries 是否是天猫系，如果不是则为京东系
     * @return 受影响行数
     */
    private int saveOrUpdateJiagepiluModel(CmsBtTaskJiagepiluModel model, boolean isTmSeries) {

        int result = 0;

        CmsBtTaskJiagepiluModel queryModel = new CmsBtTaskJiagepiluModel();
        queryModel.setTaskId(model.getTaskId());
        queryModel.setNumIid(model.getNumIid());
        if (!isTmSeries) {
            queryModel.setProductCode(model.getProductCode());
        }

        CmsBtTaskJiagepiluModel targetModel = cmsBtTaskJiagepiluDao.selectOne(queryModel);
        if (targetModel == null) {
            result = cmsBtTaskJiagepiluDao.insert(model);
        } else {
            CmsBtTaskJiagepiluModel updateModel = new CmsBtTaskJiagepiluModel();
            updateModel.setId(targetModel.getId());
            // 更新导入字段
            updateModel.setPrice(model.getPrice());
            updateModel.setImageName(model.getImageName());
            result = cmsBtTaskJiagepiluDao.update(updateModel);
        }
        return result;
    }

    /**
     * 查询价格披露任务Task的导入信息
     *
     * @param taskId 价格披露任务ID
     */
    public List<CmsBtTaskJiagepiluImportInfoModel> getImportInfoList(Integer taskId) {
        CmsBtTaskJiagepiluImportInfoModel queryModel = new CmsBtTaskJiagepiluImportInfoModel();
        queryModel.setTaskId(taskId);
        List<CmsBtTaskJiagepiluImportInfoModel> importInfoModelList = cmsBtTaskJiagepiluImportInfoDao.selectList(queryModel);
        if (CollectionUtils.isNotEmpty(importInfoModelList)) {
            // 默认按ID递增，倒序
            Collections.sort(importInfoModelList, new Comparator<CmsBtTaskJiagepiluImportInfoModel>() {
                public int compare(CmsBtTaskJiagepiluImportInfoModel arg0, CmsBtTaskJiagepiluImportInfoModel arg1) {
                    return arg1.getId().compareTo(arg0.getId());
                }
            });
        }
        return importInfoModelList;
    }

    public SearchTaskJiagepiluResult search(SearchTaskJiagepiluBean search) {
        search.parseEnum(); // 枚举处理
        search.handlePage(); // 处理分页参数
        SearchTaskJiagepiluResult result = new SearchTaskJiagepiluResult();
        List<CmsBtTaskJiagepiluBean> productList = cmsBtTaskJiagepiluDaoExt.search(search);
        result.setProducts(productList);
        int total = cmsBtTaskJiagepiluDaoExt.count(search);
        result.setTotal(total);

        List<Map<String, Object>> summary = cmsBtTaskJiagepiluDaoExt.selectSummary(search.getTaskId());
        // 数据查询出来的是整数, 转换为枚举名
        for (Map<String, Object> elementMap : summary) {
            Object flag = elementMap.get("flag");
            Integer flagId = Integer.valueOf(String.valueOf(flag));
            BeatFlag flagEnum = BeatFlag.valueOf(flagId);
            elementMap.put("flag", flagEnum.name());
        }

        result.setSummary(summary);
        return result;
    }

    /**
     * 添加/编辑商品
     * @param id 记录ID，有则编辑，无则添加
     * @param taskId
     * @param numIid
     * @param code
     * @param price
     * @param username
     * @return 受影响行数
     */
    @VOTransactional
    public int addJiagepiluProduct(Integer id, Integer taskId, String numIid, String code, Double price, String username) {

        CmsBtTasksModel tasksModel = null;
        if (taskId != null) {
            tasksModel = cmsBtTasksDao.select(taskId);
        }
        if (tasksModel == null) {
            throw new BusinessException(String.format("价格披露任务(taskId:%d)不存在", taskId));
        }

        Integer cartId = tasksModel.getCartId();
        CartEnums.Cart cart = CartEnums.Cart.getValueByID(String.valueOf(cartId));
        boolean isTmSeries = CartEnums.Cart.isTmSeries(cart);
        if (!isTmSeries) {
            boolean isJdSeries = CartEnums.Cart.isJdSeries(cart);
            if (!isJdSeries) {
                throw new BusinessException(String.format("价格披露Task平台(%d)既不是天猫系也不是京东系", cartId));
            }
        }

        int affected = 0;

        // 看重复记录是已存在，天猫系和京东系的条件不一致
        CmsBtTaskJiagepiluModel queryModel = new CmsBtTaskJiagepiluModel();
        queryModel.setTaskId(taskId);
        queryModel.setNumIid(Long.valueOf(numIid));
        if (!isTmSeries) {
            queryModel.setProductCode(code);
        }

        CmsBtTaskJiagepiluModel targetModel = cmsBtTaskJiagepiluDao.selectOne(queryModel);
        if (id == null && targetModel != null) {
            String message = "";
            if (isTmSeries) {
                message = String.format("NumIid(%s)对应的记录存在");
            } else {
                message = String.format("NumIid(%s)和ProductCode(%s)对应的记录存在");
            }
            throw new BusinessException(message);
        }

        if (id != null && targetModel != null &&  targetModel.getId().intValue() != id.intValue()) {
            throw new BusinessException("7000002");
        }

        String imageName = this.getJiagepiluProductImage(tasksModel.getChannelId(), code, tasksModel.getCartId());
        if (StringUtils.isBlank(imageName)) {
            throw new BusinessException("根据产品Code找不到商品主图");
        }

        if (id == null) {
            CmsBtTaskJiagepiluModel newModel = new CmsBtTaskJiagepiluModel();
            newModel.setCreated(new Date());
            newModel.setCreater(username);
            newModel.setTaskId(taskId);
            newModel.setNumIid(Long.valueOf(numIid));
            newModel.setProductCode(code);
            newModel.setPrice(price);
            newModel.setImageName(imageName);

            // 初始"STOP"状态
            newModel.setSynFlag(BeatFlag.STOP.getFlag());
            newModel.setImageStatus(ImageStatus.None.getId()); // 无图状态
            newModel.setImageTaskId(0);
            affected = cmsBtTaskJiagepiluDao.insert(newModel);
        } else {
            CmsBtTaskJiagepiluModel updateModel = new CmsBtTaskJiagepiluModel();
            updateModel.setId(id);
            updateModel.setSynFlag(BeatFlag.STOP.getFlag());
            updateModel.setImageStatus(ImageStatus.None.getId()); // 无图状态

            updateModel.setNumIid(Long.valueOf(numIid));
            updateModel.setProductCode(code);
            updateModel.setPrice(price);
            updateModel.setImageName(imageName);
            updateModel.setModifier(username);
            updateModel.setModified(new Date());
            affected = cmsBtTaskJiagepiluDao.update(updateModel);
        }

        return affected;
    }


    /**
     * 操作价格披露任务的商品
     *
     * @param id     价格披露商品项Id
     * @param taskId 价格披露任务ID
     * @param flag   要改成的状态
     * @param force  是否强制将“CANT_BEAT”状态的产品更新
     * @return 影响行数
     */
    @VOTransactional
    public int operateProduct(Integer id, Integer taskId, BeatFlag flag, Boolean force, String username) {
        if (id != null) {
            if (flag == null) {
                throw new BusinessException("7000002"); // 参数错误
            }
            CmsBtTaskJiagepiluModel targetModel = cmsBtTaskJiagepiluDao.select(id);
            if (targetModel != null) {
                CmsBtTaskJiagepiluModel updateModel = new CmsBtTaskJiagepiluModel();
                updateModel.setId(id);
                updateModel.setImageStatus(ImageStatus.None.getId());
                updateModel.setSynFlag(flag.getFlag());
                updateModel.setModifier(username);
                updateModel.setModified(new Date());
                return cmsBtTaskJiagepiluDao.update(updateModel);
            }
        } else if (taskId != null) {
            if (flag == null) {
                throw new BusinessException("7000002");
            }
            return cmsBtTaskJiagepiluDaoExt.updateFlags(taskId, flag.getFlag(), ImageStatus.None.getId(), force, username);
        }
        return 0;
    }

    /**
     * 启动失败或者还原失败的产品重启
     * @param taskId
     * @param flag
     * @param username
     * @return
     */
    @VOTransactional
    public int reBeating(Integer taskId, BeatFlag flag, String username) {
        // 刷图成功或者还原失败的商品重启
        if (flag.getFlag() == BeatFlag.FAIL.getFlag() || flag.getFlag() == BeatFlag.RE_FAIL.getFlag()) {
            return cmsBtTaskJiagepiluDaoExt.rebeatFailFlags(taskId, flag.getFlag(), BeatFlag.BEATING.getFlag(), ImageStatus.None.getId(), username);
        }
        return 0;
    }

    public CmsBtTaskJiagepiluModel getProductById(Integer id) {
        if (id != null) {
            return cmsBtTaskJiagepiluDao.select(id);
        }
        return null;
    }

    /**
     * 下载价格披露任务产品列表
     * @param taskId
     * @return
     */
    public byte[] download(int taskId) {

        List<CmsBtTaskJiagepiluModel> jiagepiluModels = this.getProductList(taskId);
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        // 标题栏
        Row row = FileUtils.row(sheet, 0);
        FileUtils.cell(row, 0, null).setCellValue("num_iid");
        FileUtils.cell(row, 1, null).setCellValue("商品 Code");
        FileUtils.cell(row, 2, null).setCellValue("价格");
        FileUtils.cell(row, 3, null).setCellValue("图名名称");
        FileUtils.cell(row, 4, null).setCellValue("状态");
        FileUtils.cell(row, 5, null).setCellValue("结果信息");

        int size = jiagepiluModels.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {

                CmsBtTaskJiagepiluModel model = jiagepiluModels.get(i);

                row = FileUtils.row(sheet, i + 1);

                FileUtils.cell(row, 0, null).setCellValue(model.getNumIid());

                FileUtils.cell(row, 1, null).setCellValue(model.getProductCode());

                FileUtils.cell(row, 2, null).setCellValue(model.getPrice());

                FileUtils.cell(row, 3, null).setCellValue(model.getImageName());

                FileUtils.cell(row, 4, null).setCellValue(BeatFlag.valueOf(model.getSynFlag()).name());

                FileUtils.cell(row, 5, null).setCellValue(model.getMessage());

            }
        }

        // 打开保存
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new BusinessException("下载出错了", e);
        }
    }
}
