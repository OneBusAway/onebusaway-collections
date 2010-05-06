/**
 * 
 */
package org.onebusaway.collections.tuple;

import java.util.NoSuchElementException;

public interface Pair<T> extends T2<T, T> {

  public boolean isReflexive();

  public boolean contains(T element);

  /**
   * Return {@link #getFirst()} if element equals {@link #getSecond()}, returns
   * {@link #getSecond()} if element equals {@link #getFirst()}, and throws
   * {@link NoSuchElementException} if element is equal to neither.
   * 
   * @param element
   * @return returns
   * @throws NoSuchElementException if element is not equal to either member of
   *           the pair
   */
  public T getOpposite(T element) throws NoSuchElementException;

  public Pair<T> swap();
}