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
package org.actorsguildframework;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.actorsguildframework.internal.AsyncResultImpl;
import org.actorsguildframework.internal.Controller;
import org.actorsguildframework.internal.ControllerImplementation;
import org.actorsguildframework.internal.ThreadState;
import org.actorsguildframework.internal.codegenerator.BeanCreator;
import org.actorsguildframework.internal.util.L;

/**
 * DefaultAgent is an implementation of the Agent interface. 
 */
public class DefaultAgent implements Agent {
	private final static L log = new L(DefaultAgent.class);
	private final Controller controller;
	
	/**
	 * Represents a configuration for an Agent. This class is not thread-safe. You must create
	 * it in the same thread that creates the agent. 
	 */
	public static class Configuration {
		/**
		 * The maximum number of physical worker threads to run.
		 * Default: max(32, maxEffectiveWorker*2+2).
		 */
		private int maxPhysicalWorker;
		
		/**
		 * The maximum number of effective worker threads to run.
		 * Default: {@link Runtime#availableProcessors()} * 1.5 plus 2.
		 */
		private int maxEffectiveWorker;

		/**
		 * The ThreadFactory for the controller.
		 */
		private ThreadFactory threadFactory;
		
		/**
		 * If enabled, the Agent will log all messages that Agents send
		 * (using Java's logging system as INFO messages).
		 */
		private boolean loggingActions;
		
		/**
		 * Creates a new configuration with default values.
		 */
		public Configuration() {
			maxEffectiveWorker = (Runtime.getRuntime().availableProcessors()*3/2) + 2;
			maxPhysicalWorker = Math.max(32, (maxEffectiveWorker * 2) + 2);
		}

		/**
		 * Returns the maximum number of worker threads that will be used.
		 * Has a reasonable default value, depending on the number of processors
		 * ({@link Runtime#availableProcessors()}).
		 * @return the maximum number of threads
		 */
		public int getMaxPhysicalWorker() {
			return maxPhysicalWorker;
		}

		/**
		 * Sets the maximum number of worker threads that will be used.
		 * Has a reasonable default value, depending on the number of processors
		 * ({@link Runtime#availableProcessors()}).
		 * @param maxPhysicalWorker the maximum number of threads
		 */
		public void setMaxPhysicalWorker(int maxPhysicalWorker) {
			this.maxPhysicalWorker = maxPhysicalWorker;
		}

		/**
		 * Returns the maximum number of effective threads. Effective threads
		 * are threads that are actually working. I/O threads count partially.
		 * Has a reasonable default value, depending on the number of processors
		 * ({@link Runtime#availableProcessors()}).
		 * @return the maxEffectiveWorker
		 */
		public int getMaxEffectiveWorker() {
			return maxEffectiveWorker;
		}

		/**
		 * Sets the maximum number of effective threads. Effective threads
		 * are threads that are actually working. I/O threads count partially.
		 * Has a reasonable default value, depending on the number of processors
		 * ({@link Runtime#availableProcessors()}).
		 * @param maxEffectiveWorker the maxEffectiveWorker to set
		 */
		public void setMaxEffectiveWorker(int maxEffectiveWorker) {
			this.maxEffectiveWorker = maxEffectiveWorker;
		}

		/**
		 * Returns the ThreadFactory to use for the creation of threads.
		 * Default: {@link Executors#defaultThreadFactory()}
		 * @return the ThreadFactory
		 */
		public ThreadFactory getThreadFactory() {
			if (threadFactory == null)
				threadFactory = Executors.defaultThreadFactory();
			return threadFactory;
		}

		/**
		 * Sets the ThreadFactory to use for the creation of threads.
		 * Default: {@link Executors#defaultThreadFactory()}
		 * @param threadFactory the ThreadFactory
		 */
		public void setThreadFactory(ThreadFactory threadFactory) {
			this.threadFactory = threadFactory;
		}

		/**
		 * Checks whether the agent will log all actions (messages, new actors).
		 * @return true for logging, false otherwise
		 */
		public boolean isLoggingActions() {
			return loggingActions;
		}

		/**
		 * Sets whether the agent will log all actions (messages, new actors).
		 * @param logActions true for logging, false otherwise
		 */
		public void setLoggingActions(boolean logActions) {
			this.loggingActions = logActions;
		}
	}
	
	/**
	 * Creates a new agent with the given Configuration. 
	 * Use {@link #create(Class)} to add actors to the agent.
	 * @param configuration the configuration to use
	 */
	public DefaultAgent(Configuration configuration) {
		controller = new ControllerImplementation(this, 
				    configuration.getThreadFactory(),
					configuration.getMaxPhysicalWorker(), 
					configuration.getMaxEffectiveWorker(),
					configuration.isLoggingActions());
	}

