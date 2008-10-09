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
 * $Id: ResultToXml.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix.visitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.perfidix.IMeter;
import org.perfidix.IResult;
import org.perfidix.NiceTable;
import org.perfidix.Result;

/**
 * this visitor allows the saving of the result.
 */
public class ResultToXml extends ResultVisitor {

    /**
     * where to store the results XML file.
     */
    private File destinationResults;

    /**
     * the XML document that represents the results.
     */
    private Document document;

    /**
     * the root element.
     */
    private Element root;

    /**
     * temporary elements.
     */
    private Element snapshot;

    /**
     * the logger object.
     */
    private final static Log LOGGER = LogFactory.getLog("ResultToXML");

    /**
     * convenience constructor. the result file is empty.
     */
    public ResultToXml() {
        this("");
    }

    /**
     * The constructor in case we create a new results file.
     * 
     * @TODO allow id and svn attribute.
     * @param resultsFile
     *            the filename to store the results to.
     */
    public ResultToXml(final String resultsFile) {

        destinationResults = new File(resultsFile);
        document = DocumentHelper.createDocument();
        root = document.addElement("root");
        snapshot =
                root
                        .addElement("snapshot")
                        .addAttribute("svn", "")
                        .addAttribute("id", "")
                        .addAttribute(
                                "date",
                                new Date(System.currentTimeMillis()).toString());
    }

    /**
     * The constructor in case we append new results to an existing result set.
     * 
     * @param previousResults
     *            the results read from the existing file
     * @param resultFile
     *            where to store it..actually in the same place as the previous
     */
    public ResultToXml(final Document previousResults, final String resultFile) {

        document = previousResults;
        destinationResults = new File(resultFile);

        snapshot =
                root
                        .addElement("snapshot").addAttribute("svn", "")
                        .addAttribute("id", "").addAttribute("date", "");
    }

    private void addDefaultAttributes(final Result r, final Element e) {
        e.addAttribute("max", Long.toString(r.max()));
        e.addAttribute("min", Long.toString(r.min()));
        e.addAttribute("avg", Double.toString(r.avg()));
        e.addAttribute("stddev", Double.toString(r.getStandardDeviation()));
        e.addAttribute("conf95", Double.toString(r.getConf95()));
        e.addAttribute("conf99", Double.toString(r.getConf99()));

        e.addAttribute("name", r.getName());
    }

    /**
     * visits a benchmark result.
     * 
     * @param r
     */
    private void visit(final IResult.BenchmarkResult r) {
        Element e = snapshot.addElement("benchmark");
        addDefaultAttributes(r, e);
        Iterator<IResult.ClassResult> it = r.getChildren().iterator();
        while (it.hasNext()) {
            visit(it.next(), e);
        }
    }

    /**
     * visits the class result.
     * 
     * @param my
     * @param parent
     */
    private void visit(final IResult.ClassResult my, final Element parent) {
        Element classResult = parent.addElement("class");
        addDefaultAttributes(my, classResult);
        classResult.addAttribute("class", my.getClassUnderTest());

        Iterator<IResult.MethodResult> classIterator =
                my.getChildren().iterator();
        while (classIterator.hasNext()) {
            visit(classIterator.next(), classResult);
        }
    }

    private void visit(
            final IResult.MethodResult method, final Element classResult) {
        LOGGER.info("visiting the method results...");
        Element methodResult = classResult.addElement("method");
        addDefaultAttributes(method, methodResult);

        Iterator<IResult.SingleResult> methodIterator =
                method.getChildren().iterator();

        while (methodIterator.hasNext()) {
            visit(methodIterator.next(), methodResult);

        }
    }

    private void visit(
            final IResult.SingleResult result, final Element methodResult) {
        LOGGER.info("visiting the singleresult...");

        Element singleResult = methodResult.addElement("result");
        singleResult.addAttribute("name", result.getName());
        Element meter = singleResult.addElement("meter");
        IMeter theMeter = result.getDefaultMeter();
        addDefaultAttributes(result, meter);
        meter.addAttribute("unit", theMeter.getUnit());
        meter.addAttribute("description", theMeter.getUnit());
        meter.addAttribute("class", theMeter.getClass().getSimpleName());

        Element data = singleResult.addElement("data");
        data.addCDATA(NiceTable.Util.implode(",", result.getResultSet()));

    }

    /**
     * TODO ALEX....explain this!
     * 
     * <pre>
     * Visitor v = new SaveResultVisitor();
     * Result r = new Result();
     * v.visit(r);
     * </pre>
     * 
     * @param r
     *            the result to visit.
     */
    @Override
    public void visit(final IResult r) {

        if (r instanceof IResult.BenchmarkResult) {
            visit((IResult.BenchmarkResult) r);
        } else if (r instanceof IResult.ClassResult) {
            visit((IResult.ClassResult) r, snapshot.addElement("benchmark"));
        } else if (r instanceof IResult.MethodResult) {
            visit((IResult.MethodResult) r, snapshot
                    .addElement("benchmark").addElement("class"));
        } else if (r instanceof IResult.SingleResult) {
            visit((IResult.SingleResult) r, snapshot
                    .addElement("benchmark").addElement("class").addElement(
                            "method"));
        } else {
            throw new RuntimeException("unsupported result type.");
        }

    }

    /**
     * Save the results in a file.
     * 
     * @throws IOException
     *             if the fileWriter could not write the file.
     */
    public void save() throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer1 =
                new XMLWriter(new FileWriter(destinationResults), format);
        LOGGER.trace("will save the results in");
        LOGGER.trace(document.asXML());
        writer1.write(document);
        writer1.flush();
        writer1.close();

    }

    /**
     * @return the XML document
     */
    public Document getDocument() {

        return document;
    }

}
