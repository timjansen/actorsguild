/*
 *    Copyright 2008 Tim Jansen
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.actorsguildframework.internal.util;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logging helper class. Provides easy access to a per-class logger, as well as
 * a globally increasing number to find out the right order of log messages.
 */
public class L {
	private final static AtomicLong logNumber = new AtomicLong();
	private final Logger logger;
	private final Class<?> srcClass;
	
	/**
	 * Creates a Logger for the given class.
	 * @param c the logger
	 */
	public L(Class<?> c)
	{
		srcClass = c;
		logger = Logger.getLogger(c.getClass().getName());
	}
	
	/**
	 * Returns the logger of this instance.
	 * @return the instance's logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Returns the next log number.
	 * @return the next log number
	 */
	public static long getTxNumber() 
	{
		return logNumber.addAndGet(1);
	}
	
	/**
	 * Returns a string with the given format and prepends the next {@link #getTxNumber() log number}.
	 * @param fmt the string to format, possibly containing {@link String#format(String, Object...)}
	 *    formatting
	 * @param args the arguments for the format
	 * @return the f
	 */
	public static String formatLogMessage(String fmt, Object... args) 
	{
		String f;
		if (args.length == 0)
			f = fmt;
		else
			f = String.format(fmt, args);
		return getTxNumber() + ": " + f;
	}

	
	/**
	 * Logs the given message, with log number, as info message.
	 * @param fmt the text/format to log in ({@link String#format(String, Object...)})
	 * @param args the arguments for the logger
	 */
	public void info(String fmt, Object... args) 
	{
		logger.logp(Level.INFO, srcClass.getName(), "", formatLogMessage(fmt, args));
	}
	
	/**
	 * Logs the given message, with log number, as an error (SEVERE log level).
	 * @param fmt the text/format to log in ({@link String#format(String, Object...)})
	 * @param args the arguments for the logger
	 */
	public void error(String fmt, Object... args) 
	{
		logger.logp(Level.SEVERE, srcClass.getName(), "", formatLogMessage(fmt, args));

	}
	
	/**
	 * Logs the given message, with log number, as debug message (FINE log level).
	 * @param fmt the text/format to log in ({@link String#format(String, Object...)})
	 * @param args the arguments for the logger
	 */
	public void debug(String fmt, Object... args) 
	{
		logger.logp(Level.FINE, srcClass.getName(), "", formatLogMessage(fmt, args));
	}
	
	/**
	 * Logs the given exception.
	 * @param t the exception to log
	 */
	public void exception(Throwable t) 
	{
		logger.throwing(srcClass.getName(), "", t);
	}
}
