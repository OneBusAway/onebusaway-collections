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

public class DefaultPropertyMethodResolver implements PropertyMethodResolver {

  @Override
  public PropertyMethod getPropertyMethod(Class<?> targetType,
      String propertyName) {
    String methodName = "get" + propertyName.substring(0, 1).toUpperCase()
        + propertyName.substring(1);
    Method method = null;
    try {
      method = targetType.getMethod(methodName);
    } catch (Exception ex) {
      throw new IllegalStateException("error introspecting class: "
          + targetType, ex);
    }
    if (method == null) {
      throw new IllegalStateException("could not find property \""
          + propertyName + "\" for type " + targetType.getName());
    }
    method.setAccessible(true);
    return new PropertyMethodImpl(method);
  }
}
