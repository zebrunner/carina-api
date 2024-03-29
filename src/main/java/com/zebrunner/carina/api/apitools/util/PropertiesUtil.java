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
package com.zebrunner.carina.api.apitools.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

public class PropertiesUtil {

    private PropertiesUtil() {
        //hide
    }

    public static Properties readProperties(String path) {
        Properties prop = new Properties();
        try {
            prop.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(path));
        } catch (Exception e) {
            throw new RuntimeException("Can't read properties from file", e);
        }
        return prop;
    }
}
