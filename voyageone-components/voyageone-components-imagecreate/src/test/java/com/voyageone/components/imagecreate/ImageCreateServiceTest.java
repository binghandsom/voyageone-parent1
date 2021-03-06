package com.voyageone.components.imagecreate;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.imagecreate.bean.*;
import com.voyageone.components.imagecreate.service.ImageCreateService;
import com.voyageone.service.bean.openapi.image.CreateImageParameter;
import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ImageCreateServiceTest
 *
 * @author chuanyu.liang, 12/5/16.
 * @version 2.0.1
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class ImageCreateServiceTest {

    @Autowired
    private ImageCreateService imageCreateService;

    @Test
    public void testGet() throws Exception {
        ImageCreateGetRequest request = new ImageCreateGetRequest();
        request.setChannelId("001");
        request.setTemplateId(15L);
        request.setFile("testaacc-111");
        request.setVParam(new String[]{"file:bcbg/bcbg-sku.png", "http://mce042-fs.nexcess.net:81/voyageone_image/001/under-armour-fire-shot-1269276669-5-2.png", "Text String to be rendered"});
        ImageCreateGetResponse response = imageCreateService.getImage(request);
        System.out.println(JacksonUtil.bean2Json(response));
    }

    @Test
    public void testGetImageWithOutputStream() throws Exception {
        ImageCreateGetRequest request = new ImageCreateGetRequest();
        request.setChannelId("001");
        request.setTemplateId(15);
        request.setFile("testaacc-111");
        // 同时加载图片stream
        request.setFillStream(true);
        request.setVParam(new String[]{"file:bcbg/bcbg-sku.png", "http://mce042-fs.nexcess.net:81/voyageone_image/001/under-armour-fire-shot-1269276669-5-2.png", "Text String to be rendered"});
        ImageCreateGetResponse response = imageCreateService.getImage(request);
        System.out.println(JacksonUtil.bean2Json(response));

        InputStream is = response.getImageInputStream();
        File file = new File("d:/testaacc-111.jpg");
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream os = new FileOutputStream(file);

        int len;
        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        is.close();
        os.close();
    }


    @Test
    public void testAddList() throws Exception {

        List<CreateImageParameter> datas = new ArrayList<>();
        CreateImageParameter createImageParameter = new CreateImageParameter();
        createImageParameter.setChannelId("001");
        createImageParameter.setTemplateId(15L);
        createImageParameter.setFile("testbbb-112");
        createImageParameter.setVParam(new String[]{"file:bcbg/bcbg-sku.png", "http://mce042-fs.nexcess.net:81/001/under-armour-fire-shot-1269276669-5-2.png", "Text String to be rendered"});
        datas.add(createImageParameter);

        createImageParameter = new CreateImageParameter();
        createImageParameter.setChannelId("001");
        createImageParameter.setTemplateId(15L);
        createImageParameter.setFile("testbbb-113");
        createImageParameter.setVParam(new String[]{"file:bcbg/bcbg-sku.png", "http://mce042-fs.nexcess.net:81/001/under-armour-fire-shot-1269276669-5-2.png", "Text String to be rendered"});
        datas.add(createImageParameter);


        ImageCreateAddListRequest request = new ImageCreateAddListRequest();
        request.setData(datas);

        ImageCreateAddListResponse response = imageCreateService.addList(request);


        System.out.println(JacksonUtil.bean2Json(response));
    }

    @Test
    public void testGetListResult() throws Exception {

        ImageCreateGetListResultRequest request = new ImageCreateGetListResultRequest();
        request.setTaskId(30994);

        ImageCreateGetListResultResponse response = imageCreateService.getListResult(request);

        for (CmsMtImageCreateFileModel model : response.getCmsMtImageCreateFiles()) {
            System.out.println(JacksonUtil.bean2Json(model));
        }
    }
}
