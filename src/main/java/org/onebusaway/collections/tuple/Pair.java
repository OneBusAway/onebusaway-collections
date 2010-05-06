/**
 * 
 */
package org.onebusaway.collections.tuple;

public interface Pair<T> extends T2<T,T> {

  public boolean isReflexive();

  public boolean contains(T element);

  public T getOpposite(T element);

  public Pair<T> swap();
}