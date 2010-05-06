/*
 * Copyright 2008 Brian Ferris
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.onebusaway.collections.tuple;

public abstract class Tuples {

  private Tuples() {

  }

  public static <T> Pair<T> pair(T first, T second) {
    return new PairImpl<T>(first, second);
  }

  public static <S1, S2> T2<S1, S2> tuple(S1 first, S2 second) {
    return new T2Impl<S1, S2>(first, second);
  }

  static final boolean equals(Object a, Object b) {
    return a == null ? (b == null) : (a.equals(b));
  }
}
