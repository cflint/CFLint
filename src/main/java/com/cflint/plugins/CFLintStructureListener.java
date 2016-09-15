package com.cflint.plugins;

import com.cflint.BugList;

/**
 * This interface supports plugins recieving structure notifications (file
 * start/end, component start/end, function start/end)
 *
 * Normally it is used for initialization or summarization types of behaviours
 * 
 * @author eberlyrh
 *
 */
public interface CFLintStructureListener {

	/**
	 * Called when processing of a new file has started
	 * 
	 * @param fileName
	 *            Name of the file that started
	 * @param bugs
	 *            List of errors reported
	 */
	public void startFile(String fileName, BugList bugs);

	/**
	 * Called when processing of current file has ended
	 * 
	 * @param fileName
	 *            Name of the file that finished
	 * @param bugs
	 *            List of errors reported
	 */
	public void endFile(String fileName, BugList bugs);

	/**
	 * Called when processing a new component has started
	 * 
	 * @param context
	 *            Current context
	 * @param bugs
	 *            List of errors reported
	 */
	public void startComponent(Context context, BugList bugs);

	/**
	 * Called when processing of current component has ended
	 * 
	 * @param context
	 *            Current context
	 * @param bugs
	 *            List of errors reported
	 */
	public void endComponent(Context context, BugList bugs);

	/**
	 * Called when processing of a new function has started
	 * 
	 * @param context
	 *            Current context
	 * @param bugs
	 *            List of errors reported
	 */
	public void startFunction(Context context, BugList bugs);

	/**
	 * Called when processing of current function has ended
	 * 
	 * @param context
	 *            Current context
	 * @param bugs
	 *            List of errors reported
	 */
	public void endFunction(Context context, BugList bugs);

}
