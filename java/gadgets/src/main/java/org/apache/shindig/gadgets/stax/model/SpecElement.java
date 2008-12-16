package org.apache.shindig.gadgets.stax.model;
/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */


import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.shindig.gadgets.spec.SpecParserException;

public interface SpecElement {

    void seal();

    void setAttribute(final QName name, final String value);

    void addNamespace(final String prefix, final String uri);

    void addChild(final SpecElement element);

    void toXml(final XMLStreamWriter writer) throws XMLStreamException;

    public static abstract class Parser<T extends SpecElement> {

        private static final Logger LOG = Logger.getLogger(SpecElement.class.getName());

        private final Map<QName, Parser<? extends SpecElement>> children = new HashMap<QName, Parser<? extends SpecElement>>();

        private final QName name;

        protected Parser(final QName name) {
            this.name = name;
        }

        public QName getName() {
            return name;
        }

        protected void register(Parser<? extends SpecElement> parseElement) {
            children.put(parseElement.getName(), parseElement);
        }

        public T parse(final XMLStreamReader reader) throws IllegalStateException, XMLStreamException, SpecParserException {

            // This assumes, that parse it at the right element.
            T element = newElement();
            addAttributes(reader, element);
            addNamespaces(reader, element);

            while(true) {
                int event = reader.next();

                switch (event) {
                case XMLStreamConstants.ATTRIBUTE:
                    addAttributes(reader, element);
                    break;
                case XMLStreamConstants.END_ELEMENT:
                case XMLStreamConstants.END_DOCUMENT:
                    element.seal();
                    return element;

                case XMLStreamConstants.START_ELEMENT:
                    final QName elementName = reader.getName();
                    if (children.containsKey(elementName)) {
                        final SpecElement child = children.get(elementName).parse(reader);
                        addChild(reader, element, child);
                    } else {// Ignore non-defined children. TODO: Does that make sense?
                        LOG.warning("No idea what to do with " + elementName + ", ignoring!");
                        final SpecElement child = new GenericElement.Parser(elementName).parse(reader);

                        element.addChild(child);
                    }
                    break;
                default:
                    break; // TODO: Do we need to parse more things?
                }
            }
        }

        protected abstract T newElement();

        protected void addAttributes(final XMLStreamReader reader, final T element) {
            for (int i=0; i < reader.getAttributeCount(); i++) {
                element.setAttribute(reader.getAttributeName(i), reader.getAttributeValue(i));
            }
        }

        protected void addNamespaces(final XMLStreamReader reader, final T element) {
            for (int i=0; i < reader.getNamespaceCount(); i++) {
                element.addNamespace(reader.getNamespacePrefix(i), reader.getNamespaceURI(i));
            }
        }

        protected abstract void addChild(final XMLStreamReader reader, final T element, final SpecElement child);

        public abstract void validate(final T element) throws SpecParserException;
    }

}
