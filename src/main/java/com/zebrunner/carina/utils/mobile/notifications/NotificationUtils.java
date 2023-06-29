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
package com.zebrunner.carina.utils.mobile.notifications;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zebrunner.carina.utils.rest.RestUtil;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;

@Deprecated(forRemoval = true)
public final class NotificationUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String PASSED_MESSAGE = "Call passed with status code '{}'. ";
    private static final String FAILED_MESSAGE = "Call failed with status code '{}'. ";
    private static final String REQUEST_URL_MESSAGE = "Request url: {}";

    private NotificationUtils() {
    }

    /**
     * call Push Service
     *
     * @param contentType String
     * @param parameters Map String, ?
     * @param url String
     * @return JsonObject
     */
    public static JsonObject callPushService(String contentType, Map<String, ?> parameters, String url) {
        return callPushService(contentType, parameters, url, true);
    }

    /**
     * call Push Service
     *
     * @param contentType String
     * @param parameters Map String, ?
     * @param url String
     * @param responseLog boolean
     * @return JsonObject
     */
    public static JsonObject callPushService(String contentType, Map<String, ?> parameters, String url, boolean responseLog) {
        try {
            LOGGER.info(REQUEST_URL_MESSAGE, url);
            Response response = RestUtil.sendHttpPost(contentType, parameters, url, responseLog);

            if (response.getStatusCode() == 200) {
                LOGGER.debug(PASSED_MESSAGE, response.getStatusCode());
                return JsonParser.parseString(response.asString()).getAsJsonObject();
            } else {
                LOGGER.error(FAILED_MESSAGE, response.getStatusCode());
            }

        } catch (Exception e) {
            LOGGER.error("callPushService failure", e);
        }
        return null;
    }

    /**
     * get Push Service Response
     *
     * @param request String
     * @param url String
     * @return JsonObject
     */
    public static JsonObject getPushServiceResponse(String request, String url) {
        return getPushServiceResponse("application/json", request, url, true);
    }

    /**
     * get Push Service Response
     *
     * @param contentType String
     * @param request String
     * @param url String
     * @param responseLog boolean
     * @return JsonObject
     */
    public static JsonObject getPushServiceResponse(String contentType, String request, String url, boolean responseLog) {
        try {

            LOGGER.info(REQUEST_URL_MESSAGE, url);
            Response response = RestUtil.sendHttpPost(contentType, request, url.toString(), responseLog);

            if (response.getStatusCode() == 200) {
                LOGGER.debug(PASSED_MESSAGE, response.getStatusCode());
                return JsonParser.parseString(response.asString()).getAsJsonObject();
            } else {
                LOGGER.error(FAILED_MESSAGE, response.getStatusCode());
            }

        } catch (Exception e) {
            LOGGER.error("getPushServiceResponse failure", e);
        }
        return null;
    }

    /**
     * get Get Service Response
     *
     * @param url String
     * @return JsonObject
     */
    public static JsonObject getGetServiceResponse(String url) {
        return getGetServiceResponse("application/json", url, true);
    }

    /**
     * get Get Service Response
     *
     * @param contentType String
     * @param url String
     * @param responseLog boolean
     * @return JsonObject
     */
    public static JsonObject getGetServiceResponse(String contentType, String url, boolean responseLog) {
        try {

            LOGGER.info(REQUEST_URL_MESSAGE, url);
            Response response = RestUtil.sendHttpGet(contentType, url, responseLog);

            if (response.getStatusCode() == 200) {
                LOGGER.debug(PASSED_MESSAGE, response.getStatusCode());
                return JsonParser.parseString(response.asString()).getAsJsonObject();
            } else {
                LOGGER.error(FAILED_MESSAGE, response.getStatusCode());
            }

        } catch (Exception e) {
            LOGGER.error("getGetServiceResponse failure", e);
        }
        return null;
    }
}
