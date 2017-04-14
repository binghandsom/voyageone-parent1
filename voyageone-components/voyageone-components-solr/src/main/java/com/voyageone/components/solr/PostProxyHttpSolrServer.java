package com.voyageone.components.solr;

import org.apache.http.client.HttpClient;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.request.RequestWriter;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;

import java.io.IOException;
import java.util.*;

/**
 * 提供POST的方式提交给 Solr Server
 *
 * @author chuanyu.liang 2017/04/10
 * @version 2.0.0
 * @since 2.0.0
 */
public class PostProxyHttpSolrServer extends SolrServer {

    private HttpSolrServer orgSolrServer;
    public PostProxyHttpSolrServer(HttpSolrServer httpSolrServer) {
        this.orgSolrServer = httpSolrServer;
    }

    @Override
    public QueryResponse query(SolrParams params) throws SolrServerException {
        return this.orgSolrServer.query(params, SolrRequest.METHOD.POST);
    }

    public Set<String> getQueryParams() {
        return this.orgSolrServer.getQueryParams();
    }

    public void setQueryParams(Set<String> queryParams) {
        this.orgSolrServer.setQueryParams(queryParams);
    }

    public NamedList<Object> request(SolrRequest request) throws SolrServerException, IOException {
        return this.orgSolrServer.request(request);
    }

    public NamedList<Object> request(SolrRequest request, ResponseParser processor) throws SolrServerException, IOException {
        return this.orgSolrServer.request(request, processor);
    }

    public HttpSolrServer.HttpUriRequestResponse httpUriRequest(SolrRequest request) throws SolrServerException, IOException {
        return this.orgSolrServer.httpUriRequest(request);
    }

    public HttpSolrServer.HttpUriRequestResponse httpUriRequest(SolrRequest request, final ResponseParser processor) throws SolrServerException, IOException {
        return this.orgSolrServer.httpUriRequest(request, processor);
    }

    public ModifiableSolrParams getInvariantParams() {
        return this.orgSolrServer.getInvariantParams();
    }

    public String getBaseURL() {
        return this.orgSolrServer.getBaseURL();
    }

    public void setBaseURL(String baseURL) {
        this.orgSolrServer.setBaseURL(baseURL);
    }

    public ResponseParser getParser() {
        return this.orgSolrServer.getParser();
    }

    public void setParser(ResponseParser processor) {
        this.orgSolrServer.setParser(processor);
    }

    public HttpClient getHttpClient() {
        return this.orgSolrServer.getHttpClient();
    }

    public void setConnectionTimeout(int timeout) {
        this.orgSolrServer.setConnectionTimeout(timeout);
    }

    public void setSoTimeout(int timeout) {
        this.orgSolrServer.setSoTimeout(timeout);
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.orgSolrServer.setFollowRedirects(followRedirects);
    }

    public void setAllowCompression(boolean allowCompression) {
        this.orgSolrServer.setAllowCompression(allowCompression);
    }

    public void setMaxRetries(int maxRetries) {
        this.orgSolrServer.setMaxRetries(maxRetries);
    }

    public void setRequestWriter(RequestWriter requestWriter) {
        this.orgSolrServer.setRequestWriter(requestWriter);
    }

    public UpdateResponse add(Iterator<SolrInputDocument> docIterator) throws SolrServerException, IOException {
        return this.orgSolrServer.add(docIterator);
    }

    public UpdateResponse addBeans(final Iterator<?> beanIterator) throws SolrServerException, IOException {
        return this.orgSolrServer.addBeans(beanIterator);
    }

    public void shutdown() {
        this.orgSolrServer.shutdown();
    }

    public void setDefaultMaxConnectionsPerHost(int max) {
        this.orgSolrServer.setDefaultMaxConnectionsPerHost(max);
    }

    public void setMaxTotalConnections(int max) {
        this.orgSolrServer.setMaxTotalConnections(max);
    }

    public boolean isUseMultiPartPost() {
        return this.orgSolrServer.isUseMultiPartPost();
    }

    public void setUseMultiPartPost(boolean useMultiPartPost) {
        this.orgSolrServer.setUseMultiPartPost(useMultiPartPost);
    }
}
