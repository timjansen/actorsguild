/*
 *    Copyright 2008,2009 Tim Jansen
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
package simpleBenchmarks;

import java.util.Arrays;

import org.actorsguildframework.Agent;
import org.actorsguildframework.AsyncResult;
import org.actorsguildframework.DefaultAgent;
import org.actorsguildframework.Props;

/**
 * Main class of the AG simple benchmarks.
 */
public class Main {
	final static int DEFAULT_ITERATIONS = 100000;
	final static int DEFAULT_QUEUE_SIZE = 1000;

	final static int WAIT_ITERATIONS = 1000;
	final static int WAIT_QUEUE_SIZE = 100;
	
	final static int CHAIN_ITERATIONS = 100;
	
	public static void executeSendAndWait(TestInterface da) {
		for (int i = 0; i < DEFAULT_ITERATIONS; i++)
			da.nop().await();
	}
	
	public static long runSendAndWaitBenchmark(Class<? extends TestInterface> actorClass) {
		DefaultAgent a = new DefaultAgent();
		
		TestInterface da = a.create(actorClass);
		executeSendAndWait(da); // warm up!
		long t = System.nanoTime();
		executeSendAndWait(da);
		long d = System.nanoTime() - t;
		a.shutdown();
		return d;
	}
	
	public static void executeQueueAndWait(Agent a, TestInterface[] actors) {
		AsyncResult<Void>[] w = new AsyncResult[DEFAULT_QUEUE_SIZE];
		
		int j = 0, k = 0;
		for (int i = 0; i < DEFAULT_ITERATIONS; i++) {
			w[j++] = actors[k++ % actors.length].nop();
			if (j == w.length) {
				a.awaitAll(w);
				j = 0;
			}
		}
	}
	
	public static long runQueueAndWaitBenchmark(Class<? extends TestInterface> actorClass, int instanceNum) {
		DefaultAgent a = new DefaultAgent();

		TestInterface[] actors = new TestInterface[instanceNum];
		for (int i = 0; i < instanceNum; i++)
			actors[i] = a.create(actorClass);
		executeQueueAndWait(a, actors); // warm up!
		long t = System.nanoTime();
		executeQueueAndWait(a, actors);
		long d = System.nanoTime() - t;
		a.shutdown();
		return d;
	}
	
	public static void executeQueue1msAndWait(Agent a, TestInterface da) {
		AsyncResult<Void>[] w = new AsyncResult[WAIT_QUEUE_SIZE];
		
		int j = 0;
		for (int i = 0; i < WAIT_ITERATIONS; i++) {
			w[j++] = da.wait1ms();
			if (j == w.length) {
				a.awaitAll(w);
				j = 0;
			}
		}
	}
	
	public static long runQueue1msAndWaitBenchmark(Class<? extends TestInterface> actorClass) {
		DefaultAgent a = new DefaultAgent();

		TestInterface da = a.create(actorClass);
		executeQueue1msAndWait(a, da); // warm up!
		long t = System.nanoTime();
		executeQueue1msAndWait(a, da);
		long d = System.nanoTime() - t;
		a.shutdown();
		return d;
	}

	public static TestInterface[] createChain(Agent a, Class<? extends TestInterface> actorClass, int len) {
		TestInterface[] actors = new TestInterface[len];
		actors[len-1] = a.create(actorClass);
		for (int i = len - 2; i >= 0; i--) 
			actors[i] = a.create(actorClass, new Props("next", actors[i+1]));
		return actors;
	}
	
	public static void executeChain(Agent a, TestInterface[][] chains) {
		AsyncResult<Void>[] w = new AsyncResult[chains.length];
		
		for (int i = 0; i < CHAIN_ITERATIONS; i++) { 
			for (int j = 0; j < w.length; j++) {
				w[j] = chains[j][0].invokeNext();
			}
			a.awaitAll(w);
		}
	}
	
	public static long runChainBenchmark(Class<? extends TestInterface> actorClass, int instanceNum, int chainLength) {
		DefaultAgent a = new DefaultAgent();

		TestInterface[][] chains = new TestInterface[instanceNum][];
		for (int i = 0; i < instanceNum; i++)
			chains[i] = createChain(a, actorClass, chainLength);
		executeChain(a, chains); // warm up!
		long t = System.nanoTime();
		executeChain(a, chains);
		long d = System.nanoTime() - t;
		a.shutdown();
		return d;
	}
	
	public static void executeForwardChain(Agent a, TestInterface[][] chains) {
		AsyncResult<Void>[] w = new AsyncResult[chains.length];
		
		for (int i = 0; i < CHAIN_ITERATIONS; i++) { 
			for (int j = 0; j < w.length; j++) {
				w[j] = chains[j][0].forwardNext();
			}
			a.awaitAll(w);
		}
	}
	
