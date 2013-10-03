package parallelSorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.actorsguildframework.DefaultAgent;
import org.actorsguildframework.immutable.FreezableList;
import org.actorsguildframework.immutable.ImmutableList;

/**
 * Example to show the ParallelSorter class that sorts integer arrays
 * using several CPU cores.
 */
public class Main {
	/**
	 * Creates an array of random Strings
	 * @param size the size of the list
	 * @return the list
	 */
	private static FreezableList<String> createRandomList(int size) {
		Random rnd = new Random(1);
		FreezableList<String> a = new FreezableList<String>(String.class, size);
		for (int i = 0; i < size; i++)
			a.add(Integer.toString(rnd.nextInt()));
		return a;
	}
	
	/**
	 * Main method for the example
	 * @param args not used
	 */
	public static void main(String args[]) {
		DefaultAgent a = new DefaultAgent();
		ParallelSorter ps = a.create(ParallelSorter.class);
		
		int listSize = 100000;

		FreezableList<String> l = createRandomList(listSize);
		long start = System.currentTimeMillis();
		Collections.sort(l);
		System.out.println("Reference / single-threaded sort done after "+(System.currentTimeMillis() - start)+" ms.");
				
		testSort(ps, 100000, listSize);
		testSort(ps, 50000, listSize);
		testSort(ps, 25000, listSize);
		testSort(ps, 10000, listSize);
		testSort(ps, 1000, listSize);
		testSort(ps, 100, listSize);
		testSort(ps, 10, listSize);
		
		a.shutdown();
	}

	/**
	 * Starts a test run of ParallelSorter.
	 * @param ps the sorter instance
	 * @param maxElemPerMsg the number of elements per message
	 * @param listSize the size of the list to sort
	 */
	private static void testSort(ParallelSorter ps,	int maxElemPerMsg, int listSize) {
		FreezableList<String> list = createRandomList(listSize);
		long start = System.currentTimeMillis();
		System.out.print("Starting sort of "+listSize+" elements with "+maxElemPerMsg+" elements per message...");
		ImmutableList<String> r = ps.sort(list.freeze(), maxElemPerMsg).get();
		System.out.println("done after "+(System.currentTimeMillis() - start)+" ms.");
		
		// Validate result
		if (r.size() != list.size())
			throw new RuntimeException("Sorted array has wrong length, "+r.size()+" instead of "+list.size());

		ArrayList<String> reference = new ArrayList<String>(list);
		Collections.sort(reference);
		for (int i = 0; i < reference.size(); i++)
			if (!reference.get(i).equals(r.get(i)))
				throw new RuntimeException("Array not sorted, position "+i);
	}
}
