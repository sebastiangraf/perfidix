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
 * @author Sebastian Graf, University of Konstanz
 */
public final class Header extends AbstractTabularComponent {

    /**
     * Title of the header.
     */
    private transient final String title;

    /**
     * Enclosed char of the header.
     */
    private transient final char enclosing;

    /**
     * Orientation of the title.
     */
    private transient final Alignment orientation;

    /**
     * Constructor.
     * 
     * @param contentToDisplay
     *            the string to be present in the header row.
     * @param theEnclosing
     *            a char filling up the rest of the row.
     * @param theOrientation
     *            the orientation of the text
     * @param theTable
     *            the parent table.
     */
    public Header(
            final String contentToDisplay, final char theEnclosing,
            final Alignment theOrientation, final NiceTable theTable) {
        super(theTable);
        this.orientation = theOrientation;
        this.enclosing = theEnclosing;

        this.title =
                Util.combine(
                        new String(new char[] { enclosing }).toString(), SPACE,
                        contentToDisplay, SPACE, new String(
                                new char[] { enclosing }).toString());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String draw() {
        return Util.combine(
                new String(new char[] { AbstractTabularComponent.BORDER }),
                Util.pad(
                        title, enclosing, getTable().getTotalWidth() - 2,
                        orientation), new String(
                        new char[] { AbstractTabularComponent.BORDER }),
                NEWLINE);

    }

}
