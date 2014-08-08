package com.parser.main.cfml;

/*
Copyright (c) 2007 Mark Mandel, Mark Drew

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.	
*/

import java.util.*;

/**
 * Observerl for CFML Parser / Lexer Errors
 * @author mark
 *
 */
public class ErrorObservable
{
	List<IErrorObserver> observers; 
	
	public ErrorObservable()
	{
		setObservers(Collections.synchronizedList(new LinkedList<IErrorObserver>()));
	}
	
	public void addObserver(IErrorObserver observer)
	{
		getObservers().add(observer);
	}
	
	public void removeObserver(IErrorObserver observer)
	{
		getObservers().remove(observer);
	}
	
	public void notifyObservers(ErrorEvent event)
	{
		for(IErrorObserver observer : getObservers())
		{
			observer.actionCFMLParserError(event);			
		}
	}

	private List<IErrorObserver> getObservers()
	{
		return observers;
	}

	private void setObservers(List<IErrorObserver> observers)
	{
		this.observers = observers;
	}
}
