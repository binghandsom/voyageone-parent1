package com.voyageone.components.sneakerhead.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyageone.components.sneakerhead.SneakerHeadBase;
import com.voyageone.components.sneakerhead.bean.CmsBtProductModel_SalesBean;
import com.voyageone.components.sneakerhead.bean.SneakerHeadCodeModel;
import com.voyageone.components.sneakerhead.bean.SneakerHeadFeedInfoRequest;
import org.springframework.http.*;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

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
public class SneakerHeadFeedService extends SneakerHeadBase {
    /**
     * 批量查询商品
     */
    @Retryable
    public List<SneakerHeadCodeModel> sneakerHeadResponse(SneakerHeadFeedInfoRequest request, String domain) throws Exception {
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
    public int sneakerHeadFeedCount(Date date, String domain) throws Exception {
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
    public List<CmsBtProductModel_SalesBean> sneakerHeadSale(List<String> codeList, String domain) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(CONTENT_TYPE));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(codeList);
        HttpEntity<String> httpEntity = new HttpEntity<>(json, httpHeaders);
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(getSneakerSaleUrl(domain), HttpMethod.POST, httpEntity, String.class);
        return objectMapper.readValue(responseEntity.getBody(), objectMapper.getTypeFactory().constructParametrizedType(ArrayList.class, List.class, CmsBtProductModel_SalesBean.class));

    }
}
