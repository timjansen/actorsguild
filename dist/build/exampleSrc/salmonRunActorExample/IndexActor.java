package salmonRunActorExample;

import org.actorsguildframework.*;
import org.actorsguildframework.annotations.*;


public abstract class IndexActor extends Actor {
	@Prop abstract public WriteActor getWriteActor();
	
	@Message
	public AsyncResult<Void> index(int id, String payload) {
	    String newPayload = payload.replaceFirst("Downloaded ", "Indexed ");
	    //System.out.println(newPayload);
	    return getWriteActor().write(id, newPayload);
	}
}
