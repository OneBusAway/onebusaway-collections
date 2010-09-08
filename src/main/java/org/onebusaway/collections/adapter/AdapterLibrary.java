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
package org.onebusaway.collections.adapter;

import java.io.Serializable;

public class AdapterLibrary {

  public static <T> IAdapter<T, T> getIdentityAdapter(Class<T> type) {
    return new IdentityAdapter<T>();
  }

  public static <FROM, TO> Iterable<TO> adapt(Iterable<FROM> source,
      IAdapter<FROM, TO> adapter) {
    return new IterableAdapter<FROM, TO>(source, adapter);
  }

  /*****************************************************************************
   * 
   ****************************************************************************/

  private static final class IdentityAdapter<T> implements IAdapter<T, T>,
      Serializable {

    private static final long serialVersionUID = 1L;

    public T adapt(T source) {
      return source;
    }
  }

}
