/**
 * Copyright (c) 2011, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Konstanz nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
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