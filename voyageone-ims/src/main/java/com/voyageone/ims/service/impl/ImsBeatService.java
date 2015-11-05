package com.voyageone.ims.service.impl;

import com.voyageone.base.BaseAppService;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.core.MessageConstants.ComMsg;
import com.voyageone.core.ajax.dt.DtResponse;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.ims.dao.impl.ImsBeatDao;
import com.voyageone.ims.model.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.Color;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.voyageone.common.configs.Properties.readValue;
import static com.voyageone.common.util.ExcelUtils.getNum;
import static com.voyageone.common.util.ExcelUtils.getString;
import static com.voyageone.core.MessageConstants.ComMsg.UPDATE_BY_OTHER;
import static com.voyageone.ims.ImsConstants.BeatProps.*;
import static com.voyageone.ims.ImsMsgConstants.BeatMsg.*;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * 价格披露
 * <p>
 * Created by Jonas on 15/6/29.
 */
@Service
public class ImsBeatService extends BaseAppService {

    private static final int MAX_INSERT_LENGTH = 1000;

    private static final String ERR_DOC_LEV_A = "A";

    private static final String ERR_DOC_LEV_B = "B";

    private static final String ERR_DOC_LEV_C = "C";

    @Autowired
    private TransactionRunner transactionRunner;

    @Autowired
    private ImsBeatDao imsBeatDao;

    /**
     * 获取目标渠道下，所有可用的店铺
     *
     * @param order_channel_id 渠道
     * @return 店铺 ShopBean
     */
    public List<ShopBean> getShops(String order_channel_id) {

        Channel channel = Channel.valueOfId(order_channel_id);

        // 检查渠道
        if (channel == null) throw new BusinessException(ComMsg.NOT_FOUND_CHANNEL);

        return ShopConfigs.getChannelShopList(channel.getId());
    }

    /**
     * 创建一个价格披露的任务
     *
     * @param beat 新任务
     * @param user 用户
     * @return 任务
     */
    public ImsBeat create(ImsBeat beat, UserSessionBean user) {

        $info("准备创建价格披露");

        // 检查提交的任务数据是否合法
        createCheck(beat);

        $info(1, "检查完成");

        // 补全
        beat.setCreater(user.getUserName());
        beat.setModifier(user.getUserName());

        // 如果检查没问题，就直接插入到数据库
        if (imsBeatDao.insert(beat) < 1) {
            $info(1, "插入没有返回任何受影响行数");
            throw new BusinessException(UPDATE_BY_OTHER);
        }

        $info(1, "已插入数据库");

        // 从数据库反查任务，并返回
        return imsBeatDao.selectByFinger(beat);
    }

