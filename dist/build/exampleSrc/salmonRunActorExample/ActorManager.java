package salmonRunActorExample;

import org.actorsguildframework.*;

public class ActorManager {
	public static void main(String[] args) {
		DefaultAgent ag = new DefaultAgent();
		
		WriteActor writeActor = ag.create(WriteActor.class);
		IndexActor indexActor = ag.create(IndexActor.class, new Props("writeActor", writeActor));
		DownloadActor downloadActor = ag.create(DownloadActor.class, new Props("indexActor", indexActor));
		
		long start = System.currentTimeMillis();
		
		int numberOfRequests = 100000;
		int tasksDone = 0;
		while (tasksDone < numberOfRequests) {
			int s = Math.min(numberOfRequests - tasksDone, 10000);
			AsyncResult[] results = new AsyncResult[s];
			for (int i = 0; i < s; i++)
				results[i] = downloadActor.download(tasksDone+i, "Requested " + (tasksDone+i));
			ag.awaitAllUntilError(results);
			tasksDone += s;
		}
		
		System.out.println("elapsed = " + (System.currentTimeMillis() - start));
		ag.shutdown();
	}
}
