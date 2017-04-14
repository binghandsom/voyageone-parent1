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
public class VoPostHttpSolrServer extends HttpSolrServer {
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
    public QueryResponse query(SolrParams params) throws SolrServerException {
        return this.query(params, SolrRequest.METHOD.POST);
    }

}
