package parallelSorting;

import java.util.Collections;

import org.actorsguildframework.Actor;
import org.actorsguildframework.AsyncResult;
import org.actorsguildframework.annotations.ConcurrencyModel;
import org.actorsguildframework.annotations.Message;
import org.actorsguildframework.annotations.Model;
import org.actorsguildframework.immutable.FreezableList;
import org.actorsguildframework.immutable.ImmutableList;

/**
 * ParallelSorter can sort String list, taking advantage of several CPU cores. 
 * 
 * Please note that this example will not be very fast, because the integer
 * arrays will be copied for each message. Java arrays are not particularly multi-threading
 * friendly... on a quad-core machine it is still faster than Java's built-in
 * sort though.
 */
@Model(ConcurrencyModel.Stateless)
public class ParallelSorter extends Actor {
	
	@Message
	public AsyncResult<ImmutableList<String>> sort(ImmutableList<String> list, int maxElemPerMessage) {
		// if list small enough, sort list using built-in sort
		if (list.size() <= maxElemPerMessage) {
			FreezableList<String> l = new FreezableList<String>(String.class, list);
			Collections.sort(l);
			return result(l.freeze());
		}

		// Split task into two sub-tasks (recursively)
		int m = list.size()/2;
		AsyncResult<ImmutableList<String>> partA = sort(list.subList(0, m), maxElemPerMessage);
		AsyncResult<ImmutableList<String>> partB = sort(list.subList(m, list.size()), maxElemPerMessage);
		
		// run them in parallel
		getAgent().awaitAll(partA, partB);

		// merge both results
		ImmutableList<String> a = partA.get();
		ImmutableList<String> b = partB.get();
		FreezableList<String> r = new FreezableList<String>(String.class, a.size() + b.size());
		mergeInternal(a, b, r);
		return result(r.freeze());
	}

	/**
	 * Merges both sorted arrays into one sorted list.
	 * @param a the first sorted list
	 * @param b the second sorted list
	 * @param r the result will be written here. 
	 */
	private void mergeInternal(ImmutableList<String> a, ImmutableList<String> b, FreezableList<String> r) {
		int ac = 0, bc = 0;
		if ((a.size() > 0) && (b.size() > 0)) {
			String x = a.get(ac++);
			String y = b.get(bc++);
			while (true) {
				if (x.compareTo(y) < 0) {
					r.add(x);
					if (ac >= a.size()) {
						r.add(y);
						break;
					}
					x = a.get(ac++);
				}
				else {
					r.add(y);
					if (bc >= b.size()) {
						r.add(x);
						break;
					}
					y = b.get(bc++);
				}
			}
		}
		
		if (ac < a.size())
			r.addAll(a.subList(ac, a.size()));
		else
			r.addAll(b.subList(bc, b.size()));
	}
	
}
