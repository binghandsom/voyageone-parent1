package com.voyageone.components.sneakerhead.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.overstock.mp.mpc.externalclient.api.exception.ClientException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.sneakerhead.SneakerHeadBase;
import com.voyageone.components.sneakerhead.bean.SneakerHeadCodeModel;
import com.voyageone.components.sneakerhead.bean.SneakerHeadRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gjl on 2016/11/15.
 */
@Service
public class SneakerHeadFeedService extends SneakerHeadBase {
    /**
     * 批量查询商品
     * @param request
     * @return
     * @throws Exception
     */
    @Retryable
    public List<SneakerHeadCodeModel> sneakerHeadResponse(SneakerHeadRequest request) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(contentType));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
        HttpEntity<String> httpEntity = new HttpEntity<>(json, httpHeaders);
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(sneakerInfoUrl, HttpMethod.POST, httpEntity, String.class);
        return objectMapper.readValue(responseEntity.getBody(), objectMapper.getTypeFactory().constructParametricType(List.class, SneakerHeadCodeModel.class));
    }

    /**
     * 批量查询商品件数
     * @param date
     * @return
     * @throws Exception
     */
    @Retryable
    public int sneakerHeadFeedCount(Date date) throws Exception {;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(contentType));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(date);
        HttpEntity<String> httpEntity = new HttpEntity<>(json, httpHeaders);
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(sneakerCountUrl, HttpMethod.POST, httpEntity, String.class);
        return objectMapper.readValue(responseEntity.getBody(), Integer.class);
    }
}
