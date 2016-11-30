package com.voyageone.components.sneakerhead.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.voyageone.components.sneakerhead.SneakerHeadBase;
import com.voyageone.components.sneakerhead.SneakerheadRemoteUrlConstants;
import com.voyageone.components.sneakerhead.bean.SneakerHeadCodeModel;
import com.voyageone.components.sneakerhead.bean.SneakerHeadFeedInfoRequest;
import com.voyageone.components.sneakerhead.bean.SneakerheadCategoryModel;
import org.springframework.http.*;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.voyageone.components.sneakerhead.SneakerheadRemoteUrlConstants.FEED_INFO_URL;
import static com.voyageone.components.sneakerhead.SneakerheadRemoteUrlConstants.FEED_SUM_URL;

/**
 * Created by gjl on 2016/11/15.
 */
@Service
public class SneakerHeadFeedService extends SneakerHeadBase {
    /**
     * 批量查询商品
     *
     * @param request
     * @return
     * @throws Exception
     */
    @Retryable
    public List<SneakerHeadCodeModel> getFeedInfo(SneakerHeadFeedInfoRequest request) throws Exception {
        String response = getResponse(FEED_INFO_URL, request);
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType type = typeFactory.constructParametrizedType(List.class, List.class, SneakerHeadCodeModel.class);
        return objectMapper.readValue(response, type);
    }

    /**
     * 批量查询商品件数
     *
     * @param date
     * @return
     * @throws Exception
     */
    @Retryable
    public int getFeedCount(Date date) throws Exception {
        String response = getResponse(FEED_SUM_URL, date);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response, Integer.class);
    }

    /**
     * 获取美国的类目结构
     *
     * @return 类目树
     * @throws Exception 获取失败产生的异常
     */
    @Retryable
    public List<SneakerheadCategoryModel> getCategory(boolean withCode) throws IOException {
        String responseJson = getResponse(SneakerheadRemoteUrlConstants.CATEGORY_URL, withCode);
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType type = typeFactory.constructParametrizedType(List.class, List.class, SneakerheadCategoryModel.class);
        return objectMapper.readValue(responseJson, type);
    }

    private String getResponse(String url, Object param) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(APPLICATION_JSON_UTF_8));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(param);
        HttpEntity<String> httpEntity = new HttpEntity<>(json, httpHeaders);
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(SNEAKERHEAD_BASE_URL + url, HttpMethod.POST, httpEntity, String.class);
        return responseEntity.getBody();
    }

    private String getResponse(String url) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(APPLICATION_JSON_UTF_8));
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(SNEAKERHEAD_BASE_URL + url, HttpMethod.POST, httpEntity, String.class);
        return responseEntity.getBody();
    }
}
