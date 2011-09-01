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
package org.perfidix.example;

/**
 * <h1>FastLongStack</h1>
 * <p>
 * Unsynchronized stack optimized for long primitive type. Is significantly
 * faster than a normal Stack.
 * </p>
 * 
 * @author Marc Kramis, University of Konstanz / Seabix
 */
public final class FastIntStack {

    /** Constant for static int stackSize. */
    private static final int INIT_STACK_SIZE = 32;

    /** Internal array to store stack elements. */
    private transient int[] stack;

    /** Current stackSize of stack. */
    private transient int stackSize;

    /**
     * Constructor.
     */
    public FastIntStack() {
        stack = new int[INIT_STACK_SIZE];
        stackSize = 0;
    }

    /**
     * Place new element on top of stack. This might require to double the
     * stackSize of the internal array.
     * 
     * @param element
     *            Element to push.
     */
    public void push(final int element) {
        if (stack.length == stackSize) {
            final int[] biggerStack = new int[stack.length << 1];
            System.arraycopy(stack, 0, biggerStack, 0, stack.length);
            stack = biggerStack;
        }
        stack[stackSize++] = element;
    }

    /**
     * Get the element on top of the stack. The internal array performs boundary
     * checks.
     * 
     * @return Topmost stack element.
     */
    public int peek() {
        return stack[stackSize - 1];
    }

    /**
     * Get element at given position in stack. The internal array performs
     * boundary checks.
     * 
     * @param position
     *            Position in stack from where to get the element.
     * @return Stack element at given position.
     */
    public int get(final int position) {
        return stack[position];
    }

    /**
     * Remove topmost element from stack.
     * 
     * @return Removed topmost element of stack.
     */
    public int pop() {
        return stack[--stackSize];
    }

    /**
     * Reset the stack.
     */
    public void clear() {
        stackSize = 0;
    }

    /**
     * Get the current stackSize of the stack.
     * 
     * @return Current stackSize of stack.
     */
    public int size() {
        return stackSize;
    }

}
