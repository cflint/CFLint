package com.cflint.plugins;

import com.cflint.BugList;

/**
 * This interface supports plugins recieving structure notifications (file start/end, component start/end, function start/end)
 * 
 * Normally it is used for initialization or summarization types of behaviours
 * @author eberlyrh
 *
 */
public interface CFLintStructureListener {

	/**
	 * Called when processing of a new file has started 
	 * @param context
	 */
	public void startFile(String fileName, BugList bugs);
	/**
	 * Called when processing of current file has ended 
	 * @param context
	 */
	public void endFile(String fileName, BugList bugs);

	/**
	 * Called when processing a new component has started 
	 * @param context
	 */
	public void startComponent(Context context, BugList bugs);
	/**
	 * Called when processing of current component has ended 
	 * @param context
	 */
	public void endComponent(Context context, BugList bugs);

	/**
	 * Called when processing of a new function has started 
	 * @param context
	 */
	public void startFunction(Context context, BugList bugs);
	/**
	 * Called when processing of current function has ended 
	 * @param context
	 */
	public void endFunction(Context context, BugList bugs);
	
}
