package com.voyageone.web2.vms.views.feed;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.vms.feed.FeedFileService;
import com.voyageone.web2.base.BaseViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.voyageone.web2.vms.VmsConstants.TYPE_ID.IMPORT_FEED_FILE_STATUS;


/**
 * VmsFeedImportResultService
 * Created on 2016/7/11.
 * @author jeff.duan
 * @version 1.0
 */
@Service
public class VmsFeedImportResultService extends BaseViewService {

    @Autowired
    private FeedFileService feedFileService;

    /**
     * 取得检索条件信息
     *
     * @param param 客户端参数
     * @return 检索条件信息
     */
    public Map<String, Object> init (Map<String, Object> param) {

        Map<String, Object> result = new HashMap<>();

        // 对客户只展示2,3,4三个状态
        List<TypeBean> typeBeans = Types.getTypeList(IMPORT_FEED_FILE_STATUS, (String)param.get("lang")).stream()
                .filter(typeBean -> "2".equals(typeBean.getValue()) || "3".equals(typeBean.getValue()) || "4".equals(typeBean.getValue())).collect(Collectors.toList());

        // 状态
        result.put("statusList", typeBeans);

        return result;
    }

    /**
     * 检索
     *
     * @param param 客户端参数
     * @return 检索结果
     */
    public Map<String, Object> search(Map<String, Object> param) {

        Map<String, Object> result = new HashMap<>();
        int curr = (int) param.get("curr");
        int size = (int) param.get("size");

        String uploadDateStart = String.valueOf(param.get("uploadDateStart"));
        if (!StringUtils.isEmpty(uploadDateStart)) {
            param.put("uploadDateStart", new Date(Long.parseLong(uploadDateStart)));
        }

        String uploadDateEnd = String.valueOf(param.get("uploadDateEnd"));
        if (!StringUtils.isEmpty(uploadDateEnd)) {
            param.put("uploadDateEnd", new Date(Long.parseLong(uploadDateEnd)));
        }

        Map<String, Object> newMap = MySqlPageHelper.build(param)
                .page(curr)
                .limit(size)
                .addSort("created", Order.Direction.DESC)
                .toMap();

        // 根据条件取得检索结果
        List<Map<String, Object>> feedImportResultList = feedFileService.getFeedFileList(newMap);
        result.put("total", feedFileService.getFeedFileListCount(param));

        editFeedImportResultList(feedImportResultList, (String)param.get("lang"));
        // 检索结果转换
        result.put("feedImportResultList",  feedImportResultList);

        return result;

    }

    /**
     * 检索结果编辑
     *
     * @param feedImportResultList 检索结果
     * @param lang 语言
     */
    private void editFeedImportResultList(List<Map<String, Object>> feedImportResultList, String lang) {

        for (Map<String, Object> feedImportResult : feedImportResultList) {
            // 对用户展示是状态 1=2，5=4
            String status = (String)feedImportResult.get("status");
            if ("1".equals(status)) {
                status = "2";
            }
            if ("5".equals(status)) {
                status = "4";
            }

            // Status
            feedImportResult.put("statusName", Types.getTypeName(IMPORT_FEED_FILE_STATUS, lang, status));
        }
    }
}