package com.voyageone.web2.openapi.bi.control;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.web2.openapi.OpenApiBaseController;
import com.voyageone.web2.openapi.bi.constants.BiUrlConstants;
import com.voyageone.web2.openapi.bi.service.DataServiceTB;
import com.voyageone.web2.sdk.api.VoApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;


@RestController
@RequestMapping(
        value = BiUrlConstants.URL.LIST.ROOT,
        method = {RequestMethod.POST}
)
public class BiSaveDataController extends OpenApiBaseController {

    @Autowired
    private DataServiceTB dataServiceTB;

    @RequestMapping(BiUrlConstants.URL.LIST.SAVE_SHOP_URL_DATA)
    public VoApiResponse saveShopData(@RequestBody Map<String,Object> params) {
        $info(BiUrlConstants.URL.LIST.SAVE_SHOP_URL_DATA);

        return simpleResponse(dataServiceTB.saveStoreUrlData(params));
    }

    @RequestMapping(BiUrlConstants.URL.LIST.CHECK_URL_LIST)
    public VoApiResponse getShopList(HttpServletRequest request) throws Exception {
        $info(BiUrlConstants.URL.LIST.CHECK_URL_LIST  + "/" + BiUrlConstants.URL.LIST.CHECK_URL_LIST);
        return simpleResponse("OK");
    }

    @RequestMapping(BiUrlConstants.URL.LIST.SAVE_PRODUCT_FILE_DATA)
    public VoApiResponse saveProductFile(HttpServletRequest request, @RequestParam String shopFileInfo, @RequestPart("file") MultipartFile file) throws UnsupportedEncodingException {
        $info(BiUrlConstants.URL.LIST.SAVE_PRODUCT_FILE_DATA);

        Map<String, Object> shopFileInfoMap = JacksonUtil.jsonToMap(shopFileInfo);

        @SuppressWarnings("unchecked")
        Map<String, Object> shopItem = (Map<String, Object>)shopFileInfoMap.get("shopItem");
        String fileName = (String)shopFileInfoMap.get("fileName");
        fileName = URLDecoder.decode(fileName, "UTF-8");
        return simpleResponse(dataServiceTB.saveProductFileData(shopItem, fileName, file));
    }

}
