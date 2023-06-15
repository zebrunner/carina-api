/*******************************************************************************
 * Copyright 2020-2022 Zebrunner Inc (https://www.zebrunner.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.zebrunner.carina.api.http;

import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.commons.SpecialKeywords;
import com.zebrunner.carina.utils.exception.InvalidConfigurationException;
import io.restassured.response.Response;
import io.restassured.specification.ProxySpecification;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/*
 * HttpClient - sends HTTP request with specified parameters and returns response.
 *
 * @author Alex Khursevich
 */
public class HttpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static Response send(RequestSpecification request, String methodPath, HttpMethodType methodType) {
        Response response;
        addProxy(request);
        // SystemProxy.setupProxy();
        switch (methodType) {
        case HEAD:
            response = request.head(methodPath);
            break;
        case GET:
            response = request.get(methodPath);
            break;
        case PUT:
            response = request.put(methodPath);
            break;
        case POST:
            response = request.post(methodPath);
            break;
        case DELETE:
            response = request.delete(methodPath);
            break;
        case PATCH:
            response = request.patch(methodPath);
            break;
        case OPTIONS:
            response = request.options(methodPath);
            break;
        default:
            throw new IllegalArgumentException("MethodType is not specified for the API method: " + methodPath);
        }

        return response;
    }

    private static void addProxy(RequestSpecification requestSpecification) {
        if (((QueryableRequestSpecification) requestSpecification).getProxySpecification() != null) {
            LOGGER.debug("Request specification already contains proxy specification, so we will not add global proxy settings.");
            return;
        }
        boolean isEnabled = Boolean.parseBoolean(getConfigurationValue("carina.api.proxy.enable"));
        if (!isEnabled) {
            return;
        }

        String proxyHost = getConfigurationValue("carina.api.proxy.host");
        Integer proxyPort = !getConfigurationValue("carina.api.proxy.port").isEmpty() ?
                Integer.parseInt(getConfigurationValue("carina.api.proxy.port")) : null;

        String proxyUsername = getConfigurationValue("carina.api.proxy.username");
        String proxyPassword = getConfigurationValue("carina.api.proxy.password");

        if (proxyHost.isEmpty() || proxyPort == null) {
            throw new InvalidConfigurationException(
                    "Cannot create proxy for API. 'carina.api.proxy.host' or 'proxy_port' or 'carina.api.proxy.port' are empty.");
        }

        ProxySpecification proxySpecification = ProxySpecification.host(proxyHost)
                .withPort(proxyPort);
        if (!proxyUsername.isEmpty() && !proxyPassword.isEmpty()) {
            proxySpecification.withAuth(proxyUsername, proxyPassword);
        }
        requestSpecification.proxy(proxySpecification);
    }

    // todo remove when params will be added to the Configuration class
    private static String getConfigurationValue(String param) {
        String value = R.CONFIG.get(param);
        return !(value == null || value.equalsIgnoreCase(SpecialKeywords.NULL)) ? value : StringUtils.EMPTY;
    }

}
