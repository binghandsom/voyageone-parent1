package com.voyageone.ims.controller;

import com.voyageone.base.BaseController;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.ajax.dt.DtRequest;
import com.voyageone.core.ajax.dt.DtResponse;
import com.voyageone.core.ajax.dt.DtSearch;
import com.voyageone.ims.ImsUrlConstants.BeatIconUrls;
import com.voyageone.ims.model.ImsBeat;
import com.voyageone.ims.model.ImsBeatInfo;
import com.voyageone.ims.model.ImsBeatItem;
import com.voyageone.ims.service.impl.ImsBeatService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 为打标画面提供数据和数据处理
 * Created by Jonas on 15/6/29.
 */
@Controller
@RequestMapping(value = BeatIconUrls.ROOT, method = RequestMethod.POST)
public class ImsTaskBeatController extends BaseController {

    @Autowired
    private ImsBeatService imsBeatService;

    /**
     * 获取渠道下的所有店铺
     */
    @RequestMapping(BeatIconUrls.GET_CARTS)
    @ResponseBody
    public AjaxResponseBean getCarts(@RequestBody Map<String, Object> params) {

        String order_channel_id = String.valueOf(params.get("order_channel_id"));

        List<ShopBean> shopBeans = imsBeatService.getShops(order_channel_id);

        return success(shopBeans);
    }

    /**
     * 创建新的 价格披露 任务
     */
    @RequestMapping(BeatIconUrls.CREATE)
    @ResponseBody
    public AjaxResponseBean create(@RequestBody ImsBeat beat) throws IOException {

        ImsBeat dbBeat = imsBeatService.create(beat, getUser());

        return AjaxResponseBean.newResult(true, this).setResultInfo(dbBeat);
    }

    /**
     * 手动下载已经处理好的文件
     *
     * @param beat_id 目标活动
     * @return excel 文件，包含商品的 ims cms wms 信息
     * @throws IOException
     */
    @RequestMapping(value = BeatIconUrls.DOWN_ITEMS)
    public ResponseEntity<byte[]> downItems(@RequestParam long beat_id) throws IOException, InvalidFormatException {

        byte[] fileCode = imsBeatService.getBeatItemsExcel(beat_id);

        String fileName = String.format("[PRICE-DOC][%s][IMS-PRICE-BEAT][%s].xlsx", beat_id, DateTimeUtil.getLocalTime(getUserTimeZone()));

        return genResponseEntityFromBytes(fileName, fileCode);
    }

    @RequestMapping(BeatIconUrls.DOWN_ERR)
    public ResponseEntity downErr(@RequestParam long beat_id, @RequestParam String errLev) throws IOException, InvalidFormatException {

        byte[] fileCode = imsBeatService.getErrExcel(beat_id, errLev);

        if (fileCode == null) {
            AjaxResponseBean bean = AjaxResponseBean.newResult(false, this);

            bean.setMessage(String.format("没有找到文档 [ %s ] - [ %s ]", beat_id, errLev));

            return new ResponseEntity<>(bean, HttpStatus.OK);
        }

        String fileName = String.format("[RESULT-INFO-DOC][%s][IMS-PRICE-BEAT][%s].xlsx", beat_id, errLev);

        return genResponseEntityFromBytes(fileName, fileCode);
    }

    /**
     * 从文件中读取商品信息
     */
    @RequestMapping(BeatIconUrls.ADD_ITEMS)
    @ResponseBody
    public AjaxResponseBean addItems(@RequestParam long beat_id, @RequestParam boolean isCode, @RequestParam MultipartFile file) throws IOException, InvalidFormatException {

        imsBeatService.addBeatItems(beat_id, isCode, file, getUser());

        return AjaxResponseBean.newResult(true, this);
    }

    /**
     * 客服填写完成后，检查填写的内容，并预览结果
     */
    @RequestMapping(BeatIconUrls.SAVE_ITEMS)
    @ResponseBody
    public AjaxResponseBean saveItems(@RequestParam MultipartFile file) throws IOException, InvalidFormatException {
        imsBeatService.saveBeatIcon(file, getUser());

        return AjaxResponseBean.newResult(true, this);
    }

