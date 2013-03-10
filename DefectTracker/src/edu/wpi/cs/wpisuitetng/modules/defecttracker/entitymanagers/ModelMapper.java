/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andrew Hurle
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.defecttracker.entitymanagers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.wpi.cs.wpisuitetng.modules.Model;

/**
 * Responsible for copying properties from one Model to another.
 */
public class ModelMapper {

	private final Set<String> blacklist = new HashSet<String>();
	private final MapCallback defaultMapCallback = new MapCallback() {
		@Override
		public Object call(Model source, Model destination, String fieldName, Object sourceValue,
				Object destinationValue) {
			// just copy source value to destination
			return sourceValue;
		}
	};
	
	/**
	 * Construct a ModelMapper with default blacklist of "permission"
	 */
	public ModelMapper() {
		// this has two parameters
		blacklist.add("permission");
	}
	
	/**
	 * Blacklist should contain field names.
	 * e.g. getSomeField() -> getBlacklist().add("someField")
	 * 
	 * @return A set of field names to ignore, which can be modified
	 */
	public Set<String> getBlacklist() {
		return blacklist;
	}
	
	/**
	 * Callback to pass to {@link ModelMapper#map(Model, Model, MapCallback)}
	 */
	public interface MapCallback {
		/**
		 * Called for every get/set method pair.
		 * 
		 * @param source The source Model
		 * @param destination The destination Model
		 * @param fieldName The name of the field, pulled from method names
		 *                  getSomeField() -> "someField"
		 * @param sourceValue The return value of source.get[fieldName]()
		 * @param destinationValue The return value of destination.get[fieldName]()
		 * @return The value to pass to destination.setMethod()
		 */
		Object call(Model source, Model destination, String fieldName, Object sourceValue,
				Object destinationValue);
	}
	
	/**
	 * @return field name from given accessor name ("getBlahField" -> "blahField")
	 */
	private static String accessorNameToFieldName(String methodName) {
		methodName = methodName.substring(3); // cut out "get" or "set"
		return methodName.substring(0, 1).toLowerCase() + methodName.substring(1); // BlahField -> blahField
	}
	
	/**
	 * For each get/set pair (ignoring blacklisted fields), copy the result of callback to destination.
	 * Setters without matching getters on the source are ignored.
	 * 
	 * @param source The Model to copy from
	 * @param destination The Model to copy to
	 * @param callback The callback whose return value is set on the destination
	 * @throws RuntimeException If something goes wrong during copying
	 */
	public void map(final Model source, final Model destination, MapCallback callback)
			throws RuntimeException {
		final Method[] sourceMethods = source.getClass().getMethods();
		final Method[] destMethods = destination.getClass().getMethods();
		
		// build maps of field names to their corresponding get/set methods in destination
		final Map<String, Method> destSetMap = new HashMap<String, Method>();
		final Map<String, Method> destGetMap = new HashMap<String, Method>();
		for (Method method : destMethods) {
			String name = method.getName();
			boolean isSetter = name.startsWith("set");
			boolean isGetter = !isSetter && name.startsWith("get");
			if(isSetter || isGetter) {
				name = accessorNameToFieldName(name);
				if(!blacklist.contains(name)) {
					(isSetter ? destSetMap : destGetMap).put(name, method);
				}
			}
		}
		
		// for each getter in the source model...
		for (Method sourceMethod : sourceMethods) {
			String name = sourceMethod.getName();
			if(!name.startsWith("get")) {
				continue;
			}
			name = accessorNameToFieldName(name);
			
			// if the destination has a corresponding setter...
			if (destSetMap.containsKey(name)) {
				try {
					Object sourceValue = sourceMethod.invoke(source);
					Object destinationValue = destGetMap.get(name).invoke(destination);
					// set the destination field to whatever the callback returns
					destSetMap.get(name).invoke(destination,
							callback.call(source, destination, name, sourceValue, destinationValue));
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	/**
	 * Copy all get/set pairs across from source to destination, ignoring blacklisted fields.
	 * Setters without matching getters on the source are ignored.
	 * 
	 * @param source The Model to copy from
	 * @param destination The Model to copy to
	 * @throws RuntimeException If something goes wrong during copying
	 */
	public void map(final Model source, final Model destination) throws RuntimeException {
		this.map(source, destination, defaultMapCallback );
	}
	
}
