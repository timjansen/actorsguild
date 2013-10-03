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
package org.actorsguildframework.internal;

import java.util.ArrayList;

import org.actorsguildframework.Actor;
import org.actorsguildframework.ActorException;
import org.actorsguildframework.AsyncResult;
import org.actorsguildframework.ImmediateResult;
import org.actorsguildframework.WrappedException;
import org.actorsguildframework.immutable.ImmutableHelper;
import org.actorsguildframework.immutable.SerializableFreezer;
import org.actorsguildframework.internal.util.L;

/**
 * The 'real' implementation of AsyncResult that waits for the asynchronously running message to
 * finish. 
 * 
 * Synchronization: lock instance before accessing it. Wait on the instance if you want to
 * be notified of the result.
 * 
 * @param <T> the type of the result
 */
public final class AsyncResultImpl<T> implements AsyncResult<T> {
	private static L log = new L(AsyncResultImpl.class);
	
	/**
	 * Describes the state of the result.
	 */
	public enum State {
		/**
		 * Not finished yet.
		 */
		NOT_DONE,
		/**
		 * Result is ready.
		 */
		DONE_IMMUTABLE,
		/**
		 * Result is ready, wrapped into a SerializableFreezer.
		 */
		DONE_FROZEN,
		/**
		 * Result is an exception.
		 */
		EXCEPTION,
	};

	
	/**
	 * A MessageInvocation that can be called to speed up the message processing
	 * by using the current thread to execute the message, and that will be notified when
	 * the thread is waiting.
	 */
	private final MessageInvocation<T> mInvocation;

	/**
	 * Specifies the state of the result. 
 	 * Synchronize on this instance before accessing it.
	 */
	private State mState = State.NOT_DONE;
	
	/**
	 * If not null, the list of notifiers.
	 * Synchronize this instance before access!
	 */
	private ArrayList<Notifier<T>> notifiers;
	
	/**
	 * The result value: result or exception or AsyncResult, depending on State.
	 * Synchronize on this instance before accessing it.
	 */
	private Object resultValue; 
	
	/**
	 * Creates a new instance.
	 * @param invocation the MessageInvocation to use
	 */
	public AsyncResultImpl(MessageInvocation<T> invocation) {
		this.mInvocation = invocation;
	}
	
	/* (non-Javadoc)
	 * @see org.actorsguildframework.AsyncResult#addNotifier(org.actorsguildframework.AsyncResult.Notifier)
	 */
	public void addNotifier(AsyncResult.Notifier<T> notifier) {
		boolean callDirectly = false;
		synchronized(this) {
			if (mState == State.NOT_DONE) {
				if (notifiers == null)
					notifiers = new ArrayList<Notifier<T>>(2);
				notifiers.add(notifier);
			}
			else
				callDirectly = true;
		}
		
		// call unsynchronized
		if (callDirectly)
			notifier.resultReady(this);
	}

	/**
	 * Sleeps until a result is available. 
	 * @param ts the current ThreadState
	 * @param throwExceptionif true, the method throws an exception if that is the method result. 
	 *    Otherwise it returns null instead.
	 * @return the result, or null for error
	 * @throws ActorException if the {@link #wait()} is interrupted
	 * @throws WrappedException if the message threw an exception and throwException was set
	 */
	@SuppressWarnings("unchecked")
	private T sleepUntilResultAvailable(ThreadState ts, boolean throwException)
			throws ActorException {
		synchronized (this) {
			try {
				WorkerState oldState = ts.setState(WorkerState.WaitingInternal);
				while (mState == State.NOT_DONE)
					this.wait();
				ts.setState(oldState);
			}
			catch (InterruptedException e) {
				throw new ActorException("Got InterruptedException while waiting", e);
			}
			
			if (mState == State.DONE_IMMUTABLE)
				return (T) resultValue;
			else if (mState == State.DONE_FROZEN)
				return ((SerializableFreezer<T>) resultValue).get();
			else if (throwException)
				throw new WrappedException("Got exception", (Throwable) resultValue);
			else
				return null;
		}
	}


	/**
	 * Sleeps until a result is available. Tries to execute the message it is waiting for
	 * to accelerate the result.
	 * @param throwException if true, the method throws an exception if that is the method result. 
	 *    Otherwise it returns null instead.
	 * @return the result, or null for error
	 * @throws WrappedException if the message threw an exception and throwException was set
	 */
	private T tryExecuteOrSleepUntilResultIsAvailable(boolean throwException) {
		ThreadState ts = ThreadState.get();
		
		// try to execute the message we wait for now (better than waiting for another thread to do it)
		mInvocation.getTargetActor().tryExecuteNow(mInvocation, ts);
		return sleepUntilResultAvailable(ts, throwException);
	}
	
