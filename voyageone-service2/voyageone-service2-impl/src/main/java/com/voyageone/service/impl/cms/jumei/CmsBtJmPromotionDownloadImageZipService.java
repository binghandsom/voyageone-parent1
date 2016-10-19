package com.voyageone.service.impl.cms.jumei;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.jumei.CmsBtJmPromotionSaveBean;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtJmPromotionImagesDao;
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
public class CmsBtJmPromotionDownloadImageZipService {
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

    /**
     * 下载专场图片包
     *
     * @param promotionId
     * @param strZipName
     * @return outputStream
     */
    public byte[] selectSpecialImagesList(Integer promotionId, String strZipName) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //压缩图片的所需要的对象
        List<Map<String, String>> promotionImagesList = new ArrayList<>();

        /*---------------------专场入口图 Start---------------------*/
        //根据promotionId在数据库中取得对应的Url
        CmsBtJmPromotionSaveBean cmsBtJmPromotionSaveBean = cmsBtJmPromotionService.getEditModel(promotionId, true);
        //取得打包图片的名称
        CmsBtJmPromotionImagesModel picNameModel = cmsBtJmPromotionImagesDao.selectPromotionImagesList(promotionId);
        //取得打包图片的名称转换成对象
        Map<String, Object> imageNameMap = JacksonUtil.jsonToMap(JacksonUtil.bean2Json(picNameModel));
        imageNameMap.forEach((s, o) -> {
            if (o instanceof String) {
                CmsBtJmImageTemplateModel cmsBtJmImageTemplateModel = cmsBtJmImageTemplateService.getJMImageTemplateByType(s);
                if (cmsBtJmImageTemplateModel != null) {
                    //imageName
                    String imageName = String.valueOf(o);
                    //imageType
                    String imageType = cmsBtJmImageTemplateModel.getImageType();
                    //imagePath
                    String imagePath = cmsBtJmImageTemplateModel.getName();
                    //imageUrl
                    String url = cmsBtJmImageTemplateService.getUrl(imageName, imageType, cmsBtJmPromotionSaveBean);
                    //压缩图片的所需要的对象
                    Map<String, String> urlMap = new HashMap<>();
                    urlMap.put("url", url);
                    urlMap.put("picturePath", imagePath);
                    promotionImagesList.add(urlMap);
                }
            }
        });
        /*---------------------专场入口图 end---------------------*/

        /*---------------------专场分隔栏 Start---------------------*/
        List<String> moduleTitleList = cmsBtJmPromotionSaveBean
                .getTagList()
                .stream()
                .map(cmsBtTagModel -> tagService.getJmModule(cmsBtTagModel))
                .map(CmsBtTagJmModuleExtensionModel::getModuleTitle)
                .collect(toList());
        for (String moduleName : moduleTitleList) {
            Map<String, String> urlMap = new HashMap<>();
            String url = cmsBtJmImageTemplateService.getSeparatorBar(moduleName);
            urlMap.put("url", url);
            urlMap.put("picturePath", "聚美专场图片sample\\专场分隔栏\\" + moduleName);
            promotionImagesList.add(urlMap);
        }
        /*---------------------专场分隔栏 End---------------------*/


        /*---------------------专场飘窗 Start---------------------*/
        CmsBtJmBayWindowModel cmsBtJmBayWindowModel = CmsBtJmBayWindowService.getBayWindowByJmPromotionId(promotionId);
        for (CmsBtJmBayWindowModel.BayWindow model : cmsBtJmBayWindowModel.getBayWindows()) {
            Map<String, String> urlMap = new HashMap<>();
            urlMap.put("url", model.getUrl());
            urlMap.put("picturePath", "聚美专场图片sample\\专场飘窗\\" + model.getName());
            promotionImagesList.add(urlMap);
        }
        /*---------------------专场飘窗 End---------------------*/

        //压缩图片的流
        ZipOutputStream zipOutputStream = imageToZip(strZipName, promotionImagesList);
        //返回压缩流
        return outputStream.toByteArray();
    }

    /**
     * 下载商品主图包
     *
     * @param promotionId
     * @param strZipName
     * @return 返回压缩流
     */
    public byte[] selectWaresImageList(Integer promotionId, String strZipName) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //压缩图片的所需要的对象
        List<Map<String, String>> promotionImagesList = new ArrayList<>();
        //抽取图片数据的条件
        Map<String, Object> params = new HashMap<>();
        //promotionId
        params.put("cmsBtJmPromotionId", promotionId);
        //取得打包图片的名称和url
        List<CmsBtJmPromotionProductModel> modelList = cmsBtJmPromotionProductDao.selectList(params);
        for (CmsBtJmPromotionProductModel model : modelList) {
            //压缩图片的所需要的对象
            Map<String, String> urlMap = new HashMap<>();
            urlMap.put("url", url + model.getImage1() + suffix);
            urlMap.put("picturePath", model.getProductCode());
            promotionImagesList.add(urlMap);
        }
        //压缩图片的流
        ZipOutputStream zipOutputStream = imageToZip(strZipName, promotionImagesList);

        //返回压缩包流
        return outputStream.toByteArray();
    }

    /**
     * 压缩图片
     *
     * @param strZipName
     * @param promotionImagesList
     * @return ZipOutputStream
     */
    public ZipOutputStream imageToZip(String strZipName, List<Map<String, String>> promotionImagesList) {
        byte[] buffer = new byte[1024];
        //压缩流
        ZipOutputStream zipOutputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(strZipName));
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(strZipName));
            for (Map<String, String> urlMap : promotionImagesList) {
                try {
                    int len;
                    URL url = new URL(urlMap.get("url"));
                    //Url
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //压缩包内生成图片的路径以及名称
                    out.putNextEntry(new ZipEntry(urlMap.get("picturePath") + ".jpg"));
                    InputStream inputStream = conn.getInputStream();
                    //读入需要下载的文件的内容，打包到zip文件
                    while ((len = inputStream.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                    inputStream.close();
                    out.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipOutputStream;
    }
}
