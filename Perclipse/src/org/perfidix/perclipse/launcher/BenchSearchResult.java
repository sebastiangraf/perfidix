package org.perfidix.perclipse.launcher;

import org.eclipse.jdt.core.IType;

public class BenchSearchResult {

  private final IType[] fTypes;

  public BenchSearchResult(IType[] types) {
    fTypes = types;
  }

  public IType[] getTypes() {
    return fTypes;
  }

  boolean isEmpty() {
    return getTypes().length <= 0;
  }
}