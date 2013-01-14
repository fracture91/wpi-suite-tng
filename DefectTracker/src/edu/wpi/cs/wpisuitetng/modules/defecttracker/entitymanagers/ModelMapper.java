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
	
	/**
	 * Construct a ModelMapper with default blacklist of "permission"
	 */
	public ModelMapper() {
		// this has two parameters
		blacklist.add("permission");
	}
	
	/**
	 * Blacklist should contain field names.
	 * e.g. getSomeField -> getBlacklist().add("someField")
	 * 
	 * @return A set of field names to ignore, which can be modified
	 */
	public Set<String> getBlacklist() {
		return blacklist;
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
		final Method[] sourceMethods = source.getClass().getMethods();
		final Method[] destMethods = destination.getClass().getMethods();
		final Map<String, Method> destMap = new HashMap<String, Method>();
		for (Method method : destMethods) {
			String name = method.getName();
			if(name.startsWith("set")) {
				name = name.substring(3);
				if(!blacklist.contains(name.substring(0, 1).toLowerCase() + name.substring(1))) {
					destMap.put(name.toLowerCase(), method);
				}
			}
		}
		
		for (Method sourceMethod : sourceMethods) {
			String name = sourceMethod.getName().toLowerCase();
			if(!name.startsWith("get")) {
				continue;
			}
			name = name.substring(3);
			if (destMap.containsKey(name)) {
				try {
					destMap.get(name).invoke(destination, sourceMethod.invoke(source));
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
	
}
