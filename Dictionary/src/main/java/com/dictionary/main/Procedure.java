/*
 * Created on Mar 4, 2004
 *
 * The MIT License
 * Copyright (c) 2004 Rob Rohan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software 
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 * SOFTWARE.
 */
package com.dictionary.main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Rob
 * 
 *         This class represents a Procedure that resides within the syntax dictionary.
 * 
 *         A procedure is a sub-routine that does not return a value (though it can have out parameters, i.e.
 *         &lt;cfquery&gt;).
 * 
 *         Currently one can define a procedure as belonging to CFMX, BD, W3C (i.e. HTML) and USR (user defined
 *         procedure). This will all change soon as we bring in separate syntax files for different languages.
 * 
 */
public class Procedure implements Comparable {
	/*
	 * String def's of the PARAM_* stuff are required due to the HashMap return used by getAvailParams(). Could this be
	 * done better? I.e. By having a string param trigger value to ArrayList of params in that category?
	 */
	/** The Parameter is required (<strong>and</strong> triggered) */
	static final String PARAM_REQUIRED = "__required";
	/**
	 * The Parameter is triggered (and therefore available to the user at code assist, but not marked as
	 * mandatory/required)
	 */
	static final String PARAM_TRIGGERED = "__triggeredonly";
	/**
	 * The Parameter is not triggered (and therefore not available to the user at code assist)
	 */
	static final String PARAM_NOTTRIGGERED = "_nopemyfriend";
	
	/**
	 * Returns the currently available parameters based upon the currently 'active' parameters (i.e. the ones that are
	 * currently entered by the user). Essentially a filtering takes place where each parameter decides whether it is
	 * triggered or not (or required and triggered. Oh the joys of tri-states).
	 * 
	 * @param activeParams
	 *            - the parameters currently entered by the user
	 * @return A HashMap between triggered Parameter & it's status (see Procedure.PARAM_*)
	 */
	public HashMap getAvailParams(HashMap activeParams) {
		/*
		 * Simply cycles through the parameters available to the tag, testing each parameter against the active
		 * parameters. For every Parameter the Procedure has it asks it whether it would be triggered by any of the
		 * passed in active parameters.
		 * 
		 * If so it allocates whether it is required & triggered or just triggered.
		 */
		HashMap params2Return = new HashMap();
		Iterator paramIter = this.parameters.iterator();
		while (paramIter.hasNext()) {
			Parameter currParam = (Parameter) paramIter.next();
			// System.out.print("Testing \"" + currParam.getName() + "\"");
			
			if (activeParams.containsKey(currParam.getName())) // Parameter
			// already used
			{
				// System.out.println("Param already used");
				continue;
			}
			
			int trigVal = currParam.isTriggered(activeParams);
			
			// System.out.print(" Trigger val is " + trigVal);
			if ((trigVal & Parameter.PARAM_REQUIRED) == Parameter.PARAM_REQUIRED) {
				params2Return.put(currParam, PARAM_REQUIRED);
			} else if ((trigVal & Parameter.PARAM_TRIGGERED) == Parameter.PARAM_TRIGGERED) {
				// System.out.println(" adding param");
				params2Return.put(currParam, PARAM_TRIGGERED);
			}
		}
		return params2Return;
	}
	
	/* cfml "types" */
	/** cf string type */
	public static final String STRING = "string";
	/** cf numeric type */
	public static final String NUMERIC = "numeric";
	/** cf object type */
	public static final String OBJECT = "object";
	/** cf void type (functions) */
	public static final String VOID = "void";
	/** cf struct type (functions) */
	public static final String STRUCT = "struct";
	/** cf query type (functions) */
	public static final String QUERY = "query";
	
	/** tag for MX */
	public static final byte MX = 0x1;
	/** tag for BlueDragon */
	public static final byte BD = 0x2;
	/** w3c tag (normal html etc) */
	public static final byte W3C = 0x4;
	/** user defined tag */
	public static final byte USR = 0x8;
	
	/** this procedure's name */
	protected String name = "";
	
	/**
	 * what platform this procedure is avaiable on this is kind of lame, but it uses the same values as Tag - so use
	 * those Tag.MX Tag.BD etc
	 */
	protected byte creator = MX;
	
	/** The help associated with this procedure */
	protected String help = "";
	
	/** The parameters that belong to this procedure (if any) */
	protected Set parameters = null;
	
	/**
	 * Constructs the procedure with a name.
	 * 
	 * @param name
	 *            - name of the procedure to create.
	 */
	public Procedure(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the defined users of this tag. For example 3 is both MX and BD. This is also a bit misnamed as it is also
	 * used to tell what kind of tag this is e.g. getCreatorFlags == FORM
	 * 
	 * @return who can use this tag
	 */
	public byte getCreatorFlags() {
		return creator;
	}
	
	/**
	 * Has this procedure got any parameters?
	 * 
	 * @return true/false, figure it out :)
	 */
	public boolean hasParameters() {
		if (parameters == null || parameters.size() < 1)
			return false;
		
		return true;
	}
	
	/**
	 * Adds a parameter to this procedure
	 * 
	 * @param param
	 *            the parameter to add
	 */
	public void addParameter(Parameter param) {
		if (parameters == null)
			parameters = new HashSet();
		
		parameters.add(param);
	}
	
	/**
	 * Debug function for dumping what parameters belong to this procedure.
	 * 
	 */
	public void dumpParams() {
		Object[] params = parameters.toArray();
		for (int i = 0; i < params.length; i++) {
			System.err.println("Procedure::getParameters() - Param for \'" + name + "\' is \'"
					+ ((Parameter) params[i]).name + "\'");
		}
	}
	
	/**
	 * Gets the parameters for this procedure.
	 * 
	 * @return Set of parameters belong to this procedure.
	 */
	public Set getParameters() {
		return parameters;
	}
	
	/**
	 * Gets the name of this procedure.
	 * 
	 * @return name of procedure
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Sets the help to be associated with this procedure
	 * 
	 * @param help
	 */
	public void setHelp(String help) {
		this.help = help;
	}
	
	/**
	 * Gets the help associated with this procedure.
	 * 
	 * @return
	 */
	public String getHelp() {
		return help;
	}
	
	public String toString() {
		if (parameters != null) {
			return name + ":" + parameters.size();
		}
		return name;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof Procedure) {
			// if it has the same name and number of parameters assume its
			// the same (this may need to be adjusted in the future)
			if (((Procedure) obj).getName().equals(this.name)
					&& ((Procedure) obj).getParameters().size() == parameters.size()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Enables us to sort this object
	 * 
	 * @param o
	 *            - the object to compare this procedure with
	 * @throws NullPointerException
	 *             if <code>o</code> is null.
	 */
	public int compareTo(Object o) {
		if (o == null)
			throw new NullPointerException("Null!");
		
		if (o instanceof Procedure) {
			// lowercase for the createobject hack
			return name.compareTo(((Procedure) o).getName());
		}
		
		return 0;
	}
}
