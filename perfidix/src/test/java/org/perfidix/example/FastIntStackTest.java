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
/**
 * 
 */
package org.perfidix.example;

import static org.junit.Assert.assertEquals;

import java.util.ArrayDeque;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.perfidix.example.stack.FastIntStack;

/**
 * @author Nuray Guerler, University of Konstanz This is a class that tests the
 *         {@link FastIntStack} class with all its methods
 */
public class FastIntStackTest
{

    /**
     * This member variable contains a FastIntStack object.
     */
    private FastIntStack stack;
    /**
     * This member variable contains an ArrayDeque object.
     */
    private ArrayDeque <Integer> deque;
    /**
     * Create constant to determine size of test data
     */
    private static final int SIZE = 5;
    /**
     * This integer array contains five random values to test FastIntStack
     * methods
     */
    private static int[] r = new int[SIZE];

    /**
     * Fill integer array with raandom values
     */
    @BeforeClass
    public static void initR()
    {
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < SIZE; i++)
        {
            r[i] = random.nextInt();
        }
    }

    /**
     * @throws java.lang.Exception
     */
    /**
     * initialize stack
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception
    {
        stack = new FastIntStack();
        deque = new ArrayDeque<Integer>();
    }

    /**
     * Test method for {@link org.perfidix.example.FastIntStack#push(int)}.,
     * {@link org.perfidix.example.FastIntStack#get(int)}.,
     * {@link org.perfidix.example.FastIntStack#peek()}. and
     * {@link org.perfidix.example.FastIntStack#pop()}.
     */
    @Test
    public void testStackPushSizeGetPeekPop()
    {
        assertEquals("Stack should be empty", 0, stack.size());
        for (int i = 0; i < SIZE; i++)
        {
            stack.push(r[i]);
        }
        assertEquals(new StringBuilder("Size of stack is ").append(SIZE)
                .toString(), SIZE, stack.size());
        for (int i = SIZE - 1; i >= 0; i--)
        {
            assertEquals(new StringBuilder("value of position ").append(i)
                    .append(" is returned").toString(), r[i], stack.get(i));
            assertEquals(
                    new StringBuilder("Stack element contains").append(r[i])
                            .toString(), r[i], stack.peek());
            assertEquals("Element r[i] is removed from stack", r[i],
                    stack.pop());
        }
    }

    /**
     * Test method for {@link org.perfidix.example.FastIntStack#clear()}.
     */
    @Test
    public void testStackClear()
    {
        for (int i = 0; i < SIZE; i++)
        {
            stack.push(r[i]);
        }
        assertEquals("Size of stack is " + SIZE, SIZE, stack.size());
        stack.clear();
    }
    
    /**
     * Test method for {@link org.perfidix.example.ArrayDeque#push(int)}.,
     * {@link org.perfidix.example.ArrayDeque#get(int)}.,
     * {@link org.perfidix.example.ArrayDeque#peek()}. and
     * {@link org.perfidix.example.ArrayDeque#pop()}.
     */
    @Test
    public void testDequePushSizeGetPeekPop()
    {
        assertEquals("Deque should be empty", 0, deque.size());
        for (int i = 0; i < SIZE; i++)
        {
            deque.push(r[i]);
        }
        assertEquals(new StringBuilder("Size of Deque is ").append(SIZE)
                .toString(), SIZE, deque.size());
        for (int i = SIZE - 1; i >= 0; i--)
        {
//            assertEquals(new StringBuilder("value of position ").append(i)
//                    .append(" is returned").toString(), r[i], deque.get(i));
            assertEquals(
                    new StringBuilder("Deque element contains").append(r[i])
                            .toString(), r[i], deque.peek().intValue());
            assertEquals("Element r[i] is removed from stack", r[i],
                    deque.pop().intValue());
        }
    }


}
