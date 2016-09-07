package com.voyageone.web2.openapi.channeladvisor.service.sandbox;

import com.voyageone.common.util.JacksonUtil;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Service
class JsonResourcesService implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    <T> List<T> getResourceData(String className, String methodName, Class<T> resultClass) {
        String location = "classpath:" + className.replaceAll("\\.", "/") + "_" + methodName + ".json";
        String dataStr = getResourceData(location);
        return JacksonUtil.jsonToBeanList(dataStr, resultClass);
    }

    private String getResourceData(String location) {
        StringBuilder result = new StringBuilder();
        Resource resource = resourceLoader.getResource(location);
        try {
            InputStream is = resource.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            br.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result.toString();
    }
}
