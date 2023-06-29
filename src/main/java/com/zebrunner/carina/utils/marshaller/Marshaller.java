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
package com.zebrunner.carina.utils.marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

class Marshaller {

    private static Marshaller instance;

    private final Map<Class<?>, JAXBContext> contextCache;

    /**
     * Class Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private Marshaller() {

        contextCache = new HashMap<>();
    }

    /**
     * Get instance for Marshaller
     *
     * @return Marshaller
     */
    static Marshaller getInstance() {
        if (null == instance) {
            instance = new Marshaller();
        }
        return instance;
    }

    /**
     * Returns cached JAXBContext for specified class
     * 
     * @param clazz
     *            - Class
     * @return - JAXBContext for specified class
     */
    private JAXBContext getJAXBContext(Class<?> clazz) {
        try {
            if (!contextCache.containsKey(clazz)) {
                JAXBContext context = JAXBContext.newInstance(clazz);
                contextCache.put(clazz, context);
                return context;

            }
            return contextCache.get(clazz);
        } catch (JAXBException e) {
            LOGGER.error("Cant create new instance of JAXBContext!", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns Marshaller from JAXBContext for specified class
     * 
     * @param clazz
     *            - Class
     * @return - Marshaller
     */
    private javax.xml.bind.Marshaller getMarshaller(Class<?> clazz) {
        try {

            return getJAXBContext(clazz)
                    .createMarshaller();

        } catch (JAXBException e) {
            LOGGER.error("Error while creating marshaller!", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns Unmarshaller from JAXBContext for specified class
     * 
     * @param clazz
     *            - Class
     * @return - Unmarshaller
     */
    private javax.xml.bind.Unmarshaller getUnmarshaller(Class<?> clazz) {
        try {

            return getJAXBContext(clazz)
                    .createUnmarshaller();

        } catch (JAXBException e) {
            LOGGER.error("Error while creating unmarshaller!", e);
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T unmarshall(Source source, Class<T> resultClazz) {
        try {

            return (T) getUnmarshaller(resultClazz).unmarshal(source);
        } catch (JAXBException e) {
            LOGGER.error("Error while unmarshalling!", e);
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T unmarshall(String string, Class<T> resultClazz) {
        try {

            return (T) getUnmarshaller(resultClazz).unmarshal(new ByteArrayInputStream(string.getBytes()));
        } catch (JAXBException e) {
            LOGGER.error("Error while unmarshalling!", e);
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T unmarshall(File file, Class<T> resultClazz) {
        try {
            return (T) getUnmarshaller(resultClazz).unmarshal(file);
        } catch (JAXBException e) {
            LOGGER.error("Error while unmarshalling!", e);
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T unmarshall(InputStream is, Class<T> resultClazz) {
        try {
            return (T) getUnmarshaller(resultClazz).unmarshal(is);
        } catch (JAXBException e) {
            LOGGER.error("Error while unmarshalling", e);
            throw new RuntimeException(e);
        }
    }

    public void marshall(Object jaxbElement, Result paramResult) {
        try {
            getMarshaller(jaxbElement.getClass()).marshal(jaxbElement,
                    paramResult);
        } catch (JAXBException e) {
            LOGGER.error("Error while marshalling!", e);
            throw new RuntimeException(e);
        }

    }

    public void marshall(Object jaxbElement, Writer writer) {
        try {
            getMarshaller(jaxbElement.getClass()).marshal(jaxbElement, writer);
        } catch (JAXBException e) {
            LOGGER.error("Error while marshalling!", e);
            throw new RuntimeException(e);
        }

    }

    public String marshall(Object jaxbElement) {
        try {
            final StringWriter w = new StringWriter();
            getMarshaller(jaxbElement.getClass()).marshal(jaxbElement, w);
            return w.toString();
        } catch (JAXBException e) {
            LOGGER.error("Error while marshalling!", e);
            throw new RuntimeException(e);
        }

    }
}
