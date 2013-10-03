package salmonRunActorExample;

import org.actorsguildframework.*;
import org.actorsguildframework.annotations.*;


public class WriteActor extends Actor {
	@Message
	public AsyncResult<Void> write(int id, String payload) {
	    //System.out.println(payload.replaceFirst("Indexed ", "Wrote "));
	    return noResult();
	}
}
