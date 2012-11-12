package edu.wpi.cs.wpisuitetng.modules;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.Permission;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;


public abstract class AbstractModel implements Model {

	private Map<User, Permission> permission;
	
	public AbstractModel()
	{
		permission = new HashMap<User, Permission>();
	}
	
	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public abstract String toJSON();

	@Override
	public Permission getPermission(User u) {
		return permission.get(u);
	}

	@Override
	public void setPermission(Permission p, User u) {
		permission.put(u, p);
	}

}
