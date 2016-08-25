package com.voyageone.service.daoext.com;

import com.voyageone.service.model.openapi.oauth.ComOpenApiClientModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComOpenApiClientDaoExt {

    ComOpenApiClientModel selectByClientSecret(@Param("clientId") String clientId, @Param("clientSecret") String clientSecret);

}
