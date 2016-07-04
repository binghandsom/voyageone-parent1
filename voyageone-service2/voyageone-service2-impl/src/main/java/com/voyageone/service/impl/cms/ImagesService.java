package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.util.ImgUtils;
import com.voyageone.service.dao.cms.CmsBtImagesDao;
import com.voyageone.service.daoext.cms.CmsBtImagesDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtImagesModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Edward
 * @version 2.0.0, 16/5/10
 */
@Service
public class ImagesService extends BaseService {

    @Autowired
    private CmsBtImagesDao cmsBtImagesDao;
    @Autowired
    private CmsBtImagesDaoExt cmsBtImagesDaoExt;

    /**
     * 根据channelId和code返回对应的所有图片列表
     */
    public List<CmsBtImagesModel> selectImagesByCode(String channelId, String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", channelId);
        map.put("code", code);
        return cmsBtImagesDao.selectList(map);
    }

    /**
     * 根据channelId和originalUrl返回对应的图片
     */
    public CmsBtImagesModel selectImagesByOriginalUrl(String channelId, String code, String originalUrl) {
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", channelId);
        map.put("code", code);
        map.put("originalUrl", originalUrl);
        return cmsBtImagesDao.selectOne(map);
    }

    /**
     * 插入新图片
     */
    public int insert(CmsBtImagesModel record) {
        return cmsBtImagesDao.insert(record);
    }

    /**
     * 更新图片
     */
    public int update(CmsBtImagesModel record) {
        return cmsBtImagesDao.update(record);
    }

    /**
     * 根据code或者originalUrl取得该图片是否已经存在
     */
    public CmsBtImagesModel getImageIsExists(String channelId, String code, String originalUrl) {

        CmsChannelConfigBean imageCompareRule = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.IMAGE_COMPARE_RULE);

        if (imageCompareRule == null)
            throw new BusinessException(channelId + "该店铺对应的PRICE_RETAIL_CALC_FORMULA在cms_mt_channel_config表中不存在");

        if (CmsConstants.IMAGE_COMPARE_RULE.ORIGINAL_IAMGE_NAME.equals(imageCompareRule.getConfigValue1())) {
            String imageName = ImgUtils.getImageName(originalUrl);//originalUrl.substring(originalUrl.lastIndexOf("/") + 1).substring(0, originalUrl.substring(originalUrl.lastIndexOf("/") + 1).indexOf("?"));


            List<CmsBtImagesModel> findImages = this.selectImagesByCode(channelId, code);

            findImages = findImages.stream().filter(image ->
                    ImgUtils.getImageName(image.getOriginalUrl()).equals(imageName))
                    .collect(Collectors.toList());

            if (!findImages.isEmpty()) {
                return findImages.get(0);
            }
        } else if (CmsConstants.IMAGE_COMPARE_RULE.ORIGINAL_URL.equals(imageCompareRule.getConfigValue1())) {
            return this.selectImagesByOriginalUrl(channelId, code, originalUrl);
        }

        return null;
    }

    /**
     * getImagesByCode
     */
    public List<Map> getImagesByCode(String channelId, List<String> prodCodeList) {
        return cmsBtImagesDaoExt.selectImagesByCode(channelId, prodCodeList);
    }
}
