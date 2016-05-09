package com.voyageone.components.imagecreate;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.imagecreate.bean.ImageCreateAddListRequest;
import com.voyageone.components.imagecreate.bean.ImageCreateAddListResponse;
import com.voyageone.components.imagecreate.bean.ImageCreateGetRequest;
import com.voyageone.components.imagecreate.bean.ImageCreateGetResponse;
import com.voyageone.components.imagecreate.service.ImageCreateService;
import com.voyageone.service.bean.openapi.image.CreateImageParameter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * ImageCreateServiceTest
 *
 * @author chuanyu.liang, 12/5/16.
 * @version 2.0.1
 * @since 2.0.0
 */
public class ImageCreateServiceTest {

    @Test
    public void testGet() throws Exception {
        ImageCreateService service = new ImageCreateService();

        ImageCreateGetRequest request = new ImageCreateGetRequest();
        request.setChannelId("001");
        request.setTemplateId(15);
        request.setFile("testaacc-111");
        request.setVParam(new String[]{"file:bcbg/bcbg-sku.png", "ftp://images@xpairs.com:voyageone5102@ftp.xpairs.com/001/under-armour-fire-shot-1269276669-5-2.png", "Text String to be rendered"});
        ImageCreateGetResponse response = service.getImage(request);
        System.out.println(JacksonUtil.bean2Json(response));
    }


    @Test
    public void testAddList() throws Exception {
        ImageCreateService service = new ImageCreateService();

        List<CreateImageParameter> datas = new ArrayList<>();
        CreateImageParameter createImageParameter = new CreateImageParameter();
        createImageParameter.setChannelId("001");
        createImageParameter.setTemplateId(15);
        createImageParameter.setFile("testbbb-112");
        createImageParameter.setVParam(new String[]{"file:bcbg/bcbg-sku.png", "ftp://images@xpairs.com:voyageone5102@ftp.xpairs.com/001/under-armour-fire-shot-1269276669-5-2.png", "Text String to be rendered"});
        datas.add(createImageParameter);

        createImageParameter = new CreateImageParameter();
        createImageParameter.setChannelId("001");
        createImageParameter.setTemplateId(15);
        createImageParameter.setFile("testbbb-113");
        createImageParameter.setVParam(new String[]{"file:bcbg/bcbg-sku.png", "ftp://images@xpairs.com:voyageone5102@ftp.xpairs.com/001/under-armour-fire-shot-1269276669-5-2.png", "Text String to be rendered"});
        datas.add(createImageParameter);


        ImageCreateAddListRequest request = new ImageCreateAddListRequest();
        request.setData(datas);

        ImageCreateAddListResponse response = service.addList(request);


        System.out.println(JacksonUtil.bean2Json(response));
    }
}
