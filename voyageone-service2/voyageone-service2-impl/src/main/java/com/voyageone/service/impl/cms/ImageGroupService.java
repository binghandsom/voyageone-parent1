package com.voyageone.service.impl.cms;

import com.jcraft.jsch.ChannelSftp;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.SFtpUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsBtTasksBean;
import com.voyageone.service.bean.cms.task.stock.StockExcelBean;
import com.voyageone.service.bean.cms.task.stock.StockIncrementExcelBean;
import com.voyageone.service.dao.cms.mongo.CmsBtImageGroupDao;
import com.voyageone.service.daoext.cms.*;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel_Image;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.ibatis.type.IntegerTypeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * ImageGroup Service
 *
 * @author jeff.duan 16/6/6
 * @version 2.0.0
 */
@Service
public class ImageGroupService extends BaseService {

    private final String URL_PREFIX = "http://image.voyageone.com.cn/cms";
    private final String DIRECTORY_SIZE_CHART_IMAGE = "/size/";
    private final String DIRECTORY_BRAND_STORY_IMAGE = "/brand/";
    private final String DIRECTORY_SHIPPING_DESCRIPTION_IMAGE = "/shipping/";
    private final String DIRECTORY_STORE_DESCRIPTION_IMAGE = "/store/";

    @Autowired
    private CmsBtImageGroupDao cmsBtImageGroupDao;
    @Autowired
    MongoSequenceService commSequenceMongoService; // DAO: Sequence