	/**
	 * Creates a new agent with a default configuration. 
	 * Use {@link #create(Class)} to add actors to the agent.
	 */
	public DefaultAgent() {
		this(new Configuration());
	}
	
	/**
	 * Internal implementation for unit testing. 
	 * @param controller the controller implementation to use
	 */
	DefaultAgent(Controller controller) {
		this.controller = controller;
	}
	
	/* (non-Javadoc)
	 * @see org.actorsguildframework.Agent#create(java.lang.Class)
	 */
	
	public <T> T create(Class<T> actorOrBeanClass) {
		return create(actorOrBeanClass, null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.actorsguildframework.Agent#create(java.lang.Class, org.actorsguildframework.Props)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T create(Class<T> actorOrBeanClass, Props props) {
		if (actorOrBeanClass == null)
			throw new IllegalArgumentException("The given class was null.");
		
		try {
			Object bean = BeanCreator.getInstance().getFactory(actorOrBeanClass).createNewInstance(controller, props);
			if (controller.isLoggingActions())
				log.info("Created new bean %s", actorOrBeanClass.getName());
			return (T)bean;
		}
		catch (ConfigurationException e) {
			throw e;
		}
		catch (IllegalArgumentException e) {
			throw e;
		}
		catch (Exception e) {
			throw new WrappedException("Failure creating new instance of "+actorOrBeanClass, e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.actorsguildframework.Agent#awaitAll(org.actorsguildframework.AsyncResult)
	 */
	@SuppressWarnings("unchecked")
	public void awaitAll(AsyncResult...asyncResults )
	{
		if (asyncResults == null)
			throw new IllegalArgumentException("The argument array must not be null.");
		ThreadState ts = ThreadState.get();
		
		int toDoNum = 0;
		AsyncResult[] toDo = new AsyncResult[asyncResults.length];
		
		// first tryExecute the messages, if possible. Reduces number of threads needed.
		for (int i = 0; i < asyncResults.length; i++) {
			if (asyncResults[i] == null)
				throw new IllegalArgumentException("argument "+i+" was null.");
			if (asyncResults[i] instanceof AsyncResultImpl)
				if (!((AsyncResultImpl<?>) asyncResults[i]).tryExecute(ts))
					toDo[toDoNum++] = asyncResults[i];
		}
		
		// wait for the rest
		for (int i = 0; i < toDoNum; i++)
			toDo[i].await();
	}

	/* (non-Javadoc)
	 * @see org.actorsguildframework.Agent#awaitAllUntilError(org.actorsguildframework.AsyncResult)
	 */
	@SuppressWarnings("unchecked")
	public void awaitAllUntilError(AsyncResult...asyncResults ) {
		awaitAll(asyncResults);
		for (AsyncResult r: asyncResults)
			r.get(); // throws the exception
	}
	
	/* (non-Javadoc)
	 * @see org.actorsguildframework.Agent#awaitAny(org.actorsguildframework.AsyncResult)
	 */
	@SuppressWarnings("unchecked")
	public AsyncResult awaitAny(AsyncResult...asyncResults ) 
	{
		if (asyncResults == null)
			throw new IllegalArgumentException("The argument array must not be null.");
		else if (asyncResults.length == 0)
			return null;
		else if (asyncResults.length == 1) {
			asyncResults[0].await();
			return asyncResults[0];
		}
			
		for (int i = 0; i < asyncResults.length; i++) {
			if (asyncResults[i] == null)
				throw new IllegalArgumentException("argument "+i+" was null.");
			if (asyncResults[i].isReady())
				return asyncResults[i];
		}
		
		final AsyncResult[] firstResultHolder = new AsyncResult[1]; // using array as holder!
		final CountDownLatch countdown = new CountDownLatch(1);		
		AsyncResult.Notifier notifier = new AsyncResult.Notifier() {
			public void resultReady(AsyncResult result) {
				countdown.countDown();
				firstResultHolder[0] = result;
			}
		};
		
		for (int i = 0; i < asyncResults.length; i++)
			asyncResults[i].addNotifier(notifier);
		
		do {
			try {
				countdown.await();
			} catch (InterruptedException e) {
				// ignore and go on
				continue;
			}
		} while (false);
		
		for (int i = 0; i < asyncResults.length; i++)
			if (asyncResults[i] != firstResultHolder[0]) 
				asyncResults[i].removeNotifier(notifier);
		return firstResultHolder[0];
	}

	/*
	 * (non-Javadoc)
	 * @see org.actorsguildframework.Agent#shutdown()
	 */
	public void shutdown() {
		controller.shutdown();		
	}
}
