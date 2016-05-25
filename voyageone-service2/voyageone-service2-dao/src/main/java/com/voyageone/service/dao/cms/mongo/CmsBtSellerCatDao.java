package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.JomgoUpdate;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CmsBtSellerCatDao extends BaseMongoDao<CmsBtSellerCatModel> {

    /**
     * 取得 根据ChannelId, CartId
     */
    public List<CmsBtSellerCatModel> selectByChannelCart(String channelId, int cartId) {

        String queryStr = "{\"channelId\":\"" + channelId + "\"" + ",\"cartId\"" + ":" + cartId + "}";

        List<CmsBtSellerCatModel> result = select(queryStr);

        return result;
    }


    public void update(String channelId, int cartId, String cName, String cId) {
        String queryStr = "{'channelId':'" + channelId + "','cartId':" + cartId + "}";

        List<CmsBtSellerCatModel> allCat = select(queryStr);
        List<CmsBtSellerCatModel> resultList =  findCId(allCat, cId);
        if(resultList.size() > 0)
        {
            CmsBtSellerCatModel result = resultList.get(0);
            String oldCName = result.getCatName();
            result.setCatName(cName);
            String oldPath = result.getCatPath();

            String[] paths = oldPath.split("->");
            if( paths.length > 0)
            {
                paths[paths.length -1] = cName;
            }

            String catPath= "";
            for (String path:paths) {
                catPath = catPath + "->" +path;
            }

            result.setCatPath(oldPath.replace(oldCName, cName));


            //递归更新子节点
            updateChildren(result.getChildren(), result.getCatPath());

            for (CmsBtSellerCatModel model: allCat) {
                update(model);
            }
        }
    }

    private void updateChildren(List<CmsBtSellerCatModel> children, String parentCatPath)
    {
        for (CmsBtSellerCatModel model:children) {
            model.setCatPath(parentCatPath + "->" + model.getCatName());
            updateChildren(model.getChildren(), model.getCatPath());
        }

    }

    private List<CmsBtSellerCatModel> findCId(List<CmsBtSellerCatModel> list, String cId)
    {
        List<CmsBtSellerCatModel> result = new ArrayList<CmsBtSellerCatModel>();
        for (CmsBtSellerCatModel model:list) {
            if (model.getCatId().equals(cId))
            {
                result.add(model);
            }
            else
            {
                result = findCId(model.getChildren() ,cId );
            }

        }
        return  result;
    }


}
