/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. * Neither the name of the University of Konstanz nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.perfidix.example.list;


import java.util.Arrays;


/**
 * This class provides convenience methods for handling arrays and serves as an extension to the {@link Arrays} class of
 * Java.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Christian Gruen
 */
public final class Array {
    /**
     * Default factor for resizing dynamic arrays.
     */
    public static final double RESIZE = 1.5;

    /**
     * Private constructor.
     */
    private Array() {
    }

    /**
     * Copies the specified array.
     *
     * @param a array to be copied
     * @param s new array size
     * @return new array
     */
    public static byte[][] copyOf(final byte[][] a, final int s) {
        final byte[][] tmp = new byte[s][];
        System.arraycopy(a, 0, tmp, 0, Math.min(s, a.length));
        return tmp;
    }

    /**
     * Copies the specified array.
     *
     * @param a array to be copied
     * @param s new array size
     * @return new array
     */
    public static int[][] copyOf(final int[][] a, final int s) {
        final int[][] tmp = new int[s][];
        System.arraycopy(a, 0, tmp, 0, Math.min(s, a.length));
        return tmp;
    }

    /**
     * Copies the specified array.
     *
     * @param a array to be copied
     * @param s new array size
     * @return new array
     */
    public static String[] copyOf(final String[] a, final int s) {
        final String[] tmp = new String[s];
        System.arraycopy(a, 0, tmp, 0, Math.min(s, a.length));
        return tmp;
    }

    /**
     * Adds an entry to the end of an array and returns the new array.
     *
     * @param ar  array to be resized
     * @param e   entry to be added
     * @param <T> array type
     * @return array
     */
    public static <T> T[] add(final T[] ar, final T e) {
        final int s = ar.length;
        final T[] t = Arrays.copyOf(ar, s + 1);
        t[s] = e;
        return t;
    }

    /**
     * Adds an entry to the end of an array and returns the new array.
     *
     * @param ar array to be resized
     * @param e  entry to be added
     * @return array
     */
    public static int[] add(final int[] ar, final int e) {
        final int s = ar.length;
        final int[] t = Arrays.copyOf(ar, s + 1);
        t[s] = e;
        return t;
    }

    /**
     * Moves entries inside an array.
     *
     * @param ar  array
     * @param pos position
     * @param off move offset
     * @param l   length
     */
    public static void move(final Object ar, final int pos, final int off, final int l) {
        System.arraycopy(ar, pos, ar, pos + off, l);
    }

    /**
     * Removes an array entry at the specified position.
     *
     * @param ar  array to be resized
     * @param p   position
     * @param <T> array type
     * @return array
     */
    public static <T> T[] delete(final T[] ar, final int p) {
        final int s = ar.length - 1;
        move(ar, p + 1, -1, s - p);
        return Arrays.copyOf(ar, s);
    }

    /**
     * Reverses the order of the elements in the given array.
     *
     * @param arr array
     */
    public static void reverse(final byte[] arr) {
        reverse(arr, 0, arr.length);
    }

    /**
     * Reverses the order of all elements in the given interval.
     *
     * @param arr array
     * @param pos position of first element of the interval
     * @param len length of the interval
     */
    public static void reverse(final byte[] arr, final int pos, final int len) {
        for (int l = pos, r = pos + len - 1; l < r; l++, r--) {
            final byte tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;
        }
    }

    /**
     * Reverses the order of all elements in the given interval.
     *
     * @param arr array
     * @param pos position of first element of the interval
     * @param len length of the interval
     */
    public static void reverse(final Object[] arr, final int pos, final int len) {
        for (int l = pos, r = pos + len - 1; l < r; l++, r--) {
            final Object tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;
        }
    }

    /**
     * Returns a value for a new array size, which will always be larger than the specified value.
     *
     * @param old old size
     * @return resulting size
     */
    public static int newSize(final int old) {
        return newSize(old, RESIZE);
    }

    /**
     * Returns a value for a new array size, which will always be larger than the specified value.
     *
     * @param old    old size
     * @param factor resize factor; must be larger than or equal to 1
     * @return resulting size
     */
    public static int newSize(final int old, final double factor) {
        return (int) (old * factor) + 1;
    }

    /**
     * Swaps two entries of the given int array.
     *
     * @param arr array
     * @param a   first position
     * @param b   second position
     */
    public static void swap(final int[] arr, final int a, final int b) {
        final int c = arr[a];
        arr[a] = arr[b];
        arr[b] = c;
    }

    /**
     * Swaps arr[a .. (a+n-1)] with arr[b .. (b+n-1)].
     *
     * @param arr order array
     * @param a   first offset
     * @param b   second offset
     * @param n   number of values
     */
    public static void swap(final int[] arr, final int a, final int b, final int n) {
        for (int i = 0; i < n; ++i)
            swap(arr, a + i, b + i);
    }
}
