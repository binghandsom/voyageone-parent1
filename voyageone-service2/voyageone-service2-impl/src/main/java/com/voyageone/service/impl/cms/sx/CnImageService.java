package com.voyageone.service.impl.cms.sx;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.components.cn.service.CnUploadImageService;
import com.voyageone.service.dao.cms.CmsBtSxCnImagesDao;
import com.voyageone.service.daoext.cms.CmsBtSxCnImagesDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtSxCnImagesModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 独立域名图片相关Service
 *
 * @author morse on 2016/9/26
 * @version 2.6.0
 */
@Service
public class CnImageService extends BaseService {

    @Autowired
    private CmsBtSxCnImagesDao cmsBtSxCnImagesDao;
    @Autowired
    private CmsBtSxCnImagesDaoExt cmsBtSxCnImagesDaoExt;
    @Autowired
    private CnUploadImageService cnUploadImageService;

    /**
     * 检索等待上传的图片
     */
    public List<CmsBtSxCnImagesModel> selectListWaitingUpload(String channelId, int cartId, int limit) {
        return cmsBtSxCnImagesDaoExt.selectListWaitingUpload(channelId, cartId, limit);
    }

    /**
     * 检索此code下正在使用的图片的数据
     */
    public List<CmsBtSxCnImagesModel> selectListByCodeWithUsing(String channelId, int cartId, String code, String urlKey) {
        return cmsBtSxCnImagesDaoExt.selectListByCodeWithUsing(channelId, cartId, code, urlKey);
    }

    /**
     * 更新状态
     */
    public int updateStatus(CmsBtSxCnImagesModel model, String status, String modifier) {
        model.setStatus(status);
        model.setModifier(modifier);
        model.setModified(DateTimeUtil.getDate());
        return cmsBtSxCnImagesDao.update(model);
    }

    /**
     * 更新code下图片的status为0:等待上传,相同图片传过了就不更新成0了,没有数据就插入
     */
    public void updateImageInfo(String channelId, int cartId, String code, String urlKey, List<String> images, String modifier) {
        List<CmsBtSxCnImagesModel> listDBData = selectListByCodeWithUsing(channelId, cartId, code, urlKey);
        List<Integer> listOriIndex = new ArrayList<>(); // 原来上过的index

        for (CmsBtSxCnImagesModel model : listDBData) {
            String oriImageName = model.getImageName();
            int oriIndex = model.getIndex();

            if (!images.get(oriIndex - 1).equals(oriImageName)) {
                // 原来的图和现在的图不一致，则需要重新上传
                // 原数据status更新成3
                updateStatus(model, "3", modifier);
            } else {
                // 上过的index且不需要重新传的，记录一下
                listOriIndex.add(oriIndex);
            }
        }

        List<CmsBtSxCnImagesModel> listInsertData = new ArrayList<>();
        int imageCnt = images.size() > 5 ? 5 : images.size(); // 最多5张图
        for (int i = 1; i <= imageCnt; i++) {
            // 目前只上传5张商品主图
            if (!listOriIndex.contains(i)) {
                // 不是原来上过的图,即这次需要上的图(包括重新需要上传的图)
                CmsBtSxCnImagesModel insertData = new CmsBtSxCnImagesModel();
                insertData.setChannelId(channelId);
                insertData.setCartId(cartId);
                insertData.setCode(code);
                insertData.setUrlKey(urlKey);
                insertData.setImageName(images.get(i-1));
                insertData.setIndex(i);
                insertData.setCreater(modifier);
                listInsertData.add(insertData);
            }
        }

        if (!listInsertData.isEmpty()) {
            cmsBtSxCnImagesDaoExt.insertByList(listInsertData);
        }
    }

    /**
     * 上传图片
     *
     * @param url 图片url
     * @param strOssFilePath OSS存放路径
     */
    public void doUploadImage(String url, String strOssFilePath) {
        byte[] bytes;
        try {
            bytes = cnUploadImageService.downloadImage(url);
        } catch (IOException e) {
            String errMsg = String.format("独立域名图片取得失败![%s]", url);
            $warn(errMsg);
            throw new BusinessException(errMsg);
        } catch (Exception ex) {
            throw ex;
        }
        $info("独立域名读取图片成功![%s]", url);


        try {
            cnUploadImageService.uploadImage(bytes, strOssFilePath);
        } catch (IOException e) {
            String errMsg = String.format("独立域名图片上传失败![%s]", url);
            $warn(errMsg);
            throw new BusinessException(errMsg);
        } catch (Exception ex) {
            throw ex;
        }

        $info("独立域名上传图片成功![%s]", url);

    }
}
