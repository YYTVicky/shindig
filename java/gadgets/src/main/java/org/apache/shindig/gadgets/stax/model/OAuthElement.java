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

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.spec.SpecParserException;

public abstract class OAuthElement extends SpecElement {

  public static final String ATTR_URL = "url";
  public static final String ATTR_METHOD = "method";
  public static final String ATTR_PARAM_LOCATION = "param_location";

  private boolean request = false;

  protected OAuthElement(final QName name, final Map<String, QName> attrNames,
      final Uri base, boolean request) {
    super(name, attrNames, base);
    this.request = request;
  }

  public boolean isRequest() {
    return request;
  }

  public Uri getUrl() {
    return attrUriNull(ATTR_URL);
  }

  public Method getMethod() {
    return Method.parse(attr(ATTR_METHOD));
  }

  public Location getParamLocation() {
    return Location.parse(attr(ATTR_PARAM_LOCATION));
  }

  @Override
  protected void writeAttributes(final XMLStreamWriter writer)
      throws XMLStreamException {
    final String namespaceURI = name().getNamespaceURI();
    if (getUrl() != null) {
      writer.writeAttribute(namespaceURI, ATTR_URL, getUrl().toString());
    }
    if (attr(ATTR_METHOD) != null) {
      writer.writeAttribute(namespaceURI, ATTR_METHOD, getMethod().toString());
    }
    if (attr(ATTR_PARAM_LOCATION) != null) {
      writer.writeAttribute(namespaceURI, ATTR_PARAM_LOCATION,
          getParamLocation().toString());
    }
  }

  @Override
  public void validate() throws SpecParserException {
    if (getUrl() == null) {
      throw new SpecParserException(name().getLocalPart() + "@url must be set!");
    }
    if ((getMethod() == Method.GET) && (getParamLocation() == Location.BODY)) {
      throw new SpecParserException(name().getLocalPart()
          + "@method is GET but parameter location is body!");
    }
  }

  public static enum Method {
    GET, POST;

    public static Method parse(String value) {
      for (Method method : Method.values()) {
        if (StringUtils.equalsIgnoreCase(method.toString(), value)) {
          return method;
        }
      }
      return GET;
    }
  }

  public static enum Location {
    HEADER, URL, BODY;

    @Override
    public String toString() {
      return name().toLowerCase();
    }

    public static Location parse(String value) {
      for (Location location : Location.values()) {
        if (StringUtils.equalsIgnoreCase(location.toString(), value)) {
          return location;
        }
      }
      return HEADER;
    }
  }

  public static abstract class Parser extends SpecElement.Parser<OAuthElement> {

    public Parser(final QName name, final Uri base) {
      super(name, base);
      register(ATTR_URL, ATTR_METHOD, ATTR_PARAM_LOCATION);
    }

    @Override
    protected abstract OAuthElement newElement();
  }
}
