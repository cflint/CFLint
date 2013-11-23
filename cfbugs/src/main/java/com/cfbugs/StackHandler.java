package com.cfbugs;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

public class StackHandler {

	Deque<Set<String>> varStack = new ArrayDeque<Set<String>>();

	public StackHandler() {
		super();
		excludes.add("VARIABLES");
		excludes.add("ARGUMENTS");
		excludes.add("SUPER");
		excludes.add("SESSION");
		excludes.add("APPLICATION");
		varStack.push(new HashSet<String>());
	}

	//Set<String> variables = new HashSet<String>();
	Set<String> excludes = new HashSet<String>();

	public void addVariable(final String name) {
		varStack.peek().add(name.toUpperCase());
	}
	
	public void push(){
		varStack.push(new HashSet<String>());
	}

	public void pop(){
		varStack.pop();
	}
	public boolean checkVariable(final String name) {
		if(excludes.contains(name.toUpperCase())){
			return true;
		}
		Iterator<Set<String>> iter = varStack.iterator();
		while(iter.hasNext()){
			Set<String> vars = iter.next();
			if(vars.contains(name.toUpperCase())){
				return true;
			}
		}
		//reported once gets pushed to avoid duplicates
		addVariable(name);
		return false;
	}
}
