package org.perfidix.example.list;

import java.util.Arrays;

/**
 * This is a simple container for native integers.
 * 
 * @author BaseX Team 2005-11, BSD License
 * @author Christian Gruen
 */
public class IntList extends ElementList {
	/** Element container. */
	protected int[] list;

	/**
	 * Default constructor.
	 */
	public IntList() {
		this(CAP);
	}

	/**
	 * Constructor, specifying an initial array capacity.
	 * 
	 * @param c
	 *            array capacity
	 */
	public IntList(final int c) {
		list = new int[c];
	}

	/**
	 * Constructor.
	 * 
	 * @param f
	 *            resize factor
	 */
	public IntList(final double f) {
		this();
		factor = f;
	}

	/**
	 * Constructor, specifying an initial array.
	 * 
	 * @param a
	 *            initial array
	 */
	public IntList(final int[] a) {
		list = a;
		size = a.length;
	}

	/**
	 * Adds an entry to the array.
	 * 
	 * @param e
	 *            entry to be added
	 */
	public final void add(final int e) {
		if (size == list.length)
			list = Arrays.copyOf(list, newSize());
		list[size++] = e;
	}

	/**
	 * Returns the element at the specified index position.
	 * 
	 * @param i
	 *            index
	 * @return element
	 */
	public final int get(final int i) {
		return list[i];
	}

	/**
	 * Sets an element at the specified index position.
	 * 
	 * @param i
	 *            index
	 * @param e
	 *            element to be set
	 */
	public final void set(final int i, final int e) {
		if (i >= list.length)
			list = Arrays.copyOf(list, newSize(i + 1));
		list[i] = e;
		size = Math.max(size, i + 1);
	}

	/**
	 * Checks if the specified element is found in the list.
	 * 
	 * @param e
	 *            element to be found
	 * @return result of check
	 */
	public final boolean contains(final int e) {
		for (int i = 0; i < size; ++i)
			if (list[i] == e)
				return true;
		return false;
	}

	/**
	 * Inserts elements at the specified index position.
	 * 
	 * @param i
	 *            index
	 * @param e
	 *            elements to be inserted
	 */
	public final void insert(final int i, final int[] e) {
		final int l = e.length;
		if (l == 0)
			return;
		if (size + l > list.length)
			list = Arrays.copyOf(list, newSize(size + l));
		Array.move(list, i, l, size - i);
		System.arraycopy(e, 0, list, i, l);
		size += l;
	}

	/**
	 * Deletes the specified element.
	 * 
	 * @param i
	 *            element to be deleted
	 */
	public final void delete(final int i) {
		Array.move(list, i + 1, -1, --size - i);
	}

	/**
	 * Adds a difference to all elements starting from the specified index.
	 * 
	 * @param e
	 *            difference
	 * @param i
	 *            index
	 */
	public final void move(final int e, final int i) {
		for (int a = i; a < size; a++)
			list[a] += e;
	}

	/**
	 * Returns the uppermost element from the stack.
	 * 
	 * @return the uppermost element
	 */
	public final int peek() {
		return list[size - 1];
	}

	/**
	 * Pops the uppermost element from the stack.
	 * 
	 * @return the popped element
	 */
	public final int pop() {
		return list[--size];
	}

	/**
	 * Pushes an element onto the stack.
	 * 
	 * @param val
	 *            element
	 */
	public final void push(final int val) {
		add(val);
	}

	/**
	 * Searches the specified element via binary search. Note that all elements
	 * must be sorted.
	 * 
	 * @param e
	 *            element to be found
	 * @return index of the search key, or the negative insertion point - 1
	 */
	public final int sortedIndexOf(final int e) {
		return Arrays.binarySearch(list, 0, size, e);
	}

	/**
	 * Returns an array with all elements.
	 * 
	 * @return array
	 */
	public final int[] toArray() {
		return Arrays.copyOf(list, size);
	}

	/**
	 * Sorts the data.
	 * 
	 * @return self reference
	 */
	public IntList sort() {
		Arrays.sort(list, 0, size);
		return this;
	}

}
