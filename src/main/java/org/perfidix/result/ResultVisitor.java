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
 * $Id: ResultVisitor.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix.result;

import java.util.Formatter;

/**
 * the ResultVisitor is able to visit and view the results. mainly an iterator,
 * it can parse the results.
 */
public abstract class ResultVisitor {

    /**
     * FIXME: comment.
     */
    protected String floatFormat = "%05.2f";

    /**
     * visits the results.
     * 
     * @param r
     *            the Result to visit.
     */
    public abstract void visit(AbstractResult r);

    /**
     * @param res
     *            FIXME
     * @return FIXME
     */
    protected double getConf95Min(final AbstractResult res) {
        return Math.max(0, res.avg() - res.getConf95());
    }

    /**
     * @param res
     *            FIXME
     * @return FIXME
     */
    protected double getConf95Max(final AbstractResult res) {
        return res.avg() + res.getConf95();
    }

    protected double getConf99Min(final AbstractResult res) {
        return Math.max(0, res.avg() - res.getConf99());
    }

    protected double getConf99Max(final AbstractResult res) {
        return res.avg() + res.getConf99();
    }

    /**
     * formats a double. if you want to change the output of the doubles, give
     * in the floatFormat through the alternative constructor.
     * 
     * @param i
     *            the number to format
     * @see java.util.Formatter for the documentation.
     * @return the formatted string.
     */
    protected String format(final double i) {
        return new Formatter().format(floatFormat, i).toString();
    }

}
