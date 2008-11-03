/*
 * Copyright 2007 University of Konstanz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * $Id: XMLCreatorTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import java.util.List;

import org.dom4j.Document;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.result.MethodResult;
import org.perfidix.result.AbstractResult;
import org.perfidix.result.ResultContainer;
import org.perfidix.result.ResultToXml;

public class XMLCreatorTest extends PerfidixTest {

    private ResultToXml v;

    private AbstractResult s;

    private ResultContainer rc;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        long[] simpleData = { 1, 2, 3 };
        v = new ResultToXml("output.xml");
        s = Perfidix.createSingleResult("singleResult", simpleData);
        rc = new MethodResult("testname");
        rc.append(s);
        rc.append(s);
    }

    @Test
    public void test1() {

        v.visit(s);
        Document d = v.getDocument();
        String xml = d.asXML();
        List myContent = d.content();

        for (int i = 0; i < myContent.size(); i++) {
            getLog().info(myContent.get(i).toString());
        }

        getLog().info(xml);

    }
}
