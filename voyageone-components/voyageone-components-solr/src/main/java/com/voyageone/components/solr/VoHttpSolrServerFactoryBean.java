package com.voyageone.components.solr;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.LBHttpSolrServer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.solr.server.support.HttpSolrServerFactory;
import org.springframework.data.solr.server.support.HttpSolrServerFactoryBean;
import org.springframework.util.Assert;

import java.net.MalformedURLException;

/**
 * Created by james on 2017/4/14.
 */
public class VoHttpSolrServerFactoryBean extends HttpSolrServerFactory implements FactoryBean<SolrServer>,
        InitializingBean, DisposableBean {

    private static final String SERVER_URL_SEPARATOR = ",";
    private String url;
    private Integer timeout;
    private Integer maxConnections;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(url);
        initSolrServer();
    }

    private void initSolrServer() {
        if (this.url.contains(SERVER_URL_SEPARATOR)) {
            createLoadBalancedHttpSolrServer();
        } else {
            createHttpSolrServer();
        }
    }

    private void createHttpSolrServer() {
        HttpSolrServer httpSolrServer = new VoPostHttpSolrServer(this.url);
        if (timeout != null) {
            httpSolrServer.setConnectionTimeout(timeout.intValue());
        }
        if (maxConnections != null) {
            httpSolrServer.setMaxTotalConnections(maxConnections);
        }
        this.setSolrServer(httpSolrServer);
    }

    private void createLoadBalancedHttpSolrServer() {
        try {
            LBHttpSolrServer lbHttpSolrServer = new LBHttpSolrServer(StringUtils.split(this.url, SERVER_URL_SEPARATOR));
            if (timeout != null) {
                lbHttpSolrServer.setConnectionTimeout(timeout.intValue());
            }
            this.setSolrServer(lbHttpSolrServer);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Unable to create Load Balanced Http Solr Server", e);
        }
    }

    @Override
    public SolrServer getObject() throws Exception {
        return getSolrServer();
    }

    @Override
    public Class<?> getObjectType() {
        if (getSolrServer() == null) {
            return HttpSolrServer.class;
        }
        return getSolrServer().getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public void setMaxConnections(Integer maxConnections) {
        this.maxConnections = maxConnections;
    }

}
