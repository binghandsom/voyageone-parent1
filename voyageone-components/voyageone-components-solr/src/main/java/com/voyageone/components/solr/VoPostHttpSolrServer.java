package com.voyageone.components.solr;

import org.apache.http.client.HttpClient;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.SolrParams;

import java.io.IOException;

/**
 * 提供POST的方式提交给 Solr Server
 *
 * @author chuanyu.liang 2017/04/10
 * @version 2.0.0
 * @since 2.0.0
 */
public class VoPostHttpSolrServer extends HttpSolrClient {
    public VoPostHttpSolrServer(String baseURL) {
        super(baseURL);
    }

    public VoPostHttpSolrServer(String baseURL, HttpClient client) {
        super(baseURL, client);
    }

    public VoPostHttpSolrServer(String baseURL, HttpClient client, ResponseParser parser) {
        super(baseURL, client, parser);
    }

    @Override
    public QueryResponse query(SolrParams params) throws SolrServerException, IOException {
        return this.query(params, SolrRequest.METHOD.POST);
    }

}
