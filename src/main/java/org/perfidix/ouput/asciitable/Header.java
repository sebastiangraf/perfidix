/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the University of Konstanz nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
    public Header(final String contentToDisplay, final char theEnclosing, final Alignment theOrientation,
        final NiceTable theTable) {
        super(theTable);
        this.orientation = theOrientation;
        this.enclosing = theEnclosing;

        this.title = Util.combine(new String(new char[] {
            enclosing
        }).toString(), SPACE, contentToDisplay, SPACE, new String(new char[] {
            enclosing
        }).toString());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String draw() {
        return Util.combine(new String(new char[] {
            AbstractTabularComponent.BORDER
        }), Util.pad(title, enclosing, getTable().getTotalWidth() - 2, orientation), new String(new char[] {
            AbstractTabularComponent.BORDER
        }), NEWLINE);

    }

}
