package com.voyageone.common.components.jumei;

import com.voyageone.common.components.jumei.Bean.JmCategoryBean;
import com.voyageone.common.components.jumei.Bean.JmImageFileBean;
import com.voyageone.common.components.jumei.base.JmBase;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chuanyu.laing on 2016/1/25.
 */
public class JumeiImageFileService extends JmBase {

    private static List<JmCategoryBean> categoryListLevel4 = null;

    private static String IMAGWE_UPLOAD = "/v1/img/upload";

    /**
     * 图片上传至聚美图片空间
     */
    public void imageFileUpload(ShopBean shopBean, JmImageFileBean fileBean) throws Exception {
        if (fileBean == null) {
            throw new Exception("fileBean not found!");
        }

        fileBean.check();

        Map<String, String> params = new HashMap<>();
        params.put("imgName", fileBean.getImgName());
        params.put("dirName", fileBean.getImgName());
        params.put("needReplace", fileBean.getNeedReplace().toString());

        params.put("imgData", fileBean.getBase64Content());

        reqJmApi(shopBean, IMAGWE_UPLOAD);
    }




}
