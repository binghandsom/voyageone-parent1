package com.voyageone.service.impl.cms.jumei;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.ImageServer;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.jumei.CmsBtJmPromotionSaveBean;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtJmPromotionImagesDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CmsBtJmBayWindowService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.CmsBtTagJmModuleExtensionModel;
import com.voyageone.service.model.cms.mongo.CmsBtJmImageTemplateModel;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsBtJmBayWindowModel;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsBtJmPromotionImagesModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.util.stream.Collectors.toList;

/**
 * Created by gjl on 2016/10/18.
 */
@Service
public class CmsBtJmPromotionDownloadImageZipService extends BaseService {
    @Autowired
    CmsBtJmPromotionImagesDao cmsBtJmPromotionImagesDao;
    @Autowired
    CmsBtJmPromotionProductDao cmsBtJmPromotionProductDao;
    @Autowired
    CmsBtJmImageTemplateService cmsBtJmImageTemplateService;
    @Autowired
    private CmsBtJmPromotionService cmsBtJmPromotionService;
    @Autowired
    private CmsBtJmBayWindowService CmsBtJmBayWindowService;
    @Autowired
    private TagService tagService;

    //图片模板后缀
    private static final String suffix = "?fmt=jpg&scl=1&qlt=100";
    //专场飘窗
    private static final String bayWindowPath = "聚美专场图片sample\\专场飘窗\\";
    //专场分隔栏
    private static final String bayTagPath = "聚美专场图片sample\\专场分隔栏\\";

