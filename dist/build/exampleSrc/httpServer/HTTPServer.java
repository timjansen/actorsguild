package httpServer;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.actorsguildframework.Actor;
import org.actorsguildframework.AsyncResult;
import org.actorsguildframework.annotations.ConcurrencyModel;
import org.actorsguildframework.annotations.Message;
import org.actorsguildframework.annotations.Model;
import org.actorsguildframework.annotations.Prop;
import org.actorsguildframework.annotations.Shared;
import org.actorsguildframework.annotations.ThreadUsage;
import org.actorsguildframework.annotations.Usage;
import org.actorsguildframework.immutable.ImmutableMap;

/**
 * Example Actor that implements a minimal and non-RFC compliant HTTP/1.0 server.
 * It should be used to demonstrate how to implement a server as agent (and not how to
 * implement HTTP).
 * 
 * This server listens on the given port, and returns a preconfigured set of pages
 * that were specified in the initializer.
 * 
 * In this example, the actor was implemented as {@link ConcurrencyModel#Stateless}. An 
 * alternative, especially for a more complex server, would be to create a separate actor 
 * class for each of them, and have several instances of these actors. 
 * @author tim
 */
@Model(ConcurrencyModel.Stateless)
public abstract class HTTPServer extends Actor {
	final static int MAX_CONNECTIONS = 20;
	final static int BACKLOG = 10;
	
	/**
	 * Returns a map PATH => HTMLCONTENT. Path is the path of the page, HTMLCONTENT
	 *   is the page body as string. It will be served as text/html.
	 * @return the HTML page map.
	 */
	@Prop
	public abstract ImmutableMap<String, String> getHtmlPages(); 

	/**
	 * Returns the html content for the given path, as initialized with.
	 * @param path the path to retrieve content for 
	 * @return the content, or null for unknown path
	 */
	@Message
	public AsyncResult<String> getPageContentFor(String path) {
		return result(getHtmlPages().get(path));
	}
	
	/**
	 * Removes all ready results from the given list.
	 * @param results the result list to clean up
	 */
	private static void removeFinishedConnections(List<AsyncResult<Void>> results) {
		Iterator<AsyncResult<Void>> it = results.iterator();
		while (it.hasNext()) {
			AsyncResult<Void> ar = it.next();
			if (ar.isReady()) {
				it.remove();
				if (ar.getException() != null)
					System.err.println("Got exception in listener: "+ar.getException());
			}
		}
	}

	/**
	 * Listens on the given TCP port. Runs forever.
	 * @param port the TCP port to listen on
	 * @return will never return
	 * @throws Exception will never throw
	 */
	@Message
	@Usage(ThreadUsage.Waiting)
	public AsyncResult<Void> listen(int port) throws Exception {
		List<AsyncResult<Void>> activeConnections = new ArrayList<AsyncResult<Void>>();
		ServerSocket socket = new ServerSocket(port, BACKLOG);
		while (true) {
			Socket s = socket.accept(); // << blocking!
			activeConnections.add(processConnection(s));
			
			// manage connections
			removeFinishedConnections(activeConnections);
			if (activeConnections.size() >= MAX_CONNECTIONS) {
				getAgent().awaitAny(activeConnections.toArray(new AsyncResult[MAX_CONNECTIONS]));
				removeFinishedConnections(activeConnections);
			}
		}
	}
	
	/**
	 * Processes one HTTP connection that has been accepted.
	 * @param socket the socket to use
	 * @return nothing
	 * @throws Exception on error
	 */
	@Message
	@Usage(ThreadUsage.IO)
	public AsyncResult<Void> processConnection(@Shared Socket socket) throws Exception {
		try {
			String path = HTTPHelper.readHeader(socket.getInputStream());
			OutputStream os = socket.getOutputStream();
			if (path == null) 
				HTTPHelper.writeError(400, "Can not parse request", os);
			else {
				String content = getPageContentFor(path).get();
				if (content == null)
					HTTPHelper.writeError(404, "Can not find path "+path, os);
				else 
					HTTPHelper.writeContent("text/html", content, os);
			}
		}
		finally {
			socket.close();
		}
		return noResult();
	}
}
