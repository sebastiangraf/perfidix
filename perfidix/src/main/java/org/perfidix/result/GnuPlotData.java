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
 * $Id: GnuPlotData.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix.result;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import org.perfidix.IMeter;
import org.perfidix.result.IResult.MethodResult;

/**
 * the gnu plot visitor parses the result into a gnu readable file.
 * 
 * @author onea which alex wanted... fix the TODOs .
 */
public class GnuPlotData extends ResultVisitor {

    /**
     * a string buffer for the concantenation.
     */
    private String buffer = new String();

    /**
     * the default file ID being parsed as a comment.
     */
    private String theID =
            "$Id: GnuPlotData.java 2624 2007-03-28 15:08:52Z kramis $";

    /**
     * the next row ID.
     */
    private int nextID = 0;

    /**
     * the file being parsed as a comment into the data file.
     */
    private String theFileName = "";

    /**
     * the IMeter which was used.
     */
    private IMeter meter = null;

    /**
     * the gnuplot visitor converts a perfidix result to a gnuplot file.
     */
    public GnuPlotData() {
        super();
    }

    /**
     * resets itself.
     */
    private void reset() {
        nextID = 0;
    }

    /**
     * sets the filename.
     * 
     * @param filename
     *            the filename
     */
    public void setFilename(final String filename) {
        theFileName = filename;
    }

    /**
     * sets the meter to display the results for.
     * 
     * @param which
     *            the meter.
     */
    public void setMeter(final IMeter which) {
        meter = which;
    }

    /**
     * allows a simpler configuration through a facade.
     * 
     * @param which
     *            the meter to display the results for.
     * @param id
     *            the subversion-id.
     * @param theFilename
     *            the filename, whatever information.
     */
    public void configure(
            final IMeter which, final String id, final String theFilename) {

        setID(id);
        setFilename(theFilename);
        setMeter(which);

    }

    /**
     * if you want to catch other results than the default IMeter, take care to
     * use configure() or setMeter() in order to achieve this.
     * 
     * @see #configure(IMeter, String, String)
     * @see #setMeter(IMeter)
     * @param r
     *            the result which will be visited the result may only be a
     *            BenchmarkResult and nothing else!
     */
    public void visit(final IResult r) {

        if (!(r instanceof IResult.BenchmarkResult)) {
            throw new RuntimeException("only benchmark results are supported!");
        }

        reset();

        if (null == meter) {
            meter = r.getDefaultMeter();
        }

        addHeaderComment("Id", theID);
        addHeaderComment("About", getAboutText(meter));
        addHeaderComment("Filename", theFileName);
        addHeaderComment("Title", r.getName());
        addHeaderComment("Date", new Date(System.currentTimeMillis())
                .toString());
        addHeaderComment("Unit", meter.getUnit());
        addBlankLine();
        addColumnComment();
        parseMethods((IResult.BenchmarkResult) r);
        addBlankLine();

    }

    private String getAboutText(final IMeter m) {
        return "Perfidix gnuplot (.gpd) data output. Meter is "
                + m.getUnitDescription();
    }

    /**
     * @param r
     */
    private void parseMethods(final IResult.BenchmarkResult r) {
        Iterator<IResult.ClassResult> it = r.getChildren().iterator();
        while (it.hasNext()) {
            Iterator<IResult.MethodResult> m =
                    it.next().getChildren().iterator();
            while (m.hasNext()) {
                parseMethod(m.next());
            }
        }
    }

    private void parseMethod(final MethodResult m) {
        Hashtable<IMeter, ArrayList<IResult.SingleResult>> tree =
                m.getCustomChildren();
        if (!tree.containsKey(meter)) {
            return;
        }

        Iterator<IResult.SingleResult> singleIt = tree.get(meter).iterator();
        while (singleIt.hasNext()) {
            parseSingleResult(singleIt.next(), m);
        }

    }

    private void parseSingleResult(
            final IResult.SingleResult single, final MethodResult m) {

        String[] data =
                {
                        "" + nextID, "" + single.min(), "" + single.max(),
                        "" + format(single.avg()),
                        "" + format(single.getStandardDeviation()),
                        "" + format(getConf95Min(single)),
                        "" + format(getConf95Max(single)),
                        "" + single.resultCount(), "" + single.sum(),
                        m.getName(), "\n", };

        buffer += NiceTable.Util.implode("\t", data);
        nextID++;
    }

    private void addColumnComment() {
        String[] cols =
                {
                        "### id", "min", "max", "avg", "stddev", "conf95min",
                        "conf95max", "runs", "sum", "benchMethodName" };

        buffer += NiceTable.Util.implode("\t", cols) + "\n";

    }

    /**
     * inserts a blank line.
     */
    private void addBlankLine() {
        buffer += "\n";
    }

    private void addHeaderComment(final String key, final String value) {
        buffer += "# " + key + "\t# " + value + "\n";
    }

    /**
     * @param someData
     *            the data to set.
     */
    public void setID(final String someData) {
        theID = someData;
    }

    /**
     * @return the buffered result.
     */
    @Override
    public String toString() {
        return buffer;
    }

    /**
     * some formatting.
     * 
     * @param bla
     *            bla.
     * @overrides
     * @return the formatted string
     */
    @Override
    protected String format(final double bla) {
        return super.format(bla).replace(',', '.');
    }

    /**
     * writes the contents of the buffer to the file name. usage:
     * 
     * <pre>
     * Result r; // get your result
     * GnuPlotData d = new GnuPlotData();
     * d.configure(r.getMeter(), &quot;0xDEAD&quot;, &quot;testing/gpdFile.gpd&quot;);
     * d.visit(r);
     * d.save();
     * </pre>
     * 
     * @throws IOException
     *             if writing didn't work.
     */
    public void save() throws IOException {
        File f = new File(theFileName);
        FileWriter w = new FileWriter(f);
        w.write(buffer);
    }

}
