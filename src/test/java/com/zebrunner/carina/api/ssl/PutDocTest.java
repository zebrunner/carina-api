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
package com.zebrunner.carina.api.ssl;

import com.zebrunner.carina.api.http.HttpResponseStatusType;
import com.zebrunner.carina.api.mock.apimethod.PutDocMethod;

@Deprecated
public class PutDocTest {
    // @Test
    public void testPath() {
        PutDocMethod putDocMethod = new PutDocMethod();
        putDocMethod.expectResponseStatus(HttpResponseStatusType.OK_200);
        putDocMethod.setSSLContext(new SSLContextBuilder("src/test/resources/keysecure", true).createSSLContext());
        putDocMethod.callAPI();
    }

    // @Test
    public void testClasspath() {
        PutDocMethod putDocMethod = new PutDocMethod();
        putDocMethod.expectResponseStatus(HttpResponseStatusType.OK_200);
        putDocMethod.setSSLContext(new SSLContextBuilder(true).createSSLContext());
        putDocMethod.callAPI();
    }

    // @Test
    public void testDefaultTLS() {
        PutDocMethod putDocMethod = new PutDocMethod();
        putDocMethod.expectResponseStatus(HttpResponseStatusType.OK_200);
        putDocMethod.setDefaultTLSSupport();
        putDocMethod.callAPI();
    }

    // @Test
    public void testCfgParam() {
        PutDocMethod putDocMethod = new PutDocMethod();
        putDocMethod.expectResponseStatus(HttpResponseStatusType.OK_200);
        putDocMethod.setSSLContext(new SSLContextBuilder(true).createSSLContext());
        putDocMethod.callAPI();
    }
}
