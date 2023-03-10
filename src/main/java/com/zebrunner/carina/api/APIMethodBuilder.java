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
package com.zebrunner.carina.api;

import com.zebrunner.carina.utils.report.ReportContext;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.util.UUID;

public class APIMethodBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final File temp;
    private final PrintStream ps;

    public APIMethodBuilder() {
        temp = new File(String.format("%s/%s.tmp", ReportContext.getTempDir().getAbsolutePath(), UUID.randomUUID()));
        try {
            ps = new PrintStream(temp);
        } catch (FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }
    }

    public <T extends AbstractApiMethod> T build(T method) {
        method.getRequest().filter(new RequestLoggingFilter(ps)).filter(new ResponseLoggingFilter(ps));
        return method;
    }

    public File getTempFile() {
        return temp;
    }

    public void close() {
        try {
            ps.close();
            Files.delete(temp.toPath());
        } catch (Exception e) {
            LOGGER.error("Error when called close method of APIMethodBuilder class.", e);
        }
    }
}
