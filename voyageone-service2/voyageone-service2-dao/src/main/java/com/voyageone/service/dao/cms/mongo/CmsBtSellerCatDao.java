package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public CmsBtSellerCatModel selectByCatId(String channelId, String catId) {

        String queryStr = "{\"channelId\":\"" + channelId + "\"" + ",\"catId\"" + ":\"" + catId + "\"}";

        List<CmsBtSellerCatModel> result = select(queryStr);
        if(result.size()==0) return  null;
        return result.get(0);

    }

    public CmsBtSellerCatModel selectByRootCatPath(String channelId, int cartId, String catPath) {

        String queryStr = "{\"channelId\":\"" + channelId + "\"" + ",\"cartId\"" + ":" + cartId + ",\"catPath\"" + ":\"" + catPath + "\"}";

        return selectOneWithQuery(queryStr);
    }
    public CmsBtSellerCatModel selectByRootCatId(String channelId, int cartId, String catId) {

        String queryStr = "{\"channelId\":\"" + channelId + "\"" + ",\"cartId\"" + ":" + cartId + ",\"catId\"" + ":\"" + catId + "\"}";

        return selectOneWithQuery(queryStr);
    }

    /**
     *
     * @param channelId
     * @param cartId
     * @param parentCId
     * @param cId
     * @return
     */
    public CmsBtSellerCatModel delete(String channelId, int cartId, String parentCId, String cId){


        //如果是根节点
        if(parentCId.equals("0"))
        {
            String queryStr = "{'channelId':'" + channelId + "','cartId':" + cartId + ", catId: '"+ cId  +"'}";
            List<CmsBtSellerCatModel> resultList = select(queryStr);
            if (resultList.size() > 0) {
                CmsBtSellerCatModel me = resultList.get(0);
                delete(me);
                return me;
            }
        }
        //非根节点
        else {
            String queryStr = "{'channelId':'" + channelId + "','cartId':" + cartId + "}";
            List<CmsBtSellerCatModel> allCat = select(queryStr);
            List<CmsBtSellerCatModel> resultList = findCId(allCat, parentCId);
            if (resultList.size() > 0) {
                CmsBtSellerCatModel result = null;
                CmsBtSellerCatModel parent = resultList.get(0);
                List<CmsBtSellerCatModel> children = parent.getChildren();
                for (int i = children.size() - 1; i >= 0; i--) {
                    if (children.get(i).getCatId().equals(cId)) {
                        result = children.get(i);
                        children.remove(i);
                        break;
                    }
                }

                if(children.size() == 0)
                {
                    parent.setIsParent(0);
                }

                //更新Document
                for (CmsBtSellerCatModel model : allCat) {
                    update(model);
                }
                return result;
            }

        }

        return null;
    }


    /**
     *
     * @param channelId
     * @param cartId
     * @param cName 店铺自定义分类名
     * @param urlKey 2016-12-08 新增
     * @param parentCId 父cId
     * @param cId 店铺自定义分类Id
     * @param creator 创建人
     */
    public void add(String channelId, int cartId, String cName, String urlKey, String parentCId , String cId, String creator) {

        //如果是根节点
        if(parentCId.equals("0"))
        {
            CmsBtSellerCatModel newNode = new CmsBtSellerCatModel();
            newNode.setCatName(cName);
            newNode.setUrlKey(urlKey);
            newNode.setChannelId(channelId);
            newNode.setCartId(cartId);
            newNode.setCatId(cId);
            String now = DateTimeUtil.getNow();
            newNode.setCreated(now);
            newNode.setModified(now);
            newNode.setCreater(creator);
            newNode.setModifier(creator);
            newNode.setCatPath(cName);
            newNode.setParentCatId("0");
            newNode.setFullCatId(cId);
            newNode.setIsParent(0);
            newNode.setChildren(new ArrayList<CmsBtSellerCatModel>());
            insert(newNode);
        }
        //非根节点
        else {
            String queryStr = "{'channelId':'" + channelId + "','cartId':" + cartId + "}";

            List<CmsBtSellerCatModel> allCat = select(queryStr);
            List<CmsBtSellerCatModel> resultList = findCId(allCat, parentCId);
            if (resultList.size() > 0) {
                CmsBtSellerCatModel parent = resultList.get(0);
                CmsBtSellerCatModel newNode = new CmsBtSellerCatModel();
                newNode.setCatName(cName);
                newNode.setUrlKey(urlKey);
                newNode.setChannelId(channelId);
                newNode.setCartId(cartId);
                newNode.setCatId(cId);
                String now = DateTimeUtil.getNow();
                newNode.setCreated(now);
                newNode.setModified(now);
                newNode.setCreater(creator);
                newNode.setModifier(creator);
                newNode.setCatPath(parent.getCatPath() + ">" + cName);
                newNode.setFullCatId(parent.getFullCatId() + "-" + cId);
                newNode.setParentCatId(parent.getCatId());
                newNode.setIsParent(0);
                newNode.setChildren(new ArrayList<CmsBtSellerCatModel>());
                parent.setIsParent(1);
                parent.getChildren().add(newNode);

                //更新Document
                for (CmsBtSellerCatModel model : allCat) {
                    update(model);
                }
            }
        }
    }

    /**
     *  @param channelId
     * @param cartId
     * @param cName 分类自定义分类名
     * @param mapping
     * @param cId   店铺自定义分类Id
     * @param modifier 更新者
     */
    public  List<CmsBtSellerCatModel> update(String channelId, int cartId, String cName, Map<String, Object> mapping, String cId, String modifier) {

        String queryStr = "{'channelId':'" + channelId + "','cartId':" + cartId + "}";

        List<CmsBtSellerCatModel> allCat = select(queryStr);
        List<CmsBtSellerCatModel> resultList =  findCId(allCat, cId);
        List<CmsBtSellerCatModel> changedList = new ArrayList<>();
        if(resultList.size() > 0)
        {
            CmsBtSellerCatModel result = resultList.get(0);

            // 类目名称发生变化
            if(!result.getCatName().equals(cName)) {
                result.setCatName(cName);
                String oldPath = result.getCatPath();

                String[] paths = oldPath.split(">");
                if (paths.length > 0) {
                    paths[paths.length - 1] = cName;
                }

                String catPath = "";
                for (String path : paths) {
                    if (catPath.equals("")) {
                        catPath = path;
                    } else {
                        catPath = catPath + ">" + path;
                    }
                }
                result.setCatPath(catPath);
                //递归更新子节点
                updateChildren(result.getChildren(), result.getCatPath(), modifier);
                //展开result的所有node
                changedList = expandNode(result);
            }
            if(mapping != null){
                result.setMapping(mapping);
            }
            result.setModifier(modifier);
            result.setModified(DateTimeUtil.getNow());

            //更新Document
            for (CmsBtSellerCatModel model: allCat) {
                update(model);
            }
            return changedList;
        }
        return null;

    }


    /**
     * 找出所有孩子节点
     * @param root
     * @return
     */
    private List<CmsBtSellerCatModel> expandNode(CmsBtSellerCatModel root)
    {
        List<CmsBtSellerCatModel> result = new ArrayList<>();
        result.add(root);
        for (CmsBtSellerCatModel model: root.getChildren()) {
            result.addAll(expandNode(model) );
        }
        return  result;
    }

    private void updateChildren(List<CmsBtSellerCatModel> children, String parentCatPath, String modifier)
    {
        for (CmsBtSellerCatModel model:children) {
            model.setCatPath(parentCatPath + ">" + model.getCatName());
            model.setModifier(modifier);
            model.setModified(DateTimeUtil.getNow());
            updateChildren(model.getChildren(), model.getCatPath(), modifier);
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
                List<CmsBtSellerCatModel> children = findCId(model.getChildren() ,cId );
                result.addAll(children);
            }

        }
        return  result;
    }

    /**
     *
     * @param cartId
     * @param channelId
     * @return
     */
    public WriteResult deleteSortableCat(Integer cartId,String channelId){
        String queryStr = "{'cartId':"+cartId+",'channelId':'"+channelId+"'}";
        return deleteWithQuery(queryStr);
    }

}
