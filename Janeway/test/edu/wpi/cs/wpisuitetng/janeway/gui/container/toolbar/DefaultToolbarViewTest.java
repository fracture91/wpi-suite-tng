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

package edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar;

import static org.junit.Assert.*;

import java.awt.Component;
import java.util.Arrays;
import java.util.List;

import javax.swing.Box.Filler;
import javax.swing.JToolBar.Separator;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;

public class DefaultToolbarViewTest {

	private DefaultToolbarView toolbar;
	private ToolbarGroupView group1;
	private ToolbarGroupView group2;
	private ToolbarGroupView group3;
	private ToolbarGroupView newGroup;
	
	/**
	 * Make sure the contents of toolbar match what's expected.
	 * @param list List of ToolbarGroupView - use null to indicate separators
	 */
	public void assertContents(List<ToolbarGroupView> list) {
		assertEquals(list.size() + 1, toolbar.getComponentCount());
		int i = 0;
		for(Component c : list) {
			Component currentComp = toolbar.getComponentAtIndex(i);
			if(c == null) {
				assertTrue(currentComp instanceof Separator);
			} else {
				assertEquals(c, currentComp);
			}
			i++;
		}
		assertTrue(toolbar.getComponentAtIndex(i) instanceof Filler);
	}
	
	@Before
	public void setUp() throws Exception {
		toolbar = new DefaultToolbarView();
		group1 = new ToolbarGroupView(null);
		group2 = new ToolbarGroupView(null);
		group3 = new ToolbarGroupView(null);
		toolbar.addGroup(group1);
		toolbar.addGroup(group2);
		toolbar.addGroup(group3);
		newGroup = new ToolbarGroupView(null);
	}

	@Test
	public void testInitialState() {
		assertContents(Arrays.asList(group1, null, group2, null, group3));
	}
	
	@Test
	public void testInsertBeginning() {
		toolbar.insertGroupAt(newGroup, 0);
		assertContents(Arrays.asList(newGroup, null, group1, null, group2, null, group3));
	}
	
	@Test
	public void testInsertEnd() {
		toolbar.insertGroupAt(newGroup, 5);
		assertContents(Arrays.asList(group1, null, group2, null, group3, null, newGroup));
	}
	
	@Test
	public void testInsertBeforeSeparator() {
		toolbar.insertGroupAt(newGroup, 1);
		assertContents(Arrays.asList(group1, null, newGroup, null, group2, null, group3));
	}
	
	@Test
	public void testInsertAfterSeparator() {
		toolbar.insertGroupAt(newGroup, 2);
		assertContents(Arrays.asList(group1, null, newGroup, null, group2, null, group3));
	}
	
	@Test
	public void testInsertSameIndex() {
		toolbar.insertGroupAt(group1, 0);
		assertContents(Arrays.asList(group1, null, group2, null, group3));
	}
	
	@Test(expected=RuntimeException.class)
	public void testInsertDifferentIndex() {
		toolbar.insertGroupAt(group1, 1);
	}
	
	@Test
	public void testRemoveBeginning() {
		toolbar.removeGroup(group1);
		assertContents(Arrays.asList(group2, null, group3));
	}
	
	@Test
	public void testRemoveEnd() {
		toolbar.removeGroup(group3);
		assertContents(Arrays.asList(group1, null, group2));
	}
	
	@Test
	public void testRemoveMiddle() {
		toolbar.removeGroup(group2);
		assertContents(Arrays.asList(group1, null, group3));
	}
	
	@Test
	public void testRemoveNonexistent() {
		toolbar.removeGroup(newGroup);
		assertContents(Arrays.asList(group1, null, group2, null, group3));
	}

}