    /**
     * 下载专场图片包
     */
    public byte[] selectSpecialImagesList(Integer promotionId) {
        //压缩图片的所需要的对象
        List<Map<String, String>> promotionImagesList = new ArrayList<>();
        //根据promotionId在数据库中取得对应的Url-----专场入口图
        CmsBtJmPromotionSaveBean cmsBtJmPromotionSaveBean = cmsBtJmPromotionService.getEditModel(promotionId, true);
        //取得打包图片的名称
        CmsBtJmPromotionImagesModel picNameModel = cmsBtJmPromotionImagesDao.selectPromotionImagesList(promotionId);
        //取得打包图片的名称转换成对象
        Map<String, Object> imageNameMap = JacksonUtil.jsonToMap(JacksonUtil.bean2Json(picNameModel));

        final String channelId = cmsBtJmPromotionSaveBean.getModel().getChannelId();

        imageNameMap.forEach((s, o) -> {
            if (o instanceof String) {
                //取得打包图片的信息(图片类型及图片路径)
                CmsBtJmImageTemplateModel cmsBtJmImageTemplateModel = cmsBtJmImageTemplateService.getJMImageTemplateByType(s);
                if (cmsBtJmImageTemplateModel != null) {
                    //imageName
                    String imageName = String.valueOf(o);
                    //imageType
                    String imageType = cmsBtJmImageTemplateModel.getImageType();
                    //imagePath
                    String imagePath = cmsBtJmImageTemplateModel.getName();
                    //压缩图片的所需要的对象
                    Map<String, String> urlMap = new HashMap<>();
                    if (picNameModel.getUseTemplate() != null && picNameModel.getUseTemplate()) {
                        //imageUrl
                        String url;
                        try {
                            url = cmsBtJmImageTemplateService.getUrl(imageName, imageType, cmsBtJmPromotionSaveBean);
                            if (url != null) {
                                urlMap.put("url", url);
                                urlMap.put("picturePath", imagePath);
                                $info("geturl:" + url);
                                promotionImagesList.add(urlMap);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(imageType);
                        }
                    } else {
                        urlMap.put("url", ImageServer.imageUrl(channelId, imageName + suffix));
                        urlMap.put("picturePath", imagePath);
                        promotionImagesList.add(urlMap);
                    }
                }
            }
        });
        //根据promotionId在数据库中取得对应的Url-----专场分隔栏图
        List<String> moduleTitleList = cmsBtJmPromotionSaveBean
                .getTagList()
                .stream()
                .map(tag -> tagService.getJmModule(tag.getModel()))
                .map(CmsBtTagJmModuleExtensionModel::getModuleTitle)
                .collect(toList());
        if (moduleTitleList.size() > 0) {
            for (String moduleName : moduleTitleList) {
                Map<String, String> urlMap = new HashMap<>();
                String url = cmsBtJmImageTemplateService.getSeparatorBar(moduleName);
                urlMap.put("url", url);
                urlMap.put("picturePath", bayTagPath + moduleName);
                promotionImagesList.add(urlMap);
            }
        }
        //根据promotionId在数据库中取得对应的Url-----专场飘窗图
        CmsBtJmBayWindowModel cmsBtJmBayWindowModel = CmsBtJmBayWindowService.getBayWindowByJmPromotionId(promotionId);
        if (cmsBtJmBayWindowModel != null) {
            for (CmsBtJmBayWindowModel.BayWindow model : cmsBtJmBayWindowModel.getBayWindows()) {
                Map<String, String> urlMap = new HashMap<>();
                urlMap.put("url", model.getUrl());
                urlMap.put("picturePath", bayWindowPath + model.getName());
                promotionImagesList.add(urlMap);
            }
        }
        //返回压缩流
        return imageToZipThread(promotionImagesList, channelId);
    }

    /**
     * 下载商品主图包
     */
    public byte[] selectWaresImageList(Integer promotionId) {
        //压缩图片的所需要的对象
        List<Map<String, String>> promotionImagesList = new ArrayList<>();
        //抽取图片数据的条件
        Map<String, Object> params = new HashMap<>();
        //promotionId
        params.put("cmsBtJmPromotionId", promotionId);
        //取得打包图片的名称和url
        List<CmsBtJmPromotionProductModel> modelList = cmsBtJmPromotionProductDao.selectList(params);
        if (modelList.size() > 0) {
            for (CmsBtJmPromotionProductModel model : modelList) {
                //压缩图片的所需要的对象
                Map<String, String> urlMap = new HashMap<>();
                urlMap.put("url", ImageServer.imageUrl(model.getChannelId(), model.getImage1() + "?wid=2000&hei=2000"));
                urlMap.put("picturePath", model.getProductCode());
                promotionImagesList.add(urlMap);
            }
        }
        //返回压缩包流
        return imageToZip(promotionImagesList);
    }

    /**
     * 压缩图片
     */
    private byte[] imageToZip(List<Map<String, String>> promotionImagesList) {

        if (CollectionUtils.isEmpty(promotionImagesList)) {
            return null;
        }

        byte[] buffer = new byte[1024 * 10];

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            for (Map<String, String> urlMap : promotionImagesList) {
                int len;
                URL url = new URL(urlMap.get("url"));
                $info(urlMap.get("url"));
                //Url
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                try (InputStream inputStream = conn.getInputStream()) {
                    //压缩包内生成图片的路径以及名称
                    zipOutputStream.putNextEntry(new ZipEntry(urlMap.get("picturePath") + ".jpg"));
                    $info(urlMap.get("picturePath") + ".jpg");
                    //读入需要下载的文件的内容，打包到zip文件
                    while ((len = inputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, len);
                        $info(len + "");
                    }
                    $info("finish");
                    inputStream.close();
                    zipOutputStream.closeEntry();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            zipOutputStream.close();
            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            throw new FailedZipException(e);
        }
    }

    private byte[] imageToZipThread(List<Map<String, String>> promotionImagesList, String channelId) {
        //如果promotionImagesList为空的时，不做处理
        List<Map<String, Object>> imageBytes = Collections.synchronizedList(new ArrayList<>());

        if (CollectionUtils.isEmpty(promotionImagesList)) {
            return null;
        }

        try {
            ExecutorService es = Executors.newFixedThreadPool(5);

            promotionImagesList.forEach(stringStringMap ->
                    es.execute(() -> downImageThread(stringStringMap, imageBytes, channelId)));

            es.shutdown();
            es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            for (Map<String, Object> imageByte : imageBytes) {
                if (imageByte.get("byte") != null) {
                    //压缩包内生成图片的路径以及名称
                    zipOutputStream.putNextEntry(new ZipEntry(imageByte.get("picturePath") + ".jpg"));
                    //读入需要下载的文件的内容，打包到zip文件
                    byte[] imageTemp = (byte[]) imageByte.get("byte");
                    zipOutputStream.write(imageTemp, 0, imageTemp.length);
                    zipOutputStream.closeEntry();
                }
            }

            zipOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new FailedZipException(e);
        }
    }

    private void downImageThread(Map<String, String> promotionImage, List<Map<String, Object>> imageBytes,
                                 String channelId) {
        Map<String, Object> imageByte = new HashMap<>();
        imageByte.put("picturePath", promotionImage.get("picturePath"));
        imageByte.put("byte", downImage(promotionImage.get("url"), channelId));
        imageBytes.add(imageByte);
    }

    byte[] downImage(String imageUrl, String channelId) {
        long threadNo = Thread.currentThread().getId();
        $info("threadNo:" + threadNo + " url:" + imageUrl + "下载开始");
        try (InputStream inputStream = ImageServer.proxyDownloadImage(imageUrl, channelId)) {
            $info("threadNo:" + threadNo + " url:" + imageUrl + "下载结束");
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new FailedDownloadException(e);
        }
    }

    public static class FailedZipException extends BusinessException {
        FailedZipException(Throwable cause) {
            super("打包聚美图片失败", cause);
        }
    }

    public static class FailedDownloadException extends BusinessException {
        FailedDownloadException(Throwable cause) {
            super("聚美图片打包下载失败", cause);
        }
    }
}
