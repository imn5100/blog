package com.shaw.util.smms;

import com.shaw.constants.Constants;
import com.shaw.util.HttpClientUtils;
import com.shaw.util.PropertiesUtil;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by shaw on 2016/11/16 0016.
 */
public class SMMSUtils {
    private static RestTemplate restTemplate;
    public static final String UPLOAD_URL = PropertiesUtil.getConfiguration().getString(Constants.QINIU_KEY_KEY);

    static {
        HttpComponentsClientHttpRequestFactory requestFactory = null;
        try {
            requestFactory = new HttpComponentsClientHttpRequestFactory(HttpClientUtils.acceptsUntrustedCertsHttpClient());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        requestFactory.setReadTimeout(Constants.HTTP_READ_TIMEOUT);
        requestFactory.setConnectTimeout(Constants.HTTP_CONNECT_TIMEOUT);
        restTemplate = new RestTemplate(requestFactory);
    }

    public static Object uploadFile(File file) {
        FileSystemResource resource = new FileSystemResource(file);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("smfile", resource);
        String response = restTemplate.postForObject(UPLOAD_URL, param, String.class);
        return response;
    }


    public static void main(String[] args) {
        uploadFile(new File("E:/imageDownLoad/13531995.png"));
    }
}