	public static long runForwardChainBenchmark(Class<? extends TestInterface> actorClass, int instanceNum, int chainLength) {
		DefaultAgent a = new DefaultAgent();

		TestInterface[][] chains = new TestInterface[instanceNum][];
		for (int i = 0; i < instanceNum; i++)
			chains[i] = createChain(a, actorClass, chainLength);
		executeForwardChain(a, chains); // warm up!
		long t = System.nanoTime();
		executeForwardChain(a, chains);
		long d = System.nanoTime() - t;
		a.shutdown();
		return d;
	}
	
	/**
	 * Main method for the example
	 * @param args not used
	 * @throws Exception 
	 */
	public static void main(String args[]) throws Exception {
		{
			System.out.println("Send and Wait Benchmark. Sends an empty message, waits for completion.");
			double d = runSendAndWaitBenchmark(TestActor.class);
			System.out.println(String.format("Result: %.2f s (%d messages per second)\n", d / 1000000000.0, Math.round(DEFAULT_ITERATIONS / (d / 1000000000.0))));
		}
		
		{
			System.out.println("Send and Wait Stateless Benchmark. Sends an empty message, waits for completion.");
			double d = runSendAndWaitBenchmark(StatelessTestActor.class);
			System.out.println(String.format("Result: %.2f s (%d messages per second)\n", d / 1000000000.0, Math.round(DEFAULT_ITERATIONS / (d / 1000000000.0))));
		}

		for (int instanceNum: Arrays.asList(1, 2, 4, 8, 32, 128, 1024)) {
			{
				System.out.println("Queue and Wait Benchmark. Sends many empty messages to "+instanceNum+" actors, waits for their completion.");
				double d = runQueueAndWaitBenchmark(TestActor.class, instanceNum);
				System.out.println(String.format("Result: %.2f s (%d messages per second)\n", d / 1000000000.0, Math.round(DEFAULT_ITERATIONS / (d / 1000000000.0))));
			}
	
			{
				System.out.println("Queue and Wait Stateless Benchmark. Sends many empty messages to "+instanceNum+" actors, waits for their completion.");
				double d = runQueueAndWaitBenchmark(StatelessTestActor.class, instanceNum);
				System.out.println(String.format("Result: %.2f s (%d messages per second)\n", d / 1000000000.0, Math.round(DEFAULT_ITERATIONS / (d / 1000000000.0))));
			}	
		}

		for (int chainNum: Arrays.asList(1, 8, 32, 128)) {
			int chainLength = 512 / chainNum;
			int msgNum = CHAIN_ITERATIONS * chainNum * chainLength;
			{
				System.out.println("Chain Benchmark. Sends messages through to "+chainNum+" chains of "+chainLength+" actors each, waits for their completion.");
				double d = runChainBenchmark(TestActor.class, chainNum, chainLength);
				System.out.println(String.format("Result: %.2f s (%d messages per second)\n", d / 1000000000.0, Math.round(msgNum / (d / 1000000000.0))));
			}
	
			{
				System.out.println("Stateless Chain Benchmark. Sends messages through to "+chainNum+" chains of "+chainLength+" stateless actors each, waits for their completion.");
				double d = runChainBenchmark(StatelessTestActor.class, chainNum, chainLength);
				System.out.println(String.format("Result: %.2f s (%d messages per second)\n", d / 1000000000.0, Math.round(msgNum / (d / 1000000000.0))));
			}
			
			{
				System.out.println("Chain Forward Benchmark. Sends messages through to "+chainNum+" chains of "+chainLength+" actors each, passes results back to the beginning.");
				double d = runForwardChainBenchmark(TestActor.class, chainNum, chainLength);
				System.out.println(String.format("Result: %.2f s (%d messages per second)\n", d / 1000000000.0, Math.round(msgNum / (d / 1000000000.0))));
			}
	
			{
				System.out.println("Stateless Chain Forward Benchmark. Sends messages through to "+chainNum+" chains of "+chainLength+" stateless actors each, passes results back to the beginning.");
				double d = runForwardChainBenchmark(StatelessTestActor.class, chainNum, chainLength);
				System.out.println(String.format("Result: %.2f s (%d messages per second)\n", d / 1000000000.0, Math.round(msgNum / (d / 1000000000.0))));
			}	
		}
		
		{
			System.out.println("Queue 1ms Message and Wait Benchmark. Sends many messages that wait 1ms, waits for their completion.");
			double d = runQueue1msAndWaitBenchmark(TestActor.class);
			System.out.println(String.format("Result: %.2f s (%d messages per second)\n", d / 1000000000.0, Math.round(WAIT_ITERATIONS / (d / 1000000000.0))));
		}

		{
			System.out.println("Queue 1ms Message and Wait Stateless Benchmark. Sends many  messages that wait 1ms, waits for their completion.");
			double d = runQueue1msAndWaitBenchmark(StatelessTestActor.class);
			System.out.println(String.format("Result: %.2f s (%d messages per second)\n", d / 1000000000.0, Math.round(WAIT_ITERATIONS / (d / 1000000000.0))));
		}
	}
}