    /**
     * 根据 channel_id 查询价格披露任务，如果提供 cart_id 则附带 cart_id 的筛选条件
     */
    @RequestMapping(BeatIconUrls.DT_GET_BEATS)
    @ResponseBody
    public AjaxResponseBean dtGetBeats(@RequestBody DtRequest<BeatParam> dtRequest) {

        BeatParam param = dtRequest.getParam();

        int offset = dtRequest.getStart();

        int limit = dtRequest.getLength();

        DtResponse<List<ImsBeat>> dtResponse = imsBeatService.getBeats(param.channel_id, param.cart_id, offset, limit);

        // 格式化将显示数据的时期
        for (ImsBeat item : dtResponse.getData()) {
            item.setModified(DateTimeUtil.getLocalTime(item.getModified(), getUserTimeZone()));
            item.setEnd(DateTimeUtil.getLocalTime(item.getEnd(), getUserTimeZone()));
        }

        dtResponse.setDraw(dtRequest.getDraw());
        dtResponse.setRecordsFiltered(dtResponse.getRecordsTotal());

        return success(dtResponse);
    }

    @RequestMapping(BeatIconUrls.DT_GET_ITEMS)
    @ResponseBody
    public AjaxResponseBean dtGetItems(@RequestBody DtRequest<DtItemsParam> dtRequest) {

        int offset = dtRequest.getStart();

        int limit = dtRequest.getLength();

        DtSearch search = dtRequest.getSearch();

        // 主要表格数据
        DtResponse<List<ImsBeatItem>> dtResponse = imsBeatService.getBeatItems(dtRequest.getParam().beat,
                dtRequest.getParam().flg, search.getValue(), offset, limit);

        // 格式化将显示数据的时期
        for (ImsBeatItem item : dtResponse.getData())
            item.setModified(DateTimeUtil.getLocalTime(item.getModified(), getUserTimeZone()));

        dtResponse.setDraw(dtRequest.getDraw());
        dtResponse.setRecordsFiltered(dtResponse.getRecordsTotal());

        List<ImsBeatInfo> beatSummary = imsBeatService.getBeatSummary(dtRequest.getParam().beat);

        ItemRes res = new ItemRes();
        res.dtResponse = dtResponse;
        res.beatSummary = beatSummary;
        
        return success(res);
    }

    @RequestMapping(BeatIconUrls.CONTROL)
    @ResponseBody
    public AjaxResponseBean control(@RequestBody ControlParam param) {

        int count = imsBeatService.control(param.beat_id, param.item_id, param.action);

        return success(count);
    }

    @RequestMapping(BeatIconUrls.SET_PRICE)
    @ResponseBody
    public AjaxResponseBean setItemPrice(@RequestBody ControlParam param) {

        int count = imsBeatService.updateItemPrice(param.beat_id, param.item_id, param.price);

        return success(count);
    }

    @RequestMapping(BeatIconUrls.ADD_CODE)
    @ResponseBody
    public AjaxResponseBean addCode(@RequestBody ImsBeatItem item) {

        int count = imsBeatService.addBeatItem(item, getUser());

        return success(count);
    }

    @RequestMapping(BeatIconUrls.SET_CODE)
    @ResponseBody
    public AjaxResponseBean setCode(@RequestBody ImsBeatItem item) {

        int count = imsBeatService.setItemCode(item, getUser());
        
        return success(count);
    }

    private static class ItemRes {
        public DtResponse<List<ImsBeatItem>> dtResponse;
        public List<ImsBeatInfo> beatSummary;
    }

    private static class DtItemsParam {
        public ImsBeat beat;
        public Integer flg;
    }

    private static class BeatParam {
        public String channel_id;
        public String cart_id;
    }

    private static class ControlParam {
        public String item_id;
        public long beat_id;
        public String action;
        public double price;
    }
}
