/*
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
 */
package org.apache.shindig.gadgets;

import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.stax.StaxMessageBundleFactory;
import org.apache.shindig.gadgets.stax.StaxTestUtils;
import org.apache.shindig.gadgets.stax.model.LocaleSpec;
import org.apache.shindig.gadgets.stax.model.ShindigGadgetSpec;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for Gadget
 */
public class GadgetTest {
  private final static String SPEC_URL = "http://example.org/gadget.xml";
  private final static String SPEC_XML
      = "<Module>" +
        "<ModulePrefs title='title'>" +
        "  <Preload href='http://example.org/foo'/>" +
        "  <Locale>" +
        "    <msg name='name'>VALUE</msg>" +
        "  </Locale>" +
        "</ModulePrefs>" +
        "<Content type='html'>DEFAULT VIEW</Content>" +
        "<Content view='one' type='html'>VIEW ONE</Content>" +
        "<Content view='two' type='html'>VIEW TWO</Content>" +
        "</Module>";

  private final DummyContext context = new DummyContext();

  @Test
  public void getLocale() throws Exception {
    Gadget gadget = new Gadget()
        .setContext(context)
        .setSpec((ShindigGadgetSpec) StaxTestUtils.parseElement(SPEC_XML, new ShindigGadgetSpec.Parser(Uri.parse(SPEC_URL), null)));

    LocaleSpec localeSpec = gadget.getLocale();
    assertEquals("VALUE", StaxMessageBundleFactory.addMessages(null, localeSpec.getLocaleMsgs()) .get("name"));
  }

  private static class DummyContext extends GadgetContext {
    public String view = super.getView();
    public String container = super.getContainer();

    @Override
    public String getView() {
      return view;
    }

    @Override
    public String getContainer() {
      return container;
    }
  }
}
