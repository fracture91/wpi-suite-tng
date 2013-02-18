package edu.wpi.cs.wpisuitetng;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.Permission;

public class PermissionTest {

	AbstractModel perm;
	User u;
	
	@Before
	public void setUp() throws Exception 
	{
		perm = new AbstractModel(){

			@Override
			public void delete() {		
			}

			@Override
			public Boolean identify(Object arg0) {
				return null;
			}

			@Override
			public void save() {
			}

			@Override
			public String toJSON() {
				return null;
			}

			@Override
			public Project getProject() {
				return null;
			}

			@Override
			public void setProject(Project aProject) {
				// TODO Auto-generated method stub
				
			}
			
		};
		u = new User("ted", "tdude", "nothing", 1);
	}

	@Test
	public void testPermission() {
		perm.setPermission(Permission.READ, u);
		
		assertEquals(perm.getPermission(u), Permission.READ);
	}

}
