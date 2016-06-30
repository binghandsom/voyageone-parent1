package com.voyageone.service.impl.cms;

import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.translation.TaskSummaryBean;
import com.voyageone.service.bean.cms.translation.TranslationTaskBean;
import com.voyageone.service.bean.cms.translation.TranslationTaskBean_CommonFields;
import com.voyageone.service.bean.cms.translation.TranslationTaskBean_CustomProps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Ethan Shi on 2016/6/28.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TranslationTaskServiceTest {

    @Autowired
    TranslationTaskService translationTaskService;

    @Test
    public void testGetTaskSummary() throws Exception {
        TaskSummaryBean result = translationTaskService.getTaskSummary("010", "salley.yang");
        System.out.println(JsonUtil.bean2Json(result));

    }

    @Test
    public void testGetCurrentTask() throws Exception {
        TranslationTaskBean result  = translationTaskService.getCurrentTask("010", "will");
        System.out.println(JsonUtil.bean2Json(result));

    }

    @Test
    public void testSaveTask() throws Exception {
        TranslationTaskBean result  = translationTaskService.getCurrentTask("010", "will");

        TranslationTaskBean_CommonFields fields = result.getCommonFields();
        fields.setMaterialCn(fields.getMaterialCn() + "__测试");
        fields.setUsageCn(fields.getUsageCn()+ "__测试");
        fields.setLongDesCn(fields.getLongDesCn()+ "__测试");
        fields.setOriginalTitleCn(fields.getOriginalTitleCn()+ "__测试");
        fields.setShortDesCn(fields.getShortDesCn()+ "__测试");

        List<TranslationTaskBean_CustomProps> props = result.getCustomProps();
        props = props.stream().filter(w-> (!StringUtils.isNullOrBlank2(w.getFeedAttrCn()) && !StringUtils.isNullOrBlank2(w.getFeedAttrEn()))).collect(Collectors.toList());

        for (TranslationTaskBean_CustomProps prop: props) {
            prop.setFeedAttrValueCn(prop.getFeedAttrValueCn()+ "__测试");
        }
        result.setCustomProps(props);

        result = translationTaskService.saveTask(result, "010","will", "0");

        System.out.println(JsonUtil.bean2Json(result));
    }
}