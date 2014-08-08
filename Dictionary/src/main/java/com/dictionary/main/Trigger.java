package com.dictionary.main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Trigger - represents the attribute conditions required to trigger (allow) an attribute to be available. For example,
 * on the tag <cffile> the parameter 'source' is available when the 'append' attribute is set to either: copy,move or
 * rename.
 * 
 * When that occurs it is not only available, but required as well.
 * 
 * @author Oliver Tupman
 * 
 */
public class Trigger {
	protected HashMap triggerParams = new HashMap();
	protected boolean isRequired = false;
	protected int index = -1;
	
	/**
	 * Creates a simple, one attribute set trigger. Just used to reduce the amount of coding required :) This one sets
	 * the parameter trigger required value to false.
	 * 
	 * @param paramName
	 *            Name of the parameter to create the trigger from
	 * @param paramVal
	 *            The value of said trigger
	 * @return The newly created trigger
	 */
	public static Trigger CreateSimpleTrigger(String paramName, String paramVal) {
		return CreateSimpleTrigger(paramName, paramVal, false);
	}
	
	/**
	 * Creates a simple, one attribute set trigger. Just used to reduce the amount of coding required :)
	 * 
	 * @param paramName
	 *            Name of the parameter to create the trigger from
	 * @param paramVal
	 *            The value of said triggered
	 * @param required
	 *            Whether this trigger triggered means that the parameter must be used
	 * @return The newly created trigger
	 */
	public static Trigger CreateSimpleTrigger(String paramName, String paramVal, boolean required) {
		HashMap params = new HashMap();
		params.put(paramName, paramVal);
		return new Trigger(params, required);
	}
	
	/**
	 * Creates a simple, one attribute set trigger with a required value and a positional index. Just used to reduce the
	 * amount of coding required :)
	 * 
	 * @param paramName
	 *            Name of the parameter to create the trigger from
	 * @param paramVal
	 *            The value of said triggered
	 * @param required
	 *            Whether this trigger triggered means that the parameter must be used
	 * @param index
	 *            The positional index of this parameter in the tag or function call
	 * @return The newly created trigger
	 */
	public static Trigger CreateSimpleTrigger(String paramName, String paramVal, boolean required, int index) {
		HashMap params = new HashMap();
		params.put(paramName, paramVal);
		return new Trigger(params, required, index);
	}
	
	/**
	 * Constructor with only the parameters required for the trigger. By default this trigger will be optional
	 * 
	 * @param newParams
	 */
	Trigger(HashMap newParams) {
		triggerParams = newParams;
	}
	
	/**
	 * Constructor with the parameters required and whether the trigger is required.
	 * 
	 * @param newParams
	 * @param required
	 */
	Trigger(HashMap newParams, boolean required) {
		isRequired = required;
		triggerParams = newParams;
	}
	
	/**
	 * Constructor with the parameters required, whether the trigger is required and the positional index of the
	 * parameter for this trigger.
	 * 
	 * @param newParams
	 * @param required
	 * @param index
	 */
	Trigger(HashMap newParams, boolean required, int index) {
		isRequired = required;
		this.index = index;
		triggerParams = newParams;
	}
	
	/**
	 * Compares two maps. Looks like AbstractMap::equals() doesn't work correctly!
	 * 
	 * @param m1
	 *            First map to check
	 * @param m2
	 *            Second map for the comparison
	 * @return true/false if they're equal or not
	 */
	private boolean mapsEqual(Map m1, Map m2) {
		// boolean retVal = false;
		
		if (!m1.keySet().equals(m2.keySet())) {
			return false;
		}
		
		Iterator m1Iter = m1.keySet().iterator();
		while (m1Iter.hasNext()) {
			Object key = m1Iter.next();
			if (m1.get(key) == null)
				return false;
			if (m2.get(key) == null)
				return false;
			
			if (!m1.get(key).equals(m2.get(key))) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Returns whether the trigger has, um, triggered or not.
	 * 
	 * @param params
	 *            The attributes/parameters currently set.
	 * @return PARAM_TRIGGERD | PARAM_REQUIRED (triggered & required) PARAM_TRIGGERED (simply triggered, therefore
	 *         optional) PARAM_NOTTRIGGERED (not triggered, technically optional)
	 */
	public int WillTrigger(HashMap params) {
		// /System.out.print("  Trigger:WillTrigger() - ");
		
		if (triggerParams.size() == 0) {
			// System.out.println(" no trigger params, triggered and required");
			return Parameter.PARAM_TRIGGERED | (isRequired ? Parameter.PARAM_REQUIRED : 0);
		}
		
		if (mapsEqual(this.triggerParams, params)) {
			if (isRequired) {
				// System.out.println(" required and triggered");
				return Parameter.PARAM_TRIGGERED | Parameter.PARAM_REQUIRED;
			} else {
				// System.out.println(" triggered, not required");
				return Parameter.PARAM_TRIGGERED;
			}
		}
		
		Set paramSet = params.keySet();
		Iterator i = paramSet.iterator();
		while (i.hasNext()) {
			Object key = i.next();
			String value = params.get(key).toString();
			HashMap thisParam = new HashMap();
			thisParam.put(key.toString(), value);
			// System.out.println("Param");
			// System.out.println(key.toString() + ":" + value);
			// System.out.println("Trigger");
			// System.out.println(this.toString());
			if (this.triggerParams.toString().equalsIgnoreCase(thisParam.toString())) {
				if (this.isRequired) {
					return Parameter.PARAM_TRIGGERED | Parameter.PARAM_REQUIRED;
				} else {
					return Parameter.PARAM_TRIGGERED;
				}
			}
		}
		
		return Parameter.PARAM_NOTTRIGGERED;
	}
	
	public String toString() {
		
		return this.triggerParams.toString();
		
	}
	
	/**
	 * Returns the parmeter index for this trigger.
	 * 
	 * @return
	 */
	
	public int paramIndex() {
		return this.index;
	}
}