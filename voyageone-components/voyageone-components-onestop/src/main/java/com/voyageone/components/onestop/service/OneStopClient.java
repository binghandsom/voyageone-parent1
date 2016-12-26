package com.voyageone.components.onestop.service;


import com.google.common.net.MediaType;
import com.google.gson.reflect.TypeToken;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.components.onestop.bean.*;
import com.voyageone.components.onestop.util.OneStopHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * OneStop的API客户端,shutdown需要在引用此类完成后调用.关闭一些可能的资源.如果是Spring管理的bean
 * 可以这样用
 *
 * @PreDestroy void shutdown(){
 * client.shutdown();
 * }
 * @description
 * @author: holysky.zhao
 * @date: 2016/11/18 18:45
 * @version:1.0.0 COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class OneStopClient {
    private static final Logger logger = LoggerFactory.getLogger(OneStopClient.class);
    OneStopConfig config = null;
    //thread safe
    AtomicReference<OAuthJSONAccessTokenResponse> _accessTokenResp = new AtomicReference<>();
    private static final OAuthClient _httpclient = new OAuthClient(new OneStopHttpClient());

    private Thread _refreashTokenThread = new Thread(() -> { //自动刷新token的守护线程
        try {
            while (true) {
                TimeUnit.SECONDS.sleep( 60 * 50);
                getToken();
            }
        } catch (Exception e) {
            logger.error("demo thread refresh token error", e);
        }
    },"OneStopClient_RefreashTokenThread");

    public OneStopClient(OneStopConfig oauth2Req) {
        this.config = oauth2Req;
        getToken();
        _refreashTokenThread.setDaemon(true);
        _refreashTokenThread.start();
    }

    /**
     * 请调用此方法释放资源.
     */
    public void shutdown() {
        logger.info(OneStopClient.class.getSimpleName()+" will shutdown !");
        _httpclient.shutdown();
    }


    /**
     * 获取QA环境的oauth2验证req
     *
     * @return
     */
    public static OneStopConfig getConfigForQA() {
        try {
            OAuthClientRequest req = OAuthClientRequest
                    .tokenLocation("https://admin.qa.onestop.com//identity/connect/token")
                    .setScope("osapi")
                    .setGrantType(GrantType.CLIENT_CREDENTIALS)
                    .setClientId("acmeapi")
                    .setClientSecret("kpKFz79Thm2sJ8D5ZbOpAj8xgYSBj6Qb").buildBodyMessage();

            return new OneStopConfig("https://apiv2.qa.onestop.com/osapi", req);
        } catch (OAuthSystemException e) {
            throw new OneStopApiException("build onestop oauth2 req error", e);
        }
    }

    public static OneStopConfig getConfigFromDb(String channelId) {

        checkArgument(channelId != null, "channel must not null");

        String clientId = ThirdPartyConfigs.getVal1(channelId, "clientId");
        String clientSecret = ThirdPartyConfigs.getVal1(channelId, "clientSecret");
        String authServerUrl = ThirdPartyConfigs.getVal1(channelId, "authServerUrl");
        String scope = ThirdPartyConfigs.getVal1(channelId, "scope");
        String baseURI = ThirdPartyConfigs.getVal1(channelId, "baseURI");
        checkArgument(StringUtils.isNotEmpty(clientId),"clientId must not empty");
        checkArgument(StringUtils.isNotEmpty(clientSecret),"clientSecret must not empty");
        checkArgument(StringUtils.isNotEmpty(authServerUrl),"authServerUrl must not empty");
        checkArgument(StringUtils.isNotEmpty(scope),"scope must not empty");
        checkArgument(StringUtils.isNotEmpty(scope),"scope must not empty");
        checkArgument(StringUtils.isNotEmpty(baseURI),"baseURI must not empty");
        try {
            OAuthClientRequest req = OAuthClientRequest
                    .tokenLocation(authServerUrl)
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .setScope(scope)
                    .setGrantType(GrantType.CLIENT_CREDENTIALS)
                    .buildBodyMessage();
            return new OneStopConfig(baseURI,req);
        } catch (OAuthSystemException e) {
            throw new OneStopApiException("Can not build oauth2 request", e);
        }
//        OneStopConfig config = new OneStopConfig();
    }
    /**
     * 从服务端获取token
     *
     * @return
     */
    private void getToken() {
        try {
            OAuthJSONAccessTokenResponse tokenResp = _httpclient.accessToken(config.getOauth2Config());
            this._accessTokenResp.set(tokenResp);
            logger.info("onestop get access token :"+tokenResp.getAccessToken()+" expired after:"+
                    tokenResp.getExpiresIn()+" s");
        } catch (Exception e) {
            throw new OneStopApiException("get onestop api access_token error", e);
        }
    }

    /**
     * 在200,正常返回.404并且body为空返回时表示资源未找到,其他情况返回异常
     * get方法均需要判断是否404的情况
     *
     * @param uri
     * @return
     */
    protected OAuthClientJSONResponse get(String uri) {
        OAuthClientJSONResponse resource = resource(uri, OAuth.HttpMethod.GET, "");
        if (resource.getResponseCode() == HttpStatus.SC_FORBIDDEN) {
            logger.info("access_token is invalid, refresh token and retry");
            getToken();
            resource = resource(uri, OAuth.HttpMethod.GET, "");
        }
        if (resource.getResponseCode() == HttpStatus.SC_OK) {
            return resource;
        }
        //TODO add more robust error handling
        throw new OneStopApiException("get http status_code is not 200:" + resource.getResponseCode());
    }

    /**
     * post方法返回201表示成功,其他的都当做失败
     * @param uri
     * @return
     */
    protected OAuthClientJSONResponse post(String uri,String body) {
        OAuthClientJSONResponse resource = resource(uri, OAuth.HttpMethod.POST, body);
        if (resource.getResponseCode() == HttpStatus.SC_FORBIDDEN) {
            logger.info("access_token is invalid, refresh token and retry");
            getToken();
            resource = resource(uri, OAuth.HttpMethod.POST, body);
        }
        if (resource.getResponseCode() == HttpStatus.SC_FORBIDDEN) {
            throw new OneStopApiException("unauthorized access error");
        } else if (resource.getResponseCode() == HttpStatus.SC_CREATED) {
            return resource;
        } else if (resource.getResponseCode() == HttpStatus.SC_BAD_REQUEST) {
            throw new OneStopBizException(resource.getBody());
        } else {
            //TODO add more robust error handling
            throw new OneStopApiException("post http status_code is not 201:" + resource.getResponseCode());
        }
    }

    /**
     * put方法返回201和202表示成功.均失败.
     * @param uri
     * @return
     */
    protected OAuthClientJSONResponse put(String uri,String body) {

        OAuthClientJSONResponse resource = resource(uri, OAuth.HttpMethod.PUT, body);
        if (resource.getResponseCode() == HttpStatus.SC_FORBIDDEN) {
            //Attempt token refresh
            logger.info("access_token is invalid, refresh token and retry");
            getToken();
            resource(uri, OAuth.HttpMethod.PUT, body);
        }
        if (resource.getResponseCode() == HttpStatus.SC_FORBIDDEN) {
            throw new OneStopApiException("unauthorized access error");
        } else if (resource.getResponseCode() == HttpStatus.SC_CREATED || resource.getResponseCode() == HttpStatus.SC_ACCEPTED) {
            return resource;
        }else if (resource.getResponseCode() == HttpStatus.SC_BAD_REQUEST) {
            throw new OneStopBizException(resource.getBody());
        }
        else {
            //todo: add more robust error handling (for example return validation errors from response body, etc.)
            throw new OneStopApiException("put http status_code is not in (201,202):" + resource.getResponseCode());
        }
    }

    /**
     * 底层核心方法,检查必要参数,post和put方法设置参数头,bodyMsg可以为"",如果通信正常返回OAuthClientJSONResponse对象,
     * 带有状态吗和其他必要信息,get,post,和put方法依据此对象进行合理判断,其他通信不正常的情况下抛出异常
     * @param uri
     * @param requestMethod
     * @param bodyMsg
     * @return
     */
    private OAuthClientJSONResponse resource(String uri, String requestMethod, String bodyMsg) {
        checkArgument(StringUtils.isNotBlank(uri), "uri can not empty");
        checkArgument(StringUtils.isNotBlank(requestMethod), "requestMethod can not empty");
        OAuthClientRequest req = null;
        try {
            String realUrl = (!config.getBaseUrl().endsWith("/") ? config.getBaseUrl() + "/" : config.getBaseUrl())
                    + (uri.startsWith("/") ? uri.substring(1) : uri);
            req = new OAuthBearerClientRequest( realUrl)
                    .setAccessToken(this._accessTokenResp.get().getAccessToken())
                    .buildHeaderMessage();
            if (HttpMethod.POST.equals(requestMethod) || HttpMethod.PUT.equals(requestMethod)) {
                req.setHeader(OAuth.HeaderType.CONTENT_TYPE, MediaType.JSON_UTF_8.toString());
            }
            req.setBody(bodyMsg);
        } catch (OAuthSystemException e) {
            throw new OneStopApiException(String.format("build onestop api uri(%s)  request error", uri), e);
        }
        try {
            OAuthClientJSONResponse result = _httpclient.resource(req, requestMethod, OAuthClientJSONResponse.class);
            return result;
//
        } catch (OAuthSystemException e) {
            if (OAuth.HttpMethod.GET.equals(requestMethod) && e.getCause() instanceof FileNotFoundException) {
                //404
                throw new OneStopResouceNotFoundException(e.getMessage(), e);
            }else{
                throw new OneStopApiException(String.format("query onestop api uri(%s)  error", uri), e);
            }
        }catch (Exception e) {
            throw new OneStopApiException(String.format("query onestop api uri(%s)  error", uri), e);
        }
    }

    //静态URL定义
    public static final String URL_CATALOG = "/api/catalog/%s";
    public static final String URL_CATALOG_PRODUCTID = "/api/catalog/productid";

    /**
     * 这就是封装示例
     * @Modular Catalog
     * @param productId
     *  Gets an Order object by order Id
     */
    public OneStopProduct catalog(Long productId) {
        checkArgument(productId != null, "productId must not null");

        try {
            OAuthClientJSONResponse response = get(String.format(URL_CATALOG, productId));
            return response.getBodyAs(OneStopProduct.class);
        } catch (OneStopResouceNotFoundException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * @Modular Catalog
     * @param lastModDate
     *  Get a list of product Ids by last modified date
     * */
    public List<Long> catalogProductId(Date lastModDate) {
        checkArgument(lastModDate!=null,"lastModDate must not null");

        String lastModDateStr = new DateTime(lastModDate).toString("yyyy-MM-dd HH:mm:ss");
        try{
            String url = new URIBuilder(URL_CATALOG_PRODUCTID).addParameter("lastModDate", lastModDateStr)
                    .build().toString();
            OAuthClientJSONResponse response = get(url);
            return response.getBodyAs(new TypeToken<List<Long>>() {
            }.getType());
        }catch (OneStopResouceNotFoundException e) {
            logger.error(e.getMessage());
            return Collections.EMPTY_LIST;//返回集合对象的方法不要返回null
        } catch (URISyntaxException e) {
            throw new OneStopApiException("url error");
        }
    }


    /**
     * @Modular Facility
     * @param facilityId
     * Gets a list of inventory by status and location a link
     */
    //URL定义
    public static final String URL_FacilityId = "/api/Facility/%s/Inventory";

    public List<OneStopInventory> FacilityId(Integer facilityId){
        try{

            checkArgument(facilityId != null, "facilityId must not null");
            OAuthClientJSONResponse response =get(String.format(URL_FacilityId, facilityId));
            Type listType = new TypeToken<ArrayList<OneStopInventory>>(){}.getType();
            return response.getBodyAs(listType);
        }catch (OneStopResouceNotFoundException e) {
            logger.error(e.getMessage());
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * @Modular Facility
     * @param  upc facilityIdUpc
     * Gets a list of inventory by UPC, status and location
     */
    //URL定义
    public static final String URL_FacilityIdUpc = "/api/Facility/";

    public List<OneStopInventory> FacilityIdUpc(Integer facilityId,String upc){
        try{

            checkArgument(upc != null, "upc must not null");
            checkArgument(facilityId != null, "facilityId must not null");

            URIBuilder url = new URIBuilder().setPath(URL_FacilityIdUpc+facilityId+"/Inventory/"+upc);
            OAuthClientJSONResponse response = get(url.toString());
            return  response.getBodyAs(new TypeToken<List<OneStopInventory>>() {
            }.getType());
        }catch (OneStopResouceNotFoundException e) {
            logger.error(e.getMessage());
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * @Modular Inventory
     * @param upc
     * Gets a list of inventory by UPC and status
     */
    //URL定义
    public static final String URL_INVENTOY = "/api/Inventory/%s";

    public List<OneStopInventory> inventory(String upc){
        try{
            checkArgument(upc != null, "upc must not null");
            OAuthClientJSONResponse response =get(String.format(URL_INVENTOY, upc));
            Type listType = new TypeToken<ArrayList<OneStopInventory>>(){}.getType();
            return response.getBodyAs(listType);
        }catch (OneStopResouceNotFoundException e) {
            logger.error(e.getMessage());
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * @Modular Order
     * @param id
     * Gets an Order object by order Id
     */
    //URL定义
    public static final String URL_ORDER = "/api/order/%s";

    public OneStopOrder order(Integer id){
        try{

            checkArgument(id != null, "id must not null");
            OAuthClientJSONResponse response =get(String.format(URL_ORDER, id));
            return response.getBodyAs(OneStopOrder.class);
        }catch (OneStopResouceNotFoundException e) {
            logger.error(e.getMessage());
            return null;
        }
    }


    /**
     * @Modular Order
     * @param idList
     * Gets a list of Order objects by order ids
     */
    //URL定义
    public static final String URL_ORDER_IDLIST = "/api/order/%s";

    public List<OneStopOrder> orderlist(String idList){ //例如："765308,765208,765108,765008"
        try{

            checkArgument(idList != null, "idList must not null");
            OAuthClientJSONResponse response =get(String.format(URL_ORDER_IDLIST,idList));
            Type listType = new TypeToken<ArrayList<OneStopOrder>>(){}.getType();
            return response.getBodyAs(listType);
        }catch (OneStopResouceNotFoundException e) {
            logger.error(e.getMessage());
            return Collections.EMPTY_LIST;//返回集合对象的方法不要返回null
        }
    }

    /**
     * @Modular Order
     * @param order
     * Records and processes an order
     */
    //URL定义
    public static final String URL_ORDER_POST = "/api/order";

    public   OneStopOrderPostResp orderPost(OneStopOrder order){
        try {
            checkArgument(order != null, "order must not null");
            OAuthClientJSONResponse response = post(URL_ORDER_POST, JsonUtil.bean2Json(order));
            return response.getBodyAs(OneStopOrderPostResp.class);
        } catch (OneStopResouceNotFoundException e) {
            logger.error(e.getMessage());
            return null;
        } catch (OneStopBizException e) {
            return JsonUtil.jsonToBean(e.getMessage(), OneStopOrderPostResp.class);
        } catch (OneStopApiException e) { //这里就不能return了.需要抛异常
            throw e;
        } catch (Exception e) {
            throw new OneStopApiException("invoke order post error", e);
        }
    }

    /**
     * @Modular orderId
     * @param orderStatus　startDateTime　endDateTime　source
     * Retrieves a list of Order ID values, given an order status and create date range
     */

    //URL定义
    public static final String URL_ORDERID = "/api/orderid/";

    public List<Long> orderId(String orderStatus, Date startDateTime, Date endDateTime, String source) {

        try{
            checkArgument(orderStatus != null, "orderStatus must not null");
            checkArgument(startDateTime != null, "startDateTime must not null");
            checkArgument(endDateTime != null, "endDateTime must not null");

            String startDateTimeStr = new DateTime(startDateTime).toString("yyyy-MM-dd HH:mm:ss");
            String endDateTimeStr = new DateTime(endDateTime).toString("yyyy-MM-dd HH:mm:ss");

            URIBuilder url = new URIBuilder().setPath(URL_ORDERID+orderStatus).addParameter("startDateTime", startDateTimeStr)
                    .addParameter("endDateTime",endDateTimeStr);
            if(source != null && !source.isEmpty())
                url.addParameter("source",source);
            OAuthClientJSONResponse response = get(url.build().toString());
            return  response.getBodyAs(new TypeToken<List<Long>>() {
            }.getType());
        }catch (OneStopResouceNotFoundException e) {
            logger.error(e.getMessage());
            return Collections.EMPTY_LIST;//返回集合对象的方法不要返回null
        }catch (URISyntaxException e) {
            throw new OneStopApiException("url error");
        }

    }

    /**
     * @Modular OrderItems
     * @param id
     * Gets a list of order items associated with an order
     */
    //URL定义
    public static final String URL_ORDER_ITEMS = "/api/Order/%s/items";
    public List<OneStopOrderItem> orderItems(Integer id){
        try{

            checkArgument(id != null, "id must not null");
            OAuthClientJSONResponse response =get(String.format(URL_ORDER_ITEMS, id));
            Type listType = new TypeToken<ArrayList<OneStopOrderItem>>(){}.getType();
            return response.getBodyAs(listType);
        }catch (OneStopResouceNotFoundException e) {
            logger.error(e.getMessage());
            return Collections.EMPTY_LIST;
        }
    }

//    public <T > HttpResponse<T> resource(String uri,String requestMethod, Class<T> responseClass)  {
//        checkArgument(StringUtils.isNotBlank(uri),"uri can not empty");
//        checkArgument(StringUtils.isNotBlank(requestMethod),"requestMethod can not empty");
//        checkArgument(responseClass!=null,"responseClass can not nul");
//
//        String url = config.getBaseUrl()+ uri;
//        try {
//            switch (requestMethod) {
//                case HttpMethod.GET:
//                    return Unirest.get(url)
//                            .header("authorization", "Bearer "+_accessTokenResp.getAccessToken())
//                            .header(OAuth.HeaderType.CONTENT_TYPE, MediaType.JSON_UTF_8.toString())
//                            .asObject(responseClass);
//                case HttpMethod.POST:
//                    return Unirest.post(url)
//                            .header("authorization", "Bearer "+_accessTokenResp.getAccessToken())
//                             .asObject(responseClass);
//                case HttpMethod.PUT:
//                    return Unirest.put(url)
//                            .header("authorization", "Bearer "+_accessTokenResp.getAccessToken())
//                            .asObject(responseClass);
//                default:
//                    throw new OneStopApiException("incorrect http method");
//            }
//        } catch (UnirestException e) {
//            throw new OneStopApiException("invoke rest api error ",e);
//        }
//
//    }

}