	/* (non-Javadoc)
	 * @see org.actorsguildframework.AsyncResult#get()
	 */
	@SuppressWarnings("unchecked")
	public T get() {
		synchronized(this) {
			if (mState == State.DONE_IMMUTABLE)
				return (T) resultValue;
			else if (mState == State.DONE_FROZEN)
				return ((SerializableFreezer<T>) resultValue).get();
			else if (mState == State.EXCEPTION)
				throw new WrappedException("Got exception", (Exception) resultValue);
		}

		return tryExecuteOrSleepUntilResultIsAvailable(true);
	}


	/* (non-Javadoc)
	 * @see org.actorsguildframework.AsyncResult#waitForResult()
	 */
	public void await() {
		synchronized(this) {
			if (mState != State.NOT_DONE)
				return;
		}
		tryExecuteOrSleepUntilResultIsAvailable(false);
	}


	/* (non-Javadoc)
	 * @see org.actorsguildframework.AsyncResult#isReady()
	 */
	public boolean isReady() {
		synchronized (this) {
			return (mState != State.NOT_DONE);
		}
	}

	/* (non-Javadoc)
	 * @see org.actorsguildframework.AsyncResult#removeNotifier(org.actorsguildframework.AsyncResult.Notifier)
	 */
	public void removeNotifier(Notifier<T> notifier) {
		synchronized(this) {
			if (notifiers != null) {
				int l = notifiers.size();
				int i = 0;
				while (i < l)
					if (notifiers.get(i) == notifier) {
						notifiers.remove(i);
						l--;
					}
					else
						i++;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.actorsguildframework.AsyncResult#getException()
	 */
	public Throwable getException() {
		synchronized (this) {
			if (mState != State.EXCEPTION)
				return null;
			return (Throwable) resultValue;
		}
	}

	/**
	 * Call this when the result is ready.
	 * @param result the result
	 */
	public void resultReady(AsyncResult<T> result) {
		if (result instanceof ImmediateResult)
			resultValue(result.get());
		else
			resultIsAsync(result);
	}
	
	/**
	 * Call this when the result is ready and the result is a value.
	 * @param result the result
	 */
	public void resultValue(T result) {
		if ((result == null) || ImmutableHelper.isImmutableType(result.getClass()) || (result instanceof Actor))
			resultReadyInternal(State.DONE_IMMUTABLE, result);
		else
			resultReadyInternal(State.DONE_FROZEN, SerializableFreezer.freeze(result));
	}

	/**
	 * Call this when there was an exception. 
	 * @param exception the exception
	 */
	public void resultException(Throwable exception) {
		resultReadyInternal(State.EXCEPTION, exception);
	}
	
	/**
	 * Call this when the result is ready.
	 * @param state the type of result (must not be NOT_DONE!!)
	 * @param result the result or exception (may be null if the result is null)
	 */
	@SuppressWarnings("unchecked")
	private void resultReadyInternal(State state, Object result) {
		assert state != State.NOT_DONE;
		
		Notifier[] notifiersCopy = null;
		synchronized (this) {
			mState = state;
			resultValue = result;
			if (notifiers != null) {
				notifiersCopy = notifiers.toArray(new Notifier[notifiers.size()]);
				notifiers = null;
			}
			this.notifyAll();
		}
		// invoke notifiers without synchronization
		if (notifiersCopy != null)
			for (Notifier n: notifiersCopy) {
				try {
					n.resultReady(this); 
				}
				catch (Throwable t) {
					log.error("Got exception from notifier: %s", t);
					log.exception(t);
				}
			}
	}

	/**
	 * Extracts the result from the given AsyncResult. You must synchronize the instance
	 * before calling this.
	 * @param ar the async result to extract from
	 */
	private void extractAsyncResultUnsynchronized(AsyncResult<T> ar) {
		Throwable e = ar.getException();
		if (e != null) {
			resultException(e);
			return;
		}
		
		try {
			resultValue(ar.get());
		}
		catch (WrappedException t) {
			// just in case.. shouldn't happen because the method in only called after ar.isReady().
			resultException(t.getCause());
		}
		catch (Throwable t) {
			resultException(t);
		}
	}
	
	/**
	 * Call this when the message returned with a generic AsyncResult.
	 * @param aresult the synchronous result
	 */
	private void resultIsAsync(AsyncResult<T> aresult) {
		synchronized (this) {
			if (aresult.isReady()) 
				extractAsyncResultUnsynchronized(aresult);
			else 
				aresult.addNotifier(new Notifier<T>() {
					public void resultReady(AsyncResult<T> result) {
						extractAsyncResultUnsynchronized(result);
					}
				});
		}
	}
	
	/**
	 * Checks whether there is already a result. If not, it tries to execute
	 * the message in the current thread.
	 * @param ts the current ThreadState
	 * @return true if the message is finished now. false if it is not known whether it is finished or not
	 *  (may be still running)
	 */
	public boolean tryExecute(ThreadState ts) {
		synchronized(this) {
			if (mState != State.NOT_DONE)
				return true;
		}
		return mInvocation.getTargetActor().tryExecuteNow(mInvocation, ts);
	}
}
