package edu.wpi.cs.wpisuitetng.modules.core.models;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProjectTest {

	@Test
	public void testProjectToJson() {
		User[] u = {new User("steve","ste","pass",7),new User("tom","t","pas",8)};
		String[] s = {"core","bugs","chat"};
		System.out.println(new Project("windows","001",u[0],u,s).toJSON());
		assert(true);
	}

}
