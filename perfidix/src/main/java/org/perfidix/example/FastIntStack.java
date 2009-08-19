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
 * $Revision$
 * $Author$
 * $Date$
 *
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
     * Place new element on top of stack. This might require to double the stackSize
     * of the internal array.
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
