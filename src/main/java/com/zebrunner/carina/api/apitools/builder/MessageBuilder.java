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
package com.zebrunner.carina.api.apitools.builder;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.util.Properties;

public class MessageBuilder {

    private static final Configuration FREEMARKER_CONFIGURATION;

    static {
        FREEMARKER_CONFIGURATION = new Configuration(Configuration.VERSION_2_3_0);
        FREEMARKER_CONFIGURATION.setTemplateLoader(new ClassTemplateLoader(MessageBuilder.class, "/"));
    }

    private MessageBuilder() {
        // hide
    }

    public static synchronized String buildStringMessage(String templatePath, Properties... propertiesArr) {
        try (StringWriter sw = new StringWriter()) {
            Template template = FREEMARKER_CONFIGURATION.getTemplate(templatePath);
            Properties resultProperties = new Properties();
            for (Properties properties : propertiesArr) {
                resultProperties.putAll(properties);
            }
            template.process(resultProperties, sw);
            return sw.getBuffer().toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
