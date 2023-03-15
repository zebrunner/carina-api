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
package com.zebrunner.carina.api.log;

import org.hamcrest.Matcher;

import java.io.PrintStream;
import java.util.Set;

import io.restassured.filter.FilterContext;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.internal.print.ResponsePrinter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class CarinaResponseHeadersLoggingFilter extends ResponseLoggingFilter {

    private static final LogDetail LOG_DETAIL = LogDetail.HEADERS;

    private final PrintStream stream;
    private final Matcher<?> matcher;
    private final boolean shouldPrettyPrint;
    private final Set<String> blacklistedHeaders;

    public CarinaResponseHeadersLoggingFilter(boolean prettyPrint, PrintStream stream, Matcher<Integer> matcher, Set<String> blacklistedHeaders) {
        super(LOG_DETAIL, prettyPrint, stream, matcher);
        this.stream = stream;
        this.matcher = matcher;
        this.shouldPrettyPrint = prettyPrint;
        this.blacklistedHeaders = blacklistedHeaders;
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        final int statusCode = response.statusCode();
        if (matcher.matches(statusCode)) {
            ResponsePrinter.print(response, response, stream, LOG_DETAIL, shouldPrettyPrint, blacklistedHeaders);
        }
        return response;
    }
}
