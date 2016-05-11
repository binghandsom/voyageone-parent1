package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MD5;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jumei.bean.JmImageFileBean;
import com.voyageone.components.jumei.enums.JumeiImageType;
import com.voyageone.components.jumei.service.JumeiImageFileService;
import com.voyageone.service.dao.cms.mongo.CmsBtImageGroupDao;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel_Image;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 图片上传到平台（暂时只支持天猫和聚美）
 * @author jeff.duan on 2016/5/10.
 * @version 2.0.0
 */
@Service
public class CmsUploadImageToPlatformService extends BaseTaskService {


    /* 斜杠分隔符 */
    private static final String SLASH = "/";

    @Autowired
    private CmsBtImageGroupDao cmsBtImageGroupDao;

    @Autowired
    private JumeiImageFileService jumeiImageFileService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsUploadImageToPlatformJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {


        ExecutorService es = Executors.newFixedThreadPool(10);
        try {
            // 取得图片上传状态为2：等待上传的对象
            JomgoQuery queryObject = new JomgoQuery();
            // 暂时只支持天猫和聚美
            queryObject.setQuery("{\"image.status\":"
                    + CmsConstants.ImageUploadStatus.WAITING_UPLOAD + ",\"active\":1,\"cartId\":{$in:[" + CartEnums.Cart.TM.getId() + "," + CartEnums.Cart.TB.getId() + "," + CartEnums.Cart.TG.getId() + "," + CartEnums.Cart.JM.getId() + "]}}");
            List<CmsBtImageGroupModel> imageGroupList = cmsBtImageGroupDao.select(queryObject);

            for (CmsBtImageGroupModel imageGroup : imageGroupList) {
                List<CmsBtImageGroupModel_Image> images = imageGroup.getImage();
                for (CmsBtImageGroupModel_Image image : images) {
                    if (CmsConstants.ImageUploadStatus.WAITING_UPLOAD.equals(String.valueOf(image.getStatus()))) {
                        uploadImageToPlatform(imageGroup.getChannelId(), String.valueOf(imageGroup.getCartId()), imageGroup.getImageType(), image);
                        imageGroup.setModifier(getTaskName());
                        imageGroup.setModified(DateTimeUtil.getNowTimeStamp());
                        cmsBtImageGroupDao.update(imageGroup);
                    }
                }
            }
        } catch (Exception ex) {
            $error(ex.getMessage(), ex);
            issueLog.log(ex, ErrorType.BatchJob, SubSystem.CMS);
        }
    }

    private void uploadImageToPlatform(String channelId, String cartId, int imageType, CmsBtImageGroupModel_Image image) {
        if (cartId.equals(CartEnums.Cart.TM.getId()) || cartId.equals(CartEnums.Cart.TB.getId()) ||cartId.equals(CartEnums.Cart.TG.getId())) {

        } else if (cartId.equals(CartEnums.Cart.JM.getId())) {
            uploadImageToJM(channelId, imageType, image);
        }
    }

    private void uploadImageToJM(String channelId, int imageType, CmsBtImageGroupModel_Image image) {
        String originUrl = image.getOriginUrl();
        String platformUrl = image.getPlatformUrl();
        $info("开始上传平台图片---图片原始Url:" + originUrl + " 平台id;27");
        InputStream inputStream = null;
        try {
            URL url = new URL(originUrl);
            HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            inputStream = new BufferedInputStream(httpUrl.getInputStream());
        } catch (Exception e) {
            // 设置返回的错误信息
            image.setErrorMsg("Origin Url is illegal");
            // 设置图片上传状态为上传成功
            image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_FAIL));
        }

        ShopBean shopBean = null;
        try {
            JmImageFileBean jmImageFileBean = new JmImageFileBean();
            if (StringUtils.isEmpty(platformUrl)) {
                // 新建的场合
                String imageName = originUrl.substring(originUrl.lastIndexOf("/") + 1, originUrl.lastIndexOf("."));
                jmImageFileBean.setImgName(imageName);
            } else {
                // 更新的场合
                String imageName = platformUrl.substring(platformUrl.lastIndexOf("/") + 1, platformUrl.lastIndexOf("."));
                jmImageFileBean.setImgName(imageName);
            }
            jmImageFileBean.setDirName(SLASH + channelId + SLASH + imageType);
            jmImageFileBean.setInputStream(inputStream);
            jmImageFileBean.setNeedReplace(true);
            jmImageFileBean.setExtName("jpg");

            shopBean = Shops.getShop(channelId, CartEnums.Cart.JM.getId());
            String platFormUrl = jumeiImageFileService.imageFileUpload(shopBean, jmImageFileBean);
            // 设置平台返回的Url
            image.setPlatformUrl(platFormUrl);
            // 设置图片上传状态为上传成功
            image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_SUCCESS));
            // 清除errorMsg
            image.setErrorMsg(null);
        } catch (Exception e) {
            String errMsg = e.getMessage();
            if (errMsg.indexOf("调用聚美API错误")  > -1 ) {
                if (errMsg.indexOf(shopBean.getApp_url()) > -1) {
                    errMsg = errMsg.substring(0, errMsg.lastIndexOf(shopBean.getApp_url()));
                } else if (errMsg.lastIndexOf("}") > -1) {
                    errMsg = errMsg.substring(0, errMsg.lastIndexOf("}") + 1);
                }
            }
            // 设置返回的错误信息
            image.setErrorMsg(errMsg);
            // 设置图片上传状态为上传成功
            image.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.UPLOAD_FAIL));
        }
    }
}

