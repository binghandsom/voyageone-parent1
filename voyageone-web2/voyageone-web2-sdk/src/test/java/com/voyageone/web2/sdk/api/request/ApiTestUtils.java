package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.VoApiResponse;
import org.apache.commons.beanutils.BeanMap;
import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * VoApi测试类
 * 使用：
 * 调用Run(VoApiRequest request)方法，基类提供两种测试方法
 * RunWithRest(),RunWithVoApi()
 * 例子：
 * com.voyageone.web2.sdk.api.request.BusinessLogGetRequestTest
 *
 * @author aooer 2016/1/21.
 * @version 2.0.0
 * @since 2.0.0
 */
public class ApiTestUtils {

    /* logger */
    private static final Logger LOGGER = Logger.getLogger(ApiTestUtils.class);

    /* serverurl */
    protected static String SERVICE_HOME = "http://localhost:8080/rest";

    /* restTemplate */
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    /* voApiClient */
    private static final VoApiDefaultClient VOAIP_CLIENT = new VoApiDefaultClient(REST_TEMPLATE, SERVICE_HOME);

    /**
     * 使用RestTemplate进行测试
     */
    public static void RunWithRest(VoApiRequest request) {
        try {
            VoApiResponse voApiResponse = restProcess(validateRequest(request));
            if(LOGGER.isInfoEnabled())
                LOGGER.info(new BeanMap(voApiResponse));
            else
                System.out.println(new BeanMap(voApiResponse));
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    /**
     * 使用voApiClient进行测试
     */
    public static void RunWithVoApi(VoApiRequest request) {
        try {
            VoApiResponse voApiResponse = voApiProcess(validateRequest(request));
            if(LOGGER.isInfoEnabled())
                LOGGER.info(new BeanMap(voApiResponse));
            else
                System.out.println(new BeanMap(voApiResponse));
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    /**
     * 参数校验
     *
     * @param request VoApiRequest
     */
    private static VoApiRequest validateRequest(VoApiRequest request) {
        LOGGER.info("当前测试Request类型：" + request.getClass() + "\tResponse类型：" + request.getResponseClass());
        request.check();
        return request;
    }

    /**
     * rest处理过程
     *
     * @param request 请求对象
     * @param <E>     响应对象response
     * @return 响应对象response
     */
    private static <E extends VoApiResponse> E restProcess(VoApiRequest<E> request) {
        RequestEntity<VoApiRequest> requestEntity = new RequestEntity<VoApiRequest>(request, request.getHeaders(), request.getHttpMethod(),
                URI.create(SERVICE_HOME + request.getApiURLPath()));
        ResponseEntity<E> responseEntity = REST_TEMPLATE.exchange(requestEntity, request.getResponseClass());
        return responseEntity.getBody();
    }

    /**
     * Vo Api处理过程
     *
     * @param request 请求参数
     * @return 响应
     */
    private static <E extends VoApiResponse> E voApiProcess(VoApiRequest<E> request) {
        return VOAIP_CLIENT.execute(request);
    }

}
