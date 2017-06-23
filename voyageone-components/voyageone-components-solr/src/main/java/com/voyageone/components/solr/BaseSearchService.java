package com.voyageone.components.solr;

import com.voyageone.common.spring.SpringContext;
import com.voyageone.components.ComponentBase;
import com.voyageone.components.solr.bean.SolrUpdateBean;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.data.solr.core.SolrTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Base Search Service
 *
 * @author chuanyu.liang 2016/10/20
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class BaseSearchService extends ComponentBase {

    protected abstract String getSolrTemplateName();

    /**
     * getSolrTemplate
     */
    protected SolrTemplate getSolrTemplate() {
        return (SolrTemplate) SpringContext.getBean(getSolrTemplateName());
    }

    /**
     * saveBean
     */
    public String saveBean(SolrUpdateBean bean) {
        UpdateResponse response = getSolrTemplate().saveBean(bean);
        return response.toString();
    }

    public String saveBean(String coreName, SolrUpdateBean bean) {
        UpdateResponse response = getSolrTemplate().saveBean(coreName, bean);
        return response.toString();
    }

    /**
     * saveBeans
     */
    public String saveBeans(List<SolrUpdateBean> beans) {
        UpdateResponse response = getSolrTemplate().saveBeans(beans);
        return response.toString();
    }

    public String saveBeans(String coreName, List<SolrUpdateBean> beans) {
        UpdateResponse response = getSolrTemplate().saveBeans(coreName, beans);
        return response.toString();
    }

    /**
     * saveBean
     */
    public String deleteById(String id) {
        UpdateResponse response = getSolrTemplate().deleteById(id);
        return response.toString();
    }

    public String deleteById(String coreName, String id) {
        UpdateResponse response = getSolrTemplate().deleteById(coreName, id);
        return response.toString();
    }

    /**
     * saveBeans
     */
    public String deleteByIds(List<String> ids) {
        UpdateResponse response = getSolrTemplate().deleteById(ids);
        return response.toString();
    }
    public String deleteByIds(String coreName, List<String> ids) {
        UpdateResponse response = getSolrTemplate().deleteById(coreName, ids);
        return response.toString();
    }

    /**
     * commit
     */
    public void commit() {
        getSolrTemplate().commit();
    }

    /**
     * commit specified collection
     */
    public void commit(String coreName) {
        getSolrTemplate().commit(coreName);
    }

    /**
     * optimize
     */
    public void optimize() {
        try {
            getSolrTemplate().getSolrClient().optimize();
        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void optimize(String coreName) {
        try {
            getSolrTemplate().getSolrClient().optimize(coreName);
        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * getDataFromDocument
     */
    protected Object getDataFromDocument(Map<String, Object> doc, String path) {
        return getDataFromDocument(doc, path, new ArrayList<>());
    }

    /**
     * getDataFromDocument
     */
    @SuppressWarnings("ConstantConditions")
    private Object getDataFromDocument(Map<String, Object> doc, String path, List<String> rightPathList) {
        if (path == null || doc == null) {
            return null;
        }
        if (doc.containsKey(path)) {
            if (rightPathList.isEmpty()) {
                return doc.get(path);
            } else {
                Object docMap = doc.get(path);
                if (docMap instanceof Map) {
                    for (int i = 0; i < rightPathList.size() - 1; i++) {
                        docMap = ((Map) docMap).get(rightPathList.get(i));
                        if (!Map.class.isInstance(docMap)) {
                            return null;
                        }
                    }
                    return ((Map) docMap).get(rightPathList.get(rightPathList.size() - 1));
                }
            }
        } else {
            if (path.contains("\\.")) {
                String newPath = path.substring(0, path.lastIndexOf("\\."));
                String newRightPath = path.substring(path.lastIndexOf("\\.") + 1);
                List<String> newRightPathList = new ArrayList<>();
                newRightPathList.add(newRightPath);
                newRightPathList.addAll(rightPathList);
                return getDataFromDocument(doc, newPath, newRightPathList);
            }
        }
        return null;
    }
}
