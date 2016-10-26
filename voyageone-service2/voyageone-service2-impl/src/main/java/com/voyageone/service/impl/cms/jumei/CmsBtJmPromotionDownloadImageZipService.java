package com.voyageone.service.impl.cms.jumei;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    //图片模板前缀
    private static final String url = "http://s7d5.scene7.com/is/image/sneakerhead/";
    //图片模板后缀
    private static final String suffix = "?fmt=jpg&scl=1&qlt=100";
    //专场飘窗
    private static final String bayWindowPath = "聚美专场图片sample\\专场飘窗\\";
    //专场分隔栏
    private static final String bayTagPath = "聚美专场图片sample\\专场分隔栏\\";

    /**
     * 下载专场图片包
     *
     * @param promotionId
     * @return outputStream
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
                    if (picNameModel.getUseTemplate()) {
                        //imageUrl
                        String url = "";
                        try {
                            url = cmsBtJmImageTemplateService.getUrl(imageName, imageType, cmsBtJmPromotionSaveBean);
                            if(url!=null){
                                urlMap.put("url", url);
                                urlMap.put("picturePath", imagePath);
                                promotionImagesList.add(urlMap);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(imageType);
                        }
                    } else {
                        urlMap.put("url", url + imageName + suffix);
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
                .map(cmsBtTagModel -> tagService.getJmModule(cmsBtTagModel))
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
        return imageToZip(promotionImagesList);
    }

    /**
     * 下载商品主图包
     *
     * @param promotionId
     * @return 返回压缩流
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
                urlMap.put("url", url + model.getImage1());
                urlMap.put("picturePath", model.getProductCode());
                promotionImagesList.add(urlMap);
            }
        }
        //返回压缩包流
        return imageToZip(promotionImagesList);
    }

    /**
     * 压缩图片
     *
     * @param promotionImagesList
     * @return ZipOutputStream
     */
    public byte[] imageToZip(List<Map<String, String>> promotionImagesList) {
        //如果promotionImagesList为空的时，不做处理
        if (promotionImagesList.size() > 0 || promotionImagesList != null) {
            byte[] buffer = new byte[1024];
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);) {
                for (Map<String, String> urlMap : promotionImagesList) {
                    int len;
                    URL url = new URL(urlMap.get("url"));
                    $info(urlMap.get("url"));
                    //Url
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    try (InputStream inputStream = conn.getInputStream()) {
                        //压缩包内生成图片的路径以及名称
                        zipOutputStream.putNextEntry(new ZipEntry(urlMap.get("picturePath") + ".jpg"));
                        //读入需要下载的文件的内容，打包到zip文件
                        while ((len = inputStream.read(buffer)) > 0) {
                            zipOutputStream.write(buffer, 0, len);
                        }
                        inputStream.close();
                        zipOutputStream.closeEntry();

                    } catch (Exception e) {

                    }
                }
                zipOutputStream.close();
                return byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
