/**
 * 
 */
package org.onebusaway.collections.tuple;

import java.io.Serializable;
import java.util.NoSuchElementException;

final class PairImpl<T> implements Pair<T>, Serializable {

  private static final long serialVersionUID = 1L;

  private final T _first;

  private final T _second;

  public PairImpl(T first, T second) {
    _first = first;
    _second = second;
  }

  public T getFirst() {
    return _first;
  }

  public T getSecond() {
    return _second;
  }

  public boolean isReflexive() {
    return Tuples.equals(_first, _second);
  }

  public boolean contains(T element) {
    return Tuples.equals(_first, element) || Tuples.equals(_second, element);
  }

  public T getOpposite(T element) {
    if (Tuples.equals(_first, element))
      return _second;
    if (Tuples.equals(_second, element))
      return _first;
    throw new NoSuchElementException();
  }

  public PairImpl<T> swap() {
    return new PairImpl<T>(_second, _first);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_first == null) ? 0 : _first.hashCode());
    result = prime * result + ((_second == null) ? 0 : _second.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PairImpl<?> other = (PairImpl<?>) obj;
    return Tuples.equals(_first, other._first)
        && Tuples.equals(_second, other._second);
  }

  @Override
  public String toString() {
    return "Pair(" + _first + "," + _second + ")";
  }
}