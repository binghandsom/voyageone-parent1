package com.voyageone.task2.cms.service.system;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoServerException;
import com.mongodb.util.JSON;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.mail.Mail;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.service.dao.cms.mongo.CmsZiIndexDao;
import com.voyageone.service.model.cms.mongo.meta.CmsZiIndexModel;
import com.voyageone.service.model.cms.mongo.meta.CmsZiIndexModel_Index;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;


@Service
public class CmsMongoOptimizeIndexService extends BaseCronTaskService {

    @Autowired
    private CmsZiIndexDao indexOptDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsMongoOptimizeIndexJob";
    }

    /**
     * onStartup
     *
     * @param taskControlList job 配置
     * @throws Exception
     */
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        $info("CmsMongoOptimizeIndexService in onStartup");

        String indexDefColl = "cms_zi_index";

        // 1.取得所有DB中所有Collection名
        Set<String> collNames = indexOptDao.getAllCollections(indexDefColl);

        List<String> allDefColls = indexOptDao.getAllDefinedCollections();
        // 取得需要忽略的表的Coll
        List<String> allExDefColls = indexOptDao.getAllExcludeCollections();
        List<String> ignoreCollList = new ArrayList<>();

        List<Map<String, Object>> allOperList = new ArrayList<>();
        List<String> nonDefIndexList = new ArrayList<>();
        // 2.逐个Collection检查索引是否完备
        for (String collName : collNames) {
            $info("开始检查：" + collName);

            String exName = getDefineName(collName, allExDefColls);
            if (exName != null) {
                String ignoreMsg = "已忽略：" + collName + ",因为匹配了->" + exName;
                $info(ignoreMsg);
                ignoreCollList.add(ignoreMsg);
                continue;
            }

            // 取得Coll上存在的索引
            List<DBObject> indexInfoList = indexOptDao.getCollectionIndexesFromDB(collName);
            // 取得Coll定义的索引
            String defCollName = getDefineName(collName, allDefColls);

            if (defCollName == null) {
                // 真实存在的Coll，在Meta中没有定义
                // 直接取得存在的索引，放入未定义列表中
                if (indexInfoList != null) {
                    for (DBObject index : indexInfoList) {
                        boolean unique = index.get("unique") == null ? false : (Boolean) index.get("unique");
                        String noDefined = String.format("%s->name:%s,key:%s,unique:%s", collName, index.get("name"), index.get("key"), unique);
                        nonDefIndexList.add(noDefined);
                    }
                }
            } else {
                // 取得在Coll中存在有在Meta中定义的索引
                CmsZiIndexModel indexListDefined = indexOptDao.getCollectionIndexesFromMeta(defCollName);
                if (indexListDefined != null) {
                    List<Map<String, Object>> operList = prepIndexOperations(indexListDefined, indexInfoList, collName);
                    allOperList.addAll(operList);
                }

                // 取得在Coll中存在没有在Meta中定义的索引
                if (indexInfoList != null) {
                    for (DBObject index : indexInfoList) {
                        boolean isDefined = checkIndexDefined(index, indexListDefined);
                        // 如果没有在Meta中`定义
                        if (!isDefined) {
                            boolean unique = index.get("unique") == null ? false : (Boolean) index.get("unique");
                            String noDefined = String.format("%s->name:%s,key:%s,unique:%s", collName, index.get("name"), index.get("key"), unique);
                            nonDefIndexList.add(noDefined);
                        }
                    }
                }
            }
            $info("结束检查：" + collName);
        }

        // 3. 执行索引操作
        $info("#############维护索引开始###############");
        List<String> proccessResult = processIndexes(allOperList);
        $info("#############维护索引结束###############");

        // 3.邮件通知处理结果(被创建，删除，未定义的索引)
        boolean isNeedMailNotify = true;
        StringBuilder content = new StringBuilder();
        if ((proccessResult == null || proccessResult.isEmpty()) && (nonDefIndexList == null || nonDefIndexList.isEmpty()) && (ignoreCollList == null || ignoreCollList.isEmpty())) {
            content.append("mongodb索引正常，不需要特别处理！");
            isNeedMailNotify = false;
        } else {
            for (String oper : proccessResult) {
                content.append(oper);
                content.append("<br/>");
            }

            if (nonDefIndexList != null && nonDefIndexList.size() > 0) {
                content.append("<br/><b>如下索引没有在meta中定义：</b>");
                content.append("<br/>");
            }
            for (String nonDef : nonDefIndexList) {
                content.append(nonDef);
                content.append("<br/>");
            }

            if (ignoreCollList != null && ignoreCollList.size() > 0) {
                content.append("<br/><b>下列Coll因为可能是临时表，所以被忽略：</b>");
                content.append("<br/>");
            }
            for (String ignore : ignoreCollList) {
                content.append(ignore);
                content.append("<br/>");
            }
        }

        if (isNeedMailNotify) {
            String subject = "mongodb索引维护结果通知 ";
            // 取得通知接收者
            String receiver = Codes.getCodeName(Constants.MAIL.EMAIL_RECEIVER, "MONGODB_INDEX_AUTO_MAINTAIN");
            $info("receiver from code table:" + receiver);
            if (StringUtil.isEmpty(receiver)) {
                $info("没有取到邮件接收者！");
            } else {
                Mail.send(receiver, subject, content.toString());
            }
        }
    }


    private List<Map<String, Object>> prepIndexOperations(CmsZiIndexModel index, List<DBObject> indexInfoList, String collName) {
        List<CmsZiIndexModel_Index> indexList = index.getIndexes();
        List<Map<String, Object>> operList = new ArrayList<>();
        for (CmsZiIndexModel_Index idx : indexList) {
            //String name = idx.getName();
            String keyDef = idx.getKey().replace(" ", "");
            boolean uniqueDef = idx.getUnique() == null ? false : idx.getUnique().booleanValue();
            boolean isExisted = false;

            for (DBObject idxExisting : indexInfoList) {

                String idxNameExist = idxExisting.get("name").toString();
                if ("_id_".equals(idxNameExist)) {
                    continue;
                }

                DBObject keyObj = (DBObject) idxExisting.get("key");
                keyObj.keySet().stream().filter(s -> keyObj.get(s) instanceof Double).forEach(s -> {
                    keyObj.put(s, ((Double) keyObj.get(s)).intValue());
                });


                String keyExists = keyObj.toString().replace(" ", "");

                boolean uniqueExists = false;
                if (idxExisting.containsField("unique")) {
                    uniqueExists = (Boolean) idxExisting.get("unique");
                }
                if (keyDef.equals(keyExists) && isIndexOptionsEqual(idxExisting, idx)) {
                    // 相同key,相同属性（unique,sparse,partialFilterExpression）的索引存在
                    isExisted = true;
                    break;
                } else if (keyDef.equals(keyExists) && !isIndexOptionsEqual(idxExisting, idx)) {
                    // 相同key,不同unique的索引存在,需要先删除索引然后重建
                    Map<String, Object> dropMap = new HashMap<>();
                    dropMap.put("coll_name", collName);
                    dropMap.put("action", "drop");
                    dropMap.put("key", keyExists);
                    dropMap.put("unique", uniqueExists);
                    dropMap.put("name", idxNameExist);
                    operList.add(dropMap);

                    //索引然后重建
                    Map<String, Object> createMap = new HashMap<>();
                    createMap.put("coll_name", collName);
                    createMap.put("action", "create");
                    createMap.put("key", keyDef);
                    createMap.put("unique", uniqueDef);
                    createMap.put("sparse", idx.getSparse() == null ? false : idx.getSparse().booleanValue());
                    createMap.put("partialFilterExpression", idx.getPartialFilterExpression());
                    createMap.put("name", idx.getName());

                    operList.add(createMap);

                    isExisted = true;
                    break;
                }
            }

            if (!isExisted) {
                Map<String, Object> createMap = new HashMap<>();
                createMap.put("coll_name", collName);
                createMap.put("action", "create");
                createMap.put("key", keyDef);
                createMap.put("unique", uniqueDef);
                createMap.put("sparse", idx.getSparse() == null ? false : idx.getSparse().booleanValue());
                createMap.put("partialFilterExpression", idx.getPartialFilterExpression());
                createMap.put("name", idx.getName());
                operList.add(createMap);
            }

        }
        return operList;
    }

    private List<String> processIndexes(List<Map<String, Object>> allOperList) {
        List<String> proccessResult = new ArrayList<>();
        for (Map<String, Object> operMap : allOperList) {
            String oper = operMap.get("action").toString();
            String coll_name = operMap.get("coll_name").toString();
            String failMsg = "";
            try {
                if ("drop".equals(oper)) {
                    // 删除索引
                    String indexName = operMap.get("name").toString();
                    String keys = operMap.get("key").toString();
                    boolean unique = ((Boolean) operMap.get("unique")).booleanValue();
                    String indexDef = String.format("coll->%s,index->%s,key->%s,unique->%s", coll_name, indexName, keys, unique);
                    failMsg = "删除索引失败：" + indexDef;
                    $info("删除索引开始：" + indexDef);
                    indexOptDao.dropCollectionIndexFromDB(coll_name, indexName);
                    $info("删除索引完成：" + indexDef);
                    proccessResult.add("删除索引：" + indexDef);
                } else if ("create".equals(oper)) {
                    // 创建索引的名称传入null，也就是利用默认索引名称
                    String name = (String) operMap.get("name");
                    String keys = operMap.get("key").toString();
                    boolean unique = ((Boolean) operMap.get("unique")).booleanValue();
                    boolean sparse = ((Boolean) operMap.get("sparse")).booleanValue();
                    DBObject keyObj = (DBObject) JSON.parse(keys);

                    DBObject optionsObj = new BasicDBObject();
                    if (unique) {
                        optionsObj.put("unique", unique);
                    }
                    if (sparse) {
                        optionsObj.put("sparse", sparse);
                    }
                    String partialExp = (String) operMap.get("partialFilterExpression");
                    if (partialExp != null && partialExp.trim().length() > 0) {
                        optionsObj.put("partialFilterExpression", JSON.parse(partialExp));
                    }
                    if (name != null && name.trim().length() > 0) {
                        optionsObj.put("name", name);
                    }

//                    optionsObj.put("background", true);
                    String indexDef = String.format("coll->%s,key->%s,options->%s", coll_name, keys, optionsObj.toString());
                    failMsg = "创建索引失败：" + indexDef;
                    $info("创建索引开始：" + indexDef);
                    indexOptDao.createCollectionIndexFromDB(coll_name, keyObj, optionsObj);
                    $info("创建索引完成：" + indexDef + ",option-->" + optionsObj.toString());
                    proccessResult.add("创建索引：" + indexDef);
                }
            } catch (MongoServerException e) {
                proccessResult.add("<font color=\"#FF0000\">" + failMsg + "</font>");
                proccessResult.add(e.getMessage());
                //继续处理
                continue;
            }
        }
        return proccessResult;
    }

    private boolean checkIndexDefined(DBObject index, CmsZiIndexModel indexListDefined) {
        if (indexListDefined == null) {
            return false;
        }
        String key = index.get("key").toString().replace(" ", "");
        boolean isDefined = false;
        for (CmsZiIndexModel_Index idx : indexListDefined.getIndexes()) {
            String keyDefine = idx.getKey().replace(" ", "");
            if (key.equals(keyDefine)) {
                isDefined = true;
                break;
            }
        }
        return isDefined;
    }

    private String getDefineName(String collName, List<String> definedCollNameList) {

        for (String defName : definedCollNameList) {
            Pattern p = Pattern.compile(defName);
            if (p.matcher(collName).matches()) {
                return defName;
            }
        }
        return null;
    }

    private boolean isIndexOptionsEqual(DBObject indexInfo, CmsZiIndexModel_Index indexModel) {

        boolean existingUnique = false;
        if (indexInfo.containsField("unique")) {
            existingUnique = (Boolean) indexInfo.get("unique");
        }
        boolean defUnique = indexModel.getUnique() == null ? false : indexModel.getUnique().booleanValue();
        boolean defSparse = indexModel.getSparse() == null ? false : indexModel.getSparse().booleanValue();
        boolean existingSparse = false;
        if (indexInfo.containsField("sparse")) {
            existingSparse = (Boolean) indexInfo.get("sparse");
        }
        DBObject partialExp = null;
        if (indexInfo.containsField("partialFilterExpression")) {
            partialExp = (DBObject) indexInfo.get("partialFilterExpression");
        }
        String defPartialExp = indexModel.getPartialFilterExpression();
        if (existingUnique != defUnique || existingSparse != defSparse
                || (partialExp == null && defPartialExp != null) || (partialExp != null && defPartialExp == null)) {
            return false;
        }

        if (partialExp == null && defPartialExp == null) {
            return true;
        }

        String existingPartialExp = partialExp.toString().replace(" ", "");
        if (existingPartialExp.equals(defPartialExp.replace(" ", ""))) {
            return true;
        }

        return false;
    }
}