    /**
     * 新建ImageGroup信息
     * @param channelId 渠道id
     * @param userName 新建/更新用户名
     * @param cartId 平台id
     * @param imageGroupName 图片组名称
     * @param imageType 图片类型
     * @param viewType 客户端类型
     * @param brandNameList 相关品牌名称列表
     * @param productTypeList 相关产品类型列表
     * @param sizeTypeList 相关尺码列表
     *
     */
    public void save(String channelId, String userName, String cartId, String imageGroupName, String imageType, String viewType,
                     List<String> brandNameList, List<String> productTypeList, List<String> sizeTypeList) {
        CmsBtImageGroupModel model = new CmsBtImageGroupModel();
        model.setChannelId(channelId);
        model.setCreater(userName);
        model.setModifier(userName);
        model.setCartId(Integer.parseInt(cartId));
        model.setImageGroupId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_IMAGE_GROUP_ID));
        model.setImageGroupName(imageGroupName);
        model.setImageType(Integer.parseInt(imageType));
        model.setViewType(Integer.parseInt(viewType));
        // 什么都不选的情况下，要设置成"All"
        if (brandNameList.size() == 0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            model.setBrandName(lst);
        } else {
            model.setBrandName(brandNameList);
        }
        if (productTypeList.size() == 0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            model.setProductType(lst);
        } else {
            model.setProductType(productTypeList);
        }
        if (sizeTypeList.size() == 0) {
            List lst = new ArrayList<String>();
            lst.add("All");
            model.setSizeType(lst);
        } else {
            model.setSizeType(sizeTypeList);
        }
        model.setActive(1);
        cmsBtImageGroupDao.insert(model);
    }

    /**
     * 删除Image项目
     * @param userName 新建/更新用户名
     * @param imageGroupId 图片组id
     * @param originUrl 原始Url
     */
    public void deleteImage(String userName, String imageGroupId, String originUrl) {
        CmsBtImageGroupModel model = getImageGroupModel(imageGroupId);
        if (model != null) {
            List<CmsBtImageGroupModel_Image> images = model.getImage();
            if(images != null) {
                for (CmsBtImageGroupModel_Image image : images) {
                    if (image.getOriginUrl().equals(originUrl)) {
                        images.remove(image);
                        break;
                    }
                }
                model.setModifier(userName);
                model.setModified(DateTimeUtil.getNowTimeStamp());
                cmsBtImageGroupDao.update(model);
            }
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * 更新ImageGroup信息
     * @param userName 新建/更新用户名
     * @param cartId 平台id
     * @param imageGroupName 图片组名称
     * @param imageType 图片类型
     * @param viewType 客户端类型
     * @param brandNameList 相关品牌名称列表
     * @param productTypeList 相关产品类型列表
     * @param sizeTypeList 相关尺码列表
     */
    public void update(String userName, String imageGroupId, String cartId, String imageGroupName, String imageType, String viewType,
                     List<String> brandNameList, List<String> productTypeList, List<String> sizeTypeList) {
        CmsBtImageGroupModel model = getImageGroupModel(imageGroupId);
        if (model != null) {
            model.setModifier(userName);
            model.setModified(DateTimeUtil.getNowTimeStamp());
            model.setCartId(Integer.parseInt(cartId));
            model.setImageGroupName(imageGroupName);
            model.setImageType(Integer.parseInt(imageType));
            model.setViewType(Integer.parseInt(viewType));
            if (brandNameList.size() == 0) {
                List lst = new ArrayList<String>();
                lst.add("All");
                model.setBrandName(lst);
            } else {
                model.setBrandName(brandNameList);
            }
            if (productTypeList.size() == 0) {
                List lst = new ArrayList<String>();
                lst.add("All");
                model.setProductType(lst);
            } else {
                model.setProductType(productTypeList);
            }
            if (sizeTypeList.size() == 0) {
                List lst = new ArrayList<String>();
                lst.add("All");
                model.setSizeType(lst);
            } else {
                model.setSizeType(sizeTypeList);
            }
            cmsBtImageGroupDao.update(model);
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * 逻辑删除ImageGroupInfo
     * @param imageGroupId 图片组id
     * @param userName 新建/更新用户名

     */
    public void logicDelete(String imageGroupId, String userName) {
        CmsBtImageGroupModel model = getImageGroupModel(imageGroupId);
        if (model != null) {
            model.setModifier(userName);
            model.setModified(DateTimeUtil.getNowTimeStamp());
            model.setActive(0);
            cmsBtImageGroupDao.update(model);
        }
    }

    /**
     * 根据检索条件取得ImageGroupInfo
     * @param channelId 渠道id
     * @param platFormChangeList 平台id列表
     * @param imageType 图片类型
     * @param beginModified 更新开始时间
     * @param endModified 更新结束时间
     * @param brandNameList 相关品牌名称列表
     * @param productTypeList 相关产品类型列表
     * @param sizeTypeList 相关尺码列表
     * @param curr 当前页Index
     * @param size 每页件数
     * @return 检索结果
     */
    public List<CmsBtImageGroupModel> getList(String channelId, List<Integer> platFormChangeList, String imageType, String beginModified,
                                              String endModified, List<String> brandNameList, List<String> productTypeList, List<String> sizeTypeList,
                                              int  curr, int size) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(getSearchQuery(channelId, platFormChangeList, imageType, beginModified,
                endModified, brandNameList, productTypeList, sizeTypeList));
        queryObject.setSort("{imageGroupId:-1}");
        queryObject.setLimit(size);
        queryObject.setSkip((curr - 1) * size);
        return cmsBtImageGroupDao.select(queryObject);
    }

    /**
     * 根据检索条件取得ImageGroupInfo件数
     * @param channelId 渠道id
     * @param platFormChangeList 平台id列表
     * @param imageType 图片类型
     * @param beginModified 更新开始时间
     * @param endModified 更新结束时间
     * @param brandNameList 相关品牌名称列表
     * @param productTypeList 相关产品类型列表
     * @param sizeTypeList 相关尺码列表
     * @return 检索结果件数
     */
    public long getCount(String channelId, List<Integer> platFormChangeList, String imageType, String beginModified,
                           String endModified, List<String> brandNameList, List<String> productTypeList, List<String> sizeTypeList) {
        String parameter = getSearchQuery(channelId, platFormChangeList, imageType, beginModified,
                endModified, brandNameList, productTypeList, sizeTypeList);
        return cmsBtImageGroupDao.countByQuery(parameter);
    }

    /**
     * 根据imageGroupId取得ImageGroupInfo
     * @param imageGroupId 图片组id
     */
    public CmsBtImageGroupModel getImageGroupModel(String imageGroupId) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{\"imageGroupId\":" + imageGroupId + ",\"active\":1}");
        return cmsBtImageGroupDao.selectOneWithQuery(queryObject);
    }

    /**
     * 返回页面端的检索条件拼装成mongo使用的条件
     * @param channelId 渠道id
     * @param platFormChangeList 平台id列表
     * @param imageType 图片类型
     * @param beginModified 更新开始时间
     * @param endModified 更新结束时间
     * @param brandNameList 相关品牌名称列表
     * @param productTypeList 相关产品类型列表
     * @param sizeTypeList 相关尺码列表
     */
    private String getSearchQuery(String channelId, List<Integer> platFormChangeList, String imageType, String beginModified,
                                  String endModified, List brandNameList, List productTypeList, List sizeTypeList) {
        StringBuilder result = new StringBuilder();

        // 获取Platform
        if (platFormChangeList.size() > 0) {
            Integer[] platFormArray = platFormChangeList.toArray(new Integer[platFormChangeList.size()]);
            result.append(MongoUtils.splicingValue("cartId", platFormArray));
            result.append(",");
        }

        // Image Type
        if (!StringUtils.isEmpty(imageType)) {
            result.append(MongoUtils.splicingValue("imageType", Integer.parseInt(imageType)));
            result.append(",");
        }

        // Update Time
        if (!StringUtils.isEmpty(beginModified) || !StringUtils.isEmpty(endModified)) {
            result.append("\"modified\":{" );
            // 获取Update Time Start
            if (!StringUtils.isEmpty(beginModified)) {
                result.append(MongoUtils.splicingValue("$gte", beginModified + " 00.00.00"));
            }
            // 获取Update Time End
            if (!StringUtils.isEmpty(endModified)) {
                if (!StringUtils.isEmpty(beginModified)) {
                    result.append(",");
                }
                result.append(MongoUtils.splicingValue("$lte", endModified + " 23.59.59"));
            }
            result.append("},");
        }

        // brandName
        if (brandNameList.size() > 0) {
            // 带上"All"
            brandNameList.add("All");
            result.append(MongoUtils.splicingValue("brandName", brandNameList.toArray(new String[brandNameList.size()])));
            result.append(",");
        }

        // productType
        if (productTypeList.size() > 0) {
            // 带上"All"
            productTypeList.add("All");
            result.append(MongoUtils.splicingValue("productType", productTypeList.toArray(new String[productTypeList.size()])));
            result.append(",");
        }

        // sizeType
        if (sizeTypeList.size() > 0) {
            // 带上"All"
            sizeTypeList.add("All");
            result.append(MongoUtils.splicingValue("sizeType", sizeTypeList.toArray(new String[sizeTypeList.size()])));
            result.append(",");
        }

        // channelId
        result.append(MongoUtils.splicingValue("channelId", channelId));
        result.append(",");

        // active
        result.append(MongoUtils.splicingValue("active", 1));

        return "{" + result.toString() + "}";
    }


    /**
     * 新建一个Image插入到cms_bt_image_group表
     * @param userName 新建/更新用户名
     * @param imageGroupId 图片组id
     * @param uploadUrl 上传到图片空间的Url
     */
    public void addImage(String userName, String imageGroupId, String uploadUrl) {
        CmsBtImageGroupModel model = getImageGroupModel(imageGroupId);
        if (model != null) {
            CmsBtImageGroupModel_Image imageModel = new CmsBtImageGroupModel_Image();
            imageModel.setOriginUrl(uploadUrl);
            imageModel.setStatus(Integer.parseInt(CmsConstants.ImageUploadStatus.NOT_UPLOAD));
            List<CmsBtImageGroupModel_Image> images = model.getImage();
            if (images == null) {
                images = new ArrayList<>();
            }
            images.add(imageModel);
            model.setImage(images);
            model.setModifier(userName);
            model.setModified(DateTimeUtil.getNowTimeStamp());

            cmsBtImageGroupDao.update(model);
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * 新建一个Image插入到cms_bt_image_group表
     * @param userName 新建/更新用户名
     * @param imageGroupId 图片组id
     * @param uploadUrl 上传到图片空间的Url
     */
    public void updateImage(String userName, String imageGroupId, String key,String uploadUrl) {
        CmsBtImageGroupModel model = getImageGroupModel(imageGroupId);
        if (model != null) {
            List<CmsBtImageGroupModel_Image> images = model.getImage();
            if (images != null && images.size() > 0) {
                for (CmsBtImageGroupModel_Image image : images) {
                    if (image.getOriginUrl().equals(key)) {
                        image.setOriginUrl(uploadUrl);
                        image.setStatus(1);
                        break;
                    }
                }
            }
            model.setModifier(userName);
            model.setModified(DateTimeUtil.getNowTimeStamp());
            cmsBtImageGroupDao.update(model);
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * 移动Image
     * @param userName 新建/更新用户名
     * @param imageGroupId 图片组id
     * @param originUrl 原始Url
     * @param direction 移动方向 up:往上移，move:往下移
     */
    public void move(String userName, String imageGroupId, String originUrl, String direction) {
        CmsBtImageGroupModel model = getImageGroupModel(imageGroupId);
        if (model != null) {
            int index = -1;
            List<CmsBtImageGroupModel_Image> images = model.getImage();
            if (images != null && images.size() > 0) {
                for (int i = 0; i < images.size(); i++) {
                    if (images.get(i).getOriginUrl().equals(originUrl)) {
                        index = i;
                        break;
                    }
                }
            }
            // 找不到或者第一条往上移或者最后一条往下移，那么什么都不操作
            if (index == -1 || (index == 0 && "up".equals(direction)) || ((index == images.size() -1) && "down".equals(direction))) {
                return;
            }

            // 往上移
            if ("up".equals(direction)) {
                Collections.swap(images, index, index - 1);
            } else {
                //往下移
                Collections.swap(images, index, index + 1);
            }

            model.setModifier(userName);
            model.setModified(DateTimeUtil.getNowTimeStamp());
            cmsBtImageGroupDao.update(model);
        }
    }

    /**
     * 重刷Image
     * @param userName 新建/更新用户名
     * @param imageGroupId 图片组id
     * @param originUrl 原始Url
     */
    public void refresh(String userName, String imageGroupId, String originUrl) {
        CmsBtImageGroupModel model = getImageGroupModel(imageGroupId);
        if (model != null) {
            List<CmsBtImageGroupModel_Image> images = model.getImage();
            if (images != null && images.size() > 0) {
                for (CmsBtImageGroupModel_Image image : images) {
                    if (image.getOriginUrl().equals(originUrl)) {
                        image.setStatus(2);
                        break;
                    }
                }
            }
            model.setModifier(userName);
            model.setModified(DateTimeUtil.getNowTimeStamp());
            cmsBtImageGroupDao.update(model);
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * 文件上传到FTP
     * @param channelId 渠道id
     * @param imageType 图片类型
     * @param suffix 图片文件名后缀
     * @param inputStream 图片流
     */
    public String uploadFile(String channelId, String imageType, String suffix, InputStream inputStream) {

        FtpBean ftpBean = formatFtpBean();
        ftpBean.setUpload_filename(DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_2) + "." + suffix);
        if (CmsConstants.ImageType.SIZE_CHART_IMAGE.equals(imageType)) {
            ftpBean.setUpload_path(DIRECTORY_SIZE_CHART_IMAGE + channelId);
        } else if (CmsConstants.ImageType.BRAND_STORY_IMAGE.equals(imageType)) {
            ftpBean.setUpload_path(DIRECTORY_BRAND_STORY_IMAGE  + channelId);
        } else if (CmsConstants.ImageType.SHIPPING_DESCRIPTION_IMAGE.equals(imageType)) {
            ftpBean.setUpload_path(DIRECTORY_SHIPPING_DESCRIPTION_IMAGE + channelId);
        } else if (CmsConstants.ImageType.STORE_DESCRIPTION_IMAGE.equals(imageType)) {
            ftpBean.setUpload_path(DIRECTORY_STORE_DESCRIPTION_IMAGE + channelId);
        }

        ftpBean.setUpload_input(inputStream);

        try {
            SFtpUtil ftpUtil = new SFtpUtil();
            //建立连接
            ChannelSftp ftpClient = ftpUtil.linkFtp(ftpBean);
            boolean isSuccess = ftpUtil.uploadFile(ftpBean, ftpClient);
            if (!isSuccess) {
                throw new BusinessException("upload error");
            }
        } catch (Exception e) {
            throw new BusinessException("upload error");
        }
         return URL_PREFIX + ftpBean.getUpload_path() + "/" + ftpBean.getUpload_filename();
    }

    private FtpBean formatFtpBean(){
        String url = "image.voyageone.com.cn";
        // ftp连接port
        String port = "22";
        // ftp连接usernmae
        String username = "voyageone-cms-sftp";
        // ftp连接password
        String password = "Li48I-22aBz";

        FtpBean ftpBean = new FtpBean();
        ftpBean.setPort(port);
        ftpBean.setUrl(url);
        ftpBean.setUsername(username);
        ftpBean.setPassword(password);
        ftpBean.setFile_coding("iso-8859-1");
        return ftpBean;
    }
}