    /**
     * 下载价格披露详细商品信息的 Excel 表格
     *
     * @param beat_id 价格披露任务标识
     * @return 文件字节码
     */
    public byte[] getBeatItemsExcel(long beat_id) throws IOException, InvalidFormatException {

        String templatePath = readValue(TEMPLATE);

        List<ImsBeatItem> items = imsBeatDao.selectItems(beat_id);

        $info("准备生成 Item 文档 [ %s ]", items.size());

        $info("准备打开文档 [ %s ]", templatePath);

        try (InputStream inputStream = new FileInputStream(templatePath);
             Workbook book = WorkbookFactory.create(inputStream)) {

            /*
             * 现有表格的列:
             * 0: Code
             * 1: Num_iid
             * 2: Price
             * 3: Comment
             *
             * 以下为隐藏的，程序使用的列
             * 4: url_key
             * 5: image_name
             * 6: BeatID
             */

            CellStyle lock = createLockStyle(book);

            CellStyle unlock = createUnLockStyle(book);

            Sheet sheet = book.getSheetAt(0);

            sheet.protectSheet("voyageone");

            for (int i = 0; i < items.size(); i++) {

                ImsBeatItem item = items.get(i);

                Row row = row(sheet, i + 1);

                cell(row, 0, lock).setCellValue(item.getCode());

                cell(row, 1, lock).setCellValue(item.getNum_iid());

                cell(row, 2, unlock).setCellValue(Constants.EmptyString);

                cell(row, 3, lock).setCellValue(Constants.EmptyString);

                cell(row, 4, lock).setCellValue(item.getUrl_key());

                cell(row, 5, lock).setCellValue(item.getImage_name());
            }

            // 设置第一行的第六列，只设置一个单元格即可
            cell(sheet.getRow(1), 6, lock).setCellValue(items.get(0).getBeat_id());

            $info("文档写入完成");

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                book.write(outputStream);

                $info("已写入输出流");

                return outputStream.toByteArray();
            }
        }
    }

    /**
     * 保存详细的商品信息
     *
     * @param file 之前下载的详细信息 Excel 表
     * @param user 用户
     */
    public int saveBeatIcon(MultipartFile file, UserSessionBean user) throws IOException, InvalidFormatException {

        if (file.getSize() < 1)
            throw new BusinessException(NO_UPLOAD_FILE);

        try (Workbook book = WorkbookFactory.create(file.getInputStream())) {

            List<ImsBeatItemTemp> items = new ArrayList<>();

            Sheet sheet = book.getSheetAt(0);

            $info("已打开上传的文档 [ %s ]", sheet.getSheetName());

            Double value = getNum(sheet.getRow(1), 6);

            $info("读取文档对应任务主 ID [ %s ]", value);

            if (value == null)
                throw new BusinessException(EXCEL_NO_BEAT_ID);

            long beat_id = value.longValue();

            ImsBeat beat = imsBeatDao.selectById(beat_id);

            if (beat == null)
                throw new BusinessException(NO_BEAT);

            /*
             * 现有表格的列:
             * 0: Code
             * 1: Num_iid
             * 2: Price
             * 3: Comment
             *
             * 以下为隐藏的，程序使用的列
             * 4: url_key
             * 5: image_name
             * 6: BeatID
             */

            for (Row row : sheet) {

                if (row.getRowNum() == 0) continue;

                items.add(createTempItem(beat_id, row, user));
            }

            $info("已读入数据 [ %s ]", items.size());

            // 过滤错误商品
            List<ImsBeatItemTemp> errItems = items.stream().filter(this::checkRead).collect(toList());

            $info("过滤错误信息 [ %s ]", errItems.size());

            // 如果过滤后，存在错误数据。则生成错误文档
            if (errItems.size() > 0) {
                writeErrDoc(beat_id, "读取商品时", errItems, ERR_DOC_LEV_B);
                throw new BusinessException(READ_PRICE_DOC_ERR);
            }

            int clearCount = clearItems(beat);

            $info("准备更新价格，已清空 Item [ %s ]", clearCount);

            int insertCount = insertItems(items);

            $info("更新价格，已插入 [ %s ]", insertCount);

            return insertCount;
        }
    }

    /**
     * 获取所有任务
     *
     * @param channel_id 渠道
     * @param cart_id    店铺
     * @param offset     起始行
     * @param limit      行数
     * @return 任务集合
     */
    public DtResponse<List<ImsBeat>> getBeats(String channel_id, String cart_id, int offset, int limit) {
        DtResponse<List<ImsBeat>> response = new DtResponse<>();

        response.setData(imsBeatDao.selectByChannel(channel_id, cart_id, offset, limit));

        response.setRecordsTotal(imsBeatDao.selectCountByChannel(channel_id, cart_id));

        return response;
    }

    /**
     * 获取任务的所有项
     *
     * @param beat     任务
     * @param beat_flg 用于筛选的状态
     * @param num_iid  用于搜索的 num_iid
     * @param offset   起始行
     * @param limit    行数
     * @return 任务项集合
     */
    public DtResponse<List<ImsBeatItem>> getBeatItems(ImsBeat beat, Integer beat_flg, String num_iid, int offset, int limit) {

        DtResponse<List<ImsBeatItem>> response = new DtResponse<>();

        response.setData(imsBeatDao.selectItems(beat.getBeat_id(), beat_flg, num_iid, offset, limit));

        response.setRecordsTotal(imsBeatDao.selectItemsCount(beat.getBeat_id(), beat_flg, num_iid));

        return response;
    }

    /**
     * 是否可以修改现有任务信息
     *
     * @param beat_id 任务标识
     * @return 是否可以修改现有任务信息
     */
    public boolean canModifyItem(long beat_id) {

        // 在任务结束之前，有任务运行，则可以修改

        return imsBeatDao.selectCountInRunning(beat_id) > 0;
    }

    /**
     * 增加任务项
     *
     * @param beat_id 任务
     * @param file    包含初始商品信息的文件
     */
    public int addBeatItems(long beat_id, boolean isCode, MultipartFile file, UserSessionBean user) throws IOException, InvalidFormatException {

        // 任务的添加，只有在最早的状态下可以处理
        // 有任务启动后，就不能批量处理，只能单个处理

        ImsBeat beat = imsBeatDao.selectById(beat_id);

        if (beat == null) {
            throw new BusinessException(NO_BEAT);
        }

        $info("读取任务 [ %s ] [ %s ]", beat_id, beat.getDescription());

        // 检查是否可以执行刷新
        if (!canReUpload(beat)) {
            throw new BusinessException(CANT_REUPLOAD);
        }

        // 从 A 文档中读取 code 或 num_iid
        // 生成产品信息，如果信息出错，则方法内部会直接中断，并生成错误文档
        List<ImsBeatItemTemp> products = getProductFromAFile(beat_id, isCode, file);

        $info("成功获取商品信息 [ %s ]", products.size());

        // 到达这一步说明信息无错误
        // 清空原有数据，插入新的数据
        int clearCount = clearItems(beat);

        $info("清空 Item 数据 [ %s ]", clearCount);

        // 补全属性
        for (ImsBeatItemTemp item : products) {
            item.setBeat_id(beat_id);
            item.setBeatFlg(BeatFlg.Startup);
            item.setCreater(user.getUserName());
            item.setModifier(user.getUserName());
        }

        int insertCount = insertItems(products);

        $info("插入 Item 数据 [ %s ]", insertCount);

        return insertCount;
    }

    /**
     * 下载指定任务的错误文档
     *
     * @param beat_id 任务
     * @param errLev  步骤
     */
    public byte[] getErrExcel(long beat_id, String errLev) throws IOException, InvalidFormatException {

        if (StringUtils.isEmpty(errLev))
            return null;

        // 如果是 C 阶段。则生成后返回
        if (errLev.equals(ERR_DOC_LEV_C))
            return writeErrExcelC(beat_id);

        String outputPath = getErrDocPath(beat_id, errLev);
        File file = new File(outputPath);
        if (!file.exists())
            return null;

        try (InputStream inputStream = new FileInputStream(file)) {
            return IOUtils.toByteArray(inputStream);
        }
    }

    /**
     * 控制单个或全部任务的状态
     *
     * @param beat_id 任务
     * @param item_id 子任务
     * @param action  执行状态
     * @return 成功处理的任务数
     */
    public int control(long beat_id, String item_id, String action) {

        BeatFlg beatFlg = getBeatFlgFromAction(action);

        $info("从 Action 参数 [ %s ] 转换获取 [ %s ]", action, beatFlg);

        if (beatFlg == null)
            throw new BusinessException(CONTROL_ERR);

        if (!StringUtils.isEmpty(item_id)) {
            // 查询单个，处理数据。直接更改
            // 不检查类型，直接用于查询
            $info("准备单个处理: [ %s ] [ %s ]", beat_id, item_id);

            ImsBeatItem item = imsBeatDao.selectItem(item_id, beat_id);

            if (item == null)
                throw new BusinessException(NO_BEAT_ITEM);

            item.setBeatFlg(beatFlg);

            int updateCount = imsBeatDao.updateBeatFlg(item);

            $info("已更新 [ %s ]", updateCount);

            return updateCount;
        }

        // 批量处理所有任务下的子任务
        // 查询所有数据，重新填充数据
        // 清空，在重新插入

        $info("准备对任务 [ %s ] 全量更新", beat_id);

        ImsBeat beat = imsBeatDao.selectById(beat_id);

        if (beat == null)
            throw new BusinessException(NO_BEAT);

        List<ImsBeatItem> items = imsBeatDao.selectItems(beat_id);

        $info("子任务查询 [ %s ]", items.size());

        for (ImsBeatItem item : items) {
            item.setBeatFlg(beatFlg);
        }

        transactionRunner.runWithTran(() -> {
            int deleteCount = imsBeatDao.deleteItems(beat);

            $info("清空数 [ %s ]", deleteCount);

            int insertCount = insertItems(items);

            $info("重新插入 [ %s ]", insertCount);

            if (deleteCount != insertCount)
                throw new BusinessException(PART_INSERT_ERR);
        });

        return items.size();
    }

    /**
     * 获取主任务的统计信息
     *
     * @param beat 主任务
     * @return 统计信息
     */
    public List<ImsBeatInfo> getBeatSummary(ImsBeat beat) {
        return imsBeatDao.selectInfo(beat);
    }

    /**
     * 更新一项的价格，并重启这个子任务
     *
     *
     * @param beat_id 主任务
     * @param item_id 子任务
     * @param price   新价格
     * @return 更新数
     */
    public int updateItemPrice(long beat_id, String item_id, double price) {

        if (price < 1)
            throw new BusinessException(PRICE_ERR);

        ImsBeatItem item = imsBeatDao.selectItem(item_id, beat_id);

        if (item == null)
            throw new BusinessException(NO_BEAT_ITEM);

        NumberFormat format = new DecimalFormat("#.#");

        item.setPrice(format.format(price));

        item.setBeatFlg(BeatFlg.Waiting);

        return imsBeatDao.updateBeatFlg(item);
    }

    public int addBeatItem(ImsBeatItem item, UserSessionBean user) {

        if (item.getBeat_id() < 1)
            throw new BusinessException(NO_BEAT);

        ImsBeat beat = imsBeatDao.selectById(item.getBeat_id());

        if (beat == null)
            throw new BusinessException(NO_BEAT);

        item.setCreater(user.getUserName());
        item.setModifier(user.getUserName());

        if (!StringUtils.isEmpty(item.getCode())) {

            String num_iid = imsBeatDao.selectNumiidByCode(item, beat);

            if (StringUtils.isEmpty(num_iid))
                throw new BusinessException(NO_IMS_INFO);

            item.setNum_iid(num_iid);

        } else if (!StringUtils.isEmpty(item.getNum_iid())) {

            String code = imsBeatDao.selectCodeByNumiid(item, beat);

            if (StringUtils.isEmpty(code))
                throw new BusinessException(NO_CMS_INFO);

            item.setCode(code);

        } else {
            throw new BusinessException(NO_IMS_INFO);
        }

        int count = imsBeatDao.selectItemCountByNumiid(item, beat);

        if (count > 0)
            throw new BusinessException(NUM_IID_EXISTS);

        item.setBeatFlg(BeatFlg.Waiting);
        return imsBeatDao.insertItem(item);
    }

    public int setItemCode(ImsBeatItem item, UserSessionBean user) {

        if (item.getBeat_id() < 1)
            throw new BusinessException(NO_BEAT);

        ImsBeat beat = imsBeatDao.selectById(item.getBeat_id());

        if (beat == null)
            throw new BusinessException(NO_BEAT);

        item.setModifier(user.getUserName());

        if (StringUtils.isEmpty(item.getCode()))
            throw new BusinessException(NO_CMS_INFO);

        String num_iid = imsBeatDao.selectNumiidByCode(item, beat);

        if (StringUtils.isEmpty(num_iid) || !num_iid.equals(item.getNum_iid()))
            throw new BusinessException(DIFF_NUM_IID);

        item.setBeatFlg(BeatFlg.Waiting);
        return imsBeatDao.updateItemCode(item);
    }

    private void createCheck(ImsBeat beat) {

        CartBean cart = ShopConfigs.getCart(beat.getCart_id());

        OrderChannelBean channel = ChannelConfigs.getChannel(beat.getChannel_id());

        StringBuilder messageBuilder = new StringBuilder();

        if (cart == null) messageBuilder.append("<li>店铺错误</li>");

        if (channel == null) messageBuilder.append("<li>渠道错误</li>");

        if (cart != null && channel != null) {
            ShopBean shop = ShopConfigs.getShop(channel, cart);
            if (shop == null)
                messageBuilder.append(format("<li>没有找到 [ %s ] [ %s ] 店</li>", channel.getName(), cart.getName()));
        }

        if (StringUtils.isEmpty(beat.getDescription())) {
            messageBuilder.append("<li>没有填写描述</li>");
        } else if (imsBeatDao.selectCountByDescription(beat) > 0) {
            messageBuilder.append("<li>输入的描述已经被占用，请重新输入后，再次尝试</li>");
        }

        if (StringUtils.isEmpty(beat.getTemplate_url())) {
            messageBuilder.append("<li>没有输入图片模板</li>");
        }

        if (StringUtils.isEmpty(beat.getEnd())) {
            messageBuilder.append("<li>没有输入时间</li>");
        } else {
            Date end = DateTimeUtil.parse(beat.getEnd());
            Date gmtEnd = DateTimeUtil.addHours(end, -8);

            // 如果正确，则直接放回 bean
            beat.setEnd(DateTimeUtil.format(gmtEnd, DateTimeUtil.DEFAULT_DATETIME_FORMAT));
        }

        // --- 最终进行错误判断 ---
        if (messageBuilder.length() > 0) {
            $info(1, messageBuilder.toString());
            throw new BusinessException(BEAT_UNVALID, format("<ul>%s</ul>", messageBuilder));
        }
    }

    private CellStyle createUnLockStyle(Workbook book) {
        CellStyle cellStyle = book.createCellStyle();

        cellStyle.setLocked(false);

        return cellStyle;
    }

    private CellStyle createLockStyle(Workbook book) {
        CellStyle cellStyle = book.createCellStyle();

        cellStyle.setLocked(true);

        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);

        if (book instanceof HSSFWorkbook) {

            HSSFCellStyle hssfCellStyle = (HSSFCellStyle) cellStyle;

            hssfCellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
            hssfCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            hssfCellStyle.setTopBorderColor(HSSFColor.GREY_50_PERCENT.index);
            hssfCellStyle.setRightBorderColor(HSSFColor.GREY_50_PERCENT.index);
            hssfCellStyle.setBottomBorderColor(HSSFColor.GREY_50_PERCENT.index);
            hssfCellStyle.setLeftBorderColor(HSSFColor.GREY_50_PERCENT.index);

        } else if (book instanceof XSSFWorkbook) {

            XSSFCellStyle xssfCellStyle = (XSSFCellStyle) cellStyle;

            XSSFColor lightGray = new XSSFColor(Color.lightGray);
            XSSFColor gray = new XSSFColor(Color.gray);

            xssfCellStyle.setFillForegroundColor(lightGray);
            xssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            xssfCellStyle.setTopBorderColor(gray);
            xssfCellStyle.setRightBorderColor(gray);
            xssfCellStyle.setBottomBorderColor(gray);
            xssfCellStyle.setLeftBorderColor(gray);

        }

        return cellStyle;
    }

    private boolean checkRead(ImsBeatItemTemp item) {

        // 检查每项从价格文档读取的内容

        String code = item.getCode();

        String num_iid = item.getNum_iid();

        if (StringUtils.isAnyEmpty(code, num_iid)) {
            item.setComments("读取错误，请对照提交的表格验证");
        }

        if (StringUtils.isEmpty(item.getPrice())) {
            item.setComments("没有获取到价格。");
        }

        return item.hasComments();
    }

    private ImsBeatItemTemp createTempItem(long beat_id, Row row, UserSessionBean user) {

        String code = getString(row, 0);

        String num_iid = getString(row, 1);

        String price = getString(row, 2, "#.##");

        String url_key = getString(row, 4);

        String image_name = getString(row, 5);

        ImsBeatItemTemp item = new ImsBeatItemTemp();

        item.setSrc("对应原表行号：" + row.getRowNum());

        item.setBeat_id(beat_id);

        item.setBeatFlg(BeatFlg.Startup);

        item.setCode(code);

        item.setNum_iid(num_iid);

        item.setPrice(price);

        item.setUrl_key(url_key);

        item.setImage_name(image_name);

        item.setCreater(user.getUserName());

        item.setModifier(user.getUserName());

        return item;
    }

    private <T extends ImsBeatItem> int insertItems(List<T> products) {
        int insertLen = MAX_INSERT_LENGTH;
        int total = products.size();
        int insertCount = 0;

        for (int count = 0; count < calcSubCount(total, insertLen); count++) {

            int start = insertLen * count;

            int end = insertLen * (count + 1);

            if (end > total) end = total;

            List<T> subList = products.subList(start, end);

            insertCount += imsBeatDao.insertItems(subList);

            $info(1, "批量插入 Item [ %s ]，第 [ %s ] 次，本次预计 [ %s ]，实际数量（包含之前数量）[ %s ]",
                    total, count, subList.size(), insertCount);
        }

        if (insertCount != products.size())
            throw new BusinessException(PART_INSERT_ERR);

        return insertCount;
    }

    private int clearItems(ImsBeat beat) {
        return imsBeatDao.deleteItems(beat);
    }

    private boolean canReUpload(ImsBeat beat) {

        // 在结束时间之前，有任务在运行时，则不能重新上传。

        return imsBeatDao.selectCountInRunning(beat.getBeat_id()) < 1;
    }

    private List<ImsBeatItemTemp> getProductFromAFile(long beat_id, boolean isCode, MultipartFile file) throws IOException, InvalidFormatException {

        Workbook wb;

        try {
            wb = WorkbookFactory.create(file.getInputStream());
        } catch (IOException | InvalidFormatException e) {
            $info(1, "读取 A 文档出现异常 [ %s ]", e.getMessage());
            throw new BusinessException(READ_EXCEL_ERR, e.getMessage());
        }

        // 默认读取第一个表格
        Sheet sheet = wb.getSheetAt(0);

        List<ImsBeatItem> items = new ArrayList<>();

        for (Row row : sheet) {

            String value = getString(row, 0, "#");

            if (StringUtils.isEmpty(value)) break;

            ImsBeatItem item = new ImsBeatItem();

            item.setBeat_id(beat_id);

            if (isCode) item.setCode(value);

            else item.setNum_iid(value);

            items.add(item);
        }

        $info(1, "从 A 文档中读取 [ %s ] 行", items.size());

        if (items.size() < 1) {
            throw new BusinessException(NO_DATA_IN_FILE);
        }

        // 根据输入的内容反差 code，最终和数据库比对时，以最终的主商品 code 为基准
        // 插入生成的临时数据
        int clearCount = clearTempItems(beat_id);

        $info(1, "清空临时数据 [ %s ]", clearCount);

        insertTempItems(items);

        // 更具不同的条件查询，但是输出的应该是 !相同 且 !完整 的
        List<ImsBeatItemTemp> products = isCode ? imsBeatDao.selectProductsByCode(beat_id)
                : imsBeatDao.selectProductsByNumiid(beat_id);

        $info(1, "从数据库查询商品 [ isCode: %s ] [ %s ]", isCode, products.size());

        // 校验产品数据
        List<ImsBeatItemTemp> errProducts = products.stream().filter(this::checkProduct).collect(toList());

        $info(1, "过滤错误商品 [ %s ]", errProducts.size());

        // 如果过滤后，存在错误数据。则生成错误文档
        if (errProducts.size() > 0) {
            writeErrDoc(beat_id, "查询方式：" + (isCode ? "code" : "num_iid"), errProducts, ERR_DOC_LEV_A);
            throw new BusinessException(HAS_ERR_PRODUCT);
        }

        // 如果成功了，则即时清除临时数据。
        // 不成功，则会通过异常终止执行。会由下一次删除
        clearTempItems(beat_id);

        return products;
    }

    private int clearTempItems(long beat_id) {
        return imsBeatDao.deleteTempItems(beat_id);
    }

    private void writeErrDoc(long beat_id, String comment, List<ImsBeatItemTemp> errProducts, String lev) throws IOException, InvalidFormatException {

        String errTemplatePath = readValue(ERROR_CODE_TEMPLATE);
        String outputPath = getErrDocPath(beat_id, lev);

        // 创建写入
        try (InputStream inputStream = new FileInputStream(errTemplatePath);
             Workbook book = WorkbookFactory.create(inputStream)) {

            Sheet sheet = book.getSheetAt(0);

            // 设置查询类型
            Row row = row(sheet, 0);
            cell(row, 0, null).setCellValue(comment);

            for (int i = 0; i < errProducts.size(); i++) {

                ImsBeatItemTemp product = errProducts.get(i);

                row = row(sheet, i + 2);

                cell(row, 0, null).setCellValue(product.getSrc());

                cell(row, 1, null).setCellValue(product.getNum_iid());

                cell(row, 2, null).setCellValue(product.getCode());

                cell(row, 3, null).setCellValue(product.getComments());
            }

            // 打开保存
            try (OutputStream outputStream = new FileOutputStream(outputPath)) {
                book.write(outputStream);
            }
        } catch (InvalidFormatException | IOException e) {
            $info(2, "写入错误 %s 文档出现异常 [ %s ]", lev, e.getMessage());
            // 非业务类，强制终止
            throw e;
        }
    }

    private String getErrDocPath(long beat_id, String lev) {
        String errDir = readValue(ERROR_DIR);
        return format("%s/价格披露.%s.ERR.DOC.%s.xlsx", errDir, beat_id, lev);
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

    private boolean checkProduct(ImsBeatItemTemp item) {

        /*
         * 现在部分店铺没有使用 wms，所以库存暂时忽略。
         * 如果 code 和 num iid 抽取为空，则 ims 数据有问题。进而影响 cms 的数据抽出
         * 如果上述数据没有问题。则，如果与 image 相关的 name 和 key 都为空。则 cms 数据抽取错误
         */

        String code = item.getCode();

        // code 由 num_iid 关联反查，如果 num_iid 没查询到，则必然 code 为空
        if (StringUtils.isEmpty(code)) {
            item.setComments("没有从 ims 中找到 code 和 num_iid");
        }

        return item.hasComments();
    }

    private int insertTempItems(List<ImsBeatItem> items) {
        int insertLen = MAX_INSERT_LENGTH;
        int total = items.size();
        int insertCount = 0;

        for (int count = 0; count < calcSubCount(total, insertLen); count++) {

            int start = insertLen * count;

            int end = insertLen * (count + 1);

            if (end > total) end = total;

            List<ImsBeatItem> subList = items.subList(start, end);

            insertCount += imsBeatDao.insertTempItems(subList);

            $info(1, "批量插入 TempItem [ %s ]，第 [ %s ] 次，本次预计 [ %s ]，实际数量（包含之前数量）[ %s ]",
                    total, count, subList.size(), insertCount);
        }

        if (insertCount != items.size())
            throw new BusinessException(PART_INSERT_ERR);

        return insertCount;
    }

    private static int calcSubCount(int totalCount, int length) {
        int count = totalCount / length;
        int fix = totalCount % length;

        return count + (fix > 0 ? 1 : 0);
    }

    private byte[] writeErrExcelC(long beat_id) throws IOException, InvalidFormatException {

        // 获取任务的当前子任务数据
        ImsBeat beat = imsBeatDao.selectById(beat_id);

        if (beat == null)
            throw new BusinessException(NO_BEAT);

        List<ImsBeatItem> items = imsBeatDao.selectItems(beat_id);

        /*
         * 现有为 5 列
         * Num iid
         * Code
         * Price
         * Status
         * Comment
         */

        String errTemplatePath = readValue(ERROR_CODE_TEMPLATE);

        // 打开文档
        try (InputStream inputStream = new FileInputStream(errTemplatePath);
             Workbook book = WorkbookFactory.create(inputStream)) {

            Sheet sheet = book.getSheetAt(0);

            for (int i = 0; i < items.size(); i++) {

                ImsBeatItem item = items.get(i);

                Row row = row(sheet, i + 2);

                cell(row, 0, null).setCellValue(item.getNum_iid());

                cell(row, 1, null).setCellValue(item.getCode());

                cell(row, 2, null).setCellValue(item.getPrice());

                cell(row, 3, null).setCellValue(item.getBeatFlg().getCnName());

                cell(row, 4, null).setCellValue(item.getComment());
            }

            // 最后写入生成时间
            cell(row(sheet, 0), 0, null).setCellValue("文档生成时间点：" + DateTimeUtil.getLocalTime(8));

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                book.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }

    private BeatFlg getBeatFlgFromAction(String action) {
        switch (action) {
            case "startup":
            case "redo":
                return BeatFlg.Waiting;
            case "cancel":
                return BeatFlg.Cancel;
            case "stop":
                return BeatFlg.Startup;
        }
        return null;
    }
}

