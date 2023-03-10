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

/*
 * HTTP Response status.
 *
 * @author Aaron Davis
 */
public final class HttpResponseStatus {

    private final int code;
    private final String message;

    public HttpResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpResponseStatus withMessageOverride(String messageOverride) {
        if (null == messageOverride || messageOverride.isEmpty() || messageOverride.equals(message)) {
            return this;
        } else {
            return new HttpResponseStatus(code, messageOverride);
        }
    }
}
