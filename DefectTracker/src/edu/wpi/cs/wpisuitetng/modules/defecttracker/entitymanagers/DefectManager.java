package edu.wpi.cs.wpisuitetng.modules.defecttracker.entitymanagers;

import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;

public class DefectManager implements EntityManager<Defect> {

	@Override
	public int Count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean deleteEntity(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Defect[] getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Defect[] getEntity(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Defect makeEntity(String arg0) {
		System.out.println("Make entity: " + arg0);
		return null;
	}

	@Override
	public void save(Defect arg0) {
		System.out.println("Save entity: " + arg0);
		
	}

}
