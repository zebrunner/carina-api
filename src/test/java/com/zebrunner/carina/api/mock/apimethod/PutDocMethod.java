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
package com.zebrunner.carina.api.mock.apimethod;

import com.zebrunner.carina.api.AbstractApiMethodV2;

@Deprecated
public class PutDocMethod extends AbstractApiMethodV2 {

	private String baseURL = "";
	
    public PutDocMethod() {
        super(null, null);
        replaceUrlPlaceholder("base_url", baseURL);
        request.header("Content-Type", "text/plain");
        request.header("LCDOC_TYPE", "test");
        request.body("Test Core Client");
    }

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
}
