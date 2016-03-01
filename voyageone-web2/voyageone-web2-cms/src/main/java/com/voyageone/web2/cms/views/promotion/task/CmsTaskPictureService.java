package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.enums.BeatFlag;
import com.voyageone.common.configs.Enums.PromotionTypeEnums;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.beat.TaskBean;
import com.voyageone.web2.cms.dao.CmsBtBeatInfoDao;
import com.voyageone.web2.cms.dao.CmsBtTaskDao;
import com.voyageone.web2.cms.model.CmsBtBeatInfoModel;
import com.voyageone.web2.cms.model.CmsBtTaskModel;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.sdk.api.VoApiClient;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;
import com.voyageone.web2.sdk.api.request.PromotionsGetRequest;
import com.voyageone.web2.sdk.api.response.PromotionsGetResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.voyageone.common.util.ExcelUtils.getNum;
import static com.voyageone.common.util.ExcelUtils.getString;

/**
 * Created by jonasvlag on 16/2/29.
 *
 * @version 2.0.0
 */
@Service
public class CmsTaskPictureService extends BaseAppService {

    @Autowired
    private VoApiClient apiClient;

    @Autowired
    private CmsBtTaskDao taskDao;

    @Autowired
    private CmsBtBeatInfoDao beatInfoDao;

    public TaskBean create(TaskBean taskBean, UserSessionBean user) {

        CmsBtPromotionModel promotion = getPromotion(taskBean.getPromotion_id());

        if (promotion == null)
            throw new BusinessException("没找到 Promotion");

        if (!taskBean.getConfig().isValid())
            throw new BusinessException("配置错误");

        List<CmsBtTaskModel> taskModels = taskDao.selectByName(
                taskBean.getPromotion_id(),
                taskBean.getTask_name(),
                PromotionTypeEnums.Type.JIAGEPILU.getTypeId());

        if (!taskModels.isEmpty())
            throw new BusinessException("Task 重名");

        taskBean.setTask_type(PromotionTypeEnums.Type.JIAGEPILU);
        taskBean.getConfig().setBeat_time(promotion.getPrePeriodStart());
        taskBean.getConfig().setRevert_time(promotion.getActivityEnd());
        taskBean.setCreater(user.getUserName());
        taskBean.setModifier(user.getUserName());

        int count = taskDao.insert(taskBean.toModel());

        if (count < 1)
            return null;

        taskModels = taskDao.selectByName(
                taskBean.getPromotion_id(),
                taskBean.getTask_name(),
                taskBean.getTask_type().getTypeId());

        return new TaskBean(taskModels.get(0));
    }

    public List<CmsBtBeatInfoModel> getAllBeat(int task_id, int offset, int size) {

        return beatInfoDao.selectListByTask(task_id, offset, size);
    }

    public int getAllBeatCount(int task_id) {

        return beatInfoDao.selectListByTaskCount(task_id);
    }

    public List<CmsBtBeatInfoModel> importBeatInfo(int task_id, int size, MultipartFile file, UserSessionBean user) {

        // 如果存在以下标识数据, 就不能重新导入
        int count = beatInfoDao.selectCountInFlags(task_id,
                BeatFlag.BEATING, BeatFlag.RE_FAIL, BeatFlag.REVERT, BeatFlag.SUCCESS);

        if (count > 0)
            throw new BusinessException("存在[等待刷图, 还原失败, 等待还原, 刷图成功]的数据时,不能重新导入. 请人工处理后重新再试.");

        beatInfoDao.deleteByTask(task_id);

        Workbook wb;

        try {
            wb = WorkbookFactory.create(file.getInputStream());
        } catch (IOException | InvalidFormatException e) {
            throw new BusinessException("导入失败");
        }

        Sheet sheet = wb.getSheetAt(0);

        List<CmsBtBeatInfoModel> models = new ArrayList<>();

        for (Row row : sheet) {

            Double value = getNum(row, 0);

            if (value == null)
                throw new BusinessException("格式不对");

            CmsBtBeatInfoModel model = new CmsBtBeatInfoModel();

            model.setNum_iid(value.longValue());
            model.setProduct_code(getString(row, 1));
            model.setCreater(user.getUserName());
            model.setModifier(user.getUserName());
            model.setBeatFlag(BeatFlag.STOP); // syn_flag
            model.setTask_id(task_id);

            models.add(model);
        }

        beatInfoDao.insertList(models);

        beatInfoDao.updateNoCodeMessage(task_id, "该 Code 不在 Promotion 内");

        return beatInfoDao.selectListByTask(task_id, 0, size);
    }

    public byte[] downloadBeatInfo(int task_id) {

        List<CmsBtBeatInfoModel> beatInfoModels = beatInfoDao.selectListByTask(task_id);

        // 注意: HSSFWorkbook 为 2003 的 xls 格式
        try (Workbook book = new HSSFWorkbook()) {

            Sheet sheet = book.createSheet();

            // 设置查询类型
            for (int i = 0; i < beatInfoModels.size(); i++) {

                CmsBtBeatInfoModel model = beatInfoModels.get(i);

                Row row = row(sheet, i + 1);

                cell(row, 0, null).setCellValue(model.getNum_iid());

                cell(row, 1, null).setCellValue(model.getProduct_code());

                cell(row, 2, null).setCellValue(model.getBeatFlag().name());

                cell(row, 3, null).setCellValue(model.getMessage());
            }

            // 打开保存
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                book.write(outputStream);
                return outputStream.toByteArray();
            }

        } catch (IOException e) {
            throw new BusinessException("写入失败", e);
        }
    }

    private CmsBtPromotionModel getPromotion(int promotion_id) {

        PromotionsGetRequest request = new PromotionsGetRequest();
        request.setPromotionId(promotion_id);

        PromotionsGetResponse response = apiClient.execute(request);

        List<CmsBtPromotionModel> promotionModels = response.getCmsBtPromotionModels();

        return promotionModels.isEmpty() ? null : promotionModels.get(0);
    }

    private Row row(Sheet sheet, int rowIndex) {

        Row row = sheet.getRow(rowIndex);

        if (row == null) row = sheet.createRow(rowIndex);

        return row;
    }

    private Cell cell(Row row, int index, CellStyle cellStyle) {

        Cell cell = row.getCell(index);

        if (cell == null) cell = row.createCell(index);

        if (cellStyle != null) cell.setCellStyle(cellStyle);

        return cell;
    }
}
