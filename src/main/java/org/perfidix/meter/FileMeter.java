package org.perfidix.meter;


import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;


/**
 * File meter for measuring the size of a file or directory registered beforehand.
 * 
 * @author Sebastian Graf, University of Konstanz
 * 
 */
public class FileMeter extends AbstractMeter {

    /** Static name of meter. */
    private static final String NAME = "FileMeter";

    /** Reference to file to be benched. */
    private final File mFile;

    /** Scale of memory. */
    private transient final Memory mScale;

    /**
     * Constructor.
     * 
     * @param pFile to be evaluated, is going to be traversed recursively
     * @param pScale for returning the data
     */
    public FileMeter (final File pFile, final Memory pScale) {
        super();
        mFile = pFile;
        mScale = pScale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getValue () {
        long size = iterateRecursive(mFile);
        return new BigDecimal(size, MathContext.DECIMAL128).divide(new BigDecimal(mScale.getNumberOfBytes()), MathContext.DECIMAL128).doubleValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUnit () {
        return mScale.getUnit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUnitDescription () {
        return mScale.getUnitDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName () {
        return NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mFile == null) ? 0 : mFile.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals (Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        FileMeter other = (FileMeter) obj;
        if (mFile == null) {
            if (other.mFile != null) return false;
        } else if (!mFile.equals(other.mFile)) return false;
        return true;
    }

    private static long iterateRecursive (final File pFile) {
        long size = 0;
        if (pFile.isDirectory()) {
            for (final File child : pFile.listFiles()) {
                size = size + iterateRecursive(child);
            }
        }
        if (pFile.isFile()) {
            size = size + pFile.length();
        }
        return size;
    }

}
