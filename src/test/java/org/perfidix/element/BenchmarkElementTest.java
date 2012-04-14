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
package org.perfidix.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.perfidix.annotation.Bench;

/**
 * Test case for BenchmarkElements.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class BenchmarkElementTest {

    private transient BenchmarkElement benchClass1;
    private transient BenchmarkElement benchClass2;

    /**
     * Simple setUp
     * 
     * @throws Exception
     *             of any kind
     */
    @Before
    public void setUp() throws Exception {
        final Class<?> clazz = BenchClass.class;
        final Method meth = clazz.getMethod("bench");
        final BenchmarkMethod benchMeth = new BenchmarkMethod(meth);

        benchClass1 = new BenchmarkElement(benchMeth);
        benchClass2 = new BenchmarkElement(benchMeth);
    }

    /**
     * Test method for {@link org.perfidix.element.BenchmarkElement#getId()} .
     */
    @Test
    public void testID() {
        assertNotSame("Id should be the same", benchClass1.getId(), benchClass2.getId());
    }

    /**
     * Test method for {@link org.perfidix.element.BenchmarkElement#equals(Object)} .
     */
    @Test
    public void testEquals() {
        assertFalse("Bench classes should be the same", benchClass1.equals(benchClass2));
        assertTrue("Methods should be the same", benchClass1.getMeth().equals(benchClass2.getMeth()));
    }

    /**
     * Test method for {@link org.perfidix.element.BenchmarkElement#hashCode()} .
     */
    @Test
    public void testHashCode() {
        assertNotSame("HashCode of Class should not be the same", benchClass1.hashCode(), benchClass2
            .hashCode());
        assertEquals("HashCode of Method should be the same", benchClass1.getMeth().hashCode(), benchClass2
            .getMeth().hashCode());
    }

    class BenchClass {
        @Bench
        public void bench() {
            // Just a test for identifying classes and methods.
        }
    }
}
