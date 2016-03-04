package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.enums.BeatFlag;
import com.voyageone.common.configs.Enums.PromotionTypeEnums;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.wsdl.bean.task.beat.TaskBean;
import com.voyageone.web2.cms.wsdl.dao.CmsBtBeatInfoDao;
import com.voyageone.web2.cms.wsdl.dao.CmsBtTaskDao;
import com.voyageone.web2.cms.wsdl.models.CmsBtBeatInfoModel;
import com.voyageone.web2.cms.wsdl.models.CmsBtTaskModel;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.sdk.api.VoApiClient;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionCodeModel;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;
import com.voyageone.web2.sdk.api.request.PromotionCodeGetRequest;
import com.voyageone.web2.sdk.api.request.PromotionModelsGetRequest;
import com.voyageone.web2.sdk.api.request.PromotionsGetRequest;
import com.voyageone.web2.sdk.api.response.PromotionCodeGetResponse;
import com.voyageone.web2.sdk.api.response.PromotionModelsGetResponse;
import com.voyageone.web2.sdk.api.response.PromotionsGetResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.voyageone.common.util.ExcelUtils.getString;

/**
 * 价格披露的数据服务
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

    /**
     * 创建一个价格披露任务
     *
     * @param taskBean 输入的创建参数
     * @param user     当前用户
     * @return 创建完成后的模型
     */
    public TaskBean create(TaskBean taskBean, UserSessionBean user) {

        CmsBtPromotionModel promotion = getPromotion(taskBean.getPromotion_id());

        if (promotion == null)
            throw new BusinessException("7000001");

        if (!taskBean.getConfig().isValid())
            throw new BusinessException("7000002");

        // 尝试检查任务的名称, 是否已经存在
        List<CmsBtTaskModel> taskModels = taskDao.selectByName(
                taskBean.getPromotion_id(),
                taskBean.getTask_name(),
                PromotionTypeEnums.Type.JIAGEPILU.getTypeId());

        if (!taskModels.isEmpty())
            throw new BusinessException("7000003");

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

    /**
     * 获取 task_id 任务下的所有价格披露信息
     *
     * @param task_id 任务 ID
     * @param flag    指定的任务状态
     * @param offset  分页偏移量
     * @param size    分页数
     * @return 数据集合
     */
    public List<CmsBtBeatInfoModel> getAllBeat(int task_id, BeatFlag flag, int offset, int size) {

        return beatInfoDao.selectListByTask(task_id, flag, offset, size);
    }

    /**
     * 获取 task_id 任务下的所有价格披露信息的总数, 参看方法 getAllBeat
     *
     * @param task_id 任务 ID
     * @param flag    指定的任务状态
     * @return 数据集合
     */
    public int getAllBeatCount(int task_id, BeatFlag flag) {

        return beatInfoDao.selectListByTaskCount(task_id, flag);
    }

    /**
     * 获取价格披露的统计信息
     * @param task_id 任务 ID
     * @return 统计信息, List[Map{flag&count}]
     */
    public List<Map> getBeatSummary(int task_id) {
        List<Map> result = beatInfoDao.selectSummary(task_id);
        // 数据查询出来的是整数, 转换为枚举
        for (Map map : result)
            map.put("flag", BeatFlag.valueOf((Integer) map.get("flag")));
        return result;
    }


    public List<CmsBtBeatInfoModel> importBeatInfo(int task_id, int size, MultipartFile file, UserSessionBean user) {

        // 如果存在以下标识数据, 就不能重新导入
        int count = beatInfoDao.selectCountInFlags(task_id,
                BeatFlag.BEATING, BeatFlag.RE_FAIL, BeatFlag.REVERT, BeatFlag.SUCCESS);

        if (count > 0)
            throw new BusinessException("7000004");

        beatInfoDao.deleteByTask(task_id);

        Workbook wb;

        try {
            wb = WorkbookFactory.create(file.getInputStream());
        } catch (IOException | InvalidFormatException e) {
            throw new BusinessException("7000005");
        }

        Sheet sheet = wb.getSheetAt(0);

        List<CmsBtBeatInfoModel> models = new ArrayList<>();

        for (Row row : sheet) {

            String value = getString(row, 0, "#");

            if (!StringUtils.isNumeric(value))
                throw new BusinessException("7000006");

            CmsBtBeatInfoModel model = new CmsBtBeatInfoModel();

            model.setNum_iid(Long.valueOf(value));
            model.setProduct_code(getString(row, 1));
            model.setBeatFlag(BeatFlag.STOP); // syn_flag
            model.setTask_id(task_id);
            model.setCreater(user.getUserName());
            model.setModifier(user.getUserName());
            String now = DateTimeUtil.getNow();
            model.setCreated(now);
            model.setModified(now);

            models.add(model);
        }

        beatInfoDao.insertList(models);

        beatInfoDao.updateDiffPromotionMessage(task_id, "与 Promotion 信息不符");

        return getAllBeat(task_id, null, 0, size);
    }

    public byte[] downloadBeatInfo(int task_id) {

        List<CmsBtBeatInfoModel> beatInfoModels = beatInfoDao.selectListByTask(task_id);

        // 注意: HSSFWorkbook 为 2003 的 xls 格式
        try (Workbook book = new HSSFWorkbook()) {

            Sheet sheet = book.createSheet();

            // 设置查询类型
            for (int i = 0; i < beatInfoModels.size(); i++) {

                CmsBtBeatInfoModel model = beatInfoModels.get(i);

                Row row = row(sheet, i);

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
            throw new BusinessException("7000007", e);
        }
    }

    public int setFlag(int beat_id, BeatFlag flag, UserSessionBean user) {

        if (flag == null)
            throw new BusinessException("7000002");

        CmsBtBeatInfoModel beatInfoModel = beatInfoDao.selectOneById(beat_id);

        if (beatInfoModel == null)
            return 0;

        beatInfoModel.setBeatFlag(flag);
        beatInfoModel.setModifier(user.getUserName());
        return beatInfoDao.updateFlag(beatInfoModel);
    }

    public int setFlags(int task_id, BeatFlag flag, UserSessionBean user) {

        if (flag == null)
            throw new BusinessException("7000002");

        return beatInfoDao.updateFlags(task_id, flag, user.getUserName());
    }

    public int control(Integer beat_id, Integer task_id, BeatFlag flag, UserSessionBean user) {
        if (beat_id != null)
            return setFlag(beat_id, flag, user);
        else if (task_id != null)
            return setFlags(task_id, flag, user);
        else
            return 0;
    }

    public List<Map<String, Object>> getNewNumiid(Integer task_id) {
        if (task_id == null) return null;
        CmsBtTaskModel taskModel = taskDao.selectByIdWithPromotion(task_id);
        if (taskModel == null) return null;
        Map<String, Object> map = new HashMap<>();
        map.put("promotionId", taskModel.getPromotion_id());
        PromotionModelsGetRequest request = new PromotionModelsGetRequest();
        request.setParam(map);
        PromotionModelsGetResponse response = apiClient.execute(request);
        return response.getPromotionGroups();
    }

    public List<CmsBtPromotionCodeModel> getCodes(int promotionId, int modelId) {
        Map<String, Object> map = new HashMap<>();
        map.put("promotionId", promotionId);
        map.put("modelId", modelId);
        PromotionCodeGetRequest request = new PromotionCodeGetRequest();
        request.setParam(map);
        PromotionCodeGetResponse response = apiClient.execute(request);
        return response.getCodeList();
    }

    public List<CmsBtBeatInfoModel> addCheck(int task_id, String num_iid) {
        CmsBtTaskModel taskModel = taskDao.selectByIdWithPromotion(task_id);
        if (taskModel == null)
            throw new BusinessException("没找到 Promotion");
        return beatInfoDao.selectListByNumiidInOtherTask(taskModel.getPromotion_id(), task_id, num_iid);
    }

    public Integer add(int task_id, String num_iid, String code, UserSessionBean user) {
        CmsBtTaskModel taskModel = taskDao.selectByIdWithPromotion(task_id);
        if (taskModel == null) return null;
        CmsBtBeatInfoModel model = beatInfoDao.selectOneByNumiid(task_id, num_iid);
        if (model != null) {
            if (model.getProduct_code().equals(code))
                return 0;
            model.setProduct_code(code);
            model.setModifier(user.getUserName());
            return beatInfoDao.updateCode(model);
        }
        model = new CmsBtBeatInfoModel();
        model.setNum_iid(Long.valueOf(num_iid));
        model.setProduct_code(code);
        model.setBeatFlag(BeatFlag.STOP);
        model.setTask_id(task_id);
        model.setCreater(user.getUserName());
        model.setModifier(user.getUserName());
        String now = DateTimeUtil.getNow();
        model.setCreated(now);
        model.setModified(now);
        List<CmsBtBeatInfoModel> list = new ArrayList<>();
        list.add(model);
        return beatInfoDao.insertList(list);
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
