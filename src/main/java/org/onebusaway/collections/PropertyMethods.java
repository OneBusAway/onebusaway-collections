/**
 * Copyright (C) 2012 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onebusaway.collections;

import java.lang.reflect.Method;

/**
 * Generic methods for working with {@link PropertyMethod} objects.
 * 
 * @author bdferris
 * 
 */
public class PropertyMethods {

  public static PropertyMethod getPropertyMethod(Class<?> valueType,
      String name, PropertyMethodResolver resolver) {
    if (resolver != null) {
      PropertyMethod method = resolver.getPropertyMethod(valueType, name);
      if (method != null) {
        return method;
      }
    }
    String methodName = "get" + name.substring(0, 1).toUpperCase()
        + name.substring(1);
    Method method = null;
    try {
      method = valueType.getMethod(methodName);
    } catch (Exception ex) {
      throw new IllegalStateException(
          "error introspecting class: " + valueType, ex);
    }

    if (method == null)
      throw new IllegalStateException("could not find property: " + name);

    method.setAccessible(true);
    return new PropertyMethodImpl(method);
  }
}
