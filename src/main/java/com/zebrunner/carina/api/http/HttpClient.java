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

import com.zebrunner.carina.proxy.SystemProxy;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/*
 * HttpClient - sends HTTP request with specified parameters and returns response.
 * 
 * @author Alex Khursevich
 */
public class HttpClient {
    public static Response send(RequestSpecification request, String methodPath, HttpMethodType methodType) {
        Response response = null;
        SystemProxy.setupProxy();
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
            throw new RuntimeException("MethodType is not specified for the API method: " + methodPath);
        }

        return response;
    }

}
