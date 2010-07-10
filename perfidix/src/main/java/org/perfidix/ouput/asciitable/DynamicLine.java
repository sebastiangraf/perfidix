/*
 * Copyright 2009 Distributed Systems Group, University of Konstanz
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
 * $LastChangedRevision$
 * $LastChangedBy$
 * $LastChangedDate$
 *
 */
package org.perfidix.ouput.asciitable;

/**
 * A dynamic, extensible line. Good as a row delemiter.
 * 
 * @author Alexander Onea, neue Couch
 */
public final class DynamicLine extends AbstractTabularComponent {

    /** Content for this dynamic line. */
    private transient final char content;

    /**
     * Constructor.
     * 
     * @param theContent
     *            where the row should be filled with.
     * @param theTable
     *            the table to be drawn to.
     */
    public DynamicLine(final char theContent, final NiceTable theTable) {
        super(theTable);
        this.content = theContent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String draw() {
        return Util.combine(new String(
                new char[] { AbstractTabularComponent.BORDER }), Util.repeat(
                new String(new char[] { content }),
                getTable().getTotalWidth() - 2), new String(
                new char[] { AbstractTabularComponent.BORDER }), NEWLINE);

    }

}