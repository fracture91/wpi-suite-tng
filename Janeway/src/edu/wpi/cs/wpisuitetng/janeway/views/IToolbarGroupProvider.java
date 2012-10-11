package edu.wpi.cs.wpisuitetng.janeway.views;

/**
 * Implementations of this interface provide button groups to display when they are relevant.
 * For example, defect views might provide buttons to save or discard changes.
 */
public interface IToolbarGroupProvider {
	
	/**
	 * @return a ToolbarGroupView containing buttons to display.
	 */
	public ToolbarGroupView getGroup();
}
