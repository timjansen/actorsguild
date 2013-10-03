package salmonRunActorExample;

import org.actorsguildframework.*;
import org.actorsguildframework.annotations.*;


public abstract class DownloadActor extends Actor {
	@Prop public abstract  IndexActor getIndexActor();
	
	@Message
	public AsyncResult<Void> download(int id, String payload) {
	    String newPayload = payload.replaceFirst("Requested ", "Downloaded ");
	    //System.out.println(newPayload);
	    return getIndexActor().index(id, newPayload);
	}
}
