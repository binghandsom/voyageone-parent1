package com.voyageone.service.impl.cms.task;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.cms.CmsBtBeatInfoBean;
import com.voyageone.service.dao.cms.CmsBtTaskJiagepiluDao;
import com.voyageone.service.dao.cms.CmsBtTasksDao;
import com.voyageone.service.daoext.cms.CmsBtBeatInfoDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtTaskJiagepiluModel;
import com.voyageone.service.model.cms.CmsBtTasksModel;
import com.voyageone.service.model.cms.enums.jiagepilu.BeatFlag;
import com.voyageone.service.model.cms.enums.jiagepilu.ImageStatus;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    private ProductService productService;

    @Autowired
    private CmsBtBeatInfoDaoExt beatInfoDaoExt;

    @VOTransactional
    public int saveOrUpdateTaskJiagepiluModel(CmsBtTaskJiagepiluModel taskJiagepiluModel) {
        int result = 0;
        if (taskJiagepiluModel != null) {
            CmsBtTaskJiagepiluModel queryModel = new CmsBtTaskJiagepiluModel();
            queryModel.setTaskId(taskJiagepiluModel.getTaskId());
            queryModel.setNumIid(taskJiagepiluModel.getNumIid());

            CmsBtTaskJiagepiluModel targetModel = cmsBtTaskJiagepiluDao.selectOne(queryModel);
            if (targetModel == null) {
                result = cmsBtTaskJiagepiluDao.insert(taskJiagepiluModel);
            } else {
                CmsBtTaskJiagepiluModel updateModel = new CmsBtTaskJiagepiluModel();
                updateModel.setId(targetModel.getId());

                updateModel.setProductCode(taskJiagepiluModel.getProductCode());
                updateModel.setPrice(taskJiagepiluModel.getPrice());
                updateModel.setImageName(taskJiagepiluModel.getImageName());
                updateModel.setSynFlag(taskJiagepiluModel.getSynFlag());
                updateModel.setMessage(taskJiagepiluModel.getMessage());
                updateModel.setModified(new Date());
                updateModel.setModifier(taskJiagepiluModel.getCreater());

                result = cmsBtTaskJiagepiluDao.update(updateModel);
            }
        }

        return result;
    }

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
    public void importProduct(Integer taskId, MultipartFile file, String username) {
        
        CmsBtTasksModel tasksModel = cmsBtTasksDao.select(taskId);
        if (tasksModel == null) {
            throw new BusinessException(String.format("价格披露任务(taskId:%d)不存在", taskId));
        }

        // TODO: 2017/6/21  重复导入的条件判断

        String channelId = tasksModel.getChannelId();
        Integer cartId = tasksModel.getCartId();

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            List<CmsBtBeatInfoBean> models = new ArrayList<CmsBtBeatInfoBean>();

            for (Row row : sheet) {
                String numIidVal = getString(row, 0, "#");
                String code = getString(row, 1);
                String priceVal = getString(row, 2, "#");
                String imageName = getString(row, 3);

                // numIid为空即终止继续读取Excel
                if (StringUtils.isBlank(numIidVal)) {
                    break;
                }

                // numIid是否为空，需要check该channel，该平台下，该code是否存在于该num_iid下面
                if (StringUtils.isEmpty(numIidVal) || !StringUtils.isNumeric(numIidVal)) {
                    throw new BusinessException("7000006", row.getRowNum());
                }

                // 创建价格披露商品Model，天猫系和京东系校验条件不一样
                CmsBtTaskJiagepiluModel model = new CmsBtBeatInfoBean();
                model.setCreated(new Date());
                model.setCreater(username);
                model.setTaskId(taskId);
                model.setImageStatus(ImageStatus.None.getId()); // 无图状态
                model.setImageTaskId(0);
                model.setNumIid(Long.valueOf(numIidVal));
                model.setProductCode(code);

                List<CmsBtProductModel> productModels = productService.getProductByNumIid(channelId, numIidVal, cartId);
                if (CollectionUtils.isEmpty(productModels)) {
                    model.setSynFlag(BeatFlag.CANT_BEAT.getFlag());
                    model.setMessage(String.format("NumIid(%s)在平台(channelId=%s,cartId=%d)不存在", numIidVal, channelId, cartId));
                } else if (StringUtils.isBlank(code)) {
                    model.setSynFlag(BeatFlag.CANT_BEAT.getFlag());
                    model.setMessage("Code为空");
                } else {
                    // 看Code是否在numIid下
                    CmsBtProductModel productModel = null;
                    for (CmsBtProductModel product : productModels) {
                        if (code.equalsIgnoreCase(product.getCommon().getFields().getCode())) {
                            productModel = product;
                            break;
                        }
                    }
                    if (productModel == null) {
                        model.setSynFlag(BeatFlag.CANT_BEAT.getFlag());
                        model.setMessage(String.format("NumIid(%s)和Code(%s)在平台(channelId=%s,cartId=%d)下不匹配", numIidVal, code, channelId, cartId));
                    } else {
                        // TODO: 2017/6/20 价格>0的判断
                        if (StringUtils.isBlank(imageName)) {
                            CmsBtProductModel_Platform_Cart platform = productModel.getPlatform(cartId);
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
                                CmsBtProductModel_Field fields = productModel.getCommon().getFields();
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

                            if (StringUtils.isBlank(imageName)) {
                                model.setSynFlag(BeatFlag.CANT_BEAT.getFlag());
                                model.setMessage("图名名称为空且找不到平台或master下images6/image1图片");
                            }
                        }

                    }
                }

                if (!Objects.equals(model.getSynFlag(), Integer.valueOf(BeatFlag.CANT_BEAT.getFlag()))
                        && ((StringUtils.isEmpty(priceVal) || !StringUtils.isNumeric(priceVal) || Double.valueOf(priceVal) <= 0d))) {
                    model.setSynFlag(BeatFlag.CANT_BEAT.getFlag());
                    model.setMessage(String.format("价格(%s)为空或非数字或者数值不大于0", priceVal));
                } else {
                    if (!Objects.equals(model.getSynFlag(), Integer.valueOf(BeatFlag.CANT_BEAT.getFlag()))) {
                        model.setPrice(Double.valueOf(priceVal));
                    }
                }
                model.setImageName(imageName);

                // 插入或者更新
                int result = 0;
                if (result == 0) {
                    $error(String.format("NumIid(%s)保存或更新错误"));
                }
            }

        } catch (IOException | InvalidFormatException e) {
            throw new BusinessException("7000005");
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


}
