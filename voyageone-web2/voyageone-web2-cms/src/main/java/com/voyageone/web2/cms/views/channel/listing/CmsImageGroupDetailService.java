package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsBtImageGroupBean;
import com.voyageone.service.impl.cms.ImageGroupService;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel_Image;
import com.voyageone.web2.base.BaseAppService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;


/**
 * Created by jeff.duan on 2016/5/5.
 */
@Service
public class CmsImageGroupDetailService extends BaseAppService {

    @Autowired
    private ImageGroupService imageGroupService;

    @Autowired
    MongoSequenceService commSequenceMongoService; // DAO: Sequence

    /**
     * 取得检索条件信息
     *
     * @param param 客户端参数
     * @return 检索条件信息
     */
    public Map<String, Object> init (Map<String, Object> param) {

        Map<String, Object> result = new HashMap<>();

        // 取得当前channel, 有多少个platform(Approve平台)
        result.put("platformList", TypeChannels.getTypeListSkuCarts((String)param.get("channelId"), "A", (String)param.get("lang")));
        // 品牌下拉列表
        result.put("brandNameList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, (String)param.get("channelId"), (String)param.get("lang")));
        // 产品类型下拉列表
        result.put("productTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_57, (String)param.get("channelId"), (String)param.get("lang")));
        // 尺寸类型下拉列表
        result.put("sizeTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_58, (String)param.get("channelId"), (String)param.get("lang")));

        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{\"imageGroupId\":" + param.get("imageGroupId") + "}");
        queryObject.setProjection("{'_id':0}");
        CmsBtImageGroupModel imageGroupInfo = imageGroupService.getOne(queryObject);

        result.put("imageGroupInfo", imageGroupInfo);
        return result;
    }

    /**
     * 检索图片
     *
     * @param param 客户端参数
     * @return 检索结果
     */
    public List<CmsBtImageGroupModel_Image> search(Map<String, Object> param) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{\"imageGroupId\":" + param.get("imageGroupId") + "}");
        CmsBtImageGroupModel imageGroupInfo = imageGroupService.getOne(queryObject);
        editImageModel(imageGroupInfo.getImage(), (String)param.get("lang"));
        return imageGroupInfo.getImage();
    }

    /**
     * 检索结果转换
     *
     * @param images 图片列表
     * @param lang 语言
     */
    private void editImageModel(List<CmsBtImageGroupModel_Image> images, String lang) {
        if (images != null) {
            for (CmsBtImageGroupModel_Image image : images) {
                if ("cn".equals(lang)) {
                    // ImageStatusName
                    if (image.getStatus() == 1) {
                        image.setStatusName("等待上传");
                    } else if (image.getStatus() == 2) {
                        image.setStatusName("上传成功");
                    } else if (image.getStatus() == 3) {
                        image.setStatusName("上传失败");
                    }
                } else {
                    // ImageStatusName
                    if (image.getStatus() == 1) {
                        image.setStatusName("Waiting Upload");
                    } else if (image.getStatus() == 2) {
                        image.setStatusName("Upload Success");
                    } else if (image.getStatus() == 3) {
                        image.setStatusName("Upload Fail");
                    }
                }
            }
        }
    }

    /**
     * 编辑ImageGroup信息
     *
     * @param param 客户端参数
     * @return 检索结果
     */
    public void save(Map<String, Object> param) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{\"imageGroupId\":" + param.get("imageGroupId") + "}");
        CmsBtImageGroupModel model = imageGroupService.getOne(queryObject);
        if (model != null) {
            model.setCartId(Integer.parseInt((String) param.get("platform")));
            model.setImageGroupName((String) param.get("imageGroupName"));
            model.setImageType(Integer.parseInt((String) param.get("imageType")));
            model.setViewType(Integer.parseInt((String) param.get("viewType")));
            if (((List) param.get("brandName")).size() == 0) {
                List lst = new ArrayList<String>();
                lst.add("All");
                model.setBrandName(lst);
            } else {
                model.setBrandName((List) param.get("brandName"));
            }
            if (((List) param.get("productType")).size() == 0) {
                List lst = new ArrayList<String>();
                lst.add("All");
                model.setProductType(lst);
            } else {
                model.setProductType((List) param.get("productType"));
            }
            if (((List) param.get("sizeType")).size() == 0) {
                List lst = new ArrayList<String>();
                lst.add("All");
                model.setSizeType(lst);
            } else {
                model.setSizeType((List) param.get("sizeType"));
            }
            imageGroupService.update(model);
        }
    }

    /**
     * 保存ImageGroup信息
     *
     * @param param 客户端参数
     * @param file 导入文件
     */
    public void saveImage(Map<String, Object> param, MultipartFile file) {
    }

    /**
     * 逻辑删除ImageGroup信息
     *
     * @param param 客户端参数
     */
    public void delete(Map<String, Object> param) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{\"imageGroupId\":" + param.get("imageGroupId") + "}");
        CmsBtImageGroupModel model = imageGroupService.getOne(queryObject);
        if (model != null) {
            List<CmsBtImageGroupModel_Image> images = model.getImage();
            if(images != null) {
                for (CmsBtImageGroupModel_Image image : images) {
                    if (image.getOriginUrl().equals((String)param.get("originUrl"))) {
                        images.remove(image);
                    }
                }
                imageGroupService.update(model);
            }
        }
    }

}