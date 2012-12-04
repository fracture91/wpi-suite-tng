/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mpdelladonna
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules;

public abstract class AbstractEntityManager implements EntityManager<Model> 
{

	@Override
	public Model makeEntity(String content) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model[] getEntity(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model[] getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Model model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean deleteEntity(String id) {
		return false;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int Count() {
		// TODO Auto-generated method stub
		return 0;
	}

}
