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
package org.onebusaway.collections.beans;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

public class DefaultPropertyMethodResolver implements PropertyMethodResolver {

  private ClassGraph classScanner = new ClassGraph();

  @Override
  public PropertyMethod getPropertyMethod(Class<?> targetType,
      String propertyName) {
	String methodName = "get";
	for(String part : propertyName.split(" |_")) {
	    methodName += part.substring(0, 1).toUpperCase()
	            + part.substring(1);
	}
    Method method = null;
    try {
    	if(targetType.isInterface()) {
        	ScanResult scanResult = new ClassGraph()
        			.acceptPackages("org.onebusaway")
        			.enableClassInfo()
        			.scan();

        	List<Method> methods = new ArrayList<Method>();
        	for (ClassInfo ci : scanResult.getClassesImplementing(targetType.getCanonicalName())) {
        		try {
            		methods.add(Class.forName(ci.getName()).getMethod(methodName));
        		} catch(Exception e) {
        			continue;
        		}
        	}	
        	
        	if(methods.size() == 1)
        		method = methods.get(0);
        	else
        	    throw new IllegalStateException("Ambiguous implementation set for interface: "
        	              + targetType + " potentials: " + methods);
    	} else
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
