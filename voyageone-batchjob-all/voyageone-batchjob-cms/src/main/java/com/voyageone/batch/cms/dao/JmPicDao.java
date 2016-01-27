package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.bean.JmPicBean;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/1/25.
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class JmPicDao extends BaseDao{

    /**
     * 获取聚美imageKey集合
     * @return list
     */
    public List<Map<String,Object>> getJmPicImageKeyGroup(){
        List<Map<String,Object>> ret = selectList(Constants.DAO_NAME_SPACE_CMS + "cms_work_jm_bt_images_getJmPic_ImageKeyGroup");
        if(ret == null){
            ret = new ArrayList<>();
        }
        return ret;
    }

    /**
     * 获取imageKey相同的数据集
     * @param imageKey imageKey
     * @return list
     */
    public List<JmPicBean> getJmPicsByImgKey(String imageKey) {
        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_work_jm_bt_images_getJmPics_ByImgKey", imageKey);
    }

    /**
     * 修改聚美图片上传状态
     * @param seq seq
     * @return effect count
     */
    public int updateJmpicUploaded(String jmUrl,Integer seq,String modifier){
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("seq",seq);
        params.put("jmUrl",jmUrl);
        params.put("modifier",modifier);
        return update(Constants.DAO_NAME_SPACE_CMS + "cms_work_jm_bt_images_updateJmPic_uploaded", params);
    }

    /**
     * 修改产品导入状态
     * @param imageKey 参数
     * @return effect count
     */
    public int updateJmProductImportUploaded(String imageKey,String modifier){
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("imageKey",imageKey);
        params.put("modifier",modifier);
        return update(Constants.DAO_NAME_SPACE_CMS + "cms_work_jm_bt_product_import_updateJmProductImport_uploaded", params);
    }
}