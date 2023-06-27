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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.lang.invoke.MethodHandles;

public final class XmlFormatter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private XmlFormatter() {
    }

    public static String prettyPrint(String xml) {
        if (StringUtils.isEmpty(xml))
            return xml;

        try {
            final InputSource src = new InputSource(new StringReader(xml));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // to be compliant, completely disable DOCTYPE declaration:
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            final Node document = factory.newDocumentBuilder().parse(src).getDocumentElement();
            final boolean keepDeclaration = xml.startsWith("<?xml");

            final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            final LSSerializer writer = impl.createLSSerializer();

            writer.getDomConfig().setParameter("format-pretty-print", true);
            writer.getDomConfig().setParameter("xml-declaration", keepDeclaration);

            return writer.writeToString(document);
        } catch (Exception e) {
            LOGGER.error("Error during adjusting xml!", e);
        }
        return xml; // Return the same string if error take place
    }
}