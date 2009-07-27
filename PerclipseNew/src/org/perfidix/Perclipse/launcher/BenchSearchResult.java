package org.perfidix.Perclipse.launcher;

import org.eclipse.jdt.core.IType;

/**
 * @author lewandow
 */
public class BenchSearchResult {

    private final IType[] fTypes;

    /**
     * The constructor sets the IType array fTypes with the given parameter.
     * 
     * @param types
     */
    public BenchSearchResult(IType[] types) {
        fTypes = types;
    }

    /**
     * This method gives an array of type IType.
     * 
     * @return the IType array.
     */
    public IType[] getTypes() {
        return fTypes;
    }

    /**
     * This method tells you if the getTypes array is empty.
     * 
     * @return a boolean value for the isEmpty request, true if the getTypes
     *         array length is smaller than 1 or false if the getTypes array
     *         length is bigger than 0
     */
    boolean isEmpty() {
        return getTypes().length <= 0;
    }
}