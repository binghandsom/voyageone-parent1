package com.voyageone.components.sneakerhead.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyageone.components.sneakerhead.SneakerHeadBase;
import com.voyageone.components.sneakerhead.bean.CmsBtProductModel_SalesBean;
import com.voyageone.components.sneakerhead.bean.SneakerHeadCodeModel;
import com.voyageone.components.sneakerhead.bean.SneakerHeadFeedInfoRequest;
import com.voyageone.components.sneakerhead.bean.SneakerheadCategoryModel;
import org.springframework.http.*;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SneakerHeadFeedService
 *
 * @author gjl on 2016/11/15.
 * @version 0.0.1
 */
@Service
public class SneakerheadApiService extends SneakerHeadBase {
    /**
     * 批量查询商品
     */
    @Retryable
    public List<SneakerHeadCodeModel> getFeedInfo(SneakerHeadFeedInfoRequest request, String domain) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(CONTENT_TYPE));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
        HttpEntity<String> httpEntity = new HttpEntity<>(json, httpHeaders);
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(getSneakerInfoUrl(domain), HttpMethod.POST, httpEntity, String.class);
        return objectMapper.readValue(responseEntity.getBody(), objectMapper.getTypeFactory().constructParametrizedType(ArrayList.class, List.class, SneakerHeadCodeModel.class));
    }

    /**
     * 批量查询商品件数
     */
    @Retryable
    public int getFeedCount(Date date, String domain) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(CONTENT_TYPE));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(date);
        HttpEntity<String> httpEntity = new HttpEntity<>(json, httpHeaders);
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(getSneakerCountUrl(domain), HttpMethod.POST, httpEntity, String.class);
        return objectMapper.readValue(responseEntity.getBody(), Integer.class);
    }

    /**
     * 取得销售数据
     */
    @Retryable
    public List<CmsBtProductModel_SalesBean> getUsSales(List<String> codeList, String domain) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(CONTENT_TYPE));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(codeList);
        HttpEntity<String> httpEntity = new HttpEntity<>(json, httpHeaders);
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(getSneakerSaleUrl(domain), HttpMethod.POST, httpEntity, String.class);
        return objectMapper.readValue(responseEntity.getBody(), objectMapper.getTypeFactory().constructParametrizedType(ArrayList.class, List.class, CmsBtProductModel_SalesBean.class));

    }

    /**
     * 获取美国官网分类树
     *
     * @param withCode 是否包含 category 下挂的 code
     * @param domain   服务器对外地址
     * @return 官网分类树(已去除外部的default category)
     * @throws IOException 网络通讯中出现的异常
     */
    @Retryable
    public List<SneakerheadCategoryModel> getCategory(boolean withCode, String domain) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(CONTENT_TYPE));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(withCode);
        HttpEntity<String> httpEntity = new HttpEntity<>(json, httpHeaders);
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(getCategoryUrl(domain), HttpMethod.POST, httpEntity, String.class);
        return objectMapper.readValue(responseEntity.getBody(), objectMapper.getTypeFactory().constructParametrizedType(ArrayList.class, List.class, SneakerheadCategoryModel.class));

    }
}
