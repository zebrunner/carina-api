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
package com.zebrunner.carina.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.zebrunner.carina.utils.exception.JsonParseException;

import java.io.File;
import java.lang.reflect.Type;

/**
 * Created by yauhenipatotski on 4/13/16.
 */
public final class JsonUtils {

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
    }

    private JsonUtils() {

    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return mapper.readValue(json, classOfT);
        } catch (Exception e) {
            throw new JsonParseException(e.getMessage(), e);
        }
    }

    public static String toJson(Object src) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(src);
        } catch (Exception e) {
            throw new JsonParseException(e.getMessage(), e);
        }
    }

    public static <T> T fromJson(File file, Class<T> classOfT) {
        try {
            return mapper.readValue(file, classOfT);
        } catch (Exception e) {
            throw new JsonParseException(e.getMessage(), e);
        }
    }

    public static <T> T fromJson(File file, Type type) {
        try {
            TypeFactory tf = mapper.getTypeFactory();
            JavaType javaType = tf.constructType(type);
            return mapper.readValue(file, javaType);
        } catch (Exception e) {
            throw new JsonParseException(e.getMessage(), e);
        }
    }

    public static <T> T fromJson(String json, Type type) {
        try {
            TypeFactory tf = mapper.getTypeFactory();
            JavaType javaType = tf.constructType(type);
            return mapper.readValue(json, javaType);
        } catch (Exception e) {
            throw new JsonParseException(e.getMessage(), e);
        }
    }

    public static JsonNode readTree(String content) {
        try {
            return mapper.readTree(content);
        } catch (Exception e) {
            throw new JsonParseException(e.getMessage(), e);
        }
    }

    public static <T> T treeToValue(JsonNode node, Class<? extends T> type) {
        try {
            return mapper.treeToValue(node, type);
        } catch (Exception e) {
            throw new JsonParseException(e.getMessage(), e);
        }
    }
}
